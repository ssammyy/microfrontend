package org.kebs.app.kotlin.apollo.api

import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.scheduler.SchedulerImpl
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.UpAndDownLoad
import org.kebs.app.kotlin.apollo.api.utils.Delimiters
import org.kebs.app.kotlin.apollo.api.utils.XMLDocument
import org.kebs.app.kotlin.apollo.common.dto.UserEntityDto
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.ConsignmentDocument
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.customdto.PvocReconciliationReportDto
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ICdInspectionMotorVehicleItemChecklistRepository
import org.kebs.app.kotlin.apollo.store.repo.di.ICountryTypeCodesRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDestinationInspectionFeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader


@SpringBootTest
@RunWith(SpringRunner::class)
class DITest {
    @Autowired
    lateinit var destinationInspectionDaoServices: DestinationInspectionDaoServices

    @Autowired
    lateinit var consignmentDocumentDaoService: ConsignmentDocumentDaoService

    @Autowired
    lateinit var registrationDaoServices: RegistrationDaoServices

    @Autowired
    lateinit var invoiceDaoService: InvoiceDaoService

    @Autowired
    lateinit var qaDaoServices: QADaoServices

    @Autowired
    lateinit var daoServiceAdmin: SystemsAdminDaoService

    @Autowired
    lateinit var schedulerImpl: SchedulerImpl

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var idfsEntityRepository: IdfsEntityRepository

    @Autowired
    lateinit var cocRepository: ICocsRepository

    @Autowired
    lateinit var coisRepository: ICoisRepository

    @Autowired
    lateinit var corsEntityRepository: ICorsBakRepository

    @Autowired
    lateinit var usersRepo: IUserRepository

    @Autowired
    lateinit var iDIFeeDetailsRepo: IDestinationInspectionFeeRepository

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var upAndDownLoad: UpAndDownLoad

    @Autowired
    lateinit var pvocReconciliationReportEntityRepo: PvocReconciliationReportEntityRepo

    @Autowired
    lateinit var destinationInspectionBpmn: DestinationInspectionBpmn

    @Autowired
    lateinit var demandNoteRepository: IDemandNoteRepository

    @Autowired
    lateinit var notifications: Notifications
    @Autowired
    lateinit var motorVehicleInspectionEntityRepo: ICdInspectionMotorVehicleItemChecklistRepository


    @Value("\${bpmn.di.mv.cor.process.definition.key}")
    lateinit var diMvWithCorProcessDefinitionKey: String

    fun convertStringToDate(sDate1: String): Date {
        val formatter6 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val date6: Date = formatter6.parse(sDate1)
        println(sDate1 + "\t" + date6)
        return date6
    }

    fun convertStringToDate2(sDate1: String): Date {
        val formatter1 = SimpleDateFormat("dd/MM/yyyy")
        val date6: Date = formatter1.parse(sDate1)
        println(sDate1 + "\t" + date6)
        return date6
    }

    fun convertStringToBigDecimal(priceValue: String): BigDecimal {
        val result = BigDecimal(priceValue.replace(",", ""))
        println(result)
        return result
    }

    fun consignmentDocumentResults(): ConsignmentDocumentEntity {
        val cdResult = ConsignmentDocumentEntity()
        with(cdResult) {
            documentName = "KEBSIA - KEBS Inspection and Approval Document"
            documentTypeCode = "INR"
            documnetTypeDescription = "Inspection Report"
            process = "KEBSIAPR-KEBS Inspection and Approval Process"
            applicationRefNo = "CD202000KEBSKEBSIA0002316734"
            masterApprovalNo = null
            masterApprovalVersionNo = null
            ucrNumber = "UCR201901950062"
            versionNo = "1"
            remarks = null
            conditionOfApproval = null
            purposeOfImport = "Trading"
            purposeOfExport = "Trading"
            termsAndConditions = null
        }

        return cdResult
    }

    fun applicationStatusCDResults(): CdApplicationStatusEntity {
        val cdResult = CdApplicationStatusEntity()
        with(cdResult) {
            approvalStatus = "PC"
            expiryDate = null
            usedDated = null
            usedStatus = null
            amendedDate = null
            applicationDate = java.sql.Date(convertStringToDate("05-11-2020 13:54:36").time)
            issuanceDate = java.sql.Date(convertStringToDate2("05/11/2020").time)

        }

        return cdResult
    }

    fun applicantDetailsCDResults(): CdApplicantDetailsEntity {
        val cdResult = CdApplicantDetailsEntity()
        with(cdResult) {
            name = "FASTLANE LOGISTICS SYSTEMS LTD"
            pin = "P051221419H"
            address = "93 00200"
            contactPerson = "MANGEADANIEL"
            applicationCode = "FSS"
            country = "KENYA"
            email = "mumo_dan@yahoo.co.uk"
        }

        return cdResult
    }

    fun consigneeDetailsCDResults(): CdConsigneeDetailsEntity {
        val cdResult = CdConsigneeDetailsEntity()
        with(cdResult) {
            name = "TECHNO RELIEF SERVICES (EPZ) LIMITED"
            pin = "P051193385E"
            physicalAddress = "34910 00100 NAIROBI"
            physicalCountry = "KENYA"
            postalAddress = "34910 00100 NAIROBI"
            postalCountry = "KENYA"
            telephone = "254 722 200539"
            fax = "254 20650112"
            sectorOfActivity = "Trade"
            warehouseCode = "TECHNO RELIEF SERVICES (EPZ) LIMITED"
            warehouseLocation = "NBI"
            ogaRefNo = null
            email = "exim@technorelief.com"
        }

        return cdResult
    }

    fun importerDetailsCDResults(): CdImporterDetailsEntity {
        val cdResult = CdImporterDetailsEntity()
        with(cdResult) {
            name = "TECHNO RELIEF SERVICES (EPZ) LIMITED"
            pin = "P051193385E"
            physicalAddress = "34910 00100 NAIROBI"
            physicalCountry = "KENYA"
            postalAddress = "34910 00100 NAIROBI"
            postalCountry = "KENYA"
            telephone = "254 722 200539"
            fax = "254 20650112"
            sectorOfActivity = "Trade"
            warehouseCode = "TECHNO RELIEF SERVICES (EPZ) LIMITED"
            warehouseLocation = "NBI"
            ogaRefNo = null
            email = "exim@technorelief.com"
        }

        return cdResult
    }

