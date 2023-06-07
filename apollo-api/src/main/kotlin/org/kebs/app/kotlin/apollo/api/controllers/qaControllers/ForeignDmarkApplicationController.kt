package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.config.properties.storage.StorageProperties
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.nio.file.Path
import java.nio.file.Paths


@Controller
@RequestMapping("/foreign/")
class ForeignDmarkApplicationController(
        applicationMapProperties: ApplicationMapProperties,
        private val storageProperties: StorageProperties
) {
    val appId: Int = applicationMapProperties.mapPermitApplication


    fun loadFile(filename: String): Resource {
    val uploadDirectory = storageProperties.uploadDirectory
//        val uploadDirectory = System.getProperty("user.dir") + "/uploads"
        val rootLocation: Path = Paths.get(uploadDirectory)
        val file = rootLocation.resolve(filename)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("FAIL!")
        }
    }

    @GetMapping("/files/{filename}")
    fun downloadFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = loadFile(filename)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + "\"")
                .body(file)
    }
}