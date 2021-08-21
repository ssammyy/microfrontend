package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_COM_STANDARD_DRAFT")
class ComStdDraft {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id:Long=0

    @Column(name="UPLOAD_DATE")
    @Basic
    var uploadDate: Timestamp?=null



    @Column(name="UPLOADED_BY")
    @Basic
    var uploadedBy : String?=null

    @Column(name="DOC_NAME")
    @Basic
    var documentName : String?=null

    @Column(name="UPLOADED_BY_NAME")
    @Basic
    var uploadedByName : String?=null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null
}
