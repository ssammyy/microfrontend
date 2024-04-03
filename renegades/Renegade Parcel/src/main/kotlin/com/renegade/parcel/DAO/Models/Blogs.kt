package com.renegade.parcel.DAO.Models

import jakarta.persistence.*
import lombok.Data
import java.time.LocalDateTime


@Entity
@Data
@Table(name = "BLOGS")
class Blogs  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    var title: String = ""
    var content: String = ""
    var author: String = ""
    var category: String = ""
    var imageUrl: String = ""
    var additionalImage: String =""
    var publishedDate: LocalDateTime? = null
}
