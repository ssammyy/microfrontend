package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.InformationTracker
import org.springframework.data.repository.CrudRepository

interface InformationTrackerRepository : CrudRepository<InformationTracker, Long>
