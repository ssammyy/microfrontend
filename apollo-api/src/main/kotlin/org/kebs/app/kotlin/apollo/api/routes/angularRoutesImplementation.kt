package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.MasterDataHandler
import org.kebs.app.kotlin.apollo.api.handlers.RegistrationHandler
import org.kebs.app.kotlin.apollo.api.handlers.RegistrationManagementHandler
import org.kebs.app.kotlin.apollo.api.handlers.SystemsAdministrationHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.router


@Configuration
class AngularRoutes {

    @Bean
    fun systemsAdministrationMigrationRoutes(handler: SystemsAdministrationHandler) = router {
        "/api/v1/migration/".nest {
            GET("/home", handler::sysadminHome)
            "/ui".nest {
                GET("/rbac-user-roles", handler::rbacUserRoles)
                GET("/rbac-user-requests", handler::rbacUserRequests)
                GET("/rbac-role-authorities", handler::rbacRoleAuthorities)
                GET("/users-crud", handler::usersCrud)
                GET("/roles-crud", handler::rolesCrud)
                GET("/authorities-crud", handler::authoritiesCrud)
                GET("/titles-crud", handler::titlesCrud)
                GET("/userTypes-crud", handler::userTypesCrud)

            }

            GET("/", handler::sysadminHome)
//            GET("/rbacRolesTemplate", handler::sysadminHome)
            "/security".nest {
                "/rbac".nest {
                    GET("/fetch/roles/{status}", handler::listActiveRbacRoles)
                    GET("/fetch/authorities/{roleId}/{status}", handler::authoritiesByRoleAndStatusListing)
                    POST("/revoke/{roleId}/{privilegeId}/{status}", handler::revokeAuthorizationFromRole)
                    POST("/assign/{roleId}/{privilegeId}/{status}", handler::assignAuthorizationFromRole)
//                    GET("/fetch/users/{status}/{criteria}", handler::list)
                    GET("/fetch/users/{status}", handler::listActiveRbacUsers)
                    GET("/fetch/user-roles/{userId}/{status}", handler::listActiveRbacUserRoles)
                    POST("/role/revoke/{userId}/{roleId}/{status}", handler::revokeRoleFromUser)
                    POST("/role/assign/{userId}/{roleId}/{status}", handler::assignRoleToUser)
                    POST("/cfs/revoke/{userProfileId}/{cfsId}/{status}", handler::revokeCfsFromUser)
                    POST("/cfs/assign/{userProfileId}/{cfsId}/{status}", handler::assignCfsToUser)
                    POST(
                        "/user/request/role/assign/{userId}/{roleId}/{status}/{requestID}",
                        handler::assignRoleToUserThroughRequest
                    )
//                    POST("/user/request/{userId}/{cfsId}/{status}", handler::assignCfsToUser)


                }
                "/authorities".nest {
                    GET("/load", handler::authoritiesListing)
                    GET("/loads/{status}", handler::authoritiesListing)
                    PUT("/", handler::authorityUpdate)
                    POST("/", handler::authorityCreate)
                }
                "/roles".nest {
                    GET("/load", handler::roleListing)
                    GET("/loads/{status}", handler::roleListing)
                    PUT("/", handler::roleUpdate)
                    POST("/", handler::roleCreate)
                }
                "/titles".nest {
                    GET("/load", handler::titlesListing)
                    GET("/loads/{status}", handler::titlesListing)
                    PUT("/", handler::titleUpdate)
                    POST("/", handler::titleUpdate)
                }
                "/userTypes".nest {
                    GET("/load", handler::userTypeListing)
                    GET("/loads/{status}", handler::userTypeListing)
                    PUT("/", handler::userTypeUpdate)
                    POST("/", handler::userTypeUpdate)
                }
                "/users".nest {
//                    GET("/lists", handler::usersList)
//                    GET("/{id}", handler::userView)
//                    POST("/save", handler::userWriteView)
//                    GET("/new", handler::userNewFormView)
//                    GET("/list", handler::usersListView)
                    GET("/load", handler::usersListing)
                    GET("/load/users-requests", handler::listUserRequests)
                    GET("/user-details", handler::userDetails)
                    POST("/search", handler::usersSearchListing)
                    POST("/{userId}/user-request", handler::usersRequests)
                    POST("/{userId}/update/company-profile", handler::usersUpdateCompanyProfile)
                    PUT("/", handler::usersUpdate)
                    POST("/", handler::usersUpdate)
                }
            }

        }


    }

    @Bean
    fun organizationManagementRoutes(handler: RegistrationManagementHandler) = router {
        "/api/v1/migration/".nest {
            "/company".nest {
                GET("", handler::handleFetchCompaniesByUserId)
                POST("", handler::handleUpdateCompanyDetails)
                "/{companyId}".nest {
                    PUT("", handler::handleUpdateCompanyDetails)
                    GET("", handler::handleFetchCompanyById)
                    "/branches".nest {
                        GET("", handler::handleFetchBranchesByCompanyIdAndUserId)
                        POST("", handler::handleUpdatePlantEntity)
                        "/{branchId}".nest {
                            PUT("", handler::handleUpdatePlantEntity)
                            GET("", handler::handleFetchBranchesByIdAndUserId)
                            "/users".nest {
                                GET("", handler::handleFetchUsersByCompanyIdAndBranchIdAndUserId)
                                POST("", handler::handleUpdateBranchUsers)
                                "/{userId}".nest {
                                    PUT("", handler::handleUpdateBranchUsers)
                                    GET("", handler::handleFetchUserByIdAndUserId)
                                }
                            }
                        }
                    }
                    "/directors".nest {
                        GET("", handler::handleFetchDirectorsByCompanyIdAndUserId)
                        POST("", handler::handleUpdateProfileDirectors)
                        "/{directorId}".nest {
                            PUT("", handler::handleUpdateProfileDirectors)
                            GET("", handler::handleFetchDirectorsByIdAndUserId)

                        }
                    }
                }
            }
        }
    }

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