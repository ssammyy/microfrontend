package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.*
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoFluxService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router


@Configuration
class AngularRoutes(private val daoService: DaoFluxService) {

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
                    GET("/fetch/user-type/{userId}", handler::listUserType)

                    GET("/fetch/user-section/{userId}/{status}", handler::listActiveRbacUserSections)
                    GET("/fetch/user-cfs/{userProfileId}/{status}", handler::listActiveRbacUserCfs)
                    POST("/role/revoke/{userId}/{roleId}/{status}", handler::revokeRoleFromUser)
                    POST("/role/assign/{userId}/{roleId}/{status}", handler::assignRoleToUser)
                    POST("/section/revoke/{userId}/{sectionId}/{status}", handler::revokeSectionFromUser)
                    POST("/section/assign/{userId}/{sectionId}/{status}", handler::assignSectionToUser)
                    POST("/cfs/revoke/{userProfileId}/{cfsId}/{status}", handler::revokeCfsFromUser)
                    POST("/cfs/assign/{userProfileId}/{cfsId}/{status}", handler::assignCfsToUser)
                    POST(
                        "/user/request/role/assign/{userId}/{roleId}/{status}/{requestID}",
                        handler::assignRoleToUserThroughRequest
                    )
                    POST("/userType/assign/{userId}/{roleId}/{status}", handler::assignUserTypeToUser)
                    POST("/userType/revoke/{userId}/{roleId}/{status}", handler::revokeUserTypeToUser)

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
                "/firm-types".nest {
                    GET("/load", handler::firmTypeListing)
                    GET("/loads/{status}", handler::firmTypeListing)
                    PUT("/", handler::notSupported)
                    POST("/", handler::notSupported)
                }
                "/company-list".nest {
                    GET("/load", handler::companyListing)
                    GET("/loads/{status}", handler::companyListing)
                    PUT("/", handler::notSupported)
                    POST("/updateTivet", handler::tivetUpdate)
                    POST("/rejectTivet", handler::tivetReject)

                    GET("/tivetListing", handler::tivetListing)
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
                "/sidebar".nest {
                    GET("", handler::handleGetSideBarMenusBasedOnLoggedInUser)
                    POST("") {
                        ServerResponse.badRequest().body("Invalid Request: Feature currently not supported")
                    }
                    "/{id}".nest {
                        PUT("") { ServerResponse.badRequest().body("Invalid Request: Feature currently not supported") }
                        GET("") { ServerResponse.badRequest().body("Invalid Request: Feature currently not supported") }
                    }

                }
                "/companyDetails".nest {
                    POST("", handler::handleProvideCompanyDetailsForUser)
                }
                "/user".nest {
                    GET("/notifications", handler::notificationList)
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
                POST("/update-turn-over", handler::handleUpdateCompanyTurnOverDetails)
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
    fun migrationRegistrationRoutes(handler: RegistrationHandler, otherHandler: MasterDataHandler, msHandler: NewMarketSurveillanceHandler
    ) = router {
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
                "/resetPasswordValidateToken".nest {
                    POST("", handler::resetPasswordHandleValidatePhoneNumberAndToken)
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
                "/registerTivet".nest {
                    POST("", handler::handleRegisterTivet)
                    GET("", otherHandler::notSupported)
                    "/{id}".nest {
                        GET("", otherHandler::notSupported)
                        PUT("", otherHandler::notSupported)
                    }

                }
                "/complaint".nest {
                    POST("/new", msHandler::saveNewComplaint)
                }
            }
            "validateToken".nest {
                POST("", handler::handleValidatePhoneNumberAndTokenSecure)
                GET("", otherHandler::notSupported)
                "/{id}".nest {
                    GET("", otherHandler::notSupported)
                    PUT("", otherHandler::notSupported)
                }

            }
        }

    }

    @Bean
    fun migrationUserAdminNgrxRoutes(handler: SystemsAdministrationHandler) = router {
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
            GET("/permit-qrcode/details", handler::permitQRCodeScanned)
        }
    }

