
/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn


import org.flowable.engine.TaskService
import org.junit.Ignore
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.store.repo.IManufacturerRepository
import org.kebs.app.kotlin.apollo.store.repo.IPermitApplicationRemarksRepository
import org.kebs.app.kotlin.apollo.store.repo.IPermitRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.Test


@SpringBootTest
@RunWith(SpringRunner::class)
class QABpmnTest{
    @Autowired
    lateinit var qualityAssuranceBpmn: QualityAssuranceBpmn

    @Autowired
    lateinit var bpmnCommonFunctions: BpmnCommonFunctions

    @Autowired
    lateinit var bpmnNotifications: BpmnNotifications

    @Autowired
    lateinit var notifications: Notifications

    @Value("\${bpmn.qa.app.review.process.definition.key}")
    lateinit var qaAppReviewProcessDefinitionKey: String

    @Value("\${bpmn.qa.app.review.business.key}")
    lateinit var qaAppReviewBusinessKey: String

    @Value("\${bpmn.qa.sf.mark.inspection.process.definition.key}")
    lateinit var qaSfMarkInspectionProcessDefinitionKey: String

    @Value("\${bpmn.qa.dm.application.review.process.definition.key}")
    lateinit var qaDmApplicationReviewProcessDefinitionKey: String

    @Value("\${bpmn.qa.sf.application.payment.process.definition.key}")
    lateinit var qaSfApplicationPaymentProcessDefinitionKey: String

    @Value("\${bpmn.qa.sf.permit.award.process.definition.key}")
    lateinit var qaSfPermitAwardProcessDefinitionKey: String

    @Value("\${bpmn.qa.ii.schedule.process.definition.key}")
    lateinit var qaIiScheduleProcessDefinitionKey: String

    @Value("\${bpmn.qa.ii.reporting.process.definition.key}")
    lateinit var qaIiReportingProcessDefinitionKey: String

    @Value("\${bpmn.qa.dm.assessment.process.definition.key}")
    lateinit var qaDmAssessmentProcessDefinitionKey: String

    @Value("\${bpmn.qa.dm.app.payment.process.definition.key}")
    lateinit var qaDmAppPaymentProcessDefinitionKey: String

    @Value("\${qa.bpmn.email.default.from}")
    lateinit var qaEmailDefaultFrom: String

    @Value("\${qa.bpmn.email.default.subject}")
    lateinit var qaEmailDefaultSubject: String

    @Value("\${qa.bpmn.email.default.body}")
    lateinit var qaEmailDefaultBody: String



    val assigneeId:Long = 101
    val qaoAssigneeId: Long = 102
    val directorAssigneeId: Long = 103
    val hofAssigneeId: Long = 104
    val custAssigneeId: Long = 105
    val labAssigneeId:Long = 106
    val pscAssigneeId:Long=107
    val pcmAssigneeId:Long=108
    val pmAssigneeId:Long = 109
    val hodAssigneeId:Long = 110
    val assessorAssigneeId:Long = 111
    val pacAssigneeId:Long = 112
    val customerEmail = "james.mantu@gmal.com"

