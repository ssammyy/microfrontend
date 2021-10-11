package org.kebs.app.kotlin.apollo.api.payload

import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmittedPdfListDetailsEntity

class LimsPdfFilesDto {
    var fileSavedStatus: Boolean? = null
    var fileName: String? = null
    var ssfId: Long? = null

    companion object {
        fun fromEntity(filePdf: QaSampleSubmittedPdfListDetailsEntity): LimsPdfFilesDto {
            return LimsPdfFilesDto().apply {
                fileName = filePdf.pdfName
                fileSavedStatus = filePdf.status == 1
                ssfId = filePdf.sffId
            }
        }
        fun fromList(filePdfs: List<QaSampleSubmittedPdfListDetailsEntity>): List<LimsPdfFilesDto> {
            val pdfs= mutableListOf<LimsPdfFilesDto>()
            filePdfs.forEach {
                pdfs.add(fromEntity(it))
            }
            return pdfs
        }
    }
}