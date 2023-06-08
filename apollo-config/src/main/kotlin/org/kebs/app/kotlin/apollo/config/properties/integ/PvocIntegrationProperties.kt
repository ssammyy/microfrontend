package org.kebs.app.kotlin.apollo.config.properties.integ

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
@EncryptablePropertySource("file:\${CONFIG_PATH}/pvoc-integration.properties")
class PvocIntegrationProperties {

    @Value("\${org.kebs.app.kotlin.apollo.pvoc.integration.success.response.code}")
    val pvocIntegSuccessResponseCode: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.pvoc.integration.failure.response.code}")
    val pvocIntegFailureResponseCode: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.pvoc.integration.success.response.message}")
    val pvocIntegSuccessResponseMessage: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.pvoc.integration.failure.response.message}")
    val pvocIntegFailureResponseMessage: String? = null
}
