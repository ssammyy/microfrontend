package org.kebs.app.kotlin.apollo.api.handlers.di

import org.kebs.app.kotlin.apollo.api.service.AuctionService
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class AuctionHandler(
        private val validationService: DaoValidatorService,
        private val auctionService: AuctionService
) {
    fun listAuctionCategory(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(auctionService.listAuctionCategories())
    }
}