package org.kebs.app.kotlin.apollo.api.controllers

import org.kebs.app.kotlin.apollo.store.repo.ISchemesOfSupervisionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/scheme-of-supervision-cl")
class SchemeOfSupervisionCustomer(
        private val schemesRepo: ISchemesOfSupervisionRepository
) {

    @GetMapping("")
    fun getSchemes(model: Model): String {
        model.addAttribute("schemes", schemesRepo.findAll())
        return "quality-assurance/customer-schemes"
    }

    @GetMapping("/{id}")
    fun singleScheme(model: Model, @PathVariable("id") id: Long): String {
        model.addAttribute("scheme", schemesRepo.findByIdOrNull(id))
        return "quality-assurance/single-scheme"
    }
}