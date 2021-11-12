package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.kebs.app.kotlin.apollo.store.model.StagingStandardsLevyManufacturerPenalty
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.stdLevy.ManufacturePenaltyDetailsDTO
import org.springframework.stereotype.Service

@Service
class StandardLevyService(
    private val iStagingStandardsLevyManufacturerPenaltyRepository: IStagingStandardsLevyManufacturerPenaltyRepository
) {

    fun getManufacturerPenaltyHistory(): MutableIterable<StagingStandardsLevyManufacturerPenalty>
    {
        return iStagingStandardsLevyManufacturerPenaltyRepository.findAll()
    }
}