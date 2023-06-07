package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import org.flowable.task.api.Task

class TaskDetails(var permitId: Long, var objectId:Long, var task: Task, var permitRefNo:String) {

}