    fun exporterDetailsCDResults(): CdExporterDetailsEntity {
        val cdResult = CdExporterDetailsEntity()
        with(cdResult) {
            name = "KANAK RATNA WOVEN SACK PVT LTD"
            pin = "P000000000N"
            physicalAddress = "PLOT NO 270 GAM VADOLI TA OLPAO DISURAT- 394110 GUJARAT INDIA"
            physicalCountry = "INDIA"
            postalAddress = "PLOT NO 270 GAM VADOLI TA OLPAO DISURAT- 394110 GUJARAT INDIA"
            postalCountry = "INDIA"
            telephone = "+91 9726 094905"
            fax = "+91 9726 094904"
            sectorOfActivity = "Trade"
            warehouseCode = "TECHNO RELIEF SERVICES (EPZ) LIMITED"
            warehouseLocation = "NBI"
            ogaRefNo = null
            email = "kanakratna@gmail.com"
        }

        return cdResult
    }

    fun consignorDetailsCDResults(): CdConsignorDetailsEntity {
        val cdResult = CdConsignorDetailsEntity()
        with(cdResult) {
            name = "KANAK RATNA WOVEN SACK PVT LTD"
            pin = "P000000000N"
            physicalAddress = "PLOT NO 270 GAM VADOLI TA OLPAO DISURAT- 394110 GUJARAT INDIA"
            physicalCountry = "INDIA"
            postalAddress = "PLOT NO 270 GAM VADOLI TA OLPAO DISURAT- 394110 GUJARAT INDIA"
            postalCountry = "INDIA"
            telephone = "+91 9726 094905"
            fax = "+91 9726 094904"
            sectorOfActivity = "Trade"
            warehouseCode = "TECHNO RELIEF SERVICES (EPZ) LIMITED"
            warehouseLocation = "NBI"
            ogaRefNo = null
            email = "kanakratna@gmail.com"
        }

        return cdResult
    }

    fun transportDetailsCDResults(): CdTransportDetailsEntity {
        val cdResult = CdTransportDetailsEntity()
        with(cdResult) {
            modeOfTransport = "A"
            portOfArrival = "Nairobi"
            freightStation = "Transglobal Cargo Centre"
            modeOfTransportDesc = "Air"
            cargoTypeIndicator = "General Cargo"
            customOffice = "JKA"
        }
        return cdResult
    }

    fun itemDetailsDetailsCDResults(): CdItemDetailsEntity {
        val cdResult = CdItemDetailsEntity()
        with(cdResult) {
            itemDescription = "PP WOVEN SACK"
            itemHsCode = "3923290000"
            hsDescription = "Sacks and bags (incl. cones) of other plastics (excl. ethylene)"
//            quantity = 74500
            packageQuantity = 149.00.toBigDecimal()
            unitOfQuantity = "Net Kilogram"
            unitPriceFcy = "23.21".toBigDecimal()
            unitPriceNcy = "0.21".toBigDecimal()
            itemGrossWeight = "11748 Net Kilogram"
            countryOfOrgin = "INDIA"
            foreignCurrencyCode = "USD"
            totalPriceFcy = convertStringToBigDecimal("15,923.05")
            itemNetWeight = "11548 Net Kilogram"
            packageType = "Bundle"
            supplimentaryQuantity = "0"
            applicantRemarks = "FOR APPROVAL"
        }
        return cdResult
    }

    fun valuesHeaderDetailsCDResults(): CdValuesHeaderLevelEntity {
        val cdResult = CdValuesHeaderLevelEntity()
        with(cdResult) {
            foreignCurrencyCode = "USD "
            freightFcy = "800.00"
            freightNcy = "86,868.24"
            cifFcy = "16,748.05 "
            cifNcy = "1,818,592.03"
            insuranceFcy = "25.00"
            insuranceNyc = "2,714.63"
            fobNcy = "1,729,009.16"
            fobFcy = "86,868.24"
            otherChargesFcy = "0.00"
            otherChargesNcy = "0.00"
            forexRate = "108.59"
        }

        return cdResult
    }


//    fun createLocalCoc(user: UsersEntity, consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity, map: ServiceMapsEntity): CocsBakEntity {
//        val coc = CocsBakEntity()
//        with(coc) {
//            cocNumber = "KEBSCOC${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
//            idfNumber = consignmentDocumentDetailsEntity.idfNumber
//            rfiNumber = ""
//            ucrNumber = consignmentDocumentDetailsEntity.ucrNumber
//            rfcDate = commonDaoServices.getTimestamp()
//            shipmentQuantityDelivered = ""
//            cocIssueDate = commonDaoServices.getTimestamp()
//            clean = ""
//            cocRemarks = ""
//            issuingOffice = ""
//            val cdImporter = consignmentDocumentDetailsEntity.cdImporter?.let { destinationInspectionDaoServices.findCDImporterDetails(it) }
//            importerName = cdImporter?.name
//            importerPin = cdImporter?.pin
//            importerAddress1 = cdImporter?.physicalAddress
//            importerAddress2 = ""
//            importerCity = " "
//            importerCountry = cdImporter?.physicalCountryName
//            importerZipCode = " "
//            importerTelephoneNumber = cdImporter?.telephone
//            importerFaxNumber = cdImporter?.fax
//            importerEmail = cdImporter?.email
//            val cdExporter = consignmentDocumentDetailsEntity.cdExporter?.let { destinationInspectionDaoServices.findCdExporterDetails(it) }
//            exporterName = cdExporter?.name
//            exporterPin = cdExporter?.pin
//            exporterAddress1 = cdExporter?.physicalAddress
//            exporterAddress2 = ""
//            exporterCity = ""
//            exporterCountry = cdExporter?.physicalCountryName
//            exporterZipCode = ""
//            exporterTelephoneNumber = cdExporter?.telephone
//            exporterFaxNumber = cdExporter?.fax
//            exporterEmail = cdExporter?.email
//
//            placeOfInspection = ""
//            dateOfInspection = commonDaoServices.getTimestamp()
//
//            val cdTransport = consignmentDocumentDetailsEntity.cdTransport?.let { destinationInspectionDaoServices.findCdTransportDetails(it) }
//            portOfDestination = cdTransport?.portOfArrival
//            shipmentMode = ""
//            countryOfSupply = ""
//            finalInvoiceCurrency = "KES"
//            finalInvoiceDate = commonDaoServices.getTimestamp()
//            shipmentSealNumbers = ""
//            shipmentContainerNumber = ""
//            shipmentGrossWeight = ""
//            route = "Route A"
//            cocType = "L"
//            productCategory = ""
//            createdBy = commonDaoServices.concatenateName(user)
//            createdOn = commonDaoServices.getTimestamp()
//        }
//        return cocBakRepository.save(coc)
//    }

