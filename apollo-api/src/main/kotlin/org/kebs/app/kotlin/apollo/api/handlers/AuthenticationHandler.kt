/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.common.dto.JwtResponse
import org.kebs.app.kotlin.apollo.common.dto.LoginRequest
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.repo.ICompanyProfileRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.http.server.ServletServerHttpRequest

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Component
class AuthenticationHandler(
    private val tokenService: JwtTokenService,
    private val authenticationManager: AuthenticationManager,
    private val commonDaoServices: CommonDaoServices,
    private val usersRepo: IUserRepository,
    private val companyRepo: ICompanyProfileRepository
) {


    fun home(req: ServerRequest): ServerResponse = ServerResponse.ok().render("index", req.attributes())

    fun login(req: ServerRequest): ServerResponse = ServerResponse.ok().render("auth/login", req.attributes())

    fun uiLogin(req: ServerRequest): ServerResponse =
        try {
            val loginRequest = req.body<LoginRequest>()
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
            authentication
                ?.let { auth ->
                    SecurityContextHolder.getContext().authentication = auth

                    usersRepo.findByUserName(auth.name)
                        ?.let { user ->
                            val request = ServletServerHttpRequest(req.servletRequest())
                            val token = tokenService.tokenFromAuthentication(
                                auth,
                                commonDaoServices.concatenateName(user),
                                request
                            )

                            val roles = tokenService.extractRolesFromToken(token)?.map { it.authority }
                            val response = JwtResponse(
                                token,
                                user.id,
                                user.userName,
                                user.email,
                                commonDaoServices.concatenateName(user),
                                roles
                            ).apply {
//                                companyID = user.companyId
//                                branchID = user.plantId
//                                turnover =
//                                    commonDaoServices.findCompanyProfileWithID(user.companyId ?: -2L).yearlyTurnover
                            }
                            ServerResponse.ok().body(response)
                        }
                        ?: throw NullValueNotAllowedException("Empty authentication after authentication attempt")
                }
                ?: throw NullValueNotAllowedException("Empty authentication after authentication attempt")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

}
