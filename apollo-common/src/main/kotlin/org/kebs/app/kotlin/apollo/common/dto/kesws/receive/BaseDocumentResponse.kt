package org.kebs.app.kotlin.apollo.common.dto.kesws.receive

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import javax.validation.constraints.NotNull

@JacksonXmlRootElement(localName="message")
class BaseDocumentResponse {
    @NotNull
    @JsonProperty("data")
    val data: Data? = null

    @NotNull
    @JsonProperty("header")
    val header: Header? = null
}

class Header {
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

class Data {
    @NotNull
    @JsonProperty("data_in")
    val dataIn: DataIn? = null
}

class DataIn {
    @NotNull
    @JsonProperty("SAD")
    val sad: SAD? = null
}

class SAD {
    @JsonProperty("message_nature")
    var messageNature: Long? = null

    @JsonProperty("sad_id")
    var sadId: String? = null

    @JsonProperty("version")
    var version: String? = null

    @JsonProperty("CD")
    val cd: CD? = null

//    @JsonProperty("DS")
    @JacksonXmlElementWrapper(useWrapping = false)
    val dsList: List<DS>? = null

    @JacksonXmlElementWrapper(useWrapping = false)
    val csList: List<CS>? = null

    @JsonProperty("Items")
    val items: Items ? = null
}

class CD {
    @JsonProperty("b27_place_of_loading")
    var b27PlaceOfLoading: String? = null

    @JsonProperty("b14_declarant_tr")
    var b14DeclarantTr: String? = null

    @JsonProperty("b14_declarant_aeo_flag")
    var b14DeclarantAeoFlag: String? = null

    @JsonProperty("b1_decla_sub1")
    var b1DeclaSub1: String? = null

    @JsonProperty("total_customs_value")
    var totalCustomsValue: String? = null

    @JsonProperty("b20_deliv_terms_sub2")
    var b20DelivTermsSub2: String? = null

    @JsonProperty("total_other_charges")
    var totalOtherCharges: String? = null

    @JsonProperty("fs_t18_entry_date")
    var fsT18EntryDate: String? = null

    @JsonProperty("b20_deliv_terms_sub1")
    var b20DelivTermsSub1: String? = null

    @JsonProperty("company_tr")
    var companyTr: String? = null

    @JsonProperty("b22_total_amount")
    var b22TotalAmount: String? = null

    @JsonProperty("boxa_office_sub_code")
    var boxaOfficeSubCode: String? = null

    @JsonProperty("b7_ref_num")
    var b7RefNum: String? = null

    @JsonProperty("b22_currency_code")
    var b22CurrencyCode: String? = null

    @JsonProperty("boxa_office_code")
    var boxaOfficeCode: String? = null

    @JsonProperty("CD_registration_date_time")
    var cdRegistrationDateTime: String? = null

    @JsonProperty("b9_total_insurance")
    var b9TotalInsurance: String? = null

    @JsonProperty("trans_type")
    var transType: Long? = null

    @NotNull
    @JsonProperty("consignee")
    val consignee: Consignee? = null

    @NotNull
    @JsonProperty("consignor")
    val consigner: Consignor ? = null
}

class Consignee {
    @JsonProperty("trnumber")
    var trNumber: String? = null

    @JsonProperty("country_code")
    var countryCode: String? = null

    @JsonProperty("businessname")
    var businessName: String? = null

    @JsonProperty("aeo_flag")
    var aeoFlag: String? = null

    @JsonProperty("businessaddr")
    var businessAddr: String? = null

    @JsonProperty("businessnation")
    var businessNation: String? = null
}

class Consignor {
    @JsonProperty("trnumber")
    var trNumber: String? = null

    @JsonProperty("country_code")
    var countryCode: String? = null

    @JsonProperty("businessname")
    var businessName: String? = null

    @JsonProperty("aeo_flag")
    var aeoFlag: String? = null

    @JsonProperty("businessaddr")
    var businessAddr: String? = null

    @JsonProperty("businessnation")
    var businessNation: String? = null
}

class Items {
    @JsonProperty("IS")
    @JacksonXmlElementWrapper(useWrapping = false)
    var isList: List<IS>? = null
}

class IS {
    @JsonProperty("category_type_pro")
    var categoryTypePro: String? = null

    @JsonProperty("b37_previous_pro")
    var b37PreviousPro: String? = null

    @JsonProperty("tariff_goods_desc")
    var tariffGoodsDesc: String? = null

    @JsonProperty("b42_item_price")
    var b42ItemPrice: String? = null

    @JsonProperty("item_num")
    var itemNum: Long? = null

    @JsonProperty("subcategory_pro")
    var subcategoryPro: String? = null

    @JsonProperty("b38_net_mass")
    var b38NetMass: Long? = null

    @JsonProperty("b31_commercial_goods")
    var b31CommercialGoods: String? = null

    @JsonProperty("item_freight_value")
    var itemFreightValue: Double? = null

    @JsonProperty("b33_commodity_code")
    var b33CommodityCode: String? = null

    @JsonProperty("item_insurance_value")
    var itemInsuranceValue: String? = null

    @JsonProperty("customs_value")
    var customsValue: String? = null

    @JsonProperty("b34_origin_country")
    var b34OriginCountry: String? = null

    @JsonProperty("b41_unit_num")
    var b41UnitNum: Long? = null

    @JsonProperty("b37_requested_pro")
    var b37RequestedPro: String? = null

    @JsonProperty("item_other_charges")
    var itemOtherCharges: String? = null

    @JsonProperty("unit_code")
    var unitCode: String? = null

    @JsonProperty("b45_item_fob_val")
    var b45ItemFobVal: String? = null

    @JsonProperty("DS")
    @JacksonXmlElementWrapper(useWrapping = false)
    var dsList: List<DS>? = null

    @JsonProperty("CS")
    @JacksonXmlElementWrapper(useWrapping = false)
    var csList: List<CS>? = null

    @JsonProperty("OGA_identifications")
    @JacksonXmlElementWrapper(useWrapping = false)
    var ogaidentificationsList: List<OGAIdentifications>? = null
}

class OGAIdentifications {
    @JsonProperty("OGA_identification")
    var ogaIdentification: String? = null
}

class DS {
    @JsonProperty("certificate_reference")
    var certificateReference: String? = null

    @JsonProperty("certificate_type")
    var certificateType: String? = null
}

class CS {
    @JsonProperty("element_value")
    var elementValue: String? = null

    @JsonProperty("element_name")
    var elementName: String? = null
}


