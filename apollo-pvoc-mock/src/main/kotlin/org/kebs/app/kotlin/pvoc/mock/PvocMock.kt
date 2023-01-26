package org.kebs.app.kotlin.pvoc.mock

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackages = ["org.kebs.app.kotlin.pvoc.mock"])
class PvocMock : SpringBootServletInitializer() {
    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        return mapper
    }

    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(PvocMock::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<PvocMock>(*args)
}