package com.apollo.standardsdevelopment.controllers

import com.apollo.standardsdevelopment.dto.InfoCheckHandler
import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.dto.TaskIdManager
import com.apollo.standardsdevelopment.models.DepartmentResponse
import com.apollo.standardsdevelopment.models.InformationTracker
import com.apollo.standardsdevelopment.models.NationalEnquiryPoint
import com.apollo.standardsdevelopment.services.NationalEnquiryPointService
import org.springframework.web.bind.annotation.*


    @RestController
    @CrossOrigin(origins = ["http://localhost:4200"])
    @RequestMapping("/National_enquiry_point")
class NationalEnquiryPointController(val nationalEnquiryPointService: NationalEnquiryPointService) {
    
    //********************************************************** deployment endpoints **********************************************************

    @PostMapping("/deploy")
    fun deployWorkflow(){
        nationalEnquiryPointService.deployProcessDefinition()
    }

    //********************************************************** process endpoints **********************************************************

    @PostMapping("/notification_request")
    fun notificationRequest(@RequestBody nationalEnquiryPoint: NationalEnquiryPoint): ProcessInstanceResponse? {
        return nationalEnquiryPoint?.let { nationalEnquiryPointService.notificationRequest(it) }
    }

    //Retrieves requests made by users on the front end
        @GetMapping("/nep_officer/tasks")
        fun getTasks() : List<TaskDetails> {
        return nationalEnquiryPointService.getManagerTasks() as List<TaskDetails>
        }

        @PostMapping("/nep_officer/is_available")
        fun approveTask(@RequestBody infoCheckHandler: InfoCheckHandler) {
            nationalEnquiryPointService.informationAvailable(infoCheckHandler.taskId, infoCheckHandler.isAvailable)
        }

        @PostMapping("/user/submit_enquiry")
        fun submitEnquiry(@RequestBody taskIdManager: TaskIdManager) {
            nationalEnquiryPointService.closeTask(taskIdManager.taskId)
        }

        @GetMapping("/process/{processId}")
        fun checkState(@PathVariable("processId") processId: String?) {
            nationalEnquiryPointService.checkProcessHistory(processId)
        }

        @PostMapping("information_available/send_email")
        fun sendEmailInfoAvailable(@RequestBody informationTracker: InformationTracker){
            nationalEnquiryPointService.sendEmailInfoAvailable(informationTracker, informationTracker.taskId)
        }
        @GetMapping("/division/tasks")
        fun getDivisionTasks() : List<TaskDetails> {
            return nationalEnquiryPointService.getDepartmentTasks() as List<TaskDetails>
        }
        @PostMapping("division_response/send_response")
        fun divisionResponse(@RequestBody departmentResponse: DepartmentResponse){
            nationalEnquiryPointService.departmentOrganizationResponse(departmentResponse, departmentResponse.taskId)
        }
}
