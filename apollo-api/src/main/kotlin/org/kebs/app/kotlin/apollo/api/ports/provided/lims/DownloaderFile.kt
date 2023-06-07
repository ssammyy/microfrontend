package org.kebs.app.kotlin.apollo.api.ports.provided.lims

import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.http.HttpHeaders
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.message.BasicNameValuePair
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.activation.MimetypesFileTypeMap


@Service
class DownloaderFile(
    private val jasyptStringEncryptor: StringEncryptor,
    private val commonDaoServices: CommonDaoServices,
) {
    fun download(dstFile: File, applicationMapID: Long, postDataParams: HashMap<String, String>): File {
        val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapID)
        val url = URL(config.url)
        val b = Base64()
        val encoding: String =
            b.encodeAsString(("${jasyptStringEncryptor.decrypt(config.username)}:${jasyptStringEncryptor.decrypt(config.password)}").toByteArray())

        val httpclient: CloseableHttpClient = HttpClients.custom()
            .setRedirectStrategy(LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods
            .build()
        return try {
            val post = HttpPost(url.toURI()) // we're using GET but it could be via POST as well
            post.setHeader(HttpHeaders.AUTHORIZATION, "Basic $encoding")
            httpclient.execute(getPostDataString(postDataParams, post), FileDownloadResponseHandler(dstFile))
        } catch (e: Exception) {
            throw IllegalStateException(e)
        } finally {
            IOUtils.closeQuietly(httpclient)
        }
    }

    internal class FileDownloadResponseHandler(private val target: File?) : ResponseHandler<File> {
        @Throws(ClientProtocolException::class, IOException::class)
        override fun handleResponse(response: HttpResponse): File? {
            println("::::::::::::::::::::::::CONNECTION MADE::::::::::::::::")
            val source: InputStream = response.entity.content
            FileUtils.copyInputStreamToFile(source, target)
            println("::::::::::::::::::::::::FILE FOUND::::::::::::::::")
            return target
        }

    }


    fun getPostDataString(params: HashMap<String, String>, httpPost: HttpPost): HttpPost {
        val postParameters: ArrayList<NameValuePair> = ArrayList<NameValuePair>()
        for ((key, value) in params) {
            postParameters.add(BasicNameValuePair(key, value))
        }
        httpPost.entity = UrlEncodedFormEntity(postParameters, "UTF-8")

        return httpPost
    }

    fun getFileTypeByMimetypesFileTypeMap(fileName: String?): String? {
        val fileTypeMap = MimetypesFileTypeMap()
        return fileTypeMap.getContentType(fileName)
    }
}