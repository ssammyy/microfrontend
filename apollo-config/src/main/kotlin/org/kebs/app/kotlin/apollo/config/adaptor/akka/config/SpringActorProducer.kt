package org.kebs.app.kotlin.apollo.config.adaptor.akka.config

import akka.actor.Actor
import akka.actor.IndirectActorProducer

import org.springframework.context.ApplicationContext

class SpringActorProducer : IndirectActorProducer {
    private val applicationContext: ApplicationContext
    private val actorBeanClass: Class<out Actor>
    private val parameters: Array<Any>?


    constructor(applicationContext: ApplicationContext, actorBeanClass: Class<out Actor>, parameters: Array<Any>?) {
        this.applicationContext = applicationContext
        this.actorBeanClass = actorBeanClass
        this.parameters = parameters
    }

    constructor(applicationContext: ApplicationContext, actorBeanClass: Class<out Actor>) {
        this.applicationContext = applicationContext
        this.actorBeanClass = actorBeanClass
        parameters = null
    }

    override fun produce(): Actor {
        parameters?.let {
            return applicationContext.getBean(actorBeanClass, *it)
        }
            ?: return applicationContext.getBean(actorBeanClass)


    }

    override fun actorClass(): Class<out Actor> {
        return actorBeanClass
    }
}