    @Bean
    fun migrationQualityAssuranceRoutes(handler: QualityAssuranceHandler,internalUserhandler: QualityAssuranceInternalUserHandler) = router {
        "/api/v1/migration/qa".nest {
            GET("/sections-list", handler::sectionListMigration)
            GET("/branch-list", handler::branchListMigration)
            GET("/standards-list", handler::standardsListMigration)
            GET("/payments", handler::permitInvoiceListPaid)
            "/company".nest {
                GET("/un-payed-invoices", handler::permitInvoiceListUnPaid)
                GET("/approval-request-edit", handler::companyGetApprovalRequest)
                POST("/update-turn-over", handler::handleUpdateCompanyTurnOverDetails)
                POST("/generate-inspection-fee", handler::handleGenerateInspectionFeesDetails)
            }
            "/permit".nest {
                POST("/mpesa/stk-push", handler::permitMPesaPushStk)
                GET("/task-list", handler::permitTaskListMigration)
                GET("/list", handler::permitListMigration)
                GET("/smark-clone-list", handler::permitListMigrationSmark)
                GET("/dmark-clone-list", handler::permitListMigrationDmark)

                GET("/awarded-list", handler::permitListAwardedMigration)
                GET("/my-permits-loaded", handler::permitListAwardedMigrationb)
                GET("/my-permits-loaded-dmark", handler::permitListAwardedMigrationDmark)
                GET("/my-permits-loaded-fmark", handler::permitListAwardedMigrationFmark)

                GET("/all-my-permits-loaded", handler::loadAllMyPermits)
                GET("/awarded-list-completely", handler::permitCompletelyListAwardedMigration)
                POST("/delete", handler::deleteAPermit)

                GET("/awarded-list-fmark-generate", handler::permitListAwardedGenerateFmarkMigration)
                GET("/awarded-list-fmark-generated", handler::permitListAwardedGenerateFmarkMigrationAllPaidSmark)

                GET("/firm-list", handler::firmPermitListMigration)
                GET("/firm-branch-list", handler::firmBranchPermitListMigration)
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
                    POST("/updateMigratedPermit", handler::updatePermitMigrated)
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
                        POST("/batch-invoice-add", handler::notSupported)
                        PUT("/batch-invoice-remove", handler::notSupported)
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
                    GET("/list-no-batch-Id-permit-type", handler::invoiceListNoBatchIDByPermitTypeMigration)
                    GET("/batch-invoice-list", handler::invoiceBatchListMigration)

                }
                "/renew".nest {
                    POST("/", handler::permitRenewMigration)
                }
                "/attach".nest {
                    GET("/ordinary-file-list", handler::permitAttachGetOrdinaryFilesListMigration)
                    POST("/ordinary", handler::permitAttachUploadOrdinaryMigration)
                }

                "reports".nest {
                    GET("/allPermitsWithNoFmarkGenerated", handler::loadAllPermitsForReports)
                    GET("/allPermitsAwarded", handler::loadAllAwardedPermitsForReports)
                    GET("/allPermitsRenewed", handler::loadAllRenewedPermitsForReports)
                    GET("/allSamplesSubmitted", handler::loadAllSamplesSubmittedForReports)
                    GET("/allDejectedPermits", handler::loadAllDejectedPermitsForReports)
                    GET("/allOfficers", handler::loadAllOfficersForReports)
                    GET("/allStatuses", handler::loadAllStatusesForReports)
                    POST("/filter", handler::filterAllApplicationsReports)
                    POST("/filterAwarded", handler::filterAllAAwardedPermitsReports)
                    POST("/filterRenewed", handler::filterAllRenewedApplicationsReports)
                    POST("/filterDejected", handler::filterAllDejectedApplicationsReports)


                }

            }

            "internal-users".nest{
                "/view".nest {
                    GET("/permits-list", internalUserhandler::getAllMyTaskList)
                    GET("/permit-detail", internalUserhandler::getPermitDetails)
                }
                "/apply".nest {
                    "/permit".nest {
                        POST("/section", internalUserhandler::updatePermitDetailsSection)
                        POST("/completeness", internalUserhandler::updatePermitDetailsCompleteness)
                        POST("/assign-officer", internalUserhandler::updatePermitDetailsAssignOfficer)
                    }
                GET("/permit-detail", internalUserhandler::getPermitDetails)
            }
            }

        }
    }

    @Bean
    fun migrationMarketSurveillanceRoutes(handler: NewMarketSurveillanceHandler) = router {
        "/api/v1/migration/ms".nest {
            "/dashboard".nest {
                GET("/recommendation-list", handler::msRecommendationList)
                GET("/notification-list", handler::msNotificationTaskList)
                PUT("/notification-read", handler::msNotificationTaskRead)
            }
            "/common".nest {
                GET("/officer-list", handler::msOfficerListDetails)
                GET("/search-permit-number", handler::msSearchPermitNumberDetails)
                GET("/search-ucr-number", handler::msSearchUCRNumberDetails)
                GET("/dashboard", handler::msDashBoardDetails)
                GET("/towns", handler::townsListingAdmin)
                GET("/regions", handler::regionListingAdmin)
                GET("/counties", handler::countiesListingAdmin)
                GET("/departments", handler::msDepartments)
                GET("/divisions", handler::msDivisions)
                GET("/laboratories", handler::msLaboratories)
                GET("/standards", handler::standardsList)
                GET("/standardProductCategory", handler::msStandardsCategory)
                GET("/predefinedResourcesRequired", handler::msPredefinedResources)
                GET("/ogaList", handler::msOGAList)
                GET("/productCategories", handler::msProductCategories)
                GET("/countries", handler::msCountries)
                GET("/broadProductCategory", handler::msBroadProductCategory)
                GET("/products", handler::msProducts)
                GET("/productSubcategory", handler::msProductSubcategory)
                GET("/recommendation-list", handler::msRecommendationList)
                GET("/notification-list", handler::msNotificationTaskList)
                PUT("/notification-read", handler::msNotificationTaskRead)
            }
            "/complaint".nest {
                GET("/list", handler::getAllComplaintList)
                GET("/list-completed", handler::getAllComplaintCompletedList)
                GET("/list-new", handler::getAllComplaintNewList)
                GET("/list-on-going", handler::getAllComplaintOnGoingList)
                GET("/list-my-task", handler::getAllComplaintMyTaskList)
                GET("/allocated-task-view", handler::getAllComplaintAllocatedTaskList)
                GET("/pending-allocation-view", handler::getAllComplaintPendingAllocationList)
                GET("/allocated-task-overDue-view", handler::getAllComplaintAllocatedOverDueTaskList)
                GET("/details", handler::getComplaintDetails)
                "/update".nest {
                    PUT("/accept", handler::updateComplaintByAccepting)
                    PUT("/reject", handler::updateComplaintByRejecting)
                    PUT("/advice-where", handler::updateComplaintByMandatedForOga)
                    PUT("/reject-for-amendment", handler::updateComplaintByForAmendmentUser)
                    PUT("/assign-hof", handler::updateComplaintByAssigningHof)
                    PUT("/re-assign-region", handler::updateComplaintByReAssigningRegion)
                    PUT("/assign-io", handler::updateComplaintByAssigningIO)
                    PUT("/add-classification-details", handler::updateComplaintByAddingClassificationDetails)
                    PUT("/start-ms-process", handler::updateComplaintByAddingClassificationDetails)
                }
                POST("/add/complaint-work-plan", handler::addComplaintToWorkPlanDetails)
                "/reports".nest {
                    PUT("/complaint-search", handler::putAllComplaintSearchList)
                    PUT("/complaint-search", handler::putAllComplaintSearchList)
                    PUT("/sample-products-search", handler::putAllSampleProductsSearchList)
                    PUT("/seized-goods-search", handler::putAllSeizedGoodsSearchList)
                    GET("/seized-goods", handler::getAllSeizedGoodsViewList)
                    "/seized-goods".nest {
                        GET("/view-all", handler::getAllSeizedGoodsReportList)
                        PUT("/search", handler::putAllSeizedGoodsViewSearchList)
                    }
                    "/consumer-complaint".nest {
                        GET("/view-all", handler::getAllConsumerComplaintReportList)
                        PUT("/search", handler::putAllConsumerComplaintSearchList)
                    }
                    "/submitted-samples-summary".nest {
                        GET("/view-all", handler::getAllSubmittedSamplesSummaryReportList)
                        PUT("/search", handler::putAllSubmittedSamplesSummarySearchList)
                    }
                    "/field-inspection-summary".nest {
                        GET("/view-all", handler::getAllFieldInspectionSummaryReportList)
                        PUT("/search", handler::putAllFieldInspectionSummarySearchList)
                    }
                    "/work-plan-monitoring-tool".nest {
                        GET("/view-all", handler::getAllWorkPlanMonitoringToolReportList)
                        PUT("/search", handler::putAllWorkPlanMonitoringToolSearchList)
                    }
                    "/statusReport".nest {
                        GET("/complaint-investigation", handler::getStatusReportComplaintInvestigationList)
                        GET("/performance-selected-product", handler::getPerformanceOfSelectedProductViewList)
                    }

                }
            }
            "/workPlan".nest {
                GET("/allocated-task-wp-view", handler::getAllWorkPlanAllocatedTaskList)
                GET("/report-pending-review-wp-view", handler::getAllWorkPlanReportsPendingReviewList)
                GET("/junior-task-overDue-wp-view", handler::getAllWorkPlanJuniorTaskOverdueWPReviewList)
                GET("/junior-task-overDue-wp-cp-view", handler::getAllWorkPlanJuniorTaskOverdueWPCPReviewList)
                GET("/report-pending-review-wp-cp-view", handler::getAllWorkPlanComplaintReportsPendingReviewList)
                GET("/allocated-task-wp-cp-view", handler::getAllWorkPlanComplaintAllocatedTaskList)
                GET("/allocated-task-overDue-wp-view", handler::getAllWorkPlanAllocatedTaskOverDueList)
                GET("/allocated-task-overDue-wp-cp-view", handler::getAllWorkPlanComplaintAllocatedTaskOverDueList)
                GET("/all-batch-list", handler::getAllWorkPlanBatchList)
                GET("/all-batch-closed", handler::getAllWorkPlanBatchListClosed)
                GET("/all-batch-open", handler::getAllWorkPlanBatchListOpen)
                POST("/add", handler::saveNewWorkPlanBatch)
                PUT("/close", handler::closeWorkPlanBatchEntry)
                "/inspection".nest {
                    GET("/list", handler::getAllWorkPlanList)
                    GET("/list-completed", handler::getAllWorkPlanCompletedList)
//                    GET("/list-new", handler::getAllWorkPlanNewList)
                    GET("/list-on-going", handler::getAllWorkPlanOnGoingList)
                    GET("/list-my-task", handler::getAllWorkPlanMyTaskList)
                    POST("/new", handler::saveNewWorkPlanSchedule)
                    PUT("/update", handler::updateWorkPlanSchedule)
                    GET("/details", handler::getWorkPlanInspectionDetails)
                    "/update".nest {
                        PUT("/submit-for-approval", handler::submitWorkPlanScheduleEntry)
                        PUT("/approval-schedule", handler::updateWorkPlanScheduleApproval)
                        POST("/start-onsite-activities", handler::startWorkPlanInspectionOnsiteDetails)
                        GET("/end-onsite-activities", handler::endWorkPlanInspectionOnsiteDetails)
                        PUT("/end-all-recommendation-done", handler::endWorkPlanInspectionAllRecommendationDone)
                        PUT("/client-appealed-status", handler::updateWorkPlanClientAppealed)
                        PUT("/client-appealed-successfully", handler::updateWorkPlanClientAppealSuccessful)
                        PUT("/final-remarks-seized", handler::updateWorkPlanScheduleFinalRemarkOnSized)
                        "/hof".nest {
                            PUT("/assign-io", handler::updateWorkPlanByAssigningIO)
                            PUT(
                                "/approval-preliminary-report",
                                handler::updateWorkPlanScheduleApprovalPreliminaryReportHOF
                            )
                            PUT(
                                "/approval-final-preliminary-report",
                                handler::updateWorkPlanScheduleApprovalPreliminaryReportHOF
                            )
                        }
                        "/hod".nest {
                            PUT("/assign-hof", handler::updateWorkPlanByAssigningHof)
                            PUT(
                                "/approval-preliminary-report",
                                handler::updateWorkPlanScheduleApprovalPreliminaryReportHOD
                            )
                            PUT(
                                "/approval-final-preliminary-report",
                                handler::updateWorkPlanScheduleApprovalPreliminaryReportHOD
                            )
                            PUT("/final-recommendation", handler::addWorkPlanScheduleFinalRecommendationByHOD)
                            PUT(
                                "/end-adding-final-recommendation",
                                handler::addWorkPlanScheduleEndFinalRecommendationAddingByHOD
                            )
                            PUT("/feedBack-notification", handler::addWorkPlanScheduleFeedBackByHOD)
                        }
                        "/director".nest {
                            PUT("/recommendation", handler::addWorkPlanScheduleFeedBackByDirector)
                            PUT(
                                "/end-recommendation",
                                handler::addWorkPlanScheduleEndFinalRecommendationAddingByDirector
                            )
                            PUT(
                                "/approval-final-preliminary-report",
                                handler::updateWorkPlanScheduleApprovalPreliminaryReportDirector
                            )
                        }
                    }
                    "/add".nest {
                        POST("/charge-sheet", handler::addWorkPlanScheduleChargeSheet)
//                        POST("/data-report", handler::addWorkPlanDataReportSheet)
                        POST("/end-data-report", handler::endAddingWorkPlanDataReportSheet)
//                        POST("/seizure-declaration", handler::addWorkPlanSeizureDeclaration)
                        POST("/end-seizure-declaration", handler::addWorkPlanEndSeizureDeclaration)
                        POST("/inspection-investigation", handler::addWorkPlanInspectionInvestigationReport)
                        POST("/sample-collect", handler::addWorkPlanScheduleSampleCollection)
                        POST("/sample-submission", handler::addWorkPlanScheduleSampleSubmission)
                        POST("/end-sample-submission", handler::addWorkPlanScheduleEndSampleSubmission)
                        PUT("/sample-submission-bs-number", handler::addWorkPlanScheduleSampleSubmissionBsNumber)
                        PUT("/end-sample-submission-bs-number", handler::addWorkPlanScheduleSampleSubmissionEndBsNumber)
                        PUT("/lab-results-pdf-save", handler::saveWorkPlanScheduleLabResultsPDFSelected)
                        PUT("/ssf-compliance-status-save", handler::saveWorkPlanScheduleSSFComplianceStatusAdd)
                        PUT("/ssf-send-result-saved", handler::saveWorkPlanScheduleSSFComplianceStatusSend)
                        PUT(
                            "/final-ssf-compliance-status-save",
                            handler::saveWorkPlanScheduleFinalSSFComplianceStatusAdd
                        )
                        POST("/preliminary-report", handler::addWorkPlanSchedulePreliminaryReport)
                        POST(
                            "/preliminary-report-hod-hof-director",
                            handler::addWorkPlanSchedulePreliminaryReportHofHodDirector
                        )
//                        PUT("/preliminary-report", handler::addWorkPlanSchedulePreliminaryReport)
//                        POST("/final-report", handler::addWorkPlanScheduleFinalPreliminaryReport)
                    }
                }


            }
            "/fuel".nest {
                GET("/all-batch-list", handler::getAllFuelBatchList)
                POST("/add", handler::saveNewFuelScheduleBatch)
                PUT("/close", handler::closeFuelBatchEntry)
                "teams".nest {
                    POST("/add", handler::saveNewFuelScheduleTeam)
                    GET("/list", handler::getFuelBatchTeamsList)
                    "county".nest {
                        GET("/list", handler::getFuelInspectionTeamsDetails)
                    }
                }
                "/inspection".nest {
                    GET("/list", handler::getAllFuelInspectionList)
                    POST("/add", handler::saveNewFuelSchedule)
                    GET("/details", handler::getFuelInspectionDetails)
                    "/update".nest {
                        PUT("/assign", handler::updateFuelScheduleAssignOfficer)
                        PUT("/rapid-test", handler::setFuelScheduleRapidTest)
                        POST("/rapid-test-products", handler::addFuelScheduleRapidTestProducts)
                        POST("/sample-collect", handler::setFuelScheduleSampleCollection)
                        POST("/sample-submission", handler::setFuelScheduleSampleSubmission)
                        POST("/end-ssf-adding", handler::endsSampleSubmissionAdding)
                        PUT("/sample-submission-bs-number", handler::setFuelScheduleSampleSubmissionBsNumber)
                        POST("/end-ssf-adding-bs-number", handler::setFuelScheduleEndSampleSubmissionBsNumber)
                        PUT("/lab-results-pdf-save", handler::saveFuelScheduleLabResultsPDFSelected)
                        PUT("/ssf-compliance-status-save", handler::saveFuelScheduleSSFComplianceStatusAdd)
                        PUT("/ssf-compliance-final-status-save", handler::saveFuelScheduleSSFFinalComplianceStatusAdd)
                        POST("/fuel-remediation-schedule", handler::saveFuelScheduleRemediationAfterPayment)
                        POST("/fuel-remediation-invoice", handler::saveFuelScheduledRemediationInvoice)
                        PUT("/fuel-remediation", handler::saveFuelScheduleUpdateRemediation)
                        PUT("/end-inspection", handler::endFuelInspection)
                    }
                    "/fetch".nest {
                        GET("/assign", handler::updateFuelScheduleAssignOfficer)
                        GET("/laboratory-list", handler::getAllLaboratoryList)
//                        POST("/rapid-test", handler::setFuelScheduleRapidTest)
//                        POST("/sample-collect", handler::setFuelScheduleSampleCollection)
//                        POST("/sample-submission", handler::setFuelScheduleSampleSubmission)
//                        POST("/sample-submission-bs-number", handler::setFuelScheduleSampleSubmissionBsNumber)
                    }

                }


            }
//            GET("/background/smark/image", handler::smarkBackGroundImage)
        }
    }

    @Bean
    fun kraApiRoutes(handler: StandardsLevyHandler) = router {
        "/api/v1/kra".nest {
            POST("/receiveSL2Payment".and(contentType(MediaType.APPLICATION_JSON)), handler::processReceiveSL2Payment)
            //POST("/receiveSL2Payment", handler::processReceiveSL2Payment)

        }
    }


    @Bean
    fun KebsWebsiteApiRoutes(handler: QualityAssuranceHandler) = router {
        "/api/v1/migration/anonymous/kebsWebsite".nest {
            GET("/getAwardedSmarkPermits", handler::loadAllSmarksAwardedPermitsForReportsApi)
            GET("/getAwardedFmarkPermits", handler::loadAllFmarksAwardedPermitsForReportsApi)
            GET("/getAwardedDmarkPermits", handler::loadAllDmarksAwardedPermitsForReportsApi)
            GET("/getAllAwardedPermits", handler::getAllAwardedPermitsByPermitNumber)
            GET("/getAllAwardedPermitsByCompanyName", handler::getAllAwardedPermitsByCompanyName)
            GET("/getAllCompanies", handler::getAllCompanies)
//          GET("/getAllAwardedPermitsByPermitNumberSms", handler::getAllAwardedPermitsByPermitNumberSms)
            //POST("/receiveSL2Payment", handler::processReceiveSL2Payment)
            POST("/getAllAwardedPermitsByPermitNumberSmsRequest", handler::processReceiveMessageBody)


        }
    }

}
