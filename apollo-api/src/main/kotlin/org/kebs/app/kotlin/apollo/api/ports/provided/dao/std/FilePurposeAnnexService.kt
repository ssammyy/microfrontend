package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import java.io.IOException;
import java.util.stream.Stream;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*


@Service
class FilePurposeAnnexService(private val filePurposeAnnexRepository: FilePurposeAnnexRepository) {

    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): FilePurposeAnnex? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = FilePurposeAnnex()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data= file.bytes
        fileDB.itemId=itemId


        return filePurposeAnnexRepository.save(fileDB)
    }

    fun getFile(id: String): FilePurposeAnnex? {
        return filePurposeAnnexRepository.findById(id).get()
    }

    fun getAllFiles(itemId: String): Stream<FilePurposeAnnex> {
        return filePurposeAnnexRepository.findByItemId(itemId).stream()
    }

}
