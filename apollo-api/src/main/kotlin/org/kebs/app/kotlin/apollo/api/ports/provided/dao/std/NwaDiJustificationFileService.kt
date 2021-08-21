package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import org.springframework.util.StringUtils;
import java.util.stream.Stream;

@Service
class NwaDiJustificationFileService (private val nwaDocumentsRepository: NwaDocumentsRepository){
    @Throws(IOException::class)
    fun store(file: MultipartFile, itemId: String): NwaDocument? {
        val fileName: String = StringUtils.cleanPath(file.originalFilename.toString())
        val fileDB = NwaDocument()
        fileDB.name = fileName
        fileDB.type = file.contentType.toString()
        fileDB.data= file.bytes
        fileDB.itemId=itemId


        return nwaDocumentsRepository.save(fileDB)
    }

    fun getFile(id: String): NwaDocument? {
        return nwaDocumentsRepository.findById(id).get()
    }

    fun getAllFiles(itemId: String): Stream<NwaDocument> {
        return nwaDocumentsRepository.findByItemId(itemId).stream()
    }

}
