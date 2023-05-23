package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import com.google.gson.JsonObject
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.qa.branchUpdateDTO
import org.kebs.app.kotlin.apollo.common.dto.qa.fmarkSmarkDTO
import org.kebs.app.kotlin.apollo.store.model.SampleStandardsEntity
import org.kebs.app.kotlin.apollo.store.repo.ISampleStandardsRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/migration/qa/fixes/")
class QaFaultMitigation(
    private val commonDaoServices: CommonDaoServices,
    ) {
    @PostMapping("/add/standard")
    fun updateStandard(@RequestBody sample : SampleStandardsEntity): ResponseEntity<SampleStandardsEntity> {
        return commonDaoServices.addStandard(sample)
    }
    @PostMapping("/inspection-report/clean-up")
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
    @PostMapping("/tie-fmark-smark")
    fun tieFmarkToSmark(@RequestBody entity : fmarkSmarkDTO): ResponseEntity<String>{
        return commonDaoServices.tieFmarkToSmark(entity)
    }


    @PostMapping("/update-invoice-status")
    fun updateInvoiceStatus(@RequestBody entity : fmarkSmarkDTO): ResponseEntity<String>{
        return commonDaoServices.updateInvoiceStatus(entity)
    }
    @PostMapping("/update-user-branch")
    fun updateUserBranch(@RequestBody entity : branchUpdateDTO): ResponseEntity<String>{
        return commonDaoServices.updateUserBranch(entity)
    }

}