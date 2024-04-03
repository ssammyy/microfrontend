package com.ngumo.inventoryapi.Services

import com.ngumo.inventoryapi.DTOs.SaleDto
import com.ngumo.inventoryapi.Dao.Models.Debt
import com.ngumo.inventoryapi.Dao.Models.Sale
import com.ngumo.inventoryapi.Dao.Models.SaleLines
import com.ngumo.inventoryapi.Dao.Repositories.InventoryRepository
import com.ngumo.inventoryapi.Dao.Repositories.SaleLinesRepository
import com.ngumo.inventoryapi.Dao.Repositories.SaleRepository
import com.ngumo.inventoryapi.Dao.Repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class SaleService(
       val saleRepository: SaleRepository,
        val userRepository: UserRepository,
        val inventoryRepository: InventoryRepository,
        val debtRepository: InventoryRepository,
        val saleLinesRepository: SaleLinesRepository
) {

    @Transactional
    fun registerSale(userObject: UserDetails, saleDto: SaleDto): Any {
        val thisUser = userRepository.findByUsername(userObject.username)?: throw IllegalArgumentException("User not found")

        try {
            if (saleDto.isCredit == true) {
                val newDebt = Debt().apply {
                    referenceCode = saleDto.referenceCode
                    customerTel = saleDto.customerName
                    customerName = saleDto.customerName
                    remainingDebt = saleDto.creditAmount
                    initialDebt = saleDto.creditAmount
                    status = 0
                    user= thisUser
                    proposedDateOfPayment = saleDto.proposedDateOfPayment
                }
            }
            val newSale = Sale().apply{
                referenceCode = saleDto.referenceCode
                cartQuantity = saleDto.items.size .toLong()
                saleTotal = saleDto.saleTotal
                paymentMethod = saleDto.paymentMethod
                isCleared = saleDto.isCleared
                isCredit = saleDto.isCredit
                user = thisUser
                customerName = saleDto .customerName
                customerTel= saleDto.customerTel
                creditAmount = saleDto.creditAmount

            }
            saleRepository.save(newSale)

            for(record in saleDto.items){
                record.id?.let { record.newQuan?.let { it1 -> updateInventory(it, it1) } }

                val saleLine = SaleLines().apply{
                    sale = saleDto.referenceCode?.let {
                        saleRepository.findByReferenceCode(it)
                    }
                    inventory = record.id?.let{
                        inventoryRepository.findById(it).get()
                    }
                    referenceCode = saleDto.referenceCode
                    itemName = record.itemName
                    itemPrice = record.amount
                    quantity = record.newQuan


                }
                saleLinesRepository.save(saleLine)

            }

            return ResponseEntity.ok(newSale);
        }catch (e: Exception){
            e.printStackTrace()
            throw e
        }

    }

    fun updateInventory(itemId: Long, itemQuantity: Long){
        val optionalEntity= inventoryRepository.findById(itemId)
        if (optionalEntity.isPresent) {
            val item = optionalEntity.get()
            item.quantity = item.quantity?.minus(itemQuantity)
            inventoryRepository.save(item)
        } else {
            throw EntityNotFoundException("Item with ID $itemId not found")
        }

    }
}