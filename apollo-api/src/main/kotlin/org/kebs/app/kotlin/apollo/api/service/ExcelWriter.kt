//package org.kebs.app.kotlin.apollo.api.service
//
//import org.apache.poi.ss.usermodel.Cell
//import org.apache.poi.ss.usermodel.Sheet
//import org.apache.poi.ss.usermodel.WorkbookFactory
//import org.apache.poi.xssf.usermodel.XSSFWorkbook
//import org.springframework.stereotype.Service
//import java.io.*
//import java.lang.Exception
//import java.lang.reflect.Method
//import java.util.ArrayList
//
//
//
//
//@Service
//class ExcelWriter {
//    fun <T> writeToExcelInMultiSheets(fileName: String?, sheetName: String?, data: List<T>, fields: List<String>) {
//        var file: File? = null
//        var fos: OutputStream? = null
//        var workbook: XSSFWorkbook? = null
//        try {
//            file = File(fileName)
//            var sheet: Sheet? = null
//            workbook = if (file.exists()) WorkbookFactory.create(FileInputStream(file)) as XSSFWorkbook else {
//                XSSFWorkbook()
//            }
//            sheet = workbook.createSheet(sheetName)
////            val fieldNames = getFieldNamesForClass(clazz = data[0].javaClass)
//            var rowCount = 0
//            var columnCount = 0
//            var row = sheet.createRow(rowCount++)
//            for (fieldName in fields) {
//                val cell: Cell = row.createCell(columnCount++)
//                cell.setCellValue(fieldName)
//            }
//            val classz: Class<out Any> = data[0].javaClass
//            for (t in data) {
//                row = sheet.createRow(rowCount++)
//                columnCount = 0
//                for (fieldName in fieldNames) {
//                    val cell: Cell = row.createCell(columnCount)
//                    var method: Method? = null
//                    method = try {
//                        classz.getMethod("get" + capitalize(fieldName))
//                    } catch (nme: NoSuchMethodException) {
//                        classz.getMethod("get$fieldName")
//                    }
//                    val value = method!!.invoke(t, null as Array<Any?>?)
//                    if (value != null) {
//                        if (value is String) {
//                            cell.setCellValue(value)
//                        } else if (value is Long) {
//                            cell.setCellValue(value as Long?.toDouble())
//                        } else if (value is Int) {
//                            cell.setCellValue(value as Int?.toDouble())
//                        } else if (value is Double) {
//                            cell.setCellValue(value)
//                        }
//                    }
//                    columnCount++
//                }
//            }
//            fos = FileOutputStream(file)
//            workbook.write(fos)
//            fos.flush()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            try {
//                fos?.close()
//            } catch (e: IOException) {
//            }
//            try {
//                workbook?.close()
//            } catch (e: IOException) {
//            }
//        }
//    }
//
//    // retrieve field names from a POJO class
//    @Throws(Exception::class)
//    private fun getFieldNamesForClass(clazz: Class<*>): List<String> {
//        val fieldNames: MutableList<String> = ArrayList()
//        val fields = clazz.declaredFields
//        for (i in fields.indices) {
//            fieldNames.add(fields[i].name)
//        }
//        return fieldNames
//    }
//
//    // capitalize the first letter of the field name for retriving value of the
//    // field later
//    private fun capitalize(s: String): String {
//        return if (s.length == 0) s else s.substring(0, 1).toUpperCase() + s.substring(1)
//    }
//}