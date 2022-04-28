package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.invoice.InvoiceHandlers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Configuration
class CallbackRouter {

    @Bean
    @CrossOrigin
    fun paymentCallbacks(handler: InvoiceHandlers) = router {
        "/api/v1/callback".nest {
            POST("/payment/completed", handler::paymentCallback)
//            POST("/payment-qa/completed", handler::processPaymentSageNotification)
        }
        "/api/v1/callback/qa".nest {
            POST("/payment/completed", handler::processPaymentSageNotification)
        }
    }
}
