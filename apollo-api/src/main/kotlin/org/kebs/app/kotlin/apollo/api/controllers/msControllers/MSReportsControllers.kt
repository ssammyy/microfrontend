package org.kebs.app.kotlin.apollo.api.controllers.msControllers



import mu.KotlinLogging
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.xml.JRXmlLoader
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.io.File
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/api/ms/")
class MSReportsControllers(
    private val iLaboratoryRepo: ILaboratoryRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val destinationInspectionRepo: IDestinationInspectionRepository,
    private val iSampleCollectRepo: ISampleCollectRepository,
    private val iCheckListRepo: ICheckListRepository,
    private val iFuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository,
    private val iSampleCollectViewRepo: ISampleCollectionViewRepository,
    private val iFuelInspectionRepo: IFuelInspectionRepository,
    private val iSampleSubmitRepo: ISampleSubmitRepository
) {


//    @PreAuthorize("hasAuthority('M')")
    @RequestMapping(value = ["report"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun myReport(
        response: HttpServletResponse,
        @RequestParam(value = "docType", required = false) docType: String,
        @RequestParam(value = "id", required = false) id: Long?,
        @RequestParam(value = "fuelInspectionId") fuelInspectionId: Long?,
        @RequestParam(value = "sampleCollectID") sampleCollectID: Long?,
        @RequestParam(value = "sendAsAttachment") sendAsAttachment: String?,
        @RequestParam(value = "cdId", required = false) cdId: Long?,
        @RequestParam(value = "itemNo", required = false) itemNo: Long?
) {

//    //Get file from resources folder
//    //Get file from resources folder
//    val classLoader: ClassLoader = Utils::class.java.getClassLoader()
//    val file = File(classLoader.getResource(fileName).file)

        val imagePath = ResourceUtils.getFile("classpath:static/images/KEBS_SMARK.png").toString()
        val map = hashMapOf<String, Any>()
//        map["ITEM_ID"] = id
        map["imagePath"] = imagePath
        var viewValue = listOf<Any>()
        when {
            docType.equals("sampleCollection") -> {
//                sampCollect = iSampleCollectRepo.findByItemIdAndStatus(id,1)
                viewValue = id?.let { iSampleCollectRepo.findFirstByItemId(it) }!!
            }
            docType.equals("sampleSubmission") -> {
                viewValue = id?.let { iSampleSubmitRepo.findFirstByItemId(it) }!!
            }
            docType.equals("agrochemChecklist") -> {
                viewValue = id?.let { iCheckListRepo.findFirstByItemId(it) }!!
            }
            docType.equals("allAgrochemChecklist") -> {
                viewValue = cdId?.let { iCheckListRepo.findByCdIdNumberAndItemNumber(it, 1L) }!!
            }
            docType.equals("allEngineringChecklist") -> {
                viewValue = cdId?.let { iCheckListRepo.findByCdIdNumberAndItemNumber(it, 2L) }!!
            }
            docType.equals("allOtherChecklist") -> {
                viewValue = cdId?.let { iCheckListRepo.findByCdIdNumberAndItemNumber(it, 3L) }!!
            }
//            docType.equals("demandNote") -> {
//                viewValue = id?.let { iDemandNoteRepo.findFirstByItemIdNo(it) }!!
//            }
            docType.equals("msSampleCollection") -> {
                viewValue = sampleCollectID?.let { iSampleCollectViewRepo.findBySampleCollectionId(it) }!!
            }
            docType.equals("remediationInvoice") -> {
                iFuelInspectionRepo.findByIdOrNull(fuelInspectionId)
                        ?.let { msFuelInspectionEntity->
                            viewValue =  iFuelRemediationInvoiceRepo.findFirstByFuelInspectionId(msFuelInspectionEntity)
                        }

            }
        }
        extractReport(map, response, "classpath:reports/$docType.jrxml", viewValue)
//        if(sendAsAttachment.equals("yes")){
//            extractAndSaveReport(map, "classpath:reports/$docType.jrxml", viewValue)
//        }

    }

    private fun extractReport(map: HashMap<String, Any>, response: HttpServletResponse, filePath: String, sampCollect: List<Any>) {
        JRBeanCollectionDataSource(sampCollect)
                .let { dataSource ->
                    ResourceUtils.getFile(filePath)
                            .let { file ->
                                JRXmlLoader.load(file)
                                        .let { design ->
                                            JasperCompileManager.compileReport(design)
                                                    .let { jasperReport ->
                                                        JasperFillManager.fillReport(jasperReport, map, dataSource)
                                                                .let { jasperPrint ->
                                                                    JRPdfExporter()
                                                                            .let { pdfExporter ->
                                                                                ByteArrayOutputStream()
                                                                                        .let { pdfReportStream ->
                                                                                            pdfExporter.setExporterInput(SimpleExporterInput(jasperPrint))
                                                                                            pdfExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
                                                                                            pdfExporter.exportReport()
                                                                                            response.contentType = "text/html"
                                                                                            response.contentType = "application/pdf"
                                                                                            response.setHeader("Content-Length", pdfReportStream.size().toString())
                                                                                            response.addHeader("Content-Dispostion", "inline; filename=jasper.pdf;")
                                                                                            response.outputStream
                                                                                                    .let { responseOutputStream ->
                                                                                                        responseOutputStream.write(pdfReportStream.toByteArray())
                                                                                                        responseOutputStream.close()
                                                                                                        pdfReportStream.close()
                                                                                                    }
                                                                                        }

                                                                            }
                                                                }
                                                    }
                                        }
                            }
                }
    }

    fun extractAndSaveReport(map: HashMap<String, Any>, filePath: String, fileName: String, sampCollect: List<Any>) {
        JRBeanCollectionDataSource(sampCollect)
                .let { dataSource ->
                    ResourceUtils.getFile(filePath)
                            .let { file ->
                                JRXmlLoader.load(file)
                                        .let { design ->
                                            JasperCompileManager.compileReport(design)
                                                    .let { jasperReport ->
                                                        JasperFillManager.fillReport(jasperReport, map, dataSource)
                                                                .let { jasperPrint ->
                                                                    val exportPath =
                                                                        applicationMapProperties.mapPDFProfomaInvoiceWithItemsPath
                                                                    JasperExportManager.exportReportToPdfFile(jasperPrint, exportPath)
                                                                    KotlinLogging.logger { }.info { "Report Generated Successfully Ready for attachment"}
                                                                }
                                                    }
                                        }
                            }
                }
    }
}
