package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream


interface FilesStorageService {
    fun init()
    fun save(file: MultipartFile?)
    fun load(filename: String?): Resource?
    fun deleteAll()
    fun loadAll(): Stream<Path?>?
}
