package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import org.kebs.app.kotlin.apollo.api.service.SparesReportService
import org.kebs.app.kotlin.apollo.store.model.PvocExceptionIndustrialSparesCategoryEntity
import org.kebs.app.kotlin.apollo.store.repo.IPvocExceptionIndustrialSparesCategoryEntityRepo
import org.kebs.app.kotlin.apollo.store.repo.IPvocExceptionRawMaterialCategoryEntityRepo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/reports")
class ReportTester(
    private val sparesReportService: SparesReportService,
    private val iPvocExceptionIndustrialSparesCategoryEntityRepo: IPvocExceptionIndustrialSparesCategoryEntityRepo,
    private val iPvocExceptionRawMaterialCategoryEntityRepo: IPvocExceptionRawMaterialCategoryEntityRepo,

) {

    @GetMapping("/spares")
    fun getAllSpares() : MutableIterable<PvocExceptionIndustrialSparesCategoryEntity> {
        return iPvocExceptionIndustrialSparesCategoryEntityRepo.findAll()
    }


    @GetMapping("/download/spares")
    fun downloadSparesReport() : String{
        return sparesReportService.exportReport()
    }

    @GetMapping("/download/raw")
    fun downloadRawReport() : String{
        return sparesReportService.exportRawMaterialReport()
    }

    @GetMapping("/download/main")
    fun downloadMainReport() : String{
        return sparesReportService.exportMachineryReport()
    }

}