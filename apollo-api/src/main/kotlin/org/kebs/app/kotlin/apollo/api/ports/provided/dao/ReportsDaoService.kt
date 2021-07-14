package org.kebs.app.kotlin.apollo.api.ports.provided.dao


import com.google.common.io.Files
import mu.KotlinLogging
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.data.JRMapArrayDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.xml.JRXmlLoader
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.kebs.app.kotlin.apollo.common.dto.reports.LocalCocItemsReportInput
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CocsEntity
import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.ICocsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.ByteArrayOutputStream
import java.io.File
import javax.servlet.http.HttpServletResponse


@Service
class ReportsDaoService(
    private val applicationMapProperties: ApplicationMapProperties,
    private val resourceLoader: ResourceLoader,
    private val commonDaoServices: CommonDaoServices,
    private val invoiceDaoService: InvoiceDaoService
) {

    @Lazy
    @Autowired
    lateinit var diDaoServices: DestinationInspectionDaoServices

    //Get KEBS Logo
    final val logoImageResource = resourceLoader.getResource(applicationMapProperties.mapKebsLogoPath)
    val logoImageFile = logoImageResource.file.toString()



    fun addBankAndMPESADetails(map: HashMap<String, Any>): HashMap<String, Any> {
        //Get MPESA Logo
        val logoMpesaImageResource = resourceLoader.getResource(applicationMapProperties.mapMPESALogoPath)
        val logoMpesaImageFile = logoMpesaImageResource.file.toString()

        val mpesaDetails = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapMpesaDetails)
        val bank1Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankOneDetails)
        val bank2Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankTwoDetails)
        val bank3Details =invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankThreeDetails)
//        val map = hashMapOf<String, Any>()


        map["mpesaLogo"] = logoMpesaImageFile
        map["paybillNo"] = mpesaDetails.payBillNo.toString()
        map["mpesaACNo"] = mpesaDetails.mpesaAccNo.toString()
        map["vatNo"] = mpesaDetails.vatNo.toString()
        map["pinNo"] = mpesaDetails.pinNo.toString()
