package org.kebs.app.kotlin.apollo.common.dto.kesws.receive

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

import javax.validation.constraints.NotNull

@JacksonXmlRootElement(localName="message")
class DeclarationDocumentMessage {

    @NotNull
    @JsonProperty("header")
    val header: DCLHeader? = null
    
    @NotNull
    @JsonProperty("data")
    val data: DCLData? = null
}

class DCLHeader {
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

class DCLData {
    @NotNull
    @JsonProperty("data_in")
    val dataIn: DCLDataIn? = null
}

class DCLDataIn {
    @NotNull
    @JsonProperty("SAD")
    val sad: DCLSAD? = null
}

class DCLSAD {
    @JsonProperty("message_nature")
    var messageNature: Long? = null

    @JsonProperty("sad_id")
    var sadId: String? = null

    @JsonProperty("version")
    var version: String? = null

    @JsonProperty("CD")
    val cd: DCLCD? = null

    @JsonProperty("Items")
    val items: DCLItems ? = null

    @JsonProperty("TS")
    val ts: DCLTS? = null
}

class DCLCD {
    @JsonProperty("boxa_office_code")
    var officeCode: String? = null

    @JsonProperty("boxa_office_sub_code")
    var officeSubCode: String? = null

    @JsonProperty("b1_decla_sub1")
    var declarantRegime: String? = null

    @NotNull
    @JsonProperty("consignee")
    val consignee: DCLConsignee? = null

    @NotNull
    @JsonProperty("consignor")
    val consigner: DCLConsignor ? = null

    @JsonProperty("b7_ref_num")
    var refNum: String? = null

    @JsonProperty("b9_total_insurance")
    var totalInsurance: String? = null

    @JsonProperty("total_other_charges")
    var totalOtherCharges: String? = null

    @JsonProperty("total_customs_value")
    var totalCustomsValue: String? = null

    @JsonProperty("valuation_method")
    var valuationMethod: String? = null

    @JsonProperty("b14_declarant_tr")
    var declarantPin: String? = null

    @JsonProperty("b14_declarant_aeo_flag")
    var declarantAeoFlag: String? = null

    @JsonProperty("company_tr")
    var companyPin: String? = null

    @JsonProperty("last_consignment_country")
    var lastConsignmentCountry: String? = null

    @JsonProperty("fs_t18_entry_date")
    var entryDate: String? = null

    @JsonProperty("b17_region_of_destination_code")
    var regionOfDestinationCode: String? = null

    @JsonProperty("iden_mean_trans_dep_arr")
    var idenMeanTransDepArr: String? = null

    @JsonProperty("Nationality_Trans_dep_arr")
    var nationalityTransportDepArr: String? = null

    @JsonProperty("b20_deliv_terms_sub1")
    var delivTermsSub1: String? = null

    @JsonProperty("b20_deliv_terms_sub2")
    var delivTermsSub2: String? = null

    @JsonProperty("b22_currency_code")
    var currencyCode: String? = null

    @JsonProperty("b22_total_amount")
    var totalAmount: String? = null

    @JsonProperty("exchange_tate")
    var exchangeRate: String? = null

    @JsonProperty("trans_type")
    var transportType: String? = null

    @JsonProperty("b26_inland_trans")
    var inlandTransport: String? = null

    @JsonProperty("b27_place_of_loading")
    var placeOfLoading: String? = null

    @JsonProperty("terms_payment")
    var termsPayment: String? = null

    @JsonProperty("bank_account")
    var bankAccount : String? = null

    @JsonProperty("bank_brannch_id")
    var bankBranchId : String? = null

    @JsonProperty("bond_num")
    var bondNumber : String? = null

    @JsonProperty("bond_amount")
    var bondAmount : String? = null

    @JsonProperty("sub_office2")
    var subOffice2 : String? = null

    @JsonProperty("sub_office3")
    var subOffice3 : String? = null

    @JsonProperty("ide_wh_type")
    var whType : String? = null

    @JsonProperty("ide_eh")
    var eh : String? = null

    @JsonProperty("country_transit")
    var countryTransit : String? = null

    @JsonProperty("CD_registration_date_time")
    var cdRegistrationDateTime: String? = null

    @JsonProperty("seals_num")
    var sealsNum : String? = null

    @JsonProperty("exemption_year")
    var exemptionYear : String? = null

    @JsonProperty("invoice_date")
    var invoiceDate : String? = null

    @JsonProperty("cd_pay_amount")
    var cdPayAmount : String? = null

