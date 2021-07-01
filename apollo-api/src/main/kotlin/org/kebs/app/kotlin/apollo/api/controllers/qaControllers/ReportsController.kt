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

    final val sMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapSmarkImagePath)
    val sMarkImageFile = sMarkImageResource.file.toString()

    final val fMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapFmarkImagePath)
    val fMarkImageFile = fMarkImageResource.file.toString()

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
    @RequestMapping(value = ["permit-certificate"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun dmarkPermit(
        response: HttpServletResponse,
        @RequestParam(value = "permit_id") id: Long
    ) {
        var map = hashMapOf<String, Any>()
        val appId: Int = applicationMapProperties.mapQualityAssurance
        val s = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(id)

        val foundPermitDetails = qaDaoServices.permitDetails(permit, s)

//        val standardNo = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)?.standardNumber
//        val ksApplicable = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory)?.standardTitle

//        val standardsDetails = qaDaoServices.findStandardsByID(
//            permit.productStandard
//                ?: throw Exception("INVALID STANDARDS NUMBER [ID = ${permit.productStandard}]")
//        )
//        val standardNo = standardsDetails.standardNumber
//        val ksApplicable = standardsDetails.standardTitle

        map["FirmName"] = foundPermitDetails.firmName.toString()
        map["PermitNo"] = foundPermitDetails.permitNumber.toString()
        map["PostalAddress"] = foundPermitDetails.postalAddress.toString()
        map["PhysicalAddress"] = foundPermitDetails.physicalAddress.toString()
        map["DateOfIssue"] =
            foundPermitDetails.dateOfIssue?.let { commonDaoServices.convertDateToString(it, "dd-MM-YYYY") }!!
        map["ExpiryDate"] =
            foundPermitDetails.dateOfExpiry?.let { commonDaoServices.convertDateToString(it, "dd-MM-YYYY") }!!

//        map["FirmName"] = foundPermitDetails.firmName.toString()
        map["CommodityDesc"] = foundPermitDetails.commodityDescription.toString()
        map["TradeMark"] = foundPermitDetails.brandName.toString()
        map["StandardTitle"] = "${foundPermitDetails.standardNumber}".plus(" ${foundPermitDetails.standardTitle}")

        map["faxNumber"] = foundPermitDetails.faxNo.toString()
        map["EmailAddress"] = foundPermitDetails.email.toString()
        map["phoneNumber"] = foundPermitDetails.telephoneNo.toString()


        when (foundPermitDetails.permitTypeID) {
            applicationMapProperties.mapQAPermitTypeIDDmark -> {
                map["DmarkLogo"] = dMarkImageFile
                reportsDaoService.extractReportEmptyDataSource(
                    map,
                    response,
                    applicationMapProperties.mapReportDmarkPermitReportPath
                )
            }
            applicationMapProperties.mapQAPermitTypeIdSmark -> {
                map["SmarkLogo"] = sMarkImageFile
                reportsDaoService.extractReportEmptyDataSource(
                    map,
                    response,
                    applicationMapProperties.mapReportSmarkPermitReportPath
                )
            }
            applicationMapProperties.mapQAPermitTypeIdFmark -> {
                map["FmarkLogo"] = fMarkImageFile
                reportsDaoService.extractReportEmptyDataSource(
                    map,
                    response,
                    applicationMapProperties.mapReportFmarkPermitReportPath
                )
            }
        }

    }
}