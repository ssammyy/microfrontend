package org.kebs.app.kotlin.apollo.common.dto.kesws.receive

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

import javax.validation.constraints.NotNull

class ConsignmentDocument {
    @NotNull
    @JsonProperty("DocumentHeader")
    val documentHeader: DocumentHeader? = null

    @NotNull
    @JsonProperty("DocumentDetails")
    val documentDetails: DocumentDetails? = null
}

class DocumentDetails {
    @NotNull
    @JsonProperty("ConsignmentDocDetails")
    val consignmentDocDetails: ConsignmentDocDetails? = null
}

class DocumentHeader {
    @NotNull
    @JsonProperty("DocumentReference")
    val documentReference: DocumentReference? = null

    @NotNull
    @JsonProperty("DocumentExchangeDetails")
    val documentExchangeDetails: DocumentExchangeDetails? = null
}

class DocumentExchangeDetails {
    @NotNull
    @JsonProperty("ReceivingPartyDetails")
    val receivingPartyDetails: ReceivingPartyDetails? = null

    @NotNull
    @JsonProperty("NotifyPartyDetails")
    val notifyPartyDetails: NotifyPartyDetails? = null
}

class NotifyPartyDetails {
    @NotNull
    @JsonProperty("NotifyParty")
    var notifyParty: String? = null
}

class ReceivingPartyDetails {
    @NotNull
    @JsonProperty("ReceivingParty")
    var receivingParty: String? = null
}

class DocumentReference {
    @NotNull
    @JsonProperty("DocumentType")
    var documentType: String? = null

    @NotNull
    @JsonProperty("DocumentName")
    var documentName: String? = null

    @NotNull
    @JsonProperty("DocumentNumber")
    var documentNumber: String? = null

    @NotNull
    @JsonProperty("CommonRefNumber")
    var commonRefNumber: String? = null

    @NotNull
    @JsonProperty("MessageType")
    var messageType: String? = null

    @NotNull
    @JsonProperty("SenderID")
    var senderID: String? = null

    @NotNull
    @JsonProperty("RegimeCode")
    var regimeCode: String? = null

    @NotNull
    @JsonProperty("CMSRegimeCode")
    var cMSRegimeCode: String? = null

    @NotNull
    @JsonProperty("ApprovalCategory")
    var approvalCategory: String? = null
}


class ConsignmentDocDetails {
    @NotNull
    @JsonProperty("CDStandard")
    val cdStandard: CDStandardResponse? = null

    @NotNull
    @JsonProperty("ApprovalDetails")
    val approvalDetails: ApprovalDetails? = null

    @NotNull
    @JsonProperty("CDImporter")
    val cdImporter: CdImporterResponse? = null

    @NotNull
    @JsonProperty("CDHeaderOne")
    val cdHeaderOne: CdHeaderOneResponse? = null

    @NotNull
    @JsonProperty("CDHeaderTwo")
    val cdHeaderTwo: CDHeaderTwoResponse? = null

    @NotNull
    @JsonProperty("CDExporter")
    val cdExporter: CdExporterResponse? = null

    @NotNull
    @JsonProperty("CDConsignee")
    val cdConsignee: CdConsigneeResponse? = null

    @NotNull
    @JsonProperty("CDConsignor")
    val cdConsignor: CdConsignorResponse? = null

    @NotNull
    @JsonProperty("CDTransport")
    val cdTransport: CdTransportResponse? = null

//    @NotNull
    @JsonProperty("PGAHeaderFields")
    val cdPGAHeaderFields: PGAHeaderFieldsResponse? = null

    @NotNull
    @JsonProperty("CDStandardTwo")
    val cdStandardTwo: CDStandardTwoResponse? = null

    @NotNull
    @JsonProperty("CDProductDetails")
    val cdProductDetails: CDProductDetails? = null

    @NotNull
    @JsonProperty("CDRiskDetails")
    val cdRiskDetailsResponse: CDRiskDetailsResponse? = null

    @NotNull
    @JsonProperty("CDHdrAdditional")
    val CDHdrAdditional: CDHdrAdditional? = null

}

class CDHdrAdditional {
//Todo:  written on xsd details "You may enter ANY elements at this point"
}

class CDRiskDetailsResponse {
    @NotNull
    @JsonProperty("RiskAssessment")
    @JacksonXmlElementWrapper(useWrapping = false)
    val riskAssessmentResponse: List<RiskAssessmentResponse>? = null
}

