package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.PvocBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.utils.ExcelParserUtils
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class FileExcelProcessService {

    @Autowired
    lateinit var pvocBpmn: PvocBpmn

    @Autowired
    lateinit var commonDaoServices : CommonDaoServices

    @Autowired
    lateinit var iPvocApplicationProductsRepo: IPvocApplicationProductsRepo

    @Autowired
    lateinit var iPvocApplicationRepo: IPvocApplicationRepo

    @Autowired
    lateinit var iPvocExceptionIndustrialSparesCategoryEntityRepo: IPvocExceptionIndustrialSparesCategoryEntityRepo

    @Autowired
    lateinit var iPvocExceptionMainMachineryCategoryEntityRepo: IPvocExceptionMainMachineryCategoryEntityRepo

    @Autowired
    lateinit var iPvocExceptionRawMaterialCategoryEntityRepo: IPvocExceptionRawMaterialCategoryEntityRepo

    fun store(file: MultipartFile, manufactuer: PvocApplicationEntity) {
        try {
            val lstProducts: List<PvocApplicationProductsEntity> = ExcelParserUtils.parseExcelFile(file.inputStream)
            val lstRawMaterials: List<PvocExceptionRawMaterialCategoryEntity> =
                ExcelParserUtils.parseExcelFile2(file.inputStream)
            val lstMainMachineries: List<PvocExceptionMainMachineryCategoryEntity> =
                ExcelParserUtils.parseExcelFile3(file.inputStream)
            val lstSpares: List<PvocExceptionIndustrialSparesCategoryEntity> =
                ExcelParserUtils.parseExcelFile4(file.inputStream)
            lstProducts.forEach { lstProduct ->
                lstProduct.createdBy = manufactuer.createdBy
                lstProduct.createdOn = Timestamp.from(Instant.now())
            }
            lstRawMaterials.forEach { lstRawMaterial ->
                lstRawMaterial.createdBy = manufactuer.createdBy
                lstRawMaterial.createdOn = Timestamp.from(Instant.now())
            }
            lstMainMachineries.forEach { lstMainMachinery ->
                lstMainMachinery.createdBy = manufactuer.createdBy
                lstMainMachinery.createdOn = Timestamp.from(Instant.now())
            }

            lstSpares.forEach { lstSpare ->
                lstSpare.createdBy = manufactuer.createdBy
                lstSpare.createdOn = Timestamp.from(Instant.now())
            }
            // Save Customers to DataBase
            iPvocApplicationProductsRepo.saveAll(lstProducts)
            KotlinLogging.logger { }.info { "Saved successfully" }

            iPvocExceptionRawMaterialCategoryEntityRepo.saveAll(lstRawMaterials)
            KotlinLogging.logger { }.info { "Saved successfully" }

            iPvocExceptionMainMachineryCategoryEntityRepo.saveAll(lstMainMachineries)
            KotlinLogging.logger { }.info { "Saved successfully" }

            iPvocExceptionIndustrialSparesCategoryEntityRepo.saveAll(lstSpares)
            manufactuer.finished = 1
            iPvocApplicationRepo.save(manufactuer)
            KotlinLogging.logger { }.info { "Saved successfully" }
            manufactuer.id?.let { commonDaoServices.getLoggedInUser()?.id?.let { it1 ->
                pvocBpmn.startPvocApplicationExemptionsProcess(it,
                    it1
                )
            } }
            manufactuer.id?.let { pvocBpmn.pvocEaSubmitApplicationComplete(it, 502) }
//            return "redirect:/api/di/pvoc/officer?currentPage=0&pageSize=10&fromDate=${fro}&toDate=${to}&filter=0"
        } catch (e: IOException) {
            throw Exception(e.message)
        }
    }
}