package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.InformationTracker
import org.springframework.data.repository.CrudRepository

interface InformationTrackerRepository : CrudRepository<InformationTracker, Long> {

}
