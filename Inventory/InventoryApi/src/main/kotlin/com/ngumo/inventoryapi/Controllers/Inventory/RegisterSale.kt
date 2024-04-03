package com.ngumo.inventoryapi.Controllers.Inventory

import com.ngumo.inventoryapi.DTOs.SaleDto
import com.ngumo.inventoryapi.Dao.Models.Sale
import com.ngumo.inventoryapi.Services.SaleService
import com.ngumo.inventoryapi.Utils.Commons
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/inventory")
class RegisterSale(
        val saleService: SaleService,
        val userDetailsService: UserDetailsService,
        val commons: Commons

) {


    @PostMapping("/sale")
    fun stageSale(@RequestBody saleDto: SaleDto): Any {
        val user = commons.getLoggedInUser();
        return saleService.registerSale(user, saleDto);
    }

}