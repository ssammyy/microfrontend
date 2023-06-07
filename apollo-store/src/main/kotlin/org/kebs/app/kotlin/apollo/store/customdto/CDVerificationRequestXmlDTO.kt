package org.kebs.app.kotlin.apollo.store.customdto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName="message")
class CDVerificationRequestXmlDTO {

    @JacksonXmlProperty(localName = "header")
    var cdVerificationRequestHeader: CdVerificationRequestHeader? = null

    @JacksonXmlProperty(localName = "data")
    var cdVerificationRequestData: CdVerificationRequestData? = null
}

class CdVerificationRequestHeader(version: Int) {

    @JacksonXmlProperty(localName = "module")
    var module: String? = "KESWS_HLD_HOL"

    @JacksonXmlProperty(localName = "action")
    var action: Int? = 9

    @JacksonXmlProperty(localName = "direction")
    var direction: String? = "IN"

    @JacksonXmlProperty(localName = "user_id")
    var userId: String? = "KESWS"

    @JacksonXmlProperty(localName = "information")
    var information: String? = "Request for Consignment verification"

    @JacksonXmlProperty(localName = "message_version")
    var messageVersion: Int? = version

    @JacksonXmlProperty(localName = "message_date")
    var messageDate: String? = "2021-05-04T13:37:45.970+03:00"
}

class CdVerificationRequestData {
    @JacksonXmlProperty(localName = "data_in")
    var cdVerificationRequestDataIn: CdVerificationRequestDataIn? = null
}

class CdVerificationRequestDataIn {
    @JacksonXmlProperty(localName = "SAD")
    var cdVerificationRequestDataSAD: CdVerificationRequestDataSAD? = null
}

class CdVerificationRequestDataSAD(dclRefNo: String) {

    @JacksonXmlProperty(localName = "message_nature")
    var messageDate: Long? = 41

    @JacksonXmlProperty(localName = "sad_id")
    var sadId: String? = dclRefNo

    @JacksonXmlProperty(localName = "version")
    var version: Int? = 1

    @JacksonXmlProperty(localName = "oga_identification")
    var ogaIentification: String? = "KEBS"

    @JacksonXmlProperty(localName = "hold_detail")
    var holdetail: String? = "Request for Inspection"
}
