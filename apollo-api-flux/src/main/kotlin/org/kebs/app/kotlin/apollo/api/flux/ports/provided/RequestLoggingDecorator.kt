package org.kebs.app.kotlin.apollo.api.flux.ports.provided

import mu.KotlinLogging
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets

class RequestLoggingDecorator(delegate: ServerHttpRequest) : ServerHttpRequestDecorator(delegate) {
    override fun getBody(): Flux<DataBuffer> {
        val byteArrayOutputStream = ByteArrayOutputStream()
        return super.getBody().doOnNext { dataBuffer: DataBuffer ->
            try {
                Channels.newChannel(byteArrayOutputStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer())
                val body = String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8)
                KotlinLogging.logger { }.debug("Request: payload={}", body)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    byteArrayOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


}