package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_NEP_REQUEST_REMARKS")
class NepRemarks {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_NEP_REQUEST_REMARKS_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_NEP_REQUEST_REMARKS_SEQ"
    )
    @GeneratedValue(generator = "SD_NEP_REQUEST_REMARKS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name="REQUEST_ID")
    @Basic
    var requestId:Long? =null

    @Column(name="REMARKS")
    @Basic
    var remarks:String? =null

    @Column(name="REMARK_BY")
    @Basic
    var remarkBy:String? =null

    @Column(name="ROLE")
    @Basic
    var role:Int? =null

    @Column(name="DESCRIPTION")
    @Basic
    var description:String? =null

    @Column(name="DATE_OF_REMARK")
    @Basic
    var dateOfRemark:Timestamp? =null

    @Column(name="NEP_DRAFT_ID")
    @Basic
    var nepDraftId:Long? =null




    
}