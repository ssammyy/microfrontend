package com.api.tech.pulse.techpulseapi.Store.Entities

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @CreatedDate
    @Column(nullable = false)
    var createdOn: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false)
    var createdBy: String = ""

    @Column(nullable = false)
    var softDelete: Boolean = false
}