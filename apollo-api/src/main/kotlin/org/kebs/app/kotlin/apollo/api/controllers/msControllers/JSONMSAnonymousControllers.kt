package org.kebs.app.kotlin.apollo.api.controllers.msControllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.MarketSurveillanceComplaintProcessDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.MarketSurveillanceFuelDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.ms.MSComplaintSubmittedSuccessful
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ms.MsUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.IComplaintRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.IFuelRemediationInvoiceRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.IMsUploadsRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.ISampleCollectionViewRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/api/v1/migration/ms/anonymous")
class JSONMSAnonymousControllers(
    private val applicationMapProperties: ApplicationMapProperties,
    private val msUploadRepo: IMsUploadsRepository,
    private val serviceRequestsRepo: IServiceRequestsRepository,
    private val marketSurveillanceDaoComplaintServices: MarketSurveillanceComplaintProcessDaoServices,
    private val iSampleCollectViewRepo: ISampleCollectionViewRepository,
    private val fuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
    private val commonDaoServices: CommonDaoServices,
    private val msDaoService: MarketSurveillanceFuelDaoServices,
    private val reportsDaoService: ReportsDaoService,
    private val limsServices: LimsServices,
    private val resourceLoader: ResourceLoader,
){
    final var appId = applicationMapProperties.mapMarketSurveillance

    @PostMapping("/complaint/file/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesMsComplaintFile(
        @RequestParam("refNumber") refNumber: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
//        @RequestParam("data") data: List<MultipartFile>,
        model: Model
    ): MSComplaintSubmittedSuccessful {
        val map = commonDaoServices.serviceMapDetails(appId)
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val complaintDetails = marketSurveillanceDaoComplaintServices.findComplaintByRefNumber(refNumber)

        docFile.forEach { fileDoc ->
            var sr = commonDaoServices.createServiceRequest(map)
            try {
                var upload = MsUploadsEntity()
                with(upload) {
                    msComplaintId = complaintDetails.id
                    complaintUploads = 1
                    ordinaryStatus = 0
                    versionNumber = 1
                    name = fileDoc.name
                    fileType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(fileDoc.name)
                    documentType = "COMPLAINT FILE"
                    document = fileDoc.bytes
                    transactionDate = commonDaoServices.getCurrentDate()
                    status = 1
                    createdBy = complaintDetails.createdBy
                    createdOn = commonDaoServices.getTimestamp()
                }
                upload = msUploadRepo.save(upload)
            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
                sr.payload = "${fileDoc.bytes}"
                sr.status = sr.serviceMapsId?.exceptionStatus
                sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
                sr.responseMessage = e.message
                sr = serviceRequestsRepo.save(sr)

            }
            KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        }

        return MSComplaintSubmittedSuccessful(complaintDetails.referenceNumber,true,"Complaint submitted successful with ref number ${complaintDetails.referenceNumber}", null)
    }



    @RequestMapping(value = ["/report/remediation-invoice"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun msRemediationInvoicePDF(
        response: HttpServletResponse,
        @RequestParam(value = "fuelInspectionId") fuelInspectionId: Long
    ) {
        val map = hashMapOf<String, Any>()
        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

        val invoiceRemediationDetails = fuelRemediationInvoiceRepo.findFirstByFuelInspectionId(fuelInspectionId)
        val fuelRemediationDetailsDto = msDaoService.mapFuelRemediationDetails(invoiceRemediationDetails)
        val pdfReportStream = reportsDaoService.extractReport(
            map,
            applicationMapProperties.mapMSFuelInvoiceRemediationPath,
            fuelRemediationDetailsDto
        )
        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; Remediation-Invoice-${invoiceRemediationDetails[0].fuelInspectionId}.pdf;")
        response.outputStream.let { responseOutputStream ->
            responseOutputStream.write(pdfReportStream.toByteArray())
            responseOutputStream.close()
            pdfReportStream.close()
        }
    }



//    @GetMapping("populate-all-labs",  produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun populateBroadCategory(): String? {
//        val gson = Gson()
//        return gson.toJson(iLaboratoryRepo.findAll())
//    }

//    @GetMapping("email-check/{email}",  produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun myEmail(
//            @PathVariable(value = "email", required = false) email: String
//    ): String? {
//        var result = "notExist"
//        usersRepo.findByEmail(email)
//                .let {usersEntity ->
//                    if (usersEntity?.email == email){
//                        result = email
//                    }else{
//                        result
//                    }
//                }
//        return result
//    }


}
