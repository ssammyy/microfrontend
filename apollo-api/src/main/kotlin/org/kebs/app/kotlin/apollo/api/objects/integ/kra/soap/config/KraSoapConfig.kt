package org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller

@Configuration
class KraSoapConfig {
    @Bean
    fun marshaller(): Jaxb2Marshaller {
        val marshaller = Jaxb2Marshaller()
        marshaller.setPackagesToScan("org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.kraPinIntegrations")
        return marshaller
    }
}