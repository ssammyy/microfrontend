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
            POST("/send/rfc/coi", handler::foreignCoiRfc)
            POST("/send/rfc/coc", handler::foreignCocRfc)
            POST("/send/rfc/cor", handler::foreignCorRfc)
            POST("/coc", handler::foreignCoc)
            POST("/cor", handler::foreignCor)
            POST("/ncr", handler::foreignNcr)
            POST("/risk/profile", handler::riskProfile)
            GET("/risk/profiles", handler::listRiskProfile)
            GET("/invoice/{inv_date}", handler::listMonthlyInvoiceDetails)
            POST("/idf/items", handler::idfDataWithItems)
            GET("/get/idf/withItems", handler::idfRequestDataWithItems)
            GET("/get/rfc/withItems", handler::rfcRequestDataWithItems)
            POST("/document/query/request", handler::pvocPartnerQueryRequest)
            POST("/document/query/response", handler::pvocPartnerQueryResponse)
            GET("/document/queries", handler::kebsPartnerQueries)
            GET("/timeline/issues", handler::pvocTimelineIssues)
        }
    }
}