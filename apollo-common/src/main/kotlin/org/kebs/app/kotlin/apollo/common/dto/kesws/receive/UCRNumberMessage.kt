package org.kebs.app.kotlin.apollo.common.dto.kesws.receive

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

import javax.validation.constraints.NotNull

@JacksonXmlRootElement(localName="message")
class UCRNumberMessage {
    @NotNull
    @JsonProperty("data")
    val data: UCRData? = null

    @NotNull
    @JsonProperty("header")
    val header: UCRHeader? = null
}

class UCRHeader {
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

class UCRData {
    @NotNull
    @JsonProperty("data_in")
    val dataIn: UCRDataIn? = null
}

class UCRDataIn {
    @JsonProperty("message_nature")
    var messageNature: Long? = null

    @JsonProperty("sad_id")
    var sadId: String? = null

    @JsonProperty("version")
    var version: String? = null

    @JsonProperty("b7_ref_num")
    var ucrNumber: String? = null

    @JsonProperty("permits_link")
    var permitsLink: String? = null

    @JsonProperty("permits_details")
    val permitsDetails: PermitsDetails? = null
}

class PermitsDetails {
    @JsonProperty("permit_num")
    var permitNumber: String? = null

    @JsonProperty("version_num")
    var versionNumber: Long? = null

    @JsonProperty("pga_code")
    var pgaCode: String? = null

    @JsonProperty("approver")
    var approver: String? = null

    @JsonProperty("inspection_req")
    var inspectionReq: String? = null

    @JsonProperty("approved_date")
    var approvedDate: String? = null

    @JsonProperty("status")
    var status: String? = null
}
