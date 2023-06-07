/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.config.properties.web

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
@EncryptablePropertySource("file:\${CONFIG_PATH}/thymeleaf.properties")
class ThymeleafProperties {

    @Value("\${org.kebs.app.kotlin.apollo.application.thymeleaf.template.prefix}")
    var templatePrefix: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.thymeleaf.template.suffix}")
    var templateSuffix: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.thymeleaf.template.mode}")
    var templateMode: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.thymeleaf.template.character.encoding}")
    var templateCharacterEncoding: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.thymeleaf.template.order}")
    var templateOrder: Int? = null

    @Value("\${org.kebs.app.kotlin.apollo.application.thymeleaf.template.check.template}")
    var templateCheckTemplate: Boolean? = null


    @Value("\${org.kebs.app.kotlin.apollo.ssl.client.keystore.path}")
   var ussdSslClientKeystorePath: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.ssl.client.truststore.path}")
   var ussdSslClientTruststorePath: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.ssl.server.keystore.path}")
   var ussdSslServerKeystorePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.ssl.server.truststore.path}")
   var ussdSslServerTruststorePath: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.ssl.server.keystore.password}")
   var ussdSslServerKeystorePassword: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.ssl.server.truststore.password}")
   var ussdSslServerTruststorePassword: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.ssl.keystore.keyword}")
   var ussdSslKeystoreKeyword: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.ssl.truststore.keyword}")
   var ussdSslTruststoreKeyword: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.ssl.keystore.type}")
   var ussdSslKeystoreType: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.ssl.server.context.instance}")
   var ussdSslServerContextInstance: String = ""


}
