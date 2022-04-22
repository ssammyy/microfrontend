package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.json.JSONObject
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.MarketSurveillanceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.*
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdLaboratoryEntity
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleLabTestResultsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmittedPdfListDetailsEntity
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
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.ServerResponse
import java.io.File
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant


@Service
class MarketSurveillanceComplaintProcessDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val msTypesRepo: IMsTypesRepository,
    private val complaintsRepo: IComplaintRepository,
    private val complaintCustomersRepo: IComplaintCustomersRepository,
    private val complaintLocationRepo: IComplaintLocationRepository,
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


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewComplaint(body: NewComplaintDto, page: PageRequest): MSComplaintSubmittedSuccessful {
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
                return MSComplaintSubmittedSuccessful(it.referenceNumber,true,"Complaint submitted successful with ref number ${it.referenceNumber}", null)
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            return MSComplaintSubmittedSuccessful(null,false,null, e.message ?: "Unknown Error")
        }
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

    fun findComplaintByRefNumber(refNumber: String): ComplaintEntity {
        complaintsRepo.findByReferenceNumber(refNumber)
            ?.let { complaint ->
                return complaint
            }
            ?: throw ExpectedDataNotFound("Complaint with [refNumber = ${refNumber}], does not Exist")
    }

}




