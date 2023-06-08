package org.kebs.app.kotlin.apollo.api.payload

import org.flowable.idm.engine.impl.persistence.entity.UserEntity
import org.kebs.app.kotlin.apollo.store.model.UserProfilesEntity

class UsersDao {
    var firstName: String? = null
    var lastName: String? = null
    var displayName: String? = null
    var emailAddress: String? = null
    var id: String? = null

    companion object {
        fun fromEntity(entity: UserEntity) = UsersDao().apply {
            firstName = entity.firstName
            lastName = entity.lastName
            displayName = entity.displayName
            id = entity.id
            emailAddress = entity.email
        }

        fun fromList(entities: List<UserEntity>): List<UsersDao> {
            val userList = mutableListOf<UsersDao>()
            entities.forEach {
                userList.add(fromEntity(it))
            }
            return userList
        }
    }
}

class UserProfileDao {
    var firstName: String? = null
    var lastName: String? = null
    var displayName: String? = null
    var emailAddress: String? = null
    var id: Long? = null
    var profileId: Long?=null

    companion object {
        fun fromEntity(entity: UserProfilesEntity) = UserProfileDao().apply {
            firstName = entity.userId?.firstName
            lastName = entity.userId?.lastName
            displayName = entity.userId?.userName
            id = entity.userId?.id
            profileId=entity.id
            emailAddress = entity.userId?.email
        }

        fun fromList(entities: List<UserProfilesEntity>): List<UserProfileDao> {
            val userList = mutableListOf<UserProfileDao>()
            entities.forEach {
                userList.add(fromEntity(it))
            }
            return userList
        }
    }
}