package com.letar.realetar.DataClasses

import java.time.LocalDate
import java.util.*

data class ExcelData(
    val structure: String,
    val element: String,
    val floor: String,
    val material: String,
    val itemName: String,
    val itemId: String,
    val quantity: Int,
    val startDate: LocalDate,
    val orderDate: LocalDate
)
