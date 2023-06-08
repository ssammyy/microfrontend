package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_COM_STD_CONTACT_DETAILS")
class ComContactDetails {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_COM_STD_CONTACT_DETAILS_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_COM_STD_CONTACT_DETAILS_SEQ"
    )
    @GeneratedValue(generator = "SD_COM_STD_CONTACT_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0
    @Column(name = "REQUEST_ID")
    @Basic
    var requestId: Long? = null

    @Column(name = "FULL_NAME")
    @Basic
    var fullName: String? = null

    @Column(name = "EMAIL")
    @Basic
    var email: String? = null

    @Column(name = "TELEPHONE")
    @Basic
    var telephone: String? = null

    @Column(name = "DATE_OF_CREATION")
    @Basic
    var dateOfCreation: Timestamp? = null



}
