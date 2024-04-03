package com.letar.realetar.Controllers.Vendors

import com.letar.realetar.DataClasses.VendorsDto
import com.letar.realetar.Models.Vendor
import com.letar.realetar.Services.VendorService
import com.letar.realetar.Utilities.Logging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vendors")
class VendorController(private val vendorService: VendorService,
    ) {

    @GetMapping
    fun getAllVendors(): List<Vendor> {

        return vendorService.getAllVendors()
    }

    @GetMapping("/{id}")
    fun getVendorById(@PathVariable id: Long): ResponseEntity<Vendor> {
        val vendor = vendorService.getVendorById(id)
        return if (vendor != null) {
            ResponseEntity.ok(vendor)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createVendor(@RequestBody vendor: VendorsDto.CreateVendorRequest): Vendor {
        return vendorService.createVendor(vendor)
    }

//    @PutMapping("/{id}")
//    fun updateVendor(@PathVariable id: Long, @RequestBody vendor: Vendor): ResponseEntity<Vendor> {
//        val existingVendor = vendorService.getVendorById(id)
//        return if (existingVendor != null) {
//            val updatedVendor = vendor.copy(id = id)
//            ResponseEntity.ok(vendorService.updateVendor(updatedVendor))
//        } else {
//            ResponseEntity.notFound().build()
//        }
//    }

    @DeleteMapping("/{id}")
    fun deleteVendor(@PathVariable id: Long): ResponseEntity<Void> {
        val vendor = vendorService.getVendorById(id)
        return if (vendor != null) {
            vendorService.deleteVendor(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
