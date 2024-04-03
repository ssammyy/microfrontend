package com.api.tech.pulse.techpulseapi.DataTransfer

import com.api.tech.pulse.techpulseapi.Store.Entities.Blogs
import org.springframework.stereotype.Service
import java.time.LocalDateTime
class BlogsDtos {

    data class BlogDto(
            val id : Long? = null,
            val title: String,
            val content: String,
            val author: String,
            val category: String,
            val imageUrl: String,
            val additionalImage: String,
            val publishedDate: LocalDateTime?
    )



}