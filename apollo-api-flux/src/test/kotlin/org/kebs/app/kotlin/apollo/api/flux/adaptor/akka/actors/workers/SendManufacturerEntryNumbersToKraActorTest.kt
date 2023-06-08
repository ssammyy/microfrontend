package org.kebs.app.kotlin.apollo.api.flux.adaptor.akka.actors.workers

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.testkit.javadsl.TestKit
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.base256Encode
import org.kebs.app.kotlin.apollo.common.dto.jobs.PenaltyJobDetails
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
//@DataJpaTest
class SendManufacturerEntryNumbersToKraActorTest {

    @Autowired
    lateinit var jobsRepo: IBatchJobDetailsRepository

    @Autowired
    lateinit var configRepo: IIntegrationConfigurationRepository

    @Autowired
    lateinit var daoService: DaoService

    @Autowired
    lateinit var extension: ActorSpringExtension

    @Autowired
    lateinit var actorSystem: ActorSystem

    @Test
    fun testFormatDateGivenConfig() {
        configRepo.findByIdOrNull(26L)
            ?.let { config ->
                val timeNow = Instant.now()

//yyyy-MM-dd'T'HH:mm:ss
                val transmissionDate = DateTimeFormatter.ofPattern(config.gsonDateFormt).withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(timeNow)
                val str = "3$transmissionDate"

                println(transmissionDate)
                println(base256Encode(str, config))

            }
    }

    @Test
    fun givenJobIdInvokeWorkerAndSendManufacturerDetailsToKRA() {

        val tester: ActorRef?

        val job = jobsRepo.findByIdOrNull(5L) ?: throw NullValueNotAllowedException("Empty job not alloed")



        object : TestKit(actorSystem) {
            init {

                tester = actorSystem.actorOf(extension.get(actorSystem).props("sendManufacturerEntryNumbersToKraActor"), "sendManufacturerEntryNumbersToKraActor")
                tester?.tell(job, tester)
                runBlocking { delay(25000) }


            }
        }


    }

    @Test
    fun givenJobIdInvokeWorkerAndSendManufacturerPenaltyDetailsToKRA() {
        val tester: ActorRef?
        object : TestKit(actorSystem) {
            init {

                tester = actorSystem.actorOf(extension.get(actorSystem).props("sendManufacturerEntryNumbersToKraActor"), "sendManufacturerEntryNumbersToKraActor")
                val detail = PenaltyJobDetails(6L, "Penalty")
                tester?.tell(detail, tester)
                runBlocking { delay(25000) }


            }
        }

    }


    @Test
    fun givenJobDetailAndEntryNumberListDoBatchUpdate() {
        jobsRepo.findByIdOrNull(2L)
            ?.let { _ ->
//                val data = mutableListOf<Long?>(4633L, 4989L, 9982L, 11695L, 14303L, 14380L)
                daoService.generateTransactionReference()
//                daoService.postJobProcessingRecordsCleanUp(
////                    entityManager,
//                    job,
//                    data,
//                    daoService.generateTransactionReference(),
//                    Timestamp.from(Instant.now()),
//                    log)

            }


    }


}
