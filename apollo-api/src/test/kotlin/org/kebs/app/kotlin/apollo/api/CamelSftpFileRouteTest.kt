package org.kebs.app.kotlin.apollo.api

import org.apache.camel.RoutesBuilder
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.test.junit5.CamelTestSupport
import org.apache.http.client.utils.URIBuilder
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SFTPService


class CamelSftpFileRouteTest() : CamelTestSupport() {

    fun createRouteBuilder2(): RoutesBuilder {
        return object : RouteBuilder() {
            override fun configure() {
                from("direct:start").to("mock:result")
            }
        }
    }

    @Throws(Exception::class)
    override fun createRouteBuilder(): RouteBuilder {
        val fromFtpUrl = URIBuilder()
                .setScheme("sftp")
                .setHost("10.10.0.127")
                .setPath("/C/mhxapps/inbound/")
                .addParameter("username", "kebs\\\\bsk")
                .addParameter("password", "1ntegrat10n@!234")

        return object : RouteBuilder() {
            override fun configure() {
                from(fromFtpUrl.toString())
                        .setHeader("useId").simple("system123")
                        .setHeader("fileName").simple("\${file:name}")
                        .bean(SFTPService::class.java, "downloadAndProcessFile(\${body}, \${headers})")
                        .log("Downloaded file \${file:name} complete.")
            }
        }
    }


}