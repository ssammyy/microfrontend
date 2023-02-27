package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import com.google.gson.Gson
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.qa.QaInvoiceMasterDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.ISampleStandardsRepository
import org.kebs.app.kotlin.apollo.store.repo.UserSignatureRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayInputStream
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap

@RestController
@RequestMapping("/api/qa/report/")
//@RequestMapping("/api/v1/migration/qa/report")
class ReportsController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val qaDaoServices: QADaoServices,
    private val reportsDaoService: ReportsDaoService,
    private val commonDaoServices: CommonDaoServices,
    private val resourceLoader: ResourceLoader,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val usersSignatureRepository: UserSignatureRepository


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

        map = reportsDaoService.addBankAndMPESADetails(map, batchInvoice.invoiceNumber.toString())

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
        map["demandNoteNo"] = batchInvoice.sageInvoiceNumber.toString()
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

        map = reportsDaoService.addBankAndMPESADetails(map, batchInvoice.invoiceNumber.toString())
        return Pair(map, batchInvoiceList)
    }


//    fun createRemediationInvoicePdf(ID: Long): Pair<HashMap<String, Any>, List<QaInvoiceMasterDetailsEntity>> {
//        var map = hashMapOf<String, Any>()
//        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)
//
//        return Pair(map, batchInvoiceList)
//    }


    fun permitCertificateIssuedCreation(id: Long): Pair<HashMap<String, Any>, String> {
        var map = hashMapOf<String, Any>()
        val appId: Int = applicationMapProperties.mapQualityAssurance
        val s = commonDaoServices.serviceMapDetails(appId)
        val permit = qaDaoServices.findPermitBYID(id)

        val user = permit.varField6?.toLong().let { it?.let { it1 -> commonDaoServices.findUserByID(it1) } }


        if (user != null) {
            val mySignature: ByteArray?
            val image: ByteArrayInputStream?
            println("UserID is" + user.id)
            val signatureFromDb = user.id?.let { usersSignatureRepository.findByUserId(it) }
            if (signatureFromDb != null) {
                mySignature= signatureFromDb.signature
                image = ByteArrayInputStream(mySignature)
                map["Signature"] = image

            }
        }
        val foundPermitDetails = qaDaoServices.permitDetails(permit, s)
        var filePath: String? = null
        var permitType:String? = null


        when (foundPermitDetails.permitTypeID) {
            applicationMapProperties.mapQAPermitTypeIDDmark -> {
                map["DmarkLogo"] = dMarkImageFile
                map["backGroundImage"] = dMarkImageBackGroundFile
                filePath = applicationMapProperties.mapReportDmarkPermitReportPath
                permitType ="DM#"

            }
            applicationMapProperties.mapQAPermitTypeIdSmark -> {
                map["SmarkLogo"] = sMarkImageFile
                map["backGroundImage"] = sMarkImageBackGroundFile
                filePath = applicationMapProperties.mapReportSmarkPermitReportPath
                permitType ="SM#"

            }
            applicationMapProperties.mapQAPermitTypeIdFmark -> {
                map["FmarkLogo"] = fMarkImageFile
                map["backGroundImage"] = fMarkImageBackGroundFile
                filePath = applicationMapProperties.mapReportFmarkPermitReportPath
                permitType ="FM#"

            }
        }

        map["FirmName"] = foundPermitDetails.firmName.toString()
        map["PermitNo"] = foundPermitDetails.permitNumber.toString()
        map["PostalAddress"] = foundPermitDetails.postalAddress.toString()
        map["PhysicalAddress"] = foundPermitDetails.buildingName.toString()

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

        map["QrCode"] = "${applicationMapProperties.baseEndPointValue}getAllAwardedPermits?permitNumber=$permitType${foundPermitDetails.permitNumber}"




//        map["QrCode"] = "${applicationMapProperties.baseUrlQRValue}qr-code-qa-permit-scan#${foundPermitDetails.permitNumber}"

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

        map = reportsDaoService.addBankAndMPESADetails(map, masterInvoice.invoiceRef.toString())

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
        var filePath: String? = null
        val user = permit.varField6?.toLong().let { it?.let { it1 -> commonDaoServices.findUserByID(it1) } }


        if (user != null) {
            val mySignature: ByteArray?
            val image: ByteArrayInputStream?
            println("UserID is" + user.id)
            val signatureFromDb = user.id?.let { usersSignatureRepository.findByUserId(it) }
            if (signatureFromDb != null) {
               mySignature= signatureFromDb.signature
                image = ByteArrayInputStream(mySignature)
                map["Signature"] = image

            }
        }

//        val imageBase64: String = Base64.getEncoder().encodeToString(mySignature)


//        println(imageBase64)

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
        map["QrCode"] =
            "${applicationMapProperties.baseUrlQRValue}qr-code-qa-permit-scan#${foundPermitDetails.permitNumber}"



        when (foundPermitDetails.permitTypeID) {
//            applicationMapProperties.mapQAPermitTypeIDDmark -> {
//                map["DmarkLogo"] = dMarkImageFile
//                 map["backGroundImage"] = dMarkImageBackGroundFile
//                reportsDaoService.extractReportEmptyDataSource(
//                    map,
//                    response,
//                    applicationMapProperties.mapReportDmarkPermitReportPath
//                )
//            }
//            applicationMapProperties.mapQAPermitTypeIdSmark -> {
//                map["SmarkLogo"] = sMarkImageFile
//                 map["backGroundImage"] = sMarkImageBackGroundFile
//                reportsDaoService.extractReportEmptyDataSource(
//                    map,
//                    response,
//                    applicationMapProperties.mapReportSmarkPermitReportPath
//                )
//            }
//            applicationMapProperties.mapQAPermitTypeIdFmark -> {
//                map["FmarkLogo"] = fMarkImageFile
//                 map["backGroundImage"] = fMarkImageBackGroundFile
//                reportsDaoService.extractReportEmptyDataSource(
//                    map,
//                    response,
//                    applicationMapProperties.mapReportFmarkPermitReportPath
//                )
//            }

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
        if (filePath != null) {
            reportsDaoService.extractReportEmptyDataSource(
                map,
                response,
                filePath
            )
        }

    }
}
