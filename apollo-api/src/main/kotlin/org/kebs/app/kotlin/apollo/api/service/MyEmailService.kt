package org.kebs.app.kotlin.apollo.api.service

import org.springframework.mail.SimpleMailMessage

import org.springframework.mail.javamail.JavaMailSender

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class MyEmailService {
    @Autowired
    private val javaMailSender: JavaMailSender? = null

    fun sendOtpMessage(to: String?, subject: String?, message: String?) {
        val simpleMailMessage = SimpleMailMessage()
        simpleMailMessage.setTo(to)
        simpleMailMessage.setSubject(subject!!)
        simpleMailMessage.setText(message!!)
        //Uncomment to send mail
        //javaMailSender.send(simpleMailMessage);
    }
}