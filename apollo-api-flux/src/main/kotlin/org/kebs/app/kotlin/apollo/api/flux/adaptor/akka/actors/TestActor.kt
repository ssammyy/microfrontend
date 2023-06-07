package org.kebs.app.kotlin.apollo.api.flux.adaptor.akka.actors

import akka.actor.AbstractActor
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.service.MessageService
import org.kebs.app.kotlin.apollo.config.adaptor.akka.interfaces.Actor


@Actor
class TestActor(
    private var messageService: MessageService,
) : AbstractActor() {
    override fun createReceive(): Receive {
        return receiveBuilder()
            .match(String::class.java, messageService::print)
            .build()

    }
}
