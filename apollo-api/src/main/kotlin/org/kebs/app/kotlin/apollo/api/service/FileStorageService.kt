package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.SdlFactoryVisitReportsUploadEntity
import org.kebs.app.kotlin.apollo.store.repo.ISdlFactoryVisitReportsUploadEntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import kotlin.Throws
import java.io.IOException
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp

@Service
class FileStorageService {
    @Autowired
    lateinit var iSdlFactoryVisitReportsUploadEntityRepository: ISdlFactoryVisitReportsUploadEntityRepository

    @Throws(IOException::class)
    fun store(file: MultipartFile, reportId : Long, createdBy: String, createdOn: Timestamp){
        val fileName = file.originalFilename?.let { StringUtils.cleanPath(it) }
        val sdlFactoryVisitReportsUploadEntity = SdlFactoryVisitReportsUploadEntity()
        sdlFactoryVisitReportsUploadEntity.data = file.bytes
        sdlFactoryVisitReportsUploadEntity.name = fileName
        KotlinLogging.logger {  }.info { "Uploaded file name is "+fileName }
        sdlFactoryVisitReportsUploadEntity.type = file.contentType
        sdlFactoryVisitReportsUploadEntity.reportId = reportId
        sdlFactoryVisitReportsUploadEntity.createdBy = createdBy
        sdlFactoryVisitReportsUploadEntity.createdOn = createdOn
//        val FileDB = SdlFactoryVisitReportsUploadEntity(name = fileName, file.contentType, file.bytes)
        iSdlFactoryVisitReportsUploadEntityRepository.save(sdlFactoryVisitReportsUploadEntity)
        KotlinLogging.logger {  }.info { "Saved uploaded file OK" }
    }
}