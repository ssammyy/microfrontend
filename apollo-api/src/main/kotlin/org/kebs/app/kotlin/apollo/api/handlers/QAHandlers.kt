package org.kebs.app.kotlin.apollo.api.handlers

import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok

@Component
class QAHandlers(
        applicationMapProperties: ApplicationMapProperties,
        private val sendToKafkaQueue: SendToKafkaQueue,
        private val surveyItemsRepo: ISurveyItemRepository,
        private val serviceMapsRepo: IServiceMapsRepository,
        private val departmentRepo: IDepartmentsRepository,
        private val divisionRepo: IDivisionsRepository,
        private val regionsRepository: IRegionsRepository,
        private val countyRepo: ICountiesRepository,
        private val workPlansRepo: IWorkplanRepository,
        private val workPlansUsersRepo: IWorkPlanUsersRepository,
        private val planItemsRepository: IWorkPlanItemsRepository,
        private val planBudgetLinesRepository: IWorkPlanBudgetLinesRepository,
        private val planResourcesRepository: IWorkPlanResourcesRepository,
        private val usersRepo: IUserRepository,
        private val permitRepo: IPermitRepository,
        val townsRepo: ITownsRepository,
        val productBrandRepository: IManufacturerProductBrandRepository
) {
    final val appId = applicationMapProperties.mapPermitApplication
    final val allPermits = "quality-assurance/customer/my-permits"
    final val createPermit = "redirect:/api/v1/permit/apply/new/smark"
    final val currentUserSessionAttribute = "userId"
    private final val errors = mutableMapOf<String, String>()

//    fun listAllPermits(req: ServerRequest): ServerResponse {
//        return serviceMapsRepo.findByIdAndStatus(appId, 1)
//                ?.let { map->
//                    var permits: List<PermitApplicationEntity>? = null
//                    val currentUser: String? = req.session().getAttribute(currentUserSessionAttribute) as String?
//                    var currentPage: Int? = null
//                    var pageSize: Int? = null
//                    req.param("page").ifPresent { p -> currentPage = p.toIntOrNull() }
//                    req.param("size").ifPresent { p -> pageSize = p.toIntOrNull() }
//
//                    val pages: Pageable? = (pageSize ?: map.uiPageSize)?.let { PageRequest.of(currentPage ?: 0, it) }
//                    if (currentUser.isNullOrEmpty()) {
//                        errors["appId"] = "User not found"
//                    } else {
//                        // usersRepo.findFirstByIdAndStatus(currentUser.toLong(), map.activeStatus)
//                        //         ?.let { userId ->
//                        req.attributes()["permits"] = permitRepo.findByStatus(1, pages)
//
//                        val totalPages: Int? = permitRepo.findByStatus(1).size
//                        var pageNumbers: List<Int>? = null
//                        req.attributes()["totalPages"] = totalPages ?: 0
//
//                        totalPages
//                                ?.let { pages ->
//                                    pageNumbers = IntStream.rangeClosed(1, pages)
//                                            .boxed().collect(Collectors.toList())
//                                    req.attributes()["pageNumbers"] = pageNumbers
//
//                                }
//
//                                // }
//                    }
//                    req.attributes()["errors"] = errors
//                    req.attributes()["hasErrors"] = errors.isEmpty()
//
//
//
//                    ok().render(
//                            allPermits, req.attributes()
//                    )
//                }
//                ?: addErrorsAndRender(createPermit, "appId", "Missing Application Map", null)
//    }

    private fun addErrorsAndRender(path: String, errorId: String?, error: String?, pair: Pair<String, Any>?): ServerResponse {
        errorId?.let {
            when {
                error != null -> errors[it] = error
            }

        }

        return ok().render(
                path,
                pair?.let {
                    mapOf(
                            Pair("errors", errors),
                            Pair("hasErrors", errors.isEmpty()),
                            it

                    )
                } ?: mapOf(
                        Pair("errors", errors),
                        Pair("hasErrors", errors.isEmpty())

                )
        )
    }
}


//@GetMapping("/all-permits")
//fun allPermits(model: Model): String {
//    model.addAttribute("permits", permitRepo.findByStatus(1))
//    return "quality-assurance/customer/my-permits"
//}