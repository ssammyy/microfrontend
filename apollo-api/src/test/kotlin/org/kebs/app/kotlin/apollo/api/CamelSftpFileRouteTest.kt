package org.kebs.app.kotlin.apollo.api

import mu.KotlinLogging
import org.apache.camel.RoutesBuilder
import org.apache.camel.builder.RouteBuilder
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SFTPService
import org.kebs.app.kotlin.apollo.api.service.FileStorageService
import org.kebs.app.kotlin.apollo.store.repo.ISftpTransmissionEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.springframework.test.context.junit4.SpringRunner
import java.io.File

@SpringBootTest
@RunWith(SpringRunner::class)
class CamelSftpFileRouteTest{
    @Autowired
    private lateinit var sftpService: SFTPService
    @Autowired
    private lateinit var resourceLoader: ResourceLoader
    @Autowired
    private lateinit var sftpRepository: ISftpTransmissionEntityRepository
    fun createRouteBuilder2(): RoutesBuilder {
        return object : RouteBuilder() {
            override fun configure() {
                from("direct:start").to("mock:result")
            }
        }
    }

    @Test
    fun resubmitFile() {
        val file=sftpRepository.findById(100)
        if(file.isPresent) {
            val exchange = this.sftpService.resubmitFile(file.get())
            Assert.assertNotNull("Expected delivery to be successful", exchange)
        } else {
            Assert.fail("Expected file not found")
        }
    }

    @Test
    fun resubmitFileViaSftp() {
        val file=File("sent-files/KEBS_DEMAND-DN20210407C4C3D-1-B-20211508091506.xml")
        KotlinLogging.logger {  }.info("File: ${file.absoluteFile}")
        val exchange = this.sftpService.uploadFile(file)
        Assert.assertTrue("Expected delivery to be successful",exchange)
    }

}