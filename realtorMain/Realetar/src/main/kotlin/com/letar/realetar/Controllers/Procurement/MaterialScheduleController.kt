package com.letar.realetar.Controllers.Procurement

import com.letar.realetar.DataClasses.ExcelData
import com.letar.realetar.Models.MaterialSchedule
import com.letar.realetar.Services.MaterialScheduleService
import com.letar.realetar.Utilities.Converters
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.IOException

@RestController
@RequestMapping("/realetar/material-schedule")
class MaterialScheduleController (
    private val materialScheduleService: MaterialScheduleService,
    private val converters: Converters
){
    /**
     * EXCEL UPLOAD
     * @param file
     * receive file as MultipartFile
     */
    @PostMapping("/upload-template")
    fun uploadExcel(@RequestBody file: MultipartFile): ResponseEntity<*> {
        val excelDataList = mutableListOf<ExcelData>()
        val excelDataEntities = mutableListOf<MaterialSchedule>()


        try {
            val workbook = XSSFWorkbook(ByteArrayInputStream(file.bytes))
            val sheet = workbook.getSheetAt(0)
            for (rowIndex in 1 until sheet.physicalNumberOfRows) {
                val row = sheet.getRow(rowIndex)
                if (row==null)
                    break
                val structure = row.getCell(1)?.stringCellValue ?: ""
                val element = row.getCell(2)?.stringCellValue ?: ""
                val floor = row.getCell(3)?.stringCellValue ?: ""
                val material = row.getCell(4)?.stringCellValue ?: ""
                val itemName = row.getCell(5)?.stringCellValue ?: ""
                val itemId = row.getCell(6)?.numericCellValue?.toString() ?: ""
                val quantity = row.getCell(7)?.numericCellValue?.toInt()
                val startDate = row.getCell(8)?.stringCellValue ?: ""
                val orderDate = row.getCell(9)?.stringCellValue ?: ""

                excelDataList.add(ExcelData(structure,element,floor,material,itemName,
                    itemId, quantity as Int, converters.formatDate(startDate), converters.formatDate(orderDate)))


            }
            workbook.close()
             materialScheduleService.createMaterialSchedule(excelDataList)
        } catch (e: IOException) {
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O0ps! Check your template and try again.")

        }
        return ResponseEntity.ok().body(excelDataList)
    }





    @GetMapping
    fun getAllMaterialSchedules(): List<MaterialSchedule> {
        return materialScheduleService.getAllMaterialSchedules()
    }

    @GetMapping("/{id}")
    fun getMaterialScheduleById(@PathVariable id: Long): ResponseEntity<MaterialSchedule> {
        val materialSchedule = materialScheduleService.getMaterialScheduleById(id)
        return if (materialSchedule != null) {
            ResponseEntity.ok(materialSchedule)
        } else {
            ResponseEntity.notFound().build()
        }
    }


    @PostMapping
    fun createMaterialSchedule(@RequestBody materialSchedule: MutableList<ExcelData>): MutableList<MaterialSchedule> {
        return materialScheduleService.createMaterialSchedule(materialSchedule)
    }

    @PutMapping("/{id}")
    fun updateMaterialSchedule(
        @PathVariable id: Long,
        @RequestBody materialSchedule: MaterialSchedule
    ): ResponseEntity<MaterialSchedule> {
        val existingMaterialSchedule = materialScheduleService.getMaterialScheduleById(id)
        return if (existingMaterialSchedule != null) {
            val updatedMaterialSchedule = materialScheduleService.updateMaterialSchedule(materialSchedule)
            ResponseEntity.ok(updatedMaterialSchedule)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    @DeleteMapping("/{id}")
    fun deleteMaterialSchedule(@PathVariable id: Long): ResponseEntity<Void> {
        val existingMaterialSchedule = materialScheduleService.getMaterialScheduleById(id)
        return if (existingMaterialSchedule != null) {
            materialScheduleService.deleteMaterialSchedule(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

}







