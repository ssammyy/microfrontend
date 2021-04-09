package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
import org.kebs.app.kotlin.apollo.api.utils.DummyProduct
import org.kebs.app.kotlin.apollo.common.exceptions.MissingConfigurationException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.PermitTypesEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitTypesEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.PermitApplicationsRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.servlet.http.HttpSession
import javax.validation.Valid


@Controller
@RequestMapping("/api/qa")
class QualityAssuranceController(

        private val daoServices: QualityAssuranceDaoServices
) {
    private val permitList =  "redirect:/api/qa/permits-list?permitTypeID"

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-permit")
    fun saveNewSmark(
            @ModelAttribute("permit") permit: PermitApplicationEntity,
            @RequestParam( "permitTypeID") permitTypeID: Long,
                     model: Model, result: BindingResult): String? {
        if (result.hasErrors()) {
            return "quality-assurance/customer/permit-application"
        } else {
            daoServices.permitSave(permit, daoServices.loggedInUserDetails(), daoServices.serviceMapDetails())
        }
        return "$permitList=$permitTypeID"
    }

}