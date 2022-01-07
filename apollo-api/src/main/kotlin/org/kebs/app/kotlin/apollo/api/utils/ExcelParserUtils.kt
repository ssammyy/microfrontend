package org.kebs.app.kotlin.apollo.api.utils

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocApplicationProductsEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocExceptionIndustrialSparesCategoryEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocExceptionMainMachineryCategoryEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocExceptionRawMaterialCategoryEntity
import java.io.IOException
import java.io.InputStream
import java.util.*


object ExcelParserUtils {
    private val dataFormatter = DataFormatter()
    fun parseExcelFile(`is`: InputStream?) : MutableList<PvocApplicationProductsEntity> {
        val lstProducts: MutableList<PvocApplicationProductsEntity> = ArrayList<PvocApplicationProductsEntity>()
        return try {
            val workbook: Workbook = XSSFWorkbook(`is`)
            val productsSheet = workbook.getSheet("PRODUCTS")
            val productRows: Iterator<Row> = productsSheet.iterator()
            var rowNumber = 0
            while (productRows.hasNext()) {
                val currentRow = productRows.next()
                // skip header
                if (rowNumber == 0) {
                    rowNumber++
                    continue
                }

                val cellsInRow: Iterator<Cell> = currentRow.iterator()
                val cust = PvocApplicationProductsEntity()
                var cellIndex = 0

                while (cellsInRow.hasNext()) {
                    val currentCell = cellsInRow.next()
                    when (cellIndex) {

                        1 -> { // HS code
                            cust.productName = dataFormatter.formatCellValue(currentCell)
                        }
                        2 -> { // Description
                            cust.brand = dataFormatter.formatCellValue(currentCell)
                        }
                        3 -> { // end product
                            cust.kebsStandardizationMarkPermit = dataFormatter.formatCellValue(currentCell)
                        }
                        4 -> { // duty rate
                            cust.expirelyDate = dataFormatter.formatCellValue(currentCell)
                        }
                    }
                    cellIndex++
                }
                lstProducts.add(cust)
            }
            workbook.close()
            lstProducts
        } catch (e: IOException) {
            throw RuntimeException("FAIL! -> message = " + e.message)
        }
    }
    fun parseExcelFile2(`is`: InputStream?) : MutableList<PvocExceptionRawMaterialCategoryEntity> {
        val lstRawMaterial = mutableListOf<PvocExceptionRawMaterialCategoryEntity>()
        return try {
            val workbook: Workbook = XSSFWorkbook(`is`)
            val rawMaterialSheet = workbook.getSheet("RAW MATERIALS")
            val rawMaterialRows: Iterator<Row> = rawMaterialSheet.iterator()
            var rowNumber = 0
            while (rawMaterialRows.hasNext()) {
                val currentRow = rawMaterialRows.next()
                // skip header
                if (rowNumber == 0) {
                    rowNumber++
                    continue
                }
                val cellsInRow: Iterator<Cell> = currentRow.iterator()
                val cust = PvocExceptionRawMaterialCategoryEntity()
                var cellIndex = 0
                while (cellsInRow.hasNext()) {
                    val currentCell = cellsInRow.next()
                    when (cellIndex) {
                        1 -> { // HS code
                            cust.hsCode =   dataFormatter.formatCellValue(currentCell)
                        }
                        2 -> { // Description
                            cust.rawMaterialDescription = dataFormatter.formatCellValue(currentCell)
                        }
                        3 -> { // end product
                            cust.endProduct = dataFormatter.formatCellValue(currentCell)
                        }
                        4 -> { // duty rate
//                            cust.dutyRate = dataFormatter.formatCellValue(currentCell)
                            cust.dutyRate = dataFormatter.formatCellValue(currentCell).toString().toLong()
                        }
                    }
                    cellIndex++
                }
               lstRawMaterial.add(cust)
            }
            // Close WorkBook
            workbook.close()
            lstRawMaterial

        } catch (e: IOException) {
            throw RuntimeException("FAIL! -> message = " + e.message)
        }
    }
    fun parseExcelFile3(`is`: InputStream?) : MutableList<PvocExceptionMainMachineryCategoryEntity> {
        val lstMainMachinery = mutableListOf<PvocExceptionMainMachineryCategoryEntity>()
        return try {
            val workbook: Workbook = XSSFWorkbook(`is`)
            val mainMachinerySheet = workbook.getSheet("MACHINERY")
            val mainMachineryRows: Iterator<Row> = mainMachinerySheet.iterator()
            var rowNumber = 0
            while (mainMachineryRows.hasNext()) {
                val currentRow = mainMachineryRows.next()
                // skip header
                if (rowNumber == 0) {
                    rowNumber++
                    continue
                }
                val cellsInRow: Iterator<Cell> = currentRow.iterator()
                val cust = PvocExceptionMainMachineryCategoryEntity()
                var cellIndex = 0
                while (cellsInRow.hasNext()) {
                    val currentCell = cellsInRow.next()
                    when (cellIndex) {
                        1 -> { // Hs code
                            cust.hsCode = dataFormatter.formatCellValue(currentCell)
                        }
                        2 -> { // Machine Description
                            cust.machineDescription = dataFormatter.formatCellValue(currentCell)
                        }
                        3 -> { // Make and Model
                            cust.makeModel = dataFormatter.formatCellValue(currentCell)
                        }
                        4 -> { // Country of Origin
                            cust.countryOfOrigin = dataFormatter.formatCellValue(currentCell)
                        }
                    }
                    cellIndex++
                }
                lstMainMachinery.add(cust)
            }
            // Close WorkBook
            workbook.close()
            lstMainMachinery

        } catch (e: IOException) {
            throw RuntimeException("FAIL! -> message = " + e.message)
        }
    }
    fun parseExcelFile4(`is`: InputStream?) : MutableList<PvocExceptionIndustrialSparesCategoryEntity> {
        val lstSpares = mutableListOf<PvocExceptionIndustrialSparesCategoryEntity>()
        return try {
            val workbook: Workbook = XSSFWorkbook(`is`)
            val sparesSheet = workbook.getSheet("SPARES")
            val sparesRows: Iterator<Row> = sparesSheet.iterator()
            var rowNumber = 0
            //parsing spares excel
            while (sparesRows.hasNext()) {
                val currentRow = sparesRows.next()
                // skip header
                if (rowNumber == 0) {
                    rowNumber++
                    continue
                }
                val cellsInRow: Iterator<Cell> = currentRow.iterator()
                val cust = PvocExceptionIndustrialSparesCategoryEntity()
                var cellIndex = 0
                while (cellsInRow.hasNext()) {
                    val currentCell = cellsInRow.next()
                    when (cellIndex) {
                        1 -> { // HS code
                            cust.hsCode = dataFormatter.formatCellValue(currentCell)
                        }
                        2 -> { // Industrial Spares
                            cust.industrialSpares = dataFormatter.formatCellValue(currentCell)
                        }
                        3 -> { // Machine to fit
                            cust.machineToFit = dataFormatter.formatCellValue(currentCell)
                        }
                        4 -> { // Origin Country
                            cust.countryOfOrigin = dataFormatter.formatCellValue(currentCell)
                        }
                    }
                    cellIndex++
                }
                lstSpares.add(cust)
            }
            // Close WorkBook
            workbook.close()
            lstSpares

        } catch (e: IOException) {
            throw RuntimeException("FAIL! -> message = " + e.message)
        }
    }

}