///*
// *
// * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
// * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
// * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
// * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
// * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
// * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
// * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
// * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
// * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
// * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
// *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
// *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
// *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
// *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
// *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
// *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
// *
// *
// *
// *
// *
// *   Copyright (c) 2020.  BSK
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *   you may not use this file except in compliance with the License.
// *   You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// */
//
//package org.kebs.app.kotlin.apollo.api.controllers.msControllers
//
//import mu.KotlinLogging
//import org.flowable.engine.RuntimeService
//import org.flowable.engine.TaskService
//import org.json.JSONObject
//import org.kebs.app.kotlin.apollo.api.notifications.Notifications
//import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.MarketSurveillanceDaoServices
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
//import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.kebs.app.kotlin.apollo.store.model.ms.*
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.kebs.app.kotlin.apollo.store.repo.ms.*
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.security.access.prepost.PreAuthorize
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.validation.BindingResult
//import org.springframework.web.bind.annotation.*
//import org.springframework.web.multipart.MultipartFile
//import org.springframework.web.servlet.mvc.support.RedirectAttributes
//import java.sql.Timestamp
//import java.time.Instant
//import javax.servlet.http.HttpServletResponse
//
//
//@Controller
//@RequestMapping("/api/ms")
//class MSController(
//        final val applicationMapProperties: ApplicationMapProperties,
//        private val serviceMapsRepo: IServiceMapsRepository,
//        private val serviceRequestsRepository: IServiceRequestsRepository,
//        private val complaintsRepo: IComplaintRepository,
//        private val iFuelInspectionRepo: IFuelInspectionRepository,
//        private val complaintRemarksRepo: IComplaintRemarksRepository,
//        internal val generateWorkPlanRepo: IWorkPlanGenerateRepository,
//        private val chargeSheetRepo: IChargeSheetRepository,
//        private val preliminaryRepo: IPreliminaryReportRepository,
//        private val cfgRecommendationRepo: ICfgRecommendationRepository,
//        private val finalReportRepo: IFinalReportRepository,
//        private val onsiteUploadRepo: IOnsiteUploadRepository,
//        private val dataReportRepo: IDataReportRepository,
//        private val sampleCollectRepo: ISampleCollectionRepository,
//        private val sampleCollectParameterRepo: ISampleCollectParameterRepository,
//        private val sampleSubmitParameterRepo: ISampleSubmitParameterRepository,
//        private val sampleSubmitRepo: IMSSampleSubmissionRepository,
//        private val preliminaryOutletRepo: IPreliminaryOutletsRepository,
//        private val investInspectReportRepo: IMSInvestInspectReportRepository,
//        private val seizureDeclarationRepo: IMSSeizureDeclarationRepository,
//        private val dataReportParameterRepo: IDataReportParameterRepository,
//        private val iFuelInspectionOfficerRepo: IFuelInspectionOfficerRepository,
//        private val daoServices: MarketSurveillanceDaoServices,
//        private val userRepository: IUserRepository,
//        private val notifications: Notifications,
//        private val userTypeRepo: IUserTypesEntityRepository,
//        private val userProfilesRepository: IUserProfilesRepository,
//        private val designationRepository: IDesignationsRepository,
//        private val departmentRepo: IDepartmentsRepository,
//        private val divisionsRepo: IDivisionsRepository,
//        private val countyRepo: ICountiesRepository,
//        private val commonDaoServices: CommonDaoServices,
//
//        private val complaintsDocRepo: IComplaintDocumentsRepository,
//        private val complaintLocationRepo: IComplaintLocationRepository,
//        private val complaintCustomersRepository: IComplaintCustomersRepository,
//        private val regionsRepository: IRegionsRepository,
//
//        private val marketSurveillanceBpmn: MarketSurveillanceBpmn,
//
//        private val runtimeService: RuntimeService,
//        private val registrationDaoServices: RegistrationDaoServices,
//        private val marketSurveillanceDaoServices: MarketSurveillanceDaoServices,
//        private val taskService: TaskService
//) {
//    final var appId = applicationMapProperties.mapMarketSurveillance
//    final var appComplaintCustomer: Int? = null
//
//
//    //    api/ms/sample-submit-lab/(labParmID=${sampleSubmitParam?.id}, currentPage=0,pageSize=10,ViewType=${ViewType})
//    final var redirectSiteSampleLabResults = "redirect:/api/ms/sample-submit-lab"
//    final var redirectSiteSampleDetails = "redirect:/api/ms/sample-submit"
//    final var redirectSiteWorkPlan = daoServices.redirectSiteMsWorkPlanActivityPage
//
//    init {
//
//        appComplaintCustomer = applicationMapProperties.mapMarketSurveillance
//    }
//
//    final var complaintSteps: Int = 6
//    final var workPlanSteps: Int = 15
//    final var laboratoryID: Long = 15
//    final var designationIDDI: Long = 23
//    final var designationIDHOD: Long = 25
//    final var designationIDHOF: Long = 24
//    final var designationIDRM: Long = 27
//    final var designationIDIO: Long = 26
//    final var designationIDMP: Long = 81
//    final var designationIDIOP: Long = 82
//    final var recommendationDestructionID: Long = 1
//
//
//    fun progressSteps(stepsValue: Int): JSONObject {
//        val progressObject = JSONObject()
//        var step = 0
//        for (progress in stepsValue downTo 1) {
//            step++
//            val dividend = 100
//            val quotient: Int = dividend / progress
////            val remainder = dividend % divisor
//            progressObject.put("step-$step", quotient)
//
//        }
//        return progressObject
//    }
//
//
//    @PostMapping("/complaints/new/save")
//    fun saveComplaints(
//            model: Model,
//            @ModelAttribute("complaintEntity") complaintEntity: ComplaintEntity,
//            @ModelAttribute("complaintCustomersEntity") complaintCustomersEntity: ComplaintCustomersEntity,
//            @ModelAttribute("complaintDocumentsEntity") complaintDocumentsEntity: ComplaintDocumentsEntity,
//            @ModelAttribute("complaintLocationEntity") complaintLocationEntity: ComplaintLocationEntity,
//            @RequestParam("doc_file") docFile: MultipartFile,
//            @RequestParam("msTypeUuid") msTypeUuid: String,
//            @RequestParam("pageView") pageView: String?,
////            @SessionAttribute("user") usersEntity: UsersEntity,
//            results: BindingResult,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val msType = daoServices.findMsTypeDetailsWithUuid(msTypeUuid)
//        var sr = commonDaoServices.createServiceRequest(map)
//        var payload = ""
//
////        var complaint = daoServices.saveNewComplaint(complaintEntity, msType, map, complaintCustomersEntity)
////        payload = "${this::saveComplaints.name} saved with id =[${complaint.id}]"
////        KotlinLogging.logger { }.trace("${this::saveComplaints.name} saved with id =[${complaint.id}] ")
////
////        val complaintCustomers = daoServices.saveNewComplaintCustomers(complaintCustomersEntity, map, complaint)
////        payload = "${payload}: File Saved [${this::saveComplaints.name} saved with id =[${complaintCustomers.id}]"
////        KotlinLogging.logger { }.trace("${this::saveComplaints.name} saved with id =[${complaintCustomers.id}] ")
////
////        val complaintLocation = daoServices.saveNewComplaintLocation(complaintLocationEntity, map, complaint)
////        payload = "${payload}: File Saved [${this::saveComplaints.name} saved with id =[${complaintLocation.id}]"
////        KotlinLogging.logger { }.trace("${this::saveComplaints.name} saved with id =[${complaintLocation.id}] ")
////
////        val documents = daoServices.complaintDocumentsSave(complaintDocumentsEntity, "COMPLIANT_DOC", docFile, map, complaint)
////        payload = "${payload}: File Saved [${this::saveComplaints.name} saved with id =[${documents.id}]]"
////        KotlinLogging.logger { }.trace("${this::saveComplaints.name} saved with id =[${documents.id}] ")
////
////        val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOD)
////        val regionsEntity = complaintLocation.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
////        val departmentsEntity = complaint.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
////        val hod = departmentsEntity?.let {
////            when {
////                regionsEntity != null -> {
////                    commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(designationsEntity, regionsEntity, it, map.activeStatus).userId
////                }
////                else -> throw ExpectedDataNotFound("No region value In the Complaint Location where [complaint ID =${complaint.id}]")
////            }
////        }
////
////        complaint.id?.let { complaintID ->
////            hod?.id?.let { hodID ->
////                complaintCustomersEntity.emailAddress?.let { customerEmail ->
////                    marketSurveillanceBpmn.startMSComplaintProcess(complaintID, hodID, customerEmail)
////                            ?.let {
////                                marketSurveillanceBpmn.msSubmitComplaintComplete(complaintID, hodID)
////                                        .let {
////                                            with(complaint) {
////                                                hodAssigned = hod.id
////                                            }
////                                            val updatedComplaint = complaint
////                                            complaint = commonDaoServices.updateDetails(updatedComplaint, complaint) as ComplaintEntity
////                                            complaint = complaintsRepo.save(complaint)
////
////                                            sr = commonDaoServices.mapServiceRequestForSuccessUserNotRegistered(map, payload, "${complaintCustomersEntity.firstName} ${complaintCustomersEntity.lastName}")
////                                            commonDaoServices.sendEmailWithUserEmail(customerEmail, applicationMapProperties.mapMsComplaintAcknowledgementNotification, complaint, map, sr)
////                                            commonDaoServices.sendEmailWithUserEntity(hod, applicationMapProperties.mapMsComplaintSubmittedHodNotification, complaint, map, sr)
////                                            /**
////                                             * TODO: Lets discuss to understand better how to keep track of schedules
////                                             */
//        var redirectResult = ""
//        redirectResult = if (commonDaoServices.checkLoggedInUser().isNullOrEmpty()) {
//            daoServices.redirectSiteComplaintOnLine
//        } else {
//            daoServices.redirectSiteComplaintList + "${msType.uuid}"
//        }
//        return redirectResult
////
////                                        }
////                            } ?: throw  ExpectedDataNotFound("marketSurveillanceBpmn process error")
////                } ?: throw  ExpectedDataNotFound("Complaint Customers Email is missing")
////            } ?: throw  ExpectedDataNotFound("HOD user ID is missing")
////        } ?: throw  ExpectedDataNotFound("Complaint ID is missing")
////
//    }
//
//
////    @PostMapping("/complaints/add-remarks")
////    fun saveComplaintsRemarks(
////            model: Model,
//////            @ModelAttribute("complaintRemarksEntity") complaintRemarksEntity: ComplaintRemarksEntity,
////            @ModelAttribute("complaintEntity") complaintEntity: ComplaintEntity,
//////            @RequestParam("compliantId") compliantId: Long,
////            @RequestParam("complaintUuid") complaintUuid: String,
//////            @RequestParam("ViewType") ViewType: String?,
////            results: BindingResult,
////            redirectAttributes: RedirectAttributes
////    ): String {
////        val map = commonDaoServices.serviceMapDetails(appId)
////        val loggedInUser = commonDaoServices.loggedInUserDetails()
////        val complaint = daoServices.findComplaintByUuid(complaintUuid)
////
////        var sr = commonDaoServices.createServiceRequest(map)
////        var payload = ""
////
////        val compliantId: Long = complaint.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
////        complaintEntity.id = compliantId
////
////        val complaintLocation = daoServices.findComplaintLocationByComplaintID(compliantId)
////        val complaintCustomers = daoServices.findComplaintCustomerByComplaintID(compliantId)
////
////        val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOF)
////        val regionsEntity = complaintLocation.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
////        val departmentsEntity = complaint.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
////
////        var hof: UsersEntity? = null
////        //Before
////        when {
////            complaintEntity.approved == map.activeStatus -> {
////                hof = daoServices.getUserWithProfile(departmentsEntity, regionsEntity, designationsEntity, map)
////                with(complaintEntity) {
////                    hofAssigned = hof?.id
////                    msHofAssignedDate = commonDaoServices.getCurrentDate()
////                }
////                payload = "${this::saveComplaints.name} HOF ASSIGNED FOR COMPLAINT  with id =[${complaint.id}]"
////            }
////            complaintEntity.rejected == map.activeStatus -> {
////                hof = daoServices.getUserWithProfile(departmentsEntity, regionsEntity, designationsEntity, map)
////            }
////        }
////
////        payload = "${this::saveComplaints.name} saved with id =[${complaint.id}]"
////
////        //Update
////        var updatedComplaint = commonDaoServices.updateDetails(complaintEntity, complaint) as ComplaintEntity
////        updatedComplaint = daoServices.updateComplaintDetailsInDB(updatedComplaint, loggedInUser)
////
////        //after
////        when {
////            complaintEntity.approved == map.activeStatus -> {
////                hof?.id?.let { hofID ->
////                    updatedComplaint.id?.let {
////                        marketSurveillanceBpmn.msAcceptComplaintComplete(it, hofID, true)
////                                .let {
////                                    payload = "${payload}: File Saved [${this::saveComplaints.name} saved with id =[${complaintEntity.id}]]"
////
////                                    sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
////                                    hof.email?.let { it1 -> commonDaoServices.sendEmailWithUserEmail(it1, applicationMapProperties.mapMsComplaintApprovedHofNotification, updatedComplaint, map, sr) }
////
////                                }
////                    }
////                }
////
////            }
////            complaintEntity.rejected == map.activeStatus -> {
////                payload = "${payload}: File Saved [${this::saveComplaints.name} saved with id =[${complaintEntity.id}]]"
////                hof?.id?.let { hofID ->
////                    updatedComplaint.id?.let { marketSurveillanceBpmn.msAcceptComplaintComplete(it, hofID, false) }
////
////                    sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
////                    complaintCustomers.emailAddress?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsComplaintAcknowledgementRejectionNotification, updatedComplaint, map, sr) }
//////                                                                                                        KotlinLogging.logger { }.info { "Complaint id =${complaintRemark.complaintId?.id}  and  HOF ASSIGNED ID =  $hofID  and APPROVE = True" }
////                } ?: throw  ExpectedDataNotFound("Missing HOF ID")
////            }
////            complaintEntity.mandateForOga == map.activeStatus -> {
////                payload = "${payload}: File Saved [${this::saveComplaints.name} saved with id =[${complaintEntity.id}]]"
////                hof?.id?.let { hofID ->
////                    updatedComplaint.id?.let { marketSurveillanceBpmn.msAcceptComplaintComplete(it, hofID, false) }
////
////                    sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
////                    complaintCustomers.emailAddress?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsComplaintAcknowledgementRejectionNotification, updatedComplaint, map, sr) }
//////                                                                                                        KotlinLogging.logger { }.info { "Complaint id =${complaintRemark.complaintId?.id}  and  HOF ASSIGNED ID =  $hofID  and APPROVE = True" }
////                } ?: throw  ExpectedDataNotFound("Missing HOF ID")
////            }
////            complaintEntity.assignedIoStatus == map.activeStatus -> {
////                payload = "${payload}: File Saved [${this::saveComplaints.name} saved with id =[${complaintEntity.id}]]"
////                updatedComplaint.id?.let {
////                    updatedComplaint.assignedIo?.let { it1 ->
////                        marketSurveillanceBpmn.msAssignMsioComplete(it, it1)
////                                .let {
////                                    sr = commonDaoServices.mapServiceRequestForSuccessUserNotRegistered(map, payload, """complaintCustomersEntity.firstName + " " + complaintCustomersEntity.lastName""")
////                                    commonDaoServices.sendEmailWithUserEntity(commonDaoServices.findUserByID(it1), applicationMapProperties.mapMsComplaintAssignedIONotification, updatedComplaint, map, sr)
////                                }
////                    }
////                }
////            }
////
////        }
////
////        val msType = updatedComplaint.msTypeId?.let { daoServices.findMsTypeDetailsWithId(it) }
////        return daoServices.redirectSiteComplaintList + "${msType?.uuid}"
////    }
//
//    @PostMapping("complaint/create/workPlan")
//    fun saveComplaintGenerateWorkPlan(
//            model: Model,
//            @RequestParam("complaintUuid") complaintUuid: String,
//            @ModelAttribute("complaintEntity") complaintEntity: ComplaintEntity,
//            @ModelAttribute("msWorkPlanGeneratedEntity") msWorkPlanGeneratedEntity: MsWorkPlanGeneratedEntity,
//            results: BindingResult,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        val complaint = daoServices.findComplaintByUuid(complaintUuid)
//
//        var sr = commonDaoServices.createServiceRequest(map)
//        var payload = ""
//
//        val msType = daoServices.findMsTypeDetailsWithUuid(applicationMapProperties.mapMsWorkPlanTypeUuid)
//
//        val workPlanYearCodes = daoServices.findWorkPlanYearsCodesEntity(daoServices.getCurrentYear(), map)
//        val userWorkPlan = daoServices.findWorkPlanCreatedEntity(loggedInUser, workPlanYearCodes)
//        val checkCreationDate = daoServices.isWithinRange(commonDaoServices.getCurrentDate(), workPlanYearCodes)
//        when {
//            checkCreationDate -> {
//                when (userWorkPlan) {
//                    null -> {
//                        throw ExpectedDataNotFound("Create a workPlan First")
//                    }
//                    else -> {
//
//                        val savedMsWorkPlanGeneratedEntity = msType.let { daoServices.saveNewWorkPlanFromComplaint(msWorkPlanGeneratedEntity, complaint, it, userWorkPlan, map, loggedInUser) }
//
//                        //Before update
//                        val compliantId: Long = complaint.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
//                        complaintEntity.id = compliantId
//
//                        when (complaintEntity.msProcessStatus) {
//                            map.activeStatus -> {
//                                with(complaint) {
//                                    progressStep = savedMsWorkPlanGeneratedEntity.progressStep
//                                    progressValue = progressSteps(complaintSteps).getInt("step-3")
//                                }
//                            }
//                        }
//                        //Update details
//                        var updatedComplaint = commonDaoServices.updateDetails(complaintEntity, complaint) as ComplaintEntity
//                        updatedComplaint = daoServices.updateComplaintDetailsInDB(updatedComplaint, loggedInUser)
//
//                        payload = "${this::saveComplaints.name} HOF ASSIGNED FOR COMPLAINT  with id =[${complaint.id}]"
//
//                        var designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOD)
//                        val regionsEntity = savedMsWorkPlanGeneratedEntity.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
//                        val departmentsEntity = complaint.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
//
//                        val hod = departmentsEntity?.let {
//                            if (regionsEntity != null) {
//                                daoServices.getUserWithProfile(it, regionsEntity, designationsEntity, map)
//                            }
//                        }
//
//                        designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationDirector)
//                        val director = departmentsEntity?.let {
//                            if (regionsEntity != null) {
//                                daoServices.getUserWithProfile(it, regionsEntity, designationsEntity, map)
//                            }
//                        }
//
////                        hod?.id?.let { hodID ->
////                            director?.id?.let { diID ->
////                                loggedInUser.email?.let {
////                                    savedMsWorkPlanGeneratedEntity.id?.let { generatedWorkPLanId ->
////                                        marketSurveillanceBpmn.startMSMarketSurveillanceProcess(generatedWorkPLanId, hodID, it, diID)
////                                                ?.let {
////                                                    marketSurveillanceBpmn.msMsGenerateWorkplanComplete(generatedWorkPLanId, hodID)
////                                                    return daoServices.redirectSiteComplaintList + "${msType.uuid}"
////                                                }
////
////                                    } ?: throw  ExpectedDataNotFound("Missing HOF ID")
////                                } ?: throw  ExpectedDataNotFound("Missing LOGGED IN USER EMAIL")
////                            } ?: throw  ExpectedDataNotFound("Missing DIRECTOR ID")
////                        } ?: throw  ExpectedDataNotFound("Missing HOF ID")
//                        return " "
//                    }
//                }
//
//            }
//            else -> {
//                throw ExpectedDataNotFound("You Cannot create a workPlan due to the current date of the Year")
//            }
//        }
//    }
//
//
//    @PostMapping("create/workPlan/save")
//    fun saveGeneratedWorkPlanActivity(
//            model: Model,
//            @RequestParam("createdWorkPlanUuid") createdWorkPlanUuid: String,
//            @ModelAttribute("msWorkPlanGeneratedEntity") msWorkPlanGeneratedEntity: MsWorkPlanGeneratedEntity,
//            results: BindingResult,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//
//        var sr = commonDaoServices.createServiceRequest(map)
//        var payload = ""
//
//        val msType = daoServices.findMsTypeDetailsWithUuid(applicationMapProperties.mapMsWorkPlanTypeUuid)
//
//        val createdWorkPlan = daoServices.findCreatedWorkPlanWIthUuid(createdWorkPlanUuid)
//        val savedMsWorkPlanGeneratedEntity = daoServices.saveNewWorkPlanActivity(msWorkPlanGeneratedEntity, msType, createdWorkPlan, map, loggedInUser)
//
//        payload = "${this::saveGeneratedWorkPlanActivity.name} HOD ASSIGNED FOR WORKPLAN ACTIVITY FOR APPROVAL  with id =[${savedMsWorkPlanGeneratedEntity.id}]"
//
//        var designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOD)
//        val regionsEntity = savedMsWorkPlanGeneratedEntity.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
//        val departmentsEntity = savedMsWorkPlanGeneratedEntity.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
//
//        val hod = departmentsEntity?.let { regionsEntity?.let { it1 -> daoServices.getUserWithProfile(it, it1, designationsEntity, map) } }
//
//        designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationDirector)
//        val director = departmentsEntity?.let { regionsEntity?.let { it1 -> daoServices.getUserWithProfile(it, it1, designationsEntity, map) } }
//
//        hod?.id?.let { hodID ->
//            director?.id?.let { diID ->
//                loggedInUser.email?.let {
//                    savedMsWorkPlanGeneratedEntity.id?.let { generatedWorkPLanId ->
//                        marketSurveillanceBpmn.startMSMarketSurveillanceProcess(generatedWorkPLanId, hodID, it, diID)
//                                ?.let {
//                                    marketSurveillanceBpmn.msMsGenerateWorkplanComplete(generatedWorkPLanId, hodID)
//                                    return """${daoServices.redirectSiteMsCreatedWorkPlanActivities}${createdWorkPlan.uuid}"""
//                                }
//
//                    } ?: throw  ExpectedDataNotFound("Missing HOF ID")
//                } ?: throw  ExpectedDataNotFound("Missing LOGGED IN USER EMAIL")
//            } ?: throw  ExpectedDataNotFound("Missing DIRECTOR ID")
//        } ?: throw  ExpectedDataNotFound("Missing HOF ID")
//
//    }
//
//    @GetMapping("/load/attachment")
//    fun downloadCompliantDocument(
//            response: HttpServletResponse,
//            @RequestParam("compliantId") compliantId: Long
//    ) {
//        daoServices.findComplaintDocByComplaintID(compliantId).let { doc ->
//            response.contentType = doc.fileType
//            response.addHeader("Content-Dispostion", "inline; filename=${doc.name};")
//            response.outputStream
//                    .let { responseOutputStream ->
//                        responseOutputStream.write(doc.document)
//                        responseOutputStream.close()
//                    }
//        }
//
//
//    }
//
//
//    @PostMapping("/onsite/uploads/save")
//    fun saveUploadOnsiteDocSave(
//            model: Model,
//            @ModelAttribute("onSiteUploadsEntity") onSiteUploadsEntity: MsOnSiteUploadsEntity,
//            @RequestParam("doc_file") docFile: MultipartFile,
//            @RequestParam("workPlanUuid") workPlanUuid: String,
//            results: BindingResult,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//
//        var sr = commonDaoServices.createServiceRequest(map)
//        var payload = ""
//
//        val fetchActivity = daoServices.findWorkPlanActivityWithUuid(workPlanUuid)
//        var onsiteUploads = daoServices.onSiteUploads(docFile, map, loggedInUser, fetchActivity)
//
//        return daoServices.redirectSiteMsWorkPlanActivityPage + "${fetchActivity.uuid}"
//    }
//
//
//    @PostMapping("/onsite/uploads")
//    fun saveUploadOnsiteDoc(
//            model: Model,
//            @ModelAttribute("onSiteUploadsEntity") onSiteUploadsEntity: MsOnSiteUploadsEntity,
//            @ModelAttribute("recommendedWorkPlan") recommendedWorkPlan: MsWorkPlanGeneratedEntity?,
//            @RequestParam("doc_file") docFile: MultipartFile,
//            @RequestParam("workPlanId") workPlanId: Long,
//            @RequestParam("ViewType") ViewType: String?,
//            @RequestParam("docType") docType: String?,
////            @SessionAttribute("user") usersEntity: UsersEntity,
//            results: BindingResult,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        var result = "/"
//        SecurityContextHolder.getContext().authentication?.name
//                ?.let { username ->
//                    userRepository.findByUserName(username)
//                            ?.let { loggedInUser ->
//                                serviceMapsRepo.findByIdAndStatus(appId, 1)
//                                        ?.let { map ->
//                                            if (docFile.isEmpty) {
//                                                //                    redirectAttributes.addFlashAttribute("message", "Please select a file to upload.")
//                                                //                    return "redirect:/api/ms/complaints/new"
//                                                KotlinLogging.logger { }.info("No files included")
//                                            }
//
//                                            generateWorkPlanRepo.findByIdOrNull(workPlanId)
//                                                    ?.let { fetchWorkPlan ->
//                                                        var onsiteUploads = onSiteUploadsEntity
//
//                                                        with(onsiteUploads) {
//                                                            name = commonDaoServices.saveDocuments(docFile)
//                                                            fileType = docFile.contentType
//                                                            documentType = "ONSITE_UPLOADS"
//                                                            document = docFile.bytes
//                                                            transactionDate = commonDaoServices.getCurrentDate()
//                                                            status = map.activeStatus
//                                                            createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                            createdOn = Timestamp.from(Instant.now())
//                                                            workPlanGeneratedID = fetchWorkPlan
//                                                        }
//                                                        onsiteUploads = onsiteUploadRepo.save(onsiteUploads)
//
//                                                        onSiteUploadsEntity.saveType?.let {
//                                                            if (it.equals("INSPECTINVEST")) {
//                                                                fetchWorkPlan.investInspectReportStatus = map.activeStatus
//                                                                generateWorkPlanRepo.save(fetchWorkPlan)
//                                                            } else if (it.equals("DESTRUCTION")) {
//                                                                when (recommendedWorkPlan) {
//                                                                    null -> {
//                                                                    }
//                                                                    else -> {
//                                                                        with(fetchWorkPlan) {
//                                                                            destructionDocId = onsiteUploads.id
//
//                                                                        }
//                                                                        val workPlan = generateWorkPlanRepo.save(fetchWorkPlan)
//
//                                                                        val hod = workPlan.complaintDepartment?.let { workPlan.region?.let { it1 -> findRegionUserId(designationIDHOD, commonDaoServices.findDepartmentByID(it), map.activeStatus, commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus)) } }
//                                                                        hod?.email?.let { it1 ->
//                                                                            destructionReportStatus(it1, hod)
//                                                                        }
//                                                                    }
//                                                                }
//
//
//                                                            } else if (it.equals("DESTRUCTIONNOTICE")) {
//                                                                when (recommendedWorkPlan) {
//                                                                    null -> {
//                                                                    }
//                                                                    else -> {
//                                                                        with(fetchWorkPlan) {
//                                                                            destructionClientEmail = recommendedWorkPlan.destructionClientEmail
//                                                                            destructionNotificationDate = commonDaoServices.getCurrentDate()
//                                                                            destructionNotificationStatus = map.activeStatus
//                                                                        }
//                                                                        val savedDestruction = generateWorkPlanRepo.save(fetchWorkPlan)
//                                                                        savedDestruction.destructionClientEmail?.let { it1 -> destructionNotice(it1) }
//                                                                    }
//                                                                }
//
//
//                                                            } else {
//
//                                                            }
//                                                        }
//                                                        /*
//                                                        if(onsiteUploadsEntity.saveType?.equals("INSPECTINVEST")){
//                                                            fetchWorkPlan.investInspectReportStatus = map.activeStatus
//                                                            genarateWorkPlanRepo.save(fetchWorkPlan)
//
//                                                        }
//                                                        */
//
//                                                        KotlinLogging.logger { }.trace("${this::saveComplaints.name} saved with id =[${onsiteUploads.id}] ")
//                                                        KotlinLogging.logger { }.info { "Document saved = ${onsiteUploads.id} " }
//
//                                                        result = daoServices.redirectSiteMsWorkPlanActivityPage + "${fetchWorkPlan.uuid}"
//                                                    }
//                                        }
//                            }
//                }
//
//        return result
//    }
//
//
//    @PostMapping("/workPlan/add-remarks")
//    fun saveWorkPlanRemarks(
//            model: Model,
//            @ModelAttribute("msWorkPlanGeneratedEntity") msWorkPlanGeneratedEntity: MsWorkPlanGeneratedEntity?,
//            @ModelAttribute("preliminaryEntity") preliminaryEntity: MsPreliminaryReportEntity?,
//            @ModelAttribute("finalEntity") finalEntity: MsFinalReportEntity?,
//            @ModelAttribute("labParametersEntity") labParametersEntity: MsLaboratoryParametersEntity?,
//            @RequestParam("workPlanId") workPlanId: Long?,
//            @RequestParam("ViewType") ViewType: String?,
//            @RequestParam("labParamID") labParamID: Long?,
//            @RequestParam("docType") docType: String,
//            results: BindingResult,
//            redirectAttributes: RedirectAttributes
//    ): String {
//        var result = "/"
//        SecurityContextHolder.getContext().authentication?.name
//                ?.let { username ->
//                    userRepository.findByUserName(username)
//                            ?.let { loggedInUser ->
//                                serviceMapsRepo.findByIdAndStatus(appId, 1)
//                                        ?.let { map ->
//                                            var fetchedFuelEntity: MsFuelInspectionEntity? = null
//                                            var workPlanEntity: MsWorkPlanGeneratedEntity? = null
//                                            if (workPlanId != null) {
//                                                fetchedFuelEntity = iFuelInspectionRepo.findByIdOrNull(workPlanId)
//                                                workPlanEntity = generateWorkPlanRepo.findByIdOrNull(workPlanId)
//                                            }
////                                            generateWorkPlanRepo.findByIdOrNull(workPlanId)
////                                                    ?.let { workPlanEntity ->
//
////                                                        /**
////                                                         * TODO: Lets discuss to understand better what how to assign HOD to a complaint is it based on Region or Randomly
////                                                         */
////                                                        val hofID = findRegionUserId("MS HOF", map.activeStatus, regionsEntity.id)
////                                                        KotlinLogging.logger { }.info { "Complaint id =${complaintRemark.complaintId?.id}  and  HOF ASSIGNED ID =  $hofID" }
////
//                                            when (docType) {
//                                                "workplan" -> {
//                                                    if (msWorkPlanGeneratedEntity != null && workPlanEntity != null) {
//                                                        when {
//
//                                                            msWorkPlanGeneratedEntity.approved.equals("APPROVED") -> {
//
//                                                                workPlanEntity.approved = msWorkPlanGeneratedEntity.approved
//                                                                workPlanEntity.approvedRemarks = msWorkPlanGeneratedEntity.approvedRemarks
//                                                                workPlanEntity.approvedBy = msWorkPlanGeneratedEntity.approvedBy
//                                                                workPlanEntity.approvedStatus = map.activeStatus
//                                                                workPlanEntity.rejectedStatus = map.inactiveStatus
//                                                                workPlanEntity.approvedOn = commonDaoServices.getCurrentDate()
//
//                                                                val workPlan = generateWorkPlanRepo.save(workPlanEntity)
//
//                                                                workPlan.id
//                                                                        ?.let { workPlanId ->
//                                                                            workPlan.officerId
//                                                                                    ?.let { workPlanOfficerId ->
//                                                                                        marketSurveillanceBpmn.msMsApproveWorkplanComplete(workPlanId, true, workPlanOfficerId)
//                                                                                        result = redirectSiteWorkPlan + "${workPlan.uuid}"
//                                                                                    }
//                                                                        }
//                                                                //                                                                    KotlinLogging.logger { }.info { "Complaint id =${complaintRemark.complaintId?.id}  and  HOF ASSIGNED ID =  $hofID  and APPROVE = True" }
//
//                                                            }
//                                                            msWorkPlanGeneratedEntity.rejected.equals("REJECTED") -> {
//                                                                workPlanEntity.rejected = msWorkPlanGeneratedEntity.rejected
//                                                                workPlanEntity.rejectedRemarks = msWorkPlanGeneratedEntity.rejectedRemarks
//                                                                workPlanEntity.rejectedBy = msWorkPlanGeneratedEntity.rejectedBy
//                                                                workPlanEntity.approvedStatus = map.inactiveStatus
//                                                                workPlanEntity.rejectedStatus = map.activeStatus
//                                                                workPlanEntity.rejectedOn = commonDaoServices.getCurrentDate()
//
//                                                                val workPlan = generateWorkPlanRepo.save(workPlanEntity)
//
//                                                                workPlan.id
//                                                                        ?.let { workPlanId ->
//                                                                            workPlan.officerId
//                                                                                    ?.let { workPlanOfficerId ->
//                                                                                        marketSurveillanceBpmn.msMsApproveWorkplanComplete(workPlanId, false, workPlanOfficerId)
//                                                                                        result = redirectSiteWorkPlan + "${workPlan.uuid}"
//                                                                                    }
//                                                                        }
//
//
//                                                            }
//                                                            msWorkPlanGeneratedEntity.process.equals("ENDMSPROCESS") -> {
//                                                                workPlanEntity.msEndProcessRemarks = msWorkPlanGeneratedEntity.msEndProcessRemarks
//                                                                workPlanEntity.modifiedBy = msWorkPlanGeneratedEntity.modifiedBy
//                                                                workPlanEntity.modifiedOn = Timestamp.from(Instant.now())
//
//                                                                val workPlan = generateWorkPlanRepo.save(workPlanEntity)
//                                                                val di = workPlan.complaintDepartment?.let { workPlan.region?.let { it1 -> findRegionUserId(designationIDDI, commonDaoServices.findDepartmentByID(it), map.activeStatus, commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus)) } }
//
//                                                                workPlan.id
//                                                                        ?.let { workPlanId ->
//                                                                            marketSurveillanceBpmn.endMsMarketSurveillanceProcess(workPlanId.toString())
//                                                                            workPlan.msEndProcessRemarks?.let {
//                                                                                if (di != null) {
//                                                                                    when {
//                                                                                        workPlan.complaintId != null -> {
//                                                                                            workPlan.complaintId?.let { it1 -> marketSurveillanceBpmn.msProcessComplete(it1) }
//                                                                                        }
//                                                                                    }
//                                                                                    endProcessAlertDI(it, di, workPlan)
//                                                                                }
//                                                                            }
//                                                                            result = redirectSiteWorkPlan + "${workPlan.uuid}"
//
//                                                                        }
//
//
//                                                            }
//
//                                                        }
//                                                    }
//                                                }
//                                                "LabReport" -> {
//                                                    if (labParametersEntity != null && labParamID != null) {
//                                                        if (ViewType.equals("fuel")) {
//                                                            sampleSubmitParameterRepo.findByIdOrNull(labParamID)
//                                                                    ?.let { fetchedLabPrams ->
//                                                                        with(fetchedLabPrams) {
//                                                                            when {
//                                                                                labParametersEntity.processStage.equals("LabResultsComments") -> {
//                                                                                    when {
//                                                                                        labParametersEntity.labResultStatusGood == 1 -> {
//                                                                                            labResultStatusBad = labParametersEntity.labResultStatusBad
//                                                                                            labResultStatusGood = labParametersEntity.labResultStatusGood
//                                                                                            labResultStatusGoodBy = labParametersEntity.labResultStatusGoodBy
//                                                                                            labResultStatusGoodRemarks = labParametersEntity.labResultStatusGoodRemarks
//                                                                                            labResultStatusGoodOn = commonDaoServices.getCurrentDate()
//
//                                                                                            sampleSubmitParameterRepo.save(fetchedLabPrams)
//                                                                                        }
//                                                                                        labParametersEntity.labResultStatusBad == 1 -> {
//                                                                                            labResultStatusBad = labParametersEntity.labResultStatusBad
//                                                                                            labResultStatusGood = labParametersEntity.labResultStatusGood
//                                                                                            labResultStatusBadBy = labParametersEntity.labResultStatusBadBy
//                                                                                            labResultStatusBadRemarks = labParametersEntity.labResultStatusBadRemarks
//                                                                                            labResultStatusBadOn = commonDaoServices.getCurrentDate()
//
//                                                                                            sampleSubmitParameterRepo.save(fetchedLabPrams)
//
//                                                                                            when (ViewType) {
//                                                                                                null -> {
//                                                                                                }
//                                                                                                else -> {
//                                                                                                    simulateLabReportResults(fetchedLabPrams, ViewType)
//                                                                                                }
//                                                                                            }
//                                                                                        }
//                                                                                        else -> {
//
//                                                                                        }
//                                                                                    }
//
//                                                                                }
//                                                                                labParametersEntity.processStage.equals("LabResults") -> {
//                                                                                    when {
//                                                                                        labParametersEntity.labPassedStatus == 1 -> {
//                                                                                            labFailedStatus = labParametersEntity.labFailedStatus
//                                                                                            labPassedStatus = labParametersEntity.labPassedStatus
//                                                                                            labPassedStatusBy = labParametersEntity.labPassedStatusBy
//                                                                                            labPassedStatusRemarks = labParametersEntity.labPassedStatusRemarks
//                                                                                            labPassedStatusOn = commonDaoServices.getCurrentDate()
//
//                                                                                            sampleSubmitParameterRepo.save(fetchedLabPrams)
//                                                                                        }
//                                                                                        labParametersEntity.labFailedStatus == 1 -> {
//                                                                                            labFailedStatus = labParametersEntity.labFailedStatus
//                                                                                            labPassedStatus = labParametersEntity.labPassedStatus
//                                                                                            labFailedStatusBy = labParametersEntity.labFailedStatusBy
//                                                                                            labFailedStatusRemarks = labParametersEntity.labFailedStatusRemarks
//                                                                                            labFailedStatusOn = commonDaoServices.getCurrentDate()
//
//                                                                                            sampleSubmitParameterRepo.save(fetchedLabPrams)
//                                                                                        }
//                                                                                        else -> {
//
//                                                                                        }
//                                                                                    }
//
//                                                                                }
//                                                                                else -> {
//
//                                                                                }
//                                                                            }
//                                                                            result = redirectSiteSampleLabResults + "?currentPage=0&pageSize=10&labParmID=${labParamID}&ViewType=${ViewType}"
//                                                                            //                                                                            result = redirectSiteSampleLabResults+"?currentPage=0&pageSize=10&labParmID=${labParamID}&ViewType=${ViewType}"
//                                                                            //                                                                            resultRedirect = "redirect:/api/ms/sample-collect?fuelInspectID=${fetchedFuelEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//
//                                                                        }
//
//                                                                        //                                                                        fetchedLabprams.sampleCollectionId = fetchedSampleCollect
//                                                                        //                                                                        sampleSubmitParameterRepo.save(sampleCollectParam)
//                                                                        //
//                                                                        //                                                                        resultRedirect = "redirect:/api/ms/sample-collect?fuelInspectID=${fetchedFuelEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                    }
//
//                                                        } else if (ViewType.equals("workPlan")) {
//                                                            sampleSubmitParameterRepo.findByIdOrNull(labParamID)
//                                                                    ?.let { fetchedLabPrams ->
//                                                                        with(fetchedLabPrams) {
//                                                                            when {
//                                                                                labParametersEntity.processStage.equals("LabResultsComments") -> {
//                                                                                    when {
//                                                                                        labParametersEntity.labResultStatusGood == 1 -> {
//                                                                                            labResultStatusBad = labParametersEntity.labResultStatusBad
//                                                                                            labResultStatusGood = labParametersEntity.labResultStatusGood
//                                                                                            labResultStatusGoodBy = labParametersEntity.labResultStatusGoodBy
//                                                                                            labResultStatusGoodRemarks = labParametersEntity.labResultStatusGoodRemarks
//                                                                                            labResultStatusGoodOn = commonDaoServices.getCurrentDate()
//
//                                                                                            sampleSubmitParameterRepo.save(fetchedLabPrams)
//                                                                                        }
//                                                                                        labParametersEntity.labResultStatusBad == 1 -> {
//                                                                                            labResultStatusBad = labParametersEntity.labResultStatusBad
//                                                                                            labResultStatusGood = labParametersEntity.labResultStatusGood
//                                                                                            labResultStatusBadBy = labParametersEntity.labResultStatusBadBy
//                                                                                            labResultStatusBadRemarks = labParametersEntity.labResultStatusBadRemarks
//                                                                                            labResultStatusBadOn = commonDaoServices.getCurrentDate()
//
//                                                                                            sampleSubmitParameterRepo.save(fetchedLabPrams)
//
//                                                                                            when (ViewType) {
//                                                                                                null -> {
//                                                                                                }
//                                                                                                else -> {
//                                                                                                    simulateLabReportResults(fetchedLabPrams, ViewType)
//                                                                                                }
//                                                                                            }
//                                                                                        }
//                                                                                        else -> {
//
//                                                                                        }
//                                                                                    }
//
//                                                                                }
//                                                                                labParametersEntity.processStage.equals("LabResults") -> {
//                                                                                    when {
//                                                                                        labParametersEntity.labPassedStatus == 1 -> {
//                                                                                            labFailedStatus = labParametersEntity.labFailedStatus
//                                                                                            labPassedStatus = labParametersEntity.labPassedStatus
//                                                                                            labPassedStatusBy = labParametersEntity.labPassedStatusBy
//                                                                                            labPassedStatusRemarks = labParametersEntity.labPassedStatusRemarks
//                                                                                            labPassedStatusOn = commonDaoServices.getCurrentDate()
//
//                                                                                            sampleSubmitParameterRepo.save(fetchedLabPrams)
//                                                                                        }
//                                                                                        labParametersEntity.labFailedStatus == 1 -> {
//                                                                                            labFailedStatus = labParametersEntity.labFailedStatus
//                                                                                            labPassedStatus = labParametersEntity.labPassedStatus
//                                                                                            labFailedStatusBy = labParametersEntity.labFailedStatusBy
//                                                                                            labFailedStatusRemarks = labParametersEntity.labFailedStatusRemarks
//                                                                                            labFailedStatusOn = commonDaoServices.getCurrentDate()
//
//                                                                                            sampleSubmitParameterRepo.save(fetchedLabPrams)
//                                                                                        }
//                                                                                        else -> {
//
//                                                                                        }
//                                                                                    }
//
//                                                                                }
//                                                                                else -> {
//
//                                                                                }
//                                                                            }
//                                                                            result = redirectSiteSampleLabResults + "?currentPage=0&pageSize=10&labParmID=${labParamID}&ViewType=${ViewType}"
//
//                                                                        }
//
//                                                                        //                                                                        fetchedLabprams.sampleCollectionId = fetchedSampleCollect
//                                                                        //                                                                        sampleSubmitParameterRepo.save(sampleCollectParam)
//                                                                        //
//                                                                        //                                                                        resultRedirect = "redirect:/api/ms/sample-collect?fuelInspectID=${fetchedFuelEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                    }
//                                                        }
//                                                    }
//                                                }
//                                                "preliminary" -> {
//                                                    if (preliminaryEntity != null && workPlanEntity != null) {
//                                                        preliminaryRepo.findByWorkPlanGeneratedID(workPlanEntity)
//                                                                ?.let { fetchedPreliminary ->
//                                                                    val hod = workPlanEntity.complaintDepartment?.let { workPlanEntity.region?.let { it1 -> findRegionUserId(designationIDHOD, commonDaoServices.findDepartmentByID(it), map.activeStatus, commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus)) } }
//                                                                    val hof = workPlanEntity.complaintDepartment?.let { workPlanEntity.region?.let { it1 -> findRegionUserId(designationIDHOF, commonDaoServices.findDepartmentByID(it), map.activeStatus, commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus)) } }
//                                                                    when {
//                                                                        preliminaryEntity.approved.equals("APPROVEDPRELIMINARY") -> {
//                                                                            fetchedPreliminary.approved = preliminaryEntity.approved
//                                                                            fetchedPreliminary.approvedRemarks = preliminaryEntity.approvedRemarks
//                                                                            fetchedPreliminary.approvedBy = preliminaryEntity.approvedBy
//                                                                            fetchedPreliminary.approvedStatus = map.activeStatus
//                                                                            fetchedPreliminary.rejectedStatus = map.inactiveStatus
//                                                                            fetchedPreliminary.approvedOn = commonDaoServices.getCurrentDate()
//
//                                                                            val preliminaryDetails = preliminaryRepo.save(fetchedPreliminary)
//
//                                                                            workPlanEntity.officerId?.let {
//                                                                                hod?.id?.let { it1 ->
//                                                                                    workPlanEntity.id?.let { it2 ->
//                                                                                        marketSurveillanceBpmn.msHOFApprovePreliminaryReportComplete(it2, it, it1, true)
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//                                                                        preliminaryEntity.rejected.equals("REJECTEDPRELIMINARY") -> {
//                                                                            fetchedPreliminary.rejected = preliminaryEntity.rejected
//                                                                            fetchedPreliminary.rejectedRemarks = preliminaryEntity.rejectedRemarks
//                                                                            fetchedPreliminary.rejectedBy = preliminaryEntity.rejectedBy
//                                                                            fetchedPreliminary.approvedStatus = map.inactiveStatus
//                                                                            fetchedPreliminary.rejectedStatus = map.activeStatus
//                                                                            fetchedPreliminary.rejectedOn = commonDaoServices.getCurrentDate()
//
//                                                                            val preliminaryDetails = preliminaryRepo.save(fetchedPreliminary)
//
//                                                                            workPlanEntity.officerId?.let {
//                                                                                hod?.id?.let { it1 ->
//                                                                                    workPlanEntity.id?.let { it2 ->
//                                                                                        marketSurveillanceBpmn.msHOFApprovePreliminaryReportComplete(it2, it, it1, false)
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//                                                                        preliminaryEntity.approvedHod.equals("APPROVEDHODPRELIMINARY") -> {
//                                                                            fetchedPreliminary.approvedHod = preliminaryEntity.approvedHod
//                                                                            fetchedPreliminary.approvedRemarksHod = preliminaryEntity.approvedRemarksHod
//                                                                            fetchedPreliminary.approvedByHod = preliminaryEntity.approvedByHod
//                                                                            fetchedPreliminary.approvedStatusHod = map.activeStatus
//                                                                            fetchedPreliminary.rejectedStatusHod = map.inactiveStatus
//                                                                            fetchedPreliminary.approvedOnHod = commonDaoServices.getCurrentDate()
//
//                                                                            val preliminaryDetails = preliminaryRepo.save(fetchedPreliminary)
//
//                                                                            workPlanEntity.msFinalReportStatus = map.inactiveStatus
//                                                                            workPlanEntity.preliminaryApprovedStatus = map.activeStatus
//                                                                            generateWorkPlanRepo.save(workPlanEntity)
//
//                                                                            workPlanEntity.id?.let {
//                                                                                workPlanEntity.officerId?.let { it1 ->
//                                                                                    hof?.id?.let { it2 ->
//                                                                                        marketSurveillanceBpmn.msHODApprovePreliminaryReportComplete(it, it1, it2, true)
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//                                                                        preliminaryEntity.rejectedHod.equals("REJECTEDHODPRELIMINARY") -> {
//                                                                            fetchedPreliminary.rejectedHod = preliminaryEntity.rejectedHod
//                                                                            fetchedPreliminary.rejectedRemarksHod = preliminaryEntity.rejectedRemarksHod
//                                                                            fetchedPreliminary.rejectedByHod = preliminaryEntity.rejectedByHod
//                                                                            fetchedPreliminary.approvedStatusHod = map.inactiveStatus
//                                                                            fetchedPreliminary.rejectedStatusHod = map.activeStatus
//                                                                            fetchedPreliminary.rejectedOnHod = commonDaoServices.getCurrentDate()
//
//                                                                            val preliminaryDetails = preliminaryRepo.save(fetchedPreliminary)
//
//                                                                            //                                                                                workPlanEntity.officerId?.id?.let { hod?.id?.let { it1 -> marketSurveillanceBpmn.msHOFApprovePreliminaryReportComplete(preliminaryDetails.id, it, it1,false) } }
//                                                                            workPlanEntity.id?.let {
//                                                                                workPlanEntity.officerId?.let { it1 ->
//                                                                                    hof?.id?.let { it2 ->
//                                                                                        marketSurveillanceBpmn.msHODApprovePreliminaryReportComplete(it, it1, it2, false)
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//
//                                                                        preliminaryEntity.approvedHofFinal.equals("APPROVEDPRELIMINARYFINAL") -> {
//                                                                            fetchedPreliminary.approvedHofFinal = preliminaryEntity.approvedHofFinal
//                                                                            fetchedPreliminary.approvedRemarksHofFinal = preliminaryEntity.approvedRemarksHofFinal
//                                                                            fetchedPreliminary.approvedByHofFinal = preliminaryEntity.approvedByHofFinal
//                                                                            fetchedPreliminary.approvedStatusHofFinal = map.activeStatus
//                                                                            fetchedPreliminary.rejectedStatusHofFinal = map.inactiveStatus
//                                                                            fetchedPreliminary.approvedOnHofFinal = commonDaoServices.getCurrentDate()
//
//                                                                            val preliminaryDetails = preliminaryRepo.save(fetchedPreliminary)
//
//                                                                            workPlanEntity.officerId?.let {
//                                                                                hod?.id?.let { it1 ->
//                                                                                    workPlanEntity.id?.let { it2 ->
//                                                                                        marketSurveillanceBpmn.msHOFApproveFinalReportComplete(it2, it, it1, true)
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//                                                                        preliminaryEntity.rejectedHofFinal.equals("REJECTEDPRELIMINARYFINAL") -> {
//                                                                            fetchedPreliminary.rejectedHofFinal = preliminaryEntity.rejectedHofFinal
//                                                                            fetchedPreliminary.rejectedRemarksHofFinal = preliminaryEntity.rejectedRemarksHofFinal
//                                                                            fetchedPreliminary.rejectedByHofFinal = preliminaryEntity.rejectedByHofFinal
//                                                                            fetchedPreliminary.approvedStatusHofFinal = map.inactiveStatus
//                                                                            fetchedPreliminary.rejectedStatusHofFinal = map.activeStatus
//                                                                            fetchedPreliminary.rejectedOnHofFinal = commonDaoServices.getCurrentDate()
//
//                                                                            val preliminaryDetails = preliminaryRepo.save(fetchedPreliminary)
//
//                                                                            workPlanEntity.officerId?.let {
//                                                                                hod?.id?.let { it1 ->
//                                                                                    workPlanEntity.id?.let { it2 ->
//                                                                                        marketSurveillanceBpmn.msHOFApproveFinalReportComplete(it2, it, it1, false)
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//                                                                        preliminaryEntity.approvedHodFinal.equals("APPROVEDHODPRELIMINARYFINAL") -> {
//                                                                            fetchedPreliminary.approvedHodFinal = preliminaryEntity.approvedHodFinal
//                                                                            fetchedPreliminary.approvedRemarksHodFinal = preliminaryEntity.approvedRemarksHodFinal
//                                                                            fetchedPreliminary.approvedByHodFinal = preliminaryEntity.approvedByHodFinal
//                                                                            fetchedPreliminary.approvedStatusHodFinal = map.activeStatus
//                                                                            fetchedPreliminary.rejectedStatusHodFinal = map.inactiveStatus
//                                                                            fetchedPreliminary.approvedOnHodFinal = commonDaoServices.getCurrentDate()
//
//                                                                            val preliminaryDetails = preliminaryRepo.save(fetchedPreliminary)
//
//                                                                            workPlanEntity.finalApprovedStatus = map.activeStatus
//                                                                            generateWorkPlanRepo.save(workPlanEntity)
//                                                                            //
//
//                                                                            workPlanEntity.id?.let {
//                                                                                workPlanEntity.officerId?.let { it1 ->
//                                                                                    hof?.id?.let { it2 ->
//                                                                                        marketSurveillanceBpmn.msHODApproveFinalReportComplete(it, it1, true)
//                                                                                        workPlanEntity.hodRecommendationStart = map.activeStatus
//                                                                                        generateWorkPlanRepo.save(workPlanEntity)
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//                                                                        preliminaryEntity.rejectedHodFinal.equals("REJECTEDHODPRELIMINARYFINAL") -> {
//                                                                            fetchedPreliminary.rejectedHodFinal = preliminaryEntity.rejectedHodFinal
//                                                                            fetchedPreliminary.rejectedRemarksHodFinal = preliminaryEntity.rejectedRemarksHodFinal
//                                                                            fetchedPreliminary.rejectedByHodFinal = preliminaryEntity.rejectedByHodFinal
//                                                                            fetchedPreliminary.approvedStatusHodFinal = map.inactiveStatus
//                                                                            fetchedPreliminary.rejectedStatusHodFinal = map.activeStatus
//                                                                            fetchedPreliminary.rejectedOnHodFinal = commonDaoServices.getCurrentDate()
//
//                                                                            val preliminaryDetails = preliminaryRepo.save(fetchedPreliminary)
//
//                                                                            //                                                                                workPlanEntity.officerId?.id?.let { hod?.id?.let { it1 -> marketSurveillanceBpmn.msHOFApprovePreliminaryReportComplete(preliminaryDetails.id, it, it1,false) } }
//                                                                            workPlanEntity.id?.let {
//                                                                                workPlanEntity.officerId?.let { it1 ->
//                                                                                    hof?.id?.let { it2 ->
//                                                                                        marketSurveillanceBpmn.msHODApproveFinalReportComplete(it, it1, false)
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//                                                                        else -> {
//
//                                                                        }
//                                                                    }
//                                                                    result = redirectSiteWorkPlan + "${workPlanEntity.uuid}"
//                                                                }
//                                                    }
//                                                }
//                                                "finalReport" -> {
//                                                    if (finalEntity != null && workPlanEntity != null) {
//                                                        finalReportRepo.findByWorkPlanGeneratedID(workPlanEntity)
//                                                                ?.let { fetchedfinal ->
//                                                                    val hod = workPlanEntity.complaintDepartment?.let { workPlanEntity.region?.let { it1 -> findRegionUserId(designationIDHOD, commonDaoServices.findDepartmentByID(it), map.activeStatus, commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus)) } }
//                                                                    val hof = workPlanEntity.complaintDepartment?.let { workPlanEntity.region?.let { it1 -> findRegionUserId(designationIDHOF, commonDaoServices.findDepartmentByID(it), map.activeStatus, commonDaoServices.findRegionEntityByRegionID(it1, map.activeStatus)) } }
//                                                                    when {
//                                                                        finalEntity.approvedHof.equals("APPROVEDHOF") -> {
//                                                                            fetchedfinal.approvedHof = finalEntity.approvedHof
//                                                                            fetchedfinal.approvedRemarksHof = finalEntity.approvedRemarksHof
//                                                                            fetchedfinal.approvedByHof = finalEntity.approvedByHof
//                                                                            fetchedfinal.approvedStatusHof = map.activeStatus
//                                                                            fetchedfinal.rejectedStatusHof = map.inactiveStatus
//                                                                            fetchedfinal.approvedOnHof = commonDaoServices.getCurrentDate()
//
//                                                                            val finalRepoDetails = finalReportRepo.save(fetchedfinal)
//
//                                                                            workPlanEntity.officerId?.let { hod?.id?.let { it1 -> workPlanEntity.id?.let { it2 -> marketSurveillanceBpmn.msHOFApproveFinalReportComplete(it2, it, it1, true) } } }
//
//
//                                                                        }
//                                                                        finalEntity.rejectedHof.equals("REJECTEDHOF") -> {
//                                                                            fetchedfinal.rejectedHof = finalEntity.rejectedHof
//                                                                            fetchedfinal.rejectedRemarksHof = finalEntity.rejectedRemarksHof
//                                                                            fetchedfinal.rejectedByHof = finalEntity.rejectedByHof
//                                                                            fetchedfinal.approvedStatusHof = map.inactiveStatus
//                                                                            fetchedfinal.rejectedStatusHof = map.activeStatus
//                                                                            fetchedfinal.rejectedOnHof = commonDaoServices.getCurrentDate()
//
//                                                                            val finalRepoDetails = finalReportRepo.save(fetchedfinal)
//
//                                                                            workPlanEntity.officerId?.let { hod?.id?.let { it1 -> workPlanEntity.id?.let { it2 -> marketSurveillanceBpmn.msHOFApproveFinalReportComplete(it2, it, it1, false) } } }
//
//
//                                                                        }
//                                                                        finalEntity.approvedHod.equals("APPROVEDHOD") -> {
//                                                                            fetchedfinal.approvedHod = finalEntity.approvedHod
//                                                                            fetchedfinal.approvedRemarksHod = finalEntity.approvedRemarksHod
//                                                                            fetchedfinal.approvedByHod = finalEntity.approvedByHod
//                                                                            fetchedfinal.approvedStatusHod = map.activeStatus
//                                                                            fetchedfinal.rejectedStatusHod = map.inactiveStatus
//                                                                            fetchedfinal.approvedOnHod = commonDaoServices.getCurrentDate()
//
//                                                                            val finalRepoDetails = finalReportRepo.save(fetchedfinal)
//
//                                                                            workPlanEntity.id.let {
//                                                                                workPlanEntity.officerId.let { it1 ->
//                                                                                    hof?.id.let { it2 ->
//                                                                                        if (it != null) {
//                                                                                            if (it1 != null) {
//                                                                                                marketSurveillanceBpmn.msHODApproveFinalReportComplete(it, it1, true)
//                                                                                            }
//                                                                                        }
//                                                                                    }
//                                                                                }
//                                                                            }
//
//
//                                                                        }
//                                                                        finalEntity.rejectedHod.equals("REJECTEDHOD") -> {
//                                                                            fetchedfinal.rejectedHod = finalEntity.rejectedHod
//                                                                            fetchedfinal.rejectedRemarksHod = finalEntity.rejectedRemarksHod
//                                                                            fetchedfinal.rejectedByHod = finalEntity.rejectedByHod
//                                                                            fetchedfinal.approvedStatusHod = map.inactiveStatus
//                                                                            fetchedfinal.rejectedStatusHod = map.activeStatus
//                                                                            fetchedfinal.rejectedOnHod = commonDaoServices.getCurrentDate()
//
//                                                                            val finalRepoDetails = finalReportRepo.save(fetchedfinal)
//
//                                                                            //                                                                                workPlanEntity.officerId?.id?.let { hod?.id?.let { it1 -> marketSurveillanceBpmn.msHOFApprovePreliminaryReportComplete(preliminaryDetails.id, it, it1,false) } }
//                                                                            workPlanEntity.id?.let { workPlanEntity.officerId?.let { it1 -> hof?.id?.let { it2 -> marketSurveillanceBpmn.msHODApproveFinalReportComplete(it, it1, false) } } }
//
//
//                                                                        }
//                                                                        else -> {
//
//                                                                        }
//                                                                    }
//                                                                }
//                                                        result = redirectSiteWorkPlan + "${workPlanEntity.uuid}"
//                                                    }
//                                                }
//                                            }
////                                            result = redirectSiteWorkPlan
////                                                    }
//
//
//                                        }
//                            }
//                }
//
//
//        return result
//    }
//
//    @PreAuthorize("hasAuthority('MS_IO_MODIFY') or  hasAuthority('MS_IOP_MODIFY') or hasAuthority('MS_HOD_MODIFY') or hasAuthority('MS_RM_MODIFY')")
//    @PostMapping("onsite/forms/save")
//    fun saveMSONSITE(
//        @ModelAttribute("chargeSheet") chargeSheet: MsChargeSheetEntity?,
//        @ModelAttribute("preliminaryReport") preliminaryReport: MsPreliminaryReportEntity?,
//        @ModelAttribute("finalReport") finalReport: MsFinalReportEntity?,
//        @ModelAttribute("dataReport") dataReport: MsDataReportEntity?,
//        @ModelAttribute("dataReportParamEntity") dataReportParamEntity: MsDataReportParametersEntity?,
//        @ModelAttribute("sampleCollectParamsEntity") sampleCollectParamsEntity: MsCollectionParametersEntity?,
//        @ModelAttribute("sampleSubmitParamsEntity") sampleSubmitParamsEntity: MsLaboratoryParametersEntity?,
//        @ModelAttribute("preliminaryOutletEntity") preliminaryOutletEntity: MsPreliminaryReportOutletsVisitedEntity?,
//        @ModelAttribute("sampleCollect") sampleCollect: MsSampleCollectionEntity?,
//        @ModelAttribute("sampleSubmit") sampleSubmit: MsSampleSubmissionEntity?,
//        @ModelAttribute("labParametersEntity") labParametersEntity: MsLaboratoryParametersEntity?,
//        @ModelAttribute("recommendedWorkPlan") recommendedWorkPlan: MsWorkPlanGeneratedEntity?,
//        @ModelAttribute("investInspectReport") investInspectReport: MsInspectionInvestigationReportEntity?,
//        @ModelAttribute("seizureDeclaration") seizureDeclaration: MsSeizureDeclarationEntity?,
//        @RequestParam("reportDate") reportDate: String?,
//        @RequestParam("ViewType") ViewType: String?,
////            @ModelAttribute("complaintRemarksEntity") complaintRemarksEntity: ComplaintRemarksEntity,
//        @RequestParam("workPlanID") workPlanID: Long,
//        @RequestParam("labParamID") labParamID: Long?,
////            @RequestParam("dataReportId") dataReportId: Long?,
//        @RequestParam("docType") docType: String,
//        results: BindingResult,
//        redirectAttributes: RedirectAttributes
//    ): String {
//        return try {
//            var resultRedirect = ""
//            SecurityContextHolder.getContext().authentication?.name
//                    ?.let { username ->
//                        userRepository.findByUserName(username)
//                                ?.let { loggedInUser ->
//                                    serviceMapsRepo.findByIdAndStatus(appId, 1)
//                                            ?.let { map ->
////                                                generateWorkPlanRepo.findByIdOrNull(workPlanID)
////                                                        ?.let { fetchedWorkPlanEntity ->
//                                                var fetchedFuelEntity: MsFuelInspectionEntity? = null
//                                                var fetchedWorkPlanEntity: MsWorkPlanGeneratedEntity? = null
//                                                if (workPlanID != 0L) {
//                                                    fetchedFuelEntity = iFuelInspectionRepo.findByIdOrNull(workPlanID)
//                                                    fetchedWorkPlanEntity = generateWorkPlanRepo.findByIdOrNull(workPlanID)
//                                                }
//
//
//                                                when {
//                                                    docType.equals("dataReportParam") -> {
//                                                        if (fetchedWorkPlanEntity != null) {
//                                                            dataReportRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
//                                                                    ?.let { fetchedDataReport ->
//                                                                        dataReportParamEntity
//                                                                                ?.let { datReportParam ->
//                                                                                    datReportParam.dataReportId = fetchedDataReport
//                                                                                    //                                                                                            datReportParam.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                                    datReportParam.createdOn = Timestamp.from(Instant.now())
//                                                                                    datReportParam.status = map.activeStatus
//
//                                                                                    dataReportParameterRepo.save(datReportParam)
//
//                                                                                    //                                                                                            fetchedWorkPlanEntity.dataReportGoodsStatus = map.activeStatus
//                                                                                    //                                                                                            genarateWorkPlanRepo.save(fetchedWorkPlanEntity)
//
//                                                                                    resultRedirect = "redirect:/api/ms/data-report-seized-goods?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                }
//
//                                                                    }
//                                                        }
//                                                    }
//                                                    docType.equals("sampleCollectParam") -> {
//                                                        sampleCollectParamsEntity
//                                                                ?.let { sampleCollectParam ->
//                                                                    sampleCollectParam.createdOn = Timestamp.from(Instant.now())
//                                                                    sampleCollectParam.status = map.activeStatus
//
//                                                                    if (ViewType.equals("fuel")) {
//                                                                        if (fetchedFuelEntity != null) {
//                                                                            sampleCollectRepo.findByMsFuelInspectionId(fetchedFuelEntity)
//                                                                                    ?.let { fetchedSampleCollect ->
//                                                                                        sampleCollectParam.sampleCollectionId = fetchedSampleCollect
//                                                                                        sampleCollectParameterRepo.save(sampleCollectParam)
//
//                                                                                        resultRedirect = "redirect:/api/ms/sample-collect?fuelInspectID=${fetchedFuelEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                    }
//                                                                        }
//
//                                                                    } else if (ViewType.equals("workPlan")) {
//                                                                        if (fetchedWorkPlanEntity != null) {
//                                                                            sampleCollectRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
//                                                                                    ?.let { fetchedSampleCollect ->
//                                                                                        sampleCollectParam.sampleCollectionId = fetchedSampleCollect
//                                                                                        sampleCollectParameterRepo.save(sampleCollectParam)
//                                                                                        resultRedirect = "redirect:/api/ms/sample-collect?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                    }
//                                                                        }
//                                                                    }
//                                                                }
//                                                    }
//                                                    docType.equals("sampleSubmitParam") -> {
//                                                        sampleSubmitParamsEntity
//                                                                ?.let { sampleSubmitParam ->
//                                                                    sampleSubmitParam.createdOn = Timestamp.from(Instant.now())
//                                                                    sampleSubmitParam.status = map.activeStatus
//
//                                                                    if (ViewType.equals("fuel")) {
//                                                                        if (fetchedFuelEntity != null) {
//                                                                            sampleSubmitRepo.findByMsFuelInspectionId(fetchedFuelEntity)
//                                                                                    ?.let { fetchedSampleSubmit ->
//                                                                                        sampleSubmitParam.sampleSubmissionId = fetchedSampleSubmit
//                                                                                        sampleSubmitParameterRepo.save(sampleSubmitParam)
//
//                                                                                        resultRedirect = "redirect:/api/ms/sample-submit?fuelInspectID=${fetchedFuelEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                    }
//                                                                        }
//
//                                                                    } else if (ViewType.equals("workPlan")) {
//                                                                        if (fetchedWorkPlanEntity != null) {
//                                                                            sampleSubmitRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
//                                                                                    ?.let { fetchedSampleSubmit ->
//                                                                                        sampleSubmitParam.sampleSubmissionId = fetchedSampleSubmit
//                                                                                        sampleSubmitParameterRepo.save(sampleSubmitParam)
//                                                                                        resultRedirect = "redirect:/api/ms/sample-submit?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                    }
//                                                                        }
//                                                                    }
//                                                                }
//
//                                                    }
//                                                    docType.equals("pleriminaryOutlets") -> {
//                                                        if (fetchedWorkPlanEntity != null) {
//                                                            preliminaryRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
//                                                                    ?.let { fetchedPreliminary ->
//                                                                        //                                                                                preliminaryOutletRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
//                                                                        //                                                                                        ?.let { fetchedSampleSubmit ->
//                                                                        preliminaryOutletEntity
//                                                                                ?.let { outlets ->
//                                                                                    outlets.preliminaryReportID = fetchedPreliminary
//                                                                                    outlets.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                                    outlets.createdOn = Timestamp.from(Instant.now())
//                                                                                    outlets.status = map.activeStatus
//
//                                                                                    preliminaryOutletRepo.save(outlets)
//
//                                                                                    resultRedirect = "redirect:/api/ms/preliminary?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                }
//
//                                                                        //                                                                                        }
//                                                                    }
//                                                        }
//                                                    }
//                                                    docType.equals("sampleBsNumber") -> {
//                                                        if (labParametersEntity != null) {
//                                                            if (ViewType.equals("fuel")) {
//                                                                if (labParamID != null) {
//                                                                    sampleSubmitParameterRepo.findByIdOrNull(labParamID)
//                                                                            ?.let { fetchedLabParam ->
//                                                                                fetchedLabParam.bsNumber = labParametersEntity.bsNumber
//                                                                                fetchedLabParam.modifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                                fetchedLabParam.modifiedOn = Timestamp.from(Instant.now())
//                                                                                val ssfWithBsNumber = sampleSubmitParameterRepo.save(fetchedLabParam)
//
//
//                                                                                ssfWithBsNumber.sampleSubmissionId?.msFuelInspectionId?.id
//                                                                                        ?.let { fuelInspectionID ->
//                                                                                            marketSurveillanceBpmn.msFmUpdateBSNumberAndSubmitComplete(fuelInspectionID, laboratoryID)
//                                                                                                    .let {
//                                                                                                        if (ViewType != null) {
//                                                                                                            if (simulateLabReportResults(ssfWithBsNumber, ViewType).equals(true)) {
//
//                                                                                                                iFuelInspectionRepo.findByIdOrNull(fuelInspectionID)
//                                                                                                                        ?.let { MsFuelInspectionEntity ->
//                                                                                                                            with(MsFuelInspectionEntity) {
////                                                                                                                                bsNumberStatus = 1
//                                                                                                                                lastModifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                                                                                lastModifiedOn = Timestamp.from(Instant.now())
//                                                                                                                            }
//                                                                                                                            iFuelInspectionRepo.save(MsFuelInspectionEntity)
//                                                                                                                            resultRedirect = "redirect:/api/ms/sample-submit?fuelInspectID=${ssfWithBsNumber.sampleSubmissionId?.msFuelInspectionId?.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                                                        }
//
//                                                                                                            }
//                                                                                                        }
//
//                                                                                                    }
//                                                                                        }
//
//                                                                            }
//                                                                }
//
//                                                            } else if (ViewType.equals("workPlan")) {
//                                                                if (labParamID != null) {
//                                                                    sampleSubmitParameterRepo.findByIdOrNull(labParamID)
//                                                                            ?.let { fetchedLabParam ->
//                                                                                fetchedLabParam.bsNumber = labParametersEntity.bsNumber
//                                                                                fetchedLabParam.modifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                                fetchedLabParam.modifiedOn = Timestamp.from(Instant.now())
//                                                                                val ssfWithBsNumber = sampleSubmitParameterRepo.save(fetchedLabParam)
//
////                                                                                if (fetchedWorkPlanEntity != null) {
////                                                                                    fetchedWorkPlanEntity.bsNumberStatus = map.activeStatus
////                                                                                    generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//
//
////                                                                                            ?.let { workPlanFetchedID ->
//                                                                                ssfWithBsNumber.sampleSubmissionId?.workPlanGeneratedID?.id
//                                                                                        ?.let { workPlanFetchedID ->
//                                                                                            marketSurveillanceBpmn.msMsUpdateBsNumberAndSubmitComplete(workPlanFetchedID, 1)
//                                                                                                    .let {
//                                                                                                        ssfWithBsNumber.sampleSubmissionId?.workPlanGeneratedID?.officerId?.let { it1 ->
//                                                                                                            marketSurveillanceBpmn.msMsLabProcessSamplesComplete(workPlanFetchedID, it1)
//                                                                                                                    .let {
//                                                                                                                        if (ViewType != null) {
//                                                                                                                            if (simulateLabReportResults(ssfWithBsNumber, ViewType).equals(true)) {
//                                                                                                                                generateWorkPlanRepo.findByIdOrNull(workPlanFetchedID)
//                                                                                                                                        ?.let { workPlanFetched ->
//                                                                                                                                            with(workPlanFetched) {
////                                                                                                                                                bsNumberStatus = 1
//                                                                                                                                                modifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                                                                                                modifiedOn = Timestamp.from(Instant.now())
//                                                                                                                                            }
//                                                                                                                                            generateWorkPlanRepo.save(workPlanFetched)
//                                                                                                                                            resultRedirect = "redirect:/api/ms/sample-submit?workPlanID=${ssfWithBsNumber.sampleSubmissionId?.workPlanGeneratedID?.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                                                                        }
//
//                                                                                                                            }
//                                                                                                                        }
//
//                                                                                                                    }
//                                                                                                        }
//
//                                                                                                    }
//                                                                                        }
////
//
//                                                                            }
//                                                                }
//                                                            }
//
//
//                                                        }
//                                                    }
//                                                    docType.equals("recommend") -> {
//                                                        recommendedWorkPlan
//                                                                ?.let { recommendWorkPlanEntity ->
//                                                                    if (fetchedWorkPlanEntity != null) {
//                                                                        cfgRecommendationRepo.findByIdOrNull(recommendWorkPlanEntity.recommendationId)
//                                                                                ?.let { cfgMsRecommendationEntity ->
//                                                                                    val recommendationID = recommendWorkPlanEntity.recommendationId
//                                                                                    fetchedWorkPlanEntity.hodRecommendationRemarks = recommendWorkPlanEntity.hodRecommendationRemarks
//                                                                                    fetchedWorkPlanEntity.hodRecommendation = cfgMsRecommendationEntity.recommendationName
//                                                                                    fetchedWorkPlanEntity.hodRecommendationStatus = map.activeStatus
//                                                                                    fetchedWorkPlanEntity.modifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                                    fetchedWorkPlanEntity.modifiedOn = Timestamp.from(Instant.now())
//
//                                                                                    val savedRecommendation = generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//
//                                                                                    if (recommendationID != null) {
//                                                                                        if (recommendationID.equals(recommendationDestructionID)) {
//                                                                                            /**
//                                                                                             * TODO: BPM To assign Task to an Officer after Recommendations Is added
//                                                                                             */
//                                                                                            KotlinLogging.logger { }.info { "Destruction Selected" }
//                                                                                            savedRecommendation.officerId?.let { commonDaoServices.findUserByID(it)?.email?.let { recommendationDestruction(it) } }
//                                                                                        }
//                                                                                    }
//                                                                                }
//                                                                    }
//
//
//                                                                }
//
//                                                    }
//                                                    docType.equals("chargeSheet") -> {
//                                                        chargeSheet
//                                                                ?.let { chargeSheetEntity ->
//                                                                    if (fetchedWorkPlanEntity != null) {
//                                                                        chargeSheetEntity.workPlanGeneratedID = fetchedWorkPlanEntity
//                                                                        chargeSheetEntity.status = map.activeStatus
//                                                                        chargeSheetEntity.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                        chargeSheetEntity.createdOn = Timestamp.from(Instant.now())
//
//                                                                        chargeSheetRepo.save(chargeSheetEntity)
//
//                                                                        fetchedWorkPlanEntity.chargeSheetStatus = map.activeStatus
//                                                                        generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//                                                                    }
//                                                                }
//
//                                                    }
//                                                    docType.equals("preliminary") -> {
//                                                        preliminaryReport
//                                                                ?.let { preliminaryReportEntity ->
//                                                                    if (fetchedWorkPlanEntity != null) {
////                                                                                preliminaryReportEntity.reportDate = Date.valueOf(LocalDate.parse(reportDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//                                                                        preliminaryReportEntity.workPlanGeneratedID = fetchedWorkPlanEntity
//                                                                        preliminaryReportEntity.status = map.activeStatus
//                                                                        preliminaryReportEntity.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                        preliminaryReportEntity.createdOn = Timestamp.from(Instant.now())
//
//                                                                        val preliminaryID = preliminaryRepo.save(preliminaryReportEntity)
//
//                                                                        fetchedWorkPlanEntity.msPreliminaryReportStatus = map.activeStatus
//                                                                        generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//
//                                                                        resultRedirect = "redirect:/api/ms/preliminary?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//
//                                                                    }
//                                                                }
//
//                                                    }
//                                                    docType.equals("finalReport") -> {
//                                                        if (fetchedWorkPlanEntity != null) {
//                                                            preliminaryRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
//                                                                    ?.let { fetchedPreliminaryReport ->
//                                                                        preliminaryReport
//                                                                                ?.let { preliminaryReportEntity ->
//                                                                                    fetchedPreliminaryReport.surveillanceConclusions = preliminaryReportEntity.surveillanceConclusions
//                                                                                    fetchedPreliminaryReport.surveillanceRecommendation = preliminaryReportEntity.surveillanceRecommendation
//                                                                                    fetchedPreliminaryReport.modifiedBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                                    fetchedPreliminaryReport.modifiedOn = Timestamp.from(Instant.now())
//
//                                                                                    val preliminaryID = preliminaryRepo.save(fetchedPreliminaryReport)
//
//                                                                                    //                                                                                            fetchedWorkPlanEntity.msPreliminaryReportStatus = map.activeStatus
//                                                                                    //                                                                                            genarateWorkPlanRepo.save(fetchedWorkPlanEntity)
//
//                                                                                    resultRedirect = "redirect:/api/ms/preliminary?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//
//                                                                                }
//                                                                    }
//                                                        }
//
//
//                                                    }
//                                                    docType.equals("final") -> {
//                                                        finalReport
//                                                                ?.let { finalReportEntity ->
//                                                                    if (fetchedWorkPlanEntity != null) {
//                                                                        finalReportEntity.workPlanGeneratedID = fetchedWorkPlanEntity
//                                                                        finalReportEntity.status = map.activeStatus
//                                                                        finalReportEntity.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                        finalReportEntity.createdOn = Timestamp.from(Instant.now())
//
//                                                                        val preliminaryID = finalReportRepo.save(finalReportEntity)
//
////                                                                        val hof = fetchedWorkPlanEntity.complaintDepartment?.let { fetchedWorkPlanEntity.region?.let { it1 -> findRegionUserId(designationIDHOF, it, map.activeStatus, it1) } }
////                                                                                KotlinLogging.logger { }.info { "Complaint id =${complaintRemark.complaintId?.id}  and  HOF ASSIGNED ID =  ${hof?.id}" }
//
//
////                                                                        hof?.id?.let { fetchedWorkPlanEntity.complaintId?.id?.let { it1 -> marketSurveillanceBpmn.msGenerateFinalReportComplete(it1, it) } }
//
////                                                                                fetchedWorkPlanEntity.chargeSheetStatus = map.activeStatus
////                                                                                genarateWorkPlanRepo.save(fetchedWorkPlanEntity)
//                                                                    }
//                                                                }
//
//                                                    }
//                                                    docType.equals("dataReport") -> {
//                                                        dataReport
//                                                                ?.let { dataReportEntity ->
//                                                                    if (fetchedWorkPlanEntity != null) {
//                                                                        dataReportEntity.workPlanGeneratedID = fetchedWorkPlanEntity
//                                                                        dataReportEntity.status = map.activeStatus
//                                                                        dataReportEntity.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                        dataReportEntity.createdOn = Timestamp.from(Instant.now())
//
//                                                                        dataReportRepo.save(dataReportEntity)
//
//                                                                        fetchedWorkPlanEntity.dataReportStatus = map.activeStatus
//                                                                        generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//
//                                                                        resultRedirect = "redirect:/api/ms/data-report-seized-goods?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                    }
//                                                                }
//
//                                                    }
//                                                    docType.equals("sampleCollect") -> {
//                                                        sampleCollect
//                                                                ?.let { sampleCollectEntity ->
//                                                                    sampleCollectEntity.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                    sampleCollectEntity.createdOn = Timestamp.from(Instant.now())
//                                                                    sampleCollectEntity.status = map.status
//
//                                                                    when {
//                                                                        ViewType.equals("fuel") -> {
//                                                                            if (fetchedFuelEntity != null) {
//                                                                                sampleCollectEntity.msFuelInspectionId = fetchedFuelEntity
//                                                                                sampleCollectRepo.save(sampleCollectEntity)
//
//                                                                                fetchedFuelEntity.sampleCollectionStatus = map.activeStatus
//                                                                                iFuelInspectionRepo.save(fetchedFuelEntity)
//                                                                                resultRedirect = "redirect:/api/ms/sample-collect?fuelInspectID=${fetchedFuelEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                            }
//
//                                                                        }
//                                                                        ViewType.equals("workPlan") -> {
//                                                                            if (fetchedWorkPlanEntity != null) {
//                                                                                sampleCollectEntity.workPlanGeneratedID = fetchedWorkPlanEntity
//                                                                                sampleCollectRepo.save(sampleCollectEntity)
//
//                                                                                fetchedWorkPlanEntity.sampleCollectionStatus = map.activeStatus
//                                                                                generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//                                                                                resultRedirect = "redirect:/api/ms/sample-collect?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                            }
//                                                                        }
//                                                                    }
//                                                                }
//
//
//                                                    }
//                                                    docType.equals("sampleSubmit") -> {
//
//                                                        sampleSubmit
//                                                                ?.let { sampleSubmitEntity ->
//                                                                    sampleSubmitEntity.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                    sampleSubmitEntity.createdOn = Timestamp.from(Instant.now())
//                                                                    sampleSubmitEntity.status = map.status
//                                                                    when {
//                                                                        ViewType.equals("fuel") -> {
//                                                                            if (fetchedFuelEntity != null) {
//                                                                                sampleCollectRepo.findByMsFuelInspectionId(fetchedFuelEntity)
//                                                                                        ?.let { fetchedSampleCollect ->
//                                                                                            sampleSubmitEntity.sampleCollectionNumber = fetchedSampleCollect
//                                                                                            sampleSubmitEntity.msFuelInspectionId = fetchedFuelEntity
//                                                                                            sampleSubmitRepo.save(sampleSubmitEntity)
//
//                                                                                            fetchedFuelEntity.sampleSubmittedStatus = map.activeStatus
//                                                                                            iFuelInspectionRepo.save(fetchedFuelEntity)
//                                                                                            resultRedirect = "redirect:/api/ms/sample-submit?fuelInspectID=${fetchedFuelEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                        }
//                                                                            }
//
//                                                                        }
//                                                                        ViewType.equals("workPlan") -> {
//                                                                            if (fetchedWorkPlanEntity != null) {
//                                                                                sampleCollectRepo.findByWorkPlanGeneratedID(fetchedWorkPlanEntity)
//                                                                                        ?.let { fetchedSampleCollect ->
//                                                                                            sampleSubmitEntity.sampleCollectionNumber = fetchedSampleCollect
//                                                                                            sampleSubmitEntity.workPlanGeneratedID = fetchedWorkPlanEntity
//                                                                                            sampleSubmitRepo.save(sampleSubmitEntity)
//
//                                                                                            fetchedWorkPlanEntity.sampleSubmittedStatus = map.activeStatus
//                                                                                            generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//                                                                                            resultRedirect = "redirect:/api/ms/sample-submit?workPlanID=${fetchedWorkPlanEntity.id}&currentPage=0&pageSize=10&ViewType=${ViewType}"
//                                                                                        }
//                                                                            }
//                                                                        }
//                                                                    }
//                                                                }
//
//                                                    }
//                                                    docType.equals("investInspectReport") -> {
//                                                        investInspectReport
//                                                                ?.let { investInspectReportEntity ->
//                                                                    if (fetchedWorkPlanEntity != null) {
//                                                                        investInspectReportEntity.workPlanGeneratedID = fetchedWorkPlanEntity
//                                                                        investInspectReportEntity.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                        investInspectReportEntity.createdOn = Timestamp.from(Instant.now())
//
//                                                                        investInspectReportRepo.save(investInspectReportEntity)
//
//                                                                        fetchedWorkPlanEntity.investInspectReportStatus = map.activeStatus
//                                                                        generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//                                                                    }
//                                                                }
//
//                                                    }
//                                                    docType.equals("seizureDeclaration") -> {
//                                                        seizureDeclaration
//                                                                ?.let { seizureDeclarationEntity ->
//                                                                    if (fetchedWorkPlanEntity != null) {
//                                                                        seizureDeclarationEntity.workPlanGeneratedID = fetchedWorkPlanEntity
//                                                                        seizureDeclarationEntity.createdBy = loggedInUser.firstName + " " + loggedInUser.lastName
//                                                                        seizureDeclarationEntity.createdOn = Timestamp.from(Instant.now())
//
//                                                                        seizureDeclarationRepo.save(seizureDeclarationEntity)
//
//                                                                        fetchedWorkPlanEntity.seizureDeclarationStatus = map.activeStatus
//                                                                        generateWorkPlanRepo.save(fetchedWorkPlanEntity)
//                                                                    }
//                                                                }
//
//                                                    }
//                                                    else -> {
//
//                                                    }
//                                                }
//
//                                                resultRedirect = redirectSiteWorkPlan + "${fetchedWorkPlanEntity?.uuid}"
//
////                                                        }
//
//                                            }
//                                }
//                    }
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.info { e }
//            "redirect:/"
//        } as String
//
//    }
//
////
////    fun saveCompliantDocuments(docFile: MultipartFile): String {
////
////        val compliantDocFileName: String = StringUtils.cleanPath(docFile.originalFilename!!)
////        if (compliantDocFileName.contains("..")) {
////            throw StorageException("Sorry! Filename contains invalid path sequence: compliant attached Document file = $compliantDocFileName")
////        }
////        return compliantDocFileName
////    }
//
//
//    @GetMapping("/load/uploads")
//    fun downloadWorkPlanDocument(
//            response: HttpServletResponse,
//            @RequestParam("uploadId") uploadId: Long
//    ) {
//        onsiteUploadRepo.findByIdOrNull(uploadId)
//                ?.let { doc ->
//                    response.contentType = doc.fileType
////                    response.setHeader("Content-Length", pdfReportStream.size().toString())
//                    response.addHeader("Content-Dispostion", "inline; filename=${doc.name};")
//                    response.outputStream
//                            .let { responseOutputStream ->
//                                responseOutputStream.write(doc.document)
//                                responseOutputStream.close()
//                            }
//
//
//                }
//
//
//    }
//
//
//    fun simulateLabReportResults(labEntity: MsLaboratoryParametersEntity, viewPage: String): Boolean {
//        var valueRetuned = false
//        with(labEntity) {
//            reportUid = commonDaoServices.generateUUIDString()
//            results = 13.5.toString()
//            testMethodNo = "KS 02-17"
//            requirements = "15Max"
//            modifiedOn = Timestamp.from(Instant.now())
//            modifiedBy = "LAB"
//            labResultStatus = 1
//        }
//        val ssfWithBsNumber = sampleSubmitParameterRepo.save(labEntity)
//
//        if (viewPage.equals("fuel")) {
//            ssfWithBsNumber.sampleSubmissionId?.msFuelInspectionId
//                    ?.let {
//                        iFuelInspectionOfficerRepo.findByMsFuelInspectionId(it)
//                                ?.let { msFuelInspectionOfficersEntity ->
//                                    msFuelInspectionOfficersEntity.assignedIo?.id?.let { it1 ->
//                                        marketSurveillanceBpmn.msFmLabProcessSamplesComplete(it.id, it1).let {
//                                            val recipient = msFuelInspectionOfficersEntity.assignedIo?.email
//                                            val subject = "LAB RESULTS"
//                                            val messageBody = "Check The System For The Lab results of attached Samples With the following BS Number ${ssfWithBsNumber.bsNumber} "
//                                            if (recipient != null) {
//                                                notifications.sendEmail(recipient, subject, messageBody)
//                                                valueRetuned = true
//                                            }
//                                        }
//                                    }
//
//                                }
//                    }
//                    ?: throw ExpectedDataNotFound("SAMPLE WITH THE ID DOES NOT EXIST")
//        } else if (viewPage.equals("workPlan")) {
//            ssfWithBsNumber.sampleSubmissionId?.workPlanGeneratedID
//                    ?.let { MsWorkplanGeneratedEntity ->
//                        MsWorkplanGeneratedEntity.officerId
//                                ?.let { msWorkPlanOfficersEntity ->
//                                    val recipient = commonDaoServices.findUserByID(msWorkPlanOfficersEntity).email
//                                    val subject = "LAB RESULTS"
//                                    val messageBody = "Check The System For The Lab results of attached Samples With the following BS Number ${ssfWithBsNumber.bsNumber} "
//                                    if (recipient != null) {
//                                        notifications.sendEmail(recipient, subject, messageBody)
//                                        valueRetuned = true
//                                    }
//
//                                }
//                    }
//                    ?: throw ExpectedDataNotFound("SAMPLE WITH THE ID DOES NOT EXIST")
//        }
//
//
//
//
//
//        return valueRetuned
//
//    }
//
//    fun recommendationDestruction(recipient: String): Boolean {
//        val subject = "DESTRUCTION NOTICE"
//        val messageBody = "The option for destruction was Recommended by the HOD, Send a Destruction notice to the client immediately"
//        notifications.sendEmail(recipient, subject, messageBody)
//
//        return true
//    }
//
//
//    fun destructionNotice(recipient: String): Boolean {
//        val subject = "DESTRUCTION NOTICE"
//        val messageBody = "Dear Client, \n\n" +
//                "Find the attached destruction Notices"
//        notifications.sendEmail(recipient, subject, messageBody)
//
//        return true
//    }
//
//    fun destructionReportStatus(recipient: String, hod: UsersEntity): Boolean {
//        val subject = "DESTRUCTION REPORT STATUS"
//        val messageBody = "Dear " + hod.firstName + " " + hod.lastName + ", \n\nThe Goods were destroyed and report has been submitted to the system And Status Marked as Coled "
//        notifications.sendEmail(recipient, subject, messageBody)
//
//        return true
//    }
//
//    fun endProcessAlertDI(bodyPart: String, director: UsersEntity, workplan: MsWorkPlanGeneratedEntity): Boolean {
//        val subject = "MS PROCESS CLOSSED"
//        val messageBody = "Dear " + director.firstName + " " + director.lastName + ", \n\nTHE WORK PLAN HAS BEEN CLOSED with the following remarks " + bodyPart + " "
//        director.email?.let { notifications.sendEmail(it, subject, messageBody) }
//
//        if (workplan.complaintId != null) {
//            val complainID = workplan.complaintId
//            complaintsRepo.findByIdOrNull(complainID)
//                    ?.let { complaintFound ->
//                        complaintFound.id?.let {
//                            complaintCustomersRepository.findByComplaintId(it)
//                                    ?.let { complaintCustomersEntity ->
//                                        val messageBody2 = "Dear " + complaintCustomersEntity.firstName + " " + complaintCustomersEntity.lastName + ", \n\nTHE WORK PLAN HAS BEEN CLOSED successfully with the following remarks " + bodyPart + " For the" +
//                                                "Complaint with Title " + complaintFound.complaintTitle + ""
//                                        director.email?.let { notifications.sendEmail(it, subject, messageBody) }
//                                    }
//                        }
//                    }
//
//
//        }
//        return true
//    }
//
//    fun findRegionUserId(designationID: Long, departmentsEntity: DepartmentsEntity, status: Int, regionsEntity: RegionsEntity): UsersEntity? {
//        var results: UsersEntity? = null
////        regionsRepository.findByIdOrNull(regionId)
////                ?.let { regionsEntity ->
//        designationRepository.findByIdOrNull(designationID)
//                ?.let { designationsEntity ->
//                    userProfilesRepository.findByDesignationIdAndRegionIdAndDepartmentIdAndStatus(designationsEntity, regionsEntity, departmentsEntity, status)
//                            ?.let { MSUSERREGION ->
//                                KotlinLogging.logger { }.info { "User in region = ${MSUSERREGION.regionId?.region} user Name =[${MSUSERREGION.userId?.firstName}] " }
//                                results = MSUSERREGION.userId!!
//
//                            }
//
////                            }
//
//
//                }
//        return results
//    }
//
//
//}
