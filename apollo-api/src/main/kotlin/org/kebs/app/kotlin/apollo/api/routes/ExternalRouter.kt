package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.ism.ISMHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Component
class ExternalRouter {

    @Bean
    @CrossOrigin
    fun externalStandardMark(handler: ISMHandler) = router {
        "/api/v1/external/ism".nest {
            POST("/apply", handler::createIsmRequests)
            POST("/requests", handler::listExternalUserApplications)
        }
    }
}