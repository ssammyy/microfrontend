package org.kebs.app.kotlin.apollo.api.routes;

import org.kebs.app.kotlin.apollo.api.handlers.di.ConfigurationsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.function.router

@Configuration
class ConfigurationRoutes {

    @Bean
    @CrossOrigin
    fun destinationInspectionConfigurations(handler: ConfigurationsHandler) = router {
        "/api/v1/di/config".nest {
            GET("/cfs/stations", handler::listCfsStations)
            POST("/cfs/station", handler::addCfsStations)
            PUT("/cfs/station/{cfsId}", handler::updateCfsStation)
            GET("/revenue/lines", handler::listRevenueLines)
            DELETE("/cfs/station/{cfsId}", handler::removeCfsStation)
            GET("/custom/offices", handler::listCustomsOffices)
            POST("/custom/office", handler::addCustomsOffice)
            PUT("/custom/office/{officeId}", handler::updateCustomsOffice)
            DELETE("/custom/office/{officeId}", handler::deleteCustomOffice)
            GET("/billing/limits", handler::listBillLimits)
            POST("/billing/limit", handler::addBillingLimit)
            PUT("/billing/limit/{limitId}", handler::updateBillingLimit)
            DELETE("/billing/limit/{limitId}", handler::removeBillLimit)
            GET("/inspection/fee", handler::listDestinationInspectionFee)
            POST("/inspection/fee", handler::addDestinationInspectionFee)
            GET("/inspection/fee/{feeId}", handler::destinationInspectionFeeDetails)
            PUT("/inspection/fee/{feeId}", handler::updateDestinationInspectionFee)
            DELETE("/inspection/fee/{feeId}", handler::deleteDestinationInspectionFee)
            POST("/inspection/fee/{feeId}/range", handler::addDestinationInspectionFeeRange)
            PUT("/inspection/fee/{feeId}/range/{rangeId}", handler::updateDestinationInspectionFeeRange)
        }
    }

    @Bean
    @CrossOrigin
    fun pvocConfigurations(handler: ConfigurationsHandler) = router {
        "/api/v1/pvoc/config".nest {
            GET("/countries", handler::listPvocCountries)
            POST("/country", handler::addPvocCountry)
            PUT("/country/{countryId}", handler::updatePvocCountry)
            DELETE("/country/{countryId}", handler::deletePvocCountry)
            GET("/regions", handler::listPvocRegions)
            POST("/region", handler::addPvocRegion)
            PUT("/region/{regionId}", handler::updatePvocRegion)
            DELETE("/region/{regionId}", handler::deletePvocRegion)

        }
    }
}
