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

package org.kebs.app.kotlin.apollo.api.ports.provided.dao


import com.ctc.wstx.api.WstxInputProperties
import com.ctc.wstx.api.WstxOutputProperties
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.common.io.Files
import com.google.gson.Gson
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.json.JSONObject
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.RegistrationEmailDTO
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.RegistrationForEntryNumberEmailDTO
import org.kebs.app.kotlin.apollo.api.ports.provided.sms.SmsServiceImpl
import org.kebs.app.kotlin.apollo.common.dto.CustomResponse
import org.kebs.app.kotlin.apollo.common.dto.HashedStringDto
import org.kebs.app.kotlin.apollo.common.dto.SectionsDto
import org.kebs.app.kotlin.apollo.common.dto.UserEntityDto
import org.kebs.app.kotlin.apollo.common.exceptions.*
import org.kebs.app.kotlin.apollo.common.utils.composeUsingSpel
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.common.utils.placeHolderMapper
import org.kebs.app.kotlin.apollo.common.utils.replacePrefixedItemsWithObjectValues
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdLaboratoryEntity
import org.kebs.app.kotlin.apollo.store.model.qa.ManufacturePlantDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileCommoditiesManufactureEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileContractsUndertakenEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileDirectorsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.math.BigDecimal
import java.net.URLConnection
import java.security.SecureRandom
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoUnit
import java.util.*
import javax.activation.MimetypesFileTypeMap
import javax.servlet.http.HttpServletResponse
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory


@Service
class CommonDaoServices(
    private val jasyptStringEncryptor: StringEncryptor,
    private val usersRepo: IUserRepository,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val workPlanYearsCodesRepo: IWorkplanYearsCodesRepository,
    private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
    private val batchJobRepository: IBatchJobDetailsRepository,
    private val iSubSectionsLevel2Repo: ISubSectionsLevel2Repository,
    private val iSubSectionsLevel1Repo: ISubSectionsLevel1Repository,
    private val configurationRepository: IIntegrationConfigurationRepository,
    private val workflowTransactionsRepository: IWorkflowTransactionsRepository,
    private val iUserProfilesRepo: IUserProfilesRepository,
    private val iImporterRepo: IImporterContactRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val verificationTokensRepo: IUserVerificationTokensRepository,
    private val iUserRepository: IUserRepository,
    private val emailVerificationTokenEntityRepo: EmailVerificationTokenEntityRepo,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val countriesRepository: ICountriesRepository,
    private val notifications: Notifications,
    private val directorateRepo: IDirectoratesRepository,
    private val businessLinesRepo: IBusinessLinesRepository,
    private val businessNatureRepo: IBusinessNatureRepository,
    private val notificationsRepo: INotificationsRepository,
    private val notificationsBufferRepo: INotificationsBufferRepository,
    private val manufacturerContactDetailsRepository: IManufacturerContactsRepository,
    private val manufacturersRepo: IManufacturerRepository,
    private val iDivisionsRepo: IDivisionsRepository,
    private val designationRepo: IDesignationsRepository,
    private val iSectionsRepo: ISectionsRepository,
    private val departmentRepo: IDepartmentsRepository,
    private val regionsRepo: IRegionsRepository,
    private val countiesRepo: ICountiesRepository,
    private val townsRepo: ITownsRepository,
    private val userTypesRepo: IUserTypesEntityRepository,
    private val companyProfileDirectorsRepo: ICompanyProfileDirectorsRepository,
    private val companyProfileCommoditiesManufactureRepo: ICompanyProfileCommoditiesManufactureRepository,
    private val companyProfileContractsUndertakenRepo: ICompanyProfileContractsUndertakenRepository,

    private val countyRepo: ICountiesRepository,
    private val standardCategoryRepo: IStandardCategoryRepository,
    private val productCategoriesRepository: IKebsProductCategoriesRepository,
    private val broadProductCategoryRepository: IBroadProductCategoryRepository,
    private val productsRepo: IProductsRepository,
    private val productSubCategoryRepo: IProductSubcategoryRepository,

    private val iProcessesStagesRepo: IProcessesStagesRepository,
    private val iLaboratoryRepo: ILaboratoryRepository,

    private val bufferRepo: INotificationsBufferRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val smsService: SmsServiceImpl,
) {

    @Value("\${common.page.view.name}")
    lateinit var viewPage: String

    @Value("\${common.active.status}")
    lateinit var activeStatus: String

    @Value("\${common.inactive.status}")
    lateinit var inActiveStatus: String

    @Value("\${destination.inspection.division.id}")
    lateinit var pointOfEntries: String

    val successLink = "redirect:/api/auth/signup/notification/success/message"

    //Deserialize XML to POJO
    final inline fun <reified T> deserializeFromXML(xmlString: String, toExclude: String = ""): T {
        try {
            var trimmedString = xmlString
            if (toExclude.isNotEmpty()) {
                trimmedString = xmlString.replace(toExclude, "")
            }
            return xmlMapper.readValue(trimmedString, T::class.java)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred with xml deserialization", e)
            throw RuntimeException("An error occurred while deserializing xml")
        }
    }

    fun serializeToXml(fileName: String, obj: Any): File {
        try {
            val xmlString = xmlMapper.writeValueAsString(obj)
            KotlinLogging.logger { }.info(":::::: The XML String: $xmlString :::::::")

//            val targetFile = File(fileName)
            val targetFile = File(Files.createTempDir(), fileName)
            targetFile.deleteOnExit()

            val fileWriter = FileWriter(targetFile)
            fileWriter.write(xmlString)
            fileWriter.close()

            return targetFile
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred with xml serialization", e)
            throw RuntimeException("An error occurred while serializing xml")
        }
    }

    fun convertTimestampToKeswsValidDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = sdf.format(timestamp)
        return date
    }

    fun convertDateToSAGEDate(dateChange: Date): String {
        val sdf = SimpleDateFormat("mm/dd/yyyy")
        return sdf.format(dateChange)
    }

    fun convertDateToString(date: Date, format: String): String {
        val format = SimpleDateFormat(format)
        return format.format(date)
    }

    fun convertISO8601DateToTimestamp(dateString: String): Timestamp? {
        try {
            val formatter = DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_DATE_TIME)
                .toFormatter()
            val instant = LocalDateTime.parse(dateString, formatter)
            return Timestamp.valueOf(instant)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Error occurred while converting ISO8601 to Timestamp", e)
        }
        return null
    }

    fun findWorkPlanYearsCodesEntity(currentYear: String, map: ServiceMapsEntity): WorkplanYearsCodesEntity {
        workPlanYearsCodesRepo.findByYearNameAndStatus(currentYear, map.activeStatus)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Workplan Years Codes with [status=$map.activeStatus], do Not Exists")
    }

    fun convertStringAmountToBigDecimal(amount: String): BigDecimal? {
        try {
            return amount.toBigDecimal()
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Error occurred while converting String amount: $amount to BigDecimal", e)
        }
        return null
    }

    fun createKesWsFileName(filePrefix: String, documentIdentifier: String): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyymmddhhmmss")
        val formatted = current.format(formatter)
        KotlinLogging.logger { }.info(":::::: Formatted datetime: $formatted :::::::")

        //TODO: Add static fields to config file
        var finalFileName = filePrefix
            .plus("-")
            .plus(documentIdentifier)
            .plus("-1-B-")
            .plus(formatted)
            .plus(".xml")
        finalFileName = finalFileName.replace("\\s".toRegex(), "")

        KotlinLogging.logger { }.info(":::::: Final filename: $finalFileName :::::::")

        return finalFileName
    }

    fun getFileType(imageBytes: ByteArray): String {
        return URLConnection.guessContentTypeFromStream(ByteArrayInputStream(imageBytes))
    }

    fun findBatchJobDetails(batchJobID: Long): BatchJobDetails {
        batchJobRepository.findByIdOrNull(batchJobID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Batch Job Details With the following ID $batchJobID, does not exist")
    }

    fun companyDirectorList(companyID: Long): List<CompanyProfileDirectorsEntity> {
        companyProfileDirectorsRepo.findByCompanyProfileId(companyID)?.let {
            return it
        }
            ?: throw ExpectedDataNotFound("Directors details for company with  ID $companyID, does not exist")
    }

    companion object {
        val xmlMapper: ObjectMapper = run {
            val iFactory: XMLInputFactory = WstxInputFactory()
            iFactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, 32000)
            val oFactory: XMLOutputFactory = WstxOutputFactory()
            oFactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, true)
            val xf = XmlFactory(iFactory, oFactory)
            val xmlMapper: ObjectMapper = XmlMapper(xf)
                .registerModule(KotlinModule())
//        xmlMapper.registerModule(JodaModule())
            xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
            xmlMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)
            xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            xmlMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            xmlMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
            /**
             * This line causes an exception to be thrown if the test has more than one thread!
             */
