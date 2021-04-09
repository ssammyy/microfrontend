package org.kebs.app.kotlin.apollo.api.controllers

import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.InvoiceEntity
import org.kebs.app.kotlin.apollo.store.repo.IInvoiceRepository
import org.kebs.app.kotlin.apollo.store.repo.IPermitRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp
import java.time.Instant
import javax.servlet.http.HttpSession

@Controller
class HomeController(
        applicationMapProperties: ApplicationMapProperties
) {

    val defaultApplicationName = applicationMapProperties.applicationName

    @GetMapping("")
    fun home(
            model: Model,
            session:HttpSession
    ): String {
        session.setAttribute("applicationName", defaultApplicationName)
        model.addAttribute("applicationName", defaultApplicationName)
        return "index"
    }


}