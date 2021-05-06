package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.ISampleStandardsRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/qa/report/")
class ReportsController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val qaDaoServices: QADaoServices,
    private val reportsDaoService: ReportsDaoService,
    private val commonDaoServices: CommonDaoServices,
    private val resourceLoader: ResourceLoader,
    private val sampleStandardsRepository: ISampleStandardsRepository
) {

    final val dMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapDmarkImagePath)
    val dMarkImageFile = dMarkImageResource.file.toString()

    @RequestMapping(value = ["pac_summary"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun pacSummary(
        response: HttpServletResponse,
        @RequestParam(value = "permit_id") id: Long
    ) {
        var map = hashMapOf<String, Any>()
        val permit = qaDaoServices.findPermitBYID(id)

        map["FirmName"] = "Test"
        map["Product"] = "Test"
        map["ApplicationDate"] = "Test"
        map["ApplicationType"] = "Test"
        map["AssessmentDate"] = "Test"
        map["Assessors"] = "Test"

        reportsDaoService.extractReportEmptyDataSource(map, response, applicationMapProperties.mapReportPacSummaryReportPath)
    }

    /*
    DMARK Permit Report
     */
    @RequestMapping(value = ["dmark_permit"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun dmarkPermit(
        response: HttpServletResponse,
        @RequestParam(value = "permit_id") id: Long
    ) {
        var map = hashMapOf<String, Any>()
        val permit = qaDaoServices.findPermitBYID(id)

        val standardNo = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)?.standardNumber
        val ksApplicable = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)?.standardTitle

        map["FirmName"] = permit.firmName.toString()
        map["PermitNo"] = permit.permitNumber.toString()
        map["PostalAddress"] = permit.postalAddress.toString()
        map["PhysicalAddress"] = permit.physicalAddress.toString()
        map["DateOfIssue"] = permit.dateOfIssue?.let { commonDaoServices.convertDateToString(it, "dd-MM-YYYY") }!!
        map["ExpiryDate"] = permit.dateOfExpiry?.let { commonDaoServices.convertDateToString(it, "dd-MM-YYYY") }!!
        map["DmarkLogo"] = dMarkImageFile
        map["FirmName"] = permit.firmName.toString()
        map["CommodityDesc"] = permit.commodityDescription.toString()
        map["TradeMark"] = permit.tradeMark.toString()
        map["StandardTitle"] = "$standardNo".plus(" $ksApplicable")

        reportsDaoService.extractReportEmptyDataSource(map, response, applicationMapProperties.mapReportDmarkPermitReportPath)
    }
}