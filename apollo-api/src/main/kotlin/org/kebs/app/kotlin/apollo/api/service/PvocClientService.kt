package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.request.CocEntityForm
import org.springframework.stereotype.Service

@Service
class PvocClientService {

    fun receiveCoc(coc: CocEntityForm): ApiResponseModel {
        val response=ApiResponseModel()

        return response
    }
}