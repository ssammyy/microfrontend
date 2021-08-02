package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name = "SD_CALL_FOR_TC_APPLICATION")
class CallForTCApplication {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long =0

    @Column(name = "DATE_OF_PUBLISHING")
    @Basic
    var dateOfPublishing: String? = null

    @Column(name = "TC")
    @Basic
    var tc: String? = null
}
