package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import com.google.gson.JsonObject
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.qa.branchUpdateDTO
import org.kebs.app.kotlin.apollo.common.dto.qa.fmarkSmarkDTO
import org.kebs.app.kotlin.apollo.store.model.SampleStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.ISampleStandardsRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@Controller
@RestController
@RequestMapping("/api/v1/migration/")
class QaFaultMitigation(
    private val commonDaoServices: CommonDaoServices,
    ) {
    @PostMapping("/qa/fixes/add/standard")
    fun updateStandard(@RequestBody sample : SampleStandardsEntity): ResponseEntity<SampleStandardsEntity> {
        return commonDaoServices.addStandard(sample)
    }
    @PostMapping("/qa/fixes/inspection-report/clean-up")
    fun removeInspectionReportDuplicates(): ResponseEntity<String>{
        val responseObject = JsonObject()
        return if (!commonDaoServices.deleteDuplicatePermits().equals("No duplicates found.")) {
            responseObject.addProperty("status",200)
            responseObject.addProperty("message","Duplicate records removed sucessfully")
            ResponseEntity.ok(responseObject.toString())
        } else {
            responseObject.addProperty("status",400)
            responseObject.addProperty("message","No Duplicates Found")
            ResponseEntity.ok(responseObject.toString())
        }
    }
    @PostMapping("/qa/fixes/tie-fmark-smark")
    fun tieFmarkToSmark(@RequestBody entity : fmarkSmarkDTO): ResponseEntity<String>{
        return commonDaoServices.tieFmarkToSmark(entity)
    }


    @PostMapping("/qa/fixes/update-invoice-status")
    fun updateInvoiceStatus(@RequestBody entity : fmarkSmarkDTO): ResponseEntity<String>{
        return commonDaoServices.updateInvoiceStatus(entity)
    }
    @PostMapping("/qa/fixes/update-user-branch")
    fun updateUserBranch(@RequestBody entity : branchUpdateDTO): ResponseEntity<String>{
        return commonDaoServices.updateUserBranch(entity)
    }
    @GetMapping("/system/admin/masters/companies/loads")
    @ResponseBody
    fun getAllCompanies(): List<CompanyProfileEntity>{
        return commonDaoServices.getAllCompanies()
    }

//    @PostMapping("/get-user-branches")
//    fun getUserBranches(@RequestBody entity: branchUpdateDTO): ResponseEntity<String>{
//        return commonDaoServices.getUserBranches(entity)
//    }

}