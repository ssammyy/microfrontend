package org.kebs.app.kotlin.apollo.api.payload.response

import org.kebs.app.kotlin.apollo.store.model.di.InspectionFeeRanges
import java.math.BigDecimal

class DiFeeRangesDao {
    var rangeId: Long? = null
    var feeId: Long? = null
    var minimumUsd: BigDecimal? = null
    var maximumUsd: BigDecimal? = null
    var minimumKsh: BigDecimal? = null
    var maximumKsh: BigDecimal? = null
    var fixedAmount: BigDecimal? = null
    var rate: BigDecimal? = null
    var rateType: String? = null
    var name: String? = null
    var description: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(range: InspectionFeeRanges): DiFeeRangesDao {
            return DiFeeRangesDao().apply {
                feeId = range.inspectionFee?.id
                rangeId = range.id
                minimumUsd = range.minimumUsd
                minimumKsh = range.minimumUsd
                maximumUsd = range.maximumUsd
                maximumKsh = range.maximumKsh
                fixedAmount = range.fixedAmount
                rate = range.rate
                rateType = range.rateType
                name = range.name
                description = range.description
                status = range.status
            }
        }

        fun fromList(ranges: List<InspectionFeeRanges>): List<DiFeeRangesDao> {
            val daos = mutableListOf<DiFeeRangesDao>()
            ranges.forEach { ll -> daos.add(fromEntity(ll)) }
            return daos
        }
    }
}