package org.kebs.app.kotlin.apollo.store.repo.stdLevy

import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.Basic
import javax.persistence.Column

class StdLevyDTO {
}
class ManufacturePenaltyDetailsDTO{
    var id: Long? = null
    var manufacturerId: Long? = null
    var kraPin: String? = null
    var manufacturerName: String? = null
    var recordStatus: String? = null
    var transactionDate: Date? = null
    var transmissionDate: Timestamp? = null
    var penaltyPayable: BigDecimal? = null
    var penaltyGenerationDate: Timestamp? = null
    var periodFrom: java.util.Date? = null
    var periodTo: java.util.Date? = null
    var retries: Int? = null
    var retried: Int? = null
    var descriptions: String? = null
    var description: String? = null
    var status: Int? = null

}