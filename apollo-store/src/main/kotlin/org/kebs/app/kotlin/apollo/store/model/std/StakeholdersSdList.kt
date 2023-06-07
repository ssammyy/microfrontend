package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="CFG_SD_STAKE_HOLDERS")
class StakeholdersSdList {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "CFG_SD_STAKE_HOLDERS_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "CFG_SD_STAKE_HOLDERS_SEQ"
    )
    @GeneratedValue(generator = "CFG_SD_STAKE_HOLDERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name="NAME")
    @Basic
    var name:String? =null

    @Column(name="EMAIL")
    @Basic
    var email:String? =null

    @Column(name="TELEPHONE")
    @Basic
    var telephone:String? =null

    @Column(name="STATUS")
    @Basic
    var status:Long? =null

    @Column(name="SUB_CATEGORY_ID")
    @Basic
    var subCategoryId:Long? =null

    @Column(name="CREATED_ON")
    @Basic
    var createdOn:Timestamp? =null

    
}