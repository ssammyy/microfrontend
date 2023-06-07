package org.kebs.app.kotlin.apollo.config.security.ssl

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.SslProvider
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.kebs.app.kotlin.apollo.config.loader.resources.ResourceLoaderService
import org.kebs.app.kotlin.apollo.config.properties.web.ThymeleafProperties
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.net.ssl.*

@Component
class SslContextFactory(
    private val properties: ThymeleafProperties,
    private val loaderService: ResourceLoaderService,
) {
//    private val TRUST_ALL_CERTS: Array<TrustManager> = arrayOf<X509TrustManager>(DummyTrustManager())


    @Throws(IOException::class)
    private fun buildTrustManagers(trustStore: KeyStore?): Array<TrustManager> {
        /*
            TODO: trustStore == null does not make sense, to be tested
         */

        return if (trustStore != null) {
            try {
                val trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(trustStore)
                trustManagerFactory.trustManagers
            } catch (exc: NoSuchAlgorithmException) {
                throw IOException("Unable to initialise TrustManager[]", exc)
            } catch (exc: KeyStoreException) {
                throw IOException("Unable to initialise TrustManager[]", exc)
            }
        } else {
            TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).trustManagers
        }
    }

    @Throws(IOException::class)
    private fun buildKeyManagers(keyStore: KeyStore, storePassword: CharArray): Array<KeyManager> {
        return try {
            val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(keyStore, storePassword)
            keyManagerFactory.keyManagers
        } catch (exc: NoSuchAlgorithmException) {
            throw IOException("Unable to initialise KeyManager[]", exc)
        } catch (exc: UnrecoverableKeyException) {
            throw IOException("Unable to initialise KeyManager[]", exc)
        } catch (exc: KeyStoreException) {
            throw IOException("Unable to initialise KeyManager[]", exc)
        }
    }

    fun createNettySslContext(): SslContext? {
        var sslContext: SslContext? = null
        try {
            val keyStoreName: String = properties.ussdSslServerKeystorePath
            val keyStoreType: String = properties.ussdSslKeystoreType
            val keyStorePassword: String = properties.ussdSslServerKeystorePassword
            val keyStore = loadKeyStore(keyStoreName, keyStoreType, keyStorePassword)
            val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray())
//            val trustManagers: Array<TrustManager> = buildTrustManagers(keyStore)
            sslContext = SslContextBuilder.forClient()
                .keyManager(keyManagerFactory)
                .sslProvider(SslProvider.JDK)
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
        return sslContext
    }

    @Throws(IOException::class)
    fun createSslContext(): SSLContext {
        val keyStoreName: String = properties.ussdSslServerKeystorePath
        val keyStoreType: String = properties.ussdSslKeystoreType
        val keyStorePassword: String = properties.ussdSslServerKeystorePassword
        val keyStore = loadKeyStore(keyStoreName, keyStoreType, keyStorePassword)

        val keyManagers: Array<KeyManager> = buildKeyManagers(keyStore, keyStorePassword.toCharArray())

        val trustManagers: Array<TrustManager> = buildTrustManagers(keyStore)
        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance(properties.ussdSslServerContextInstance)
            sslContext.init(keyManagers, trustManagers, null)
        } catch (e: NoSuchAlgorithmException) {
            throw IOException("Unable to create and initialise the SSLContext invalid algoritm", e)
        } catch (e: KeyManagementException) {
            throw IOException("Unable to create and initialise the SSLContext, invalid Keys ", e)
        }
        return sslContext
    }

    @Throws(IOException::class)
    private fun loadKeyStore(location: String, type: String, storePassword: String): KeyStore {
        /**
         * DONE: Use resource loader
         */
        val resource: Resource = loaderService.getResource(location)
        try {
            resource.inputStream.use { stream ->
                val loadedKeystore = KeyStore.getInstance(type)
                loadedKeystore.load(stream, storePassword.toCharArray())
                return loadedKeystore
            }
        } catch (exc: KeyStoreException) {
            throw IOException(String.format("Unable to load KeyStore %s", location), exc)
        } catch (exc: NoSuchAlgorithmException) {
            throw IOException(String.format("Unable to load KeyStore %s", location), exc)
        } catch (exc: CertificateException) {
            throw IOException(String.format("Unable to load KeyStore %s", location), exc)
        }
    }


}