/*
 *
 *  $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 *  $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 *  $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 *  $$$$$$$\ |\$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 *  $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 *  $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 *  $$$$$$$  |\$$$$  |$$ | \$\       \$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 *  \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 *  $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 *  \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *     $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *     $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$\
 *     $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *     $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *     $$ |   $$$$$$$$\ \$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$  |$$$$$$\ $$$$$$$$\ \$$$$  |
 *     \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *    Copyright (c) 2020.  BSK
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package org.kebs.app.kotlin.apollo.api.flux.security.service

import kotlinx.coroutines.reactor.mono
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.store.model.UserRoleAssignmentsEntity
import org.kebs.app.kotlin.apollo.store.repo.IUserPrivilegesRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRoleAssignmentsRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomUserDetailsService(
    private val userRepository: IUserRepository,
    private val assignmentsRepository: IUserRoleAssignmentsRepository,
    private val authoritiesRepo: IUserPrivilegesRepository,
    private val authenticationProperties: AuthenticationProperties
) : ReactiveUserDetailsService {


    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        userRepository.findByUserName(username)
            ?.let { user ->
                user.id
                    ?.let {
                        assignmentsRepository.findUserRoleAssignments(it, 1)
                            ?.let { roleAssignments ->
                                val authorities = getAuthorities(roleAssignments)
                                User(
                                    user.userName,
                                    user.credentials,
                                    user.enabled > 0,
                                    user.accountExpired <= 0,
                                    user.credentialsExpired <= 0,
                                    user.accountLocked <= 0,
                                    authorities

                                )

                            }
                            ?: throw InvalidValueException("Login Failed $username not authorized")
                    }
                    ?: throw NullValueNotAllowedException("Invalid User Details")


            }
            ?: throw UsernameNotFoundException("Login Failed $username not found")


    }


    private fun getAuthorities(roles: List<UserRoleAssignmentsEntity>): MutableCollection<GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        return if (roles.isEmpty()) {

            mutableListOf(SimpleGrantedAuthority(authenticationProperties.defaultRole))

        } else {
            val rolesMap = roles.map { it.roleId }
            authoritiesRepo.findAuthoritiesList(rolesMap, 1)
                ?.map { it.name }
                ?.distinct()
                ?.map { authorities.add(SimpleGrantedAuthority(it)) }
            return authorities
        }
    }


}
