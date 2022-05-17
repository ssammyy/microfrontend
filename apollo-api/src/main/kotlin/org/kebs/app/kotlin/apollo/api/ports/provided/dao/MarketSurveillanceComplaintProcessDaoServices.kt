package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.json.JSONObject
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.*
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ApiResponseModel
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestResultsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmittedPdfListRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile


@Service
class MarketSurveillanceComplaintProcessDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val msTypesRepo: IMsTypesRepository,
    private val complaintsRepo: IComplaintRepository,
    private val complaintCustomersRepo: IComplaintCustomersRepository,
    private val complaintLocationRepo: IComplaintLocationRepository,
    private val remarksRepo: IMsRemarksComplaintRepository,
    private val processNameRepo: IMsProcessNamesRepository,
    private val marketSurveillanceBpmn: MarketSurveillanceBpmn,
    private val complaintsDocRepo: IComplaintDocumentsRepository,
    private val limsServices: LimsServices,
    private val laboratoryRepo: ILaboratoryRepository,
    private val fuelRemediationInvoiceChargesRepo: IFuelRemediationChargesRepository,
    private val sampleSubmissionSavedPdfListRepo: IQaSampleSubmittedPdfListRepository,
    private val workPlanYearsCodesRepo: IWorkplanYearsCodesRepository,
    private val sampleCollectRepo: ISampleCollectionRepository,
    private val sampleLabTestResultsRepo: IQaSampleLabTestResultsRepository,
    private val fuelInspectionOfficerRepo: IFuelInspectionOfficerRepository,
    private val sampleCollectParameterRepo: ISampleCollectParameterRepository,
    private val sampleSubmitParameterRepo: ISampleSubmitParameterRepository,
    private val sampleSubmissionLabRepo: IQaSampleSubmissionRepository,
    private val msUploadRepo: IMsUploadsRepository,
    private val fuelRemediationRepo: IFuelRemediationRepository,
    private val sampleSubmitRepo: IMSSampleSubmissionRepository,
    private val fuelInspectionRepo: IFuelInspectionRepository,
    private val fuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
    private val invoiceDaoService: InvoiceDaoService,
    private val reportsDaoService: ReportsDaoService,
    private val serviceRequestsRepo: IServiceRequestsRepository,
    private val commonDaoServices: CommonDaoServices
) {
    final var complaintSteps: Int = 6
    private final val activeStatus: Int = 1

    final var appId = applicationMapProperties.mapMarketSurveillance

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController


//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewComplaint(body: NewComplaintDto, docFile: List<MultipartFile>): MSComplaintSubmittedSuccessful {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val msType = findMsTypeDetailsWithUuid(applicationMapProperties.mapMsComplaintTypeUuid)
            val sr: ServiceRequestsEntity
            var payload: String

            val complaint = saveNewComplaint(body.complaintDetails, map, msType, body.customerDetails)
            payload = "${commonDaoServices.createJsonBodyFromEntity(complaint)}"

            val complaintCustomers = saveNewComplaintCustomers(body.customerDetails, map, complaint.second)
            payload += "${commonDaoServices.createJsonBodyFromEntity(complaintCustomers)}"
//
            val complaintLocation = saveNewComplaintLocation(body.locationDetails, body.complaintDetails, map, complaint.second)
            payload += "${commonDaoServices.createJsonBodyFromEntity(complaintLocation)}"

            val uploads = saveComplaintFiles(docFile, map, complaint.second)


            val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOD)
            val regionsEntity =
                body.locationDetails.county?.let {
                    commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId?.let {
                        commonDaoServices.findRegionEntityByRegionID(
                            it, map.activeStatus
                        )
                    }
                }
//            val departmentsEntity = complaint.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
            val hod = commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(
                designationsEntity,
                regionsEntity?: throw ExpectedDataNotFound("No region value In the Complaint Location where [complaint ID =${complaint.second.id}]"),
                map.activeStatus
            ).userId

            val complaintID = complaint.second.id ?: throw  ExpectedDataNotFound("Complaint ID is missing")
            val hodID = hod?.id ?: throw  ExpectedDataNotFound("HOD user ID is missing")
            val customerEmail = body.customerDetails.emailAddress
                ?: throw  ExpectedDataNotFound("Complaint Customers Email is missing")
//            marketSurveillanceBpmn.startMSComplaintProcess(complaintID, hodID, customerEmail)
//                ?: throw  ExpectedDataNotFound("marketSurveillanceBpmn process error")
//
//            marketSurveillanceBpmn.msSubmitComplaintComplete(complaintID, hodID)

            with(complaint.second) {
                hodAssigned = hod.id
            }
            val updatedComplaint = complaintsRepo.save(complaint.second)
