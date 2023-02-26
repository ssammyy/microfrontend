package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import org.joda.time.DateTime
import org.junit.Ignore
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.export.ExportFile
import org.kebs.app.kotlin.apollo.store.repo.IInvoiceRepository
import org.kebs.app.kotlin.apollo.store.repo.ISchedulerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.Test

@SpringBootTest
@RunWith(SpringRunner::class)
class MSBpmnTest {
    @Autowired
    lateinit var marketSurveillanceBpmn: MarketSurveillanceBpmn

    @Autowired
    lateinit var invoiceRepo: IInvoiceRepository

    @Autowired
    lateinit var notifications: Notifications

    @Autowired
    lateinit var exportFile: ExportFile

    @Autowired
    lateinit var bpmnCommonFunctions: BpmnCommonFunctions

    @Autowired
    lateinit var schedulerRepo: ISchedulerRepository

    @Value("\${bpmn.ms.complaints.process.definition.key}")
    lateinit var msComplaintsProcessDefinitionKey: String

    @Value("\${bpmn.ms.fuel.monitoring.process.definition.key}")
    lateinit var msFuelMonitoringProcessDefinitionKey: String

    @Value("\${bpmn.ms.market.surveillance.process.definition.key}")
    lateinit var msMarketSurveillanceProcessDefinitionKey: String

    val complaintId: Long = 22
    val fuelInspectionId:Long = 42
    val workPlanId:Long = 321
    val assigneeId:Long = 101
    val hodAssigneeId: Long = 102
    val hofAssigneeId: Long = 103
    val msioAssigneeId: Long = 104
    val officerAssigneeId:Long = 105
    val labAssigneeId:Long = 106
    val customerAssigneeId:Long = 107
    val directorId:Long = 108
    val customerEmail: String = "james.mantu@gmail.com"
    val epraEmail: String = "james.mantu@gmail.com"
    val complainantEmail: String = "james.mantu@gmail.com"

