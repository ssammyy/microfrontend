package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.BaseDocumentResponse
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.IS
import org.kebs.app.kotlin.apollo.store.model.di.IDFDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.IDFItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.IIDFDetailsEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IIDFItemDetailsEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IDFDaoService {

    @Autowired
    lateinit var iIDFDetailsEntityRepository: IIDFDetailsEntityRepository

    @Autowired
    lateinit var iIDFItemDetailsEntityRepository: IIDFItemDetailsEntityRepository

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    private val createdByValue = "SYSTEM"

    fun mapBaseDocumentToIDF(baseDocumentResponse: BaseDocumentResponse): Boolean {

        val idfRefNumber = baseDocumentResponse.data?.dataIn?.sad?.sadId

        if (idfRefNumber == null) {
            KotlinLogging.logger { }.error { "Sad ID does not exist. Invalid base document received" }
            throw Exception("Invalid Base Document Received")
        }

        this.baseDocumentToIDFDetailsEntity(baseDocumentResponse)?.let { idfDetailsEntity ->
            baseDocumentResponse.data?.dataIn?.sad?.items?.isList?.let { items ->
                if (!items.isEmpty()) {
                    for (item in items) {
                        this.baseDocumentIsToIDFItemDetailsEntity(item, idfDetailsEntity)
                    }
                }
                return true
            }
        }
        return false
    }

    fun baseDocumentToIDFDetailsEntity(baseDocumentResponse: BaseDocumentResponse): IDFDetailsEntity? {
        val idfRefNumber = baseDocumentResponse.data?.dataIn?.sad?.sadId
        // Create or update IDF details if it exists
        var idfDetailsEntity = iIDFDetailsEntityRepository.findByBaseDocRefNo(idfRefNumber!!) ?: IDFDetailsEntity()

        with(idfDetailsEntity) {
            userId = baseDocumentResponse.header?.userId
            messageDate = baseDocumentResponse.header?.messageDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            module = baseDocumentResponse.header?.module
            action = baseDocumentResponse.header?.action
            information = baseDocumentResponse.header?.information
            messageVersion = baseDocumentResponse.header?.messageVersion
            direction = baseDocumentResponse.header?.direction
            messageNature = baseDocumentResponse.data?.dataIn?.sad?.messageNature
            baseDocRefNo = baseDocumentResponse.data?.dataIn?.sad?.sadId
            placeOfLoading = baseDocumentResponse.data?.dataIn?.sad?.cd?.b27PlaceOfLoading
            declarantPin = baseDocumentResponse.data?.dataIn?.sad?.cd?.b14DeclarantTr
            declarantAeoFlag = baseDocumentResponse.data?.dataIn?.sad?.cd?.b14DeclarantAeoFlag
            declarantRegime = baseDocumentResponse.data?.dataIn?.sad?.cd?.b1DeclaSub1
            delivTermsSub2 = baseDocumentResponse.data?.dataIn?.sad?.cd?.b20DelivTermsSub2
            totalCustomsValue = baseDocumentResponse.data?.dataIn?.sad?.cd?.totalCustomsValue?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            totalOtherCharges = baseDocumentResponse.data?.dataIn?.sad?.cd?.totalOtherCharges?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            entryDate = baseDocumentResponse.data?.dataIn?.sad?.cd?.fsT18EntryDate?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            delivTermsSub1 = baseDocumentResponse.data?.dataIn?.sad?.cd?.b20DelivTermsSub1
            companyPin = baseDocumentResponse.data?.dataIn?.sad?.cd?.companyTr
            totalAmount = baseDocumentResponse.data?.dataIn?.sad?.cd?.b22TotalAmount?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            officeSubDivisionCode = baseDocumentResponse.data?.dataIn?.sad?.cd?.boxaOfficeSubCode
            refNo = baseDocumentResponse.data?.dataIn?.sad?.cd?.b7RefNum
            currencyCode = baseDocumentResponse.data?.dataIn?.sad?.cd?.b22CurrencyCode
            officeCode = baseDocumentResponse.data?.dataIn?.sad?.cd?.boxaOfficeCode
            cdRegistrationDate = baseDocumentResponse.data?.dataIn?.sad?.cd?.cdRegistrationDateTime?.let { commonDaoServices.convertISO8601DateToTimestamp(it) }
            totalInsurance = baseDocumentResponse.data?.dataIn?.sad?.cd?.b9TotalInsurance?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            transportType = baseDocumentResponse.data?.dataIn?.sad?.cd?.transType
            consigneePinNo = baseDocumentResponse.data?.dataIn?.sad?.cd?.consignee?.trNumber
            consigneeCountryCode = baseDocumentResponse.data?.dataIn?.sad?.cd?.consignee?.countryCode
            consigneeBusinessName = baseDocumentResponse.data?.dataIn?.sad?.cd?.consignee?.businessName
            consigneeAeoFlag = baseDocumentResponse.data?.dataIn?.sad?.cd?.consignee?.aeoFlag
            consigneeBusinessAddress = baseDocumentResponse.data?.dataIn?.sad?.cd?.consignee?.businessAddr
            consigneeBusinessNation = baseDocumentResponse.data?.dataIn?.sad?.cd?.consignee?.businessNation
            consignorPinNo = baseDocumentResponse.data?.dataIn?.sad?.cd?.consigner?.trNumber
            consignorCountryCode = baseDocumentResponse.data?.dataIn?.sad?.cd?.consigner?.countryCode
            consignorBusinessName = baseDocumentResponse.data?.dataIn?.sad?.cd?.consigner?.businessName
            consignorAeoFlag = baseDocumentResponse.data?.dataIn?.sad?.cd?.consigner?.aeoFlag
            consignorBusinessAddress = baseDocumentResponse.data?.dataIn?.sad?.cd?.consigner?.businessAddr
            consignorBusinessNation = baseDocumentResponse.data?.dataIn?.sad?.cd?.consigner?.businessNation
            createdBy = createdByValue
            createdOn = commonDaoServices.getTimestamp()
        }
        idfDetailsEntity = iIDFDetailsEntityRepository.save(idfDetailsEntity)

        KotlinLogging.logger { }.info { "IDF Details Entity ID = ${idfDetailsEntity.id}" }

        return idfDetailsEntity
    }

    fun baseDocumentIsToIDFItemDetailsEntity(baseDocIs: IS, iDFDetailsEntity: IDFDetailsEntity): IDFItemDetailsEntity {
        var iDFItemDetailsEntity = IDFItemDetailsEntity()
        with(iDFItemDetailsEntity) {
            categoryTypePro = baseDocIs.categoryTypePro
            subCategoryPro = baseDocIs.subcategoryPro
            previousPro = baseDocIs.b37PreviousPro
            tariffGoodsDesc = baseDocIs.tariffGoodsDesc
            itemPrice = baseDocIs.b42ItemPrice?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            itemNum = baseDocIs.itemNum
            netMass = baseDocIs.b38NetMass
            commercialGoods = baseDocIs.b31CommercialGoods
            itemFreightValue = baseDocIs.itemFreightValue
            commodityCode = baseDocIs.b33CommodityCode
            insuranceValue = baseDocIs.itemInsuranceValue?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            customsValue = baseDocIs.customsValue?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            originCountry = baseDocIs.b34OriginCountry
            unitNum = baseDocIs.b41UnitNum
            unitCode = baseDocIs.unitCode
            requestedPro = baseDocIs.b37RequestedPro
            itemOtherCharges = baseDocIs.itemOtherCharges?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            itemFobValue = baseDocIs.b45ItemFobVal?.let { commonDaoServices.convertStringAmountToBigDecimal(it) }
            idfDetails = iDFDetailsEntity
            createdBy = createdByValue
            createdOn = commonDaoServices.getTimestamp()
        }
        iDFItemDetailsEntity = iIDFItemDetailsEntityRepository.save(iDFItemDetailsEntity)

        KotlinLogging.logger { }.info { "IDF Item Details Entity ID = ${iDFItemDetailsEntity.id}" }

        return iDFItemDetailsEntity
    }

    //Update UCR no for IDF
    fun updateIdfUcrNumber(baseDocRefNo: String, ucrNumber: String): Boolean {
        iIDFDetailsEntityRepository.findByBaseDocRefNo(baseDocRefNo)?.let { idfDetailsEntity ->
            with(idfDetailsEntity) {
                ucrNo = ucrNumber
                modifiedBy = "SYSTEM"
                modifiedOn = commonDaoServices.getTimestamp()
            }
            iIDFDetailsEntityRepository.save(idfDetailsEntity)
            return true
        } ?: run {
            KotlinLogging.logger { }.warn { "IDF Details Entity with REF NO: ${baseDocRefNo} not found" }
            // Create details to ensure ordering is not required for Document processing
            val idfDetailsEntity = IDFDetailsEntity()
            idfDetailsEntity.baseDocRefNo = baseDocRefNo
            idfDetailsEntity.ucrNo = baseDocRefNo
            idfDetailsEntity.createdBy = createdByValue
            idfDetailsEntity.createdOn = commonDaoServices.getTimestamp()
            this.iIDFDetailsEntityRepository.save(idfDetailsEntity)
        }

        return false
    }
}
