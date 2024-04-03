package com.letar.realetar.Controllers.Order

import com.letar.realetar.Models.PurchaseHeader
import com.letar.realetar.Repositories.PurchaseHeaderRepo
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/purchaseHeaders")
class PurchaseHeaderController(private val purchaseHeaderRepository: PurchaseHeaderRepo) {

    @GetMapping
    fun getAllPurchaseHeaders(): List<PurchaseHeader> {
        return purchaseHeaderRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getPurchaseHeaderById(@PathVariable id: Long): ResponseEntity<PurchaseHeader> {
        val purchaseHeader = purchaseHeaderRepository.findById(id)
        return if (purchaseHeader.isPresent) {
            ResponseEntity.ok(purchaseHeader.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createPurchaseHeader(@RequestBody purchaseHeader: PurchaseHeader): ResponseEntity<PurchaseHeader> {
        val savedPurchaseHeader = purchaseHeaderRepository.save(purchaseHeader)
        return ResponseEntity(savedPurchaseHeader, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updatePurchaseHeader(@PathVariable id: Long, @RequestBody purchaseHeader: PurchaseHeader): ResponseEntity<PurchaseHeader> {
        if (!purchaseHeaderRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        purchaseHeader.id = id
        val updatedPurchaseHeader = purchaseHeaderRepository.save(purchaseHeader)
        return ResponseEntity(updatedPurchaseHeader, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deletePurchaseHeader(@PathVariable id: Long): ResponseEntity<Void> {
        if (!purchaseHeaderRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        purchaseHeaderRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
