package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.FileInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.stream.Collectors


@Controller
@CrossOrigin("http://localhost:8081")
@RequestMapping("api/v1/migration")
class FilesController {
    @Autowired
    var storageService: FilesStorageService? = null

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<ResponseMessage> {
        var message = ""
        return try {
            storageService!!.save(file)
            message = "Uploaded the file successfully: " + file.originalFilename
            ResponseEntity.status(HttpStatus.OK).body(ResponseMessage(message))
        } catch (e: Exception) {
            message = "Could not upload the file: " + file.originalFilename + "!"
            ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseMessage(message))
        }
    }

    @GetMapping("/files")
    fun getListFiles(): ResponseEntity<List<FileInfo>> {
        val fileInfos = storageService?.loadAll()?.map { path ->
            val filename = path?.fileName.toString()
            val url = MvcUriComponentsBuilder
                .fromMethodName(this.javaClass, "getFile", path?.fileName.toString()).build().toString()
            FileInfo(filename, url)
        }?.collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos)
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    fun getFile(@PathVariable filename: String?): ResponseEntity<Resource> {
        val file = storageService!!.load(filename)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file!!.filename + "\"").body(file)
    }
}




