package org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc.*
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SftpServiceImpl
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.integ.PvocIntegrationProperties
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.customdto.*
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocQueriesDataEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocSealIssuesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocStdMonitoringDataEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant

@Service
class PvocServiceFlux(
    private val workflowTransactionsRepository: IWorkflowTransactionsRepository,
    private val pvocIntegrationProperties: PvocIntegrationProperties,
    private val cocsRepository: ICocsRepository,
    private val daoService: DaoService,
    private val cocItemsRepository: ICocItemsRepository,
    private val coiItemsRepository: ICoiItemsRepository,
    private val corsBakRepository: ICorsBakRepository,
    private val coisRepository: ICoisRepository,
    private val riskProfileDataRepository: IRiskProfileDataRepository,
   // private val timelinesDataRepository: IPvocTimelinesDataRepository,
    private val pvocStdMonitoringDataRepository: IPvocStdMonitoringDataRepository,
    private val pvocQueriesDataRepository: IPvocQueriesDataRepository,
    private val idfsRepository: IIdfsRepository,
    private val idfItemsRepository: IIdfItemsRepository,
    private val pvocInvoicing: IPvocInvoicingRepository,
    private val pvocPartnersRepository: IPvocPartnersRepository,
    private val rfcGoodsRepository: IRfcGoodsRepository,
    private val rfcCoiRepository: IRfcCoiRepository,
    private val rfcCoiItemsRepository: IRfcCoiItemsRepository,
    private val rfcCorRepository: IRfcCorRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val sftpService: SftpServiceImpl
) {

    @Autowired
    lateinit var iCocItemRepository: ICocItemRepository

    fun saveCocData(cocData: CocsEntity): GeneralResponse {
        val generalResponse = GeneralResponse()

        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }
            cocsRepository.save(cocData)
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
            log.responseStatus = generalResponse.responseCode.toString()
            log.responseMessage = generalResponse.responseMessage

        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegFailureResponseMessage
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveCocDataWithItems(cocData: CocWithItems): GeneralResponse {
        val generalResponse = GeneralResponse()

        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())
            }
            //Check if CoC with same UCR Number has been created before
            var cocExists = false
            cocData.coc?.ucrNumber?.let {
      //          cocsRepository.findByUcrNumber(it)?.let { cocExists = true }
            }
            if (cocExists) {
                KotlinLogging.logger {  }.info { "CoC with UCR number already exists" }
                generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
                generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegFailureResponseMessage
                log.responseStatus = generalResponse.responseCode.toString()
                log.responseMessage = generalResponse.responseMessage
                log.transactionCompletedDate = Timestamp.from(Instant.now())

                try {
                    workflowTransactionsRepository.save(log)
                } catch (e: Exception) {
                    KotlinLogging.logger { }.error(e.message)
                    KotlinLogging.logger { }.debug(e.message, e)

                }
                return generalResponse
            }
            //Save the CoC
            val savedCoc = cocData.coc?.let { cocsRepository.save(it) }
            if (savedCoc != null) {
                //Save Items
                cocData.items?.forEach { item ->
                    item.cocId = savedCoc.id
                    cocItemsRepository.save(item)
                }
                //Send CoC to KeSWS
                try {
                    // this.submitCocToKeSWS(savedCoc)
                } catch (e: Exception) {
                    e.printStackTrace()
                    KotlinLogging.logger { }.error(e.message)
                    KotlinLogging.logger { }.debug(e.message, e)
                }
            }
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
            log.responseStatus = generalResponse.responseCode.toString()
            log.responseMessage = generalResponse.responseMessage

        } catch (e: Exception) {
            e.printStackTrace()
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegFailureResponseMessage
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveCoiDataWithItems(coiData: CoiWithItems): GeneralResponse {
        val generalResponse = GeneralResponse()

        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())
            }
            //Check if CoI with same UCR Number has been created before
            var cocExists = false
            coiData.coi?.ucrNumber?.let {
                //    cocsRepository.findByUcrNumber(it)?.let { cocExists = true }
            }
            if (cocExists) {
                KotlinLogging.logger {  }.info { "CoC with UCR number already exists" }
                generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
                generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegFailureResponseMessage
                log.responseStatus = generalResponse.responseCode.toString()
                log.responseMessage = generalResponse.responseMessage
                log.transactionCompletedDate = Timestamp.from(Instant.now())

                try {
                    workflowTransactionsRepository.save(log)
                } catch (e: Exception) {
                    KotlinLogging.logger { }.error(e.message)
                    KotlinLogging.logger { }.debug(e.message, e)

                }
                return generalResponse
            }
            //Save the CoI
            val savedCoc = coiData.coi?.let { cocsRepository.save(it) }
            if (savedCoc != null) {
                //Save Items
                coiData.items?.forEach { item ->
                    item.cocId = savedCoc.id
                    cocItemsRepository.save(item)
                }
                //Send CoI to KeSWS
                try {
                  //  this.submitCoiToKeSWS(savedCoc)
                } catch (e: Exception) {
//                    e.printStackTrace()
                    KotlinLogging.logger { }.error(e.message, e)
                    KotlinLogging.logger { }.debug(e.message, e)
                }
            }
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
            log.responseStatus = generalResponse.responseCode.toString()
            log.responseMessage = generalResponse.responseMessage
        } catch (e: Exception) {
            e.printStackTrace()
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegFailureResponseMessage
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

//    fun submitCocToKeSWS(cocData: CocsEntity) {
//        val coc = CustomCocXmlDto(this.cocNumber, this.idfNumber, this.rfiNumber, this.ucrNumber, this.rfcDate, this.cocIssueDate, this.clean, this.cocRemarks, this.issuingOffice, this.importerName, this.importerPin, this.importerAddress1, this.importerAddress2, this.importerCity, this, importerCountry, this.importerZipCode, this.importerTelephoneNumber, this.importerFaxNumber, this.importerEmail, this.exporterName, this.exporterPin, this.exporterAddress1, this.exporterAddress2, this.exporterCity, this.exporterCountry, this.exporterZipCode, this.exporterTelephoneNumber, this.exporterFaxNumber, this.exporterEmail, this.placeOfInspection, this.dateOfInspection, this.portOfDestination, this.shipmentMode, this.countryOfSupply, this.finalInvoiceFobValue, ).apply {
//            cocNumber = cocData.cocNumber
//            idfNumber = cocData.idfNumber
//            rfiNumber = cocData.rfiNumber
//            ucrNumber = cocData.ucrNumber
//            rfcDate = convertTimestampToKeswsValidDate(cocData.rfcDate ?: throw Exception("MISSING rfcDate"))
//            cocIssueDate =
//                convertTimestampToKeswsValidDate(cocData.cocIssueDate ?: throw Exception("MISSING cocIssueDate"))
//            isClean = cocData.clean
//            cocRemarks = cocData.cocRemarks
//            issuingOffice = cocData.issuingOffice
//            importerName = cocData.importerName
//            importerPin = cocData.importerPin
//            importerAddress1 = cocData.importerAddress1
//            importerAddress2 = cocData.importerAddress2
//            importerCity = cocData.importerCity
//            importerCountry = cocData.importerCountry
//            importerZipCode = cocData.importerZipCode
//            importerTelephoneNumber = cocData.importerTelephoneNumber
//            importerFaxNumber = cocData.importerFaxNumber
//            importerEmail = cocData.importerEmail
//            exporterName = cocData.exporterName
//            exporterPin = cocData.exporterPin
//            exporterAddress1 = cocData.exporterAddress1
//            exporterAddress2 = cocData.exporterAddress2
//            exporterCity = cocData.exporterCity
//            exporterCountry = cocData.exporterCountry
//            exporterZipCode = cocData.exporterZipCode
//            exporterTelephoneNumber = cocData.exporterTelephoneNumber
//            exporterFaxNumber = cocData.exporterFaxNumber
//            exporterEmail = cocData.exporterEmail
//            placeOfInspection = cocData.placeOfInspection
//            dateOfInspection = convertTimestampToKeswsValidDate(
//                cocData.dateOfInspection ?: throw Exception("MISSING dateOfInspection")
//            )
//            portOfDestination = cocData.portOfDestination
//            shipmentMode = cocData.shipmentMode
//            countryOfSupply = cocData.countryOfSupply
//            finalInvoiceFobValue = cocData.finalInvoiceFobValue.toString()
//            finalInvoiceExchangeRate = cocData.finalInvoiceExchangeRate.toString()
//            finalInvoiceCurrency = cocData.finalInvoiceCurrency
//            finalInvoiceDate = convertTimestampToKeswsValidDate(
//                cocData.finalInvoiceDate ?: throw Exception("MISSING finalInvoiceDate")
//            )
//            shipmentPartialNumber = cocData.shipmentPartialNumber
//            shipmentSealNumbers = cocData.shipmentSealNumbers
//            shipmentContainerNumber = cocData.shipmentContainerNumber
//            shipmentGrossWeight = cocData.shipmentGrossWeight
//            shipmentQuantityDelivered = cocData.shipmentQuantityDelivered
//            route = cocData.route
//            productCategory = cocData.productCategory
//            partner = cocData.partner
//            cocDetals = null
//        }
//        val cocItem = iCocItemRepository.findByCocId(cocData.id)?.get(0)
//        cocItem?.toCocItemDetailsXmlRecordRefl(cocData.cocNumber?:"").let {
//            coc.cocDetals = it
//            val cocFinalDto = COCXmlDTO()
//            cocFinalDto.coc = coc
//            val fileName = cocFinalDto.coc?.ucrNumber?.let {
//                createKesWsFileName(
//                    applicationMapProperties.mapKeswsCocDoctype,
//                    it
//                )
//            }
//            val xmlFile = fileName?.let { serializeToXml(it, cocFinalDto) }
//            xmlFile.let { it1 -> it1?.let { sftpService.uploadFile(it) } }
//        }
//    }

    fun convertTimestampToKeswsValidDate(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.format(timestamp)
        return date
    }

//    fun submitCoiToKeSWS(cocData: CocsEntity) {
//        KotlinLogging.logger { }.info { "The COI entity: ${cocData.coiNumber}" }
//        val coi = CustomCoiXmlDto().apply {
//            id = cocData.id
//            coiNumber = cocData.coiNumber
//            idfNumber = cocData.idfNumber
//            rfiNumber = cocData.rfiNumber
//            ucrNumber = cocData.ucrNumber
//            rfcDate = convertTimestampToKeswsValidDate(cocData.rfcDate ?: throw Exception("MISSING RFC DATE"))
//            coiIssueDate =
//                convertTimestampToKeswsValidDate(cocData.coiIssueDate ?: throw Exception("MISSING  coiIssueDate"))
//            clean = cocData.clean
//            coiRemarks = cocData.coiRemarks
//            issuingOffice = cocData.issuingOffice
//            importerName = cocData.importerName
//            importerPin = cocData.importerPin
//            importerAddress1 = cocData.importerAddress1
//            importerCity = cocData.importerCity
//            importerCountry = cocData.importerCountry
//            importerZipcode = cocData.importerZipCode
//            importerTelephoneNumber = cocData.importerTelephoneNumber
//            importerFaxNumber = cocData.importerFaxNumber
//            importerEmail = cocData.importerEmail
//            exporterName = cocData.exporterName
//            exporterPin = cocData.exporterPin
//            exporterAddress1 = cocData.exporterAddress1
//            exporterCity = cocData.exporterCity
//            exporterCountry = cocData.exporterCountry
//            exporterZipcode = cocData.exporterZipCode
//            exporterTelephoneNumber = cocData.exporterTelephoneNumber
//            exporterFaxNumber = cocData.exporterFaxNumber
//            exporterEmail = cocData.exporterEmail
//            placeOfInspection = cocData.placeOfInspection
//            dateOfInspection = convertTimestampToKeswsValidDate(
//                cocData.dateOfInspection ?: throw Exception("MISSING  dateOfInspection")
//            )
//            portOfDestination = cocData.portOfDestination
//            shipmentMode = cocData.shipmentMode
//            countryOfSupply = cocData.countryOfSupply
//            finalInvoiceFobValue = cocData.finalInvoiceFobValue
//            finalInvoiceExchangeRate = cocData.finalInvoiceExchangeRate
//            finalInvoiceCurrency = cocData.finalInvoiceCurrency
//            finalInvoiceNumber = cocData.finalInvoiceNumber
//            finalInvoiceDate = convertTimestampToKeswsValidDate(
//                cocData.finalInvoiceDate ?: throw Exception("MISSING  finalInvoiceDate")
//            )
//            shipmentPartialNumber = cocData.shipmentPartialNumber
//            shipmentSealNumbers = cocData.shipmentSealNumbers
//            shipmentGrossWeight = cocData.shipmentGrossWeight
//            productCategory = cocData.productCategory
//            route = "D"
//        }
//        val coiFinalDto = COIXmlDTO()
//        coiFinalDto.coi = coi
//
//        val fileName = coiFinalDto.coi?.ucrNumber?.let {
//            createKesWsFileName(
//                applicationMapProperties.mapKeswsCoiDoctype,
//                it
//            )
//        }
//        val xmlFile = fileName?.let { serializeToXml(it, coiFinalDto) }
//        xmlFile?.let { it1 -> sftpService.uploadFile(it1) }
//    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveCoiDataWithItems(cocData: CoiWithItemsResponse): GeneralResponse {
        val generalResponse = GeneralResponse()

        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }
            cocData.coiData?.let {
                coisRepository.save(it).id.let { id ->
                    cocData.coiItems?.forEach { item ->
                        item.coiId = id
                        coiItemsRepository.save(item)
                    }
                }

            }

            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
            log.responseStatus = generalResponse.responseCode.toString()
            log.responseMessage = generalResponse.responseMessage

        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegFailureResponseMessage
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    fun saveCocItemsData(cocItemsData: CocItemsEntity): GeneralResponse {
        val generalResponse = GeneralResponse()
        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())
            }

