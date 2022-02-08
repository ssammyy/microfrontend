/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.*
import org.kebs.app.kotlin.apollo.api.handlers.pvoc.PvocComplaintHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Configuration
class ControllerRoutes {
    @Bean
    fun masterDataRoutes(handler: MasterDataHandler) = router {
        "/api/v1/system/admin".nest {
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
                    GET("/load", handler::countiesListing)
                    GET("/loads/{status}", handler::countiesListing)
                    PUT("/", handler::countiesUpdate)
                    POST("/", handler::countiesUpdate)
                }
                "/towns".nest {
                    GET("/load", handler::townsListing)
                    GET("/loads/{status}", handler::townsListing)
                    PUT("/", handler::townsUpdate)
                    POST("/", handler::townsUpdate)
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
    fun homeRoute(handler: AuthenticationHandler) = router {
        "/".nest {
            GET("/")(handler::home)
            GET("/index")(handler::home)
            POST("/ui/login", handler::uiLogin)
        }
    }

    @Bean
    fun ApiAuthRoute(handler: ApiAuthenticationHandler) = router {
        "/api/v1".nest {
            POST("/login", handler::uiLogin)
            POST("/login/b", handler::uiLogin2)
            POST("/otp", handler::generateOtp)
        }
    }

//     @Bean
//    fun ApiBackGroundImageRoute(handler: ImagesHandlers) = router {
//        "/api/v1".nest {
//            POST("/smark/backgroud/image", handler::smarkBackGroundImage)
//        }
//    }


    @Bean
    fun utilitiesRoute(handler: UtilitiesHandler) = router {
        "/api/utilities".nest {
            POST("/hash", handler::hashString)
        }
    }

    @Bean
    fun pvocWaivers(handler: PvocComplaintHandler) = router {
        "/".nest {
            GET("api/di/pvoc/waivers-application")(handler::waiversForm)
        }
    }


    @Bean
    fun tasksRoute(handler: BpmnTasksHandler) =
            router {
                "/api/user/tasks".nest {
                    GET(pattern = "/list", f = handler::tasksListView)
                    GET(pattern = "/task/{taskId}", f = handler::reviewTask)
                    POST(pattern = "/task/{taskId}/claim", f = handler::claimTask)
                    POST(pattern = "/task/{taskId}/complete/{taskStatus}", f = handler::completeTask)
                }
            }

    @Bean
    fun userRoute(handler: UserHandler) = router {
        "/api/v1/user".nest {
            GET("/notifications", handler::notificationList)
            GET("/profile", handler::userProfile)
            GET("/blacklist/types",handler::loadBlacklistTypes)
            GET("/officers/{cdUuid}", handler::listInspectionOfficers)
            GET("/in-my-station/{designation}", handler::listOfficersByDesignation)
            GET("/add/plant-details/save", handler::userProfile)
        }
    }

    @Bean
    @CrossOrigin
    fun registrationRoutes(handler: RegistrationHandler) = router {
        "/api/v1/auth/signup".nest {
            POST("/user", handler::signUpAllUsers)
            POST("/validate/brs", handler::signUpAllUsers)
            PUT("/authorize", handler::signUpAllUsersVerification)
            PUT("/forgot-password", handler::signUpUserRestPassword)

            GET(pattern = "/employee", f = handler::signUpEmployeeView)
            GET(pattern = "/update-user-details", f = handler::updateUserView)
            GET("/new", handler::userNewFormView)
            "notification".nest {
                GET(pattern = "success", f = handler::registrationSuccessNotificationView)
                GET(pattern = "fail", f = handler::registrationFailureNotificationView)
            }
            GET(pattern = "/authorize", f = handler::authorizeUserAccountView)
            GET(pattern = "/otp-confirmation", f = handler::confirmOtpView)
            GET(pattern = "/forgot-password", f = handler::forgotPasswordView)
            POST(pattern = "/forgotPassword", f = handler::forgotPasswordAction)
            GET(pattern = "/reset", f = handler::resetPasswordView)
            POST(pattern = "/reset", f = handler::submitPasswordReset)
//            GET(pattern = "/activate-now", f = handler::authorizeActivateAccount)
//            GET(pattern = "/activate", f = handler::activateUserAccountView)
//            POST(pattern = "/activated", f = handler::submitActivateUserAccount)
//            POST(pattern = "/forgot-password", f = handler::submitAuthorizeUserAccount)
        }

        "/api/auth/signup".nest {
            GET(pattern = "/user", f = handler::signUpAllUsersView)
//            GET(pattern = "/manufacturer", f = handler::signupManufacturerView)
            GET(pattern = "/employee", f = handler::signUpEmployeeView)
            GET(pattern = "/update-user-details", f = handler::updateUserView)
            GET("/new", handler::userNewFormView)
            "notification".nest {
                GET(pattern = "success", f = handler::registrationSuccessNotificationView)
                GET(pattern = "success/message", f = handler::successNotificationView)
                GET(pattern = "fail", f = handler::registrationFailureNotificationView)
            }
            GET(pattern = "/authorize", f = handler::authorizeUserAccountView)
            GET(pattern = "/authorize/{userName}", f = handler::authorizeUAccountView)
            POST(pattern = "/authorize", f = handler::submitAuthorizeUserAccount)
            GET(pattern = "/forgot-password", f = handler::forgotPasswordView)
            POST(pattern = "/forgotPassword", f = handler::forgotPasswordAction)
            GET(pattern = "/reset", f = handler::resetPasswordView)
            POST(pattern = "/reset", f = handler::submitPasswordReset)
//            GET(pattern = "/activate-now", f = handler::authorizeActivateAccount)
//            GET(pattern = "/activate", f = handler::activateUserAccountView)
//            POST(pattern = "/activated", f = handler::submitActivateUserAccount)
//            POST(pattern = "/forgot-password", f = handler::submitAuthorizeUserAccount)
        }
        "/api/auth/kebs/signup/authorize/auth".nest {
            GET(pattern = "/otp-verification", f = handler::forgotPasswordOtp)
            GET(pattern = "/reset-password", f = handler::resetPasswordOtp)

        }

        "/auth".nest {
            GET("logout", handler::signOut)
            GET("login", handler::loginPageView)
            GET(pattern = "/otp-confirmation", f = handler::confirmOtpView)
            GET(pattern = "/forgot-password", f = handler::forgotPasswordView)
            GET(pattern = "/reset-password", f = handler::resetPasswordOtp)

            POST(pattern = "/forgotPassword", f = handler::forgotPasswordAction)
        }

//        POST(pattern = "/api/signup/manufacturer", f = handler::signupManufacturer)
    }

    @Bean
    fun importerRoutes(handler: ImporterHandler) = router {
        "/api/importer".nest {
            GET("/home", handler::home)
            GET("/di-application-list", handler::diApplicationList)
            GET("/di-application-new", handler::newDiApplication)
            GET("/di-application-detail", handler::diApplicationDetails)
            GET("/cs-approval-detail", handler::diCsApprovalDetails)
            GET("/rfc-list", handler::rfcList)
            GET("/rfc-detail", handler::rfcDetails)
            GET("/rfc-new", handler::newRfc)
            GET("/rfc/finished/adding/item", handler::rfcDetails)
        }
    }


    @Bean
    fun qualityAssuranceRoutes(handler: QualityAssuranceHandler) = router {
        "/api/v1/qa".nest {
            println("**************************")
            GET("/home", handler::home)
            GET("/permits-list", handler::permitList)
            GET("/permit-details", handler::permitDetails)
            GET("/view/fmark-generated", handler::permitViewSmarkDetails)
            GET("/new-permit", handler::newPermit)
            GET("/new-sta3", handler::newSta3)
            GET("/old-sta3", handler::oldSta3)
            GET("/new-sta10", handler::newSta10)
            GET("/old-sta10", handler::oldSta10)
            GET("/new-sta10-officer", handler::newSta10Officer)
            GET("/view-sta3", handler::viewSta3)
            GET("/view-sta10", handler::viewSta10)
            GET("/view-request", handler::viewRequestDetails)
            GET("/new-sta10-submit", handler::submitSta10)
            GET("/new-scheme-of-supervision", handler::newSchemeSupervision)
            GET("/update-scheme-of-supervision", handler::updateSchemeSupervision)
            GET("/scheme-of-supervision", handler::generatedSchemeSupervision)
            GET("/product-quality-status", handler::generateProductQualityStatus)
            GET("/invoice-details", handler::getInvoiceDetails)

            "/workPlan".nest {
                GET("/list", handler::allWorkPlanList)
                GET("/details", handler::workPlanDetails)
            }
            "/inspection".nest {
                GET("/new-inspection-report", handler::newInspectionReport)
                GET("/inspection-report-list", handler::getInspectionListDetails)
                GET("/inspection-report-details", handler::inspectionReportDetails)
                GET("/check-results", handler::checkLabResults)
                GET("/ssf-list", handler::getSSfListDetails)
                GET("/ssf-details", handler::getSSfDetails)
                GET("/details", handler::workPlanDetails)
            }
            "/invoice".nest {
                GET("/permit-invoice-list/{plantID}", handler::permitInvoiceList)
                GET("/list-batch-invoices", handler::batchInvoiceList)
                GET("/details", handler::workPlanDetails)
                GET("/batch-details", handler::batchInvoiceDetails)
            }
        }
    }


//    @Bean
//    fun diestinationInspectionRoutes(handler: DestinationInspectionHandler) = router {
//        "/api/di".nest {
//            GET("/home", handler::home)
//            GET("/cd-list", handler::cdList)
//            GET("/cd-details", handler::cDDetails)
//            GET("/cd-item-details", handler::cDItemDetails)
//            GET("/cd-coc", handler::cdCocDetails)
//            GET("/cd-cor", handler::cdCorDetails)
////            GET("/cd-coi", handler::cdCoiDetails)
//            GET("/cd-idf", handler::cdIdfDetails)
//            GET("/cd-manifest", handler::cdManifestDetails)
//            GET("/cd-customDeclartion", handler::cdCustomDeclarationDetails)
//            GET("/cd-all-invoices", handler::cdInvoiceDetails)
//
//
//            "/inspection".nest {
////                GET("/check-list", handler::inspectionDetails)
//                GET("/ssf-details", handler::getSSfDetails)
//                GET("/sample-collection", handler::inspectionDetails)
//                GET("/sample-submission", handler::inspectionDetails)
////                GET("/item-report", handler::inspectionChecklistReportDetails)
//                GET("/item/sample-Submit-param/bs-number", handler::inspectionDetails)
//            }
//
////            GET("/ministry-submission", handler::submitMVInspectionRequestToMinistry)
//            GET("/ministry-inspection-home", handler::ministryInspectionHome)
//            GET("/motor-vehicle-inspection-details", handler::mvInspectionDetails)
//
////            GET("/ministry-inspection-report", handler::ministryInspectionReport)
////            GET("/mv-inspection-checklist-detail", handler::mvInspectionChecklistDetails)
//
////            GET("/cd-inspection-module", handler::cDInspectionModuleDetails)
//        }
//    }


    @Bean
//    @PreAuthorize("hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_IO_READ')")
    fun marketSurveillanceRoutes(handler: MarketSurveillanceHandler) = router {
        "/api/v1/ms".nest {
            GET("/home", handler::home)
            GET("/complaints/new", handler::complaintsNew)
            GET("/ms-list", handler::msList)
            GET("/complaint-detail", handler::viewComplaints)

            "/ui".nest {
                GET("/types", handler::msType)
                GET("/towns", handler::msTowns)
                GET("/counties", handler::msCounties)
                GET("/departments", handler::msDepartments)
                GET("/divisions", handler::msDivisions)
                GET("/standardProductCategory", handler::msStandardsCategory)
                GET("/productCategories", handler::msProductCategories)
                GET("/broadProductCategory", handler::msBroadProductCategory)
                GET("/products", handler::msProducts)
                GET("/productSubcategory", handler::msProductSubcategory)

                "/complaint".nest {
                    GET("/list", handler::msComplaintLists)
                    POST("/search", handler::complaintsSearchListing)

                    "/detail".nest {
                        GET("/view", handler::msComplaintDetails)
                        POST("/new/save", handler::msSaveNewComplaint)

                        "/update".nest {
                            PUT("/approve", handler::msComplaintUpdateApproveDetails)
                            PUT("/reject", handler::msComplaintUpdateRejectDetails)
                            PUT("/advice", handler::msComplaintUpdateAdviceRejectDetails)
                            PUT("/assign", handler::msComplaintUpdateAssignIODetails)
                        }

                    }
                }


            }

            GET("/create-workPlan/new", handler::workPlanCreationNew)
            GET("/ms-list-workPlans", handler::allWorkPlanCreatedList)
            GET("/work-plan-generate", handler::loadWorkPlanForm)
            GET("/workPlan-detail", handler::viewWorkPlans)
            GET("/finished/generating-workPlan", handler::workPlansAddingFinished)
            GET("/approve-all-workPlans", handler::allWorkPlansApprove)
            GET("/reject-all-workPlans", handler::allWorkPlansReject)


//            GET("/onsite-start-end", handler::viewOnsiteButtons)


            GET("/onsite-start-end", handler::viewOnsiteButtons)
            GET("/workPlan-uploads", handler::uploadsView)
            GET("/data-report-seized-goods", handler::viewDataReportSeizedGoods)

            GET("/preliminary", handler::viewPreliminary)
            GET("/appealed", handler::viewPreliminary)
            GET("/appeal-not-successful", handler::viewPreliminary)
            GET("/final", handler::viewFinal)
            GET("/charge-sheet", handler::viewChargeSheet)
            GET("/data-report", handler::viewDataReport)
            GET("/preliminary-report", handler::viewPreliminaryReport)
            GET("/final-report", handler::viewFinalReport)
            GET("/sample-collection", handler::viewSampleCollection)
            GET("/sample-collect", handler::viewSampleCollect)
            GET("/sample-submission", handler::viewSampleSubmission)
            GET("/sample-submit", handler::viewSampleSubmit)
            GET("/sample-submit-lab", handler::viewLabParams)
            GET("/seizure-declaration", handler::viewSeizureDeclaration)
            GET("/notes-taking", handler::viewNotesTaking)
            GET("/invest-inspect-report", handler::viewInvestigationInspection)

            GET("/fuel-inspection/new", handler::fuelInspectionNew)
            GET("/all-fuels", handler::allFuels)
            GET("/fuel-detail", handler::viewFuelDetails)
//            GET("/fuel/sample-collection", handler::viewSampleCollection)
//            GET("/fuel/sample-submission", handler::viewSampleSubmittion)
//            GET("/sample-collect", handler::viewSampleCollect)
//            GET("/sample-submit", handler::viewSampleSubmit)
        }
    }


    @Bean
    fun systemsAdministrationRoutes(handler: SystemsAdministrationHandler) = router {
        "/api/v1/system/admin".nest {
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
    fun errorRoutes(handler: ErrorHandler) = router {
        GET("/error", handler::errorView)
        GET("/js/errors", handler::jsErrorView)
        GET("/accessDenied", handler::accessDeniedView)
        GET("/js/accessDenied", handler::jsAccessDeniedErrorView)
    }

    @Bean
    fun standardLevyRoutes(handler: StandardLevyHandler) = router {
        "/sl".nest {
            GET("", handler::home)
            GET("/manufacturers", handler::loadManufacturers)
            GET("/manufacturer", handler::singleManufacturer)
            GET("/schedule/{manufacturer}", handler::scheduleVisit)
            POST("/schedule", handler::actionScheduleVisit)
            GET("/general-actions", handler::generalActions)
        }

    }


}


