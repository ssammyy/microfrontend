package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di

import java.util.*

class BpmnTaskDetails(var objectId: Long, var task: org.flowable.task.api.Task) {
}

class DiTaskDetails(var objectId: String,
                    var task: String,
                    var createdOn: Date,
                    var description: String?,
                    var map: Map<String, Any?>,
                    var category: String? = null) {
}