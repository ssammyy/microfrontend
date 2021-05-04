package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.NEPNotification
import org.springframework.data.repository.CrudRepository

interface NEPNotificationRepository : CrudRepository<NEPNotification, Long> {
}
