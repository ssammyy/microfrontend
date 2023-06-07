package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.EnqiryTracker
import org.springframework.data.repository.CrudRepository

interface EnquiryTrackerRepository : CrudRepository<EnqiryTracker, Long>
