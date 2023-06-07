package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name = "SD_RANKING_CRITERIA")
class RankingCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUESTOR_ID")
    var id: Long = 0

    @Column(name = "DEPARTMENT_ID")
    @Basic
    var departmentId: Long = 0

    @Column(name = "COMMITTEE_ID")
    @Basic
    var committeeId: Long = 0

    @Column(name = "HSCODE")
    @Basic
    var hsCode: Long = 0

    @Column(name = "PRODUCT_LEVEL")
    @Basic
    var productLevel: String? = null

    @Column(name = "PERCENTAGE")
    @Basic
    var percentage: Double = 0.0

    @Column(name = "RANK")
    @Basic
    var rank: Long = 0


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RankingCriteria

        if (id != other.id) return false
        if (departmentId != other.departmentId) return false
        if (committeeId != other.committeeId) return false
        if (hsCode != other.hsCode) return false
        if (productLevel != other.productLevel) return false
        if (percentage != other.percentage) return false
        if (rank != other.rank) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (departmentId.hashCode() ?: 0)
        result = 31 * result + (committeeId.hashCode() ?: 0)
        result = 31 * result + (hsCode.hashCode() ?: 0)

        result = 31 * result + (productLevel?.hashCode() ?: 0)
        result = 31 * result + (percentage.hashCode() ?: 0)
        result = 31 * result + (rank.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "RankingCriteria(id=$id, departmentId=$departmentId, committeeId=$committeeId" +
                "hsCode=$hsCode,productLevel=$productLevel, percentage=$percentage, rank=$rank)"
    }

}
