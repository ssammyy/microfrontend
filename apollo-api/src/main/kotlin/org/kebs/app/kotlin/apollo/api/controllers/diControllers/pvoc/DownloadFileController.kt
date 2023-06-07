package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import org.kebs.app.kotlin.apollo.api.service.FileExcelGeneratorServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DownloadFileController {
    @Autowired
    var fileExcelGeneratorServices: FileExcelGeneratorServices? = null

    /*
     * Download Files
     */
    @GetMapping("/file")
    fun downloadFile(): ResponseEntity<InputStreamResource> {
        val headers = HttpHeaders()
        headers.add("Content-Disposition", "attachment; filename=exception.xlsx")
        return ResponseEntity
            .ok()
            .headers(headers)
            .body(
                InputStreamResource(
                    fileExcelGeneratorServices?.loadFile()!!
                )
            )
    }
}