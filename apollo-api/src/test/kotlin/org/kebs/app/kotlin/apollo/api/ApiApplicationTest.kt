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

package org.kebs.app.kotlin.apollo.api


//import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaHttpClient
import mu.KotlinLogging
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.PvocDaoServices
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.ResourceUtils
import org.junit.jupiter.api.Test


@SpringBootTest
@RunWith(SpringRunner::class)
class ApiApplicationTest {
    @Autowired
    lateinit var sendToKafkaQueue: SendToKafkaQueue

    @Autowired
    lateinit var mpesaService: MPesaService

    @Autowired
    lateinit var permitRepo: IPermitRepository

//    @Autowired
//    lateinit var daoservises: QualityAssuranceDaoServices

    @Autowired
    lateinit var diDaoServices: DestinationInspectionDaoServices

//    @Autowired
//    lateinit var msReportsControllers: MSReportsControllers

    @Autowired
    lateinit var pvocDaoServices: PvocDaoServices

//    @Autowired
//    lateinit var msFuelControllers: MSFuelInspectionController

    @Autowired
    lateinit var usersRepo: IUserRepository

    @Autowired
    lateinit var iFuelRemediationInvoiceRepo: IFuelRemediationInvoiceRepository

    @Autowired
    lateinit var iFuelInspectionRepo: IFuelInspectionRepository

    @Autowired
    lateinit var iArrivalPointRepo: IArrivalPointRepository

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var serviceRequestRepo: IServiceRequestsRepository

    @Autowired
    lateinit var destinationInspectionRepo: IDestinationInspectionRepository

    @Autowired
    lateinit var idfRepo: IdfsEntityRepository

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var iPvocInvoicingRepository: IPvocInvoicingRepository

    @Autowired
    lateinit var iPvocPartnersRepository: IPvocPartnersRepository

    @Autowired
    lateinit var iPvocReconciliationReportEntityRepo: PvocReconciliationReportEntityRepo


    @Test
    fun contextLoads() {
    }


    @Test
    fun submitUserRegistrationToKafka() {
        try {

            usersRepo.findByIdOrNull(101L)
                    ?.let { user ->
                        sendToKafkaQueue.submitAsyncRequestToBus(user, "user-registration")

                    }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.trace(e.message, e)
        }
    }

    @Test
    fun workFlowCocTest() {

        var cocDocuments = destinationInspectionRepo.findAll()
        var idfDocument = idfRepo.findAll()
        var totalFound = 0
        for (coc in cocDocuments) {
            for (idf in idfDocument) {
                if (coc.ucrNumber == idf.ucr) {
                    coc.idfId = idf
                    destinationInspectionRepo.save(coc)
                    totalFound++
                    KotlinLogging.logger { }.info { "Idf found ucr number  = " + idf.ucr }
                    KotlinLogging.logger { }.info { "Coc found ucr number  = " + coc.ucrNumber }
                }
            }
        }
        KotlinLogging.logger { }.info { "Total found with same ucr number  = $totalFound" }

    }


//    @Test
//    fun savePermitApplication() {
//        val permitID: Long = 5000
//        val permitApply = PermitApplicationsEntity()
//        with(permitApply) {
////            description = "This is a smark test Drive"
////            permitNumber = "DRFghdhj145855"
//            tradeMark = "bsk.supp.temp@gmail.com"
//            ksNumber = "varField1"
//            id = permitID
////            status = 1
//        }
//
//        usersRepo.findByUserName("gkingori139@gmail.com")
//                ?.let { loggedInUser ->
//                    daoservises.updatePermit(permitApply,loggedInUser, daoservises.serviceMapDetails(), permitID)
//                }
//
//
//    }

