package org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc

import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CoiItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CoisEntity

class CoiWithItemsResponse {
    var coiData : CoisEntity? = null
    var coiItems : List<CoiItemsEntity> ? = null
}