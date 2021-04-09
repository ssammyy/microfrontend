package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class RfcCoiItemsDataList {
    var coiItems: MutableList<RfcCoiItemsResponse> = ArrayList<RfcCoiItemsResponse>()
}