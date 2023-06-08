package org.kebs.app.kotlin.apollo.api.ports.provided.dao


import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdBlackListUserTargetTypesEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdConsigneeDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdConsignorDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdExporterDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdImporterDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.riskProfile.RiskProfileConsigneeEntity
import org.kebs.app.kotlin.apollo.store.model.riskProfile.RiskProfileConsignorEntity
import org.kebs.app.kotlin.apollo.store.model.riskProfile.RiskProfileExporterEntity
import org.kebs.app.kotlin.apollo.store.model.riskProfile.RiskProfileImporterEntity
import org.kebs.app.kotlin.apollo.store.repo.riskProfile.IRiskProfileConsigneeRepository
import org.kebs.app.kotlin.apollo.store.repo.riskProfile.IRiskProfileConsignorRepository
import org.kebs.app.kotlin.apollo.store.repo.riskProfile.IRiskProfileExporterRepository
import org.kebs.app.kotlin.apollo.store.repo.riskProfile.IRiskProfileImporterRepository
import org.springframework.stereotype.Service

@Service
class RiskProfileDaoService(
        private val iRiskProfileConsigneeRepo: IRiskProfileConsigneeRepository,
        private val iRiskProfileConsignorRepo: IRiskProfileConsignorRepository,
        private val iRiskProfileExporterRepo: IRiskProfileExporterRepository,
        private val iRiskProfileImporterRepo: IRiskProfileImporterRepository,
        private val applicationMapProperties: ApplicationMapProperties,
        private val commonDaoServices: CommonDaoServices
) {

    val appId = applicationMapProperties.mapRiskProfile
    val map = commonDaoServices.serviceMapDetails(appId)

    fun addImporterToRiskProfile(cdImporterDetailsEntity: CdImporterDetailsEntity, reasonForBlacklisting: String, user:UsersEntity) :Boolean{
        val riskProfileImporter = RiskProfileImporterEntity()
        with(riskProfileImporter){
            name = cdImporterDetailsEntity.name
            pin = cdImporterDetailsEntity.pin
            postalAddress = cdImporterDetailsEntity.postalAddress
            physicalAddress = cdImporterDetailsEntity.physicalAddress
            telephone = cdImporterDetailsEntity.telephone
            applicationCode = cdImporterDetailsEntity.applicationCode
            postalCountry = cdImporterDetailsEntity.postalCountry
            physicalCountry = cdImporterDetailsEntity.physicalCountry
            email = cdImporterDetailsEntity.email
            ogaRefNo = cdImporterDetailsEntity.ogaRefNo
            fax = cdImporterDetailsEntity.fax
            sectorOfActivity = cdImporterDetailsEntity.sectorOfActivity
            warehouseLocation = cdImporterDetailsEntity.warehouseLocation
            warehouseCode = cdImporterDetailsEntity.warehouseCode
            postalCountryName = cdImporterDetailsEntity.postalCountryName
            physicalCountryName = cdImporterDetailsEntity.physicalCountryName
            mdaRefNo = cdImporterDetailsEntity.mdaRefNo
            reason = reasonForBlacklisting
//            description = cdImporterDetailsEntity.description
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        iRiskProfileImporterRepo.save(riskProfileImporter)

        return true
    }

    fun addExporterToRiskProfile(cdExporterDetailsEntity: CdExporterDetailsEntity, reasonForBlacklisting: String,user:UsersEntity) :Boolean{
        val riskProfileExporter = RiskProfileExporterEntity()
        with(riskProfileExporter){
            name =cdExporterDetailsEntity.name
            pin =cdExporterDetailsEntity.pin
            postalAddress =cdExporterDetailsEntity.postalAddress
            physicalAddress =cdExporterDetailsEntity.physicalAddress
            telephone =cdExporterDetailsEntity.telephone
            applicationCode =cdExporterDetailsEntity.applicationCode
            postalCountry =cdExporterDetailsEntity.postalCountry
            physicalCountry =cdExporterDetailsEntity.physicalCountry
            email =cdExporterDetailsEntity.email
            ogaRefNo =cdExporterDetailsEntity.ogaRefNo
            fax =cdExporterDetailsEntity.fax
            sectorOfActivity =cdExporterDetailsEntity.sectorOfActivity
            warehouseLocation =cdExporterDetailsEntity.warehouseLocation
            warehouseCode =cdExporterDetailsEntity.warehouseCode
            postalCountryName =cdExporterDetailsEntity.postalCountryName
            physicalCountryName =cdExporterDetailsEntity.physicalCountryName
            mdaRefNo =cdExporterDetailsEntity.mdaRefNo
            reason = reasonForBlacklisting
//            description = cdImporterDetailsEntity.description
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        iRiskProfileExporterRepo.save(riskProfileExporter)

        return true
    }

    fun addConsignorToRiskProfile(cdConsignorDetailsEntity: CdConsignorDetailsEntity, reasonForBlacklisting: String,user:UsersEntity) :Boolean{
        val riskProfileConsignor = RiskProfileConsignorEntity()
        with(riskProfileConsignor){
            name =cdConsignorDetailsEntity.name
            pin =cdConsignorDetailsEntity.pin
            postalAddress =cdConsignorDetailsEntity.postalAddress
            physicalAddress =cdConsignorDetailsEntity.physicalAddress
            telephone =cdConsignorDetailsEntity.telephone
            applicationCode =cdConsignorDetailsEntity.applicationCode
            postalCountry =cdConsignorDetailsEntity.postalCountry
            physicalCountry =cdConsignorDetailsEntity.physicalCountry
            email =cdConsignorDetailsEntity.email
            ogaRefNo =cdConsignorDetailsEntity.ogaRefNo
            fax =cdConsignorDetailsEntity.fax
            sectorOfActivity =cdConsignorDetailsEntity.sectorOfActivity
            warehouseLocation =cdConsignorDetailsEntity.warehouseLocation
            warehouseCode =cdConsignorDetailsEntity.warehouseCode
            postalCountryName =cdConsignorDetailsEntity.postalCountryName
            physicalCountryName =cdConsignorDetailsEntity.physicalCountryName
            mdaRefNo =cdConsignorDetailsEntity.mdaRefNo
            reason = reasonForBlacklisting
//            description = cdImporterDetailsEntity.description
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        iRiskProfileConsignorRepo.save(riskProfileConsignor)

        return true
    }

    fun addConsigneeToRiskProfile(cdConsigneeDetailsEntity: CdConsigneeDetailsEntity, reasonForBlacklisting: String,user:UsersEntity) :Boolean{
        val riskProfileConsignee = RiskProfileConsigneeEntity()
        with(riskProfileConsignee){
            name =cdConsigneeDetailsEntity.name
            pin =cdConsigneeDetailsEntity.pin
            postalAddress =cdConsigneeDetailsEntity.postalAddress
            physicalAddress =cdConsigneeDetailsEntity.physicalAddress
            telephone =cdConsigneeDetailsEntity.telephone
            applicationCode =cdConsigneeDetailsEntity.applicationCode
            postalCountry =cdConsigneeDetailsEntity.postalCountry
            physicalCountry =cdConsigneeDetailsEntity.physicalCountry
            email =cdConsigneeDetailsEntity.email
            ogaRefNo =cdConsigneeDetailsEntity.ogaRefNo
            fax =cdConsigneeDetailsEntity.fax
            sectorOfActivity =cdConsigneeDetailsEntity.sectorOfActivity
            warehouseLocation =cdConsigneeDetailsEntity.warehouseLocation
            warehouseCode =cdConsigneeDetailsEntity.warehouseCode
            postalCountryName =cdConsigneeDetailsEntity.postalCountryName
            physicalCountryName =cdConsigneeDetailsEntity.physicalCountryName
            mdaRefNo =cdConsigneeDetailsEntity.mdaRefNo
            reason = reasonForBlacklisting
//            description = cdImporterDetailsEntity.description
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        iRiskProfileConsigneeRepo.save(riskProfileConsignee)

        return true
    }

    fun findImportersInRiskProfile(pinNumber:String,status: Int): RiskProfileImporterEntity? {
        return iRiskProfileImporterRepo.findByPinAndStatus(pinNumber,status)
    }

    fun findExporterInRiskProfile(pinNumber:String,status: Int): RiskProfileExporterEntity? {
        return iRiskProfileExporterRepo.findByPinAndStatus(pinNumber,status)
    }

    fun findConsigneeInRiskProfile(pinNumber:String,status: Int): RiskProfileConsigneeEntity? {
        return iRiskProfileConsigneeRepo.findByPinAndStatus(pinNumber,status)
    }

    fun findConsignorInRiskProfile(pinNumber:String,status: Int): RiskProfileConsignorEntity? {
        return iRiskProfileConsignorRepo.findByPinAndStatus(pinNumber,status)
    }


}

