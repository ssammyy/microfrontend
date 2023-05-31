package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ConsignmentDocumentDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.RfcItemForm
import org.kebs.app.kotlin.apollo.api.payload.response.*
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocAgentMonitoringStatusEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

enum class MonitoringStatus(val status: Int) {
    OPEN(1), NEW(2), REVIEW(3), APPROVE(4), REJECTED(5)
}

@Service
class PvocMonitoringService(
    private val iPvocAgentMonitoringStatusEntityRepo: IPvocAgentMonitoringStatusEntityRepo,
    private val timelinesRepository: IPvocTimelinesDataEntityRepository,
    private val sealIssuesRepository: IPvocSealIssuesEntityRepository,
    private val timelinePenaltiesRepository: IPvocTimelinePenaltiesRepository,
    private val categorizationRepository: IPvocProductCategorizationIssuesRepository,
    private val rfcRepository: IRfcEntityRepo,
    private val rfcItemRepository: IRfcItemsRepository,
    private val rfcCorRepository: IRfcCorRepository,
    private val cocItemRepository: ICocItemRepository,
    private val corBakRepository: ICorsBakRepository,
    private val riskProfileRepository: IRiskProfileRepository,
    private val cocCoiRepository: ICocsRepository,
    private val pvocQuerriesRepository: IPvocQuerriesRepository,
    private val partnerService: PvocPartnerService,
) {
    fun findMonitoringRecord(yearMonth: String, agent: PvocPartnersEntity): PvocAgentMonitoringStatusEntity {
        val monitoringOptional = this.iPvocAgentMonitoringStatusEntityRepo.findFirstByPartnerIdAndYearMonthAndStatus(
            agent,
            yearMonth,
            MonitoringStatus.OPEN.status
        )
        if (monitoringOptional.isPresent) {
            return monitoringOptional.get()
        } else {
            val monitoring = PvocAgentMonitoringStatusEntity()
            monitoring.partnerId = agent
            monitoring.recordNumber =
                yearMonth + (this.iPvocAgentMonitoringStatusEntityRepo.countByPartnerIdAndYearMonth(
                    agent,
                    yearMonth
                ) + 1)
            monitoring.yearMonth = yearMonth
            monitoring.name = agent.partnerName
            monitoring.description = "Agent Monitoring record for $yearMonth"
            monitoring.monitoringStatus = MonitoringStatus.OPEN.status
            monitoring.monitoringStatusDesc = MonitoringStatus.OPEN.name
            monitoring.status = 1
            monitoring.modifiedOn = Timestamp.from(Instant.now())
            monitoring.modifiedBy = "system"
            monitoring.createdOn = Timestamp.from(Instant.now())
            monitoring.createdBy = "system"
            return this.iPvocAgentMonitoringStatusEntityRepo.save(monitoring)
        }
    }

    fun monitoringDetails(monitId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val monitoringOptional = iPvocAgentMonitoringStatusEntityRepo.findById(monitId)
            if (monitoringOptional.isPresent) {
                val data = mutableMapOf<String, Any>()
                data["monitoring"] = PvocMonitoringDto.fromEntity(monitoringOptional.get())
                monitoringOptional.get().partnerId?.let {
                    data["partner"] = PvocPartnerDto.fromEntity(it)
                }
                data["timelines"] = this.timelinesRepository.findByMonitoringId(monitId, PageRequest.of(0, 100))
//                data["seals"] = this.sealIssuesRepository.findByMonitoringId(monitId)
//                data["categorization"] = this.categorizationRepository.findByMonitoringId(monitId)
//                data["penalties"] = this.timelinePenaltiesRepository.findByMonitoringId(monitId)
                response.data = data
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Response Message"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Record not found"
            }
        } catch (ex: Exception) {
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun listAgentMonitoring(status: String, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val result = when (status) {
                "OPEN" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.OPEN.status, page)
                "NEW" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.NEW.status, page)
                "REVIEW" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.REVIEW.status, page)
                "APPROVE" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.APPROVE.status, page)
                "REJECTED" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(
                    MonitoringStatus.REJECTED.status,
                    page
                )
                else -> null
            }
            if (result == null) {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Invalid record status"
            } else {
                response.setPage(result)
                response.data = PvocMonitoringDto.fromList(result.toList())
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            }
        } catch (ex: Exception) {
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun listRfcCocCoiRequests(
        keywords: String?,
        documentType: String,
        status: Int,
        page: PageRequest
    ): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val data = when {
                !keywords.isNullOrEmpty() -> this.rfcRepository.findByRfcNumberContainsAndReviewStatus(
                    keywords,
                    status,
                    page
                )
                else -> this.rfcRepository.findByReviewStatus(status, page)
            }
            response.data = RfcDao.fromList(data.toList())
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.pageNo = data.number
            response.totalCount = data.totalElements
            response.totalPages = data.totalPages
        } catch (ex: Exception) {
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun getRfcCocCoi(rfcId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val data = this.rfcRepository.findById(rfcId)
            if (data.isEmpty) {
                response.message = "Record not found"
                response.responseCode = ResponseCodes.NOT_FOUND
            } else {
                val dataMap = mutableMapOf<String, Any>()
                dataMap["rfc"] = RfcDao.fromEntity(data.get())
                val items = this.rfcItemRepository.findByRfcId(rfcId)
                dataMap["items"] = RfcItemForm.fromList(items)
                dataMap["queries"] = data.get().rfcNumber?.let {
                    PvocKebsQueryDao.fromList(pvocQuerriesRepository.findByRfcNumber(it))
                } ?: emptyArray<PvocKebsQueryDao>()
                response.data = dataMap
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            }
        } catch (ex: Exception) {
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun listRfcCorRequests(keywords: String?, status: Int, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            // Find by chassis number or RfcNumber
            val data = when {
                !keywords.isNullOrEmpty() -> this.rfcCorRepository.findByRfcNumberContainsOrChassisNumberContains(
                    keywords,
                    keywords,
                    page
                )
                else -> this.rfcCorRepository.findByReviewStatusAndStatus(status, status.toLong(), page)
            }
            response.data = RfcCorDao.fromList(data.toList())
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.pageNo = data.number
            response.totalCount = data.totalElements
            response.totalPages = data.totalPages
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to load CORs", ex)
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun getRfcCor(rfcId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val data = this.rfcCorRepository.findById(rfcId)
            if (data.isEmpty) {
                response.message = "Record not found"
                response.responseCode = ResponseCodes.NOT_FOUND
            } else {
                val dataMap = mutableMapOf<String, Any>()
                dataMap["rfc"] = RfcCorDao.fromEntity(data.get())
                dataMap["queries"] =
                    data.get().rfcNumber?.let { PvocKebsQueryDao.fromList(pvocQuerriesRepository.findByRfcNumber(it)) }
                        ?: emptyArray<PvocKebsQueryDao>()
                response.data = dataMap
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to load CORs", ex)
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun listForeignCocCoi(
        documentType: String,
        documentCategory: String?,
        reviewStatus: Int?,
        page: PageRequest,
        keywords: String? = null
    ): ApiResponseModel {
        val response = ApiResponseModel()

        try {
            val data = when (documentCategory) {
                "F", "foreign" -> when {
                    !keywords.isNullOrEmpty() -> this.cocCoiRepository.findByCocTypeAndDocumentsTypeAndCocNumberContainsOrCocTypeAndDocumentsTypeAndUcrNumberContains(
                        documentType,
                        "F",
                        keywords,
                        documentType,
                        "F",
                        keywords,
                        page
                    )
                    else -> reviewStatus?.let { status ->
                        this.cocCoiRepository.findByCocTypeAndDocumentsTypeAndReviewStatus(
                            documentType,
                            "F",
                            status,
                            page
                        )
                    } ?: this.cocCoiRepository.findByCocTypeAndDocumentsType(documentType, "F", page)
                }
                "L", "local" -> {
                    when {
                        !keywords.isNullOrEmpty() -> this.cocCoiRepository.findByCocTypeAndDocumentsTypeAndCocNumberContainsOrCocTypeAndDocumentsTypeAndUcrNumberContains(
                            documentType,
                            "L",
                            keywords,
                            documentType,
                            "L",
                            keywords,
                            page
                        )
                        else -> reviewStatus?.let { status ->
                            this.cocCoiRepository.findByCocTypeAndDocumentsTypeAndReviewStatus(
                                documentType,
                                "L",
                                status,
                                page
                            )
                        } ?: this.cocCoiRepository.findByCocTypeAndDocumentsType(documentType, "L", page)
                    }
                }
                else -> {
                    when {
                        !keywords.isNullOrEmpty() -> this.cocCoiRepository.findByCocTypeAndCocNumberContainsOrCocTypeAndUcrNumberContains(
                            documentType,
                            keywords,
                            documentType,
                            keywords,
                            page
                        )
                        else -> reviewStatus?.let { status ->
                            this.cocCoiRepository.findByCocTypeAndReviewStatus(
                                documentType,
                                status,
                                page
                            )
                        } ?: this.cocCoiRepository.findByCocType(documentType, page)
                    }
                }
            }
            response.data = data.toList()
            response.message = "Success"
            response.pageNo = data.number
            response.totalCount = data.totalElements
            response.totalPages = data.totalPages
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun getForeignCoirOrCoc(foreignId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val data = this.cocCoiRepository.findById(foreignId)
            if (data.isEmpty) {
                response.message = "Record not found"
                response.responseCode = ResponseCodes.NOT_FOUND
            } else {
                val dataMap = mutableMapOf<String, Any?>()
                dataMap["certificate_details"] = data.get()
                dataMap["cd_details"] = data.get().consignmentDocId?.let { ConsignmentDocumentDao.fromEntity(it) }
                dataMap["pvoc_client"] = data.get().partner?.let { partnerId ->
                    this.partnerService.getPartner(partnerId)?.let { part -> PvocPartnerDto.fromEntity(part) }
                }
                dataMap["items"] = cocItemRepository.findByCocId(foreignId)
                dataMap["queries"] = PvocKebsQueryDao.fromList(
                    pvocQuerriesRepository.findAllByCertNumber(
                        when (data.get().cocType) {
                            "COC" -> data.get().cocNumber.orEmpty()
                            "COI" -> data.get().coiNumber.orEmpty()
                            else -> data.get().cocNumber.orEmpty()
                        }
                    )
                )
                response.data = dataMap
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to load foreign COC/COI", ex)
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun listForeignCor(
        documentType: String?,
        category: String?,
        reviewStatus: Int?,
        page: PageRequest,
        keywords: String? = null
    ): ApiResponseModel {
        val response = ApiResponseModel()
        val compliant = when (documentType?.toUpperCase()) {
            ConsignmentCertificatesIssues.NCR_COR.nameDesc -> "N"
            else -> "Y"
        }
        try {
            val data = when (category) {
                "F", "foreign" -> {
                    when {
                        !keywords.isNullOrEmpty() -> this.corBakRepository.findByDocumentsTypeAndCorNumberContainsAndCompliantOrDocumentsTypeAndChasisNumberContainsAndCompliant(
                            "F",
                            keywords,
                            compliant,
                            "F",
                            keywords,
                            compliant,
                            page
                        )
                        else -> reviewStatus?.let { status ->
                            this.corBakRepository.findByDocumentsTypeAndReviewStatusAndCompliant(
                                "F",
                                status,
                                compliant,
                                page
                            )
                        } ?: this.corBakRepository.findByDocumentsType("F", page)
                    }
                }
                "L", "local" -> {
                    when {
                        !keywords.isNullOrEmpty() -> this.corBakRepository.findByDocumentsTypeAndCorNumberContainsAndCompliantOrDocumentsTypeAndChasisNumberContainsAndCompliant(
                            "L",
                            keywords,
                            compliant,
                            "L",
                            keywords,
                            compliant,
                            page
                        )
                        else -> reviewStatus?.let { status ->
                            this.corBakRepository.findByDocumentsTypeAndReviewStatusAndCompliant(
                                "L",
                                status,
                                compliant,
                                page
                            )
                        } ?: this.corBakRepository.findByDocumentsType("L", page)
                    }
                }
                else -> {
                    when {
                        !keywords.isNullOrEmpty() -> this.corBakRepository.findByCorNumberContainsAndCompliantOrChasisNumberContainsAndCompliant(
                            keywords,
                            compliant,
                            keywords,
                            compliant,
                            page
                        )
                        else -> reviewStatus?.let { status ->
                            this.corBakRepository.findByReviewStatus(status, page)
                        } ?: this.corBakRepository.findAll(page)
                    }
                }

            }
            response.data = CorEntityDao.fromList(data.toList())
            response.message = "Success"
            response.pageNo = data.number
            response.totalCount = data.totalElements
            response.totalPages = data.totalPages
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun getForeignCor(foreignId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val data = this.corBakRepository.findById(foreignId)
            if (data.isEmpty) {
                response.message = "Record not found"
                response.responseCode = ResponseCodes.NOT_FOUND
            } else {
                val dataMap = mutableMapOf<String, Any?>()
                dataMap["cor_details"] = CorEntityDao.fromEntity(data.get())
                dataMap["cd_details"] = data.get().consignmentDocId?.let { ConsignmentDocumentDao.fromEntity(it) }
                dataMap["pvoc_client"] = data.get().partner?.let { partnerId ->
                    this.partnerService.getPartner(partnerId)?.let { part -> PvocPartnerDto.fromEntity(part) }
                }
                dataMap["queries"] =
                    PvocKebsQueryDao.fromList(pvocQuerriesRepository.findAllByCertNumber(data.get().corNumber.orEmpty()))
                response.data = dataMap
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to load foreign COR", ex)
            response.message = "Failed to get record"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    fun getRiskProfileDetails(riskId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val riskOptional = this.riskProfileRepository.findById(riskId)
            if (riskOptional.isPresent) {
                val responseData = mutableMapOf<String, Any?>()
                responseData["risk"] = UiRiskProfileDao.fromEntity(riskOptional.get())
                riskOptional.get().partnerId?.let {
                    responseData["partner"] =
                        partnerService.getPartner(it)?.let { it1 -> PvocPartnerDto.fromEntity(it1) }
                }
                response.data = responseData
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Risk profile not found"
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to load risk profile by id"
        }
        return response
    }

    fun listRiskProfiles(keywords: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val risks = when {
                keywords.isNullOrEmpty() -> this.riskProfileRepository.findAll(page)
                else -> this.riskProfileRepository.findByHsCodeContainsOrCreatedByContains(
                    keywords,
                    keywords,
                    page
                )
            }
            response.data = UiRiskProfileDao.fromList(risks.toList())
            response.pageNo = risks.number
            response.totalCount = risks.totalElements
            response.totalPages = risks.totalPages
            // Response codes
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to load risk profiles"
        }
        return response
    }

}