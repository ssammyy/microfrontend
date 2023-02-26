package org.kebs.app.kotlin.apollo.api.payload.request

import org.kebs.app.kotlin.apollo.store.model.di.CfsTypeCodesEntity
import org.kebs.app.kotlin.apollo.store.model.di.CustomsOfficeTypeCodesEntity
import org.kebs.app.kotlin.apollo.store.model.di.DestinationInspectionFeeEntity
import org.kebs.app.kotlin.apollo.store.model.di.InspectionFeeRanges
import org.kebs.app.kotlin.apollo.store.model.invoice.BillingLimits
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersCountriesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersRegionEntity
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class DIRageFees {
    var rangeId: Long? = null
    var currencyCode: String? = null
    var minimumAmount: BigDecimal? = null
    var maximumAmount: BigDecimal? = null
    var amount: BigDecimal? = null
    var rate: BigDecimal? = null
    var rateType: String? = null
    var name: String? = null
    var description: String? = null
    fun fillEntity(dataEntity: InspectionFeeRanges) {
        dataEntity.rate = rate
        dataEntity.modifiedOn = Timestamp.from(Instant.now())
        dataEntity.createdOn = Timestamp.from(Instant.now())
        dataEntity.rateType = rateType
        dataEntity.name = name
        dataEntity.description = description
        when {
            "FIXED".equals(rateType, true) -> {
                dataEntity.fixedAmount = amount
            }
            else -> {
                dataEntity.fixedAmount = rate
            }
        }
        when {
            "USD".equals(currencyCode, true) -> {
                dataEntity.minimumUsd = minimumAmount
                dataEntity.maximumUsd = maximumAmount
            }
            else -> {
                dataEntity.minimumKsh = minimumAmount
                dataEntity.maximumKsh = maximumAmount
            }
        }

    }
}


class DIFees {
    @NotEmpty(message = "Currency code is required")
    var currencyCode: String? = null

    @NotNull(message = "Minimum Amount is required")
    var minimumAmount: BigDecimal? = null

    @NotNull(message = "Maximum Amount is required")
    var maximumAmount: BigDecimal? = null

    @NotNull(message = "Rate is required")
    var rate: BigDecimal? = null

    @NotNull(message = "Please select rate type")
    var rateType: String? = null

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name cannot be empty")
    var name: String? = null
    var goodCode: String? = null

    @NotEmpty(message = "Description cannot be empty")
    var description: String? = null
    var status: Int? = null
    var feeRanges: List<DIRageFees>? = null
    fun fillEntity(dataEntity: DestinationInspectionFeeEntity) {
        dataEntity.rate = rate
        dataEntity.modifiedOn = Timestamp.from(Instant.now())
        dataEntity.createdOn = Timestamp.from(Instant.now())
        dataEntity.rateType = rateType
        dataEntity.name = name
        dataEntity.description = description
        dataEntity.goodCode = goodCode
        dataEntity.amountKsh = rate?.toString() ?: "0.0"
        dataEntity.status = status ?: 1
        when {
            "USD".equals(currencyCode, true) -> {
                dataEntity.minimumUsd = minimumAmount?.toString() ?: "0.0"
                dataEntity.higher = maximumAmount?.toString() ?: "0.0"
            }
            else -> {
                dataEntity.minimumKsh = minimumAmount?.toString() ?: "0.0"
                dataEntity.maximumKsh = maximumAmount?.toString() ?: "0.0"
            }
        }

    }
}

class CfsStationForm {
    @NotEmpty(message = "CFS code is required")
    @Size(min = 1, max = 4, message = "CFS code should be 1-4 characters")
    var cfsCode: String? = null
    var altCfsCode: String? = null
    var cfsNumber: String? = null

    @NotEmpty(message = "Revenue line number is required")
    @Size(min = 1, max = 30, message = "Revenue line number should be 1-30 characters")
    var revenueLineNumber: String? = null

    @NotEmpty(message = "CFS name is required")
    var cfsName: String? = null