//        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
            xmlMapper
        }
    }

    fun hashString(plainText: List<HashedStringDto>): List<HashedStringDto> {
        val hashedList = mutableListOf<HashedStringDto>()
        plainText.forEach {
            val hashed = jasyptStringEncryptor.encrypt(it.stringHashed)
            KotlinLogging.logger { }.info { "my hashed value =$it =  $hashed" }
            hashedList.add(HashedStringDto(hashed))
        }
        return hashedList
    }

    fun findAllPlantDetails(userId: Long): List<ManufacturePlantDetailsEntity> {
        manufacturePlantRepository.findByUserId(userId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Plant details found with the following [user id=$userId]")
    }

    fun convertClassToJson(classToConvert: Any): String? {
        val gson = Gson()
        return gson.toJson(classToConvert)
    }

    fun getUserTypeDetails(usertypeID: Long): UserTypesEntity {
        userTypesRepo.findByIdOrNull(usertypeID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("User Type Details with [ID=${usertypeID}] , does not exist")
    }

    fun findProcesses(map: Int): ProcessesStagesEntity {
        iProcessesStagesRepo.findByServiceMapId(map)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Processes with [Service Map=${map}] , does not exist")
    }

    fun findAllContractsUnderTakenDetails(companyProfileID: Long): List<CompanyProfileContractsUndertakenEntity> {
        companyProfileContractsUndertakenRepo.findByCompanyProfileId(companyProfileID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("Company Details Does not exist")
    }

    fun findAllCommoditiesDetails(companyProfileID: Long): List<CompanyProfileCommoditiesManufactureEntity> {
        companyProfileCommoditiesManufactureRepo.findByCompanyProfileId(companyProfileID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("Company Details Does not exist")
    }

    fun findAllFreightStationOnPortOfArrival(
        sectionsEntity: SectionsEntity,
        status: Int
    ): List<SubSectionsLevel2Entity> {
        iSubSectionsLevel2Repo.findBySectionIdAndStatus(sectionsEntity, status)
            ?.let {
                return it
            }
            ?: throw Exception("CFS Stations With section ID  = ${sectionsEntity.id} and status = ${status}, does not Exist")

    }

    fun findIntegrationConfigurationEntity(configID: Long): IntegrationConfigurationEntity {
        configurationRepository.findByIdOrNull(configID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Configuration With the following ID $configID, does not exist")
    }

    fun findManufactureWithID(manufactureID: Long): ManufacturersEntity {
        manufacturersRepo.findByIdOrNull(manufactureID)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Manufacture With the following ID $manufactureID, does not exist")
    }

    fun getExpirationTime(expirationTime: Int): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.HOUR, expirationTime)
        return Timestamp(calendar.timeInMillis)
    }

    fun randomNumber(digitSize: Int): String {
        val random = SecureRandom()
        val num: Int = random.nextInt(100000)
        return String.format("%0${digitSize}d", num)
    }

    fun failedStatusDetails(sr: ServiceRequestsEntity): String {
        return "ERROR STATUS CODE= ${sr.responseStatus} AND MESSAGE = ${sr.responseMessage}"
    }

    fun findOfficersListBasedOnRegionCountyAndRole(
        roleId: Long,
        countyId: Long,
        regionId: Long
    ): List<UsersEntity>? {

        return usersRepo.findOfficerUsersByRegionAndCountyAndRoleFromUserDetails(roleId, countyId, regionId, 1)
    }


    fun serviceMapDetails(appId: Int): ServiceMapsEntity {
        serviceMapsRepository.findByIdAndStatus(appId, activeStatus.toInt())
            ?.let { s ->
                return s
            }
            ?: throw ServiceMapNotFoundException("No service map found for appId=$appId, aborting")

    }

    fun makeAnyNotBeNull(anyValue: Any): Any {
        return anyValue
    }


    class MessageSuccessFailDTO {

        var message: String? = null

        var closeLink: String? = null
    }


    fun loadCommonUIComponents(s: ServiceMapsEntity): MutableMap<String, Any> {

        return mutableMapOf(
            Pair("activeStatus", s.activeStatus),
            Pair("inActiveStatus", s.inactiveStatus),
            Pair("currentDate", getCurrentDate())
//                Pair("CDStatusTypes", daoServices.findCdStatusValueList(s.activeStatus))
        )
    }

    fun loadNotificationsUIComponents(s: ServiceMapsEntity, loggedInUserEMail: String): MutableMap<String, Any> {

        return mutableMapOf(
            Pair("notifications", findAllUserNotification(loggedInUserEMail))

//                Pair("CDStatusTypes", daoServices.findCdStatusValueList(s.activeStatus))
        )
    }


    //    fun assignUserType(userType: Long?): UserTypesEntity? = iUserTypesRepo.findByIdOrNull(userType)
    fun assignCounty(countyId: Long?): CountiesEntity? = countiesRepo.findByIdOrNull(countyId)
    fun assignTown(townId: Long?): TownsEntity? = townsRepo.findByIdOrNull(townId)

    //    fun assignDepartment(departmentId: Long?): DepartmentsEntity? = departmentsRepo.findByIdOrNull(departmentId)
    fun assignDirectorate(directorateId: Long?): DirectoratesEntity? = directorateRepo.findByIdOrNull(directorateId)

    //    fun assignDivision(divisionId: Long?): DivisionsEntity? = divisionsRepo.findByIdOrNull(divisionId)
//    fun assignSubRegion(subRegionId: Long?): SubRegionsEntity? = subRegionsRepo.findByIdOrNull(subRegionId)
    fun assignDesignation(designationId: Long?): DesignationsEntity? = designationRepo.findByIdOrNull(designationId)

    fun returnValues(
        result: ServiceRequestsEntity,
        map: ServiceMapsEntity,
        sm: MessageSuccessFailDTO
    ): String? {
        return when (result.status) {
            map.successStatus -> "${successLink}?message=${sm.message}&closeLink=${sm.closeLink}"
            else -> map.failureNotificationUrl
        }
    }

    fun convertMultipartFileToFile(file: MultipartFile): File {
        val convFile = File(file.originalFilename)
        convFile.createNewFile()
        val fos = FileOutputStream(convFile)
        fos.write(file.bytes)
        fos.close()
        return convFile
    }

    fun convertFileToMultipartFile(file: File): MultipartFile {
        return MockMultipartFile(file.name, FileInputStream(file))
    }

    fun concatenateName(user: UsersEntity): String {
        return "${user.firstName} ${user.lastName}"
    }

    fun createJsonBodyFromEntity(entitySaved: Any): String? {
        val gson = Gson()
        return gson.toJson(entitySaved)
    }

    fun concatenateName(firstName: String, lastName: String): String {
        return "$firstName ${lastName}"
    }

    fun getUserName(user: UsersEntity): String {
        return "${user.userName}"
    }

    fun getTimestamp(): Timestamp {
        return Timestamp.from(Instant.now())
    }

    fun saveDocuments(docFile: MultipartFile): String? {

        return try {
            docFile.originalFilename?.let { StringUtils.cleanPath(it) }
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            e.message
        }
    }

    class FileDTO {
        var fileType: String? = null
        var name: String? = null
        var document: ByteArray? = null

        override fun toString(): String {
            return "fileType=$fileType, name=$name, document=$document"
        }
    }

    fun mapClass(mappingClass: Any): FileDTO {
        val modelMapper = ModelMapper()
        return modelMapper.map(mappingClass, FileDTO::class.java)
    }

    fun userRegisteredSuccessfulEmailCompose(
        user: UsersEntity,
        sr: ServiceRequestsEntity,
        map: ServiceMapsEntity,
        token: String?
    ): RegistrationEmailDTO {
        val dataValue = RegistrationEmailDTO()
        with(dataValue) {
            baseUrl = applicationMapProperties.baseUrlValue
            fullName = concatenateName(user)
            dateSubmitted = user.registrationDate
            modifiedON = user.modifiedOn
            otpGenerated = if (token.isNullOrEmpty()) generateVerificationToken(sr, user, map)?.token else token
            otpGeneratedDate = getCurrentDate()

        }

        return dataValue
    }

    fun userRegisteredEntryNumberSuccessfulEmailCompose(
        companyProfile: CompanyProfileEntity,
        sr: ServiceRequestsEntity,
        map: ServiceMapsEntity,
        token: String?
    ): RegistrationForEntryNumberEmailDTO {
        val dataValue = RegistrationForEntryNumberEmailDTO()
        with(dataValue) {
            baseUrl = applicationMapProperties.baseUrlValue
            fullName =
                concatenateName(findUserByID(companyProfile.userId ?: throw ExpectedDataNotFound("USER ID NOT FOUND")))
            entryNumber = companyProfile.entryNumber
            dateSubmitted = getCurrentDate()

        }

        return dataValue
    }


    fun sendEmailAfterCompose(
        user: UsersEntity,
        emailTemplateUuid: String,
        emailEntity: Any,
        appID: Int,
        payload: String
    ) {
        val map = serviceMapDetails(appID)
        val sr = mapServiceRequestForSuccess(map, payload, user)
        user.email?.let { sendEmailWithUserEmail(it, emailTemplateUuid, emailEntity, map, sr) }
//        user.email?.let { commonDaoServices.sendEmailWithUserEmail(it, applicationMapProperties.mapMsComplaintAcknowledgementRejectionWIthOGANotification, userRegisteredSuccessfulEmailCompose(user), commonDaoServices.serviceMapDetails(appId), commonDaoServices.mapServiceRequestForSuccess(map, payload, user)) }
    }

    fun downloadFile(response: HttpServletResponse, doc: FileDTO) {
        response.contentType = doc.fileType
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${doc.name};")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(doc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")
    }

    fun getFileTypeByMimetypesFileTypeMap(fileName: String?): String? {
        val fileTypeMap = MimetypesFileTypeMap()
        return fileTypeMap.getContentType(fileName)
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

    fun checkLoggedInUser(): String? {
        return SecurityContextHolder.getContext().authentication?.name
    }


    fun loggedInUserAuthentication(): Authentication {
        SecurityContextHolder.getContext().authentication
            ?.let { auths ->
                return auths
            }
            ?: throw ExpectedDataNotFound("No user has logged in")
    }

    fun findAllSectionsListWithDivision(division: DivisionsEntity, status: Int): List<SectionsEntity> {
        iSectionsRepo.findByDivisionIdAndStatus(division, status)
            ?.let { entryPointDetails ->
                return entryPointDetails
            }
            ?: throw Exception("Entry Point Details with division id = ${division.id} and Status = ${status}, do not Exist")
    }


    fun findDivisionWIthId(divisionId: Long): DivisionsEntity {
        iDivisionsRepo.findByIdOrNull(divisionId)
            ?.let { division ->
                return division
            }
            ?: throw ExpectedDataNotFound("Division with id = ${divisionId}, does not Exist")
    }

    fun findSectionWIthId(sectionId: Long): SectionsEntity {
        iSectionsRepo.findByIdOrNull(sectionId)
            ?.let { section ->
                return section
            }
            ?: throw ExpectedDataNotFound("Section with id = ${sectionId}, does not Exist")
    }

    fun findSectionLevel1WIthId(sectionL1Id: Long): SubSectionsLevel1Entity {
        iSubSectionsLevel1Repo.findByIdOrNull(sectionL1Id)
            ?.let { sectionL1 ->
                return sectionL1
            }
            ?: throw ExpectedDataNotFound("Section Level 1 with id = ${sectionL1Id}, does not Exist")
    }

    fun findSectionLevel2WIthId(sectionL2Id: Long): SubSectionsLevel2Entity {
        iSubSectionsLevel2Repo.findByIdOrNull(sectionL2Id)
            ?.let { sectionL2 ->
                return sectionL2
            }
            ?: throw ExpectedDataNotFound("Section Level 2 with id = ${sectionL2Id}, does not Exist")
    }

    fun findCountryList(): List<CountriesEntity> {
        countriesRepository.findByStatus(activeStatus.toInt())
            ?.let { CountriesList ->
                return CountriesList
            }
            ?: throw ExpectedDataNotFound("Country List with status = ${activeStatus.toInt()}, do not Exist")
    }


    fun findUserProfileWithDesignationRegionDepartmentAndStatus(
        designationsEntity: DesignationsEntity,
        regionsEntity: RegionsEntity,
        departmentsEntity: DepartmentsEntity,
        status: Int
    ): UserProfilesEntity {
        iUserProfilesRepo.findByDesignationIdAndRegionIdAndDepartmentIdAndStatus(
            designationsEntity,
            regionsEntity,
            departmentsEntity,
            status
        )
            ?.let { userProfile ->
                return userProfile
            }
            ?: throw ExpectedDataNotFound("No user Profile Matched the following details [designation id = ${designationsEntity.id}] and [region id = ${regionsEntity.id}]and [department id = ${departmentsEntity.id}] and [status = $status]")
    }

    fun findUserProfileWithDesignationRegionDepartmentAndStatusAndSection(
        designationsEntity: DesignationsEntity,
        sectionsEntity: SectionsEntity,
        regionsEntity: RegionsEntity,
        departmentsEntity: DepartmentsEntity,
        status: Int
    ): UserProfilesEntity {
        iUserProfilesRepo.findByRegionIdAndDepartmentIdAndStatusAndSectionIdAndDesignationId(
            regionsEntity,
            departmentsEntity,
            status,
            sectionsEntity,
            designationsEntity
        )?.let { userProfile ->
            return userProfile
        }
            ?: throw ExpectedDataNotFound("No user Profile Matched the following details [sections name = ${sectionsEntity.section}] and [region name = ${regionsEntity.region}]and [department id = ${departmentsEntity.department}] and [status = $status]")
    }

    fun findUserProfileWithDesignationAndStatus(
        designationsEntity: DesignationsEntity,
        status: Int
    ): UserProfilesEntity {
        iUserProfilesRepo.findByDesignationIdAndStatus(designationsEntity, status)
            ?.let { userProfile ->
                return userProfile
            }
            ?: throw ExpectedDataNotFound("No user Profile Matched the following details [designation id = ${designationsEntity.id}] and [status = $status]")
    }

    fun findAllUsersProfileWithDesignationAndStatus(
        designationsEntity: DesignationsEntity,
        status: Int
    ): List<UserProfilesEntity> {
        iUserProfilesRepo.findAllByDesignationIdAndStatus(designationsEntity, status)
            ?.let { users ->
                return users
            }
            ?: throw ExpectedDataNotFound("No user Profile Matched the following details [designation id = ${designationsEntity.id}] and [status = $status]")
    }

    fun findAllUsersWithDesignationRegionDepartmentAndStatus(
        designationsEntity: DesignationsEntity,
        regionsEntity: RegionsEntity,
        departmentsEntity: DepartmentsEntity,
        status: Int
    ): List<UserProfilesEntity> {
        iUserProfilesRepo.findAllByDesignationIdAndRegionIdAndDepartmentIdAndStatus(
            designationsEntity,
            regionsEntity,
            departmentsEntity,
            status
        )
            ?.let { users ->
                return users
            }
            ?: throw ExpectedDataNotFound("No users Profile Matched the following details [designation id = ${designationsEntity.id}] and [region id = ${regionsEntity.id}]and [department id = ${departmentsEntity.id}] and [status = $status]")
    }

    fun findAllUsersWithDesignationRegionDepartmentSectionAndStatus(
        designationsEntity: DesignationsEntity,
        regionsEntity: RegionsEntity,
        sectionsEntity: SectionsEntity,
        departmentsEntity: DepartmentsEntity,
        status: Int
    ): List<UserProfilesEntity> {
        iUserProfilesRepo.findAllByDesignationIdAndRegionIdAndDepartmentIdAndStatusAndSectionId(
            designationsEntity,
            regionsEntity,
            departmentsEntity,
            status,
            sectionsEntity
        )
            ?.let { users ->
                return users
            }
            ?: throw ExpectedDataNotFound("No users Profile Matched the following details [designation id = ${designationsEntity.id}] and [region id = ${regionsEntity.id}]and [department id = ${departmentsEntity.id}] and [status = $status]")
    }


    fun findRegionEntityByRegionID(regionsId: Long, status: Int): RegionsEntity {
        regionsRepo.findByIdAndStatus(regionsId, status)
            ?.let { regionEntity ->
                return regionEntity
            }
            ?: throw ExpectedDataNotFound("The following Region with ID  = $regionsId and status = $status, does not Exist")
    }

    fun findCountiesEntityByCountyId(countyId: Long, status: Int): CountiesEntity {
        countiesRepo.findByIdAndStatus(countyId, status)
            ?.let { countyEntity ->
                return countyEntity
            }
            ?: throw ExpectedDataNotFound("The following County with ID  = $countyId and status = $status, does not Exist")
    }

    fun findTownEntityByTownId(townId: Long): TownsEntity {
        townsRepo.findByIdOrNull(townId)
            ?.let { townEntity ->
                return townEntity
            }
            ?: throw ExpectedDataNotFound("The following Town with ID  = $townId, does not Exist")
    }


    fun findRegionListByStatus(status: Int): List<RegionsEntity> {

        regionsRepo.findByStatusOrderByRegion(status)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Region List with status = $status, does not Exist")
    }

    fun findCountyListByStatus(status: Int): List<CountiesEntity> {
        countiesRepo.findByStatusOrderByCounty(status)

            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No county List with status = $status, does not Exist")
    }

    fun findCountyListByRegion(regionsId: Long, status: Int): List<CountiesEntity> {
        countiesRepo.findByRegionIdAndStatus(regionsId, status)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No County List with Region ID  = $regionsId and status = $status, does not Exist")
    }

    fun findTownListByCountyID(countiesEntity: CountiesEntity, status: Int): List<TownsEntity> {
        townsRepo.findByCountiesAndStatus(countiesEntity, status)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Town List with ID county ID = ${countiesEntity.id} and status = $status, does not Exist")
    }

    fun findBusinessLineEntityByID(businessLineId: Long, status: Int): BusinessLinesEntity {
        businessLinesRepo.findByIdAndStatus(businessLineId, status)
            ?.let { businessLineEntity ->
                return businessLineEntity
            }
            ?: throw ExpectedDataNotFound("No BusinessLinesEntity with ID  = $businessLineId and status = $status, does not Exist")
    }

    fun findBusinessNatureEntityByID(businessNatureId: Long, status: Int): BusinessNatureEntity {
        businessNatureRepo.findByIdAndStatus(businessNatureId, status)
            ?.let { businessNatureEntity ->
                return businessNatureEntity
            }
            ?: throw ExpectedDataNotFound("No BusinessNatureEntity with ID  = $businessNatureId and status = $status, does not Exist")
    }

    fun findBusinessLineListByStatus(businessLineId: Long, status: Int): List<BusinessLinesEntity> {
        businessLinesRepo.findByStatusOrderByName(status)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Business Line List with status = $status, does not Exist")
    }

    fun findBusinessNatureListByBusinessNatureLine(
        businessLineEntity: BusinessLinesEntity,
        status: Int
    ): List<BusinessNatureEntity> {
        businessNatureRepo.findByBusinessLinesIdAndStatus(businessLineEntity, status)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("No Business Nature with Business Line of ID  = ${businessLineEntity.id} and status = $status, Existing")
    }


    fun findUserByUserName(userName: String): UsersEntity {
        usersRepo.findByUserName(userName)
            ?.let { userEntity ->
                return userEntity
            }
            ?: throw ExpectedDataNotFound("Username  = ${userName}, does not Exist")
    }


    fun findCompanyProfile(userID: Long): CompanyProfileEntity {
        companyProfileRepo.findByUserId(userID)
            ?.let { userCompanyDetails ->
                return userCompanyDetails
            }
            ?: throw ExpectedDataNotFound("Company Profile with [user ID= ${userID}], does not Exist")
    }

    fun findCompanyProfileWithRegistrationNumber(registrationNumber: String): CompanyProfileEntity? {
        return companyProfileRepo.findByRegistrationNumber(registrationNumber)
//                ?.let { userCompanyDetails ->
//                     userCompanyDetails
//                }
    }

    fun findCompanyProfileWithKraPin(kraPin: String): CompanyProfileEntity? {
        return companyProfileRepo.findByKraPin(kraPin)
//                ?.let { userCompanyDetails ->
//                     userCompanyDetails
//                }
    }

    fun findCompanyProfileWhoAreManufactures(status: Int): List<CompanyProfileEntity> {
        companyProfileRepo.findByManufactureStatus(status)
            ?.let { userCompanyDetails ->
                return userCompanyDetails
            }
            ?: throw ExpectedDataNotFound("Company Profile list with [user ID= ${status}], does not Exist")
    }

    fun findCompanyProfileWithID(id: Long): CompanyProfileEntity {
        companyProfileRepo.findByIdOrNull(id)
            ?.let { userCompanyDetails ->
                return userCompanyDetails
            }
            ?: throw ExpectedDataNotFound("Company Profile with ID= ${id}, does not Exist")
    }


    fun findAllUsers(): List<UsersEntity> {
        usersRepo.findAllByOrderByIdAsc()
            .let { usersEntity ->
                return usersEntity
            }

    }

    fun removeKeyAndUpdateValueJsonObject(jsonObject: JSONObject, keyRemove: String, valueUpdate: Any): JSONObject {
        with(jsonObject) {
            remove(keyRemove)
            put(keyRemove, valueUpdate)
        }
        return jsonObject
    }

    fun updateDetails(updatedDetails: Any, detailToUpdate: Any): Any {
//        updatedDetails.id = updateId
        // Getting an Object with fields that user Has Updated that are needed to be updated to the database
        JSONObject(ObjectMapper().writeValueAsString(updatedDetails))
            .let { addValues ->
                // Creating of a json object that can be user to map the details from Database with the updated fields from user
                JSONObject(ObjectMapper().writeValueAsString(detailToUpdate))
                    .let { JCD ->
                        // Looping each field of the updated Entity to be updated
                        for (key in addValues.keys()) {
                            key.let { keyStr ->
                                // Checks if the field with the following Key is null or not Null (meaning it is the field that is updated)
                                when {
                                    addValues.isNull(keyStr) -> {
                                        //Todo remove the logger
//                                        KotlinLogging.logger { }.info { "MY null values key: $keyStr value: ${addValues.get(keyStr)}" }
                                    }
                                    else -> {
                                        removeKeyAndUpdateValueJsonObject(JCD, keyStr, addValues.get(keyStr))
                                        KotlinLogging.logger { }
                                            .info { "My values key: $keyStr value: ${addValues.get(keyStr)}" }
                                    }
                                }
                            }
                        }
                        // Change the JCD to an Entity to be saved
                        ObjectMapper().readValue(JCD.toString(), detailToUpdate::class.java)
                            .let { updateDetail ->
                                return updateDetail
                            }

                    }
            }
    }

    fun getCurrentDate(): Date {
        return Date(Date().time)
    }

    fun addMonthsToCurrentDate(noOfMonths: Long): Date {
        return Date.valueOf(LocalDate.now().plusMonths(noOfMonths))
    }

    fun addYearsToCurrentDate(noOfYears: Long): Date {
        return Date.valueOf(LocalDate.now().plusYears(noOfYears))
    }

    fun addYearsToDate(dateValue: Date, noOfYears: Long): Date {
        return Date.valueOf(dateValue.toLocalDate().plusYears(noOfYears))
    }

    fun addYDayToDate(dateValue: Date, noOfDays: Long): Date {
        return Date.valueOf(dateValue.toLocalDate().plusDays(noOfDays))
    }


    fun generateUUIDString(): String {
        // Creating a random UUID (Universally unique identifier).
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    //    generating token
    private fun generateTransactionReference(map: ServiceMapsEntity): String =
        generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, false).toUpperCase()

    fun createServiceRequest(s: ServiceMapsEntity): ServiceRequestsEntity {
        var serviceRequests = ServiceRequestsEntity()

        with(serviceRequests) {
            serviceMapsId = s
            processingStartDate = Timestamp.from(Instant.now())
            transactionReference = generateTransactionReference(s)
            requestDate = Date(Date().time)
            createdBy = transactionReference
            createdOn = Timestamp.from(Instant.now())
            eventBusSubmitDate = Timestamp.from(Instant.now())
            try {
                requestId = s.id.toLong()
            } catch (e: Exception) {
                0L
            }


        }
        serviceRequests = serviceRequestsRepository.save(serviceRequests)

        return serviceRequests
    }

    fun createTransactionLog(sr: ServiceRequestsEntity, map: ServiceMapsEntity): WorkflowTransactionsEntity {
        val log = WorkflowTransactionsEntity()
        with(log) {
            transactionDate = Date(Date().time)
            transactionStartDate = Timestamp.from(Instant.now())
            serviceRequests = sr
            retried = 0
            transactionStatus = map.initStatus
            createdBy = "${sr.id}_${sr.transactionReference}"
            transactionReference = "${sr.transactionReference}_${sr.currentStage}"
            createdOn = Timestamp.from(Instant.now())

        }
        return log
    }

    fun validateVerificationToken(sr: ServiceRequestsEntity, token: String?): Int {
        var log: WorkflowTransactionsEntity? = null
        try {
            sr.serviceMapsId
                ?.let { map ->
                    sr.payload = token.toString()

                    log = createTransactionLog(sr, map)


                    verificationTokensRepo.findByTokenAndStatus(token, map.initStatus)
                        ?.let { verificationToken ->
                            log?.integrationResponse = "${verificationToken.id}"
                            verificationToken.tokenExpiryDate
                                ?.let { expiry ->
                                    when {
                                        expiry.after(Timestamp.from(Instant.now())) -> {
//                                                        /**
//                                                         * If user exists activate and enable
//                                                         */
//                                                        verificationToken.userId
//                                                                ?.let {
//                                                                    activateUser(it, map, sr)
////                                                                                variables["continue"] = true
//
//                                                                }

                                            verificationToken.status = map.successStatus
                                            verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                                            verificationToken.lastModifiedBy = "Verification Token Received"
                                            verificationTokensRepo.save(verificationToken)

                                        }
                                        else -> {
                                            verificationToken.status = map.failedStatus
                                            verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                                            verificationToken.lastModifiedBy = "Expired Verification Token Received"
                                            verificationTokensRepo.save(verificationToken)
                                            throw Exception("Expired Verification Token Received")
                                        }
                                    }

                                }
                                ?: throw Exception("Verification Token without a valid expiry found")


                        } ?: throw Exception("Verification Token not found")




                    sr.responseStatus = sr.serviceMapsId?.successStatusCode
                    sr.responseMessage = "Success ${sr.payload}"
                    sr.status = map.successStatus
                    sr.processingEndDate = Timestamp.from(Instant.now())

                    log?.responseMessage = "Token generation successful"
                    log?.responseStatus = map.successStatusCode
                    log?.transactionStatus = map.successStatus

                }
//                        taskService.complete(task.id, variables)


//                    }
//                    ?: throw ProcessInstanceNotFoundException("No process instance with [businessKey=${sr.transactionReference}]")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log?.responseMessage = e.message
            log?.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log?.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log?.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log?.let { log = workflowTransactionsRepository.save(it) }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }

        }
        return log?.transactionStatus ?: 25

    }

    fun mapServiceRequestForSuccess(
        map: ServiceMapsEntity,
        payloadValues: String,
        user: UsersEntity?
    ): ServiceRequestsEntity {
        var sr = createServiceRequest(map)
        with(sr) {
            payload = payloadValues
            names = user?.let { concatenateName(it) }
            responseStatus = sr.serviceMapsId?.successStatusCode
            responseMessage = "Success $payload"
            status = map.successStatus
        }

        sr = serviceRequestsRepository.save(sr)
        return sr
    }

    fun mapServiceRequestForSuccessUserNotRegistered(
        map: ServiceMapsEntity,
        payloadValues: String,
        userName: String
    ): ServiceRequestsEntity {
        var sr = createServiceRequest(map)
        with(sr) {
            payload = payloadValues
            names = userName
            responseStatus = sr.serviceMapsId?.successStatusCode
            responseMessage = "Success $payload"
            status = map.successStatus
        }

        sr = serviceRequestsRepository.save(sr)
        return sr
    }

    fun sendEmailWithUserEntity(
        user: UsersEntity,
        uuid: String,
        valuesMapped: Any,
        map: ServiceMapsEntity,
        sr: ServiceRequestsEntity
    ): Boolean {

        KotlinLogging.logger { }.info { "Started Mail process" }
        notificationsUseCase(map, mutableListOf(user.email), uuid, valuesMapped, sr)
            ?.let { list ->
                list.forEach { buffer ->
                    /**
                     * TODO: Make topic a field on the Buffer table
                     */
                    buffer.recipient?.let { recipient ->
                        KotlinLogging.logger { }.info { "Started recipient $recipient" }
                        buffer.subject?.let { subject ->
                            KotlinLogging.logger { }.info { "Started subject $subject" }
                            buffer.messageBody?.let { messageBody ->
                                KotlinLogging.logger { }.info { "Started messageBody $messageBody" }
                                notifications.sendEmail(recipient, subject, messageBody)
//                                    notifications.processEmail(recipient, subject, messageBody)
                                KotlinLogging.logger { }.info { "Email sent" }
                            }
                        }
                    }
                }
                sr.processingEndDate = getTimestamp()
                serviceRequestsRepository.save(sr)
            }

        return true
    }

    fun sendEmailWithUserEmail(
        userEmail: String,
        uuid: String,
        valuesMapped: Any,
        map: ServiceMapsEntity,
        sr: ServiceRequestsEntity
    ): Boolean {

        KotlinLogging.logger { }.info { "Started Mail process" }
        notificationsUseCase(map, mutableListOf(userEmail), uuid, valuesMapped, sr)
            ?.let { list ->
                list.forEach { buffer ->
                    /**
                     * TODO: Make topic a field on the Buffer table
                     */
                    buffer.recipient?.let { recipient ->
                        KotlinLogging.logger { }.info { "Started recipient $recipient" }
                        buffer.subject?.let { subject ->
                            KotlinLogging.logger { }.info { "Started subject $subject" }
                            buffer.messageBody?.let { messageBody ->
                                KotlinLogging.logger { }.info { "Started messageBody $messageBody" }
                                notifications.sendEmail(recipient, subject, messageBody)
//                                    notifications.processEmail(recipient, subject, messageBody)
                                KotlinLogging.logger { }.info { "Email sent" }
                            }
                        }
                    }
                }
                sr.processingEndDate = getTimestamp()
                serviceRequestsRepository.save(sr)
            }

        return true
    }


    fun notificationsUseCase(
        map: ServiceMapsEntity,
        email: MutableList<String?>,
        uuid: String,
        data: Any?,
        sr: ServiceRequestsEntity? = null
    ): List<NotificationsBufferEntity>? {
        notificationsRepo.findByServiceMapIdAndUuidAndStatus(map, uuid, map.activeStatus)
            ?.let { notifications ->
                return generateBufferedNotification(notifications, map, email, data, sr)
            }
            ?: throw MissingConfigurationException("Notification for current Scenario is missing, review setup and try again later")

    }

    private fun composeMessage(data: Any?, notification: NotificationsEntity): String? {
        val p = notification.notificationType?.let { notifier ->
            notifier.delimiter?.let {
                notification.spelProcessor?.split(it)?.replacePrefixedItemsWithObjectValues(
                    data,
                    notifier.beanprefix,
                    notifier.beanprefixreplacement
                ) { d, p ->
                    composeUsingSpel(d, p)
//                    val finalMessageBody = composeUsingSpel(d, p)
//                    KotlinLogging.logger { }.info("finalMessageBody: ${finalMessageBody}")
//                    val finalMessageBodyReplaced = finalMessageBody?.replace(properties.baseUrlKey, properties.baseUrlValue)
//                    KotlinLogging.logger { }.info("finalMessageBodyReplaced: ${finalMessageBodyReplaced}")
//                    finalMessageBodyReplaced
//                    composeUsingSpel(d, p)?.replace(properties.baseUrlKey, properties.baseUrlValue)
                }
            }?.toTypedArray()
        }
        return p?.let { placeHolderMapper(input = notification.description, parameters = it) }

    }

    fun generateVerificationToken(
        sr: ServiceRequestsEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): UserVerificationTokensEntity? {
        var tokensEntity = UserVerificationTokensEntity()
        with(tokensEntity) {
            token = sr.transactionReference
            userId = user
            status = map.initStatus
            createdBy = sr.transactionReference
            createdOn = Timestamp.from(Instant.now())
            map.tokenExpiryHours?.let { h -> tokenExpiryDate = Timestamp.from(Instant.now().plus(h, ChronoUnit.HOURS)) }
                ?: throw Exception("Missing Configuration: Hours to Token Expiry")
            transactionDate = Date(Date().time)
        }

        tokensEntity = verificationTokensRepo.save(tokensEntity)
        return tokensEntity
    }

    fun generateEmailVerificationToken(
        sr: ServiceRequestsEntity,
        user: PvocComplaintsEmailVerificationEntity?,
        map: ServiceMapsEntity
    ): EmailVerificationTokenEntity {
        var tokensEntity = EmailVerificationTokenEntity()
        with(tokensEntity) {
            token = sr.transactionReference
            email = user?.email
            status = map.initStatus
            createdBy = sr.transactionReference
            createdOn = Timestamp.from(Instant.now())
            map.tokenExpiryHours?.let { h -> tokenExpiryDate = Timestamp.from(Instant.now().plus(h, ChronoUnit.HOURS)) }
                ?: throw Exception("Missing Configuration: Hours to Token Expiry")
            transactionDate = Date(Date().time)
        }

        tokensEntity = emailVerificationTokenEntityRepo.save(tokensEntity)
        return tokensEntity
    }

    fun generateBufferedNotification(
        notifications: Collection<NotificationsEntity>,
        map: ServiceMapsEntity,
        emails: MutableList<String?>,
        data: Any?,
        sr: ServiceRequestsEntity? = null
    ): List<NotificationsBufferEntity>? {
        val buffers = mutableListOf<NotificationsBufferEntity>()

        var recipients = ""
        emails.forEachIndexed { i, email ->
            recipients = when (i) {
                0 -> email ?: ""
                else -> "$recipients, ${email ?: ""}"
            }
        }
        notifications.forEach { notification ->
            var buffer = NotificationsBufferEntity()
            with(buffer) {
                messageBody = composeMessage(data, notification)
                subject = notification.subject
                serviceRequestId = sr?.id
                transactionReference = sr?.transactionReference
                sender = notification.sender
                this.recipient = recipients
                varField1 = notification.requestTopic
                status = map.initStatus
                createdOn = Timestamp.from(Instant.now())
                createdBy = sr?.transactionReference
                notificationId = notification.id
                actorClassName = notification.actorClass
            }
            buffer = bufferRepo.save(buffer)

            buffers.add(buffer)


        }

        return buffers

    }

    fun getCalculatedDate(days: Int): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, days)
        return Date(cal.timeInMillis)
    }


    fun findUserByID(id: Long): UsersEntity {
        usersRepo.findByIdOrNull(id)
            ?.let { userEntity ->
                return userEntity
            }
            ?: throw ExpectedDataNotFound("User with ID  = ${id}, does not Exist")
    }

    fun findDesignationByID(id: Long): DesignationsEntity {
        designationRepo.findByIdOrNull(id)
            ?.let { designation ->
                return designation
            }
            ?: throw ExpectedDataNotFound("Designation with ID  = ${id}, does not Exist")
    }

    fun mapAllSectionsTogether(sections: List<SectionsEntity>): List<SectionsDto> {
        return sections.map {
            SectionsDto(it.id, it.section, it.divisionId?.id, it.descriptions, it.status == 1)
        }
    }

    fun userListDto(userList: List<UsersEntity>): List<UserEntityDto> {
        return userList.map { u ->
            UserEntityDto(
                u.id,
                u.firstName,
                u.lastName,
                u.userName,
                u.userPinIdNumber,
                u.personalContactNumber,
                u.typeOfUser,
                u.email,
                u.userRegNo,
                u.enabled == 1,
                u.accountExpired == 1,
                u.accountLocked == 1,
                u.credentialsExpired == 1,
                u.status == 1,
                u.registrationDate,
                u.userTypes,
                u.title,
            )
        }
    }

    fun findDepartmentByID(departmentId: Long): DepartmentsEntity {
        departmentRepo.findByIdOrNull(departmentId)
            ?.let { department ->
                return department
            }
            ?: throw ExpectedDataNotFound("Department with ID  = ${departmentId}, does not Exist")
    }

    fun findBroadCategoryByID(broadCategoryId: Long): BroadProductCategoryEntity {
        broadProductCategoryRepository.findByIdOrNull(broadCategoryId)
            ?.let { broadCategory ->
                return broadCategory
            }
            ?: throw ExpectedDataNotFound("Broad Category with ID  = ${broadCategoryId}, does not Exist")
    }

    fun findProductCategoryByID(productCategoryId: Long): KebsProductCategoriesEntity {
        productCategoriesRepository.findByIdOrNull(productCategoryId)
            ?.let { productCategory ->
                return productCategory
            }
            ?: throw ExpectedDataNotFound("Product Category with ID  = ${productCategoryId}, does not Exist")
    }

    fun findProductSubCategoryByID(productSubCategoryId: Long): ProductSubcategoryEntity {
        productSubCategoryRepo.findByIdOrNull(productSubCategoryId)
            ?.let { productSubCategory ->
                return productSubCategory
            }
            ?: throw ExpectedDataNotFound("Product Sub Category with ID  = ${productSubCategoryId}, does not Exist")
    }

    fun findProductByID(productId: Long): ProductsEntity {
        productsRepo.findByIdOrNull(productId)
            ?.let { product ->
                return product
            }
            ?: throw ExpectedDataNotFound("Product with ID  = ${productId}, does not Exist")
    }

    fun findDivisionByDepartmentId(departmentsEntity: DepartmentsEntity, status: Int): List<DivisionsEntity> {
        iDivisionsRepo.findByDepartmentIdAndStatus(departmentsEntity, status)
            ?.let { division ->
                return division
            }
            ?: throw ExpectedDataNotFound("Division with [Department ID = ${departmentsEntity.id}] and [status=${status}], does not Exist")
    }

    fun findUserProfileByUserID(user: UsersEntity, status: Int): UserProfilesEntity {
        iUserProfilesRepo.findByUserIdAndStatus(user, status)
            ?.let { userProfile ->
                return userProfile
            }
            ?: throw ExpectedDataNotFound("User Profile with user ID  = ${user.id} and status = $status, does not Exist")
    }

    fun findDirectorateByID(directorateID: Long): DirectoratesEntity {
        directorateRepo.findByIdOrNull(directorateID)
            ?.let { directoratesEntity ->
                return directoratesEntity
            }
            ?: throw ExpectedDataNotFound("Directorate with [ID  = ${directorateID}], does not Exist")
    }

    fun findDepartmentByDirectorate(directorate: DirectoratesEntity, status: Int): List<DepartmentsEntity> {
        departmentRepo.findByDirectorateIdAndStatus(directorate, status)
            ?.let {
                return it
            }
            ?: throw ExpectedDataNotFound("Department Lists with [directorate ID = ${directorate.id}], doe not Exist")
    }

    fun findUserProfileWithSectionIdAndDesignationId(
        sectionsEntity: SectionsEntity,
        designationsEntity: DesignationsEntity,
        status: Int
    ): UserProfilesEntity {
        iUserProfilesRepo.findByDesignationIdAndSectionIdAndStatus(designationsEntity, sectionsEntity, status)
            ?.let { userProfile ->
                return userProfile
            }
            ?: throw ExpectedDataNotFound("User Profile with section ID  = ${sectionsEntity.id} and Designation ID  = ${designationsEntity.id} and status = $status, does not Exist")
    }

    fun findUserProfileListWithRegionDesignationDepartmentAndStatus(
        region: RegionsEntity,
        designation: DesignationsEntity,
        department: DepartmentsEntity,
        status: Int
    ): List<UserProfilesEntity> {
        iUserProfilesRepo.findByRegionIdAndDesignationIdAndDepartmentIdAndStatus(
            region,
            designation,
            department,
            status
        )
            ?.let { userProfile ->
                return userProfile
            }
            ?: throw ExpectedDataNotFound("UserProfile List with region ID  = ${region.id} and Designation ID  = ${designation.id} and Department ID  = ${department.id} and status = $status, do not Exist")
    }

    fun findAllUsersWithSectionId(sectionsEntity: SectionsEntity, status: Int): List<UserProfilesEntity> {
        iUserProfilesRepo.findBySectionIdAndStatus(sectionsEntity, status)
            ?.let { users ->
                return users
            }
            ?: throw ExpectedDataNotFound("Users with section ID  = ${sectionsEntity.id} and status = $status, does not Exist")
    }

    fun findAllUsersWithSectionIdAndDesignation(
        sectionsEntity: SectionsEntity,
        designationsEntity: DesignationsEntity,
        status: Int
    ): List<UserProfilesEntity> {
        iUserProfilesRepo.findBySectionIdAndDesignationIdAndStatus(sectionsEntity, designationsEntity, status)
            ?.let { users ->
                return users
            }
            ?: throw ExpectedDataNotFound("Users with section ID  = ${sectionsEntity.id} and status = $status, does not Exist")
    }

    fun findAllUsersWithinRegionDepartmentDivisionSectionId(
        region: RegionsEntity,
        department: DepartmentsEntity,
        division: DivisionsEntity,
        section: SectionsEntity,
        status: Int
    ): List<UserProfilesEntity> {
        iUserProfilesRepo.findByRegionIdAndDepartmentIdAndDivisionIdAndSectionIdAndStatus(
            region,
            department,
            division,
            section,
            status
        )
            ?.let { users ->
                return users
            }
            ?: throw ExpectedDataNotFound("Users List with section ID  = ${section.id} and status = $status, does not Exist")
    }


    fun findManufacturerProfileByUserID(userId: UsersEntity, status: Int): ManufacturersEntity {
        manufacturersRepo.findByUserIdAndStatus(userId, status)
            ?.let { manufacturerProfile ->
                return manufacturerProfile
            }
            ?: throw ExpectedDataNotFound("Manufacturer Profile with user ID  = ${userId.id} and status = $status, does not Exist")
    }


    fun findAllUserNotification(userEmail: String): List<NotificationsBufferEntity> {
        notificationsBufferRepo.findByRecipient(userEmail)
            ?.let { notifications ->
                return notifications
            }
            ?: throw ExpectedDataNotFound("Notifications for the user with email = $userEmail, do not Exist")
    }

    fun updateNotification(notificationsBufferEntity: List<NotificationsBufferEntity>, user: UsersEntity): Boolean {
        notificationsBufferEntity
            .forEach { notifications ->
                with(notifications) {
                    readStatus = activeStatus.toInt()
                    modifiedBy = concatenateName(user)
                    modifiedOn = getTimestamp()
                }
                notificationsBufferRepo.save(notifications)
            }

        return true
    }

    fun findNotification(notificationId: Long, userEmail: String): List<NotificationsBufferEntity> {
        notificationsBufferRepo.findByIdAndRecipient(notificationId, userEmail)
            ?.let { notification ->
                return notification
            }
            ?: throw ExpectedDataNotFound("Notification with the following ID = $notificationId, does not Exist")
    }


    fun findManufacturerContactDetailsByManufacturerProfile(
        userId: UsersEntity,
        status: Int
    ): ManufacturerContactsEntity {
        manufacturerContactDetailsRepository.findByManufacturerId(findManufacturerProfileByUserID(userId, status))
            ?.let { manufacturerContactDetails ->
                return manufacturerContactDetails
            }
            ?: throw ExpectedDataNotFound("Manufacturer Contact Details with user ID  = ${userId.id} and status = $status, does not Exist")
    }

