package org.kebs.app.kotlin.apollo.api.utils

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

object ExcelCreatorUtils {
    @Throws(IOException::class)
    fun generateExemptionApplicationExcel(): ByteArrayInputStream {
        val productColumns = arrayOf("SN", "PRODUCT NAME", "BRAND", "KEBS STANDARDIZATION MARK PERMIT NUMBER", "EXPIRY DATE")
        val rawMaterialColumns = arrayOf("SN", "HS CODE", "RAW MATERIAL DESCRIPTION", "END PRODUCT", "DUTY RATE")
        val mainMachineColumns = arrayOf("SN", "HS CODE", "MACHINE DESCRIPTION", "MAKE AND MODEL", "COUNTRY OF ORIGIN")
        val spareColumns = arrayOf("SN", "HS CODE", "INDUSTRIAL SPARES", "MAKE AND MODEL", "MACHINE TO BE FITTED TO","COUNTRY OF ORIGIN")

        var workbook = XSSFWorkbook()
        workbook = createSheet(workbook, "PRODUCTS", productColumns)
        workbook = createSheet(workbook, "RAW MATERIALS", rawMaterialColumns)
        workbook = createSheet(workbook, "MACHINERY", mainMachineColumns)
        workbook = createSheet(workbook, "SPARES", spareColumns)

        ByteArrayOutputStream().use { out ->
            workbook.write(out)
            return ByteArrayInputStream(out.toByteArray())
        }
    }

    fun createSheet(xssfWorkbook: XSSFWorkbook, sheetName: String, columns: Array<String>): XSSFWorkbook {
        val sheet: Sheet = xssfWorkbook.createSheet(sheetName)
        val headerFont: Font = xssfWorkbook.createFont()
        headerFont.bold = true
        headerFont.color = IndexedColors.BLUE.getIndex()
        val headerCellStyle: CellStyle = xssfWorkbook.createCellStyle()
        headerCellStyle.setFont(headerFont)

        // Row for Header
        val headerRow = sheet.createRow(0)
        // Header
        for (col in columns.indices) {
            val cell = headerRow.createCell(col)
            cell.setCellValue(columns[col])
            cell.cellStyle = headerCellStyle
        }
        return xssfWorkbook
    }
}