    @NotEmpty(message = "CFS description is required")
    var description: String? = null
    var status: Int = 1
    fun fillEntity(cfs: CfsTypeCodesEntity) {
        cfs.cfsCode = cfsCode
        cfs.altCfsCode = altCfsCode
        cfs.cfsName = cfsName
        cfs.revenueLineNumber = revenueLineNumber
        cfs.description = description
        cfs.status = status
    }
}

class CfsStationDao {
    var id: Long? = null
    var cfsCode: String? = null
    var altCfsCode: String? = null
    var cfsNumber: String? = null
    var revenueLineNumber: String? = null
    var cfsName: String? = null
    var description: String? = null
    var status: Int? = 1

    companion object {
        fun fromEntity(cfsEntity: CfsTypeCodesEntity): CfsStationDao {
            val cfs = CfsStationDao()
            cfs.cfsCode = cfsEntity.cfsCode
            cfs.altCfsCode = cfsEntity.altCfsCode
            cfs.cfsName = cfsEntity.cfsName
            cfs.revenueLineNumber = cfsEntity.revenueLineNumber
            cfs.description = cfsEntity.description
            cfs.id = cfsEntity.id
            cfs.status = cfsEntity.status
            return cfs
        }

        fun fromList(cfsList: List<CfsTypeCodesEntity>): List<CfsStationDao> {
            val cfsLst = mutableListOf<CfsStationDao>()
            cfsList.forEach { cfs ->
                cfsLst.add(fromEntity(cfs))
            }
            return cfsLst
        }
    }
}

class BillingLimitForm {
    @NotEmpty(message = "Bill type is required")
    var billType: String? = null

    @NotEmpty(message = "Corporate type is required")
    var corporateType: String? = null

    @NotEmpty(message = "Bill receipt prefix is required")
    var billReceiptPrefix: String? = null

    // DATE_RANGE, LAST_MONTH
    @NotEmpty(message = "Bill Date type is required")
    var billDateType: String? = null

    var billStartDate: Int? = null
    var billEndDay: Int? = null

    @NotNull(message = "Bill payment date is required")
    var billPaymentDay: Int? = null

    @NotNull(message = "Bill penalty is required")
    var penaltyAmount: BigDecimal? = null

    @NotEmpty(message = "Bill penalty type is required")
    var penaltyType: String? = null

    @NotNull(message = "Max bill amount is required")
    var maxBillAmount: BigDecimal? = null

    @NotNull(message = "Status is required")
    var status: Int? = null

    fun fillEntity(billLimit: BillingLimits) {
        billLimit.status = status ?: 1
        billLimit.billPaymentDay = billPaymentDay
        billLimit.corporateType = corporateType
        billLimit.billReceiptPrefix = billReceiptPrefix
        billLimit.billDateType = billDateType
        billLimit.billType = billType
        billLimit.penaltyAmount = penaltyAmount
        billLimit.penaltyType = penaltyType
        billLimit.maxBillAmount = maxBillAmount
        billLimit.billStartDate = billStartDate
        billLimit.billEndDay = billEndDay
    }
}

class CustomOfficeForm {
    var customsOfficeCode: String? = null
    var customsOfficeName: String? = null
    var description: String? = null
    var status: Int? = null

    fun fillEntity(office: CustomsOfficeTypeCodesEntity) {
        office.customsOfficeCode = customsOfficeCode
        office.customsOfficeName = customsOfficeName
        office.description = description
        office.status = status ?: 1
    }
}

class PvocPartnerRegionForm {
    @NotEmpty(message = "Region name is required")
    var regionName: String? = null

    @NotEmpty(message = "Region description is required")
    var description: String? = null
    var status: String? = null

    fun fillEntity(region: PvocPartnersRegionEntity) {
        region.regionName = regionName
        region.description = description
        region.status = status
    }
}

class PvocPartnerCountryForm {
    @NotEmpty(message = "Country name is required")
    var countryName: String? = null

    @NotEmpty(message = "Country code is required")
    var abbreviation: String? = null

    @NotNull(message = "Please select region")
    @Min(value = 1, message = "Select valid region")
    var regionId: Long? = null

    @NotEmpty(message = "Country description is required")
    var description: String? = null

    fun fillEntity(country: PvocPartnersCountriesEntity) {
        country.countryName = countryName
        country.abbreviation = abbreviation
        country.description = description
    }
}