package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class DiBpmnTest {

    @Autowired
    lateinit var destinationInspectionBpmn: DestinationInspectionBpmn

    @Value("\${bpmn.di.mv.cor.process.definition.key}")
    lateinit var diMvWithCorProcessDefinitionKey: String

    @Value("\${bpmn.di.mv.cor.business.key}")
    lateinit var diMvWithCorBusinessKey: String

    @Value("\${bpmn.di.mv.inspection.process.definition.key}")
    lateinit var diMvInspectionProcessDefinitionKey: String

    val cdEntityId: Long = 381
    val inspectionOfficerInChargeId: Long = 1081
    val inspectionOfficerId: Long = 1083

    //Inspection variables
    val cdItemDetails: Long = 441

    @Test
    fun testDiImportedVehiclesWithCorProcessSupervisorTarget() {
        val supervisorTarget = 1
        val cdDecision: String = "APPROVE"

        //Start the process
        destinationInspectionBpmn.startImportedVehiclesWithCorProcess(cdEntityId, inspectionOfficerId)?.let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Assisgn IO
        destinationInspectionBpmn.diAssignIOComplete(cdEntityId, inspectionOfficerId, supervisorTarget).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after diAssignIOComplete task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return
    }

    @Test
    fun testDiImportedVehiclesWithCorProcessIOTarget() {
        val supervisorTarget = 0
        val supervisorTargetApproved = true
        val cdDecision = "TARGET"
        val ministryOfficerId: Long = 1143
        val cdItemId: Long = 441

        //Start the process
        destinationInspectionBpmn.startImportedVehiclesWithCorProcess(cdEntityId, inspectionOfficerId)?.let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Assisgn IO
        destinationInspectionBpmn.diAssignIOComplete(cdEntityId, inspectionOfficerId, supervisorTarget).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after diAssignIOComplete task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

//        Check application complete
        destinationInspectionBpmn.diCheckCdComplete(cdEntityId, inspectionOfficerInChargeId, cdDecision).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after diCheckCdComplete task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        // Target review request complete
        destinationInspectionBpmn.diReviewTargetRequestComplete(cdEntityId, inspectionOfficerInChargeId, supervisorTargetApproved, cdItemId).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after diReviewTargetRequestComplete task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Receive Inspection Schedule Complete
        destinationInspectionBpmn.diReceiveInspectionScheduleComplete(cdEntityId, inspectionOfficerId).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after triggerInspectionScheduleRequest task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Payment Required Complete
        destinationInspectionBpmn.diPaymentRequiredComplete(cdEntityId, inspectionOfficerId, true).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Payment Required task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

//        //Generate demand note Complete
//        destinationInspectionBpmn.diGenerateDemandNoteComplete(cdEntityId).let {
//            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after Generate Demand Note task complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return

        //Receive Payment Confirmation
        destinationInspectionBpmn.diReceivePaymentConfirmation(cdEntityId, inspectionOfficerId).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Receive Payment Confirmation task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Fill Inspection Forms
        destinationInspectionBpmn.diFillInspectionForms(cdEntityId, inspectionOfficerId).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Fill Inspection Forms task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Check Ministry Inspection Required
        destinationInspectionBpmn.diMinistryInspectionRequiredComplete(cdEntityId, ministryOfficerId, true).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Ministry Inspection Required task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Generate Ministry Inspection Report
        destinationInspectionBpmn.diGenerateMinistryInspectionReportComplete(cdEntityId, inspectionOfficerId).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Generate Ministry Inspection task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Review Ministry Inspection Report
//        destinationInspectionBpmn.diReviewMinistryInspectionReportComplete(cdEntityId, inspectionOfficerId).let {
//            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after Review Ministry Inspection task complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return

        //Generate Motor Inspection Report
        destinationInspectionBpmn.diGenerateMotorInspectionReportComplete(cdEntityId, inspectionOfficerInChargeId).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Generate Motor Inspection Report task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Review Motor Inspection Report Complete
        destinationInspectionBpmn.diReviewMotorInspectionReportComplete(cdEntityId, inspectionOfficerInChargeId, true).let {
            destinationInspectionBpmn.fetchTaskByObjectId(cdEntityId, diMvWithCorProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Review Motor Inspection Report task complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return
    }
}
