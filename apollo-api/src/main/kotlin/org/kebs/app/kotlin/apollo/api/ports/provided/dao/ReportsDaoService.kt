package org.kebs.app.kotlin.apollo.api.ports.provided.dao


import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.xml.JRXmlLoader
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.ByteArrayOutputStream
import javax.servlet.http.HttpServletResponse

@Service
class ReportsDaoService(
        private val applicationMapProperties: ApplicationMapProperties,
        private val resourceLoader: ResourceLoader,
        private val commonDaoServices: CommonDaoServices,
        private val invoiceDaoService: InvoiceDaoService
) {

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

}

