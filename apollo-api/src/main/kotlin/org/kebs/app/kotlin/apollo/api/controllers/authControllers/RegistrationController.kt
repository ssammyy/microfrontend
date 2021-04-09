/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.api.controllers.authControllers


//import org.flowable.engine.RuntimeService
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.ManufacturePlantDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid


@Controller
@RequestMapping("/api/auth/")
class RegisterController(
        applicationMapProperties: ApplicationMapProperties,
        private val commonDaoServices: CommonDaoServices,
        private val sendToKafkaQueue: SendToKafkaQueue,
        private val serviceMapsRepository: IServiceMapsRepository,
        private val userTypesEntityRepository: IUserTypesEntityRepository,
        private val daoServices: RegistrationDaoServices,
        private val manufacturePlantRepository: IManufacturePlantDetailsRepository,
        private val usersRepo: IUserRepository

//        private val runtimeService: RuntimeService


) {


    private val profilePageDetails = "redirect:/api/user/user-profile?userName="
    final val manufacturerRegAppId: Int = applicationMapProperties.mapManufacturerRegistration

//    private fun loadUIComponents(s: ServiceMapsEntity, model: Model, app: Int): Model {
//
//        model.addAttribute("businessLines", businessLinesRepository.findByStatus(s.activeStatus))
//        model.addAttribute("businessNatures", businessNatureRepository.findByStatus(s.activeStatus))
//        model.addAttribute("contactTypes", contactTypesRepository.findByStatus(s.activeStatus))
//        model.addAttribute("userTypes", userTypesEntityRepository.findByStatus(s.activeStatus))
//        model.addAttribute("app", app)
//        return model
//    }

    @PostMapping("/signup/employee/save")
    fun registerEmployee(
            model: Model,
            @RequestParam(value = "appId", required = false) appId: Int,
            @ModelAttribute("userProfilesEntity") userProfilesEntity: UserProfilesEntity,
            @ModelAttribute("usersEntity") usersEntity: UsersEntity,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String? {

        var result: ServiceRequestsEntity?

        appId.let {
            serviceMapsRepository.findByIdAndStatus(it, 1)
                    ?.let { s ->

//                                var proc = runtimeService.startProcessInstanceByKey(s.bpmnProcessKey, variables)


                        result = daoServices.registerEmployee(s, usersEntity, userProfilesEntity)
                        return when (result?.status) {
                            s.successStatus -> s.successNotificationUrl
                            else -> s.failureNotificationUrl

                        }

                    }
                    ?: throw ServiceMapNotFoundException("No service map found for appId=$it, aborting")

        }


    }

    @PostMapping("/add/plant-details/save")
    fun addManufacturePlantDetails(
            model: Model,
            @RequestParam(value = "manufactureID") manufactureID: Long,
            @ModelAttribute("manufacturePlantDetailsEntity") manufacturePlantDetailsEntity: ManufacturePlantDetailsEntity,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {

        commonDaoServices.serviceMapDetails(manufacturerRegAppId)
                .let { map ->
                    commonDaoServices.loggedInUserDetails()
                            .let { loggedInUser ->
                                addPlantDetailsManufacture(manufactureID,manufacturePlantDetailsEntity, map, loggedInUser)
                                return """$profilePageDetails${loggedInUser.userName}"""
                            }
                }
    }

    private fun addPlantDetailsManufacture(manufactureID: Long, manufacturePlantDetailsEntity: ManufacturePlantDetailsEntity, map: ServiceMapsEntity, loggedInUser: UsersEntity) : Boolean{
        with(manufacturePlantDetailsEntity) {
            manufactureId = commonDaoServices.findManufactureWithID(manufactureID).id
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            createdOn = commonDaoServices.getTimestamp()
            createdBy = loggedInUser.userName
        }
        manufacturePlantRepository.save(manufacturePlantDetailsEntity)
        return true
    }


    @PostMapping("/signup/manufacturer/save")
    fun registerManufacturer(
            model: Model,
            @RequestParam(value = "userTypeId", required = false) userTypeId: Int?,
            @RequestParam(value = "userLoggedInId", required = true) userLoggedInId: Long,
            @RequestParam(value = "appId", required = false) appId: Int?,
            @ModelAttribute("manufacturersEntity") @Valid manufacturersEntity: ManufacturersEntity,
            @ModelAttribute("manufacturerContactEntity") @Valid manufacturerContactsEntity: ManufacturerContactsEntity,
            @ModelAttribute("manufacturerAddressesEntity") @Valid manufacturerAddressesEntity: ManufacturerAddressesEntity,
            @ModelAttribute("yearlyTurnoverEntity") yearlyTurnoverEntity: ManufacturePaymentDetailsEntity,
            @ModelAttribute("stdLevyNotificationFormEntity") stdLevyNotificationFormEntity: StdLevyNotificationFormEntity,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String? {


//        var result = "redirect:/api/auth/signup/notification/success"
        var result: ServiceRequestsEntity?

        appId?.let {
            serviceMapsRepository.findByIdAndStatus(it, 1)
                    ?.let { s ->
//                                when {
//                                    results.hasErrors() -> {
//                                        result = "redirect:/api/auth/signup/manufacturer"
//                                    }
//                                }
//                                val variables = mutableMapOf<String, Any?>()
//                                variables["map"] = s
//                                variables["contact"] = manufacturerContactsEntity
//                                variables["manufacturer"] = manufacturersEntity
//                                variables["address"] = manufacturerAddressesEntity
//                                variables["userTypeId"] = userTypeId
//                                runtimeService.startProcessInstanceByKey(s.bpmnProcessKey, variables)
                        result = daoServices.registerManufacturer(s, manufacturerContactsEntity, stdLevyNotificationFormEntity, manufacturersEntity, manufacturerAddressesEntity, userTypeId, userLoggedInId, yearlyTurnoverEntity)
                        return when (result?.status) {
                            s.successStatus -> s.successNotificationUrl
                            else -> s.failureNotificationUrl
                        }

                    }
                    ?: throw ServiceMapNotFoundException("No service map found for appId=$it, aborting")

        }
                ?: throw ServiceMapNotFoundException("Empty and/or Invalid Application Id Received, aborting")


    }

    @PostMapping("/signup/importer/save")
    fun registerImporterExporter(
            model: Model,
            @RequestParam(value = "userLoggedInId", required = false) userLoggedInId: Long,
            @RequestParam(value = "appId", required = false) appId: Int?,
            @ModelAttribute("importerContactDetailsEntity") @Valid  importerContactDetailsEntity: ImporterContactDetailsEntity,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String? {

        var result: ServiceRequestsEntity?

        appId?.let {
            serviceMapsRepository.findByIdAndStatus(it, 1)
                    ?.let { s ->
                        result = daoServices.registerImporter(s, importerContactDetailsEntity, userLoggedInId)
                        return when (result?.status) {
                            s.successStatus -> s.successNotificationUrl
                            else -> s.failureNotificationUrl
                        }

                    }
                    ?: throw ServiceMapNotFoundException("No service map found for appId=$it, aborting")

        }
                ?: throw ServiceMapNotFoundException("Empty and/or Invalid Application Id Received, aborting")


    }

//   @PostMapping("/signup/epra/save")
//    fun registerEPRA(
//            model: Model,
//            @RequestParam(value = "userLoggedInId", required = false) userLoggedInId: Long,
//            @RequestParam(value = "appId", required = false) appId: Int?,
//            @ModelAttribute("importerExporterContactDetailsEntity") @Valid importerExporterContactDetailsEntity: ImporterExporterContactDetailsEntity,
//            @ModelAttribute("cdImporterDetailsEntity") @Valid cdImporterDetailsEntity: CdImporterDetailsEntity,
//            results: BindingResult,
//            redirectAttributes: RedirectAttributes
//    ): String? {
//
//        var result: ServiceRequestsEntity?
//
//        appId?.let {
//            serviceMapsRepository.findByIdAndStatus(it, 1)
//                    ?.let { s ->
//                        result = daoServices.registerImporter(s, importerExporterContactDetailsEntity, cdImporterDetailsEntity, userLoggedInId)
//                        return when (result?.status) {
//                            s.successStatus -> s.successNotificationUrl
//                            else -> s.failureNotificationUrl
//                        }
//
//                    }
//                    ?: throw ServiceMapNotFoundException("No service map found for appId=$it, aborting")
//
//        }
//                ?: throw ServiceMapNotFoundException("Empty and/or Invalid Application Id Received, aborting")
//
//
//    }

    @PostMapping("/signup/user/save")
    fun registerUser(
            model: Model,
            @ModelAttribute("usersEntity") usersEntity: UsersEntity,
            @ModelAttribute("userProfilesEntity") userProfilesEntity: UserProfilesEntity?,
            @RequestParam(value = "appId", required = false) appId: Int?,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String? {

        var result: ServiceRequestsEntity?

        appId?.let {
            serviceMapsRepository.findByIdAndStatus(it, 1)
                    ?.let { s ->
                        KotlinLogging.logger { }.info { "My Service Request map Id:  = ${s.id}" }
                        usersEntity.confirmUserType
                                ?.let { userTypeId ->
                                    userTypesEntityRepository.findByIdOrNull(userTypeId)
                                            ?.let { userTypeEntity ->
                                                usersEntity.email?.let { it1 ->
                                                    usersRepo.findByEmail(it1)
                                                            .let { checkUsersEntity ->
                                                                return if (checkUsersEntity !== null) {
//                                                                    if (userTypeEntity.id == 5){
//                                                                        "redirect:/api/auth/signup/user?message=exists&messageUser=Employee"
//                                                                    }else{
                                                                        "redirect:/api/auth/signup/user?message=exists"
//                                                                    }
                                                                } else {
                                                                    result = daoServices.registerUser(s, usersEntity, userProfilesEntity, userTypeEntity.id)
                                                                    when (result?.status) {
                                                                        s.successStatus -> s.successNotificationUrl
                                                                        else -> s.failureNotificationUrl
                                                                    }
                                                                }

                                                            }
                                                }


                                            }
                                }

                    }
                    ?: throw ServiceMapNotFoundException("No service map found for appId=$it, aborting")

        }
                ?: throw ServiceMapNotFoundException("Empty and/or Invalid Application Id Received, aborting")


    }


//    @GetMapping("/forgot-password")
//    fun forgotPasswordUI(model: Model, email: String): String {
//        model.addAttribute("forgotPasswordForm", email)
//        return "auth/forgot-pass"
//    }

    @PostMapping("/forgot-password")
    fun forgotPassword(
            @ModelAttribute("forgotPasswordForm") email: String,
            redirectAttributes: RedirectAttributes
    ): String {

        /*
        This method is responsible for receiving user input(email) during the password reset process.
        It generates a token if the user with the said email exists and sends that token as part of a link to the user through the email.
         */

//        validEmail = email
        // SEND TO KAFKA GOES HERE!!!
//        sendToKafkaQueue.submitASyncRequestToBus(validEmail, "topic")
        redirectAttributes.addFlashAttribute("message", "We are validating your details. Check your Email.")
        return "redirect:/forgot-password"
    }

//    @GetMapping("/create-password/{token}")
//    fun createPasswordUI(model: Model, userEntity: UsersEntity): String {
//        model.addAttribute("passwordForm", userEntity)
//        return "auth/create-pass"
//    }


    @PostMapping("/create-password/{token}")
    fun createPassword(
            @ModelAttribute("passwordForm") userDetails: UsersEntity,
            redirectAttributes: RedirectAttributes, @PathVariable token: String
    ): String {

        /*
        This method is responsible for receiving user input(email) during the password reset process.
        It generates a token if the user with the said email exists and sends that token as part of a link to the user through the email.
         */

        when {
            userDetails.credentials != userDetails.confirmCredentials -> {
                redirectAttributes.addFlashAttribute("message", "Passwords don't match!")
                return "redirect:/create-password/{token}"
            }

            // STEPS!!
            /*
            1. Get user based on token through kafka
            2. Assign userDetails passwords to userEntity from 1 above.
             */

            // SEND TO KAFKA GOES HERE!!!
            else -> {
                userDetails.credentials = BCryptPasswordEncoder().encode(userDetails.confirmCredentials)

                // STEPS!!
                /*
            1. Get user based on token through kafka
            2. Assign userDetails passwords to userEntity from 1 above.
             */


//                validEmail = base64.decode(token).toString()
//                userDetails.email = validEmail

                // SEND TO KAFKA GOES HERE!!!
                sendToKafkaQueue.submitAsyncRequestToBus(userDetails, "topic")

                redirectAttributes.addFlashAttribute("message", "Passwords updated successfully")
                return "redirect:/create-password/{token}"
            }
        }

    }

    @GetMapping("/register-employee")
    fun employeeRegistrationUI(model: Model, userEntity: UsersEntity): String {
        model.addAttribute("employeeSignUpForm", userEntity)
        return "auth/employee-registration"
    }

    @PostMapping("/create-user-emp")
    fun createUserEmployee(
            @ModelAttribute("employeeSignUpForm") userDetails: UsersEntity,

            redirectAttributes: RedirectAttributes
    ): String {

        /*
        This method is responsible for creation of a valid user in the DB and AD
        once all the validation check have been performed and passed
        */



        userDetails.credentials = BCryptPasswordEncoder().encode(userDetails.confirmCredentials)

        // SEND TO KAFKA GOES HERE!!!
        sendToKafkaQueue.submitAsyncRequestToBus(userDetails, "topic")
        redirectAttributes.addFlashAttribute("message", "Check your Email.")
        return "redirect:/register-employee"
    }
}
