package org.kebs.app.kotlin.apollo.common.dto.kra.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.sql.Date


class PinValidationResponse {
    var pinValidationResponseResult: PinValidationResponseResult? = null
    val pinData: PinData? = null
    val principalPhysicalAddress: PrincipalPhysicalAddress? = null
    val principalPostalAddress: PrincipalPostalAddress? = null
    val shareholderDetails: ShareholderDetails? = null
}

@JacksonXmlRootElement(localName = "PINDATA")
class PinData {
    @JsonProperty("KRAPIN")
    var kraPin: String? = null

    @JsonProperty("PINIssuanceDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var pINIssuanceDate: Date? = null

    @JsonProperty("TypeOfTaxpayer")
    var typeOfTaxpayer: String? = null

    @JsonProperty("BusinessType")
    var businessType: String? = null

    @JsonProperty("BusinessSubType")
    var businessSubType: String? = null

    @JsonProperty("ResidentialStatus")
    var residentialStatus: String? = null

    @JsonProperty("IdentificationNumber")
    var identificationNumber: String? = null

    @JsonProperty("FirstName")
    var firstName: String? = null

    @JsonProperty("MiddleName")
    var middleName: String? = null

    @JsonProperty("LastName")
    var lastName: String? = null

    @JsonProperty("StatusOfPIN")
    var statusOfPIN: String? = null

    @JsonProperty("DateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var dateOfBirth: Date? = null

    @JsonProperty("Gender")
    var gender: String? = null

    @JsonProperty("Country")
    var country: String? = null

    @JsonProperty("MobileNumber")
    var mobileNumber: String? = null

    @JsonProperty("TelephoneNumber")
    var telephoneNumber: String? = null

    @JsonProperty("BusinessRegistrationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var businessRegistrationDate: Date? = null

    @JsonProperty("EPZEffectiveDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var ePZEffectiveDate: Date? = null

    @JsonProperty("EmailAddress")
    var emailAddress: String? = null

    @JsonProperty("LegalRepresentativePIN")
    var legalRepresentativePIN: String? = null

    @JsonProperty("PrincipalPhysicalAddress")
    var principalPhysicalAddress: PrincipalPhysicalAddress? = null

    @JsonProperty("PrincipalPostalAddress")
    var principalPostalAddress: PrincipalPostalAddress? = null

    @JsonProperty("ShareholderDetails")
    var shareholderDetails: ShareholderDetails? = null
}

class ShareholderDetails {
    var shareholder: List<Shareholder>? = null
}

class Shareholder {
    @JsonProperty("ShareholderPin")
    var shareholderPin: String? = null

    @JsonProperty("TypeOfShareholder")
    var typeOfShareholder: String? = null

    @JsonProperty("ShareholdingRatio")
    var shareholdingRatio: String? = null


}


class PrincipalPostalAddress {
    @JsonProperty("PostalCode")
    var postalCode: String? = null

    @JsonProperty("POBox")
    var pOBox: String? = null

}

class PrincipalPhysicalAddress {
    @JsonProperty("LRNumber")
    var lRNumber: String? = null

    @JsonProperty("Building")
    var building: String? = null

    @JsonProperty("StreetRoad")
    var streetRoad: String? = null

    @JsonProperty("CityTown")
    var cityTown: String? = null

    @JsonProperty("County")
    var county: String? = null

    @JsonProperty("District")
    var district: String? = null

}

@JacksonXmlRootElement(localName = "RESULT")
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
class PinValidationResponseResult {
    var responseCode: String? = null
    var message: String? = null
    var status: String? = null
}

class RequestFinalResults{
    @JsonProperty("RESULT")
    var requestResult: RequestResult? = null
}


@JacksonXmlRootElement(localName = "RESULT")
class RequestResult {
    @JsonProperty("RESPONSECODE")
    var responseCode: String? = null

    @JsonProperty("MESSAGE")
    var message: String? = null

    @JsonProperty("STATUS")
    var status: String? = null
}
