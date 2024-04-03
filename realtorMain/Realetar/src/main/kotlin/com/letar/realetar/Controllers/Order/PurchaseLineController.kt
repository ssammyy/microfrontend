package com.letar.realetar.Controllers.Order

import com.letar.realetar.Models.PurchaseLine
import com.letar.realetar.Repositories.PurchaseLineRepo
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/purchaseLines")
class PurchaseLineController(private val purchaseLineRepository: PurchaseLineRepo) {

    @GetMapping
    fun getAllPurchaseLines(): List<PurchaseLine> {
        return purchaseLineRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getPurchaseLineById(@PathVariable id: Long): ResponseEntity<PurchaseLine> {
        val purchaseLine = purchaseLineRepository.findById(id)
        return if (purchaseLine.isPresent) {
            ResponseEntity.ok(purchaseLine.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createPurchaseLine(@RequestBody purchaseLine: PurchaseLine): ResponseEntity<PurchaseLine> {
        val savedPurchaseLine = purchaseLineRepository.save(purchaseLine)
        return ResponseEntity(savedPurchaseLine, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updatePurchaseLine(@PathVariable id: Long, @RequestBody purchaseLine: PurchaseLine): ResponseEntity<PurchaseLine> {
        if (!purchaseLineRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        purchaseLine.id = id
        val updatedPurchaseLine = purchaseLineRepository.save(purchaseLine)
        return ResponseEntity(updatedPurchaseLine, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deletePurchaseLine(@PathVariable id: Long): ResponseEntity<Void> {
        if (!purchaseLineRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        purchaseLineRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
