package org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao


import com.ctc.wstx.api.WstxInputProperties
import com.ctc.wstx.api.WstxOutputProperties
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.LoginRequest
import org.kebs.app.kotlin.apollo.store.model.IntegrationConfigurationEntity
import org.springframework.core.ResolvableType
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.json.AbstractJackson2Decoder
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.io.File
import java.io.FileWriter
import com.google.common.io.Files
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory

object HttpExceptionFactory {
    fun badRequest(message: String?): ResponseStatusException =

        ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request $message")

    fun unauthorized(): ResponseStatusException = ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")
}

fun extractUsernameAndPassword(exchange: ServerWebExchange, jacksonDecoder: AbstractJackson2Decoder): LoginRequest? {
    val dataBuffer = exchange.request.body
    val type = ResolvableType.forClass(LoginRequest::class.java)
    return jacksonDecoder
        .decodeToMono(dataBuffer, type, MediaType.APPLICATION_JSON, mapOf())
        .onErrorResume { Mono.empty<LoginRequest>() }
        .cast(LoginRequest::class.java)
        .block()
}

fun base256Encode(inputStr: String, config: IntegrationConfigurationEntity? = null): String {
    val algo = config?.let { it.hashingAlgorithm ?: "SHA3-256" } ?: "SHA3-256"
    val digest = MessageDigest.getInstance(algo)
    val hashBytes = digest.digest(inputStr.toByteArray())
    return bytesToHex(hashBytes)
}

fun bytesToHex(hash: ByteArray): String {
    val hexString = StringBuilder(2 * hash.size)
    for (h in hash) {
        val hex = Integer.toHexString(0xff and h.toInt())
        if (hex.length == 1) hexString.append('0')
        hexString.append(hex)
    }
    return hexString.toString()
}

fun createKesWsFileName(filePrefix: String, documentIdentifier: String): String {
    val current = LocalDateTime.now()

    val formatter = DateTimeFormatter.ofPattern("yyyymmddhhmmss")
    val formatted = current.format(formatter)

    var finalFileName = filePrefix
        .plus("-")
        .plus(documentIdentifier)
        .plus("-1-B-")
        .plus(formatted)
        .plus(".xml")
    finalFileName = finalFileName.replace("\\s".toRegex(), "")

    return finalFileName
}

val xmlMapper: ObjectMapper = run {
    val iFactory: XMLInputFactory = WstxInputFactory()
    iFactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, 32000)
    val oFactory: XMLOutputFactory = WstxOutputFactory()
    oFactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, true)
    val xf = XmlFactory(iFactory, oFactory)
    val xmlMapper: ObjectMapper = XmlMapper(xf)
        .registerModule(KotlinModule())
    xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
    xmlMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)
    xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    xmlMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
    xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    xmlMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
    xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
    /**
     * This line causes an exception to be thrown if the test has more than one thread!
     */
//        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
    xmlMapper
}

fun serializeToXml(fileName: String, obj: Any): File {
    try {
        val xmlString = xmlMapper.writeValueAsString(obj)
        val targetFile = File(Files.createTempDir(), fileName)
        targetFile.deleteOnExit()
        val fileWriter = FileWriter(targetFile)
        fileWriter.write(xmlString)
        fileWriter.close()
        return targetFile
    } catch (e: Exception) {
        KotlinLogging.logger { }.error("An error occurred with xml serialization", e)
        throw RuntimeException("An error occurred while serializing xml")
    }
}
