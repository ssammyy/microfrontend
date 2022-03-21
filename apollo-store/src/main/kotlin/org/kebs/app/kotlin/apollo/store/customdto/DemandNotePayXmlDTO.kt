package org.kebs.app.kotlin.apollo.store.customdto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import java.math.BigDecimal
import java.sql.Date
import java.text.SimpleDateFormat

@JacksonXmlRootElement(localName = "APOLLO.DAT_KEBS_CD_DEMAND_NOTES_PAYMENT")
class DemandNotePayXmlDTO {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private val xmlns = "http://DAT_KEBS_CD_DEMAND_NOTE_PAYMENT/xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xdb")
    private val xmlnsXdb = "http://xmlns.oracle.com/xdb"

    @JacksonXmlProperty(isAttribute = true, localName = "xsi:schemaLocation")
    private val schemaLocation = "http://DAT_KEBS_CD_DEMAND_NOTE_PAYMENT/xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsi")
    private val xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance"

    @JacksonXmlProperty(localName = "APOLLO.DAT_KEBS_CD_DEMAND_NOTE_PAYMENT")
    var customDemandNotePay: CustomDemandNotePayXmlDto? = null
}

class CustomDemandNotePayXmlDto(demandNoteEntity: CdDemandNoteEntity) {

    @JacksonXmlProperty(localName = "DEMAND_NOTE_NUMBER")
    var demandNoteNumber: String? = demandNoteEntity.postingReference

    @JacksonXmlProperty(localName = "AMOUNT_PAID")
    var amountPaid: BigDecimal? = demandNoteEntity.totalAmount

    @JacksonXmlProperty(localName = "RECEIPT_NO")
    var receiptNo: String? = demandNoteEntity.receiptNo

    //Todo: create paymentDate from reconciliation table
    @JacksonXmlProperty(localName = "PAYMENT_DATE")
    var paymentDate: String? = convertTimestampToKeswsValidDate(Date(java.util.Date().time))

    fun convertTimestampToKeswsValidDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date =sdf.format(date)
        return date
    }
}
