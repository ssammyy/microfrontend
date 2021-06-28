package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.SystemsAdminDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.badRequest
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull


@Service
class SystemsAdministrationHandler(
    private val daoService: SystemsAdminDaoService,
    applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
) : AbstractValidationHandler() {
    final val appId: Int = applicationMapProperties.mapUserRegistration

    @PreAuthorize("hasAuthority('RBAC_USER_ROLES_VIEW')")
    fun rbacUserRoles(req: ServerRequest) = try {
        ok().render("admin/rbac-user-roles")
    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message)
        KotlinLogging.logger { }.debug(e.message, e)
        badRequest().body(e.message ?: "Unknown Error")
    }

    @PreAuthorize("hasAuthority('RBAC_USER_ROLES_VIEW')")
    fun rbacUserRequests(req: ServerRequest) = try {
        ok().render("admin/rbac-user-requests")
    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message)
        KotlinLogging.logger { }.debug(e.message, e)
        badRequest().body(e.message ?: "Unknown Error")
    }

    @PreAuthorize("hasAuthority('RBAC_ROLE_AUTHORITIES_VIEW')")
    fun rbacRoleAuthorities(req: ServerRequest) = try {
        ok().render("admin/rbac-role-authorities")
    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message)
        KotlinLogging.logger { }.debug(e.message, e)
        badRequest().body(e.message ?: "Unknown Error")
    }

    @PreAuthorize("hasAuthority('USERS_VIEW')")
    fun usersCrud(req: ServerRequest) = try {
        ok().render("admin/users-crud")
    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message)
        KotlinLogging.logger { }.debug(e.message, e)
        badRequest().body(e.message ?: "Unknown Error")
    }

    @PreAuthorize("hasAuthority('ROLES_VIEW')")
    fun rolesCrud(req: ServerRequest) = try {
        ok().render("admin/roles-crud")
    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message)
        KotlinLogging.logger { }.debug(e.message, e)
        badRequest().body(e.message ?: "Unknown Error")
    }

    @PreAuthorize("hasAuthority('AUTHORITIES_VIEW')")
    fun authoritiesCrud(req: ServerRequest) = try {
        ok().render("admin/authorities-crud")
    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message)
        KotlinLogging.logger { }.debug(e.message, e)
        badRequest().body(e.message ?: "Unknown Error")
    }

    @PreAuthorize("hasAuthority('TITLES_VIEW')")
    fun titlesCrud(req: ServerRequest) = try {
        ok().render("admin/titles-crud")
    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message)
        KotlinLogging.logger { }.debug(e.message, e)
        badRequest().body(e.message ?: "Unknown Error")
    }

    @PreAuthorize("hasAuthority('USER_TYPES_VIEW')")
    fun userTypesCrud(req: ServerRequest) = try {
        ok().render("admin/user-types-crud")
    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message)
        KotlinLogging.logger { }.debug(e.message, e)
        badRequest().body(e.message ?: "Unknown Error")
    }

    @PreAuthorize("hasAuthority('SYSADMIN_VIEW')")
    fun sysadminHome(req: ServerRequest): ServerResponse {
        req.attributes()["authoritiesListLink"] = "/api/system/admin/security/authorities/load"
        req.attributes()["usersListViewLink"] = "/api/system/admin/security/users/load"
        req.attributes()["rolesListLinkLink"] = "/api/system/admin/security/roles/load"
        req.attributes()["authoritiesListLink"] = "/api/system/admin/security/authorities/load"
        req.attributes()["titlesListLink"] = "/api/system/admin/security/authorities/load"
        req.attributes()["userTypesListLink"] = "/api/system/admin/security/authorities/load"

        req.attributes()["getLink"] = "/api/system/admin/security/authorities"
        req.attributes()["rolesPostLink"] = "/api/system/admin/security/roles/"
        req.attributes()["listLink"] = "/api/system/admin/security/authorities/list"
//        req.attributes()["titles"] = daoService.listTitles(1)
//        req.attributes()["userTypes"] = daoService.listUserTypes(1)

        return ok().render("admin/systems-admin-main")
    }

    @PreAuthorize("hasAuthority('USERS_LIST')")
    fun listActiveRbacUsers(req: ServerRequest): ServerResponse {
        return try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    daoService.listRbacUsersByStatus(status)
                        .let { ok().body(it) }
                }
                ?: throw InvalidValueException("Valid value for status required")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")

        }
    }

    fun listActiveRbacUserRoles(req: ServerRequest): ServerResponse {
        return try {

            req.pathVariable("userId").toLongOrNull()
                ?.let { userId ->
                    req.pathVariable("status").toIntOrNull()
                        ?.let { status ->
                            daoService.listRbacRolesByUsersIdAndByStatus(userId, status)
                                ?.let { ok().body(it) }
                        }
                        ?: throw InvalidValueException("Valid value for status required")

                }
                ?: throw InvalidValueException("Valid value for UserId required")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")

        }
    }

    fun listActiveRbacRoles(req: ServerRequest): ServerResponse {
        try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    daoService.listRbacRolesByStatus(status)
                        ?.let { return ok().body(it) }
                }
                ?: throw InvalidValueException("Valid value for status required")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")

        }

    }


    @PreAuthorize("hasAuthority('ROLES_LIST')")
    fun roleListing(req: ServerRequest): ServerResponse {


        val s: String = try {
            req.pathVariable("status")
        } catch (e: Exception) {
            ""
        }
        try {
            s.toIntOrNull()
                ?.let { roles ->
                    daoService.listAllRolesByStatus(roles)
                        ?.let { return ok().body(it) }
                        ?: throw NullValueNotAllowedException("No Roles found")
                }
                ?: run {
                    daoService.listAllRoles()
                        ?.let { return ok().body(it) }
                        ?: throw NullValueNotAllowedException("No Roles found")
                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('ROLES_WRITE')")
    fun roleUpdate(req: ServerRequest): ServerResponse {
        try {
            val role = req.body<RolesEntityDto>()

            daoService.updateRole(role)?.let {
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('ROLES_WRITE')")
    fun roleCreate(req: ServerRequest): ServerResponse {
        try {
            val role = req.body<RolesEntityDto>()
            daoService.updateRole(role)?.let {
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }

    }


    @PreAuthorize("hasAuthority('USERS_LIST')")
    fun usersListing(req: ServerRequest): ServerResponse {
        try {
            var page = req.paramOrNull("page")?.toIntOrNull() ?: 1
            when {
                page < 1 -> page = 1
            }
            val records = req.paramOrNull("records")?.toIntOrNull() ?: 20
            daoService.listUsers(page, records)
                ?.let { return ok().body(it) }
                ?: throw NullValueNotAllowedException("No users found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('USERS_LIST')")
    fun listUserRequests(req: ServerRequest): ServerResponse {
        try {
            //Todo: Ask ken why put a 1 instead of 0
            var page = req.paramOrNull("page")?.toIntOrNull() ?: 0
            when {
                page < 0 -> page = 0
            }
            val records = req.paramOrNull("records")?.toIntOrNull() ?: 20
            daoService.listUsersRequest(page, records)
                ?.let { return ok().body(it) }
                ?: throw NullValueNotAllowedException("No users Request found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun userDetails(req: ServerRequest): ServerResponse {
        return try {
            val userID = req.paramOrNull("userID") ?: throw ExpectedDataNotFound("Required USER ID, check config")
            val userDetails = daoService.getUserDetails(userID.toLong())
            ok().body(userDetails)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

    }

    fun userView(req: ServerRequest): ServerResponse {
        try {
            val user = daoService.getUser(req.pathVariable("id")) ?: UsersEntity()

            req.attributes()["content"] = user
            req.attributes()["heading"] = "USERS LISTING"
            req.attributes()["postLink"] = "/api/system/admin/security/users/save"
            req.attributes()["getLink"] = "/api/system/admin/security/users"

            return ok().render("fragments/security-crud :: user-form-view", req.attributes())
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }

    }


    @PreAuthorize("hasAuthority('AUTHORITY_WRITE')")
    fun authorityUpdate(req: ServerRequest): ServerResponse {
        try {
            val role = req.body<AuthoritiesEntityDto>()

            daoService.updateAuthorities(role)?.let {
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('AUTHORITY_WRITE')")
    fun authorityCreate(req: ServerRequest): ServerResponse {
        try {
            val role = req.body<AuthoritiesEntityDto>()

            daoService.updateAuthorities(role)?.let {
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('AUTHORITY_LIST')")
    fun authoritiesListing(req: ServerRequest): ServerResponse {
        try {
            val s: String = try {
                req.pathVariable("status")
            } catch (e: Exception) {
                ""
            }
            s.toIntOrNull()
                ?.let { status ->
                    daoService.listAllAuthoritiesByStatus(status)
                        ?.let { return ok().body(it) }
                        ?: throw NullValueNotAllowedException("No Authorities found")
                }
                ?: run {
                    daoService.listAllAuthorities()
                        ?.let { return ok().body(it) }
                        ?: throw NullValueNotAllowedException("No Authorities found")

                }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('RBAC_ASSIGN_CFS')")
    fun assignCfsToUser(req: ServerRequest): ServerResponse {
        return try {
            req.pathVariable("userProfileId").toLongOrNull()
                ?.let { userProfileId ->
                    req.pathVariable("cfsId").toLongOrNull()
                        ?.let { cfsId ->
                            daoService.assignCFSToUser(userProfileId, cfsId)
                                ?.let { ok().body(it) }
                                ?: throw NullValueNotAllowedException("No records found")
                        }
                        ?: throw InvalidValueException("Valid value for cfsId required")

                }
                ?: throw InvalidValueException("Valid value for userProfileId required")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('RBAC_REVOKE_CFS')")
    fun revokeCfsFromUser(req: ServerRequest): ServerResponse {
        return try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    req.pathVariable("userProfileId").toLongOrNull()
                        ?.let { userProfileId ->
                            req.pathVariable("cfsId").toLongOrNull()
                                ?.let { cfsId ->
                                    daoService.revokeCfsFromUser(userProfileId, cfsId, status)
                                        ?.let { ok().body(it) }
                                        ?: throw NullValueNotAllowedException("No records found")
                                }
                                ?: throw InvalidValueException("Valid value for cfsId required")

                        }
                        ?: throw InvalidValueException("Valid value for userProfileId required")

                }
                ?: throw InvalidValueException("Valid value for status required")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('RBAC_ASSIGN_ROLE')")
    fun assignRoleToUser(req: ServerRequest): ServerResponse {
        return try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    req.pathVariable("userId").toLongOrNull()
                        ?.let { userId ->
                            req.pathVariable("roleId").toLongOrNull()
                                ?.let { roleId ->
                                    daoService.assignRoleToUser(userId, roleId, status)
                                        ?.let { ok().body(it) }
                                        ?: throw NullValueNotAllowedException("No records found")
                                }
                                ?: throw InvalidValueException("Valid value for roleId required")

                        }
                        ?: throw InvalidValueException("Valid value for userId required")

                }
                ?: throw InvalidValueException("Valid value for status required")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('RBAC_ASSIGN_ROLE')")
    fun assignRoleToUserThroughRequest(req: ServerRequest): ServerResponse {
        return try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    req.pathVariable("userId").toLongOrNull()
                        ?.let { userId ->
                            req.pathVariable("roleId").toLongOrNull()
                                ?.let { roleId ->
                                    req.pathVariable("requestID").toLongOrNull()
                                        ?.let { requestId ->
                                            val u = UserRequestEntityDto()
                                            u.userId = userId
                                            u.requestId = requestId
                                            u.userRoleAssigned =
                                                daoService.assignRoleToUser(userId, roleId, status)?.roleId
                                            daoService.userRequest(u)
                                                ?.let { ok().body(it) }
                                                ?: throw NullValueNotAllowedException("No records found")
                                        }
                                        ?: throw InvalidValueException("Valid value for roleId required")
                                }
                                ?: throw InvalidValueException("Valid value for roleId required")

                        }
                        ?: throw InvalidValueException("Valid value for userId required")

                }
                ?: throw InvalidValueException("Valid value for status required")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('RBAC_REVOKE_ROLE')")
    fun revokeRoleFromUser(req: ServerRequest): ServerResponse {
        return try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    req.pathVariable("userId").toLongOrNull()
                        ?.let { userId ->
                            req.pathVariable("roleId").toLongOrNull()
                                ?.let { roleId ->
                                    daoService.revokeRoleFromUser(userId, roleId, status)
                                        ?.let { ok().body(it) }
                                        ?: throw NullValueNotAllowedException("No records found")
                                }
                                ?: throw InvalidValueException("Valid value for roleId required")

                        }
                        ?: throw InvalidValueException("Valid value for userId required")

                }
                ?: throw InvalidValueException("Valid value for status required")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('RBAC_ASSIGN_AUTHORIZATION')")
    fun assignAuthorizationFromRole(req: ServerRequest): ServerResponse {
        return try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    req.pathVariable("roleId").toLongOrNull()
                        ?.let { roleId ->
                            req.pathVariable("privilegeId").toLongOrNull()
                                ?.let { privilegeId ->
                                    daoService.assignAuthorizationFromRole(roleId, privilegeId, status)
                                        ?.let { ok().body(it) }
                                        ?: throw NullValueNotAllowedException("No records found")
                                }
                                ?: throw InvalidValueException("Valid value for privilegeId required")

                        }
                        ?: throw InvalidValueException("Valid value for roleId required")

                }
                ?: throw InvalidValueException("Valid value for status required")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('RBAC_REVOKE_AUTHORIZATION')")
    fun revokeAuthorizationFromRole(req: ServerRequest): ServerResponse {
        return try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    req.pathVariable("roleId").toLongOrNull()
                        ?.let { roleId ->
                            req.pathVariable("privilegeId").toLongOrNull()
                                ?.let { privilegeId ->
                                    daoService.revokeAuthorizationFromRole(roleId, privilegeId, status)
                                        ?.let { ok().body(it) }
                                        ?: throw NullValueNotAllowedException("No records found")
                                }
                                ?: throw InvalidValueException("Valid value for privilegeId required")

                        }
                        ?: throw InvalidValueException("Valid value for roleId required")

                }
                ?: throw InvalidValueException("Valid value for status required")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('RBAC_ROLE_AUTHORITIES_LIST')")
    fun authoritiesByRoleAndStatusListing(req: ServerRequest): ServerResponse {
        try {
            req.pathVariable("status").toIntOrNull()
                ?.let { status ->
                    req.pathVariable("roleId").toLongOrNull()
                        ?.let { roleId ->
                            daoService.listAuthoritiesByRoleAndStatus(roleId, status)
                                ?.let { return ok().body(it) }
                                ?: throw NullValueNotAllowedException("No records found")
                        }
                        ?: throw InvalidValueException("Valid value for roleId required")

                }
                ?: throw InvalidValueException("Valid value for status required")


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")

        }

    }

    @PreAuthorize("hasAuthority('TITLES_LIST')")
    fun titlesListing(req: ServerRequest): ServerResponse {
        try {
            daoService.listTitles(1)
                ?.let { return ok().body(it) }
                ?: throw NullValueNotAllowedException("No Titles found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('TITLES_WRITE')")
    fun titleUpdate(req: ServerRequest): ServerResponse {
        try {
            val role = req.body<TitlesEntityDto>()

            daoService.updateTitle(role)?.let {
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }

    }

    @PreAuthorize("hasAuthority('USER_TYPES_LIST')")
    fun userTypeListing(req: ServerRequest): ServerResponse {
        try {
            daoService.listUserTypes(1)
                ?.let { return ok().body(it) }
                ?: throw NullValueNotAllowedException("No UserTypes found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('USERS_WRITE')")
    fun usersUpdate(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val dto = req.body<UserEntityDto>()
            dto.userName = dto.email
            dto.userRegNo =
                "KEBS#EMP${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            daoService.updateUserDetails(dto)?.let {
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    fun usersUpdateCompanyProfile(req: ServerRequest): ServerResponse {
        try {
            req.pathVariable("userId").toLongOrNull()
                ?.let { userId ->
                    val map = commonDaoServices.serviceMapDetails(appId)
                    val dto = req.body<UserCompanyEntityDto>()
                    dto.userId = userId
                    daoService.updateUserCompanyDetails(dto).let {
                        return ok().body(it)
                    }
                } ?: throw NullValueNotAllowedException("User ID is null")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('USERS_LIST')")
    fun usersSearchListing(req: ServerRequest): ServerResponse {
        try {
            val dto = req.body<UserSearchValues>()

            daoService.userSearchResultListing(dto)?.let {
                KotlinLogging.logger { }.info("Record found ${it.count()}")
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    fun usersRequests(req: ServerRequest): ServerResponse {
        try {
            req.pathVariable("userId").toLongOrNull()
                ?.let { id ->
                    val dto = req.body<UserRequestEntityDto>()
                    dto.userId = id
                    daoService.userRequest(dto)?.let {
                        return ok().body(it)
                    }
                        ?: throw NullValueNotAllowedException("User ID is null")
                } ?: throw NullValueNotAllowedException("Update failed")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }
    }

    @PreAuthorize("hasAuthority('USER_TYPES_WRITE')")
    fun userTypeUpdate(req: ServerRequest): ServerResponse {
        try {
            val role = req.body<UserTypesEntityDto>()

            daoService.updateUserType(role)?.let {
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }

    }


}
