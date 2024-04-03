package com.api.tech.pulse.techpulseapi.Store.Entities

import java.time.LocalDateTime
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Data

@Entity
@Data
@Table(name = "BLOGS")
class Blogs : BaseEntity() {
    var title: String = ""
    var content: String = ""
    var author: String = ""
    var category: String = ""
    var imageUrl: String = ""
    var additionalImage: String =""
    var publishedDate: LocalDateTime? = null
}
