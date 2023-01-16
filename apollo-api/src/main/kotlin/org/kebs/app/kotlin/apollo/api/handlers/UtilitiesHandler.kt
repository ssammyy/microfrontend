package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.apache.poi.util.IOUtils
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.HashListDto
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import javax.servlet.http.HttpServletResponse

@Component
class UtilitiesHandler(
        private val commonDaoServices: CommonDaoServices
) {
    fun hashString(req: ServerRequest): ServerResponse {
        try {
            val dto = req.body<HashListDto>()
            val hashedString = dto.stringDetails?.let { commonDaoServices.hashString(it) }
            hashedString?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No Hashed String Found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun download(stream: ByteArrayOutputStream, fileName: String, httResponse: HttpServletResponse): Boolean {
        httResponse.setHeader("Content-Disposition", "inline; filename=\"${fileName}\";")
        httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
        httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
        httResponse.setContentLengthLong(stream.size().toLong())
        httResponse.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(stream.toByteArray())
                responseOutputStream.close()
            }
        return true
    }

    fun downloadFile(stream: FileInputStream, fileName: String, httResponse: HttpServletResponse): Boolean {
        httResponse.setHeader("Content-Disposition", "inline; filename=\"${fileName}\";")
        httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
        httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
        httResponse.setContentLengthLong(stream.channel.size())
        IOUtils.copy(stream, httResponse.outputStream)
        return true
    }

    fun downloadBytes(bytes: ByteArray, fileName: String, httResponse: HttpServletResponse): Boolean {
        httResponse.setHeader("Content-Disposition", "inline; filename=\"${fileName}\";")
        httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
        httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
        httResponse.setContentLength(bytes.size)
        httResponse.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(bytes)
                responseOutputStream.close()
            }
        return true
    }

}





