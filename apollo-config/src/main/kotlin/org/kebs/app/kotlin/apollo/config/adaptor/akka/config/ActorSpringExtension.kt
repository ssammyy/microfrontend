package org.kebs.app.kotlin.apollo.config.adaptor.akka.config

import akka.actor.*
import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component("springExtension")
class ActorSpringExtension(
    private val applicationContext: ApplicationContext,
) : AbstractExtensionId<ActorSpringExtension.SpringExt>() {

    override fun createExtension(system: ExtendedActorSystem): SpringExt {
        return SpringExt(applicationContext)
    }


    class SpringExt(private val applicationContext: ApplicationContext) : Extension {


        fun props(actorBeanClass: Class<out Actor?>?): Props? {
            return Props.create(SpringActorProducer::class.java, applicationContext, actorBeanClass)
        }

        fun props(actorBeanClass: Class<out Actor?>?, vararg parameters: Any?): Props? {
            return Props.create(SpringActorProducer::class.java, applicationContext, actorBeanClass, parameters)
        }

        //Todo Review common error
        fun props(actorBeanName: String): Props {
            return applicationContext.getType(actorBeanName)
                ?.let { extractedType ->
//                    when (extractedType.isAssignableFrom(Actor::class.java)) {
//                        true -> Props.create(SpringActorProducer::class.java, applicationContext, extractedType as Class<out Actor>)
//                        else -> throw InvalidValueException("$actorBeanName is not a valid actor class")
//                    }
                    try {
                        Props.create(
                            SpringActorProducer::class.java,
                            applicationContext,
                            extractedType as Class<out Actor>
                        )
                    } catch (e: Exception) {
                        KotlinLogging.logger { }.debug(e.message, e)
                        throw RuntimeException(e.message)
                    }
                }
                ?: throw RuntimeException("$actorBeanName is not a valid actor class, returns null type")


//            return Props.create(SpringActorProducer::class.java, applicationContext, applicationContext.getType(actorBeanName) as Class<out Actor>)
        }

    }


}


