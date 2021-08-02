package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.NEPNotification
import org.springframework.data.repository.CrudRepository

interface NEPNotificationRepository : CrudRepository<NEPNotification, Long>
