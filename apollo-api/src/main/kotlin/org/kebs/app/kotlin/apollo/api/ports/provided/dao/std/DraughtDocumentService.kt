package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import java.io.IOException;
import java.util.stream.Stream;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;





@Service
class DraughtDocumentService(private val draughtDocumentRepository: DraughtDocumentRepository) {

    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): DraughtDocuments? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = DraughtDocuments()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data= file.bytes
        fileDB.itemId=itemId


        return draughtDocumentRepository.save(fileDB)
    }

    fun getFile(id: String): DraughtDocuments? {
        return draughtDocumentRepository.findById(id).get()
    }

    fun getAllFiles(itemId: String): Stream<DraughtDocuments> {
        return draughtDocumentRepository.findByItemId(itemId).stream()
    }

}
