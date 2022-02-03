package org.kebs.app.kotlin.apollo.api.ports.provided.dao


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
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream


@Service
class ReportsDaoService(
        private val applicationMapProperties: ApplicationMapProperties,
        private val resourceLoader: ResourceLoader,
        private val commonDaoServices: CommonDaoServices,
        private val invoiceDaoService: InvoiceDaoService
) {


    fun addBankAndMPESADetails(map: HashMap<String, Any>, mpesaAccountNumber: String): HashMap<String, Any> {
        //Get MPESA Logo
        val logoMpesaImageResource = resourceLoader.getResource(applicationMapProperties.mapMPESALogoPath)
        val logoMpesaImageFile = logoMpesaImageResource.file.toString()

        val mpesaDetails = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapMpesaDetails)
        val bank1Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankOneDetails)
        val bank2Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankTwoDetails)
        val bank3Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankThreeDetails)
//        val map = hashMapOf<String, Any>()


        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)
        map["mpesaLogo"] = logoMpesaImageFile
        map["paybillNo"] = mpesaDetails.payBillNo.toString()
        map["mpesaACNo"] = mpesaAccountNumber
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

        map["bankName3"] = bank3Details.bankAccountName.toString()
        map["bank3"] = bank3Details.bankName.toString()
        map["bankBranch3"] = bank3Details.bankBranch.toString()
        map["kesAcNo3"] = bank3Details.bankAccountKesNumber.toString()
        map["usdNo3"] = bank3Details.bankAccountUsdNumber.toString()
        map["bankCode3"] = bank3Details.bankCode.toString()
        map["branchCode3"] = bank3Details.branchCode.toString()
        map["swiftCode3"] = bank3Details.swiftCode.toString()

        return map
    }

    fun extractReport(
            map: HashMap<String, Any>,
            filePath: String,
            listCollect: List<Any>
    ): ByteArrayOutputStream {
//        map["imagePath"] = logoImageFile
        val dataSource = JRBeanCollectionDataSource(listCollect)
        // Handle classpath resource
        val file: InputStream?
        if (filePath.startsWith("classpath:")) {
            file = resourceLoader.getResource(filePath).inputStream
        } else {
            file = ResourceUtils.getFile(filePath).inputStream()
        }
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)
        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource)
        val pdfExporter = JRPdfExporter()
        val pdfReportStream = ByteArrayOutputStream()
        pdfExporter.setExporterInput(SimpleExporterInput(jasperPrint))
        pdfExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
        pdfExporter.exportReport()
        return pdfReportStream
    }

    /*
    Note: Use this method if your report contains multiple data bands and you have not set any fields in the report,
    i.e you're using Parameters only.
     */
    fun extractReportEmptyDataSource(map: HashMap<String, Any>, filePath: String): ByteArrayOutputStream {
//        map["imagePath"] = logoImageFile
        // Handle classpath resource
        val file: InputStream?
        if (filePath.startsWith("classpath:")) {
            file = resourceLoader.getResource(filePath).inputStream
        } else {
            file = ResourceUtils.getFile(filePath).inputStream()
        }
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)
        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, JREmptyDataSource())
        val pdfExporter = JRPdfExporter()
        val pdfReportStream = ByteArrayOutputStream()
        pdfExporter.setExporterInput(SimpleExporterInput(jasperPrint))
        pdfExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
        pdfExporter.exportReport()


        return pdfReportStream

    }

    /*
   Note: Use this method if your report contains multiple data bands and you have not set any fields in the report,
   i.e you're using Parameters only.
    */
    fun extractReportMapDataSource(map: HashMap<String, Any>, filePath: String, data: HashMap<String, List<Any>>): ByteArrayOutputStream {
        val file: InputStream?
        if (filePath.startsWith("classpath:")) {
            file = resourceLoader.getResource(filePath).inputStream
        } else {
            file = ResourceUtils.getFile(filePath).inputStream()
        }
        KotlinLogging.logger { }.info("COC ITEMS: $data")
        val dataSources: MutableMap<String, JRBeanCollectionDataSource?> = HashMap()
        for (k in data.keys) {
            dataSources[k] = JRBeanCollectionDataSource(data[k], false)
        }
        val dataSourceMap = JRMapArrayDataSource(arrayOf(dataSources))
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)
        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSourceMap)
        val pdfExporter = JRPdfExporter()
        val pdfReportStream = ByteArrayOutputStream()
        pdfExporter.setExporterInput(SimpleExporterInput(jasperPrint))
        pdfExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
        pdfExporter.exportReport()
        return pdfReportStream

    }

    fun createFileFromBytes(pdfStream: ByteArrayOutputStream, filePath: String): File? {
        val targetFile = File(filePath)
        targetFile.writeBytes(pdfStream.toByteArray())
        return targetFile
    }


    fun generateEmailPDFReportWithDataSource(
            fileName: String,
            map: HashMap<String, Any>,
            filePath: String,
            dataSourceList: List<Any>
    ): File {

        val file = ResourceUtils.getFile(filePath)
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)
        val dataSource = JRBeanCollectionDataSource(dataSourceList)

        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource)

        val targetFile = File(fileName)

        JasperExportManager.exportReportToPdfFile(jasperPrint, targetFile.absolutePath)

        return targetFile
    }

    fun generateEmailPDFReportWithNoDataSource(fileName: String, map: HashMap<String, Any>, filePath: String): File? {

        val file = ResourceUtils.getFile(filePath)
        val design = JRXmlLoader.load(file)
        val jasperReport = JasperCompileManager.compileReport(design)


        val jasperPrint = JasperFillManager.fillReport(jasperReport, map, JREmptyDataSource())

        val targetFile = File(fileName)

        JasperExportManager.exportReportToPdfFile(jasperPrint, targetFile.absolutePath)

        return targetFile
    }

}

