package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component


@Component
class IdfDataItemsList {
    var idfItems: MutableList<IdfItemsResponse> = ArrayList<IdfItemsResponse>()
}