//            complaint = commonDaoServices.updateDetails(updatedComplaint, complaint) as ComplaintEntity
//            complaint.second =updatedComplaint

            sr = commonDaoServices.mapServiceRequestForSuccessUserNotRegistered(
                map,
                payload,
                "${body.customerDetails.firstName} ${body.customerDetails.lastName}"
            )
            val complainantEmailComposed = complaintSubmittedDTOEmailCompose(updatedComplaint)
            val complaintReceivedEmailComposed = complaintReceivedDTOEmailCompose(updatedComplaint, hod)
            commonDaoServices.sendEmailWithUserEmail(
                customerEmail,
                applicationMapProperties.mapMsComplaintAcknowledgementNotification,
                complainantEmailComposed,
                map,
                sr
            )
            commonDaoServices.sendEmailWithUserEntity(
                hod,
                applicationMapProperties.mapMsComplaintSubmittedHodNotification,
                complaintReceivedEmailComposed,
                map,
                sr
            )
            /**
             * TODO: Lets discuss to understand better how to keep track of schedules
             */

            updatedComplaint.let {
                return MSComplaintSubmittedSuccessful(it.referenceNumber,true,"Complaint submitted successful with ref number ${it.referenceNumber}, Check you Email for further Investigation", null)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            return MSComplaintSubmittedSuccessful(null,false,null, e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msComplaintLists(page: PageRequest): ApiResponseModel {
        val response: ApiResponseModel
        val auth = commonDaoServices.loggedInUserAuthentication()
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_IO_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findByAssignedIo(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOD_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findByHodAssigned(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_RM_READ" } -> {

                    response = listMsComplaints(complaintsRepo.findByHodAssigned(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_HOF_READ" } -> {
                    response =  listMsComplaints(complaintsRepo.findByHofAssigned(loggedInUser.id ?: throw ExpectedDataNotFound("Missing Logged In User ID"),page), map)
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "MS_DIRECTOR_READ" } -> {
                    response = listMsComplaints(complaintsRepo.findAllByOrderByIdDesc(page), map)
                }
                else -> throw ExpectedDataNotFound("Can't access this page Due to Invalid authority")
            }

        return  response
    }

    @PreAuthorize("hasAuthority('MS_IO_READ') or hasAuthority('MS_HOD_READ') or hasAuthority('MS_RM_READ') or hasAuthority('MS_HOF_READ') or hasAuthority('MS_DIRECTOR_READ')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun getComplaintDetailsBasedOnRefNo(referenceNo: String): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        return complaintInspectionMappingCommonDetails(referenceNo, map)
    }

    @PreAuthorize("hasAuthority('MS_MP_MODIFY')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateComplaintAcceptStatus(
        referenceNo: String,
        body: ComplaintApproveDto
    ) {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaintFound = findComplaintByRefNumber(referenceNo)
//        val fileOfficerSaved = fuelInspectionDetailsAssignOfficer(body, fileInspectionDetail, map, loggedInUser)

//        when (fileOfficerSaved.first.status) {
//            map.successStatus -> {
//                with(fileInspectionDetail) {
//                    userTaskId = applicationMapProperties.mapMSUserTaskNameOFFICER
//                    assignedOfficerStatus = map.activeStatus
//                }
//                val fileSaved2 = updateFuelInspectionDetails(fileInspectionDetail, map, loggedInUser)
//                when (fileSaved2.first.status) {
//                    map.successStatus -> {
//                        fileInspectionDetail = fileSaved2.second
//                        val officerDetails = commonDaoServices.findUserByID(fileOfficerSaved.second.assignedIo?:throw ExpectedDataNotFound("MISSING OFFICER ASSIGNED DETAILS"))
//                        val dataValue = FuelScheduledAssignedDTO()
//                        with(dataValue) {
//                            baseUrl = applicationMapProperties.baseUrlValue
//                            fullName = commonDaoServices.concatenateName(officerDetails)
//                            refNumber = fileSaved2.second.referenceNumber
//                            commentRemarks = fileOfficerSaved.second.remarks
//                            dateSubmitted = fileOfficerSaved.second.transactionDate
//                        }
//                        commonDaoServices.sendEmailWithUserEntity(officerDetails, applicationMapProperties.mapMsFuelAssignedIONotification, dataValue, map, fileSaved2.first)
//                        return fuelInspectionMappingCommonDetails(fileInspectionDetail, map, batchDetails)
//                    }
//                    else -> {
//                        throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileSaved2.first))
//                    }
//                }
//            }
//            else -> {
//                throw ExpectedDataNotFound(commonDaoServices.failedStatusDetails(fileOfficerSaved.first))
//            }
//        }
    }

    fun msComplaintMappedDetails(map: ServiceMapsEntity, complaintApproveDto: ComplaintApproveDto?,
                                 complaintRejectDto: ComplaintRejectDto?,
                                 complaintAdviceRejectDto: ComplaintAdviceRejectDto?,
                                 complaintAssignDto: ComplaintAssignDto?): ComplaintApproveRejectAssignDto {
        val comp = ComplaintApproveRejectAssignDto()
        with(comp) {
            when {
                complaintApproveDto != null -> {
                    division = complaintApproveDto.division
                    approved = map.activeStatus
                    approvedRemarks = complaintApproveDto.approvedRemarks
                }
                complaintRejectDto != null -> {
                    rejected = map.activeStatus
                    rejectedRemarks = complaintRejectDto.rejectedRemarks
                }
                complaintAdviceRejectDto != null -> {
                    rejected = map.activeStatus
                    mandateForOga = map.activeStatus
                    advisedWhereToRemarks = complaintAdviceRejectDto.advisedWhereToRemarks
                    rejectedRemarks = complaintAdviceRejectDto.rejectedRemarks
                }
                complaintAssignDto != null -> {
                    assignedIo = complaintAssignDto.assignedIo
                    assignedIoRemarks = complaintAssignDto.assignedRemarks
                    assignedIoStatus = map.activeStatus
                }
            }

        }
        return comp
    }


    fun msComplaintUpdate(refNumber: String, complaintUpdateDetails: ComplaintApproveRejectAssignDto, map: ServiceMapsEntity): AllComplaintsDetailsDto {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var (comp, complaintCustomersDetails, complaintLocationDetails) = complaintDetails(refNumber)
        val complaintID = getComplaintID(comp, refNumber)

        val (designationsEntity, regionsEntity, departmentsEntity) = returnAssignDetails(complaintLocationDetails, map, complaintID, comp)

        var hof: UsersEntity? = null

        //Before
        when {
            complaintUpdateDetails.approved == map.activeStatus -> {
                with(comp) {
                    hof = getUserWithProfile(departmentsEntity, regionsEntity, designationsEntity, map)
                    hofAssigned = hof?.id
                    msHofAssignedDate = commonDaoServices.getCurrentDate()
                    division = complaintUpdateDetails.division
                    approved = complaintUpdateDetails.approved
                    rejected = complaintUpdateDetails.rejected
                    approvedRemarks = complaintUpdateDetails.approvedRemarks
                    approvedBy = commonDaoServices.getUserName(loggedInUser)
                    approvedDate = commonDaoServices.getCurrentDate()
                }
            }
            complaintUpdateDetails.rejected == map.activeStatus && complaintUpdateDetails.mandateForOga != map.activeStatus -> {
                with(comp) {
                    rejected = complaintUpdateDetails.rejected
                    approved = complaintUpdateDetails.approved
                    rejectedRemarks = complaintUpdateDetails.rejectedRemarks
                    mandateForOga = complaintUpdateDetails.mandateForOga
                    advisedWhereto = complaintUpdateDetails.advisedWhereToRemarks
                    rejectedDate = commonDaoServices.getCurrentDate()
                    rejectedBy = commonDaoServices.getUserName(loggedInUser)
                }
            }
            complaintUpdateDetails.mandateForOga == map.activeStatus && complaintUpdateDetails.rejected == map.activeStatus -> {
                with(comp) {
                    rejected = complaintUpdateDetails.rejected
                    approved = complaintUpdateDetails.approved
                    rejectedRemarks = complaintUpdateDetails.rejectedRemarks
                    mandateForOga = complaintUpdateDetails.mandateForOga
                    advisedWhereto = complaintUpdateDetails.advisedWhereToRemarks
                    advisedWheretoOn = commonDaoServices.getCurrentDate()
                    advisedWheretoBy = commonDaoServices.getUserName(loggedInUser)
                }
            }
            complaintUpdateDetails.assignedIoStatus == map.activeStatus -> {
                with(comp) {
                    assignedRemarks = complaintUpdateDetails.assignedIoRemarks
                    assignedIoStatus = complaintUpdateDetails.assignedIoStatus
                    assignedIo = complaintUpdateDetails.assignedIo?.let { commonDaoServices.findUserByID(it).id }
                    assignedDate = commonDaoServices.getCurrentDate()
                    assignedBy = commonDaoServices.getUserName(loggedInUser)
                }
            }
        }


        //Update
        comp = updateComplaintDetailsInDB(comp, loggedInUser)
        val payload = "File Saved [${this::msComplaintUpdate.name} saved with id =[${comp.id}]]"
        val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)


        //after
        when {
            complaintUpdateDetails.approved == map.activeStatus -> {
                val hofID = hof?.id ?: throw ExpectedDataNotFound("HOF ID IS MISSING $refNumber")
                val hofEMAIL = hof?.email ?: throw ExpectedDataNotFound("HOF EMAIL IS MISSING for HOF WITH ID $hofID")
                val user = hof ?: throw ExpectedDataNotFound("HOF CANNOT BE NULL CHECK REF: $refNumber")
                val complainantEmailComposed = complaintReceivedDTOEmailCompose(comp, user)
                marketSurveillanceBpmn.msAcceptComplaintComplete(complaintID, hofID, true)
                commonDaoServices.sendEmailWithUserEmail(hofEMAIL, applicationMapProperties.mapMsComplaintApprovedHofNotification, complainantEmailComposed, map, sr)
            }
            complaintUpdateDetails.rejected == map.activeStatus && complaintUpdateDetails.mandateForOga != map.activeStatus -> {
                val complainantEmailComposed = complaintRejectedDTOEmailCompose(comp)
                marketSurveillanceBpmn.msAcceptComplaintComplete(complaintID, null, false)
                complaintCustomersDetails.emailAddress?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsComplaintAcknowledgementRejectionNotification, complainantEmailComposed, map, sr) }
            }
            complaintUpdateDetails.mandateForOga == map.activeStatus && complaintUpdateDetails.rejected == map.activeStatus -> {
                val complainantEmailComposed = complaintRejectedWithAdviceOGADTOEmailCompose(comp)
                marketSurveillanceBpmn.msAcceptComplaintComplete(complaintID, null, false)
                complaintCustomersDetails.emailAddress?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsComplaintAcknowledgementRejectionWIthOGANotification, complainantEmailComposed, map, sr) }
            }
            complaintUpdateDetails.assignedIoStatus == map.activeStatus -> {
                comp.assignedIo?.let { it1 ->
                    val user = commonDaoServices.findUserByID(it1)
                    val complainantEmailComposed = complaintReceivedDTOEmailCompose(comp, user)
                    marketSurveillanceBpmn.msAssignMsioComplete(complaintID, it1)
                    commonDaoServices.sendEmailWithUserEntity(user, applicationMapProperties.mapMsComplaintAssignedIONotification, complainantEmailComposed, map, sr)
                }
            }

        }

        return complaintInspectionMappingCommonDetails(refNumber, map)
    }

    fun returnAssignDetails(complaintLocationDetails: ComplaintLocationEntity?, map: ServiceMapsEntity, complaintID: Long, comp: ComplaintEntity): Triple<DesignationsEntity, RegionsEntity, DepartmentsEntity> {
        val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOF)
        val regionsEntity = complaintLocationDetails?.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Region Details for Complaint location With Complaint ID $complaintID, does not exist")
        val departmentsEntity = comp.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
            ?: throw ExpectedDataNotFound("Department details for complaint With ID $complaintID, does not Exist")
        return Triple(designationsEntity, regionsEntity, departmentsEntity)
    }

    fun complaintReceivedDTOEmailCompose(dataDetails: ComplaintEntity, user: UsersEntity): ComplaintAssignedDTO {
        val dataValue = ComplaintAssignedDTO()
        with(dataValue) {
            refNumber = dataDetails.referenceNumber
            baseUrl = applicationMapProperties.baseUrlValue
            fullName = commonDaoServices.concatenateName(user)
            assignIORemarks = dataDetails.assignedRemarks
            assignHOFRemarks = dataDetails.approvedRemarks
//            commentRemarks = dataDetails.approvedRemarks
            dateSubmitted = dataDetails.transactionDate
        }

        return dataValue
    }

    fun complaintRejectedDTOEmailCompose(dataDetails: ComplaintEntity): CustomerComplaintRejectedDTO {
        val dataValue = CustomerComplaintRejectedDTO()
        with(dataValue) {
            refNumber = dataDetails.referenceNumber
            baseUrl = applicationMapProperties.baseUrlValue
            complaintTitle = dataDetails.complaintTitle
            fullName = dataDetails.createdBy
            commentRemarks = dataDetails.rejectedRemarks
            dateSubmitted = dataDetails.transactionDate
        }

        return dataValue
    }

    fun complaintRejectedWithAdviceOGADTOEmailCompose(dataDetails: ComplaintEntity): CustomerComplaintRejectedWIthOGADTO {
        val dataValue = CustomerComplaintRejectedWIthOGADTO()
        with(dataValue) {
            refNumber = dataDetails.referenceNumber
            baseUrl = applicationMapProperties.baseUrlValue
            complaintTitle = dataDetails.complaintTitle
            fullName = dataDetails.createdBy
            commentRemarks = dataDetails.rejectedRemarks
            adviceRemarks = dataDetails.advisedWhereto
            dateSubmitted = dataDetails.transactionDate
        }

        return dataValue
    }

    fun getUserWithProfile(departmentsEntity: DepartmentsEntity, regionsEntity: RegionsEntity, designationsEntity: DesignationsEntity, map: ServiceMapsEntity): UsersEntity {
        return commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(designationsEntity, regionsEntity, departmentsEntity, map.activeStatus).userId
            ?: throw ExpectedDataNotFound("NO ${designationsEntity.designationName} Found on REGION ${regionsEntity.region} and Department ${departmentsEntity.department} Whose status is ${map.activeStatus}")
    }

    fun listMsComplaints(complaints: Page<ComplaintEntity>, map: ServiceMapsEntity): ApiResponseModel {
        val complaintList = mutableListOf<ComplaintsListDto>()
        complaints.toList().map { comp ->
            complaintList.add(
                ComplaintsListDto(
                    comp.referenceNumber,
                    comp.complaintTitle,
                    comp.targetedProducts,
//                    comp.complaintCategory,
                    comp.transactionDate,
                    comp.msProcessId?.let { findMsProcessComplaintByID(1, it)?.processName }
                )
            )
        }

        return commonDaoServices.setSuccessResponse(complaintList.stream().distinct(),complaints.totalPages,complaints.number,complaints.totalElements)
    }



    fun saveNewComplaint(complaintDto: ComplaintDto,
                         map: ServiceMapsEntity,
                         msType: MsTypesEntity,
                         complaintCustomersDto: ComplaintCustomersDto
    ): Pair<ServiceRequestsEntity, ComplaintEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var complaint = ComplaintEntity()
        try {

            with(complaint) {
                complaintTitle = complaintDto.complaintTitle
                complaintDetails = complaintDto.complaintDescription
                targetedProducts = complaintDto.productBrand
//                complaintDepartment = complaintDto.complaintCategory
//                standardCategory = complaintDto.productClassification
//                broadProductCategory = complaintDto.broadProductCategory
//                productCategory = complaintDto.productCategory
//                product = complaintDto.myProduct
//                productSubCategory = complaintDto.productSubcategory
                uuid = commonDaoServices.generateUUIDString()
                msTypeId = msType.id
                transactionDate = commonDaoServices.getCurrentDate()
                status = map.initStatus
                createdBy = complaintCustomersDto.firstName?.let { complaintCustomersDto.lastName?.let { it1 -> commonDaoServices.concatenateName(it, it1) } }
                createdOn = commonDaoServices.getTimestamp()
                submissionDate = commonDaoServices.getTimestamp()
                serviceMapsId = map.id
                msProcessStatus = map.inactiveStatus
                msProcessId = applicationMapProperties.msComplaintProcessOnlineSubmitted
                progressValue = progressSteps(complaintSteps).getInt("step-1")
                progressStep = "COMPLAINT SUBMITTED"
                referenceNumber = "${msType.markRef}${generateRandomText(7, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            }
            complaint = complaintsRepo.save(complaint)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(complaintDto)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, complaint)
    }

    fun saveComplaintProcess(complaintFound: ComplaintEntity,
                         map: ServiceMapsEntity,
                         processValueID: Long, user: UsersEntity
    ): Pair<ServiceRequestsEntity, ComplaintEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var complaint = complaintFound
        try {

            with(complaint) {
                msProcessId = processValueID
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            complaint = complaintsRepo.save(complaint)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            sr.payload = "${commonDaoServices.createJsonBodyFromEntity(complaint)}"
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepo.save(sr)

        }
        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, complaint)
    }

    fun saveComplaintFiles(
        docFile: List<MultipartFile>,
        map: ServiceMapsEntity,
        complaintDetails: ComplaintEntity
    ) {
        val savedAllFilesUploads =  false
        docFile.forEach { fileDoc ->
            var sr = commonDaoServices.createServiceRequest(map)
            try {
                var upload = MsUploadsEntity()
                with(upload) {
                    msComplaintId = complaintDetails.id
                    complaintUploads = 1
                    ordinaryStatus = 0
                    versionNumber = 1
                    name = fileDoc.originalFilename
                    fileType = fileDoc.contentType
                    documentType = "COMPLAINT FILE"
                    document = fileDoc.bytes
                    transactionDate = commonDaoServices.getCurrentDate()
                    status = 1
                    createdBy = complaintDetails.createdBy
                    createdOn = commonDaoServices.getTimestamp()
                }
                upload = msUploadRepo.save(upload)
            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message, e)
                //            KotlinLogging.logger { }.trace(e.message, e)
                sr.payload = "${fileDoc.bytes}"
                sr.status = sr.serviceMapsId?.exceptionStatus
                sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
                sr.responseMessage = e.message
                sr = serviceRequestsRepo.save(sr)

            }
            KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        }
    }

    fun updateComplaintDetailsInDB(updateCD: ComplaintEntity, user: UsersEntity): ComplaintEntity {
        with(updateCD) {
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        KotlinLogging.logger { }.info { "MY UPDATED COMPLAINT WITH ID =  ${updateCD.id}" }
        return complaintsRepo.save(updateCD)
    }

    fun getComplaintID(comp: ComplaintEntity, refNumber: String): Long {
        return comp.id ?: throw ExpectedDataNotFound("Complaint ID IS NULL for complaint with ref number $refNumber")
    }

    fun saveNewComplaintCustomers(complaintCustomersDto: ComplaintCustomersDto, map: ServiceMapsEntity, complaint: ComplaintEntity): ComplaintCustomersEntity {
        var complaintCustomers = ComplaintCustomersEntity()

        with(complaintCustomers) {
            firstName = complaintCustomersDto.firstName
            lastName = complaintCustomersDto.lastName
            mobilePhoneNumber = complaintCustomersDto.phoneNumber
            emailAddress = complaintCustomersDto.emailAddress
            postalAddress = complaintCustomersDto.postalAddress
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = complaintCustomersDto.firstName?.let { complaintCustomersDto.lastName?.let { it1 -> commonDaoServices.concatenateName(it, it1) } }
            createdOn = commonDaoServices.getTimestamp()
            complaintId = complaint.id
        }
        complaintCustomers = complaintCustomersRepo.save(complaintCustomers)
        return complaintCustomers
    }

    fun saveNewComplaintLocation(complaintLocationDto: ComplaintLocationDto, complaintDto: ComplaintDto, map: ServiceMapsEntity, complaint: ComplaintEntity): ComplaintLocationEntity {
        var complaintLocation = ComplaintLocationEntity()
        with(complaintLocation) {
            county = complaintLocationDto.county
            town = complaintLocationDto.town
            marketCenter = complaintLocationDto.marketCenter
            building = complaintLocationDto.buildingName
            productBrand = complaintDto.productBrand
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = complaint.createdBy
            createdOn = commonDaoServices.getTimestamp()
            complaintId = complaint.id
        }
        complaintLocation = complaintLocationRepo.save(complaintLocation)
        return complaintLocation
    }

    fun findMsTypeDetailsWithUuid(uuid: String): MsTypesEntity {
        msTypesRepo.findByUuid(uuid)
            ?.let { msTypeDetails ->
                return msTypeDetails
            }
            ?: throw Exception("MS Type Details with the following uuid = ${uuid}, does not Exist")
    }

    fun findMsProcessComplaintByID(complaintStatus: Int, processValueID: Long): MsProcessNamesEntity? {
        return processNameRepo.findByComplaintStatusAndId(complaintStatus, processValueID)
    }

    fun progressSteps(stepsValue: Int): JSONObject {
        val progressObject = JSONObject()
        var step = 0
        for (progress in stepsValue downTo 1) {
            step++
            val dividend = 100
            val quotient: Int = dividend / progress
//            val remainder = dividend % divisor
            progressObject.put("step-$step", quotient)

        }
        return progressObject
    }

    fun complaintSubmittedDTOEmailCompose(dataDetails: ComplaintEntity): CustomerComplaintSubmittedDTO {
        val dataValue = CustomerComplaintSubmittedDTO()
        with(dataValue) {
            refNumber = dataDetails.referenceNumber
            baseUrl = applicationMapProperties.baseUrlValue
            fullName = dataDetails.createdBy
            dateSubmitted = dataDetails.transactionDate
        }

        return dataValue
    }

    fun findComplaintByRefNumber(refNumber: String): ComplaintEntity {
        complaintsRepo.findByReferenceNumber(refNumber)
            ?.let { complaint ->
                return complaint
            }
            ?: throw ExpectedDataNotFound("Complaint with [refNumber = ${refNumber}], does not Exist")
    }

    fun complaintInspectionMappingCommonDetails(
        refNumber: String,
        map: ServiceMapsEntity
    ): AllComplaintsDetailsDto {
        val (comp, complaintCustomersDetails, complaintLocationDetails) = complaintDetails(refNumber)
        val complaintFilesSaved = findUploadedFileForComplaints(comp.id?: throw ExpectedDataNotFound("MISSING COMPLAINT ID"))
        val complaintRemarks = findRemarksForComplaints(comp.id?: throw ExpectedDataNotFound("MISSING COMPLAINT ID"))
        val officerList = commonDaoServices.findOfficersListBasedOnRegionCountyAndRole(
            applicationMapProperties.mapMSMappedOfficerROLEID,
            complaintLocationDetails.county ?: throw ExpectedDataNotFound("MISSING COMPLAINT COUNTY ID"),
            complaintLocationDetails.region ?: throw ExpectedDataNotFound("MISSING COMPLAINT REGION ID")
        )


//        val rapidTestStatus = mapRapidTestDto(fileInspectionDetail, map)
//        var rapidTestDone = false
//        if (rapidTestStatus!=null){
//            rapidTestDone = true
//        }
//
//        val compliantDetailsStatus = mapCompliantStatusDto(fileInspectionDetail, map)
//        var compliantStatusDone = false
//        if (compliantDetailsStatus!=null){
//            compliantStatusDone = true
//        }
//        val sampleCollected = findSampleCollectedDetailByFuelInspectionID(fileInspectionDetail.id)
//        val sampleCollectedParamList = sampleCollected?.id?.let { findAllSampleCollectedParametersBasedOnSampleCollectedID(it) }
//        val sampleCollectedDtoValues = sampleCollectedParamList?.let { mapSampleCollectedParamListDto(it) }?.let { mapSampleCollectedDto(sampleCollected, it) }
//
//        val sampleSubmitted = findSampleSubmissionDetailByFuelInspectionID(fileInspectionDetail.id)
//        val sampleSubmittedParamList = sampleSubmitted?.id?.let { findAllSampleSubmissionParametersBasedOnSampleSubmissionID(it) }
//        val sampleSubmittedDtoValues = sampleSubmittedParamList?.let { mapSampleSubmissionParamListDto(it) }?.let { mapSampleSubmissionDto(sampleSubmitted, it) }
//
//        val labResultsParameters = sampleSubmitted?.bsNumber?.let { findSampleLabTestResultsRepoBYBSNumber(it) }
//        val ssfDetailsLab = findSampleSubmittedBYFuelInspectionId(fileInspectionDetail.id)
//        val savedPDFFilesLims = ssfDetailsLab?.id?.let { findSampleSubmittedListPdfBYSSFid(it)?.let { ssfDetails->mapLabPDFFilesListDto(ssfDetails) } }
////        val savedPDFFilesLims = ssfDetailsLab?.id?.let { findSampleSubmittedListPdfBYSSFid(it)?.let { mapLabPDFFilesListDto(it) } }
//        val ssfResultsListCompliance = ssfDetailsLab?.let { mapSSFComplianceStatusDetailsDto(it) }
//        val limsPDFFiles = ssfDetailsLab?.bsNumber?.let { mapLIMSSavedFilesDto(it,savedPDFFilesLims)}
//        val labResultsDto = mapLabResultsDetailsDto(ssfResultsListCompliance,savedPDFFilesLims,limsPDFFiles,labResultsParameters?.let { mapLabResultsParamListDto(it) })
//        val remediationDetails = findFuelScheduledRemediationDetails(fileInspectionDetail.id)
//        val invoiceRemediationDetails = fuelRemediationInvoiceRepo.findFirstByFuelInspectionId(fileInspectionDetail.id)
//        var invoiceCreatedStatus = false
//        if (invoiceRemediationDetails.isNotEmpty()){
//            invoiceCreatedStatus = true
//        }
//        val fuelRemediationDto = remediationDetails?.let { mapFuelRemediationDto(it,invoiceCreatedStatus) }
        return mapComplaintInspectionDto(
            mapComplaintDto(comp, complaintCustomersDetails, complaintLocationDetails, complaintFilesSaved, map),
            null,
            complaintRemarks,
            null,
            null,
            null,
            null,
        )
    }

    fun mapComplaintInspectionDto(
        complaintsDetails: ComplaintsDetailsDto,
        officerList: List<UsersEntity>?,
        remarksList: List<MsRemarksEntity>?,
        officersAssigned : UsersEntity?,
        sampleCollected: SampleCollectionDto?,
        sampleSubmitted: SampleSubmissionDto?,
        sampleLabResults: MSSSFLabResultsDto?,
    ): AllComplaintsDetailsDto {
        return AllComplaintsDetailsDto(
            complaintsDetails,
            officerList?.let { mapOfficerListDto(it) },
            officersAssigned?.let { mapOfficerDto (it) },
            remarksList?.let { mapRemarksListDto(it) },
            sampleCollected,
            sampleSubmitted,
            sampleLabResults
        )
    }


    fun mapOfficerDto(officer: UsersEntity): MsUsersDto {
        return MsUsersDto(
            officer.id,
            officer.firstName,
            officer.lastName,
            officer.userName,
            officer.email,
            officer.status == 1,
        )

    }

    fun mapComplaintDto(comp: ComplaintEntity, complaintCustomersDetails: ComplaintCustomersEntity, complaintLocationDetails: ComplaintLocationEntity, complaintFilesSaved: List<MsUploadsEntity>?, map: ServiceMapsEntity): ComplaintsDetailsDto {
        return  ComplaintsDetailsDto(
            comp.id,
            comp.referenceNumber,
            complaintCustomersDetails.firstName?.let { complaintCustomersDetails.lastName?.let { it1 -> commonDaoServices.concatenateName(it, it1) } },
            complaintCustomersDetails.emailAddress,
            complaintCustomersDetails.postalAddress,
            complaintCustomersDetails.mobilePhoneNumber,
            comp.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it).department },
            comp.complaintTitle,
            comp.complaintDetails,
            comp.broadProductCategory?.let { commonDaoServices.findBroadCategoryByID(it).category },
            comp.productCategory?.let { commonDaoServices.findProductCategoryByID(it).name },
            comp.productSubCategory?.let { commonDaoServices.findProductSubCategoryByID(it).name },
            comp.product?.let { commonDaoServices.findProductByID(it).name },
            complaintLocationDetails.productBrand,
            complaintLocationDetails.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county },
            complaintLocationDetails.town?.let { commonDaoServices.findTownEntityByTownId(it).town },
            complaintLocationDetails.marketCenter,
            complaintLocationDetails.building,
            comp.transactionDate,
            comp.msProcessId?.let { findMsProcessComplaintByID(1, it)?.processName },
