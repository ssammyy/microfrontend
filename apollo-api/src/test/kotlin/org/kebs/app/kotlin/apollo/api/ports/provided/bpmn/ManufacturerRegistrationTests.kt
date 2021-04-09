//
//
///*
// *
// * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
// * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
// * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
// * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
// * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
// * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
// * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
// * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
// * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
// * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
// *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
// *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
// *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
// *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
// *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
// *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
// *
// *
// *
// *
// *
// *   Copyright (c) 2020.  BSK
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *   you may not use this file except in compliance with the License.
// *   You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// */
//
//package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn
//
//import mu.KotlinLogging
//import org.flowable.engine.HistoryService
//import org.flowable.engine.RepositoryService
//import org.flowable.engine.RuntimeService
//import org.flowable.engine.TaskService
//import org.flowable.task.api.Task
//import org.junit.runner.RunWith
////import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
//import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
//import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.kebs.app.kotlin.apollo.store.repo.*
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.test.context.junit4.SpringRunner
//import kotlin.test.Test
//
//
//@SpringBootTest
//@RunWith(SpringRunner::class)
//class ManufacturerRegistrationTests {
//    @Autowired
//    lateinit var notificationsService: NotificationsService
//
//    @Autowired
//    lateinit var runtimeService: RuntimeService
//
//    @Autowired
//    lateinit var taskService: TaskService
//
//    @Autowired
//    lateinit var historyService: HistoryService
//
//    @Autowired
//    lateinit var repositoryService: RepositoryService
//
//    @Autowired
//    lateinit var serviceMapsRepo: IServiceMapsRepository
//
//    @Autowired
//    lateinit var serviceRequestssRepo: IServiceRequestsRepository
//
//    var data: ComplaintEntity? = null
//
//    @Autowired
//    lateinit var titlesRepo: ITitlesRepository
//
//    @Autowired
//    lateinit var departmentRepo: IDepartmentsRepository
//
//    @Autowired
//    lateinit var divisionsRepo: IDivisionsRepository
//
//    @Autowired
//    lateinit var designationsRepo: IDesignationsRepository
//
//    @Autowired
//    lateinit var sectionsRepo: ISectionsRepository
//
//    @Autowired
//    lateinit var subRegionsRepo: ISubRegionsRepository
//
//    @Autowired
//    lateinit var usersRepo: IUserRepository
//
//    @Autowired
//    lateinit var manufacturersRepo: IManufacturerRepository
//
//    @Autowired
//    lateinit var userProfilesRepo: IUserProfilesRepository
//
//    @Autowired
//    lateinit var employeesRepo: IEmployeesRepository
//
//    @Autowired
//    lateinit var daoServices: RegistrationDaoServices
//
//    @Autowired
//    lateinit var manufacturerAddressesRepo: IManufacturerAddressRepository
//
//    @Autowired
//    lateinit var manufacturerContactRepo: IManufacturerContactsRepository
//
//    @Test
//    fun createProcessInstanceAndCompleteManufacturerRegistration() {
//        val appId = 127
//        val userTypeId = 5
//
//        val manufacturer = manufacturersRepo.findByIdOrNull(4L)
//        val address = manufacturerAddressesRepo.findByIdOrNull(4L)
//        val contact = manufacturerContactRepo.findByIdOrNull(0L)
//        val user = usersRepo.findByIdOrNull(64L)
//        serviceRequestssRepo.findByIdOrNull(448)
//                ?.let { sr ->
//
//                    serviceMapsRepo.findByIdAndStatus(appId, 1)
//                            ?.let { s ->
//                                sr.transactionReference = generateRandomText(s.transactionRefLength, s.secureRandom, s.messageDigestAlgorithm)
//                                val srs = serviceRequestssRepo.save(sr)
//                                val variables = mutableMapOf<String, Any?>()
//                                variables["map"] = s
//                                variables["address"] = address
//                                variables["user"] = user
//                                variables["contact"] = contact
//                                variables["manufacturer"] = manufacturer
//                                variables["userTypeId"] = userTypeId
//                                variables["transactionRef"] = srs.transactionReference
//                                variables["sr"] = srs
//
//                                runtimeService
//                                        .createProcessInstanceBuilder()
//                                        .processDefinitionKey(s.bpmnProcessKey)
//                                        .businessKey(sr.transactionReference)
//                                        .variables(variables)
//                                        .start()
//                            }
//
//                    Thread.sleep(10000)
//
//                }
//
//
//    }
//
////    @Test
//    /*
//fun manufacturerRegistrationUsingBpmnTest() {
//    val appId = 127
//    val userTypeId = 4
//
//    val manufacturer = ManufacturersEntity()
//    with(manufacturer) {
//        id = 0L
//    }
//
////        val userProfile = UserProfilesEntity()
////        with(userProfile) {
////            id = 0L
////            divisionId = divisionsRepo.findByIdOrNull(1L)
////            designationId = designationsRepo.findByIdOrNull(1L)
////            sectionId = sectionsRepo.findByIdOrNull(1L)
////            subRegionId = subRegionsRepo.findByIdOrNull(1L)
////
////        }
//
//
//        val user = UsersEntity()
//        with(user) {
//            id = 0L
//            title = titlesRepo.findByIdOrNull(1L)
//            firstName = "Bpmn"
//            lastName = "Test"
//            email = "mine@yours.com"
//        }
//
//        val manC = ManufacturerContactsEntity()
//        with(manC) {
//
//        }
//        val manEnt = ManufacturersEntity()
//        with(manEnt){
//
//        }
//        val manAddr = ManufacturerAddressesEntity()
//        with(manAddr) {
//
//        }
//
//
//
//        serviceMapsRepo.findByIdAndStatus(appId, 1)
//                ?.let { s ->
//                    val ref = generateRandomText(s.transactionRefLength, s.secureRandom, s.messageDigestAlgorithm)
//
//                    daoServices.registerManufacturer(s, manC, manEnt, manAddr, userTypeId)
//                    val variables = mutableMapOf<String, Any?>()
//                    variables["map"] = s
//                    variables["user"] = user
////                    variables["userProfile"] = userProfile
////                    variables["employee"] = employee
//                    variables["userTypeId"] = userTypeId
//                    variables["transactionRef"] = ref
////
//                    runtimeService.startProcessInstanceByKey(s.bpmnProcessKey, ref, variables)
//
//
////                    runtimeService
////                            .createProcessInstanceBuilder()
////                            .processDefinitionKey(s.bpmnProcessKey)
////                            .businessKey(ref)
////                            .variables(variables)
////                            .start()
////                                var proc = runtimeService.startProcessInstanceByKey(s.bpmnProcessKey, variables)
//
//
////                                result = daoServices.registerEmployee(s, usersEntity, userProfilesEntity, employeesEntity)
//
//                }
//                ?: throw ServiceMapNotFoundException("No service map found for appId=$appId, aborting")
//
//    }
//*/
//    //    @Test
//    fun happyPathTest() {
//
//        serviceMapsRepo.findByIdOrNull(129)
//                ?.let { map ->
//                    data?.serviceMapsId = map
//                    data?.referenceNumber = "${map.transactionRefPrefix}_${generateRandomText(map.transactionRefLength, map.secureRandom, map.messageDigestAlgorithm, true)}"
//                    val variables = mutableMapOf<String, Any?>()
//                    variables["appId"] = map
//
//                    variables["complaint"] = data
//                    variables["Name"] = "Kenneth Muhia"
//
//
//                    val processInstance = runtimeService.startProcessInstanceByKey(map.bpmnProcessKey, "myKey", variables)
//
//                    val task: Task = taskService.createTaskQuery()
//                            .processInstanceId(processInstance.id)
//                            .taskCandidateGroup("MS_OFFICER")
//                            .singleResult()
//
//
//                    KotlinLogging.logger { }.info(task.name)
//
//                }
//
//
//    }
//
//
//}
//
