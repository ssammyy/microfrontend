package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import com.google.common.io.Files
import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import jdk.internal.net.http.common.Log.channel
import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*


@Service("sftpService")
class SftpServiceImpl(
    private val applicationMapProperties: ApplicationMapProperties,
    private val  jasyptStringEncryptor: StringEncryptor
) : ISftpService {

//    val SESSION_CONFIG_STRICT_HOST_KEY_CHECKING: String = "StrictHostKeyChecking"
    val username = applicationMapProperties.mapSftpUsername
    val host = applicationMapProperties.mapSftpHost
    val password = applicationMapProperties.mapSftpPassword

    fun createSftp(): ChannelSftp {
//        val decryptedUsername = jasyptStringEncryptor.decrypt(applicationMapProperties.mapSftpUsername)
//        val decryptedPassword = jasyptStringEncryptor.decrypt(applicationMapProperties.mapSftpPassword)

        val decryptedUsername = "kebs\\bsk"
        val decryptedPassword = "1ntegrat10n@!234"

        val jsch = JSch()
        KotlinLogging.logger { }.info(":::: Trying to connect sftp[$decryptedUsername @ $host], using password: $decryptedPassword ::::")

        val session: Session = createSession(jsch, host, decryptedUsername, port = applicationMapProperties.mapSftpPort)
        try {
            session.setPassword(decryptedPassword)
            session.connect()
        } catch (e: IOException) {
            KotlinLogging.logger { }.error("An error occurred while acquiring SFTP connection", e)
            throw RuntimeException("An error occurred while acquiring SFTP connection")
        }
        KotlinLogging.logger { }.info(":::: SFTP session connected successfully ::::")

        val channel: Channel  = session.openChannel(applicationMapProperties.mapSftpClientProtocol)
        channel.connect(applicationMapProperties.mapSftpChannelConnectedTimeout.toInt());
        KotlinLogging.logger { }.info(":::: SFTP channel created successfully ::::")

        return  channel as ChannelSftp
    }

    fun createSession(jSch: JSch, host: String, username: String, port: String): Session {
        var session: Session? = null
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
        try {
            sftp.cd(applicationMapProperties.mapSftpUploadRoot)
            KotlinLogging.logger { }.info(":::: Uploading file to: ${applicationMapProperties.mapSftpUploadRoot} ::::")

            val fileName = file.name
            sftp.put(file.inputStream(), fileName)

            return true
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while uploading sftp file: ${file.name}", e)
            throw RuntimeException("An error occurred while uploading sftp file")
        } finally {
            KotlinLogging.logger { }.info(":::: Disconnect sftp after upload ::::")
            this.disconnect(sftp)
        }
    }

    override fun downloadFilesByDocType(docType: String): List<File> {
        val sftp: ChannelSftp = this.createSftp()
        val filesList = mutableListOf<File>()
        try {
            sftp.cd(applicationMapProperties.mapSftpDownloadRoot)
            KotlinLogging.logger { }.info(":::: Downloading file to: ${applicationMapProperties.mapSftpDownloadRoot} ::::")

            val allFiles = sftp.ls(applicationMapProperties.mapSftpDownloadRoot)
            for (file in allFiles) {
                val entry: ChannelSftp.LsEntry = file as ChannelSftp.LsEntry
                if (validateKeswsFileByDocType(entry.filename, docType)) {
                    KotlinLogging.logger { }.info(":::: File found: ${entry.filename} ::::")
                    filesList.add(convertInputstreamToFile(sftp.get(entry.filename), entry.filename))
                }
            }
            return filesList
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while downloading sftp files", e)
            throw RuntimeException("An error occurred while downloading sftp files")
        }
    }

    fun moveFileToProcessedFolder(file: File): Boolean {
        val sftp: ChannelSftp = this.createSftp()
        try {
            val fileName = file.name
            KotlinLogging.logger { }.info(":::: Moving file: $fileName from directory: ${applicationMapProperties.mapSftpDownloadRoot}" +
                    " to directory: ${applicationMapProperties.mapSftpProcessedRoot}   ::::")
            sftp.rename(applicationMapProperties.mapSftpDownloadRoot.plus("/").plus(fileName), applicationMapProperties.mapSftpProcessedRoot.plus("/").plus(fileName))
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
        targetFile.deleteOnExit();
        try {
            FileUtils.copyInputStreamToFile(inputStream, targetFile)
            return targetFile
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while creating temp file for file: $fileName", e)
            throw RuntimeException("An error occurred while creating temp file")
        }
    }
}
