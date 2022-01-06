package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.di.AuctionHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Component
class AuctionRouter {

    @CrossOrigin
    @Bean
    fun auctionServiceRouter(handler: AuctionHandler) = router {
        "/api/v1/auction".let {
            GET("/categories", handler::listAuctionCategory)
            GET("/auctions/{auctionType}", handler::listAuctions)
            POST("/auction/add", handler::addAuctionRequest)
            GET("/auction/{auctionId}", handler::findAuctionGoodById)
            POST("/auction/assign/{auctionId}", handler::assignAuctionRequest)
            POST("/auction/generate/demand-note/{auctionId}", handler::generateDemandNoteRequest)
            POST("/auction/approve-reject/{auctionId}", handler::updateAuctionStatus)
        }
    }
}