package org.kebs.app.kotlin.apollo.api.flux.routes

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.handlers.PaymentsApiHandler
import org.kebs.app.kotlin.apollo.api.flux.handlers.PvocHandler
import org.kebs.app.kotlin.apollo.api.flux.handlers.RegistrationHandler
import org.kebs.app.kotlin.apollo.api.flux.handlers.StandardsLevyHandler
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.RequestLoggingDecorator
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.server.ServerWebExchangeDecorator

@Configuration
class ControllerRoutes(private val daoService: DaoService) {
    @Bean
    fun registrationRoutes(handler: RegistrationHandler) = coRouter {
        "/".nest {
            GET("/", handler::home)
            GET("/error", handler::error)
            POST("/", handler::homePost)
        }
        "/api/integ".nest {
            POST(pattern = "/internal", f = handler::internalResponse)
        }


    }

    @Bean
    fun paymentsApiRoutes(handler: PaymentsApiHandler) = coRouter {
        "/api/payments".nest {
            "/validate".nest {
                GET("/", daoService::invalidGetOnPostUrl)
                POST("/", handler::validateInvoice)
            }
            "/notify".nest {
                GET("/", daoService::invalidGetOnPostUrl)
                POST("/", handler::processPaymentNotification)
            }
        }
    }

    @Bean
    fun kraApiRoutes(handler: StandardsLevyHandler) = coRouter {
        "/api/kra".nest {
            GET("/receiveSL2Payment", daoService::invalidGetOnPostUrl)
            POST("/receiveSL2Payment", handler::processReceiveSL2Payment)
            "/send".nest {
                "/entryNumber".nest {
                    POST("{job}/start", handler::processSendEntryNumbers)
                }
                "/penalty".nest {
                    POST("{job}/start", handler::processSendPenalties)
                }
            }

        }
    }

    @Bean
    fun pvocPartnerRoutes(handler: PvocHandler) = coRouter {
        "/api/pvoc".nest {
            "/send".nest {
                GET("/coc", daoService::invalidGetOnPostUrl)
                POST("/coc", handler::receiveCoc)
                POST("/cocWithItems/v2", handler::receiveCocWithItems)
                POST("/coiWithItems/v2", handler::receiveCOIWithItems)
                POST("/rfcCoiWithItems/v2", handler::receiveRfcCOIWithItems)
                POST("/idfWithItems/v2", handler::receiveIdfWithItems)
                GET("/cocItems", daoService::invalidGetOnPostUrl)
                POST("/cocItems", handler::receiveCOCItems)
                GET("/cor", daoService::invalidGetOnPostUrl)
                POST("/cor", handler::receiveCOR)
                GET("/coi", daoService::invalidGetOnPostUrl)
                POST("/coi", handler::receiveCOI)
                //POST("/coiWithItems/v2", handler::receiveCOIWithItems)
                GET("/coiItems", daoService::invalidGetOnPostUrl)
                POST("/coiItems", handler::receiveCOIItems)
                GET("/riskProfile", daoService::invalidGetOnPostUrl)
                POST("/riskProfile", handler::receiveRiskProfile)


                "/monitoring".nest {
                    GET("/timelines", daoService::invalidGetOnPostUrl)
                    POST("/timelines", handler::receiveMonitoringTimelines)
                    GET("/standards", daoService::invalidGetOnPostUrl)
                    POST("/standards", handler::receiveMonitoringStandards)
                    GET("/queries", daoService::invalidGetOnPostUrl)
                    POST("/queries", handler::receiveMonitoringQueries)

                }

            }
            "/get".nest {
                "/monitoring".nest {
                    GET("/queries", handler::getMonitoringQueriesData)
                    POST("/queries", daoService::invalidGetOnPostUrl)

                }
                "/idf".nest {
                    GET("/{country}", handler::getIdfData)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/idfs".nest {
                    GET("/{country}", handler::getIdfsData)
                    POST("/{country}", daoService::invalidGetOnPostUrl)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/idfsWithItems".nest {
                    GET("/idfsWithItems/{country}", handler::getIdfsDataAndItems)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/idfItems".nest {
                    GET("/idfItems/{idfNumber}", handler::getIdfItemsData)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/invoice".nest {
                    GET("/{invoiceDate}/{soldTo}", handler::getInvoiceData)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/riskProfile".nest {
                    GET("/{categorizationDate}", handler::getRiskProfileData)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/goodsRfc".nest {
                    GET("/{partnerRef}/{rfcDate}", handler::getGoodsRfcData)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/coiRfc".nest {
                    GET("/{partnerRef}/{rfcDate}", handler::getCoiRfcData)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/coiRfcWithItems".nest {
                    GET("/{partnerRef}/{rfcDate}", handler::getCoiRfcDataAndItems)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/coiRfcItems".nest {
                    GET("/{rfcNumber}", handler::getCoiRfcItemsData)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
                "/corRfc".nest {
                    GET("/{partnerRef}/{rfcDate}", handler::getCorRfcData)
                    POST("/", daoService::invalidGetOnPostUrl)

                }
            }
        }
    }
        .thenLog()

    fun <T : ServerResponse> RouterFunction<T>.thenLog(): RouterFunction<T> {
        return this.filter { request, next ->

            val decorator: ServerWebExchangeDecorator = object : ServerWebExchangeDecorator(request.exchange()) {
                override fun getRequest(): ServerHttpRequest {
                    return RequestLoggingDecorator(request.exchange().request)
                }
            }

            KotlinLogging.logger { }.trace("Processing request $request with body $decorator")
            next.handle(request).doOnSuccess { KotlinLogging.logger { }.trace { "Handling with response $it" } }

        }
    }

}