class RiskAssessmentResponse {
    @JsonProperty("ProfileCode")
    var profileCode: String? = null

    @JsonProperty("ProfileName")
    var profileName: String? = null

    @JsonProperty("AssessedLane")
    var assessedLane: String? = null

    @JsonProperty("AssesedDate")
    var assessedDate: String? = null

    @JsonProperty("ActionDetails")
    var actionDetails: ActionDetails? = null
}

class ActionDetails {
    @NotNull
    @JsonProperty("Action")
    @JacksonXmlElementWrapper(useWrapping = false)
    val actionValues: List<ActionValues>? = null
}

class ActionValues {
    @JsonProperty("ActionCode")
    var actionCode: String? = null

    @JsonProperty("ActionName")
    var actionName: String? = null
}

class CDHeaderTwoResponse {
    @JsonProperty("TermsOfPayment")
    var termsOfPayment: String? = null

    @JsonProperty("TermsOfPaymentDesc")
    var termsOfPaymentDesc: String? = null

    @JsonProperty("LocalBankCode")
    var localBankCode: String? = null

    @JsonProperty("LocalBankDesc")
    var localBankDesc: String? = null

    @JsonProperty("ReceiptOfRemittance")
    var receiptOfRemittance: String? = null

    @JsonProperty("RemittanceCurrency")
    var remittanceCurrency: String? = null

    @JsonProperty("RemittanceAmount")
    var remittanceAmount: String? = null

    @JsonProperty("RemittanceDate")
    var remittanceDate: String? = null

    @JsonProperty("RemittanceReference")
    var remittanceReference: String? = null
}

class PGAHeaderFieldsResponse {
    @JsonProperty("CollectionOffice")
    var collectionOffice: String? = null

    @JsonProperty("CertificationCategory")
    var certificationCategory: String? = null

    @JsonProperty("PlaceOfApplication")
    var placeOfApplication: String? = null

    @JsonProperty("PreferredInspectionDate")
    var preferredInspectionDate: String? = null
}

class ApprovalDetails {
    @NotNull
    @JsonProperty("ApprovalHistory")
    @JacksonXmlElementWrapper(useWrapping = false)
    val approvalHistory: List<ApprovalHistoryResponse>? = null
}

class ApprovalHistoryResponse {

    @JsonProperty("StageNo")
    var stageNo: String? = null

    @JsonProperty("StepCode")
    var stepCode: String? = null

    @JsonProperty("MDACode")
    var MDACode: String? = null

    @JsonProperty("RoleCode")
    var roleCode: String? = null

    @JsonProperty("Status")
    var status: String? = null

    @JsonProperty("UserId")
    var userId: String? = null

    @JsonProperty("UpdatedDate")
    var updatedDate: String? = null

    @JsonProperty("PremiseInspection")
    var premiseInspection: String? = null

    @JsonProperty("ExaminationRequired")
    var examinationRequired: String? = null

    @JsonProperty("TechnicalRejection")
    var technicalRejection: String? = null

}


class CDStandardTwoResponse {
    @JsonProperty("ConditionsOfApproval")
    var conditionsOfApproval: String? = null

    @JsonProperty("ApplicantRemarks")
    var applicantRemarks: String? = null

    @JsonProperty("MDARemarks")
    var mdaRemarks: String? = null

    @JsonProperty("CustomsRemarks")
    var customsRemarks: String? = null

    @JsonProperty("PurposeOfImport")
    var purposeOfImport: String? = null

    @JsonProperty("COCType")
    var cocType: String? = null

    @JsonProperty("LocalCOCType")
    var localCocType: String? = null

    @JsonProperty("ThirdPartyDetails")
    var thirdPartyDetails: ThirdPartyDetails? = null

    @JsonProperty("ApplicantDefinedThirdPartyDetails")
    var applicantDefinedThirdPartyDetails: ApplicantDefinedThirdPartyDetails? = null

    @JsonProperty("ProcessingFee")
    var processingFeeResponse: ProcessingFeeResponse? = null

    @JsonProperty("DocumentFee")
    var documentFeeResponse: DocumentFeeResponse? = null


}

class DocumentFeeResponse {
    @JsonProperty("Currency")
    var currency: String? = null

    @JsonProperty("AmountFCY")
    var amountFCY: String? = null

