package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.EnqiryTracker
import org.springframework.data.repository.CrudRepository

interface EnquiryTrackerRepository : CrudRepository<EnqiryTracker, Long>
