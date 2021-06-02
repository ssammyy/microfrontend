package org.kebs.app.kotlin.apollo.standardsdevelopment.controllers

import org.kebs.app.kotlin.apollo.standardsdevelopment.config.EmailConfig
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.Feedback
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.ValidationException


@RestController
@RequestMapping("/feedback")
class FeedbackController(val emailConfig: EmailConfig) {
    @PostMapping
    fun sendFeedback(@RequestBody feedback: Feedback,
                     bindingResult: BindingResult) {
        if (bindingResult.hasErrors()) {
            throw ValidationException("Feedback is not valid")
        }

        // Create a mail sender
        val mailSender = JavaMailSenderImpl()
        mailSender.host=this.emailConfig.host
        mailSender.port=this.emailConfig.port
        mailSender.username=this.emailConfig.username
        mailSender.password = this.emailConfig.password


        // Create an email instance
        val mailMessage = SimpleMailMessage()
        feedback.email?.let { mailMessage.setFrom(it) }
        mailMessage.setTo("rc@feedback.com")
        mailMessage.setSubject("New feedback from " + feedback.name)
        feedback.feedback?.let { mailMessage.setText(it) }

        // Send mail
        mailSender.send(mailMessage)
    }
}
