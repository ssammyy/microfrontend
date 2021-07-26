package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity

class QaInvoiceCalculation(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices
) {
    var appId = applicationMapProperties.mapQualityAssurance
    val map = commonDaoServices.serviceMapDetails(appId)

    fun calculatePayment(permit: PermitApplicationsEntity, users: UsersEntity) {

    }

}