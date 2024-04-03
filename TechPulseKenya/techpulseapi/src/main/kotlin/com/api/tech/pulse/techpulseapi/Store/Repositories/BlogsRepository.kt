package com.api.tech.pulse.techpulseapi.Store.Repositories

import com.api.tech.pulse.techpulseapi.Store.Entities.Blogs
import org.springframework.data.jpa.repository.JpaRepository

interface BlogsRepository: JpaRepository<Blogs, Long> {


}