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

package org.kebs.app.kotlin.apollo.api.handlers

//import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.kra.StandardsLevyDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.api.security.service.CustomAuthenticationProvider
import org.kebs.app.kotlin.apollo.common.dto.*
import org.kebs.app.kotlin.apollo.common.dto.kra.request.RootMsg
import org.kebs.app.kotlin.apollo.common.dto.qa.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.model.std.SampleSubmissionDTO
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaInvoiceMasterDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaProcessStatusRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.validation.Validator
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.badRequest
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull
import java.text.SimpleDateFormat


@Component
class QualityAssuranceInternalUserHandler(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val qaDaoServices: QADaoServices,



    ) {


    final val appId: Int = applicationMapProperties.mapQualityAssurance


    /*:::::::::::::::::::::::::::::::::::::::::::::START INTERNAL USER FUNCTIONALITY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


    fun notSupported(req: ServerRequest): ServerResponse = badRequest().body("Invalid Request: Not supported")

    fun getAllMyTaskList(req: ServerRequest): ServerResponse {
        return try {
            val page = commonDaoServices.extractPageRequest(req)
            qaDaoServices.findLoggedInUserTask(page)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }

    fun getPermitDetails(req: ServerRequest): ServerResponse {
        return try {
            val permitID = req.paramOrNull("permitID")?.toLong() ?: throw ExpectedDataNotFound("Required Permit ID, check config")
            qaDaoServices.findPermitDetails(permitID)
                .let {
                    ServerResponse.ok().body(it)
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "UNKNOWN_ERROR")
        }
    }








    /*:::::::::::::::::::::::::::::::::::::::::::::END INTERNAL USER FUNCTIONALITY:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/


}



