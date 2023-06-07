package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import org.jasypt.encryption.StringEncryptor
import org.json.JSONObject
import org.json.XML
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.IntegrationConfigurationEntity
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.IOException

@Service
class UpAndDownLoad(
        private val jasyptStringEncryptor: StringEncryptor,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties
) {

    private fun gimmeFactory(config: IntegrationConfigurationEntity): DefaultSftpSessionFactory {
        val factory = DefaultSftpSessionFactory()
        factory.setHost(config.url)
        factory.setPort(config.portNumber)
        config.unknownHostKey?.let { factory.setAllowUnknownKeys(it) } ?: factory.setAllowUnknownKeys(false)

        factory.setUser(jasyptStringEncryptor.decrypt(config.username))
        factory.setPassword(jasyptStringEncryptor.decrypt(config.password))
        return factory
    }

    fun upload(configID: Long) {
        val config = commonDaoServices.findIntegrationConfigurationEntity(configID)
        val session = gimmeFactory(config).session
        UpAndDownLoad::class.java.classLoader.getResourceAsStream(applicationMapProperties.mapSftpUploadName)
                ?.let {resourceAsStream->
                    try {
                        session.write(resourceAsStream, "${applicationMapProperties.mapSftpUploadNameDestination}${applicationMapProperties.mapSftpUploadName}")
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                    session.close()
                }
                ?: throw Exception("File with name = ${applicationMapProperties.mapSftpUploadName},does Not exist check config")

    }

    fun download(configID: Long): ByteArray {
        val config = commonDaoServices.findIntegrationConfigurationEntity(configID)
        val session = gimmeFactory(config).session
        val outputStream = ByteArrayOutputStream()
         try {
            session.read("${applicationMapProperties.mapSftpUploadNameDestination}${applicationMapProperties.mapSftpUploadName}", outputStream)
             return outputStream.toByteArray()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun convertXmlToJson(xmlFile:String): String {
        val fileJsonObject: JSONObject = XML.toJSONObject(xmlFile)
        return fileJsonObject.toString()
    }
}
