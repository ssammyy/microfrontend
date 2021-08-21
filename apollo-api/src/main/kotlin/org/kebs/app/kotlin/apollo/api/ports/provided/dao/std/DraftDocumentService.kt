package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import java.io.IOException;
import java.util.stream.Stream;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*


@Service
class DraftDocumentService(private val draftDocumentRepository: DraftDocumentRepository) {

    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): DraftDocument? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = DraftDocument()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data= file.bytes
        fileDB.itemId=itemId


        return draftDocumentRepository.save(fileDB)
    }

    fun getFile(id: String): DraftDocument? {
        return draftDocumentRepository.findById(id).get()
    }

    fun getAllFiles(itemId: String): Stream<DraftDocument> {
        return draftDocumentRepository.findByItemId(itemId).stream()
    }

}
