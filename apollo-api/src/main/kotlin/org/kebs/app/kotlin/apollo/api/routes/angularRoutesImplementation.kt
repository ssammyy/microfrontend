package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router


@Configuration
class AngularRoutes {

    @Bean
    fun systemsAdministrationMigrationRoutes(handler: SystemsAdministrationHandler) = router {
        "/api/v1/migration/".nest {
            "/security".nest {
                "/rbac".nest {
                    GET("/fetch/roles/{status}", handler::listActiveRbacRoles)
                    GET("/fetch/authorities/{roleId}/{status}", handler::authoritiesByRoleAndStatusListing)
                    POST("/revoke/{roleId}/{privilegeId}/{status}", handler::revokeAuthorizationFromRole)
                    POST("/assign/{roleId}/{privilegeId}/{status}", handler::assignAuthorizationFromRole)
//                    GET("/fetch/users/{status}/{criteria}", handler::list)
                    GET("/fetch/users/{status}", handler::listActiveRbacUsers)
                    GET("/fetch/user-roles/{userId}/{status}", handler::listActiveRbacUserRoles)
                    GET("/fetch/user-cfs/{userProfileId}/{status}", handler::listActiveRbacUserCfs)
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
                    GET("/user-email-exist", handler::userSearchEmailExistsDetails)
                    GET("/user-username-exist", handler::userSearchUserNameExistsDetails)
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
    fun masterDataRoutesMigration(handler: MasterDataHandler) = router {
        "/api/v1/migration/system/admin".nest {
            "/masters".nest {
                "/ui".nest {
                    GET("/designations", handler::designationsUi)
                    GET("/directorates", handler::directoratesUi)
                    GET("/departments", handler::departmentsUi)
                    GET("/divisions", handler::divisionsUi)
                    GET("/sections", handler::sectionsUi)
                    GET("/sub-sections-l1", handler::subSectionsL1Ui)
                    GET("/sub-sections-l2", handler::subSectionsL2Ui)
                    GET("/regions", handler::regionsUi)
                    GET("/sub-regions", handler::subRegionsUi)
                    GET("/counties", handler::countiesUi)
                    GET("/towns", handler::townsUi)
                }
                "/designations".nest {
                    GET("/load", handler::designationsListing)
                    GET("/loads/{status}", handler::designationsListing)
                    PUT("/", handler::designationsUpdate)
                    POST("/", handler::designationsUpdate)
                }
                "/departments".nest {
                    GET("/load", handler::departmentsListing)
                    GET("/loads/{status}", handler::departmentsListing)
                    PUT("/", handler::departmentsUpdate)
                    POST("/", handler::departmentsUpdate)
                }
                "/divisions".nest {
                    GET("/load", handler::divisionsListing)
                    GET("/loads/{status}", handler::divisionsListing)
                    PUT("/", handler::divisionsUpdate)
                    POST("/", handler::divisionsUpdate)
                }
                "/directorate".nest {
                    GET("/load", handler::directoratesListing)
                    GET("/loads/{status}", handler::directoratesListing)
                    PUT("/", handler::directoratesUpdate)
                    POST("/", handler::directoratesUpdate)
                }
                "/regions".nest {
                    GET("/load", handler::regionsListing)
                    GET("/loads/{status}", handler::regionsListing)
                    PUT("/", handler::regionsUpdate)
                    POST("/", handler::regionsUpdate)
                }
                "/subRegions".nest {
                    GET("/load", handler::subRegionsListing)
                    GET("/loads/{status}", handler::subRegionsListing)
                    PUT("/", handler::subRegionsUpdate)
                    POST("/", handler::subRegionsUpdate)
                }
                "/sections".nest {
                    GET("/load", handler::sectionsListing)
                    GET("/loads/{status}", handler::sectionsListing)
                    PUT("/", handler::sectionsUpdate)
                    POST("/", handler::sectionsUpdate)
                }
                "/subsections".nest {
                    "/l1".nest {
                        GET("/load", handler::subSectionsL1Listing)
                        GET("/loads/{status}", handler::subSectionsL1Listing)
                        PUT("/", handler::subSectionsL1Update)
                        POST("/", handler::subSectionsL1Update)

                    }
                    "/l2".nest {
                        GET("/load", handler::subSectionsL2Listing)
                        GET("/loads/{status}", handler::subSectionsL2Listing)
                        PUT("/", handler::subSectionsL2Update)
                        POST("/", handler::subSectionsL2Update)
                    }

                }
                "/freightStations".nest {
                    GET("/load", handler::cfsListing)
                    GET("/loads/{status}", handler::cfsListing)
//                    PUT("/", handler::townsUpdate)
//                    POST("/", handler::townsUpdate)
                }
                "/counties".nest {
                    GET("/load", handler::countiesListingAdmin)
                    GET("/loads/{status}", handler::countiesListingAdmin)
                    PUT("/", handler::countiesUpdate)
                    POST("/", handler::countiesUpdate)
                }
                "/towns".nest {
                    GET("/load", handler::townsListingAdmin)
                    GET("/loads/{status}", handler::townsListingAdmin)
                    PUT("/", handler::townsUpdate)
                    POST("/", handler::townsUpdate)
                }
                "/businessLines".nest {
                    GET("/load", handler::businessLinesListing)
                    POST("", handler::notSupported)
                    "/{id}".nest {
                        GET("", handler::notSupported)
                        PUT("", handler::notSupported)
                    }

                }
                "/businessNatures".nest {
                    GET("/load", handler::businessNaturesListing)
                    POST("", handler::notSupported)
                    "/{id}".nest {
                        GET("/load", handler::notSupported)
                        PUT("", handler::notSupported)
                    }

                }
                "/regionCountyTown".nest {
                    GET("/load", handler::regionCountyTownListing)

                }
                "/regionSubRegion".nest {
                    GET("/load", handler::regionSubRegionListing)

                }
                "/directorateDesignations".nest {
                    GET("/load", handler::getDirectorateDesignationsViewDtoListing)

                }
                "/directorateToSubSectionL2".nest {
                    GET("/load", handler::getDirectorateToSubSectionL2ViewDtoListing)

                }
                "/standardProductCategory".nest {
                    GET("/load", handler::standardProductCategoryListing)
                    GET("/loads/{status}", handler::standardProductCategoryListing)
                    PUT("/", handler::standardProductCategoryUpdate)
                    POST("/", handler::standardProductCategoryUpdate)
                }
                "/userRequestType".nest {
                    GET("/load", handler::userRequestTypeListing)
                    GET("/loads/{status}", handler::userRequestTypeListing)
                    PUT("/", handler::userRequestTypeUpdate)
                    POST("/", handler::userRequestTypeUpdate)
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
                "/companyDetails".nest {
                    POST("", handler::handleProvideCompanyDetailsForUser)
                }
                "/user".nest {
                    "/details".nest {
                        GET("") { ServerResponse.badRequest().body("Invalid Request: Feature currently not supported") }
                        POST("") {
                            ServerResponse.badRequest().body("Invalid Request: Feature currently not supported")
                        }
                        "/{userId}".nest {
                            PUT("", handler::handleUpdateLoggedInUserUserEntityDtoDetails)
                            GET("", handler::handleLoggedInUserUserEntityDtoDetails)
                        }
                    }
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
    fun migrationUserAdminNgrxRoutes(handler: SystemsAdministrationHandler)= router{
        "/api/v1/migration/anonymous".nest {
            "/titles".nest {
                GET("", handler::titlesListing)
                POST("") { ServerResponse.badRequest().body("Invalid Request: Feature currently not supported") }
                "/{id}".nest {
                    GET("") { ServerResponse.badRequest().body("Invalid Request: Feature currently not supported") }
                    PUT("") { ServerResponse.badRequest().body("Invalid Request: Feature currently not supported") }
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
    fun ApiBackGroundImageRoute(handler: ImagesHandlers) = router {
        "/api/v1/migration/anonymous".nest {
            GET("/background/smark/image", handler::smarkBackGroundImage)
            GET("/background/dmark/image", handler::dmarkBackGroundImage)
            GET("/background/fmark/image", handler::fmarkBackGroundImage)
        }
    }

    @Bean
    fun migrationQualityAssuranceRoutes(handler: QualityAssuranceHandler) = router {
        "/api/v1/migration/qa".nest {
            GET("/sections-list", handler::sectionListMigration)
            GET("/branch-list", handler::branchListMigration)
            GET("/standards-list", handler::standardsListMigration)
            "/permit".nest {
                POST("/mpesa/stk-push", handler::permitMPesaPushStk)
                GET("/task-list", handler::permitTaskListMigration)
                GET("/list", handler::permitListMigration)
                GET("/awarded-list", handler::permitListAwardedMigration)
                GET("/firm-list", handler::firmPermitListMigration)
                "/apply".nest {
                    POST("/fmark", handler::permitFMARKGenerateMigration)
                    POST("/sta1", handler::permitApplySTA1Migration)
                    POST("/process-step-add", handler::permitProcessStepMigration)
                    PUT("/sta1-update", handler::permitUpdateSTA1Migration)
                    POST("/submit-application", handler::permitSubmitApplicationInvoiceMigration)
                    POST("/submit-application-review", handler::permitSubmitApplicationReviewMigration)
                    POST("/submit-application-qam-hod-review", handler::permitSubmitApplicationQAMHODReviewMigration)
                    POST("/submit-application-ssc-approval-rejection", handler::permitApproveRejectSSCMigration)
                    POST("/re-submit-application", handler::permitResubmitMigration)
                    POST("/sta3", handler::permitApplySTA3Migration)
                    PUT("/sta3-update", handler::permitUpdateSTA3Migration)
//                    POST(
//                        "/sta3-update-upload".and(contentType(MediaType.MULTIPART_FORM_DATA)),
//                        handler::permitUploadSTA3Migration
//                    )
                    "/sta10".nest {
                        POST("/firm_details", handler::permitApplySTA10FirmDetailsMigration)
                        PUT("/firm_details_update", handler::permitUpdateSTA10FirmDetailsMigration)
                        POST("/personnel_details", handler::permitApplySTA10PersonnelMigration)
                        PUT("/personnel_details_update", handler::permitApplySTA10PersonnelMigration)
                        POST(
                            "/products_being_manufactured",
                            handler::permitApplySTA10ProductsBeingManufacturedMigration
                        )
                        PUT(
                            "/products_being_manufactured_update",
                            handler::permitApplySTA10ProductsBeingManufacturedMigration
                        )
                        POST("/raw_material", handler::permitApplySTA10RawMaterialsMigration)
                        PUT("/raw_material_update", handler::permitApplySTA10RawMaterialsMigration)
                        POST("/machinery_plant", handler::permitApplySTA10MachineryAndPlantMigration)
                        PUT("/machinery_plant_update", handler::permitApplySTA10MachineryAndPlantMigration)
                        POST("/manufacturing_process", handler::permitApplySTA10ManufacturingProcessMigration)
                        PUT("/manufacturing_process_update", handler::permitApplySTA10ManufacturingProcessMigration)
                    }
                    "/invoice".nest {
                        POST("/batch-invoice-submit", handler::invoiceBatchSubmitMigration)
                        POST("/batch-invoice-add", handler::invoiceBatchAddMigration)
                        PUT("/batch-invoice-remove", handler::invoiceBatchRemoveMigration)
                    }
                }
                "/view".nest {
                    GET("/details", handler::permitDetailsMigration)
                    GET("/certificate-issued-details-pdf", handler::certificateIssuedDetailsPDFMigration)
                    GET("/sta1", handler::permitViewSTA1Migration)
                    GET("/sta3", handler::permitViewSTA3Migration)
                    GET("/invoice-permit", handler::permitViewInvoiceDetailsMigration)
                    "/sta10".nest {
                        GET("/view-details", handler::permitViewSTA10AllDetailsMigration)
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
                        GET("/batch-invoice-details", handler::invoiceBatchDetailsMigration)
                        GET("/batch-invoice-balance-details", handler::invoiceBatchDetailsBalanceMigration)
                        GET("/batch-invoice-pdf-details", handler::invoiceBatchDetailsPDFMigration)

                    }
                }
                "/invoice".nest {
                    GET("/list", handler::invoiceListMigration)
                    GET("/list-no-batch-Id", handler::invoiceListNoBatchIDMigration)
                    GET("/batch-invoice-list", handler::invoiceBatchListMigration)

                }
                "/renew".nest {
                    POST("/", handler::permitRenewMigration)
                }
                "/attach".nest {
                    GET("/ordinary-file-list", handler::permitAttachGetOrdinaryFilesListMigration)
                    POST("/ordinary", handler::permitAttachUploadOrdinaryMigration)
                }
            }

        }
    }
}
