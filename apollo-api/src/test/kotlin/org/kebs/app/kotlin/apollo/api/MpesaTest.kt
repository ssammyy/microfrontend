package org.kebs.app.kotlin.apollo.api


import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IMpesaTransactionsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MpesaTest {

    @Autowired
    lateinit var daoService: DaoService

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var iMpesaTransactionsRepo: IMpesaTransactionsRepository

    @Autowired
    lateinit var configurationRepository: IIntegrationConfigurationRepository

    @Autowired
    lateinit var batchJobRepository: IBatchJobDetailsRepository

    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor

    @Autowired
    lateinit var mpesaServices: MPesaService

    @Autowired
    lateinit var limsServices: LimsServices

    @Autowired
    lateinit var usersRepo: IUserRepository

//
//    @Test
//    fun mpesaTest() {
//        mpesaServices.mainMpesaTransaction("1", "254715668934",809, "kpaul7747@gmail.com", null)
//    }

    @Test
    fun hashString() {
        val plainText = listOf("Alartz@123")

        plainText.forEach {
            val hashed = jasyptStringEncryptor.encrypt(it)
            KotlinLogging.logger { }.info { "my hashed value =$it =  $hashed" }
        }
    }

    @Test
    fun unHashString() {
        val hashed = listOf("dNQ60pPzbf+4J3+33XXDZUyF8zgpohBv")
        hashed.forEach {
            val plainText = jasyptStringEncryptor.decrypt(it)
            KotlinLogging.logger { }.info { plainText }
        }

    }

//    @Test
//    fun mpesaTestPushAndStaging() {
////        usersRepo.findByUserName("kpaul7747@gmail.com")
////            ?.let { loggedInUser ->
//        mpesaServices.mainMpesaTransaction(
//            10,
//            "254715668934",
//            "DN2021040521BBB",
//            "kpaul7747@gmail.com",
//            applicationMapProperties.mapInvoiceTransactionsForDemandNote
//        )
////            }
//
//    }

    @Test
    fun limsTestResults() {

        /* This is how to declare HashMap */
        /* This is how to declare HashMap */
        val hmap = HashMap<String, String>()

        /*Adding elements to HashMap*/

        /*Adding elements to HashMap*/hmap["bsnumber"] = "BS202108613"
        val myResults = limsServices.performPostCall(hmap, applicationMapProperties.mapLimsConfigIntegrationPDF)

        KotlinLogging.logger { }.info { myResults }

    }


}

