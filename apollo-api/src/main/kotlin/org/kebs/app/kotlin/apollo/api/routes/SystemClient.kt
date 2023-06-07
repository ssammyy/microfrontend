package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.ApiClientHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Configuration
class SystemClient {

    @Bean
    @CrossOrigin
    fun systemApiClients(handler: ApiClientHandler) = router {
        "/api/v1/client".nest {
            GET("/list", handler::loadClients)
            POST("/add", handler::addApiClient)
            PATCH("/update/{clientId}", handler::updateApiClient)
            POST("/status", handler::updateApiClientStatus)
        }
        "/api/v1/oauth".nest {
            POST("/token",handler::authenticateApiClient)
        }
    }
}