    @Test
    fun sendmailBaseUrl() {
        val appId = applicationMapProperties.mapImportInspection

        usersRepo.findByUserName("kpaul7747@gmail.com")
                ?.let { loggedInUser ->
                    val payload = "Assigned Inspection Officer [assignedStatus= 1, assignedRemarks= test]"
                    val map = commonDaoServices.serviceMapDetails(appId)
                    val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
                   commonDaoServices.sendEmailWithUserEntity(loggedInUser, diDaoServices.diCdAssignedUuid,  diDaoServices.findCD(122L),map, sr)
//                    daoservises.updatePermit(permitApply,loggedInUser, daoservises.serviceMapDetails(), permitID)
                }



    }

//    fun foo(url: String?, json: JSONObject): JSONObject? {
//        // create your json here
//
//        // create your json here
//        val jsonObject = JSONObject()
//        try {
//            jsonObject.put("username", "kims")
//            jsonObject.put("password", "QVZy>Bzm7\">3Dq5P")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        val client = OkHttpClient()
//        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
//        // put your json here
//        // put your json here
//        val body = RequestBody.create(JSON, jsonObject.toString())
//        val request: Request =  Request.Builder()
//                .url("https://countypay.bskglobaltech.com/kappa/integ/login")
//                .post(body)
//                .build()
//
//        var response: Response? = null
//        try {
//            response = client.newCall(request).execute()
//            val resStr = response.body?.string()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//    @Test
//    fun workFlowCocTransportDetailsTest() {
//
//        var cocDocuments = destinationInspectionRepo.findAll()
//        var transportDocument = iTransportDetailsRepo.findAll()
//        var totalFound = 0
//        for (coc in cocDocuments){
//            for (trans in transportDocument){
//                if (coc.transportId?.id == trans.id){
//                    trans.ucrNumber = coc.ucrNumber
//                    iTransportDetailsRepo.save(trans)
//                    totalFound++
//                    KotlinLogging.logger{  }.info{"Coc found transport ID number  = " + coc.ucrNumber}
//                }
//            }
//        }
//        KotlinLogging.logger{  }.info{ "Total found with same ID number  = $totalFound" }
//
//    }

//    @Test
//    fun workFlowCocTransportDetailsToCDTest() {
//
//        var cocDocuments = destinationInspectionRepo.findAll()
//        var transportDocument = iTransportDetailsRepo.findAll()
//        var arrivalList = iArrivalPointRepo.findAll()
//        var totalFound = 0
//        for (coc in cocDocuments){
//            for (trans in transportDocument){
//                for (arrivalPoint in arrivalList)
//                if (coc.ucrNumber == trans.ucrNumber && trans.portOfArrival.equals(arrivalPoint.description)){
//                    coc.arrivalPoint = arrivalPoint
//                    coc.transportId = trans
//                    destinationInspectionRepo.save(coc)
//                    totalFound++
//                    KotlinLogging.logger{  }.info{"Coc found transport ID number  = " + coc.ucrNumber}
//                }
//            }
//        }
//        KotlinLogging.logger{  }.info{ "Total found with same ID number  = $totalFound" }
//
//    }

    @Test
    fun workFlowQATest() {

        var cocDocuments = destinationInspectionRepo.findAll()
        var idfDocument = idfRepo.findAll()
        var totalFound = 0
        for (coc in cocDocuments) {
            for (idf in idfDocument) {
                if (coc.ucrNumber == idf.ucr) {
                    coc.idfId = idf
                    destinationInspectionRepo.save(coc)
                    totalFound++
                    KotlinLogging.logger { }.info { "Idf found ucr number  = " + idf.ucr }
                    KotlinLogging.logger { }.info { "Coc found ucr number  = " + coc.ucrNumber }
                }
            }
        }
        KotlinLogging.logger { }.info { "Total found with same ucr number  = $totalFound" }

    }


    @Test
    fun submitUserTokenForValidation() {
        try {
            sendToKafkaQueue.submitAsyncRequestToBus("74164ae36b38a30", "user-validation")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.trace(e.message, e)
        }
    }

    @Test
    fun testFileSendAttachment() {
        iFuelInspectionRepo.findByIdOrNull(101)
                ?.let { msFuelInspectionEntity ->
                    val imagePath = ResourceUtils.getFile("classpath:static/images/KEBS_SMARK.png").toString()
                    val map = hashMapOf<String, Any>()
                    map["imagePath"] = imagePath
//                    msReportsControllers.extractAndSaveReport(map, "classpath:reports/remediationInvoice.jrxml", "Remediation-Invoice", iFuelRemediationInvoiceRepo.findFirstByFuelInspectionId(msFuelInspectionEntity))
//                    msFuelControllers.sendEmailWithProforma("gabworks51@gmail.com", ResourceUtils.getFile("classpath:templates/TestPdf/Remediation-Invoice.pdf").toString())
                }
    }

    @Test
    fun testSendAttachment() {
        pvocDaoServices.sendInvoice(403)
    }

}
