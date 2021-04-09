//package org.kebs.app.kotlin.apollo.api.handlers
//
//import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.springframework.stereotype.Component
//import org.springframework.web.servlet.function.ServerRequest
//import org.springframework.web.servlet.function.ServerResponse
//import org.springframework.web.servlet.function.ServerResponse.ok
//
//@Component
//class QAHofHandlers(
//        applicationMapProperties: ApplicationMapProperties,
//        private val standardsCategoryRepository: IStandardCategoryRepository,
//        private val sampleStandardsRepository: ISampleStandardsRepository,
//        private val dmarkForeignApplicationsRepository: IDmarkForeignApplicationsRepository,
//        private val permitTypesRepo: IPermitTypesEntityRepository,
//        private val permitRepo: IPermitRepository,
//        private val broadProductCategoryRepository: IBroadProductCategoryRepository,
//        private val sendToKafkaQueue: SendToKafkaQueue,
//        private val usersRepository: IUserRepository,
//        private val serviceMapsRepo: IServiceMapsRepository
//) {
//    final val appId = applicationMapProperties.mapPermitApplication
//    private final val hofHome = "quality-assurance/HOF/hof-home"
//    final val applications = "quality-assurance/HOF/applications"
////    val allocateFileToQAO = "quality-assurance/HOF/assign"
//
//    fun renderHofHome(req: ServerRequest): ServerResponse = ok().render(hofHome)
//
//    //    fun renderApplications(req: ServerRequest): ServerResponse = ok().render(applications)
//    fun renderApplications(req: ServerRequest): ServerResponse =
//            serviceMapsRepo.findByIdAndStatus(appId, 1)
//                    ?.let { map ->
//                        req.attributes()["permitApplications"] = permitRepo.findByPaymentStatusOrderByIdDesc(1)
//                        req.attributes()["maps"] = map
//                        return ok().render(applications, req.attributes())
//                    }
//                    ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")
//}