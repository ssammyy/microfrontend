package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component

@Component
class GeneralResponse {
    var responseCode: Int? =0
    var responseMessage : String? =null

}