//            msInspectionOfficer,
//            msDivisionList,
            comp.approved,
            comp.assignedIoStatus,
            comp.rejected,
            complaintFilesSaved?.let { mapFileListDto(it) }
        )

    }

    fun mapOfficerListDto(officerList: List<UsersEntity>): List<MsUsersDto> {
        return officerList.map {
            MsUsersDto(
                it.id,
                it.firstName,
                it.lastName,
                it.userName,
                it.email,
                it.status == 1,
            )
        }
    }

    fun mapRemarksListDto(remarksList: List<MsRemarksEntity>): List<MSRemarksDto> {
        return remarksList.map {
            MSRemarksDto(
                it.id,
                it.remarksDescription,
                it.processBy,
                it.processName
            )
        }
    }

    fun mapFileListDto(fileFoundList: List<MsUploadsEntity>): List<ComplaintsFilesFoundDto> {
        return fileFoundList.map {
            ComplaintsFilesFoundDto(
                it.id,
                it.documentType,
                it.name,
                it.fileType
            )
        }
    }


    fun findUploadedFileForComplaints(complaintID: Long): List<MsUploadsEntity>? {
        return msUploadRepo.findAllByMsComplaintIdAndComplaintUploads(complaintID, 1)
    }

    fun findRemarksForComplaints(complaintID: Long): List<MsRemarksEntity>? {
        return remarksRepo.findAllByComplaintId(complaintID)
    }

    private fun complaintDetails(refNumber: String): Triple<ComplaintEntity, ComplaintCustomersEntity, ComplaintLocationEntity> {
        val comp = findComplaintByRefNumber(refNumber)
        val complaintCustomersDetails = findComplaintCustomerByComplaintID(comp.id?: throw ExpectedDataNotFound("Missing complaint ID"))
        val complaintLocationDetails = findComplaintLocationByComplaintID(comp.id?: throw ExpectedDataNotFound("Missing complaint ID"))
        return Triple(comp, complaintCustomersDetails, complaintLocationDetails)
    }

    fun findComplaintCustomerByComplaintID(complaintID: Long): ComplaintCustomersEntity {
        complaintCustomersRepo.findByComplaintId(complaintID)
            ?.let { complaintCustomer ->
                return complaintCustomer
            }
            ?: throw ExpectedDataNotFound("Complaint Customer with Complaint [ID = ${complaintID}], does not Exist")
    }

    fun findComplaintLocationByComplaintID(complaintID: Long): ComplaintLocationEntity {
        complaintLocationRepo.findByComplaintId(complaintID)
            ?.let { complaintLocation ->
                return complaintLocation
            }
            ?: throw ExpectedDataNotFound("Complaint Location with Complaint [ID = ${complaintID}], does not Exist")
    }


    fun listMsDepartments(status: Int): List<MsDepartmentDto>? {
        val directoratesEntity = commonDaoServices.findDirectorateByID(applicationMapProperties.mapMsDirectorateID)
        return commonDaoServices.findDepartmentByDirectorate(directoratesEntity, status).sortedBy { it.id }.map { MsDepartmentDto(it.id, it.department, it.descriptions, it.directorateId?.id, it.status == 1) }
    }

    fun listMsDivision(status: Int, departmentID: Long): List<MsDivisionDto>? {
        val department = commonDaoServices.findDepartmentByID(departmentID)
        return commonDaoServices.findDivisionByDepartmentId(department, status).sortedBy { it.id }.map { MsDivisionDto(it.id, it.division, it.descriptions, it.status, it.departmentId?.id) }
    }
}