//            cocItemsData.cocNumber
//                ?.let {
//                    cocsRepository.findByUcrNumber(it)
//                        ?.let { coc ->
//                            cocItemsData.cocId = coc.id
//                            cocItemsRepository.save(cocItemsData)
//                        }
//                        ?: throw InvalidValueException("No COC Items found for UCR#:  $it")
//                }
//                ?: throw InvalidValueException("Invalid COC#:  ${cocItemsData.cocNumber}")

            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = e.message
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }


//    fun saveCorData(corData: CorsBakEntity): GeneralResponse {
//        val generalResponse = GeneralResponse()
//        val log = WorkflowTransactionsEntity()
//        try {
//            with(log) {
//                transactionDate = Date(java.util.Date().time)
//                transactionStartDate = Timestamp.from(Instant.now())
//                retried = 0
//                createdOn = Timestamp.from(Instant.now())
//
//            }
//            val savedCorData = corsBakRepository.save(corData)
//
//            //Send COR data to KeSWS
//            try {
//                val cor = CustomCorXmlDto().apply {
//                    id = savedCorData.id.toString()
//                    corNumber = savedCorData.corNumber
//                    corIssueDate = convertTimestampToKeswsValidDate(
//                        savedCorData.corIssueDate ?: throw Exception("Missing corIssueDate")
//                    )
//                    countryOfSupply = savedCorData.countryOfSupply
//                    inspectionCenter = savedCorData.inspectionCenter
//                    exporterName = savedCorData.exporterName
//                    exporterAddress1 = savedCorData.exporterAddress1
//                    exporterEmail = savedCorData.exporterEmail
//                    applicationBookingDate = convertTimestampToKeswsValidDate(
//                        savedCorData.applicationBookingDate ?: throw Exception("Missing applicationBookingDate")
//                    )
//                    inspectionDate = convertTimestampToKeswsValidDate(
//                        savedCorData.inspectionDate ?: throw Exception("Missing inspectionDate")
//                    )
//                    make = savedCorData.make
//                    model = savedCorData.model
//                    chasisNumber = savedCorData.chasisNumber
//                    engineNumber = savedCorData.engineNumber
//                    engineCapacity = savedCorData.engineCapacity
//                    yearOfManufacture = savedCorData.yearOfManufacture
//                    yearOfFirstRegistration = savedCorData.yearOfFirstRegistration
//                    inspectionMileage = savedCorData.inspectionMileage
//                    unitsOfMileage = savedCorData.unitsOfMileage
//                    inspectionRemarks = savedCorData.inspectionRemarks
//                    previousRegistrationNumber = savedCorData.previousRegistrationNumber
//                    previousCountryOfRegistration = savedCorData.previousCountryOfRegistration
//                    tareWeight = savedCorData.tareWeight.toString()
//                    loadCapacity = savedCorData.loadCapacity.toString()
//                    grossWeight = savedCorData.grossWeight.toString()
//                    numberOfAxles = savedCorData.numberOfAxles.toString()
//                    typeOfVehicle = savedCorData.typeOfVehicle
//                    numberOfPassangers = savedCorData.numberOfPassangers.toString()
//                    typeOfBody = savedCorData.typeOfBody
//                    bodyColor = savedCorData.bodyColor
//                    fuelType = savedCorData.fuelType
//                    version = "1"
//                }
//                val corDto = CORXmlDTO()
//                corDto.cor = cor
//                val fileName = corDto.cor?.chasisNumber?.let {
//                    createKesWsFileName(
//                        applicationMapProperties.mapKeswsCorDoctype,
//                        it
//                    )
//                }
//                val xmlFile = fileName?.let { serializeToXml(it, corDto) }
//                xmlFile.let { it1 -> it1?.let { sftpService.uploadFile(it) } }
//            } catch (e: Exception) {
//                KotlinLogging.logger { }.error(e.message)
//                KotlinLogging.logger { }.debug(e.message, e)
//            }
//
//            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
//            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
//        } catch (e: Exception) {
//            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
//            generalResponse.responseMessage = e.message
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//            log.responseMessage = e.message
//        }
//        log.transactionCompletedDate = Timestamp.from(Instant.now())
//
//        try {
//            workflowTransactionsRepository.save(log)
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//
//        }
//
//        return generalResponse
//    }

