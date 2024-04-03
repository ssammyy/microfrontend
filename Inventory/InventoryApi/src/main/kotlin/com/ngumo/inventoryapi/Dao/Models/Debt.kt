package com.ngumo.inventoryapi.Dao.Models

import java.util.*
import javax.persistence.*

@Table(name ="TB_DEBT")
@Entity
class Debt: BaseEntity() {
    var referenceCode: String ?= null
    var customerTel: String ?= null
    var customerName: String ?= null
    var initialDebt: Long ?= null
    var remainingDebt: Long ?= null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soldBy")
    var user: User? = null
    var status: Long?= null
    var proposedDateOfPayment: Date?= null
}