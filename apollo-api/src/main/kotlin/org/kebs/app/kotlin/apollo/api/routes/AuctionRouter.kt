package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.di.AuctionHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.router

@Component
class AuctionRouter {

    fun auctionService(handler: AuctionHandler) = router {
        "/api/v1/auction".let {
            GET("categories", handler::listAuctionCategory)
        }
    }
}