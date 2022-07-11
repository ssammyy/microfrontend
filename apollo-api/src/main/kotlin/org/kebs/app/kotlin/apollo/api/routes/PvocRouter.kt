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
            GET("/names", handler::listPartnerNames)
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
    @CrossOrigin
    fun pvocAgentMonitoring(handler: PvocMonitoringHandler) = router {
        "api/v1/pvoc/monitoring".nest {
            GET("/get/{applicationStatus}", handler::listMonitoringIssues)
            GET("/rfc/{rfcId}", handler::getRfcForCoiOrCoc)
            GET("/rfc/cocs", handler::listRfcsCoc)
            GET("/rfc/cois", handler::listRfcsCoi)
            GET("/foreign/cois", handler::listForeignCoi)
            GET("/foreign/cocs", handler::listForeignCoc)
            GET("/foreign/cocorcoi/{cocCoiId}", handler::getForeignCoiOrCoc)
            GET("/foreign/cor", handler::listForeignCor)
            GET("/foreign/cor/{corId}", handler::getForeignCor)
        }
    }

    @Bean
    @CrossOrigin
    fun pvocQueries(handler: PvocQueryHandler) = router {
        "api/v1/pvoc/kebs/query".nest {
            POST("/request", handler::pvocPartnerQueryRequest)
            POST("/conclusion", handler::pvocPartnerQueryConclusion)
            POST("/response", handler::pvocPartnerQueryResponse)
        }
    }

    @Bean
    fun pvocComplaint(handler: PvocComplaintHandler) = router {
        "api/v1/pvoc/complaint".nest {
            GET("/get/{applicationStatus}", handler::complaintApplications)
            GET("/details/{complaintId}", handler::complaintApplicationDetails)
            POST("/status/update/{complaintId}", handler::approveCurrentComplaintTask)
        }
    }

    @Bean
    fun pvocWaiverApplications(handler: PvocWaiverHandler) = router {
        "/api/v1/pvoc/waiver".nest {
            GET("/get/{applicationStatus}", handler::waiverApplications)
            GET("/details/{waiverId}", handler::waiverApplicationDetails)
            POST("/status/update/{waiverId}", handler::waiverTaskUpdate)
        }
    }
}