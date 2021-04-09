package org.kebs.app.kotlin.apollo.common.dto.kesws.receive

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import javax.validation.constraints.NotNull

@JacksonXmlRootElement(localName="message")
class ManifestDocumentMessage {

    @NotNull
    @JsonProperty("message_header")
    val header: MessageHeader? = null

    @NotNull
    @JsonProperty("message_body")
    val data: MessageBody? = null
}

class MessageHeader {
    @NotNull
    @JsonProperty("module")
    var module: String? = null

    @NotNull
    @JsonProperty("action")
    var action: String? = null

    @NotNull
    @JsonProperty("sender")
    var sender: String? = null

    @NotNull
    @JsonProperty("receiver")
    var receiver: String? = null

    @NotNull
    @JsonProperty("information")
    var information: String? = null

    @NotNull
    @JsonProperty("message_id")
    var messageId: String? = null

    @NotNull
    @JsonProperty("message_version")
    var messageVersion: Long? = null

    @NotNull
    @JsonProperty("message_date")
    var messageDate: String? = null
}

class MessageBody {
    @NotNull
    @JsonProperty("sd")
    val sd: SD? = null
}

class SD {
    @JsonProperty("sd_reg_no")
    var sdRegNo: String? = null

    @JsonProperty("cus_office_country")
    var cusOfficeCountry: String? = null

    @JsonProperty("exit_office_sub2")
    var exitOfficeSub2: String? = null

    @JsonProperty("exit_office_sub3")
    var exitOfficeSub3: String? = null

    @JsonProperty("carrieragent")
    val carrierAgent: CarrierAgent? = null

    @JsonProperty("trans_mode")
    var transMode: String? = null

    @JsonProperty("trade_movement")
    var tradeMovement: String? = null

    @JsonProperty("exp_date")
    var expDate: String? = null

    @JsonProperty("dispatch_ctr")
    var dispatchCtr: String? = null

    @JsonProperty("unload_place")
    var unloadPlace: String? = null

    @JsonProperty("trans_nationality")
    var transNationality: String? = null

    @JsonProperty("trans_id")
    var transId: String? = null

    @JsonProperty("voyage_no")
    var voyageNo: String? = null

    @JsonProperty("booking_no")
    var bookingNo: String? = null

    @JsonProperty("nil_cargo")
    var nilCargo: String? = null

    @JsonProperty("onboard")
    var onboard: String? = null

    @JsonProperty("remark")
    var remark: String? = null

    @JsonProperty("Consolidated_TD")
    val consolidatedTd: ConsolidatedTd? = null

    @JsonProperty("td")
    val td: TD? = null
}

class CarrierAgent {
    @JsonProperty("registered")
    var registered: String? = null

    @JsonProperty("carrierpin")
    var carrierPin: String? = null

    @JsonProperty("businessnation")
    var businessNation: String? = null

    @JsonProperty("businessname")
    var businessName: String? = null

    @JsonProperty("businessaddr")
    var businessAddr: String? = null
}

class ConsolidatedTd {
    @JsonProperty("ref_sd_reg_no")
    var refSdRegNo: String? = null

    @JsonProperty("ref_td_prefix")
    var refTdPrefix: String? = null

    @JsonProperty("ref_td_bill_code")
    var refTdBillCode: String? = null
}

class TD {
    @JsonProperty("td_prefix")
    var tdPrefix: String? = null

    @JsonProperty("td_bill_code")
    var tdBillCode: String? = null

    @JsonProperty("consignment_no")
    var consignmentNo: String? = null

    @JsonProperty("ucrn")
    var ucrn: String? = null

    @JsonProperty("consignee")
    var consignee: ManConsignee? = null

    @JsonProperty("consignor")
    var consignor: ManConsignor? = null

    @JsonProperty("notify_party")
    var notifyParty: String? = null

    @JsonProperty("notify_name")
    var notifyName: String? = null

    @JsonProperty("freight_cost")
    var freightCost: String? = null

    @JsonProperty("insurance_costs")
    var insuranceCosts: String? = null

    @JsonProperty("first_port")
    var firstPort: String? = null

    @JsonProperty("first_port_date")
    var firstPortDate: String? = null

    @JsonProperty("transport_equ_id")
    var transportEquId: String? = null

    @JsonProperty("routing")
    var routing: String? = null

    @JsonProperty("final_dest_loc_city")
    var finalDestLocCity: String? = null

    @JsonProperty("pay_method")
    var payMethod: String? = null

    @JsonProperty("currency_code")
    var currencyCode: String? = null

    @JsonProperty("total_invoice_amount")
    var totalInvoiceAmount: String? = null

    @JsonProperty("sub_type")
    var subType: String? = null

    @JsonProperty("low_value")
    var lowValue: String? = null

    @JsonProperty("remarks")
    var remarks: String? = null

    @JsonProperty("tstatus")
    var tStatus: String? = null

    @JsonProperty("cons_flag")
    var consFlag: String? = null

    @JsonProperty("tranship_flag")
    var transhipFlag: String? = null

    @JsonProperty("load_place")
    var loadPlace: String? = null

    @JsonProperty("load_date")
    var loadDate: String? = null

    @JsonProperty("place_of_delivery")
    var placeOfDelivery: String? = null

    @JsonProperty("direct_delivery_indicator")
    var directDeliveryIndicator: String? = null

    @JsonProperty("cargo_type")
    var cargoType: String? = null

    @JsonProperty("goods_desc")
    var goodsDesc: String? = null

    @JsonProperty("nominated_temp_storage")
    var nominatedTempStorage: String? = null

    @JsonProperty("lp")
    var lp: LP? = null
}

class ManConsignee {
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

    @JsonProperty("phone_no")
    var phoneNo: String? = null
}

class ManConsignor {
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

    @JsonProperty("phone_no")
    var phoneNo: String? = null
}

class LP {
    @JsonProperty("lp_type")
    var lpType: String? = null

    @JsonProperty("lp_no")
    var lpNo: String? = null

    @JsonProperty("decl_qty")
    var declQty: String? = null

    @JsonProperty("decl_gross_wgt")
    var declGrossWgt: String? = null

    @JsonProperty("lp_marks")
    var lpMarks: String? = null

    @JsonProperty("net_weight")
    var netWeight: String? = null

    @JsonProperty("volume_unit")
    var volumeUnit: String? = null

    @JsonProperty("volume")
    var volume: String? = null

    @JsonProperty("goods_origin_country")
    var goodsOriginCountry: String? = null

    @JsonProperty("description")
    var description: String? = null

    @JsonProperty("commodity")
    var commodity: String? = null

    @JsonProperty("UNDangerousGoods")
    var unDangerousGoods: String? = null

    @JsonProperty("remarks")
    var remarks: String? = null

    @JsonProperty("temperature")
    var temperature: String? = null

    @JsonProperty("container_ref")
    var containerRef: String? = null

    @JsonProperty("engine_num")
    var engineNum: String? = null

    @JsonProperty("chassis_num")
    var chassisNum: String? = null
}


