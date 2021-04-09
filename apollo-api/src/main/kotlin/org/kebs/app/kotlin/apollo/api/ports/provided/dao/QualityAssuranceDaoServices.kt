package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import org.json.JSONObject
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.service.FuelInspectionCalculator
import org.kebs.app.kotlin.apollo.api.service.PaymentCalculator
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.exceptions.StorageException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.config.properties.storage.StorageProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class QualityAssuranceDaoServices(
    private val permitPaymentCal: PaymentCalculator,
    private val fuelInspectionCal: FuelInspectionCalculator,
    private val invoiceRepository: IInvoiceRepository,
    private val usersRepo: IUserRepository,
    private val permitRepo: IPermitRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val manufacturersRepo: IManufacturerRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val storageProperties: StorageProperties
//        ,
//        private val storageProperties: StorageProperties
//        ,
//        private val storageService: FileSystemStorageService
) {

    @Value("\${common.active.status}")
    lateinit var activeStatus: String

    @Value("\${common.inactive.status}")
    lateinit var inActiveStatus: String

    final var appId: Int? = null

    init {
        appId = applicationMapProperties.mapPermitApplication
    }

    fun serviceMapDetails(): ServiceMapsEntity {
        appId?.let {
            serviceMapsRepository.findByIdAndStatus(it, activeStatus.toInt())
                ?.let { s ->
                    return s
                }
                ?: throw ServiceMapNotFoundException("No service map found for appId=$it, aborting")
        }
            ?: throw ServiceMapNotFoundException("Empty and/or Invalid Application Id Received, aborting")
    }

    fun loggedInUserDetails(): UsersEntity {
        SecurityContextHolder.getContext().authentication?.name
            ?.let { username ->
                usersRepo.findByUserName(username)
                    ?.let { loggedInUser ->
                        return loggedInUser
                    }
                    ?: throw ExpectedDataNotFound("No userName with the following userName=$username, Exist in the users table")
            }
            ?: throw ExpectedDataNotFound("No user has logged in")
    }

    fun extractManufacturerEntityFromUser(user: UsersEntity): ManufacturersEntity {
        manufacturersRepo.findByUserId(user)
            ?.let { manufacture ->
                return manufacture
            }
            ?: throw ExpectedDataNotFound("No userName with the following userName=${user.userName}, Exist in the manufactures table")
    }

    fun permitSave(permits: PermitApplicationEntity, user: UsersEntity, s: ServiceMapsEntity): PermitApplicationEntity {
        with(permits) {
            manufacturerEntity = extractManufacturerFromUser(user)
            status = s.activeStatus
            createdBy = "${user.firstName} ${user.lastName}"
            createdOn = Timestamp.from(Instant.now())
        }
        return permitRepo.save(permits)
    }

//    fun updatePermitSave(permits: PermitApplicationEntity, user: UsersEntity, s: ServiceMapsEntity): PermitApplicationEntity {
//        with(permits) {
//            status = s.activeStatus
//            modifiedBy = "${user.firstName} ${user.lastName}"
//            modifiedOn = Timestamp.from(Instant.now())
//        }
//        return permitRepo.save(permits)
//    }

//    fun findByPermitId(permitID: Long): PermitApplicationEntity {
//        permitRepo.findByIdOrNull(permitID)
//                ?.let { permitDetails ->
//                    return permitDetails
//                }
//                ?: throw ExpectedDataNotFound("The following Permit Detail with ID = ${permitID}, Does not Exist")
//    }

    fun findByPermitId(permitID: Long): PermitApplicationEntity {
        permitRepo.findByIdOrNull(permitID)
            ?.let { permitDetails ->
                return permitDetails
            }
            ?: throw ExpectedDataNotFound("The following Permit Detail with ID = ${permitID}, Does not Exist")
    }

    fun findByPermitType(permitID: Long): PermitApplicationEntity {
        permitRepo.findByIdOrNull(permitID)
            ?.let { permitDetails ->
                return permitDetails
            }
            ?: throw ExpectedDataNotFound("The following Permit Detail with ID = ${permitID}, Does not Exist")
    }

    fun findAllByManufacture(user: UsersEntity): List<PermitApplicationEntity> {
        permitRepo.findByManufacturerIdOrderByIdDesc(
            extractManufacturerEntityFromUser(user).id
        )
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("The following Permit List for Manufacture with USERNAME = ${user.userName}, Does not Exist")
    }


    fun findAllByManufactureAndType(user: UsersEntity, type: Long): List<PermitApplicationEntity> {
        permitRepo.findByManufacturerIdAndPermitTypeOrderByIdDesc(
            extractManufacturerEntityFromUser(user).id, type
        )
            ?.let { permitList ->
                return permitList
            }
            ?: throw ExpectedDataNotFound("The following Permit List for Manufacture with USERNAME = ${user.userName}, Does not Exist")
    }

    fun updatePermit(permit: PermitApplicationEntity, user: UsersEntity, s: ServiceMapsEntity, permitID: Long): PermitApplicationEntity {
        // Getting an Object with fields that user Has Updated that are needed to be updated to the database
        JSONObject(ObjectMapper().writeValueAsString(permit))
            .let { addValues ->
                // Creating of a json object that can be user to map the details from Database with the updated fields from user
                JSONObject(ObjectMapper().writeValueAsString(findByPermitId(permitID)))
                    .let { JPermit ->
                        // Looping each field of the updated Entity to be updated
                        for (key in addValues.keys()) {
                            key.let { keyStr ->
                                // Checks if the field with the following Key is null or not Null (meaning it is the field that is updated)
                                if (addValues.isNull(keyStr as String?)) {
                                    KotlinLogging.logger { }.info { "MY null values key: $keyStr value: ${addValues.get(keyStr)}" }
                                }
                                // Field with Key Have values so we will update the  database value with the updated one
                                else {
                                    JPermit.remove(keyStr)
                                    JPermit.put(keyStr, addValues.get(keyStr))
                                    KotlinLogging.logger { }.info { "My values key: $keyStr value: ${addValues.get(keyStr)}" }
                                }
                            }
                        }
                        // Change the JPermit to an Entity to be saved
                        ObjectMapper().readValue(JPermit.toString(), PermitApplicationEntity::class.java)
                            .let { updatePermit ->
                                with(updatePermit) {
                                    modifiedBy = "${user.firstName} ${user.lastName}"
                                    modifiedOn = Timestamp.from(Instant.now())
                                }
                                KotlinLogging.logger { }.info { "MY Permit ID =  ${updatePermit.id}" }
                                return permitRepo.save(updatePermit)
                            }

                    }
            }
    }


    /*****************************************************************************************************************************************
     *
     *
     *
     * OLD CODES IN DAO
     *
     *
     *
     *
     *
     *
     *****************************************************************************************************************************************/
//    private final var uploadDirectory = storageProperties.uploadDirectory
    /**
     * TODO: file upload
     */

    //private final var uploadDirectory = System.getProperty("user.dir") + "/uploads"
    private final var uploadDirectory = storageProperties.uploadDirectory
    //    private final var uploadDirectory = "C:\\Users\\thor\\Desktop\\BSK\\apollo\\uploads"
    fun extractUserFromContext(context: SecurityContext?): UsersEntity? {
        context?.authentication?.name
            ?.let { userName ->
                return usersRepo.findByUserName(userName)
            }
            ?: return null
    }

    fun extractManufacturerFromUser(usersEntity: UsersEntity?): ManufacturersEntity? {
        usersEntity
            ?.let {
                return manufacturersRepo.findByUserId(it)
            }
            ?: return null

    }

    fun invoiceGen(invoice: InvoiceEntity, permits: PermitApplicationEntity?, fuelInspectionEntity: PetroleumInstallationInspectionEntity?, entity: ManufacturersEntity?,  appId: Int?): InvoiceEntity? {
        serviceMapsRepository.findByIdOrNull(appId)
            ?.let { map ->
                with(invoice) {

                    /**
                     * Get rid of hard coded data
                     */
                    conditions = "Must be paid in 30 days"
                    createdOn = Timestamp.from(Instant.now())
                    status = 0
                    map.tokenExpiryHours?.let { expiryDate = Timestamp.from(Instant.now().plus(it, ChronoUnit.HOURS)) }


                    if(permits != null){
                        signature = entity?.userId?.firstName
                        createdBy = entity?.userId?.firstName
                        val generatedPayments = permits.let { permitPaymentCal.calculatePayment(it) }
                        amount = generatedPayments[3]
                        applicationCost = generatedPayments[2]
                        val cost: BigDecimal? = generatedPayments[0]
                        standardCost = cost
                        inspectionCost = generatedPayments[1]
                        fmarkStatus = generatedPayments[4]
                        fmarkCost = generatedPayments[5]
                        tax = generatedPayments[6]
                        manufacturer = entity?.id
                        businessName = entity?.name
                        signature = entity?.name
                        goods = permits.saveReason
                        permitId = permits.id
                    }
                    else if (fuelInspectionEntity != null) {
                        val fuelPayment = fuelInspectionCal.calculateFuelInspectionCost(fuelInspectionEntity)
                        amount = fuelPayment[0]
                        fuelInspectionCost = fuelPayment[1]
                        tax = fuelPayment[2]
                        businessName = fuelInspectionEntity.stationName
                        installationInspectionId = fuelInspectionEntity
                        createdBy = fuelInspectionEntity.contactEmail
                        signature = fuelInspectionEntity.contactEmail
                        /**
                         * I'll get rid of this static data
                         */
                        goods = "Fuel Inspection"
                    }



                }
                return invoiceRepository.save(invoice)

            }
            ?: throw ServiceMapNotFoundException("Application configuration map [id=$appId ]not found check configuration")

    }

    fun saveDocuments(labReportDocumentEntity: LabReportDocumentsEntity?, sampleCollected: SampleCollectionDocumentsEntity?, sampleSubmitted: SampleSubmissionDocumentsEntity?, pvocWaiversApplicationEntity: PvocWaiversApplicationEntity?,docFile: MultipartFile): String {

        val docFileName: String = StringUtils.cleanPath(docFile.originalFilename!!)
        if (docFileName.contains("..")) {
            throw StorageException("Sorry! Filename contains invalid path sequence: compliant attached Document file = $docFileName")
        }
        return docFileName
    }

    //    fun storeFile(file: MultipartFile) {
//        val storageService = FileSystemStorageService(storageProperties)
//        storageService.store(file)
////        TODO("Not yet implemented")
//    }
    fun storeFile(file: MultipartFile) {
        return Paths.get(uploadDirectory, file.originalFilename)
            .let { fl ->
                Files.write(fl, file.bytes)
            }
    }

    fun storeFiles(files: Array<MultipartFile>) {
        for (file in files){
            try {
                file?.let{ file->
                    file.originalFilename.toString()?.let{ originalFilename->
                        if (originalFilename.isNotEmpty()){
                            Files.write(Paths.get(uploadDirectory, originalFilename), file.bytes)
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun storagePath(path: String?): String {
//        val storageService = FileSystemStorageService(storageProperties)
//        return "${storageService.rootLocation?.toUri()}/$path}"
        return ""
    }
}