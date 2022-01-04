package org.kebs.app.kotlin.apollo.api.payload.response

import org.kebs.app.kotlin.apollo.store.model.external.SystemApiClient
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersRegionEntity
import javax.persistence.Basic
import javax.persistence.Column

class ApiClientDao {
    var recordId: Long? = null
    var clientType: String? = null
    var clientName: String? = null
    var clientId: String? = null
    var clientRole: String? = null
    var callbackURL: String? = null
    var eventsURL: String? = null // System events
    var descriptions: String? = null
    var clientStatus: Int? = null
    var blockedStatus: Int? = null

    companion object {
        fun fromList(clients: List<SystemApiClient>): List<ApiClientDao> {
            val clientList = mutableListOf<ApiClientDao>()
            clients.forEach { clientList.add(fromEntity(it)) }
            return clientList
        }

        fun fromEntity(client: SystemApiClient): ApiClientDao {
            return ApiClientDao().apply {
                recordId = client.id
                clientType = client.clientType
                clientName = client.clientName
                clientId = client.clientId
                clientRole = client.clientRole
                callbackURL = client.callbackURL
                eventsURL = client.eventsURL
                clientStatus = client.status
                blockedStatus = client.clientBlocked
                descriptions = client.descriptions
            }
        }
    }
}

class PvocPartnerRegionDto {
    var regionId: Long? = null
    var regionName: String? = null
    var description: String? = null
    companion object {
        fun fromEntity(region: PvocPartnersRegionEntity): PvocPartnerRegionDto {
            val dto = PvocPartnerRegionDto()
            dto.apply {
                regionId = region.id
                regionName = region.regionName
                description = region.description
            }
            return dto
        }

        fun fromList(regions: List<PvocPartnersRegionEntity>): List<PvocPartnerRegionDto> {
            val dtos = mutableListOf<PvocPartnerRegionDto>()
            regions.forEach { dtos.add(fromEntity(it)) }
            return dtos
        }
    }
}

class PvocPartnerDto {
    var partnerId: Long? = null
    var clientId: Long? = null
    var partnerRefNo: String? = null
    var partnerName: String? = null
    var partnerPin: String? = null
    var partnerAddress1: String? = null
    var partnerAddress2: String? = null
    var partnerCity: String? = null
    var partnerCountry: String? = null
    var partnerZipcode: String? = null
    var partnerTelephoneNumber: String? = null
    var partnerFaxNumber: String? = null
    var partnerEmail: String? = null
    var remarks: String? = null

    companion object {
        fun fromEntity(partner: PvocPartnersEntity): PvocPartnerDto {
            val dto = PvocPartnerDto()
            dto.apply {
                partnerId = partner.id
                clientId = partner.apiClientId
                partnerName = partner.partnerName
                partnerRefNo = partner.partnerRefNo
                partnerEmail = partner.partnerEmail
                partnerAddress1 = partner.partnerAddress1
                partnerAddress2 = partner.partnerAddress2
                partnerCity = partner.partnerCity
                partnerCountry = partner.partnerCountry
                partnerFaxNumber = partner.partnerFaxNumber
                partnerPin = partner.partnerPin
                partnerTelephoneNumber = partner.partnerTelephoneNumber
                partnerZipcode = partner.partnerZipcode
                remarks = partner.varField10
            }
            return dto
        }

        fun fromList(partners: List<PvocPartnersEntity>): List<PvocPartnerDto> {
            val dtos = mutableListOf<PvocPartnerDto>()
            partners.forEach { dtos.add(fromEntity(it)) }
            return dtos
        }
    }
}
