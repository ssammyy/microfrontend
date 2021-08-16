package org.kebs.app.kotlin.apollo.api.service;

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.dto.MinistryInspectionListResponseDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionGeneralEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.IConsignmentDocumentTypesEntityRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Service
class DestinationInspectionService(
        private val applicationMapProperties: ApplicationMapProperties,
        private val riskProfileDaoService: RiskProfileDaoService,
        private val commonDaoServices: CommonDaoServices,
        private val daoServices: DestinationInspectionDaoServices,
        private val cdTypesRepo: IConsignmentDocumentTypesEntityRepository,
        private val importerDaoServices: ImporterDaoServices,
        private val qaDaoServices: QADaoServices,
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
        response.data = this.cdTypesRepo.findByStatus(1)
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun applicationTypes(): ApiResponseModel {
        val response = ApiResponseModel()
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        response.data = this.importerDaoServices.findDiApplicationTypes(1)
        return response
    }

    fun listMinistryInspection(completed: Boolean): ApiResponseModel {
        val requests: List<CdItemDetailsEntity>
        val ministryInspectionItems: MutableList<MinistryInspectionListResponseDto> = ArrayList()
        val response = ApiResponseModel()
        if (completed) {
            requests = daoServices.findAllCompleteMinistryInspectionRequests()
        } else {
            requests = daoServices.findAllOngoingMinistryInspectionRequests()

        }
        requests.forEach {
            ministryInspectionItems.add(this.daoServices.convertCdItemDetailsToMinistryInspectionListResponseDto(it))
        }
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
            response.data = loadCDDetails(cdDetails, map)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Consignment Document"
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Not valid"
        }
        return response
    }

    fun consignmentDocumentItemDetails(cdItemUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val dataMap = mutableMapOf<String, Any?>()
            val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
            dataMap.put("cd_type", cdItemDetails.cdDocId)
            dataMap.put("item_non_standard", daoServices.findCdItemNonStandardByItemID(cdItemDetails))
            dataMap.put("demand_notes", daoServices.findDemandNote(cdItemDetails))
            // Check Certificate of Roadworthiness(CoR)
            cdItemDetails.cdDocId?.let { itemType ->
                if (itemType.equals(corCdType)) {
                    itemType.docTypeId?.let {
                        dataMap.put("cor", daoServices.findCORById(it))
                    }
                }
                // Add vehicle inspection result
                daoServices.findMotorVehicleInspectionByCdItem(cdItemDetails)?.let {
                    KotlinLogging.logger { }.info { "Motor vehicle inspection returned = ${it.id}" }
                    dataMap.put("vehicle_inspection_checklist", it)
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
            response.message = "Failed"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    private fun loadCDDetails(cdDetails: ConsignmentDocumentDetailsEntity,
                              map: ServiceMapsEntity): MutableMap<String, Any?> {
        val dataMap = mutableMapOf<String, Any?>()
        // Importer details
        cdDetails.cdImporter?.let { cdImporterID ->
            val cdImporter = daoServices.findCDImporterDetails(cdImporterID)
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
        } ?: run {
            dataMap.put("risk_profile_importer", false)
            dataMap.put("cd_importer", null)
        }
        // Consignee details
        cdDetails.cdConsignee?.let { cdConsigneeID ->
            val cdConsignee = daoServices.findCdConsigneeDetails(cdConsigneeID)
            var riskProfileConsignee = false
            cdConsignee.pin?.let {
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
        } ?: run {
            dataMap.put("cd_consignee", null)
            dataMap.put("risk_profile_consignee", false)
        }
        // Importer details
        cdDetails.cdImporter?.let { cdExporterID ->
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
        } ?: run {
            dataMap.put("cd_exporter", null)
            dataMap.put("risk_profile_exporter", false)
        }

        // Consignor ID
        cdDetails.cdConsignor?.let { cdConsignorID ->
            val cdConsignor = daoServices.findCdConsignorDetails(cdConsignorID)
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
        } ?: run {
            dataMap.put("cd_consignor", null)
            dataMap.put("risk_profile_consignor", false)
        }
        // CdType details
        cdDetails.cdType?.let {
            dataMap.put("cd_type", daoServices.findCdTypeDetails(it))
        } ?: run {
            dataMap.put("cd_type", null)
        }
        // Standard
        cdDetails.cdStandardsTwo?.let {
            dataMap.put("cd_standards_two", daoServices.findCdStandardsTWODetails(it))
        } ?: run {
            dataMap.put("cd_standards_two", null)
        }
        // Transporter
        cdDetails.cdTransport?.let {
            dataMap.put("cd_transport", daoServices.findCdTransportDetails(it))
        } ?: run {
            dataMap.put("cd_transport", null)
        }
        // Headers
        cdDetails.cdHeaderOne?.let {
            dataMap.put("cd_header_one", daoServices.findCdHeaderOneDetails(it))
        } ?: run {
            dataMap.put("cd_header_one", null)
        }
        dataMap.put("cd_details", cdDetails)
        dataMap.put("items_cd", daoServices.findCDItemsListWithCDID(cdDetails))

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
            dataMap.put("configuration", map)
            dataMap.put("idf_details", idfDetails)
            dataMap.put("consignment_details", cdDetails)
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
            dataMap.put("cd_details", cdDetails)
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
}
