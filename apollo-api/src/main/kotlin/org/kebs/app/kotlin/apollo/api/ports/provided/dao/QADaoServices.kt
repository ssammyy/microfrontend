package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.common.dto.PermitEntityDto
import org.kebs.app.kotlin.apollo.common.dto.qa.CommonPermitDto
import org.kebs.app.kotlin.apollo.common.dto.qa.PermitDetailsDto
import org.kebs.app.kotlin.apollo.common.dto.qa.WorkPlanDto
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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class QADaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val productsRepo: IProductsRepository,
    private val workPlanCreatedRepo: IQaWorkplanRepository,
    private val iTurnOverRatesRepository: ITurnOverRatesRepository,
    private val iManufacturePaymentDetailsRepository: IManufacturerPaymentDetailsRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val invoiceDaoService: InvoiceDaoService,
    private val paymentUnitsRepository: ICfgKebsPermitPaymentUnitsRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
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
    private val notifications: Notifications,
) {

    final var appId = applicationMapProperties.mapQualityAssurance

    val permitList = "redirect:/api/qa/permits-list?permitTypeID"
    val permitDetails = "redirect:/api/qa/permit-details?permitID"
    val sta10Details = "redirect:/api/qa/view-sta10?permitID"

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

    fun findAllHODPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByHodIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findPermitWithPermitNumberLatest(permitRefNumber: String): PermitApplicationsEntity {
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

    fun findAllAssessorPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByAssessorIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
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

    fun findAllPCMPermitListWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No USER ID Found")
        permitRepo.findByPcmIdAndPermitTypeAndOldPermitStatusIsNull(userId, permitType)
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

    fun findAllQAOPermitListWithPaymentStatus(paymentStatus: Int): List<PermitApplicationsEntity> {
        permitRepo.findAllByPaidStatus(paymentStatus)?.let { permitList ->
            return permitList
        } ?: throw ExpectedDataNotFound("No Permit Found for the following PAYMENT STATUS = ${paymentStatus}")
    }

    fun findPermitBYID(id: Long): PermitApplicationsEntity {
        permitRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit found with the following [ID=$id]")
    }


    fun findSampleSubmittedBYPermitID(permitId: Long): QaSampleSubmissionEntity {
        SampleSubmissionRepo.findByPermitId(permitId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample submission found with the following [permitId=$permitId]")
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

    fun findAllRequestByPermitID(permitId: Long): List<PermitUpdateDetailsRequestsEntity> {
        permitUpdateDetailsRequestsRepo.findByPermitId(permitId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Requests found  with Permit [ID=$permitId]")
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
        permitRepo.findByUserIdAndPermitTypeAndEndOfProductionStatusAndPermitAwardStatus(userId, permitTypeID, status, permitAwardedStatus)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit List found with the following user [ID=$userId]")
    }

    fun findAllProductManufactureINPlantWithID(
        permitAwardedStatus: Int,
        endProductionStatus: Int,
        permitTypeID: Long,
        plantID: Long
    ): List<PermitApplicationsEntity> {
        permitRepo.findByPermitTypeAndEndOfProductionStatusAndPermitAwardStatusAndAttachedPlantId(permitTypeID, endProductionStatus, permitAwardedStatus, plantID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Product Being Manufacture List with the following plantID [ID=$plantID]")
    }

    fun findPermitInvoiceByPermitID(id: Long, userId: Long): InvoiceEntity {
        invoiceRepository.findByPermitIdAndUserId(id, userId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Invoice found with the following [PERMIT ID=$id  and LoggedIn User]")
    }

    fun findSTA3WithPermitIDBY(permitId: Long): QaSta3Entity {
        sta3Repo.findByPermitId(permitId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA3 found with the following [permit id=$permitId]")
    }

    fun findAllPlantDetails(userId: Long): List<ManufacturePlantDetailsEntity> {
        manufacturePlantRepository.findByUserId(userId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [user id=$userId]")
    }


    fun findPlantDetails(plantID: Long): ManufacturePlantDetailsEntity {
        manufacturePlantRepository.findByIdOrNull(plantID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [id=$plantID]")
    }


    fun findSTA10WithPermitIDBY(permitId: Long): QaSta10Entity {
        sta10Repo.findByPermitId(permitId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA10 found with the following [permit id=$permitId]")
    }

    fun findSchemeOfSupervisionWithPermitIDBY(permitId: Long): QaSchemeForSupervisionEntity {
        schemeForSupervisionRepo.findByPermitId(permitId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No SCHEME OF SUPERVISION found with the following [permit id=$permitId]")
    }

    fun findUploadedFileBYId(fileID: Long): QaUploadsEntity {
        qaUploadsRepo.findByIdOrNull(fileID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$fileID]")
    }

    fun findUploadedFileByPermitIdAndDocType(permitId: Long, docType: String): QaUploadsEntity {
        qaUploadsRepo.findByPermitIdAndDocumentType(permitId, docType)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("No File found with the following details: [ permitId=$permitId], [ docType=$docType]")
    }

    fun findAllUploadedFileBYPermitIDAndOrdinarStatus(permitId: Long, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitIdAndOrdinaryStatus(permitId, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$permitId]")
    }

    fun findAllUploadedFileBYPermitIDAndCocStatus(permitId: Long, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitIdAndCocStatus(permitId, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$permitId]")
    }

    fun findAllUploadedFileBYPermitIDAndSscStatus(permitId: Long, status: Int): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitIdAndSscStatus(permitId, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No File found with the following [ id=$permitId]")
    }

    fun findProductsManufactureWithSTA10ID(sta10Id: Long): List<QaProductManufacturedEntity>? {
        return productsManufactureSTA10Repo.findBySta10Id(sta10Id)
    }


    fun findRawMaterialsWithSTA10ID(sta10Id: Long): List<QaRawMaterialEntity>? {
        return rawMaterialsSTA10Repo.findBySta10Id(sta10Id)
    }

    fun findMachinePlantsWithSTA10ID(sta10Id: Long): List<QaMachineryEntity>? {
        return machinePlantsSTA10Repo.findBySta10Id(sta10Id)
    }

    fun findManufacturingProcessesWithSTA10ID(sta10Id: Long): List<QaManufacturingProcessEntity>? {
        return manufacturingProcessSTA10Repo.findBySta10Id(sta10Id)
    }

    fun findAllOldPermitWithPermitID(permitRefNumber: String): List<PermitApplicationsEntity>? {
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

        val directorsNames= mutableListOf<String>()
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
            pd.region

        )
    }

    fun listPermits(permits: List<PermitApplicationsEntity>, map: ServiceMapsEntity): List<PermitEntityDto> {
        val permitsList = mutableListOf<PermitEntityDto>()
        permits.map { p ->
            permitsList.add(
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
                    p.userId
                )
            )
        }
        return permitsList.sortedBy { it.id }
    }

    fun permitDetails(permit: PermitApplicationsEntity,ksApplicable: SampleStandardsEntity?, map: ServiceMapsEntity): PermitDetailsDto {
        val plantAttached = permit.attachedPlantId?.let { findPlantDetails(it) }
        val companyProfile = plantAttached?.companyProfileId?.let { commonDaoServices.findCompanyProfileWithID(it) }
        val p = PermitDetailsDto()
        with(p){
            Id = permit.id
            permitNumber =  permit.awardedPermitNumber
            permitRefNumber =  permit.permitRefNumber
            firmName = companyProfile?.name
            postalAddress = plantAttached?.postalAddress
            physicalAddress = plantAttached?.physicalAddress
            contactPerson = plantAttached?.contactPerson
            telephoneNo = plantAttached?.telephone
            regionPlantValue = plantAttached?.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus).region }
            countyPlantValue = plantAttached?.county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).county }
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
            standardNumber = ksApplicable?.standardNumber
            standardTitle = ksApplicable?.standardTitle
            permitForeignStatus = permit.permitForeignStatus == 1
            when (permit.assignOfficerStatus) {
                map.activeStatus -> {
                    assignOfficer = commonDaoServices.concatenateName(commonDaoServices.findUserByID(permit.qaoId?: throw Exception("INVALID QAO ID")))
                }
            }
            when (permit.assignAssessorStatus) {
                map.activeStatus -> {
                    assignAssessor = commonDaoServices.concatenateName(commonDaoServices.findUserByID(permit.assessorId?: throw Exception("INVALID ASSESSOR ID")))
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
        }
        return p
    }

    fun listWorkPlan(workPlan: List<QaWorkplanEntity>, map: ServiceMapsEntity): List<WorkPlanDto> {
        val permitsList = mutableListOf<WorkPlanDto>()
        workPlan.map { wp ->
            val permit =findPermitBYID(wp.permitId?:throw Exception("INVALID PERMIT ID"))
            val permitDetailsCommon =  companyDtoDetails(permit, map)
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

    fun findOfficersList(
        plantID: Long,
        map: ServiceMapsEntity,
        designationID: Long
    ): List<UserProfilesEntity> {
        val plantAttached = findPlantDetails(plantID)
        val region = plantAttached.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Plant attached Region Id is Empty, check config")
        val department = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)
        val designation = commonDaoServices.findDesignationByID(designationID)

        //return commonDaoServices.findAllUsersWithinRegionDepartmentDivisionSectionId(region, department, division, section, map.activeStatus)
        return commonDaoServices.findAllUsersWithDesignationRegionDepartmentAndStatus(
            designation,
            region,
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

    fun assignNextOfficerWithDesignation(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        designationID: Long
    ): UsersEntity? {
        val designation = commonDaoServices.findDesignationByID(designationID)
        return commonDaoServices.findUserProfileWithDesignationAndStatus(designation, map.activeStatus).userId

    }

    fun sendAppointAssessorNotificationEmail(recipientEmail: String, permit: PermitApplicationsEntity): Boolean {
        val subject = "DMARK Application Assessment"
        val messageBody = "DMARK application with the details below has been assisgned to you for assessment:  \n" +
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
        val messageBody = "Dmark assessment report and conformity status is available for below:  \n" +
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
//                productName = product?.let { commonDaoServices.findProductByID(it).name }
                permitType = permitTypeDetails.id
                permitRefNumber = "REF${permitTypeDetails.markNumber}${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
                enabled = map.initStatus
                versionNumber = 1
                endOfProductionStatus = map.initStatus
                status = map.activeStatus
                fmarkGenerated = map.inactiveStatus
                permitStatus = applicationMapProperties.mapQaStatusDraft
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
                permitId = permits.id
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
        permitDetails: PermitApplicationsEntity,
        ssfDetails: QaSampleSubmissionEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSF = findSampleSubmittedBYPermitID(permitDetails.id ?: throw Exception("MISSING ITEM ID"))
        try {

            with(saveSSF) {
                resultsAnalysis = ssfDetails.resultsAnalysis
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }


            saveSSF = SampleSubmissionRepo.save(saveSSF)


            permitDetails.compliantStatus = saveSSF.resultsAnalysis
            var complianceValue: String? = null
            when (permitDetails.compliantStatus) {
                map.activeStatus -> {
                    complianceValue = "COMPLIANT"
                }
                map.inactiveStatus -> {
                    complianceValue = "NON-COMPLIANT"
                }
            }
            permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPRecommendation, user)
            sendComplianceStatusAndLabReport(
                permitDetails,
                complianceValue ?: throw ExpectedDataNotFound("INVALID VALUE")
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
                    schemeSendEmail(permitUpdate, reasonValue, foundSSC)
                }
                schemeSupervision.acceptedRejectedStatus == map.inactiveStatus -> {
                    var permitUpdate = schemeUpdatePermit(foundSSC, map.inactiveStatus)
                    permitUpdate.generateSchemeStatus = map.inactiveStatus
                    permitUpdate = permitUpdateDetails(permitUpdate, map, user).second
                    reasonValue = "REJECTED"

                    schemeSendEmail(permitUpdate, reasonValue, foundSSC)
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

    private fun schemeSendEmail(
        permitUpdate: PermitApplicationsEntity,
        reasonValue: String?,
        foundSSC: QaSchemeForSupervisionEntity
    ) {
        //todo: for now lets work with this i will change it
        val userPermit = permitUpdate.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "SCHEME FOR SUPERVISION AND CONTROL (SSC)"
        val messageBody = "Dear ${userPermit?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "Scheme For Supervision And Control was ${reasonValue} due to the Following reason ${foundSSC.acceptedRejectedReason}:" +
                "\n " +
                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitUpdate.id}"

        userPermit?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    private fun schemeUpdatePermit(
        foundSSC: QaSchemeForSupervisionEntity,
        status: Int?
    ): PermitApplicationsEntity {
        val permitUpdate =
            foundSSC.permitId?.let { findPermitBYID(it) } ?: throw ExpectedDataNotFound("Permit ID cannot be null")
        permitUpdate.approvedRejectedScheme = status
        return permitUpdate
    }

    fun sta3NewSave(
        permitNewID: Long,
        qaSta3Details: QaSta3Entity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaSta3Entity {

        with(qaSta3Details) {
            permitId = permitNewID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return sta3Repo.save(qaSta3Details)
    }

    fun sta10NewSave(
        permitNewID: Long,
        qaSta10Details: QaSta10Entity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaSta10Entity {

        with(qaSta10Details) {
            totalNumberPersonnel = totalNumberMale?.let { totalNumberFemale?.plus(it) }
            town = town?.let { commonDaoServices.findTownEntityByTownId(it).id }
            county = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).id }
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            permitId = permitNewID
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
        productManufacturedDetails: QaProductManufacturedEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaProductManufacturedEntity {

        with(productManufacturedDetails) {
            sta10Id = qaSta10ID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return productsManufactureSTA10Repo.save(productManufacturedDetails)
    }

    fun sta10RawMaterialsNewSave(
        qaSta10ID: Long,
        rawMaterialsDetails: QaRawMaterialEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaRawMaterialEntity {

        with(rawMaterialsDetails) {
            sta10Id = qaSta10ID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return rawMaterialsSTA10Repo.save(rawMaterialsDetails)
    }

    fun sta10MachinePlantNewSave(
        qaSta10ID: Long,
        machinePlantsDetails: QaMachineryEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaMachineryEntity {

        with(machinePlantsDetails) {
            sta10Id = qaSta10ID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return machinePlantsSTA10Repo.save(machinePlantsDetails)
    }


    fun saveQaFileUploads(
        docFile: MultipartFile,
        doc: String,
        user: UsersEntity,
        map: ServiceMapsEntity,
        qaUploads: QaUploadsEntity,
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
                transactionDate = commonDaoServices.getCurrentDate()
                versionNumber = versionNumberAdded
                status = map.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }

            uploads = qaUploadsRepo.save(uploads)

            sr.payload = "DOC File Added [${uploads.name} and ${uploads.permitId}]"
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

    fun sta10ManufacturingProcessNewSave(
        qaSta10ID: Long,
        manufacturingProcessDetails: QaManufacturingProcessEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaManufacturingProcessEntity {

        with(manufacturingProcessDetails) {
            sta10Id = qaSta10ID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return manufacturingProcessSTA10Repo.save(manufacturingProcessDetails)
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
                findPermitWithPermitNumberLatest(pm.awardedPermitNumber ?: throw Exception("INVALID PERMIT NUMBER"))
            KotlinLogging.logger { }
                .info { "::::::::::::::::::PERMIT With PERMIT NUMBER = ${pm.awardedPermitNumber}, DOES Exists::::::::::::::::::::: " }
            val versionNumberOld =
                oldPermit.versionNumber ?: throw ExpectedDataNotFound("Permit Version Number is Empty")

            oldPermit.oldPermitStatus = 1
//            oldPermit.renewalStatus = s.activeStatus
            //update last previous version permit old status
            oldPermit = permitUpdateDetails(oldPermit, s, user).second

            val permitTypeDetails = findPermitType(oldPermit.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))

            with(savePermit) {
                permitRefNumber = "${permitTypeDetails.markNumber}${
                    generateRandomText(
                        5,
                        s.secureRandom,
                        s.messageDigestAlgorithm,
                        true
                    )
                }".toUpperCase()
                renewalStatus = s.activeStatus
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
                    val sta10 = findSTA10WithPermitIDBY(oldPermit.id ?: throw Exception("INVALID PERMIT ID"))
                    var newSta10 = QaSta10Entity()
                    newSta10 = commonDaoServices.updateDetails(sta10, newSta10) as QaSta10Entity
                    newSta10.id = null
                    sta10NewSave(savePermit.id ?: throw Exception("INVALID PERMIT ID"), newSta10, user, s)
                }
                applicationMapProperties.mapQAPermitTypeIDDmark -> {
                    val sta3 = findSTA3WithPermitIDBY(oldPermit.id ?: throw Exception("INVALID PERMIT ID"))
                    var newSta3 = QaSta3Entity()
                    newSta3 = commonDaoServices.updateDetails(sta3, newSta3) as QaSta3Entity
                    newSta3.id = null
                    sta3NewSave(savePermit.id ?: throw Exception("INVALID PERMIT ID"), newSta3, user, s)
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


    fun invoiceUpdateDetails(invoice: InvoiceEntity, user: UsersEntity): InvoiceEntity {

        with(invoice) {
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceRepository.save(invoice)
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

            val invoiceNumber = invoiceGenerated.invoiceNumber
                ?: throw NullValueNotAllowedException("Invoice Number Is Missing For Invoice with [ID = ${invoiceGenerated.id}]")

            val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(user, invoiceNumber)
            val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                invoiceGenerated,
                applicationMapProperties.mapInvoiceTransactionsForPermit,
                user,
                batchInvoiceDetail
            )

            //Todo: Payment selection
            val manufactureDetails = commonDaoServices.findCompanyProfile(user.id!!)
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
        invoice: InvoiceEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            //TODO: PAYMENT METHOD UPDATE THE AMOUNT BY REMOVING THE STATIC VALUE
            user.userName?.let {
                invoice.invoiceNumber?.let { it1 ->
                    mpesaServices.sanitizePhoneNumber(phoneNumber)?.let { it2 ->
                        mpesaServices.mainMpesaTransaction(
                            "10",
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
            with(plantDetail){
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
            manufactureTurnOver < iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkMediumTurnOverId)?.upperLimit && manufactureTurnOver > iTurnOverRatesRepository.findByIdOrNull(
                applicationMapProperties.mapQASmarkMediumTurnOverId
            )?.lowerLimit -> {
                findFirmTypeById(applicationMapProperties.mapQASmarkLargeFirmsTurnOverId)
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
        val permitType = findPermitType(permit.permitType?:throw Exception("INVALID PERMIT TYPE ID"))
        val numberOfYears = permitType.numberOfYears?.toBigDecimal()?:throw Exception("INVALID NUMBER OF YEARS")
        val manufactureTurnOver = permit.userId?.let { commonDaoServices.findCompanyProfile(it).yearlyTurnover }
        val plantDetails = findPlantDetails(permit.attachedPlantId?:throw Exception("INVALID PLANT ID"))
        var m = mutableListOf<BigDecimal?>()


        when (permitType.id) {
            applicationMapProperties.mapQAPermitTypeIdSmark -> {
                when {
                    manufactureTurnOver != null -> {
                        val turnOverValues = manufactureType(manufactureTurnOver)
                        val taxRate = turnOverValues.taxRate?:throw Exception("INVALID TAX RATE")
                        when {
                            manufactureTurnOver > iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkLargeFirmsTurnOverId)?.lowerLimit -> {

                                m = mutableListForTurnOverAbove500KSmark(
                                    plantDetails,
                                    permitType,
                                    permit,
                                    turnOverValues.fixedAmountToPay?.times(numberOfYears)?:throw Exception("INVALID FIXED AMOUNT TO PAY"),
                                    turnOverValues.variableAmountToPay?.times(numberOfYears)?:throw Exception("INVALID VARIABLE AMOUNT TO PAY"),
                                    taxRate,
                                    map,
                                    user,
                                    commonDaoServices.getCurrentDate(),
                                    m
                                )

                            }
                            manufactureTurnOver < iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkMediumTurnOverId)?.upperLimit && manufactureTurnOver > iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkMediumTurnOverId)?.lowerLimit -> {
                                m = mutableListForTurnOverBelow500kAndAbove200KSmark(
                                    plantDetails,
                                    permitType,
                                    permit,
                                    turnOverValues.fixedAmountToPay?.times(numberOfYears)?:throw Exception("INVALID FIXED AMOUNT TO PAY"),
                                    turnOverValues.variableAmountToPay?.times(numberOfYears)?:throw Exception("INVALID VARIABLE AMOUNT TO PAY"),
                                    turnOverValues.maxProduct?:throw Exception("INVALID PRODUCT MAX VALUE"),
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
                                    turnOverValues.fixedAmountToPay?.times(numberOfYears)?:throw Exception("INVALID FIXED AMOUNT TO PAY"),
                                    turnOverValues.variableAmountToPay?.times(numberOfYears)?:throw Exception("INVALID VARIABLE AMOUNT TO PAY"),
                                    turnOverValues.maxProduct?:throw Exception("INVALID PRODUCT MAX VALUE"),
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

                        var stgAmt : BigDecimal? = null
                        var taxAmount : BigDecimal? = null
                        var amountToPay : BigDecimal? = null
                        val inspectionCostValue : BigDecimal? = null
                        val applicationCostValue = permitType.dmarkLocalAmount?.times(numberOfYears)

                        stgAmt = applicationCostValue
                        taxAmount = stgAmt?.let { permitType.taxRate?.times(it) }
                        amountToPay = taxAmount?.let { stgAmt?.plus(it) }

                        m =  myReturPaymentValues(
                            m,
                            inspectionCostValue,
                            applicationCostValue,
                            amountToPay,
                            taxAmount
                        )
                    }
                    applicationMapProperties.mapQaDmarkForeginStatus -> {
                        val foreignAmountCalculated = iMoneyTypeCodesRepo.findByTypeCode(applicationMapProperties.mapUssRateName)?.typeCodeValue?.toBigDecimal()
                        var stgAmt : BigDecimal? = null
                        var taxAmount : BigDecimal? = null
                        var amountToPay : BigDecimal? = null
                        val inspectionCostValue : BigDecimal? = null
                        val applicationCostValue = permitType.dmarkForeignAmount?.times(numberOfYears)?.times(foreignAmountCalculated?:throw Exception("INVALID AMOUNT CONVERSION RATE"))

                        stgAmt = applicationCostValue
                        taxAmount = stgAmt?.let { permitType.taxRate?.times(it) }
                        amountToPay = taxAmount?.let { stgAmt?.plus(it) }

                        m =  myReturPaymentValues(
                            m,
                            inspectionCostValue,
                            applicationCostValue,
                            amountToPay,
                            taxAmount
                        )
                    }
                }
            }
            applicationMapProperties.mapQAPermitTypeIdFmark -> {
                var stgAmt : BigDecimal? = null
                var taxAmount : BigDecimal? = null
                var amountToPay : BigDecimal? = null
                val inspectionCostValue : BigDecimal? = null
                val applicationCostValue: BigDecimal? = permitType.fmarkAmount?.times(numberOfYears)


                stgAmt = applicationCostValue
                taxAmount = stgAmt?.let { permitType.taxRate?.times(it) }
                amountToPay = taxAmount?.let { stgAmt?.plus(it) }

                m =  myReturPaymentValues(
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

        var stgAmt : BigDecimal? = null
        var taxAmount : BigDecimal? = null
        var amountToPay : BigDecimal? = null
        var inspectionCostValue : BigDecimal? = inspectionCost
        var applicationCostValue: BigDecimal?
        var m1 = m

        val productList = findAllProductManufactureINPlantWithID(map.activeStatus,map.inactiveStatus,permitType.id?:throw Exception("INVALID PERMIT TYPE ID"),plantDetail.id)
        val productSize = productList.size
        var remainingSize: BigDecimal
        if (productSize > productMaximum) {
            remainingSize = productSize.minus(productMaximum).toBigDecimal()
            applicationCostValue = remainingSize.times(applicationCost)
            stgAmt = applicationCostValue.plus(inspectionCostValue ?: throw Exception("INVALID INSPECTION COST VALUE"))
        }else{
            applicationCostValue = inspectionCostValue
            inspectionCostValue = null
            stgAmt = applicationCostValue
        }

        taxAmount =  taxRate.times(stgAmt?:throw Exception("INVALID STG AMOUNT"))
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
        var stgAmt : BigDecimal? = null
        var taxAmount : BigDecimal? = null
        var amountToPay : BigDecimal? = null
        var inspectionCostValue : BigDecimal? = inspectionCost
        var applicationCostValue: BigDecimal?
        var m1 = m

        val productList = findAllProductManufactureINPlantWithID(map.activeStatus,map.inactiveStatus,permitType.id?:throw Exception("INVALID PERMIT TYPE ID"),plantDetail.id)
        val productSize = productList.size
        var remainingSize: BigDecimal
        if (productSize > productMaximum) {
            remainingSize = productSize.minus(productMaximum).toBigDecimal()
            applicationCostValue = remainingSize.times(applicationCost)
            stgAmt = applicationCostValue.plus(inspectionCostValue ?: throw Exception("INVALID INSPECTION COST VALUE"))
        }else{
            applicationCostValue = inspectionCostValue
            inspectionCostValue = null
            stgAmt = applicationCostValue
        }

        taxAmount =  taxRate.times(stgAmt?:throw Exception("INVALID STG AMOUNT"))
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

        var stgAmt : BigDecimal? = null
        var taxAmount : BigDecimal? = null
        var amountToPay : BigDecimal? = null
        var inspectionCostValue : BigDecimal? = inspectionCost
        var m1 = m

        when {
            plantDetail.paidDate== null && plantDetail.endingDate == null && plantDetail.inspectionFeeStatus == null -> {
                stgAmt = applicationCost.plus(inspectionCostValue ?: throw Exception("INVALID INSPECTION COST VALUE"))
                with(plantDetail) {
                    inspectionFeeStatus = 1
                    paidDate = commonDaoServices.getCurrentDate()
                    endingDate = commonDaoServices.addYearsToCurrentDate(permitType.numberOfYears ?: throw Exception("INVALID NUMBER OF YEARS"))
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
                    endingDate = commonDaoServices.addYearsToCurrentDate(permitType.numberOfYears ?: throw Exception("INVALID NUMBER OF YEARS"))
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

    fun sendComplianceStatusAndLabReport(permitDetails: PermitApplicationsEntity, compliantStatus: String) {
        val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "LAB REPORT AND COMPLIANCE STATUS "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The lab test report are available  at ${applicationMapProperties.baseUrlValue}/qa/kebs/view/attached?fileID=${permitDetails.testReportId}: \n" +
                " with the following compliance status  $compliantStatus" +
                "\n " +
                "for the following permit : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForRecommendation(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qamId?.let { commonDaoServices.findUserByID(it) }
        val subject = "RECOMMENDATION FOR AWARDING OF PERMIT"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "A recommendation has been added for the following permit : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id} \n" +
                "That awaits your approval"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForRecommendationCorrectness(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qaoId?.let { commonDaoServices.findUserByID(it) }
        val subject = "RECOMMENDATION FOR AWARDING OF PERMIT CORRECTION"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The recommendation has been rejected due to the following reasons: ${permitDetails.recommendationApprovalRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id} \n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForDeferredPermitToQaoFromPSC(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qaoId?.let { commonDaoServices.findUserByID(it) }
        val subject = "AWARDING OF PERMIT DEFERRED "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The below permit has been DEFERRED due to the following reasons: ${permitDetails.pscMemberApprovalRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForDeferredPermitToQaoFromPCM(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.qaoId?.let { commonDaoServices.findUserByID(it) }
        val subject = "AWARDING OF PERMIT DEFERRED "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The below permit has been DEFERRED due to the following reasons: ${permitDetails.pcmApprovalRemarks} \n" +
                "Please do some correction and send it back for approval : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationForPermitReviewRejectedFromPCM(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
        val subject = "PERMIT REJECTED "
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The below permit has been REJECTED due to the following reasons: ${permitDetails.pcmApprovalRemarks} \n" +
                "Please do some correction and send it back for review again : ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationPSCForAwardingPermit(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.pscMemberId?.let { commonDaoServices.findUserByID(it) }
        val subject = "INSPECTION REVIEW FOR APPROVAL"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The permit below awaits your approval for inspection review \n" +
                " ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    fun sendNotificationPCMForAwardingPermit(permitDetails: PermitApplicationsEntity) {
        val manufacturer = permitDetails.pcmId?.let { commonDaoServices.findUserByID(it) }
        val subject = "INSPECTION REVIEW FOR APPROVAL"
        val messageBody = "Dear ${manufacturer?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "The permit below awaits your approval for inspection review \n" +
                " ${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}\n"

        manufacturer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

}