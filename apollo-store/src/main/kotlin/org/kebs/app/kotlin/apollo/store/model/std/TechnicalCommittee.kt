package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
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
    var technicalCommitteeNo: String?=null

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


    @Column(name = "USER_ID")
    @Basic
    var userId: Int?= null


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

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "ADVERTISING_STATUS")
    @Basic
    var advertisingStatus: String? = null



}
