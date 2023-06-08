package org.kebs.app.kotlin.apollo.api.handlers

import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsRepository
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok

@Component
class ForeignDmarkHandler(
        applicationMapProperties: ApplicationMapProperties,

        private val serviceMapsRepo: IServiceMapsRepository
) {

    final val appId = applicationMapProperties.mapPermitApplication
    private final val applyOrRenew = "quality-assurance/customer/dmark/dmark-foreign/dmark-apply-or-renew"


    fun sgr(req: ServerRequest): ServerResponse = ok().render(applyOrRenew)
    fun applicationPage(req: ServerRequest) =
            serviceMapsRepo.findByIdAndStatus(appId, 1)
                    ?.let {
                        req.attributes()["permitEntity"] = PermitApplicationEntity()
                    }
}