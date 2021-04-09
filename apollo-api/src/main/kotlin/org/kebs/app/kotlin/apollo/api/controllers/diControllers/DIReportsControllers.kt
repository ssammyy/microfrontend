package org.kebs.app.kotlin.apollo.api.controllers.diControllers


import mu.KotlinLogging
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.xml.JRXmlLoader
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.core.io.ResourceLoader
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/api/di/")
class DIReportsControllers(
        private val applicationMapProperties: ApplicationMapProperties,
        private val iLaboratoryRepo: ILaboratoryRepository,
        private val iSampleCollectRepo: ISampleCollectRepository,
        private val iDemandNoteRepo: IDemandNoteRepository,
        private val iCheckListRepo: ICheckListRepository,
        private val iSampleSubmitRepo: ISampleSubmitRepository,
        private val iPvocInvoicingRepository: IPvocInvoicingRepository,
        private val iPvocPartnersRepository: IPvocPartnersRepository,
        private val daoServices: DestinationInspectionDaoServices,
        private val iCdItemsRepo: IConsignmentItemsRepository,
        private val reportsDaoService: ReportsDaoService,
        private val commonDaoServices: CommonDaoServices,
        private val invoiceDaoService: InvoiceDaoService,
        private val resourceLoader: ResourceLoader
) {


    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
    @RequestMapping(value = ["report"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun myReport(
            response: HttpServletResponse,
            @RequestParam(value = "docType", required = false) docType: String,
            @RequestParam(value = "id", required = false) id: Long?,
            @RequestParam(value = "cdId", required = false) cdId: Long?,
            @RequestParam(value = "itemNo", required = false) itemNo: Long?
    ) {

//        val imagePath = ResourceUtils.getFile("classpath:static/images/KEBS_SMARK.png").toString()

        val resource = resourceLoader.getResource("classpath:static/images/KEBS_SMARK.png")
        val imageFile = resource.file.toString()

        val map = hashMapOf<String, Any>()
//        map["ITEM_ID"] = id
        map["imagePath"] = imageFile
        var sampCollect = listOf<Any>()
        when {
            docType.equals("sampleCollection") -> {
//                sampCollect = iSampleCollectRepo.findByItemIdAndStatus(id,1)
                sampCollect = id?.let { iSampleCollectRepo.findFirstByItemId(it) }!!
            }
            docType.equals("sampleSubmission") -> {
                sampCollect = id?.let { iSampleSubmitRepo.findFirstByItemId(it) }!!
            }
            docType.equals("agrochemChecklist") -> {
                sampCollect = id?.let { iCheckListRepo.findFirstByItemId(it) }!!
            }
            docType.equals("allAgrochemChecklist") -> {
                sampCollect = cdId?.let { iCheckListRepo.findByCdIdNumberAndItemNumber(it, 1L) }!!
            }
            docType.equals("allEngineringChecklist") -> {
                sampCollect = cdId?.let { iCheckListRepo.findByCdIdNumberAndItemNumber(it, 2L) }!!
            }
            docType.equals("allOtherChecklist") -> {
                sampCollect = cdId?.let { iCheckListRepo.findByCdIdNumberAndItemNumber(it, 3L) }!!
            }
            docType.equals("demandNote") -> {
                iCdItemsRepo.findByIdOrNull(id)?.let { cdItemDetailsEntity ->
                    iDemandNoteRepo.findFirstByItemId(cdItemDetailsEntity).let {
                        sampCollect = it
                    }
                }
            }
        }
        reportsDaoService.extractReport(map, response, "classpath:reports/$docType.jrxml", sampCollect)

    }

    fun getDateTime(s: Timestamp?): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            return sdf.format(s)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    //    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
    @RequestMapping(value = ["/pvoc/reconciliation/report"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun myReconciliationReport(
            response: HttpServletResponse,
            @RequestParam(value = "id", required = false) id: Long?
    ) {
        val map = hashMapOf<String, Any>()
        id?.let { it ->
            iPvocInvoicingRepository.findByReconcialitionId(it).let { invoice ->
                iPvocPartnersRepository.findByIdOrNull(invoice?.partner)?.let { partinerDetails ->
                    map["exporterName"] = partinerDetails.partnerName.toString()
                    map["exporterAddress"] = partinerDetails.partnerAddress1.toString()
                }
                map["invoiceNumber"] = invoice?.invoiceNumber.toString()
                map["documentDate"] = getDateTime(invoice?.createdOn).toString()
                map["expoterAddress"] = invoice?.partner.toString()
                map["orderNumber"] = invoice?.orderNumber.toString()
                map["orderDate"] = invoice?.orderDate.toString()
                map["customerNumber"] = invoice?.customerNumber.toString()
                map["poNumber"] = invoice?.poNumber.toString()
                map["shipVia"] = invoice?.shipVia.toString()
                map["termsCode"] = invoice?.partner.toString()
                map["amount"] = invoice?.amountDue.toString()
                map["description"] = invoice?.description.toString()
                map["subtotalBeforeTaxes"] = "0.00"
                map["totalTaxes"] = "0.00"
                map["totalAmount"] = invoice?.amountDue.toString()
            }
        } ?: throw Exception("Id does not exist")

        reportsDaoService.extractReport(map, response, "classpath:reports/reconciliationReport.jrxml", listOf<Any>())
    }


    @RequestMapping(value = ["report/demandNote"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun demandNote(
            response: HttpServletResponse,
            @RequestParam(value = "id") id: Long
    ) {
        var map = hashMapOf<String, Any>()
        val cdItemDetailsEntity = daoServices.findItemWithItemID(id)
        val demandNote = daoServices.findDemandNote(cdItemDetailsEntity)

        map["preparedBy"] = demandNote?.generatedBy.toString()
        map["datePrepared"] = demandNote?.dateGenerated.toString()
        map["demandNoteNo"] = demandNote?.demandNoteNumber.toString()
        map["importerName"] = demandNote?.nameImporter.toString()
        map["importerAddress"] = demandNote?.address.toString()
        map["importerTelephone"] = demandNote?.telephone.toString()
        map["productName"] = demandNote?.product.toString()
        map["cfValue"] = demandNote?.cfvalue.toString()
        map["rate"] = demandNote?.rate.toString()
        map["amountPayable"] = demandNote?.amountPayable.toString()
        map["ablNo"] = demandNote?.entryAblNumber.toString()
        map["totalAmount"] = demandNote?.totalAmount.toString()
        //Todo: config for amount in words

//                    map["amountInWords"] = demandNote?.
        map["receiptNo"] = demandNote?.receiptNo.toString()

        map = reportsDaoService.addBankAndMPESADetails(map)

        reportsDaoService.extractReportEmptyDataSource(map, response, applicationMapProperties.mapReportDemandNotePath)
    }


    /*
    GetDemand Note with all list of Items In It
     */
    @RequestMapping(value = ["report/demandNote-with-Item"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun demandNoteWithMoreItems(
        response: HttpServletResponse,
        @RequestParam(value = "id") id: Long
    ) {
        var map = hashMapOf<String, Any>()
//        val cdItemDetailsEntity = daoServices.findItemWithItemID(id)
        val demandNote = daoServices.findDemandNoteWithCdID(id)
        val demandNoteItemList = demandNote?.id?.let { daoServices.findDemandNoteItemDetails(it) }
            ?: throw ExpectedDataNotFound("No List Of Details Available does not exist")

        map["preparedBy"] = demandNote.generatedBy.toString()
        map["datePrepared"] = demandNote.dateGenerated.toString()
        map["demandNoteNo"] = demandNote.demandNoteNumber.toString()
        map["importerName"] = demandNote.nameImporter.toString()
        map["importerAddress"] = demandNote.address.toString()
        map["importerTelephone"] = demandNote.telephone.toString()
//        map["productName"] = demandNote?.product.toString()
//        map["cfValue"] = demandNote?.cfvalue.toString()
//        map["rate"] = demandNote?.rate.toString()
//        map["amountPayable"] = demandNote?.amountPayable.toString()
        map["ablNo"] = demandNote.entryAblNumber.toString()
        map["totalAmount"] = demandNote.totalAmount.toString()
        //Todo: config for amount in words

//                    map["amountInWords"] = demandNote?.
        map["receiptNo"] = demandNote.receiptNo.toString()

        map = reportsDaoService.addBankAndMPESADetails(map)

        reportsDaoService.extractReport(
            map,
            response,
            applicationMapProperties.mapReportDemandNoteWithItemsPath,
            demandNoteItemList
        )
    }

    /*
    Get Ministry inspection unfilled checklist
     */
    @RequestMapping(value = ["/ministry-checklist"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun ministryMVchecklistFile(
        response: HttpServletResponse,
        @RequestParam(value = "mvInspectionChecklistId", required = false) mvInspectionChecklistid: Long?
    ) {
        val map = hashMapOf<String, Any>()
        mvInspectionChecklistid?.let { it ->
            daoServices.findInspectionMotorVehicleById(it)?.let { mvInspectionChecklist ->
                map["ImporterName"] = mvInspectionChecklist.inspectionGeneral?.importersName.toString()
                map["VehicleMake"] = mvInspectionChecklist.makeVehicle.toString()
                map["EngineCapacity"] = mvInspectionChecklist.engineNoCapacity.toString()
                map["ManufactureDate"] = mvInspectionChecklist.manufactureDate.toString()
                map["OdometerReading"] = mvInspectionChecklist.odemetreReading.toString()
                map["RegistrationDate"] = mvInspectionChecklist.registrationDate.toString()
                map["ChassisNo"] = mvInspectionChecklist.chassisNo.toString()

            } ?: throw ExpectedDataNotFound("Motor Vehicle Inspection Checklist does not exist")

            reportsDaoService.extractReportEmptyDataSource(map, response, "classpath:reports/UsedMVMinistryChecklistFinal.jrxml")
        }
    }

    /*
    Get Ministry inspection report
     */
    @RequestMapping(value = ["/motor-inspection-report"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun motorInspectionReport(
            response: HttpServletResponse,
            @RequestParam(value = "mvInspectionChecklistId", required = false) mvInspectionChecklistid: Long?
    ) {
//        //Get KEBS Logo
//        val logoImageresource = resourceLoader.getResource("classpath:static/images/KEBS_SMARK.png")
//        val logoImageFile = logoImageresource.file.toString()

        //Get checkmark image
        val checkMarkImageresource = resourceLoader.getResource("classpath:static/images/checkmark.png")
        val checkMarkImageFile = checkMarkImageresource.file.toString()

        val map = hashMapOf<String, Any>()
        mvInspectionChecklistid?.let { it ->
            daoServices.findInspectionMotorVehicleById(it)?.let { mvInspectionChecklist ->
//                map["imagePath"] = logoImageFile
                map["CheckMark"] = checkMarkImageFile
                when (mvInspectionChecklist.inspectionGeneral?.inspection) {
                    "100% Inspection" -> map["HundredPercentInspectionCheckmark"] = checkMarkImageFile
                    "Partial Inspection" -> map["PartialInspectionCheckMark"] = checkMarkImageFile
                }
                map["EntryPoint"] = mvInspectionChecklist.inspectionGeneral?.entryPoint.toString()
                map["Cfs"] = mvInspectionChecklist.inspectionGeneral?.cfs.toString()
                map["Date"] = mvInspectionChecklist.inspectionGeneral?.inspectionDate.toString()
                map["ImportersName"] = mvInspectionChecklist.inspectionGeneral?.importersName.toString()
                map["ClearingAgent"] = mvInspectionChecklist.inspectionGeneral?.clearingAgent.toString()
                map["CustomsEntryNo"] = mvInspectionChecklist.inspectionGeneral?.customsEntryNumber.toString()
                map["IdfNo"] = mvInspectionChecklist.inspectionGeneral?.idfNumber.toString()
                map["UcrNo"] = mvInspectionChecklist.inspectionGeneral?.ucrNumber.toString()
                map["CocNo"] = mvInspectionChecklist.inspectionGeneral?.cocNumber.toString()
                map["ReceiptNo"] = mvInspectionChecklist.inspectionGeneral?.receiptNumber.toString()
                map["SerialNo"] = mvInspectionChecklist.serialNumber.toString()
                map["VehicleMake"] = mvInspectionChecklist.makeVehicle.toString()
                map["ChassisNo"] = mvInspectionChecklist.chassisNo.toString()
                map["EngineCapacity"] = mvInspectionChecklist.engineNoCapacity.toString()
                map["ManufacturerDate"] = mvInspectionChecklist.manufactureDate.toString()
                map["RegistrationDate"] = mvInspectionChecklist.registrationDate.toString()
                map["OdometreReading"] = mvInspectionChecklist.odemetreReading.toString()
                map["Drive"] = mvInspectionChecklist.driveRhdLhd.toString()
                map["Transmission"] = mvInspectionChecklist.transmissionAutoManual.toString()
                map["Colour"] = mvInspectionChecklist.colour.toString()
                map["OverallAppearance"] = mvInspectionChecklist.overallAppearance.toString()
                map["OverallAppearance"] = mvInspectionChecklist.overallAppearance.toString()
                map["Remarks"] = mvInspectionChecklist.remarks.toString()
                map["Remarks"] = mvInspectionChecklist.remarks.toString()
                map["OverallRemarks"] = mvInspectionChecklist.inspectionGeneral?.overallRemarks.toString()

            } ?: throw ExpectedDataNotFound("Motor Vehicle Inspection Checklist does not exist")

            reportsDaoService.extractReportEmptyDataSource(map, response, "classpath:reports/MotorVehicleChecklistReport.jrxml")
        }
    }

    /*
    Get Local CoR report
     */
/*
Get Ministry inspection report
 */
    @RequestMapping(value = ["/local-cor-report"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun localCorReport(
            response: HttpServletResponse,
            @RequestParam(value = "cdUuid", required = false) consignmentDocUuid: String?
    ) {
        //Get KEBS Logo
        val logoImageresource = resourceLoader.getResource("classpath:static/images/KEBS_SMARK.png")
        val logoImageFile = logoImageresource.file.toString()

        //Get checkmark image
        val checkMarkImageresource = resourceLoader.getResource("classpath:static/images/checkmark.png")
        val checkMarkImageFile = checkMarkImageresource.file.toString()

        var map = hashMapOf<String, Any>()
        val cdEntity = consignmentDocUuid?.let { daoServices.findCDWithUuid(it) }
        KotlinLogging.logger {}.info { "The cdEntity found: ${cdEntity?.id}" }
        cdEntity?.ucrNumber?.let {
            daoServices.findLocalCorByUcrNumber(it).let {
                KotlinLogging.logger {}.info { "The cdLocalCorEntity found: ${it.id}" }
                map = daoServices.createLocalCorReportMap(it)
//                map["imagePath"] = logoImageFile
                KotlinLogging.logger {}.info { "Final Map: ${map.values}" }
            }
        }
        reportsDaoService.extractReportEmptyDataSource(map, response, "classpath:reports/LocalCoRReport.jrxml")
    }

//    private fun extractReport(map: HashMap<String, Any>, response: HttpServletResponse, filePath: String, sampCollect: List<Any>) {
//       val dataSource = JRBeanCollectionDataSource(sampCollect)
//
//                    ResourceUtils.getFile(filePath)
//                            .let { file ->
//                                JRXmlLoader.load(file)
//                                        .let { design ->
//                                            JasperCompileManager.compileReport(design)
//                                                    .let { jasperReport ->
//                                                        JasperFillManager.fillReport(jasperReport, map, dataSource)
//                                                                .let { jasperPrint ->
//                                                                    JRPdfExporter()
//                                                                            .let { pdfExporter ->
//                                                                                ByteArrayOutputStream()
//                                                                                        .let { pdfReportStream ->
//                                                                                            pdfExporter.setExporterInput(SimpleExporterInput(jasperPrint))
//                                                                                            pdfExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
//                                                                                            pdfExporter.exportReport()
//                                                                                            response.contentType = "text/html"
//                                                                                            response.contentType = "application/pdf"
//                                                                                            response.setHeader("Content-Length", pdfReportStream.size().toString())
//                                                                                            response.addHeader("Content-Dispostion", "inline; filename=jasper.pdf;")
//                                                                                            response.outputStream
//                                                                                                    .let { responseOutputStream ->
//                                                                                                        responseOutputStream.write(pdfReportStream.toByteArray())
//                                                                                                        responseOutputStream.close()
//                                                                                                        pdfReportStream.close()
//                                                                                                    }
//                                                                                        }
//
//                                                                            }
//                                                                }
//                                                    }
//                                        }
//                            }
//
//    }
//
//    /*
//    Note: Use this method if your report contains multiple data bands and you have not set any fields in the report,
//    i.e you're using Parameters only.
//     */
//    private fun extractReportEmptyDataSource(map: HashMap<String, Any>, response: HttpServletResponse, filePath: String) {
//        ResourceUtils.getFile(filePath).let { file ->
//            JRXmlLoader.load(file).let { design ->
//                JasperCompileManager.compileReport(design).let { jasperReport ->
//                    JasperFillManager.fillReport(jasperReport, map, JREmptyDataSource()).let { jasperPrint ->
//                        JRPdfExporter().let { pdfExporter ->
//                            ByteArrayOutputStream().let { pdfReportStream ->
//                                pdfExporter.setExporterInput(SimpleExporterInput(jasperPrint))
//                                pdfExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
//                                pdfExporter.exportReport()
//                                response.contentType = "text/html"
//                                response.contentType = "application/pdf"
//                                response.setHeader("Content-Length", pdfReportStream.size().toString())
//                                response.addHeader("Content-Dispostion", "inline; filename=jasper.pdf;")
//                                response.outputStream.let { responseOutputStream ->
//                                            responseOutputStream.write(pdfReportStream.toByteArray())
//                                            responseOutputStream.close()
//                                            pdfReportStream.close()
//                                        }
//                            }
//                        }
//                    }
//                    }
//                }
//            }
//        }
}


