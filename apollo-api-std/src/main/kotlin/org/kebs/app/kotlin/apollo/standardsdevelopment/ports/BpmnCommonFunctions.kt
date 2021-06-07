package org.kebs.app.kotlin.apollo.standardsdevelopment.ports

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.springframework.beans.factory.annotation.Qualifier

class BpmnCommonFunctions(private val runtimeService: RuntimeService,
                          private val taskService: TaskService,
                          @Qualifier("processEngine") private val processEngine: ProcessEngine,
                          private val repositoryService: RepositoryService)
