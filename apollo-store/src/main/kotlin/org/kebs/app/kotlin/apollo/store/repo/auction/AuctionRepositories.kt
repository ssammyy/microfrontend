package org.kebs.app.kotlin.apollo.store.repo.auction

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.auction.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import java.util.*


interface IAuctionCategoryRepository : HazelcastRepository<AuctionCategory, Long> {
    fun findByStatus(status: Int): List<AuctionCategory>
    fun findByCategoryCode(category: String): Optional<AuctionCategory>
}

interface IAuctionRequestsRepository : HazelcastRepository<AuctionRequests, Long> {
    fun findByApprovalStatus(status: Int, page: Pageable): Page<AuctionRequests>
    fun findByApprovalStatusInAndAssignedOfficerIsNull(status: List<Int>, page: Pageable): Page<AuctionRequests>
    fun findByAssignedOfficer(officer: UsersEntity?, page: Pageable): Page<AuctionRequests>
    fun findByAuctionLotNoContains(keyword: String, page: Pageable): Page<AuctionRequests>
}


interface IAuctionItemDetailsRepository : HazelcastRepository<AuctionItemDetails, Long> {
    fun findByAuctionId(auctionId: Long): List<AuctionItemDetails>
}

interface IAuctionRequestHistoryRepository : HazelcastRepository<AuctionRequestHistory, Long> {
    fun findByAuctionIdAndStatus(auctionId: Long,status: Int): List<AuctionRequestHistory>
}

interface IAuctionUploadsEntityRepository : HazelcastRepository<AuctionUploadsEntity, Long> {
    fun findByAuctionId(auctionId: Long): List<AuctionUploadsEntity>
}