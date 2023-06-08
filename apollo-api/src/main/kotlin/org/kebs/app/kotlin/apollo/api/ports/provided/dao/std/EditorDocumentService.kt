package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import java.io.IOException;
import java.util.stream.Stream;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;




@Service
class EditorDocumentService(private val editorDocumentRepository: EditorDocumentRepository) {

    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): EditorDocuments? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = EditorDocuments()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data= file.bytes
        fileDB.itemId=itemId


        return editorDocumentRepository.save(fileDB)
    }

    fun getFile(id: String): EditorDocuments? {
        return editorDocumentRepository.findById(id).get()
    }

    fun getAllFiles(itemId: String): Stream<EditorDocuments> {
        return editorDocumentRepository.findByItemId(itemId).stream()
    }

}
