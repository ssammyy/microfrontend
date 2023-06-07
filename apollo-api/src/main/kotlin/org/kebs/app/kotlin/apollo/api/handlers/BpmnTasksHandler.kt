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

package org.kebs.app.kotlin.apollo.api.handlers


import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidAuthenticationException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull

@Service
class BpmnTasksHandler(
        private val runtimeService: RuntimeService,
        private val taskService: TaskService,
        private val serviceMapsRepo: IServiceMapsRepository
) {

    private val userTasksListView = "/auth/user-tasks"
    private val userTaskView = "/auth/review-task"

    private fun tasksFromSecurityContext(req: ServerRequest): ServerRequest {
        /**
         * TODO: Consider adding this to the session and only removing the completed one from the list
         */
        SecurityContextHolder.getContext().authentication
                ?.let { auth ->
                    req.attributes()["userTasks"] = taskService.createTaskQuery().taskAssignee(auth.name).list()
                    val groupTasks: List<Task> = mutableListOf()
                    auth.authorities.forEach { groupTasks.zip(taskService.createTaskQuery().taskCandidateGroup(it.authority).list()) }
                    req.attributes()["groupTasks"] = groupTasks
                }
        return req
    }


    fun completeTask(req: ServerRequest): ServerResponse =
            req.paramOrNull("appId")
                    ?.let { appId ->
                        serviceMapsRepo.findByIdOrNull(appId.toIntOrNull())
                                ?.let { map ->
                                    req.pathVariable("taskId")
                                            .let { taskId ->
                                                req.pathVariable("taskStatus")
                                                        .let { taskStatus ->
                                                            val processInstanceId = runtimeService.createProcessInstanceQuery().processDefinitionKey(map.bpmnProcessKey).list()[0].processInstanceId
                                                            val comment = req.attribute("comment").toString()
                                                            taskService.addComment(taskId, processInstanceId, comment)
                                                            val taskVariables: MutableMap<String, Any> = HashMap()
                                                            taskVariables["taskStatus"] = taskStatus
                                                            taskVariables["remarks"] = req.attribute("comment")

                                                            taskService.complete(taskId, taskVariables)

                                                        }

                                                ok().render(userTaskView, tasksFromSecurityContext(req).attributes())
                                            }

                                }
                                ?: throw ServiceMapNotFoundException("ServiceMap [id=${appId}] Not found")
                    }
                    ?: throw ServiceMapNotFoundException("ServiceMap id Not found for request")


    fun claimTask(req: ServerRequest): ServerResponse =
            SecurityContextHolder.getContext().authentication
                    ?.let { auth ->
                        req.pathVariable("taskId")
                                .let { taskId ->
                                    taskService.claim(taskId, auth.name)
                                    ok().render(userTaskView, tasksFromSecurityContext(req))
                                }

                    }
                    ?: throw InvalidAuthenticationException("Login information not found")

    fun reviewTask(req: ServerRequest): ServerResponse =
            req.pathVariable("taskId").let { taskId ->
                req.attributes()["currentTask"] = taskService.createTaskQuery().taskId(taskId).list()[0]

                ok().render(userTaskView, req.attributes())
            }


    fun tasksListView(req: ServerRequest): ServerResponse =
            ok().render(userTasksListView, tasksFromSecurityContext(req).attributes())


}