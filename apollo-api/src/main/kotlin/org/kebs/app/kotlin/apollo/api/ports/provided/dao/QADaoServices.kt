package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.apache.commons.lang3.SerializationUtils
import org.kebs.app.kotlin.apollo.api.controllers.msControllers.MSReportsControllers
import org.kebs.app.kotlin.apollo.api.controllers.qaControllers.ReportsController
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
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
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ICfgMoneyTypeCodesRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
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
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors


@Service
class QADaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val productsRepo: IProductsRepository,
//    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
    private val workPlanCreatedRepo: IQaWorkplanRepository,
    private val iTurnOverRatesRepository: ITurnOverRatesRepository,
    private val iManufacturePaymentDetailsRepository: IManufacturerPaymentDetailsRepository,
    private val sampleStandardsRepo: ISampleStandardsRepository,
    private val invoiceDaoService: InvoiceDaoService,
    private val paymentUnitsRepository: ICfgKebsPermitPaymentUnitsRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val qaInspectionOPCRepo: IQaInspectionOpcEntityRepository,
    private val qaPersonnelInchargeRepo: IQaPersonnelInchargeEntityRepository,
    private val qaInspectionTechnicalRepo: IQaInspectionTechnicalRepository,
    private val qaInspectionReportRecommendationRepo: IQaInspectionReportRecommendationRepository,
    private val qaInspectionHaccpImplementationRepo: IQaInspectionHaccpImplementationRepository,
    private val permitRepo: IPermitApplicationsRepository,
    private val permitUpdateDetailsRequestsRepo: IPermitUpdateDetailsRequestsRepository,
    private val userRequestsRepo: IUserRequestTypesRepository,
    private val SampleCollectionRepo: IQaSampleCollectionRepository,
    private val SampleSubmissionRepo: IQaSampleSubmissionRepository,
    private val sampleLabTestResultsRepo: IQaSampleLabTestResultsRepository,
    private val sampleLabTestParametersRepo: IQaSampleLabTestParametersRepository,
    private val schemeForSupervisionRepo: IQaSchemeForSupervisionRepository,
    private val sta3Repo: IQaSta3EntityRepository,
    private val smarkFmarkRepo: IQaSmarkFmarkRepository,
    private val invoiceRepository: IInvoiceRepository,
    private val invoiceBatchRepo: IQaBatchInvoiceRepository,
    private val sta10Repo: IQaSta10EntityRepository,
    private val productsManufactureSTA10Repo: IQaProductBrandEntityRepository,
    private val rawMaterialsSTA10Repo: IQaRawMaterialRepository,
    private val machinePlantsSTA10Repo: IQaMachineryRepository,
    private val qaUploadsRepo: IQaUploadsRepository,
    private val manufacturingProcessSTA10Repo: IQaManufactureProcessRepository,
    private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
    private val permitTypesRepo: IPermitTypesEntityRepository,
    private val processStatusRepo: IQaProcessStatusRepository,
    private val iMoneyTypeCodesRepo: ICfgMoneyTypeCodesRepository,
    private val mpesaServices: MPesaService,
    private val msReportsControllers: MSReportsControllers,
    private val reportsDaoService: ReportsDaoService,