//    fun findManufacturerAddressDetailsByManufacturerProfile(userId: UsersEntity, status: Int): ManufacturerContactsEntity {
//        manufacturerAddressesDetailsRepo.findByManufacturerIdAndStatusOrderByVersions(findManufacturerProfileByUserID(userId, status))
//                ?.let { manufacturerContactDetails ->
//                    return manufacturerContactDetails
//                }
//                ?: throw ExpectedDataNotFound("Manufacturer Contact Details with user ID  = ${userId.id} and status = $status, does not Exist")
//    }

    fun findImporterProfileByUserID(userId: UsersEntity, status: Int): ImporterContactDetailsEntity {
        iImporterRepo.findByUserIdAndStatus(userId, status)
            ?.let { importerProfile ->
                return importerProfile
            }
            ?: throw ExpectedDataNotFound("Importer Profile with user ID  = ${userId.id} and status = $status, does not Exist")
    }

    fun checkUserType(userId: UsersEntity, status: Int): UserProfilesEntity {
        iUserProfilesRepo.findByUserIdAndStatus(userId, status)


            ?.let { userProfile ->
                return userProfile
            }
            ?: throw ExpectedDataNotFound("User Profile with user ID  = ${userId.id} and status = $status, does not Exist")
    }


//    fun getCurrentUrl(req: ServerRequest): String{
//        val url : URL = req.servletRequest().requestURL.
//
////        return url.host
//    }

