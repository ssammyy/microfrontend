package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_COM_STANDARD_ASSIGNEE")
class ComStdAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long=0

    @Column(name="REQUEST_NUMBER")
    @Basic
    var requestNumber:String?=null

    @Column(name="DATE_ASSIGNED")
    @Basic
    var dateAssigned: Timestamp?=null

    @Column(name="ASSIGNED_TO")
    @Basic
    var assignedTo:Long?=null

    @Column(name="NAME_ASSIGNED_TO")
    @Basic
    var plAssigned:String?=null

    //  @Transient
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null


}