    @JsonProperty("AmountNCY")
    var amountNCY: String? = null

    @JsonProperty("PaymentMode")
    var paymentMode: String? = null

    @JsonProperty("ReceiptNumber")
    var receiptNumber: String? = null

    @JsonProperty("ReceiptDate")
    var receiptDate: String? = null
}

class ProcessingFeeResponse {
    @JsonProperty("Currency")
    var currency: String? = null

    @JsonProperty("AmountFCY")
    var amountFCY: String? = null

    @JsonProperty("AmountNCY")
    var amountNCY: String? = null

    @JsonProperty("PaymentMode")
    var paymentMode: String? = null

    @JsonProperty("ReceiptNumber")
    var receiptNumber: String? = null

    @JsonProperty("ReceiptDate")
    var receiptDate: String? = null
}

class ApplicantDefinedThirdPartyDetails {
    @NotNull
    @JsonProperty("ThirdParties")
    @JacksonXmlElementWrapper(useWrapping = false)
    val thirdPartiesApplicantDefinedResponse: List<ThirdPartiesApplicantDefinedResponse>? = null
}

class ThirdPartiesApplicantDefinedResponse {
    @JsonProperty("ThirdPartyCode")
    var thirdPartyCode: String? = null

    @JsonProperty("ThirdPartyDescription")
    var thirdPartyDescription: String? = null

    @JsonProperty("DistributionMethod")
    var distributionMethod: String? = null

    @JsonProperty("ThirdPartyMailbox")
    var thirdPartyMailbox: String? = null

    @JsonProperty("ThirdPartyAccount")
    var thirdPartyAccount: String? = null
}

class ThirdPartyDetails {
    @NotNull
    @JsonProperty("ThirdParties")
    @JacksonXmlElementWrapper(useWrapping = false)
    val thirdPartyResponses: List<ThirdPartiesResponse>? = null
}

class ThirdPartiesResponse {
    @JsonProperty("ThirdPartyCode")
    var thirdPartyCode: String? = null

    @JsonProperty("ThirdPartyDescription")
    var thirdPartyDescription: String? = null

    @JsonProperty("DistributionMethod")
    var distributionMethod: String? = null

    @JsonProperty("ThirdPartyMailbox")
    var thirdPartyMailbox: String? = null

    @JsonProperty("ThirdPartyAccount")
    var thirdPartyAccount: String? = null
}


class CDStandardTwoAttachmentsResponse {
    @JsonProperty("AttachDocumentCode")
    var attachDocumentCode: String? = null

    @JsonProperty("AttachDocumentCodeDesc")
    var attachDocumentCodeDesc: String? = null

    @JsonProperty("AttachDocumentRefNo")
    var attachDocumentRefNo: String? = null

    @JsonProperty("AttachDocumentInternalRefNo")
    var attachDocumentInternalRefNo: String? = null

}


class CDStandardResponse {
    @JsonProperty("ApplicationTypeCode")
    var applicationTypeCode: String? = null

    @JsonProperty("ApplicationTypeDescription")
    var applicationTypeDescription: String? = null

    @JsonProperty("DocumentTypeCode")
    var documentTypeCode: String? = null

    @JsonProperty("CMSDocumentTypeCode")
    var cmsDocumentTypeCode: String? = null

    @JsonProperty("DocumentTypeDescription")
    var documentTypeDescription: String? = null

    @JsonProperty("ConsignmentTypeCode")
    var consignmentTypeCode: String? = null

    @JsonProperty("ConsignmentTypeDescription")
    var consignmentTypeDescription: String? = null

    @JsonProperty("MDACode")
    var mdaCode: String? = null

    @JsonProperty("MDADescription")
    var mdaDescription: String? = null

    @JsonProperty("DocumentCode")
    var documentCode: String? = null

    @JsonProperty("DocumentDescription")
    var documentDescription: String? = null

    @JsonProperty("ProcessCode")
    var processCode: String? = null

    @JsonProperty("ProcessDescription")
    var processDescription: String? = null

    @JsonProperty("ApplicationDate")
    var applicationDate: String? = null

    @JsonProperty("UpdatedDate")
    var updatedDate: String? = null

    @JsonProperty("ExpiryDate")
    var expiryDate: String? = null

    @JsonProperty("AmendedDate")
    var amendedDate: String? = null

    @JsonProperty("ApprovalStatus")
    var approvalStatus: String? = null

