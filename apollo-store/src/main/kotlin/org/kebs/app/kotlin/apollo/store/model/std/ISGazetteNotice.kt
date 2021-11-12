package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_IS_GAZETTE_NOTICE")
class ISGazetteNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="INTERNATIONAL_STANDARD_NUMBER")
    @Basic
    var iSNumber:String? =null

    @Column(name="DATE_UPLOADED")
    @Basic
    var dateUploaded: Timestamp?=null

    @Column(name="DESCRIPTION")
    @Basic
    var description:String? =null



    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    val accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null
}
