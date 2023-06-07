package org.kebs.app.kotlin.apollo.common.dto.kesws.receive

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import javax.validation.constraints.NotNull

@JacksonXmlRootElement(localName = "message")
class KraCancellationMessage {
    @NotNull
    @JsonProperty("data")
    val data: CanceledData? = null

    @NotNull
    @JsonProperty("header")
    val header: CancelHeader? = null
}

class CancelHeader {
    @NotNull
    @JsonProperty("user_id")
    var userId: String? = null

    @NotNull
    @JsonProperty("message_date")
    var messageDate: String? = null

    @NotNull
    @JsonProperty("module")
    var module: String? = null

    @NotNull
    @JsonProperty("action")
    var action: Long? = null

    @NotNull
    @JsonProperty("information")
    var information: String? = null

    @NotNull
    @JsonProperty("message_version")
    var messageVersion: Long? = null

    @NotNull
    @JsonProperty("direction")
    var direction: String? = null
}

class CanceledData {
    @NotNull
    @JsonProperty("data_in")
    val dataIn: CanceledDataIn? = null
}

class CanceledDataIn {
    @NotNull
    @JsonProperty("SAD")
    val sad: CanceledSAD? = null
}

class CanceledSAD {
    @JsonProperty("message_nature")
    var messageNature: String? = null

    @JsonProperty("sad_id")
    var sadId: String? = null

    @JsonProperty("version")
    var versionNumber: Long? = null

    @JsonProperty("cd_status")
    var cdStatus: String? = null

    @JsonProperty("cd_cancelled_date")
    var canceledDate: String? = null
}