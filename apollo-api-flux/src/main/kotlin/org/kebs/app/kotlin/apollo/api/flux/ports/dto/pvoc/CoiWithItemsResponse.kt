package org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc

import org.kebs.app.kotlin.apollo.store.model.CocContainersEntity
import org.kebs.app.kotlin.apollo.store.model.CoisEntity

class CoiWithItemsResponse {
    var coiData: CoisEntity? = null
    var coiItems: List<CocContainersEntity>? = null
}