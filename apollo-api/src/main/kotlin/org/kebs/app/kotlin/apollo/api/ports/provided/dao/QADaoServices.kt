package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.apache.commons.lang3.SerializationUtils
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.common.dto.CompanyTurnOverUpdateDto
import org.kebs.app.kotlin.apollo.common.dto.UserCompanyEntityDto
import org.kebs.app.kotlin.apollo.common.dto.qa.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileDirectorsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.registration.UserRequestTypesEntity
import org.kebs.app.kotlin.apollo.store.model.std.SampleSubmissionDTO
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ICfgMoneyTypeCodesRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.stream.Collectors


@Service
class QADaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val usersRepo: IUserRepository,
    private val qaInvoiceCalculation: QaInvoiceCalculationDaoServices,
    private val limsServices: LimsServices,
    private val productsRepo: IProductsRepository,
    private val jasyptStringEncryptor: StringEncryptor,
    private val workPlanCreatedRepo: IQaWorkplanRepository,
    private val iPermitRatingRepo: IPermitRatingRepository,
    private val iManufacturePaymentDetailsRepository: IManufacturerPaymentDetailsRepository,
    private val iQaAwardedPermitTrackerEntityRepository: IQaAwardedPermitTrackerEntityRepository,

    private val sampleStandardsRepo: ISampleStandardsRepository,
    private val remarksEntityRepo: IQaRemarksEntityRepository,
    private val invoiceDaoService: InvoiceDaoService,
    private val paymentUnitsRepository: ICfgKebsPermitPaymentUnitsRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val qaInspectionOPCRepo: IQaInspectionOpcEntityRepository,
    private val qaPersonnelInchargeRepo: IQaPersonnelInchargeEntityRepository,
    private val qaInspectionTechnicalRepo: IQaInspectionTechnicalRepository,
    private val qaInspectionReportRecommendationRepo: IQaInspectionReportRecommendationRepository,
    private val qaInspectionHaccpImplementationRepo: IQaInspectionHaccpImplementationRepository,
    private val permitRepo: IPermitApplicationsRepository,
    private val iPermitRepo: PermitRepository,
    private val permitMigratedRepo: IPermitMigrationApplicationsEntityRepository,
    private val permitMigratedRepoDmark: IPermitMigrationApplicationsDmarkEntityRepository,
    private val permitMigratedRepoFmark: IPermitMigrationApplicationsFmarkEntityRepository,

    private val permitUpdateDetailsRequestsRepo: IPermitUpdateDetailsRequestsRepository,
    private val userRequestsRepo: IUserRequestTypesRepository,
    private val SampleCollectionRepo: IQaSampleCollectionRepository,
    private val SampleSubmissionRepo: IQaSampleSubmissionRepository,
    private val SampleSubmissionSavedPdfListRepo: IQaSampleSubmittedPdfListRepository,
    private val sampleLabTestResultsRepo: IQaSampleLabTestResultsRepository,
    private val sampleLabTestParametersRepo: IQaSampleLabTestParametersRepository,
    private val schemeForSupervisionRepo: IQaSchemeForSupervisionRepository,
    private val sta3Repo: IQaSta3EntityRepository,
    private val smarkFmarkRepo: IQaSmarkFmarkRepository,
    private val invoiceRepository: IInvoiceRepository,
    private val invoiceDetailsRepo: IQaInvoiceDetailsRepository,
    private val invoiceMasterDetailsRepo: IQaInvoiceMasterDetailsRepository,
    private val invoiceQaBatchRepo: IQaBatchInvoiceRepository,
    private val paymentRevenueCodesRepo: IPaymentRevenueCodesEntityRepository,
    private val invoiceStagingReconciliationRepo: IStagingPaymentReconciliationRepo,
    private val invoiceBatchDetailsRepo: InvoiceBatchDetailsRepo,
    private val sta10Repo: IQaSta10EntityRepository,
    private val productsManufactureSTA10Repo: IQaProductBrandEntityRepository,
    private val rawMaterialsSTA10Repo: IQaRawMaterialRepository,
    private val machinePlantsSTA10Repo: IQaMachineryRepository,
    private val qaUploadsRepo: IQaUploadsRepository,
    private val manufacturingProcessSTA10Repo: IQaManufactureProcessRepository,
    private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val permitTypesRepo: IPermitTypesEntityRepository,
    private val processStatusRepo: IQaProcessStatusRepository,
    private val iMoneyTypeCodesRepo: ICfgMoneyTypeCodesRepository,
    private val mpesaServices: MPesaService,
    private val reportsDaoService: ReportsDaoService,
