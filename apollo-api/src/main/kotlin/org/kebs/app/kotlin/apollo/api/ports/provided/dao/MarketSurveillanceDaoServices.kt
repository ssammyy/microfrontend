package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.json.JSONObject
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.criteria.SearchCriteria
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.ComplaintAssignedDTO
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.CustomerComplaintRejectedDTO
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.CustomerComplaintRejectedWIthOGADTO
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.CustomerComplaintSubmittedDTO
import org.kebs.app.kotlin.apollo.api.ports.provided.spec.ComplaintSpecification
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintCustomersEntity
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintLocationEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsTypesEntity
import org.kebs.app.kotlin.apollo.store.repo.IDivisionsRepository
import org.kebs.app.kotlin.apollo.store.repo.IWorkPlanCreatedRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IConsignmentItemsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDestinationInspectionFeeRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*


@Service
class MarketSurveillanceDaoServices(
        private val msTypesRepo: IMsTypesRepository,
        private val complaintsRepo: IComplaintRepository,
        private val complaintCustomersRepo: IComplaintCustomersRepository,
        private val complaintLocationRepo: IComplaintLocationRepository,
        private val complaintsDocRepo: IComplaintDocumentsRepository,
        private val onsiteUploadRepo: IOnsiteUploadRepository,
        private val divisionsRepo: IDivisionsRepository,
        private val generateWorkPlanRepo: IWorkPlanGenerateRepository,
        private val workPlanYearsCodesRepository: IWorkplanYearsCodesRepository,
        private val workPlanCreatedRepository: IWorkPlanCreatedRepository,

        private val iConsignmentItemsRepo: IConsignmentItemsRepository,
        private val applicationMapProperties: ApplicationMapProperties,
        private val iDemandNoteRepo: IDemandNoteRepository,
        private val commonDaoServices: CommonDaoServices,
        private val iDestinationInspectionFeeRepo: IDestinationInspectionFeeRepository,
        private val marketSurveillanceBpmn: MarketSurveillanceBpmn,


        ) {

    final var redirectSiteComplaintOnLine = "redirect:/"
    final var redirectSiteComplaintList = "redirect:/api/ms/ms-list?msTypeUuid="
    final var redirectSiteComplaintPage = "redirect:/api/ms/ms-list?msTypeUuid="
    final val redirectSiteMsCreatedWorkPlanActivities = "redirect:/api/ms/ms-list-workPlans?createdWorkPlanUuid="
    final val redirectSiteMsWorkPlanActivityPage = "redirect:/api/ms/workPlan-detail?workPlanUuid="

    final var complaintSteps: Int = 6
    final var workPlanSteps: Int = 15


    /*******************************************NEW MS DAO SERVICES FOR JSON OBJECTS PAGES******************************
     **************************************************************************************************************
     ***************************************************************************************************************/

    fun listMsTypes(status: Int): List<MSTypeDto>? {
        return msTypesRepo.findByStatus(status)?.sortedBy { it.id }?.map { MSTypeDto(it.uuid, it.typeName, it.markRef, it.description, it.status) }
    }

    fun listMsDepartments(status: Int): List<MsDepartmentDto>? {
        val directoratesEntity = commonDaoServices.findDirectorateByID(applicationMapProperties.mapMsDirectorateID)
        return commonDaoServices.findDepartmentByDirectorate(directoratesEntity, status).sortedBy { it.id }.map { MsDepartmentDto(it.id, it.department, it.descriptions, it.directorateId?.id, it.status == 1) }
    }

    fun listMsDivision(status: Int, departmentID: Long): List<MsDivisionDto>? {
        val department = commonDaoServices.findDepartmentByID(departmentID)
        return commonDaoServices.findDivisionByDepartmentId(department, status).sortedBy { it.id }.map { MsDivisionDto(it.id, it.division, it.descriptions, it.status, it.departmentId?.id) }
    }

    fun saveNewComplaint(complaintDto: ComplaintDto, msType: MsTypesEntity, map: ServiceMapsEntity, complaintCustomersDto: ComplaintCustomersDto): ComplaintEntity {
        var complaint = ComplaintEntity()

        with(complaint) {
            complaintTitle = complaintDto.complaintTitle
            complaintDetails = complaintDto.complaintDescription
            complaintDepartment = complaintDto.complaintCategory
            standardCategory = complaintDto.productClassification
            broadProductCategory = complaintDto.broadProductCategory
            productCategory = complaintDto.productCategory
            product = complaintDto.myProduct
            productSubCategory = complaintDto.productSubcategory
            uuid = commonDaoServices.generateUUIDString()
            msTypeId = msType.id
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.initStatus
            createdBy = complaintCustomersDto.firstName?.let { complaintCustomersDto.lastName?.let { it1 -> commonDaoServices.concatenateName(it, it1) } }
            createdOn = commonDaoServices.getTimestamp()
            submissionDate = commonDaoServices.getTimestamp()
            serviceMapsId = map.id
            msProcessStatus = map.inactiveStatus
            progressValue = progressSteps(complaintSteps).getInt("step-1")
            progressStep = "COMPLAINT SUBMITTED"
            referenceNumber = "${msType.markRef}${generateRandomText(7, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
        }
        complaint = complaintsRepo.save(complaint)

        return complaint
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


    fun complaintDocumentsSave(complaintFilesDto: ComplaintFilesDto, map: ServiceMapsEntity, complaint: ComplaintEntity): MutableList<Long> {
        val complaintDocIDList = mutableListOf<Long>()
        complaintFilesDto.fileDetails?.forEach { documents ->
            var complaintDocumentsEntity = ComplaintDocumentsEntity()
            with(complaintDocumentsEntity) {
                name = documents.name
                fileType = documents.fileType
//                documentType = doc
                document = documents.document
                transactionDate = commonDaoServices.getCurrentDate()
                status = map.activeStatus
                createdBy = complaint.createdBy
                createdOn = commonDaoServices.getTimestamp()
                complaintId = complaint.id
            }
            complaintDocumentsEntity = complaintsDocRepo.save(complaintDocumentsEntity)
            complaintDocIDList.add(complaintDocumentsEntity.id)
        }

        return complaintDocIDList
    }


    fun listMsComplaints(complaints: List<ComplaintEntity>, map: ServiceMapsEntity): List<ComplaintsDto> {
        val complaintList = mutableListOf<ComplaintsDto>()
        complaints.map { comp ->

            complaintList.add(
                    ComplaintsDto(
                            comp.referenceNumber,
                            comp.createdBy,
                        comp.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it).department },
                        comp.complaintTitle,
                        comp.transactionDate,
                        comp.progressStep
                    )
            )
        }
        return complaintList.sortedBy { it.date }
    }

    fun complaintSearchResultListing(search: ComplaintSearchValues): List<ComplaintsDto>? {
        val complaintList = mutableListOf<ComplaintsDto>()
        val refNumberSpec: ComplaintSpecification?
        var dateSpec: ComplaintSpecification? = null


        refNumberSpec = ComplaintSpecification(SearchCriteria("referenceNumber", ":", search.refNumber))
        search.date?.let {
            dateSpec = ComplaintSpecification(SearchCriteria("transactionDate", ":", search.date))
        }

        complaintsRepo.findAll(refNumberSpec.or(dateSpec))
            .map { comp ->
                complaintList.add(
                    ComplaintsDto(
                        comp.referenceNumber,
                        comp.createdBy,
                        comp.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it).department },
                        comp.complaintTitle,
                        comp.transactionDate,
                        comp.progressStep
                    )
                )
            }

        return complaintList.sortedBy { it.date }

    }

    fun msComplaint(refNumber: String, map: ServiceMapsEntity): ComplaintsDetailsDto {
//        complaints.map { comp ->
        val (comp, complaintCustomersDetails, complaintLocationDetails) = complaintDetails(refNumber)
        val complaintID = getComplaintID(comp, refNumber)
        var msDivisionList = mutableListOf<MsDivisionDto>()
        val msInspectionOfficer = mutableListOf<MsUsersDto>()
        when (comp.assignedIo) {
            null -> {
                val (designationsEntity, regionsEntity, departmentsEntity) = returnAssignDetails(
                    complaintLocationDetails,
                    map,
                    complaintID,
                    comp
                )
                val officerList = commonDaoServices.findUserProfileListWithRegionDesignationDepartmentAndStatus(regionsEntity, designationsEntity, departmentsEntity, map.activeStatus)
                officerList.forEach { userProfilesEntity ->
                    msInspectionOfficer.add(MsUsersDto(
                            userProfilesEntity.userId?.id,
                            userProfilesEntity.userId?.firstName,
                            userProfilesEntity.userId?.lastName,
                            userProfilesEntity.userId?.userName,
                            userProfilesEntity.userId?.email,
                            userProfilesEntity.userId?.status == 1
                    )
                    )
                }
            }
        }

        when (comp.division) {
            null -> {
                msDivisionList = comp.complaintDepartment?.let { listMsDivision(map.activeStatus, it) } as MutableList<MsDivisionDto>

            }
        }


        return ComplaintsDetailsDto(
                comp.id,
                comp.referenceNumber,
                complaintCustomersDetails?.firstName?.let { complaintCustomersDetails.lastName?.let { it1 -> commonDaoServices.concatenateName(it, it1) } },
                complaintCustomersDetails?.emailAddress,
                complaintCustomersDetails?.postalAddress,
                complaintCustomersDetails?.mobilePhoneNumber,
                comp.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it).department },
                comp.complaintTitle,
                comp.complaintDetails,
                comp.broadProductCategory?.let { commonDaoServices.findBroadCategoryByID(it).category },
                comp.productCategory?.let { commonDaoServices.findProductCategoryByID(it).name },
                comp.productSubCategory?.let { commonDaoServices.findProductSubCategoryByID(it).name },
                comp.product?.let { commonDaoServices.findProductByID(it).name },
                complaintLocationDetails?.productBrand,
                complaintLocationDetails?.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county },
                complaintLocationDetails?.town?.let { commonDaoServices.findTownEntityByTownId(it).town },
                complaintLocationDetails?.marketCenter,
                complaintLocationDetails?.building,
                comp.transactionDate,
                comp.progressStep,
                msInspectionOfficer,
                msDivisionList,
                comp.approved,
                comp.assignedIoStatus,
                comp.rejected

        )
    }

    private fun complaintDetails(refNumber: String): Triple<ComplaintEntity, ComplaintCustomersEntity?, ComplaintLocationEntity?> {
        val comp = findComplaintByRefNumber(refNumber)
        val complaintCustomersDetails = comp.id?.let { findComplaintCustomerByComplaintID(it) }
        val complaintLocationDetails = comp.id?.let { findComplaintLocationByComplaintID(it) }
        return Triple(comp, complaintCustomersDetails, complaintLocationDetails)
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

    fun msComplaintUpdate(refNumber: String, complaintUpdateDetails: ComplaintApproveRejectAssignDto, map: ServiceMapsEntity): ComplaintsDetailsDto {
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
                complaintCustomersDetails?.emailAddress?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsComplaintAcknowledgementRejectionNotification, complainantEmailComposed, map, sr) }
            }
            complaintUpdateDetails.mandateForOga == map.activeStatus && complaintUpdateDetails.rejected == map.activeStatus -> {
                val complainantEmailComposed = complaintRejectedWithAdviceOGADTOEmailCompose(comp)
                marketSurveillanceBpmn.msAcceptComplaintComplete(complaintID, null, false)
                complaintCustomersDetails?.emailAddress?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsComplaintAcknowledgementRejectionWIthOGANotification, complainantEmailComposed, map, sr) }
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
        //Todo: ask @KEN if its genuine to call this here the refNumber
        return msComplaint(refNumber, map)
    }

    private fun getComplaintID(comp: ComplaintEntity, refNumber: String): Long {
        return comp.id
                ?: throw ExpectedDataNotFound("Complaint ID IS NULL for complaint with ref number $refNumber")
    }

    fun returnAssignDetails(complaintLocationDetails: ComplaintLocationEntity?, map: ServiceMapsEntity, complaintID: Long, comp: ComplaintEntity): Triple<DesignationsEntity, RegionsEntity, DepartmentsEntity> {
        val designationsEntity = commonDaoServices.findDesignationByID(applicationMapProperties.mapMsComplaintAndWorkPlanDesignationHOF)
        val regionsEntity = complaintLocationDetails?.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
                ?: throw ExpectedDataNotFound("Region Details for Complaint location With Complaint ID $complaintID, does not exist")
        val departmentsEntity = comp.complaintDepartment?.let { commonDaoServices.findDepartmentByID(it) }
                ?: throw ExpectedDataNotFound("Department details for complaint With ID $complaintID, does not Exist")
        return Triple(designationsEntity, regionsEntity, departmentsEntity)
    }

    /*******************************************OLD MS DAO SERVICES FOR THYMELEAF PAGES******************************
     **************************************************************************************************************
     ***************************************************************************************************************/

    fun findMsTypeDetailsWithUuid(uuid: String): MsTypesEntity {
        msTypesRepo.findByUuid(uuid)
                ?.let { msTypeDetails ->
                    return msTypeDetails
                }
                ?: throw Exception("MS Type Details with the following uuid = ${uuid}, does not Exist")
    }

    fun assignComplaintDetails(complaintId: Long?): ComplaintEntity? = complaintsRepo.findByIdOrNull(complaintId)

    fun msDepartmentList(map: ServiceMapsEntity): List<DepartmentsEntity> {
        val directoratesEntity = commonDaoServices.findDirectorateByID(applicationMapProperties.mapMsDirectorateID)
        return commonDaoServices.findDepartmentByDirectorate(directoratesEntity, map.activeStatus)
    }

    fun findMsTypeDetailsWithId(ID: Long): MsTypesEntity {
        msTypesRepo.findByIdOrNull(ID)
                ?.let { msTypeDetails ->
                    return msTypeDetails
                }
                ?: throw Exception("MS Type Details with the following id = ${ID}, does not Exist")
    }

    fun findComplaintByID(complaintID: Long): ComplaintEntity {
        complaintsRepo.findByIdOrNull(complaintID)
                ?.let { complaint ->
                    return complaint
                }
                ?: throw ExpectedDataNotFound("Complaint with [ID = ${complaintID}], does not Exist")
    }

    fun findWorkPlanActivityWithUuid(workPlanUuid: String): MsWorkPlanGeneratedEntity {
        generateWorkPlanRepo.findByUuid(workPlanUuid)
                ?.let { activityWorkPlan ->
                    return activityWorkPlan
                }
                ?: throw ExpectedDataNotFound("WorkPlan Activity with [UUID = ${workPlanUuid}], does not Exist")
    }

    fun updateComplaintDetailsInDB(updateCD: ComplaintEntity, user: UsersEntity): ComplaintEntity {
        with(updateCD) {
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        KotlinLogging.logger { }.info { "MY UPDATED COMPLAINT WITH ID =  ${updateCD.id}" }
        return complaintsRepo.save(updateCD)
    }

    fun getUserWithProfile(departmentsEntity: DepartmentsEntity, regionsEntity: RegionsEntity, designationsEntity: DesignationsEntity, map: ServiceMapsEntity): UsersEntity {
        return commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(designationsEntity, regionsEntity, departmentsEntity, map.activeStatus).userId
                ?: throw ExpectedDataNotFound("NO ${designationsEntity.designationName} Found on REGION ${regionsEntity.region} and Department ${departmentsEntity.department} Whose status is ${map.activeStatus}")
    }

    fun findComplaintByUuid(complaintUuid: String): ComplaintEntity {
        complaintsRepo.findByUuid(complaintUuid)
                ?.let { complaint ->
                    return complaint
                }
                ?: throw ExpectedDataNotFound("Complaint with [uuid = ${complaintUuid}], does not Exist")
    }

    fun findComplaintByRefNumber(refNumber: String): ComplaintEntity {
        complaintsRepo.findByReferenceNumber(refNumber)
                ?.let { complaint ->
                    return complaint
                }
                ?: throw ExpectedDataNotFound("Complaint with [refNumber = ${refNumber}], does not Exist")
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

    fun findComplaintDocByComplaintID(complaintID: Long): ComplaintDocumentsEntity {
        complaintsDocRepo.findByComplaintId(complaintID)
                ?.let { complaintDocument ->
                    return complaintDocument
                }
                ?: throw ExpectedDataNotFound("Complaint Location with Complaint [ID = ${complaintID}], does not Exist")
    }

    fun findCreatedWorkPlanWIthUuid(createdWorkPlanUuid: String): WorkPlanCreatedEntity {
        workPlanCreatedRepository.findByUuid(createdWorkPlanUuid)
                ?.let { createdWorkPlan ->
                    return createdWorkPlan
                }
                ?: throw ExpectedDataNotFound("Created Work Plan with the following [uuid = ${createdWorkPlanUuid}], does not Exist")
    }

    fun findALlWorkPlanDetailsAssociatedWithWorkPlanID(createdWorkPlanID: Long): List<MsWorkPlanGeneratedEntity> {
        generateWorkPlanRepo.findByWorkPlanYearId(createdWorkPlanID)
                ?.let {
                    return it
                }
                ?: throw ExpectedDataNotFound("No workPlan Details Associated with the following workPlan [ID = ${createdWorkPlanID}]")
    }

    fun findALlOnsiteUploadsWithActivityID(activityEntity: MsWorkPlanGeneratedEntity, map: ServiceMapsEntity): List<MsOnSiteUploadsEntity> {
        onsiteUploadRepo.findByWorkPlanGeneratedIDAndStatus(activityEntity, map.activeStatus)
                ?.let {
                    return it
                }
                ?: throw ExpectedDataNotFound("No Activity Uploads Details Associated with the following activity [ID = ${activityEntity.id}]")
    }


    fun onSiteUploads(docFile: MultipartFile, map: ServiceMapsEntity, loggedInUser: UsersEntity, fetchActivity: MsWorkPlanGeneratedEntity): MsOnSiteUploadsEntity {
        var onsiteUploads = MsOnSiteUploadsEntity()

        with(onsiteUploads) {
            name = commonDaoServices.saveDocuments(docFile)
            fileType = docFile.contentType
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(loggedInUser)
            createdOn = commonDaoServices.getTimestamp()
//            workPlanGeneratedID = fetchActivity
        }
        onsiteUploads = onsiteUploadRepo.save(onsiteUploads)

        return onsiteUploads
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

    fun saveNewWorkPlanFromComplaint(msWorkPlanGeneratedEntity: MsWorkPlanGeneratedEntity, complaint: ComplaintEntity, msType: MsTypesEntity, userWorkPlan: WorkPlanCreatedEntity, map: ServiceMapsEntity, usersEntity: UsersEntity): MsWorkPlanGeneratedEntity {
//        var msWorkPlanGeneratedEntity = msWorkPlanGeneratedEntity

        with(msWorkPlanGeneratedEntity) {
            uuid = commonDaoServices.generateUUIDString()
            msTypeId = msType.id
            complaintId = complaint.id
            referenceNumber = "${msType.markRef}${generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            workPlanYearId = userWorkPlan.id
            progressValue = progressSteps(workPlanSteps).getInt("step-1")
            status = map.initStatus
            officerId = usersEntity.id
            officerName = commonDaoServices.concatenateName(usersEntity)
            createdBy = commonDaoServices.concatenateName(usersEntity)
            createdOn = commonDaoServices.getTimestamp()
        }


        return generateWorkPlanRepo.save(msWorkPlanGeneratedEntity)
    }


    fun saveNewWorkPlanActivity(msWorkPlanGeneratedEntity: MsWorkPlanGeneratedEntity, msType: MsTypesEntity, userWorkPlan: WorkPlanCreatedEntity, map: ServiceMapsEntity, usersEntity: UsersEntity): MsWorkPlanGeneratedEntity {


        with(msWorkPlanGeneratedEntity) {
            uuid = commonDaoServices.generateUUIDString()
            msTypeId = msType.id
            progressStep = "WorkPlan Generated"
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            referenceNumber = "${msType.markRef}${generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            workPlanYearId = userWorkPlan.id
            progressValue = progressSteps(workPlanSteps).getInt("step-1")
            status = map.initStatus
            officerId = usersEntity.id
            officerName = commonDaoServices.concatenateName(usersEntity)
            createdBy = commonDaoServices.concatenateName(usersEntity)
            createdOn = commonDaoServices.getTimestamp()
        }

        return generateWorkPlanRepo.save(msWorkPlanGeneratedEntity)


    }


    fun findAllWorkPlansWithCreatedWorkPlanID(createdWorkPlanID: Long): List<MsWorkPlanGeneratedEntity> {
        generateWorkPlanRepo.findByWorkPlanYearId(createdWorkPlanID)
                ?.let {
                    return it
                }
                ?: throw ExpectedDataNotFound("WorkPlans with [created WorkPlan ID=$createdWorkPlanID], do Not Exists")
    }


    fun findDivisionEntity(divisionID: Long): DivisionsEntity {
        divisionsRepo.findByIdOrNull(divisionID)
                ?.let { divisionsEntity ->
                    return divisionsEntity
                } ?: throw ExpectedDataNotFound("The division with the following [id=$divisionID], does not Exist")
    }


    fun isWithinRange(checkDate: Date, workPlanYearCodes: WorkplanYearsCodesEntity): Boolean {

        return !(checkDate.before(workPlanYearCodes.workplanCreationStartDate) || checkDate.after(workPlanYearCodes.workplanCreationEndDate))


    }

    fun getCurrentYear(): String {
        val year = Calendar.getInstance()[Calendar.YEAR]
        return year.toString()
    }

    fun findWorkPlanYearsCodesEntity(currentYear: String, map: ServiceMapsEntity): WorkplanYearsCodesEntity {
        workPlanYearsCodesRepository.findByYearNameAndStatus(currentYear, map.activeStatus)
                ?.let {
                    return it
                }
                ?: throw ExpectedDataNotFound("Workplan Years Codes with [status=$map.activeStatus], do Not Exists")
    }


    fun findCreatedWorkPlanWithID(createdWorkPlanID: Long): WorkPlanCreatedEntity {
        workPlanCreatedRepository.findByIdOrNull(createdWorkPlanID)
                ?.let {
                    return it
                }
                ?: throw ExpectedDataNotFound(" [created WorkPlan  with ID=$createdWorkPlanID], doe Not Exists")
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

    fun updateCreatedWorkPlan(createdWorkPlan: WorkPlanCreatedEntity, usersEntity: UsersEntity, status: Int): Boolean {
        with(createdWorkPlan) {
            workPlanStatus = status
            modifiedBy = commonDaoServices.concatenateName(usersEntity)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        workPlanCreatedRepository.save(createdWorkPlan)
        return true
    }

    fun approveAllWorkPlans(workPlans: List<MsWorkPlanGeneratedEntity>, loggedInUser: UsersEntity, map: ServiceMapsEntity): Boolean {
        val userName = loggedInUser.userName
        workPlans.forEach { workPlanDetails ->
            with(workPlanDetails) {
                approved = "APPROVED"
                progressStep = approved
                approvedBy = userName
                approvedStatus = map.activeStatus
                rejectedStatus = map.inactiveStatus
                approvedOn = commonDaoServices.getCurrentDate()
                modifiedBy = commonDaoServices.concatenateName(loggedInUser)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            generateWorkPlanRepo.save(workPlanDetails)
        }
        return true
    }

    fun rejectAllWorkPlans(workPlans: List<MsWorkPlanGeneratedEntity>, loggedInUser: UsersEntity, map: ServiceMapsEntity): Boolean {
        val userName = loggedInUser.userName
        workPlans.forEach { workPlanDetails ->
            with(workPlanDetails) {
                rejected = "REJECTED"
                progressStep = rejected
                rejectedBy = userName
                rejectedStatus = map.activeStatus
                approvedStatus = map.inactiveStatus
                rejectedOn = commonDaoServices.getCurrentDate()
                modifiedBy = commonDaoServices.concatenateName(loggedInUser)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            generateWorkPlanRepo.save(workPlanDetails)
        }
        return true
    }


    fun createWorkPlanYear(loggedInUser: UsersEntity, map: ServiceMapsEntity, workPlanYearCodes: WorkplanYearsCodesEntity) {
        val workPlanCreated = WorkPlanCreatedEntity()
        with(workPlanCreated) {
            uuid = commonDaoServices.generateUUIDString()
            workPlanRegion = commonDaoServices.findUserProfileByUserID(loggedInUser, map.activeStatus).regionId?.id
            referenceNumber = "WORKPLAN#${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
            yearNameId = workPlanYearCodes
            userCreatedId = loggedInUser
            createdDate = commonDaoServices.getCurrentDate()
            createdStatus = map.activeStatus
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(loggedInUser)
            createdOn = commonDaoServices.getTimestamp()
        }
        workPlanCreatedRepository.save(workPlanCreated)
    }

    fun findWorkPlanCreatedEntity(loggedInUser: UsersEntity, workPlanYearCodes: WorkplanYearsCodesEntity): WorkPlanCreatedEntity? {
        return workPlanCreatedRepository.findByUserCreatedIdAndYearNameId(loggedInUser, workPlanYearCodes)
    }

//    fun startHOFSchedule(complaint: ComplaintEntity): Boolean {
//        serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
//                ?.let { map ->
//                    complaintScheduleRepo.findByComplaintIdAndStatus(complaint, map.activeStatus)
//                            ?.let { MsComplaintSchedule ->
//                                cal.add(Calendar.DAY_OF_MONTH, hofDeskDays)
//                                with(MsComplaintSchedule) {
//                                    hofDeskArrivalDate = Date(Date().time)
//                                    hofDeskLeaveDays = hofDeskDays
//                                    hofDeskDueDate = cal.time as Date?
//                                }
//                                complaintScheduleRepo.save(MsComplaintSchedule)
//                                        .let {
//                                            return true
//                                        }
//
//                            }
//                            ?: throw ExpectedDataNotFound("Complaint with the following [id=${complaint.id}] does not exist")
//                }
//                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")
//    }
//
//    fun startIOSchedule(complaint: ComplaintEntity): Boolean {
//        serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
//                ?.let { map ->
//                    complaintScheduleRepo.findByComplaintIdAndStatus(complaint, map.activeStatus)
//                            ?.let { MsComplaintSchedule ->
//                                cal.add(Calendar.DAY_OF_MONTH, ioDeskDays)
//                                with(MsComplaintSchedule) {
//                                    ioDeskArrivalDate = Date(Date().time)
//                                    hodioDeskArrivalDate = Date(Date().time)
//                                    ioDeskAlertDays = ioDeskDays
//                                    ioDeskAlertDate = cal.time as Date?
//                                    cal.add(Calendar.DAY_OF_MONTH, hodIoDeskDays)
//                                    hodioDeskLeaveDays = hodIoDeskDays
//                                    hodioDeskDueDate = cal.time as Date?
//                                }
//                                complaintScheduleRepo.save(MsComplaintSchedule)
//                                        .let {
//                                            return true
//                                        }
//
//                            }
//                            ?: throw ExpectedDataNotFound("Complaint with the following [id=${complaint.id}] does not exist")
//                }
//                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")
//    }

//    fun runScheduler(): Boolean {
//        serviceMapsRepo.findByIdAndStatus(appId, activeStatus)
//                ?.let { map ->
//                    complaintScheduleRepo.findAllByStatus(map.activeStatus)
//                            ?.let { allComplaintsActive ->
////                                val cal = Calendar.getInstance()
//                                for (complaint in allComplaintsActive) {
//
//                                    if (complaint.status == map.activeStatus) {
//
////                                        if (complaint.ho == ){
////
////                                        }
//                                    }
//                                }
//                            }
//
//                    return true
//                }
//                ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")
//    }

    fun generateDemandNote(demand: CdDemandNoteEntity, statusEntity: CdStatusesValuesEntity): CdDemandNoteEntity? {
        var note: CdDemandNoteEntity? = null

        iDestinationInspectionFeeRepo.findByIdOrNull(demand.destinationFeeValue)
                ?.let { fee ->
                    iConsignmentItemsRepo.findByIdOrNull(demand.itemIdNo)
                            ?.let { itemUpdate ->
                                demand.destinationFeeId = fee
                                demand.rate = fee.rate
                                KotlinLogging.logger { }.info { "FEE RATE VALUE" + fee.rate?.toLong() }

                                val percentage = 100
                                demand.amountPayable = demand.cfvalue?.multiply(fee.rate?.toBigDecimal())?.divide(percentage.toBigDecimal())

                                demand.createdOn = Timestamp.from(Instant.now())

                                note = iDemandNoteRepo.save(demand)
                                KotlinLogging.logger { }.info { "FEE RATE VALUE${note?.amountPayable}" }
                                itemUpdate.dnoteStatus = statusEntity.statusOne
                                iConsignmentItemsRepo.save(itemUpdate)
                            }
                }



        return note
    }
}
