package org.kebs.app.kotlin.apollo.api.controllers.admin

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.admin.AdminDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.store.model.BusinessLinesEntity
import org.kebs.app.kotlin.apollo.store.model.BusinessNatureEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/migration/")
class AdminController (
    val adminDaoServices: AdminDaoServices
        )
{

    @GetMapping("admin/getAllBusinessLines")
    fun getAllBusinessLines(): MutableIterable<BusinessLinesEntity> {
        return adminDaoServices.getAllBusinessLines()
    }

    @GetMapping("admin/getAllBusinessNature")
    fun getAllBusinessNature(): MutableIterable<BusinessNatureEntity> {
        return adminDaoServices.getAllBusinessNature()
    }

    @PostMapping("admin/createBusinessLine")
    fun createBusinessLine(@RequestBody businessLinesEntity: BusinessLinesEntity): ServerResponse
    {
        return ServerResponse(
            HttpStatus.OK,
            "Upload Product Category",
            adminDaoServices.createBusinessLine(businessLinesEntity)
        )
    }

    @PostMapping("admin/createBusinessNature")
    fun createBusinessNature(@RequestBody businessNatureEntity: BusinessNatureEntity): ServerResponse
    {
        return ServerResponse(
            HttpStatus.OK,
            "Upload Product Category",
            adminDaoServices.createBusinessNature(businessNatureEntity)
        )
    }



}