//    private val reportsControllers: ReportsController,
    private val notifications: Notifications,
) {


    @Lazy
    @Autowired
    lateinit var qualityAssuranceBpmn: QualityAssuranceBpmn

    @Lazy
    @Autowired
    lateinit var reportsControllers: ReportsController


    final var appId = applicationMapProperties.mapQualityAssurance

    val permitList = "redirect:/api/qa/permits-list?permitTypeID"
    val permitDetails = "redirect:/api/qa/permit-details?permitID"
    val sta10Details = "redirect:/api/qa/view-sta10?permitID"
    val batchInvoiceList = "redirect:/api/qa/invoice/list-batch-invoices"

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

    fun findALlCreatedWorkPlanWIthOfficerID(officerID: Long): List<QaWorkplanEntity> {
        workPlanCreatedRepo.findByOfficerId(officerID)
            ?.let { createdWorkPlan ->
                return createdWorkPlan
            }
            ?: throw ExpectedDataNotFound("Created Work Plan with the following [USER ID = ${officerID}], does not Exist")
    }

    fun findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(userID: Long, status: Int): List<InvoiceEntity> {
        invoiceRepository.findAllByUserIdAndPaymentStatusAndBatchInvoiceNoIsNull(userID, status)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userID}] and [status = ${status}], does not Exist")
    }

    fun findALlInvoicesCreatedByUser(userID: Long): List<InvoiceEntity> {
        invoiceRepository.findAllByManufacturer(userID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [USER ID = ${userID}], does not Exist")
    }

    fun findALlInvoicesPermitWithBatchID(batchID: Long): List<InvoiceEntity> {
        invoiceRepository.findAllByBatchInvoiceNo(batchID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [BATCH ID = ${batchID}], does not Exist")
    }

    fun findALlBatchInvoicesWithUserID(userID: Long): List<QaBatchInvoiceEntity> {
        invoiceBatchRepo.findByUserId(userID)
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

    fun findBatchInvoicesWithID(batchID: Long): QaBatchInvoiceEntity {
        invoiceBatchRepo.findByIdOrNull(batchID)
            ?.let { it ->
                return it
            }
            ?: throw ExpectedDataNotFound("Invoices With [batch ID = ${batchID}], does not Exist")
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
        permitRepo.findByUserId(userId)?.let { permitList ->
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

    fun findAllUserPermitWithPermitTypeAwardedStatusIsNull(
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

    fun findAllQAOPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByQaoIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
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

    fun findAllQaInspectionOPCWithPermitRefNumber(permitRefNumber: String): List<QaInspectionOpcEntity> {
        qaInspectionOPCRepo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No inspection details for OPC with PERMIT REF NO =$permitRefNumber")
    }

    fun findPermitBYID(id: Long): PermitApplicationsEntity {
        permitRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit found with the following [ID=$id]")
    }

    fun findPermitBYIDAndBranchID(id: Long, plantID: Long): PermitApplicationsEntity {
        permitRepo.findByIdAndAttachedPlantId(id, plantID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit found with the following [ID=$id] and [PLANT ID = $plantID]")
    }


    fun findSampleSubmittedBYPermitRefNumber(permitRefNumber: String): QaSampleSubmissionEntity {
        SampleSubmissionRepo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No sample submission found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findSampleSubmittedBYID(ssfID: Long): QaSampleSubmissionEntity {
        SampleSubmissionRepo.findByIdOrNull(ssfID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample submission found with the following ID number=$ssfID]")
    }

    fun findSampleSubmittedListBYPermitRefNumber(permitRefNumber: String, status: Int): List<QaSampleSubmissionEntity> {
        SampleSubmissionRepo.findByPermitRefNumberAndStatus(permitRefNumber, status)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No sample submission found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findSampleSubmittedBYBsNumber(bsNumber: String): QaSampleSubmissionEntity {
        SampleSubmissionRepo.findByBsNumber(bsNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample submission found with the following [bsNumber=$bsNumber]")
    }

    fun findQaInspectionHaccpImplementationBYPermitRefNumber(permitRefNumber: String): QaInspectionHaccpImplementationEntity {
        qaInspectionHaccpImplementationRepo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Haccp Implementation found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findQaInspectionReportRecommendationBYPermitRefNumber(permitRefNumber: String): QaInspectionReportRecommendationEntity {
        qaInspectionReportRecommendationRepo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Inspection Report Recommendation found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findQaInspectionOpcBYPermitRefNumber(permitRefNumber: String): List<QaInspectionOpcEntity> {
        qaInspectionOPCRepo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Inspection OPC found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findQaInspectionTechnicalBYPermitRefNumber(permitRefNumber: String): QaInspectionTechnicalEntity {
        qaInspectionTechnicalRepo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Inspection Technical found with the following [PERMIT REF NO =$permitRefNumber]")
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

    fun findAllRequestByPermitRefNumber(permitRefNumber: String): List<PermitUpdateDetailsRequestsEntity> {
        permitUpdateDetailsRequestsRepo.findByPermitRefNumber(permitRefNumber)?.let {
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
                        userAssigned = assignNextOfficerBasedOnSection(
                            permit,
                            map,
                            applicationMapProperties.mapQADesignationIDForHODId
                        )
                        permit.userTaskId = applicationMapProperties.mapUserTaskNameHOD
                        permit.hodId = userAssigned.id
                        sendEmailWithTaskDetails(
                            userAssigned.email ?: throw ExpectedDataNotFound("Missing Email address"),
                            permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
                        )
                        qualityAssuranceBpmn.startQAAppReviewProcess(
                            permit.id ?: throw ExpectedDataNotFound("Permit Id Not found"),
                            permit.hodId ?: throw ExpectedDataNotFound("HOD Not found")
                        )
                    }
                    applicationMapProperties.mapQAPermitTypeIdSmark -> {
                        userAssigned = assignNextOfficerBasedOnSection(
                            permit,
                            map,
                            applicationMapProperties.mapQADesignationIDForQAMId
                        )
                        permit.userTaskId = applicationMapProperties.mapUserTaskNameQAM
                        permit.qamId = userAssigned.id
                        sendEmailWithTaskDetails(
                            userAssigned.email ?: throw ExpectedDataNotFound("Missing Email address"),
                            permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
                        )
                        qualityAssuranceBpmn.startQAAppReviewProcess(
                            permit.id ?: throw ExpectedDataNotFound("Permit Id Not found"),
                            permit.qamId ?: throw ExpectedDataNotFound("QAM Not found")
                        )
                    }
                    applicationMapProperties.mapQAPermitTypeIdFmark -> {
                        userAssigned = assignNextOfficerBasedOnSection(
                            permit,
                            map,
                            applicationMapProperties.mapQADesignationIDForQAMId
                        )
                        permit.userTaskId = applicationMapProperties.mapUserTaskNameQAM
                        permit.qamId = userAssigned.id
                        sendEmailWithTaskDetails(
                            userAssigned.email ?: throw ExpectedDataNotFound("Missing Email address"),
                            permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
                        )
                        qualityAssuranceBpmn.startQAAppReviewProcess(
                            permit.id ?: throw ExpectedDataNotFound("Permit Id Not found"),
                            permit.qamId ?: throw ExpectedDataNotFound("QAM Not found")
                        )
                    }
                }

                permit.paidStatus = map.initStatus
                permit.permitStatus = applicationMapProperties.mapQaStatusPApprovalCompletness
                permitUpdateDetails(permit, map, userDetails)

                val batchInvoice = findPermitInvoiceByPermitRefNumber(
                    permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER"),
                    permit.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
                )
                sendEmailWithProformaPaid(
                    userDetails.email ?: throw ExpectedDataNotFound("MISSING USER ID"),
                    invoiceCreationPDF(
                        batchInvoice.id ?: throw ExpectedDataNotFound("MISSING BATCH INVOICE ID"),
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

    fun findPermitInvoiceByPermitRefNumber(permitRefNumber: String, userId: Long): InvoiceEntity {
        invoiceRepository.findByPermitRefNumberAndUserId(permitRefNumber, userId)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No Invoice found with the following [PERMIT REF NO =${permitRefNumber}  and LoggedIn User]")
    }

    fun findSTA3WithPermitRefNumber(permitRefNumber: String): QaSta3Entity {
        sta3Repo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA3 found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllPlantDetails(userId: Long): List<ManufacturePlantDetailsEntity> {
        manufacturePlantRepository.findByUserId(userId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [user id=$userId]")
    }

    fun findAllPlantDetailsWithCompanyID(companyID: Long): List<ManufacturePlantDetailsEntity> {
        manufacturePlantRepository.findByCompanyProfileId(companyID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [COMPANY ID =$companyID]")
    }


    fun findPlantDetails(plantID: Long): ManufacturePlantDetailsEntity {
        manufacturePlantRepository.findByIdOrNull(plantID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [id=$plantID]")
    }


    fun findSTA10WithPermitRefNumberBY(permitRefNumber: String): QaSta10Entity {
        sta10Repo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA10 found with the following [PERMIT REF NO =$permitRefNumber]")
    }

    fun findSchemeOfSupervisionWithPermitRefNumberBY(permitRefNumber: String): QaSchemeForSupervisionEntity {
        schemeForSupervisionRepo.findByPermitRefNumber(permitRefNumber)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No SCHEME OF SUPERVISION found with the following [PERMIT REF NO =$permitRefNumber]")
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
    fun findAllUploadedFileBYPermitRefNumberAndOrdinarStatus(permitRefNumber: String, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndOrdinaryStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndCocStatus(permitRefNumber: String, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndCocStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndAssessmentReportStatus(permitRefNumber: String, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndAssessmentReportStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndSscStatus(permitRefNumber: String, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndSscStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndInspectionReportStatus(permitRefNumber: String, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitRefNumberAndInspectionReportStatus(permitRefNumber, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ PERMIT REF NO =$permitRefNumber]")
    }

    fun findAllUploadedFileBYPermitRefNumberAndSta10Status(permitRefNumber: String, status: Int): List<QaUploadsEntity> {
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
        return permitRepo.findByPermitRefNumberAndOldPermitStatus(permitRefNumber, 1)
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
            commonDaoServices.companyDirectorList(companyProfile.id ?: throw Exception("INVALID COMPANY ID DETAILS"))
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
            null

        )
    }

    fun listPermits(permits: List<PermitApplicationsEntity>, map: ServiceMapsEntity): List<PermitEntityDto> {
        return permits.map { p ->
            PermitEntityDto(
                p.id,
                p.firmName,
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
                p.permitStatus
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
        permitInvoices: List<InvoiceEntity>,
        plantID: Long?,
        map: ServiceMapsEntity
    ): List<PermitInvoiceDto> {
        val permitsInvoiceList = mutableListOf<PermitInvoiceDto>()
        permitInvoices.map { pi ->
            val permitDetails = findPermitBYID(pi.permitId ?: throw Exception("Invalid Permit ID"))
            if (plantID != null) {
                if (permitDetails.attachedPlantId == plantID) {
                    permitsInvoiceList.add(
                        PermitInvoiceDto(
                            pi.permitId,
                            pi.invoiceNumber,
                            permitDetails.commodityDescription,
                            permitDetails.tradeMark,
                            pi.amount,
                            pi.paymentStatus,
                            pi.permitRefNumber
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
                        pi.invoiceNumber,
                        permitDetails.commodityDescription,
                        permitDetails.tradeMark,
                        pi.amount,
                        pi.paymentStatus,
                        pi.permitRefNumber
                    )
                )
            }

        }
        return permitsInvoiceList.sortedBy { it.permitID }
    }


    fun permitsInvoiceDTO(
        permitInvoices: InvoiceEntity,
        permitDetails: PermitApplicationsEntity,
    ): PermitInvoiceDto {
        return PermitInvoiceDto(
            permitInvoices.permitId,
            permitInvoices.invoiceNumber,
            permitDetails.commodityDescription,
            permitDetails.tradeMark,
            permitInvoices.amount,
            permitInvoices.paymentStatus,
            permitInvoices.permitRefNumber
        )
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
            commodityDescription = permit.commodityDescription
            brandName = permit.tradeMark

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
    ): AllSTA10DetailsDto {
        return AllSTA10DetailsDto(
            sta10FirmDetails,
            sta10PersonnelDetails,
            sta10ProductsManufactureDetails,
            sta10RawMaterialsDetails,
            sta10MachineryAndPlantDetails,
            sta10ManufacturingProcessDetails,
        )
    }


    fun findOfficersList(
        plantID: Long,
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        designationID: Long
    ): List<UserProfilesEntity> {
        val plantAttached = findPlantDetails(plantID)
        val region = plantAttached.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Plant attached Region Id is Empty, check config")
        val department = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)
        val designation = commonDaoServices.findDesignationByID(designationID)
        val section = commonDaoServices.findSectionWIthId(
            permit.sectionId ?: throw ExpectedDataNotFound("SECTION VALUE IS MISSING")
        )

        //return commonDaoServices.findAllUsersWithinRegionDepartmentDivisionSectionId(region, department, division, section, map.activeStatus)
        return commonDaoServices.findAllUsersWithDesignationRegionDepartmentSectionAndStatus(
            designation,
            region,
            section,
            department,
            map.activeStatus
        )
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
    ):List<UserProfilesEntity> {
        val designation = commonDaoServices.findDesignationByID(designationID)
        return commonDaoServices.findAllUsersProfileWithDesignationAndStatus(designation, map.activeStatus)
    }

    fun findAllPcmOfficers(): List<UsersEntity> {
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

    fun permitSave(
        permits: PermitApplicationsEntity,
        permitTypeDetails: PermitTypesEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var savePermit = permits
        try {

            with(savePermit) {
                userId = user.id
                productName = permits.commodityDescription
                permitType = permitTypeDetails.id
                permitRefNumber = "REF${permitTypeDetails.markNumber}${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
                enabled = map.initStatus
                divisionId = commonDaoServices.findSectionWIthId(sectionId ?: throw ExpectedDataNotFound("SECTION ID IS MISSING")).divisionId?.id
                versionNumber = 1
                endOfProductionStatus = map.initStatus
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
                resultsAnalysis = ssfDetails.resultsAnalysis
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }


            saveSSF = SampleSubmissionRepo.save(saveSSF)

            val permitDetails = findPermitWithPermitRefNumberLatest(
                saveSSF.permitRefNumber ?: throw Exception("MISSING permit Ref Number")
            )

            permitDetails.compliantStatus = saveSSF.resultsAnalysis
            var complianceValue: String? = null
            when (permitDetails.compliantStatus) {
                map.activeStatus -> {
                    complianceValue = "COMPLIANT"
                    if (permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIDDmark) {
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
                    permitDetails.userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                    permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPendingCorrectionManf, user)
                }
            }

            val fileUploaded = findUploadedFileBYId(
                saveSSF.labReportFileId ?: throw ExpectedDataNotFound("MISSING LAB REPORT FILE ID STATUS")
            )
            val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
            sendComplianceStatusAndLabReport(
                permitDetails,
                complianceValue ?: throw ExpectedDataNotFound("MISSING COMPLIANCE STATUS"),
                saveSSF.complianceRemarks ?: throw ExpectedDataNotFound("MISSING COMPLIANCE REMARKS"),
                String(mappedFileClass.document ?: throw ExpectedDataNotFound("MISSING BYTE ARRAY FILE"))
            )


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
        permitNewRefNumber: String,
        qaSta3Details: QaSta3Entity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaSta3Entity {

        var sta3Upadted: QaSta3Entity? = null
        sta3Repo.findByPermitRefNumber(permitNewRefNumber)?.let {
            sta3Upadted = sta3Update(qaSta3Details, map, user)
        } ?: kotlin.run {
            with(qaSta3Details) {
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
        docFile: File,
        doc: String,
        permitRefNUMBER: String,
        user: UsersEntity
    ): QaUploadsEntity {

        with(uploads) {
            ordinaryStatus = 1
            name = docFile.name
            fileType = docFile.extension
            documentType = doc
            document = docFile.readBytes()
            permitRefNumber = permitRefNUMBER
            transactionDate = commonDaoServices.getCurrentDate()
            versionNumber = 1
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
        var savePermit: PermitApplicationsEntity? = null
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

            savePermit = SerializationUtils.clone(oldPermit)

            with(savePermit) {
                id = null
                oldPermitStatus = null
                versionNumber = versionNumberOld.plus(1)
            }

            savePermit = permitRepo.save(savePermit)

            sr.payload = "Permit Renewed Updated [updatePermit= ${savePermit.id}]"
            sr.names = "${savePermit.permitRefNumber}} ${savePermit.userId}"
            sr.varField1 = "${savePermit.id}"

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
        return Pair(sr, savePermit ?: throw ExpectedDataNotFound("MISSING SAVED PERMIT"))
    }

    fun permitUpdateNewWithSamePermitNumber(
        permitID: Long,
        s: ServiceMapsEntity,
        user: UsersEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var savePermit = PermitApplicationsEntity()
        try {
            val pm = findPermitBYID(permitID)
            var oldPermit =
                findPermitWithPermitRefNumberLatest(pm.permitRefNumber ?: throw Exception("INVALID PERMIT NUMBER"))
            KotlinLogging.logger { }
                .info { "::::::::::::::::::PERMIT With PERMIT NUMBER = ${pm.permitRefNumber}, DOES Exists::::::::::::::::::::: " }
            val versionNumberOld =
                oldPermit.versionNumber ?: throw ExpectedDataNotFound("Permit Version Number is Empty")

            oldPermit.oldPermitStatus = 1
//            oldPermit.renewalStatus = s.activeStatus
            //update last previous version permit old status
            oldPermit = permitUpdateDetails(oldPermit, s, user).second

            val permitTypeDetails = findPermitType(oldPermit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))

            with(savePermit) {
                renewalStatus = s.activeStatus
                userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                userId = user.id
                attachedPlantId = oldPermit.attachedPlantId
                permitType = oldPermit.permitType
                permitRefNumber = oldPermit.permitRefNumber
                enabled = s.initStatus
                versionNumber = versionNumberOld.plus(1)
                endOfProductionStatus = s.initStatus
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
            }
            savePermit = permitRepo.save(savePermit)

            when (oldPermit.permitType) {
                applicationMapProperties.mapQAPermitTypeIdSmark -> {
                    val sta10 = findSTA10WithPermitRefNumberBY(oldPermit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"))
                    var newSta10 = QaSta10Entity()
                    newSta10 = commonDaoServices.updateDetails(sta10, newSta10) as QaSta10Entity
                    newSta10.id = null
                    sta10NewSave(savePermit, newSta10, user, s)
                }
                applicationMapProperties.mapQAPermitTypeIDDmark -> {
                    val sta3 = findSTA3WithPermitRefNumber(oldPermit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"))
                    var newSta3 = QaSta3Entity()
                    newSta3 = commonDaoServices.updateDetails(sta3, newSta3) as QaSta3Entity
                    newSta3.id = null
                    sta3NewSave(savePermit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), newSta3, user, s)
                }
            }


            sr.payload = "Permit Renewed Updated [updatePermit= ${savePermit.id}]"
            sr.names = "${savePermit.permitRefNumber}} ${savePermit.userId}"
            sr.varField1 = "${savePermit.id}"

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
        return Pair(sr, savePermit)
    }

    fun getFileInvoicePDFForm(batchID: Long): File {
        val myDetails = reportsControllers.createInvoicePdf(batchID)
        reportsDaoService.generateEmailPDFReportWithDataSource(
            "Proforma-Invoice-${myDetails.first.getValue("demandNoteNo")}.pdf",
            myDetails.first,
            applicationMapProperties.mapReportProfomaInvoiceWithItemsPath,
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

        val upload = QaUploadsEntity()
        uploadQaFile(
            upload,
            invoicePDFCreated ?: throw ExpectedDataNotFound("MISSING FILE"),
            "PERMIT INVOICE",
            myDetails.first.getValue("demandNoteNo").toString(),
            loggedInUser
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

    fun sendEmailWithSSC(recipient: String, attachment: String?, permitRefNumber: String): Boolean {
        val subject = "SSC GENERATED"
        val messageBody =
            "Check The attached SCHEME OF SUPERVISION AND CONTROL for permit with Ref number $permitRefNumber" +
                    "and Approve or reject it on KIMS system for your application to continue being processesd"

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
        return invoiceBatchRepo.save(invoice)
    }


    fun pcmGenerateInvoice(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permit: PermitApplicationsEntity,
        permitTypeID: Long
    ) {
        val permitType = findPermitType(permitTypeID)
        val permitUser =
            commonDaoServices.findUserByID(permit.userId ?: throw ExpectedDataNotFound("Permit USER Id Not found"))
        permitInvoiceCalculation(s, permitUser, permit, permitType)
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
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val permitFound = findPermitBYID(permitID)
            var inspectionTechnicalDetails = inspectionTechnical

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

                    with(inspectionTechnicalDetails) {
                        permitId = permitFound.id
                        permitRefNumber = permitFound.permitRefNumber
                        status = s.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    inspectionTechnicalDetails = qaInspectionTechnicalRepo.save(inspectionTechnicalDetails)

                    var qaInspectionReportRecommendation = QaInspectionReportRecommendationEntity()
                    with(qaInspectionReportRecommendation) {
                        permitId = permitFound.id
                        permitRefNumber = permitFound.permitRefNumber
                        filledQpsmsStatus = s.activeStatus
                        status = s.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    qaInspectionReportRecommendation =
                        qaInspectionReportRecommendationRepo.save(qaInspectionReportRecommendation)
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
        return sr
    }

    fun permitAddNewInspectionReportDetailsHaccp(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permitID: Long,
        haccpImplementation: QaInspectionHaccpImplementationEntity,
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
                        permitId = permitFound.id
                        permitRefNumber = permitFound.permitRefNumber
                        status = s.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    haccpAddedDetails = qaInspectionHaccpImplementationRepo.save(haccpAddedDetails)

                    var qaInspectionReportRecommendation = findQaInspectionReportRecommendationBYPermitRefNumber(
                        permitFound.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER FOUND")
                    )
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
        map: ServiceMapsEntity,
        loggedInUser: UsersEntity
    ): Pair<QaBatchInvoiceEntity, PermitApplicationsEntity> {
        //Consolidate invoice now first
        var permit = findPermitBYID(permitID)
        val newBatchInvoiceDto = NewBatchInvoiceDto()
        with(newBatchInvoiceDto) {
            permitInvoicesID = arrayOf(permit.permitRefNumber.toString())
        }
        var batchInvoice = permitMultipleInvoiceCalculation(map, loggedInUser, newBatchInvoiceDto).second

        // submit invoice to get way
        with(newBatchInvoiceDto) {
            batchID = batchInvoice.id!!
        }

        batchInvoice = permitMultipleInvoiceSubmitInvoice(map, loggedInUser, newBatchInvoiceDto).second

        //Update Permit Details
        with(permit) {
            sendApplication = map.activeStatus
            invoiceGenerated = map.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPPayment
        }
        permit = permitUpdateDetails(permit, map, loggedInUser).second

        //Send email with attached Invoice details
        sendEmailWithProforma(
            commonDaoServices.findUserByID(permit.userId ?: throw ExpectedDataNotFound("MISSING USER ID")).email
                ?: throw ExpectedDataNotFound("MISSING USER ID"),
            invoiceCreationPDF(
                batchInvoice.id ?: throw ExpectedDataNotFound("MISSING BATCH INVOICE ID"),
                loggedInUser
            ).path,
            permit.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
        )

        return Pair(batchInvoice, permit)
    }

    fun permitAddNewInspectionReportDetailsOPC(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permitID: Long,
        opc: QaInspectionOpcEntity,
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
                        permitId = permitFound.id
                        permitRefNumber = permitFound.permitRefNumber
                        status = s.activeStatus
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    opcAddedDetails = qaInspectionOPCRepo.save(opcAddedDetails)

                    var qaInspectionReportRecommendation = findQaInspectionReportRecommendationBYPermitRefNumber(
                        permitFound.permitRefNumber ?: throw Exception("INVALID PERMIT ID FOUND")
                    )
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
    ): Pair<ServiceRequestsEntity, QaBatchInvoiceEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var invoiceBatchDetails: QaBatchInvoiceEntity? = null
        try {

            var batchID = batchInvoiceDto.batchID
            batchInvoiceDto.permitInvoicesID
                ?.forEach { permitRefNumber ->
                    val userID = user.id ?: throw Exception("INVALID USER ID")
                    var permitInvoiceFound = findPermitInvoiceByPermitRefNumber(permitRefNumber, userID)
                    val permitType = findPermitType(applicationMapProperties.mapQAPermitTypeIdInvoices)

                    invoiceBatchRepo.findByIdOrNull(batchID)
                        ?.let { invoiceDetails ->

                            with(permitInvoiceFound) {
                                batchInvoiceNo = invoiceDetails.id
                                modifiedBy = commonDaoServices.concatenateName(user)
                                modifiedOn = commonDaoServices.getTimestamp()
                            }
                            permitInvoiceFound = invoiceRepository.save(permitInvoiceFound)

                            with(invoiceDetails) {
                                description = "${permitInvoiceFound.invoiceNumber},$description"
                                totalAmount =
                                    totalAmount?.plus(permitInvoiceFound.amount ?: throw Exception("INVALID AMOUNT"))
                            }

                            invoiceBatchDetails = invoiceBatchRepo.save(invoiceDetails)
                        }
                        ?: kotlin.run {
                            var batchInvoicePermit = QaBatchInvoiceEntity()
                            with(batchInvoicePermit) {
                                invoiceNumber = "KIMS${permitType.markNumber}${
                                    generateRandomText(
                                        5,
                                        s.secureRandom,
                                        s.messageDigestAlgorithm,
                                        true
                                    )
                                }".toUpperCase()
                                userId = userID
                                plantId = batchInvoiceDto.plantID
                                status = s.activeStatus
                                description = "${permitInvoiceFound.invoiceNumber}"
                                totalAmount = permitInvoiceFound.amount
                                createdBy = commonDaoServices.concatenateName(user)
                                createdOn = commonDaoServices.getTimestamp()
                            }
                            batchInvoicePermit = invoiceBatchRepo.save(batchInvoicePermit)

                            with(permitInvoiceFound) {
                                batchInvoiceNo = batchInvoicePermit.id
                                modifiedBy = commonDaoServices.concatenateName(user)
                                modifiedOn = commonDaoServices.getTimestamp()
                            }
                            permitInvoiceFound = invoiceRepository.save(permitInvoiceFound)

                            invoiceBatchDetails = batchInvoicePermit
                        }

                    batchID = invoiceBatchDetails?.id!!

                    sr.payload = "permitInvoiceFound[id= ${permitInvoiceFound.userId}]"
                    sr.names = "${permitInvoiceFound.invoiceNumber} ${permitInvoiceFound.amount}"
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
        return Pair(sr, invoiceBatchDetails ?: throw Exception("INVALID BATCH INVOICE DETAILS"))
    }

    fun permitMultipleInvoiceRemoveInvoice(
        s: ServiceMapsEntity,
        user: UsersEntity,
        batchInvoiceDto: NewBatchInvoiceDto,
    ): Pair<ServiceRequestsEntity, QaBatchInvoiceEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var invoiceDetails = invoiceBatchRepo.findByIdOrNull(batchInvoiceDto.batchID)
            ?: throw Exception("INVOICE BATCH WITH [ID=${batchInvoiceDto.batchID}],DOES NOT EXIXT ")
        try {

            val userID = user.id ?: throw Exception("INVALID USER ID")
            var permitInvoiceFound = findPermitInvoiceByPermitRefNumber(
                batchInvoiceDto.permitRefNumber ?: throw Exception("PERMIT REF NUMBER REQUIRED"), userID
            )
            var batchID: Long? = null


            with(invoiceDetails) {
                description = "${permitInvoiceFound.invoiceNumber},$description"
                totalAmount = totalAmount?.minus(permitInvoiceFound.amount ?: throw Exception("INVALID AMOUNT"))
            }
            batchID = invoiceBatchRepo.save(invoiceDetails).id

            with(permitInvoiceFound) {
                batchInvoiceNo = null
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
            permitInvoiceFound = invoiceRepository.save(permitInvoiceFound)

            sr.payload = "permitInvoiceFound[id= ${permitInvoiceFound.userId}]"
            sr.names = "${permitInvoiceFound.invoiceNumber} ${permitInvoiceFound.amount}"
            sr.varField1 = batchID.toString()

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
        return Pair(sr, invoiceDetails)
    }

    fun permitMultipleInvoiceSubmitInvoice(
        s: ServiceMapsEntity,
        user: UsersEntity,
        batchInvoiceDto: NewBatchInvoiceDto,
    ): Pair<ServiceRequestsEntity, QaBatchInvoiceEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var invoiceDetails: QaBatchInvoiceEntity? = null
        try {

            val userID = user.id ?: throw Exception("INVALID USER ID")
            if (batchInvoiceDto.batchID == -1L) {
                throw Exception("INVALID BATCH ID NUMBER")
            }
            invoiceDetails = findBatchInvoicesWithID(batchInvoiceDto.batchID)


            val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(
                user,
                invoiceDetails.invoiceNumber ?: throw Exception("MISSING INVOICE NUMBER")
            )
            val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                invoiceDetails,
                applicationMapProperties.mapInvoiceTransactionsForPermit,
                user,
                batchInvoiceDetail
            )

            //Todo: Payment selection
            val manufactureDetails = commonDaoServices.findCompanyProfile(userID)
            val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
            with(myAccountDetails) {
                accountName = manufactureDetails.name
                accountNumber = manufactureDetails.registrationNumber
                currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
            }

            invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                user,
                updateBatchInvoiceDetail,
                myAccountDetails
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
        permitType: PermitTypesEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val userCompany = user.id?.let { commonDaoServices.findCompanyProfile(it) }
                ?: throw NullValueNotAllowedException("Company Details For User with [ID = ${user.id}] , does Not exist")

            val invoiceGenerated = invoiceGen(permit, userCompany, user, permitType)

            sr.payload = "User[id= ${userCompany.userId}]"
            sr.names = "${userCompany.name} ${userCompany.kraPin}"

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
                        mpesaServices.mainMpesaTransaction(
                            10.00.toBigDecimal(),
                            it2, it1, it, applicationMapProperties.mapInvoiceTransactionsForPermit
                        )
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
        permit: PermitApplicationsEntity,
        auth: Authentication
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val permitType = findPermitType(applicationMapProperties.mapQAPermitTypeIdFmark)
//            val smark = permit.id?.let { findPermitBYID(it) } ?: throw ExpectedDataNotFound("SMARK Id Not found")

            var fmarkPermit = PermitApplicationsEntity()
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
        return sr
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


    //Todo: CHECK THE METHODE AGAIN AFTER DEMO
    fun invoiceGen(
        permits: PermitApplicationsEntity,
        entity: CompanyProfileEntity,
        user: UsersEntity,
        permitType: PermitTypesEntity
    ): InvoiceEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        val invoice = InvoiceEntity()
        with(invoice) {

            /**
             * Get rid of hard coded data
             */
            conditions = "Must be paid in 30 days"
            permitRefNumber = permits.permitRefNumber
            permitId = permits.id
            createdOn = Timestamp.from(Instant.now())
            status = 0
            map.tokenExpiryHours?.let { expiryDate = Timestamp.from(Instant.now().plus(it, ChronoUnit.HOURS)) }

            signature = commonDaoServices.concatenateName(user)
            createdBy = commonDaoServices.concatenateName(user)
            val generatedPayments = permits.let { calculatePayment(it, map, user) }

            inspectionCost = generatedPayments[0]
            applicationCost = generatedPayments[1]
            amount = generatedPayments[2]
            tax = generatedPayments[3]
            businessName = entity.name
            permitId = permits.id
            goods = permits.tradeMark
            userId = user.id
            paymentStatus = 0
            invoiceNumber = "KIMS${permitType.markNumber}${
                generateRandomText(
                    3,
                    map.secureRandom,
                    map.messageDigestAlgorithm,
                    true
                ).toUpperCase()
            }"
        }

        return invoiceRepository.save(invoice)


    }

    fun findFirmTypeById(firmTypeID: Long): TurnOverRatesEntity {
        iTurnOverRatesRepository.findByIdOrNull(firmTypeID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Firm type [Id = ${firmTypeID}]")
    }

    fun manufactureType(manufactureTurnOver: BigDecimal): TurnOverRatesEntity {
        return when {
            manufactureTurnOver > iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkLargeFirmsTurnOverId)?.lowerLimit -> {
                findFirmTypeById(applicationMapProperties.mapQASmarkLargeFirmsTurnOverId)
            }
            manufactureTurnOver <= iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkMediumTurnOverId)?.upperLimit && manufactureTurnOver >= iTurnOverRatesRepository.findByIdOrNull(
                applicationMapProperties.mapQASmarkMediumTurnOverId
            )?.lowerLimit -> {
                findFirmTypeById(applicationMapProperties.mapQASmarkMediumTurnOverId)
            }
            else -> {
                findFirmTypeById(applicationMapProperties.mapQASmarkJuakaliTurnOverId)
            }
        }

    }


    //Todo: CHECK THE METHODE AGAIN AFTER DEMO
    fun calculatePayment(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): MutableList<BigDecimal?> {

        KotlinLogging.logger { }.info { "ManufacturerId, ${permit.userId}" }
        val permitType = findPermitType(permit.permitType ?: throw Exception("INVALID PERMIT TYPE ID"))
        val numberOfYears = permitType.numberOfYears?.toBigDecimal() ?: throw Exception("INVALID NUMBER OF YEARS")
        val manufactureTurnOver = permit.userId?.let { commonDaoServices.findCompanyProfile(it).yearlyTurnover }
        val plantDetails = findPlantDetails(permit.attachedPlantId ?: throw Exception("INVALID PLANT ID"))
        var m = mutableListOf<BigDecimal?>()


        when (permitType.id) {
            applicationMapProperties.mapQAPermitTypeIdSmark -> {
                when {
                    manufactureTurnOver != null -> {
                        val turnOverValues = manufactureType(manufactureTurnOver)
                        val taxRate = turnOverValues.taxRate ?: throw Exception("INVALID TAX RATE")
                        when {
                            manufactureTurnOver > iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkLargeFirmsTurnOverId)?.lowerLimit -> {

                                m = mutableListForTurnOverAbove500KSmark(
                                    plantDetails,
                                    permitType,
                                    permit,
                                    turnOverValues.fixedAmountToPay?.times(numberOfYears) ?: throw Exception("INVALID FIXED AMOUNT TO PAY"),
                                    turnOverValues.variableAmountToPay?.times(numberOfYears) ?: throw Exception("INVALID VARIABLE AMOUNT TO PAY"),
                                    taxRate,
                                    map,
                                    user,
                                    commonDaoServices.getCurrentDate(),
                                    m
                                )

                            }
                            manufactureTurnOver <= iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkMediumTurnOverId)?.upperLimit && manufactureTurnOver >= iTurnOverRatesRepository.findByIdOrNull(
                                applicationMapProperties.mapQASmarkMediumTurnOverId
                            )?.lowerLimit -> {
                                m = mutableListForTurnOverBelow500kAndAbove200KSmark(
                                    plantDetails,
                                    permitType,
                                    permit,
                                    turnOverValues.fixedAmountToPay?.times(numberOfYears) ?: throw Exception("INVALID FIXED AMOUNT TO PAY"),
                                    turnOverValues.variableAmountToPay?.times(numberOfYears) ?: throw Exception("INVALID VARIABLE AMOUNT TO PAY"),
                                    turnOverValues.maxProduct ?: throw Exception("INVALID PRODUCT MAX VALUE"),
                                    taxRate,
                                    map,
                                    m
                                )

                            }
                            else -> {
                                m = mutableListForTurOverBelow200KSmark(
                                    plantDetails,
                                    permitType,
                                    permit,
                                    turnOverValues.fixedAmountToPay?.times(numberOfYears)
                                        ?: throw Exception("INVALID FIXED AMOUNT TO PAY"),
                                    turnOverValues.variableAmountToPay?.times(numberOfYears)
                                        ?: throw Exception("INVALID VARIABLE AMOUNT TO PAY"),
                                    turnOverValues.maxProduct ?: throw Exception("INVALID PRODUCT MAX VALUE"),
                                    taxRate,
                                    map,
                                    m
                                )
                            }
                        }
                    }
                    else -> {
                        throw ExpectedDataNotFound("The Turn over Details are missing for logged in user")
                    }
                }
            }
            applicationMapProperties.mapQAPermitTypeIDDmark -> {
                when (permit.permitForeignStatus) {
                    applicationMapProperties.mapQaDmarkDomesticStatus -> {

                        var stgAmt: BigDecimal? = null
                        var taxAmount: BigDecimal? = null
                        var amountToPay: BigDecimal? = null
                        val inspectionCostValue: BigDecimal? = null
                        val applicationCostValue = permitType.dmarkLocalAmount?.times(numberOfYears)

                        stgAmt = applicationCostValue
                        taxAmount = stgAmt?.let { permitType.taxRate?.times(it) }
                        amountToPay = taxAmount?.let { stgAmt?.plus(it) }

                        m = myReturPaymentValues(
                            m,
                            inspectionCostValue,
                            applicationCostValue,
                            amountToPay,
                            taxAmount
                        )
                    }
                    applicationMapProperties.mapQaDmarkForeginStatus -> {
                        val foreignAmountCalculated =
                            iMoneyTypeCodesRepo.findByTypeCode(applicationMapProperties.mapUssRateName)?.typeCodeValue?.toBigDecimal()
                        var stgAmt: BigDecimal? = null
                        var taxAmount: BigDecimal? = null
                        var amountToPay: BigDecimal? = null
                        val inspectionCostValue: BigDecimal? = null
                        val applicationCostValue = permitType.dmarkForeignAmount?.times(numberOfYears)
                            ?.times(foreignAmountCalculated ?: throw Exception("INVALID AMOUNT CONVERSION RATE"))

                        stgAmt = applicationCostValue
                        taxAmount = stgAmt?.let { permitType.taxRate?.times(it) }
                        amountToPay = taxAmount?.let { stgAmt?.plus(it) }

                        m = myReturPaymentValues(
                            m,
                            inspectionCostValue,
                            applicationCostValue,
                            amountToPay,
                            taxAmount
                        )
                    }
                    else -> throw ExpectedDataNotFound("MISSING DMARK APPLICATION TYPE ")
                }
            }
            applicationMapProperties.mapQAPermitTypeIdFmark -> {
                var stgAmt: BigDecimal? = null
                var taxAmount: BigDecimal? = null
                var amountToPay: BigDecimal? = null
                val inspectionCostValue: BigDecimal? = null
                val applicationCostValue: BigDecimal? = permitType.fmarkAmount?.times(numberOfYears)


                stgAmt = applicationCostValue
                taxAmount = stgAmt?.let { permitType.taxRate?.times(it) }
                amountToPay = taxAmount?.let { stgAmt?.plus(it) }

                m = myReturPaymentValues(
                    m,
                    inspectionCostValue,
                    applicationCostValue,
                    amountToPay,
                    taxAmount
                )
            }
        }

        return m
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
        KotlinLogging.logger { }.info { "Turnover is less than 200, 000" }

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

        when {
            plantDetail.paidDate == null && plantDetail.endingDate == null && plantDetail.inspectionFeeStatus == null -> {
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
            currentDate > plantDetail.paidDate && currentDate < plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 -> {
                stgAmt = applicationCost
                inspectionCostValue = null
            }
            else -> {
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
                " qaInspectionReportRecommendation ON = ${qaInspectionReportRecommendation.modifiedOn} BY ${qaInspectionReportRecommendation.modifiedBy}"
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
        attachment: String,
    ) {
        val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "LAB REPORT AND COMPLIANCE STATUS "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "Find Attached lab test report for   \n" +
                " with the following compliance status  $compliantStatus" +
                "\n  and  the Following Remarks  $compliantRemarks" +
                "for the following permit with REF number ${permitDetails.permitRefNumber} : You have 30 days to perform corrective action for re-inspection.  "

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody, attachment) }
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
        val subject = "INSPECTION REVIEW FOR APPROVAL"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The following permit with REF number ${permitDetails.permitRefNumber}, awaits your approval for inspection review \n" +
                " ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationPCMForAwardingPermit(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.pcmId?.let { commonDaoServices.findUserByID(it) }
        val subject = "INSPECTION REVIEW FOR APPROVAL"
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

    fun mapDtoSTA3View(sta3: QaSta3Entity): STA3Dto {
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
        }

        return sta1ViewDto
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
        val allInvoicesInBatch =
            findALlInvoicesPermitWithBatchID(batchInvoiceEntity.id ?: throw Exception("MISSING INVOICE BATCH ID"))
        val companyProfile =
            commonDaoServices.findCompanyProfile(loggedInUser.id ?: throw  Exception("MISSING USER ID FOUND"))

        return BatchInvoiceDto(
            populateInvoiceDetails(companyProfile, batchInvoiceEntity, map),
            listPermitsInvoices(allInvoicesInBatch, null, map)
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
                p.street,
                p.buildingName,
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

}