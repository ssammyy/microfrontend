package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ConsignmentDocumentDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.RfcItemForm
import org.kebs.app.kotlin.apollo.api.payload.response.CorEntityDao
import org.kebs.app.kotlin.apollo.api.payload.response.PvocPartnerDto
import org.kebs.app.kotlin.apollo.api.payload.response.RfcCorDao
import org.kebs.app.kotlin.apollo.api.payload.response.RfcDao
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
    private val rfcRepository: IRfcEntityRepo,
    private val rfcItemRepository: IRfcItemsRepository,
    private val rfcCorRepository: IRfcCorRepository,
    private val cocItemRepository: ICocItemRepository,
    private val corBakRepository: ICorsBakRepository,
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
            monitoring.description = "Agent Monitoring record for $yearMonth"
            monitoring.monitoringStatus = MonitoringStatus.OPEN.status
            monitoring.monitoringStatusDesc = MonitoringStatus.OPEN.name
            monitoring.status = 1
            monitoring.createdOn = Timestamp.from(Instant.now())
            monitoring.createdBy = "system"
            return this.iPvocAgentMonitoringStatusEntityRepo.save(monitoring)
        }
    }

    fun listAgentMonitoring(status: String, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        when (status) {
            "OPEN" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.OPEN.status, page)
            "NEW" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.NEW.status, page)
            "REVIEW" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.REVIEW.status, page)
            "APPROVE" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.APPROVE.status, page)
            "REJECTED" -> iPvocAgentMonitoringStatusEntityRepo.findAllByStatus(MonitoringStatus.REJECTED.status, page)
        }
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Response Message"
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
                dataMap["queries"] = emptyArray<Any>()
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
                !keywords.isNullOrEmpty() -> this.rfcCorRepository.findByRfcNumberContainsAndReviewStatusAndStatusOrChassisNumberContainsAndReviewStatusAndStatus(
                    keywords,
                    status,
                    1,
                    keywords,
                    status,
                    1,
                    page
                )
                else -> this.rfcCorRepository.findByReviewStatusAndStatus(status, 1, page)
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
                dataMap["queries"] = emptyArray<Any>()
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
                dataMap["queries"] = pvocQuerriesRepository.findAllByCertNumber(
                    when (data.get().cocType) {
                        "COC" -> data.get().cocNumber.orEmpty()
                        "COI" -> data.get().coiNumber.orEmpty()
                        else -> data.get().cocNumber.orEmpty()
                    }
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
        category: String?,
        reviewStatus: Int?,
        page: PageRequest,
        keywords: String? = null
    ): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val data = when (category) {
                "F", "foreign" -> {
                    when {
                        !keywords.isNullOrEmpty() -> this.corBakRepository.findByDocumentsTypeAndCorNumberContainsOrDocumentsTypeAndChasisNumberContains(
                            "F",
                            keywords,
                            "F",
                            keywords,
                            page
                        )
                        else -> reviewStatus?.let { status ->
                            this.corBakRepository.findByDocumentsTypeAndReviewStatus("F", status, page)
                        } ?: this.corBakRepository.findByDocumentsType("F", page)
                    }
                }
                "L", "local" -> {
                    when {
                        !keywords.isNullOrEmpty() -> this.corBakRepository.findByDocumentsTypeAndCorNumberContainsOrDocumentsTypeAndChasisNumberContains(
                            "L",
                            keywords,
                            "L",
                            keywords,
                            page
                        )
                        else -> reviewStatus?.let { status ->
                            this.corBakRepository.findByDocumentsTypeAndReviewStatus("L", status, page)
                        } ?: this.corBakRepository.findByDocumentsType("L", page)
                    }
                }
                else -> {
                    when {
                        !keywords.isNullOrEmpty() -> this.corBakRepository.findByCorNumberContainsOrChasisNumberContains(
                            keywords,
                            keywords,
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
                dataMap["queries"] = pvocQuerriesRepository.findAllByCertNumber(data.get().corNumber.orEmpty())
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

}