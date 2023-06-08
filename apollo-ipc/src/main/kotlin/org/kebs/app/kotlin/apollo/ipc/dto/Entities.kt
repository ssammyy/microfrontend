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

package org.kebs.app.kotlin.apollo.ipc.dto

import com.google.gson.annotations.SerializedName
import org.kebs.app.kotlin.apollo.common.dto.BaseEntity
import org.kebs.app.kotlin.apollo.common.dto.BaseRequest
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.time.Instant


@Configuration
class Encryption
(
        override var id: Long,
        override var name: String,
        override var description: String,
        override var status: Int
) : BaseEntity<Long>(id, name, description, status) {
    constructor() : this(0L, "", "", 0)

    var algorithm: String = ""
    var keyObtentionIterations: Int = 0
    var password: String = ""
    var poolSize: Int = 0
    var providerName: String = ""
    var saltGeneratorClassname: String = ""
    var stringOutputType: String = ""
}


data class KafkaRequest(
        override var id: Long,
        override var name: String,
        override var description: String,
        override var status: Int
) :
        BaseRequest(id, name, description, status) {
    constructor() : this(0L, "", "", 0)

    var kafkaPartitionId: Int = 0
    var kafkaOffset: Long = 0L
    var kafkaTopic: String = ""
    var kafkaMessageKey: String = ""
    var kafkaTimestampType: String = ""
    var kafkaTimestamp: Long = 0L
    var responseStatus: String = ""
    var responseMessage: String = ""
    var eventBusSubmitDate: Instant = Instant.now()
    var processingStartDate: Instant? = null
    var processingEndDate: Instant? = null
    var serviceMapsDto: ServiceMapsEntity? = null
    var payload: Any? = null

}

class BrsLookUpRecords {

    var status: String = ""

    @SerializedName("registration_number")
    var registrationNumber: String = ""

    @SerializedName("registration_date")
    var registrationDate: java.util.Date? = null

    @SerializedName("postal_address")
    var postalAddress: String = ""

    @SerializedName("physical_address")
    var physicalAddress: String = ""

    @SerializedName("phone_number")
    var phoneNumber: String = ""
    var partners: MutableList<BrsLookupBusinessPartners> = mutableListOf()

    var id: String = ""
    var email: String = ""

    @SerializedName("kra_pin")
    var kraPin: String = ""

    @SerializedName("business_name")
    var businessName: String = ""
    var branches: MutableList<BrsLookupBusinessBranches> = mutableListOf()
}

class BrsLookupBusinessBranches {
    var name: String = ""
    override fun toString(): String {
        return "[name=$name]"
    }
}


class BrsLookupBusinessPartners {
    var name: String = ""

    @SerializedName("id_type")
    var idType: String = ""

    @SerializedName("id_number")
    var idNumber: String = ""


}

@Component
class BrsLookUpResponse {
    var records: MutableList<BrsLookUpRecords>? = mutableListOf()
    var count: Int = 0
    var record: BrsLookUpRecords? = null
}


class MtSmsRequest {
    var senderId: String? = ""
    var messageParameters: List<MessageParams>? = null
    var apiKey: String? = ""
    var clientId: String? = ""
}

class MessageParams {
    var number: String = ""
    var text: String = ""
}