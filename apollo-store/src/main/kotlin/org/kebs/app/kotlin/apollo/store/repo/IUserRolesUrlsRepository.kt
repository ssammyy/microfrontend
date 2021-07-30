package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SidebarChildrenEntity
import org.kebs.app.kotlin.apollo.store.model.SidebarMainEntity
import org.kebs.app.kotlin.apollo.store.model.UserRolesEntity
import org.kebs.app.kotlin.apollo.store.model.UserRolesUrlsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IUserRolesUrlsRepository : HazelcastRepository<UserRolesUrlsEntity, Long> {
    fun findFirst5ByRoleId(roleId: UserRolesEntity): List<UserRolesUrlsEntity?>
}

interface ISidebarMainRepository : HazelcastRepository<SidebarMainEntity, Long>
interface ISidebarChildrenRepository : HazelcastRepository<SidebarChildrenEntity, Long>