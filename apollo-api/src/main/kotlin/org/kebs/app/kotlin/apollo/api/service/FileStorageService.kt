package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.store.repo.ISdlFactoryVisitReportsUploadEntityRepository
import org.springframework.util.StringUtils
import kotlin.Throws
import java.io.IOException
import org.springframework.web.multipart.MultipartFile

class FileStorageService {
    private val iSdlFactoryVisitReportsUploadEntityRepository: ISdlFactoryVisitReportsUploadEntityRepository? = null
    @Throws(IOException::class)
    fun store(file: MultipartFile): FileDB {
        val fileName = StringUtils.cleanPath(file.originalFilename!!)
        val FileDB = FileDB(fileName, file.contentType, file.bytes)
        return fileDBRepository.save(FileDB)
    }
}