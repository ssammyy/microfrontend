package org.kebs.app.kotlin.apollo.common.dto.std

import java.util.*
import kotlin.collections.HashMap


class TaskDetails(val taskId:String, val name:String,val taskData: Map<String,Any>) {
}

class TaskDetailsBody(val taskId:String, val name:String,val processId: String,val taskData: Map<String,Any>) {
}

class WorkShopAgreementTasks(val taskId:String, val name:String,val processId: String,val taskData: Map<String,Any>) {
}
