package org.kebs.app.kotlin.apollo.api.controllers.userManagementControllers

import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class UserController(
        applicationMapProperties: ApplicationMapProperties
) {
    //change this to make the class complile, but the propety is missing for user management
    val appId = applicationMapProperties.mapUserActivation

    @GetMapping("")
    fun listUsers(
            model: Model
    ): String {

        return "admin/users"
    }
}