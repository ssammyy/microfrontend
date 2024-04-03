package com.ngumo.inventoryapi.Controllers.Inventory

import com.ngumo.inventoryapi.Dao.Models.Inventory
import com.ngumo.inventoryapi.Dao.Repositories.InventoryRepository
import org.springframework.beans.factory.annotation.Required
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.awt.print.Pageable


@RestController
@RequestMapping("api/v1/inventory")
class Inventory(
    private val inventoryRepository: InventoryRepository
) {
    @PostMapping("/get-items")
    fun getItems(@RequestBody page : pages): Page<Inventory> {
        val pageable: PageRequest = PageRequest.of(page.pageNumber, page.pageSize)
        return inventoryRepository.findAll(pageable)
    }

    @GetMapping("/get-all-items")
    fun getAllItems(): List<Inventory>{
        return inventoryRepository.findAll()
    }


    data class pages(
        val pageSize: Int = 0,
        val pageNumber: Int = 0
    )

}