    @Test
    //@Ignore
    fun testMsComplaintProcess() {

        //Start the process
        marketSurveillanceBpmn.startMSComplaintProcess (complaintId, assigneeId,customerEmail)?.let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msComplaintsProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Submit complaint complete
        marketSurveillanceBpmn.msSubmitComplaintComplete(complaintId, hodAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msComplaintsProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after submit complaint")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Accept complaint complete
        marketSurveillanceBpmn.msAcceptComplaintComplete(complaintId, hofAssigneeId,true).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msComplaintsProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after accept complaint")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Assign MSIO complete
        marketSurveillanceBpmn.msAssignMsioComplete(complaintId, msioAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msComplaintsProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after assign msio")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        Thread.sleep(3000)
        Thread.sleep(3000)
        Thread.sleep(3000)
        Thread.sleep(3000)

        println("Finito!!")
        /*
        //MS Process complete
        marketSurveillanceBpmn.msProcessComplete(complaintId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msComplaintsProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after MS Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
        */
    }

    @Test
    @Ignore
    fun testNotifs() {
        var message= ""
        bpmnCommonFunctions.getOverdueTasks(DateTime().toDate(),"ms")?.let { tasks ->
            for (task in tasks){
                bpmnCommonFunctions.getTaskVariables(task.id)?.let{
                    message += "${task.name} assigned to ${task.assignee} due on ${task.dueDate} is still pending\n"
                }
            }
            notifications.sendEmailServiceTask(101,0,225,null,message)
        }

    }

    @Test
    @Ignore
    fun testMsFuelInspectionProcess() {
        //marketSurveillanceBpmn.fetchFuelInspectionRecords(fuelInspectionId)

        //Start the process
        marketSurveillanceBpmn.startMSFuelMonitoringProcess(fuelInspectionId, assigneeId,customerEmail,epraEmail,directorId)?.let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(fuelInspectionId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //HOD assign officer complete
        marketSurveillanceBpmn.msFmHodAssignOfficerComplete(fuelInspectionId, officerAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(fuelInspectionId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after HOD assign officer Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Fill sample collection forms complete
        marketSurveillanceBpmn.msFmFillSampleCollectionFormComplete(fuelInspectionId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(fuelInspectionId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Fill sample collection forms Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Generate and submit SSF complete
        marketSurveillanceBpmn.msFmGenSSFAndSubmitComplete(fuelInspectionId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(fuelInspectionId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Generate and submit SSF Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Update BS Number and Submit complete
        marketSurveillanceBpmn.msFmUpdateBSNumberAndSubmitComplete(fuelInspectionId, labAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(fuelInspectionId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Update BS Number and Submit Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Lab process samples complete
        marketSurveillanceBpmn.msFmLabProcessSamplesComplete(fuelInspectionId, officerAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(fuelInspectionId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Lab process samples Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        /*
        //----------------------------
        //LAB REPORT OK
        //----------------------------
        //Check lab report complete
        marketSurveillanceBpmn.msFmCheckLabReportComplete(complaintId, true).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Check lab report Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
        */

        //***************************
        //LAB REPORT NOT OK
        //***************************
        //Check lab report complete
        marketSurveillanceBpmn.msFmCheckLabReportComplete(complaintId, false).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Check lab report Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Send proforma invoice complete
        marketSurveillanceBpmn.msFmSendProformaInvoiceComplete(complaintId, customerAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Send proforma invoice Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Payment complete
        marketSurveillanceBpmn.msFmPaymentComplete(complaintId, officerAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Payment Process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Remediation complete
        marketSurveillanceBpmn.msFmRemediationComplete(complaintId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msFuelMonitoringProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after Remediation Process")
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
    fun testMsMarketSurveillanceProcess() {
        //Start the process
        marketSurveillanceBpmn.startMSMarketSurveillanceProcess(workPlanId, assigneeId, complainantEmail,directorId)?.let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }
        } ?: return

        //Generate workplan complete
        marketSurveillanceBpmn.msMsGenerateWorkplanComplete(workPlanId, hodAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Approve workplan complete
        marketSurveillanceBpmn.msMsApproveWorkplanComplete(workPlanId, true,msioAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Online activities complete
        marketSurveillanceBpmn.msMsOnsiteActivitiesComplete(workPlanId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Generate preliminary report complete
        marketSurveillanceBpmn.msGeneratePreliminaryReportComplete(workPlanId,hofAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //HOF approve preliminary report complete
        marketSurveillanceBpmn.msHOFApprovePreliminaryReportComplete(workPlanId,msioAssigneeId,hodAssigneeId,false).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return



        //Submit lab samples complete
        marketSurveillanceBpmn.msMsSubmitLabSamplesComplete(workPlanId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Update BS number complete
        marketSurveillanceBpmn.msMsUpdateBsNumberAndSubmitComplete(workPlanId,labAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return


        //Lab process samples complete
        marketSurveillanceBpmn.msMsLabProcessSamplesComplete(workPlanId,msioAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(workPlanId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return


        /*
        //HOD approve preliminary report complete
        marketSurveillanceBpmn.msHODApprovePreliminaryReportComplete(complaintId,msioAssigneeId,hofAssigneeId,true).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //Generate final report complete
        marketSurveillanceBpmn.msGenerateFinalReportComplete(complaintId,hofAssigneeId).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //HOF approve final report complete
        marketSurveillanceBpmn.msHOFApproveFinalReportComplete(complaintId,msioAssigneeId,hodAssigneeId,true).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return

        //HOD approve final report complete
        marketSurveillanceBpmn.msHODApproveFinalReportComplete(complaintId,msioAssigneeId,true).let {
            marketSurveillanceBpmn.fetchTaskByComplaintId(complaintId, msMarketSurveillanceProcessDefinitionKey)?.let { taskDetails ->
                println("Task details after process")
                for (taskDetail in taskDetails){
                    taskDetail.task.let{ task->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} " )
                    }
                }
            }
        } ?: return
        */
    }

    @Test
    @Ignore
    fun testMsFetchAllTasks() {

            marketSurveillanceBpmn.fetchAllTasks()?.let { taskDetails ->
                println("Task details after starting the process")
                for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} ")
                    }
                }
            }

    }
    @Test
    @Ignore
    fun testMsFetchAllTasksByAssignee() {

        marketSurveillanceBpmn.fetchAllTasksByAssignee(564)?.let { lstTaskDetails ->
            println("Task details after starting the process")
            lstTaskDetails.sortedBy { it.objectId }.forEach() { taskDetail ->
                //for (taskDetail in taskDetails) {
                    taskDetail.task.let { task ->
                        println("${taskDetail.objectId} -- ${task.id} -- ${task.name} -- ${task.assignee} -- ${task.processInstanceId} -- ${task.taskDefinitionKey} -- ${task.createTime}")
                    }
                //}
            }

        }

    }

    @Test
    @Ignore
    fun testEmailAttachment() {
        notifications.processEmail("james.mantu@gmail.com","Test subject","Test Message","F:\\projects\\KEBS\\KEBS Tasks.xlsx")
    }

    @Test
    @Ignore
    fun testDownloadPdf() {
        invoiceRepo.findByIdOrNull(2224)?.let{
            exportFile.parseThymeleafTemplate("templates/TestPdf/TestPdf","invoice",it)?.let { htmlString->
                exportFile.generatePdfFromHtml(htmlString)
            }
        }
    }

}
