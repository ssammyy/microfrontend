package org.kebs.app.kotlin.apollo.store.customdto

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.PaymentMethodsEntity
import java.math.BigDecimal
import java.sql.Date
import java.text.SimpleDateFormat
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@JacksonXmlRootElement(localName = "APOLLO.DAT_KEBS_CD_DEMAND_NOTES")
class DemandNoteXmlDTO {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private val xmlns = "http://DAT_KEBS_CD_DEMAND_NOTE/xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xdb")
    private val xmlnsXdb = "http://xmlns.oracle.com/xdb"

    @JacksonXmlProperty(isAttribute = true, localName = "xsi:schemaLocation")
    private val schemaLocation = "http://DAT_KEBS_CD_DEMAND_NOTE/xsd"

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:xsi")
    private val xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance"

    @JacksonXmlProperty(localName = "APOLLO.DAT_KEBS_CD_DEMAND_NOTE")
    var customDemandNote: CustomDemandNoteXmlDto? = null
}

class CustomDemandNoteXmlDto {

    @JacksonXmlProperty(localName = "ID")
    var id: Long? = null

    @JacksonXmlProperty(localName = "NAME_IMPORTER")
    var nameImporter: String? = null

    @JacksonXmlProperty(localName = "ADDRESS")
    var address: String? = null

    @JacksonXmlProperty(localName = "TELEPHONE")
    var telephone: String? = null

    @JacksonXmlProperty(localName = "PRODUCT")
    var product: String? = null

    @JacksonXmlProperty(localName = "C_F_VALUE")
    var cfvalue: BigDecimal? = null

    @JacksonXmlProperty(localName = "RATE")
    var rate: String? = null

    @JacksonXmlProperty(localName = "AMOUNT_PAYABLE")
    var amountPayable: BigDecimal? = null

    @JacksonXmlProperty(localName = "ENTRY_ABL_NUMBER")
    var entryAblNumber: String? = null

    @JacksonXmlProperty(localName = "DATE_GENERATED")
    var dateGenerated: String? = null

    @JacksonXmlProperty(localName = "TOTAL_AMOUNT")
    var totalAmount: BigDecimal? = null

    @JacksonXmlProperty(localName = "CURRENCY")
    var currency: String? = null

    @JacksonXmlProperty(localName = "DEMAND_NOTE_NUMBER")
    var demandNoteNumber: String? = null

    @JacksonXmlProperty(localName = "RECEIPT_NO")
    var receiptNo: String? = null

    @JacksonXmlProperty(localName = "PAYMENT_INSTRUCTION_1")
    var paymentInstruction1: PaymenInstruction1? = null

    @JacksonXmlProperty(localName = "PAYMENT_INSTRUCTION_2")
    var paymentInstruction2: PaymenInstruction2? = null

    @JacksonXmlProperty(localName = "PAYMENT_INSTRUCTION_3")
    var paymentInstruction3: PaymenInstruction3? = null

    @JacksonXmlProperty(localName = "PAYMENT_INSTRUCTION_MPESA")
    var paymentInstructionMpesa: PaymenInstructionMpesa? = null

    @JacksonXmlProperty(localName = "PAYMENT_INSTRUCTION_OTHER")
    var paymentInstructionOther: PaymenInstructionOther? = null

    @JacksonXmlProperty(localName = "TRANSACTION_TYPE")
    var transactionType: String? = null

    @JacksonXmlProperty(localName = "TRANSACTION_NUMBER")
    var transactionNumber: String? = null

    @JacksonXmlProperty(localName = "VERSION")
    var version: Long? = null

    fun convertTimestampToKeswsValidDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date =sdf.format(date)
        return date
    }

}

class PaymenInstruction1(bank1Details: PaymentMethodsEntity) {

    @JacksonXmlProperty(localName = "NAME")
    var name: String? = bank1Details.bankAccountName.toString()

    @JacksonXmlProperty(localName = "BANK")
    var bank: String? = bank1Details.bankName.toString()

    @JacksonXmlProperty(localName = "BRANCH")
    var branch: String? = bank1Details.bankBranch.toString()

    @JacksonXmlProperty(localName = "KES_AC_NO")
    var kesAccNo: String? = bank1Details.bankAccountKesNumber.toString()

    @JacksonXmlProperty(localName = "USD_AC_NO")
    var usdAccNo: String? = bank1Details.bankAccountUsdNumber.toString()

    @JacksonXmlProperty(localName = "BANK_CODE")
    var bankCode: String? = bank1Details.bankCode.toString()
}

class PaymenInstruction2(bank2Details: PaymentMethodsEntity) {

    @JacksonXmlProperty(localName = "NAME")
    var name: String? = bank2Details.bankAccountName.toString()

    @JacksonXmlProperty(localName = "BANK")
    var bank: String? = bank2Details.bankName.toString()

    @JacksonXmlProperty(localName = "BRANCH")
    var branch: String? = bank2Details.bankBranch.toString()

    @JacksonXmlProperty(localName = "KES_AC_NO")
    var kesAccNo: String? = bank2Details.bankAccountKesNumber.toString()

    @JacksonXmlProperty(localName = "USD_AC_NO")
    var usdAccNo: String? = bank2Details.bankAccountUsdNumber.toString()

    @JacksonXmlProperty(localName = "BANK_CODE")
    var bankCode: String? = bank2Details.bankCode.toString()
}

class PaymenInstruction3(bank3Details: PaymentMethodsEntity) {

    @JacksonXmlProperty(localName = "NAME")
    var name: String? = bank3Details.bankAccountName.toString()

    @JacksonXmlProperty(localName = "BANK")
    var bank: String? = bank3Details.bankName.toString()

    @JacksonXmlProperty(localName = "BRANCH")
    var branch: String? = bank3Details.bankBranch.toString()

    @JacksonXmlProperty(localName = "KES_AC_NO")
    var kesAccNo: String? = bank3Details.bankAccountKesNumber.toString()

    @JacksonXmlProperty(localName = "USD_AC_NO")
    var usdAccNo: String? = bank3Details.bankAccountUsdNumber.toString()

    @JacksonXmlProperty(localName = "BANK_CODE")
    var bankCode: String? = bank3Details.bankCode.toString()
}

class PaymenInstructionMpesa(mpesaDetails: PaymentMethodsEntity) {

    @JacksonXmlProperty(localName = "PAYBILL_NO")
    var name: String? = mpesaDetails.payBillNo

    @JacksonXmlProperty(localName = "AC_NO")
    var bank: String? = mpesaDetails.mpesaAccNo
}

class PaymenInstructionOther(otherDetails: PaymentMethodsEntity) {

    @JacksonXmlProperty(localName = "VAT_NO")
    var name: String? = otherDetails.vatNo

    @JacksonXmlProperty(localName = "PIN_NO")
    var bank: String? = otherDetails.pinNo
}

fun CdDemandNoteEntity.toCdDemandNoteXmlRecordRefl() = with(CustomDemandNoteXmlDto::class.primaryConstructor!!) {
    val propertiesByName = CdDemandNoteEntity::class.memberProperties.associateBy { it.name }
    callBy(args = parameters.associate { parameter ->
        parameter to when (parameter.name) {
            else -> propertiesByName[parameter.name]?.get(this@toCdDemandNoteXmlRecordRefl)
        }
    })
}