    @JsonProperty("ApprovalDate")
    var approvalDate: String? = null

    @JsonProperty("FinalApprovalDate")
    var finalApprovalDate: String? = null

    @JsonProperty("UsedStatus")
    var usedStatus: String? = null

    @JsonProperty("UsedDate")
    var usedDate: String? = null

    @JsonProperty("ApplicationRefNo")
    var applicationRefNo: String? = null

    @JsonProperty("VersionNo")
    var versionNo: String? = null

    @JsonProperty("UCRNumber")
    var ucrNumber: String? = null

    @JsonProperty("ReferencedPermitExemptionNo")
    var referencedPermitExemptionNo: String? = null

    @JsonProperty("ReferencedPermitExemptionVersionNo")
    var referencedPermitExemptionVersionNo: String? = null

    @JsonProperty("DeclarationNumber")
    var declarationNumber: String? = null

    @JsonProperty("ServiceProvider")
    val cdServiceProvider: CDServiceProviderResponse? = null
}


class CDServiceProviderResponse {
    @JsonProperty("ApplicationCode")
    var applicationCode: String? = null

    @JsonProperty("Name")
    var name: String? = null

    @JsonProperty("TIN")
    var pin: String? = null

    @JsonProperty("PhysicalAddress")
    var physicalAddress: String? = null

    @JsonProperty("PhyCountry")
    var phyCountry: String? = null
}


class CdImporterResponse {
    @JsonProperty("Name")
    val name: String? = null

    @JsonProperty("TIN")
    val pin: String? = null

    @JsonProperty("MDARefNo")
    val mdaRefNo: String? = null

    @JsonProperty("PhysicalAddress")
    val physicalAddress: String? = null

    @JsonProperty("PhyCountry")
    val physicalCountry: String? = null

    var physicalCountryName: String? = null

    @JsonProperty("PostalAddress")
    val postalAddress: String? = null

    @JsonProperty("PosCountry")
    val postalCountry: String? = null

    val postalCountryName: String? = null

    @set:JsonProperty("TeleFax")
    var telefax: String?
        get() = this.telephone ?: this.fax
        set(value) {
            telephone = value
            fax = value
        }

    var telephone: String? = null

    var fax: String? = null

    @JsonProperty("Email")
    val email: String? = null

    @JsonProperty("SectorofActivity")
    val sectorOfActivity: String? = null

    @JsonProperty("WarehouseCode")
    val warehouseCode: String? = null

    @JsonProperty("WarehouseLocation")
    val warehouseLocation: String? = null

//    @JsonProperty("")
//    val ogaRefNo :String? = null

}


class CdExporterResponse {
    @JsonProperty("Name")
    val name: String? = null

    @JsonProperty("TIN")
    val pin: String? = null

    @JsonProperty("MDARefNo")
    val mdaRefNo: String? = null

    @JsonProperty("PhysicalAddress")
    val physicalAddress: String? = null

    @JsonProperty("PhyCountry")
    val physicalCountry: String? = null

    var physicalCountryName: String? = null

    @JsonProperty("PostalAddress")
    val postalAddress: String? = null

    @JsonProperty("PosCountry")
    val postalCountry: String? = null

    val postalCountryName: String? = null

    @set:JsonProperty("TeleFax")
    var telefax: String?
        get() = this.telephone ?: this.fax
        set(value) {
            telephone = value
            fax = value
        }

    var telephone: String? = null

    var fax: String? = null

    @JsonProperty("Email")
    val email: String? = null

    @JsonProperty("SectorofActivity")
    val sectorOfActivity: String? = null

    @JsonProperty("WarehouseCode")
    val warehouseCode: String? = null

    @JsonProperty("WarehouseLocation")
    val warehouseLocation: String? = null

//    @JsonProperty("")
//    val ogaRefNo :String? = null

}


class CdConsignorResponse {
    @JsonProperty("Name")
    val name: String? = null

    @JsonProperty("TIN")
    val pin: String? = null

    @JsonProperty("MDARefNo")
    val mdaRefNo: String? = null

    @JsonProperty("PhysicalAddress")
    val physicalAddress: String? = null

    @JsonProperty("PhyCountry")
    val physicalCountry: String? = null

    var physicalCountryName: String? = null

    @JsonProperty("PostalAddress")
    val postalAddress: String? = null

    @JsonProperty("PosCountry")
    val postalCountry: String? = null

