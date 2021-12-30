package org.kebs.app.kotlin.apollo.api.routes;

import org.kebs.app.kotlin.apollo.api.handlers.invoice.CorporateCustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.function.router

@Component
class InvoiceRouter {
    @Bean
    @CrossOrigin
    fun pvocCorporatesManagement(handler:CorporateCustomerHandler) = router {
        "/api/v1/corporate".nest {
            GET("/list", handler::listCorporateCustomers)
            POST("/add", handler::addCorporateCustomer)
            GET("/details/{corporateId}", handler::corporateDetails)
            GET("/bills/{corporateId}", handler::currentCorporateBills)
            GET("/bills/{corporateId}/status/{billStatus}", handler::corporateBillStatus)
            GET("/bill/{corporateId}/details/{billId}", handler::corporateBillDetails)
            PUT("/update/{corporateId}", handler::updateCorporateCustomer)
            PATCH("/status/{corporateId}", handler::updateCorporateCustomerStatus)
        }
    }
}
