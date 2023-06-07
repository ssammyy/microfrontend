package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import java.io.File

interface ISftpService {

    fun uploadFile(file: File): Boolean

    fun downloadFilesByDocType(docType: String): List<File>
}
