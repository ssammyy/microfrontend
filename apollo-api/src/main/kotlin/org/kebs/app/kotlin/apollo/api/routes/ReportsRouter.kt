package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.reports.ReportsHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Component
class ReportsRouter {

    @Bean
    @CrossOrigin
    fun applicationReports(handler: ReportsHandler) = router {
        "/api/v1/reports".nest {
            POST("/generate", handler::consignmentReport)
        }
    }
}