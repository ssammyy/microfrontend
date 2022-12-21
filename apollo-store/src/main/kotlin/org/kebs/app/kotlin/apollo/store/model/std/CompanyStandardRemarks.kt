package org.kebs.app.kotlin.apollo.store.model.std

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_COM_STD_REMARKS")
class CompanyStandardRemarks : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_COM_STD_REMARKS_SEQ_GEN",
        sequenceName = "DAT_KEBS_COM_STD_REMARKS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_COM_STD_REMARKS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "REQUEST_ID")
    @Basic
    var requestId: Long? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "REMARK_BY")
    @Basic
    var remarkBy: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null

    @Column(name = "ROLE")
    @Basic
    var role: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "DATE_OF_REMARK")
    @Basic
    var dateOfRemark: Timestamp? = null

}