package org.kebs.app.kotlin.apollo.api.ports.provided.kra.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.OptBoolean
import org.json.simple.JSONArray
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class KraRequest {

    @JsonProperty("HEADER")
    @NotNull(message = "Required field")
    @Valid
    var header: KraHeader? = null


    @JsonProperty("DETAILS")
    @Valid
    @NotNull(message = "Required field")
    var details: MutableList<KraDetails>? = null
}

class KraHeader {

    companion object {
        var globalVar = ""
    }

    @NotNull(message = "Required field")
    var loginId: String? = null

    @NotNull(message = "Required field")
    var password: String? = null

    @NotEmpty(message = "Required field")
    var hash: String? = null

    @NotNull(message = "Required field")
    var noOfRecords: String? = null

    @NotNull(message = "Required field")
//    DD-MM-YYYY’T’HH:MM:SS
    //  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
    var transmissionDate: String? = globalVar


}

data class KraDataRequest(
    @JsonProperty("REQUEST")
    @NotNull(message = "Required field")
    @Valid
    var request : List<Any>? = null
    //var request: KraRequests? = null


)

//class KraRequest {
//
//    @JsonProperty("HEADER")
//    @NotNull(message = "Required field")
//    @Valid
//    var header: KraHeader? = null
//
//
//    @JsonProperty("DETAILS")
//    @Valid
//    @NotNull(message = "Required field")
//    var details: MutableList<KraDetails>? = null
//}

class KraRequestHeader{
    @JsonProperty("HEADER")
    @NotNull(message = "Required field")
    @Valid
    var header: KraHeader? = null
}

class KraRequestDetails{
    @JsonProperty("DETAILS")
    @Valid
    @NotNull(message = "Required field")
    var details: KraDetails? = null
    // var details: List<KraDetails>? = null
}



//class KraRequest {
//
//    @JsonProperty("REQUEST")
//    @NotNull(message = "Required field")
//    @Valid
//    var request : List<Any>? = null
//}





class KraDetails {

    @NotEmpty(message = "Required field")
    var entryNumber: String? = null

    @NotEmpty(message = "Required field")
    var kraPin: String? = null

    @NotEmpty(message = "Required field")
    var manufacturerName: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var registrationDate: String? = null

    @NotEmpty(message = "Required field")
    var status: String? = null

}



class SageRequestBody {

    @JsonProperty("header")
    @NotNull(message = "Required field")
    @Valid
    var header: SageHeader? = null

    @JsonProperty("request")
    @Valid
    @NotNull(message = "Required field")
    var request: SageRequest? = null

    @JsonProperty("details")
    @Valid
    @NotNull(message = "Required field")
    var details: MutableList<SageDetails>? = null
}

class SageHeader
{

    @JsonProperty("serviceName")
    @NotNull(message = "Required field")
    var serviceName: String? = null

    @JsonProperty("messageID")
    @NotNull(message = "Required field")
    var messageID: String? = null

    @JsonProperty("connectionID")
    @NotEmpty(message = "Required field")
    var connectionID: String? = null

    @JsonProperty("connectionPassword")
    @NotNull(message = "Required field")
    var connectionPassword: String? = null
}
class SageRequest
{
    @JsonProperty("BatchNo")
    @NotNull(message = "Required field")
    var BatchNo: String? = null

    @JsonProperty("DocumentDate")
    @NotNull(message = "Required field")
    var DocumentDate: String? = null

    @JsonProperty("InvoiceType")
    @NotEmpty(message = "Required field")
    var InvoiceType: Long? = null

    @JsonProperty("ServiceType")
    @NotNull(message = "Required field")
    var ServiceType: String? = null

    @JsonProperty("CurrencyCode")
    @NotNull(message = "Required field")
    var CurrencyCode: String? = null

    @JsonProperty("CustomerCode")
    @NotNull(message = "Required field")
    var CustomerCode: String? = null
    @JsonProperty("CustomerName")
    @NotNull(message = "Required field")
    var CustomerName: String? = null
    @JsonProperty("InvoiceDesc")
    @NotNull(message = "Required field")
    var InvoiceDesc: String? = null
    @JsonProperty("InvoiceAmnt")
    @NotNull(message = "Required field")
    var InvoiceAmnt: BigDecimal? = null

    @JsonProperty("TaxPINNo")
    @NotNull(message = "Required field")
    var TaxPINNo: String? = null
}
class SageDetails
{
    @JsonProperty("RevenueAcc")
    @NotNull(message = "Required field")
    var RevenueAcc: String? = null

    @JsonProperty("RevenueAccDesc")
    @NotNull(message = "Required field")
    var RevenueAccDesc: String? = null

    @JsonProperty("Taxable")
    @NotEmpty(message = "Required field")
    var Taxable: Long? = null

    @JsonProperty("MAmount")
    @NotNull(message = "Required field")
    var MAmount: Long? = null

    @JsonProperty("TaxAmount")
    @NotNull(message = "Required field")
    var TaxAmount: Long? = null
}

class HeaderSage {
    var messageID: String? = null
    var statusCode = 0
    var statusDescription: String? = null
}

class ResponseSage {
    @JsonProperty("DocumentNo")
    var documentNo: String? = null

    @JsonProperty("ResponseDate")
    var responseDate: String? = null
}

class QASageRoot {
    var header: HeaderSage? = null
    var response: ResponseSage? = null
}




class SageHeaderB {
    @JsonProperty("messageID")
    var messageID: String? = null

    @JsonProperty("statusCode")
    @NotNull(message = "Status code is required")
    var statusCode: Int? = null

    @JsonProperty("statusDescription")
    @NotNull(message = "Status description is required")
    var statusDescription: String? = null
}

class SageResponse {
    @JsonProperty("DocumentNo")
    @NotNull(message = "DocumentNo is required")
    val documentNo: String? = null

    @JsonProperty("ResponseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, lenient = OptBoolean.TRUE)
    val responseDate: Timestamp? = null
}

class SagePostingResponseResult {
    @JsonProperty("header")
    @Valid
    @NotNull(message = "Header details are required")
    var header: SageHeaderB? = null

    @Valid
    @JsonProperty("response")
    @NotNull(message = "Request details are required")
    val response: SageResponse? = null
}
