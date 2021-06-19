package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.MasterDataHandler
import org.kebs.app.kotlin.apollo.api.handlers.RegistrationHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.router


@Configuration
class AngularRoutes{
    @Bean
    fun migrationRegistrationRoutes(handler: RegistrationHandler, otherHandler: MasterDataHandler) = router {
        "/api/v1/migration/".nest {
            "anonymous".nest {
                "/validateBrs".nest {
                    GET("", otherHandler::notSupported)
                    POST("", handler::handleValidateAgainstBrs)
                    "/{id}".nest {
                        GET("", otherHandler::notSupported)
                        PUT("", otherHandler::notSupported)
                    }

                }
                "/sendToken".nest {
                    GET("", otherHandler::notSupported)
                    POST("", handler::handleSendValidationTokenToCellphoneNumber)
                    "/{id}".nest {
                        GET("", otherHandler::notSupported)
                        PUT("", otherHandler::notSupported)
                    }

                }
                "/validateToken".nest {
                    POST("", handler::handleValidatePhoneNumberAndToken)
                    GET("", otherHandler::notSupported)
                    "/{id}".nest {
                        GET("", otherHandler::notSupported)
                        PUT("", otherHandler::notSupported)
                    }

                }
                "/registerCompany".nest {
                    POST("", handler::handleRegisterCompany)
                    GET("", otherHandler::notSupported)
                    "/{id}".nest {
                        GET("", otherHandler::notSupported)
                        PUT("", otherHandler::notSupported)
                    }

                }
            }
        }

    }

    @Bean
    fun migrationMasterDataNgrxRoutes(handler: MasterDataHandler) = router {
        "/api/v1/migration/anonymous".nest {
            "/regions".nest {
                GET("", handler::regionsListing)
                POST("", handler::notSupported)
                "/{id}".nest {
                    GET("", handler::notSupported)
                    PUT("", handler::notSupported)
                }

            }
            "/county".nest {
                GET("", handler::countiesListing)
                POST("", handler::notSupported)
                "/{id}".nest {
                    GET("", handler::notSupported)
                    PUT("", handler::notSupported)
                }

            }
            "/towns".nest {
                GET("", handler::townsListing)
                POST("", handler::notSupported)
                "/{id}".nest {
                    GET("", handler::notSupported)
                    PUT("", handler::notSupported)
                }

            }
            "/businessLines".nest {
                GET("", handler::businessLinesListing)
                POST("", handler::notSupported)
                "/{id}".nest {
                    GET("", handler::notSupported)
                    PUT("", handler::notSupported)
                }

            }
            "/businessNatures".nest {
                GET("", handler::businessNaturesListing)
                POST("", handler::notSupported)
                "/{id}".nest {
                    GET("", handler::notSupported)
                    PUT("", handler::notSupported)
                }

            }
        }
    }
}