//        map["preparedBy"] = demandNote?.
//        map["datePrepared"] = demandNote?.
        map["bankName1"] = bank1Details.bankAccountName.toString()
        map["bank1"] = bank1Details.bankName.toString()
        map["bankBranch1"] = bank1Details.bankBranch.toString()
        map["kesAcNo1"] = bank1Details.bankAccountKesNumber.toString()
        map["usdNo1"] = bank1Details.bankAccountUsdNumber.toString()
        map["bankCode1"] = bank1Details.bankCode.toString()
        map["branchCode1"] = bank1Details.branchCode.toString()
        map["swiftCode1"] = bank1Details.swiftCode.toString()

        map["bankName2"] = bank2Details.bankAccountName.toString()
        map["bank2"] = bank2Details.bankName.toString()
        map["bankBranch2"] = bank2Details.bankBranch.toString()
        map["kesAcNo2"] = bank2Details.bankAccountKesNumber.toString()
        map["usdNo2"] = bank2Details.bankAccountUsdNumber.toString()
        map["bankCode2"] = bank2Details.bankCode.toString()
        map["branchCode2"] = bank2Details.branchCode.toString()
        map["swiftCode2"] = bank2Details.swiftCode.toString()

        map["bankName3"] =bank3Details.bankAccountName.toString()
        map["bank3"] =bank3Details.bankName.toString()
        map["bankBranch3"] =bank3Details.bankBranch.toString()
        map["kesAcNo3"] =bank3Details.bankAccountKesNumber.toString()
        map["usdNo3"] =bank3Details.bankAccountUsdNumber.toString()
        map["bankCode3"] =bank3Details.bankCode.toString()
        map["branchCode3"] =bank3Details.branchCode.toString()
        map["swiftCode3"] =bank3Details.swiftCode.toString()

        return map
    }

    fun extractReport(
        map: HashMap<String, Any>,
        response: HttpServletResponse,
        filePath: String,
        listCollect: List<Any>
    ) {
        map["imagePath"] = logoImageFile
        val dataSource = JRBeanCollectionDataSource(listCollect)
        val file = ResourceUtils.getFile(filePath)
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)
        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource)
        val pdfExporter = JRPdfExporter()
        val pdfReportStream = ByteArrayOutputStream()
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

    /*
    Note: Use this method if your report contains multiple data bands and you have not set any fields in the report,
    i.e you're using Parameters only.
     */
    fun extractReportEmptyDataSource(map: HashMap<String, Any>, response: HttpServletResponse, filePath: String) {
        map["imagePath"] = logoImageFile
        val file = ResourceUtils.getFile(filePath)
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)
        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, JREmptyDataSource())
        val pdfExporter = JRPdfExporter()
        val pdfReportStream = ByteArrayOutputStream()
        pdfExporter.setExporterInput(SimpleExporterInput(jasperPrint))
        pdfExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
        pdfExporter.exportReport()
        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; filename=jasper.pdf;")
        response.outputStream.let { responseOutputStream ->
            responseOutputStream.write(pdfReportStream.toByteArray())
            responseOutputStream.close()
            pdfReportStream.close()
        }

    }

    fun generateLocalCoCReportWithDataSource(cdDetails: ConsignmentDocumentDetailsEntity, filePath: String): File? {
        var map = hashMapOf<String, Any>()
        map["imagePath"] = logoImageFile
        cdDetails.ucrNumber?.let {
            diDaoServices.findCocByUcrNumber(it)?.let { coc ->
                map = diDaoServices.createLocalCocReportMap(coc)
                val cocItems = cdDetails.let { diDaoServices.findCDItemsListWithCDID(it) }

                val itemsReportInput: LocalCocItemsReportInput = assembleCocItemReportInput(cocItems)
                val dataSource = JRMapArrayDataSource(arrayOf(itemsReportInput.getDataSources()))

                val file = ResourceUtils.getFile(filePath)
                val design = JRXmlLoader.load(file)
                val jasperReport = JasperCompileManager.compileReport(design)

                val jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource)

                val fileName: String = cdDetails.ucrNumber.plus("-coc-report.pdf")

                val targetFile = File(Files.createTempDir(), fileName)
                targetFile.deleteOnExit()

                JasperExportManager.exportReportToPdfFile(jasperPrint, targetFile.absolutePath)

                with(coc) {
                    localCocFile = targetFile.readBytes()
                    localCocFileName = targetFile.name
                }
                diDaoServices.saveCoc(coc)

                return targetFile
            }
        }
        return null
    }

    fun assembleCocItemReportInput(items: List<CdItemDetailsEntity>): LocalCocItemsReportInput {
        val itemDataSource = JRBeanCollectionDataSource(items, false)
        var localCocItemsReportInput = LocalCocItemsReportInput()
        localCocItemsReportInput.itemDataSource = itemDataSource

        return localCocItemsReportInput
    }

    fun generateLocalCoRReport(cdDetails: ConsignmentDocumentDetailsEntity, filePath: String): File? {
        var map = hashMapOf<String, Any>()
        map["imagePath"] = logoImageFile

        map = diDaoServices.createLocalCorReportMap(cdDetails)

        val file = ResourceUtils.getFile(filePath)
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)

        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, JREmptyDataSource())

        val fileName: String = cdDetails.ucrNumber.plus("-cor-report.pdf")

        val targetFile = File(Files.createTempDir(), fileName)
        targetFile.deleteOnExit()

        JasperExportManager.exportReportToPdfFile(jasperPrint, targetFile.absolutePath)

        return targetFile
    }

    fun generateEmailPDFReport(fileName: String, map: HashMap<String, Any>, filePath: String): File? {

        val file = ResourceUtils.getFile(filePath)
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)

        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, JREmptyDataSource())

        val targetFile = File(Files.createTempDir(), fileName)
        targetFile.deleteOnExit()

        JasperExportManager.exportReportToPdfFile(jasperPrint, targetFile.absolutePath)

        return targetFile
    }

}

