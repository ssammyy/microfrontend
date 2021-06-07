package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.repo.IUserPrivilegesRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRoleAssignmentsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRolesPrivilegesRepository
import org.springframework.stereotype.Service

@Service
class UserRolesService(
    private val privilegesRepository: IUserPrivilegesRepository,
    private val rolesPrivilegesRepository: IUserRolesPrivilegesRepository,
    private val userRolesAssignmentsRepository: IUserRoleAssignmentsRepository,
    private val userRepo: IUserRepository

    ) {

    fun findAllUserWhoHaveRole(roleId: Long) : List<UsersEntity>?{
        val userIds  = mutableListOf<Long>()
        userRolesAssignmentsRepository.findByRoleId(roleId).let { users ->
            KotlinLogging.logger {  }.info { "UserIds count from role id ==> "+users.count() }
            users.forEach { user ->
                user.userId?.let { userIds.add(it) }
            }
            KotlinLogging.logger {  }.info { "UserIds count after loop ==> "+userIds.count() }
            userIds.let { userRepo.findAllByIdIn(it).let { usersReturned ->
                KotlinLogging.logger {  }.info { "UserIds count from repo loop ==> "+usersReturned?.count() }
                return usersReturned
            } }
        }

//        KotlinLds?.count() }ogging.logger {  }.info { "UserIds count ==> "+userI


    }

    fun getUserId(privilegeName: String) : Long? {

        privilegesRepository.findByName(privilegeName).let { priviledge ->
            KotlinLogging.logger {  }.info { "Pleviledge id "+priviledge.id }
            rolesPrivilegesRepository.findByPrivilege(priviledge).let { items ->
                items?.get(0)?.userRoles?.id?.let {
                    userRolesAssignmentsRepository.findByRoleId(it).let { userIds ->
                        KotlinLogging.logger {  }.info { "User id is ==> "+ userIds.get(0).userId }
                        return userIds.get(0).userId
                    }
                }?: throw Exception("No roles exist for you")
            }
        }
    }
}