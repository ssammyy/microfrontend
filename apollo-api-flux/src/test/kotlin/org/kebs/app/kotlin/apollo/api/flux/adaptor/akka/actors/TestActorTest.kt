package org.kebs.app.kotlin.apollo.api.flux.adaptor.akka.actors

import akka.actor.ActorRef
import akka.actor.ActorSystem
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
@SpringBootTest
class TestActorTest {

    @Autowired
    lateinit var extension: ActorSpringExtension

    @Autowired
    lateinit var actorSystem: ActorSystem


    @Test
    fun apacheAkkaIntegrationTest() {
        try {
            val testActor = actorSystem.actorOf(extension.get(actorSystem).props("testActor"), "dummyActor")
            testActor.tell("hello world at ${Instant.now()}", ActorRef.noSender())
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
    }
}