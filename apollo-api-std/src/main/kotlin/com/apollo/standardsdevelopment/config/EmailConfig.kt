package com.apollo.standardsdevelopment.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class EmailConfig {

    @Value("\${spring.mail.host}")
     val host: String? = null

    @Value("\${spring.mail.port}")
     val port = 0

    @Value("\${spring.mail.username}")
     val username: String? = null

    @Value("\${spring.mail.password}")
     val password: String? = null



}