    val postalCountryName: String? = null

    @set:JsonProperty("TeleFax")
    var telefax: String?
        get() = this.telephone ?: this.fax
        set(value) {
            telephone = value
            fax = value
        }

    var telephone: String? = null

    var fax: String? = null

    @JsonProperty("Email")
    val email: String? = null

    @JsonProperty("SectorofActivity")
    val sectorOfActivity: String? = null

    @JsonProperty("WarehouseCode")
    val warehouseCode: String? = null

    @JsonProperty("WarehouseLocation")
    val warehouseLocation: String? = null

//    @JsonProperty("")
//    val ogaRefNo :String? = null

}


class CdConsigneeResponse {
    @JsonProperty("Name")
    val name: String? = null

    @JsonProperty("TIN")
    val pin: String? = null

    @JsonProperty("MDARefNo")
    val mdaRefNo: String? = null

    @JsonProperty("PhysicalAddress")
    val physicalAddress: String? = null

    @JsonProperty("PhyCountry")
    val physicalCountry: String? = null

    var physicalCountryName: String? = null

    @JsonProperty("PostalAddress")
    val postalAddress: String? = null

    @JsonProperty("PosCountry")
    val postalCountry: String? = null

    val postalCountryName: String? = null

    @set:JsonProperty("TeleFax")
    var telefax: String?
        get() = this.telephone ?: this.fax
        set(value) {
            telephone = value
            fax = value
        }

    var telephone: String? = null

    var fax: String? = null

    @JsonProperty("Email")
    val email: String? = null

    @JsonProperty("SectorofActivity")
    val sectorOfActivity: String? = null

    @JsonProperty("WarehouseCode")
    val warehouseCode: String? = null

    @JsonProperty("WarehouseLocation")
    val warehouseLocation: String? = null

//    @JsonProperty("")
//    val ogaRefNo :String? = null

}


class CdTransportResponse {

    @JsonProperty("ModeOfTransport")
    val modeOfTransport: String? = null

    @JsonProperty("ModeOfTransportDesc")
    val modeOfTransportDesc: String? = null

    @JsonProperty("VesselName")
    val vesselName: String? = null

    @JsonProperty("VoyageNo")
    val voyageNo: String? = null

    @JsonProperty("DateOfArrival")
    val dateOfArrival: String? = null

    @JsonProperty("ShipmentDate")
    val shipmentDate: String? = null

    @JsonProperty("Carrier")
    val carrier: String? = null

    @JsonProperty("ManifestNo")
    val manifestNo: String? = null

    @JsonProperty("BLAWB")
    val blawb: String? = null

    @JsonProperty("MarksAndNumbers")
    val marksAndNumbers: String? = null

    @JsonProperty("PortOfArrival")
    val portOfArrival: String? = null

    @JsonProperty("PortOfArrivalDesc")
    val portOfArrivalDesc: String? = null

    @JsonProperty("PortOfDeparture")
    val portOfDeparture: String? = null

    @JsonProperty("PortOfDepartureDesc")
    val portOfDepartureDesc: String? = null

    @JsonProperty("CustomsOffice")
    val customOffice: String? = null

    @JsonProperty("CustomsOfficeDesc")
    val customOfficeDesc: String? = null

    @JsonProperty("FreightStation")
    val freightStation: String? = null

    @JsonProperty("FreightStationDesc")
    val freightStationDesc: String? = null

    @JsonProperty("CargoTypeIndicator")
    val cargoTypeIndicator: String? = null

    @JsonProperty("InlandTransportCo")
    var inLandTransPortCo: String? = null

    @JsonProperty("InlandTransportCoRefNo")
    var inLandTransPortCoRefNo: String? = null

    @JsonProperty("KEPHISCollectionOffice")
    var kephisCollectionOffice: String? = null

    @JsonProperty("CertificationCategory")
    var certificationCategory: String? = null

    @JsonProperty("PlaceOfApplication")
    var placeOfApplication: String? = null

    @JsonProperty("ContainerDetails")
    var containerDetails: ContainerDetails? = null

}

class ContainerDetails {
    @NotNull
    @JsonProperty("Containers")
    @JacksonXmlElementWrapper(useWrapping = false)
    val containers: List<ContainersResponse>? = null
}

class ContainersResponse {
    @JsonProperty("ContainerNumbers")
    var containerNumbers: String? = null

