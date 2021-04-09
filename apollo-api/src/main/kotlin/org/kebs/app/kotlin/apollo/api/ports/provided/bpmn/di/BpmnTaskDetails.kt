package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.di

import org.flowable.task.api.Task

class BpmnTaskDetails(var objectId: Long, var task: Task) {
}