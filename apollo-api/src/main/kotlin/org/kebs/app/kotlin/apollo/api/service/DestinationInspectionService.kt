package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.dto.MinistryInspectionListResponseDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CocsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.DiUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.ICocItemsRepository
import org.kebs.app.kotlin.apollo.store.repo.ICocsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IConsignmentDocumentTypesEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.sql.Timestamp
import java.time.Instant

@Service
class DestinationInspectionService(
        private val applicationMapProperties: ApplicationMapProperties,
        private val riskProfileDaoService: RiskProfileDaoService,
        private val commonDaoServices: CommonDaoServices,
        private val service: DaoService,
        private val cocsRepository: ICocsRepository,
        private val cocItemsRepository: ICocItemsRepository,
        private val daoServices: DestinationInspectionDaoServices,
        private val cdTypesRepo: IConsignmentDocumentTypesEntityRepository,
        private val importerDaoServices: ImporterDaoServices,
        private val cdAuditService: ConsignmentDocumentAuditService,
        private val qaDaoServices: QADaoServices,
        private val diBpmn: DestinationInspectionBpmn,
) {
    @Value("\${destination.inspection.cd.type.cor}")
    lateinit var corCdType: String
    fun applicationConfigurations(appId: Int): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.commonDaoServices.serviceMapDetails(appId)
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun loadCDTypes(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.cdTypesRepo.findByStatus(1)?.let { ConsignmentDocumentTypesEntityDao.fromList(it) }
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveUploadedCsvFileAndSendToKeSWS(
            docFile: MultipartFile,
            upLoads: DiUploadsEntity,
            loggedInUser: UsersEntity,
            map: ServiceMapsEntity
    ) {
        if (docFile.contentType != "text/csv") {
            throw InvalidValueException("Incorrect file type received, try again later")
        }

        daoServices.saveUploads(
                upLoads,
                docFile,
                "File upload",
                loggedInUser,
                map,
                null,
                null
        )
        val file = File(docFile.originalFilename ?: "UploadedFile")
        file.createNewFile()
        val fos = FileOutputStream(file)
        fos.use {
            it.write(docFile.bytes)
            it.close()
        }

        val cocs = service.readCocFileFromController(',', FileReader(file))
        val uniqueCoc = cocs.map { it.cocNumber }.distinct()
        uniqueCoc.forEach {
            it
                    ?.let { u ->
                        val coc = cocs.firstOrNull { it.cocNumber == u }
                        cocsRepository.findByUcrNumber(
                                coc?.ucrNumber
                                        ?: throw InvalidValueException("Record with empty UCR Number not allowed")
                        )
                                ?.let {
                                    throw InvalidValueException("CoC with UCR number already exists")
                                }
                                ?: run {
                                    var entity = CocsEntity().apply {
                                        cocNumber = coc.cocNumber
                                        idfNumber = coc.idfNumber
                                        rfiNumber = coc.rfiNumber
                                        ucrNumber = coc.ucrNumber
                                        rfcDate = coc.rfcDate
                                        cocIssueDate = coc.cocIssueDate
                                        clean = coc.clean
                                        cocRemarks = coc.cocRemarks
                                        issuingOffice = coc.issuingOffice
                                        importerName = coc.importerName
                                        importerPin = coc.importerPin
                                        importerAddress1 = coc.importerAddress1
                                        importerAddress2 = coc.importerAddress2
                                        importerCity = coc.importerCity
                                        importerCountry = coc.importerCountry
                                        importerZipCode = coc.importerZipCode
                                        importerTelephoneNumber = coc.importerTelephoneNumber
                                        importerFaxNumber = coc.importerFaxNumber
                                        importerEmail = coc.importerEmail
                                        exporterName = coc.exporterName ?: "UNDEFINED"
                                        exporterPin = coc.exporterPin ?: "UNDEFINED"
                                        exporterAddress1 = coc.exporterAddress1 ?: "UNDEFINED"
                                        exporterAddress2 = coc.exporterAddress2 ?: "UNDEFINED"
                                        exporterCity = coc.exporterCity ?: "UNDEFINED"
                                        exporterCountry = coc.exporterCountry ?: "UNDEFINED"
                                        exporterZipCode = coc.exporterZipCode ?: "UNDEFINED"
                                        exporterTelephoneNumber = coc.exporterTelephoneNumber ?: "UNDEFINED"
                                        exporterFaxNumber = coc.exporterFaxNumber ?: "UNDEFINED"
                                        exporterEmail = coc.exporterEmail ?: "UNDEFINED"
                                        placeOfInspection = coc.placeOfInspection ?: "UNDEFINED"
                                        dateOfInspection =
                                                coc.dateOfInspection ?: Timestamp.from(Instant.now())
                                        portOfDestination = coc.portOfDestination ?: "UNDEFINED"
                                        shipmentMode = coc.shipmentMode ?: "UNDEFINED"
                                        countryOfSupply = coc.countryOfSupply ?: "UNDEFINED"
                                        finalInvoiceFobValue = coc.finalInvoiceFobValue
                                        finalInvoiceExchangeRate = coc.finalInvoiceExchangeRate
                                        finalInvoiceCurrency = coc.finalInvoiceCurrency ?: "UNDEFINED"
                                        finalInvoiceDate =
                                                coc.finalInvoiceDate ?: Timestamp.from(Instant.now())
                                        shipmentPartialNumber = coc.shipmentPartialNumber
                                        shipmentSealNumbers = coc.shipmentSealNumbers ?: "UNDEFINED"
                                        route = coc.route ?: "UNDEFINED"
                                        productCategory = coc.productCategory ?: "UNDEFINED"
                                        productCategory = coc.productCategory ?: "UNDEFINED"
                                        status = 1L
                                        createdBy = loggedInUser.userName
                                        createdOn = Timestamp.from(Instant.now())
                                        partner = loggedInUser.userName
                                        pvocPartner = loggedInUser.id


                                    }
                                    entity = cocsRepository.save(entity)
                                    cocs.filter { dto -> dto.cocNumber == u }.forEach { cocItems ->
                                        val itemEntity = CocItemsEntity().apply {
                                            cocId = entity.id
                                            shipmentLineNumber = cocItems.shipmentLineNumber
                                            shipmentLineHscode = cocItems.shipmentLineHscode ?: "UNDEFINED"
                                            shipmentLineQuantity = cocItems.shipmentLineQuantity
                                            shipmentLineUnitofMeasure = cocItems.shipmentLineUnitofMeasure
                                                    ?: "UNDEFINED"
                                            shipmentLineDescription = cocItems.shipmentLineDescription ?: "UNDEFINED"
                                            shipmentLineVin = cocItems.shipmentLineVin ?: "UNDEFINED"
                                            shipmentLineStickerNumber = cocItems.shipmentLineStickerNumber
                                                    ?: "UNDEFINED"
                                            shipmentLineIcs = cocItems.shipmentLineIcs ?: "UNDEFINED"
                                            shipmentLineStandardsReference =
                                                    cocItems.shipmentLineStandardsReference ?: "UNDEFINED"
                                            shipmentLineRegistration = cocItems.shipmentLineRegistration ?: "UNDEFINED"
                                            shipmentLineRegistration = cocItems.shipmentLineRegistration ?: "UNDEFINED"
                                            status = 1
                                            createdBy = loggedInUser.userName
                                            createdOn = Timestamp.from(Instant.now())
                                            cocNumber = entity.cocNumber
                                            shipmentLineBrandName = "UNDEFINED"


                                        }
                                        cocItemsRepository.save(itemEntity)

                                        service.submitCocToKeSWS(entity)

                                    }


                                }

                    }
                    ?: KotlinLogging.logger { }.info("Empty value")
        }
    }

    fun ministryInspectionList(inspectionChecklistId: Long, comment: String?, docFile: MultipartFile): ApiResponseModel {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        commonDaoServices.loggedInUserDetails()
                .let { loggedInUser ->
                    KotlinLogging.logger { }.info { "mvInspectionChecklistId = $inspectionChecklistId" }
                    daoServices.findInspectionMotorVehicleById(inspectionChecklistId)
                            ?.let { inspectionMotorVehicle ->
                                inspectionMotorVehicle.ministryReportFile = docFile.bytes
                                inspectionMotorVehicle.ministryReportReinspectionRemarks = comment
                                inspectionMotorVehicle.ministryReportSubmitStatus = map.activeStatus
                                daoServices.updateCdInspectionMotorVehicleItemChecklistInDB(
                                        inspectionMotorVehicle,
                                        loggedInUser
                                ).let { cdInspectionMVChecklist ->
                                    cdInspectionMVChecklist.inspectionGeneral?.cdItemDetails?.let { cdItemDetails ->
                                        cdItemDetails.cdDocId?.let { cdEntity ->
                                            //Update status
                                            cdEntity.cdStandard?.let { cdStd ->
                                                daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeMinistryInspectionUploadedId)
                                            }
                                            //daoServices.sendMinistryInspectionReportSubmittedEmail(it, cdItemDetails)
                                            this.cdAuditService.addHistoryRecord(cdEntity.id, comment, "MINISTRY", "Ministry Inspection Report")
                                            //Complete Generate Ministry Inspection Report & Assign Review Ministry Inspection Report
                                            cdEntity.id?.let {
                                                cdEntity.assignedInspectionOfficer?.id?.let { it1 ->
                                                    diBpmn.diGenerateMinistryInspectionReportComplete(
                                                            it, it1
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                response.message = "Report Submitted Successfully"
                                response.responseCode = ResponseCodes.SUCCESS_CODE


                            } ?: run {
                        response.message = "No Motor Vehicle Inspection Checklist Found"
                        response.responseCode = ResponseCodes.NOT_FOUND
                    }
                }
        return response
    }

    fun applicationTypes(): ApiResponseModel {
        val response = ApiResponseModel()
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        response.data = this.importerDaoServices.findDiApplicationTypes(1)
        return response
    }

    fun listMinistryInspection(completed: Boolean, page: PageRequest): ApiResponseModel {
        val requests: Page<CdItemDetailsEntity>
        val ministryInspectionItems: MutableList<MinistryInspectionListResponseDto> = ArrayList()
        val response = ApiResponseModel()
        if (completed) {
            requests = daoServices.findAllCompleteMinistryInspectionRequests(page)
        } else {
            requests = daoServices.findAllOngoingMinistryInspectionRequests(page)

        }
        requests.toList().forEach {
            ministryInspectionItems.add(this.daoServices.convertCdItemDetailsToMinistryInspectionListResponseDto(it))
        }
        response.pageNo = requests.number
        response.totalPages = requests.totalPages
        response.data = ministryInspectionItems
        response.extras = daoServices.motorVehicleMinistryInspectionChecklistName
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"

        return response
    }

    fun consignmentDocumentDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
//            KotlinLogging.logger { }.info(ObjectMapper().writeValueAsString(cdDetails))
            response.data = loadCDDetails(cdDetails, map)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Consignment Document"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.data = null
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Record not found"
        }
        return response
    }

    fun consignmentDocumentAttachments(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            response.data = daoServices.findAllAttachmentsByCd(cdDetails)?.let { DiUploadsEntityDao.fromList(it) }
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Consignment Document"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.data = null
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Record not found"
        }
        return response
    }

    fun consignmentDocumentItemDetails(cdItemUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val dataMap = mutableMapOf<String, Any?>()
            val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
            dataMap.put("cd_type", cdItemDetails.cdDocId?.cdType?.let { ConsignmentDocumentTypesEntityDao.fromEntity(it) })
            dataMap.put("item_non_standard", daoServices.findCdItemNonStandardByItemID(cdItemDetails)?.let { CdItemNonStandardEntityDto.fromEntity(it) })
            dataMap.put("cd_item",CdItemDetailsDao.fromEntity(cdItemDetails))
            // Check Certificate of Roadworthiness(CoR)
            cdItemDetails.cdDocId?.let { itemType ->
                if (itemType.equals(corCdType)) {
                    itemType.docTypeId?.let {
                        dataMap.put("cor", daoServices.findCORById(it))
                    }
                }
                dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(itemType))
                // Add vehicle inspection result
                daoServices.findMotorVehicleInspectionByCdItem(cdItemDetails)?.let {
                    KotlinLogging.logger { }.info { "Motor vehicle inspection returned = ${it.id}" }
                    dataMap.put("vehicle_inspection_checklist", CdInspectionMotorVehicleItemChecklistDao.fromEntity(it))
                }
            }

            response.data = dataMap
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = "Record not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun consignmentDocumentInvoiceDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val itemListWithDemandNote = daoServices.findCDItemsListWithCDIDAndDemandNoteStatus(cdDetails, map)
            response.data = itemListWithDemandNote
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
            response.message = "Failed"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    private fun loadCDDetails(cdDetails: ConsignmentDocumentDetailsEntity,
                              map: ServiceMapsEntity): MutableMap<String, Any?> {
        val dataMap = mutableMapOf<String, Any?>()
        // Importer details
        try {
            if (cdDetails.cdImporter != null) {
                val cdImporter = daoServices.findCDImporterDetails(cdDetails.cdImporter!!)
                var riskProfileImporter = false
                cdImporter.pin?.let {
                    riskProfileDaoService.findImportersInRiskProfile(it, map.activeStatus).let { riskImporter ->
                        when {
                            riskImporter != null -> {
                                riskProfileImporter = true
                            }
                        }
                    }
                    dataMap.put("cd_importer", cdImporter)
                    dataMap.put("risk_profile_importer", riskProfileImporter)
                }
            } else {
                dataMap.put("risk_profile_importer", false)
                dataMap.put("cd_importer", null)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            dataMap.put("risk_profile_importer", false)
            dataMap.put("cd_importer", null)
        }
        // Consignee details
        try {
            if (cdDetails.cdConsignee != null) {
                val cdConsignee = cdDetails.cdConsignee?.let { daoServices.findCdConsigneeDetails(it) }
                var riskProfileConsignee = false
                cdConsignee?.pin?.let {
                    riskProfileDaoService.findConsigneeInRiskProfile(it, map.activeStatus).let { riskConsignee ->
                        when {
                            riskConsignee != null -> {
                                riskProfileConsignee = true
                            }
                        }
                    }
                    dataMap.put("cd_consignee", cdConsignee)
                    dataMap.put("risk_profile_consignee", riskProfileConsignee)
                }
            } else {
                dataMap.put("cd_consignee", null)
                dataMap.put("risk_profile_consignee", false)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
        }
        // Exporter details
        try {
            cdDetails.cdExporter?.let { cdExporterID ->
                val cdExporter = daoServices.findCdExporterDetails(cdExporterID)
                var riskProfileExporter = false
                cdExporter.pin?.let {
                    riskProfileDaoService.findExporterInRiskProfile(it, map.activeStatus).let { riskExporter ->
                        when {
                            riskExporter != null -> {
                                riskProfileExporter = true
                            }
                        }
                    }
                }
                dataMap.put("cd_exporter", cdExporter)
                dataMap.put("risk_profile_exporter", riskProfileExporter)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            dataMap.put("cd_exporter", null)
            dataMap.put("risk_profile_exporter", false)
        }
        // Consignor ID
        try {
            cdDetails.cdConsignor?.let { consignorId ->
                val cdConsignor = daoServices.findCdConsignorDetails(consignorId)
                var riskProfileConsignor = false
                cdConsignor.pin?.let {
                    riskProfileDaoService.findConsignorInRiskProfile(it, map.activeStatus).let { riskConsignor ->
                        when {
                            riskConsignor != null -> {
                                riskProfileConsignor = true
                            }
                        }
                    }
                }
                dataMap.put("cd_consignor", cdConsignor)
                dataMap.put("risk_profile_consignor", riskProfileConsignor)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            dataMap.put("cd_consignor", null)
            dataMap.put("risk_profile_consignor", false)
        }
        // CdType details
        cdDetails.cdType?.let {
            dataMap.put("cd_type", it)
        }
        // Standard
        cdDetails.cdStandardsTwo?.let {
            dataMap.put("cd_standards_two", it)
        }
        // Transporter
        cdDetails.cdTransport?.let {
            dataMap.put("cd_transport", daoServices.findCdTransportDetails(it))
        }
        // Headers
        cdDetails.cdHeaderOne?.let {
            dataMap.put("cd_header_one", daoServices.findCdHeaderOneDetails(it))
        }
        cdDetails.cdStandard?.cdServiceProvider?.let {
            dataMap.put("cd_service_provider", it)
        }
        cdDetails.ucrNumber?.let {
            dataMap.put("old_versions", daoServices.findAllOlderVersionCDsWithSameUcrNumber(it, map))
        }
        dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(cdDetails))
        dataMap.put("items_cd", CdItemDetailsDao.fromList(daoServices.findCDItemsListWithCDID(cdDetails)))
        // Consignment UI controllers
        try {
            dataMap.put("ui", ConsignmentEnableUI.fromEntity(cdDetails, map, commonDaoServices.loggedInUserAuthentication()))
        }catch (ex: Exception){
            KotlinLogging.logger { }.error { ex }
        }
        return dataMap
    }

    fun certificateOfConformanceDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val dataMap = mutableMapOf<String, Any?>()
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val cocDetails = cdDetails.ucrNumber?.let { daoServices.findCOC(it) }
            dataMap.put("configuration", map)
            dataMap.put("certificate_details", cocDetails)
            dataMap.put("consignment_document_details", cdDetails)
            dataMap.put("item_certificate_of_conformance", cocDetails?.id?.let { daoServices.findCocItemList(it) })
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CoC not found"
        }
        return response
    }

    fun importDeclarationFormDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val idfDetails = cdDetails.ucrNumber?.let { daoServices.findIdf(it) }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("idf_details", idfDetails)
            dataMap.put("cd_standard", cdDetails.cdStandard?.let { CdStandardsEntityDao.fromEntity(it) })
            dataMap.put("consignment_details", ConsignmentDocumentDao.fromEntity(cdDetails))
            dataMap.put("item_idf", idfDetails?.let { daoServices.findIdfItemList(it) })
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "IDF not found"
        }
        return response
    }

    fun certificateOfRoadWorthinessDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val cdItem = daoServices.findCDItemsListWithCDID(cdDetails)[0]
            val itemNonStandardDetail = daoServices.findCdItemNonStandardByItemID(cdItem)
            val corDetails = itemNonStandardDetail?.chassisNo?.let { daoServices.findCORByChassisNumber(it) }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("configuration", map)
            dataMap.put("cor_details", corDetails)
            dataMap.put("cd_details", cdDetails)
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CoR not found"
        }
        return response
    }

    fun consignmentDocumentManifestDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val declarationDetails = cdDetails.ucrNumber?.let { daoServices.findDeclaration(it) }
            val manifestDetails = declarationDetails?.billCode?.let { daoServices.findManifest(it) }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("manifest_details", manifestDetails)
            dataMap.put("cd_details", cdDetails)
            dataMap.put("configuration", map)
            response.data = dataMap
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CD manifests not found"
        }
        return response
    }

    fun cdCustomDeclarationDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val declarationDetails = cdDetails.ucrNumber?.let { daoServices.findDeclaration(it) }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("declaration_details", declarationDetails)
            dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(cdDetails))
            dataMap.put("cd_standard", cdDetails.cdStandard)
            dataMap.put("item_declaration", declarationDetails?.let { daoServices.findDeclarationItemList(it) })
            dataMap.put("configuration", map)
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CD manifests not found"
        }
        return response
    }

    fun inspectionChecklistReportDetails(cdItemUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
            val generalChecklist = daoServices.findInspectionGeneralWithItemDetails(cdItemDetails)
            var inspectionChecklist: Any? = null
            //Get checklist type details
            when (generalChecklist.checkListType?.uuid) {
                daoServices.agrochemItemChecklistType -> {
                    inspectionChecklist = daoServices.findInspectionAgrochemWithInspectionGeneral(generalChecklist)
                }
                daoServices.engineeringItemChecklistType -> {
                    inspectionChecklist = daoServices.findInspectionEngineeringWithInspectionGeneral(generalChecklist)
                }
                daoServices.otherItemChecklistType -> {
                    inspectionChecklist = daoServices.findInspectionOthersWithInspectionGeneral(generalChecklist)
                }
            }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("inspection_check_list", inspectionChecklist)
            dataMap.put("configuration", map)
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Checklist not found"
        }
        return response
    }

    fun getSSfDetails(cdItemId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val item = daoServices.findItemWithItemID(cdItemId)
            val ssfDetails = daoServices.findSampleSubmittedBYCdItemID(item.id
                    ?: throw ExpectedDataNotFound("MISSING CD ITEM ID"))
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("ssf_details", ssfDetails)
            dataMap.put("lab_result_parameters", qaDaoServices.findSampleLabTestResultsRepoBYBSNumber(ssfDetails.bsNumber
                    ?: throw ExpectedDataNotFound("MISSING BS NUMBER")))
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Lab results not found"
        }
        return response
    }

    fun motorVehicleInspectionDetails(itemId: Long): ApiResponseModel {
        //Get the CD Item
        val response = ApiResponseModel()
        try {
            val cdItemDetails = daoServices.findItemWithItemID(itemId)
            val dataMap = mutableMapOf<String, Any?>()
            dataMap["item"] = CdItemDetailsDao.fromEntity(cdItemDetails)
            //Get inspection checklist details
            val inspectionGeneralEntity = daoServices.findInspectionGeneralWithItemDetails(cdItemDetails)
            //Get the motor vehicle checklist

            dataMap["inspectionGeneralEntity"] = CdInspectionGeneralDao.fromEntity(inspectionGeneralEntity)
            daoServices.findInspectionMotorVehicleWithInspectionGeneral(inspectionGeneralEntity)
                    ?.let {
                        dataMap["mvInspectionChecklist"] = CdInspectionMotorVehicleItemChecklistDao.fromEntity(it)
                    } ?: run {
                dataMap["mvInspectionChecklist"] = null
            }

            //Check for flash attributes
            response.data = dataMap
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request failed, please try again"
        }
        return response
    }

}