    @JsonProperty("cd_guaranteed_amount")
    var cdGuaranteedAmount : String? = null
}

class DCLConsignee {
    @JsonProperty("registered")
    var registered: String? = null

    @JsonProperty("trnumber")
    var pinNumber: String? = null

    @JsonProperty("businessnation")
    var businessNation: String? = null

    @JsonProperty("businessname")
    var businessName: String? = null

    @JsonProperty("businessaddr")
    var businessAddress: String? = null

    @JsonProperty("country_code")
    var countryCode: String? = null

    @JsonProperty("aeo_flag")
    var aeoFlag: String? = null
}

class DCLConsignor {
    @JsonProperty("registered")
    var registered: String? = null

    @JsonProperty("trnumber")
    var pinNumber: String? = null

    @JsonProperty("businessnation")
    var businessNation: String? = null

    @JsonProperty("businessname")
    var businessName: String? = null

    @JsonProperty("businessaddr")
    var businessAddress: String? = null

    @JsonProperty("country_code")
    var countryCode: String? = null

    @JsonProperty("aeo_flag")
    var aeoFlag: String? = null
}

class DCLTS {
    @JsonProperty("manifest_no")
    var manifestNo: String? = null

    @JsonProperty("prefix")
    var prefix: String? = null

    @JsonProperty("bill_code")
    var billCode: String? = null
}

class DCLItems {
    @JsonProperty("IS")
    @JacksonXmlElementWrapper(useWrapping = false)
    var isList: List<DCLIS>? = null
}

class DCLIS {
    @JsonProperty("item_num")
    var itemNum: Int? = null

    @JsonProperty("item_packages")
    var itemPackages: String? = null

    @JsonProperty("b33_commodity_code")
    var commodityCode: String? = null

    @JsonProperty("Additional_code_1")
    var additionalCode1: String? = null

    @JsonProperty("Additional_code_2")
    var additionalCode2: String? = null

    @JsonProperty("Additional_code_3")
    var additionalCode3: String? = null

    @JsonProperty("tariff_goods_desc")
    var tariffGoodsDesc: String? = null

    @JsonProperty("b31_commercial_goods")
    var commercialGoods: String? = null

    @JsonProperty("b34_origin_country")
    var originCountry: String? = null

    @JsonProperty("b35_gross_mass")
    var grossMass: String? = null

    @JsonProperty("preference_num")
    var preferenceNum: String? = null

    @JsonProperty("b37_requested_pro")
    var requestedPro: String? = null

    @JsonProperty("b37_previous_pro")
    var previousPro: String? = null

    @JsonProperty("category_type_pro")
    var categoryTypePro: String? = null

    @JsonProperty("subcategory_pro")
    var subcategoryPro: String? = null

    @JsonProperty("b38_net_mass")
    var netMass: String? = null

    @JsonProperty("quota")
    var quota: String? = null

    @JsonProperty("pre_doc_type")
    var preDocType: String? = null

    @JsonProperty("pre_doc_abb")
    var preDocAbb: String? = null

    @JsonProperty("pre_doc_first_sub_ide")
    var preDocFirstSubIde: String? = null

    @JsonProperty("b41_unit_num")
    var unitNum: String? = null

    @JsonProperty("unit_code")
    var unitCode: String? = null

    @JsonProperty("supplementary_units")
    var supplementaryUnits: String? = null

    @JsonProperty("supplementary_units2")
    var supplementaryUnits2: String? = null

    @JsonProperty("b42_item_price")
    var itemPrice: String? = null

    @JsonProperty("b46_stat_value")
    var statValue: String? = null

    @JsonProperty("item_freight_value")
    var itemFreightValue: String? = null

    @JsonProperty("item_insurance_value")
    var itemInsuranceValue: String? = null

    @JsonProperty("item_other_charges")
    var itemOtherCharges: String? = null

    @JsonProperty("customs_value")
    var customsValue: String? = null

    @JsonProperty("chassis_number")
    var chassisNumber: String? = null

    @JsonProperty("engin_number")
    var engineNumber: String? = null

    @JsonProperty("CS")
    @JacksonXmlElementWrapper(useWrapping = false)
    var csList: List<DCLCS>? = null

    @JsonProperty("PS")
    @JacksonXmlElementWrapper(useWrapping = false)
    var psList: List<DCLPS>? = null
}

class DCLCS {
    @JsonProperty("element_value")
    var elementValue: String? = null

    @JsonProperty("element_name")
    var elementName: String? = null
}

class DCLPS {
    @JsonProperty("marks_numbers")
    var marksNumbers: String? = null
}
