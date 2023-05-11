package org.kebs.app.kotlin.apollo.api.ports.provided.dao


import mu.KotlinLogging
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.data.JRMapArrayDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import net.sf.jasperreports.engine.xml.JRXmlLoader
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.*
import javax.servlet.http.HttpServletResponse

enum class PaymentMethodCategory(val code: String) {
    BANK("BANK"), MOBILE_MONEY("MPESA")
}

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


    fun addBankAndMPESADetails(map: HashMap<String, Any>, mpesaAccountNumber: String): HashMap<String, Any> {
        //Get MPESA Logo
        val logoMpesaImageResource = resourceLoader.getResource(applicationMapProperties.mapMPESALogoPath)
        val logoMpesaImageFile = logoMpesaImageResource.file.toString()

        val mpesaDetails = invoiceDaoService.getPaymentMethodtype(PaymentMethodCategory.MOBILE_MONEY.code)
        val banksDetails = invoiceDaoService.listPaymentMethodtype(PaymentMethodCategory.BANK.code)
        // Pick bank details or null to maintain backward compatibility with generated reports
        // FIXME: Make reports not dependent on 3 banks, banks should be dynamic 1,2,3,4...
        val bank1Details = banksDetails.firstOrNull()
        val bank2Details = banksDetails.elementAtOrNull(2)
        val bank3Details = banksDetails.elementAtOrNull(2)


        map["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)
        map["mpesaLogo"] = logoMpesaImageFile
        map["paybillNo"] = mpesaDetails?.payBillNo.toString() ?: ""
        map["mpesaACNo"] = mpesaAccountNumber
        map["vatNo"] = mpesaDetails?.vatNo.toString() ?: ""
        map["pinNo"] = mpesaDetails?.pinNo.toString() ?: ""
//        map["preparedBy"] = demandNote?.
//        map["datePrepared"] = demandNote?.
        map["bankName1"] = bank1Details?.bankAccountName.orEmpty() ?: ""
        map["bank1"] = bank1Details?.bankName.orEmpty() ?: ""
        map["bankBranch1"] = bank1Details?.bankBranch.orEmpty() ?: ""
        map["kesAcNo1"] = bank1Details?.bankAccountKesNumber.orEmpty() ?: ""
        map["usdNo1"] = bank1Details?.bankAccountUsdNumber.orEmpty() ?: ""
        map["bankCode1"] = bank1Details?.bankCode.orEmpty() ?: ""
        map["branchCode1"] = bank1Details?.branchCode.orEmpty() ?: ""
        map["swiftCode1"] = bank1Details?.swiftCode.orEmpty() ?: ""

        map["bankName2"] = bank2Details?.bankAccountName.orEmpty() ?: ""
        map["bank2"] = bank2Details?.bankName.orEmpty() ?: ""
        map["bankBranch2"] = bank2Details?.bankBranch.orEmpty() ?: ""
        map["kesAcNo2"] = bank2Details?.bankAccountKesNumber.orEmpty() ?: ""
        map["usdNo2"] = bank2Details?.bankAccountUsdNumber.orEmpty() ?: ""
        map["bankCode2"] = bank2Details?.bankCode.orEmpty() ?: ""
        map["branchCode2"] = bank2Details?.branchCode.orEmpty() ?: ""
        map["swiftCode2"] = bank2Details?.swiftCode.orEmpty() ?: ""

        map["bankName3"] = bank3Details?.bankAccountName.orEmpty() ?: ""
        map["bank3"] = bank3Details?.bankName.orEmpty() ?: ""
        map["bankBranch3"] = bank3Details?.bankBranch.orEmpty() ?: ""
        map["kesAcNo3"] = bank3Details?.bankAccountKesNumber.orEmpty() ?: ""
        map["usdNo3"] = bank3Details?.bankAccountUsdNumber.orEmpty() ?: ""
        map["bankCode3"] = bank3Details?.bankCode.orEmpty() ?: ""
        map["branchCode3"] = bank3Details?.branchCode.orEmpty() ?: ""
        map["swiftCode3"] = bank3Details?.swiftCode.orEmpty() ?: ""

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
        val file: InputStream = if (filePath.startsWith("classpath:")) {
            resourceLoader.getResource(filePath).inputStream
        } else {
            ResourceUtils.getFile(filePath).inputStream()
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

    fun extractExcelReport(
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
        val xlsxExporter = JRXlsxExporter()
        val pdfReportStream = ByteArrayOutputStream()
        xlsxExporter.setExporterInput(SimpleExporterInput(jasperPrint))
        xlsxExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
        xlsxExporter.exportReport()
        return pdfReportStream
    }

    fun extractExcelReport(
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
        val xlsxExporter = JRXlsxExporter()
        //val pdfExporter = JRPdfExporter()
        val pdfReportStream = ByteArrayOutputStream()
        xlsxExporter.setExporterInput(SimpleExporterInput(jasperPrint))
        xlsxExporter.exporterOutput = SimpleOutputStreamExporterOutput(pdfReportStream)
        xlsxExporter.exportReport()
        response.contentType = "application/excel"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; filename=jasper.html;")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(pdfReportStream.toByteArray())
                responseOutputStream.close()
                pdfReportStream.close()
            }


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
//        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; filename=jasper.html;")
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
    fun extractReportMapDataSource(
        map: HashMap<String, Any>,
        filePath: String,
        data: HashMap<String, List<Any>>
    ): ByteArrayOutputStream {
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
//        response.contentType = "text/html"
        response.contentType = "application/pdf"
        response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Dispostion", "inline; filename=file.pdf;")
//        response.setHeader("Content-Disposition","inline, filename=myReport.html");
        response.outputStream.let { responseOutputStream ->
            responseOutputStream.write(pdfReportStream.toByteArray())
            responseOutputStream.close()
            pdfReportStream.close()
        }

    }

    /**
     * Generate Excel report: https://www.digitalocean.com/community/tutorials/apache-poi-tutorial
     */
    fun extractXlsReport(
        fileName: String,
        reportName: String,
        reportDate: String,
        data: Iterable<Map<String, Any>>,
        fieldMapping: Map<String, String>
    ): FileInputStream {
        var workbook: Workbook? = null

        workbook = if (fileName.endsWith("xlsx")) {
            XSSFWorkbook()
        } else if (fileName.endsWith("xls")) {
            HSSFWorkbook()
        } else {
            throw Exception("invalid file name, should be xls or xlsx")
        }

        val sheet: Sheet = workbook.createSheet(reportName.toUpperCase())
        var rowIndex = 0
        var cell0: Cell
        // Organization row
        val fontOrg = workbook.createFont()
        fontOrg.fontName = "Arial"
        fontOrg.bold = true
        fontOrg.color = IndexedColors.WHITE.getIndex()
//        fontOrg.fontHeight = 18
        val orgStyle: CellStyle = workbook.createCellStyle()
//        orgStyle.fillBackgroundColor = IndexedColors.LIGHT_BLUE.getIndex()
        orgStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
        orgStyle.fillForegroundColor = IndexedColors.SKY_BLUE.getIndex()
        orgStyle.setFont(fontOrg)

        val orgName: Row = sheet.createRow(rowIndex++)
        cell0 = orgName.createCell(2)
        cell0.cellStyle = orgStyle
        cell0.setCellValue("KENYA BUREAU OF STANDARDS")
        val ca = CellRangeAddress(0, 0, 2, 7)
        sheet.addMergedRegion(ca)
        // Report details
        val titleRow: Row = sheet.createRow(rowIndex++)
        cell0 = titleRow.createCell(0)
        cell0.setCellValue("REPORT NAME")
        cell0 = titleRow.createCell(1)
        cell0.setCellValue(reportName.replace("_", " ").toUpperCase())
        val dateRow: Row = sheet.createRow(rowIndex++)
        cell0 = dateRow.createCell(0)
        cell0.setCellValue("REPORT DATE")
        cell0 = dateRow.createCell(1)
        cell0.setCellValue(reportDate)
        val user: Row = sheet.createRow(rowIndex++)
        cell0 = user.createCell(0)
        cell0.setCellValue("GENERATED BY")
        cell0 = user.createCell(1)
        commonDaoServices.loggedInUserDetails().let { usr ->
            cell0.setCellValue("${usr.firstName} ${usr.lastName}")
        }
        // Add header row
        val header: Row = sheet.createRow(rowIndex++)
        val font = workbook.createFont()
        font.fontName = "Arial"
        font.bold = true
        font.color = IndexedColors.WHITE.getIndex()
//        font.fontHeightInPoints = 15
        val headerStyle: CellStyle = workbook.createCellStyle()
        //headerStyle.fillBackgroundColor = IndexedColors.LIGHT_BLUE.getIndex()
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
        headerStyle.fillForegroundColor = IndexedColors.SKY_BLUE.getIndex()
        headerStyle.setFont(font)
        fieldMapping.onEachIndexed { i, v ->
            cell0 = header.createCell(i)
            cell0.cellStyle = headerStyle
            cell0.setCellValue(v.value)
        }
        // Data setting
        val fontText = workbook.createFont()
        font.fontName = "Arial"
        font.bold = false
//        font.fontHeightInPoints = 12
        val dataStyle: CellStyle = workbook.createCellStyle()
        dataStyle.setFont(fontText)

        val iterator = data.iterator()
        while (iterator.hasNext()) {
            val country: Map<String, Any?> = iterator.next()
            val row: Row = sheet.createRow(rowIndex++)
            // ADD row Data
            country.onEachIndexed { i, v ->
                cell0 = row.createCell(i)
                cell0.cellStyle = dataStyle
                if (v.value == null) {
                    cell0.setCellValue("")
                } else {
                    cell0.setCellValue(v.value.toString())
                }
            }
        }

        //lets auto size all columns
        for (j in 0 until fieldMapping.size) {
            sheet.autoSizeColumn(j)
        }
        //lets write the excel data to file now
        val fos = FileOutputStream(fileName)
        workbook.write(fos)
        fos.close()
        println("$fileName written successfully")
        return FileInputStream(fileName)
    }

}

