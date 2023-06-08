package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.payload.response.DiFeeRangesDao
import org.kebs.app.kotlin.apollo.api.payload.response.PvocPartnerCountryDao
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.di.CfsTypeCodesEntity
import org.kebs.app.kotlin.apollo.store.model.di.CustomsOfficeTypeCodesEntity
import org.kebs.app.kotlin.apollo.store.model.di.DestinationInspectionFeeEntity
import org.kebs.app.kotlin.apollo.store.model.di.InspectionFeeRanges
import org.kebs.app.kotlin.apollo.store.model.invoice.BillingLimits
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersCountriesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersRegionEntity
import org.kebs.app.kotlin.apollo.store.repo.IBillingLimitsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import java.sql.Timestamp
import java.time.Instant

@Configuration
class DestinationInspectionConfigService(
        private val feeDetailsRepo: IDestinationInspectionFeeRepository,
        private val feeRangesRepository: InspectionFeeRangesRepository,
        private val customOfficeRepo: ICustomsOfficeTypeCodesRepository,
        private val cfsTypeCodesRepository: ICfsTypeCodesRepository,
        private val billingLimitsRepository: IBillingLimitsRepository,
        private val pvocCountriesRepository: IPvocPartnersCountriesRepository,
        private val pvocRegionsRepository: IPvocPartnersRegion,
        private val commonDaoServices: CommonDaoServices,
) {

    fun addDestinationInspectionFeeRange(form: DIRageFees, feeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val fee = feeDetailsRepo.findById(feeId)
            if (fee.isPresent) {
                response.data = addFeeRange(form, fee.get())
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Fee range added"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "No such inspection fee"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to add fee range")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Fee range could not be added"
        }
        return response
    }

    fun addFeeRange(form: DIRageFees, fee: DestinationInspectionFeeEntity): InspectionFeeRanges {
        val feeRange = InspectionFeeRanges()
        form.fillEntity(feeRange)
        feeRange.createdBy = commonDaoServices.loggedInUserAuthentication().name
        feeRange.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
        feeRange.inspectionFee = fee
        return feeRangesRepository.save(feeRange);
    }

    fun updateFeeRange(form: DIRageFees, feeRange: InspectionFeeRanges): InspectionFeeRanges {
        form.fillEntity(feeRange)
        feeRange.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
        return feeRangesRepository.save(feeRange);
    }

    fun removeDestinationInspectionFeeRange(feeId: Long, feeRangeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val fee = feeRangesRepository.findByIdAndInspectionFee_Id(feeRangeId, feeId)
            if (fee.isPresent) {
                val feeRange = fee.get()
                this.feeRangesRepository.delete(feeRange)
                response.data = feeRange
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Fee range removed"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "No such inspection fee range"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to remove fee range")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Fee range could not be removed"
        }
        return response
    }

    fun updateDestinationInspectionFeeRange(form: DIRageFees, feeRangeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val fee = feeRangesRepository.findById(feeRangeId)
            if (fee.isPresent) {
                response.data = updateFeeRange(form, fee.get())
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Fee range update"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "No such fee range"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to update fee range")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Fee range could not be updated"
        }
        return response
    }


    fun addDestinationInspectionFee(form: DIFees): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            var fee = DestinationInspectionFeeEntity()
            form.fillEntity(fee)
            fee.createdBy = commonDaoServices.loggedInUserAuthentication().name
            fee.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
            fee = feeDetailsRepo.save(fee)

            form.feeRanges?.forEach { range -> addFeeRange(range, fee) }
            response.data = fee
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Fee added"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to add fee")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Fee could not be added"
        }
        return response
    }

    fun removeDestinationInspectionFee(feeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalFee = feeDetailsRepo.findById(feeId)
            if (optionalFee.isPresent) {
                val fee = optionalFee.get()
                feeDetailsRepo.delete(fee)
                response.data = fee
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Fee deleted"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Fee does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to delete fee")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Fee could not be deleted"
        }
        return response
    }

    fun updateDestinationInspectionFee(form: DIFees, feeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalFee = feeDetailsRepo.findById(feeId)
            if (optionalFee.isPresent) {
                val fee = optionalFee.get()
                form.fillEntity(fee)
                fee.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                feeDetailsRepo.save(fee)
                form.feeRanges?.forEach { range ->
                    range.rangeId?.let {
                        val savedRange = feeRangesRepository.findByIdAndInspectionFee_Id(it, feeId)
                        if (savedRange.isPresent) {
                            updateFeeRange(range, savedRange.get())
                        }
                    } ?: addFeeRange(range, fee)
                }
                response.data = fee
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Fee updated"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Fee does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to add fee")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Fee could not be added"
        }
        return response
    }

    fun getDestinationInspectionFee(feeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalFee = feeDetailsRepo.findById(feeId)
            if (optionalFee.isPresent) {
                val fee = optionalFee.get()
                val dataMap = mutableMapOf<String, Any>()
                dataMap["fee"] = fee
                dataMap["feeRanges"] = DiFeeRangesDao.fromList(feeRangesRepository.findByInspectionFee(fee))
                response.data = dataMap
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Fees"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Fee does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to get fee details")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Get fee failed, please contact administator"
        }
        return response
    }

    fun listDestinationInspectionFee(page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val fees = feeDetailsRepo.findAll(page)
            response.data = fees.toList()
            response.totalCount = fees.totalElements
            response.pageNo = fees.number
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Fees"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to get fee details")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Get fee failed, please contact administator"
        }
        return response
    }

    // ================= CFS stations
    fun addCfsStation(form: CfsStationForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            cfsTypeCodesRepository.findByCfsCode(form.cfsCode ?: "")?.let {
                response.data = form
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "CFS code already exists"
                response
            } ?: run {
                val cfs = CfsTypeCodesEntity()
                form.fillEntity(cfs)
                cfs.createdOn = Timestamp.from(Instant.now())
                cfs.modifiedOn = Timestamp.from(Instant.now())
                cfs.createdBy = commonDaoServices.loggedInUserAuthentication().name
                cfs.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                response.data = cfsTypeCodesRepository.save(cfs)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Fee added"
                response
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to add fee", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Fee could not be added"
        }
        return response
    }

    fun removeCfsStation(cfsId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalCfs = cfsTypeCodesRepository.findById(cfsId)
            if (optionalCfs.isPresent) {
                val cfs = optionalCfs.get()
                cfs.deleteBy = commonDaoServices.loggedInUserAuthentication().name
                cfs.deletedOn = Timestamp.from(Instant.now())
                cfsTypeCodesRepository.delete(cfs)
                response.data = cfs
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "CFS deleted"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Fee does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to delete CFS")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CFS could not be deleted, check its not used by consignments; you can disable CFS instead"
        }
        return response
    }

    fun updateCfsStation(form: CfsStationForm, cfsId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalCfs = cfsTypeCodesRepository.findById(cfsId)
            if (optionalCfs.isPresent) {
                val cfs = optionalCfs.get()
                val existing = cfsTypeCodesRepository.findByCfsCode(form.cfsCode ?: "")
                if (existing?.id != cfsId) {
                    response.data = form
                    response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                    response.message = "CFS with CFS code exists"
                } else {
                    form.fillEntity(cfs)
                    cfs.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                    cfs.modifiedOn = Timestamp.from(Instant.now())
                    response.data = cfsTypeCodesRepository.save(cfs)
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "CFS updated"
                }
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "CFS does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to add CFS", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CFS could not be added"
        }
        return response
    }

    fun listCfsStations(page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val fees = cfsTypeCodesRepository.findAll(page)
            response.data = fees.toList()
            response.totalCount = fees.totalElements
            response.pageNo = fees.number
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "CFS codes"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to list CFS's")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Could not list CFS, please contact administator"
        }
        return response
    }

    // ================= Billing types
    fun addBillingLimits(form: BillingLimitForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val billLimit = BillingLimits()
            form.fillEntity(billLimit)
            billLimit.createdBy = commonDaoServices.loggedInUserAuthentication().name
            billLimit.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
            response.data = billingLimitsRepository.save(billLimit)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Bill Type added"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to bill type", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add bill type"
        }
        return response
    }

    fun removeBillLimit(limitId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalBillLimit = billingLimitsRepository.findById(limitId)
            if (optionalBillLimit.isPresent) {
                val limit = optionalBillLimit.get()
                limit.deleteBy = commonDaoServices.loggedInUserAuthentication().name
                limit.deletedOn = Timestamp.from(Instant.now())
                billingLimitsRepository.delete(limit)
                response.data = limit
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Bill type deleted"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Bill type does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to delete bill type")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Billing type not found, check its not used by corporate account; you can disable bill type instead"
        }
        return response
    }

    fun updateBillType(form: BillingLimitForm, cfsId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalBillType = billingLimitsRepository.findById(cfsId)
            if (optionalBillType.isPresent) {
                val billType = optionalBillType.get()
                form.fillEntity(billType)
                billType.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                response.data = billingLimitsRepository.save(billType)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Bill Type updated"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Bill type does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to update bill type", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Bill type could not be update"
        }
        return response
    }

    fun listBillTypes(page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val billLimits = billingLimitsRepository.findAll(page)
            response.data = billLimits.toList()
            response.totalCount = billLimits.totalElements
            response.pageNo = billLimits.number
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Bill types"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to list Bill types")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Could not list bill types, please contact administator"
        }
        return response
    }

    // ================= Customs office
    fun addCustomOffice(form: CustomOfficeForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val customOffice = CustomsOfficeTypeCodesEntity()
            form.fillEntity(customOffice)
            val existing = customOfficeRepo.findByCustomsOfficeCode(form.customsOfficeCode ?: "")
            if (existing.isPresent) {
                response.data = form
                response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                response.message = "Custom office code exist"
            } else {
                customOffice.createdBy = commonDaoServices.loggedInUserAuthentication().name
                customOffice.createdOn = Timestamp.from(Instant.now())
                customOffice.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                response.data = customOfficeRepo.save(customOffice)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Custom office added"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to bill type", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add bill type"
        }
        return response
    }

    fun removeCustomOffice(officeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalOffice = customOfficeRepo.findById(officeId)
            if (optionalOffice.isPresent) {
                val office = optionalOffice.get()
                office.deleteBy = commonDaoServices.loggedInUserAuthentication().name
                office.deletedOn = Timestamp.from(Instant.now())
                customOfficeRepo.delete(office)
                response.data = office
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Custom office deleted"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Custom office does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to delete customs office", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Custom office could not be deleted, check its not used by consignment; you can disable customs office instead"
        }
        return response
    }

    fun updateCustomsOffice(form: CustomOfficeForm, officeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalOffice = customOfficeRepo.findById(officeId)
            if (optionalOffice.isPresent) {
                val office = optionalOffice.get()
                val existing = customOfficeRepo.findByCustomsOfficeCode(form.customsOfficeCode ?: "")
                if (existing.isPresent && existing.get().id != officeId) {
                    response.data = form
                    response.responseCode = ResponseCodes.DUPLICATE_ENTRY_STATUS
                    response.message = "Another custom office code exist"
                } else {
                    form.fillEntity(office)
                    office.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                    office.modifiedOn = Timestamp.from(Instant.now())
                    response.data = customOfficeRepo.save(office)
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Custom office updated"
                }
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Custom office does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to update customs office", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Custom office could not be update; contact administrator"
        }
        return response
    }

    fun listCustomsOffice(page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val billLimits = customOfficeRepo.findAll(page)
            response.data = billLimits.toList()
            response.totalCount = billLimits.totalElements
            response.pageNo = billLimits.number
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Custom offices"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to list custom offices")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Could not list custom offices, please contact administator"
        }
        return response
    }

    // ================= PVOC region & countries
    fun addPvocCountry(form: PvocPartnerCountryForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val country = PvocPartnersCountriesEntity()
            val region = pvocRegionsRepository.findById(form.regionId!!)
            if (region.isPresent) {

                form.fillEntity(country)
                country.regionId = region.get()
                country.createdBy = commonDaoServices.loggedInUserAuthentication().name
                country.modifiedOn = Timestamp.from(Instant.now())
                country.createdOn = Timestamp.from(Instant.now())
                country.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                response.data = pvocCountriesRepository.save(country)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "PVOC country added"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "PVOC region not found"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC country", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add PVOC country"
        }
        return response
    }

    fun removePvocCountry(countryId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalCountry = pvocCountriesRepository.findById(countryId)
            if (optionalCountry.isPresent) {
                val country = optionalCountry.get()
                country.deleteBy = commonDaoServices.loggedInUserAuthentication().name
                country.deletedOn = Timestamp.from(Instant.now())
                pvocCountriesRepository.delete(country)
                response.data = country
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Country deleted"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Country does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to delete customs office", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Custom office could not be deleted, check its not used by consignment; you can disable customs office instead"
        }
        return response
    }

    fun updatePvocCountry(form: PvocPartnerCountryForm, officeId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalCountry = pvocCountriesRepository.findById(officeId)
            if (optionalCountry.isPresent) {
                val country = optionalCountry.get()
                val region = pvocRegionsRepository.findById(form.regionId!!)
                if (region.isPresent) {
                    form.fillEntity(country)
                    country.regionId = region.get()
                    country.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                    country.modifiedOn = Timestamp.from(Instant.now())
                    response.data = pvocCountriesRepository.save(country)
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Country updated"
                } else {
                    response.responseCode = ResponseCodes.FAILED_CODE
                    response.message = "Selected region does not exits"
                }
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "PVOC country does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to update customs office", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Custom office could not be update; contact administrator"
        }
        return response
    }

    fun listPvocCountries(page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val countries = pvocCountriesRepository.findAll(page)
            response.data = PvocPartnerCountryDao.fromList(countries.toList())
            response.totalCount = countries.totalElements
            response.pageNo = countries.number
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "PVOC countries"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to list PVOC countries")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Could not list PVOC countries, please contact administator"
        }
        return response
    }


    fun addPvocRegion(form: PvocPartnerRegionForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val region = PvocPartnersRegionEntity()
            form.fillEntity(region)
            region.createdBy = commonDaoServices.loggedInUserAuthentication().name
            region.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
            region.modifiedOn = Timestamp.from(Instant.now())
            region.createdOn = Timestamp.from(Instant.now())
            response.data = pvocRegionsRepository.save(region)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "PVOC region added"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to add PVOC region", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add PVOC region"
        }
        return response
    }

    fun removePvocRegion(regionId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalRegion = pvocRegionsRepository.findById(regionId)
            if (optionalRegion.isPresent) {
                val region = optionalRegion.get()
                region.deleteBy = commonDaoServices.loggedInUserAuthentication().name
                region.deletedOn = Timestamp.from(Instant.now())
                pvocRegionsRepository.delete(region)
                response.data = region
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Region deleted"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Region does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to delete region", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Region could not be deleted, check its not used by PVOC country; you can disable customs office instead"
        }
        return response
    }

    fun updatePvocRegion(form: PvocPartnerRegionForm, regionId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val optionalRegion = pvocRegionsRepository.findById(regionId)
            if (optionalRegion.isPresent) {
                val region = optionalRegion.get()
                form.fillEntity(region)
                region.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
                region.modifiedOn = Timestamp.from(Instant.now())
                response.data = pvocRegionsRepository.save(region)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Region updated"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "PVOC region does not exits"
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to update PVOC region", e)
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "PVOC region could not be updated; contact administrator"
        }
        return response
    }

    fun listPvocRegions(page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val countries = pvocRegionsRepository.findAll(page)
            response.data = countries.toList()
            response.totalCount = countries.totalElements
            response.pageNo = countries.number
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "PVOC regions"
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Failed to list PVOC regions")
            response.errors = e.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Could not list PVOC regions, please contact administator"
        }
        return response
    }


}