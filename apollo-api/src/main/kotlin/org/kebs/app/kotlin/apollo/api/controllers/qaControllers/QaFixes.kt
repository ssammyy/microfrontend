package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import com.google.gson.JsonObject
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.qa.fmarkSmarkDTO
import org.kebs.app.kotlin.apollo.store.model.SampleStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSmarkFmarkEntity
import org.kebs.app.kotlin.apollo.store.repo.ISampleStandardsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter

@Controller
@RequestMapping("/api/v1/migration/qa/fixes/")
class QaFixes(
    private val sampleStandardsRepo: ISampleStandardsRepository,
    private val commonDaoServices: CommonDaoServices,
    ) {
    @PostMapping("/add/standard")
    fun updateStandard(@RequestBody sample : SampleStandardsEntity): ResponseEntity<SampleStandardsEntity> {
        val sampleWithCreatedBy = SampleStandardsEntity()
        sampleWithCreatedBy.createdBy="admin"
        sampleWithCreatedBy.status=1
        sampleWithCreatedBy.createdOn= Timestamp(System.currentTimeMillis())
        sampleWithCreatedBy.modifiedOn= Timestamp(System.currentTimeMillis())
        sampleWithCreatedBy.modifiedBy= "admin"
        sampleWithCreatedBy.standardNumber=sample.standardNumber
        sampleWithCreatedBy.noOfPages=4
        sampleWithCreatedBy.standardTitle=sample.standardTitle
        val savedEntity = sampleStandardsRepo.save(sampleWithCreatedBy)
        return ResponseEntity.ok(savedEntity)
    }
    @PostMapping("/inspection-report/clean-up")
    fun removeInspectionReportDuplicates(): ResponseEntity<String>{
        val responseObject = JsonObject()
        if (!commonDaoServices.deleteDuplicatePermits().equals("No duplicates found.")) {
            responseObject.addProperty("status",200)
            responseObject.addProperty("message","Duplicate records removed sucessfully")
            return ResponseEntity.ok(responseObject.toString())
        }
        else {
            responseObject.addProperty("status",400)
            responseObject.addProperty("message","No Duplicates Found")
            return ResponseEntity.ok(responseObject.toString())
        }
    }
    @PostMapping("/tie-fmark-smark")
    fun tieFmarkToSmark(@RequestBody entity : fmarkSmarkDTO): ResponseEntity<String>{
        return commonDaoServices.tieFmarkToSmark(entity)
    }

}