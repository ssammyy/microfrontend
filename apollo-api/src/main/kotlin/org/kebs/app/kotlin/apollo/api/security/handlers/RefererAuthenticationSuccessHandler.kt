/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.api.security.handlers


import mu.KotlinLogging
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class RefererAuthenticationSuccessHandler(
        private val applicationMapProperties: ApplicationMapProperties,
        private val usersRepo: IUserRepository,
        private val usersProfilesRepository: IUserProfilesRepository,
        private val statusValuesRepo: IStatusValuesRepository,
        private val approvalStatusRepo: IApprovalStatusRepository,
        private val taskService: TaskService,
        private val url: String?
) : AuthenticationSuccessHandler {
    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
            req: HttpServletRequest,
            response: HttpServletResponse?, authentication: Authentication
    ) {
        val taskList: List<Task?> = mutableListOf()
        val userTypeId = longArrayOf(applicationMapProperties.mapUserTypeEmployee, applicationMapProperties.mapUserTypeImporter, applicationMapProperties.mapUserTypeManufacture)
        req.getSession(true)
                ?.let { session ->
                    authentication.name
                            ?.let { username ->
                                usersRepo.findByUserName(username)
                                        ?.let { currentUser ->
                                            session.setAttribute("currentUser", currentUser)


                                            approvalStatusRepo.findAllByStatusOrderByApprovalStatus(1)
                                                    ?.let { session.setAttribute("approvals", it) }


//                                            currentUser.userTypes?.defaultRole?.let {
//                                                userUrlsRepo.findFirst5ByRoleId(it)
//                                                        .let { urls ->
//                                                            session.setAttribute("myUrls", urls)
//                                                            urls.forEach { single_url ->
//                                                                KotlinLogging.logger {  }.info { "${single_url?.url}, ${single_url?.displayText}" }
//                                                            }
//                                                        }
//                                            }

                                            /**
                                             * Load the current user's profile
                                             */
                                            usersProfilesRepository.findByUserIdAndStatus(currentUser, 1)
                                                    ?.let { profile ->
                                                        statusValuesRepo.findByStatus(1)
                                                                ?.let {statusValues->
                                                                    session.setAttribute("statusValues", statusValues)
                                                                    session.setAttribute("profile", profile.userId)
                                                                    session.setAttribute("currentUserProfile", profile)
                                                                    session.setAttribute("region", profile.subRegionId)
                                                                    session.setAttribute("designation", profile.designationId)
                                                                    session.setAttribute("section", profile.sectionId)
//                                                                    session.setAttribute("notifications", commonDaoServices.findAllUseNotification(profile.userId.email))
                                                                    /**
                                                                     * TODO: Enable dynamic Front End menu here
                                                                     */

                                                                }
                                                    }
                                            /**
                                             * Load the current user's tasks, to begin with we could use both group
                                             * and user and gradually migrate all tasks to the group so that we have
                                             * business continuation
                                             * Below fetches the tasks for the user that is logged in
                                             * */
                                            taskList.zip(taskService.createTaskQuery().taskAssignee(username).list())

                                            /**
                                             * checks if the current user profile(Importer, Exporter and Manufacturer) is incomplete
                                             * , if so, it will redirect to user type profile for completion of data collection
                                             */
                                            for (k in userTypeId.indices) {
                                                if (userTypeId[k] == currentUser.userTypes && currentUser.userProfileStatus == 0 ) {
                                                    val importerUrl = "/api/auth/signup/update-user-details?userId=${currentUser.id}"
                                                    KotlinLogging.logger {  }.info { "user profile incomplete: $importerUrl" }
                                                    redirectStrategy.sendRedirect(req, response, importerUrl )
                                                }
                                            }


                                        }
                            }
                    /**
                     * Load the current user's tasks, to begin with we could use both group
                     * and user and gradually migrate all tasks to the group so that we have
                     * business continuation
                     * */
//                    authentication.authorities.forEach { authority ->
//                        taskList.zip(taskService.createTaskQuery().taskCandidateGroup(authority.authority).list())
//                    }
//                    when {
//                        taskList.isNotEmpty() -> session.setAttribute("myTasks", taskList)
//                    }

                }


        redirectStrategy.sendRedirect(req, response, url)


    }
}