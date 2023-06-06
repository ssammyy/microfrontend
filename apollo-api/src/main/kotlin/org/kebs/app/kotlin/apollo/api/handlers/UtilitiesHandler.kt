package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.apache.poi.util.IOUtils
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.common.dto.HashListDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.io.*
import javax.servlet.http.HttpServletResponse

@Component
class UtilitiesHandler(
    private val commonDaoServices: CommonDaoServices,
    private val service: DaoService,
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

    fun unHashString(req: ServerRequest): ServerResponse {
        try {
            val dto = req.body<HashListDto>()
            val hashedString = dto.stringDetails?.let { commonDaoServices.UnhashString(it) }
            hashedString?.let { return ServerResponse.ok().body(it) }
                ?: throw NullValueNotAllowedException("No UNHashed String Found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    /**
     * Split the given file into Map, the files supported format are .xls, xlsx,.csv, .pdf (with table)
     * Returns the file content as json array of maps
     * @param files At least one File to process
     * @param fileType type of file to process in case it has special formatted field
     */
    fun processCsvOrExcelFile(files: MultipartFile, fileType: String?): List<Any> {
        val result = mutableListOf<Any>()
        if (files.isEmpty) {
            return result
        }
        KotlinLogging.logger { }.info("Processing file to CSV: ${files.contentType}->${files.originalFilename}")
        when (files.contentType?.toLowerCase()) {
            MediaType.APPLICATION_JSON_VALUE -> {
                val targetReader: Reader = InputStreamReader(ByteArrayInputStream(files.bytes))
                //Generate PDF File &
                val records = service.readCsvFileIntoMap(',', targetReader)
                records.forEach { record ->
                    val resultDoc = mutableMapOf<String, Any?>()
                    record.forEach { k, v ->
                        resultDoc[k.trim().capitalize().replace(Regex.fromLiteral("^[A-Za-z0-9_]+"), "")] = v
                    }
                    result.add(resultDoc)
                }
            }
            MediaType.APPLICATION_OCTET_STREAM_VALUE -> {
                val records = service.readExcelFileIntoMap(',', files.bytes)
                records.forEach { record ->
                    val resultDoc = mutableMapOf<String, Any?>()
                    record.forEach { k, v ->
                        resultDoc[k.trim().capitalize().replace(Regex.fromLiteral("^[A-Za-z0-9_]+"), "")] = v
                    }
                    result.add(resultDoc)
                }
            }
            MediaType.APPLICATION_PDF_VALUE -> {
                throw ExpectedDataNotFound("Unsupported file type: PDF")
            }
            else -> {
                throw ExpectedDataNotFound("Unsupported file type: " + files.contentType?.toLowerCase())
            }

        }
        return result
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