    @JsonProperty("ContainerNoOfPacakges")
    var containerNoOfPackages: String? = null

    @JsonProperty("ContainerSize")
    var containerSize: String? = null

    @JsonProperty("ContainerSealNo")
    var containerSealNo: String? = null

    @JsonProperty("ContainerLoadIndicator")
    var containerLoadIndicator: String? = null
}


class CdHeaderOneResponse {
    @JsonProperty("ForeignCurrencyCode")
    val foreignCurrencyCode: String? = null

    @JsonProperty("ForexRate")
    val forexRate: String? = null

    @JsonProperty("FOBFCY")
    val fobFcy: String? = null

    @JsonProperty("FreightFCY")
    val freightFcy: String? = null

    @JsonProperty("InsuranceFCY")
    val insuranceFcy: String? = null

    @JsonProperty("OtherChargesFCY")
    val otherChargesFcy: String? = null

    @JsonProperty("CIFFCY")
    val cifFcy: String? = null

    @JsonProperty("FOBNCY")
    val fobNcy: String? = null

    @JsonProperty("FreightNCY")
    val freightNcy: String? = null

    @JsonProperty("InsuranceNCY")
    val insuranceNyc: String? = null

    @JsonProperty("OtherChargesNCY")
    val otherChargesNcy: String? = null

    @JsonProperty("CIFNCY")
    val cifNcy: String? = null

    @JsonProperty("INCOTerms")
    val incoTerms: String? = null

    @JsonProperty("TransactionTerms")
    val transactionTerms: String? = null

    @JsonProperty("COMESA")
    val comesa: String? = null

    @JsonProperty("InvoiceDate")
    val invoiceDate: String? = null

    @JsonProperty("InvoiceNumber")
    val invoiceNumber: String? = null

    @JsonProperty("CountryOfSupply")
    val countryOfSupply: String? = null
}

class CDProductDetails {
    @NotNull
    @JsonProperty("ItemDetails")
    @JacksonXmlElementWrapper(useWrapping = false)
    val itemDetails: List<ItemDetails>? = null
}


class ItemDetails {

    @NotNull
    @JsonProperty("ItemCount")
    val itemCount: String? = null

    @NotNull
    @JsonProperty("CDProduct1")
    val cdProduct1: CDProduct1Response? = null

    @NotNull
    @JsonProperty("CDProduct2")
    val cdProduct2: CDProduct2Response? = null

    @NotNull
    @JsonProperty("CDItemNonStandard")
    val cdItemNonStandard: CDItemNonStandardResponse? = null

    @NotNull
    @JsonProperty("CDItemCommodity")
    val cdItemCommodity: CDItemCommodityResponse? = null

    @NotNull
    @JsonProperty("CDItemAdditional")
    val cdItemAdditional: CDItemAdditionalResponse? = null

}

class CDItemAdditionalResponse {
//Todo: check file xsd or xml details with examples
}

class CDItemCommodityResponse {
    @JsonProperty("CommonName")
    val commonName: String? = null

    @JsonProperty("OrganismStrain")
    val organismStrain: String? = null

    @JsonProperty("TreatmentInformation")
    val treatmentInformation: String? = null

    @JsonProperty("TreatmentDate")
    val treatmentDate: String? = null

    @JsonProperty("DurationsAndTemperature")
    val durationsAndTemperature: String? = null

    @JsonProperty("ChemicalsActiveIngredients")
    val chemicalsActiveIngredients: String? = null

    @JsonProperty("ConcentrationActiveIngredients")
    val concentrationActiveIngredients: String? = null

    @JsonProperty("SeedReferenceNo")
    val seedReferenceNo: String? = null

    @JsonProperty("ProducerDetails")
    val producerDetails: String? = null
}


class CDProduct1Response {

    @JsonProperty("ItemNo")
    val itemNo: String? = null

    @JsonProperty("ItemDescription")
    val itemDescription: String? = null

    @JsonProperty("ItemHSCode")
    val itemHsCode: String? = null

    @JsonProperty("HSDescription")
    val hsDescription: String? = null

    @JsonProperty("InternalFileNumber")
    var internalFileNumber: String? = null

    @JsonProperty("InternalProductNo")
    var internalProductNo: String? = null

    @JsonProperty("ProductTechnicalName")
    var productTechnicalName: String? = null

    @JsonProperty("ProductBrandName")
    var productBrandName: String? = null

