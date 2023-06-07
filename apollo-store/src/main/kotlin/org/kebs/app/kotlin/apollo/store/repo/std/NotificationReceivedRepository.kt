package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.NotificationReceived
import org.springframework.data.repository.CrudRepository

interface NotificationReceivedRepository : CrudRepository<NotificationReceived, Long>
