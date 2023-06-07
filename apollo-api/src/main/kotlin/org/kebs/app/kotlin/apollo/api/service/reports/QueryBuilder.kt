package org.kebs.app.kotlin.apollo.api.service.reports

import org.assertj.core.util.Strings
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound

class QueryBuilder {
    val fields: MutableList<String>
    val criteria: MutableList<String>
    val params: MutableList<Any>
    val criteriaParams: MutableList<Any>
    val fromData: MutableList<String>
    val orderBy: MutableList<String>
    var orderDirection: String? = null
    var page: Int? = null
    var size: Long? = null

    init {
        fields = mutableListOf()
        criteria = mutableListOf()
        criteriaParams = mutableListOf()
        orderBy = mutableListOf()
        fromData = mutableListOf()
        params = mutableListOf()
    }

    fun from(f: String): QueryBuilder {
        this.fromData.add(f)
        return this
    }


    fun select(f: String): QueryBuilder {
        this.fields.add(f)
        return this
    }

    fun equal(f: String, v: Any): QueryBuilder {
        criteria.add(" $f=? ")
        params.add(v)
        criteriaParams.add(v)
        return this
    }

    fun and(): QueryBuilder {
        if (criteria.isEmpty()) {
            throw ExpectedDataNotFound("Criteria cannot start with AND")
        }
        criteria.add(" and ")
        return this
    }

    fun or(): QueryBuilder {
        if (criteria.isEmpty()) {
            throw ExpectedDataNotFound("Criteria cannot start with OR")
        }
        criteria.add(" or ")
        return this
    }

    fun between(f: String, v: Any, v2: Any, date: Boolean = false): QueryBuilder {
        if (date) {
            criteria.add(" $f between to_date(?,'YYYY-MM-DD') and to_date(?,'YYYY-MM-DD')")
        } else {
            criteria.add(" $f between ? and ? ")
        }
        params.add(v)
        params.add(v2)
        criteriaParams.add(v)
        criteriaParams.add(v2)
        return this
    }

    fun like(f: String, v: Any): QueryBuilder {
        criteria.add(" $f like '%?%' ")
        params.add(v)
        return this
    }

    fun paginate(p: Int, s: Long): QueryBuilder {
        this.page = p
        this.size = s
        return this
    }

    fun order(f: String): QueryBuilder {
        this.orderBy.add(f)
        return this
    }

    fun asc(): QueryBuilder {
        this.orderDirection = "asc"
        return this
    }

    fun desc(): QueryBuilder {
        this.orderDirection = "desc"
        return this
    }

    fun notEqual(f: String, v: Any): QueryBuilder {
        criteria.add(" $f!=? ")
        params.add(v)
        criteriaParams.add(v)
        return this
    }

    fun hasCriteria(): Boolean {
        return this.criteria.isNotEmpty()
    }

    fun countQuery(): Pair<String, List<Any>> {
        val query = StringBuilder()
        query.append("SELECT ")
            .append("count(*) as total_record")
            .append(" FROM ")
            .append(Strings.join(fromData).with(" "))
        if (!criteria.isEmpty()) {
            query.append(" WHERE ")
                .append(Strings.join(criteria).with(""))
                .append(" ")
        }
        return Pair(query.toString(), criteriaParams)
    }

    fun getQuery(): Pair<String, List<Any>> {
        val query = StringBuilder()
        query.append("SELECT ")
            .append(Strings.join(this.fields).with(","))
            .append(" FROM ")
            .append(Strings.join(fromData).with(" "))
        if (!criteria.isEmpty()) {
            query.append(" WHERE ")
                .append(Strings.join(criteria).with(""))
                .append(" ")
        }
        if (!this.orderBy.isEmpty()) {
            query.append(" ORDER BY ")
                .append(Strings.join(orderBy).with(","))
                .append(" ")
        }
        this.page?.let { p ->
            if (p >= 0) {
                query.append(" OFFSET ${p * (size ?: 20)} ROWS FETCH NEXT ${size ?: 20} ROWS ONLY")
            }
        }
        return Pair(query.toString(), params)
    }
}