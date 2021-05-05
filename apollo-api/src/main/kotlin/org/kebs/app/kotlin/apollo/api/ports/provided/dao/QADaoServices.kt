package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ICfgMoneyTypeCodesRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class QADaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val productsRepo: IProductsRepository,
    private val iTurnOverRatesRepository: ITurnOverRatesRepository,
    private val iManufacturePaymentDetailsRepository: IManufacturerPaymentDetailsRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val invoiceDaoService: InvoiceDaoService,
    private val paymentUnitsRepository: ICfgKebsPermitPaymentUnitsRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val permitRepo: IPermitApplicationsRepository,
    private val SampleCollectionRepo: IQaSampleCollectionRepository,
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

    fun findPermitWithPermitNumberLatest(permitNumber: String): PermitApplicationsEntity {
        permitRepo.findTopByPermitNumberOrderByIdDesc(permitNumber)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Permit Found for the following [PERMIT NO = ${permitNumber}]")
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

    fun findPermitBYUserIDANDProductionStatus(status: Int, permitTypeID: Long, userId: Long): List<PermitApplicationsEntity> {
        permitRepo.findByUserIdAndPermitTypeAndEndOfProductionStatus(userId,permitTypeID, status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit List found with the following user [ID=$userId]")
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
        } ?: throw ExpectedDataNotFound("No File found with the following details: [ permitId=$permitId], [ docType=$docType]")
    }

    fun findAllUploadedFileBYPermitID(permitId: Long): List<QaUploadsEntity> {
        qaUploadsRepo.findByPermitId(permitId)?.let {
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

    fun findAllOldPermitWithPermitID(permitNumber: String): List<PermitApplicationsEntity>? {
        return permitRepo.findByPermitNumberAndOldPermitStatus(permitNumber, 1)
    }


    fun findOfficersList(permit: PermitApplicationsEntity, map: ServiceMapsEntity, designationID:Long): List<UserProfilesEntity> {
        val plantID = permit.attachedPlantId
            ?: throw ServiceMapNotFoundException("Atta ched Plant details For Permit with ID = ${permit.id}, is Empty")

        val plantAttached = findPlantDetails(plantID)
        val region = plantAttached.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Plant attached Region Id is Empty, check config")
        val department = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)
        val designation = commonDaoServices.findDesignationByID(designationID)

        //return commonDaoServices.findAllUsersWithinRegionDepartmentDivisionSectionId(region, department, division, section, map.activeStatus)
        return commonDaoServices.findAllUsersWithDesignationRegionDepartmentAndStatus(designation, region, department, map.activeStatus)
    }


    fun assignNextOfficerAfterPayment(permit: PermitApplicationsEntity, map: ServiceMapsEntity, designationID:Long): UsersEntity? {
        val plantID = permit.attachedPlantId
            ?: throw ServiceMapNotFoundException("Attached Plant details For Permit with ID = ${permit.id}, is Empty")

        val plantAttached = findPlantDetails(plantID)
        val designation = commonDaoServices.findDesignationByID(designationID)
        val region = plantAttached.region?.let { commonDaoServices.findRegionEntityByRegionID(it, map.activeStatus) }
            ?: throw ExpectedDataNotFound("Plant attached Region Id is Empty, check config")
        val department = commonDaoServices.findDepartmentByID(applicationMapProperties.mapQADepertmentId)


        return commonDaoServices.findUserProfileWithDesignationRegionDepartmentAndStatus(designation, region, department, map.activeStatus).userId

    }

    fun sendAppointAssessorNotificationEmail(recipientEmail: String, permit: PermitApplicationsEntity): Boolean {
        val subject = "DMARK Application Assessment"
        val messageBody = "DMARK application with the details below has been assisgned to you for assessment:  \n" +
                "\n " +
                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}%26userID=${permit.userId}"
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }

    fun sendScheduledFactoryAssessmentNotificationEmail(recipientEmail: String, permit: PermitApplicationsEntity): Boolean {
        val subject = "Factory Assessment Visit Schedule"
        val messageBody = "Factory visit assessment has been scheduled on: ${permit.assessmentDate}:  \n" +
                "\n " +
                "The Assessment Criteria:" +
                "\n " +
                "${permit.assessmentCriteria}."
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }

    fun permitSave(
        permits: PermitApplicationsEntity,
        permitTypeDetails: PermitTypesEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, PermitApplicationsEntity >{

        var sr = commonDaoServices.createServiceRequest(map)
        var savePermit = permits
        try {

            with(savePermit) {
                userId = user.id
                productName = product?.let { commonDaoServices.findProductByID(it).name }
                permitType = permitTypeDetails.id
                permitNumber = "${permitTypeDetails.markNumber}${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, false)}".toUpperCase()
                enabled = map.initStatus
                versionNumber = 1
                endOfProductionStatus = map.initStatus
                status = map.activeStatus
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

    fun schemeSupervisionUpdateSave(
        schemeID: Long,
        schemeSupervision: QaSchemeForSupervisionEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(map)
        try {
            var foundSSC = schemeForSupervisionRepo.findByIdOrNull(schemeID)?: throw ExpectedDataNotFound("Scheme with [Id = $schemeID], does not exist")
            with(foundSSC) {
                acceptedRejectedStatus = schemeSupervision.acceptedRejectedStatus
                acceptedRejectedDate = commonDaoServices.getCurrentDate()
                status = map.activeStatus
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }

            foundSSC = schemeForSupervisionRepo.save(foundSSC)

            sr.payload = "UPDATED Scheme Saved [ID ${foundSSC.id} and ${foundSSC.permitId}]"
            sr.names = "${foundSSC.createdBy}"

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


    fun saveQaFileUploads(docFile: MultipartFile, doc: String, user: UsersEntity, map: ServiceMapsEntity, permitID: Long): ServiceRequestsEntity {

            var sr = commonDaoServices.createServiceRequest(map)
            try {

                 var uploads = QaUploadsEntity()
                with(uploads) {
                    name = commonDaoServices.saveDocuments(docFile)
                    fileType = docFile.contentType
                    documentType = doc
                    document = docFile.bytes
                    permitId = permitID
                    transactionDate = commonDaoServices.getCurrentDate()
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
            return sr
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
            var plantDetails = manufacturePlant
            with(plantDetails) {
                userId = loggedInUser.id
                region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, s.activeStatus).regionId }
                status = s.activeStatus
                createdOn = commonDaoServices.getTimestamp()
                createdBy = commonDaoServices.concatenateName(loggedInUser)
            }

            plantDetails =  manufacturePlantRepository.save(plantDetails)

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

    fun permitUpdateDetails(permits: PermitApplicationsEntity, s: ServiceMapsEntity, user: UsersEntity): Pair<ServiceRequestsEntity, PermitApplicationsEntity>{

            var sr = commonDaoServices.createServiceRequest(s)
            var updatePermit = permits
            try {

                with(updatePermit) {
                    modifiedBy = commonDaoServices.concatenateName(user)
                    modifiedOn = commonDaoServices.getTimestamp()
                }
                updatePermit =  permitRepo.save(permits)

                sr.payload = "Permit Updated [updatePermit= ${updatePermit.id}]"
                sr.names = "${updatePermit.permitNumber}} ${updatePermit.userId}"
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


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitUpdateNewWithSamePermitNumber(permitNo: String, s: ServiceMapsEntity, user: UsersEntity): Pair<ServiceRequestsEntity, PermitApplicationsEntity> {

        var sr = commonDaoServices.createServiceRequest(s)
        var savePermit = PermitApplicationsEntity()
        try {
            var oldPermit = findPermitWithPermitNumberLatest(permitNo)
            KotlinLogging.logger { }.info { "::::::::::::::::::PERMIT With PERMIT NUMBER = $permitNo, Exists::::::::::::::::::::: " }
            var versionNumberOld = oldPermit.versionNumber ?: throw ExpectedDataNotFound("Permit Version Number is Empty")

            oldPermit.oldPermitStatus = 1
            //update last previous version permit old status
            oldPermit = permitUpdateDetails(oldPermit,s, user).second


            with(savePermit) {
//                renewalStatus = 1
                userId = user.id
                permitType = oldPermit.permitType
                permitNumber = oldPermit.permitNumber
                enabled = s.initStatus
                versionNumber = versionNumberOld++
                endOfProductionStatus = s.initStatus
                status = s.activeStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            savePermit = permitRepo.save(savePermit)

            //make some values to be null for the oldest permit in here
            with(oldPermit){
                id = null
                userId = null
                permitType = null
                permitNumber = null
                enabled = null
                versionNumber = null
                endOfProductionStatus =null
                status =null
                createdBy = null
                createdOn = null
                oldPermitStatus = null
                permitExpiredStatus = null
                paidStatus = null
//                renewalStatus = null

            }

            //Update Permit renewed with new details
            savePermit = permitUpdateDetails(commonDaoServices.updateDetails(savePermit, oldPermit) as PermitApplicationsEntity,s, user).second


            sr.payload = "Permit Renewed Updated [updatePermit= ${savePermit.id}]"
            sr.names = "${savePermit.permitNumber}} ${savePermit.userId}"
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
        return Pair(sr,savePermit)
    }


    fun invoiceUpdateDetails(invoice: InvoiceEntity, user: UsersEntity): InvoiceEntity {

        with(invoice) {
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceRepository.save(invoice)
    }

    fun permitInvoiceCalculation(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permit: PermitApplicationsEntity,
        permitType: PermitTypesEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val userCompany = user.id?.let { commonDaoServices.findCompanyProfile(it) } ?: throw NullValueNotAllowedException("Company Details For User with [ID = ${user.id}] , does Not exist")

            val invoiceGenerated = invoiceGen(permit,userCompany, user, permitType)

            val invoiceNumber = invoiceGenerated.invoiceNumber ?: throw NullValueNotAllowedException("Invoice Number Is Missing For Invoice with [ID = ${invoiceGenerated.id}]")
            val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(user, invoiceNumber)
            val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(invoiceGenerated, applicationMapProperties.mapInvoiceTransactionsForPermit, user, batchInvoiceDetail)

            //Todo: Payment selection
            val manufactureDetails = commonDaoServices.findCompanyProfile(user.id!!)
            val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
            with(myAccountDetails) {
                accountName = manufactureDetails.name
                accountNumber = manufactureDetails.kraPin
                currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
            }

            invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(user, updateBatchInvoiceDetail, myAccountDetails)

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
            user.userName?.let { invoice.invoiceNumber?.let { it1 -> mpesaServices.sanitizePhoneNumber(phoneNumber)?.let { it2 ->
                mpesaServices.mainMpesaTransaction("10",
                    it2, it1, it, applicationMapProperties.mapInvoiceTransactionsForPermit)
            } } }

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
            with(fmarkPermit){
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
             fmarkPermit = permitSave(fmarkPermit,permitType, user, s).second

            val savedSmarkFmarkId = generateSmarkFmarkEntity(permit, fmarkPermit, user)


            with(permit){
                fmarkGenerated = 1
            }

            val updateSmarkAndFmarkDetails = permitUpdateDetails(permit, s, user)

            sr.payload = "savedSmarkFmarkId [id= ${savedSmarkFmarkId.id}]"
            sr.names = " Fmark created ID = $fmarkPermit.id} SMARK TIED ID = ${permit.id}"

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
    fun invoiceGen(permits: PermitApplicationsEntity, entity: CompanyProfileEntity, user: UsersEntity, permitType: PermitTypesEntity): InvoiceEntity {
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
            val generatedPayments = permits.let { calculatePayment(it, map, user,) }
            amount = generatedPayments[3]
            applicationCost = generatedPayments[2]
            val cost: BigDecimal? = generatedPayments[0]
            standardCost = cost
            inspectionCost = generatedPayments[1]
            fmarkStatus = generatedPayments[4]
            fmarkCost = generatedPayments[5]
            tax = generatedPayments[6]
            businessName = entity.name
            permitId = permits.id
            userId = user.id
            paymentStatus = 0
            invoiceNumber ="${permitType.markNumber}${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
        }

        return invoiceRepository.save(invoice)


    }


    //Todo: CHECK THE METHODE AGAIN AFTER DEMO
    fun calculatePayment(permit: PermitApplicationsEntity, map: ServiceMapsEntity, user: UsersEntity): MutableList<BigDecimal?> {
        KotlinLogging.logger { }.info { "ManufacturerId, ${permit.userId}" }
        val manufactureId = permit.userId
//        val manufactureTurnOver: BigDecimal? = manufactureId.let { it?.let { it1 -> iManufacturePaymentDetailsRepository.findByManufacturerIdAndStatus(it1, 1)?.turnOverAmount } }
        val manufactureTurnOver = permit.userId?.let { commonDaoServices.findCompanyProfile(it).yearlyTurnover }
        KotlinLogging.logger { }.info { manufactureTurnOver }
        var amountToPay: BigDecimal? = null
        var taxAmount: BigDecimal? = null
//        var inspectionCost: BigDecimal? = null

        var m = mutableListOf<BigDecimal?>()
        var fmarkCost: BigDecimal? = null
        val paymentUnits = paymentUnitsRepository.findByIdOrNull(2)
        KotlinLogging.logger { }.info { paymentUnits?.standardStandardCost }
        var fmark: BigDecimal? = null
        var stgAmt: BigDecimal? = null
        val taxRate = applicationMapProperties.mapKebsTaxRate


        val noOf = permit.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it)?.noOfPages }
        val standardCost: BigDecimal? = (paymentUnits?.standardStandardCost?.times(noOf!!))?.toBigDecimal()
        //                val inspectionCost: BigDecimal? = permit.noOfSitesProducingTheBrand?.let { paymentUnits?.standardInspectionCost?.times(it)?.toBigDecimal() }
      //Check if its a Renewal status
//       if (permit.renewalStatus!=1){
         val  inspectionCost = paymentUnits?.standardInspectionCost?.toBigDecimal()
//       }

        var applicationCost: BigDecimal? = null

        when (permit.permitType) {
            applicationMapProperties.mapQAPermitTypeIdSmark -> {
                when {
                    manufactureTurnOver != null -> {

                        when {
                            manufactureTurnOver > iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkLargeFirmsTurnOverId)?.lowerLimit -> {
                                KotlinLogging.logger { }.info { "Turnover is above 500, 000" }
                                //                val noOf = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory?.id)?.noOfPages))
                                val turnoverValues = iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkLargeFirmsTurnOverId)?: throw ExpectedDataNotFound("MISSING TURNOVER RATES FOR Large Firm SMARK")
                                applicationCost = turnoverValues.variableAmountToPay?.let { turnoverValues.fixedAmountToPay?.plus(it) }

                                if (permit.fmarkGenerated == 1) {
                                    stgAmt = applicationCost?.let { standardCost?.plus(inspectionCost!!)?.plus(it) }
                                    fmark = 1.toBigDecimal()
                                    fmarkCost = standardCost?.plus(inspectionCost!!)?.let { applicationMapProperties.mapQaFmarkAmountToPay.plus(it) }
                                    //Todo : ask if foreign cost will also be added as vat
                                    taxAmount = fmarkCost?.let { taxRate.times(it) }
                                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                                } else {
                                    KotlinLogging.logger { }.info { "second loop, ${permit.product}" }
                                    stgAmt = applicationCost?.let { standardCost?.plus(inspectionCost!!)?.plus(it) }
                                    fmark = 0.toBigDecimal()
//                                    taxAmount = stgAmt?.let {taxRate.times(it) }
                                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                                }
                                //                amountToPay = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                                KotlinLogging.logger { }.info { "Manufacturer turnover, $manufactureTurnOver" }
                                KotlinLogging.logger { }.info { "Total Amount To Pay   = " + amountToPay?.toDouble() }

                                m =   myReturPaymentValues(
                                    m,
                                    standardCost,
                                    inspectionCost,
                                    applicationCost,
                                    amountToPay,
                                    fmark,
                                    fmarkCost,
                                    taxAmount
                                )

                            }
                            manufactureTurnOver  < iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkMediumTurnOverId)?.upperLimit && manufactureTurnOver > iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkMediumTurnOverId)?.lowerLimit -> {
                                KotlinLogging.logger { }.info { "Turnover is less than 500, 000 but above 200, 000" }
                                //                val noOf = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory?.id)?.noOfPages))
                                val turnoverValues = iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkMediumTurnOverId)?: throw ExpectedDataNotFound("MISSING TURNOVER RATES FOR Medium Firm SMARK")
                                val productList = user.id?.let { findPermitBYUserIDANDProductionStatus(map.inactiveStatus, applicationMapProperties.mapQAPermitTypeIdSmark, it) }?: throw ExpectedDataNotFound("MISSING USER ID")
                                val productSize = productList.size
                                val remainingSize = productSize.minus(applicationMapProperties.mapQaSmarkMediumMaxProduct).toBigDecimal()
                                var extraProductCost: BigDecimal? = 0.000.toBigDecimal()
                                extraProductCost = turnoverValues.variableAmountToPay?.let { remainingSize.times(it) }
                                applicationCost = extraProductCost?.let { turnoverValues.fixedAmountToPay?.plus(it) }
                                     if (permit.fmarkGenerated == 1) {
                                    stgAmt = applicationCost?.let { standardCost?.plus(inspectionCost!!)?.plus(it) }
                                    fmark = 1.toBigDecimal()
                                    fmarkCost = standardCost?.plus(inspectionCost!!)?.let { applicationMapProperties.mapQaFmarkAmountToPay.plus(it) }
                                    //Todo : ask if foreign cost will also be added as vat
                                    taxAmount = fmarkCost?.let { taxRate.times(it) }
                                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                                } else {
                                    KotlinLogging.logger { }.info { "second loop, ${permit.product}" }
                                    stgAmt = applicationCost?.let { standardCost?.plus(inspectionCost!!)?.plus(it) }
                                    fmark = 0.toBigDecimal()
//                                    taxAmount = stgAmt?.let {taxRate.times(it) }
                                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                                }
                                //                amountToPay = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                                KotlinLogging.logger { }.info { "Manufacturer turnover, $manufactureTurnOver" }
                                KotlinLogging.logger { }.info { "Total Amount To Pay   = " + amountToPay?.toDouble() }

                                m =   myReturPaymentValues(
                                    m,
                                    standardCost,
                                    inspectionCost,
                                    applicationCost,
                                    amountToPay,
                                    fmark,
                                    fmarkCost,
                                    taxAmount
                                )

                            }
                            else -> {
                                KotlinLogging.logger { }.info { "Turnover is less than 200, 000" }

                                //                val noOf = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory?.id)?.noOfPages))
                                val turnoverValues = iTurnOverRatesRepository.findByIdOrNull(applicationMapProperties.mapQASmarkJuakaliTurnOverId)?: throw ExpectedDataNotFound("MISSING TURNOVER RATES FOR Medium Firm SMARK")
                                val productList = user.id?.let { findPermitBYUserIDANDProductionStatus(map.inactiveStatus, applicationMapProperties.mapQAPermitTypeIdSmark, it) }?: throw ExpectedDataNotFound("MISSING USER ID")
                                val productSize = productList.size
                                val remainingSize = productSize.minus(applicationMapProperties.mapQaSmarkJuakaliMaxProduct).toBigDecimal()
                                var extraProductCost: BigDecimal? = 0.000.toBigDecimal()
                                extraProductCost = turnoverValues.variableAmountToPay?.let { remainingSize.times(it) }
                                applicationCost = extraProductCost?.let { turnoverValues.fixedAmountToPay?.plus(it) }
                                 if (permit.fmarkGenerated == 1) {
                                    stgAmt = applicationCost?.let { standardCost?.plus(inspectionCost!!)?.plus(it) }
                                    fmark = 1.toBigDecimal()
                                    fmarkCost = standardCost?.plus(inspectionCost!!)?.let { applicationMapProperties.mapQaFmarkAmountToPay.plus(it) }
                                    //Todo : ask if foreign cost will also be added as vat
                                    taxAmount = fmarkCost?.let { taxRate.times(it) }
                                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                                } else {
                                    KotlinLogging.logger { }.info { "second loop, ${permit.product}" }
                                    stgAmt = applicationCost?.let { standardCost?.plus(inspectionCost!!)?.plus(it) }
                                    fmark = 0.toBigDecimal()
//                                    taxAmount = stgAmt?.let {taxRate.times(it) }
                                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                                }
                                //                amountToPay = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                                KotlinLogging.logger { }.info { "Manufacturer turnover, $manufactureTurnOver" }
                                KotlinLogging.logger { }.info { "Total Amount To Pay   = " + amountToPay?.toDouble() }

                                m =   myReturPaymentValues(
                                    m,
                                    standardCost,
                                    inspectionCost,
                                    applicationCost,
                                    amountToPay,
                                    fmark,
                                    fmarkCost,
                                    taxAmount
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
                        applicationCost = applicationMapProperties.mapQaDmarkDomesticAmountToPay
                        stgAmt = standardCost?.plus(inspectionCost!!)?.let { applicationCost!!.plus(it) }
                        fmark = 0.toBigDecimal()
                        taxAmount = stgAmt?.let {taxRate.times(it) }
                        amountToPay = taxAmount?.let { stgAmt?.plus(it) }

                        m =  myReturPaymentValues(
                            m,
                            standardCost,
                            inspectionCost,
                            applicationCost,
                            amountToPay,
                            fmark,
                            fmarkCost,
                            taxAmount
                        )
                    }
                    applicationMapProperties.mapQaDmarkForeginStatus -> {
                        val foreignAmountCalculated = iMoneyTypeCodesRepo.findByTypeCode(applicationMapProperties.mapUssRateName)?.typeCodeValue?.toBigDecimal()
                        applicationCost = foreignAmountCalculated?.let { applicationMapProperties.mapQaDmarkForeginAmountToPay.times(it) }
                        stgAmt = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                        fmark = 0.toBigDecimal()
                        taxAmount = stgAmt?.let {taxRate.times(it) }
                        amountToPay = taxAmount?.let { stgAmt?.plus(it) }

                       m = myReturPaymentValues(
                            m,
                            standardCost,
                            inspectionCost,
                            applicationCost,
                            amountToPay,
                            fmark,
                            fmarkCost,
                            taxAmount
                        )
                    }
                }
            }
        }

        return m
    }

    private fun myReturPaymentValues(
        m: MutableList<BigDecimal?>,
        standardCost: BigDecimal?,
        inspectionCost: BigDecimal?,
        applicationCost: BigDecimal?,
        amountToPay: BigDecimal?,
        fmark: BigDecimal?,
        fmarkCost: BigDecimal?,
        taxAmount: BigDecimal?
    ): MutableList<BigDecimal?> {
        m.add(standardCost)
        m.add(inspectionCost)
        m.add(applicationCost)
        m.add(amountToPay)
        m.add(fmark)
        m.add(fmarkCost)
        m.add(taxAmount)

        return m
    }

}