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

import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ImporterDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Component
class ManufacturerHandler(
        private val applicationMapProperties: ApplicationMapProperties,

        private val sendToKafkaQueue: SendToKafkaQueue,
        private val countriesRepository: ICountriesRepository,
        private val serviceMapsRepository: IServiceMapsRepository,
        private val daoServices: ImporterDaoServices,
//        private val mainRfcCocRepo: IMainRfcCocRepository,
//        private val mainRfcCoiRepo: IMainRfcCoiRepository,
//        private val mainRfcCoiItemsRepo: IMainRfcCoiItemsRepository,
        private val businessLinesRepository: IBusinessLinesRepository,
        private val businessNatureRepository: IBusinessNatureRepository,
        private val contactTypesRepository: IContactTypesRepository,
        private val userTypesRepo: IUserTypesEntityRepository,
        private val SubSectionsLevel1Repo: ISubSectionsLevel1Repository,
        private val SubSectionsLevel2Repo: ISubSectionsLevel2Repository,
        private val titlesRepository: ITitlesRepository,
        private val divisionsRepo: IDivisionsRepository,
        private val departmentsRepo: IDepartmentsRepository,
        private val directorateRepo: IDirectoratesRepository,
        private val designationRepository: IDesignationsRepository,
        private val sectionsRepo: ISectionsRepository,
        private val subRegionsRepository: ISubRegionsRepository,
        private val regionsRepository: IRegionsRepository,
        private val usersRepo: IUserRepository,
        private val userProfilesRepo: IUserProfilesRepository,
        private val userVerificationTokensRepository: IUserVerificationTokensRepository
) {
    final val appId: Int = applicationMapProperties.mapImporterDetails

    //    private final val importerHomePage = "destination-inspection/importer-documents/home"
    private final val rfcForCocPageList = "destination-inspection/importer-documents/rfc-list"
    private final val rfcDetails = "destination-inspection/importer-documents/rfc-detail"

    //    private final val rfcCOIItemDetails = "destination-inspection/importer-documents/rfc-detail"
    private final val rfcForCocPage = "destination-inspection/importer-documents/rfc-coc"
    private final val rfcForCoiPage = "destination-inspection/importer-documents/rfc-coi"
    private final val manufacturerProfile = "quality-assurance/customer/importer-documents/manufacture_profile_view"


    private val redirectAttributes: RedirectAttributes? = null
    private final val activeStatus: Int = 1
    private final val inActiveStatus: Int = 0
    private final lateinit var viewType: String


//    fun home(req: ServerRequest): ServerResponse = ok().render(importerHomePage)

//    fun rfcList(req: ServerRequest): ServerResponse =
//            serviceMapsRepository.findByIdAndStatus(appId, activeStatus)
//                    ?.let { map ->
//                        SecurityContextHolder.getContext().authentication?.name
//                                ?.let { username ->
//                                    usersRepo.findByUserName(username)
//                                            ?.let { loggedInUser ->
//                                                viewType = daoServices.findViewType(req)
//                                                req.attributes()["rfcLists"] = daoServices.findByViewTypeRfCList(loggedInUser, viewType, activeStatus)
//                                                req.attributes()["ViewType"] = viewType
//                                                req.attributes()["map"] = map
//
//                                                return ok().render(rfcForCocPageList, req.attributes())
//                                            }
//                                }
//                                ?: throw Exception("Missing [username=${SecurityContextHolder.getContext().authentication?.name}, does not exist")
//
//                    }
//                    ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")


//    fun manufacturePermitDetails(req: ServerRequest): ServerResponse =
//            serviceMapsRepository.findByIdAndStatus(appId, activeStatus)
//                    ?.let { map ->
//                        req.attributes().putAll(loadManufacturerUIComponents(map))
//                        req.attributes()["map"] = map
//
//                        return ok().render(manufacturerProfile, req.attributes())
//                    }
//                    ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")
//
//    private fun loadManufacturerUIComponents(s: ServiceMapsEntity): MutableMap<String, Any> {
//
//        return mutableMapOf(
//                Pair("country", countriesRepository.findByStatus(s.activeStatus)),
//                Pair("rfcCOIEntity", ManufacturersEntity()),
//                Pair("rfcCOCEntity", MainRfcCocEntity()),
//                Pair("rfcCOREntity", MainRfcCorEntity()),
//                Pair("rfcCoiItemEntity", MainRfcCoiItemsEntity())
//        )
//    }


}