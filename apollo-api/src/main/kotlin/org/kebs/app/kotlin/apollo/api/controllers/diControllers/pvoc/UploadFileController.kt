package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import org.kebs.app.kotlin.apollo.api.service.FileExcelProcessService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class UploadFileController {
    @Autowired
    var fileExcelProcessService: FileExcelProcessService? = null
    @GetMapping("/upload_excel")
    fun index(): String {
        return "destination-inspection/pvoc/uploadform"
    }

    @PostMapping("/upload_excel")
    fun uploadMultipartFile(@RequestParam("uploadfile") file: MultipartFile, model: Model): String {
           // fileExcelProcessService?.store(file)
            model.addAttribute("message", "File uploaded successfully!")

        return "destination-inspection/pvoc/uploadform"
    }
}