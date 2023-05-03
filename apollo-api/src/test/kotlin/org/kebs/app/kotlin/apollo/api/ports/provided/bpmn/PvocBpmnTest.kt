package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import org.junit.Ignore
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocWaiversApplicationEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.junit.Test
//import org.junit.jupiter.api.Test

@SpringBootTest
@RunWith(SpringRunner::class)
class PvocBpmnTest {
    @Autowired
    lateinit var pvocBpmn: PvocBpmn

    @Value("\${bpmn.pvoc.ea.process.definition.key}")
    lateinit var pvocEaProcessDefinitionKey: String

    @Value("\${bpmn.pvoc.wa.process.definition.key}")
    lateinit var pvocWaProcessDefinitionKey: String

    @Value("\${bpmn.pvoc.monit.process.definition.key}")
    lateinit var pvocMoProcessDefinitionKey: String


    val pvocAppEntityId: Long = 435
    val pvocWaAppEntityId: Long = 321
    val pvocTimelinesDataId: Long = 1
    val manufacturerId: Long = 101
    val sectionOfficerId:Long = 102
    val importerId:Long = 710
    val wetcId:Long = 720
    val secretaryWetcId:Long = 105
    val chairmanWetcId:Long = 106
    val nscId:Long = 107
    val csId:Long = 108
    val pvocOfficerId:Long = 109
    val hodId:Long = 110
    val mpvocId:Long = 111
    val pvocAgentId:Long = 112

    @Test
    @Ignore
    fun testPvocExemptionsApplicationsProcess() {

        //Start the process
        pvocBpmn.startPvocApplicationExemptionsProcess(PvocApplicationEntity())?.let {
            pvocBpmn.fetchTaskByObjectId(pvocAppEntityId, pvocEaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        /*
        //Fill application complete
        pvocBpmn.pvocEaFillApplicationFormComplete(pvocAppEntityId).let {
            pvocBpmn.fetchTaskByObjectId(pvocAppEntityId, pvocEaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Accept terms complete
        pvocBpmn.pvocEaAcceptTermsComplete(pvocAppEntityId).let {
            pvocBpmn.fetchTaskByObjectId(pvocAppEntityId, pvocEaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return
        */

        //Submit application complete
        pvocBpmn.pvocEaSubmitApplicationComplete(pvocAppEntityId, sectionOfficerId).let {
            pvocBpmn.fetchTaskByObjectId(pvocAppEntityId, pvocEaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Check application complete
        pvocBpmn.pvocEaCheckApplicationComplete(pvocAppEntityId, sectionOfficerId,true).let {
            pvocBpmn.fetchTaskByObjectId(pvocAppEntityId, pvocEaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        /*
        //Defer application complete
        pvocBpmn.pvocDeferApplicationComplete(pvocAppEntityId).let {
            pvocBpmn.fetchTaskByObjectId(pvocAppEntityId, pvocEaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return
        */

        //Approve application complete
        pvocBpmn.pvocEaApproveApplicationComplete(pvocAppEntityId, true).let {
            pvocBpmn.fetchTaskByObjectId(pvocAppEntityId, pvocEaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return
    }

    @Test
    fun testPvocWaiversApplicationsProcess() {
        //Start the process
        pvocBpmn.startPvocWaiversApplicationsProcess(PvocWaiversApplicationEntity(),"")?.let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaSubmitApplicationComplete(pvocWaAppEntityId, secretaryWetcId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaReviewApplicationComplete(pvocWaAppEntityId, secretaryWetcId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaGenerateReportComplete(pvocWaAppEntityId, chairmanWetcId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaApproveWaiverComplete(pvocWaAppEntityId, secretaryWetcId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaGenerateMinutesComplete(pvocWaAppEntityId, nscId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaGenerateDeferralComplete(pvocWaAppEntityId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaNscApproveComplete(pvocWaAppEntityId, nscId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaGenerateWaiverReqLetterComplete(pvocWaAppEntityId, csId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaSubmitApprovalLetterComplete(pvocWaAppEntityId, secretaryWetcId, true).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        pvocBpmn.pvocWaGenerateWaiverApprovalLetterComplete(pvocWaAppEntityId).let {
            pvocBpmn.fetchTaskByObjectId(pvocWaAppEntityId, pvocWaProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return
    }

    @Test
    @Ignore
    fun testPvocMonitoringProcess() {

        //Start the process
        pvocBpmn.startPvocMonitoringProcess(pvocTimelinesDataId, pvocOfficerId)?.let {
            pvocBpmn.fetchTaskByObjectId(pvocTimelinesDataId, pvocMoProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Generate analysis report
        pvocBpmn.pvocMoGenerateAnalysisReportComplete(pvocTimelinesDataId).let {
            pvocBpmn.fetchTaskByObjectId(pvocTimelinesDataId, pvocMoProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Check analysis report
        pvocBpmn.pvocMoCheckAnalysisReportComplete(pvocTimelinesDataId, true).let {
            pvocBpmn.fetchTaskByObjectId(pvocTimelinesDataId, pvocMoProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

    }

    @Test
    @Ignore
    fun testPvocFetchTasksByAssignee() {
        println("---------------------------")
        pvocBpmn.fetchAllTasksByAssignee(720)?.let{  taskDetails ->
            println("Task details after task complete")
            for (taskDetail in taskDetails) {
                taskDetail.task.let { task ->
                    println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} -- ${task.assignee}")
                }
            }

        }
    }
}
