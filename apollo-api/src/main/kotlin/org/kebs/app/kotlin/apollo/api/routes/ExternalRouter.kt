package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.ism.ISMHandler
import org.kebs.app.kotlin.apollo.api.handlers.pvoc.PvocClientHandler
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

    @Bean
    @CrossOrigin
    fun pvocClientRequests(handler: PvocClientHandler) = router {
        "/api/v1/pvoc/".nest {
            POST("/send/coiWithItems", handler::foreignCoi)
            POST("/send/rfcCoiWithItems", handler::foreignCoiRfc)
            POST("/coc", handler::foreignCoc)
            POST("/cor", handler::foreignCor)
            POST("/ncr", handler::foreignNcr)
            POST("/risk/profile", handler::riskProfile)
            POST("/idf/items", handler::idfDataWithItems)
            POST("/query/request", handler::pvocPartnerQueryRequest)
            POST("/query/response", handler::pvocPartnerQueryResponse)
            GET("/timeline/issues", handler::pvocTimelineIssues)
        }
    }
}