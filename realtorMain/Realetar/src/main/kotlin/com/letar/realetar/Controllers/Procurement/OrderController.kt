package com.letar.realetar.Controllers.Procurement

import com.letar.realetar.Models.Items
import com.letar.realetar.Services.ItemsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/realetar/items")
class OrderController(
    private val itemsService: ItemsService
) {
    @GetMapping
    fun getAllItems(): List<Items> {
        return itemsService.getAllItems()
    }

    @GetMapping("/{id}")
    fun getItemsById(@PathVariable id: Long): ResponseEntity<Items> {
        val items = itemsService.getItemsById(id)
        return if (items != null) {
            ResponseEntity.ok(items)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createItems(@RequestBody items: Items): Items {
        return itemsService.createItems(items)
    }

    @PutMapping("/{id}")
    fun updateItems(
        @PathVariable id: Long,
        @RequestBody items: Items
    ): ResponseEntity<Items> {
        val existingItems = itemsService.getItemsById(id)
        return if (existingItems != null) {
            val updatedItems = itemsService.updateItems(items)
            ResponseEntity.ok(updatedItems)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(Items())
        }
    }

    @DeleteMapping("/{id}")
    fun deleteItems(@PathVariable id: Long): ResponseEntity<Void> {
        val existingItems = itemsService.getItemsById(id)
        return if (existingItems != null) {
            itemsService.deleteItems(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }


}