    /*
    @Test
    @Ignore
    fun testPositiveFlowApplicationIncomplete() {
        val permitId: Long = 311
        //Update the permit ID and assignee ID accordingly
        println("test : permitId : $permitId : Running positive flow")
        println("test : permitId : $permitId :Start qa app review process with permitID=$permitId and assigneeId=$assigneeId")
        qualityAssuranceBpmn.startQAAppReviewProcess(permitId, assigneeId)?.let { startResult ->
            println("test : permitId : $permitId :Successfully started qa app review process")
            /*
            for ((k, v) in startResult) {
                println("$k = $v")
            }
             */
            //Thread.sleep(2000)
            //Process flow successfully started - confirm by fetching tasks
            println("test : permitId : $permitId :Fetch all tasks for the for the assigned user - $assigneeId")
            //Key item here is the task definition key
            qualityAssuranceBpmn.fetchQaAppReviewTasks(assigneeId)?.let { taskDetails ->
                    for (taskDetail in taskDetails){
                        taskDetail.task.let{ task->
                            println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                        }
                    }
                }
                //Reconfirm by fetching the specific task for using the permit Id
                println("test : permitId : $permitId : Fetch the specific task for this permit")
                qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                    for (taskDetail in taskDetails){
                        taskDetail.task.let{ task->
                            println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                        }
                    }
                    //Now we check if the application is complete
                    println("test : permitId : $permitId : Checking if application is complete -- Mark as incomplete")
                    qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(permitId,0,assigneeId)?.let{
                        //Reconfirm by fetching the specific task for using the permit Id
                        println("test : permitId : $permitId : Fetch the specific task for this permit")
                        qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                            for (taskDetail in taskDetails){
                                taskDetail.task.let{ task->
                                    println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                                }
                            }
                        }.let{
                            println("test : permitId : $permitId : Manufacturer correcting the application")
                            qualityAssuranceBpmn.qaAppReviewCorrectApplication(permitId).let {
                                qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                                    for (taskDetail in taskDetails){
                                        taskDetail.task.let{ task->
                                            println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                                        }
                                    }
                                }.let {
                                    //Finally we resubmit the application
                                    println("test : permitId : $permitId : Manufacturer resubmitting the application")
                                    qualityAssuranceBpmn.qaAppReviewResubmitApplication(permitId).let {
                                        println("test : permitId : $permitId : Manufacturer has successfully resubmitted the application")
                                        return
                                    }
                                    //println("test : permitId : $permitId : Unable to resubmit the application")
                                }
                            }
                            //println("test : permitId : $permitId : Unable to correct the application")
                        }
                    }
                    println("test : permitId : $permitId : Unable to check if the application is complete")
                }
                println("test : permitId : $permitId : No task found for this permit")
        }
        println("test : permitId : $permitId :Unable to start qa app review process flow")
    }

    @Test
    @Ignore
    fun testPositiveFlowApplicationComplete() {
        //Update the permit ID and assignee ID accordingly
        val permitId: Long = 311
        println("test : permitId : $permitId : Running positive flow")
        println("test : permitId : $permitId :Start qa app review process with permitID=$permitId and assigneeId=$assigneeId")
        qualityAssuranceBpmn.startQAAppReviewProcess(permitId, assigneeId)?.let { startResult ->
            println("test : permitId : $permitId :Successfully started qa app review process")
            for ((k, v) in startResult) {
                println("$k = $v")
            }
            Thread.sleep(2000)
            //Process flow successfully started - confirm by fetching tasks
            println("test : permitId : $permitId :Fetch all tasks for the for the assigned user - $assigneeId")
            //Key item here is the task definition key
            qualityAssuranceBpmn.fetchQaAppReviewTasks(assigneeId)?.let { taskDetails ->
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
            //Reconfirm by fetching the specific task for using the permit Id
            println("test : permitId : $permitId : Fetch the specific task for this permit")
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
                //Now we check if the application is complete
                println("test : permitId : $permitId : Checking if application is complete -- Mark as complete")
                qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(permitId,1,assigneeId)?.let{
                    println("test : permitId : $permitId : Application marked as complete")
                    //Reconfirm by fetching the specific task for using the permit Id
                    qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                        for (taskDetail in taskDetails){
                            taskDetail.task.let{ task->
                                println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                            }
                        }
                    }.let{
                        println("test : permitId : $permitId : Allocating file to QAO")
                        qualityAssuranceBpmn.qaAppReviewAllocateToQAO(permitId, qaoAssigneeId).let {
                            println("test : permitId : $permitId : File allocated to QAO")
                            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                                for (taskDetail in taskDetails){
                                    taskDetail.task.let{ task->
                                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                                    }
                                }
                            }.let {
                                //Finally we plan the factory visit
                                println("test : permitId : $permitId : Factory visit planning")
                                qualityAssuranceBpmn.qaAppReviewPlanFactoryVisit(permitId).let {
                                    println("test : permitId : $permitId : Factory visit planned")
                                    return
                                }
                                //println("test : permitId : $permitId : Unable to plan the factory visit")
                            }
                        }
                        //println("test : permitId : $permitId : Unable to allocate the file to QAO")
                    }
                }
                println("test : permitId : $permitId : Unable to check if the application is complete")
            }
            println("test : permitId : $permitId : No task found for this permit")
        }
        println("test : permitId : $permitId :Unable to start qa app review process flow")
    }
    */

    @Test
    @Ignore
    fun testQaAppReview() {
        val permitId: Long = 161

        //Start the process
        qualityAssuranceBpmn.startQAAppReviewProcess(permitId, hofAssigneeId)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Check if application complete
        qualityAssuranceBpmn.qaAppReviewCheckIfApplicationComplete(permitId,0).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        /*
        //Positive flow
        //Allocate to QAO
        qualityAssuranceBpmn.qaAppReviewAllocateToQAO(permitId,qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Plan factory visit
        qualityAssuranceBpmn.qaAppReviewPlanFactoryVisit(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return
        */
        //Negative flow
        //Correct application
        qualityAssuranceBpmn.qaAppReviewCorrectApplication(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return
        //Resubmit application
        qualityAssuranceBpmn.qaAppReviewResubmitApplication(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaAppReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

    }
    @Test
    @Ignore
    fun testQaSFMarkInspection() {
        val permitId: Long = 161

        //Start the process
        qualityAssuranceBpmn.startQASFMarkInspectionProcess(permitId, qaoAssigneeId)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Generate the supervisory scheme
        qualityAssuranceBpmn.qaSfMIGenSupervisionSchemeComplete(permitId, qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after generating the supervisory scheme")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Generate the supervisory scheme
        qualityAssuranceBpmn.qaSfMIGenSupervisionSchemeComplete(permitId, qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after generating the supervisory scheme")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Accept the supervisory scheme
        qualityAssuranceBpmn.qaSfMIAcceptSupervisionScheme(permitId, qaoAssigneeId, true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after accepting the supervisory scheme")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Fill the factory inspection forms
        qualityAssuranceBpmn.qaSfMIFillFactoryInspectionForms(permitId, qaoAssigneeId,labAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after filling the factory inspection forms")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return


        //Generate the inspection report
        qualityAssuranceBpmn.qaSfMIGenerateInspectionReport(permitId, hofAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after generating the inspection")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Receive the SSF
        qualityAssuranceBpmn.qaSfMIReceiveSSFComplete(permitId, qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after receiving the SSF")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Submit the samples
        qualityAssuranceBpmn.qaSfMISubmitSamplesComplete(permitId, labAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after submitting the samples")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return
        /*
        //Analyze the samples
        qualityAssuranceBpmn.qaSfMIAnalyzeSamplesComplete(permitId, qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after analyzing the samples")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Check the lab report - Lab report ok
        qualityAssuranceBpmn.qaSfMICheckLabReportComplete(permitId, qaoAssigneeId, labAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after checking the lab report")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Assign compliance status - Compliance status ok
        qualityAssuranceBpmn.qaSfMIAssignComplianceStatusComplete(permitId, hofAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after checking the compliance status")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Approve permit recommendation - Permit recommendation ok
        qualityAssuranceBpmn.qaSfMIApprovePermitRecommendationComplete(permitId, qaoAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after the permit recommendation")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return
        */
    }

    @Test
    @Ignore
    fun testQaDmAppReview() {
        val permitId: Long = 161

        //Start the process
        qualityAssuranceBpmn.startQADMApplicationReviewProcess(permitId, hofAssigneeId)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Check if application complete
        qualityAssuranceBpmn.qaDmARCheckApplicationComplete(permitId, hofAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after checking if the application is complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Manufacturer corrects the application - this will apply where application incomplete
        /*
        qualityAssuranceBpmn.qaDmARManufacturerCorrectionComplete(permitId)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after manufacturer corrects the application")
                taskDetails.tasks?.forEach{ task ->
                    println(task.id + " -- " + task.taskDefinitionKey +
                            " -- " + taskDetails.permitId + " -- " + task.assignee)
                }
            }
        }?: return;
         */

        //Assign to QAO
        qualityAssuranceBpmn.qaDmARAllocateToQAOComplete(permitId, qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after assign to QAO is complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Factory inspection
        qualityAssuranceBpmn.qaDmARFactoryInspectionComplete(permitId, qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after factory inspection complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Generate inspection report complete
        qualityAssuranceBpmn.qaDmARGenerateInspectionReportComplete(permitId, hofAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after generate inspection report complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        /*
        //Approve inspection report complete - negative approval
        qualityAssuranceBpmn.qaDmARApproveInspectionReportComplete(permitId, qaoAssigneeId,false)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after generate inspection report complete")
                taskDetails.tasks?.forEach{ task ->
                    println(task.id + " -- " + task.taskDefinitionKey +
                            " -- " + taskDetails.permitId + " -- " + task.assignee)
                }
            }
        }?: return;
        */
        //Approve inspection report complete - positive approval
        qualityAssuranceBpmn.qaDmARApproveInspectionReportComplete(permitId, qaoAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after generate inspection report complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Check test reports - positive
        qualityAssuranceBpmn.qaDmARCheckTestReportsComplete(permitId, qaoAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check test reports complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Check test compliant - negative
        /*
        qualityAssuranceBpmn.qaDmARTestReportsCompliantComplete(permitId, qaoAssigneeId,true)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check test reports complete")
                taskDetails.tasks?.forEach{ task ->
                    println(task.id + " -- " + task.taskDefinitionKey +
                            " -- " + taskDetails.permitId + " -- " + task.assignee)
                }
            }
        }?: return;
        */

        //Check test compliant - positive
        qualityAssuranceBpmn.qaDmARTestReportsCompliantComplete(permitId, qaoAssigneeId,false).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check test reports complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Fill the SSF
        qualityAssuranceBpmn.qaDmARFillSSFComplete(permitId, labAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after fill the SSF complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Receive the SSF
        qualityAssuranceBpmn.qaDmARReceiveSSFComplete(permitId, qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after receive the SSF complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Submit the samples
        qualityAssuranceBpmn.qaDmARSubmitSamplesComplete(permitId, labAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after submit the samples complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Lab analysis
        qualityAssuranceBpmn.qaDmARLabAnalysisComplete(permitId, qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after lab analysis complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Check the lab report -> positive -> lab report ok
        qualityAssuranceBpmn.qaDmARCheckLabReportsComplete(permitId, qaoAssigneeId, labAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check lab report complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return

        //Assign compliance status -> positive -> compliance ok
        qualityAssuranceBpmn.qaDmARAssignComplianceStatusComplete(permitId, qaoAssigneeId, true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId,qaDmApplicationReviewProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after assign compliance status complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        }?: return
    }

    @Test
    @Ignore
    fun testSfAppPaymentProcess() {
        val permitId: Long = 161

        //Start the process
        qualityAssuranceBpmn.startQASFApplicationPaymentProcess(permitId, 0)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Select the mark
        qualityAssuranceBpmn.qaSfAPSelectMarkComplete(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after selecting the mark")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return


        //Fill and submit
        qualityAssuranceBpmn.qaSfAPFillAndSubmitComplete(permitId, hofAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after filling and submitting the application")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Review the application
        qualityAssuranceBpmn.qaSfAPReviewApplicationComplete(permitId, true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after reviewing the application")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Make the payment
        qualityAssuranceBpmn.qaSfAPPaymentComplete(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfApplicationPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after making the payment")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
    }

    @Test
    @Ignore
    fun testSfPermitAwardProcess() {
        val permitId: Long = 161

        //Start the process
        qualityAssuranceBpmn.startQASFPermitAwardProcess(permitId, pscAssigneeId)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfPermitAwardProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return


        //PSC Approval
        qualityAssuranceBpmn.qaSfPAPscApprovalComplete(permitId, qaoAssigneeId, pcmAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfPermitAwardProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after psc approval")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //PCM Award
        qualityAssuranceBpmn.qaSfPAPermitAwardComplete(permitId, qaoAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfPermitAwardProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after permit award")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
    }

    @Test
    @Ignore
    fun testIIScheduleProcess() {
        val objectId: Long = 2
        val paymentAmount:Long = 100
        //Start the process
        qualityAssuranceBpmn.startQAIIScheduleProcess(objectId, directorAssigneeId,customerEmail)?.let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return


        //Director review
        qualityAssuranceBpmn.qaIISDirectorReviewComplete(objectId, pmAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing this step")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return


        //Petroleum manager review
        qualityAssuranceBpmn.qaIISPetroleumMangerReviewComplete(objectId).let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing this step")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //QAO allocation
        qualityAssuranceBpmn.qaIISAllocateIOComplete(objectId,qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing this step")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Generate proforma invoice
        qualityAssuranceBpmn.qaIISGenerateProformaInvComplete(objectId,qaoAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing this step")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Customer payment
        qualityAssuranceBpmn.qaIISCustomerPaymentComplete(objectId,qaoAssigneeId, paymentAmount).let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing this step")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Schedule calendar visit
        qualityAssuranceBpmn.qaIISScheduleVisitComplete(objectId,qaoAssigneeId,pmAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiScheduleProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after completing this step")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
    }

    @Test
    @Ignore
    fun testIIReportingProcess() {
        val objectId: Long = 2
        //Start the process
        qualityAssuranceBpmn.startQAIIReportingProcess(objectId, qaoAssigneeId,customerEmail)?.let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiReportingProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return


        //Inspection complete
        qualityAssuranceBpmn.qaIIRInspectionComplete(objectId, hofAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiReportingProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after inspection complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Application Review complete
        qualityAssuranceBpmn.qaIIRApplicationReviewComplete(objectId, qaoAssigneeId,directorAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByInstallationId(objectId, qaIiReportingProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after application review complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
    }

    @Test
    @Ignore
    fun testQaDmAssessmentProcess() {
        val permitId: Long = 161

        //Start the process
        qualityAssuranceBpmn.startQADmAssessmentProcess(permitId, qaoAssigneeId)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Gen justification complete
        qualityAssuranceBpmn.qaDmasGenJustificationRptComplete(permitId, hodAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after generate justification report is complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmasApproveJustificationRptComplete(permitId, qaoAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after approve justification report is complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmasAppointAssessorComplete(permitId, assessorAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after appoint assessor is complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaScheduleFactoryVisitComplete(permitId, assessorAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after schedule factory visit is complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmasGenerateAssessmentRptComplete(permitId, hofAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after generate assessment report is complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmasApproveAssessmentRptComplete(permitId, assessorAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after approve assessment report is complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmasAssessmentRptCompliantComplete(permitId, pacAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after assessment report compliant is complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmasPacApprovalComplete(permitId, assessorAssigneeId,qaoAssigneeId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after pac approval complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmasSurveillanceWorkplanComplete(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAssessmentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after create surveillance is complete")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return


    }


    @Test
    @Ignore
    fun fetchTaskDetails() {
        println("Fetch by process instance id positive")
        qualityAssuranceBpmn.fetchTaskByPermitId(276,qaSfMarkInspectionProcessDefinitionKey)?.let { taskDetails->
            for (taskDetail in taskDetails){
                taskDetail.task.let{ task->
                    println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                }
            }
        }
    }

    /*
    @Test
    @Ignore
    fun testMultipleTaskFetch() {
        println("Starting processes")
        qualityAssuranceBpmn.startQAAppReviewProcess(311, 100)
        qualityAssuranceBpmn.startQAAppReviewProcess(312, 100)
        qualityAssuranceBpmn.startQAAppReviewProcess(318, 100)
        qualityAssuranceBpmn.startQAAppReviewProcess(319, 100)
        qualityAssuranceBpmn.startQAAppReviewProcess(319, 100)
        println("Fetching tasks")
        qualityAssuranceBpmn.fetchQaAppReviewTasks(100)?.let { lstTaskDetails->
            for (taskDetails in lstTaskDetails){
                taskDetails.task.let{task->
                    println("${taskDetails.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                }
            }
        }
    }
    */
    @Test
    @Ignore
    fun testDmAppPaymentProcessForeign() {
        val permitId: Long = 283
        //Start the process
        qualityAssuranceBpmn.startQADmAppPaymentProcess(permitId, qaoAssigneeId)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Select Dmark complete
        qualityAssuranceBpmn.qaDmappSelectDmarkComplete(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after select dmark complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Check domestic complete
        qualityAssuranceBpmn.qaDmappCheckDomesticComplete(permitId, false).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check domestic complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Fill foreign application complete
        qualityAssuranceBpmn.qaDmappFillForeignAppComplete(permitId,pcmAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after fill foreign application complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Check application complete -- false
        qualityAssuranceBpmn.qaDmappCheckAppCompleteForeign(permitId,false).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check foreign application complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Manufacturer correct foreign application
        qualityAssuranceBpmn.qaDmappManufacturerCorrectionCompleteForeign(permitId,pcmAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after correct foreign application complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Check application complete -- true
        qualityAssuranceBpmn.qaDmappCheckAppCompleteForeign(permitId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check foreign application complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmappPaymentComplete(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after payment complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
    }


    @Test
    //@Ignore
    fun testDmAppPaymentProcessLocal() {
        val permitId: Long = 285
        //Start the process
        qualityAssuranceBpmn.startQADmAppPaymentProcess(permitId, qaoAssigneeId)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Select Dmark complete
        qualityAssuranceBpmn.qaDmappSelectDmarkComplete(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after select dmark complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Check domestic complete
        qualityAssuranceBpmn.qaDmappCheckDomesticComplete(permitId, true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check domestic complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Fill domestic application complete
        qualityAssuranceBpmn.qaDmappFillDomesticAppComplete(permitId,pcmAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after fill domestic application complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Check application complete -- false
        qualityAssuranceBpmn.qaDmappCheckAppCompleteDomestic(permitId,false).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check domestic application complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Manufacturer correct domestic application
        qualityAssuranceBpmn.qaDmappManufacturerCorrectionCompleteDomestic(permitId,pcmAssigneeId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after correct domestic application complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Check application complete -- true
        qualityAssuranceBpmn.qaDmappCheckAppCompleteDomestic(permitId,true).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after check domestic application complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        qualityAssuranceBpmn.qaDmappPaymentComplete(permitId).let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaDmAppPaymentProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after payment complete")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
    }

    @Test
    @Ignore
    fun getAllTasksByAssignee(){
        qualityAssuranceBpmn.fetchAllTasksByAssignee(419)?.let{ taskDetails->
            for (taskDetail in taskDetails){
                taskDetail.task.let{ task->
                    println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                }
            }
        }
    }

    @Test
    @Ignore
    fun testMailTask(){
        val permitId: Long = 311
        qualityAssuranceBpmn.startQAAppReviewProcess(permitId, 424)?.let { startResult ->
            println("test : permitId : $permitId :Successfully started qa app review process")
            for ((k, v) in startResult) {
                println("$k = $v")
            }
        }
    }


    /*
    @Test
    @Ignore
    fun testSendEmail(){
        val permitId: Long = 311
        bpmnNotifications.processEmail("james.mantu@gmail.com", "Test subject", "Test message")

        //Start the process
        qualityAssuranceBpmn.startQASFPermitAwardProcess(permitId, 424)?.let {
            qualityAssuranceBpmn.fetchTaskByPermitId(permitId, qaSfPermitAwardProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.permitId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
    }
    */

}
