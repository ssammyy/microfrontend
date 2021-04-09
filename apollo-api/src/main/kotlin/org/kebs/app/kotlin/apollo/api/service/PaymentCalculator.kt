package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class FuelInspectionCalculator() {
    fun calculateFuelInspectionCost(fuelInspectionEntity: PetroleumInstallationInspectionEntity) : MutableList<BigDecimal?> {
        val m = mutableListOf<BigDecimal?>()
        var tax: BigDecimal? = null
        val stagingAmountToPay: BigDecimal? = 30000.toBigDecimal()
        tax = stagingAmountToPay?.let { 0.16.toBigDecimal().times(it) }
        val amtToPay: BigDecimal? = tax?.let { stagingAmountToPay?.plus(it) }

        m.add(amtToPay)
        m.add(stagingAmountToPay)
        m.add(tax)

        return m
    }

}

@Component
class PaymentCalculator(
        private val iPermitRepository: IPermitRepository,
        private val iTurnOverRatesRepository: ITurnOverRatesRepository,
        private val manufacturersRepo: IManufacturerRepository,
        private val manufacturerAddressesEntityRepo: IManufacturerAddressRepository,
        private val iBrandPerSiteRepository: IBrandPerSiteRepository,
        private val iManufacturerProductRepository: IManufacturerProductRepository,
        private val iManufacturerProductBrandRepository: IManufacturerProductBrandRepository,
        private val iManufacturerBranchRepository: IManufacturerBranchRepository,
        private val iManufacturePaymentDetailsRepository: IManufacturerPaymentDetailsRepository,
        private val sampleStandardsRepository: ISampleStandardsRepository,
        private val paymentUnitsRepository: ICfgKebsPermitPaymentUnitsRepository
)
{

    fun calculatePayment(permit: PermitApplicationEntity): MutableList<BigDecimal?> {
//        val manufactureId = 100L
        KotlinLogging.logger {  }.info { "ManufacturerId, ${permit.manufacturer}" }
        val manufactureId = permit.manufacturer
//        val manufactureTurnOver: BigDecimal? = manufactureId.let { it?.let { it1 -> iManufacturePaymentDetailsRepository.findByManufacturerIdAndStatus(it1, 1)?.turnOverAmount } }
        val manufactureTurnOver = permit.manufacturer?.let { iManufacturePaymentDetailsRepository.findByManufacturerIdAndStatus(it, 1)?.turnOverAmount }
        KotlinLogging.logger {  }.info { manufactureTurnOver }
        var amountToPay: BigDecimal? = null
        var taxAmount: BigDecimal? = null

        val m = mutableListOf<BigDecimal?>()
        var fmarkCost: BigDecimal? = null
        val paymentUnits = paymentUnitsRepository.findByIdOrNull(2)
        KotlinLogging.logger {  }.info { paymentUnits?.standardStandardCost }
        var fmark: BigDecimal? = null
        if (manufactureTurnOver != null) {

            if (manufactureTurnOver > iTurnOverRatesRepository.findByIdAndFirmType(1, "Large firms")?.lowerLimit) {
                val applicationCost: BigDecimal? = paymentUnits?.standardApplicationCost?.toBigDecimal()
                val noOf = permit.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it)?.noOfPages }
//                val noOf = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory?.id)?.noOfPages))
                val standardCost: BigDecimal? = (paymentUnits?.standardStandardCost?.times(noOf!!))?.toBigDecimal()
//                val inspectionCost: BigDecimal? = permit.noOfSitesProducingTheBrand?.let { paymentUnits?.standardInspectionCost?.times(it)?.toBigDecimal() }
                val inspectionCost: BigDecimal? = paymentUnits?.standardInspectionCost?.toBigDecimal()
                var stgAmt: BigDecimal? = null
                if (permit.product == 61L) {
                    stgAmt = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }?.let { 2.toBigDecimal().times(it) }
                    fmark = 1.toBigDecimal()
                    fmarkCost = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                    taxAmount = stgAmt?.let { 0.16.toBigDecimal().times(it) }
                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                } else{
                    KotlinLogging.logger {  }.info { "second loop, ${permit.product}" }
                    stgAmt = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                    fmark = 0.toBigDecimal()
                    taxAmount = stgAmt?.let { 0.16.toBigDecimal().times(it) }
                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                }
//                amountToPay = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                KotlinLogging.logger {  }.info { "Manufacturer turnover, $manufactureTurnOver" }
                KotlinLogging.logger { }.info { "Total Amount To Pay   = " + amountToPay?.toDouble() }

                m.add(standardCost)
                m.add(inspectionCost)
                m.add(applicationCost)
                m.add(amountToPay)
                m.add(fmark)
                m.add(fmarkCost)
                m.add(taxAmount)

            } else{
                KotlinLogging.logger {  }.info { "Turnover is less than 500000" }
            }
//            else if (manufactureTurnOver < iTurnOverRatesRepository.findByIdAndFirmType(2, "Medium Enterprises")?.upperLimit && manufactureTurnOver > iTurnOverRatesRepository.findByIdAndFirmType(2, "Medium Enterprises")?.lowerLimit) {
//                fixAmountToPay = iTurnOverRatesRepository.findByIdAndFirmType(2, "Medium Enterprises")?.fixedAmountToPay
//                variableAmountToPay = iTurnOverRatesRepository.findByIdAndFirmType(2, "Medium Enterprises")?.variableAmountToPay
//                if (totalProducts != null) {
//                    if (totalProducts > 3) {
//                        val remProduct: Int? = totalProducts.minus(3)
//                        amountToPay = (remProduct?.toBigDecimal()?.multiply(variableAmountToPay))?.add(fixAmountToPay)
//                        KotlinLogging.logger { }.info { "Total Amount To Pay for Medium Enterprises = " + remProduct + " =" + amountToPay?.toDouble() }
//                    } else {
//                        amountToPay = fixAmountToPay
//                        KotlinLogging.logger { }.info { "Total Amount To Pay for Medium Enterprises = " + amountToPay?.toDouble() }
//                    }
//                }


            }else {
            KotlinLogging.logger {  }.info { "Turnover is less than 500000" }
        }
//        else if (manufactureTurnOver < iTurnOverRatesRepository.findByIdAndFirmType(3, "Jua kali and small Enterprises")?.upperLimit) {
//                fixAmountToPay = iTurnOverRatesRepository.findByIdAndFirmType(3, "Jua kali and small Enterprises")?.fixedAmountToPay
//                variableAmountToPay = iTurnOverRatesRepository.findByIdAndFirmType(3, "Jua kali and small Enterprises")?.variableAmountToPay
//                if (totalProducts != null) {
//                    if (totalProducts > 3) {
//                        val remProduct: Int? = totalProducts.minus(3)
//                        amountToPay = (remProduct?.toBigDecimal()?.multiply(variableAmountToPay))?.add(fixAmountToPay)
//                        KotlinLogging.logger { }.info { "Total Amount To Pay For Jua kali and small Enterprises  = " + remProduct + " =" + amountToPay?.toDouble() }
//                    } else {
//                        amountToPay = fixAmountToPay
//                        KotlinLogging.logger { }.info { "Total Amount To Pay For Jua kali and small Enterprises  = " + amountToPay?.toDouble() }
//                    }
//                }
//            }
//
//        }
        /**
        //         * Save the payment Details
        //         */
//        val myPermit = permit
//        with(myPermit) {
//            totalAmountToPay = amountToPay.toString()
//            KotlinLogging.logger { }.info { "Total Amount To Pay and save = " + amountToPay?.toDouble() }
//        }
//        iPermitRepository.save(permit)
    return m
}

}