//
///*
// *
// * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
// * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
// * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
// * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
// * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
// * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
// * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
// * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
// * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
// * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
// *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
// *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
// *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
// *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
// *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
// *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
// *
// *
// *
// *
// *
// *   Copyright (c) 2020.  BSK
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *   you may not use this file except in compliance with the License.
// *   You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// */
//
//package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn
//
//
//import org.junit.Ignore
//import org.junit.jupiter.api.Test
//import org.junit.runner.RunWith
//import org.kebs.app.kotlin.apollo.api.notifications.Notifications
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringRunner
//
//
//@SpringBootTest
//@RunWith(SpringRunner::class)
//class QABpmnTest{
//    @Autowired
//    lateinit var qualityAssuranceBpmn: QualityAssuranceBpmn
//
//    @Autowired
//    lateinit var bpmnCommonFunctions: BpmnCommonFunctions
//
//    @Autowired
//    lateinit var bpmnNotifications: BpmnNotifications
//
//    @Autowired
//    lateinit var notifications: Notifications
//
//    @Value("\${bpmn.qa.app.review.process.definition.key}")
//    lateinit var qaAppReviewProcessDefinitionKey: String
//
//    @Value("\${bpmn.qa.app.review.business.key}")
//    lateinit var qaAppReviewBusinessKey: String
//
//    @Value("\${bpmn.qa.sf.mark.inspection.process.definition.key}")
//    lateinit var qaSfMarkInspectionProcessDefinitionKey: String
//
//    @Value("\${bpmn.qa.dm.application.review.process.definition.key}")
//    lateinit var qaDmApplicationReviewProcessDefinitionKey: String
//
//    @Value("\${bpmn.qa.sf.application.payment.process.definition.key}")
//    lateinit var qaSfApplicationPaymentProcessDefinitionKey: String
//
//    @Value("\${bpmn.qa.sf.permit.award.process.definition.key}")
//    lateinit var qaSfPermitAwardProcessDefinitionKey: String
//
//    @Value("\${bpmn.qa.ii.schedule.process.definition.key}")
//    lateinit var qaIiScheduleProcessDefinitionKey: String
//
//    @Value("\${bpmn.qa.ii.reporting.process.definition.key}")
//    lateinit var qaIiReportingProcessDefinitionKey: String
//
//    @Value("\${bpmn.qa.dm.assessment.process.definition.key}")
//    lateinit var qaDmAssessmentProcessDefinitionKey: String
//
//    @Value("\${bpmn.qa.dm.app.payment.process.definition.key}")
//    lateinit var qaDmAppPaymentProcessDefinitionKey: String
//
//    @Value("\${qa.bpmn.email.default.from}")
//    lateinit var qaEmailDefaultFrom: String
//
//    @Value("\${qa.bpmn.email.default.subject}")
//    lateinit var qaEmailDefaultSubject: String
//
//    @Value("\${qa.bpmn.email.default.body}")
//    lateinit var qaEmailDefaultBody: String
//
//    val assigneeId:Long = 101
//    val qaoAssigneeId: Long = 1524
//    val directorAssigneeId: Long = 103
//    val hofAssigneeId: Long = 104
//    val custAssigneeId: Long = 105
//    val labAssigneeId:Long = 106
//    val pscAssigneeId:Long=107
//    val pcmAssigneeId:Long=1605
//    val pmAssigneeId:Long = 109
//    val assessorAssigneeId:Long = 1541
//    val pacAssigneeId:Long = 112
//    val assesorAssigneeId:Long = 1541
//    val manufacturerId:Long = 424
//    val customerEmail = "james.mantu@gmal.com"
//
//    //HOD
//    val hodAssigneeId: Long = 1523
//
//    @Test
//    @Ignore
//    fun testQaAppReviewPositive() {
//        val permitId: Long = 325
//
//        //Start the process
//        qualityAssuranceBpmn.startQAAppReviewProcess(permitId, hofAssigneeId)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        //Check if application complete
//        qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(permitId,1).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Allocate file to QAO complete
//        qualityAssuranceBpmn.qaAppReviewAllocateToQAO(permitId,qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Plan factory visit complete
//        qualityAssuranceBpmn.qaAppReviewPlanFactoryVisit(permitId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//    }
//
//    @Test
//    @Ignore
//    fun testQaAppReviewNegative() {
//        val permitId: Long = 325
//
//        //Start the process
//        qualityAssuranceBpmn.startQAAppReviewProcess(permitId, hofAssigneeId)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        //Check if application complete
//        qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(permitId, 0).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing the process")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//
//        //Correct application
//        qualityAssuranceBpmn.qaAppReviewCorrectApplication(permitId,hofAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Check if application complete
//        qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(permitId, 1).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing the process")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        //Allocate file to QAO complete
//        qualityAssuranceBpmn.qaAppReviewAllocateToQAO(permitId,qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Plan factory visit complete
//        qualityAssuranceBpmn.qaAppReviewPlanFactoryVisit(permitId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//    }
//
//    @Test
//    @Ignore
//    fun testQaSFMarkInspectionNegative() {
//        val permitId: Long = 382
//
//        //Start the process
//        qualityAssuranceBpmn.startQASFMarkInspectionProcess(permitId, qaoAssigneeId)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Fill the factory inspection forms
//        qualityAssuranceBpmn.qaSfMIFillFactoryInspectionForms(permitId, qaoAssigneeId,labAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after filling the factory inspection forms")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Generate the inspection report
//        qualityAssuranceBpmn.qaSfMIGenerateInspectionReport(permitId, hofAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generating the inspection")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Generate the supervisory scheme
//        qualityAssuranceBpmn.qaSfMIGenSupervisionSchemeComplete(permitId, qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generating the supervisory scheme")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//
//        //Accept the supervisory scheme -- false
//        qualityAssuranceBpmn.qaSfMIAcceptSupervisionScheme(permitId, qaoAssigneeId, false).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after accepting the supervisory scheme")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Generate the supervisory scheme
//        qualityAssuranceBpmn.qaSfMIGenSupervisionSchemeComplete(permitId, qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generating the supervisory scheme")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//
//        //Accept the supervisory scheme -- false
//        qualityAssuranceBpmn.qaSfMIAcceptSupervisionScheme(permitId, qaoAssigneeId, true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after accepting the supervisory scheme")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//
//        //Receive the SSF
//        qualityAssuranceBpmn.qaSfMIReceiveSSFComplete(permitId, qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after receiving the SSF")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Submit the samples
//        qualityAssuranceBpmn.qaSfMISubmitSamplesComplete(permitId, labAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after submitting the samples")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//    }
//
////    @Test
////    @Ignore
////    fun testQaDmAppReview() {
////        val permitId: Long = 263
////
////        //Start the process
////        qualityAssuranceBpmn.startQADMApplicationReviewProcess(permitId, hofAssigneeId,false)?.let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after starting the process")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        } ?: return
////
////        //Check if application complete
////        qualityAssuranceBpmn.qaDmARCheckApplicationComplete(permitId, hofAssigneeId,true).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after checking if the application is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Assign to QAO
////        qualityAssuranceBpmn.qaDmARAllocateQAOComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after assign to QAO is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Factory inspection
////        qualityAssuranceBpmn.qaDmARFactoryInspectionComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after factory inspection complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Generate inspection report complete
////        qualityAssuranceBpmn.qaDmARGenerateInspectionReportComplete(permitId, hofAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after generate inspection report complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Approve inspection report complete - positive approval
////        qualityAssuranceBpmn.qaDmARApproveInspectionReportComplete(permitId, qaoAssigneeId,true).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after generate inspection report complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Generate supervision scheme complete
////        qualityAssuranceBpmn.qaDmARGenSupervisionSchemeComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after generate supervision scheme complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Accept supervision scheme complete --negative
////        qualityAssuranceBpmn.qaDmARAcceptSupervisionScheme(permitId, hofAssigneeId,false).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after accept supervision scheme complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Generate supervision scheme complete
////        qualityAssuranceBpmn.qaDmARGenSupervisionSchemeComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after generate supervision scheme complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Accept supervision scheme complete --true
////        qualityAssuranceBpmn.qaDmARAcceptSupervisionScheme(permitId, hofAssigneeId,true).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after accept supervision scheme complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////
////        //Check test reports - positive
////        qualityAssuranceBpmn.qaDmARCheckTestReportsComplete(permitId, qaoAssigneeId,true).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after check test reports complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Check test compliant - negative
////        /*
////        qualityAssuranceBpmn.qaDmARTestReportsCompliantComplete(permitId, qaoAssigneeId,true)?.let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after check test reports complete")
////                taskDetails.tasks?.forEach{ task ->
////                    println(task.id + " -- " + task.taskDefinitionKey +
////                            " -- " + taskDetails.permitId + " -- " + task.assignee)
////                }
////            }
////        }?: return;
////        */
////
////        //Check test compliant - positive
////        qualityAssuranceBpmn.qaDmARTestReportsCompliantComplete(permitId, qaoAssigneeId,false).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after check test reports complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Fill the SSF
////        qualityAssuranceBpmn.qaDmARFillSSFComplete(permitId, labAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after fill the SSF complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Receive the SSF
////        qualityAssuranceBpmn.qaDmARReceiveSSFComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after receive the SSF complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Submit the samples
////        qualityAssuranceBpmn.qaDmARSubmitSamplesComplete(permitId, labAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after submit the samples complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Lab analysis
////        qualityAssuranceBpmn.qaDmARLabAnalysisComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after lab analysis complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Check the lab report -> positive -> lab report ok
////        qualityAssuranceBpmn.qaDmARCheckLabReportsComplete(permitId, qaoAssigneeId, labAssigneeId,true).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after check lab report complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Assign compliance status -> positive -> compliance ok
////        qualityAssuranceBpmn.qaDmARAssignComplianceStatusComplete(permitId, qaoAssigneeId, true).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after assign compliance status complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////    }
//
//    @Test
//    @Ignore
//    fun testSfAppPaymentProcess() {
//        val permitId: Long = 161
//
//        //Start the process
//        qualityAssuranceBpmn.startQASFApplicationPaymentProcess(permitId, 0)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Select the mark
//        qualityAssuranceBpmn.qaSfAPSelectMarkComplete(permitId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after selecting the mark")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//
//        //Fill and submit
//        qualityAssuranceBpmn.qaSfAPFillAndSubmitComplete(permitId, hofAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after filling and submitting the application")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Review the application
//        qualityAssuranceBpmn.qaSfAPReviewApplicationComplete(permitId, true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after reviewing the application")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Make the payment
//        qualityAssuranceBpmn.qaSfAPPaymentComplete(permitId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after making the payment")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//    }
//
//    @Test
//    @Ignore
//    fun testSfPermitAwardProcess() {
//        val permitId: Long = 161
//
//        //Start the process
//        qualityAssuranceBpmn.startQASFPermitAwardProcess(permitId, pscAssigneeId)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfPermitAwardProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//
//        //PSC Approval
//        qualityAssuranceBpmn.qaSfPAPscApprovalComplete(permitId, qaoAssigneeId, pcmAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfPermitAwardProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after psc approval")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //PCM Award
//        qualityAssuranceBpmn.qaSfPAPermitAwardComplete(permitId, qaoAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfPermitAwardProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after permit award")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//    }
//
//    @Test
//    @Ignore
//    fun testIIScheduleProcess() {
//        val objectId: Long = 2
//        val paymentAmount:Long = 100
//        //Start the process
//        qualityAssuranceBpmn.startQAIIScheduleProcess(objectId, directorAssigneeId,customerEmail)?.let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//
//        //Director review
//        qualityAssuranceBpmn.qaIISDirectorReviewComplete(objectId, pmAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing this step")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//
//        //Petroleum manager review
//        qualityAssuranceBpmn.qaIISPetroleumMangerReviewComplete(objectId).let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing this step")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //QAO allocation
//        qualityAssuranceBpmn.qaIISAllocateIOComplete(objectId,qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing this step")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Generate proforma invoice
//        qualityAssuranceBpmn.qaIISGenerateProformaInvComplete(objectId,qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing this step")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Customer payment
//        qualityAssuranceBpmn.qaIISCustomerPaymentComplete(objectId,qaoAssigneeId, paymentAmount).let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing this step")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Schedule calendar visit
//        qualityAssuranceBpmn.qaIISScheduleVisitComplete(objectId,qaoAssigneeId,pmAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after completing this step")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//    }
//
//    @Test
//    @Ignore
//    fun testIIReportingProcess() {
//        val objectId: Long = 2
//        //Start the process
//        qualityAssuranceBpmn.startQAIIReportingProcess(objectId, qaoAssigneeId,customerEmail)?.let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiReportingProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//
//        //Inspection complete
//        qualityAssuranceBpmn.qaIIRInspectionComplete(objectId, hofAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiReportingProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after inspection complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Application Review complete
//        qualityAssuranceBpmn.qaIIRApplicationReviewComplete(objectId, qaoAssigneeId,directorAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiReportingProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after application review complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//    }
//
//    @Test
//    //@Ignore
//    fun testQaDmAssessmentProcess() {
//        val permitId: Long = 923
//
//        //Start the process
//        qualityAssuranceBpmn.startQADmAssessmentProcess(permitId, qaoAssigneeId)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        //Gen justification complete
//        qualityAssuranceBpmn.qaDmasGenJustificationRptComplete(permitId, hodAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generate justification report is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasApproveJustificationRptComplete(permitId, qaoAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after approve justification report is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasAppointAssessorComplete(permitId, assessorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after appoint assessor is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaScheduleFactoryVisitComplete(permitId, assessorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after schedule factory visit is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasGenerateAssessmentRptComplete(permitId, hofAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generate assessment report is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasApproveAssessmentRptComplete(permitId, assessorAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after approve assessment report is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasAssessmentRptCompliantComplete(permitId, pacAssigneeId,false).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after assessment report compliant is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasCorrectiveActionComplete(permitId, assesorAssigneeId,manufacturerId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after corrective action is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasManufacturerCorrectionComplete(permitId, assesorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after manufacturer corrective action is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasCorrectiveActionComplete(permitId, assesorAssigneeId,manufacturerId,false).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after corrective action is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//
//        qualityAssuranceBpmn.qaScheduleFactoryVisitComplete(permitId, assessorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after schedule factory visit is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasGenerateAssessmentRptComplete(permitId, hofAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generate assessment report is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasApproveAssessmentRptComplete(permitId, assessorAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after approve assessment report is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasAssessmentRptCompliantComplete(permitId, pacAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after assessment report compliant is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasPacApprovalComplete(permitId, assessorAssigneeId,pcmAssigneeId,false).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after pac approval complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//
//        qualityAssuranceBpmn.qaDmasAssessorCorrectionComplete(permitId, pacAssigneeId,manufacturerId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after assessor correction complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasAssessorManufacturerCorrectionComplete(permitId, assessorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after assessor manufacturer correction complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasAssessorCorrectionComplete(permitId, pacAssigneeId,manufacturerId,false).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after assessor correction complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasPacApprovalComplete(permitId, assessorAssigneeId,pcmAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after pac approval complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasPcmApprovalComplete(permitId, assessorAssigneeId,true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after pcm approval complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//        qualityAssuranceBpmn.qaDmasSurveillanceWorkplanComplete(permitId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after create surveillance is complete")
//                for (taskDetail in taskDetails) {
//                    taskDetail.task.let { task ->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
//                    }
//                }
//            }
//        } ?: return
//
//
//    }
//
//
//    @Test
//    @Ignore
//    fun fetchTaskDetails() {
//        println("Fetch by process instance id positive")
//        qualityAssuranceBpmn.fetchTaskByPermitId(276,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails->
//            for (taskDetail in taskDetails){
//                taskDetail.task.let{ task->
//                    println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                }
//            }
//        }
//    }
//
//    /*
//    Test Application & Payment process for Foreign DMARK
//     */
//    @Test
//    @Ignore
//    fun testDmAppPaymentProcessForeign() {
//        val permitId: Long = 1094
//        //Start the process
//        qualityAssuranceBpmn.startQADmAppPaymentProcess(permitId, null)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Submit DMARK application
//        qualityAssuranceBpmn.qaDmSubmitApplicationComplete(permitId, false, false).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after select dmark complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Check list of tasks for candidate group
//        qualityAssuranceBpmn.getallPCMTasks().let { bpmnTaskDetails ->
//            println("Task details for PCM")
//        }
//
//        //Claim task by PCM
//        qualityAssuranceBpmn.claimPermitApplicationCheckTask(permitId, pcmAssigneeId)
//
//        //Check application complete
//        qualityAssuranceBpmn.qaDmCheckApplicationComplete(permitId, false).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after check application complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Manufacturer correct application
//        qualityAssuranceBpmn.qaDmManufacturerCorrectionComplete(permitId, pcmAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after manufacturer correct application complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Check application complete
//        qualityAssuranceBpmn.qaDmCheckApplicationComplete(permitId, true).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after check application complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Trigger Payment Received task
//        qualityAssuranceBpmn.diReceivePaymentComplete(permitId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after payment received task complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//    }
//
//    /*
//   Test Review process for Local DMARK
//    */
//    @Test
//    @Ignore
//    fun testQaDmAppReviewApplicationLocal() {
//        val permitId: Long = 1094
//
//        qualityAssuranceBpmn.startQaDMApplicationReviewProcess(permitId, hodAssigneeId)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //HOD application review complete
//        qualityAssuranceBpmn.qaDMHodReviewComplete(permitId, true, qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after checking if the application is complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Assign to QAO
//        qualityAssuranceBpmn.qaDMAssignQAOComplete(permitId, qaoAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after assign to QAO is complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Generate Justification Report
//        qualityAssuranceBpmn.qaDMGenerateJustificationReportComplete(permitId, hodAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generate justification report is complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        }?: return
//
//        //Approve Justification Report complete
//        qualityAssuranceBpmn.qaDMApproveJustificationReportComplete(permitId, true, assessorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after approve justification report complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Assign Assessor complete
//        qualityAssuranceBpmn.qaDMAssignAssessorComplete(permitId, assessorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after assign assessor complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Schedule inspection date complete
//        qualityAssuranceBpmn.qaDMScheduleInspectionComplete(permitId, assessorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after schedule inspection date complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        //Schedule inspection date complete
//        qualityAssuranceBpmn.qaDMGenerateInspectionComplete(permitId, assessorAssigneeId, hodAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generate inspection report complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//
//        /*
//        Approve Inspection Route
//         */
//        //Approve Inspection report complete
////        qualityAssuranceBpmn.qaDMApproveInspectionComplete(permitId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after approve inspection report complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        } ?: return
//
//        /*
//        Generate Scheme of supervision route
//         */
//        //Generate Scheme of supervision complete
//        qualityAssuranceBpmn.qaDMGenerateSchemeofSupervisionComplete(permitId, assessorAssigneeId).let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after generate scheme of supervision complete")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//        //Accept Scheme of supervision complete
//    }
//
//    @Test
//    @Ignore
//    fun testQaDmAppReviewApplicationForeign() {
//        val permitId: Long = 266
//
////        //Start the process -- foreign dmark = true
////        qualityAssuranceBpmn.startQADMApplicationReviewProcess(permitId, hofAssigneeId,true)?.let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after starting the process")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        } ?: return
//
////        //Check if application complete
////        qualityAssuranceBpmn.qaDmARCheckApplicationComplete(permitId, hofAssigneeId,true).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after checking if the application is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Assign to QAO
////        qualityAssuranceBpmn.qaDmARAllocateQAOComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after assign to QAO is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Generate justification report
////        qualityAssuranceBpmn.qaDmARGenerateJustificationReportComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after generate justification report is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Approve justification report -- false
////        qualityAssuranceBpmn.qaDmARApproveJustificationReportComplete(permitId, qaoAssigneeId, false).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after approve justification report is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Generate justification report
////        qualityAssuranceBpmn.qaDmARGenerateJustificationReportComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after generate justification report is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Approve justification report -- true
////        qualityAssuranceBpmn.qaDmARApproveJustificationReportComplete(permitId, qaoAssigneeId, true).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after approve justification report is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Assign assesor -- true
////        qualityAssuranceBpmn.qaDmARAssignAssessorComplete(permitId, assesorAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after assign is complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////
////        //Factory inspection
////        qualityAssuranceBpmn.qaDmARFactoryInspectionComplete(permitId, qaoAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after factory inspection complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
////
////        //Generate inspection report complete
////        qualityAssuranceBpmn.qaDmARGenerateInspectionReportComplete(permitId, hofAssigneeId).let {
////            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
////                println("Task details after generate inspection report complete")
////                for (taskDetail in taskDetails){
////                    taskDetail.task.let{ task->
////                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
////                    }
////                }
////            }
////        }?: return
//
//    }
//
//    @Test
//    @Ignore
//    fun getAllTasksByAssignee(){
//        qualityAssuranceBpmn.fetchAllTasksByAssignee(2046)?.let{ taskDetails->
//            for (taskDetail in taskDetails){
//                taskDetail.task.let{ task->
//                    println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} -- ${taskDetail.permitRefNo}" )
//                }
//            }
//        }
//    }
//
//    @Test
//    @Ignore
//    fun testMailTask(){
//        val permitId: Long = 311
//        qualityAssuranceBpmn.startQAAppReviewProcess(permitId, 424)?.let { startResult ->
//            println("test : permitId : $permitId :Successfully started qa app review process")
//            for ((k, v) in startResult) {
//                println("$k = $v")
//            }
//        }
//    }
//
//
//    /*
//    @Test
//    @Ignore
//    fun testSendEmail(){
//        val permitId: Long = 311
//        bpmnNotifications.processEmail("james.mantu@gmail.com", "Test subject", "Test message")
//
//        //Start the process
//        qualityAssuranceBpmn.startQASFPermitAwardProcess(permitId, 424)?.let {
//            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfPermitAwardProcessDefinitionKey)?.let { taskDetails ->
//                println("Task details after starting the process")
//                for (taskDetail in taskDetails){
//                    taskDetail.task.let{ task->
//                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
//                    }
//                }
//            }
//        } ?: return
//    }
//    */
//
//}
