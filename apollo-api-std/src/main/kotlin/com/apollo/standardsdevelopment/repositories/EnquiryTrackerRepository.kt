package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.EnqiryTracker
import org.springframework.data.repository.CrudRepository

interface EnquiryTrackerRepository : CrudRepository<EnqiryTracker, Long>  {
}
