package org.kebs.app.kotlin.apollo.api.service.reports


import mu.KotlinLogging
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.Tuple
import kotlin.math.ceil


class ReportItems {
    @JsonProperty("base_query")
    var baseQuery: String? = null

    @JsonProperty("class_name")
    var className: String? = null
    var filters: Map<String, Any>? = null
    var fields: Map<String, String>? = null
}

class Reports {
    var reports: Map<String, ReportItems>? = null
}

@Service
class DIReports(
    private val resourceLoader: ResourceLoader,
    private val mapper: ObjectMapper,
    private val entityManager: EntityManager
) {
    final val reports: Reports

    init {
        try {
            val resource = resourceLoader.getResource("classpath:apollo_reports.json").inputStream
            reports = mapper.readValue(resource, Reports::class.java)
            KotlinLogging.logger {}.info("DDD-:${mapper.writeValueAsString(reports)}")
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun createWhereClause(
        page: Int,
        size: Long,
        reportName: String,
        filters: Map<String, Any>
    ): Pair<List<*>, Any> {
        val reportDetails = this.reports.reports?.get(reportName)
        val qb = QueryBuilder()
        qb.from(reportDetails?.baseQuery ?: "")
        // Fields
        reportDetails?.fields?.forEach({ entry ->
            qb.select(entry.key)
        })

        // Conditions
        reportDetails?.filters?.forEach({ cond ->
            val conditions = cond.key.split("__")
            KotlinLogging.logger { }.info("COND: ${cond.key}-> ${cond.value}")
            if (conditions.size > 1) {
                when (conditions[1]) {
                    "range" -> {
                        filters.get("${conditions[0]}__start")?.let { start ->
                            filters.get("${conditions[0]}__end")?.let { end ->
                                if (conditions[0].contains("date")) {
                                    if (qb.hasCriteria()) {
                                        qb.and()
                                            .between(cond.value.toString(), start, end, true)
                                    } else {
                                        qb.between(cond.value.toString(), start, end, true)
                                    }
                                } else {
                                    if (qb.hasCriteria()) {
                                        qb.and()
                                            .between(cond.value.toString(), start, end);
                                    } else {
                                        qb.between(cond.value.toString(), start, end)
                                    }
                                }
                            }
                        }
                    }
                    "or" -> {
                        filters.get(conditions[0])?.let {
                            qb.or().equal(cond.value.toString(), it)
                        }
                    }
                    "ne" -> {
                        filters.get(conditions[0])?.let {
                            qb.notEqual(cond.value.toString(), it)
                        }
                    }
                    else -> KotlinLogging.logger { }.warn("Unknown ondition: ${conditions[1]}")
                }
            } else if (filters.containsKey(conditions[0])) {
                KotlinLogging.logger { }.info("COND2: ${cond.value}-> ${conditions[0]}")
                // Condition with range
                if (qb.hasCriteria()) {
                    filters.get(conditions[0])?.let {
                        qb.and().equal(cond.value.toString(), it)
                    }
                } else {
                    filters.get(conditions[0])?.let {
                        qb.equal(cond.value.toString(), it)
                    }
                }
            }
        })
        // Pagination
        if (size > 0) {
            qb.paginate(page, size)
        }
        // Execute query
        val query = qb.getQuery()
        val pageQuery = qb.countQuery()
        KotlinLogging.logger {}.info("QUERY STRING: ${query.first}")
        val res = this.entityManager.createNativeQuery(query.first, Tuple::class.java)
        val count = this.entityManager.createNativeQuery(pageQuery.first)
        query.second.forEachIndexed { i, v ->
            res.setParameter(i + 1, v)
        }
        pageQuery.second.forEachIndexed({ i, v ->
            count.setParameter(i + 1, v)
        })
        val records = mutableListOf<Map<String, Any?>>()
        res.resultList.forEach({ dt ->
            records.add(resultSetTupleMap(dt as Tuple))
        })
        return Pair(records, count.singleResult)
    }

    fun resultSetTupleMap(tuple: Tuple): Map<String, Any?> {
        val mutableMap = mutableMapOf<String, Any?>()
        tuple.elements.forEach({ el ->
            mutableMap.put(el.alias, tuple.get(el.alias))
        })
        return mutableMap
    }

    fun generateReport(filters: Map<String, Any>, reportName: String, page: Int, size: Long): ApiResponseModel {
        val model = ApiResponseModel()
        try {
            if (this.reports.reports?.contains(reportName) == true) {
                model.responseCode = ResponseCodes.SUCCESS_CODE
                KotlinLogging.logger {}.info("REPORT DATA: $filters")
                val data = createWhereClause(page, size, reportName, filters)
                model.data = data.first
                model.pageNo = page
                model.totalCount = data.second.toString().toLongOrNull()
                model.totalCount?.let { count ->
                    model.totalPages = ceil(count.div(size.toDouble())).toInt()
                }
                model.message = "Success"
            } else {
                model.responseCode = ResponseCodes.FAILED_CODE
                model.message = "Invalid report name"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger {}.error("LOAD REPORT", ex)
            model.message = "FAILED"
            model.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return model
    }

    fun downloadReport(filters: Map<String, Any>, reportName: String): ApiResponseModel {
        val model = ApiResponseModel()
        try {
            if (this.reports.reports?.contains(reportName) == true) {
                val data = createWhereClause(0, 100_000, reportName, filters)
                if (data.first.isEmpty()) {
                    model.responseCode = ResponseCodes.NOT_FOUND
                    model.message = "No record found for these parameters"
                } else {
                    val mp = mutableMapOf<String, Any>()
                    mp["fields"] = this.reports.reports?.get(reportName)?.fields!!
                    mp["data"] = data.first
                    model.data = mp
                    model.totalCount = data.second.toString().toLongOrNull()
                    model.responseCode = ResponseCodes.SUCCESS_CODE
                    model.message = "Success"
                }
            } else {
                model.responseCode = ResponseCodes.FAILED_CODE
                model.message = "Invalid report name"
            }
        } catch (ex: Exception) {
            model.responseCode = ResponseCodes.EXCEPTION_STATUS
            model.message = "Failed to generate report"
            KotlinLogging.logger {}.error("DOWNLOAD REPORT", ex)
        }
        return model
    }
}
