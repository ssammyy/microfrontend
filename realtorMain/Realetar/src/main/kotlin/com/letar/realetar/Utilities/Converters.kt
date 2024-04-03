package com.letar.realetar.Utilities

import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
@Service
class Converters {

    fun formatDate(date : String): LocalDate {
        val dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")


       val   startDate = LocalDate.parse(date, dateFormatter)
        return startDate

    }


}