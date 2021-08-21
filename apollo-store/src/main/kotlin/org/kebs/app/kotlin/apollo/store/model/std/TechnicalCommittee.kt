package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*


@Entity
@Table(name = "SD_TECHNICAL_COMMITTEE")
class TechnicalCommittee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long =0

    @Column(name = "TECHNICAL_COMMITTEE_NO")
    @Basic
    var technical_committee_no: String?=null

    @Column(name = "TC_TYPE")
    @Basic
    var type: String?=null

    @Column(name = "DEPARTMENT_ID")
    @Basic
    var departmentId: Long =0

    @Column(name = "TC")
    @Basic
    var tc : Int?= null

    @Column(name = "SC")
    @Basic
    var sc : Int?= null

    @Column(name = "WG")
    @Basic
    var wg : Int?= null

    @Column(name = "PARENT_COMMITTEE")
    @Basic
    var parentCommitte : String?= null

    @Column(name = "TC_TITLE")
    @Basic
    var title : String?= null

    @Column(name = "TC_STATUS")
    @Basic
    var status : String?= null

    @Column(name = "TC_COMMENT")
    @Basic
    var comment : String?= null




    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TechnicalCommittee

        if (id != other.id) return false
        if (technical_committee_no != other.technical_committee_no) return false
        if (type != other.type) return false
        if (tc != other.tc) return false
        if (sc != other.sc) return false
        if (wg != other.wg) return false
        if (parentCommitte != other.parentCommitte) return false
        if (title != other.title) return false
        if (status != other.status) return false
        if (comment != other.comment) return false


        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (technical_committee_no?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (tc ?: 0)
        result = 31 * result + (sc ?: 0)
        result = 31 * result + (wg ?: 0)
        result = 31 * result + (parentCommitte?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)

        return result
    }

    override fun toString(): String {
        return "TechnicalCommittee(id=$id, technical_committee_no=$technical_committee_no, type=$type, tc=$tc, sc=$sc, wg=$wg, parentCommitte=$parentCommitte, title=$title, status=$status, comment=$comment)"
    }


}
