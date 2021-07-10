package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.*
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
            "/secure".nest {
                "/logout".nest {
                    POST("", handler::handleLogout)
                }
            }
            "anonymous".nest {
                "/sendTokenForUser".nest {
                    POST("", handler::handleSendTokenToThePhone)
                }
                "/validateTokenFromThePhone".nest {
                    POST("", handler::handleValidateTokenFromThePhone)
                }
                "/reset".nest {
                    POST("", handler::handleResetUserCredentials)
                }
            }
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
                    GET("/towns", handler::townsListingByCountyId)
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

    @Bean
    fun migrationQualityAssuranceRoutes(handler: QualityAssuranceHandler) = router {
        "/api/v1/migration/qa".nest {
            GET("/sections-list", handler::sectionListMigration)
            GET("/branch-list", handler::branchListMigration)
            GET("/standards-list", handler::standardsListMigration)
            "/permit".nest {
                GET("/list", handler::permitListMigration)
                GET("/firm-list", handler::firmPermitListMigration)
                "/apply".nest {
                    POST("/sta1", handler::permitApplySTA1Migration)
                    POST("/submit-application", handler::permitSubmitApplicationMigration)
                    POST("/sta3", handler::permitApplySTA3Migration)
                    "/sta10".nest {
                        POST("/firm_details", handler::permitApplySTA10FirmDetailsMigration)
                        POST("/personnel_details", handler::permitApplySTA10PersonnelMigration)
                        POST("/products_being_manufactured", handler::permitApplySTA10ProductsBeingManufacturedMigration)
                        POST("/raw_material", handler::permitApplySTA10RawMaterialsMigration)
                        POST("/machinery_plant", handler::permitApplySTA10MachineryAndPlantMigration)
                        POST("/manufacturing_process", handler::permitApplySTA10ManufacturingProcessMigration)
                    }
                    "/invoice".nest {
                        POST("/batch-invoice-create", handler::invoiceBatchSubmitMigration)
                        PUT("/batch-invoice-add", handler::invoiceBatchAddMigration)
                        PUT("/batch-invoice-remove", handler::invoiceBatchRemoveMigration)
                    }
                }
                "/view".nest {
                    GET("/details", handler::permitDetailsMigration)
                    GET("/sta3", handler::permitViewSTA3Migration)
                    GET("/invoice-permit", handler::permitViewInvoiceDetailsMigration)
                    "/sta10".nest {
                        GET("/firm-details", handler::permitViewSTA10FirmDetailsMigration)
                        GET("/personnel_details", handler::permitViewSTA10PersonnelMigration)
                        GET("/products-being-manufactured", handler::permitViewSTA10ProductsBeingManufacturedMigration)
                        GET("/raw-material", handler::permitViewSTA10RawMaterialsMigration)
                        GET("/machinery-plant", handler::permitViewSTA10MachineryAndPlantMigration)
                        GET("/manufacturing-process", handler::permitViewSTA10ManufacturingProcessMigration)
                    }
                    "/invoice".nest {
                        GET("/list", handler::invoiceListMigration)
                        GET("/list-no-batch-Id", handler::invoiceListNoBatchIDMigration)
                        GET("/batch-invoice-list", handler::invoiceBatchListMigration)

                    }
                }
                "/invoice".nest {
                    GET("/list", handler::invoiceListMigration)
                    GET("/list-no-batch-Id", handler::invoiceListNoBatchIDMigration)
                    GET("/batch-invoice-list", handler::invoiceBatchListMigration)

                }
                "/attach".nest {
                    POST("/ordinary", handler::permitAttachUploadOrdinaryMigration)
                }
            }
        }
    }
}