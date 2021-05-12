package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import org.flowable.engine.ManagementService
import org.flowable.engine.ProcessEngineConfiguration
import org.flowable.engine.RuntimeService
import org.flowable.engine.runtime.ProcessInstance
import org.flowable.job.api.Job
import org.junit.Ignore
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.Test


@SpringBootTest
@RunWith(SpringRunner::class)
class SLBpmnTest(
        ) {
    @Autowired
    lateinit var standardsLevyBpmn: StandardsLevyBpmn

    @Autowired
    lateinit var bpmnCommonFunctions: BpmnCommonFunctions

    @Value("\${bpmn.sl.registration.process.definition.key}")
    lateinit var slRegistrationProcessDefinitionKey: String

    @Value("\${bpmn.sl.registration..business.key}")
    lateinit var slRegistrationBusinessKey: String

    @Value("\${bpmn.sl.site.visit.process.definition.key}")
    lateinit var slSiteVisitProcessDefinitionKey: String

    @Value("\${bpmn.sl.site.visit..business.key}")
    lateinit var slSiteVisitBusinessKey: String



    //val manufacturerId: Long = 2
    val standardsLevyId: Long = 2
    val standardsLevyReportId: Long = 62
    val assigneeId:Long = 101
    val asstManagerId:Long = 102
    val managerId:Long = 103

    @Test
    @Ignore
    fun testSlRegistrationProcess() {
        //Start the process
        standardsLevyBpmn.startSlRegistrationProcess(standardsLevyId, assigneeId)?.let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        println("Printing tasks for assignee $assigneeId")
        standardsLevyBpmn.fetchAllTasksByAssignee(assigneeId)?.let{ taskDetails->
            for (taskDetail in taskDetails){
                taskDetail.task.let{ task->
                    println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                }
            }
        }

        //Manufacturer registration complete
        standardsLevyBpmn.slManufacturerRegistrationComplete(standardsLevyId, true).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        println("Printing tasks for assignee $assigneeId")
        standardsLevyBpmn.fetchAllTasksByAssignee(assigneeId)?.let{ taskDetails->
            for (taskDetail in taskDetails){
                taskDetail.task.let{ task->
                    println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                }
            }
        }

        //Fill SL1C form complete
        standardsLevyBpmn.slFillSl1CFormComplete(standardsLevyId).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Submit details complete
        standardsLevyBpmn.slSubmitDetailsComplete(standardsLevyId).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyId, slRegistrationProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

    }

    @Test
    //@Ignore
    fun testSlSiteVisitProcess() {
        //Start the process
        standardsLevyBpmn.startSlSiteVisitProcess(standardsLevyReportId, assigneeId)?.let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyReportId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Query manufacturer details complete
        standardsLevyBpmn.slsvQueryManufacturerDetailsComplete(standardsLevyReportId).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyReportId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Schedule visit complete
        standardsLevyBpmn.slsvScheduleVisitComplete(standardsLevyReportId).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyReportId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Prepare visit report complete
        standardsLevyBpmn.slsvPrepareVisitReportComplete(standardsLevyReportId, asstManagerId).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyReportId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Assistant manager approve complete
        standardsLevyBpmn.slsvApproveReportAsstManagerComplete(standardsLevyReportId, managerId,true).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyReportId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Manager approve complete
        standardsLevyBpmn.slsvApproveReportManagerComplete(standardsLevyReportId, assigneeId,true).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyReportId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Draft feedback complete
        standardsLevyBpmn.slsvDraftFeedbackComplete(standardsLevyReportId).let {
            standardsLevyBpmn.fetchTaskByObjectId(standardsLevyReportId, slSiteVisitProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return
    }


}