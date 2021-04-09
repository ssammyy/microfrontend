package org.kebs.app.kotlin.apollo.common.dto.kra.request

import com.fasterxml.jackson.annotation.*
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class PinValidationWebRequest(

    @NotEmpty(message = "Required field") val integration: Long,
    @NotNull(message = "Required field") val kraPin: String
)

class PinValidationRequest {
    @NotEmpty(message = "Required field")
    var loginId: String? = null

    @NotEmpty(message = "Required field")
    var password: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("KRAPIN")
    var kraPin: String? = null
}

/**
 * Request Payload Object
 * <pre>
 *     {@literal
 *
 *         {"REQUEST": {"loginId": "", "password": "", "hash": "", "transmissionDate": "", "HEADER": {"entryNo": "", "kraPin": "", "manufacturerName": "", "paymentSlipNo": "", "paymentSlipDate": "", "paymentType": "", "paymentDate": "", "totalDeclAmt": "", "totalPenaltyAmt": "", "TotalPaymentAmt": "", "Bank": "", "bankRefNo": ""}, "DETAILS": {"DECLARATION": [{"commodityType": "", "periodFrom": "", "periodTo": "", "qtyManf": "", "exFactVal": "", "levyPaid": ""}, {"commodityType": "", "periodFrom": "", "periodTo": "", "qtyManf": "", "exFactVal": "", "levyPaid": ""}], "PENALTY": [{"penaltyOrderNo": "", "periodFrom": "", "periodTo": "", "penaltyPaid": ""}, {"penaltyOrderNo": "", "periodFrom": "", "periodTo": "", "penaltyPaid": ""}]}}}
 *     }
 * </pre>
 */
@JsonRootName(value = "REQUEST")
class ReceiveSL2PaymentRequest {

    @NotNull(message = "Required field")
    var loginId: String? = null

    @NotNull(message = "Required field")
    var password: String? = null

    @NotEmpty(message = "Required field")
    var hash: String? = null

    @NotNull(message = "Required field")
//    DD-MM-YYYY’T’HH:MM:SS
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
    var transmissionDate: Timestamp? = Timestamp.from(Instant.now())

    @JsonProperty("HEADER")
    @NotNull(message = "Required field")
    @Valid
    var header: ReceiveSL2PaymentHeader? = null

    @JsonProperty("DETAILS")
    @Valid
    @NotNull(message = "Required field")
    var details: ReceiveSL2PaymentDetails? = null
}

class ReceiveSL2PaymentDetails {
    @JsonProperty("DECLARATION")
    @Valid
    var declaration: List<ReceiveSL2PaymentDeclaration>? = null

    @JsonProperty("PENALTY")
    @Valid
    var penalty: List<ReceiveSL2PaymentPenalty>? = null
}


class ReceiveSL2PaymentPenalty {
    var penaltyOrderNo: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var periodFrom: Date? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var periodTo: Date? = null
    var penaltyPaid: BigDecimal? = null
}

class ReceiveSL2PaymentDeclaration {
    @NotEmpty(message = "Required field")
    var commodityType: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var periodFrom: Date? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var periodTo: Date? = null

    @NotNull(message = "Required field")
    var qtyManf: BigDecimal? = null

    @NotNull(message = "Required field")
    var exFactVal: BigDecimal? = null

    @NotNull(message = "Required field")
    var levyPaid: BigDecimal? = null

}

class ReceiveSL2PaymentHeader {
    @NotEmpty(message = "Required field")
    var entryNo: String? = null

    @NotEmpty(message = "Required field")
    var kraPin: String? = null

    @NotEmpty(message = "Required field")
    var manufacturerName: String? = null

    @NotEmpty(message = "Required field")
    var paymentSlipNo: String? = null


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var paymentSlipDate: Date? = null

    var paymentType: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var paymentDate: Date? = null

    var totalDeclAmt: BigDecimal? = null
    var totalPenaltyAmt: BigDecimal? = null

    @JsonProperty("TotalPaymentAmt")
    var totalPaymentAmt: BigDecimal? = null


    @NotEmpty(message = "Required field")
    @JsonProperty("Bank")
    var bank: String? = null

    @NotEmpty(message = "Required field")
    var bankRefNo: String? = null
}

class PenaltyInfo {
    @JsonProperty("PenaltyOrderNo")
    var penaltyOrderNo: String? = null
    var entryNo: String? = null
    var kraPin: String? = null
    var manufacName: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var periodFrom: Date? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var periodTo: Date? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
    var penaltyGenDate: Timestamp? = null
    var penaltyPayable: BigDecimal? = null
}


data class RequestHeader(
    val loginId: String,
    val password: String,
    val hash: String,
    val noOfRecords: String?


) {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
    var transmissionDate: Timestamp = Timestamp.from(Instant.now())
}


data class EntryDetails(
    val entryNumber: String?,
    val kraPin: String?,
    val manufacturerName: String?,
    val registrationDate: String,
    val status: String?,

    )


@JsonTypeName(value = "REQUEST")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
class EntryRequest {
    @JsonProperty("HEADER")
    var header: RequestHeader? = null

    @JsonProperty("DETAILS")
    var details: List<EntryDetails>? = null
}

@JsonTypeName(value = "REQUEST")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
class PenaltyRequest {
    @JsonProperty("HEADER")
    var header: RequestHeader? = null

    @JsonProperty("PENALTYINFO")
    var details: List<PenaltyInfo>? = null
}
