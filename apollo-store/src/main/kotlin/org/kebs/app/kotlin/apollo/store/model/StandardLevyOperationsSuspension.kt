package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_SUSPENSION_OF_OPERATIONS")
class StandardLevyOperationsSuspension : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_SUSPENSION_OF_OPERATIONS_SEQ_GEN",
        sequenceName = "DAT_KEBS_SUSPENSION_OF_OPERATIONS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_SUSPENSION_OF_OPERATIONS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "COMPANY_ID")
    @Basic
    var companyId: Long? = null

    @Column(name = "REASON")
    @Basic
    var reason: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "DATE_OF_SUSPENSION")
    @Basic
    var dateOfSuspension: String? = null



}