//    fun saveCoiData(coiData: CoisEntity): GeneralResponse {
//        val generalResponse = GeneralResponse()
//        val log = WorkflowTransactionsEntity()
//        try {
//            with(log) {
//                transactionDate = Date(java.util.Date().time)
//                transactionStartDate = Timestamp.from(Instant.now())
//                retried = 0
//                createdOn = Timestamp.from(Instant.now())
//
//            }
//            coisRepository.save(coiData)
//
//
//            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
//            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
//        } catch (e: Exception) {
//            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
//            generalResponse.responseMessage = e.message
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//            log.responseMessage = e.message
//        }
//        log.transactionCompletedDate = Timestamp.from(Instant.now())
//
//        try {
//            workflowTransactionsRepository.save(log)
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//
//        }
//
//        return generalResponse
//    }

    //coi with items main
//    fun saveCoiWithItemsData(coiData: CoiWithItemsResponse): GeneralResponse {
//        val generalResponse = GeneralResponse()
//        val log = WorkflowTransactionsEntity()
//        try {
//            with(log) {
//                transactionDate = Date(java.util.Date().time)
//                transactionStartDate = Timestamp.from(Instant.now())
//                retried = 0
//                createdOn = Timestamp.from(Instant.now())
//
//            }
//            coiData.coiData?.let {
//                coisRepository.save(it).id.let { id ->
//                    coiData.coiItems?.forEach { item ->
//                        item.coiId = id
//                        coiItemsRepository.save(item)
//                    }
//                }
//            }
//
//            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
//            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
//        } catch (e: Exception) {
//            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
//            generalResponse.responseMessage = e.message
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//            log.responseMessage = e.message
//        }
//        log.transactionCompletedDate = Timestamp.from(Instant.now())
//
//        try {
//            workflowTransactionsRepository.save(log)
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message)
//            KotlinLogging.logger { }.debug(e.message, e)
//
//        }
//
//        return generalResponse
//    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveRfcCoiWithItemsData(rfcCoiData: RfcCoiWithItems): GeneralResponse {
        val generalResponse = GeneralResponse()
        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }

            rfcCoiData.rfcCoi?.let {
                rfcCoiRepository.save(it).id.let { id ->
                    rfcCoiData.rfcCoiItems?.forEach { item ->
                        item.rfcId = id
                        rfcCoiItemsRepository.save(item)
                    }
                }

            }

            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = e.message
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveIdfWithItemsData(idfData: IdfWithItemsResponse): GeneralResponse {
        val generalResponse = GeneralResponse()
        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }

            idfData.idf?.let {
                idfsRepository.save(it).id.let { id ->
                    idfData.idfItems?.forEach { item ->
                        item.idfId = id
                        idfItemsRepository.save(item)
                    }
                }

            }

            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = e.message
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    fun saveCoiItemsData(coiItemsData: CoiItemsEntity): GeneralResponse {

        val generalResponse = GeneralResponse()
        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }

            coiItemsData.coiNumber
                ?.let {
                    coisRepository.findByCoiNumber(it)
                        ?.let { coi ->
                            coiItemsData.coiId = coi.id
                            coiItemsRepository.save(coiItemsData)
                        }
                        ?: throw InvalidValueException("No COI found for COI#:  $it")
                }
                ?: throw InvalidValueException("Invalid COI Number")
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = e.message
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    fun saveRiskData(riskData: RiskProfileDataEntity): GeneralResponse {
        val generalResponse = GeneralResponse()
        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }
            riskProfileDataRepository.save(riskData)


            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = e.message
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    fun saveTimelinesData(monitoringTimelines: PvocSealIssuesEntity): GeneralResponse {
        val generalResponse = GeneralResponse()
        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }
            //timelinesDataRepository.save(monitoringTimelines)


            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = e.message
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    fun saveStandardsUsedData(monitoringStandards: PvocStdMonitoringDataEntity): GeneralResponse {
        val generalResponse = GeneralResponse()
        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }
            pvocStdMonitoringDataRepository.save(monitoringStandards)


            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = e.message
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    fun savePvocQueriesData(monitoringQueries: PvocQueriesDataEntity): GeneralResponse {
        val generalResponse = GeneralResponse()
        val log = WorkflowTransactionsEntity()
        try {
            with(log) {
                transactionDate = Date(java.util.Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                retried = 0
                createdOn = Timestamp.from(Instant.now())

            }
            pvocQueriesDataRepository.save(monitoringQueries)


            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegSuccessResponseCode
            generalResponse.responseMessage = pvocIntegrationProperties.pvocIntegSuccessResponseMessage
        } catch (e: Exception) {
            generalResponse.responseCode = pvocIntegrationProperties.pvocIntegFailureResponseCode
            generalResponse.responseMessage = e.message
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseMessage = e.message
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)

        }

        return generalResponse
    }

    fun getMonitoringQueriesData(monitoringQueries: PvocQueriesResponse): List<PvocQueriesResponse> {

        monitoringQueries.rfcNumber
            ?.let { rfcNumber ->
                monitoringQueries.cocNumber
                    ?.let { cocNumber ->
                        monitoringQueries.ucrNumber
                            ?.let { ucrNumber ->
                                monitoringQueries.invoiceNumber
                                    ?.let { invoiceNumber ->
                                        pvocQueriesDataRepository.findByRfcNumberOrCocNumberOrUcrNumberOrInvoiceNumber(rfcNumber, cocNumber, ucrNumber, invoiceNumber)
                                            ?.let { queries ->
                                                val lists = mutableListOf<PvocQueriesResponse>()
                                                queries.forEach { query ->
                                                    val pvocQueriesResponse = PvocQueriesResponse()

                                                    pvocQueriesResponse.cocNumber = query.cocNumber
                                                    pvocQueriesResponse.rfcNumber = query.rfcNumber
                                                    pvocQueriesResponse.invoiceNumber = query.invoiceNumber
                                                    pvocQueriesResponse.ucrNumber = query.ucrNumber
                                                    pvocQueriesResponse.kebsQuery = query.kebsQuery
                                                    pvocQueriesResponse.kebsResponse = query.kebsResponse
                                                    pvocQueriesResponse.partnerQuery = query.partnerQuery
                                                    pvocQueriesResponse.partnerResponse = query.partnerResponse
                                                    pvocQueriesResponse.partnerResponseAnalysis = query.partnerResponseAnalysis
                                                    pvocQueriesResponse.conclusion = query.conclusion
                                                    pvocQueriesResponse.linkToUploads = query.linkToUploads

                                                    lists.add(pvocQueriesResponse)
                                                }
                                                return lists

                                            }

                                            ?: throw InvalidInputException("Nothing found matching criteria provided")
                                    }
                                    ?: throw InvalidInputException("Invoice number cannot be empty")
                            }
                            ?: throw InvalidInputException("UCR number cannot be empty")
                    }
                    ?: throw InvalidInputException("COC number cannot be empty")
            }
            ?: throw InvalidInputException("RFC number cannot be empty")


    }

    fun getRiskProfileData(categorizationDate: Date): MutableList<RiskProfileResponse> {
        val profiles = mutableListOf<RiskProfileResponse>()
        riskProfileDataRepository.findByCategorizationDate(categorizationDate)
            .let { riskList ->
                riskList.forEach { risk ->
                    val items = RiskProfileResponse()
                    items.hsCode = risk.hsCode
                    items.brandName = risk.brandName
                    items.productDescription = risk.productDescription
                    items.countryOfSupply = risk.countryOfSupply
                    items.manufacturer = risk.manufacturer
                    items.traderName = risk.traderName
                    items.importerName = risk.importerName
                    items.exporterName = risk.exporterName
                    items.riskLevel = risk.riskLevel
                    items.riskDescription = risk.riskDescription
                    items.remarks = risk.remarks
                    items.categorizationDate = risk.categorizationDate

                    profiles.add(items)

                }
                return profiles

            }


    }

    fun getIdfsData(country: String, includeItems: Boolean = false): List<IdfsResponse> {
        idfsRepository.findByCountryOfSupplyAndStatus(country, 0)
            ?.let { idfs ->
                val lists = mutableListOf<IdfsResponse>()
                idfs.forEach { idf ->

                    val idfsResponse = IdfsResponse()

                    idfsResponse.idfNumber = idf.idfNumber
                    idfsResponse.ucr = idf.ucr
                    idfsResponse.importerName = idf.importerName
                    idfsResponse.importerAddress = idf.importerAddress
                    idfsResponse.importerContactName = idf.importerContactName
                    idfsResponse.importerEmail = idf.importerEmail
                    idfsResponse.importerFax = idf.importerFax
                    idfsResponse.importerTelephoneNumber = idf.importerTelephoneNumber
                    idfsResponse.sellerAddress = idf.sellerAddress
                    idfsResponse.sellerContactName = idf.sellerContactName
                    idfsResponse.sellerEmail = idf.sellerEmail
                    idfsResponse.sellerFax = idf.sellerFax
                    idfsResponse.sellerName = idf.sellerName
                    idfsResponse.sellerTelephoneNumber = idf.sellerTelephoneNumber
                    idfsResponse.countryOfSupply = idf.countryOfSupply
                    idfsResponse.portOfDischarge = idf.portOfDischarge
                    idfsResponse.portOfCustomsClearance = idf.portOfCustomsClearance
                    idfsResponse.modeOfTransport = idf.modeOfTransport
                    idfsResponse.comesa = idf.comesa
                    idfsResponse.invoiceNumber = idf.invoiceNumber
                    idfsResponse.invoiceDate = idf.invoiceDate
                    idfsResponse.currency = idf.currency
//                    idfsResponse.exchangeRate = idf.exchangeRate
//                    idfsResponse.fobValue = idf.fobValue
//                    idfsResponse.freight = idf.freight
//                    idfsResponse.insurance = idf.insurance
//                    idfsResponse.otherCharges = idf.otherCharges
//                    idfsResponse.total = idf.total
                    idfsResponse.observations = idf.observations
                    if (includeItems) {
                        idfsResponse.idfNumber?.let { idfsResponse.items.addAll(getIdfItemsData(idfNumber = it)) }

                    }
                    lists.add(idfsResponse)

                }
                return lists

            }
            ?: throw InvalidInputException("No matching IDFs for  $country")
    }

    fun getIdfData(country: String): IdfsResponse {

        idfsRepository.findFirstByCountryOfSupplyAndStatus(country, 0)
            ?.let { idf ->
                val idfsResponse = IdfsResponse()

                idfsResponse.idfNumber = idf.idfNumber
                idfsResponse.ucr = idf.ucr
                idfsResponse.importerName = idf.importerName
                idfsResponse.importerAddress = idf.importerAddress
                idfsResponse.importerContactName = idf.importerContactName
                idfsResponse.importerEmail = idf.importerEmail
                idfsResponse.importerFax = idf.importerFax
                idfsResponse.importerTelephoneNumber = idf.importerTelephoneNumber
                idfsResponse.sellerAddress = idf.sellerAddress
                idfsResponse.sellerContactName = idf.sellerContactName
                idfsResponse.sellerEmail = idf.sellerEmail
                idfsResponse.sellerFax = idf.sellerFax
                idfsResponse.sellerName = idf.sellerName
                idfsResponse.sellerTelephoneNumber = idf.sellerTelephoneNumber
                idfsResponse.countryOfSupply = idf.countryOfSupply
                idfsResponse.portOfDischarge = idf.portOfDischarge
                idfsResponse.portOfCustomsClearance = idf.portOfCustomsClearance
                idfsResponse.modeOfTransport = idf.modeOfTransport
                idfsResponse.comesa = idf.comesa
                idfsResponse.invoiceNumber = idf.invoiceNumber
                idfsResponse.invoiceDate = idf.invoiceDate
                idfsResponse.currency = idf.currency
//                idfsResponse.exchangeRate = idf.exchangeRate
//                idfsResponse.fobValue = idf.fobValue
//                idfsResponse.freight = idf.freight
//                idfsResponse.insurance = idf.insurance
//                idfsResponse.otherCharges = idf.otherCharges
//                idfsResponse.total = idf.total
                idfsResponse.observations = idf.observations
                return idfsResponse
            }
            ?: throw InvalidInputException("No matching IDFs for  $country")

    }

    fun getIdfItemsData(idfNumber: String): MutableList<IdfItemsResponse> {

        idfsRepository.findByIdfNumber(idfNumber)
            ?.let { parentIdf ->
                val idfItems = mutableListOf<IdfItemsResponse>()
                idfItemsRepository.findByIdfIdAndStatus(parentIdf.id, 0)
                    .let { idfs ->
                        idfs.forEach { idf ->
                            val item = IdfItemsResponse()
                            item.idfNumber = idfNumber
                            item.itemDescription = idf.itemDescription
                            item.hsCode = idf.hsCode
                            item.unitOfMeasure = idf.unitOfMeasure
                            item.quantity = idf.quantity
                            item.newUsed = idf.newUsed
                            item.applicableStandard = idf.applicableStandard
                            item.itemCost = idf.itemCost
                            idfItems.add(item)
                        }
                        return idfItems
                    }
            }
            ?: throw InvalidInputException("No matching IDFs for  $idfNumber")
    }

    fun getInvoiceData(invoiceDate: Date, partnerRef: String): List<PvocInvoicingResponse> {
        pvocPartnersRepository.findByPartnerRefNoAndStatus(partnerRef, 1)
            ?.let { partner ->
                pvocInvoicing.findByInvoiceDateAndPartner(invoiceDate, partner.id)
                    .let { invoices ->
                        val lists = mutableListOf<PvocInvoicingResponse>()
                        invoices.forEach { invoice ->
                            val pvocInvoicingResponse = PvocInvoicingResponse()
                            pvocInvoicingResponse.invoiceNumber = invoice.invoiceNumber
                            pvocInvoicingResponse.soldTo = invoice.soldTo
                            pvocInvoicingResponse.invoiceDate = invoice.invoiceDate
                            pvocInvoicingResponse.orderDate = invoice.orderDate
                            pvocInvoicingResponse.orderNumber = invoice.orderNumber
                            pvocInvoicingResponse.customerNumber = invoice.customerNumber
                            pvocInvoicingResponse.poNumber = invoice.poNumber
                            pvocInvoicingResponse.shipVia = invoice.shipVia
                            pvocInvoicingResponse.termsCode = invoice.termsCode
                            pvocInvoicingResponse.description = invoice.description
                            pvocInvoicingResponse.dueDate = invoice.dueDate
                            pvocInvoicingResponse.discountDate = invoice.discountDate
                            pvocInvoicingResponse.amountDue = invoice.amountDue
                            pvocInvoicingResponse.discountAmount = invoice.discountAmount
                            pvocInvoicingResponse.unitPrice = invoice.unitPrice
                            pvocInvoicingResponse.unitOfMeasure = invoice.unitOfMeasure
                            pvocInvoicingResponse.currency = invoice.currency
                            pvocInvoicingResponse.subtotalBeforeTaxes = invoice.subtotalBeforeTaxes
                            pvocInvoicingResponse.totalTaxes = invoice.totalTaxes
                            pvocInvoicingResponse.totalAmount = invoice.totalAmount
                            pvocInvoicingResponse.accountName = invoice.accountName
                            pvocInvoicingResponse.bankName = invoice.bankName
                            pvocInvoicingResponse.branch = invoice.branch
                            pvocInvoicingResponse.kebsAccountNumber = invoice.kebsAccountNumber
                            pvocInvoicingResponse.usdAccountNumber = invoice.usdAccountNumber
                            pvocInvoicingResponse.bankCode = invoice.bankCode
                            pvocInvoicingResponse.swiftCode = invoice.swiftCode
                            pvocInvoicingResponse.vatNumber = invoice.vatNumber
                            pvocInvoicingResponse.pinNumber = invoice.pinNumber
                            lists.add(pvocInvoicingResponse)
                        }
                        return lists
                    }
            }
            ?: throw InvalidInputException("No matching reference numbers for  $partnerRef")
    }

    fun getGoodsRfcData(partnerRef: String, rfcDate: Date): List<RfcGoodsResponse> {
        pvocPartnersRepository.findByPartnerRefNoAndStatus(partnerRef, 1)
            ?.let { partner ->
                val lists = mutableListOf<RfcGoodsResponse>()
                rfcGoodsRepository.findByRfcDateAndPartner(rfcDate, partner.id)
                    .let { rfcs ->
                        rfcs.forEach { rfc ->
                            val rfcGoodsResponse = RfcGoodsResponse()
                            rfcGoodsResponse.rfcNumber = rfc.rfcNumber
                            rfcGoodsResponse.idfNumber = rfc.idfNumber
                            rfcGoodsResponse.ucrNumber = rfc.ucrNumber
                            rfcGoodsResponse.rfcDate = rfc.rfcDate
                            rfcGoodsResponse.countryOfDestination = rfc.countryOfDestination
                            rfcGoodsResponse.applicationType = rfc.applicationType
                            rfcGoodsResponse.sorReference = rfc.sorReference
                            rfcGoodsResponse.solReference = rfc.solReference
                            rfcGoodsResponse.importerName = rfc.importerName
                            rfcGoodsResponse.importerPin = rfc.importerPin
                            rfcGoodsResponse.importerAddress1 = rfc.importerAddress1
                            rfcGoodsResponse.importerAddress2 = rfc.importerAddress2
                            rfcGoodsResponse.importerCity = rfc.importerCity
                            rfcGoodsResponse.importerCountry = rfc.importerCountry
                            rfcGoodsResponse.importerZipcode = rfc.importerZipcode
                            rfcGoodsResponse.importerTelephoneNumber = rfc.importerTelephoneNumber
                            rfcGoodsResponse.importerFaxNumber = rfc.importerFaxNumber
                            rfcGoodsResponse.importerEmail = rfc.importerEmail
                            rfcGoodsResponse.exporterName = rfc.exporterName
                            rfcGoodsResponse.exporterPin = rfc.exporterPin
                            rfcGoodsResponse.exporterAddress1 = rfc.exporterAddress1
                            rfcGoodsResponse.exporterAddress2 = rfc.exporterAddress2
                            rfcGoodsResponse.exporterCity = rfc.exporterCity
                            rfcGoodsResponse.exporterCountry = rfc.exporterCountry
                            rfcGoodsResponse.exporterZipcode = rfc.exporterZipcode
                            rfcGoodsResponse.exporterTelephoneNumber = rfc.exporterTelephoneNumber
                            rfcGoodsResponse.exporterFaxNumber = rfc.exporterFaxNumber
                            rfcGoodsResponse.exporterEmail = rfc.exporterEmail
                            rfcGoodsResponse.thirdPartyName = rfc.thirdPartyName
                            rfcGoodsResponse.thirdPartyPin = rfc.thirdPartyPin
                            rfcGoodsResponse.thirdPartyAddress1 = rfc.thirdPartyAddress1
                            rfcGoodsResponse.thirdPartyAddress2 = rfc.thirdPartyAddress2
                            rfcGoodsResponse.thirdPartyCity = rfc.thirdPartyCity
                            rfcGoodsResponse.thirdPartyCountry = rfc.thirdPartyCountry
                            rfcGoodsResponse.thirdPartyZipcode = rfc.thirdPartyZipcode
                            rfcGoodsResponse.thirdPartyTelephoneNumber = rfc.thirdPartyTelephoneNumber
                            rfcGoodsResponse.thirdPartyEmail = rfc.thirdPartyEmail
                            rfcGoodsResponse.applicantName = rfc.applicantName
                            rfcGoodsResponse.applicantPin = rfc.applicantPin
                            rfcGoodsResponse.applicantAddress1 = rfc.applicantAddress1
                            rfcGoodsResponse.applicantAddress2 = rfc.applicantAddress2
                            rfcGoodsResponse.applicantCity = rfc.applicantCity
                            rfcGoodsResponse.applicantCountry = rfc.applicantCountry
                            rfcGoodsResponse.applicantZipcode = rfc.applicantZipcode
                            rfcGoodsResponse.applicantTelephoneNumber = rfc.applicantTelephoneNumber
                            rfcGoodsResponse.applicantFaxNumber = rfc.applicantFaxNumber
                            rfcGoodsResponse.applicantEmail = rfc.applicantEmail
                            rfcGoodsResponse.placeOfInspection = rfc.placeOfInspection
                            rfcGoodsResponse.placeOfInspectionAddress = rfc.placeOfInspectionAddress
                            rfcGoodsResponse.placeOfInspectionContacts = rfc.placeOfInspectionContacts
                            rfcGoodsResponse.placeOfInspectionEmail = rfc.placeOfInspectionEmail
                            rfcGoodsResponse.portOfDischarge = rfc.portOfDischarge
                            rfcGoodsResponse.portOfLoading = rfc.portOfLoading
                            rfcGoodsResponse.shipmentMethod = rfc.shipmentMethod
                            rfcGoodsResponse.countryOfSupply = rfc.countryOfSupply
                            rfcGoodsResponse.route = rfc.route
                            rfcGoodsResponse.goodsCondition = rfc.goodsCondition
                            rfcGoodsResponse.assemblyState = rfc.assemblyState
                            rfcGoodsResponse.linkToAttachedDocuments = rfc.linkToAttachedDocuments
                            rfcGoodsResponse.partner = partner.partnerName
                            rfcGoodsResponse.thirdPartyFaxNumber = rfc.thirdPartyFaxNumber
                            lists.add(rfcGoodsResponse)
                        }

                    }
                return lists.distinctBy { l -> l.rfcNumber }

            }
            ?: throw NullValueNotAllowedException("Invalid PartnerRef=${partnerRef} provided ")



    }

    fun getCoiRfcData(partnerRef: String, rfcDate: Date, includeItems: Boolean = false): List<RfcCoiResponse> {
        pvocPartnersRepository.findByPartnerRefNoAndStatus(partnerRef, 1)
            ?.let { partner ->
                val lists = mutableListOf<RfcCoiResponse>()
                rfcCoiRepository.findByRfcDateAndPartner(rfcDate, partner.id)
                    .let { rfcs ->
                        rfcs.forEach { rfc ->
                            val rfcCoiResponse = RfcCoiResponse()
                            rfcCoiResponse.rfcNumber = rfc.rfcNumber
                            rfcCoiResponse.idfNumber = rfc.idfNumber
                            rfcCoiResponse.ucrNumber = rfc.ucrNumber
                            rfcCoiResponse.rfcDate = rfc.rfcDate
                            rfcCoiResponse.countryOfDestination = rfc.countryOfDestination
                            rfcCoiResponse.applicationType = rfc.applicationType
                            rfcCoiResponse.sorReference = rfc.sorReference
                            rfcCoiResponse.solReference = rfc.solReference
                            rfcCoiResponse.importerName = rfc.importerName
                            rfcCoiResponse.importerPin = rfc.importerPin
                            rfcCoiResponse.importerAddress1 = rfc.importerAddress1
                            rfcCoiResponse.importerAddress2 = rfc.importerAddress2
                            rfcCoiResponse.importerCity = rfc.importerCity
                            rfcCoiResponse.importerCountry = rfc.importerCountry
                            rfcCoiResponse.importerZipcode = rfc.importerZipcode
                            rfcCoiResponse.importerTelephoneNumber = rfc.importerTelephoneNumber
                            rfcCoiResponse.importerFaxNumber = rfc.importerFaxNumber
                            rfcCoiResponse.importerEmail = rfc.importerEmail
                            rfcCoiResponse.exporterName = rfc.exporterName
                            rfcCoiResponse.exporterPin = rfc.exporterPin
                            rfcCoiResponse.exporterAddress1 = rfc.exporterAddress1
                            rfcCoiResponse.exporterAddress2 = rfc.exporterAddress2
                            rfcCoiResponse.exporterCity = rfc.exporterCity
                            rfcCoiResponse.exporterCountry = rfc.exporterCountry
                            rfcCoiResponse.exporterZipcode = rfc.exporterZipcode
                            rfcCoiResponse.exporterTelephoneNumber = rfc.exporterTelephoneNumber
                            rfcCoiResponse.exporterFaxNumber = rfc.exporterFaxNumber
                            rfcCoiResponse.exporterEmail = rfc.exporterEmail
                            rfcCoiResponse.placeOfInspection = rfc.placeOfInspection
                            rfcCoiResponse.placeOfInspectionAddress = rfc.placeOfInspectionAddress
                            rfcCoiResponse.placeOfInspectionContacts = rfc.placeOfInspectionContacts
                            rfcCoiResponse.placeOfInspectionEmail = rfc.placeOfInspectionEmail
                            rfcCoiResponse.portOfDischarge = rfc.portOfDischarge
                            rfcCoiResponse.portOfLoading = rfc.portOfLoading
                            rfcCoiResponse.shipmentMethod = rfc.shipmentMethod
                            rfcCoiResponse.countryOfSupply = rfc.countryOfSupply
                            rfcCoiResponse.route = rfc.route
                            rfcCoiResponse.goodsCondition = rfc.goodsCondition
                            rfcCoiResponse.assemblyState = rfc.assemblyState
                            rfcCoiResponse.linkToAttachedDocuments = rfc.linkToAttachedDocuments
                            rfcCoiResponse.partner = partner.partnerName
                            if (includeItems) {
                                rfcCoiResponse.rfcNumber?.let { rfcCoiResponse.items.addAll(getCoiRfcItemsData(it)) }

                            }
                            lists.add(rfcCoiResponse)
                        }

                        return lists.distinctBy { it.rfcNumber }

                    }

            }
            ?: throw InvalidInputException("No matching reference numbers for  $partnerRef")


    }

    fun getCoiRfcItemsData(rfcNumber: String): List<RfcCoiItemsResponse> {

        rfcCoiRepository.findByRfcNumber(rfcNumber)
            ?.let { rfc ->
                rfcCoiItemsRepository.findByRfcId(rfc.id)
                    .let { rfcCoiItemsList ->
                        val lists = mutableListOf<RfcCoiItemsResponse>()
                        rfcCoiItemsList.forEach { rfcItems ->
                            val items = RfcCoiItemsResponse()
                            items.declaredHsCode = rfcItems.declaredHsCode
                            items.itemQuantity = rfcItems.itemQuantity
                            items.productDescription = rfcItems.productDescription
                            items.ownerPin = rfcItems.ownerPin
                            items.ownerName = rfcItems.ownerName
                            lists.add(items)
                        }
                        return lists
                    }
            }
            ?: throw InvalidValueException("Invalid RFC:  $rfcNumber")


    }

    fun getCorRfcData(partnerRef: String, rfcDate: Date): List<RfcCorResponse> {
        pvocPartnersRepository.findByPartnerRefNoAndStatus(partnerRef, 1)
            ?.let { partner ->
                rfcCorRepository.findByRfcDateAndPartner(rfcDate, partner.id)
                    ?.let { rfcs ->
                        val lists = mutableListOf<RfcCorResponse>()
                        rfcs.forEach { rfc ->
                            val rfcCorResponse = RfcCorResponse()
                            rfcCorResponse.rfcDate = rfc.rfcDate
                            rfcCorResponse.countryOfDestination = rfc.countryOfDestination
                            rfcCorResponse.importerName = rfc.importerName
                            rfcCorResponse.importerPin = rfc.importerPin
                            rfcCorResponse.importerAddress1 = rfc.importerAddress1
                            rfcCorResponse.importerAddress2 = rfc.importerAddress2
                            rfcCorResponse.importerCity = rfc.importerCity
                            rfcCorResponse.importerCountry = rfc.importerCountry
                            rfcCorResponse.importerZipcode = rfc.importerZipcode
                            rfcCorResponse.importerTelephoneNumber = rfc.importerTelephoneNumber
                            rfcCorResponse.importerFaxNumber = rfc.importerFaxNumber
                            rfcCorResponse.importerEmail = rfc.importerEmail
                            rfcCorResponse.exporterName = rfc.exporterName
                            rfcCorResponse.exporterPin = rfc.exporterPin
                            rfcCorResponse.exporterAddress1 = rfc.exporterAddress1
                            rfcCorResponse.exporterAddress2 = rfc.exporterAddress2
                            rfcCorResponse.exporterCity = rfc.exporterCity
                            rfcCorResponse.exporterCountry = rfc.exporterCountry
                            rfcCorResponse.exporterZipcode = rfc.exporterZipcode
                            rfcCorResponse.exporterTelephoneNumber = rfc.exporterTelephoneNumber
                            rfcCorResponse.exporterFaxNumber = rfc.exporterFaxNumber
                            rfcCorResponse.exporterEmail = rfc.exporterEmail
                            rfcCorResponse.applicantName = rfc.applicantName
                            rfcCorResponse.applicantPin = rfc.applicantPin
                            rfcCorResponse.applicantAddress1 = rfc.applicantAddress1
                            rfcCorResponse.applicantAddress2 = rfc.applicantAddress2
                            rfcCorResponse.applicantCity = rfc.applicantCity
                            rfcCorResponse.applicantCountry = rfc.applicantCountry
                            rfcCorResponse.applicantZipcode = rfc.applicantZipcode
                            rfcCorResponse.applicantTelephoneNumber = rfc.applicantTelephoneNumber
                            rfcCorResponse.applicantFaxNumber = rfc.applicantFaxNumber
                            rfcCorResponse.applicantEmail = rfc.applicantEmail
                            rfcCorResponse.placeOfInspection = rfc.placeOfInspection
                            rfcCorResponse.placeOfInspectionAddress = rfc.placeOfInspectionAddress
                            rfcCorResponse.placeOfInspectionContacts = rfc.placeOfInspectionContacts
                            rfcCorResponse.placeOfInspectionEmail = rfc.placeOfInspectionEmail
                            rfcCorResponse.portOfDischarge = rfc.portOfDischarge
                            rfcCorResponse.portOfLoading = rfc.portOfLoading
                            rfcCorResponse.shipmentMethod = rfc.shipmentMethod
                            rfcCorResponse.countryOfSupply = rfc.countryOfSupply
                            rfcCorResponse.goodsCondition = rfc.goodsCondition
                            rfcCorResponse.assemblyState = rfc.assemblyState
                            rfcCorResponse.linkToAttachedDocuments = rfc.linkToAttachedDocuments
                            rfcCorResponse.preferredInspectionDate = rfc.preferredInspectionDate
                            rfcCorResponse.make = rfc.make
                            rfcCorResponse.model = rfc.model
                            rfcCorResponse.chassisNumber = rfc.chassisNumber
                            rfcCorResponse.engineCapacity = rfc.engineCapacity
                            rfcCorResponse.engineNumber = rfc.engineNumber
                            rfcCorResponse.yearOfManufacture = rfc.yearOfManufacture
                            rfcCorResponse.yearOfFirstRegistration = rfc.yearOfFirstRegistration
                            rfcCorResponse.partner = partner.partnerName
                            lists.add(rfcCorResponse)

                        }
                        return lists


                    }
                    ?: throw InvalidValueException("No RFC for partner $partnerRef for dates $rfcDate ")

            }
            ?: throw InvalidValueException("Invalid partner $partnerRef")

    }
}
