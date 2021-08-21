package org.kebs.app.kotlin.apollo.store.customdto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "Consignment_Approval_Status")
class CDApprovalResponseDTO {

//    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsi")
//    private val xmlnsXdb = "http://xmlns.oracle.com/xdb"
//
//    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsd")
//    private val xmlnsXsd = "http://www.w3.org/2001/XMLSchema"

    @JacksonXmlProperty(localName = "DocumentHeader")
    var documentHeader: DocumentHeader? = null

    @JacksonXmlProperty(localName = "DocumentDetails")
    var documentDetails: DocumentDetails? = null
}

class DocumentHeader(corNumber: String, msgDate: String, version: String) {

    @JacksonXmlProperty(localName = "refno")
    var refNumber: String? = corNumber

    @JacksonXmlProperty(localName = "msgdate")
    var msgDate: String? = msgDate

    @JacksonXmlProperty(localName = "receiver")
    var receiver: String? = "KESWS"

    @JacksonXmlProperty(localName = "sender")
    var sender: String? = "KEBS"

    @JacksonXmlProperty(localName = "msg_func")
    var msgFunc: String? = "9"

    @JacksonXmlProperty(localName = "msgid")
    var msgId: String? = "OG_CD_RES"

    @JacksonXmlProperty(localName = "version")
    var version: String? = version
}

class DocumentDetails(corNumber: String, expiryDate: String, assessedDate: String, status: String, pgaRemarks: String, checkingOfficer: String) {

    @JacksonXmlProperty(localName = "consignment_refnumber")
    var consignmentRefNumber: String? = corNumber

    @JacksonXmlProperty(localName = "amount")
    var amount: String? = "0"

    @JacksonXmlProperty(localName = "currency")
    var currency: String? = "KES"

    @JacksonXmlProperty(localName = "expiry_date")
    var expiryDate: String? = expiryDate

    @JacksonXmlProperty(localName = "role_code")
    var roleCode: String? = "CO"

    @JacksonXmlProperty(localName = "permit_no")
    var permitNo: String? = null

    @JacksonXmlProperty(localName = "conditional_remarks")
    var conditionalRemarks: String? = "Test"

    @JacksonXmlProperty(localName = "oga_queries")
    var ogaQueries: String? = ""

    @JacksonXmlProperty(localName = "queries_response")
    var queriesResponse: String? = null

    @JacksonXmlProperty(localName = "io_ind")
    var ioInd: String? = "N"

    @JacksonXmlProperty(localName = "user_id")
    var userId: String? = checkingOfficer

    @JacksonXmlProperty(localName = "pga_remarks")
    var pgaRemarks: String? = pgaRemarks

    @JacksonXmlProperty(localName = "certificate_no")
    var certificateNo: String? = null

    @JacksonXmlProperty(localName = "ver_no")
    var verNo: String? = "1"

    @JacksonXmlProperty(localName = "vo_ind")
    var voInd: String? = "N"

    @JacksonXmlProperty(localName = "inspection_type")
    var inspectionType: String? = null

    @JacksonXmlProperty(localName = "revenue_code")
    var revenueCode: String? = null

    @JacksonXmlProperty(localName = "status")
    var status: String? = status

    @JacksonXmlProperty(localName = "pga_risk_assessment_lane")
    var pgaRiskAssessmentLane: String? = "1"

    @JacksonXmlProperty(localName = "assessed_by")
    var assessedBy: String? = checkingOfficer

    @JacksonXmlProperty(localName = "assessed_date")
    var assesedDate: String? = assessedDate

    @JacksonXmlProperty(localName = "assessed_remarks")
    var assesedRemarks: String? = "Test"

    @JacksonXmlProperty(localName = "cont_details")
    var contDetails: ContDetails? = null
}

class ContDetails() {

    @JacksonXmlProperty(localName = "cont_number")
    var contNumber: String? = null

}
