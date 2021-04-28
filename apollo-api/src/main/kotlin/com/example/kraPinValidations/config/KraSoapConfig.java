package com.example.kraPinValidations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class KraSoapConfig {
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller=new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.example.kraPinValidations.kraPinIntegrations");
        return marshaller;
    }
}