    fun createNewIDF(user: UsersEntity): IdfsEntity {
        val idf = IdfsEntity()
        with(idf) {
            idfNumber = "UCR2657416385"
            ucr = "UCR2657416385"
            importerName = "importerName"
            importerAddress = "importerAddress"
            importerEmail = "importerEmail"
            importerTelephoneNumber = "importerTelephoneNumber"
            importerContactName = "importerContactName"
            importerFax = "importerFax"
            sellerName = "sellerName"
            sellerAddress = "sellerAddress"
            sellerEmail = "sellerEmail"
            sellerTelephoneNumber = "sellerTelephoneNumber"
            sellerContactName = "sellerContactName"
            sellerFax = "sellerFax"
            countryOfSupply = "countryOfSupply"
            portOfDischarge = "portOfDischarge"
            portOfCustomsClearance = "portOfCustomsClearance"
            modeOfTransport = "modeOfTransport"
            comesa = "comesa"
            invoiceNumber = "invoiceNumber"
            invoiceDate = commonDaoServices.getTimestamp()
            currency = "currency"
            insurance = 256L
            observations = "observations"
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return idfsEntityRepository.save(idf)

    }

    fun createNewCOI(user: UsersEntity): CoisEntity {
        val coi = CoisEntity()
        with(coi) {
            coiNumber = " testTest"
            idfNumber = " testTest"
            rfiNumber = " testTest"
            ucrNumber = " testTest"
            rfcDate = commonDaoServices.getTimestamp()
            coiIssueDate = commonDaoServices.getTimestamp()
            clean = " testTest"
            coiRemarks = " testTest"
            issuingOffice = " testTest"
            importerName = " testTest"
            importerPin = " testTest"
            importerAddress1 = " testTest"
            importerAddress2 = " testTest"
            importerCity = " testTest"
            importerCountry = " testTest"
            importerZipcode = " testTest"
            importerTelephoneNumber = " testTest"
            importerFaxNumber = " testTest"
            importerEmail = " testTest"
            exporterName = " testTest"
            exporterPin = " testTest"
            exporterAddress1 = " testTest"
            exporterAddress2 = " testTest"
            exporterCity = " testTest"
            exporterCountry = " testTest"
            exporterZipcode = " testTest"
            exporterTelephoneNumber = " testTest"
            exporterFaxNumber = " testTest"
            exporterEmail = " testTest"
            placeOfInspection = " testTest"
            dateOfInspection = commonDaoServices.getTimestamp()
            portOfDestination = " testTest"
            shipmentMode = " testTest"
            countryOfSupply = " testTest"
            finalInvoiceCurrency = " testTest"
            finalInvoiceNumber = " testTest"
            finalInvoiceDate = commonDaoServices.getTimestamp()
            shipmentSealNumbers = " testTest"
            shipmentContainerNumber = " testTest"
            shipmentGrossWeight = " testTest"
            shipmentLineOwnerPin = " testTest"
            shipmentLineOwnerName = " testTest"
            productCategory = " testTest"
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return coisRepository.save(coi)

    }

    //    @Test
    fun createNewCOR(user: UsersEntity): CorsBakEntity {
        val cor = CorsBakEntity()
        with(cor) {
            corNumber = "Tes101t"
            corIssueDate = commonDaoServices.getTimestamp()
            countryOfSupply = "Test"
            inspectionCenter = "Test"
            exporterName = "Test"
            exporterAddress1 = "Test"
            exporterAddress2 = "Test"
            exporterEmail = "Test"
            applicationBookingDate = commonDaoServices.getTimestamp()
            inspectionDate = commonDaoServices.getTimestamp()
            make = "Test"
            model = "Test"
            chasisNumber = "CKM7958"
            engineNumber = "Test"
            engineCapacity = "Test"
            yearOfManufacture = "Test"
            yearOfFirstRegistration = "Test"
            inspectionMileage = "Test"
            unitsOfMileage = "Test"
            inspectionRemarks = "Test"
            previousRegistrationNumber = "Test"
            previousCountryOfRegistration = "Test"
            tareWeight = 22
            loadCapacity = 52
            grossWeight = 58
            numberOfAxles = 69
            typeOfVehicle = "Test"
            numberOfPassangers = 89
            typeOfBody = "Test"
            bodyColor = "Test"
            fuelType = "Test"
            inspectionFee = 85
            inspectionFeeCurrency = "Test"
            inspectionFeeExchangeRate = 852
            inspectionFeePaymentDate = commonDaoServices.getTimestamp()
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return corsEntityRepository.save(cor)

    }


    @Test
    fun testCDAddDetails() {
//        destinationInspectionDaoServices.mainCDDetails(
//                consignmentDocumentResults(),
//                valuesHeaderDetailsCDResults(),
//                consigneeDetailsCDResults(),
//                consignorDetailsCDResults(),
//                applicationStatusCDResults(),
//                transportDetailsCDResults(),
//                applicantDetailsCDResults(),
//                importerDetailsCDResults(),
//                exporterDetailsCDResults()
//
//        ).let {mainCDDetailsSaved->
//                    mainCDDetailsSaved.id
//                            ?.let {consignmentDocumentIDFound->
//                        destinationInspectionDaoServices.itemCDDetails(itemDetailsDetailsCDResults(), consignmentDocumentIDFound)
//                    }
//
//                }


    }

    private val inputFilePath = "test_image.jpg"
    private val outputFilePath = "test_image_copy.jpg"

    @Test
    @Throws(IOException::class)
    fun fileToBase64StringConversion() {
        // load file from /src/test/resources
        val classLoader = javaClass.classLoader
        val inputFile = File(classLoader.getResource(inputFilePath).file)
        val fileContent: ByteArray = FileUtils.readFileToByteArray(inputFile)
        val encodedString = Base64
            .getEncoder()
            .encodeToString(fileContent)

        // create output file
        val outputFile = File(
            inputFile
                .parentFile
                .absolutePath + File.pathSeparator.toString() + outputFilePath
        )

        // decode the string and write to file
        val decodedBytes = Base64
            .getDecoder()
            .decode(encodedString)
        FileUtils.writeByteArrayToFile(outputFile, decodedBytes)
        assertTrue(FileUtils.contentEquals(inputFile, outputFile))
    }


    @Test
    fun updateCd() {
        val cdDetails = ConsignmentDocumentDetailsEntity()
        with(cdDetails) {
            confirmAssignedUserId = 1083
        }
        destinationInspectionDaoServices.updateCDDetails(
            cdDetails,
            24,
            commonDaoServices.loggedInUserDetails(),
            commonDaoServices.serviceMapDetails(122)
        )

    }

    @Test
    fun upLoad() {
        upAndDownLoad.upload(applicationMapProperties.mapKeswsConfigIntegration)
    }
//    --UCR202550137946
//
//    @Test
//    fun downLoad() {
//        val download = upAndDownLoad.download(applicationMapProperties.mapKeswsConfigIntegration)
//        KotlinLogging.logger { }.info { "Downloaded text = $download " }
//
//        val consignmentDoc: ConsignmentDocument = commonDaoServices.decodeXML(download)
//        val details = CdTransportDetailsEntity()
//        details.cargoTypeIndicator = consignmentDoc.documentDetails?.consignmentDocDetails?.cdTransport?.cargoTypeIndicator
//
//        assertNotNull(consignmentDoc?.documentDetails?.consignmentDocDetails?.cdStandard?.applicationTypeCode)
//        assertNotNull(consignmentDoc?.documentDetails?.consignmentDocDetails?.cdImporter?.pin)
//        assertEquals("CD", consignmentDoc?.documentDetails?.consignmentDocDetails?.cdStandard?.applicationTypeCode)
//
//        val myJsonFormat = upAndDownLoad.convertXmlToJson(download)
//        KotlinLogging.logger { }.info { "Converted xml to JsonObject = $myJsonFormat " }
//
//        destinationInspectionDaoServices.extractConsignmentDocDetailsFromJson(myJsonFormat)
//                .let { jsonObject ->
//                    destinationInspectionDaoServices.mainCDFunction(jsonObject)
//                            .let {
//                                KotlinLogging.logger { }.info { "Process results = $it " }
//                            }
//                }
//
//
//    }

    @Test
    fun startDIProcess() {
        consignmentDocumentDaoService.cdDownload()

    }

    @Test
    fun checkIfPaymentIsMade() {
        invoiceDaoService.updateOfInvoiceTables()
    }


    @Test
    fun createEmployees() {
        val appId: Int = applicationMapProperties.mapUserRegistration
        val map = commonDaoServices.serviceMapDetails(appId)
        var userCreated = UserEntityDto().apply {
            id = null
            firstName = "PHILIP"
            lastName = "CHEPKWONY"
            email = "chepkwonyp@kebs.org"
            userName = email
            userPinIdNumber = null
            personalContactNumber = "254724175178"
            typeOfUser = null
            userRegNo =
                "KEBS#EMP${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            enabled = false
            accountExpired = false
            accountLocked = false
            credentialsExpired = false
            status = false
            registrationDate = null
            userType = null
            title = 1
            directorate = 1
            department = 2
            division = 4
            section = null
            l1SubSubSection = null
            l2SubSubSection = null
            designation = 410
            profileId = null
            region = 4
            county = 26
            town = 579
            subRegion = null
        }

        userCreated = daoServiceAdmin.updateUserDetails(userCreated)!!


        invoiceDaoService.updateOfInvoiceTables()
    }

    @Test
    fun checkLabResults() {
        schedulerImpl.updateLabResultsWithDetails()
    }

    @Test
    fun loadListOfMinistry() {
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val page=PageRequest.of(1,10)
        val items=this.motorVehicleInspectionEntityRepo.findByMinistryReportSubmitStatusInAndSampled(listOf(map.workingStatus,map.initStatus, map.testStatus),"YES",page)
        Assertions.assertFalse(items.isEmpty,"Expected some list for ministry inspection")
    }

    @Test
    fun generateQaRandomInvoice() {
        val appId = applicationMapProperties.mapImportInspection
        val loggedInUser = usersRepo.findByUserName("KateWin")
        val map = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(1669)
        val permitType = qaDaoServices.findPermitType(2)
        if (loggedInUser != null) {
            qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, null)
        }
//        schedulerImpl.updateLabResultsWithDetails()
    }

    @Test
    fun demandNoteSubmission() {
        this.demandNoteRepository.findFirstByPaymentStatusAndCdRefNoIsNotNull(1)?.let { demandNote->
            destinationInspectionDaoServices.sendDemandNotGeneratedToKWIS(demandNote)
        }?:throw ExpectedDataNotFound("Could not find a single payment")
    }

    @Test
    fun cocSubmission() {
        this.cocRepository.findFirstByCocNumberIsNotNullAndCocTypeAndConsignmentDocIdIsNotNull("COC")?.let { coc->
            destinationInspectionDaoServices.sendLocalCoc(coc)
        }?:throw ExpectedDataNotFound("Could not find a COC document")
    }
    @Test
    fun coiSubmission() {
        this.cocRepository.findFirstByCoiNumberIsNotNullAndCocTypeAndConsignmentDocIdIsNotNull("COI")?.let { coi->
            destinationInspectionDaoServices.sendLocalCoi(coi)
        }?:throw ExpectedDataNotFound("Could not find a COI document")
    }

    @Test
    fun corSubmission() {
        this.corsEntityRepository.findFirstByChasisNumberIsNotNull()?.let { cor->
            destinationInspectionDaoServices.submitCoRToKesWS(cor)
        }?:throw ExpectedDataNotFound("Could not find a COR document")
    }

    @Test
    fun demandNoteCreationDetails() {
        val appId = applicationMapProperties.mapImportInspection



        usersRepo.findByUserName("kpaul7747@gmail.com")
            ?.let { loggedInUser ->
//                for (i in 101 downTo 123) {
                    //                    val payload = "Assigned Inspection Officer [assignedStatus= 1, assignedRemarks= test]"
                    val map = commonDaoServices.serviceMapDetails(appId)
//                    val demandNotes: MutableList<CdDemandNoteEntity> = mutableListOf()

                    var itemDetails = destinationInspectionDaoServices.findItemWithItemID(284)
                    with(itemDetails) {
                        paymentFeeIdSelected = iDIFeeDetailsRepo.findByIdOrNull(5)
                    }
                    itemDetails = destinationInspectionDaoServices.updateCdItemDetailsInDB(itemDetails, loggedInUser)
                    val importerDetails =
                        itemDetails.cdDocId?.cdImporter?.let { destinationInspectionDaoServices.findCDImporterDetails(it) }
                    val demandNote =
                            itemDetails.cdDocId?.let { destinationInspectionDaoServices.generateDemandNoteWithItemList(listOf(itemDetails), map, it,false,0.0, loggedInUser) }
//                    val demandNoteNumber = destinationInspectionDaoServices.demandNotePayment(demandNote, map, loggedInUser, itemDetails)
                    KotlinLogging.logger { }.info { "DEMAND NOTE GENERATED WITH ID = ${demandNote?.id} " }
//                    demandNotes.add(demandNote1)

                    demandNote?.demandNoteNumber?.let {
                        val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(loggedInUser.userName!!, it)
                        itemDetails.cdDocId?.cdType?.let { it1 ->
                            destinationInspectionDaoServices.findCdTypeDetails(it1.id).demandNotePrefix?.let { myPrefix ->
                                val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(
                                    demandNote,
                                    applicationMapProperties.mapInvoiceTransactionsForDemandNote,
                                    loggedInUser,
                                    batchInvoiceDetail
                                )

                                val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
                                with(myAccountDetails) {
                                    accountName = importerDetails?.name
                                    accountNumber = importerDetails?.pin
                                    currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
                                }
                                val invoiceDetail = invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(
                                    loggedInUser.userName!!,
                                    updateBatchInvoiceDetail,
                                    myAccountDetails
                                )
//                                invoiceDetail.invoiceId?.let {
//                                    val batchInvoiceDetail = invoiceDaoService.findInvoiceBatchDetails(it)
//                                    val updatedBatchInvoiceDetail = invoiceDaoService.updateInvoiceBatchDetailsAfterPaymentDone(batchInvoiceDetail)
//                                    if (updatedBatchInvoiceDetail.tableSource == myPrefix) {
//                                        invoiceDaoService.updateDemandNotDetailsAfterPaymentDone(invoiceDetail)
//                                    }
//                                }
                            }
                        }
                    }
//                }
            }
//        assertTrue { demandNoteNumber.isNotEmpty() }
    }

    @Test
    fun testPvocReconciliationReportDtoIsReturned() {
        val dateFromstr = "2021-01-13"
        val dateTostr = "2021-01-15"

        val returnedValues: PvocReconciliationReportDto? =
            pvocReconciliationReportEntityRepo.getPvocReconciliationReportDto(
                dateFromstr, dateTostr
            )
        KotlinLogging.logger { }.info {
            "inspectionfeesum = ${returnedValues?.inspectionfeesum}, " +
                    "verificationfeesum = ${returnedValues?.verificationfeesum}, royaltiestokebssum = ${returnedValues?.royaltiestokebssum} "
        }

    }

    @Autowired
    lateinit var iCountryTypeCodesRepository: ICountryTypeCodesRepository

    @Test
    fun canDecodeConsignmentDocument() {
        val stringToExclude = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        val consignmentDoc: ConsignmentDocument =
            commonDaoServices.deserializeFromXML(consignmentDocument, stringToExclude)
        KotlinLogging.logger {  }.info("DDD: "+consignmentDoc.documentDetails?.consignmentDocDetails?.cdStandardTwo?.attachments?.get(0)?.fileName)
        val details = CdTransportDetailsEntity()
        details.cargoTypeIndicator =
            consignmentDoc.documentDetails?.consignmentDocDetails?.cdTransport?.cargoTypeIndicator

        assertNotNull(consignmentDoc.documentDetails?.consignmentDocDetails?.cdStandard?.applicationTypeCode)
        assertNotNull(consignmentDoc.documentDetails?.consignmentDocDetails?.cdImporter?.pin)
        assertEquals("CD", consignmentDoc.documentDetails?.consignmentDocDetails?.cdStandard?.applicationTypeCode)

        assertNotNull(consignmentDoc.documentDetails?.consignmentDocDetails?.cdImporter?.physicalCountry)
        val physicalCountry = consignmentDoc.documentDetails?.consignmentDocDetails?.cdImporter?.physicalCountry!!

        val country = iCountryTypeCodesRepository.findByCountryCode(physicalCountry)
        assertNotNull(country)

        consignmentDoc.documentDetails?.consignmentDocDetails?.cdImporter?.physicalCountryName = country.countryName
        assertEquals("KENYA", consignmentDoc.documentDetails?.consignmentDocDetails?.cdImporter?.physicalCountryName)
    }

    @Test
    fun testUpdatingConsignmentDocument() {
        val cdInString = String(consignmentDocumentDaoService.findSavedCDXmlFile(401L, 1))
        consignmentDocumentDaoService.updateCDDetails(cdInString, "TEST101Status", "TEST101Date")
    }

    @Test
    fun testBRSDetails() {
       val cmp = CompanyProfileEntity()
        var arrayList = arrayListOf<String>("4","7","12")
        for(element in arrayList){
            cmp.registrationNumber = element
            registrationDaoServices.checkBrs(cmp)
        }

        val cdInString = String(consignmentDocumentDaoService.findSavedCDXmlFile(401L, 1))
        consignmentDocumentDaoService.updateCDDetails(cdInString, "TEST101Status", "TEST101Date")
    }

    @Test
    fun testInspectionScheduleReceivedFromKra() {
        val cdItemId: Long = 541
        val inspectionOfficerId: Long = 1083

        val cdItemEntity = destinationInspectionDaoServices.findItemWithItemID(cdItemId)
        val inspectionDate = commonDaoServices.getCalculatedDate(7)

//        destinationInspectionDaoServices.updateInspectionScheduleReceived(cdItemEntity, inspectionDate)

        cdItemEntity.cdDocId?.id?.let { cdId ->
            //Receive Inspection Schedule Complete
            destinationInspectionBpmn.diReceiveInspectionScheduleComplete(cdId, inspectionOfficerId).let {
                destinationInspectionBpmn.fetchTaskByObjectId(cdId, diMvWithCorProcessDefinitionKey)
                    ?.let { taskDetails ->
                        println("Task details after triggerInspectionScheduleRequest task complete")
                        for (taskDetail in taskDetails) {
                            taskDetail.task.let { task ->
                                println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                            }
                        }
                    }
            } ?: return
        }
    }

    @Test
    fun testReceiveInspectionPaymentConfirmation() {
        val cdId: Long = 481
        val inspectionOfficerId: Long = 1083

        //Receive Inspection Payment Confirmation
        destinationInspectionBpmn.diReceivePaymentConfirmation(cdId, inspectionOfficerId).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after diReceivePaymentConfirmation task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return
    }

    @Test
    fun testSendingCOC() {
//        val cdId: Long = 481
//        val inspectionOfficerId: Long = 1083
        val appId = applicationMapProperties.mapImportInspection

        usersRepo.findByUserName("kpaul7747@gmail.com")
            ?.let { loggedInUser ->
//                    val payload = "Assigned Inspection Officer [assignedStatus= 1, assignedRemarks= test]"
                val map = commonDaoServices.serviceMapDetails(appId)
                destinationInspectionDaoServices.findCdWithUcrNumber("UCR2100006322")
                    ?.let { cdDetails ->
                        KotlinLogging.logger { }.debug("Starting background task")
                        destinationInspectionDaoServices.createLocalCoc(loggedInUser, cdDetails, map, "","A")

                    }
            }
    }

    private fun updateCDDetails() {
        val consignmentDocument =
            consignmentDocument.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "")

        val xmlDoc: Document = XMLDocument.documentBuilder.parse(
            InputSource(StringReader(consignmentDocument))
        )
        xmlDoc.documentElement.normalize()
        val doc = XMLDocument(xmlDoc.documentElement)

        fun List<String>.toPath(): String {
            return "${Delimiters.pointerSeparator}" + this.joinToString(separator = "${Delimiters.pointerSeparator}")
        }

        val status = "2021-01-13"
        val date = "2021-01-15"

        val newStatus = doc.document.ownerDocument.createTextNode(status)
        val statusPath: String = listOf(
            "ConsignmentDocument",
            "DocumentDetails",
            "ConsignmentDocDetails",
            "CDStandard",
            "ApprovalStatus"
        ).toPath()
        doc.setNested(statusPath, XMLDocument(newStatus))

        val newDate = doc.document.ownerDocument.createTextNode(status)
        val datePath: String = listOf(
            "ConsignmentDocument",
            "DocumentDetails",
            "ConsignmentDocDetails",
            "CDStandard",
            "ApprovalDate"
        ).toPath()
        doc.setNested(datePath, XMLDocument(newDate))

        print(doc.toSerializable())
    }

    val consignmentDocument =
        """    
   <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ConsignmentDocument>
    <DocumentHeader>
        <DocumentReference>
            <DocumentType>CUSREG</DocumentType>
            <DocumentName>CONSIGNMENT DOCUMENT</DocumentName>
            <DocumentNumber>1215102903000</DocumentNumber>
            <CommonRefNumber>CD202000KEBSPVOC000002799101</CommonRefNumber>
            <MessageType>9</MessageType>
            <SenderID>eoga051</SenderID>
            <RegimeCode>C4</RegimeCode>
            <CMSRegimeCode>I</CMSRegimeCode>
            <ApprovalCategory>PRO</ApprovalCategory>
        </DocumentReference>
        <DocumentExchangeDetails>
            <ReceivingPartyDetails>
                <ReceivingParty>cust006</ReceivingParty>
            </ReceivingPartyDetails>
            <NotifyPartyDetails>
                <NotifyParty>NPM003</NotifyParty>
                <NotifyParty>NPM004</NotifyParty>
            </NotifyPartyDetails>
        </DocumentExchangeDetails>
    </DocumentHeader>
    <DocumentDetails>
        <ConsignmentDocDetails>
            <CDStandard>
                <ServiceProvider>
                    <ApplicationCode>RYC</ApplicationCode>
                    <Name>RYCE E AFRICA LTD</Name>
                    <TIN>P000591376W</TIN>
                    <PhysicalAddress>49729 NAIROBI</PhysicalAddress>
                    <PhyCountry>KE</PhyCountry>
                </ServiceProvider>
                <ApplicationTypeCode>CD</ApplicationTypeCode>
                <ApplicationTypeDescription>Consignment Document</ApplicationTypeDescription>
                <DocumentTypeCode>INR</DocumentTypeCode>
                <CMSDocumentTypeCode>R</CMSDocumentTypeCode>
                <DocumentTypeDescription>Inspection Report</DocumentTypeDescription>
                <ConsignmentTypeCode>C</ConsignmentTypeCode>
                <ConsignmentTypeDescription>Consignment</ConsignmentTypeDescription>
                <MDACode>KEBS</MDACode>
                <MDADescription>Kenya Bureau of Standards</MDADescription>
                <DocumentCode>PVOC</DocumentCode>
                <DocumentDescription>KEBS Inspection and Approval Document</DocumentDescription>
                <ProcessCode>PVOCPR</ProcessCode>
                <ProcessDescription>KEBS Inspection and Approval Process</ProcessDescription>
                <ApplicationDate>20201215</ApplicationDate>
                <UpdatedDate>202012151029</UpdatedDate>
                <ApprovalStatus>PC</ApprovalStatus>
                <ApprovalDate>20201215</ApprovalDate>
                <FinalApprovalDate>202012151029</FinalApprovalDate>
                <ApplicationRefNo>CD202000KEBSPVOC0000027991</ApplicationRefNo>
                <VersionNo>1</VersionNo>
                <UCRNumber>UCR26558653585</UCRNumber>
            </CDStandard>
            <CDImporter>
                <Name>RYCE E AFRICA LTD</Name>
                <TIN>P000591376W</TIN>
                <PhysicalAddress>49729 NAIROBI</PhysicalAddress>
                <PhyCountry>KE</PhyCountry>
                <PostalAddress>49729 NAIROBI</PostalAddress>
                <PosCountry>KE</PosCountry>
                <TeleFax>0</TeleFax>
                <Email>bngumi@kentrade.go.ke</Email>
            </CDImporter>
            <CDConsignee>
                <Name>TRADER</Name>
                <TIN>P000000000N</TIN>
                <PhysicalAddress>NAIROBI KENYA</PhysicalAddress>
                <PhyCountry>KE</PhyCountry>
                <PostalAddress>NAIROBI KENYA</PostalAddress>
                <PosCountry>KE</PosCountry>
                <TeleFax>0</TeleFax>
                <Email>onetouchltd@gmail.com</Email>
            </CDConsignee>
            <CDExporter>
                <Name>TRADER</Name>
                <TIN>P000000000N</TIN>
                <PhysicalAddress>NAIROBI KENYA</PhysicalAddress>
                <PhyCountry>KE</PhyCountry>
                <PostalAddress>NAIROBI KENYA</PostalAddress>
                <PosCountry>KE</PosCountry>
                <TeleFax>0</TeleFax>
                <Email>bngumi@kentrade.go.ke</Email>
                <SectorofActivity>TRD</SectorofActivity>
                <WarehouseCode>AIRFLOL</WarehouseCode>
                <WarehouseLocation>MSA</WarehouseLocation>
            </CDExporter>
            <CDConsignor>
                <Name>RYCE E AFRICA LTD</Name>
                <TIN>P000591376W</TIN>
                <PhysicalAddress>49729 NAIROBI</PhysicalAddress>
                <PhyCountry>KE</PhyCountry>
                <PostalAddress>49729 NAIROBI</PostalAddress>
                <PosCountry>KE</PosCountry>
                <TeleFax>45454545</TeleFax>
                <Email>info@ryce.com</Email>
                <SectorofActivity>MFG</SectorofActivity>
                <WarehouseCode>BJKA476</WarehouseCode>
                <WarehouseLocation>JKA</WarehouseLocation>
            </CDConsignor>
            <CDTransport>
                <ModeOfTransport>S</ModeOfTransport>
                <ModeOfTransportDesc>Sea</ModeOfTransportDesc>
                <VesselName>Others</VesselName>
                <VoyageNo>78220</VoyageNo>
                <DateOfArrival>20201005</DateOfArrival>
                <ShipmentDate>20201006</ShipmentDate>
                <Carrier>CAR</Carrier>
                <ManifestNo>1099</ManifestNo>
                <BLAWB>79900</BLAWB>
                <MarksAndNumbers>OTLX</MarksAndNumbers>
                <PortOfArrival>ESSUD</PortOfArrival>
                <PortOfArrivalDesc>JKIA</PortOfArrivalDesc>
                <PortOfDeparture>KEMBA</PortOfDeparture>
                <PortOfDepartureDesc>Mombasa</PortOfDepartureDesc>
                <CustomsOffice>ADY</CustomsOffice>
                <CustomsOfficeDesc>ANDY FORWARDERS</CustomsOfficeDesc>
                <FreightStation>TCC</FreightStation>
                <FreightStationDesc>Transglobal Cargo Centre</FreightStationDesc>
                <CargoTypeIndicator>B</CargoTypeIndicator>
            </CDTransport>
            <PGAHeaderFields/>
            <CDHeaderOne>
                <ForeignCurrencyCode>USD</ForeignCurrencyCode>
                <ForexRate>103.199</ForexRate>
                <FOBFCY>400.00</FOBFCY>
                <FreightFCY>0.00</FreightFCY>
                <InsuranceFCY>0.00</InsuranceFCY>
                <OtherChargesFCY>0.00</OtherChargesFCY>
                <CIFFCY>400.00</CIFFCY>
                <FOBNCY>41279.60</FOBNCY>
                <FreightNCY>0.00</FreightNCY>
                <InsuranceNCY>0.00</InsuranceNCY>
                <OtherChargesNCY>0.00</OtherChargesNCY>
                <CIFNCY>41279.60</CIFNCY>
                <INCOTerms>CIF</INCOTerms>
                <TransactionTerms>15</TransactionTerms>
                <COMESA>N</COMESA>
                <InvoiceDate>20201030</InvoiceDate>
                <InvoiceNumber>002</InvoiceNumber>
                <CountryOfSupply>KE</CountryOfSupply>
            </CDHeaderOne>
             <CDStandardTwo>
                <PurposeOfImport>PURPSE2</PurposeOfImport>
                <COCType>L</COCType>
                <LocalCOCType>EXE</LocalCOCType>
                <Attachments>
                    <AttachDocumentCode>COA</AttachDocumentCode>
                    <AttachDocumentCodeDesc>Certificate of analysis</AttachDocumentCodeDesc>
                    <AttachDocumentRefNo>COA</AttachDocumentRefNo>
                    <AttachDocumentInternalRefNo>CD2020000PHSDMC10002032365_1_COA_6185948.jpg
                    </AttachDocumentInternalRefNo>
                </Attachments>
                <Attachments>
                    <AttachDocumentCode>COQCOR</AttachDocumentCode>
                    <AttachDocumentCodeDesc>Certificate of quality from country of origin</AttachDocumentCodeDesc>
                    <AttachDocumentRefNo>UNBS</AttachDocumentRefNo>
                    <AttachDocumentInternalRefNo>CD2020000PHSDMC10002032365_1_UNBS_6185949.pdf
                    </AttachDocumentInternalRefNo>
                </Attachments>
                <Attachments>
                    <AttachDocumentCode>INV</AttachDocumentCode>
                    <AttachDocumentCodeDesc>Invoice</AttachDocumentCodeDesc>
                    <AttachDocumentRefNo>INV</AttachDocumentRefNo>
                    <AttachDocumentInternalRefNo>CD2020000PHSDMC10002032365_1_INV_6185947.pdf
                    </AttachDocumentInternalRefNo>
                </Attachments>
                <Attachments>
                    <AttachDocumentCode>PHR</AttachDocumentCode>
                    <AttachDocumentCodeDesc>Public health recommendation</AttachDocumentCodeDesc>
                    <AttachDocumentRefNo>INTER</AttachDocumentRefNo>
                    <AttachDocumentInternalRefNo>CD2020000PHSDMC10002032365_1_INTER_6185950.pdf
                    </AttachDocumentInternalRefNo>
                </Attachments>
                <Attachments>
                    <AttachDocumentCode>SDD</AttachDocumentCode>
                    <AttachDocumentCodeDesc>Safety data sheet</AttachDocumentCodeDesc>
                    <AttachDocumentRefNo>KDB</AttachDocumentRefNo>
                    <AttachDocumentInternalRefNo>CD2020000PHSDMC10002032365_1_KDB_6185951.pdf
                    </AttachDocumentInternalRefNo>
                </Attachments>
            </CDStandardTwo>
            <CDProductDetails>
                <ItemDetails>
                    <ItemCount>3</ItemCount>
                    <CDProduct1>
                        <ItemNo>1</ItemNo>
                        <ItemDescription>BLACK TEA</ItemDescription>
                        <ItemHSCode>0902400000</ItemHSCode>
                        <HSDescription>Other black tea (fermented) and other partly fermented tea whether or not flavoured.</HSDescription>
                        <ProductClassCode>A</ProductClassCode>
                        <ProductClassDescription>Class A</ProductClassDescription>
                        <Quantity>
                            <Qty>400.0000</Qty>
                            <UnitOfQty>KG</UnitOfQty>
                        </Quantity>
                        <PackageType>BG</PackageType>
                        <PackageQty>6.00</PackageQty>
                        <ForeignCurrencyCode>USD</ForeignCurrencyCode>
                        <UnitPriceFCY>0.50</UnitPriceFCY>
                        <TotalPriceFCY>200.00</TotalPriceFCY>
                        <UnitPriceNCY>51.60</UnitPriceNCY>
                        <CountryOfOrigin>KE</CountryOfOrigin>
                        <TotalPriceNCY>20639.80</TotalPriceNCY>
                        <ItemNetWeight>400.0000</ItemNetWeight>
                        <ItemGrossWeight>450.0000</ItemGrossWeight>
                    </CDProduct1>
                    <CDProduct2>
                        <ApplicantRemarks>-</ApplicantRemarks>
                    </CDProduct2>
                    <CDItemNonStandard/>
                </ItemDetails>
                <ItemDetails>
                    <ItemCount>3</ItemCount>
                    <CDProduct1>
                        <ItemNo>2</ItemNo>
                        <ItemDescription>Roasted Coffee</ItemDescription>
                        <ItemHSCode>0901210000</ItemHSCode>
                        <HSDescription>Coffee roasted, not decaffeinated.</HSDescription>
                        <Quantity>
                            <Qty>200.0000</Qty>
                            <UnitOfQty>KG</UnitOfQty>
                        </Quantity>
                        <PackageType>BG</PackageType>
                        <PackageQty>300.00</PackageQty>
                        <ForeignCurrencyCode>USD</ForeignCurrencyCode>
                        <UnitPriceFCY>0.50</UnitPriceFCY>
                        <TotalPriceFCY>100.00</TotalPriceFCY>
                        <UnitPriceNCY>51.60</UnitPriceNCY>
                        <CountryOfOrigin>JP</CountryOfOrigin>
                        <TotalPriceNCY>10319.90</TotalPriceNCY>
                        <ItemNetWeight>6000.0000</ItemNetWeight>
                        <ItemGrossWeight>6544.0000</ItemGrossWeight>
                    </CDProduct1>
                    <CDProduct2>
                        <ApplicantRemarks>-</ApplicantRemarks>
                    </CDProduct2>
                    <CDItemNonStandard/>
                </ItemDetails>
                <ItemDetails>
                    <ItemCount>3</ItemCount>
                    <CDProduct1>
                        <ItemNo>3</ItemNo>
                        <ItemDescription>Green Tea</ItemDescription>
                        <ItemHSCode>902200000</ItemHSCode>
                        <HSDescription>Green tea, nes</HSDescription>
                        <Quantity>
                            <Qty>300.0000</Qty>
                            <UnitOfQty>KG</UnitOfQty>
                        </Quantity>
                        <PackageType>BG</PackageType>
                        <PackageQty>300.00</PackageQty>
                        <ForeignCurrencyCode>USD</ForeignCurrencyCode>
                        <UnitPriceFCY>0.34</UnitPriceFCY>
                        <TotalPriceFCY>100.00</TotalPriceFCY>
                        <UnitPriceNCY>34.40</UnitPriceNCY>
                        <CountryOfOrigin>JP</CountryOfOrigin>
                        <TotalPriceNCY>10319.80</TotalPriceNCY>
                        <ItemNetWeight>200.0000</ItemNetWeight>
                        <ItemGrossWeight>200.0000</ItemGrossWeight>
                    </CDProduct1>
                    <CDProduct2>
                        <ApplicantRemarks>-</ApplicantRemarks>
                    </CDProduct2>
                    <CDItemNonStandard/>
                </ItemDetails>
            </CDProductDetails>
        </ConsignmentDocDetails>
    </DocumentDetails>
    <DocumentSummary>
        <IssuedDateTime>15122020134508</IssuedDateTime>
        <SummaryPageUrl>https://trial.kenyatradenet.go.ke/keswsoga/SummaryPage.mda?ucr=UCR202000124946</SummaryPageUrl>
    </DocumentSummary>
</ConsignmentDocument>
            """.trimIndent()

    @Test
    @Ignore
    fun testCocEmailAttachment() {
        val appId = applicationMapProperties.mapImportInspection
        usersRepo.findByUserName("kpaul7747@gmail.com")
            ?.let { loggedInUser ->
                val map = commonDaoServices.serviceMapDetails(appId)
                val consignmentDocumentEntity: ConsignmentDocumentDetailsEntity = destinationInspectionDaoServices.findCD(941)
                val localCoc = destinationInspectionDaoServices.createLocalCoc(loggedInUser, consignmentDocumentEntity, map, "","A")
                consignmentDocumentEntity.cdStandard?.let { cdStd ->
                    destinationInspectionDaoServices.updateCDStatus(
                        cdStd,
                        applicationMapProperties.mapDICdStatusTypeCOCGeneratedAndSendID
                    )
                }
                KotlinLogging.logger { }.info { "localCoc = ${localCoc.id}" }
//                reportsDaoService.generateLocalCoCReportWithDataSource(consignmentDocumentEntity, applicationMapProperties.mapReportLocalCocPath)?.let { file ->
//            notifications.processEmail("anthonykihagi@gmail.com","Test subject","Test Message",file.path)
                    KotlinLogging.logger { }.info { " ::::::::::::: Success :::::::::::::::" }
                }
            }

//    @Test
//    @Ignore
//    fun testCorEmailAttachment() {
//        val consignmentDocumentEntity: ConsignmentDocumentDetailsEntity = destinationInspectionDaoServices.findCD(863)
//        reportsDaoService.generateLocalCoRReport(consignmentDocumentEntity, applicationMapProperties.mapReportLocalCorPath,0L)?.let { file ->
//            consignmentDocumentEntity.cdImporter?.let { destinationInspectionDaoServices.findCDImporterDetails(it)
//            }?.let { importer ->
//                importer.email?.let { destinationInspectionDaoServices.sendLocalCorReportEmail(it, file.path) }
//            }
//        }
//    }
//
//    @Test
//    @Ignore
//    fun testKebsEmailConfig() {
//        notifications.sendEmail("anthonykihagi@gmail.com","Test subject","Test Message")
//    }
}
