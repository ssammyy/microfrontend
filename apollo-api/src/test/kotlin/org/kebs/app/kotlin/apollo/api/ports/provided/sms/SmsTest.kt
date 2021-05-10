package org.kebs.app.kotlin.apollo.api.ports.provided.sms

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SftpServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class SmsTest {

    @Autowired
    lateinit var smsService: SmsServiceImpl

    @Test
    fun testSmsSendingSuccessful() {
        val phone = "0707388882"
        val message = "This is a test message"

        val success: Boolean = smsService.sendSms(phone, message)

        Assert.assertTrue(success)
    }
}