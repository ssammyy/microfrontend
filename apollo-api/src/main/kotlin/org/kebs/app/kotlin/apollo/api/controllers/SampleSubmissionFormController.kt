/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.api.controllers

import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.store.model.SampleSubmissionFormEntity
import org.kebs.app.kotlin.apollo.store.repo.ISampleSubmissionFormsRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsRepository
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid
import javax.validation.constraints.Email

@RestController
@RequestMapping("/api/sample-insp-form")
class SampleSubmissionFormController(
    private val sendToKafkaQueue: SendToKafkaQueue,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val sampleSubmissionFormsRepository: ISampleSubmissionFormsRepository
) {
    @Email
    lateinit var validEmail: String


    @PostMapping("/apply")
    fun ssFormApp(
        model: Model,
        @ModelAttribute("ssform") @Valid ssForm: SampleSubmissionFormEntity,

        @ModelAttribute("app") app: Int,
        results: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        var result = ""
        serviceMapsRepository.findByIdAndStatus(app, 1)
            ?.let { s ->
                validEmail = ssForm.organizationEmail.toString()

                sendToKafkaQueue.submitAsyncRequestToBus(ssForm, s.serviceTopic)
                redirectAttributes.addFlashAttribute("message", "We are processing your sample data. Check your Email.")
                result = "applicationSuccess"
            }
        return result
    }
}