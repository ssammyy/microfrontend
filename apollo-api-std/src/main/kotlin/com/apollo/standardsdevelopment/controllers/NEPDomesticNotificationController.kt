package com.apollo.standardsdevelopment.controllers

import com.apollo.standardsdevelopment.dto.InfoCheckHandler
import com.apollo.standardsdevelopment.dto.ManagerApprove
import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.InformationTracker
import com.apollo.standardsdevelopment.models.NEPNotification
import com.apollo.standardsdevelopment.models.NationalEnquiryPoint
import com.apollo.standardsdevelopment.models.NotificationReceived
import com.apollo.standardsdevelopment.services.NEPDomesticNotificationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/Domestic_notification")
class NEPDomesticNotificationController(val nepDomesticNotificationService: NEPDomesticNotificationService) {

    //********************************************************** deployment endpoints **********************************************************

    @PostMapping("/deploy")
    fun deployWorkflow(){
        nepDomesticNotificationService.deployProcessDefinition()
    }

    @PostMapping("/notification_received")
    fun notificationRequest(@RequestBody notificationReceived : NotificationReceived): ProcessInstanceResponse? {
        return notificationReceived?.let { nepDomesticNotificationService.notificationReceivedfun(it) }
    }

    @GetMapping("/nep_officer/tasks")
    fun getTasks() : List<TaskDetails> {
        return nepDomesticNotificationService.getManagerTasks() as List<TaskDetails>
    }

    @PostMapping("/nep_officer/is_accepted")
    fun approveTask(@RequestBody infoCheckHandler: InfoCheckHandler) {
        nepDomesticNotificationService.notificationAccepted(infoCheckHandler.taskId, infoCheckHandler.isAvailable)
    }



    @GetMapping("/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        nepDomesticNotificationService.checkProcessHistory(processId)
    }

    @PostMapping("/nep_officer/draft_notification")
    fun draftNotification(@RequestBody nepNotification: NEPNotification){
        nepDomesticNotificationService.draftNotification(nepNotification, nepNotification.taskID)
    }

    @GetMapping("/manager/tasks")
    fun getTask() : List<TaskDetails> {
        return nepDomesticNotificationService.getManagersTasks() as List<TaskDetails>
    }

    @PostMapping("/manager/is_accepted")
    fun approveReject(@RequestBody infoCheckHandler: InfoCheckHandler) {
        nepDomesticNotificationService.managerAcceptReject(infoCheckHandler.taskId, infoCheckHandler.isAvailable)
    }

    @PostMapping("/nep_officer/upload_final")
    fun uploadWtoNss(@RequestBody infoCheckHandler: InfoCheckHandler) {
        nepDomesticNotificationService.uploadToWtoNss(infoCheckHandler.taskId)
    }

}
