package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import org.kebs.app.kotlin.apollo.store.repo.IFactoryVisitsRepository
import org.kebs.app.kotlin.apollo.store.repo.IKebsInspectionLabTestsReportFormRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/factory-visit-report")
class FactoryVisitReportController(
        private val factoryVisitRepo: IFactoryVisitsRepository
) {

    @GetMapping("")
    fun getFactoryVisitReports(model: Model): String{
        model.addAttribute("reports", factoryVisitRepo.findAll())
        return "quality-assurance/customer/factory-visit-report"
    }

    @GetMapping("/{id}")
    fun getFactoryVisitReport(model: Model, @PathVariable("id") id: Long): String{
        model.addAttribute("report", factoryVisitRepo.findByIdOrNull(id))
        return "quality-assurance/customer/factory-visit-report"
    }
}