//    private val reportsControllers: ReportsController,
    private val notifications: Notifications,
    private val daoService: DaoFluxService,


    ) {


    @Lazy
    @Autowired
    lateinit var qualityAssuranceBpmn: QualityAssuranceBpmn

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController

    @Lazy
    @Autowired
    lateinit var systemsAdminDaoService: SystemsAdminDaoService


    final var appId = applicationMapProperties.mapQualityAssurance

    val permitList = "redirect:/api/qa/permits-list?permitTypeID"
    val permitDetails = "redirect:/api/qa/permit-details?permitID"
    val sta10Details = "redirect:/api/qa/view-sta10?permitID"
    val batchInvoiceList = "redirect:/api/qa/invoice/list-batch-invoices"

    /*:::::::::::::::::::::::::::::::::::::::::::::START INTERNAL USER FUNCTIONALITY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

        fun findLoggedInUserTask(
        auth: Authentication,
        loggedInUser: UsersEntity,
        map: ServiceMapsEntity,
        permitListMyTasksAddedTogether: MutableList<PermitEntityDto>
    ): MutableList<PermitEntityDto> {
        auth.authorities.forEach { a ->
            when (a.authority) {
                "QA_OFFICER_READ" -> {
                    listPermits(
                        findAllQAOPermitListWithTaskID(
                            loggedInUser,
                            applicationMapProperties.mapUserTaskNameQAO
                        ), map
                    ).let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

            when (a.authority) {
                "QA_ASSESSORS_READ" -> {
                    listPermits(
                        findAllAssessorPermitListWithTaskID(
                            loggedInUser,
                            applicationMapProperties.mapUserTaskNameASSESSORS
                        ), map
                    ).let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

            when (a.authority) {
                "QA_HOD_READ" -> {
                    findAllUserTasksQAMHODRMHOFByTaskID(
                        loggedInUser,
                        auth,
                        "QA_HOD_READ",
                        map,
                        applicationMapProperties.mapUserTaskNameHOD
                    )?.let { listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

            when (a.authority) {
                "QA_HOF_READ" -> {
                    findAllUserTasksQAMHODRMHOFByTaskID(
                        loggedInUser,
                        auth,
                        "QA_HOF_READ",
                        map,
                        applicationMapProperties.mapUserTaskNameHOF
                    )?.let { listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

            when (a.authority) {
                "QA_RM_READ" -> {
                    findAllUserTasksQAMHODRMHOFByTaskID(
                        loggedInUser,
                        auth,
                        "QA_RM_READ",
                        map,
                        applicationMapProperties.mapUserTaskNameRM
                    )?.let { listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

            when (a.authority) {
                "QA_MANAGER_READ" -> {
                    findAllUserTasksQAMHODRMHOFByTaskID(
                        loggedInUser,
                        auth,
                        "QA_MANAGER_READ",
                        map,
                        applicationMapProperties.mapUserTaskNameQAM
                    )?.let { listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

            when (a.authority) {
                "QA_PAC_SECRETARY_READ" -> {
                    findAllUserTasksPACPSCPCMByTaskID(
                        loggedInUser,
                        auth,
                        "QA_PAC_SECRETARY_READ",
                        applicationMapProperties.mapUserTaskNamePACSECRETARY
                    )?.let { listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

            when (a.authority) {
                "QA_PSC_MEMBERS_READ" -> {
                    findAllUserTasksPACPSCPCMByTaskID(
                        loggedInUser,
                        auth,
                        "QA_PSC_MEMBERS_READ",
                        applicationMapProperties.mapUserTaskNamePSC
                    )?.let { listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

            when (a.authority) {
                "QA_PCM_READ" -> {
                    findAllUserTasksPACPSCPCMByTaskID(
                        loggedInUser,
                        auth,
                        "QA_PCM_READ",
                        applicationMapProperties.mapUserTaskNamePCM
                    )?.let { listPermits(it, map) }?.let { permitListMyTasksAddedTogether.addAll(it) }
                }
            }

        }

        return permitListMyTasksAddedTogether
    }


    fun findAllQAOPermitListWithTaskID(
        user: UsersEntity,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByQaoIdAndOldPermitStatusIsNullAndUserTaskId(userId, taskID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllAssessorPermitListWithTaskID(
        user: UsersEntity,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByAssessorIdAndOldPermitStatusIsNullAndUserTaskId(userId, taskID)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findInspectionInvoiceDetails(
        user: UsersEntity,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByAssessorIdAndOldPermitStatusIsNullAndUserTaskId(userId, taskID)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }



    fun findAllUserTasksQAMHODRMHOFByTaskID(
        user: UsersEntity,
        auth: Authentication,
        authToCompareWith: String,
        map: ServiceMapsEntity,
        taskID: Long
    ): List<PermitApplicationsEntity>? {

        val userProfile = commonDaoServices.findUserProfileByUserID(user, 1)

        val permitListAllApplications = mutableListOf<PermitApplicationsEntity>()
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == authToCompareWith } -> {
                systemsAdminDaoService.listRbacSectionByUsersIdAndByStatus(
                    user.id ?: throw Exception("MISSING USER ID"), 1
                )
                    ?.forEach { section ->
                        permitRepo.findRbacPermitByRegionIDPaymentStatusAndUserTaskIDAndSectionId(
                            map.initStatus,
                            userProfile.regionId?.id ?: throw Exception("MISSING REGION ID"),
                            taskID,
                            section.id
                        )
                            ?.let { ls ->
                                permitListAllApplications.addAll(ls)
                            }

                    }

            }
        }

        return permitListAllApplications
    }
        fun findAllUserTasksPACPSCPCMByTaskID(
        user: UsersEntity,
        auth: Authentication,
        authToCompareWith: String,
        taskID: Long
    ): List<PermitApplicationsEntity>? {

        var permitListAllApplications: List<PermitApplicationsEntity>? = null
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == authToCompareWith } -> {
                permitListAllApplications =
                    permitRepo.findAllByOldPermitStatusIsNullAndUserTaskId(taskID)
            }
        }

        return permitListAllApplications
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::END INTERNAL USER FUNCTIONALITY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    fun findPermitTypesList(status: Int): List<PermitTypesEntity> {
        permitTypesRepo.findByStatus(status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type List found")
    }

    fun findPermitType(id: Long): PermitTypesEntity {
        permitTypesRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type found with the following [ID=$id]")


    }

    fun updateCompanyTurnOverDetails(
        dto: CompanyTurnOverUpdateDto,
        user: UsersEntity,
        map: ServiceMapsEntity,
    ): UserCompanyEntityDto? {

        companyProfileRepo.findByIdOrNull(dto.companyProfileID)
            ?.let { entity ->
//                if(entity.updateFirmType==dto.selectedFirmTypeID){
                    entity.apply {
                        val firmTypeDetails = findFirmTypeById(dto.selectedFirmTypeID)
                        updateDetailsStatus = null
                        updateDetailsComment = null
                        requesterComment = null
                        updateFirmType = null
                        requesterId = null
                        firmCategory = firmTypeDetails.id
                        yearlyTurnover = firmTypeDetails.varField1?.toBigDecimal()
                        modifiedBy = user.userName
                        modifiedOn = Timestamp.from(Instant.now())
                    }

                    //If the upgradeType is 1 means UpGarding while if the UpgradeType is 0 means downgrading
                    if(entity.upgradeType==1){
                        val allPlantDetails = findAllPlantDetailsWithCompanyID(entity.id?:throw ExpectedDataNotFound("Missing Company ID"))
                        val allPermitDetailsNotPaid = permitRepo.findByPermitTypeAndPaidStatusAndCompanyIdAndInvoiceGeneratedAndPermitAwardStatusIsNullAndOldPermitStatusIsNull(applicationMapProperties.mapQAPermitTypeIdSmark,map.initStatus, entity.id!!,map.activeStatus)
                        allPermitDetailsNotPaid?.forEach { permit->
                            //Calculate Invoice Details
                            val invoiceCreated = permitInvoiceCalculationSmartFirmUpGrade(map, user, permit, null).second

                            //Update Permit Details
                            with(permit) {
                                paidStatus = 0
                                sendApplication = map.activeStatus
                                endOfProductionStatus = map.inactiveStatus
                                invoiceGenerated = map.activeStatus
                                varField10 = map.activeStatus.toString()
                                permitStatus = applicationMapProperties.mapQaStatusPPayment
                            }
                            permitUpdateDetails(permit, map, user).second
                        }

                    }

                    val companyProfileEntity = companyProfileRepo.save(entity)

                    return UserCompanyEntityDto(
                        companyProfileEntity.name,
                        companyProfileEntity.kraPin,
                        companyProfileEntity.userId,
                        null,
                        companyProfileEntity.registrationNumber,
                        companyProfileEntity.postalAddress,
                        companyProfileEntity.physicalAddress,
                        companyProfileEntity.plotNumber,
                        companyProfileEntity.companyEmail,
                        companyProfileEntity.companyTelephone,
                        companyProfileEntity.yearlyTurnover,
                        companyProfileEntity.businessLines,
                        companyProfileEntity.businessNatures,
                        companyProfileEntity.buildingName,
                        null,
                        companyProfileEntity.streetName,
                        companyProfileEntity.directorIdNumber,
                        companyProfileEntity.region,
                        companyProfileEntity.county,
                        companyProfileEntity.town,
                        null,
                        null,
                        null,
                        null,
                        iPermitRatingRepo.findByIdOrNull(companyProfileEntity.firmCategory)?.firmType
                    ).apply {
                        id = companyProfileEntity.id

                        status = companyProfileEntity.status
                    }
//                }
//        else {
//                    val selectedFirmType = findFirmTypeById(dto.selectedFirmTypeID)
//                    val requiredFirmType = entity.updateFirmType?.let { findFirmTypeById(it) }
//                     throw NullValueNotAllowedException("Your are Upgrading/downgrading to ${selectedFirmType.firmType}, While request was to upgrade/downgrade to ${requiredFirmType?.firmType}")
//                }

            } ?: throw NullValueNotAllowedException("Record not found")
    }

    fun updateInspectionFeesDetailsDetails(
        branchID: Long,
        user: UsersEntity,
        map: ServiceMapsEntity,
    ): BatchInvoiceDto {

        companyProfileRepo.findByIdOrNull(user.companyId)
            ?.let { entity ->
                val branchDetails =findPlantDetails(branchID)
                //todo: Change value to correct payment type
                val permitType = findPermitType(applicationMapProperties.mapQAPermitTypeIdSmark)
                var inspectionInvoiceFound = qaInvoiceCalculation.calculatePaymentInspectionFees(user,permitType,entity.yearlyTurnover?: throw Exception("MISSING YEARLY TURN OVER FOR THE COMPANY"),branchDetails,map)
                val paymentRevenueCode = findPaymentRevenueWithRegionIDAndPermitType(branchDetails.region ?: throw Exception("MISSING REGION ID"), permitType.id ?: throw Exception("MISSING REGION ID"))

                var batchInvoiceInspection = QaBatchInvoiceEntity()
                with(batchInvoiceInspection) {
                    invoiceNumber = applicationMapProperties.mapInvoicesPrefix + generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()
                    userId = user.id
                    plantId = branchID
                    status = map.activeStatus
                    description = "${inspectionInvoiceFound.invoiceRef}"
                    totalAmount = inspectionInvoiceFound.totalAmount
                    totalTaxAmount = inspectionInvoiceFound.taxAmount
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
                batchInvoiceInspection = invoiceQaBatchRepo.save(batchInvoiceInspection)

                with(inspectionInvoiceFound) {
                    batchInvoiceNo = batchInvoiceInspection.id
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()
                }

                inspectionInvoiceFound = invoiceMasterDetailsRepo.save(inspectionInvoiceFound)

                val sageValuesDtoList = mutableListOf<SageValuesDto>()

                val detailBody = SageValuesDto().apply {
                    revenueAcc = paymentRevenueCode.revenueCode
                    revenueAccDesc = paymentRevenueCode.revenueDescription
                    taxable = 1
                    totalAmount = batchInvoiceInspection.totalAmount
                    taxAmount = batchInvoiceInspection.totalTaxAmount
                }
                sageValuesDtoList.add(detailBody)

                val newBatchInvoiceDto = NewBatchInvoiceDto().apply {
                    batchID = batchInvoiceInspection.id!!
                    plantID = branchID
                }

                //submit to staging invoices
                val batchInvoice = permitMultipleInvoiceSubmitInvoice(map, user, newBatchInvoiceDto, sageValuesDtoList).second

                with(branchDetails){
                    varField10 = batchInvoiceInspection.id.toString()
                }
                manufacturePlantRepository.save(branchDetails)

                return mapBatchInvoiceDetails(batchInvoice, user, map)
            }?: throw NullValueNotAllowedException("No Company Record not found")
    }


//    fun findPermitIdByPermitRefNumber(permitRefNumber: String): PermitApplicationsEntity
//    {
//        permitRepo.findByPermitRefNumber(permitRefNumber)?.let {
//            return it
//        } ?: throw ExpectedDataNotFound("No Permit  found with the following [RefNumber=$permitRefNumber]")
//
//    }

    fun findALlCreatedWorkPlanWIthOfficerID(officerID: Long): List<QaWorkplanEntity> {
        workPlanCreatedRepo.findByOfficerId(officerID)
            ?.let { createdWorkPlan ->
                return createdWorkPlan
            }
            ?: throw ExpectedDataNotFound("Created Work Plan with the following [USER ID = ${officerID}], does not Exist")
    }

//    fun findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(userID: Long, status: Int): List<InvoiceEntity> {
//        invoiceRepository.findAllByUserIdAndPaymentStatusAndBatchInvoiceNoIsNull(userID, status)
//            ?.let { it ->
//                return it
//            }
//            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userID}] and [status = ${status}], does not Exist")
//    }

    fun findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(
        userID: Long,
        status: Int
    ): List<QaInvoiceMasterDetailsEntity> {
        invoiceMasterDetailsRepo.findAllByUserIdAndPaymentStatusAndBatchInvoiceNoIsNull(userID, status)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userID}] and [status = ${status}], does not Exist")
    }

    fun findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(
        permitType: Long,
        branchID: Long,
        userID: Long,
        status: Int
    ): List<QaInvoiceMasterDetailsEntity> {
        invoiceMasterDetailsRepo.findAllByUserIdAndPaymentStatusAndBatchInvoiceNoIsNullPermitType(permitType,branchID,userID, status)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userID}] and [status = ${status}], does not Exist")
    }

    fun findALlInvoicesCreatedByUser(userID: Long): List<QaInvoiceMasterDetailsEntity> {
        invoiceMasterDetailsRepo.findAllByUserIdAndVarField1IsNull(userID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userID}], does not Exist")
    }
//    fun findALlInvoicesCreatedByUser(userID: Long): List<InvoiceEntity> {
//        invoiceRepository.findAllByManufacturer(userID)
//            ?.let { it ->
//                return it
//            }
//            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userID}], does not Exist")
//    }

//    fun findALlInvoicesPermitWithBatchID(batchID: Long): List<InvoiceEntity> {
//        invoiceRepository.findAllByBatchInvoiceNo(batchID)
//            ?.let { it ->
//                return it
//            }
//            ?: throw ExpectedDataNotFound("Invoices With [BATCH ID = ${batchID}], does not Exist")
//    }

    fun findALlInvoicesPermitWithBatchID(batchID: Long): List<QaInvoiceMasterDetailsEntity> {
        invoiceMasterDetailsRepo.findAllByBatchInvoiceNo(batchID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoice list With [BATCH ID = ${batchID}], do not Exist")
    }

    fun findInvoicesPermitWithBatchID(batchID: Long): QaInvoiceMasterDetailsEntity {
        invoiceMasterDetailsRepo.findByBatchInvoiceNo(batchID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoice With [BATCH ID = ${batchID}], do not Exist")
    }

    fun findALlInvoicesPermitWithMasterInvoiceID(masterInvoiceID: Long, status: Int): List<QaInvoiceDetailsEntity> {
        invoiceDetailsRepo.findByStatusAndInvoiceMasterId(status, masterInvoiceID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoice list With Master ID = $masterInvoiceID and status = ${status}, do not Exist")
    }

    fun findALlBatchInvoicesWithUserID(userID: Long): List<QaBatchInvoiceEntity> {
        invoiceQaBatchRepo.findByUserId(userID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userID}], does not Exist")
    }

    fun findALlStandardsDetails(status: Int): List<SampleStandardsEntity> {
        sampleStandardsRepo.findByStatusOrderByStandardTitle(status)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("No Standards Found WIth [status = ${status}]")
    }

    fun findALlRemarksDetailsPerPermit(permitID: Long): List<QaRemarksEntity> {
        remarksEntityRepo.findByPermitId(permitID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("No Remarks Found WIth the following Permit ID = ${permitID}")
    }

    fun findRemarksDetailsByID(remarksID: Long): QaRemarksEntity {
        remarksEntityRepo.findByIdOrNull(remarksID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("No Remarks Found With the following Permit ID = ${remarksID}")
    }

    fun findBatchInvoicesWithID(batchID: Long): QaBatchInvoiceEntity {
        invoiceQaBatchRepo.findByIdOrNull(batchID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [batch ID = ${batchID}], does not Exist")
    }

    fun findPaymentRevenueWithRegionIDAndPermitType(regionId: Long, permitTypeId: Long): PaymentRevenueCodesEntity {
        paymentRevenueCodesRepo.findByRegionIdAndPermitTypeId(regionId, permitTypeId)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Payment Revenue Code For This [Region ID = ${regionId}] and [Permit Type ID = ${permitTypeId}], does not Exist")
    }

    fun findBatchInvoicesWithInvoiceBatchIDMapped(invoiceBatchMappedID: Long): QaBatchInvoiceEntity {
        invoiceQaBatchRepo.findByInvoiceBatchNumberId(invoiceBatchMappedID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [invoice Batch Mapped ID = ${invoiceBatchMappedID}], does not Exist")
    }

    fun findBatchInvoicesWithRefNO(refNumber: String): QaBatchInvoiceEntity {
        invoiceQaBatchRepo.findByInvoiceNumber(refNumber)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [refNumber = ${refNumber}], does not Exist")
    }

    fun findCreatedWorkPlanWIthOfficerID(officerID: Long, refNumber: String): QaWorkplanEntity {
        workPlanCreatedRepo.findByOfficerIdAndRefNumber(officerID, refNumber)
            ?.let { createdWorkPlan ->
                return createdWorkPlan
            }
            ?: throw ExpectedDataNotFound("Created Work Plan with the following [USER ID = ${officerID} and REF Number = ${refNumber}], does not Exist")
    }

    fun findPermitStatus(statusID: Long): QaProcessStatusEntity {
        processStatusRepo.findByIdOrNull(statusID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Status found with the following [ID =$statusID]")

    }

    fun findFmarkWithSmarkId(smarkID: Long): QaSmarkFmarkEntity {
        smarkFmarkRepo.findBySmarkId(smarkID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Smark ID with found with the following [ID=$smarkID]")
    }

    fun findSmarkWithFmarkId(fmarkID: Long): QaSmarkFmarkEntity {
        smarkFmarkRepo.findByFmarkId(fmarkID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Fmark ID with found with the following [ID=$fmarkID]")
    }

    fun findAllUserPermitWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllUserPermits(user: UsersEntity): List<PermitApplicationsEntity>? {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndVarField9IsNull(userId)?.let { permitList ->
            return permitList
        }
        return null
    }

    fun findAllFirmPermits(companyID: Long): List<PermitApplicationsEntity> {
        permitRepo.findByCompanyIdAndOldPermitStatusIsNull(companyID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permits Found for the following COMPANY ID = ${companyID}")
    }

    fun findAllFirmPermitsWithPermitType(companyID: Long, permitTypeID: Long): List<PermitApplicationsEntity> {
        permitRepo.findByCompanyIdAndPermitTypeAndOldPermitStatusIsNullAndVarField9IsNull(companyID, permitTypeID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permits Found for the following COMPANY ID = ${companyID} and permitType ID ${permitTypeID}")
    }

    fun findAllFirmPermitsAwardedWithPermitType(
        companyID: Long,
        awardedStatus: Int,
        permitTypeID: Long
    ): List<PermitApplicationsEntity> {
        permitRepo.findByCompanyIdAndPermitTypeAndPermitAwardStatusAndOldPermitStatusIsNull(
            companyID,
            permitTypeID,
            awardedStatus
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permits Found for the following COMPANY ID = ${companyID} and permitType ID ${permitTypeID}")
    }

    fun findAllFirmInKenyaPermitsAwardedWithPermitType(
        awardedStatus: Int,
        permitTypeID: Long
    ): List<PermitApplicationsEntity> {
        permitRepo.findByPermitTypeAndPermitAwardStatusAndOldPermitStatusIsNull(permitTypeID, awardedStatus)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permits Found for the following awardedStatus = ${awardedStatus} and permitType ID ${permitTypeID}")
    }

    fun findAllFirmsInKenyaPermitsApplicationsWithPermitTypeAndPaidStatus(
        permitTypeID: Long,
        paidStatus: Int
    ): List<PermitApplicationsEntity> {
        permitRepo.findByPermitTypeAndPaidStatusAndPermitAwardStatusIsNullAndOldPermitStatusIsNull(
            permitTypeID,
            paidStatus
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permits Found for the following paid Status = ${paidStatus} and permitType ID ${permitTypeID}")
    }

    fun findAllBranchPermits(branchID: Long): List<PermitApplicationsEntity> {
        permitRepo.findByAttachedPlantIdAndOldPermitStatusIsNull(branchID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permits Found for the following BRANCH ID = ${branchID}")
    }

    fun findAllBranchPermitsWithPermitType(branchID: Long, permitTypeID: Long): List<PermitApplicationsEntity> {
        permitRepo.findByAttachedPlantIdAndPermitTypeAndOldPermitStatusIsNull(branchID, permitTypeID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permits Found for the following BRANCH ID = ${branchID} and permitType ID ${permitTypeID}")
    }

    fun findAllUserPermitWithPermitTypeAwardedStatusIsNotNull(
        user: UsersEntity,
        permitType: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllUserPermitWithPermitTypeAwardedStatus(
        user: UsersEntity,
        permitType: Long,
        status: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(userId, permitType, status)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllUserCompletelyPermitWithPermitTypeAwardedStatus(
        user: UsersEntity,
        permitType: Long,
        status: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusAndVersionNumberIsNotNull(
            userId,
            permitType,
            status
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    // load all migrated permits to user
    fun findAllLoadedPermitList(
        user: UsersEntity,
        permitNumber: String,
        attachedPlant: UsersEntity
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        val attachedPlantId = attachedPlant.plantId ?: throw ExpectedDataNotFound("No PLANT ID Found")
        KotlinLogging.logger { }.info { userId }
        KotlinLogging.logger { }.info { attachedPlantId }
        KotlinLogging.logger { }.info { permitNumber }
        //  permitRepo.migratePermitsToNewUser(userId, permitNumber, attachedPlantId)
//        permitRepo.findByPermitRefNumber(permitNumber)?.let {
//
//            if (it.isNullOrEmpty()) {
//                throw ExpectedDataNotFound("This Permit is not assigned to you")
//
//
//            } else {
        try {
            val response = permitRepo.migratePermitsToNewUser(userId, permitNumber, attachedPlantId)
            KotlinLogging.logger { }.info("The response is $response")
            permitRepo.findByUserIdAndVarField9IsNull(userId)?.let { permitList ->
                return permitList
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }
        permitRepo.findByUserIdAndVarField9IsNull(userId)?.let { permitList ->
            return permitList
        }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }
    fun findAllLoadedPermitListDmark(
        user: UsersEntity,
        permitNumber: String,
        attachedPlant: UsersEntity
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        val attachedPlantId = attachedPlant.plantId ?: throw ExpectedDataNotFound("No PLANT ID Found")
        KotlinLogging.logger { }.info { userId }
        KotlinLogging.logger { }.info { attachedPlantId }
        KotlinLogging.logger { }.info { permitNumber }
        //  permitRepo.migratePermitsToNewUser(userId, permitNumber, attachedPlantId)
//        permitRepo.findByPermitRefNumber(permitNumber)?.let {
//
//            if (it.isNullOrEmpty()) {
//                throw ExpectedDataNotFound("This Permit is not assigned to you")
//
//
//            } else {
        try {
            val response = permitRepo.migratePermitsToNewUserDmark(userId, permitNumber, attachedPlantId)
            KotlinLogging.logger { }.info("The response is $response")
            permitRepo.findByUserIdAndVarField9IsNull(userId)?.let { permitList ->
                return permitList
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }
        permitRepo.findByUserIdAndVarField9IsNull(userId)?.let { permitList ->
            return permitList
        }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }
    fun findAllLoadedPermitListFmark(
        user: UsersEntity,
        permitNumber: String,
        attachedPlant: UsersEntity
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        val attachedPlantId = attachedPlant.plantId ?: throw ExpectedDataNotFound("No PLANT ID Found")
        KotlinLogging.logger { }.info { userId }
        KotlinLogging.logger { }.info { attachedPlantId }
        KotlinLogging.logger { }.info { permitNumber }
        //  permitRepo.migratePermitsToNewUser(userId, permitNumber, attachedPlantId)
//        permitRepo.findByPermitRefNumber(permitNumber)?.let {
//
//            if (it.isNullOrEmpty()) {
//                throw ExpectedDataNotFound("This Permit is not assigned to you")
//
//
//            } else {
        try {
            val response = permitRepo.migratePermitsToNewUserFmark(userId, permitNumber, attachedPlantId)
            KotlinLogging.logger { }.info("The response is $response")
            permitRepo.findByUserIdAndVarField9IsNull(userId)?.let { permitList ->
                return permitList
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }
        permitRepo.findByUserIdAndVarField9IsNull(userId)?.let { permitList ->
            return permitList
        }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    // find all user permits
    fun findAllMyPermits(user: UsersEntity): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        KotlinLogging.logger { }.info { userId }
        permitRepo.findByUserIdAndVarField9IsNull(userId)?.let { permitList ->
            return permitList
        }
            ?: throw ExpectedDataNotFound("No Permits Found for the following user with USERNAME = ${user.userName}")
    }

    // find all user payments
    fun findAllMyPayments(user: UsersEntity): List<QaInvoiceMasterDetailsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")

        invoiceMasterDetailsRepo.findAllByUserIdAndReceiptNoIsNotNull(userId)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userId}] does not Exist")
    }

    // find all Company UnPaied payments
    fun findAllMyPaymentsByStatusAndUserID(userId: Long,status: Int): List<QaInvoiceMasterDetailsEntity>? {
        return invoiceMasterDetailsRepo.findAllByUserIdAndPaymentStatusAndReceiptNoIsNull(userId,status)
    }


    //  }
    //  return it

//            ?: throw ExpectedDataNotFound("This Permit is not assigned to you")

    //}


//    fun listAllLoadedPermitList(
//        userId: Long,
//        permitNumber: String,
//        attachedPlantId: Long,
//        map: ServiceMapsEntity
//    ): List<PermitEntityDto> {
//        return listPermits(findAllLoadedPermitList(userId, permitNumber, attachedPlantId), map)
//    }

    fun updateQAInvoiceBatchDetails(
        invoiceBatchDetails: QaBatchInvoiceEntity,
        user: String
    ): QaBatchInvoiceEntity {
        with(invoiceBatchDetails) {
            modifiedBy = user
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceQaBatchRepo.save(invoiceBatchDetails)
    }

    fun updateQAMasterInvoiceDetails(
        permitInvoiceFound: QaInvoiceMasterDetailsEntity,
        user: String
    ): QaInvoiceMasterDetailsEntity {
        with(permitInvoiceFound) {
            modifiedBy = user
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceMasterDetailsRepo.save(permitInvoiceFound)
    }


    fun findAllUserPermitWithPermitTypeAwardedStatusAndFmarkGeneratedSTatusISNull(
        user: UsersEntity,
        permitType: Long,
        status: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(userId, permitType, status)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllUserPermitWithPermitTypeAwardedStatusIsNullAndTaskID(
        user: UsersEntity,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(userId, permitType, taskID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllUserTasksByTaskID(
        user: UsersEntity,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndOldPermitStatusIsNullAndUserTaskId(userId, taskID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllUserTasksManufactureByTaskID(
        user: UsersEntity,
        auth: Authentication,
        taskID: Long
    ): List<PermitApplicationsEntity>? {
        var permitListAllApplications: List<PermitApplicationsEntity>? = null
        permitListAllApplications = when {
            auth.authorities.stream()
                .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                permitRepo.findByCompanyIdAndOldPermitStatusIsNullAndUserTaskId(
                    user.companyId ?: throw Exception("MISSING COMPANY ID"), taskID
                )
            }

            auth.authorities.stream()
                .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                permitRepo.findByAttachedPlantIdAndOldPermitStatusIsNullAndUserTaskId(
                    user.plantId ?: throw Exception("MISSING PLANT ID"), taskID
                )
            }

            else -> {
                throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
            }
        }

        return permitListAllApplications
    }

    fun findAllUserTasksManufactureByTaskIDAndPermitType(
        user: UsersEntity,
        auth: Authentication,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity>? {
        var permitListAllApplications: List<PermitApplicationsEntity>? = null
        permitListAllApplications = when {
            auth.authorities.stream()
                .anyMatch { authority -> authority.authority == "MODIFY_COMPANY" } && auth.authorities.stream()
                .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                permitRepo.findByCompanyIdAndOldPermitStatusIsNullAndUserTaskIdAndPermitType(
                    user.companyId ?: throw Exception("MISSING COMPANY ID"), taskID, permitType
                )
            }

            auth.authorities.stream()
                .anyMatch { authority -> authority.authority != "MODIFY_COMPANY" } && auth.authorities.stream()
                .anyMatch { authority -> authority.authority == "PERMIT_APPLICATION" } -> {
                permitRepo.findByAttachedPlantIdAndOldPermitStatusIsNullAndUserTaskIdAndPermitType(
                    user.plantId ?: throw Exception("MISSING PLANT ID"), taskID, permitType
                )
            }

            else -> {
                throw ExpectedDataNotFound("UNAUTHORISED LOGGED IN USER (ACCESS DENIED)")
            }
        }

        return permitListAllApplications
    }


//    fun findAllUserTasksQAMHODRMHOFByTaskID(
//        user: UsersEntity,
//        auth: Authentication,
//        authToCompareWith: String,
//        taskID: Long
//    ): List<PermitApplicationsEntity>? {
//
//        val userProfile = commonDaoServices.findUserProfileByUserID(user, 1)
//
//        var permitListAllApplications: List<PermitApplicationsEntity>? = null
//         if (auth.authorities.stream().anyMatch { authority -> authority.authority == authToCompareWith }  }) {
//            permitListAllApplications =  permitRepo.findByCompanyIdAndOldPermitStatusIsNullAndUserTaskId( user.companyId ?: throw Exception("MISSING COMPANY ID"), taskID)
//        }
//
//        return permitListAllApplications
//    }

    fun findAllUserTasksPACPSCPCMByTaskID(
        user: UsersEntity,
        auth: Authentication,
        authToCompareWith: String,
        permitTypeID: Long,
        taskID: Long
    ): List<PermitApplicationsEntity>? {

        var permitListAllApplications: List<PermitApplicationsEntity>? = null
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == authToCompareWith } -> {
                permitListAllApplications =
                    permitRepo.findAllByOldPermitStatusIsNullAndUserTaskIdAndPermitType(taskID, permitTypeID)
            }
        }

        return permitListAllApplications
    }


    fun findAllUserTasksForPsc(
        user: UsersEntity,
        permitTypeID: Long,
        taskID: Long
    ): List<PermitApplicationsEntity>? {

        var permitListAllApplications: List<PermitApplicationsEntity>? = null
        permitListAllApplications =
            permitRepo.findAllByOldPermitStatusIsNullAndUserTaskIdAndPermitTypeAndPermitStatus(taskID, permitTypeID, 24)


        return permitListAllApplications
    }


    fun findAllUserTasksQAMHODRMHOFByTaskID(
        user: UsersEntity,
        auth: Authentication,
        authToCompareWith: String,
        permitTypeID: Long,
        map: ServiceMapsEntity,
        taskID: Long
    ): List<PermitApplicationsEntity>? {

        val userProfile = commonDaoServices.findUserProfileByUserID(user, 1)

//        var permitListAllApplications: List<PermitApplicationsEntity>? = null
        val permitListAllApplications = mutableListOf<PermitApplicationsEntity>()
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == authToCompareWith } -> {
                systemsAdminDaoService.listRbacSectionByUsersIdAndByStatus(
                    user.id ?: throw Exception("MISSING USER ID"), 1
                )
                    ?.forEach { section ->
                        permitRepo.findRbacPermitByRegionIDPaymentStatusAndUserTaskIDAndPermitTypeAndSectionId(
                            permitTypeID,
                            map.initStatus,
                            userProfile.regionId?.id ?: throw Exception("MISSING REGION ID"),
                            taskID,
                            section.id
                        )
                            ?.let { ls ->
                                permitListAllApplications.addAll(ls)
                            }

                    }

            }
        }

        return permitListAllApplications
    }

    fun findAllApplicationsQAMHODRMHOFByRegion(
        user: UsersEntity,
        auth: Authentication,
        authToCompareWith: String,
        permitTypeID: Long,
        map: ServiceMapsEntity
    ): List<PermitApplicationsEntity>? {

        val userProfile = commonDaoServices.findUserProfileByUserID(user, 1)

//        var permitListAllApplications: List<PermitApplicationsEntity>? = null
        val permitListAllApplications = mutableListOf<PermitApplicationsEntity>()
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == authToCompareWith } -> {
                systemsAdminDaoService.listRbacSectionByUsersIdAndByStatus(
                    user.id ?: throw Exception("MISSING USER ID"), 1
                )
                    ?.forEach { section ->
                        permitRepo.findRbacPermitByRegionIDPaymentStatusAndPermitTypeIDAndSectionId(
                            permitTypeID,
                            map.initStatus,
                            userProfile.regionId?.id ?: throw Exception("MISSING REGION ID"),
                            section.id
                        )
                            ?.let { ls ->
                                permitListAllApplications.addAll(ls)
                            }
                    }
            }
        }

        return permitListAllApplications
    }

    fun findAllApplicationsAwardedQAMHODRMHOFByRegion(
        user: UsersEntity,
        auth: Authentication,
        authToCompareWith: String,
        permitTypeID: Long,
        map: ServiceMapsEntity
    ): List<PermitApplicationsEntity>? {

        val userProfile = commonDaoServices.findUserProfileByUserID(user, 1)

//        var permitListAllApplications: List<PermitApplicationsEntity>? = null
        val permitListAllApplications = mutableListOf<PermitApplicationsEntity>()
        when {
            auth.authorities.stream().anyMatch { authority -> authority.authority == authToCompareWith } -> {
                systemsAdminDaoService.listRbacSectionByUsersIdAndByStatus(
                    user.id ?: throw Exception("MISSING USER ID"), 1
                )
                    ?.forEach { section ->
                        permitRepo.findRbacPermitByRegionIDPaymentStatusAndPermitTypeIDAndAwardedStatusAndSectionId(
                            permitTypeID,
                            map.initStatus,
                            map.activeStatus,
                            userProfile.regionId?.id ?: throw Exception("MISSING REGION ID"),
                            section.id
                        )
                            ?.let { ls ->
                                permitListAllApplications.addAll(ls)
                            }
                    }
            }
        }

        return permitListAllApplications
    }


    fun findAllUserPermitWithPermitTypeAwarded(
        user: UsersEntity,
        permitType: Long,
        status: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(userId, permitType, status)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllSmarkPermitWithNoFmarkGenerated(
        user: UsersEntity,
        permitType: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusAndFmarkGenerated(
            userId,
            permitType,
            status,
            fmarkGeneratedStatus
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllSmarkPermitWithNoFmarkGeneratedAndAllPaid(
        user: UsersEntity,
        permitType: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByUserIdAndPermitTypeAndOldPermitStatusIsNullAndPaidStatus(
            userId,
            permitType,
            10,
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllQAMPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByQamIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllQAMPermitListWithPermitTypeAwardedStatusIsNotNull(
        user: UsersEntity,
        permitType: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByQamIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllQAMPermitListWithPermitTypeAwardedStatusIsNotNullTaskID(
        user: UsersEntity,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByQamIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(userId, permitType, taskID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllHODPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByHodIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllHODPermitListWithPermitTypeTaskID(
        user: UsersEntity,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByHodIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(userId, permitType, taskID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllHODPermitListWithPermitTypeAwardedStatusIsNotNull(
        user: UsersEntity,
        permitType: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByHodIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findPermitWithPermitRefNumberLatest(permitRefNumber: String): PermitApplicationsEntity {
        permitRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following [PERMIT NO = ${permitRefNumber}]")
    }

    fun findPermitWithPermitRefNumberMigrated(permitRefNumber: String): PermitApplicationsEntity {
        permitRepo.findTopByPermitRefNumberOrderByIdAsc(permitRefNumber)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following [PERMIT NO = ${permitRefNumber}]")
    }

    fun findAllQAOPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByQaoIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPermitListWithPermitType(permitType: Long): List<PermitApplicationsEntity> {
        permitRepo.findByPermitTypeAndOldPermitStatusIsNull(permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found")
    }

    fun findAllQAOPermitListWithPermitTypeTaskID(
        user: UsersEntity,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByQaoIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(userId, permitType, taskID)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }



    fun findAllQAOPermitListWithPermitTypeAwardedStatusIsNotNull(
        user: UsersEntity,
        permitType: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByQaoIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPermitListWithPermitTypeAwardedStatusIsNotNull(
        permitType: Long
    ): List<PermitApplicationsEntity> {
        permitRepo.findByPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found")
    }

    fun findAllAssessorPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByAssessorIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllAssessorPermitListWithPermitTypeTaskID(
        user: UsersEntity,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByAssessorIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(userId, permitType, taskID)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllAssessorPermitListWithPermitTypeAwardedStatusIsNotNull(
        user: UsersEntity,
        permitType: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByAssessorIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
            userId,
            permitType
        )
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPacSecPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPacSecIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPacSecPermitListWithPermitTypeTaskID(
        user: UsersEntity,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPacSecIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(userId, permitType, taskID)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPacSecPermitListWithPermitTypeAwardedStatusIsNotNull(
        user: UsersEntity,
        permitType: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPacSecIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPCMPermitListWithPermitTypeTaskID(
        user: UsersEntity,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPcmIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(userId, permitType, taskID)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPCMPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPcmIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPCMPermitListWithPermitTypeAwardedStatusIsNotNull(
        user: UsersEntity,
        permitType: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPcmIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPSCPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPscMemberIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPSCPermitListWithPermitTypeTaskID(
        user: UsersEntity,
        permitType: Long,
        taskID: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPscMemberIdAndPermitTypeAndOldPermitStatusIsNullAndUserTaskId(userId, permitType, taskID)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllPSCPermitListWithPermitTypeAwardedStatusIsNotNull(
        user: UsersEntity,
        permitType: Long
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPscMemberIdAndPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatusIsNotNull(
            userId,
            permitType
        )
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findAllQAOPermitListWithPaymentStatus(paymentStatus: Int): List<PermitApplicationsEntity> {
        permitRepo.findAllByPaidStatus(paymentStatus)?.let { permitList ->
            return permitList
        } ?: throw ExpectedDataNotFound("No Permit Found for the following PAYMENT STATUS = ${paymentStatus}")
    }

    fun findAllPermitListWithPaymentStatusAndToken(paymentStatus: Int, token: String): List<PermitApplicationsEntity> {
        permitRepo.findAllByPaidStatusAndPermitFeeToken(paymentStatus, token)?.let { permitList ->
            return permitList
        }
            ?: throw ExpectedDataNotFound("No Permit Found for the following PAYMENT STATUS = ${paymentStatus} and TOKEN = ${token}")
    }

    fun findAllPermitListWithToken(token: String): List<PermitApplicationsEntity> {
        permitRepo.findAllByPermitFeeToken(token)?.let { permitList ->
            return permitList
        } ?: throw ExpectedDataNotFound("No Permit Found for the following  TOKEN = ${token}")
    }

    fun findAllQaInspectionOPCWithPermitRefNumber(permitRefNumber: String): List<QaInspectionOpcEntity> {
        qaInspectionOPCRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No inspection details for OPC with PERMIT REF NO =$permitRefNumber")
    }

    fun findAllQaInspectionOPCWithInspectReportID(inspectReportID: Long): List<QaInspectionOpcEntity> {
        qaInspectionOPCRepo.findByInspectionRecommendationId(inspectReportID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No inspection details for OPC with inspect Report ID =$inspectReportID")
    }

    fun findPermitBYID(id: Long): PermitApplicationsEntity {
        permitRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit found with the following [ID=$id]")
    }

    fun findPermitBYPermitNumber(permitNumber: String): PermitApplicationsEntity {
        permitRepo.findTopByAwardedPermitNumberOrderByIdDesc(permitNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit found with the following permit number = $permitNumber")
    }

    fun findPermitBYIDAndBranchID(id: Long, plantID: Long): PermitApplicationsEntity {
        permitRepo.findByIdAndAttachedPlantId(id, plantID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit found with the following [ID=$id] and [PLANT ID = $plantID]")
    }


    fun findSampleSubmittedBYPermitRefNumber(permitRefNumber: String): QaSampleSubmissionEntity {
        SampleSubmissionRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No sample submission found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findSampleSubmittedBYID(ssfID: Long): QaSampleSubmissionEntity {
        SampleSubmissionRepo.findByIdOrNull(ssfID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample submission found with the following ID number=$ssfID]")
    }


    fun findSampleSubmittedPDfBYID(ssfPdfID: Long): QaSampleSubmittedPdfListDetailsEntity {
        SampleSubmissionSavedPdfListRepo.findByIdOrNull(ssfPdfID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample submission pdf found with the following ID number=$ssfPdfID")
    }

    fun findSampleSubmittedListBYPermitRefNumberAndPermitID(
        permitRefNumber: String,
        status: Int,
        permitID: Long
    ): List<QaSampleSubmissionEntity> {
        SampleSubmissionRepo.findByPermitRefNumberAndStatusAndPermitId(permitRefNumber, status, permitID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No sample submission found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findInspectionReportListBYPermitRefNumberAndPermitID(
        permitRefNumber: String,
        status: Int,
        permitID: Long
    ): List<QaInspectionReportRecommendationEntity> {
        qaInspectionReportRecommendationRepo.findByPermitRefNumberAndPermitId(permitRefNumber, permitID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Report Recommendation found with the following PERMIT REF NO =$permitRefNumber")
    }

    fun saveSampleSubmittedPdf(
        qaFile: QaSampleSubmittedPdfListDetailsEntity,
        user: UsersEntity
    ): QaSampleSubmittedPdfListDetailsEntity {
        qaFile.createdOn = Timestamp.from(Instant.now())
        qaFile.modifiedOn = Timestamp.from(Instant.now())
        qaFile.modifiedBy = user.userName
        qaFile.createdBy = user.userName
        return SampleSubmissionSavedPdfListRepo.save(qaFile)
    }

    fun findSampleSubmittedListPdfBYSSFid(
        ssfID: Long
    ): List<QaSampleSubmittedPdfListDetailsEntity> {
        SampleSubmissionSavedPdfListRepo.findBySffId(ssfID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No sample submission pdf found with the following SSF ID=$ssfID]")
    }

    fun findSampleSubmittedListPdfBYSSFidANdSentToManufacture(
        ssfID: Long,
        status: Int,
    ): List<QaSampleSubmittedPdfListDetailsEntity> {
        SampleSubmissionSavedPdfListRepo.findBySffIdAndSentToManufacturerStatus(ssfID, status)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No sample submission pdf found with the following SSF ID=$ssfID]")
    }

    fun findSampleSubmittedBYBsNumber(bsNumber: String): QaSampleSubmissionEntity {
        SampleSubmissionRepo.findByBsNumber(bsNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample submission found with the following [bsNumber=$bsNumber]")
    }

    fun findQaInspectionHaccpImplementationBYPermitRefNumber(permitRefNumber: String): QaInspectionHaccpImplementationEntity {
        qaInspectionHaccpImplementationRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Haccp Implementation found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findQaInspectionHaccpImplementationBYInspectReportID(inspectReportID: Long): QaInspectionHaccpImplementationEntity {
        qaInspectionHaccpImplementationRepo.findByInspectionRecommendationId(inspectReportID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Haccp Implementation found with the following inspect Report ID =$inspectReportID")
    }

    fun findQaInspectionReportRecommendationBYPermitRefNumber(permitRefNumber: String): QaInspectionReportRecommendationEntity {
        qaInspectionReportRecommendationRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Report Recommendation found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findQaInspectionReportRecommendationBYID(inspectionReportID: Long): QaInspectionReportRecommendationEntity {
        qaInspectionReportRecommendationRepo.findByIdOrNull(inspectionReportID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Report Recommendation found with the following ID = $inspectionReportID")
    }

    fun findQaInspectionOpcBYPermitRefNumber(permitRefNumber: String): List<QaInspectionOpcEntity> {
        qaInspectionOPCRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Inspection OPC found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findQaInspectionOpcBYInspectReportID(inspectReportID: Long): List<QaInspectionOpcEntity> {
        qaInspectionOPCRepo.findByInspectionRecommendationId(inspectReportID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection OPC found with the following inspect Report ID =$inspectReportID")
    }

    fun findQaInspectionTechnicalBYPermitRefNumber(permitRefNumber: String): QaInspectionTechnicalEntity {
        qaInspectionTechnicalRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Technical found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findQaInspectionTechnicalBYInspectReportID(inspectReportID: Long): QaInspectionTechnicalEntity {
        qaInspectionTechnicalRepo.findByInspectionRecommendationId(inspectReportID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Technical found with the following inspect Report ID =$inspectReportID")
    }

    fun findSampleLabTestResultsRepoBYBSNumber(bsNumber: String): List<QaSampleLabTestResultsEntity> {
        sampleLabTestResultsRepo.findByOrderId(bsNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Results found with the following [bsNumber=$bsNumber]")
    }

    fun findSampleLabTestParametersRepoBYBSNumber(bsNumber: String): List<QaSampleLabTestParametersEntity> {
        sampleLabTestParametersRepo.findByOrderId(bsNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Paramaters found with the following [bsNumber=$bsNumber]")
    }

    fun findSampleCollectBYPermitID(permitId: Long): QaSampleCollectionEntity {
        SampleCollectionRepo.findByPermitId(permitId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample collection found with the following [permitId=$permitId]")
    }


    fun findStandardsByID(standardsID: Long): SampleStandardsEntity {
        sampleStandardsRepo.findByIdOrNull(standardsID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Standards found with the following [ID=$standardsID]")
    }

    fun findSta3BYID(id: Long): QaSta3Entity {
        sta3Repo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Sta3 found with the following [ID=$id]")
    }


    fun findSta10BYID(id: Long): QaSta10Entity {
        sta10Repo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA10 found with the following [ID=$id]")
    }

    fun findPermitBYUserIDAndId(id: Long, userId: Long): PermitApplicationsEntity {
        permitRepo.findByIdAndUserId(id, userId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit found with the following [ID=$id]")
    }

    fun findPermitBYCompanyIDAndId(id: Long, companyID: Long): PermitApplicationsEntity {
        permitRepo.findByIdAndCompanyId(id, companyID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit found with the following [ID=$id] and Company ID = $companyID")
    }

    fun findPermitBYCompanyIDAndBranchIDAndId(id: Long, companyID: Long, plantID: Long): PermitApplicationsEntity {
        permitRepo.findByIdAndCompanyIdAndAttachedPlantId(id, companyID, plantID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Permit found with the following [ID=$id] and Branch ID = $plantID and Company ID = $companyID")
    }

    fun findAllRequestByPermitRefNumber(
        permitRefNumber: String,
        permitID: Long
    ): List<PermitUpdateDetailsRequestsEntity> {
        permitUpdateDetailsRequestsRepo.findByPermitRefNumberAndPermitId(permitRefNumber, permitID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Requests found  with Permit [PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllQaRequestTypes(status: Int): List<UserRequestTypesEntity> {
        userRequestsRepo.findAllByQaRequests(status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Requests Type found")
    }


    fun findRequestWithId(id: Long): PermitUpdateDetailsRequestsEntity {
        permitUpdateDetailsRequestsRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Requests found  with  [ID=$id]")
    }

    fun findOldPermitBYPermitNumberAndVersion(permitNumber: String, versionNumber: Long): PermitApplicationsEntity {
        permitRepo.findByAwardedPermitNumberAndVersionNumber(permitNumber, versionNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Permit found with the following [PERMIT Number=$permitNumber and VERSION Number=$versionNumber]")
    }


    fun updatePermitWithDiscountWithPaymentDetails() {
        qaInvoiceCalculation.updatePermitWithZeroInvoicesAmount()
    }

    fun findPermitBYUserIDANDProductionStatus(
        permitAwardedStatus: Int,
        status: Int,
        permitTypeID: Long,
        userId: Long
    ): List<PermitApplicationsEntity> {
        permitRepo.findByUserIdAndPermitTypeAndEndOfProductionStatusAndPermitAwardStatus(
            userId,
            permitTypeID,
            status,
            permitAwardedStatus
        )?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit List found with the following user [ID=$userId]")
    }

    fun getALLLabResultsForCertainPermit(
        permit: PermitApplicationsEntity,
        permitRefNumber: String
    ): PermitSSFLabResultsDto {


        val complianceResult = mutableListOf<SSFComplianceStatusDetailsDto>()
        val result = mutableListOf<SSFPDFListDetailsDto>()

        if (permit.compliantStatus != null) {
            findSampleSubmittedListBYPermitRefNumberAndPermitID(
                permitRefNumber,
                1,
                permit.id ?: throw Exception("Missing Permit ID")
            )
                .forEach { ssf ->

                    val pdfs = findSampleSubmittedListPdfBYSSFidANdSentToManufacture(
                        ssf.id ?: throw Exception("Missing SSF ID"), 1
                    ).map { ssfPdfRemarks ->
                        SSFPDFListDetailsDto(
                            ssfPdfRemarks.pdfSavedId,
                            ssfPdfRemarks.pdfName,
                            ssfPdfRemarks.sffId,
                            ssfPdfRemarks.complianceRemarks,
                            ssfPdfRemarks.complianceStatus == 1,
                        )

                    }
                    result.addAll(pdfs)

                    val complianceResults = SSFComplianceStatusDetailsDto(
                        ssf.id,
                        ssf.bsNumber,
                        ssf.complianceRemarks,
                        ssf.resultsAnalysis == 1
                    )
                    complianceResult.add(complianceResults)
                }
        }
        return PermitSSFLabResultsDto(
            complianceResult,
            result
        )
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun assignPermitApplicationAfterPayment() {
        val map = commonDaoServices.serviceMapDetails(appId)
        //Find all permits with Paid status
        val paidPermits = findAllQAOPermitListWithPaymentStatus(map.activeStatus)
        for (permit in paidPermits) {

            try {
                //Get permit type
                var userAssigned: UsersEntity
                val userDetails =
                    commonDaoServices.findUserByID(permit.userId ?: throw ExpectedDataNotFound("MISSING USER ID"))
                when (permit.permitType) {

                    applicationMapProperties.mapQAPermitTypeIDDmark -> {
//                        userAssigned = assignNextOfficerBasedOnRegion(
//                            permit,
//                            map,
//                            applicationMapProperties.mapQADesignationIDForHODId
//                        )
                        permit.userTaskId = applicationMapProperties.mapUserTaskNameHOD
//                        permit.hodId = userAssigned.id
//                        sendEmailWithTaskDetails(
//                            userAssigned.email ?: throw ExpectedDataNotFound("Missing Email address"),
//                            permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
//                        )
//                        qualityAssuranceBpmn.startQAAppReviewProcess(
//                            permit.id ?: throw ExpectedDataNotFound("Permit Id Not found"),
//                            permit.hodId ?: throw ExpectedDataNotFound("HOD Not found")
//                        )
                        //Trigger Receive payment task BPM
//                        qualityAssuranceBpmn.diReceivePaymentComplete(
//                            permit.id ?: throw ExpectedDataNotFound("MISSING PERMIT")
//                        )
                    }

                    applicationMapProperties.mapQAPermitTypeIdSmark -> {
//                        userAssigned = assignNextOfficerBasedOnRegion(
//                            permit,
//                            map,
//                            applicationMapProperties.mapQADesignationIDForQAMId
//                        )
                        permit.userTaskId = applicationMapProperties.mapUserTaskNameQAM
//                        permit.qamId = userAssigned.id
//                        sendEmailWithTaskDetails(
//                            userAssigned.email ?: throw ExpectedDataNotFound("Missing Email address"),
//                            permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
//                        )
//                        qualityAssuranceBpmn.startQAAppReviewProcess(
//                            permit.id ?: throw ExpectedDataNotFound("Permit Id Not found"),
//                            permit.qamId ?: throw ExpectedDataNotFound("QAM Not found")
//                        )
                    }

                    applicationMapProperties.mapQAPermitTypeIdFmark -> {
//                        userAssigned = assignNextOfficerBasedOnRegion(
//                            permit,
//                            map,
//                            applicationMapProperties.mapQADesignationIDForQAMId
//                        )
                        permit.userTaskId = applicationMapProperties.mapUserTaskNameQAM
//                        permit.qamId = userAssigned.id
//                        sendEmailWithTaskDetails(
//                            userAssigned.email ?: throw ExpectedDataNotFound("Missing Email address"),
//                            permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
//                        )
//                        qualityAssuranceBpmn.startQAAppReviewProcess(
//                            permit.id ?: throw ExpectedDataNotFound("Permit Id Not found"),
//                            permit.qamId ?: throw ExpectedDataNotFound("QAM Not found")
//                        )
                    }
                }

                permit.paidStatus = map.initStatus
                permit.permitStatus = applicationMapProperties.mapQaStatusPApprovalCompletness
                permitUpdateDetails(permit, map, userDetails)

                val batchInvoice = findPermitInvoiceByPermitRefNumberANdPermitID(
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER"),
                    permit.userId ?: throw ExpectedDataNotFound("MISSING USER ID"),
                    permit.id ?: throw ExpectedDataNotFound("MISSING USER ID")
                )
                sendEmailWithProformaPaid(
                    userDetails.email ?: throw ExpectedDataNotFound("MISSING USER ID"),
                    invoiceCreationPDF(
                        batchInvoice.id,
                        userDetails
                    ).path,
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
                )
            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.debug(e.message, e)

                continue
            }
//            permitRepo.save(permit)
        }
    }

    fun findAllProductManufactureINPlantWithID(
        permitAwardedStatus: Int,
        endProductionStatus: Int,
        permitTypeID: Long,
        plantID: Long
    ): List<PermitApplicationsEntity> {
        permitRepo.findByPermitTypeAndEndOfProductionStatusAndPermitAwardStatusAndAttachedPlantId(
            permitTypeID,
            endProductionStatus,
            permitAwardedStatus,
            plantID
        )?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Product Being Manufacture List with the following plantID [ID=$plantID]")
    }

    fun findAllProductManufactureInPlantWithPlantID(
        permitAwardedStatus: Int,
        applicationStatus: Int,
        endProductionStatus: Int,
        permitTypeID: Long,
        plantID: Long
    ): List<PermitApplicationsEntity> {
//
//        val permitFoundInAwardedStatus = permitRepo.findByPermitTypeAndEndOfProductionStatusAndPermitAwardStatusAndAttachedPlantIdAndOldPermitStatusIsNull(
//                permitTypeID,
//                endProductionStatus,
//                permitAwardedStatus,
//                plantID
//            )
        val permitFoundInApplicationStatus = permitRepo.findByPermitTypeAndEndOfProductionStatusAndApplicationStatusAndAttachedPlantIdAndOldPermitStatusIsNull(
                permitTypeID,
                endProductionStatus,
                applicationStatus,
                plantID
            )

        val detailsList = mutableListOf<PermitApplicationsEntity>()
//        permitFoundInAwardedStatus?.let { detailsList.addAll(it) }
        permitFoundInApplicationStatus?.let { detailsList.addAll(it) }
        return detailsList
    }

    fun findPermitInvoiceByPermitRefNumber(permitRefNumber: String, userId: Long): InvoiceEntity {
        invoiceRepository.findByPermitRefNumberAndUserId(permitRefNumber, userId)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Invoice found with the following [PERMIT REF NO =${permitRefNumber}  and LoggedIn User]")
    }

    fun findPermitInvoiceByPermitRefNumberANdPermitID(
        permitRefNumber: String,
        userId: Long,
        permitID: Long
    ): QaInvoiceMasterDetailsEntity {
        invoiceMasterDetailsRepo.findByPermitRefNumberAndUserIdAndPermitId(permitRefNumber, userId, permitID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Invoice found with the following PERMIT REF NO =${permitRefNumber}")
    }

    fun findPermitInvoiceByPermitID(
        permitID: Long
    ): QaInvoiceMasterDetailsEntity {
        invoiceMasterDetailsRepo.findByPermitIdAndVarField10IsNull(permitID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Invoice found with the following PERMIT ID =${permitID}")
    }

    fun findSTA3WithPermitIDAndRefNumber(permitRefNumber: String, permitID: Long): QaSta3Entity {
        sta3Repo.findByPermitRefNumberAndPermitId(permitRefNumber, permitID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No STA3 found with the following PERMIT REF NO =$permitRefNumber and Permit ID =$permitID")
    }

//    fun findSTA3WithPermitRefNumber(permitRefNumber: String): QaSta3Entity {
//        sta3Repo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
//            return it
//        } ?: throw ExpectedDataNotFound("No STA3 found with the following [PERMIT REF NO =$permitRefNumber]")
//    }

    fun findAllPlantDetails(userId: Long): List<ManufacturePlantDetailsEntity> {
        manufacturePlantRepository.findByUserId(userId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [user id=$userId]")
    }

    fun findAllPlantDetailsWithCompanyDetailsAndStatus(
        companyID: Long,
        status: Int
    ): List<ManufacturePlantDetailsEntity> {
        manufacturePlantRepository.findByCompanyProfileIdAndStatus(companyID, status)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Plant details found with the following [company id=$companyID] and [status =$status]")
    }

    fun findAllCompanyWithTurnOverID(turnOverID: Long, status: Int): List<CompanyProfileEntity> {
        companyProfileRepo.findAllByFirmCategoryAndStatus(turnOverID, status)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Company details List found with the following  turnOverID=$turnOverID and status=$status")
    }

    fun findAllPlantDetailsWithCompanyID(companyID: Long): List<ManufacturePlantDetailsEntity> {
        manufacturePlantRepository.findByCompanyProfileId(companyID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [COMPANY ID =$companyID]")
    }

    fun findAllPlantDetailsWithIDAndStatus(status: Int, plantID: Long): List<ManufacturePlantDetailsEntity> {
        manufacturePlantRepository.findByStatusAndId(status, plantID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following COMPANY ID =$plantID")
    }


    fun findPlantDetails(plantID: Long): ManufacturePlantDetailsEntity {
        manufacturePlantRepository.findByIdOrNull(plantID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [id=$plantID]")
    }

    fun findPlantDetailsB(plantID: Long): ManufacturePlantDetailsEntity? {
//        manufacturePlantRepository.findByIdOrNull(plantID)?.let {
//            return it
//        } ?: "null")

        return manufacturePlantRepository.findByIdOrNull(plantID)

    }


    fun findSTA10WithPermitRefNumberANdPermitID(permitRefNumber: String, permitID: Long): QaSta10Entity {
        sta10Repo.findByPermitRefNumberAndPermitId(permitRefNumber, permitID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No STA10 found with the following PERMIT REF NO =$permitRefNumber and Permit ID =$permitID")
    }

//    fun findSTA10WithPermitRefNumberBY(permitRefNumber: String): QaSta10Entity {
//        sta10Repo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
//            return it
//        } ?: throw ExpectedDataNotFound("No STA10 found with the following [PERMIT REF NO =$permitRefNumber]")
//    }

    fun findSTA10WithPermitRefNumberBYPPermitID(permitRefNumber: String, permitID: Long): QaSta10Entity {
        sta10Repo.findByPermitRefNumberAndPermitId(permitRefNumber, permitID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA10 found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findSchemeOfSupervisionWithPermitRefNumberBY(permitRefNumber: String): QaSchemeForSupervisionEntity {
        schemeForSupervisionRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No SCHEME OF SUPERVISION found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findSchemeOfSupervisionWithPermitRefNumberBYID(permitRefNumber: String): QaSchemeForSupervisionEntity {
        schemeForSupervisionRepo.findTopByPermitRefNumberOrderByIdDesc(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No SCHEME OF SUPERVISION found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findUploadedFileBYId(fileID: Long): QaUploadsEntity {
        qaUploadsRepo.findByIdOrNull(fileID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$fileID]")
    }

    fun findUploadedFileByPermitRefNumberAndDocType(permitRefNumber: String, docType: String): QaUploadsEntity {
        qaUploadsRepo.findByPermitRefNumberAndDocumentType(permitRefNumber, docType)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No File found with the following details: [ PERMIT REF NO =$permitRefNumber], [ docType=$docType]")
    }

    //Todo : Remove
    fun findAllUploadedFileBYPermitRefNumberAndOrdinarStatus(
        permitRefNumber: String,
        status: Int
    ): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndOrdinaryStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitIDAndOrdinarStatus(permitID: Long, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitIdAndOrdinaryStatus(permitID, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT ID =$permitID]")
    }

    fun findAllUploadedFileBYPermitIDAndSta10Status(permitID: Long, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitIdAndSta10Status(permitID, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT ID =$permitID]")
    }

    fun findAllUploadedFileBYPermitIDAndSta3Status(permitID: Long, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitIdAndSta3Status(permitID, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT ID =$permitID]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndCocStatus(permitRefNumber: String, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndCocStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndAssessmentReportStatus(
        permitRefNumber: String,
        status: Int
    ): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndAssessmentReportStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndSscStatus(permitRefNumber: String, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndSscStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndJustificationReportStatusAndPermitId(
        permitRefNumber: String,
        status: Int,
        permitID: Long
    ): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndJustificationReportStatusAndPermitId(permitRefNumber, status, permitID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber and Permit ID = $permitID]")
    }

    fun findAllUploadedFileBYInspectionReportIDAndInspectionReportStatus(
        inspectReportID: Long,
        status: Int
    ): List<QaUploadsEntity> {
        qaUploadsRepo.findByInspectionReportIdAndInspectionReportStatus(inspectReportID, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ inspect Report ID =$inspectReportID]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndSta10Status(
        permitRefNumber: String,
        status: Int
    ): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndSta10Status(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findProductsManufactureWithSTA10ID(sta10Id: Long): List<QaProductManufacturedEntity>? {
        return productsManufactureSTA10Repo.findBySta10Id(sta10Id)
    }


    fun findRawMaterialsWithSTA10ID(sta10Id: Long): List<QaRawMaterialEntity>? {
        return rawMaterialsSTA10Repo.findBySta10Id(sta10Id)
    }

    fun findPersonnelWithSTA10ID(sta10Id: Long): List<QaPersonnelInchargeEntity>? {
        return qaPersonnelInchargeRepo.findBySta10Id(sta10Id)
    }

    fun findMachinePlantsWithSTA10ID(sta10Id: Long): List<QaMachineryEntity>? {
        return machinePlantsSTA10Repo.findBySta10Id(sta10Id)
    }

    fun findManufacturingProcessesWithSTA10ID(sta10Id: Long): List<QaManufacturingProcessEntity>? {
        return manufacturingProcessSTA10Repo.findBySta10Id(sta10Id)
    }

    fun findAllOldPermitWithPermitRefNumber(permitRefNumber: String): List<PermitApplicationsEntity>? {
        return permitRepo.findByPermitRefNumber(permitRefNumber)
    }

    fun companyDtoDetails(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity
    ): CommonPermitDto {
        val plantAttached =
            findPlantDetails(permit.attachedPlantId ?: throw Exception("INVALID PLANT DETAILS"))
        val companyProfile = commonDaoServices.findCompanyProfileWithID(
            plantAttached.companyProfileId ?: throw Exception("INVALID COMPANY ID DETAILS")
        )
        val directorList =
            commonDaoServices.companyDirectorList(
                companyProfile.id
                    ?: throw Exception("INVALID COMPANY ID DETAILS")
            )
        return populateCommonPermitDetails(plantAttached, companyProfile, directorList, map)
    }


    fun populateCommonPermitDetails(
        pd: ManufacturePlantDetailsEntity,
        cp: CompanyProfileEntity,
        d: List<CompanyProfileDirectorsEntity>,
        map: ServiceMapsEntity
    ): CommonPermitDto {

        val directorsNames = mutableListOf<String>()
        d.forEach { dr ->
            dr.directorName?.let { directorsNames.add(it) }
        }
        return CommonPermitDto(
            cp.name,
            directorsNames.joinToString(),
            pd.postalAddress,
            pd.physicalAddress,
            pd.contactPerson,
            pd.telephone,
            pd.emailAddress,
            pd.faxNo,
            commonDaoServices.findCountiesEntityByCountyId(
                pd.county ?: throw Exception("INVALID COUNTY ID NUMBER"),
                map.activeStatus
            ).county,
            commonDaoServices.findTownEntityByTownId(pd.town ?: throw Exception("INVALID TOWN ID NUMBER")).town,
            commonDaoServices.findRegionEntityByRegionID(
                pd.region ?: throw Exception("INVALID REGION ID NUMBER"),
                map.activeStatus
            ).region,
            cp.id,
            pd.id,
            pd.county,
            pd.town,
            pd.region,
            cp.firmCategory,
            cp.firmCategory?.let { findFirmTypeById(it).firmType }

        )
    }

    fun populateInvoiceDetails(
//        pd: ManufacturePlantDetailsEntity,
        cp: CompanyProfileEntity,
        invoice: QaBatchInvoiceEntity,
        map: ServiceMapsEntity
    ): InvoiceDto {

        return InvoiceDto(
            invoice.id,
            cp.name,
            cp.postalAddress,
            cp.physicalAddress,
            null,
            cp.companyTelephone,
            cp.companyEmail,
            invoice.invoiceNumber,
            invoice.receiptNo,
            invoice.paidDate,
            invoice.totalAmount,
            invoice.paidStatus,
            invoice.submittedStatus,
            null,
            invoice.sageInvoiceNumber,


            )
    }

    fun listBranchList(
        branchID: Long,
        map: ServiceMapsEntity
    ): List<PermitEntityDto> {
        return listPermits(findAllBranchPermits(branchID), map)
    }

    fun listBranchListWithPermitType(
        branchID: Long,
        permitType: Long,
        map: ServiceMapsEntity
    ): List<PermitEntityDto> {
        return listPermits(findAllBranchPermitsWithPermitType(branchID, permitType), map)
    }

    fun listFirmPermitList(
        companyID: Long,
        map: ServiceMapsEntity
    ): List<PermitEntityDto> {
        return listPermits(findAllFirmPermits(companyID), map)
    }

    fun listFirmPermitListWithPermitType(
        companyID: Long,
        permitType: Long,
        map: ServiceMapsEntity
    ): List<PermitEntityDto> {
        return listPermits(findAllFirmPermitsWithPermitType(companyID, permitType), map)
    }

    fun listPermits(permits: List<PermitApplicationsEntity>, map: ServiceMapsEntity): List<PermitEntityDto> {
        return permits.map { p ->
            PermitEntityDto(
                p.id,
                p.attachedPlantId?.let {
                    commonDaoServices.findCompanyProfileWithID(
                        findPlantDetailsB(it)?.companyProfileId ?: -1L
                    ).name
                },
                p.permitRefNumber,
                p.commodityDescription,
                p.tradeMark,
                p.awardedPermitNumber,
                p.dateOfIssue,
                p.dateOfExpiry,
                p.permitStatus?.let { findPermitStatus(it).processStatusName },
                p.userId,
                p.createdOn,
                p.attachedPlantId?.let {
                    commonDaoServices.findCountiesEntityByCountyId(
                        findPlantDetailsB(it)?.county ?: -1L, map.activeStatus
                    ).county
                },
                p.attachedPlantId?.let {
                    commonDaoServices.findTownEntityByTownId(
                        findPlantDetailsB(it)?.town ?: -1L
                    ).town
                },
                p.attachedPlantId?.let {
                    commonDaoServices.findRegionEntityByRegionID(
                        findPlantDetailsB(it)?.region ?: 1L, map.activeStatus
                    ).region
                },
                p.divisionId?.let { commonDaoServices.findDivisionWIthId(it).division },
                p.sectionId?.let { commonDaoServices.findSectionWIthId(it).section },
                p.permitAwardStatus == 1,
                p.permitExpiredStatus == 1,
                p.userTaskId,
                p.companyId,
                p.permitType,
                p.permitStatus,
                p.versionNumber,
                encryptedPermitId = jasyptStringEncryptor.encrypt(p.id.toString()),
                encryptedUserId = jasyptStringEncryptor.encrypt(p.userId.toString())

            )
        }
    }


    fun listInspectionReports(
        inspectionReports: List<QaInspectionReportRecommendationEntity>,
        map: ServiceMapsEntity
    ): List<InspectionReportDto> {
        return inspectionReports.map { i ->
            InspectionReportDto(
                i.id,
                i.recommendations,
                i.refNo,
                i.inspectorComments,
                i.inspectorName,
                i.inspectorDate,
                i.supervisorComments,
                i.supervisorName,
                i.supervisorDate,
                i.description,
                i.permitId,
                i.permitRefNumber,
                i.filledQpsmsStatus,
                i.filledInspectionTestingStatus,
                i.filledStandardizationMarkSchemeStatus,
                i.filledOpcStatus,
                i.filledHaccpImplementationStatus,
                i.submittedInspectionReportStatus,
                i.supervisorFilledStatus,
                i.approvedRejectedStatus,
                i.status,
                i.varField1,
                i.varField2,
                i.varField3,
                i.varField4,
                i.varField5,
                i.varField6,
                i.varField7,
                i.varField8,
                i.varField9,
                i.varField10,
                i.createdBy,
                i.createdOn,
                i.modifiedBy,
                i.modifiedOn,
                i.deleteBy,
                i.deletedOn,
                encryptedInspectionId = jasyptStringEncryptor.encrypt(i.id.toString())
            )
        }
    }

    fun listSTA10Product(sta10Products: List<QaProductManufacturedEntity>): List<STA10ProductsManufactureDto> {
        return sta10Products.map { p ->
            STA10ProductsManufactureDto(
                p.id,
                p.productName,
                p.productBrand,
                p.productStandardNumber,
                p.available == 1,
                p.permitNo,
            )
        }
    }

    fun listFilesDto(fileList: List<QaUploadsEntity>): List<FilesListDto> {
        return fileList.map { f ->
            FilesListDto(
                f.id,
                f.name,
                f.fileType,
                f.documentType,
                f.versionNumber,
                f.document,
            )
        }
    }

    fun listInvoicePerDetailsDto(detailsList: List<QaInvoiceDetailsEntity>): List<InvoicePerDetailsDto> {
        return detailsList.map { f ->
            InvoicePerDetailsDto(
                f.id,
                f.itemDescName,
                f.itemAmount,
                f.inspectionStatus == 1,
                f.permitStatus == 1,
                f.fmarkStatus == 1,
            )
        }
    }

    fun filesDtoDetails(f: QaUploadsEntity): FilesListDto {
        return FilesListDto(
            f.id,
            f.name,
            f.fileType,
            f.documentType,
            f.versionNumber,
            f.document,
        )
    }

    fun listSTA10RawMaterials(qaRawMaterialEntity: List<QaRawMaterialEntity>): List<STA10RawMaterialsDto> {
        return qaRawMaterialEntity.map { p ->
            STA10RawMaterialsDto(
                p.id,
                p.name,
                p.origin,
                p.specifications,
                p.qualityChecksTestingRecords,
            )
        }
    }


    fun listSTA10Personnel(qaPersonnelIncharge: List<QaPersonnelInchargeEntity>): List<STA10PersonnelDto> {
        return qaPersonnelIncharge.map { p ->
            STA10PersonnelDto(
                p.id,
                p.personnelName,
                p.qualificationInstitution,
                p.dateOfEmployment,
            )
        }
    }

    fun listSTA10MachinePlants(machinePlantsDetails: List<QaMachineryEntity>): List<STA10MachineryAndPlantDto> {
        return machinePlantsDetails.map { p ->
            STA10MachineryAndPlantDto(
                p.id,
                p.machineName,
                p.typeModel,
                p.countryOfOrigin,
            )
        }
    }


    fun listSTA10ManufacturingProcess(manufacturingProcessDetails: List<QaManufacturingProcessEntity>): List<STA10ManufacturingProcessDto> {
        return manufacturingProcessDetails.map { p ->
            STA10ManufacturingProcessDto(
                p.id,
                p.processFlowOfProduction,
                p.operations,
                p.criticalProcessParametersMonitored,
                p.frequency,
                p.processMonitoringRecords,
            )
        }
    }

    fun listPermitsInvoices(
        permitInvoices: List<QaInvoiceMasterDetailsEntity>,
        plantID: Long?,
        map: ServiceMapsEntity
    ): List<PermitInvoiceDto> {
        val permitsInvoiceList = mutableListOf<PermitInvoiceDto>()
        permitInvoices.map { pi ->
            val permitDetails = pi.permitId?.let { findPermitBYID(it) }
            if (plantID != null) {
                if (permitDetails?.attachedPlantId == plantID) {
                    permitsInvoiceList.add(
                        PermitInvoiceDto(
                            pi.permitId,
                            pi.invoiceRef,
                            permitDetails.commodityDescription,
                            permitDetails.tradeMark,
                            pi.totalAmount,
                            pi.paymentStatus,
                            pi.permitRefNumber,
                            pi.batchInvoiceNo

                        )
                    )
                } else {
                    KotlinLogging.logger { }
                        .info { "::::::::::::::::::::;NO PERMIT FOUND WITH PLANT ID  = $plantID:::::::::::::::::::::::::::" }
                }
            } else {
                permitsInvoiceList.add(
                    PermitInvoiceDto(
                        pi.permitId,
                        pi.invoiceRef,
                        permitDetails?.commodityDescription,
                        permitDetails?.tradeMark,
                        pi.totalAmount,
                        pi.paymentStatus,
                        pi.permitRefNumber,
                        pi.batchInvoiceNo
                    )
                )
            }

        }
        return permitsInvoiceList.sortedBy { it.permitID }
    }


    fun permitsInvoiceDTO(
        permitInvoices: QaInvoiceMasterDetailsEntity,
        permitDetails: PermitApplicationsEntity,
    ): PermitInvoiceDto {
        return PermitInvoiceDto(
            permitInvoices.permitId,
            permitInvoices.invoiceRef,
            permitDetails.commodityDescription,
            permitDetails.tradeMark,
            permitInvoices.totalAmount,
            permitInvoices.paymentStatus,
            permitInvoices.permitRefNumber
        )
    }

    fun permitsRemarksDTO(
        permitDetails: PermitApplicationsEntity,
    ): PermitAllRemarksDetailsDto {
        var hofQamCompleteness: RemarksAndStatusDto? = null
        var labResultsCompleteness: RemarksAndStatusDto? = null
        var pcmApproval: RemarksAndStatusDto? = null
        var pscMemberApproval: RemarksAndStatusDto? = null
        var pcmReviewApproval: RemarksAndStatusDto? = null
        var justificationReport: RemarksAndStatusDto? = null
        when {
            permitDetails.hofQamCompletenessStatus != null -> {
                hofQamCompleteness = RemarksAndStatusDto(
                    permitDetails.hofQamCompletenessStatus == 1,
                    permitDetails.hofQamCompletenessRemarks,
                )
            }
        }
        when {
            permitDetails.pscMemberApprovalStatus != null -> {
                pcmApproval = RemarksAndStatusDto(
                    permitDetails.pcmApprovalStatus == 1,
                    permitDetails.pcmApprovalRemarks,
                )
            }
        }
        when {
            permitDetails.compliantStatus != null -> {
                labResultsCompleteness = RemarksAndStatusDto(
                    permitDetails.compliantStatus == 1,
                    permitDetails.compliantRemarks,
                )
            }
        }
        when {
            permitDetails.pscMemberApprovalRemarks != null -> {
                pscMemberApproval = RemarksAndStatusDto(
                    permitDetails.pscMemberApprovalStatus == 1,
                    permitDetails.pscMemberApprovalRemarks,
                )
            }
        }
        when {
            permitDetails.pcmReviewApprovalStatus != null -> {
                pcmReviewApproval = RemarksAndStatusDto(
                    permitDetails.pcmReviewApprovalStatus == 1,
                    permitDetails.pcmReviewApprovalRemarks,
                )
            }
        }
        when {
            permitDetails.justificationReportStatus != null -> {
                justificationReport = RemarksAndStatusDto(
                    permitDetails.justificationReportStatus == 1,
                    permitDetails.justificationReportRemarks,
                )
            }
        }

        return PermitAllRemarksDetailsDto(
            hofQamCompleteness,
            labResultsCompleteness,
            pcmApproval,
            pscMemberApproval,
            pcmReviewApproval,
            justificationReport,
        )
    }


    fun permitsInvoiceDetailsDTO(
        permitDetails: PermitApplicationsEntity,
    ): InvoiceDetailsDto? {
        return when (permitDetails.invoiceGenerated) {
            1 -> {
                val v: QaInvoiceMasterDetailsEntity = when {
                    permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIdFmark && permitDetails.smarkGeneratedFrom == 1 -> {
                        val findSMarkID = findSmarkWithFmarkId(permitDetails.id ?: throw Exception("MISSING PERMIT ID")).smarkId
                        val findSMark = findPermitBYUserIDAndId(
                            findSMarkID ?: throw Exception("NO SMARK ID FOUND WITH FMARK ID"),
                            permitDetails.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
                        )
                        findPermitInvoiceByPermitID(findSMark.id ?: throw ExpectedDataNotFound("MISSING PERMIT ID"))

                    }

                    else -> {
                        findPermitInvoiceByPermitID(permitDetails.id ?: throw ExpectedDataNotFound("MISSING PERMIT ID"))
                    }
                }

                val myList = findALlInvoicesPermitWithMasterInvoiceID(v.id, 1)
                InvoiceDetailsDto(
                    v.id,
                    v.invoiceRef,
                    v.description,
                    v.taxAmount,
                    v.subTotalBeforeTax,
                    v.totalAmount,
                    listInvoicePerDetailsDto(myList)
                )
            }

            else -> {
                null
            }
        }
    }

    fun permitDetails(permit: PermitApplicationsEntity, map: ServiceMapsEntity): PermitDetailsDto {
        val plantAttached = permit.attachedPlantId?.let { findPlantDetails(it) }
        val permitType = findPermitType(permit.permitType ?: throw Exception("Permit TYPE ID IS MISSING"))
        val companyProfile = plantAttached?.companyProfileId?.let { commonDaoServices.findCompanyProfileWithID(it) }
        val p = PermitDetailsDto()
        with(p) {
            Id = permit.id
            permitNumber = permit.awardedPermitNumber
            permitRefNumber = permit.permitRefNumber
            firmName = companyProfile?.name
            postalAddress = plantAttached?.postalAddress
            physicalAddress = plantAttached?.physicalAddress
            contactPerson = plantAttached?.contactPerson
            telephoneNo = plantAttached?.telephone
            regionPlantValue =
                plantAttached?.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus).region }
            countyPlantValue = plantAttached?.county?.let {
                commonDaoServices.findCountiesEntityByCountyId(
                    it,
                    map.activeStatus
                ).county
            }
            townPlantValue = plantAttached?.town?.let { commonDaoServices.findTownEntityByTownId(it).town }
            location = plantAttached?.location
            street = plantAttached?.street
            buildingName = plantAttached?.buildingName
            nearestLandMark = plantAttached?.nearestLandMark
            faxNo = plantAttached?.faxNo
            plotNo = plantAttached?.plotNo
            email = plantAttached?.emailAddress
            createdOn = permit.createdOn
            dateOfIssue = permit.dateOfIssue
            dateOfExpiry = permit.dateOfExpiry
            effectiveDate = permit.effectiveDate
            commodityDescription = permit.commodityDescription
            brandName = permit.tradeMark
            varField7 = permit.varField7


            if (permit.productStandard != null) {
                val standardsDetails = findStandardsByID(
                    permit.productStandard
                        ?: throw Exception("INVALID STANDARDS NUMBER [ID = ${permit.productStandard}]")
                )
                standardNumber = standardsDetails.standardNumber
                standardTitle = standardsDetails.standardTitle
            }

            permitForeignStatus = permit.permitForeignStatus == 1

            when (permit.assignOfficerStatus) {
                map.activeStatus -> {
                    assignOfficer = commonDaoServices.concatenateName(
                        commonDaoServices.findUserByID(
                            permit.qaoId ?: throw Exception("INVALID QAO ID")
                        )
                    )
                }
            }
            when (permit.assignAssessorStatus) {
                map.activeStatus -> {
                    assignAssessor = commonDaoServices.concatenateName(
                        commonDaoServices.findUserByID(
                            permit.assessorId ?: throw Exception("INVALID ASSESSOR ID")
                        )
                    )
                }
            }

            divisionValue = permit.divisionId?.let { commonDaoServices.findDivisionWIthId(it).division }
            sectionValue = permit.sectionId?.let { commonDaoServices.findSectionWIthId(it).section }
            inspectionDate = permit.inspectionDate
            inspectionScheduledStatus = permit.inspectionScheduledStatus == 1
            assessmentDate = permit.assessmentDate
            assessmentScheduledStatus = permit.assessmentScheduledStatus == 1
            processStatusName = permit.permitStatus?.let { findPermitStatus(it).processStatusName }
            versionNumber = permit.versionNumber
            fmarkGenerated = permit.fmarkGenerated == 1
            recommendationRemarks = permit.recommendationRemarks
            factoryVisit = permit.factoryVisit
            firmTypeID = companyProfile?.firmCategory
            firmTypeName = companyProfile?.firmCategory?.let { findFirmTypeById(it).firmType }
            permitTypeName = permitType.typeName
            permitTypeID = permitType.id
            permitAwardStatus = permit.permitAwardStatus == 1
            invoiceGenerated = permit.invoiceGenerated == 1
            approvedRejectedScheme = permit.approvedRejectedScheme == 1
            sendForPcmReview = permit.sendForPcmReview == 1
            sendApplication = permit.sendApplication == 1
            pcmReviewApprove = permit.pcmReviewApprovalStatus == 1
            hofQamCompletenessStatus = permit.hofQamCompletenessStatus == 1
            generateSchemeStatus = permit.generateSchemeStatus == 1
            resubmitApplicationStatus = permit.resubmitApplicationStatus == 1
            processStep = permit.processStep
            processStatusID = permit.permitStatus
            if (permit.fmarkGenerated == 1) {
                fmarkGeneratedID = permit.id?.let { findFmarkWithSmarkId(it).fmarkId }
            }
            oldPermitStatus = permit.oldPermitStatus == 1
            encryptedPermitId = jasyptStringEncryptor.encrypt(permit.id.toString())
            encryptedUserId = jasyptStringEncryptor.encrypt(permit.userId.toString())


        }
        return p
    }

    fun mapAllPermitDetailsTogether(
        permit: PermitApplicationsEntity,
        batchID: Long?,
        map: ServiceMapsEntity
    ): AllPermitDetailsDto {
        return AllPermitDetailsDto(
            permitDetails(permit, map),
            permitsRemarksDTO(permit),
            permitsInvoiceDetailsDTO(permit),
            commonDaoServices.userListDto(
                findOfficersList(
                    permit.attachedPlantId ?: throw Exception("MISSING PLANT ID"),
                    permit,
                    map,
                    applicationMapProperties.mapQADesignationIDForQAOId
                )
            ),
            findAllOldPermitWithPermitRefNumber(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER")
            )?.let { listPermits(it, map) },
            findAllUploadedFileBYPermitIDAndOrdinarStatus(
                permit.id ?: throw Exception("MISSING PERMIT ID"),
                1
            ).let { listFilesDto(it) },
            findAllUploadedFileBYPermitIDAndSta3Status(
                permit.id ?: throw Exception("MISSING PERMIT ID"),
                1
            ).let { listFilesDto(it) },
            findAllUploadedFileBYPermitIDAndSta10Status(
                permit.id ?: throw Exception("MISSING PERMIT ID"),
                1
            ).let { listFilesDto(it) },
            getALLLabResultsForCertainPermit(
                permit,
                permit.permitRefNumber ?: throw Exception("Missing Permit Ref Number")
            ),
            permit.sscId?.let { findUploadedFileBYId(it).let { f -> filesDtoDetails(f) } },
            batchID
        )
    }

    fun listWorkPlan(workPlan: List<QaWorkplanEntity>, map: ServiceMapsEntity): List<WorkPlanDto> {
        val permitsList = mutableListOf<WorkPlanDto>()
        workPlan.map { wp ->
            val permit = findPermitBYID(wp.permitId ?: throw Exception("INVALID PERMIT ID"))
            val permitDetailsCommon = companyDtoDetails(permit, map)
            permitsList.add(
                WorkPlanDto(
                    permitDetailsCommon.firmName,
                    wp.refNumber,
                    wp.permitNumber,
                    permitDetailsCommon.physicalAddress,
                    permitDetailsCommon.town,
                    permit.commodityDescription,
                    permit.dateOfIssue,
                    permit.dateOfExpiry,
                    wp.visitsScheduled
                )
            )
        }
        return permitsList.sortedBy { it.visitsScheduled }
    }

    fun listSTA10ViewDetails(
        sta10FirmDetails: STA10SectionADto,
        sta10PersonnelDetails: List<STA10PersonnelDto>,
        sta10ProductsManufactureDetails: List<STA10ProductsManufactureDto>,
        sta10RawMaterialsDetails: List<STA10RawMaterialsDto>,
        sta10MachineryAndPlantDetails: List<STA10MachineryAndPlantDto>,
        sta10ManufacturingProcessDetails: List<STA10ManufacturingProcessDto>,
        sta10FileList: List<QaUploadsEntity>,
    ): AllSTA10DetailsDto {
        return AllSTA10DetailsDto(
            sta10FirmDetails,
            sta10PersonnelDetails,
            sta10ProductsManufactureDetails,
            sta10RawMaterialsDetails,
            sta10MachineryAndPlantDetails,
            sta10ManufacturingProcessDetails,
            listFilesDto(sta10FileList)
        )
    }


    fun findOfficersList(
        plantID: Long,
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        roleID: Long
    ): List<UsersEntity> {

        val plantAttached = findPlantDetails(plantID)


        usersRepo.findOfficerPermitUsersBySectionAndRegion(
            roleID,
            permit.sectionId ?: throw ExpectedDataNotFound("MISSING SECTION ID ON PERMIT"),
            plantAttached.region ?: throw ExpectedDataNotFound("MISSING REGION ID ON BRANCH ATTACHED ON PERMIT"),
            1
        )
            ?.let {
                return it
            } ?: throw ExpectedDataNotFound("NO USER LIST FOUND")
    }

    fun findOfficers(
        roleID: Long
    ): List<UsersEntity> {
        usersRepo.findOfficer(
            roleID,

            1
        )
            ?.let {
                return it
            } ?: throw ExpectedDataNotFound("NO USER LIST FOUND")
    }

    fun assignNextOfficerAfterPayment(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        designationID: Long
    ): UsersEntity? {
        val plantID = permit.attachedPlantId
            ?: throw ServiceMapNotFoundException("Attached Plant details For Permit with ID = ${permit.id}, is Empty")

        val plantAttached = findPlantDetails(plantID)
        val designation = commonDaoServices.findDesignationByID(designationID)
        val region = plantAttached.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Plant attached Region Id is Empty, check config")
        val department = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)


        return commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(
            designation,
            region,
            department,
            map.activeStatus
        ).userId

    }

    fun assignNextOfficerBasedOnSection(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        designationID: Long
    ): UsersEntity {
        val plantID = permit.attachedPlantId
            ?: throw ServiceMapNotFoundException("Attached Plant details For Permit with ID = ${permit.id}, is Empty")

        val plantAttached = findPlantDetails(plantID)
        val designation = commonDaoServices.findDesignationByID(designationID)
        val section = commonDaoServices.findSectionWIthId(
            permit.sectionId ?: throw ExpectedDataNotFound("SECTION VALUE IS MISSING")
        )
        val region = plantAttached.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Plant attached Region Id is Empty, check config")
        val department = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)

        return commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatusAndSection(
            designation,
            section,
            region,
            department,
            map.activeStatus
        ).userId ?: throw ExpectedDataNotFound("MISSING USER DETAILS")

    }

    fun assignNextOfficerBasedOnRegion(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        designationID: Long
    ): UsersEntity {
        val plantID = permit.attachedPlantId
            ?: throw ServiceMapNotFoundException("Attached Plant details For Permit with ID = ${permit.id}, is Empty")

        val plantAttached = findPlantDetails(plantID)
        val designation = commonDaoServices.findDesignationByID(designationID)
//        val section = commonDaoServices.findSectionWIthId(
//            permit.sectionId ?: throw ExpectedDataNotFound("SECTION VALUE IS MISSING")
//        )
        val region = plantAttached.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Plant attached Region Id is Empty, check config")
        val department = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)

        return commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(
            designation,
            region,
            department,
            map.activeStatus
        ).userId ?: throw ExpectedDataNotFound("MISSING USER DETAILS")

    }

    fun assignNextOfficerWithDesignation(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        designationID: Long
    ): UsersEntity? {
        val designation = commonDaoServices.findDesignationByID(designationID)
        return commonDaoServices.findUserProfileWithDesignationAndStatus(designation, map.activeStatus).userId

    }

    fun findAllUsersByDesignation(
        map: ServiceMapsEntity,
        designationID: Long
    ): List<UserProfilesEntity> {
        val designation = commonDaoServices.findDesignationByID(designationID)
        return commonDaoServices.findAllUsersProfileWithDesignationAndStatus(designation, map.activeStatus)
    }

    fun findAllPcmOfficers(): MutableList<UsersEntity?>? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val pcmUserProfiles = this.findAllUsersByDesignation(map, applicationMapProperties.mapQADesignationIDForPCMId)

        return pcmUserProfiles
            .stream()
            .map { x -> x.userId?.id?.let { commonDaoServices.findUserByID(it) } }
            .collect(Collectors.toList())
    }

    fun sendAppointAssessorNotificationEmail(recipientEmail: String, permit: PermitApplicationsEntity): Boolean {
        val subject = "DMARK Application Assessment"
        val messageBody = "DMARK application with the details below has been assisgned to you for assessment: " +
                "for the following permit Ref NUMBER : ${permit.permitRefNumber} \n" +
                "\n " +
                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }

    fun sendScheduledFactoryAssessmentNotificationEmail(
        recipientEmail: String,
        permit: PermitApplicationsEntity
    ): Boolean {
        val subject = "Factory Assessment Visit Schedule"
        val messageBody = "Factory visit assessment has been scheduled on: ${permit.assessmentDate}:  \n" +
                "\n " +
                "The Assessment Criteria:" +
                "\n " +
                "${permit.assessmentCriteria}."
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }

    fun sendPacDmarkAssessmentNotificationEmail(recipientEmail: String, permit: PermitApplicationsEntity): Boolean {
        val subject = "DMARK Factory Conformity Status"
        val messageBody =
            "Dmark assessment report and conformity status is available for approval, With Application Ref Number: ${permit.permitRefNumber} \n" +
                    "\n " +
                    "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }

    fun permitInsertStatus(
        permit: PermitApplicationsEntity,
        statusID: Long,
        user: UsersEntity
    ): PermitApplicationsEntity {
        with(permit) {
            permitStatus = statusID
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }

        return permitRepo.save(permit)
    }

    fun permitUpdate(
        permit: PermitApplicationsEntity,
        user: String
    ): PermitApplicationsEntity {
        with(permit) {
            modifiedBy = user
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return permitRepo.save(permit)
    }

    fun permitSave(
        permits: PermitApplicationsEntity,
        permitTypeDetails: PermitTypesEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var savePermit = permits
        var maxpermit = permitRepo.getMaxId()
        if (maxpermit.toString().contains("REF")) {
            maxpermit = 60000.toString()
        }

        var newMaxPermit = maxpermit?.plus(1)

        try {

            with(savePermit) {
                userId = user.id
                productName = permits.commodityDescription
                permitType = permitTypeDetails.id
                permitRefNumber = "REF${permitTypeDetails.markNumber}${
                    generateRandomText(
                        5,
                        map.secureRandom,
                        map.messageDigestAlgorithm,
                        true
                    )
                }".toUpperCase()
                enabled = map.initStatus
                divisionId = commonDaoServices.findSectionWIthId(
                    sectionId ?: throw ExpectedDataNotFound("SECTION ID IS MISSING")
                ).divisionId?.id
                versionNumber = 1
                endOfProductionStatus = map.inactiveStatus
                companyId = user.companyId
                status = map.activeStatus
                fmarkGenerated = map.inactiveStatus
                permitStatus = applicationMapProperties.mapQaStatusDraft
                userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }


            savePermit = permitRepo.save(savePermit)

            sr.payload = "New Permit Saved [Firm name${savePermit.firmName} and ${savePermit.id}]"
            sr.names = "${savePermit.firmName}"
            sr.varField1 = savePermit.id.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, savePermit)
    }

    fun permitRequests(
        permitsRequest: PermitUpdateDetailsRequestsEntity,
        permitID: Long,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, PermitUpdateDetailsRequestsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var savePermitRequest = permitsRequest
        try {

            with(savePermitRequest) {
                permitId = permitID
                requestStatus = map.inactiveStatus
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }


            savePermitRequest = permitUpdateDetailsRequestsRepo.save(savePermitRequest)

            sr.payload = "New Permit request Saved [Firm name${savePermitRequest.permitId} and ${savePermitRequest.id}]"
            sr.names = "${savePermitRequest.permitId}"
            sr.varField1 = savePermitRequest.permitId.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, savePermitRequest)
    }

    fun ssfSave(
        permits: PermitApplicationsEntity,
        ssfDetails: QaSampleSubmissionEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSF = ssfDetails
        try {

            with(saveSSF) {
                bsNumber?.toUpperCase()
                permitId = permits.id
                permitRefNumber = permits.permitRefNumber
                status = map.activeStatus
                labResultsStatus = map.inactiveStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }


            saveSSF = SampleSubmissionRepo.save(saveSSF)

            sr.payload = "New SSF Saved [BRAND name${saveSSF.brandName} and ${saveSSF.id}]"
            sr.names = "${saveSSF.brandName}"
            sr.varField1 = permits.id.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveSSF)
    }


    fun ssfUpdateDetails(
        ssfID: Long,
        ssfDetails: QaSampleSubmissionEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSF = findSampleSubmittedBYID(ssfID)
        try {

            with(saveSSF) {
                complianceRemarks = ssfDetails.complianceRemarks
                resultsAnalysis = ssfDetails.resultsAnalysis
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }


            saveSSF = SampleSubmissionRepo.save(saveSSF)

            val permitDetails = findPermitBYID(
                saveSSF.permitId ?: throw Exception("MISSING PERMIT ID")
            )

            permitDetails.compliantRemarks = saveSSF.complianceRemarks
            permitDetails.compliantStatus = saveSSF.resultsAnalysis
            permitDetails.testReportId = saveSSF.labReportFileId
            var complianceValue: String? = null
            when (permitDetails.compliantStatus) {
                map.activeStatus -> {
                    complianceValue = "COMPLIANT"
                    if (permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIDDmark) {
                        permitDetails.userTaskId = applicationMapProperties.mapUserTaskNameQAO
                        permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPGeneJustCationReport,
                            user
                        )
                    } else {
                        permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPRecommendation, user)
                    }
                }

                map.inactiveStatus -> {
                    complianceValue = "NON-COMPLIANT"
                    permitDetails.resubmitApplicationStatus = 1
                    permitDetails.userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                    permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPendingCorrectionManf, user)
                }
            }

//            val fileUploaded = findUploadedFileBYId(
//                saveSSF.labReportFileId ?: throw ExpectedDataNotFound("MISSING LAB REPORT FILE ID STATUS")
//            )
//            val fileContent = limsServices.mainFunctionLimsGetPDF(
//                saveSSF.bsNumber ?: throw ExpectedDataNotFound("MISSING LBS NUMBER"),
//                saveSSF.pdfSelectedName ?: throw ExpectedDataNotFound("MISSING FILE NAME")
//            )
//            val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
            sendComplianceStatusAndLabReport(
                permitDetails,
                complianceValue ?: throw ExpectedDataNotFound("MISSING COMPLIANCE STATUS"),
                saveSSF.complianceRemarks ?: throw ExpectedDataNotFound("MISSING COMPLIANCE REMARKS"),
                null
            )

//            sendEmailWithLabResults(
//                commonDaoServices.findUserByID(
//                    permitDetails.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
//                ).email ?: throw ExpectedDataNotFound("MISSING USER ID"),
//                fileContent.path,
//                permitDetails.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
//            )


            sr.payload = "New SSF Saved [BRAND name${saveSSF.brandName} and ${saveSSF.id}]"
            sr.names = "${saveSSF.brandName}"
            sr.varField1 = permitDetails.id.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveSSF)
    }

    fun ssfUpdateComplianceDetails(
        complianceSaveID: Long,
        ssfPDFDetails: QaSampleSubmittedPdfListDetailsEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSFPdf = findSampleSubmittedPDfBYID(complianceSaveID)
        val savedSSF = findSampleSubmittedBYID(saveSSFPdf.sffId ?: throw Exception("MISSING SSF ID"))
        try {

            with(saveSSFPdf) {
                sentToManufacturerStatus = 1
                complianceRemarks = ssfPDFDetails.complianceRemarks
                complianceStatus = ssfPDFDetails.complianceStatus
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }


            saveSSFPdf = SampleSubmissionSavedPdfListRepo.save(saveSSFPdf)


            val permitDetails = findPermitWithPermitRefNumberLatest(
                savedSSF.permitRefNumber ?: throw Exception("MISSING permit Ref Number")
            )
//
////            var complianceValue: String? = null
//            when (saveSSFPdf.complianceStatus) {
//                map.activeStatus -> {
////                    complianceValue = "COMPLIANT"
//                    if (permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIDDmark) {
//                        permitInsertStatus(
//                            permitDetails,
//                            applicationMapProperties.mapQaStatusPGeneJustCationReport,
//                            user )
//                    } else {
//                        permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPRecommendation, user)
//                    }
//                }
//                map.inactiveStatus -> {
////                    complianceValue = "NON-COMPLIANT"
//                    permitDetails.userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
//                    permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPendingCorrectionManf, user)
//                }
//            }

//            val fileUploaded = findUploadedFileBYId(
//                saveSSFPdf.pdfSavedId ?: throw ExpectedDataNotFound("MISSING LAB REPORT FILE ID STATUS")
//            )
//            val fileContent = limsServices.mainFunctionLimsGetPDF(
//                savedSSF.bsNumber ?: throw ExpectedDataNotFound("MISSING LBS NUMBER"),
//                saveSSFPdf.pdfName ?: throw ExpectedDataNotFound("MISSING FILE NAME")
//            )
//            val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
//            sendComplianceStatusAndLabReport(
//                permitDetails,
//                complianceValue ?: throw ExpectedDataNotFound("MISSING COMPLIANCE STATUS"),
//                saveSSFPdf.complianceRemarks ?: throw ExpectedDataNotFound("MISSING COMPLIANCE REMARKS"),
//                mappedFileClass.document.toString()
//            )
//
//            sendEmailWithLabResults(
//                commonDaoServices.findUserByID(
//                    permitDetails.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
//                ).email ?: throw ExpectedDataNotFound("MISSING USER ID"),
//                mappedFileClass.document.toString(),
//                permitDetails.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
//            )


            sr.payload = "New SSF Saved [BRAND name${savedSSF.brandName} and ${savedSSF.id}]"
            sr.names = "${savedSSF.brandName}"
            sr.varField1 = permitDetails.id.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, savedSSF)
    }

    fun requestUpdateDetails(
        requestID: Long,
        requestDetails: PermitUpdateDetailsRequestsEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, PermitUpdateDetailsRequestsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        val requestFound = findRequestWithId(requestID)
        var saveRequest =
            commonDaoServices.updateDetails(requestDetails, requestFound) as PermitUpdateDetailsRequestsEntity
        try {

            with(saveRequest) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }

            saveRequest = permitUpdateDetailsRequestsRepo.save(saveRequest)

            sr.payload = "New Request Saved [BRAND NAME ${saveRequest.brandName} and ID = ${saveRequest.id}]"
            sr.names = "${saveRequest.brandName}"
            sr.varField1 = saveRequest.permitId.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveRequest)
    }

    fun newSchemeSupervisionSave(
        permits: PermitApplicationsEntity,
        schemeSupervision: QaSchemeForSupervisionEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(map)
        try {
            var saveSSC = schemeSupervision
            with(saveSSC) {
                permitId = permits.id
                status = map.inactiveStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }


            saveSSC = schemeForSupervisionRepo.save(saveSSC)

            schemeSendToManufacture(permits)

            sr.payload = "New Scheme Saved [ID ${saveSSC.id} and ${saveSSC.permitId}]"
            sr.names = "${saveSSC.createdBy}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun schemeSendToManufacture(permits: PermitApplicationsEntity) {
        //todo: for now lets work with this i will change it
        val userPermit = permits.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "SCHEME FOR SUPERVISION AND CONTROL (SSC)"
        val messageBody = "Dear ${userPermit?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "Scheme For Supervision And Control has been Generated that needs your approval for the process to continue:" +
                "\n " +
                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permits.id}"

        userPermit?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun schemeSupervisionUpdateSave(
        schemeID: Long,
        schemeSupervision: QaSchemeForSupervisionEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(map)
        try {
            var foundSSC = schemeForSupervisionRepo.findByIdOrNull(schemeID)
                ?: throw ExpectedDataNotFound("Scheme with [Id = $schemeID], does not exist")
            schemeSupervision.id = foundSSC.id

            foundSSC = commonDaoServices.updateDetails(schemeSupervision, foundSSC) as QaSchemeForSupervisionEntity

            with(foundSSC) {
                status = map.activeStatus
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }

            foundSSC = schemeForSupervisionRepo.save(foundSSC)

            var reasonValue: String? = null
            when {
                schemeSupervision.acceptedRejectedStatus == map.activeStatus -> {
                    var permitUpdate = schemeUpdatePermit(foundSSC, map.activeStatus)

                    permitUpdate = permitUpdateDetails(permitUpdate, map, user).second
                    reasonValue = "ACCEPTED"
//                    schemeSendEmail(permitUpdate, reasonValue, foundSSC)
                }

                schemeSupervision.acceptedRejectedStatus == map.inactiveStatus -> {
                    var permitUpdate = schemeUpdatePermit(foundSSC, map.inactiveStatus)
                    permitUpdate.generateSchemeStatus = map.inactiveStatus
                    permitUpdate = permitUpdateDetails(permitUpdate, map, user).second
                    reasonValue = "REJECTED"

//                    schemeSendEmail(permitUpdate, reasonValue, foundSSC)
                }

                schemeSupervision.status == map.inactiveStatus -> {
                    var permitUpdate = schemeUpdatePermit(foundSSC, null)
                    permitUpdate = permitUpdateDetails(permitUpdate, map, user).second

                    schemeSendToManufacture(permitUpdate)
                }
            }

            sr.payload = "UPDATED Scheme Saved [ID ${foundSSC.id} and ${foundSSC.permitId}]"
            sr.names = "${foundSSC.createdBy}"
            sr.varField1 = "${foundSSC.permitId}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun schemeSendEmail(
        permitUpdate: PermitApplicationsEntity,
        user: UsersEntity,
        reasonValue: String?,
    ) {
        //todo: for now lets work with this i will change it
        val subject = "SCHEME FOR SUPERVISION AND CONTROL (SSC)"
        val messageBody = "Dear ${user.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "Scheme For Supervision And Control was ${reasonValue} " +
//                "due to the Following reason ${foundSSC.acceptedRejectedReason}:" +
                "\n " +
                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitUpdate.id}"

        user.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun justificationReportSendEmail(
        permitUpdate: PermitApplicationsEntity,
        reasonValue: String?,
    ) {
        //todo: for now lets work with this i will change it
        val userPermit = permitUpdate.qaoId?.let { commonDaoServices.findUserByID(it) }
        val subject = "JUSTIFICATION REPORT"
        val messageBody = "Dear ${userPermit?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "Justification report was ${reasonValue} " +
//                "due to the Following reason ${foundSSC.acceptedRejectedReason}:" +
                "\n FOr the following Permit application with Ref Number ${permitUpdate.permitRefNumber}" +
                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitUpdate.id}"

        userPermit?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun schemeUpdatePermit(
        foundSSC: QaSchemeForSupervisionEntity,
        status: Int?
    ): PermitApplicationsEntity {
        val permitUpdate =
            foundSSC.permitId?.let { findPermitBYID(it) } ?: throw ExpectedDataNotFound("Permit ID cannot be null")
        permitUpdate.approvedRejectedScheme = status
        return permitUpdate
    }

    fun sta3NewSave(
        permitID: Long,
        permitNewRefNumber: String,
        qaSta3Details: QaSta3Entity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaSta3Entity {

        var sta3Upadted: QaSta3Entity? = null
        sta3Repo.findByPermitRefNumberAndPermitId(permitNewRefNumber, permitID)?.let {
            sta3Upadted = sta3Update(qaSta3Details, map, user)
        } ?: kotlin.run {
            with(qaSta3Details) {
                permitId = permitID
                permitRefNumber = permitNewRefNumber
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            sta3Upadted = sta3Repo.save(qaSta3Details)
        }
        return sta3Upadted ?: throw ExpectedDataNotFound("STA 3 cannot be null")

    }

    fun sta10NewSave(
        permit: PermitApplicationsEntity,
        qaSta10Details: QaSta10Entity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaSta10Entity {
        val plantAttached = findPlantDetails(permit.attachedPlantId ?: throw ExpectedDataNotFound("MISSING PLANT ID"))
        with(qaSta10Details) {
            totalNumberPersonnel = totalNumberMale?.let { totalNumberFemale?.plus(it) }
            town = plantAttached.town
            county = plantAttached.county
            region = plantAttached.region
            permitId = permit.id
            permitRefNumber = permit.permitRefNumber
            applicationDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return sta10Repo.save(qaSta10Details)
    }

    fun sta10OfficerNewSave(
        qaSta10Details: QaSta10Entity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): QaSta10Entity {

        with(qaSta10Details) {
            officialFillDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return sta10Repo.save(qaSta10Details)
    }

    fun sta10Update(
        qaSta10Details: QaSta10Entity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): QaSta10Entity {

        with(qaSta10Details) {
            status = map.activeStatus
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return sta10Repo.save(qaSta10Details)
    }

    fun sta3Update(
        qaSta3Details: QaSta3Entity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): QaSta3Entity {

        with(qaSta3Details) {
            status = map.activeStatus
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return sta3Repo.save(qaSta3Details)
    }

    fun sta10ManufactureProductNewSave(
        qaSta10ID: Long,
        productManufacturedDetailsList: List<QaProductManufacturedEntity>,
        user: UsersEntity,
        map: ServiceMapsEntity
    ) {

        val sta10Found = findSta10BYID(qaSta10ID)

        productManufacturedDetailsList.forEach { productsManufactured ->
            var productsManufacturedDetsils = productsManufactured
            productsManufactureSTA10Repo.findByIdOrNull(productsManufactured.id ?: -1L)
                ?.let { foundProductsManufactured ->

                    productsManufacturedDetsils = commonDaoServices.updateDetails(
                        productsManufacturedDetsils,
                        foundProductsManufactured
                    ) as QaProductManufacturedEntity

                    with(productsManufacturedDetsils) {
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }

                    productsManufacturedDetsils = productsManufactureSTA10Repo.save(productsManufacturedDetsils)
                }
                ?: kotlin.run {

                    with(productsManufacturedDetsils) {
                        sta10Id = qaSta10ID
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    productsManufacturedDetsils = productsManufactureSTA10Repo.save(productsManufacturedDetsils)

                }
        }
    }

    fun sta10RawMaterialsNewSave(
        qaSta10ID: Long,
        rawMaterialsDetails: List<QaRawMaterialEntity>,
        user: UsersEntity,
        map: ServiceMapsEntity
    ) {


        val sta10Found = findSta10BYID(qaSta10ID)

        rawMaterialsDetails.forEach { rawMaterialsDetails ->
            var rawMaterialsDetails = rawMaterialsDetails
            rawMaterialsSTA10Repo.findByIdOrNull(rawMaterialsDetails.id ?: -1L)
                ?.let { foundRawMaterial ->

                    rawMaterialsDetails =
                        commonDaoServices.updateDetails(rawMaterialsDetails, foundRawMaterial) as QaRawMaterialEntity

                    with(rawMaterialsDetails) {
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }

                    rawMaterialsDetails = rawMaterialsSTA10Repo.save(rawMaterialsDetails)
                }
                ?: kotlin.run {

                    with(rawMaterialsDetails) {
                        sta10Id = qaSta10ID
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    rawMaterialsDetails = rawMaterialsSTA10Repo.save(rawMaterialsDetails)

                }
        }
    }

    fun sta10MachinePlantNewSave(
        qaSta10ID: Long,
        machinePlantsDetails: List<QaMachineryEntity>,
        user: UsersEntity,
        map: ServiceMapsEntity
    ) {

        val sta10Found = findSta10BYID(qaSta10ID)

        machinePlantsDetails.forEach { machinePlantsDetails ->
            var machinePlantsDetails = machinePlantsDetails
            machinePlantsSTA10Repo.findByIdOrNull(machinePlantsDetails.id ?: -1L)
                ?.let { foundMachinePlantsDetails ->

                    machinePlantsDetails = commonDaoServices.updateDetails(
                        machinePlantsDetails,
                        foundMachinePlantsDetails
                    ) as QaMachineryEntity

                    with(machinePlantsDetails) {
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }

                    machinePlantsDetails = machinePlantsSTA10Repo.save(machinePlantsDetails)
                }
                ?: kotlin.run {

                    with(machinePlantsDetails) {
                        sta10Id = qaSta10ID
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    machinePlantsDetails = machinePlantsSTA10Repo.save(machinePlantsDetails)

                }
        }

    }


    fun saveQaFileUploads(
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        map: ServiceMapsEntity,
        qaUploads: QaUploadsEntity,
        permitRefNUMBER: String,
        permitID: Long,
        versionNumberAdded: Long,
        manufactureNonStatus: Int?,
    ): Pair<ServiceRequestsEntity, QaUploadsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var uploads = qaUploads
        try {
            with(uploads) {
                name = commonDaoServices.saveDocuments(docFile)
                fileType = docFile.contentType
                documentType = doc
                document = docFile.bytes
                permitId = permitID
                permitRefNumber = permitRefNUMBER
                transactionDate = commonDaoServices.getCurrentDate()
                versionNumber = versionNumberAdded
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }

            uploads = qaUploadsRepo.save(uploads)

            sr.payload = "DOC File Added [${uploads.name} and ${uploads.permitRefNumber}]"
            sr.names = "${user.userName}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, uploads)
    }

    fun uploadQaFile(
        uploads: QaUploadsEntity,
        docFile: MultipartFile,
        doc: String,
        permitRefNUMBER: String,
        user: UsersEntity
    ): QaUploadsEntity {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(docFile.name)
            documentType = doc
            document = docFile.bytes
            permitRefNumber = permitRefNUMBER
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        return qaUploadsRepo.save(uploads)
    }

    fun sta10ManufacturingProcessNewSave(
        qaSta10ID: Long,
        manufacturingProcessDetails: List<QaManufacturingProcessEntity>,
        user: UsersEntity,
        map: ServiceMapsEntity
    ) {

        val sta10Found = findSta10BYID(qaSta10ID)

        manufacturingProcessDetails.forEach { manufacturingProcessDetails ->
            var manufacturingProcessDetails = manufacturingProcessDetails
            manufacturingProcessSTA10Repo.findByIdOrNull(manufacturingProcessDetails.id ?: -1L)
                ?.let { foundManufacturingProcessDetails ->

                    manufacturingProcessDetails = commonDaoServices.updateDetails(
                        manufacturingProcessDetails,
                        foundManufacturingProcessDetails
                    ) as QaManufacturingProcessEntity

                    with(manufacturingProcessDetails) {
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }

                    manufacturingProcessDetails = manufacturingProcessSTA10Repo.save(manufacturingProcessDetails)
                }
                ?: kotlin.run {

                    with(manufacturingProcessDetails) {
                        sta10Id = qaSta10ID
                        status = map.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    manufacturingProcessDetails = manufacturingProcessSTA10Repo.save(manufacturingProcessDetails)

                }
        }
    }


    fun addPlantDetailsManufacture(
        manufacturePlant: ManufacturePlantDetailsEntity,
        s: ServiceMapsEntity,
        loggedInUser: UsersEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {
            val companyProfile = commonDaoServices.findCompanyProfile(
                loggedInUser.id ?: throw ExpectedDataNotFound("MISSING USER ID FOR LOGGED IN USER")
            )
            var plantDetails = manufacturePlant
            with(plantDetails) {
                companyProfileId = companyProfile.id
                userId = loggedInUser.id
                region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, s.activeStatus).regionId }
                status = s.activeStatus
                createdOn = commonDaoServices.getTimestamp()
                createdBy = commonDaoServices.concatenateName(loggedInUser)
            }

            plantDetails = manufacturePlantRepository.save(plantDetails)

            sr.payload = "Plant Details [plantDetails ID= ${plantDetails.id}]"
            sr.names = "${plantDetails.buildingName}} ${plantDetails.userId}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }


    fun ssfSavePDFSelectedDetails(
        fileContent: File,
        ssfID: Long,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        val ssfDetails = findSampleSubmittedBYID(ssfID)
        try {

            var upload = QaUploadsEntity()
            with(upload) {
                permitId = ssfDetails.permitId
                ssfUploads = 1
                ordinaryStatus = 0
                versionNumber = 1
            }
            upload = uploadQaFile(
                upload,
                commonDaoServices.convertFileToMultipartFile(fileContent),
                "LAB RESULTS PDF",
                ssfDetails.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER"),
                user
            )

            val ssfPdfDetails = QaSampleSubmittedPdfListDetailsEntity()
            with(ssfPdfDetails) {
                sffId = ssfDetails.id
                pdfName = fileContent.name
                pdfSavedId = upload.id
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            SampleSubmissionSavedPdfListRepo.save(ssfPdfDetails)

            sr.payload = "SSF Updated [updatePermit= ${ssfDetails.id}]"
            sr.names = "PERMIT REF NO = ${ssfDetails.permitRefNumber}}  USER ID = ${user.id}"
            sr.varField1 = "${ssfDetails.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, ssfDetails)
    }

    fun permitUpdateDetails(
        permits: PermitApplicationsEntity,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var updatePermit = permits
        try {
            with(updatePermit) {
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
                if (versionNumber == null) {
                    versionNumber = 1
                    varField7 = 5.toString()
                }

            }
            updatePermit = permitRepo.save(permits)

            sr.payload = "Permit Updated [updatePermit= ${updatePermit.id}]"
            sr.names = "${updatePermit.permitRefNumber}} ${updatePermit.userId}"
            sr.varField1 = "${updatePermit.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, updatePermit)
    }

    fun permitAddRemarksDetails(
        permitID: Long,
        remarksDesc: String?,
        remarksStat: Int?,
        prBy: String,
        prName: String,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            var remarksDetails = QaRemarksEntity().apply {
                permitId = permitID
                remarksDescription = remarksDesc
                remarksStatus = remarksStat
                processBy = prBy
                processName = prName
                userId = user.id
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            remarksDetails = remarksEntityRepo.save(remarksDetails)

            sr.payload = "Remarks added [Permit ID= ${permitID}]"
            sr.names = "${remarksDetails.createdBy}} ${remarksDetails.processName}"
            sr.varField1 = "${permitID}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun permitResubmitDetails(
        permits: PermitApplicationsEntity,
        permitResubmit: ResubmitApplicationDto,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var updatePermit = permits
        try {
            KotlinLogging.logger { }.info(":::::: RESUBMIT SENT IS = ${permitResubmit.resubmittedDetails} :::::::")

            with(updatePermit) {
                when (permitResubmit.resubmittedDetails) {
                    "resubmitLabNonComplianceResults" -> {
                        resubmitApplicationStatus = 10
                        compliantStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
//                        compliantStatus = null
//                        compliantRemarks = null
                        testReportId = null
                        userTaskId = applicationMapProperties.mapUserTaskNameQAO
                        permitStatus = applicationMapProperties.mapQaStatusPendingReInspection

                        KotlinLogging.logger { }
                            .info(":::::: SELECTED RESUBMIT IS resubmitLabNonComplianceResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "MANUFACTURE",
                            "RESUBMIT APPLICATION FOR RE-SAMPLING",
                            s,
                            user
                        )
                    }

                    "resubmitPCMReviewCompletenessResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
//                        compliantStatus = null
//                        compliantRemarks = null
                        testReportId = null
                        userTaskId = applicationMapProperties.mapUserTaskNamePCM
                        permitStatus = applicationMapProperties.mapQaStatusPPCMReview

                        KotlinLogging.logger { }
                            .info(":::::: SELECTED RESUBMIT IS resubmitPCMReviewCompletenessResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "MANUFACTURE",
                            "RESUBMIT APPLICATION FOR REVIEW",
                            s,
                            user
                        )
                    }

                    "resubmitHofQamCompletenessResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
//                        hofQamCompletenessStatus = null
//                        hofQamCompletenessRemarks = null
                        permitStatus = applicationMapProperties.mapQaStatusPApprovalCompletness
                        userTaskId = when (updatePermit.permitType) {
                            applicationMapProperties.mapQAPermitTypeIDDmark -> {
                                applicationMapProperties.mapUserTaskNameHOD
                            }

                            else -> {
                                applicationMapProperties.mapUserTaskNameQAM
                            }
                        }
                        KotlinLogging.logger { }
                            .info(":::::: SELECTED RESUBMIT IS resubmitHofQamCompletenessResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "MANUFACTURE",
                            "RESUBMIT APPLICATION FOR RE-REVIEW COMPLETNESS",
                            s,
                            user
                        )
                    }

                    "resubmitHofQamPSCRejectionResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
//                        hofQamCompletenessStatus = null
//                        hofQamCompletenessRemarks = null
                        permitStatus = applicationMapProperties.mapQaStatusResubmitted
                        userTaskId = applicationMapProperties.mapUserTaskNameQAM
                        KotlinLogging.logger { }
                            .info(":::::: SELECTED RESUBMIT IS resubmitHofQamPSCRejectionResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "QAO",
                            "RESUBMIT APPLICATION, DEFERRED BY PSC",
                            s,
                            user
                        )
                    }

                    "resubmitHofQamRejectionResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
//                        hofQamCompletenessStatus = null
//                        hofQamCompletenessRemarks = null
                        permitStatus = applicationMapProperties.mapQaStatusResubmitted
                        userTaskId = applicationMapProperties.mapUserTaskNameQAM
                        KotlinLogging.logger { }
                            .info(":::::: SELECTED RESUBMIT IS resubmitHofQamRejectionResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "QAO",
                            "RESUBMIT APPLICATION, REJECTED BY QAM",
                            s,
                            user
                        )
                    }

                    "resubmitHodRmPACRejectionResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
//                        hofQamCompletenessStatus = null
//                        hofQamCompletenessRemarks = null
                        permitStatus = applicationMapProperties.mapQaStatusResubmitted
                        userTaskId = applicationMapProperties.mapUserTaskNameHOD
                        KotlinLogging.logger { }
                            .info(":::::: SELECTED RESUBMIT IS resubmitHodRmPACRejectionResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "QAO/ASSESSOR",
                            "RESUBMIT APPLICATION, DEFERRED BY PAC",
                            s,
                            user
                        )
                    }

                    "resubmitHodRmPCMRejectionResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
//                        hofQamCompletenessStatus = null
//                        hofQamCompletenessRemarks = null
                        permitStatus = applicationMapProperties.mapQaStatusResubmitted
                        userTaskId = applicationMapProperties.mapUserTaskNameHOD
                        KotlinLogging.logger { }
                            .info(":::::: SELECTED RESUBMIT IS resubmitHodRmPCMRejectionResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "QAO/ASSESSOR",
                            "RESUBMIT APPLICATION, DEFERRED BY PCM",
                            s,
                            user
                        )
                    }

                    "resubmitHofQamPCMRejectionResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
//                        hofQamCompletenessStatus = null
//                        hofQamCompletenessRemarks = null
                        permitStatus = applicationMapProperties.mapQaStatusResubmitted
                        userTaskId = applicationMapProperties.mapUserTaskNameQAM
                        KotlinLogging.logger { }
                            .info(":::::: SELECTED RESUBMIT IS resubmitHofQamPCMRejectionResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "QAO",
                            "RESUBMIT APPLICATION, DEFERRED BY PCM",
                            s,
                            user
                        )
                    }

                    "resubmitHofQamToPSCResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
                        pscMemberApprovalStatus = 10
                        permitStatus = applicationMapProperties.mapQaStatusPPSCMembersAward
                        userTaskId = applicationMapProperties.mapUserTaskNamePSC
                        KotlinLogging.logger { }.info(":::::: SELECTED RESUBMIT IS resubmitHofQamToPSCResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "HOF/QAM",
                            "RESUBMIT APPLICATION, DEFERRED BY PSC",
                            s,
                            user
                        )
                    }

                    "resubmitHodRmToPACResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
                        pacDecisionStatus = 10
                        permitStatus = applicationMapProperties.mapQaStatusPPACSecretaryAwarding
                        userTaskId = applicationMapProperties.mapUserTaskNamePACSECRETARY
                        KotlinLogging.logger { }.info(":::::: SELECTED RESUBMIT IS resubmitHofQamToPSCResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "HOD/RM",
                            "RESUBMIT APPLICATION, DEFERRED BY PAC",
                            s,
                            user
                        )
                    }

                    "resubmitHofQamToPCMResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
                        pcmApprovalStatus = 10
                        pscMemberApprovalStatus = 20
                        permitStatus = applicationMapProperties.mapQaStatusPPSCMembersAward
                        userTaskId = applicationMapProperties.mapUserTaskNamePSC
                        KotlinLogging.logger { }.info(":::::: SELECTED RESUBMIT IS resubmitHofQamToPSCResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "HOF/QAM",
                            "RESUBMIT APPLICATION, DEFERRED BY PCM",
                            s,
                            user
                        )
                    }

                    "resubmitHodRmToPCMResults" -> {
                        resubmitApplicationStatus = 10
                        resubmitRemarks = permitResubmit.resubmitRemarks
                        pcmApprovalStatus = 10
                        pacDecisionStatus = 20
                        permitStatus = applicationMapProperties.mapQaStatusPPACSecretaryAwarding
                        userTaskId = applicationMapProperties.mapUserTaskNamePACSECRETARY
                        KotlinLogging.logger { }.info(":::::: SELECTED RESUBMIT IS resubmitHodRmToPCMResults :::::::")
                        permitAddRemarksDetails(
                            updatePermit.id ?: throw Exception("ID NOT FOUND"),
                            permitResubmit.resubmitRemarks,
                            null,
                            "HOD/RM",
                            "RESUBMIT APPLICATION, DEFERRED BY PCM",
                            s,
                            user
                        )
                    }

                    else -> {
                        throw Exception("NO FUNCTION FOR RESUBMIT EXISTING (${permitResubmit.resubmittedDetails})")
                    }
                }
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            updatePermit = permitRepo.save(permits)

            sr.payload = "Permit Updated [updatePermit= ${updatePermit.id}]"
            sr.names = "${updatePermit.permitRefNumber}} ${updatePermit.userId}"
            sr.varField1 = "${updatePermit.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, updatePermit)
    }
//Todo: check why method does not create new version

    fun permitRejectedVersionCreation(
        permitID: Long,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var createNewVersionPermit: PermitApplicationsEntity? = null
        try {
            val pm = findPermitBYID(permitID)
            var oldPermit =
                findPermitWithPermitRefNumberLatest(pm.permitRefNumber ?: throw Exception("INVALID PERMIT NUMBER"))
            KotlinLogging.logger { }
                .info { "::::::::::::::::::PERMIT With PERMIT NUMBER = ${pm.permitRefNumber}, DOES Exists::::::::::::::::::::: " }
            val versionNumberOld =
                oldPermit.versionNumber ?: throw ExpectedDataNotFound("Permit Version Number is Empty")

            oldPermit.oldPermitStatus = 1
            //update last previous version permit old status
            oldPermit = permitUpdateDetails(oldPermit, s, user).second

            createNewVersionPermit = SerializationUtils.clone(oldPermit)

            with(createNewVersionPermit) {
                id = null
                oldPermitStatus = null
                versionNumber = versionNumberOld.plus(1)
            }

            createNewVersionPermit = permitRepo.save(createNewVersionPermit)

            sr.payload = "Permit Renewed Updated [updatePermit= ${createNewVersionPermit.id}]"
            sr.names = "${createNewVersionPermit.permitRefNumber}} ${createNewVersionPermit.userId}"
            sr.varField1 = "${createNewVersionPermit.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, createNewVersionPermit ?: throw ExpectedDataNotFound("MISSING SAVED PERMIT"))
    }


    fun permitRejectedVersionCreateTiedFiles(
        permitID: Long,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var createNewVersionPermit: PermitApplicationsEntity? = null
        try {
            val pm = findPermitBYID(permitID)
            var oldPermit =
                findPermitWithPermitRefNumberLatest(pm.permitRefNumber ?: throw Exception("INVALID PERMIT NUMBER"))
            KotlinLogging.logger { }
                .info { "::::::::::::::::::PERMIT With PERMIT NUMBER = ${pm.permitRefNumber}, DOES Exists::::::::::::::::::::: " }
            val versionNumberOld =
                oldPermit.versionNumber ?: throw ExpectedDataNotFound("Permit Version Number is Empty")

            oldPermit.oldPermitStatus = 1
            //update last previous version permit old status
            oldPermit = permitUpdateDetails(oldPermit, s, user).second

            createNewVersionPermit = SerializationUtils.clone(oldPermit)

            with(createNewVersionPermit) {
                id = null
                oldPermitStatus = null
                versionNumber = versionNumberOld.plus(1)
            }

            createNewVersionPermit = permitRepo.save(createNewVersionPermit)

            sr.payload = "Permit Renewed Updated [updatePermit= ${createNewVersionPermit.id}]"
            sr.names = "${createNewVersionPermit.permitRefNumber}} ${createNewVersionPermit.userId}"
            sr.varField1 = "${createNewVersionPermit.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, createNewVersionPermit ?: throw ExpectedDataNotFound("MISSING SAVED PERMIT"))
    }

    fun permitUpdateNewWithSamePermitNumber(
        permitID: Long,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var saveNewPermit = PermitApplicationsEntity()
        try {
            val pm = findPermitBYID(permitID)
            var oldPermit =
                findPermitWithPermitRefNumberLatest(pm.permitRefNumber ?: throw Exception("INVALID PERMIT NUMBER"))
            KotlinLogging.logger { }
                .info { "::::::::::::::::::PERMIT With PERMIT NUMBER = ${pm.permitRefNumber}, DOES Exists::::::::::::::::::::: " }
            val versionNumberOld =
                oldPermit.versionNumber ?: throw ExpectedDataNotFound("Permit Version Number is Empty")

            oldPermit.oldPermitStatus = 1
            oldPermit.varField7 = null
//            oldPermit.renewalStatus = s.activeStatus
            //update last previous version permit old status
            oldPermit = permitUpdateDetails(oldPermit, s, user).second

            val permitTypeDetails = findPermitType(oldPermit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))

            with(saveNewPermit) {
                renewalStatus = s.activeStatus
                userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                userId = user.id
                fmarkGenerated = s.inactiveStatus
                companyId = oldPermit.companyId
                attachedPlantId = oldPermit.attachedPlantId
                permitType = oldPermit.permitType
                permitRefNumber = oldPermit.permitRefNumber
                enabled = s.initStatus
                versionNumber = versionNumberOld.plus(1)
                endOfProductionStatus = s.inactiveStatus
                commodityDescription = oldPermit.commodityDescription
                firmName = oldPermit.firmName
                postalAddress = oldPermit.postalAddress
                telephoneNo = oldPermit.telephoneNo
                email = oldPermit.email
                physicalAddress = oldPermit.physicalAddress
                faxNo = oldPermit.faxNo
                plotNo = oldPermit.plotNo
                designation = oldPermit.designation
                tradeMark = oldPermit.tradeMark
                divisionId = oldPermit.divisionId
                sectionId = oldPermit.sectionId
                standardCategory = oldPermit.standardCategory
                broadProductCategory = oldPermit.broadProductCategory
                productCategory = oldPermit.productCategory
                product = oldPermit.product
                productSubCategory = oldPermit.productSubCategory
                permitForeignStatus = oldPermit.permitForeignStatus
                awardedPermitNumber = oldPermit.awardedPermitNumber
                permitStatus = applicationMapProperties.mapQaStatusPermitRenewalDraft
                status = s.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
                applicantName = commonDaoServices.concatenateName(user)
            }
            saveNewPermit = permitRepo.save(saveNewPermit)

            when (oldPermit.permitType) {
                applicationMapProperties.mapQAPermitTypeIdSmark -> {
                    val oldSta10 = findSTA10WithPermitRefNumberANdPermitID(
                        oldPermit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                        oldPermit.id ?: throw Exception("INVALID PERMIT ID")
                    )
                    var newSta10 = QaSta10Entity()
                    newSta10 = commonDaoServices.updateDetails(oldSta10, newSta10) as QaSta10Entity
                    newSta10.id = null
                    sta10NewSave(saveNewPermit, newSta10, user, s)

                    regenerateSameDetailsForClonedSTA10(newSta10, oldSta10, oldPermit, saveNewPermit)

                }

                applicationMapProperties.mapQAPermitTypeIDDmark -> {
                    val sta3 = findSTA3WithPermitIDAndRefNumber(
                        oldPermit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                        oldPermit.id ?: throw Exception("INVALID PERMIT ID")
                    )
                    var newSta3 = QaSta3Entity()
                    newSta3 = commonDaoServices.updateDetails(sta3, newSta3) as QaSta3Entity
                    newSta3.id = null
                    sta3NewSave(
                        saveNewPermit.id ?: throw Exception("INVALID PERMIT ID"),
                        saveNewPermit.permitRefNumber ?: throw Exception("INVALID PERMIT ID"),
                        newSta3,
                        user,
                        s
                    )

                    val sta3FileList = findAllUploadedFileBYPermitIDAndSta3Status(
                        oldPermit.id ?: throw Exception("MISSING PERMIT ID"),
                        1
                    )
                    sta3FileList.forEach { fileList ->
                        val newFileList = SerializationUtils.clone(fileList)
                        with(newFileList) {
                            id = null
                            permitId = saveNewPermit.id
                        }
                        qaUploadsRepo.save(newFileList)
                    }
                }
            }

            sr.payload = "Permit Renewed Updated [updatePermit= ${saveNewPermit.id}]"
            sr.names = "${saveNewPermit.permitRefNumber}} ${saveNewPermit.userId}"
            sr.varField1 = "${saveNewPermit.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, saveNewPermit)
    }

    fun regenerateSameDetailsForClonedSTA10(
        newSta10: QaSta10Entity,
        oldSta10: QaSta10Entity,
        oldPermit: PermitApplicationsEntity,
        newPermit: PermitApplicationsEntity
    ) {
        //Find all STA 10 related Tables
        val qaSta10ID = newSta10.id ?: throw ExpectedDataNotFound("MISSING STA 10 ID")
        val qaOldSta10ID = oldSta10.id ?: throw ExpectedDataNotFound("MISSING STA 10 ID")
        val sta10Personnel = findPersonnelWithSTA10ID(qaOldSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")
        sta10Personnel.forEach { personnel ->
            val newPersonnel = SerializationUtils.clone(personnel)
            with(newPersonnel) {
                id = null
                sta10Id = qaSta10ID
            }
            qaPersonnelInchargeRepo.save(newPersonnel)

        }


        val sta10Products =
            findProductsManufactureWithSTA10ID(qaOldSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")
        sta10Products.forEach { products ->
            val newProducts = SerializationUtils.clone(products)
            with(newProducts) {
                id = null
                sta10Id = qaSta10ID
            }
            productsManufactureSTA10Repo.save(newProducts)

        }

        val sta10Raw = findRawMaterialsWithSTA10ID(qaOldSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")
        sta10Raw.forEach { raw ->
            val newRaw = SerializationUtils.clone(raw)
            with(newRaw) {
                id = null
                sta10Id = qaSta10ID
            }
            rawMaterialsSTA10Repo.save(newRaw)

        }

        val sta10MachinePlant =
            findMachinePlantsWithSTA10ID(qaOldSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")
        sta10MachinePlant.forEach { machinaryPlant ->
            val newMachinaryPlant = SerializationUtils.clone(machinaryPlant)
            with(newMachinaryPlant) {
                id = null
                sta10Id = qaSta10ID
            }
            machinePlantsSTA10Repo.save(newMachinaryPlant)

        }
        val sta10ManufacturingProcess =
            findManufacturingProcessesWithSTA10ID(qaOldSta10ID) ?: throw ExpectedDataNotFound("EMPTY RESULTS")
        sta10ManufacturingProcess.forEach { manufacturingProcess ->
            val newManufacturingProcess = SerializationUtils.clone(manufacturingProcess)
            with(newManufacturingProcess) {
                id = null
                sta10Id = qaSta10ID
            }
            manufacturingProcessSTA10Repo.save(newManufacturingProcess)

        }
        val sta10FileList =
            findAllUploadedFileBYPermitIDAndSta10Status(oldPermit.id ?: throw Exception("MISSING PERMIT ID"), 1)
        sta10FileList.forEach { fileList ->
            val newFileList = SerializationUtils.clone(fileList)
            with(newFileList) {
                id = null
                permitId = newPermit.id
            }
            qaUploadsRepo.save(newFileList)
        }
    }

    fun getFileInvoicePDFForm(batchID: Long): File {
        val myDetails = reportsControllers.createInvoicePdf(batchID)
        reportsDaoService.generateEmailPDFReportWithDataSource(
            "Proforma-Invoice-${myDetails.first.getValue("demandNoteNo")}.pdf",
            myDetails.first,
            applicationMapProperties.mapReportProfomaInvoiceWithItemsPath,
            myDetails.second
        ).let {
            return it
        }
    }

    fun getFileCertificateIssuedPDFForm(permitID: Long): File {
        val myDetails = reportsControllers.permitCertificateIssuedCreation(permitID)
        reportsDaoService.generateEmailPDFReportWithNoDataSource(
            "Permit-Certificate-${myDetails.first.getValue("PermitNo")}.pdf",
            myDetails.first,
            myDetails.second
        )?.let {
            return it
        } ?: throw ExpectedDataNotFound("MISSING FILE")
    }

    fun invoiceCreationPDF(
        batchID: Long,
        loggedInUser: UsersEntity
    ): File {

        val myDetails = reportsControllers.createInvoicePdf(batchID)

        val invoicePDFCreated = reportsDaoService.generateEmailPDFReportWithDataSource(
            "Proforma-Invoice-${myDetails.first.getValue("demandNoteNo")}.pdf",
            myDetails.first,
            applicationMapProperties.mapReportProfomaInvoiceWithItemsPath,
            myDetails.second
        )

        return invoicePDFCreated
    }

    fun sendEmailWithProforma(recipient: String, attachment: String?, permitRefNumber: String): Boolean {
        val subject = "PRO FORMA INVOICE"
        val messageBody = "Check The attached Proforma Invoices for permit with Ref number $permitRefNumber"

        notifications.processEmail(recipient, subject, messageBody, attachment)

        return true
    }


    fun sendEmailWithLabResults(recipient: String, attachment: String?, permitRefNumber: String): Boolean {
        val subject = "LAB RESULTS"
        val messageBody = "Check The attached Lab Results for permit with Ref number $permitRefNumber"

        notifications.processEmail(recipient, subject, messageBody, attachment)

        return true
    }

    fun sendEmailWithLabResultsFound(recipient: String, permitRefNumber: String): Boolean {
        val subject = "LAB RESULTS"
        val messageBody = "Check Lab Results for permit with Ref number $permitRefNumber"

        notifications.sendEmail(recipient, subject, messageBody)

        return true
    }

    fun approvedRejectedSSC(
        sscApprovalRejectionDto: SSCApprovalRejectionDto,
        map: ServiceMapsEntity,
        permitDetails: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ) {
        var reasonValue: String? = null
        val userDetails = commonDaoServices.findUserByID(permitDetails.qaoId ?: throw Exception("MISSING QAO ID"))
        when (sscApprovalRejectionDto.approvedRejectedScheme) {
            map.activeStatus -> {
                reasonValue = "ACCEPTED"
                schemeSendEmail(permitDetails, userDetails, reasonValue)
                permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPSSF,
                    loggedInUser
                )
            }

            map.inactiveStatus -> {
                reasonValue = "REJECTED"
                with(permitDetails) {
                    approvedRejectedScheme = null
                    generateSchemeStatus = null
                }
                permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusSSCRejected,
                    loggedInUser
                )
                schemeSendEmail(permitDetails, userDetails, reasonValue)
            }
        }
    }

    fun sendEmailWithSSC(recipient: String, permitRefNumber: String): Boolean {
        val subject = "SSC GENERATED"
        val messageBody = "SCHEME OF SUPERVISION AND CONTROL for permit with Ref number $permitRefNumber, Has been Sent"

        notifications.processEmail(recipient, subject, messageBody, null)

        return true
    }

    fun sendEmailWithSSCAttached(recipient: String, attachment: String?, permitRefNumber: String): Boolean {
        val subject = "SSC ATTACHED GENERATED"
        val messageBody = "BODY"

        notifications.processEmail(recipient, subject, messageBody, attachment)

        return true
    }

    fun sendEmailWithTaskDetails(recipient: String, permitRefNumber: String): Boolean {
        val subject = "ALLOCATED TASK"
        val messageBody = "Permit with Ref number $permitRefNumber  awaits your review/action"

        notifications.processEmail(recipient, subject, messageBody, null)

        return true
    }

    fun sendEmailWithProformaPaid(recipient: String, attachment: String?, permitRefNumber: String): Boolean {
        val subject = "PRO FORMA INVOICE"
        val messageBody =
            "Check The attached Proforma Invoices  Payment made for permit with Ref number $permitRefNumber"

        notifications.processEmail(recipient, subject, messageBody, attachment)

        return true
    }


    fun invoiceUpdateDetails(invoice: InvoiceEntity, user: UsersEntity): InvoiceEntity {

        with(invoice) {
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceRepository.save(invoice)
    }

    fun qaInvoiceBatchUpdateDetails(invoice: QaBatchInvoiceEntity, user: UsersEntity): QaBatchInvoiceEntity {

        with(invoice) {
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceQaBatchRepo.save(invoice)
    }


    fun pcmGenerateInvoice(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permit: PermitApplicationsEntity,
        invoiceDetails: QaInvoiceDetailsEntity?,
        permitTypeID: Long
    ) {
        val permitType = findPermitType(permitTypeID)
        val permitUser =
            commonDaoServices.findUserByID(permit.userId ?: throw ExpectedDataNotFound("Permit USER Id Not found"))
        permitInvoiceCalculation(s, permitUser, permit, invoiceDetails)
        with(permit) {
            sendApplication = s.activeStatus
            invoiceGenerated = s.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPPayment
        }
        permitUpdateDetails(permit, s, user)
    }

    fun permitAddNewInspectionReportDetailsTechnical(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permitID: Long,
        inspectionTechnical: QaInspectionTechnicalEntity,
    ): Pair<ServiceRequestsEntity, QaInspectionTechnicalEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        val permitFound = findPermitBYID(permitID)
        var inspectionTechnicalDetails = inspectionTechnical
        try {

            qaInspectionTechnicalRepo.findByIdOrNull(inspectionTechnical.id ?: -1L)
                ?.let { iTDetails ->

                    inspectionTechnicalDetails =
                        commonDaoServices.updateDetails(inspectionTechnical, iTDetails) as QaInspectionTechnicalEntity

                    with(inspectionTechnicalDetails) {
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    inspectionTechnicalDetails = qaInspectionTechnicalRepo.save(inspectionTechnicalDetails)
                }
                ?: kotlin.run {
                    var qaInspectionReportRecommendation = QaInspectionReportRecommendationEntity()
                    with(qaInspectionReportRecommendation) {
                        refNo = "REF${
                            generateRandomText(
                                5,
                                s.secureRandom,
                                s.messageDigestAlgorithm,
                                true
                            )
                        }".toUpperCase()
                        permitId = permitFound.id
                        permitRefNumber = permitFound.permitRefNumber
                        filledQpsmsStatus = s.activeStatus
                        status = s.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    qaInspectionReportRecommendation =
                        qaInspectionReportRecommendationRepo.save(qaInspectionReportRecommendation)


                    with(inspectionTechnicalDetails) {
                        inspectionRecommendationId = qaInspectionReportRecommendation.id
                        permitId = permitFound.id
                        permitRefNumber = permitFound.permitRefNumber
                        status = s.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    inspectionTechnicalDetails = qaInspectionTechnicalRepo.save(inspectionTechnicalDetails)


                }

            permitFound.inspectionReportGenerated = s.activeStatus
            permitUpdateDetails(permitFound, s, user)




            sr.payload = "GENERATED INSPECTION REPORT ID [id= ${inspectionTechnicalDetails.id}]"
//            sr.names = "${qaInspectionTechnical.} ${permitInvoiceFound.amount}"
            sr.varField1 = inspectionTechnicalDetails.permitId.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, inspectionTechnicalDetails)
    }

    fun permitAddNewInspectionReportDetailsHaccp(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permitID: Long,
        haccpImplementation: QaInspectionHaccpImplementationEntity,
        qaInspectionReportRecommendation: QaInspectionReportRecommendationEntity,
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val permitFound = findPermitBYID(permitID)
            var haccpAddedDetails = haccpImplementation

            qaInspectionHaccpImplementationRepo.findByIdOrNull(haccpImplementation.id ?: -1L)
                ?.let { haccpImplementationDetails ->

                    haccpAddedDetails = commonDaoServices.updateDetails(
                        haccpImplementation,
                        haccpImplementationDetails
                    ) as QaInspectionHaccpImplementationEntity

                    with(haccpAddedDetails) {
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    haccpAddedDetails = qaInspectionHaccpImplementationRepo.save(haccpAddedDetails)
                }
                ?: kotlin.run {

                    with(haccpAddedDetails) {
                        inspectionRecommendationId = qaInspectionReportRecommendation.id
                        permitId = permitFound.id
                        permitRefNumber = permitFound.permitRefNumber
                        status = s.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    haccpAddedDetails = qaInspectionHaccpImplementationRepo.save(haccpAddedDetails)

//                    var qaInspectionReportRecommendation = findQaInspectionReportRecommendationBYPermitRefNumber(
//                        permitFound.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER FOUND")
//                    )
                    with(qaInspectionReportRecommendation) {
                        filledHaccpImplementationStatus = s.activeStatus
                    }
                    inspectionRecommendationUpdate(qaInspectionReportRecommendation, s, user)
                }


            sr.payload = "GENERATED INSPECTION REPORT HACCP IMPLEMENTATION  [id= ${haccpAddedDetails.id}]"
            sr.varField1 = haccpAddedDetails.permitId.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }


    fun consolidateInvoiceAndSendMail(
        permitID: Long,
        branchID: Long,
        map: ServiceMapsEntity,
        loggedInUser: UsersEntity
    ): Pair<QaBatchInvoiceEntity, PermitApplicationsEntity> {
        //Consolidate invoice now first
        var permit = findPermitBYID(permitID)
        val attachedPermitPlantDetails =findPlantDetails(branchID)
        val permitType = findPermitType(permit.permitType ?: throw ExpectedDataNotFound("Missing Permit Type ID"))


        val newBatchInvoiceDto = NewBatchInvoiceDto()
        with(newBatchInvoiceDto) {
            permitInvoicesID = arrayOf(permit.id ?: throw ExpectedDataNotFound("MISSING PERMIT ID"))
        }
        var batchInvoice = permitMultipleInvoiceCalculation(map, loggedInUser, newBatchInvoiceDto).second

        // submit invoice to get way
        with(newBatchInvoiceDto) {
            batchID = batchInvoice.first.id!!
        }

        val batchInvoiceDetails = permitMultipleInvoiceSubmitInvoice( map, loggedInUser, newBatchInvoiceDto, batchInvoice.second).second

        //Update Permit Details
        with(permit) {
            sendApplication = map.activeStatus
            invoiceGenerated = map.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPPayment
        }
        permit = permitUpdateDetails(permit, map, loggedInUser).second

        //Send email with attached Invoice details
//        sendEmailWithProforma(
//            commonDaoServices.findUserByID(permit.userId ?: throw ExpectedDataNotFound("MISSING USER ID")).email
//                ?: throw ExpectedDataNotFound("MISSING USER ID"),
//            invoiceCreationPDF(
//                batchInvoice.id ?: throw ExpectedDataNotFound("MISSING BATCH INVOICE ID"),
//                loggedInUser
//            ).path,
//            permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
//        )

        return Pair(batchInvoiceDetails, permit)
    }

    fun permitAddNewInspectionReportDetailsOPC(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permitID: Long,
        opc: QaInspectionOpcEntity,
        qaInspectionReportRecommendation: QaInspectionReportRecommendationEntity,
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val permitFound = findPermitBYID(permitID)
            var opcAddedDetails = opc

            qaInspectionOPCRepo.findByIdOrNull(opc.id ?: -1L)
                ?.let { opcDetails ->

                    opcAddedDetails = commonDaoServices.updateDetails(opc, opcDetails) as QaInspectionOpcEntity

                    with(opcAddedDetails) {
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    opcAddedDetails = qaInspectionOPCRepo.save(opcAddedDetails)
                }
                ?: kotlin.run {

                    with(opcAddedDetails) {
                        inspectionRecommendationId = qaInspectionReportRecommendation.id
                        permitId = permitFound.id
                        permitRefNumber = permitFound.permitRefNumber
                        status = s.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    opcAddedDetails = qaInspectionOPCRepo.save(opcAddedDetails)

//                    var qaInspectionReportRecommendation = findQaInspectionReportRecommendationBYPermitRefNumber(
//                        permitFound.permitRefNumber ?: throw Exception("INVALID PERMIT ID FOUND")
//                    )
                    with(qaInspectionReportRecommendation) {
                        filledOpcStatus = s.activeStatus
                    }
                    inspectionRecommendationUpdate(qaInspectionReportRecommendation, s, user)
                }


            sr.payload = "GENERATED INSPECTION REPORT OCP [id= ${opcAddedDetails.id}]"
            sr.varField1 = opcAddedDetails.permitId.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun permitUpdateNewInspectionReportDetailsOPC(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permitID: Long,
        opc: QaInspectionOpcEntity,
        inspectionReportID: String,
        qaInspectionReportRecommendation: QaInspectionReportRecommendationEntity,
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val permitFound = findPermitBYID(permitID)
            var opcAddedDetails = opc

            println("$%#$$$$$$$4$inspectionReportID")

            qaInspectionOPCRepo.findByIdOrNull(inspectionReportID.toLong())
                ?.let { opcDetails ->

                    opcAddedDetails = commonDaoServices.updateDetails(opc, opcDetails) as QaInspectionOpcEntity

                    with(opcAddedDetails) {
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    opcAddedDetails = qaInspectionOPCRepo.save(opcAddedDetails)
                }



            sr.payload = "UPDATED INSPECTION REPORT OCP [id= ${opcAddedDetails.id}]"
            sr.varField1 = opcAddedDetails.permitId.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }


    fun sta10PersonnelDetailsDetails(
        s: ServiceMapsEntity,
        user: UsersEntity,
        sta10ID: Long,
        personnelList: List<QaPersonnelInchargeEntity>,
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val sta10Found = findSta10BYID(sta10ID)
            personnelList.forEach { personnel ->
                var personnelDetails = personnel
                qaPersonnelInchargeRepo.findByIdOrNull(personnel.id ?: -1L)
                    ?.let { foundPersonnelDetails ->

                        personnelDetails = commonDaoServices.updateDetails(
                            personnel,
                            foundPersonnelDetails
                        ) as QaPersonnelInchargeEntity

                        with(personnelDetails) {
                            modifiedBy = commonDaoServices.concatenateName(user)
                            modifiedOn = commonDaoServices.getTimestamp()
                        }
                        personnelDetails = qaPersonnelInchargeRepo.save(personnelDetails)
                    }
                    ?: kotlin.run {

                        with(personnelDetails) {
                            sta10Id = sta10Found.id
                            status = s.activeStatus
                            createdBy = commonDaoServices.concatenateName(user)
                            createdOn = commonDaoServices.getTimestamp()
                        }
                        personnelDetails = qaPersonnelInchargeRepo.save(personnelDetails)

                    }

                sr.payload = "GENERATED PERSONNEL DETAILS [id= ${personnelDetails.id}]"
                sr.varField1 = personnelDetails.sta10Id.toString()

                sr.responseStatus = sr.serviceMapsId?.successStatusCode
                sr.responseMessage = "Success ${sr.payload}"
                sr.status = s.successStatus
                sr = serviceRequestsRepository.save(sr)
                sr.processingEndDate = Timestamp.from(Instant.now())
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }


    fun permitMultipleInvoiceCalculation(
        s: ServiceMapsEntity,
        user: UsersEntity,
        batchInvoiceDto: NewBatchInvoiceDto,
    ): Pair<ServiceRequestsEntity, Pair<QaBatchInvoiceEntity,List<SageValuesDto>>> {

        var sr = commonDaoServices.createServiceRequest(s)
        var invoiceBatchDetails: QaBatchInvoiceEntity? = null
        val sageValuesDtoList = mutableListOf<SageValuesDto>()
        try {
            var batchID = batchInvoiceDto.batchID
            batchInvoiceDto.permitInvoicesID
                ?.forEach { permitId ->
                    val userID = user.id ?: throw Exception("INVALID USER ID")
                    var permitInvoiceFound = findPermitInvoiceByPermitID(permitId)
                    val permitDetails = findPermitBYID(permitId)
                    val permitType = findPermitType(applicationMapProperties.mapQAPermitTypeIdInvoices)
                    val attachedPermitPlantDetails =findPlantDetails(permitDetails.attachedPlantId?: throw Exception("MISSING PLANT DETAILS (ID)"))
                    val paymentRevenueCode = findPaymentRevenueWithRegionIDAndPermitType(attachedPermitPlantDetails.region ?: throw Exception("MISSING REGION ID"), permitType.id ?: throw Exception("MISSING REGION ID"))

                    invoiceQaBatchRepo.findByIdOrNull(batchID)
                        ?.let { invoiceDetails ->

                            with(permitInvoiceFound) {
                                batchInvoiceNo = invoiceDetails.id
                                modifiedBy = commonDaoServices.concatenateName(user)
                                modifiedOn = commonDaoServices.getTimestamp()
                            }
                            permitInvoiceFound = invoiceMasterDetailsRepo.save(permitInvoiceFound)

                            with(invoiceDetails) {
                                description = "${permitInvoiceFound.invoiceRef},$description"
                                totalAmount = totalAmount?.plus(
                                    permitInvoiceFound.totalAmount ?: throw Exception("INVALID AMOUNT")
                                )
                                totalTaxAmount = totalTaxAmount?.plus(
                                    permitInvoiceFound.taxAmount ?: throw Exception("INVALID TAX AMOUNT")
                                )

                            }
                            invoiceBatchDetails = invoiceQaBatchRepo.save(invoiceDetails)

                            val detailBody = SageValuesDto().apply {
                                revenueAcc = paymentRevenueCode.revenueCode
                                revenueAccDesc = paymentRevenueCode.revenueDescription
                                taxable = 1
                                totalAmount = invoiceBatchDetails!!.totalAmount
                                taxAmount = invoiceBatchDetails!!.totalTaxAmount
                            }
                            sageValuesDtoList.add(detailBody)
                        }
                        ?: kotlin.run {
                            var batchInvoicePermit = QaBatchInvoiceEntity()
                            with(batchInvoicePermit) {
                                invoiceNumber = applicationMapProperties.mapInvoicesPrefix + generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, true).toUpperCase()
                                userId = userID
                                plantId = batchInvoiceDto.plantID
                                varField1 = batchInvoiceDto.isWithHolding.toString() // withholding field
                                if (batchInvoiceDto.isWithHolding == 1L) {
                                    var foundTotalAmount = permitInvoiceFound.totalAmount
                                    foundTotalAmount = foundTotalAmount?.minus(permitInvoiceFound.taxAmount!!)

                                    val taxFoundAmount =
                                        foundTotalAmount?.multiply(applicationMapProperties.mapInvoicesPermitWithHolding)

                                    foundTotalAmount = taxFoundAmount?.let { foundTotalAmount?.plus(it) }

                                    with(permitInvoiceFound) {
                                        totalAmount = foundTotalAmount
                                        taxAmount = taxFoundAmount
                                        varField2 = "WITH HELD TRUE"
                                        modifiedBy = commonDaoServices.concatenateName(user)
                                        modifiedOn = commonDaoServices.getTimestamp()
                                    }
                                    permitInvoiceFound = invoiceMasterDetailsRepo.save(permitInvoiceFound)
                                }
                                status = s.activeStatus
                                description = "${permitInvoiceFound.invoiceRef}"
                                totalAmount = permitInvoiceFound.totalAmount
                                totalTaxAmount = permitInvoiceFound.taxAmount
                                createdBy = commonDaoServices.concatenateName(user)
                                createdOn = commonDaoServices.getTimestamp()
                            }
                            batchInvoicePermit = invoiceQaBatchRepo.save(batchInvoicePermit)

                            with(permitInvoiceFound) {
                                batchInvoiceNo = batchInvoicePermit.id
                                modifiedBy = commonDaoServices.concatenateName(user)
                                modifiedOn = commonDaoServices.getTimestamp()
                            }
                            permitInvoiceFound = invoiceMasterDetailsRepo.save(permitInvoiceFound)

                            invoiceBatchDetails = batchInvoicePermit

                            val detailBody = SageValuesDto().apply {
                                revenueAcc = paymentRevenueCode.revenueCode
                                revenueAccDesc = paymentRevenueCode.revenueDescription
                                taxable = 1
                                totalAmount = invoiceBatchDetails!!.totalAmount
                                taxAmount = invoiceBatchDetails!!.totalTaxAmount
                            }
                            sageValuesDtoList.add(detailBody)

                            //Create details to batch invoice for all transactions at kebs main Staging table
                        }

                    batchID = invoiceBatchDetails?.id!!

                    KotlinLogging.logger { }.info("batch ID = ${invoiceBatchDetails?.id}")

                    sr.payload = "permitInvoiceFound[id= ${permitInvoiceFound.createdBy}]"
                    sr.names =
                        "${permitInvoiceFound.invoiceRef} ${permitInvoiceFound.totalAmount}${permitInvoiceFound.taxAmount}"
                    sr.varField1 = invoiceBatchDetails?.id.toString()

                    sr.responseStatus = sr.serviceMapsId?.successStatusCode
                    sr.responseMessage = "Success ${sr.payload}"
                    sr.status = s.successStatus
                    sr = serviceRequestsRepository.save(sr)
                    sr.processingEndDate = Timestamp.from(Instant.now())
                }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }



        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, Pair(invoiceBatchDetails ?: throw Exception("INVALID BATCH INVOICE DETAILS"),sageValuesDtoList))
    }

    fun permitMultipleInvoiceRemoveInvoice(
        s: ServiceMapsEntity,
        user: UsersEntity,
        batchInvoiceDto: NewBatchInvoiceDto,
    ): Pair<ServiceRequestsEntity, QaBatchInvoiceEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        val invoiceDetails = invoiceQaBatchRepo.findByIdOrNull(batchInvoiceDto.batchID)
            ?: throw Exception("INVOICE BATCH WITH [ID=${batchInvoiceDto.batchID}],DOES NOT EXIST ")
        try {

            val userID = user.id ?: throw Exception("INVALID USER ID")
            batchInvoiceDto.permitInvoicesID
                ?.forEach { id ->
                    val permit = findPermitBYID(id)
                    var permitInvoiceFound = findPermitInvoiceByPermitRefNumberANdPermitID(
                        permit.permitRefNumber ?: throw Exception("PERMIT REF NUMBER REQUIRED"), userID, id
                    )
                    var batchID: Long? = null

                    with(invoiceDetails) {
                        description = "${permitInvoiceFound.invoiceRef},$description"
                        totalAmount =
                            totalAmount?.minus(
                                permitInvoiceFound.totalAmount
                                    ?: throw Exception("INVALID AMOUNT")
                            )
                    }
                    batchID = invoiceQaBatchRepo.save(invoiceDetails).id

                    with(permitInvoiceFound) {
                        batchInvoiceNo = null
                        modifiedBy = commonDaoServices.concatenateName(user)
                        modifiedOn = commonDaoServices.getTimestamp()
                    }
                    permitInvoiceFound = invoiceMasterDetailsRepo.save(permitInvoiceFound)

                    sr.payload = "permitInvoiceFound[id= ${permitInvoiceFound.userId}]"
                    sr.names = "${permitInvoiceFound.invoiceRef} ${permitInvoiceFound.totalAmount}"
                    sr.varField1 = batchID.toString()

                    sr.responseStatus = sr.serviceMapsId?.successStatusCode
                    sr.responseMessage = "Success ${sr.payload}"
                    sr.status = s.successStatus
                    sr = serviceRequestsRepository.save(sr)
                    sr.processingEndDate = Timestamp.from(Instant.now())

                }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, invoiceDetails)
    }

//    fun permitMultipleInvoiceUpdateStagingInvoice(
//            s: ServiceMapsEntity,
//            user: UsersEntity,
//            batchID: Long,
//    ): Pair<ServiceRequestsEntity, QaBatchInvoiceEntity> {
//
//        var sr = commonDaoServices.createServiceRequest(s)
//        val invoiceDetails = invoiceQaBatchRepo.findByIdOrNull(batchID) ?: throw Exception("INVOICE BATCH WITH ID=${batchID},DOES NOT EXIST ")
//        try {
//
//            invoiceBatchDetailsRepo.findByIdOrNull(invoiceDetails.invoiceBatchNumberId)?.let {
//                var invoiceBatchDetails = it
//                when (invoiceBatchDetails.paymentStarted) {
//                    1 -> {
//                        throw Exception("INVOICE IS  ALREADY BEING PAID FOR YOU CAN'T BE ABLE TO ADD/REMOVE")
//                    }
//                    else -> {
//                        with(invoiceBatchDetails) {
//                            totalAmount = invoiceDetails.totalAmount
//                            //TODO: remove details from description also
//                            modifiedOn = commonDaoServices.getTimestamp()
//                            modifiedBy = commonDaoServices.concatenateName(user)
//                        }
//                        invoiceBatchDetails = invoiceBatchDetailsRepo.save(invoiceBatchDetails)
//
//                        var stagingReconciliationDetails =
//                                invoiceStagingReconciliationRepo.findByReferenceCodeAndInvoiceId(
//                                        invoiceBatchDetails.batchNumber
//                                                ?: throw Exception("MISSING BATCH NUMBER=${invoiceBatchDetails.batchNumber}"),
//                                        invoiceBatchDetails.id
//                                )
//                                        ?: throw Exception("INVOICE ON STAGING RECONCILIATION WITH ID=${invoiceDetails.invoiceBatchNumberId},DOES NOT EXIST ")
//                        with(stagingReconciliationDetails) {
//                            invoiceAmount = invoiceBatchDetails.totalAmount
//                            actualAmount = invoiceBatchDetails.totalAmount
//                            paidAmount = BigDecimal.ZERO
//                            modifiedOn = commonDaoServices.getTimestamp()
//                            modifiedBy = commonDaoServices.concatenateName(user)
//                        }
//                        stagingReconciliationDetails =
//                                invoiceStagingReconciliationRepo.save(stagingReconciliationDetails)
//
//                        sr.payload = "STAGING RECONCILIATION UPDATED [id= ${stagingReconciliationDetails.id}]"
//                        sr.names =
//                                "BY USER NAME ${commonDaoServices.getUserName(user)} to this amount ${stagingReconciliationDetails.invoiceAmount}"
//                        sr.varField1 = batchID.toString()
//
//                        sr.responseStatus = sr.serviceMapsId?.successStatusCode
//                        sr.responseMessage = "Success ${sr.payload}"
//                        sr.status = s.successStatus
//                        sr = serviceRequestsRepository.save(sr)
//                        sr.processingEndDate = Timestamp.from(Instant.now())
//                    }
//                }
//            } ?: kotlin.run {
//                val newBatchInvoiceDto = NewBatchInvoiceDto()
//                newBatchInvoiceDto.batchID = invoiceDetails.id ?: throw ExpectedDataNotFound("MISSING BATCH ID ON CREATED CONSOLIDATION")
//                KotlinLogging.logger { }.info("batch ID = ${newBatchInvoiceDto.batchID}")
//                permitMultipleInvoiceSubmitInvoice(s, user, newBatchInvoiceDto).second
//            }
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message, e)
////            KotlinLogging.logger { }.trace(e.message, e)
//            sr.status = sr.serviceMapsId?.exceptionStatus
//            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
//            sr.responseMessage = e.message
//            sr = serviceRequestsRepository.save(sr)
//
//        }
//
//        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
//        return Pair(sr, invoiceDetails)
//    }

    fun permitMultipleInvoiceSubmitInvoice(
        s: ServiceMapsEntity,
        user: UsersEntity,
        batchInvoiceDto: NewBatchInvoiceDto,
        sageValuesDtoList: List<SageValuesDto>
    ): Pair<ServiceRequestsEntity, QaBatchInvoiceEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var invoiceDetails: QaBatchInvoiceEntity? = null
        try {

            if (batchInvoiceDto.batchID == -1L) {
                throw Exception("INVALID BATCH ID NUMBER")
            }
            invoiceDetails = findBatchInvoicesWithID(batchInvoiceDto.batchID)

            val isWithHoldingVariable = batchInvoiceDto.isWithHolding
            val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(
                user.userName!!,invoiceDetails.invoiceNumber ?: throw Exception("MISSING INVOICE NUMBER")
            )
            val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                invoiceDetails,
                applicationMapProperties.mapInvoiceTransactionsForPermit,
                user,
                batchInvoiceDetail
            )

//            //Todo: Payment selection
//            val paymentRevenueCode = findPaymentRevenueWithRegionIDAndPermitType(
//                attachedPermitPlantDetails.region ?: throw Exception("MISSING REGION ID"),
//                permitType.id ?: throw Exception("MISSING REGION ID")
//            )
            val manufactureDetails =commonDaoServices.findCompanyProfileWithID(user.companyId ?: throw Exception("MISSING COMPANY ID"))
            val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
            with(myAccountDetails) {
//                reveneCode = paymentRevenueCode.revenueCode
//                revenueDesc = paymentRevenueCode.revenueDescription
                accountName = manufactureDetails.name
                accountNumber = manufactureDetails.kraPin
                currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
//                region = commonDaoServices.findRegionNameByRegionID(attachedPermitPlantDetails.region!!)
                isWithHolding = isWithHoldingVariable

            }

            invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                user.userName!!,
                updateBatchInvoiceDetail,
                myAccountDetails,
                applicationMapProperties.mapInvoiceTransactionsForPermit,
                sageValuesDtoList
            )

            with(invoiceDetails) {
                submittedStatus = s.activeStatus
            }
            invoiceDetails = qaInvoiceBatchUpdateDetails(invoiceDetails, user)

            sr.payload = "invoiceDetails FOUND[id= ${invoiceDetails.userId}]"
            sr.names = "${invoiceDetails.invoiceNumber} ${invoiceDetails.totalAmount}"
            sr.varField1 = invoiceDetails.id.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, invoiceDetails ?: throw Exception("INVALID DETAILS"))
    }


    fun permitInvoiceCalculation(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permit: PermitApplicationsEntity,
        invoiceDetails: QaInvoiceDetailsEntity?,
    ): Pair<ServiceRequestsEntity, QaInvoiceMasterDetailsEntity?> {

        var sr = commonDaoServices.createServiceRequest(s)
        var invoiceGenerated: QaInvoiceMasterDetailsEntity? = null
        try {

            val userDetails = commonDaoServices.findUserByID(permit.userId ?: throw Exception("MISSING USER ID ON PERMIT DETAILS"))
            val permitType = findPermitType(permit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))
            val companyDetails = commonDaoServices.findCompanyProfileWithID(userDetails.companyId ?: throw Exception("MISSING COMPANY ID ON USER DETAILS"))
            val plantDetail = findPlantDetails(permit.attachedPlantId ?: throw Exception("INVALID PLANT ID"))
            when {
                plantDetail.paidDate == null && plantDetail.endingDate == null && plantDetail.inspectionFeeStatus == null && plantDetail.tokenGiven == null && plantDetail.invoiceSharedId == null -> {
                    throw ExpectedDataNotFound("Kindly Pay the Inspection fees First before submitting current application")
                }
                commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() < plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 && plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null -> {
                    KotlinLogging.logger { }.info { "PLANT ID = ${plantDetail.id}" }
                    val manufactureTurnOver = companyDetails.yearlyTurnover ?: throw Exception("MISSING COMPANY TURNOVER DETAILS")
                    //Todo ask ken why list coming back does not have the product that is being generated for.
                    val productsManufacture = findAllProductManufactureInPlantWithPlantID(
                        s.activeStatus,
                        s.activeStatus,
                        s.inactiveStatus,
                        permitType.id ?: throw Exception("MISSING PERMIT TYPE ID"),
                        plantDetail.id
                    )

                    KotlinLogging.logger { }.info { "PRODUCT SIZE BEFORE ADDING ONE = ${productsManufacture.size}" }
        //            KotlinLogging.logger { }.info { "PRODUCT SIZE = ${productsManufacture.size.plus(1)}" }
                    when (permitType.id) {
                        applicationMapProperties.mapQAPermitTypeIdSmark -> {
                            invoiceGenerated = qaInvoiceCalculation.calculatePaymentSMark(
                                permit,
                                user,
                                manufactureTurnOver,
                                productsManufacture.size.toLong(),
                                plantDetail
                            )
                        }

                        applicationMapProperties.mapQAPermitTypeIDDmark -> {
                            invoiceGenerated = qaInvoiceCalculation.calculatePaymentDMark(permit, user, permitType)
                        }

                        applicationMapProperties.mapQAPermitTypeIdFmark -> {
                            invoiceGenerated = qaInvoiceCalculation.calculatePaymentFMark(permit, user, permitType)
                        }
                    }
                }
                commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() > plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 && plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null -> {
                    throw ExpectedDataNotFound("Kindly Pay the Inspection fees First before submitting current application")
                }
            }

            sr.payload = "User[id= ${companyDetails.userId}]"
            sr.names = "${companyDetails.name} ${companyDetails.kraPin}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, invoiceGenerated)
    }

    fun permitInvoiceCalculationSmartFirmUpGrade(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permit: PermitApplicationsEntity,
        invoiceDetails: QaInvoiceDetailsEntity?,
    ): Pair<ServiceRequestsEntity, QaInvoiceMasterDetailsEntity?> {

        var sr = commonDaoServices.createServiceRequest(s)
        var invoiceGenerated: QaInvoiceMasterDetailsEntity? = null
        try {

            val userDetails = commonDaoServices.findUserByID(permit.userId ?: throw Exception("MISSING USER ID ON PERMIT DETAILS"))
            val permitType = findPermitType(permit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))
            val companyDetails = commonDaoServices.findCompanyProfileWithID(userDetails.companyId ?: throw Exception("MISSING COMPANY ID ON USER DETAILS"))
            val plantDetail = findPlantDetails(permit.attachedPlantId ?: throw Exception("INVALID PLANT ID"))
            when {
                plantDetail.paidDate == null && plantDetail.endingDate == null && plantDetail.inspectionFeeStatus == null && plantDetail.tokenGiven == null && plantDetail.invoiceSharedId == null -> {
                    throw ExpectedDataNotFound("Kindly Pay the Inspection fees First before submitting current application")
                }
                commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() < plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 && plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null -> {
                    KotlinLogging.logger { }.info { "PLANT ID = ${plantDetail.id}" }
                    val manufactureTurnOver = companyDetails.yearlyTurnover ?: throw Exception("MISSING COMPANY TURNOVER DETAILS")
                    //Todo ask ken why list coming back does not have the product that is being generated for.
                    val productsManufacture = findAllProductManufactureInPlantWithPlantID(
                        s.activeStatus,
                        s.activeStatus,
                        s.inactiveStatus,
                        permitType.id ?: throw Exception("MISSING PERMIT TYPE ID"),
                        plantDetail.id
                    )

                    KotlinLogging.logger { }.info { "PRODUCT SIZE BEFORE ADDING ONE = ${productsManufacture.size}" }
        //            KotlinLogging.logger { }.info { "PRODUCT SIZE = ${productsManufacture.size.plus(1)}" }
                    when (permitType.id) {
                        applicationMapProperties.mapQAPermitTypeIdSmark -> {
                            invoiceGenerated = qaInvoiceCalculation.calculatePaymentSMarkAfterFirmUpgrade(
                                permit,
                                user,
                                manufactureTurnOver,
                                productsManufacture.size.toLong(),
                                plantDetail
                            )
                        }
                    }
                }
                commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() > plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 && plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null -> {
                    throw ExpectedDataNotFound("Kindly Pay the Inspection fees First before submitting current application")
                }
            }

            sr.payload = "User[id= ${companyDetails.userId}]"
            sr.names = "${companyDetails.name} ${companyDetails.kraPin}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, invoiceGenerated)
    }

    fun permitInvoiceSTKPush(
        s: ServiceMapsEntity,
        user: UsersEntity,
        phoneNumber: String,
        invoice: QaBatchInvoiceEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            //TODO: PAYMENT METHOD UPDATE THE AMOUNT BY REMOVING THE STATIC VALUE
            user.userName?.let {
                invoice.invoiceNumber?.let { it1 ->
                    mpesaServices.sanitizePhoneNumber(phoneNumber)?.let { it2 ->
                        invoice.totalAmount?.let { it3 ->
                            mpesaServices.mainMpesaTransaction(
                                it3,
                                it2, it1, it, applicationMapProperties.mapInvoiceTransactionsForPermit
                            )
                        }
                    }
                }
            }

            sr.payload = "User[id= ${user.id}]"
            sr.names = "$phoneNumber} ${invoice.invoiceNumber}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun updatePlantDetails(
        s: ServiceMapsEntity,
        user: UsersEntity,
        plantDetail: ManufacturePlantDetailsEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {
            with(plantDetail) {
                lastModifiedBy = commonDaoServices.concatenateName(user)
                lastModifiedOn = commonDaoServices.getTimestamp()
            }
            manufacturePlantRepository.save(plantDetail)

            sr.payload = "Plant Detail [id= ${plantDetail.id}]"
            sr.names = "${plantDetail.buildingName}} ${plantDetail.lastModifiedOn}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun permitGenerateFmark(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permit: PermitApplicationsEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var fmarkPermit = PermitApplicationsEntity()
        try {

            val permitType = findPermitType(applicationMapProperties.mapQAPermitTypeIdFmark)
//            val smark = permit.id?.let { findPermitBYID(it) } ?: throw ExpectedDataNotFound("SMARK Id Not found")


            with(fmarkPermit) {
                commodityDescription = permit.commodityDescription
                tradeMark = permit.tradeMark
                divisionId = permit.divisionId
                sectionId = permit.sectionId
                standardCategory = permit.standardCategory
                broadProductCategory = permit.broadProductCategory
                productCategory = permit.productCategory
                product = permit.product
                productSubCategory = permit.productSubCategory
                applicantName = permit.applicantName
                firmName = permit.firmName
                postalAddress = permit.postalAddress
                telephoneNo = permit.telephoneNo
                email = permit.email
                physicalAddress = permit.physicalAddress
                faxNo = permit.faxNo
                plotNo = permit.plotNo
                designation = permit.designation
                userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                attachedPlantId = permit.attachedPlantId
                attachedPlantRemarks = permit.attachedPlantRemarks
            }
            fmarkPermit = permitSave(fmarkPermit, permitType, user, s).second

            val savedSmarkFmarkId = generateSmarkFmarkEntity(permit, fmarkPermit, user)


            with(permit) {
                fmarkGenerated = 1
            }

            val updateSmarkAndFmarkDetails = permitUpdateDetails(permit, s, user)

//            when (oldPermit.permitType) {
//                applicationMapProperties.mapQAPermitTypeIdSmark -> {

            val oldSta10 = findSTA10WithPermitRefNumberANdPermitID(
                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                permit.id ?: throw Exception("INVALID PERMIT ID")
            )
            var newSta10 = QaSta10Entity()
            newSta10 = commonDaoServices.updateDetails(oldSta10, newSta10) as QaSta10Entity
            newSta10.id = null
            sta10NewSave(fmarkPermit, newSta10, user, s)

            regenerateSameDetailsForClonedSTA10(newSta10, oldSta10, permit, fmarkPermit)

//            val sta10 = findSTA10WithPermitRefNumberBYPPermitID(
//                permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
//                permit.id ?: throw Exception("INVALID PERMIT REF NUMBER")
//            )
//            var newSta10 = QaSta10Entity()
//            newSta10 = commonDaoServices.updateDetails(sta10, newSta10) as QaSta10Entity
//            with(newSta10) {
//                id = null
//                permitRefNumber = fmarkPermit.permitRefNumber
//                permitId = fmarkPermit.id
//            }
//
//            sta10NewSave(fmarkPermit, newSta10, user, s)
//                }
//                applicationMapProperties.mapQAPermitTypeIDDmark -> {
//                    val sta3 = findSTA3WithPermitRefNumber(oldPermit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"))
//                    var newSta3 = QaSta3Entity()
//                    newSta3 = commonDaoServices.updateDetails(sta3, newSta3) as QaSta3Entity
//                    newSta3.id = null
//                    sta3NewSave(savePermit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), newSta3, user, s)
//                }
//            }

            sr.payload = "savedSmarkFmarkId [id= ${savedSmarkFmarkId.id}]"
            sr.names = " Fmark created ID = ${fmarkPermit.id} SMARK TIED ID = ${permit.id}"
            sr.varField1 = "${fmarkPermit.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, fmarkPermit)
    }


    fun permitGenerateFMarkFromAwardedPermit(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permit: PermitApplicationsEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var fmarkPermit = PermitApplicationsEntity()
        val pcmId = user.id

        try {

            val permitTypeDetails = findPermitType(applicationMapProperties.mapQAPermitTypeIdFmark)
//            fmarkPermit = SerializationUtils.clone(permit)

            fmarkPermit = commonDaoServices.updateDetails(
                permit,
                fmarkPermit
            ) as PermitApplicationsEntity

            val awardedPermitNumberToBeAwarded = iQaAwardedPermitTrackerEntityRepository.getMaxId()?.plus(1)


            with(fmarkPermit) {
                id = null
                smarkGeneratedFrom = 1
                varField6 = pcmId.toString()
                permitType = permitTypeDetails.id
                permitStatus = applicationMapProperties.mapQaStatusPermitAwarded
                permitRefNumber = "REF${permitTypeDetails.markNumber}${
                    generateRandomText(
                        5,
                        s.secureRandom,
                        s.messageDigestAlgorithm,
                        true
                    )
                }".toUpperCase()
//                awardedPermitNumber = "${permitTypeDetails.markNumber}${
//                    generateRandomText(
//                            6,
//                            s.secureRandom,
//                            s.messageDigestAlgorithm,
//                            false
//                    )
//                }".toUpperCase()

                val a = awardedPermitNumberToBeAwarded?.toString()
                val b = permitTypeDetails.markNumber?.toUpperCase()
                awardedPermitNumber = b + a


            }

            fmarkPermit = permitRepo.save(fmarkPermit)

            //save awarded permit number
            val awardPermit = QaAwardedPermitTrackerEntity()
            awardPermit.awardedPermitNumber = awardedPermitNumberToBeAwarded
            awardPermit.createdOn = commonDaoServices.getTimestamp()
            iQaAwardedPermitTrackerEntityRepository.save(awardPermit)


            val savedSMarkFMarkId = generateSmarkFmarkEntity(permit, fmarkPermit, user)

            when (permit.permitType) {
                applicationMapProperties.mapQAPermitTypeIdSmark -> {
                    val oldSta10 = findSTA10WithPermitRefNumberANdPermitID(
                        permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                        permit.id ?: throw Exception("INVALID PERMIT ID")
                    )
                    var newSta10 = QaSta10Entity()
                    newSta10 = commonDaoServices.updateDetails(oldSta10, newSta10) as QaSta10Entity
                    newSta10.id = null
                    sta10NewSave(fmarkPermit, newSta10, user, s)

                    regenerateSameDetailsForClonedSTA10(newSta10, oldSta10, permit, fmarkPermit)

                }

                applicationMapProperties.mapQAPermitTypeIDDmark -> {
                    val sta3 = findSTA3WithPermitIDAndRefNumber(
                        permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                        permit.id ?: throw Exception("INVALID PERMIT ID")
                    )
                    var newSta3 = QaSta3Entity()
                    newSta3 = commonDaoServices.updateDetails(sta3, newSta3) as QaSta3Entity
                    newSta3.id = null
                    sta3NewSave(
                        fmarkPermit.id ?: throw Exception("INVALID PERMIT ID"),
                        fmarkPermit.permitRefNumber ?: throw Exception("INVALID PERMIT ID"),
                        newSta3,
                        user,
                        s
                    )

                    val sta3FileList = findAllUploadedFileBYPermitIDAndSta3Status(
                        permit.id ?: throw Exception("MISSING PERMIT ID"),
                        1
                    )
                    sta3FileList.forEach { fileList ->
                        val newFileList = SerializationUtils.clone(fileList)
                        with(newFileList) {
                            id = null
                            permitId = fmarkPermit.id
                        }
                        qaUploadsRepo.save(newFileList)
                    }
                }
            }

            with(permit) {
                fmarkGenerated = 1
            }

            permitUpdateDetails(permit, s, user)

            sr.payload = "savedSmarkFmarkId [id= ${savedSMarkFMarkId.id}]"
            sr.names = " Fmark created ID = ${fmarkPermit.id} SMARK TIED ID = ${permit.id}"
            sr.varField1 = "${fmarkPermit.id}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return Pair(sr, fmarkPermit)
    }

    fun generateSmarkFmarkEntity(
        smark: PermitApplicationsEntity,
        fmarkPermit: PermitApplicationsEntity,
        user: UsersEntity
    ): QaSmarkFmarkEntity {
        var savedSmarkFmarkId = QaSmarkFmarkEntity()
        with(savedSmarkFmarkId) {
            smarkId = smark.id
            fmarkId = fmarkPermit.id
            createdOn = commonDaoServices.getTimestamp()
            createdBy = commonDaoServices.concatenateName(user)
        }

        savedSmarkFmarkId = smarkFmarkRepo.save(savedSmarkFmarkId)
        return savedSmarkFmarkId
    }


    fun findFirmTypeById(firmTypeID: Long): PermitRatingEntity {
        iPermitRatingRepo.findByIdOrNull(firmTypeID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Firm type with Id = ${firmTypeID} found ")
    }

    fun manufactureType(manufactureTurnOver: BigDecimal): PermitRatingEntity {
        val ratesMap = iPermitRatingRepo.findAllByStatus(1) ?: throw Exception("SMARK RATE SHOULD NOT BE NULL")
        val selectedRate = ratesMap.filter {
            manufactureTurnOver > (it.min ?: BigDecimal.ZERO) && manufactureTurnOver <= (it.max
                ?: throw NullValueNotAllowedException(
                    "Max needs to be defined"
                ))
        }.firstOrNull() ?: throw NullValueNotAllowedException("Rate not found")

        KotlinLogging.logger { }.debug { "selected Rate fixed cost = ${selectedRate.id} and  ${selectedRate.firmType}" }

        return selectedRate
    }

    private fun mutableListForTurOverBelow200KSmark(
        plantDetail: ManufacturePlantDetailsEntity,
        permitType: PermitTypesEntity,
        permit: PermitApplicationsEntity,
        inspectionCost: BigDecimal,
        applicationCost: BigDecimal,
        productMaximum: Long,
        taxRate: BigDecimal,
        map: ServiceMapsEntity,
        m: MutableList<BigDecimal?>
    ): MutableList<BigDecimal?> {
        KotlinLogging.logger { }.debug { "Turnover is less than 200, 000" }

        var stgAmt: BigDecimal? = null
        var taxAmount: BigDecimal? = null
        var amountToPay: BigDecimal? = null
        var inspectionCostValue: BigDecimal? = inspectionCost
        var applicationCostValue: BigDecimal?
        var m1 = m

        val productList = findAllProductManufactureINPlantWithID(
            map.activeStatus,
            map.inactiveStatus,
            permitType.id ?: throw Exception("INVALID PERMIT TYPE ID"),
            plantDetail.id
        )
        val productSize = productList.size
        var remainingSize: BigDecimal
        if (productSize > productMaximum) {
            remainingSize = productSize.minus(productMaximum).toBigDecimal()
            applicationCostValue = remainingSize.times(applicationCost)
            stgAmt = applicationCostValue.plus(inspectionCostValue ?: throw Exception("INVALID INSPECTION COST VALUE"))
        } else {
            applicationCostValue = inspectionCostValue
            inspectionCostValue = null
            stgAmt = applicationCostValue
        }

        taxAmount = taxRate.times(stgAmt ?: throw Exception("INVALID STG AMOUNT"))
        amountToPay = taxAmount.let { stgAmt.plus(it) }

        KotlinLogging.logger { }.debug { "Total Amount To Pay   = " + amountToPay.toDouble() }

        m1 = myReturPaymentValues(
            m1,
            inspectionCostValue,
            applicationCostValue,
            amountToPay,
            taxAmount
        )
        return m1
    }

    private fun mutableListForTurnOverBelow500kAndAbove200KSmark(
        plantDetail: ManufacturePlantDetailsEntity,
        permitType: PermitTypesEntity,
        permit: PermitApplicationsEntity,
        inspectionCost: BigDecimal,
        applicationCost: BigDecimal,
        productMaximum: Long,
        taxRate: BigDecimal,
        map: ServiceMapsEntity,
        m: MutableList<BigDecimal?>
    ): MutableList<BigDecimal?> {

        KotlinLogging.logger { }.info { "Turnover is Below 500K And Above 200K" }
        var stgAmt: BigDecimal? = null
        var taxAmount: BigDecimal? = null
        var amountToPay: BigDecimal? = null
        var inspectionCostValue: BigDecimal? = inspectionCost
        var applicationCostValue: BigDecimal?
        var m1 = m

        val productList = findAllProductManufactureINPlantWithID(
            map.activeStatus,
            map.inactiveStatus,
            permitType.id ?: throw Exception("INVALID PERMIT TYPE ID"),
            plantDetail.id
        )
        val productSize = productList.size
        var remainingSize: BigDecimal
        if (productSize > productMaximum) {
            remainingSize = productSize.minus(productMaximum).toBigDecimal()
            applicationCostValue = remainingSize.times(applicationCost)
            stgAmt = applicationCostValue.plus(inspectionCostValue ?: throw Exception("INVALID INSPECTION COST VALUE"))
        } else {
            applicationCostValue = inspectionCostValue
            inspectionCostValue = null
            stgAmt = applicationCostValue
        }

        taxAmount = taxRate.times(stgAmt ?: throw Exception("INVALID STG AMOUNT"))
        amountToPay = taxAmount.let { stgAmt.plus(it) }

        KotlinLogging.logger { }.info { "Total Amount To Pay   = " + amountToPay.toDouble() }

        m1 = myReturPaymentValues(
            m1,
            inspectionCostValue,
            applicationCostValue,
            amountToPay,
            taxAmount
        )
        return m1
    }

    private fun mutableListForTurnOverAbove500KSmark(
        plantDetail: ManufacturePlantDetailsEntity,
        permitType: PermitTypesEntity,
        permit: PermitApplicationsEntity,
        inspectionCost: BigDecimal,
        applicationCost: BigDecimal,
        taxRate: BigDecimal,
        map: ServiceMapsEntity,
        user: UsersEntity,
        currentDate: Date,
        m: MutableList<BigDecimal?>
    ): MutableList<BigDecimal?> {
        KotlinLogging.logger { }.info { "Turnover is above 500, 000" }

        var stgAmt: BigDecimal? = null
        var taxAmount: BigDecimal? = null
        var amountToPay: BigDecimal? = null
        var inspectionCostValue: BigDecimal? = inspectionCost
        var m1 = m

        if (plantDetail.paidDate == null && plantDetail.endingDate == null && plantDetail.inspectionFeeStatus == null) {
            stgAmt = applicationCost.plus(inspectionCostValue ?: throw Exception("INVALID INSPECTION COST VALUE"))
            with(plantDetail) {
                inspectionFeeStatus = 1
                paidDate = commonDaoServices.getCurrentDate()
                endingDate = commonDaoServices.addYearsToCurrentDate(
                    permitType.numberOfYears ?: throw Exception("INVALID NUMBER OF YEARS")
                )
            }
            updatePlantDetails(map, user, plantDetail)
        } else if (currentDate > plantDetail.paidDate && currentDate < plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1) {
            stgAmt = applicationCost
            inspectionCostValue = null
        } else {
            stgAmt = applicationCost.plus(inspectionCostValue ?: throw Exception("INVALID INSPECTION COST VALUE"))
            with(plantDetail) {
                inspectionFeeStatus = 1
                paidDate = commonDaoServices.getCurrentDate()
                endingDate = commonDaoServices.addYearsToCurrentDate(
                    permitType.numberOfYears ?: throw Exception("INVALID NUMBER OF YEARS")
                )
            }
            updatePlantDetails(map, user, plantDetail)
        }

//        stgAmt = when (inspectionCost) {
//            null -> {
//                applicationCost
//            }
//            else -> {
//                applicationCost?.plus(inspectionCost)
//            }
//        }

        taxAmount = stgAmt.let { taxRate.times(it) }
        amountToPay = taxAmount.let { stgAmt.plus(it) }

        KotlinLogging.logger { }.info { "Total Amount To Pay   = " + amountToPay.toDouble() }

        m1 = myReturPaymentValues(
            m1,
            inspectionCostValue,
            applicationCost,
            amountToPay,
            taxAmount
        )
        return m1
    }

    private fun myReturPaymentValues(
        m: MutableList<BigDecimal?>,
        inspectionCost: BigDecimal?,
        applicationCost: BigDecimal?,
        amountToPay: BigDecimal?,
        taxAmount: BigDecimal?
    ): MutableList<BigDecimal?> {
        m.add(inspectionCost)
        m.add(applicationCost)
        m.add(amountToPay)
        m.add(taxAmount)

        return m
    }

    fun inspectionRecommendationUpdate(
        qaInspectionReportRecommendation: QaInspectionReportRecommendationEntity,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {
            with(qaInspectionReportRecommendation) {
                status = s.activeStatus
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            qaInspectionReportRecommendationRepo.save(qaInspectionReportRecommendation)

            sr.payload = "qaInspectionReportRecommendation updated details [id= ${qaInspectionReportRecommendation.id}]"
            sr.names =
                "qaInspectionReportRecommendation ON = ${qaInspectionReportRecommendation.modifiedOn} BY ${qaInspectionReportRecommendation.modifiedBy}"
            sr.varField1 = "${qaInspectionReportRecommendation.permitId}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger {}.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun sendComplianceStatusAndLabReport(
        permitDetails: PermitApplicationsEntity,
        compliantStatus: String,
        compliantRemarks: String,
        attachment: String?,
    ) {
        val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "LAB REPORT AND COMPLIANCE STATUS "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "Lab test report with the following compliance status  $compliantStatus" +
                "\n  and  the Following Remarks  $compliantRemarks" +
                "for the following permit with REF number ${permitDetails.permitRefNumber} : You have 30 days to perform corrective action for re-inspection.  "

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendAssessmentReportRejection(
        permitDetails: PermitApplicationsEntity,
        compliantStatus: String,
        compliantRemarks: String
    ) {
        var manufacturer = permitDetails.assessorId?.let { commonDaoServices.findUserByID(it) }
        val subject = "ASSESSMENT REPORT REJECTION"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "Assessment report was REJECTED with the following compliance status  $compliantStatus" +
                "\n  and  the Following Remarks  $compliantRemarks" +
                "for the following permit with REF number ${permitDetails.permitRefNumber}"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
        manufacturer = permitDetails.leadAssessorId?.let { commonDaoServices.findUserByID(it) }
        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForJustification(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.hodId?.let { commonDaoServices.findUserByID(it) }
        val subject = "JUSTIFICATION REPORT "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "A justification report has been generated for the following permit with REF number ${permitDetails.permitRefNumber} : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id} \n" +
                "That awaits your approval"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForRecommendation(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qamId?.let { commonDaoServices.findUserByID(it) }
        val subject = "RECOMMENDATION FOR AWARDING OF PERMIT"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "A recommendation has been added for the following permit with REF number ${permitDetails.permitRefNumber} : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id} \n" +
                "That awaits your approval"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForRecommendationCorrectness(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qaoId?.let { commonDaoServices.findUserByID(it) }
        val subject = "RECOMMENDATION FOR AWARDING OF PERMIT CORRECTION"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber},, Recommendation has been rejected due to the following reasons: ${permitDetails.recommendationApprovalRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id} \n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForPermitCorrectnessFromQamHod(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qaoId?.let { commonDaoServices.findUserByID(it) }
        val subject = "AWARDING OF PERMIT CORRECTION"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, Has been rejected due to the following reasons: ${permitDetails.hodQamApproveRejectRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id} \n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForDeferredPermitToQaoFromPSC(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qaoId?.let { commonDaoServices.findUserByID(it) }
        val subject = "AWARDING OF PERMIT DEFERRED "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, has been DEFERRED due to the following reasons: ${permitDetails.pscMemberApprovalRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForDeferredPermitToQaoFromPCM(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qaoId?.let { commonDaoServices.findUserByID(it) }
        val subject = "AWARDING OF PERMIT DEFERRED "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, has been DEFERRED due to the following reasons: ${permitDetails.pcmApprovalRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForDeferredPermitToAssessorFromPCM(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.assessorId?.let { commonDaoServices.findUserByID(it) }
        val subject = "AWARDING OF PERMIT DEFERRED "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, has been DEFERRED due to the following reasons: ${permitDetails.pacDecisionRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }

        val manufacturer2 = permitDetails.leadAssessorId?.let { commonDaoServices.findUserByID(it) }
        val subject2 = "AWARDING OF PERMIT DEFERRED "
        val messageBody2 = "Dear ${manufacturer2?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, has been DEFERRED due to the following reasons: ${permitDetails.pacDecisionRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer2?.email?.let { notifications.sendEmail(it, subject2, messageBody2) }
    }

    fun sendNotificationForPermitReviewRejectedFromPCM(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "PERMIT REJECTED "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, has been REJECTED due to the following reasons: ${permitDetails.pcmReviewApprovalRemarks} \n" +
                "Please do some correction and send it back for review again : "
//                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForPermitReviewRejected(permitDetails: PermitApplicationsEntity, rejectedReason: String) {
        val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "PERMIT REJECTED "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, has been REJECTED due to the following reasons: $rejectedReason \n" +
                "Please do some correction and send it back for review again : "
//                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationPSCForAwardingPermit(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.pscMemberId?.let { commonDaoServices.findUserByID(it) }
        val subject = "PERMIT APPLICATION FOR APPROVAL"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, awaits your approval for inspection review \n" +
                " ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForAwardedPermitToManufacture(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "PERMIT AWARDED"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit application with REF NUMBER ${permitDetails.permitRefNumber}, has been awarded" +
                "Find attached permit certificate issued"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody, null) }
    }

    fun sendNotificationPCMForAwardingPermit(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.pcmId?.let { commonDaoServices.findUserByID(it) }
        val subject = "PERMIT APPLICATION/RENEWAL FOR APPROVAL"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, your approval for inspection review \n" +
                " ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }


    fun mapDtoSTA3AndQaSta3Entity(sta3Dto: STA3Dto): QaSta3Entity {
        val sta3 = QaSta3Entity()
        with(sta3) {
            produceOrdersOrStock = sta3Dto.produceOrdersOrStock
            issueWorkOrderOrEquivalent = sta3Dto.issueWorkOrderOrEquivalent
            identifyBatchAsSeparate = sta3Dto.identifyBatchAsSeparate
            productsContainersCarryWorksOrder = sta3Dto.productsContainersCarryWorksOrder
            isolatedCaseDoubtfulQuality = sta3Dto.isolatedCaseDoubtfulQuality
            headQaQualificationsTraining = sta3Dto.headQaQualificationsTraining
            reportingTo = sta3Dto.reportingTo
            separateQcid = sta3Dto.separateQcid
            testsRelevantStandard = sta3Dto.testsRelevantStandard
            spoComingMaterials = sta3Dto.spoComingMaterials
            spoProcessOperations = sta3Dto.spoProcessOperations
            spoFinalProducts = sta3Dto.spoFinalProducts
            monitoredQcs = sta3Dto.monitoredQcs
            qauditChecksCarried = sta3Dto.qauditChecksCarried
            informationQcso = sta3Dto.informationQcso
            mainMaterialsPurchasedSpecification = sta3Dto.mainMaterialsPurchasedSpecification
            adoptedReceiptMaterials = sta3Dto.adoptedReceiptMaterials
            storageFacilitiesExist = sta3Dto.storageFacilitiesExist
            stepsManufacture = sta3Dto.stepsManufacture
            maintenanceSystem = sta3Dto.maintenanceSystem
            qcsSupplement = sta3Dto.qcsSupplement
            qmInstructions = sta3Dto.qmInstructions
            testEquipmentUsed = sta3Dto.testEquipmentUsed
            indicateExternalArrangement = sta3Dto.indicateExternalArrangement
            levelDefectivesFound = sta3Dto.levelDefectivesFound
            levelClaimsComplaints = sta3Dto.levelClaimsComplaints
            independentTests = sta3Dto.independentTests
            indicateStageManufacture = sta3Dto.indicateStageManufacture
        }

        return sta3
    }

    fun mapDtoSTA3View(sta3: QaSta3Entity, permitID: Long): STA3Dto {
        val sta3ViewDto = STA3Dto()
        with(sta3ViewDto) {
            produceOrdersOrStock = sta3.produceOrdersOrStock
            issueWorkOrderOrEquivalent = sta3.issueWorkOrderOrEquivalent
            identifyBatchAsSeparate = sta3.identifyBatchAsSeparate
            productsContainersCarryWorksOrder = sta3.productsContainersCarryWorksOrder
            isolatedCaseDoubtfulQuality = sta3.isolatedCaseDoubtfulQuality
            headQaQualificationsTraining = sta3.headQaQualificationsTraining
            reportingTo = sta3.reportingTo
            separateQcid = sta3.separateQcid
            testsRelevantStandard = sta3.testsRelevantStandard
            spoComingMaterials = sta3.spoComingMaterials
            spoProcessOperations = sta3.spoProcessOperations
            spoFinalProducts = sta3.spoFinalProducts
            monitoredQcs = sta3.monitoredQcs
            qauditChecksCarried = sta3.qauditChecksCarried
            informationQcso = sta3.informationQcso
            mainMaterialsPurchasedSpecification = sta3.mainMaterialsPurchasedSpecification
            adoptedReceiptMaterials = sta3.adoptedReceiptMaterials
            storageFacilitiesExist = sta3.storageFacilitiesExist
            stepsManufacture = sta3.stepsManufacture
            maintenanceSystem = sta3.maintenanceSystem
            qcsSupplement = sta3.qcsSupplement
            qmInstructions = sta3.qmInstructions
            testEquipmentUsed = sta3.testEquipmentUsed
            indicateExternalArrangement = sta3.indicateExternalArrangement
            levelDefectivesFound = sta3.levelDefectivesFound
            levelClaimsComplaints = sta3.levelClaimsComplaints
            independentTests = sta3.independentTests
            indicateStageManufacture = sta3.indicateStageManufacture
            sta3FilesList = findAllUploadedFileBYPermitIDAndSta3Status(permitID, 1).let { listFilesDto(it) }
        }

        return sta3ViewDto
    }

    fun mapDtoSTA1View(permit: PermitApplicationsEntity): STA1Dto {
        val sta1ViewDto = STA1Dto()
        with(sta1ViewDto) {
            id = permit.id
            commodityDescription = permit.commodityDescription
            tradeMark = permit.tradeMark
            applicantName = permit.applicantName
            sectionId = permit.sectionId
            permitForeignStatus = permit.permitForeignStatus
            attachedPlant = permit.attachedPlantId
            createFmark = permit.fmarkGenerateStatus
        }

        return sta1ViewDto
    }

    fun mapDtoQRCodeQaDetailsView(permit: PermitApplicationsEntity): QRCodeScannedQADto {
        val qrCodeViewDto = QRCodeScannedQADto()
        with(qrCodeViewDto) {
            productName = permit.productName
            tradeMark = permit.tradeMark
            awardedPermitNumber = permit.awardedPermitNumber
            dateOfIssue = permit.dateOfIssue
            dateOfExpiry = permit.dateOfExpiry
        }
        return qrCodeViewDto
    }

    fun mapDtoSTA10SectionAAndQaSta10Entity(sta10SectionADto: STA10SectionADto): QaSta10Entity {
        val sta10 = QaSta10Entity()
        with(sta10) {
            id = sta10SectionADto.id
            firmName = sta10SectionADto.firmName
            statusCompanyBusinessRegistration = sta10SectionADto.statusCompanyBusinessRegistration
            ownerNameProprietorDirector = sta10SectionADto.ownerNameProprietorDirector
            postalAddress = sta10SectionADto.postalAddress
            contactPerson = sta10SectionADto.contactPerson
            telephone = sta10SectionADto.telephone
            emailAddress = sta10SectionADto.emailAddress
            physicalLocationMap = sta10SectionADto.physicalLocationMap
            county = sta10SectionADto.county
            town = sta10SectionADto.town
            totalNumberFemale = sta10SectionADto.totalNumberFemale
            totalNumberMale = sta10SectionADto.totalNumberMale
            totalNumberPermanentEmployees = sta10SectionADto.totalNumberPermanentEmployees
            totalNumberCasualEmployees = sta10SectionADto.totalNumberCasualEmployees
            averageVolumeProductionMonth = sta10SectionADto.averageVolumeProductionMonth
            handledManufacturingProcessRawMaterials = sta10SectionADto.handledManufacturingProcessRawMaterials
            handledManufacturingProcessInprocessProducts = sta10SectionADto.handledManufacturingProcessInprocessProducts
            handledManufacturingProcessFinalProduct = sta10SectionADto.handledManufacturingProcessFinalProduct
            strategyInplaceRecallingProducts = sta10SectionADto.strategyInplaceRecallingProducts
            stateFacilityConditionsRawMaterials = sta10SectionADto.stateFacilityConditionsRawMaterials
            stateFacilityConditionsEndProduct = sta10SectionADto.stateFacilityConditionsEndProduct
            testingFacilitiesExistSpecifyEquipment = sta10SectionADto.testingFacilitiesExistSpecifyEquipment
            testingFacilitiesExistStateParametersTested = sta10SectionADto.testingFacilitiesExistStateParametersTested
            testingFacilitiesSpecifyParametersTested = sta10SectionADto.testingFacilitiesSpecifyParametersTested
            calibrationEquipmentLastCalibrated = sta10SectionADto.calibrationEquipmentLastCalibrated
            handlingConsumerComplaints = sta10SectionADto.handlingConsumerComplaints
            companyRepresentative = sta10SectionADto.companyRepresentative
            applicationDate = sta10SectionADto.applicationDate
        }

        return sta10
    }

    fun mapDtoSTA10SectionAAndQaSta10View(qasta10entity: QaSta10Entity): STA10SectionADto {
        val sta10 = STA10SectionADto()
        with(sta10) {
            id = qasta10entity.id
            firmName = qasta10entity.firmName
            statusCompanyBusinessRegistration = qasta10entity.statusCompanyBusinessRegistration
            ownerNameProprietorDirector = qasta10entity.ownerNameProprietorDirector
            postalAddress = qasta10entity.postalAddress
            contactPerson = qasta10entity.contactPerson
            telephone = qasta10entity.telephone
            emailAddress = qasta10entity.emailAddress
            physicalLocationMap = qasta10entity.physicalLocationMap
            county = qasta10entity.county
            town = qasta10entity.town
            totalNumberFemale = qasta10entity.totalNumberFemale
            totalNumberMale = qasta10entity.totalNumberMale
            totalNumberPermanentEmployees = qasta10entity.totalNumberPermanentEmployees
            totalNumberCasualEmployees = qasta10entity.totalNumberCasualEmployees
            averageVolumeProductionMonth = qasta10entity.averageVolumeProductionMonth
            handledManufacturingProcessRawMaterials = qasta10entity.handledManufacturingProcessRawMaterials
            handledManufacturingProcessInprocessProducts = qasta10entity.handledManufacturingProcessInprocessProducts
            handledManufacturingProcessFinalProduct = qasta10entity.handledManufacturingProcessFinalProduct
            strategyInplaceRecallingProducts = qasta10entity.strategyInplaceRecallingProducts
            stateFacilityConditionsRawMaterials = qasta10entity.stateFacilityConditionsRawMaterials
            stateFacilityConditionsEndProduct = qasta10entity.stateFacilityConditionsEndProduct
            testingFacilitiesExistSpecifyEquipment = qasta10entity.testingFacilitiesExistSpecifyEquipment
            testingFacilitiesExistStateParametersTested = qasta10entity.testingFacilitiesExistStateParametersTested
            testingFacilitiesSpecifyParametersTested = qasta10entity.testingFacilitiesSpecifyParametersTested
            calibrationEquipmentLastCalibrated = qasta10entity.calibrationEquipmentLastCalibrated
            handlingConsumerComplaints = qasta10entity.handlingConsumerComplaints
            companyRepresentative = qasta10entity.companyRepresentative
            applicationDate = qasta10entity.applicationDate
        }

        return sta10
    }

    fun mapDtoSTA10SectionBAndQaProductManufacturedEntity(sta10SectionBPDto: List<STA10ProductsManufactureDto>): List<QaProductManufacturedEntity> {
        return sta10SectionBPDto.map { p ->
            QaProductManufacturedEntity().apply {
                id = p.id
                productName = p.productName
                productBrand = p.productBrand
                productStandardNumber = p.productStandardNumber
                available = if (p.available == true) 1 else 0
                permitNo = p.permitNo
            }
        }
    }

    fun mapDtoSTA10SectionBAndQaRawMaterialsEntity(sta10rawmaterialsdto: List<STA10RawMaterialsDto>): List<QaRawMaterialEntity> {
        return sta10rawmaterialsdto.map { p ->
            QaRawMaterialEntity().apply {
                id = p.id
                name = p.name
                origin = p.origin
                specifications = p.specifications
                qualityChecksTestingRecords = p.qualityChecksTestingRecords
            }
        }

    }


    fun mapDtoSTA10SectionBAndPersonnelEntity(sta10Personneldto: List<STA10PersonnelDto>): List<QaPersonnelInchargeEntity> {
        return sta10Personneldto.map { p ->
            QaPersonnelInchargeEntity().apply {
                id = p.id
                personnelName = p.personnelName
                qualificationInstitution = p.qualificationInstitution
                dateOfEmployment = p.dateOfEmployment
            }
        }


    }

    fun mapDtoSTA10SectionBAndMachineryAndPlantEntity(machineryAndPlantDto: List<STA10MachineryAndPlantDto>): List<QaMachineryEntity> {
        return machineryAndPlantDto.map { p ->
            QaMachineryEntity().apply {
                id = p.id
                machineName = p.machineName
                typeModel = p.typeModel
                countryOfOrigin = p.countryOfOrigin
            }
        }

    }

    fun mapDtoSTA10SectionBAndManufacturingProcessEntity(manufacturingProcessDto: List<STA10ManufacturingProcessDto>): List<QaManufacturingProcessEntity> {
        return manufacturingProcessDto.map { p ->
            QaManufacturingProcessEntity().apply {
                id = p.id
                processFlowOfProduction = p.processFlowOfProduction
                operations = p.operations
                criticalProcessParametersMonitored = p.criticalProcessParametersMonitored
                frequency = p.frequency
                processMonitoringRecords = p.processMonitoringRecords
            }
        }
    }

    fun mapBatchInvoiceDetails(
        batchInvoiceEntity: QaBatchInvoiceEntity,
        loggedInUser: UsersEntity,
        map: ServiceMapsEntity
    ): BatchInvoiceDto {
        val allInvoicesInBatch = findALlInvoicesPermitWithBatchID(batchInvoiceEntity.id ?: throw Exception("MISSING INVOICE BATCH ID"))
        val companyProfile = commonDaoServices.findCompanyProfile(loggedInUser.id ?: throw Exception("MISSING USER ID FOUND"))

        return BatchInvoiceDto(
            batchInvoiceEntity.sageInvoiceNumber,
            populateInvoiceDetails(companyProfile, batchInvoiceEntity, map),
            listPermitsInvoices(allInvoicesInBatch, null, map)
        )
    }

    fun mapBatchInvoiceDetailsBalance(
        batchInvoiceEntity: QaBatchInvoiceEntity,
        loggedInUser: UsersEntity,
        map: ServiceMapsEntity
    ): StgInvoiceBalanceDto {

        val invoiceBatchID = invoiceDaoService.findInvoiceBatchDetails(
            batchInvoiceEntity.invoiceBatchNumberId ?: throw Exception("MISSING INVOICE BATCH ID")
        )
        val stgPayment = invoiceDaoService.findInvoiceStgReconciliationDetails(
            invoiceBatchID.batchNumber ?: throw Exception("MISSING BATCH NUMBER")
        )

        return StgInvoiceBalanceDto(
            batchInvoiceEntity.id,
            stgPayment.invoiceAmount
        )
    }

    fun mapBatchInvoiceList(batchInvoiceList: List<QaBatchInvoiceEntity>): List<ConsolidatedInvoiceDto> {

        return batchInvoiceList.map { p ->
            ConsolidatedInvoiceDto(
                p.id,
                p.invoiceNumber,
                p.totalAmount,
                p.paidDate,
                p.paidStatus,
                p.submittedStatus,
                p.receiptNo,
                p.sageInvoiceNumber,
            )
        }
    }

    fun mapAllStandardsTogether(standards: List<SampleStandardsEntity>): List<StandardsDto> {
        return standards.map {
            StandardsDto(
                it.id,
                it.standardTitle,
                it.standardNumber,
            )
        }
    }

    fun getTaskListPermit(userID: Long): List<TaskDto>? {
        return qualityAssuranceBpmn.fetchAllTasksByAssignee(userID)?.map { t ->
            TaskDto(
                t.permitId,
                t.task.name,
                t.task.createTime,
                t.permitRefNo
            )
        }

    }


    fun mapAllPlantsTogether(
        plants: List<ManufacturePlantDetailsEntity>,
        map: ServiceMapsEntity
    ): List<PlantsDetailsDto> {
        return plants.map { p ->
            PlantsDetailsDto(
                p.id,
                p.companyProfileId,
                commonDaoServices.findCountiesEntityByCountyId(p.county ?: -1L, map.activeStatus).county,
                commonDaoServices.findTownEntityByTownId(p.town ?: -1L).town,
                p.location,
                p.street,
                p.buildingName,
                p.branchName,
                p.nearestLandMark,
                p.postalAddress,
                p.telephone,
                p.emailAddress,
                p.physicalAddress,
                p.faxNo,
                p.plotNo,
                p.designation,
                p.contactPerson,
            )
        }
    }

    fun updatePermitWithSelectedPermit(permit: Long?, permitBeingUpdated: Long?) {

        try {
            val response = permit?.let { permitBeingUpdated?.let { it1 -> permitRepo.updatePermitToNew(it, it1) } }
            KotlinLogging.logger { }.info("The response is $response")
//            if(response)

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }

        val sta10Id = permit?.let { sta10Repo.findByPermitId(it)?.id }
            ?: throw ExpectedDataNotFound("No STA10 found with the following [ID=$permit]")
        val sta10IdToBeUpdated = permitBeingUpdated?.let { sta10Repo.findByPermitId(it)?.id }
            ?: throw ExpectedDataNotFound("No STA10 found with the following [ID=$permit]")
        try {
            val updateSta10 = permit.let {
                permitBeingUpdated.let { it1 ->
                    sta10Repo.updatePermitWithSta10Data(
                        it,
                        it1
                    )
                }.toString()
            }
            KotlinLogging.logger { }.info("The response is $updateSta10")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }

        //update personnel in charge
        try {
            val updateQaPersonnel = qaPersonnelInchargeRepo.updatePersonnel(sta10Id, sta10IdToBeUpdated)
            KotlinLogging.logger { }.info("The response is $updateQaPersonnel")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }



        try {
            val updateMachinery = machinePlantsSTA10Repo.updateMachinery(sta10Id, sta10IdToBeUpdated)
            KotlinLogging.logger { }.info("The response is $updateMachinery")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }

        try {

            val updateRawMaterials = rawMaterialsSTA10Repo.updateRawMaterials(sta10Id, sta10IdToBeUpdated)
            KotlinLogging.logger { }.info("The response is $updateRawMaterials")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }

        try {

            val manufacturingProcessSTA10Repo =
                manufacturingProcessSTA10Repo.updateManufacturing(sta10Id, sta10IdToBeUpdated)
            KotlinLogging.logger { }.info("The response is $manufacturingProcessSTA10Repo")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }
        try {


            val productsManufactureSTA10Repo =
                productsManufactureSTA10Repo.updateProduct(permitBeingUpdated, sta10IdToBeUpdated)
            KotlinLogging.logger { }.info("The response is $productsManufactureSTA10Repo")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)

        }


    }

    fun deletePermit(permitID: Long) {
        try {
            permitRepo.deletePermit(permitID)
            KotlinLogging.logger { }.info("The response is ok")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            e.printStackTrace();

        }
    }

    fun deleteInvoice(invoiceId: Long) {
        try {
            invoiceMasterDetailsRepo.deleteInvoice(invoiceId)
            KotlinLogging.logger { }.info("The response is ok")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            e.printStackTrace();

        }
    }

    fun findReportAllPermitWithNoFmarkGenerated(
        user: UsersEntity,
        permitType: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPermitTypeAndPaidStatusIsNotNull(
            permitType,

            )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }


    fun findReportAllAwardedPermits(
        user: UsersEntity,
        permitType: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(
            permitType,
            status
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findReportAllRenewedPermits(
        user: UsersEntity,
        permitType: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPermitTypeAndOldPermitStatusIsNotNullAndPermitAwardStatus(
            permitType,
            status
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findReportAllDejectedPermits(
        user: UsersEntity,
        permitType: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPermitTypeAndPermitAwardStatusIsNull(
            permitType
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findReportAllSamplesSubmitted(
        user: UsersEntity,
        permitType: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<SampleSubmissionDTO> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        SampleSubmissionRepo.findSamplesSubmitted(
            permitType
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun listPermitsReports(
        permits: List<PermitApplicationsEntity>,
        map: ServiceMapsEntity
    ): List<ReportPermitEntityDto> {
        return permits.map { p ->
            ReportPermitEntityDto(
                p.id,
                p.attachedPlantId?.let {
                    commonDaoServices.findCompanyProfileWithID(
                        findPlantDetails(it).companyProfileId ?: -1L
                    ).name
                },
                p.permitRefNumber,
                p.commodityDescription,
                p.tradeMark,
                p.awardedPermitNumber,
                p.dateOfIssue,
                p.dateOfExpiry,
                p.permitStatus?.let { findPermitStatus(it).processStatusName },
                p.userId,
                p.createdOn,
                p.attachedPlantId?.let {
                    commonDaoServices.findCountiesEntityByCountyId(
                        findPlantDetails(it).county ?: -1L, map.activeStatus
                    ).county
                },
                p.attachedPlantId?.let {
                    commonDaoServices.findTownEntityByTownId(
                        findPlantDetails(it).town ?: -1L
                    ).town
                },
                p.attachedPlantId?.let {
                    commonDaoServices.findRegionEntityByRegionID(
                        findPlantDetails(it).region ?: -1L, map.activeStatus
                    ).region
                },
                p.divisionId?.let { commonDaoServices.findDivisionWIthId(it).division },
                p.sectionId?.let { commonDaoServices.findSectionWIthId(it).section },
                p.permitAwardStatus == 1,
                p.permitExpiredStatus == 1,
                p.userTaskId,
                p.companyId,
                p.permitType,
                p.permitStatus,
                p.versionNumber,
                jasyptStringEncryptor.encrypt(p.id.toString()),
                jasyptStringEncryptor.encrypt(p.userId.toString()),
                p.attachedPlantId?.let { findPlantDetails(it) }?.companyProfileId?.let {
                    commonDaoServices.findCompanyProfileWithID(
                        it
                    )
                }?.firmCategory,
                p.attachedPlantId?.let { findPlantDetails(it) }?.companyProfileId?.let {
                    commonDaoServices.findCompanyProfileWithID(
                        it
                    )
                }?.firmCategory?.let { findFirmTypeById(it).firmType },
                p.id?.let { invoiceMasterDetailsRepo.findByPermitIdAndVarField10IsNull(it)?.totalAmount },
                standardNumber = p.productStandard?.let { findStandardsByID(it).standardNumber },
                standardTitle = p.productStandard?.let { findStandardsByID(it).standardTitle },
                physicalAddress = p.attachedPlantId?.let { findPlantDetails(it) }?.physicalAddress,
                telephoneNo = p.attachedPlantId?.let { findPlantDetails(it) }?.telephone,
                email = p.attachedPlantId?.let { findPlantDetails(it) }?.emailAddress,
                pscApprovalDate = p.id?.let { findPermitPscDate(it).createdOn },
                p.inspectionDate
            )
        }

    }


    fun findPermitPscDate(permitId: Long): QaRemarksEntity {
        return if (remarksEntityRepo.findFirstByPermitIdAndProcessByAndRemarksStatus(permitId, "PSC", 1) != null) {
            remarksEntityRepo.findFirstByPermitIdAndProcessByAndRemarksStatus(permitId, "PSC", 1)!!
        } else {
            QaRemarksEntity()
        }

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun filterAllApplicationsReports(
        startDate: Date?,
        endDate: Date?,
        regionID: Long?,
        sectionId: Long?,
        statusId: Long?,
        officerId: Long?,
        firmCategoryId: Long?,
        permitType: Long?,
        productDescription: String?

    ): List<PermitApplicationsEntity> {

        permitRepo.findFilteredPermits(
            startDate, endDate, regionID, sectionId, statusId, officerId, firmCategoryId, permitType, productDescription
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found")
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun filterFindFilteredAwardedPermits(
        startDate: Date?,
        endDate: Date?,
        regionID: Long?,
        sectionId: Long?,
        statusId: Long?,
        officerId: Long?,
        firmCategoryId: Long?,
        permitType: Long?,
        productDescription: String?

    ): List<PermitApplicationsEntity> {

        permitRepo.findFilteredAwardedPermits(
            startDate, endDate, regionID, sectionId, statusId, officerId, firmCategoryId, permitType, productDescription
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found")
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun filterFindFilteredRenewedPermits(
        startDate: Date?,
        endDate: Date?,
        regionID: Long?,
        sectionId: Long?,
        statusId: Long?,
        officerId: Long?,
        firmCategoryId: Long?,
        permitType: Long?,
        productDescription: String?

    ): List<PermitApplicationsEntity> {

        permitRepo.findFilteredRenewedPermits(
            startDate, endDate, regionID, sectionId, statusId, officerId, firmCategoryId, permitType, productDescription
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found")
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun filterFindFilteredDejectedPermits(
        startDate: Date?,
        endDate: Date?,
        regionID: Long?,
        sectionId: Long?,
        statusId: Long?,
        officerId: Long?,
        firmCategoryId: Long?,
        permitType: Long?,
        productDescription: String?

    ): List<PermitApplicationsEntity> {

        permitRepo.findFilteredDejectedPermits(
            startDate, endDate, regionID, sectionId, statusId, officerId, firmCategoryId, permitType, productDescription
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found")
    }

    fun findReportAllAwardedPermitsKebsWebsite(
        permitType: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<PermitApplicationsEntity> {
        permitRepo.findByPermitTypeAndOldPermitStatusIsNullAndPermitAwardStatus(
            permitType,
            status
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")
    }

    fun findByCompanyIdAllAwardedPermitsKebsWebsite(
        companyID: Long,
        status: Int,
        fmarkGeneratedStatus: Int
    ): List<PermitApplicationsEntity> {
        permitRepo.findByCompanyIdAndOldPermitStatusIsNullAndPermitAwardStatus(
            companyID,
            status
        )
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")
    }

    fun listPermitsWebsite(
        permits: List<PermitApplicationsEntity>,
        map: ServiceMapsEntity
    ): List<KebsWebistePermitEntityDto> {
        return permits.map { p ->
            KebsWebistePermitEntityDto(
                if (p.firmName == null) {
                    p.attachedPlantId?.let {
                        commonDaoServices.findCompanyProfileWithID(
                            findPlantDetailsB(it)?.companyProfileId ?: -1L
                        ).name
                    }
                } else {
                    p.firmName
                },
                if (p.physicalAddress == null) {
                    p.attachedPlantId?.let {
                        commonDaoServices.findCompanyProfileWithID(
                            findPlantDetailsB(it)?.companyProfileId ?: -1L
                        ).physicalAddress
                    }
                } else {
                    p.physicalAddress
                }, p.awardedPermitNumber,
                p.productName,
                p.tradeMark,
                p.ksNumber,
                p.commodityDescription,
                p.effectiveDate.toString(),
                p.dateOfExpiry.toString()

            )
        }
    }

    fun listFirmsWebsite(
        company: List<CompanyProfileEntity>,
        map: ServiceMapsEntity
    ): List<companyDto> {
        return company.map { p ->
            companyDto(
                p.name

            )
        }
    }

    fun listFirmsWebsiteNotMigratedDmark(
        company: List<PermitMigrationApplicationsEntityDmark>,
        map: ServiceMapsEntity
    ): List<companyDto> {
        return company.map { p ->
            companyDto(
                p.companyName

            )
        }
    }

    fun listFirmsWebsiteNotMigratedSmark(
        company: List<PermitMigrationApplicationsEntity>,
        map: ServiceMapsEntity
    ): List<companyDto> {
        return company.map { p ->
            companyDto(
                p.companyName

            )
        }
    }

    fun listFirmsWebsiteNotMigratedFmark(
        company: List<PermitMigrationApplicationsEntityFmark>,
        map: ServiceMapsEntity
    ): List<companyDto> {
        return company.map { p ->
            companyDto(
                p.companyName

            )
        }
    }

    fun listPermitsNotMigratedWebsite(
        permits: List<PermitMigrationApplicationsEntity>,
        map: ServiceMapsEntity
    ): List<KebsWebistePermitEntityDto> {
        return permits.map { p ->
            KebsWebistePermitEntityDto(
                p.companyName,
                p.physicalAddress,
                p.permitNumber,
                p.productName,
                p.tradeMark,
                p.ksNumber,
                p.commodityDescription,
                p.effectiveDate.toString(),
                p.dateOfExpiry.toString()

            )
        }
    }

    fun listPermitsNotMigratedWebsiteFmark(
        permits: List<PermitMigrationApplicationsEntityFmark>,
        map: ServiceMapsEntity
    ): List<KebsWebistePermitEntityDto> {
        return permits.map { p ->
            KebsWebistePermitEntityDto(
                p.companyName,
                p.physicalAddress,
                p.permitNumber,
                p.productName,
                p.tradeMark,
                p.ksNumber,
                p.commodityDescription,
                p.effectiveDate.toString(),
                p.dateOfExpiry.toString()

            )
        }
    }

    fun listPermitsNotMigratedWebsiteDmark(
        permits: List<PermitMigrationApplicationsEntityDmark>,
        map: ServiceMapsEntity
    ): List<KebsWebistePermitEntityDto> {
        return permits.map { p ->
            KebsWebistePermitEntityDto(
                p.companyName,
                p.physicalAddress,
                p.permitNumber,
                p.productName,
                p.tradeMark,
                p.ksNumber,
                p.commodityDescription,
                p.dateOfIssue.toString(),
                p.dateOfExpiry.toString()

            )
        }
    }

    fun findPermitByPermitNumber(
        awardedPermitNumber: String
    ): List<PermitApplicationsEntity> {


        permitRepo.findByAwardedPermitNumber(awardedPermitNumber)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findPermitByPermitNumberNotMigrated(
        awardedPermitNumber: String
    ): List<PermitMigrationApplicationsEntity> {

        permitMigratedRepo.findByPermitNumberOrderByDateOfExpiryDesc(awardedPermitNumber)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findSmarkPermitsNotMigrated(
    ): List<PermitMigrationApplicationsEntity> {

        permitMigratedRepo.findAllByMigratedStatusIsNull(PageRequest.of(0, 10000))
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findSmarkPermitsNotMigratedByCompanyName(
        companyName: String
    ): List<PermitMigrationApplicationsEntity> {

        permitMigratedRepo.findAllByCompanyName(companyName)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findPermitByPermitNumberNotMigratedDmark(
        awardedPermitNumber: String
    ): List<PermitMigrationApplicationsEntityDmark> {

        permitMigratedRepoDmark.findByPermitNumberOrderByDateOfExpiryDesc(awardedPermitNumber)
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("No Permit Found ")
    }

    fun findDmarkPermitsNotMigratedByCompanyName(
        companyName: String
    ): List<PermitMigrationApplicationsEntityDmark> {

        permitMigratedRepoDmark.findAllByCompanyName(companyName)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findPermitByPermitNumberNotMigratedFmark(
        awardedPermitNumber: String
    ): List<PermitMigrationApplicationsEntityFmark> {

        permitMigratedRepoFmark.findByPermitNumberOrderByDateOfExpiryDesc(awardedPermitNumber)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findFmarkPermitsNotMigratedByCompanyName(
        companyName: String
    ): List<PermitMigrationApplicationsEntityFmark> {

        permitMigratedRepoFmark.findAllByCompanyName(companyName)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findFmarkPermitsNotMigrated(
    ): List<PermitMigrationApplicationsEntityFmark> {

        permitMigratedRepoFmark.findAllByMigratedStatusIsNull(PageRequest.of(0, 10000))
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findDmarkPermitsNotMigrated(
    ): List<PermitMigrationApplicationsEntityDmark> {

        permitMigratedRepoDmark.findAllByMigratedStatusIsNull(PageRequest.of(0, 10000))
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findCompaniesNotMigratedDmark(
    ): List<PermitMigrationApplicationsEntityDmark> {

        permitMigratedRepoDmark.getallCompanies()
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findCompaniesNotMigratedSmark(
    ): List<PermitMigrationApplicationsEntity> {

        permitMigratedRepo.getallCompanies()
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }

    fun findCompaniesNotMigratedFmark(
    ): List<PermitMigrationApplicationsEntityFmark> {

        permitMigratedRepoFmark.getallCompanies()
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found ")


    }


}
