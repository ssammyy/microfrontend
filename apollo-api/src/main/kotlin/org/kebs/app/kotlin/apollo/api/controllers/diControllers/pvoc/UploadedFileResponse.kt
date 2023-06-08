package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UploadedFileResponse {
    val success: Boolean = true
    val fro = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val to = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}