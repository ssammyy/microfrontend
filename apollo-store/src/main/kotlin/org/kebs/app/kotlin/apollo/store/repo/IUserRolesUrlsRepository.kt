package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SidebarChildrenEntity
import org.kebs.app.kotlin.apollo.store.model.SidebarMainEntity
import org.kebs.app.kotlin.apollo.store.model.UserRolesEntity
import org.kebs.app.kotlin.apollo.store.model.UserRolesUrlsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query

interface IUserRolesUrlsRepository : HazelcastRepository<UserRolesUrlsEntity, Long> {
    fun findFirst5ByRoleId(roleId: UserRolesEntity): List<UserRolesUrlsEntity?>
}

interface ISidebarMainRepository : HazelcastRepository<SidebarMainEntity, Long> {

    @Query(
        "SELECT * from CFG_SIDEBAR_MAIN where ROLE_ID in (:authorizationIds)  and STATUS = :status",
        nativeQuery = true
    )
    fun findUsersSideBarMenus(authorizationIds: List<Long>, status: Int): List<SidebarMainEntity>?
}

interface ISidebarChildrenRepository : HazelcastRepository<SidebarChildrenEntity, Long> {
    fun findAllByMainId(mainId: Long): List<SidebarChildrenEntity>?
}