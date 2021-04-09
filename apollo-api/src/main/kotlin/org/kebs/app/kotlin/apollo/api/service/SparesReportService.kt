package org.kebs.app.kotlin.apollo.api.service

import net.sf.jasperreports.engine.JasperExportManager

import net.sf.jasperreports.engine.JasperFillManager

import net.sf.jasperreports.engine.JasperPrint

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

import net.sf.jasperreports.engine.JasperCompileManager

import net.sf.jasperreports.engine.JasperReport

import net.sf.jasperreports.engine.JRException
import org.kebs.app.kotlin.apollo.store.model.PvocExceptionIndustrialSparesCategoryEntity
import org.kebs.app.kotlin.apollo.store.model.PvocExceptionMainMachineryCategoryEntity
import org.kebs.app.kotlin.apollo.store.model.PvocExceptionRawMaterialCategoryEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocExceptionIndustrialSparesCategoryEntityRepo
import org.kebs.app.kotlin.apollo.store.repo.IPvocExceptionMainMachineryCategoryEntityRepo
import org.kebs.app.kotlin.apollo.store.repo.IPvocExceptionRawMaterialCategoryEntityRepo

import java.io.FileNotFoundException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.File


@Service
class SparesReportService {
    @Autowired
    lateinit var iPvocExceptionIndustrialSparesCategoryEntityRepo: IPvocExceptionIndustrialSparesCategoryEntityRepo

    @Autowired
    lateinit var iPvocExceptionMainMachineryCategoryEntityRepo: IPvocExceptionMainMachineryCategoryEntityRepo

    @Autowired
    lateinit var iPvocExceptionRawMaterialCategoryEntityRepo: IPvocExceptionRawMaterialCategoryEntityRepo

    @Throws(FileNotFoundException::class, JRException::class)
    fun exportReport(): String {
        val path = "C:\\Users\\user\\Desktop\\"
        val spares: List<PvocExceptionIndustrialSparesCategoryEntity?> =
            iPvocExceptionIndustrialSparesCategoryEntityRepo.findAll() as List<PvocExceptionIndustrialSparesCategoryEntity?>
        //load file and compile it
        val file: File = ResourceUtils.getFile("classpath:reports/Spares.jrxml")
        val jasperReport: JasperReport = JasperCompileManager.compileReport(file.absolutePath)
        val dataSource = JRBeanCollectionDataSource(spares)
        val parameters: MutableMap<String, Any> = HashMap()
        val jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource)
            JasperExportManager.exportReportToPdfFile(jasperPrint, "$path\\spares.pdf")
        return "report generated in path : $path"
    }

    @Throws(FileNotFoundException::class, JRException::class)
    fun exportRawMaterialReport(): String {
        val path = "C:\\Users\\user\\Desktop\\"
        val rawMats: List<PvocExceptionRawMaterialCategoryEntity?> =
            iPvocExceptionRawMaterialCategoryEntityRepo.findAll() as List<PvocExceptionRawMaterialCategoryEntity?>
        //load file and compile it
        val file: File = ResourceUtils.getFile("classpath:reports/RawMaterial.jrxml")
        val jasperReport: JasperReport = JasperCompileManager.compileReport(file.absolutePath)
        val dataSource = JRBeanCollectionDataSource(rawMats)
        val parameters: MutableMap<String, Any> = HashMap()
        val jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource)
        JasperExportManager.exportReportToPdfFile(jasperPrint, "$path\\rawsMaterial.pdf")
        return "report generated in path : $path"
    }

    @Throws(FileNotFoundException::class, JRException::class)
    fun exportMachineryReport(): String {
        val path = "C:\\Users\\user\\Desktop\\"
        val rawMats: List<PvocExceptionMainMachineryCategoryEntity?> =
            iPvocExceptionMainMachineryCategoryEntityRepo.findAll() as List<PvocExceptionMainMachineryCategoryEntity?>
        //load file and compile it
        val file: File = ResourceUtils.getFile("classpath:reports/MainMachinery.jrxml")
        val jasperReport: JasperReport = JasperCompileManager.compileReport(file.absolutePath)
        val dataSource = JRBeanCollectionDataSource(rawMats)
        val parameters: MutableMap<String, Any> = HashMap()
        val jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource)
        JasperExportManager.exportReportToPdfFile(jasperPrint, "$path\\Machinery.pdf")
        return "report generated in path : $path"
    }
}