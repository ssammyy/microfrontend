package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import java.io.IOException;
import java.util.stream.Stream;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*


@Service
class TCRelevantDocumentService(private val tcRelevantDocumentRepository: TCRelevantDocumentRepository) {

    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): TCRelevantDocument? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = TCRelevantDocument()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data= file.bytes
        fileDB.itemId=itemId


        return tcRelevantDocumentRepository.save(fileDB)
    }

    fun getFile(id: String): TCRelevantDocument? {
        return tcRelevantDocumentRepository.findById(id).get()
    }

    fun getAllFiles(itemId: String): Stream<TCRelevantDocument> {
        return tcRelevantDocumentRepository.findByItemId(itemId).stream()
    }

}
