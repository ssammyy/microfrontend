package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.NotificationReceived
import org.springframework.data.repository.CrudRepository

interface NotificationReceivedRepository: CrudRepository<NotificationReceived, Long>
