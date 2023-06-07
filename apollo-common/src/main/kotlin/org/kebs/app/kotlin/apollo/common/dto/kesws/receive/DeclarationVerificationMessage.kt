package org.kebs.app.kotlin.apollo.common.dto.kesws.receive

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import javax.validation.constraints.NotNull

@JacksonXmlRootElement(localName="message")
class DeclarationVerificationMessage {
    @NotNull
    @JsonProperty("header")
    val header: DCLVerificationHeader? = null

    @NotNull
    @JsonProperty("data")
    val data: DCLVerificationData? = null
}

class DCLVerificationHeader {
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

class DCLVerificationData {
    @NotNull
    @JsonProperty("data_in")
    val dataIn: DCLVerificationDataIn? = null
}

class DCLVerificationDataIn {
    @NotNull
    @JsonProperty("SAD")
    val sad: DCLVerificationSAD? = null
}

class DCLVerificationSAD {
    @JsonProperty("message_nature")
    var messageNature: Long? = null

    @JsonProperty("sad_id")
    var sadId: String? = null

    @JsonProperty("version")
    var version: String? = null

    @JsonProperty("KS")
    val cd: DCLVerificationKs? = null

    @JsonProperty("cd_allocated_officer")
    var cdAllocatedOfficer: String? = null

    @JsonProperty("b27_place_of_loading")
    var placeOfLoading: String? = null

    @JsonProperty("cd_verification_location")
    var verificationLocation: String? = null

    @JsonProperty("cd_verification_date_and_time")
    var verificationDateTime: String? = null

    @JsonProperty("cd_verification_remarks")
    var verificationRemarks: String? = null
}

class DCLVerificationKs {
    @JsonProperty("container_num")
    var containerNum: String? = null
}
