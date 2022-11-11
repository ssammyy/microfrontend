package org.kebs.app.kotlin.apollo.common.dto.std

import java.sql.Date
import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap


class TaskDetails(val taskId:String, val name:String,val taskData: Map<String,Any>) {
}

class TaskDetailsBody(val taskId:String, val name:String,val processId: String,val taskData: Map<String,Any>) {
}

class WorkShopAgreementTasks(val taskId:String, val name:String,val processId: String,val taskData: Map<String,Any>) {
}
class InternationalStandardTasks(val taskId:String, val name:String,val processId: String,val taskData: Map<String,Any>) {
}


class StdUserTasks(val taskId:String, val name:String,val processId: String,val taskData: Map<String,Any>) {
}
data class DepartmentDto(
    var departmentName: String? = null

)


data class StandardsDto(
    var id: Long? = null,
    var requestNumber: String? = null,
    var rank: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var submissionDate: Timestamp? = null,
    var departmentId: String? = null,
    var tcId: String? = null,
    var organisationName: String? = null,
    var subject: String? = null,
    var description: String? = null,
    var economicEfficiency: String? = null,
    var healthSafety: String? = null,
    var environment: String? = null,
    var integration: String? = null,
    var exportMarkets: String? = null,
    var levelOfStandard: String? = null,
    var status: String? = null,
    var departmentName: String? = null,
    //HOF Feedback
    var tcSecAssigned: String? = null,
    var reviewedBy: String? = null,
    var reviewDate: Timestamp? = null,
    var reason: String? = null,
    var desiredOutput: String?=null,


    var createdBy: String? = null,
    var createdOn: Timestamp? = null,
    var modifiedOn: Timestamp? = null,
    var deletedOn: Timestamp? = null,
    var deleteBy: String? = null,


    )

class StandardReviewTasks(val taskId:String, val name:String,val processId: String,val taskData: Map<String,Any>) {
}

