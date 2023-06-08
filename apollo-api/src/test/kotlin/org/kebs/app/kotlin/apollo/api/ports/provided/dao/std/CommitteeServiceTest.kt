package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class CommitteeServiceTest {

    @Test
    fun convertToTimestamp(): Unit {

        val dateFormat = SimpleDateFormat("yyyy-mm-dd")
        val parsedDate: Date = dateFormat.parse("2023-12-25")

        println(parsedDate)

    }


    @Test
    fun checkIfSplitTextWorks(): Unit {

        val text ="CHEM/292:2023"
        val splitText = text.split("/".toRegex())
        val partB = splitText[1]

        val textToDisplay=


        println(partB)

    }
}
