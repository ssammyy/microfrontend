package com.renegade.parcel.DAO.Models

import jakarta.persistence.EntityListeners
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import lombok.Data
import jakarta.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@Data
abstract class BaseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null,

    val softDelete: Boolean = false
)
