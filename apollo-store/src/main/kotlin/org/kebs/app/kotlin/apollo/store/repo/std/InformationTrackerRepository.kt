package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.InformationTracker
import org.springframework.data.repository.CrudRepository

interface InformationTrackerRepository : CrudRepository<InformationTracker, Long>
