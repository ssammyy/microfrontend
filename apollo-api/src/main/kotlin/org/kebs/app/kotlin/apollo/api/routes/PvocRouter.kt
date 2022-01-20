package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.pvoc.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router


@Configuration
class PvocRouter {

    @Bean
    @CrossOrigin
    fun pvocPartners(handler: PvocPartnersHandler) = router {
        "/api/v1/partners".nest {
            GET("/countries", handler::listPartnerCountries)
            GET("/types", handler::listPartnerTypes)
            GET("/list", handler::listPartners)
            POST("/add", handler::createPartner)
            GET("/details/{partnerId}", handler::getPartnerDetails)
            PUT("/update/{partnerId}", handler::updatePartnerDetails)
            POST("/create/api-client/{partnerId}", handler::createPartnerApiClient)
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

    @Bean
    @CrossOrigin
    fun pvocExemptions(handler: PvocExemptionHandler) = router {
        "api/v1/pvoc/exemption".nest {
            GET("/get/{applicationStatus}", handler::exemptionApplications)
            GET("/details/{exemptionId}", handler::exemptionApplicationDetails)
            POST("/status/update/{exemptionId}", handler::updateExemptionStatus)
        }
    }

    @Bean
    fun pvocComplaint(handler: PvocComplaintHandler) = router {
        "api/v1/pvoc/complaint".nest {
            GET("/get/{applicationStatus}", handler::complaintApplications)
            GET("/details/{complaintId}", handler::complaintApplicationDetails)
        }
    }

    @Bean
    fun pvocWaiverApplications(handler: PvocWaiverHandler) = router {
        "/api/v1/pvoc/waiver".nest {
            GET("/get/{applicationStatus}", handler::waiverApplications)
            GET("/details/{waiverId}", handler::waiverApplicationDetails)
        }
    }
}