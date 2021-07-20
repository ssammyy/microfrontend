package org.kebs.app.kotlin.apollo.api.ports.provided.lims

import org.apache.commons.codec.binary.Base64
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.http.HttpHeaders
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.springframework.stereotype.Service

@Service
class Downloader(
    private val jasyptStringEncryptor: StringEncryptor,
    private val commonDaoServices: CommonDaoServices,
) {
    fun download(url: URL, dstFile: File, applicationMapID: Long): File {
        val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapID)

        val b = Base64()
        val encoding: String =
            b.encodeAsString(("${jasyptStringEncryptor.decrypt(config.username)}:${jasyptStringEncryptor.decrypt(config.password)}").toByteArray())

        val httpclient: CloseableHttpClient = HttpClients.custom()
            .setRedirectStrategy(LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods
            .build()
        return try {
            val post = HttpPost(url.toURI()) // we're using GET but it could be via POST as well
            post.setHeader(HttpHeaders.AUTHORIZATION, "Basic $encoding")
            httpclient.execute(post, FileDownloadResponseHandler(dstFile))
        } catch (e: Exception) {
            throw IllegalStateException(e)
        } finally {
            IOUtils.closeQuietly(httpclient)
        }
    }

    internal class FileDownloadResponseHandler(private val target: File?) : ResponseHandler<File> {
        @Throws(ClientProtocolException::class, IOException::class)
        override fun handleResponse(response: HttpResponse): File? {
            val source: InputStream = response.entity.content
            FileUtils.copyInputStreamToFile(source, target)
            return target
        }

    }
}