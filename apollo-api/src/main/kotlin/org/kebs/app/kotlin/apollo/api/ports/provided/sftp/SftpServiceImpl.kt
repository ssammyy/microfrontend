package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import com.google.common.io.Files
import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.SftpTransmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.ISftpTransmissionEntityRepository
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.sql.Timestamp
import java.time.Instant
import java.util.*


@Service("sftpService")
class SftpServiceImpl(
    private val applicationMapProperties: ApplicationMapProperties,
    private val jasyptStringEncryptor: StringEncryptor,
    private val sftpLogRepo: ISftpTransmissionEntityRepository
) : ISftpService {

    //    val SESSION_CONFIG_STRICT_HOST_KEY_CHECKING: String = "StrictHostKeyChecking"
    val username = applicationMapProperties.mapSftpUsername
    val host = applicationMapProperties.mapSftpHost
    val password = applicationMapProperties.mapSftpPassword

    fun createSftp(): ChannelSftp {

        val username = applicationMapProperties.mapSftpUsername
        val decryptedPassword = jasyptStringEncryptor.decrypt(applicationMapProperties.mapSftpPassword)

        val jsch = JSch()
        KotlinLogging.logger { }
            .info(":::: Trying to connect sftp[$username @ $host], using password: $decryptedPassword ::::")

        val session: Session = createSession(jsch, host, username, port = applicationMapProperties.mapSftpPort)
        try {
            session.setPassword(decryptedPassword)
            session.connect()
        } catch (e: IOException) {
            KotlinLogging.logger { }.error("An error occurred while acquiring SFTP connection", e)
            throw RuntimeException("An error occurred while acquiring SFTP connection")
        }
        KotlinLogging.logger { }.info(":::: SFTP session connected successfully ::::")

        val channel: Channel = session.openChannel(applicationMapProperties.mapSftpClientProtocol)
        channel.connect(applicationMapProperties.mapSftpChannelConnectedTimeout.toInt())
        KotlinLogging.logger { }.info(":::: SFTP channel created successfully ::::")

        return channel as ChannelSftp
    }

    fun createSession(jSch: JSch, host: String, username: String, port: String): Session {
        var session: Session?
        try {
            session = jSch.getSession(username, host, port.toInt())
        } catch (e: IOException) {
            KotlinLogging.logger { }.error("An error occurred while creating SFTP session", e)
            throw RuntimeException("An error occurred while creating SFTP session")
        }
        session?.setConfig("StrictHostKeyChecking", "no")
        return session
    }

    fun disconnect(sftp: ChannelSftp) {
        try {
            if (sftp.isConnected) {
                val session: Session = sftp.getSession()
                sftp.exit()
                session.disconnect()
            } else if (sftp.isClosed) {
                KotlinLogging.logger { }.info("SFTP connection is already closed")
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while closing SFTP session", e)
        }
    }

    override fun uploadFile(file: File): Boolean {
        val sftp: ChannelSftp = this.createSftp()
        val log = SftpTransmissionEntity()
        try {
            log.filename = file.name
            log.transactionDate = Date()
            log.transactionStartDate = Timestamp.from(Instant.now())
            log.transactionStatus = 0
            log.callingMethod = Thread.currentThread().name
            log.flowDirection = "OUT"

            sftp.cd(applicationMapProperties.mapSftpUploadRoot)


            KotlinLogging.logger { }.info(":::: Uploading file to: ${applicationMapProperties.mapSftpUploadRoot} ::::")

            val fileName = file.name
            sftp.put(file.inputStream(), fileName)

            log.transactionStatus = 30
            log.responseMessage = "Successfully sent"
            log.responseStatus = "00"
            log.transactionCompletedDate = Timestamp.from(Instant.now())

            return true
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while uploading sftp file: ${file.name}", e)

            log.transactionStatus = 20
            log.responseMessage = e.message
            log.responseStatus = "99"
            log.transactionCompletedDate = Timestamp.from(Instant.now())
            throw RuntimeException("An error occurred while uploading sftp file")
        } finally {
            KotlinLogging.logger { }.info(":::: Disconnect sftp after upload ::::")
            this.disconnect(sftp)
            sftpLogRepo.save(log)
        }
    }

    override fun downloadFilesByDocType(docType: String): List<File> {
        val sftp: ChannelSftp = this.createSftp()
        val filesList = mutableListOf<File>()

        try {

            sftp.cd(applicationMapProperties.mapSftpDownloadRoot)

            val allFiles = sftp.ls(applicationMapProperties.mapSftpDownloadRoot)
            for (file in allFiles) {
                val log = SftpTransmissionEntity()
                log.transactionDate = Date()
                log.transactionStartDate = Timestamp.from(Instant.now())
                log.callingMethod = Thread.currentThread().name
                try {
                    val entry: ChannelSftp.LsEntry = file as ChannelSftp.LsEntry
                    log.filename = (file as File).name
                    log.transactionStatus = 0
                    log.flowDirection = "IN"
                    if (validateKeswsFileByDocType(entry.filename, docType)) {
                        filesList.add(convertInputstreamToFile(sftp.get(entry.filename), entry.filename))
                    }

                    log.transactionStatus = 30
                    log.responseMessage = "Successfully downloaded"
                    log.responseStatus = "00"
                    log.transactionCompletedDate = Timestamp.from(Instant.now())
                }catch (e : Exception) {
                    KotlinLogging.logger { }.error("An error occurred while downloading sftp files in the inner loop: ", e)
                    log.transactionStatus = 20
                    log.responseMessage = e.message
                    log.responseStatus = "99"
                    log.transactionCompletedDate = Timestamp.from(Instant.now())
                }
                sftpLogRepo.save(log)
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while downloading sftp files", e)
//            throw RuntimeException("An error occurred while downloading sftp files")
        } finally {
            KotlinLogging.logger { }.info(":::: Disconnect sftp after downloading ::::")
            this.disconnect(sftp)
        }
        return filesList
    }

    fun moveFileToProcessedFolder(file: File, destinationFolder: String): Boolean {
        val sftp: ChannelSftp = this.createSftp()
        try {
            val fileName = file.name
            KotlinLogging.logger { }
                .info(":::: Moving file: $fileName from directory: ${applicationMapProperties.mapSftpDownloadRoot}" + " to directory: ${destinationFolder}   ::::")
            sftp.rename(
                applicationMapProperties.mapSftpDownloadRoot.plus("/").plus(fileName),
                destinationFolder.plus("/").plus(fileName)
            )
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while moving sftp files", e)
        } finally {
            KotlinLogging.logger { }.info(":::: Disconnect sftp after moving ::::")
            this.disconnect(sftp)
        }
        return false
    }

    fun validateKeswsFileByDocType(fileName: String, docType: String): Boolean {
        val splitFileName = fileName.split("-").toTypedArray()
        val fileDocType = splitFileName.first()
        return fileDocType.equals(docType)
    }

    fun convertInputstreamToFile(inputStream: InputStream, fileName: String): File {
        val targetFile = File(Files.createTempDir(), fileName)
//        KotlinLogging.logger { }.info(":::: targetFile: ${targetFile.name} ::::")
        targetFile.deleteOnExit()
        try {
            FileUtils.copyInputStreamToFile(inputStream, targetFile)
            return targetFile
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while creating temp file for file: $fileName", e)
            throw RuntimeException("An error occurred while creating temp file")
        }
    }
}
