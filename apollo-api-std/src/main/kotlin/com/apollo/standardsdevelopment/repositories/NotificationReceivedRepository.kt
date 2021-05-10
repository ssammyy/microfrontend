package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.NotificationReceived
import org.springframework.data.repository.CrudRepository

interface NotificationReceivedRepository: CrudRepository<NotificationReceived, Long> {
}
