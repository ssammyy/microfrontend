package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.std.DraftDocument
import org.kebs.app.kotlin.apollo.store.repo.std.DraftDocumentRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardsDocumentsRepository
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.stream.Stream


@Service
class DraftDocumentService(
    private val draftDocumentRepository: DraftDocumentRepository,
    private val standardsDocumentsRepository: StandardsDocumentsRepository
) {

    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): DraftDocument? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = DraftDocument()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data = file.bytes
        fileDB.itemId = itemId


        return draftDocumentRepository.save(fileDB)
    }

    fun getFile(id: String): DraftDocument? {
        return draftDocumentRepository.findById(id).get()
    }

    fun getAllFiles(itemId: String): Stream<DraftDocument> {
        return draftDocumentRepository.findByItemId(itemId).stream()
    }

    fun findUploadedDIFileBYId(diDocumentId: Long): DatKebsSdStandardsEntity {
        return standardsDocumentsRepository.findBySdDocumentId(diDocumentId)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$diDocumentId]")
    }

    fun findUploadedDIFileBYIdAndByType(diDocumentId: Long, doctype: String): DatKebsSdStandardsEntity {
        return standardsDocumentsRepository.findBySdDocumentIdAndDocumentTypeDef(diDocumentId,doctype)
            ?: throw ExpectedDataNotFound("No File found with the following [ id=$diDocumentId]")
    }

}
