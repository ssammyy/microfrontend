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

package org.kebs.app.kotlin.apollo.ipc


import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.junit.Test
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.composeUsingSpel
import org.kebs.app.kotlin.apollo.common.utils.placeHolderMapper
import org.kebs.app.kotlin.apollo.common.utils.replacePrefixedItemsWithObjectValues
import org.kebs.app.kotlin.apollo.config.service.messaging.kafka.ConsumerConfigurer
import org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.integ.KtorHttpClient
import org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service.LookUpBusinessOnBrsService
import org.kebs.app.kotlin.apollo.ipc.dto.BrsLookUpResponse
import org.kebs.app.kotlin.apollo.store.model.IntegrationConfigurationEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.IManufacturerRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit4.SpringRunner
//import org.junit.Test


@SpringBootTest
@RunWith(SpringRunner::class)
class IpcApplicationTests {

    @Autowired
    lateinit var usersRepo: IUserRepository

    @Autowired
    lateinit var ktorHttpClient: KtorHttpClient

    @Autowired
    lateinit var manRepo: IManufacturerRepository

    @Autowired
    lateinit var lookUpBrsService: LookUpBusinessOnBrsService

    @Autowired
    lateinit var serviceRequestRepo: IServiceRequestsRepository


    @Autowired
    lateinit var consumerConfigurer: ConsumerConfigurer


    @Test
    fun contextLoads() {
    }

    @Test
    fun sendServiceRequestForNotificationProcessingTest() =
            (188L..190L).forEach { id ->
                serviceRequestRepo.findByIdOrNull(id)
                        ?.let { sr ->
                            consumerConfigurer.kafkaTemplate().send("select-notifications-use-case", sr)

                        }
            }


    @Test
    fun replacePrefixedItemsWithObjectValuesTest() {
        val user = serviceRequestRepo.findByIdOrNull(188L)
        val url = "Dear {5},\n You requested access to KEBS kims platform on {6}. Kindly click the link below to confirm\nhttp://{0}:{1}{2}?{3}={4}\n regards, \n KEBS Team"
        val params = "127.0.0.1_8005_/api/auth/signup/activate_token_#transactionReference_#names_#eventBusSubmitDate"
        val paramSeparator: String? = "_"
        val beanPrefix = "#"
        val beanPrefixReplace = " "

        val p = paramSeparator
                ?.let { s ->
                    params.split(s).replacePrefixedItemsWithObjectValues(user, beanPrefix, beanPrefixReplace) { d, p ->
                        composeUsingSpel(d, p)
                    }.toTypedArray()
                }
                ?: throw NullValueNotAllowedException("Separator should not be null")

        val message = placeHolderMapper(input = url, parameters = p)
        KotlinLogging.logger { }.info(message)

    }


    @Test
    fun shouldFormatUrlByAppendingRelevantDataTest() {
        val user = usersRepo.findByIdOrNull(101L)
//        var dummy = composeUsingSpel(user, "email")
//        KotlinLogging.logger { }.info("$dummy")
        val url = "Dear {5},\n You requested access to KEBS kims platform on {6}. Kindly click the link below to confirm\nhttp://{0}:{1}{2}?{3}={4}\n regards, \n KEBS Team"
        val params = "127.0.0.1,8005,/api/auth/signup/activate,token,1234567,#firstName,#registrationDate"
        val paramSeparator: String? = ","
        val beanPrefix = "#"
        val beanPrefixReplace = ""
        val list = mutableListOf<String>()
        paramSeparator
                ?.let {
                    params.split(it).forEach { p ->
                        when {
                            p.startsWith(beanPrefix) -> {
                                composeUsingSpel(user, p.replace(beanPrefix, beanPrefixReplace))
                                        ?.let { r ->
                                            list.add(r)

                                        }
                                        ?: list.add(p)

                            }
                            else -> {
                                list.add(p)
                            }
                        }

                    }
                }


        val message = placeHolderMapper(input = url, parameters = list.toTypedArray())
        KotlinLogging.logger { }.info(message)
    }

    fun <D> dummy(data: D, t: String): String? {
        return composeUsingSpel(data, t)
    }


    @Test
    fun testCheckBrs() {
        val log = WorkflowTransactionsEntity()
        var config = IntegrationConfigurationEntity()
        var req = ServiceRequestsEntity()
        val requestParams: MutableMap<String, String> = mutableMapOf()
        val man = manRepo.findByIdOrNull(190)
        man?.registrationNumber.let { it?.let { it1 -> requestParams.putIfAbsent(config.requestParams.split(config.requestParamsSeparator)[0], it1) } }
        runBlocking {
            ktorHttpClient.sendRequests(config, null, log, req.serviceMapsId, requestParams, null)
        }

        lookUpBrsService.convertIntegrationResponseToJson(log, config)
                ?.let { testModel ->
                    val brsLookUpResponse: BrsLookUpResponse = testModel as BrsLookUpResponse
                    brsLookUpResponse.record
                            ?.let { record ->
                                if ((record.registrationNumber == man?.registrationNumber)
                                        && (record.kraPin == man.kraPin) &&
                                        record.businessName == man.name)   {
                                    KotlinLogging.logger {  }.info { "Details match" }
                                } else {
                                    KotlinLogging.logger {  }.info { "Details mismatch" }
                                }
                            }

                }
    }


}
