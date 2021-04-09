package org.kebs.app.kotlin.apollo.api.controllers

import org.kebs.app.kotlin.apollo.store.repo.IKebsInspectionLabTestsReportFormRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/lab-reports")
class LabReports(
        private val labReportsRepo: IKebsInspectionLabTestsReportFormRepository
) {

    @GetMapping("")
    fun labReports(model: Model): String {
        model.addAttribute("reports", labReportsRepo.findAll())
        return "quality-assurance/customer/lab-reports"
    }

    @GetMapping("/{id}")
    fun reportDetails(model: Model, @PathVariable("id") id: Long): String {
        model.addAttribute("report", labReportsRepo.findByIdOrNull(id))
        return "quality-assurance/customer/lab-report-details"
    }

}