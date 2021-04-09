package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.DCLIS
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.DeclarationDocumentMessage
import org.kebs.app.kotlin.apollo.store.model.di.DeclarationDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.DeclarationItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.IDeclarationDetailsEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDeclarationItemDetailsEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DeclarationDaoService {

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var iDeclarationDetailsEntityRepo: IDeclarationDetailsEntityRepository

    @Autowired
    lateinit var iDeclarationItemDetailsEntityRepo: IDeclarationItemDetailsEntityRepository

    private val createdByValue = "SYSTEM"

    fun mapDeclarationMessageToEntities(declarationDocumentMessage: DeclarationDocumentMessage): Boolean {

        val declarationfRefNumber = declarationDocumentMessage.data?.dataIn?.sad?.sadId

        if (declarationfRefNumber == null) {
            KotlinLogging.logger { }.error { "Sad ID does not exist. Invalid declaration document received" }
            throw Exception("Invalid Declaration Document Received")
        }

        iDeclarationDetailsEntityRepo.findByDeclarationRefNo(declarationfRefNumber)?.let {
            KotlinLogging.logger { }.info { "Declaration with the reference no: $declarationfRefNumber already exists" }
            return true
        }

        this.declarationMessageToDeclarationDetailsEntity(declarationDocumentMessage)?.let { declarationDetails ->
            declarationDocumentMessage.data?.dataIn?.sad?.items?.isList?.let { items ->
                if (!items.isEmpty()) {
                    for (item in items) {
                        this.declarationMessageToDeclarationItemDetailsEntity(item, declarationDetails)
                    }
                }
                return true
            }
        }
        return false
    }

    fun declarationMessageToDeclarationDetailsEntity(declarationDocumentMessage: DeclarationDocumentMessage): DeclarationDetailsEntity? {
        var declarationDetailsEntity = DeclarationDetailsEntity()
        with(declarationDetailsEntity) {
            userId = declarationDocumentMessage.header?.userId
            messageDate = declarationDocumentMessage.header?.messageDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            module = declarationDocumentMessage.header?.module
            action = declarationDocumentMessage.header?.action
            information = declarationDocumentMessage.header?.information
            messageVersion = declarationDocumentMessage.header?.messageVersion
            direction = declarationDocumentMessage.header?.direction
            messageNature = declarationDocumentMessage.data?.dataIn?.sad?.messageNature
            declarationRefNo = declarationDocumentMessage.data?.dataIn?.sad?.sadId
            officeCode = declarationDocumentMessage.data?.dataIn?.sad?.cd?.officeCode
            officeSubCode = declarationDocumentMessage.data?.dataIn?.sad?.cd?.officeSubCode
            declarantRegime = declarationDocumentMessage.data?.dataIn?.sad?.cd?.declarantRegime
            refNum = declarationDocumentMessage.data?.dataIn?.sad?.cd?.refNum
            totalInsurance = declarationDocumentMessage.data?.dataIn?.sad?.cd?.totalInsurance?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            totalOtherCharges = declarationDocumentMessage.data?.dataIn?.sad?.cd?.totalOtherCharges?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            totalCustomsValue = declarationDocumentMessage.data?.dataIn?.sad?.cd?.totalCustomsValue?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            valuationMethod = declarationDocumentMessage.data?.dataIn?.sad?.cd?.valuationMethod
            declarantPin = declarationDocumentMessage.data?.dataIn?.sad?.cd?.declarantPin
            declarantAeoFlag = declarationDocumentMessage.data?.dataIn?.sad?.cd?.declarantAeoFlag
            companyPin = declarationDocumentMessage.data?.dataIn?.sad?.cd?.companyPin
            lastConsignmentCountry = declarationDocumentMessage.data?.dataIn?.sad?.cd?.lastConsignmentCountry
            entryDate = declarationDocumentMessage.data?.dataIn?.sad?.cd?.entryDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            regionOfDestinationCode = declarationDocumentMessage.data?.dataIn?.sad?.cd?.regionOfDestinationCode
            idenMeanTransDepArr = declarationDocumentMessage.data?.dataIn?.sad?.cd?.idenMeanTransDepArr
            nationalityTransDepArr = declarationDocumentMessage.data?.dataIn?.sad?.cd?.nationalityTransportDepArr
            delivTermsSub1 = declarationDocumentMessage.data?.dataIn?.sad?.cd?.delivTermsSub1
            delivTermsSub2 = declarationDocumentMessage.data?.dataIn?.sad?.cd?.delivTermsSub2
            currencyCode = declarationDocumentMessage.data?.dataIn?.sad?.cd?.currencyCode
            totalAmount = declarationDocumentMessage.data?.dataIn?.sad?.cd?.totalAmount?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            exchangeRate = declarationDocumentMessage.data?.dataIn?.sad?.cd?.exchangeRate?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            transType = declarationDocumentMessage.data?.dataIn?.sad?.cd?.transportType
            inlandTrans = declarationDocumentMessage.data?.dataIn?.sad?.cd?.inlandTransport
            placeOfLoading = declarationDocumentMessage.data?.dataIn?.sad?.cd?.placeOfLoading
            termsPayment = declarationDocumentMessage.data?.dataIn?.sad?.cd?.termsPayment
            bankAccount = declarationDocumentMessage.data?.dataIn?.sad?.cd?.bankAccount
            bankBranchId = declarationDocumentMessage.data?.dataIn?.sad?.cd?.bankBranchId
            bondNum = declarationDocumentMessage.data?.dataIn?.sad?.cd?.bondNumber
            bondAmount = declarationDocumentMessage.data?.dataIn?.sad?.cd?.bondAmount
            subOffice2 = declarationDocumentMessage.data?.dataIn?.sad?.cd?.subOffice2
            subOffice3 = declarationDocumentMessage.data?.dataIn?.sad?.cd?.subOffice3
            ideWhType = declarationDocumentMessage.data?.dataIn?.sad?.cd?.whType
            ideEh = declarationDocumentMessage.data?.dataIn?.sad?.cd?.eh
            countryTransit = declarationDocumentMessage.data?.dataIn?.sad?.cd?.countryTransit
            cdRegistrationDate = declarationDocumentMessage.data?.dataIn?.sad?.cd?.cdRegistrationDateTime?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            sealsNum = declarationDocumentMessage.data?.dataIn?.sad?.cd?.sealsNum
            exemptionYear = declarationDocumentMessage.data?.dataIn?.sad?.cd?.exemptionYear
            invoiceDate = declarationDocumentMessage.data?.dataIn?.sad?.cd?.invoiceDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            cdPayAmount = declarationDocumentMessage.data?.dataIn?.sad?.cd?.cdPayAmount?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            cdGuaranteedAmount = declarationDocumentMessage.data?.dataIn?.sad?.cd?.cdGuaranteedAmount?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            /*
            Consignor Details
             */
            consignorRegistered = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consigner?.registered
            consignorPinNumber = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consigner?.pinNumber
            consignorCountryCode = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consigner?.countryCode
            consignorBusinessName = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consigner?.registered
            consignorAeoFlag = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consigner?.aeoFlag
            consignorBusinessAddress = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consigner?.businessAddress
            consignorBusinessNation = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consigner?.businessNation
            /*
            Consignee Details
             */
            consigneeRegistered = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consignee?.registered
            consigneePinNumber = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consignee?.pinNumber
            consigneeCountryCode = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consignee?.countryCode
            consigneeBusinessName = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consignee?.businessName
            consigneeAeoFlag = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consignee?.aeoFlag
            consigneeBusinessAddress = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consignee?.businessAddress
            consigneeBusinessNation = declarationDocumentMessage.data?.dataIn?.sad?.cd?.consignee?.businessNation
            createdBy = createdByValue
            createdOn = commonDaoServices.getTimestamp()
            /*
            Manifest details
             */
            manifestNumber = declarationDocumentMessage.data?.dataIn?.sad?.ts?.manifestNo
            prefix = declarationDocumentMessage.data?.dataIn?.sad?.ts?.prefix
            billCode = declarationDocumentMessage.data?.dataIn?.sad?.ts?.billCode
        }
        declarationDetailsEntity = iDeclarationDetailsEntityRepo.save(declarationDetailsEntity)

        KotlinLogging.logger { }.info { "Declaration Details Entity ID = ${declarationDetailsEntity.id}" }

        return declarationDetailsEntity
    }

    fun declarationMessageToDeclarationItemDetailsEntity(declarationIs: DCLIS, declarationDetailsEntity: DeclarationDetailsEntity):
            DeclarationItemDetailsEntity {
        var declarationItemDetailsEntity = DeclarationItemDetailsEntity()
        with(declarationItemDetailsEntity) {
            itemNum = declarationIs.itemNum
            itemPackages = declarationIs.itemPackages
            commodityCode = declarationIs.commodityCode
            additionalCode1 = declarationIs.additionalCode1
            additionalCode2 = declarationIs.additionalCode2
            additionalCode3 = declarationIs.additionalCode3
            tariffGoodsDesc = declarationIs.tariffGoodsDesc
            commercialGoods = declarationIs.commercialGoods
            originCountry = declarationIs.originCountry
            grossMass = declarationIs.grossMass?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            preferenceNum = declarationIs.preferenceNum
            requestedPro = declarationIs.requestedPro
            previousPro = declarationIs.previousPro
            categoryTypePro = declarationIs.categoryTypePro
            subCategoryPro = declarationIs.subcategoryPro
            netMass = declarationIs.netMass?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            quota = declarationIs.quota
            preDocType = declarationIs.preDocType
            preDocAbb = declarationIs.preDocAbb
            preDocFirstSubIde = declarationIs.preDocFirstSubIde
            unitNum = declarationIs.unitNum?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            unitCode = declarationIs.unitCode
            supplementaryUnits = declarationIs.supplementaryUnits
            supplementaryUnits2 = declarationIs.supplementaryUnits2
            itemPrice = declarationIs.itemPrice?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            statValue = declarationIs.statValue?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            itemFreightValue = declarationIs.itemFreightValue?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            itemOtherCharges = declarationIs.itemOtherCharges?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            customsValue = declarationIs.customsValue?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            chassisNumber = declarationIs.chassisNumber
            engineNumber = declarationIs.engineNumber
            declarationDetailsId = declarationDetailsEntity
            createdBy = createdByValue
            createdOn = commonDaoServices.getTimestamp()
        }
        declarationItemDetailsEntity = iDeclarationItemDetailsEntityRepo.save(declarationItemDetailsEntity)

        KotlinLogging.logger { }.info { "Declaration Items Details Entity ID = ${declarationItemDetailsEntity.id}" }

        return declarationItemDetailsEntity
    }
}
