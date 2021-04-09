package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.DeclarationDocumentMessage
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.ManifestDocumentMessage
import org.kebs.app.kotlin.apollo.store.model.di.ManifestDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.IManifestDetailsEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ManifestDaoService {

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var iManifestDetailsEntityRepository: IManifestDetailsEntityRepository

    private val createdByValue = "SYSTEM"

    fun mapManifestMessageToManifestEntity(manifestDocumentMessage: ManifestDocumentMessage): Boolean {
        val manifestNumber = manifestDocumentMessage.data?.sd?.sdRegNo

        if (manifestNumber == null) {
            KotlinLogging.logger { }.error { "Manifest number does not exist. Invalid declaration document received" }
            throw Exception("Invalid Manifest Document Received")
        }

        iManifestDetailsEntityRepository.findByManifestNumber(manifestNumber)?.let {
            KotlinLogging.logger { }.info { "Manifest with the reference no: $manifestNumber already exists" }
            return true
        }

        this.manifestMessageToManifestDetailsEntity(manifestDocumentMessage)?.let { return true }
        return false
    }

    fun manifestMessageToManifestDetailsEntity(manifestDocumentMessage: ManifestDocumentMessage): ManifestDetailsEntity? {
        var manifestDetailsEntity = ManifestDetailsEntity()
        with(manifestDetailsEntity) {
            messageDate = manifestDocumentMessage.header?.messageDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            action = manifestDocumentMessage.header?.action
            sender = manifestDocumentMessage.header?.sender
            receiver = manifestDocumentMessage.header?.receiver
            information = manifestDocumentMessage.header?.information
            messageId = manifestDocumentMessage.header?.messageId
            messageVersion = manifestDocumentMessage.header?.messageVersion
            manifestNumber = manifestDocumentMessage.data?.sd?.sdRegNo
            customsOfficeCountry = manifestDocumentMessage.data?.sd?.cusOfficeCountry
            exitOfficeSub2 = manifestDocumentMessage.data?.sd?.exitOfficeSub2
            exitOfficeSub3 = manifestDocumentMessage.data?.sd?.exitOfficeSub3
            carrierAgentRegistered = manifestDocumentMessage.data?.sd?.carrierAgent?.registered
            carrierAgentPin = manifestDocumentMessage.data?.sd?.carrierAgent?.carrierPin
            carrierAgentBusinessNation = manifestDocumentMessage.data?.sd?.carrierAgent?.businessNation
            carrierAgentBusinessName = manifestDocumentMessage.data?.sd?.carrierAgent?.businessName
            carrierAgentBusinessAddress = manifestDocumentMessage.data?.sd?.carrierAgent?.businessAddr
            transportMode = manifestDocumentMessage.data?.sd?.transMode
            tradeMovement = manifestDocumentMessage.data?.sd?.tradeMovement
            expectedDate = manifestDocumentMessage.data?.sd?.expDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            dispatchCountry = manifestDocumentMessage.data?.sd?.dispatchCtr
            dischargePlace = manifestDocumentMessage.data?.sd?.unloadPlace
            transportNationality = manifestDocumentMessage.data?.sd?.transNationality
            transportId = manifestDocumentMessage.data?.sd?.transId
            voyageNumber = manifestDocumentMessage.data?.sd?.voyageNo
            bookingNumber = manifestDocumentMessage.data?.sd?.bookingNo
            nilCargo = manifestDocumentMessage.data?.sd?.nilCargo
            onboard = manifestDocumentMessage.data?.sd?.onboard
            remark = manifestDocumentMessage.data?.sd?.remark
            refSdRegNumber = manifestDocumentMessage.data?.sd?.consolidatedTd?.refSdRegNo
            refTdPrefix = manifestDocumentMessage.data?.sd?.consolidatedTd?.refTdPrefix
            refTdBillCode = manifestDocumentMessage.data?.sd?.consolidatedTd?.refTdBillCode
            tdPrefix = manifestDocumentMessage.data?.sd?.td?.tdPrefix
            tdBillCode = manifestDocumentMessage.data?.sd?.td?.tdBillCode
            consignmentNumber = manifestDocumentMessage.data?.sd?.td?.consignmentNo
            ucrn = manifestDocumentMessage.data?.sd?.td?.ucrn
            notifyParty = manifestDocumentMessage.data?.sd?.td?.notifyParty
            notifyName = manifestDocumentMessage.data?.sd?.td?.notifyName
            freightCost = manifestDocumentMessage.data?.sd?.td?.freightCost
            insuranceCosts = manifestDocumentMessage.data?.sd?.td?.insuranceCosts
            firstPort = manifestDocumentMessage.data?.sd?.td?.firstPort
            firstPortDate = manifestDocumentMessage.data?.sd?.td?.firstPortDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            transportEquId = manifestDocumentMessage.data?.sd?.td?.consignmentNo
            routing = manifestDocumentMessage.data?.sd?.td?.routing
            finalDestLocCity = manifestDocumentMessage.data?.sd?.td?.finalDestLocCity
            payMethod = manifestDocumentMessage.data?.sd?.td?.payMethod
            currencyCode = manifestDocumentMessage.data?.sd?.td?.currencyCode
            totalInvoiceAmount = manifestDocumentMessage.data?.sd?.td?.totalInvoiceAmount
            subType = manifestDocumentMessage.data?.sd?.td?.subType
            lowValue = manifestDocumentMessage.data?.sd?.td?.lowValue
            tstatus = manifestDocumentMessage.data?.sd?.td?.tStatus
            consFlag = manifestDocumentMessage.data?.sd?.td?.consFlag
            transhipFlag = manifestDocumentMessage.data?.sd?.td?.transhipFlag
            placeOfLoading = manifestDocumentMessage.data?.sd?.td?.loadPlace
            dateOfLoading = manifestDocumentMessage.data?.sd?.td?.loadDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            placeOfDelivery = manifestDocumentMessage.data?.sd?.td?.placeOfDelivery
            directDeliveryIndicator = manifestDocumentMessage.data?.sd?.td?.directDeliveryIndicator
            cargoType = manifestDocumentMessage.data?.sd?.td?.cargoType
            goodsDesc = manifestDocumentMessage.data?.sd?.td?.goodsDesc
            nominatedTempStorage = manifestDocumentMessage.data?.sd?.td?.nominatedTempStorage
            consigneeRegistered = manifestDocumentMessage.data?.sd?.td?.consignee?.registered
            consigneePinNumber = manifestDocumentMessage.data?.sd?.td?.consignee?.pinNumber
            consigneeCountryCode = manifestDocumentMessage.data?.sd?.td?.consignee?.countryCode
            consigneeBusinessName = manifestDocumentMessage.data?.sd?.td?.consignee?.businessName
            consigneePhoneNumber = manifestDocumentMessage.data?.sd?.td?.consignee?.phoneNo
            consigneeBusinessAddress = manifestDocumentMessage.data?.sd?.td?.consignee?.businessAddress
            consigneeBusinessNation = manifestDocumentMessage.data?.sd?.td?.consignee?.businessNation
            consignorRegistered = manifestDocumentMessage.data?.sd?.td?.consignor?.registered
            consignorPinNumber = manifestDocumentMessage.data?.sd?.td?.consignor?.pinNumber
            consignorCountryCode = manifestDocumentMessage.data?.sd?.td?.consignor?.countryCode
            consignorBusinessName = manifestDocumentMessage.data?.sd?.td?.consignor?.businessName
            consignorPhoneNumber = manifestDocumentMessage.data?.sd?.td?.consignor?.phoneNo
            consignorBusinessAddress = manifestDocumentMessage.data?.sd?.td?.consignor?.businessAddress
            consignorBusinessNation = manifestDocumentMessage.data?.sd?.td?.consignor?.businessNation
            lpType = manifestDocumentMessage.data?.sd?.td?.lp?.lpType
            lpNumber = manifestDocumentMessage.data?.sd?.td?.lp?.lpNo
            declaredQty = manifestDocumentMessage.data?.sd?.td?.lp?.declQty
            lpMarks = manifestDocumentMessage.data?.sd?.td?.lp?.remarks
            declaredGrossWeight = manifestDocumentMessage.data?.sd?.td?.lp?.declGrossWgt
            netWeight = manifestDocumentMessage.data?.sd?.td?.lp?.netWeight
            volumeUnit = manifestDocumentMessage.data?.sd?.td?.lp?.volumeUnit
            volume = manifestDocumentMessage.data?.sd?.td?.lp?.volume
            goodsOriginCountry = manifestDocumentMessage.data?.sd?.td?.lp?.goodsOriginCountry
            goodsDescription = manifestDocumentMessage.data?.sd?.td?.lp?.description
            commodity = manifestDocumentMessage.data?.sd?.td?.lp?.commodity
            unDangerousGoods = manifestDocumentMessage.data?.sd?.td?.lp?.unDangerousGoods
            lpRemarks = manifestDocumentMessage.data?.sd?.td?.lp?.remarks
            temperature = manifestDocumentMessage.data?.sd?.td?.lp?.temperature
            containerRef = manifestDocumentMessage.data?.sd?.td?.lp?.containerRef
            engineNumber = manifestDocumentMessage.data?.sd?.td?.lp?.engineNum
            chassisNumber = manifestDocumentMessage.data?.sd?.td?.lp?.chassisNum
            createdBy = createdByValue
            createdOn = commonDaoServices.getTimestamp()
        }
        manifestDetailsEntity = iManifestDetailsEntityRepository.save(manifestDetailsEntity)

        KotlinLogging.logger { }.info { "Manifest Details Entity ID = ${manifestDetailsEntity.id}" }

        return manifestDetailsEntity
    }
}
