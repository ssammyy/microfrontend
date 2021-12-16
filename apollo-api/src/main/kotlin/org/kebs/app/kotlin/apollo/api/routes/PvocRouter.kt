package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.invoice.CorporateCustomerHandler
import org.kebs.app.kotlin.apollo.api.handlers.pvoc.PvocHandler
import org.kebs.app.kotlin.apollo.api.handlers.pvoc.PvocPartnersHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router


@Configuration
class PvocRouter {

    @Bean
    @CrossOrigin
    fun pvocCorporates(handler: CorporateCustomerHandler) = router {
        "/api/v1/corporate".nest {
            GET("/list", handler::listCorporateCustomers)
            POST("/add", handler::addCorporateCustomer)
            PUT("/update/{corporateId}", handler::updateCorporateCustomer)
            PATCH("/status/{corporateId}", handler::updateCorporateCustomerStatus)
        }
    }

    @Bean
    @CrossOrigin
    fun pvocPartners(handler: PvocPartnersHandler) = router {
        "/api/v1/partners".nest {
            GET("/list", handler::listPartners)
            POST("/add", handler::createPartner)
            PUT("/update/{corporateId}", handler::updatePartnerDetails)
            POST("/create/api-client/{corporateId}", handler::createPartnerApiClient)
        }
    }

    /**
     * Pre Export Verification of Conformity
     */
    @Bean
    @CrossOrigin
    fun apiPvocApplications(handler: PvocHandler) = router {
        "/api/v1/pvoc".nest {
            GET("/waiver/categories", handler::waiverCategories)
            GET("/waiver/history", handler::waiverHistory)
            GET("/waiver/{waiverId}", handler::viewWaiver)
            POST("/waiver/apply", handler::waiversApplication)
            POST("/exemption/apply", handler::exemptionApplication)
            GET("/exemption/check/eligible", handler::checkExemptionEligibility)
            GET("/exemption/history", handler::exemptionHistory)
            GET("/exemption/{exemptionId}", handler::viewExemption)

        }
    }
}