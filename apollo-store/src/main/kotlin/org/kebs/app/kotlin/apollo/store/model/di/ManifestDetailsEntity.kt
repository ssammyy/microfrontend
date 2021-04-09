package org.kebs.app.kotlin.apollo.store.model.di

import org.kebs.app.kotlin.apollo.store.model.di.ManifestDetailsEntity
import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MANIFEST_DETAILS")
class ManifestDetailsEntity : Serializable {

    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MANIFEST_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_MANIFEST_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_MANIFEST_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "MESSAGE_DATE", nullable = true)
    @Basic
    var messageDate: Timestamp? = null

    @Column(name = "ACTION", nullable = true, precision = 0)
    @Basic
    var action: String? = null

    @Column(name = "SENDER", nullable = true, length = 200)
    @Basic
    var sender: String? = null

    @Column(name = "RECEIVER", nullable = true, length = 200)
    @Basic
    var receiver: String? = null

    @Column(name = "INFORMATION", nullable = true, length = 200)
    @Basic
    var information: String? = null

    @Column(name = "MESSAGE_ID", nullable = true, length = 200)
    @Basic
    var messageId: String? = null

    @Column(name = "MESSAGE_VERSION", nullable = true, precision = 0)
    @Basic
    var messageVersion: Long? = null

    @Column(name = "MANIFEST_NUMBER", nullable = true, length = 200)
    @Basic
    var manifestNumber: String? = null

    @Column(name = "CUSTOMS_OFFICE_COUNTRY", nullable = true, length = 200)
    @Basic
    var customsOfficeCountry: String? = null

    @Column(name = "EXIT_OFFICE_SUB2", nullable = true, length = 200)
    @Basic
    var exitOfficeSub2: String? = null

    @Column(name = "EXIT_OFFICE_SUB3", nullable = true, length = 200)
    @Basic
    var exitOfficeSub3: String? = null

    @Column(name = "CARRIER_AGENT_REGISTERED", nullable = true, length = 200)
    @Basic
    var carrierAgentRegistered: String? = null

    @Column(name = "CARRIER_AGENT_PIN", nullable = true, length = 200)
    @Basic
    var carrierAgentPin: String? = null

    @Column(name = "CARRIER_AGENT_BUSINESS_NATION", nullable = true, length = 200)
    @Basic
    var carrierAgentBusinessNation: String? = null

    @Column(name = "CARRIER_AGENT_BUSINESS_NAME", nullable = true, length = 200)
    @Basic
    var carrierAgentBusinessName: String? = null

    @Column(name = "CARRIER_AGENT_BUSINESS_ADDRESS", nullable = true, length = 200)
    @Basic
    var carrierAgentBusinessAddress: String? = null

    @Column(name = "TRANSPORT_MODE", nullable = true, length = 200)
    @Basic
    var transportMode: String? = null

    @Column(name = "TRADE_MOVEMENT", nullable = true, length = 200)
    @Basic
    var tradeMovement: String? = null

    @Column(name = "EXPECTED_DATE", nullable = true)
    @Basic
    var expectedDate: Timestamp? = null

    @Column(name = "DISPATCH_COUNTRY", nullable = true, length = 200)
    @Basic
    var dispatchCountry: String? = null

    @Column(name = "DISCHARGE_PLACE", nullable = true, length = 200)
    @Basic
    var dischargePlace: String? = null

    @Column(name = "TRANSPORT_NATIONALITY", nullable = true, length = 200)
    @Basic
    var transportNationality: String? = null

    @Column(name = "TRANSPORT_ID", nullable = true, length = 200)
    @Basic
    var transportId: String? = null

    @Column(name = "VOYAGE_NUMBER", nullable = true, length = 200)
    @Basic
    var voyageNumber: String? = null

    @Column(name = "BOOKING_NUMBER", nullable = true, length = 200)
    @Basic
    var bookingNumber: String? = null

    @Column(name = "NIL_CARGO", nullable = true, length = 20)
    @Basic
    var nilCargo: String? = null

    @Column(name = "ONBOARD", nullable = true, length = 20)
    @Basic
    var onboard: String? = null

    @Column(name = "REMARK", nullable = true, length = 500)
    @Basic
    var remark: String? = null

    @Column(name = "REF_SD_REG_NUMBER", nullable = true, length = 200)
    @Basic
    var refSdRegNumber: String? = null

    @Column(name = "REF_TD_PREFIX", nullable = true, length = 200)
    @Basic
    var refTdPrefix: String? = null

    @Column(name = "REF_TD_BILL_CODE", nullable = true, length = 200)
    @Basic
    var refTdBillCode: String? = null

    @Column(name = "TD_PREFIX", nullable = true, length = 200)
    @Basic
    var tdPrefix: String? = null

    @Column(name = "TD_BILL_CODE", nullable = true, length = 200)
    @Basic
    var tdBillCode: String? = null

    @Column(name = "CONSIGNMENT_NUMBER", nullable = true, length = 200)
    @Basic
    var consignmentNumber: String? = null

    @Column(name = "UCR_NUMBER", nullable = true, length = 200)
    @Basic
    var ucrn: String? = null

    @Column(name = "CONSIGNEE_REGISTERED", nullable = true, length = 20)
    @Basic
    var consigneeRegistered: String? = null

    @Column(name = "CONSIGNEE_PIN_NUMBER", nullable = true, length = 20)
    @Basic
    var consigneePinNumber: String? = null

    @Column(name = "CONSIGNEE_COUNTRY_CODE", nullable = true, length = 10)
    @Basic
    var consigneeCountryCode: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_NAME", nullable = true, length = 200)
    @Basic
    var consigneeBusinessName: String? = null

    @Column(name = "CONSIGNEE_PHONE_NUMBER", nullable = true, length = 200)
    @Basic
    var consigneePhoneNumber: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_ADDRESS", nullable = true, length = 255)
    @Basic
    var consigneeBusinessAddress: String? = null

    @Column(name = "CONSIGNEE_BUSINESS_NATION", nullable = true, length = 10)
    @Basic
    var consigneeBusinessNation: String? = null

    @Column(name = "CONSIGNOR_REGISTERED", nullable = true, length = 2)
    @Basic
    var consignorRegistered: String? = null

    @Column(name = "CONSIGNOR_PIN_NUMBER", nullable = true, length = 20)
    @Basic
    var consignorPinNumber: String? = null

    @Column(name = "CONSIGNOR_COUNTRY_CODE", nullable = true, length = 10)
    @Basic
    var consignorCountryCode: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_NAME", nullable = true, length = 200)
    @Basic
    var consignorBusinessName: String? = null

    @Column(name = "CONSIGNOR_PHONE_NUMBER", nullable = true, length = 50)
    @Basic
    var consignorPhoneNumber: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_ADDRESS", nullable = true, length = 255)
    @Basic
    var consignorBusinessAddress: String? = null

    @Column(name = "CONSIGNOR_BUSINESS_NATION", nullable = true, length = 10)
    @Basic
    var consignorBusinessNation: String? = null

    @Column(name = "NOTIFY_PARTY", nullable = true, length = 200)
    @Basic
    var notifyParty: String? = null

    @Column(name = "NOTIFY_NAME", nullable = true, length = 200)
    @Basic
    var notifyName: String? = null

    @Column(name = "FREIGHT_COST", nullable = true, length = 200)
    @Basic
    var freightCost: String? = null

    @Column(name = "INSURANCE_COSTS", nullable = true, length = 200)
    @Basic
    var insuranceCosts: String? = null

    @Column(name = "FIRST_PORT", nullable = true, length = 200)
    @Basic
    var firstPort: String? = null

    @Column(name = "FIRST_PORT_DATE", nullable = true)
    @Basic
    var firstPortDate: Timestamp? = null

    @Column(name = "TRANSPORT_EQU_ID", nullable = true, length = 200)
    @Basic
    var transportEquId: String? = null

    @Column(name = "ROUTING", nullable = true, length = 200)
    @Basic
    var routing: String? = null

    @Column(name = "FINAL_DEST_LOC_CITY", nullable = true, length = 200)
    @Basic
    var finalDestLocCity: String? = null

    @Column(name = "PAY_METHOD", nullable = true, length = 200)
    @Basic
    var payMethod: String? = null

    @Column(name = "CURRENCY_CODE", nullable = true, length = 200)
    @Basic
    var currencyCode: String? = null

    @Column(name = "TOTAL_INVOICE_AMOUNT", nullable = true, length = 200)
    @Basic
    var totalInvoiceAmount: String? = null

    @Column(name = "SUB_TYPE", nullable = true, length = 200)
    @Basic
    var subType: String? = null

    @Column(name = "LOW_VALUE", nullable = true, length = 200)
    @Basic
    var lowValue: String? = null

    @Column(name = "TSTATUS", nullable = true, length = 200)
    @Basic
    var tstatus: String? = null

    @Column(name = "CONS_FLAG", nullable = true, length = 200)
    @Basic
    var consFlag: String? = null

    @Column(name = "TRANSHIP_FLAG", nullable = true, length = 200)
    @Basic
    var transhipFlag: String? = null

    @Column(name = "PLACE_OF_LOADING", nullable = true, length = 200)
    @Basic
    var placeOfLoading: String? = null

    @Column(name = "DATE_OF_LOADING", nullable = true)
    @Basic
    var dateOfLoading: Timestamp? = null

    @Column(name = "PLACE_OF_DELIVERY", nullable = true, length = 200)
    @Basic
    var placeOfDelivery: String? = null

    @Column(name = "DIRECT_DELIVERY_INDICATOR", nullable = true, length = 200)
    @Basic
    var directDeliveryIndicator: String? = null

    @Column(name = "CARGO_TYPE", nullable = true, length = 200)
    @Basic
    var cargoType: String? = null

    @Column(name = "GOODS_DESC", nullable = true, length = 500)
    @Basic
    var goodsDesc: String? = null

    @Column(name = "NOMINATED_TEMP_STORAGE", nullable = true, length = 200)
    @Basic
    var nominatedTempStorage: String? = null

    @Column(name = "LP_TYPE", nullable = true, length = 200)
    @Basic
    var lpType: String? = null

    @Column(name = "LP_NUMBER", nullable = true, length = 200)
    @Basic
    var lpNumber: String? = null

    @Column(name = "DECLARED_QTY", nullable = true, length = 200)
    @Basic
    var declaredQty: String? = null

    @Column(name = "LP_MARKS", nullable = true, length = 200)
    @Basic
    var lpMarks: String? = null

    @Column(name = "DECLARED_GROSS_WEIGHT", nullable = true, length = 200)
    @Basic
    var declaredGrossWeight: String? = null

    @Column(name = "NET_WEIGHT", nullable = true, length = 200)
    @Basic
    var netWeight: String? = null

    @Column(name = "VOLUME_UNIT", nullable = true, length = 200)
    @Basic
    var volumeUnit: String? = null

    @Column(name = "VOLUME", nullable = true, length = 200)
    @Basic
    var volume: String? = null

    @Column(name = "GOODS_ORIGIN_COUNTRY", nullable = true, length = 200)
    @Basic
    var goodsOriginCountry: String? = null

    @Column(name = "GOODS_DESCRIPTION", nullable = true, length = 200)
    @Basic
    var goodsDescription: String? = null

    @Column(name = "COMMODITY", nullable = true, length = 200)
    @Basic
    var commodity: String? = null

    @Column(name = "UN_DANGEROUS_GOODS", nullable = true, length = 200)
    @Basic
    var unDangerousGoods: String? = null

    @Column(name = "LP_REMARKS", nullable = true, length = 200)
    @Basic
    var lpRemarks: String? = null

    @Column(name = "TEMPERATURE", nullable = true, length = 200)
    @Basic
    var temperature: String? = null

    @Column(name = "CONTAINER_REF", nullable = true, length = 200)
    @Basic
    var containerRef: String? = null

    @Column(name = "ENGINE_NUMBER", nullable = true, length = 200)
    @Basic
    var engineNumber: String? = null

    @Column(name = "CHASSIS_NUMBER", nullable = true, length = 200)
    @Basic
    var chassisNumber: String? = null

    @Column(name = "DESCRIPTION", nullable = true, length = 200)
    @Basic
    var description: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ManifestDetailsEntity
        return id == that.id && messageDate == that.messageDate && action == that.action && sender == that.sender && receiver == that.receiver && information == that.information && messageId == that.messageId && messageVersion == that.messageVersion && manifestNumber == that.manifestNumber && customsOfficeCountry == that.customsOfficeCountry && exitOfficeSub2 == that.exitOfficeSub2 && exitOfficeSub3 == that.exitOfficeSub3 && carrierAgentRegistered == that.carrierAgentRegistered && carrierAgentPin == that.carrierAgentPin && carrierAgentBusinessNation == that.carrierAgentBusinessNation && carrierAgentBusinessName == that.carrierAgentBusinessName && carrierAgentBusinessAddress == that.carrierAgentBusinessAddress && transportMode == that.transportMode && tradeMovement == that.tradeMovement && expectedDate == that.expectedDate && dispatchCountry == that.dispatchCountry && dischargePlace == that.dischargePlace && transportNationality == that.transportNationality && transportId == that.transportId && voyageNumber == that.voyageNumber && bookingNumber == that.bookingNumber && nilCargo == that.nilCargo && onboard == that.onboard && remark == that.remark && refSdRegNumber == that.refSdRegNumber && refTdPrefix == that.refTdPrefix && refTdBillCode == that.refTdBillCode && tdPrefix == that.tdPrefix && tdBillCode == that.tdBillCode && consignmentNumber == that.consignmentNumber && ucrn == that.ucrn && consigneeRegistered == that.consigneeRegistered && consigneePinNumber == that.consigneePinNumber && consigneeCountryCode == that.consigneeCountryCode && consigneeBusinessName == that.consigneeBusinessName && consigneePhoneNumber == that.consigneePhoneNumber && consigneeBusinessAddress == that.consigneeBusinessAddress && consigneeBusinessNation == that.consigneeBusinessNation && consignorRegistered == that.consignorRegistered && consignorPinNumber == that.consignorPinNumber && consignorCountryCode == that.consignorCountryCode && consignorBusinessName == that.consignorBusinessName && consignorPhoneNumber == that.consignorPhoneNumber && consignorBusinessAddress == that.consignorBusinessAddress && consignorBusinessNation == that.consignorBusinessNation && notifyParty == that.notifyParty && notifyName == that.notifyName && freightCost == that.freightCost && insuranceCosts == that.insuranceCosts && firstPort == that.firstPort && firstPortDate == that.firstPortDate && transportEquId == that.transportEquId && routing == that.routing && finalDestLocCity == that.finalDestLocCity && payMethod == that.payMethod && currencyCode == that.currencyCode && totalInvoiceAmount == that.totalInvoiceAmount && subType == that.subType && lowValue == that.lowValue && tstatus == that.tstatus && consFlag == that.consFlag && transhipFlag == that.transhipFlag && placeOfLoading == that.placeOfLoading && dateOfLoading == that.dateOfLoading && placeOfDelivery == that.placeOfDelivery && directDeliveryIndicator == that.directDeliveryIndicator && cargoType == that.cargoType && goodsDesc == that.goodsDesc && nominatedTempStorage == that.nominatedTempStorage && lpType == that.lpType && lpNumber == that.lpNumber && declaredQty == that.declaredQty && lpMarks == that.lpMarks && declaredGrossWeight == that.declaredGrossWeight && netWeight == that.netWeight && volumeUnit == that.volumeUnit && volume == that.volume && goodsOriginCountry == that.goodsOriginCountry && goodsDescription == that.goodsDescription && commodity == that.commodity && unDangerousGoods == that.unDangerousGoods && lpRemarks == that.lpRemarks && temperature == that.temperature && containerRef == that.containerRef && engineNumber == that.engineNumber && chassisNumber == that.chassisNumber && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            messageDate,
            action,
            sender,
            receiver,
            information,
            messageId,
            messageVersion,
            manifestNumber,
            customsOfficeCountry,
            exitOfficeSub2,
            exitOfficeSub3,
            carrierAgentRegistered,
            carrierAgentPin,
            carrierAgentBusinessNation,
            carrierAgentBusinessName,
            carrierAgentBusinessAddress,
            transportMode,
            tradeMovement,
            expectedDate,
            dispatchCountry,
            dischargePlace,
            transportNationality,
            transportId,
            voyageNumber,
            bookingNumber,
            nilCargo,
            onboard,
            remark,
            refSdRegNumber,
            refTdPrefix,
            refTdBillCode,
            tdPrefix,
            tdBillCode,
            consignmentNumber,
            ucrn,
            consigneeRegistered,
            consigneePinNumber,
            consigneeCountryCode,
            consigneeBusinessName,
            consigneePhoneNumber,
            consigneeBusinessAddress,
            consigneeBusinessNation,
            consignorRegistered,
            consignorPinNumber,
            consignorCountryCode,
            consignorBusinessName,
            consignorPhoneNumber,
            consignorBusinessAddress,
            consignorBusinessNation,
            notifyParty,
            notifyName,
            freightCost,
            insuranceCosts,
            firstPort,
            firstPortDate,
            transportEquId,
            routing,
            finalDestLocCity,
            payMethod,
            currencyCode,
            totalInvoiceAmount,
            subType,
            lowValue,
            tstatus,
            consFlag,
            transhipFlag,
            placeOfLoading,
            dateOfLoading,
            placeOfDelivery,
            directDeliveryIndicator,
            cargoType,
            goodsDesc,
            nominatedTempStorage,
            lpType,
            lpNumber,
            declaredQty,
            lpMarks,
            declaredGrossWeight,
            netWeight,
            volumeUnit,
            volume,
            goodsOriginCountry,
            goodsDescription,
            commodity,
            unDangerousGoods,
            lpRemarks,
            temperature,
            containerRef,
            engineNumber,
            chassisNumber,
            description,
            status,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }
}
