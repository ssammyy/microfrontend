package org.kebs.app.kotlin.apollo.common.dto.kesws.receive;

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.math.BigDecimal
import java.util.*


class ErrorMessageHeader {
    @JsonProperty("user_id")
    var msgId: String? = null;

    @JsonProperty("ref_no")
    var refNo: String? = null;

    @JsonProperty("msg_func")
    var msgFunc: String? = null;

    @JsonProperty("sender")
    var sender: String? = null

    @JsonProperty("receiver")
    var receiver: String? = null

    @JsonProperty("version")
    var version: BigDecimal? = null

    @JsonProperty("msg_date")
    var msgDate: Date? = null
}

class ErrorDocumentDetails {
    @JsonProperty("document_reference_no")
    var documentReferenceNo: String? = null

    @JsonProperty("doc_ver_number")
    var docVerNumber: String? = null

    @JsonProperty("file_name")
    var fileName: String? = null
}

class ErrorInformation {
    @JsonProperty("errorCode")
    var errorCode: String? = null

    @JsonProperty("errorDescription")
    var errorDescription: String? = null
}

@JacksonXmlRootElement(localName="keswsresponse")
class KeswsErrorResponse {
    @JsonProperty("msghdr")
    var msghdr: ErrorMessageHeader? = null

    @JsonProperty("document_details")
    var documentDetails: ErrorDocumentDetails? = null

    @JsonProperty("errorInformation")
    var errorInformation: ErrorInformation? = null
}
