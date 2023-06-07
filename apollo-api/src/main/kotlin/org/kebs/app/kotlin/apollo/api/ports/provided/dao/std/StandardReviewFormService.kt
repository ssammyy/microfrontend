package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import java.io.IOException;
import java.util.stream.Stream;


import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service


@Service
class StandardReviewFormService(private val standardReviewFormRepository: StandardReviewFormRepository) {
    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): StandardReviewForm? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = StandardReviewForm()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data= file.bytes
        fileDB.itemId=itemId



        return standardReviewFormRepository.save(fileDB)
    }


    fun getFile(id: String): StandardReviewForm? {
        return standardReviewFormRepository.findById(id).get()
    }

    fun getAllFiles(): Stream<StandardReviewForm?>? {
        return standardReviewFormRepository.findAll().stream()
    }
//@Throws(IOException::class)
//fun store(file: MultipartFile, itemId: String): StandardReviewForm? {
//    val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
//    val fileDB = StandardReviewForm()
//    fileDB.name = fileName
//    fileDB.type = file.contentType.toString()
//    fileDB.data= file.bytes
//    fileDB.itemId=itemId
//
//
//    return standardReviewFormRepository.save(fileDB)
//}
//
//    fun getFile(id: String): StandardReviewForm? {
//        return standardReviewFormRepository.findById(id).get()
//    }
//
//    fun getAllFiles(itemId: String): Stream<StandardReviewForm> {
//        return standardReviewFormRepository.findByItemId(itemId).stream()
//    }
//
}