//    public static String getCurrentUrl(HttpServletRequest request){
//
//        URL url = new URL(request.getRequestURL().toString())
//
//        String host  = url.getHost();
//        String userInfo = url.getUserInfo();
//        String scheme = url.getProtocol();
//        String port = url.getPort();
//        String path = request.getAttribute("javax.servlet.forward.request_uri");
//        String query = request.getAttribute("javax.servlet.forward.query_string");
//        URI uri = new URI(scheme,userInfo,host,port,path,query,null)
//        return uri.toString();
//    }

    fun findLaboratoryWIthId(laboratoryId: Long): CdLaboratoryEntity {
        iLaboratoryRepo.findByIdOrNull(laboratoryId)
            ?.let { laboratoryEntity ->
                return laboratoryEntity
            }
            ?: throw ExpectedDataNotFound("Laboratory with this ID  = ${laboratoryId}, does not Exist")
    }

    fun findAllUsersWithMinistryUserType(): List<UsersEntity>? {
        return usersRepo.findAllByUserTypes(applicationMapProperties.mapUserTypeMinistry)
    }

    fun getLoggedInUser(): UsersEntity? {
        SecurityContextHolder.getContext().authentication.let { auth ->
            iUserRepository.findByUserName(userName = auth.name).let { userDetails ->
                return userDetails
            }
        }
    }

    fun makeKenyanMSISDNFormat(phoneNumber: String?): String {
        val reversedPhoneNumber = StringBuilder(phoneNumber).reverse().substring(0, 9)
        val trimmedPhoneNumber = StringBuilder(reversedPhoneNumber).reverse().toString()
        return "254$trimmedPhoneNumber"
    }

    fun generateTransactionReference(length: Int = 12): String {
        return generateRandomText(length, "SHA1PRNG", "SHA-1")
    }

    fun validateOTPToken(token: String, phoneNumber: String?): CustomResponse {
        val result = CustomResponse()
        try {
            emailVerificationTokenEntityRepo.findFirstByTokenAndStatusOrderByIdDesc(token, 10)
                ?.let { verificationToken ->
                    if (verificationToken.email != phoneNumber) throw InvalidValueException("Invalid Token provided")

                    verificationToken.tokenExpiryDate
                        ?.let { expiry ->
                            when {
                                expiry.after(Timestamp.from(Instant.now())) -> {

                                    verificationToken.status = 30
                                    verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                                    verificationToken.lastModifiedBy = "Verification Token Received"
                                    emailVerificationTokenEntityRepo.save(verificationToken)
                                    return CustomResponse().apply {
                                        response = "00"
                                        payload = "Success, valid OTP received"
                                        status = 200
                                    }

                                }
                                else -> {
                                    verificationToken.status = 25
                                    verificationToken.lastModifiedOn = Timestamp.from(Instant.now())
                                    verificationToken.lastModifiedBy = "Expired Verification Token Received"
                                    emailVerificationTokenEntityRepo.save(verificationToken)
                                    throw InvalidValueException("Token Verification failed")
                                }
                            }

                        }
                        ?: throw InvalidValueException("Verification Token without a valid expiry found")
                }
                ?: throw NullValueNotAllowedException("Invalid Token, validation failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            return CustomResponse().apply {
                payload = e.message
                status = 500
                response = "99"
            }

        }
    }

    fun generateVerificationToken(input: String, phone: String): EmailVerificationTokenEntity {
        val tokensEntity = EmailVerificationTokenEntity()
        with(tokensEntity) {
            token = input
            email = phone
            status = 10
            createdBy = phone
            createdOn = Timestamp.from(Instant.now())
            tokenExpiryDate = Timestamp.from(Instant.now().plus(10, ChronoUnit.MINUTES))
            transactionDate = Date(java.util.Date().time)
        }


        return emailVerificationTokenEntityRepo.save(tokensEntity)
    }

    fun sendOtpViaSMS(token: EmailVerificationTokenEntity): CustomResponse {
        try {
            val phone = token.email ?: throw NullValueNotAllowedException("Invalid phone number provided")
            val message = "Your verification code is ${token.token}"
            val smsSent: Boolean = smsService.sendSms(phone, message)
            return if (smsSent) {
                CustomResponse().apply {
                    payload = "OTP successfully sent"
                    status = 200
                    response = "00"
                }
            } else {
                throw ExpectedDataNotFound("An error occurred, please try again later")
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            return CustomResponse().apply {
                payload = e.message
                status = 500
                response = "99"
            }
        }

    }

}
