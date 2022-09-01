package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.payload.response.PvocPartnerQueryDao
import org.kebs.app.kotlin.apollo.api.payload.response.PvocPartnerTimelinesDataDto
import org.kebs.app.kotlin.apollo.api.payload.response.RiskProfileDao
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ForeignPvocIntegrations
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocQueriesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocQueryResponseEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocQuerriesRepository
import org.kebs.app.kotlin.apollo.store.repo.IPvocQueryResponseRepository
import org.kebs.app.kotlin.apollo.store.repo.IPvocTimelinesDataEntityRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

enum class ComplaintStatus(val code: Int) {
    NEW(0), PVOC_APPROVED(1), PVOC_REJECTED(4)
}

@Service("pvocAgentService")
class PvocAgentService(
        private val commonDaoServices: CommonDaoServices,
        private val timelinesRepository: IPvocTimelinesDataEntityRepository,
        private val partnerService: PvocPartnerService,
        private val apiClientService: ApiClientService,
        private val partnerQuerriesRepository: IPvocQuerriesRepository,
        private val partnerQueryResponseRepository: IPvocQueryResponseRepository,
        private val daoServices: DestinationInspectionDaoServices,
        private val pvocIntegrations: ForeignPvocIntegrations,
        private val properties: ApplicationMapProperties
) {
    val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd")
    val TIMELINE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMM")


    fun raiseComplaintFrom(form: PvocComplaintForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Error ", ex)
        }
        return response
    }



    fun timelineIssues(yearMonth: Optional<String>): ApiResponseModel {
        val partner = commonDaoServices.loggedInPartnerDetails()
        val issues = when {
            yearMonth.isPresent -> this.timelinesRepository.findByRecordYearMonthAndPartnerId(yearMonth.get(), partner.id)
            else -> this.timelinesRepository.findByRecordYearMonthAndPartnerId(TIMELINE_DATE_FORMAT.format(LocalDateTime.now()), partner.id)
        }
        val response = ApiResponseModel()
        response.data = PvocPartnerTimelinesDataDto.fromList(issues)
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE

        return response
    }





    fun receiveCoc(coc: CocEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val actiVerUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignCoc(actiVerUser, coc, commonDaoServices.serviceMapDetails(properties.mapImportInspection))?.let { cocEntity ->
                this.daoServices.sendLocalCoc(cocEntity)
                response.data = coc
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "COC with ucr: " + coc.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "COC with ucr: " + coc.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add COC with ucr number" + coc.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveCor(cor: CorEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activerUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignCor(cor, commonDaoServices.serviceMapDetails(properties.mapImportInspection), activerUser)?.let { corEntity ->
                // Send COR to KWS
                this.daoServices.submitCoRToKesWS(corEntity)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "COR with ucr: " + cor.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "COR with ucr: " + cor.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add COR", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add COC with ucr number" + cor.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveCoi(form: CoiEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activerUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignCoi(activerUser, form, commonDaoServices.serviceMapDetails(properties.mapImportInspection))?.let { corEntity ->
                // Send Foreign COI to KEWS
                this.daoServices.sendLocalCoi(corEntity)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "COI with ucr: " + form.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "CORI with ucr: " + form.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add COI", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add COI with ucr number" + form.ucrNumber
        }
        return response
    }

    fun receiveRfcCoi(form: RfcEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activeUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignRfc(form, "COI", commonDaoServices.serviceMapDetails(properties.mapImportInspection), activeUser)?.let { rfcEntity ->
                response.data = form
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "RFC with number: " + form.rfcNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "RFC with ucr: " + form.ucrNumber + " already exists"
                response
            }
        } catch (ex: ExpectedDataNotFound) {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = ex.localizedMessage
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add RFC data", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add COI with ucr number" + form.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveRfcCoc(form: RfcEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activeUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignRfc(form, "COC", commonDaoServices.serviceMapDetails(properties.mapImportInspection), activeUser)?.let { rfcEntity ->
                response.data = form
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "RFC for COC with number: " + form.rfcNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "RFC for COC with ucr: " + form.ucrNumber + " already exists"
                response
            }
        } catch (ex: ExpectedDataNotFound) {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = ex.localizedMessage
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add RFC data for COC", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add RFC for COC with ucr number" + form.rfcNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveRfcCor(form: RfcCorForm): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = form
        try {
            val activeUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignRfcCor(form, commonDaoServices.serviceMapDetails(properties.mapImportInspection), activeUser)?.let { rfcEntity ->
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "RFC for COR with number: " + form.rfcNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "RFC for COR with ucr: " + form.ucrNumber + " already exists"
                response
            }
        } catch (ex: ExpectedDataNotFound) {

            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = ex.localizedMessage
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add RFC data for COC", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add RFC for COR with ucr number" + form.rfcNumber
            response.errors = ex.message
        }
        return response
    }

    fun receiveNcr(ncr: NcrEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activerUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignNcr(activerUser, ncr, commonDaoServices.serviceMapDetails(properties.mapImportInspection))?.let { ncrEntity ->
                // Create response
                response.data = ncr
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "NCR with ucr: " + ncr.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "NCR with ucr: " + ncr.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add NCR with ucr number" + ncr.ucrNumber
            response.errors = ex.message
        }
        return response
    }

    fun addRiskProfile(form: RiskProfileForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activeUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignRiskProfile(form, commonDaoServices.serviceMapDetails(properties.mapImportInspection), activeUser)?.let { riskEntity ->
                response.data = form
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "RISK for HS Code: " + form.hsCode + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "RISK for HS Code: " + form.hsCode + " already exists"
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add Risk HS code" + form.hsCode
            response.errors = ex.message
        }
        return response
    }

    fun getRiskProfile(clientId: String?, date: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activeUser = commonDaoServices.loggedInUserAuthentication()
            val activeUserId = when {
                StringUtils.hasLength(clientId) -> clientId!!
                else -> activeUser.name
            }
            val result = this.pvocIntegrations.listRiskProfile(activeUserId, date, page)
            when {
                result.isEmpty -> {
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "No Record"
                }
                else -> {
                    response.data = RiskProfileDao.fromList(result.toList())
                    response.pageNo = result.number
                    response.totalCount = result.totalElements
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Success"
                }
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to load risk data"
            response.errors = ex.message
        }
        return response
    }

    fun addIdfData(form: IdfEntityForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val activeUser = commonDaoServices.loggedInPartnerDetails()
            this.pvocIntegrations.foreignIdfData(form, commonDaoServices.serviceMapDetails(properties.mapImportInspection), activeUser)?.let { riskEntity ->
                response.data = form
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "IDF for ucr: " + form.ucrNumber + " received"
                response
            } ?: run {
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "IDF for ucr: " + form.ucrNumber + " already exists"
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add IDF data", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to add IDF for ucr: " + form.ucrNumber
            response.errors = ex.localizedMessage
        }
        return response
    }

    fun certificateExists(documentType: String, certificateNumber: String, partnerId: Long?): Boolean {
        return when (documentType.toUpperCase()) {
            "COC", "COI", "NCR" -> {
                daoServices.findCocByCocNumber(certificateNumber)?.let { coc ->
                    coc.partner == partnerId
                } ?: false
            }
            "COR" -> {
                daoServices.findCORByCorNumber(certificateNumber)?.let { cor ->
                    cor.partner == partnerId
                } ?: false
            }
            else -> false
        }
    }

    fun receivePartnerQuery(form: PvocKebsQueryForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val partner = this.commonDaoServices.loggedInPartnerDetails()
            if (certificateExists(form.documentType.orEmpty().toUpperCase(), form.certNumber.orEmpty(), partner.id)) {
                daoServices.findCdWithUcrNumberLatest(form.ucrNumber.orEmpty())?.let { cd ->
                    val query = PvocQueriesEntity()
                    query.serialNumber = queryReference("PVOC")
                    query.partnerId = partner.id
                    query.varField1 = cd.id.toString()
                    query.certNumber = form.certNumber
                    query.certType = form.documentType?.toUpperCase()
                    query.queryOrigin = "PVOC"
                    query.ucrNumber = form.ucrNumber
                    query.rfcNumber = form.rfcNumber
                    query.idfNumber = form.idfNumber
                    query.invoiceNumber = form.invoiceNumber
                    query.queryDetails = form.partnerQuery
                    query.pvocAgentReplyStatus = 1
                    query.kebsReplyReplyStatus = 0
                    query.conclusionStatus = 0
                    query.status = 1
                    query.varField10 = auth.name
                    query.createdBy = auth.name
                    query.createdOn = Timestamp.from(Instant.now())
                    query.modifiedOn = Timestamp.from(Instant.now())
                    this.partnerQuerriesRepository.save(query)
                    val data = mutableMapOf<String, Any>()
                    data["certNumber"] = form.certNumber ?: "UNKNOWN"
                    data["certType"] = form.documentType ?: "UNKNOWN"
                    data["serialNumber"] = query.serialNumber ?: "NA"
                    response.data = data
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Query received"
                    response
                } ?: run {
                    response.data = form
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "Invalid UCR number, no such consignment"
                    response
                }
            } else {
                response.data = form
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Invalid Cert number, no such certificate"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC query", ex)
            response.data = form
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Request failed, please try again later"
        }
        return response
    }

    private fun queryReference(source: String): String {
        val date = LocalDateTime.now()
        val prefix = DATE_FORMAT.format(date)
        val count = this.partnerQuerriesRepository.countAllBySerialNumberStartsWith(prefix)
        return "$prefix$source${date.minute}%05x".format(count + 1)
    }

    private fun queryResponseReference(source: String): String {
        val date = LocalDateTime.now()
        val prefix = DATE_FORMAT.format(date)
        val count = this.partnerQueryResponseRepository.countAllBySerialNumberStartsWith(prefix)
        return "$prefix$source${date.minute}%05x".format(count + 1)
    }

    fun receivePartnerQueryResponse(form: PvocQueryResponse): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            this.partnerQuerriesRepository.findAllBySerialNumber(form.serialNumber!!)?.let { query ->
                val partner = commonDaoServices.loggedInPartnerDetails()
                if (query.partnerId == partner.id) {
                    // Only receive responses for KEBS originated queries
                    val res = PvocQueryResponseEntity()
                    res.queryId = query
                    res.serialNumber = queryResponseReference("PVOC")
                    res.response = form.queryResponse
                    res.responseFrom = "PVOC"
                    res.linkToUploads = form.linkToUploads
                    res.status = 1
                    res.varField1 = form.queryAnalysis
                    res.createdOn = Timestamp.from(Instant.now())
                    res.createdBy = commonDaoServices.loggedInUserAuthentication().name
                    res.modifiedOn = Timestamp.from(Instant.now())
                    res.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                    // Add and update
                    this.partnerQueryResponseRepository.save(res)
                    query.modifiedOn = Timestamp.from(Instant.now())
                    query.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                    this.partnerQuerriesRepository.save(query)
                    response.data = mapOf(Pair("serialNumber", res.serialNumber))
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Response Received"
                } else {
                    KotlinLogging.logger { }.warn("Received query response from unexpected partner: ${partner.partnerRefNo}-Expected: ${query.partnerId}")
                    response.responseCode = ResponseCodes.INVALID_CODE
                    response.message = "Invalid response received"
                }
            } ?: kotlin.run {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Response Received"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC query", ex)
        }
        return response
    }

    fun receiveKebsQueryConclusion(form: PvocQueryConclusion): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            this.partnerQuerriesRepository.findAllBySerialNumber(form.serialNumber!!)?.let { query ->
                val partner = partnerService.getPartner(query.partnerId!!)
                if (query.partnerId == partner?.id) {
                    val data = KebsQueryResponse()
                    if ("CONCLUSION".equals(form.responseType, true)) {
                        query.conclusion = form.responseData
                        query.conclusionStatus = 1
                        query.responseAnalysis = form.queryAnalysis
                        query.modifiedOn = Timestamp.from(Instant.now())
                        query.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                        this.partnerQuerriesRepository.save(query)
                        data.responseSerialNumber = query.serialNumber
                        data.conclusion = form.responseData.orEmpty()
                    } else {
                        data.queryResponse = form.responseData.orEmpty()
                        val res = PvocQueryResponseEntity()
                        res.queryId = query
                        res.serialNumber = queryResponseReference("KEBS")
                        res.response = form.responseData
                        res.responseFrom = "KEBS"
                        res.linkToUploads = form.linkToUploads
                        res.status = 1
                        res.varField1 = form.queryAnalysis
                        res.createdOn = Timestamp.from(Instant.now())
                        res.createdBy = commonDaoServices.loggedInUserAuthentication().name
                        res.modifiedOn = Timestamp.from(Instant.now())
                        res.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                        partnerQueryResponseRepository.save(res)
                        data.responseSerialNumber = res.serialNumber
                    }
                    // Send conclusion to partner
                    partnerService.getPartnerApiClient(query.partnerId!!)?.let { apiClient ->
                        data.certNumber = query.certNumber ?: "UNKNOWN"
                        data.documentType = query.certType ?: "UNKNOWN"
                        data.rfcNumber = query.rfcNumber
                        data.invoiceNumber = query.invoiceNumber
                        data.ucrNumber = query.ucrNumber
                        data.serialNumber = query.serialNumber ?: "NA"
                        this.apiClientService.publishCallbackEvent(data, apiClient.clientId!!, "QUERY_CONCLUSION")
                    }
                    //
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Response Received"
                } else {
                    KotlinLogging.logger { }.warn("Received query response from unexpected partner: ${partner?.partnerRefNo}-Expected: ${query.partnerId}")
                    response.responseCode = ResponseCodes.INVALID_CODE
                    response.message = "Invalid response received"
                }
                response
            } ?: kotlin.run {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Response Received"
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC query", ex)
        }
        return response
    }

    fun documentExists(documentType: String, ucrNumber: String, certNumber: String): Boolean {
        return when (documentType.toUpperCase()) {
            "COC" -> daoServices.findCocByUcrNumber(ucrNumber) != null
            "COI" -> daoServices.findCoiByUcrNumber(ucrNumber) != null
            "COR" -> daoServices.findCORByCorNumber(certNumber) != null
            else -> throw ExpectedDataNotFound("Invalid document type: $documentType")
        }
    }

    /**
     * KEBS query to partner
     */
    fun sendPartnerQuery(form: KebsPvocQueryForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            partnerService.getPartner(form.partnerId!!)?.let { partner ->
                partnerService.getPartnerApiClient(form.partnerId!!)?.let { apiClient ->
                    if (documentExists(form.documentType.orEmpty(), form.ucrNumber.orEmpty(), form.certNumber.orEmpty())) {
                        val query = PvocQueriesEntity()
                        query.serialNumber = queryReference("KEBS")
                        query.partnerId = partner.id
                        query.certNumber = form.certNumber
                        query.invoiceNumber = form.invoiceNumber
                        query.certType = form.documentType
                        query.queryOrigin = "KEBS"
                        query.ucrNumber = form.ucrNumber
                        query.rfcNumber = form.rfcNumber
                        query.queryDetails = form.kebsQuery
                        query.pvocAgentReplyStatus = 0
                        query.kebsReplyReplyStatus = 1
                        query.createdBy = commonDaoServices.loggedInUserAuthentication().name
                        query.createdOn = Timestamp.from(Instant.now())
                        query.modifiedOn = Timestamp.from(Instant.now())
                        this.partnerQuerriesRepository.save(query)
                        response.data = form
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Query received"

                        // Send QUERY event to PVOC partner
                        val data = KebsPvocQueryForm()
                        data.certNumber = form.certNumber ?: "UNKNOWN"
                        data.documentType = form.documentType ?: "UNKNOWN"
                        data.invoiceNumber = form.invoiceNumber ?: "UNKNOWN"
                        data.ucrNumber = form.ucrNumber ?: "NA"
                        data.serialNumber = query.serialNumber ?: "NA"
                        data.rfcNumber = form.rfcNumber
                        data.kebsQuery = form.kebsQuery
                        this.apiClientService.publishCallbackEvent(data, apiClient.clientId!!, "QUERY_REQUEST")
                    } else {
                        response.message = "${form.documentType} with reference ${form.certNumber} does not exists"
                        response.responseCode = ResponseCodes.NOT_FOUND
                    }
                    //
                    response
                } ?: run {
                    response.message = "Failed, partner does not have an API client"
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response
                }
            } ?: run {
                response.message = "Failed, no such partner"
                response.responseCode = ResponseCodes.NOT_FOUND
                response
            }
        } catch (ex: ExpectedDataNotFound) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.INVALID_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC query", ex)
            response.message = "Failed, request could not be completed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }

    /**
     * KEBs Response
     */
    @Transactional
    fun sendPartnerQueryResponse(form: KebsQueryResponseForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            this.partnerQuerriesRepository.findAllBySerialNumber(form.serialNumber!!)?.let { query ->
                val partner = partnerService.getPartner(query.partnerId!!)
                if (query.partnerId == partner?.id) {
                    val auth = commonDaoServices.loggedInUserAuthentication()
                    val queryResponse = PvocQueryResponseEntity()
                    queryResponse.response = form.queryResponse
                    queryResponse.responseFrom = "KEBS"
                    queryResponse.serialNumber = queryResponseReference("KEBS")
                    queryResponse.linkToUploads = form.linkToUploads
                    queryResponse.queryId = query
                    queryResponse.modifiedOn = Timestamp.from(Instant.now())
                    queryResponse.modifiedBy = auth.name
                    queryResponse.createdOn = Timestamp.from(Instant.now())
                    queryResponse.createdBy = auth.name
                    this.partnerQueryResponseRepository.save(queryResponse)
                    // Updated
                    query.modifiedOn = Timestamp.from(Instant.now())
                    query.modifiedBy = auth.name
                    this.partnerQuerriesRepository.save(query)
                    // Send QUERY event to PVOC partner
                    partnerService.getPartnerApiClient(query.partnerId!!)?.let { apiClient ->
                        val data = KebsQueryResponse()
                        data.certNumber = query.certNumber ?: "UNKNOWN"
                        data.documentType = query.certType ?: "UNKNOWN"
                        data.rfcNumber = query.rfcNumber
                        data.invoiceNumber = query.invoiceNumber
                        data.ucrNumber = query.ucrNumber
                        data.conclusion = query.conclusion ?: ""
                        data.linkToUploads = form.linkToUploads ?: ""
                        data.serialNumber = queryResponse.serialNumber ?: "NA"
                        data.queryAnalysis = form.queryAnalysis ?: ""
                        data.queryResponse = form.queryResponse ?: ""
                        this.apiClientService.publishCallbackEvent(data, apiClient.clientId!!, "QUERY_RESPONSE")
                    }
                    //
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Response Received"
                } else {
                    KotlinLogging.logger { }.warn("Received query response from unexpected partner: ${partner?.partnerRefNo}-Expected: ${query.partnerId}")
                    response.responseCode = ResponseCodes.INVALID_CODE
                    response.message = "Invalid response received"
                }
            } ?: kotlin.run {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Response Received"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC query", ex)
        }
        return response
    }

    fun retrievePartnerQueries(status: Int, pg: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val partner = this.commonDaoServices.loggedInPartnerDetails()
            val data = partnerQuerriesRepository.findAllByPartnerIdAndConclusionStatus(partner.id, status, pg)
            if (data.isEmpty) {
                response.message = "No such record found"
                response.responseCode = ResponseCodes.NOT_FOUND
            } else {
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
                response.data = PvocPartnerQueryDao.fromList(data.toList())
                response.pageNo = data.number
                response.totalCount = data.totalElements
                response.totalPages = data.totalPages
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC query", ex)
            response.message = "Failed, request could not be completed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }

    fun getIdfData(idfNumber: String?, ucrNumber: String?): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val idfData = pvocIntegrations.getIdfData(idfNumber.orEmpty(), ucrNumber.orEmpty())
            response.data = idfData
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: ExpectedDataNotFound) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process request", ex)
            response.message = "Failed, request could not be completed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }

    fun getRfcData(rfcNumber: String?, ucrNumber: String?, documentType: String?): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val idfData = pvocIntegrations.getRfcData(rfcNumber.orEmpty(), ucrNumber.orEmpty(), documentType.orEmpty())
            response.data = idfData
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: ExpectedDataNotFound) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process request", ex)
            response.message = "Failed, request could not be completed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }
}