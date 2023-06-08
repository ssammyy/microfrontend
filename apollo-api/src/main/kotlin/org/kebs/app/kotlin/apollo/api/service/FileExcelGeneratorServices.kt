package org.kebs.app.kotlin.apollo.api.service

import org.kebs.app.kotlin.apollo.api.utils.ExcelCreatorUtils
import org.kebs.app.kotlin.apollo.store.repo.IPvocApplicationProductsRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.IOException

@Service
class FileExcelGeneratorServices {

    // Create Empty Excel File For Exemption Applications
    fun loadFile(): ByteArrayInputStream? {
        try {
            return ExcelCreatorUtils.generateExemptionApplicationExcel()
        } catch (e: IOException) {
        }
        return null
    }

}