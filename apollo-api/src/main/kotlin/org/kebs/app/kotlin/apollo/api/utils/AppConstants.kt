package org.kebs.app.kotlin.apollo.api.utils

import org.kebs.app.kotlin.apollo.store.model.ProcessStatusEntity
import java.sql.Timestamp

object AppConstants {
    const val EMPLOYEE_URI = "/employees"
    const val EMPLOYEE_JSON_PARAM = "empJson"
    const val EMPLOYEE_FILE_PARAM = "file"
    const val SUCCESS_CODE = "EMP-200"
    const val SUCCESS_MSG = "Employee created successfully"
    const val FILE_SEPERATOR = "_"
    const val DOWNLOAD_PATH = "/downloadFile/"
    const val DOWNLOAD_URI = "/downloadFile/{fileName:.+}"
    const val DEFAULT_CONTENT_TYPE = "application/octet-stream"
    const val FILE_DOWNLOAD_HTTP_HEADER = "attachment; filename=\"%s\""
    const val FILE_PROPERTIES_PREFIX = "file"
    const val FILE_STORAGE_EXCEPTION_PATH_NOT_FOUND = "Could not create the directory where the uploaded files will be stored"
    const val INVALID_FILE_PATH_NAME = "Sorry! Filename contains invalid path sequence"
    const val FILE_NOT_FOUND = "File not found "
    const val FILE_STORAGE_EXCEPTION = "Could not store file %s !! Please try again!"
    val INVALID_FILE_DELIMITER: CharSequence = ".."
    const val INDEX_PAGE_URI = "/index"
    const val INDEX_PAGE = "index"
    const val TEMP_DIR = "C://TMP//"
    const val INVALID_FILE_DIMENSIONS = "Invalid file dimensions. File dimension should note be more than 300 X 300"
    const val INVALID_FILE_FORMAT = "Only PNG, JPEG and JPG images are allowed"
    const val PNG_FILE_FORMAT = ".png"
    const val JPEG_FILE_FORMAT = ".jpeg"
    const val JPG_FILE_FORMAT = ".jpg"
}

class DummyProduct {
    //    Permit No.	Status	Expiry Date	Type	Product	Brand Name
    var id: Long? = null
    var status: Int? = null
    var expiryDate: Timestamp? = null
    var dateCreated: Timestamp? = null
    var type: Long? = null
    var product: Long? = null
    var brandName: String? = null
    var productName: String? = null
    var permitNumber: String? = null
    var processStatus: ProcessStatusEntity? = null
    var permitType: String? = null
}