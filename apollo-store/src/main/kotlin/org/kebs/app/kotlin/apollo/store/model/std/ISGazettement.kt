package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*

@Entity
@Table(name="SD_IS_GAZETTEMENT")
class ISGazettement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="INTERNATIONAL_STANDARD_NUMBER")
    @Basic
    var iSNumber:String? =null

    @Column(name="DATE_OF_GAZETTEMENT")
    @Basic
    var dateOfGazettement:String? =null

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
