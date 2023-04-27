package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="CFG_SD_CATEGORIES")
class StakeholdersCategories {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "CFG_SD_CATEGORIES_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "CFG_SD_CATEGORIES_SEQ"
    )
    @GeneratedValue(generator = "CFG_SD_CATEGORIES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name="NAME")
    @Basic
    var name:String? =null

    @Column(name="CREATED_ON")
    @Basic
    var createdOn:Timestamp? =null

    
}