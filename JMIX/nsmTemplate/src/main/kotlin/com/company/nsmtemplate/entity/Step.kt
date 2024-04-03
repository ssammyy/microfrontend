package com.company.nsmtemplate.entity

import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.metamodel.annotation.InstanceName
import io.jmix.core.metamodel.annotation.JmixEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.*

@JmixEntity
@Table(name = "STEP")
@Entity
open class Step {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    var id: UUID? = null

    @InstanceName
    @Column(name = "NAME", nullable = false)
    @NotNull
    var name: String? = null

    @Column(name = "DURATION", nullable = false)
    @NotNull
    var duration: Int? = null

    @Column(name = "SORTVALUE", nullable = false)
    @NotNull
    var sortvalue: Int? = null

    @Column(name = "VERSION", nullable = false)
    @Version
    var version: Int? = null

}