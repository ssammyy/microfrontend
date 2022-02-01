package org.kebs.app.kotlin.apollo.api.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import javax.persistence.EntityManager
import javax.persistence.Tuple


@Service
class InspectionDashboard(
        private val entityManager: EntityManager,
        private val objectMapper: ObjectMapper,
        private val commonDaoServices: CommonDaoServices
) {
    private final val taskQuery = "select sum(case when (AHT.END_TIME_ is not null) then 1 else 0 end) completed_tasks, sum(case when (AHT.END_TIME_ is null) then 1 else 0 end) pending_tasks from ACT_HI_TASKINST AHT where upper(ASSIGNEE_)=upper(?)"
    private final val sampleQuery = "select sum(case when (cid.SAMPLE_BS_NUMBER_STATUS = 1) then 1 else 0 end)   as bs_no_updated,\n" +
            "       sum(case when (cid.SAMPLE_BS_NUMBER_STATUS != 1) then 1 else 0 end)  as bs_no_not_updated,\n" +
            "       sum(case when (cid.SAMPLE_COLLECTED_STATUS = 1) then 1 else 0 end)   as sample_collected,\n" +
            "       sum(case when (cid.SAMPLE_COLLECTED_STATUS != 1) then 1 else 0 end)  as sample_not_collected,\n" +
            "       sum(case when (cid.SAMPLE_SUBMISSION_STATUS = 1) then 1 else 0 end)  as sample_submitted,\n" +
            "       sum(case when (cid.SAMPLE_SUBMISSION_STATUS != 1) then 1 else 0 end) as sample_not_submitted\n" +
            "from DAT_KEBS_CD_ITEM_DETAILS cid\n" +
            " where SAMPLED_STATUS = 1"
    private final val myAssignedLabStats="select sum(case when (cid.SAMPLE_BS_NUMBER_STATUS = 1) then 1 else 0 end)   as bs_no_updated,\n" +
            "       sum(case when (cid.SAMPLE_BS_NUMBER_STATUS != 1) then 1 else 0 end)  as bs_no_not_updated,\n" +
            "       sum(case when (cid.SAMPLE_COLLECTED_STATUS = 1) then 1 else 0 end)   as sample_collected,\n" +
            "       sum(case when (cid.SAMPLE_COLLECTED_STATUS != 1) then 1 else 0 end)  as sample_not_collected,\n" +
            "       sum(case when (cid.SAMPLE_SUBMISSION_STATUS = 1) then 1 else 0 end)  as sample_submitted,\n" +
            "       sum(case when (cid.SAMPLE_SUBMISSION_STATUS != 1) then 1 else 0 end) as sample_not_submitted\n" +
            " from DAT_KEBS_CD_ITEM_DETAILS cid left join DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS cd on (cid.CD_DOC_ID=cd.ID)\n" +
            " where SAMPLED_STATUS = 1 and cd.ASSIGNER=?"
    private final val mySampleSubmissions = "select sum(case when (cid.SAMPLE_BS_NUMBER_STATUS = 1) then 1 else 0 end)   as bs_no_updated,\n" +
            "       sum(case when (cid.SAMPLE_BS_NUMBER_STATUS != 1) then 1 else 0 end)  as bs_no_not_updated,\n" +
            "       sum(case when (cid.SAMPLE_COLLECTED_STATUS = 1) then 1 else 0 end)   as sample_collected,\n" +
            "       sum(case when (cid.SAMPLE_COLLECTED_STATUS != 1) then 1 else 0 end)  as sample_not_collected,\n" +
            "       sum(case when (cid.SAMPLE_SUBMISSION_STATUS = 1) then 1 else 0 end)  as sample_submitted,\n" +
            "       sum(case when (cid.SAMPLE_SUBMISSION_STATUS != 1) then 1 else 0 end) as sample_not_submitted\n" +
            " from DAT_KEBS_CD_ITEM_DETAILS cid left join DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS cd on (cid.CD_DOC_ID=cd.ID)\n" +
            " where SAMPLED_STATUS = 1 and cd.ASSIGNED_INSPECTION_OFFICER=?"
    private final val consignmentDocumentStats = "select count(*) as                                                                                 total_document,\n" +
            "       sum(case when (cd.COMPLIANT_STATUS = 1) then 1 else 0 end)                                  compliant_documents,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS = 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end)  COR_ISSUED,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS != 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end) COR_PENDING,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS != 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end) COC_PENDING,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS = 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end)  COC_ISSUED,\n" +
            "       sum(case when (cd.TARGET_STATUS = 1) then 1 else 0 end)                                     TARGETED_DOCUMENTS,\n" +
            "       sum(case when (cd.TARGET_STATUS = 1 and cd.INSPECTION_CHECKLIST = 1) then 1 else 0 end)     INSPECTION_COMPLETED,\n" +
            "       sum(case\n" +
            "               when (cd.TARGET_STATUS = 1 and cd.INSPECTION_CHECKLIST != 1) then 1\n" +
            "               else 0 end)                                                                         INSPECTION_INPROGRESS,\n" +
            "       sum(case when (cd.LOCAL_COI = 1) then 1 else 0 end)                                         COI_ISSUED,\n" +
            "       dt.VAR_FIELD_1 as KEY_NAME\n" +
            "  from DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS cd\n" +
            "         left join CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES dt on (cd.CD_TYPE = dt.ID)\n" +
            " group by dt.ID, dt.VAR_FIELD_1"
    private final val myAssignedDocumentsStats="select count(*) as                                                                                 total_document,\n" +
            "       sum(case when (cd.COMPLIANT_STATUS = 1) then 1 else 0 end)                                  compliant_documents,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS = 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end)  COR_ISSUED,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS != 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end) COR_PENDING,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS != 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end) COC_PENDING,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS = 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end)  COC_ISSUED,\n" +
            "       sum(case when (cd.TARGET_STATUS = 1) then 1 else 0 end)                                     TARGETED_DOCUMENTS,\n" +
            "       sum(case when (cd.TARGET_STATUS = 1 and cd.INSPECTION_CHECKLIST = 1) then 1 else 0 end)     INSPECTION_COMPLETED,\n" +
            "       sum(case\n" +
            "               when (cd.TARGET_STATUS = 1 and cd.INSPECTION_CHECKLIST != 1) then 1\n" +
            "               else 0 end)                                                                         INSPECTION_INPROGRESS,\n" +
            "       sum(case when (cd.LOCAL_COI = 1) then 1 else 0 end)                                         COI_ISSUED,\n" +
            "       dt.VAR_FIELD_1 as KEY_NAME\n" +
            " from DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS cd\n" +
            "         left join CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES dt on (cd.CD_TYPE = dt.ID)\n" +
            " where ASSIGNER = ?\n" +
            " group by dt.ID,, dt.VAR_FIELD_1"
    private final val myConsignmentDocumentStats = "select count(*) as total_document,\n" +
            "       sum(case when (cd.COMPLIANT_STATUS = 1) then 1 else 0 end)                                  compliant_documents,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS = 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end)  COR_ISSUED,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS != 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end) COR_PENDING,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS != 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end) COC_PENDING,\n" +
            "       sum(case when (cd.LOCAL_COC_COR_STATUS = 1 and dt.LOCAL_COR_STATUS = 1) then 1 else 0 end)  COC_ISSUED,\n" +
            "       sum(case when (cd.TARGET_STATUS = 1) then 1 else 0 end)                                     TARGETED_DOCUMENTS,\n" +
            "       sum(case when (cd.TARGET_STATUS = 1 and cd.INSPECTION_CHECKLIST = 1) then 1 else 0 end)     INSPECTION_COMPLETED,\n" +
            "       sum(case\n" +
            "               when (cd.TARGET_STATUS = 1 and cd.INSPECTION_CHECKLIST != 1) then 1\n" +
            "               else 0 end)                                                                         INSPECTION_INPROGRESS,\n" +
            "       sum(case when (cd.LOCAL_COI = 1) then 1 else 0 end)                                         COI_ISSUED,\n" +
            "       dt.VAR_FIELD_1 as KEY_NAME\n" +
            " from DAT_KEBS_CONSIGNMENT_DOCUMENT_DETAILS cd\n" +
            "         left join CFG_KEBS_CONSIGNMENT_DOCUMENT_TYPES dt on (cd.CD_TYPE = dt.ID and cd.CD_TYPE is not null)\n" +
            " where ASSIGNED_INSPECTION_OFFICER = ?\n" +
            " group by cd.CD_TYPE,dt.VAR_FIELD_1"

    fun inspectionStatistics(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val stats = mutableMapOf<String, Any?>()
            val query = entityManager.createNativeQuery(sampleQuery, Tuple::class.java)
            query.resultList.get(0).let {
                stats.put("samples", fromTuple(it as Tuple))
            }
            KotlinLogging.logger { }.debug(consignmentDocumentStats)
            val cdStats = entityManager.createNativeQuery(consignmentDocumentStats, Tuple::class.java)
            cdStats.resultList.forEach {
                val dt = fromTuple(it as Tuple)
                stats.put(dt.get("KEY_NAME").toString().toUpperCase(), dt)
            }
            response.data = stats
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO FETCH STATS", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Load stats failed"
        }
        return ServerResponse.ok().body(response)
    }

    fun myInspectionStatistics(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val stats = mutableMapOf<String, Any?>()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            var queryStr:String=""
            var sampleQueryStats=""
            val auth = commonDaoServices.loggedInUserAuthentication()
            when{
                auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                    // My Tasks
                    val query = entityManager.createNativeQuery(taskQuery, Tuple::class.java)
                    query.setParameter(1, loggedInUser.userName)
                    query.resultList.get(0).let {
                        stats.put("tasks", fromTuple(it as Tuple))
                    }
                    // Supervising
                    sampleQueryStats=myAssignedLabStats
                    queryStr=myAssignedDocumentsStats
                }
                auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                    // Working on
                    sampleQueryStats=mySampleSubmissions
                    queryStr=myConsignmentDocumentStats
                }
                else->{
                    sampleQueryStats=mySampleSubmissions
                    queryStr=myConsignmentDocumentStats
                }
            }
            val sampleQuery = entityManager.createNativeQuery(sampleQueryStats, Tuple::class.java)
            sampleQuery.setParameter(1, loggedInUser.id)
            sampleQuery.resultList.get(0).let {
                stats.put("samples", fromTuple(it as Tuple))
            }
            KotlinLogging.logger { }.debug(queryStr)
            val cdStats = entityManager.createNativeQuery(queryStr, Tuple::class.java)
            cdStats.setParameter(1, loggedInUser.id)
            cdStats.resultList.forEach {
                val dt = fromTuple(it as Tuple)
                stats.put(dt.get("KEY_NAME").toString().toUpperCase(), dt)
            }
            response.data = stats
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO GET INSPECTION STATS", ex)
            response.message = "Failed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }

    fun fromTuple(t: Tuple): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        t.elements.forEach {
            map.put(it.alias, t.get(it.alias))
        }
        return map
    }
}