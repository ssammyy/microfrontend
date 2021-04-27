package org.kebs.app.kotlin.apollo.api


import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.codehaus.jackson.annotate.JsonProperty
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


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
    lateinit var usersRepo: IUserRepository

//
//    @Test
//    fun mpesaTest() {
//        mpesaServices.mainMpesaTransaction("1", "254715668934",809, "kpaul7747@gmail.com", null)
//    }

    @Test
    fun hashString() {
        val plainText = listOf("test")

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

    @Test
    fun mpesaTestPushAndStaging() {
//        usersRepo.findByUserName("kpaul7747@gmail.com")
//            ?.let { loggedInUser ->
        mpesaServices.mainMpesaTransaction(
            "1",
            "254715668934",
            "DN2021040521BBB",
            "kpaul7747@gmail.com",
            applicationMapProperties.mapInvoiceTransactionsForDemandNote
        )
//            }

    }

}

