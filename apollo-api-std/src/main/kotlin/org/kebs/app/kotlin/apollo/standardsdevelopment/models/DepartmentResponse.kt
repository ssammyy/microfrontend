package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "SD_DEPARTMENT_RESPONSE")
class DepartmentResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DEPARTMENT_RESPONSE_ID")
    @Basic
    var departmentResponseID : Long = 0

    @Column(name = "DEPARTMENT_ID")
    @Basic
    val departmentID : Long = 0

    @Column(name = "EQUIRY_ID")
    @Basic
    val enquiryID : Long =0

    @Column(name = "FEEDBACK_PROVIDED")
    @Basic
    val feedbackProvided : String = ""

    @Transient
    var taskId: String = ""
}
