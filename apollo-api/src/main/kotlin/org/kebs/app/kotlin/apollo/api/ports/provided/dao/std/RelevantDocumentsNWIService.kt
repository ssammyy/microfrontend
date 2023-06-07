package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import java.io.IOException;
import java.util.stream.Stream;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*


@Service
class RelevantDocumentsNWIService(private val relevantDocumentsNWIRepository: RelevantDocumentsNWIRepository) {

    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): RelevantDocumentsNWI? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = RelevantDocumentsNWI()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data= file.bytes
        fileDB.itemId=itemId


        return relevantDocumentsNWIRepository.save(fileDB)
    }

    fun getFile(id: String): RelevantDocumentsNWI? {
        return relevantDocumentsNWIRepository.findById(id).get()
    }

    fun getAllFiles(itemId: String): Stream<RelevantDocumentsNWI> {
        return relevantDocumentsNWIRepository.findByItemId(itemId).stream()
    }

}
