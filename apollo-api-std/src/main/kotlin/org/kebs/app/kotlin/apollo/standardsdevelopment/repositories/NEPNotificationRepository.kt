package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.NEPNotification
import org.springframework.data.repository.CrudRepository

interface NEPNotificationRepository : CrudRepository<NEPNotification, Long>
