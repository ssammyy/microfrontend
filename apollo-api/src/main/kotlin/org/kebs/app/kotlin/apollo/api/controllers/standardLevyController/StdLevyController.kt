package org.kebs.app.kotlin.apollo.api.controllers.standardLevyController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.StandardLevyService
import org.kebs.app.kotlin.apollo.store.model.StagingStandardsLevyManufacturerPenalty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/migration/stdLevy/")
class StdLevyController(
    private val standardLevyService: StandardLevyService
) {

    @GetMapping("/getManufacturerPenaltyHistory")
    fun getManufacturerPenaltyHistory():MutableIterable<StagingStandardsLevyManufacturerPenalty>
    {
        return standardLevyService.getManufacturerPenaltyHistory()
    }
}