package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import com.google.gson.Gson
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.common.dto.qa.PermitDetailsDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.InvoiceEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaInvoiceMasterDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.ISampleStandardsRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/qa/report/")
//@RequestMapping("/api/v1/migration/qa/report")
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

    final val dMarkImageBackGroundResource =
        resourceLoader.getResource(applicationMapProperties.mapDmarkBackgroundImagePath)
    val dMarkImageBackGroundFile = dMarkImageBackGroundResource.file.toString()

    final val sMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapSmarkImagePath)
    val sMarkImageFile = sMarkImageResource.file.toString()

    final val sMarkImageBackGroundResource =
        resourceLoader.getResource(applicationMapProperties.mapSmarkBackgroundImagePath)
    val sMarkImageBackGroundFile = sMarkImageBackGroundResource.file.toString()

    final val fMarkImageResource = resourceLoader.getResource(applicationMapProperties.mapFmarkImagePath)
    val fMarkImageFile = fMarkImageResource.file.toString()

    final val fMarkImageBackGroundResource =
        resourceLoader.getResource(applicationMapProperties.mapFmarkBackgroundImagePath)
    val fMarkImageBackGroundFile = fMarkImageBackGroundResource.file.toString()

    @GetMapping("view/remarks/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun userRemarks(
        @PathVariable(value = "id", required = false) id: Long
    ): String? {
        val remarks = qaDaoServices.findRemarksDetailsByID(id)
        val gson = Gson()
        return gson.toJson(remarks)

    }

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

        reportsDaoService.extractReportEmptyDataSource(
            map,
            response,
            applicationMapProperties.mapReportPacSummaryReportPath
        )
    }


    /*
   GetDemand Note with all list of Items In It
    */
    //Todo CHANGE TO A POST


    @RequestMapping(value = ["/proforma-invoice-with-Item"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun proformaInvoiceWithMoreItems(
        response: HttpServletResponse,
        @RequestParam(value = "ID") ID: Long
    ) {
        var map = hashMapOf<String, Any>()
//        val cdItemDetailsEntity = daoServices.findItemWithItemID(id)
        val batchInvoice = qaDaoServices.findBatchInvoicesWithID(ID)
        val batchInvoiceList = qaDaoServices.findALlInvoicesPermitWithBatchID(
            batchInvoice.id ?: throw ExpectedDataNotFound("MISSING BATCH INVOICE ID")
        )
        val companyProfile = commonDaoServices.findCompanyProfileWithID(
            commonDaoServices.findUserByID(
                batchInvoice.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
            ).companyId ?: throw ExpectedDataNotFound("MISSING USER ID")
        )

        map["preparedBy"] = batchInvoice.createdBy.toString()
        map["datePrepared"] = commonDaoServices.convertTimestampToKeswsValidDate(
            batchInvoice.createdOn ?: throw ExpectedDataNotFound("MISSING CREATION DATE")
        )
        map["demandNoteNo"] = batchInvoice.invoiceNumber.toString()
        map["companyName"] = companyProfile.name.toString()
        map["companyAddress"] = companyProfile.postalAddress.toString()
        map["companyTelephone"] = companyProfile.companyTelephone.toString()
//        map["productName"] = demandNote?.product.toString()
//        map["cfValue"] = demandNote?.cfvalue.toString()
//        map["rate"] = demandNote?.rate.toString()
//        map["amountPayable"] = demandNote?.amountPayable.toString()
        map["customerNo"] = companyProfile.entryNumber.toString()
        map["totalAmount"] = batchInvoice.totalAmount.toString()
        //Todo: config for amount in words

//                    map["amountInWords"] = demandNote?.
        map["receiptNo"] = batchInvoice.receiptNo.toString()

        map = reportsDaoService.addBankAndMPESADetails(map)

        reportsDaoService.extractReport(
            map,
            response,
            applicationMapProperties.mapReportProfomaInvoiceWithItemsPath,
            batchInvoiceList
        )
    }
//    /*
//    GetDemand Note with all list of Items In It
//     */
//    @RequestMapping(value = ["proforma-invoice-with-Item"], method = [RequestMethod.GET])
//    @Throws(Exception::class)
//    fun proformaInvoiceWithMoreItems(
//        response: HttpServletResponse,
//        @RequestParam(value = "ID") ID: Long
//    ) {
//        val pair = createInvoicePdf(ID)
//        var map = pair.first
//        val batchInvoiceList = pair.second
//
//        reportsDaoService.extractReport(
//            map,
//            response,
//            applicationMapProperties.mapReportProfomaInvoiceWithItemsPath,
//            batchInvoiceList
//        )
//    }

    fun createInvoicePdf(ID: Long): Pair<HashMap<String, Any>, List<QaInvoiceMasterDetailsEntity>> {
        var map = hashMapOf<String, Any>()
        //        val cdItemDetailsEntity = daoServices.findItemWithItemID(id)
        val batchInvoice = qaDaoServices.findBatchInvoicesWithID(ID)
        val batchInvoiceList = qaDaoServices.findALlInvoicesPermitWithBatchID(
            batchInvoice.id ?: throw ExpectedDataNotFound("MISSING BATCH INVOICE ID")
        )
        val companyProfile = commonDaoServices.findCompanyProfileWithID(
            commonDaoServices.findUserByID(
                batchInvoice.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
            ).companyId ?: throw ExpectedDataNotFound("MISSING USER ID")
        )

        map["preparedBy"] = batchInvoice.createdBy.toString()
        map["datePrepared"] = commonDaoServices.convertTimestampToKeswsValidDate(
            batchInvoice.createdOn ?: throw ExpectedDataNotFound("MISSING CREATION DATE")
        )
        map["demandNoteNo"] = batchInvoice.invoiceNumber.toString()
        map["companyName"] = companyProfile.name.toString()
        map["companyAddress"] = companyProfile.postalAddress.toString()
        map["companyTelephone"] = companyProfile.companyTelephone.toString()
        //        map["productName"] = demandNote?.product.toString()
        //        map["cfValue"] = demandNote?.cfvalue.toString()
        //        map["rate"] = demandNote?.rate.toString()
        //        map["amountPayable"] = demandNote?.amountPayable.toString()
        map["customerNo"] = companyProfile.entryNumber.toString()
        map["totalAmount"] = batchInvoice.totalAmount.toString()
        //Todo: config for amount in words

        //                    map["amountInWords"] = demandNote?.
        map["receiptNo"] = batchInvoice.receiptNo.toString()

        map = reportsDaoService.addBankAndMPESADetails(map)
        return Pair(map, batchInvoiceList)
    }


    fun permitCertificateIssuedCreation(id: Long): Pair<HashMap<String, Any>, String> {
        var map = hashMapOf<String, Any>()
        val appId: Int = applicationMapProperties.mapQualityAssurance
        val s = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(id)

        val foundPermitDetails = qaDaoServices.permitDetails(permit, s)
        var filePath: String? = null

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
        map["QrCode"] = foundPermitDetails.permitNumber.toString()
        when (foundPermitDetails.permitTypeID) {
            applicationMapProperties.mapQAPermitTypeIDDmark -> {
                map["DmarkLogo"] = dMarkImageFile
                map["backGroundImage"] = dMarkImageBackGroundFile
                filePath = applicationMapProperties.mapReportDmarkPermitReportPath

            }
            applicationMapProperties.mapQAPermitTypeIdSmark -> {
                map["SmarkLogo"] = sMarkImageFile
                map["backGroundImage"] = sMarkImageBackGroundFile
                filePath = applicationMapProperties.mapReportSmarkPermitReportPath

            }
            applicationMapProperties.mapQAPermitTypeIdFmark -> {
                map["FmarkLogo"] = fMarkImageFile
                map["backGroundImage"] = fMarkImageBackGroundFile
                filePath = applicationMapProperties.mapReportFmarkPermitReportPath

            }
        }
        return Pair(map, filePath ?: throw ExpectedDataNotFound("MISSING FILE PATH"))
    }


    @RequestMapping(value = ["braked-down-invoice-with-Item"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun brakedDownInvoiceWithMoreItems(
        response: HttpServletResponse,
        @RequestParam(value = "ID") ID: Long
    ) {
        var map = hashMapOf<String, Any>()
//        val cdItemDetailsEntity = daoServices.findItemWithItemID(id)
        val masterInvoice = qaDaoServices.findPermitInvoiceByPermitID(ID)
        val invoiceDetailsList = qaDaoServices.findALlInvoicesPermitWithMasterInvoiceID(
            masterInvoice.id, 1
        )
        val companyProfile = commonDaoServices.findCompanyProfileWithID(
            commonDaoServices.findUserByID(
                masterInvoice.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
            ).companyId ?: throw ExpectedDataNotFound("MISSING USER ID")
        )

        map["preparedBy"] = masterInvoice.createdBy.toString()
        map["datePrepared"] = commonDaoServices.convertTimestampToKeswsValidDate(
            masterInvoice.createdOn ?: throw ExpectedDataNotFound("MISSING CREATION DATE")
        )
        map["demandNoteNo"] = masterInvoice.invoiceRef.toString()
        map["companyName"] = companyProfile.name.toString()
        map["companyAddress"] = companyProfile.postalAddress.toString()
        map["companyTelephone"] = companyProfile.companyTelephone.toString()
//        map["productName"] = demandNote?.product.toString()
//        map["cfValue"] = demandNote?.cfvalue.toString()
//        map["rate"] = demandNote?.rate.toString()
//        map["amountPayable"] = demandNote?.amountPayable.toString()
        map["customerNo"] = companyProfile.entryNumber.toString()
        map["taxAmount"] = masterInvoice.taxAmount.toString()
        map["subTotalAmount"] = masterInvoice.subTotalBeforeTax.toString()
        map["totalAmount"] = masterInvoice.totalAmount.toString()
        //Todo: config for amount in words

//                    map["amountInWords"] = demandNote?.
        map["receiptNo"] = masterInvoice.receiptNo.toString()

        map = reportsDaoService.addBankAndMPESADetails(map)

        reportsDaoService.extractReport(
            map,
            response,
            applicationMapProperties.mapReportBreakDownInvoiceWithItemsPath,
            invoiceDetailsList
        )
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
        map["QrCode"] = foundPermitDetails.permitNumber.toString()


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