    @JsonProperty("ProductActiveIngredients")
    var productActiveIngredients: String? = null

    @JsonProperty("ProductPackagingDetails")
    var productPackagingDetails: String? = null

    @JsonProperty("ProductClassCode")
    val productClassCode: String? = null

    @JsonProperty("ProductClassDescription")
    val productClassDescription: String? = null

    @JsonProperty("PackageType")
    val packageType: String? = null

    @JsonProperty("PackageTypeDesc")
    val packageTypeDesc: String? = null

    @JsonProperty("PackageQty")
    val packageQuantity: String? = null

    @JsonProperty("ForeignCurrencyCode")
    val foreignCurrencyCode: String? = null

    @JsonProperty("UnitPriceFCY")
    val unitPriceFcy: String? = null

    @JsonProperty("TotalPriceFCY")
    val totalPriceFcy: String? = null

    @JsonProperty("UnitPriceNCY")
    val unitPriceNcy: String? = null

    @JsonProperty("CountryOfOrigin")
    val countryOfOrigin: String? = null

    @JsonProperty("CountryOfOriginDesc")
    val countryOfOriginDesc: String? = null

    @JsonProperty("TotalPriceNCY")
    val totalPriceNcy: String? = null

    @JsonProperty("ItemNetWeight")
    val itemNetWeight: String? = null

    @JsonProperty("ItemGrossWeight")
    val itemGrossWeight: String? = null

    @JsonProperty("MarksAndContainers")
    val marksAndContainers: String? = null

    @NotNull
    @JsonProperty("Quantity")
    val quantity: CDQuantityResponse? = null


    @JsonProperty("SupplementryQuantity")
    val supplementaryQuantity: SupplementaryQuantityResponse? = null

//    val qty: String? = quantity?.qty
//
//    val unitOfQuantity: String? = quantity?.unitOfQty

}

class SupplementaryQuantityResponse {
    @JsonProperty("Qty")
    var qty: String? = null

    @JsonProperty("UnitOfQty")
    var unitOfQty: String? = null

    @JsonProperty("UnitOfQtyDesc")
    var unitOfQtyDesc: String? = null
}


class CDQuantityResponse {
    @JsonProperty("Qty")
    var qty: String? = null

    @JsonProperty("UnitOfQty")
    var unitOfQty: String? = null

    @JsonProperty("UnitOfQtyDesc")
    var unitOfQtyDesc: String? = null

}


class CDProduct2Response {

    @JsonProperty("EndUserDetails")
    var endUserDetailsResponse: EndUserDetailsResponse? = null

    @JsonProperty("RiskClassification")
    var riskClassification: String? = null

    @JsonProperty("RiskDetails")
    var riskDetails: String? = null

    @JsonProperty("SafetyClassification")
    var safetyClassification: String? = null

    @JsonProperty("SafetyDetails")
    var safetyDetails: String? = null

    @JsonProperty("RiskSafetyRemarks")
    var riskSafetyRemarks: String? = null

    @JsonProperty("SamplingRequirement")
    var samplingRequirement: String? = null

    @JsonProperty("SamplingResults")
    var samplingResults: String? = null

    @JsonProperty("ApplicantRemarks")
    var applicantRemarks: String? = null

    @JsonProperty("MDARemarks")
    var mdaRemarks: String? = null

    @JsonProperty("CustomsRemarks")
    var customsRemarks: String? = null

    @JsonProperty("MDAItemApprovalFlag")
    var mdaItemApprovalFlag: String? = null
}

class EndUserDetailsResponse {
    @JsonProperty("RegNo")
    var regNo: String? = null

    @JsonProperty("Name")
    var name: String? = null

    @JsonProperty("PhysicalAddress")
    var physicalAddress: String? = null

    @JsonProperty("TelFax")
    var telFax: String? = null

    @JsonProperty("UseGeneralDescription")
    var useGeneralDescription: String? = null

    @JsonProperty("UseDetails")
    var useDetails: String? = null
}


class CDItemNonStandardResponse {

    @JsonProperty("ChassisNo")
    val chassisNo: String? = null

    @JsonProperty("usedIndicator")
    val usedIndicator: String? = null

    @JsonProperty("vehicleYear")
    val vehicleYear: String? = null

    @JsonProperty("vehicleModel")
    val vehicleModel: String? = null

    @JsonProperty("vehicleMake")
    val vehicleMake: String? = null

}


