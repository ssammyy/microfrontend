package com.letar.realetar.Controllers.Data

import com.letar.realetar.DataClasses.Uploads
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException
import java.io.IOException


@RestController
@RequestMapping("/realetar/uploads")
class ExcelController {
    @PostMapping("/upload-excel")
    fun uploadExcel(@RequestBody file: MultipartFile): List<Uploads.ExcelData> {
        val excelDataList = mutableListOf<Uploads.ExcelData>()

        try {
            val workbook = XSSFWorkbook(ByteArrayInputStream(file.bytes))
            val sheet = workbook.getSheetAt(0)
            for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                val row = sheet.getRow(rowIndex)
                val name = row.getCell(0)?.stringCellValue ?: ""
                val age = row.getCell(1)?.numericCellValue?.toInt() ?: 0
                val email = row.getCell(2)?.stringCellValue ?: ""
                excelDataList.add(Uploads.ExcelData(name, age, email))
            }
            workbook.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return excelDataList
    }
}
