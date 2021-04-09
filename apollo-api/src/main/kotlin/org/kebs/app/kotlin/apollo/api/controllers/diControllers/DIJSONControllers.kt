package org.kebs.app.kotlin.apollo.api.controllers.diControllers

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ConsignmentDocumentEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.sql.Date
import javax.servlet.http.HttpSession


@RestController
@RequestMapping("/api/di/")
@SessionAttributes("parameter")
class DIJSONControllers(
        private val iLaboratoryRepo: ILaboratoryRepository,
        private val daoServices: DestinationInspectionDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val iPvocApplicationRepo: IPvocApplicationRepo,
        private val usersRepo: IUserRepository,
        private val iPvocWaiversCategoryDocumentsRepo: IPvocWaiversCategoryDocumentsRepo
){
    private val importInspection: Int = applicationMapProperties.mapImportInspection

    @GetMapping("populate-all-labs", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun populateBroadCategory(): String? {
        val gson = Gson()
        return gson.toJson(iLaboratoryRepo.findAll())
    }

    @GetMapping("all-docs-required/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllDocs(@PathVariable("id") id: Long): String? {
        val gson = Gson()
        return gson.toJson(iPvocWaiversCategoryDocumentsRepo.findAllByStatusAndCategoryId(1, id))
    }

    @GetMapping("get-lab-results/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getLabResults(
            @PathVariable(value = "id") id: Long,
            model: Model
    ): String {
        val gson = Gson()
        val parameter = daoServices.findSavedSampleSubmissionParameterList(id)
        model.addAttribute("parameter", parameter)
        return gson.toJson(parameter)
    }

    @GetMapping("email-check/{email}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myEmail(
            @PathVariable(value = "email", required = false) email: String
    ): String? {
        var result = "notExist"
        usersRepo.findByEmail(email)
                .let { usersEntity ->
                    if (usersEntity?.email == email){
                        result = email
                    }else{
                        result
                    }
                }
        return result
    }




//    @CrossOrigin(origins = arrayOf("https://localhost:8006"))
//    @GetMapping("search-by-io/{assignedIO}", produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun getAllCorsByAssignedIO(
//            @PathVariable("assignedIO") assignedIO: Long
//    ) : List<ConsignmentDocumentEntity>? {
//        KotlinLogging.logger {  }.info { assignedIO }
//        return destinationInspectionRepo.findAllByAssignedIo(assignedIO)
//    }
//
//    @CrossOrigin(origins = ["https://localhost:8006"])
//    @RequestMapping("search-by-application-date-range", produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun getAllCorsByApplicationDateRange(
//            @RequestParam("fromDate") fromDate: Date,
//            @RequestParam("toDate") toDate: Date
//    ) : List<ConsignmentDocumentEntity>?{
//        KotlinLogging.logger {  }.info { fromDate }
//        val gson = Gson()
//        return destinationInspectionRepo.findAllByApplicationDateBetweenAndCorIdNotNull(fromDate, toDate)
//    }
//
//    @CrossOrigin(origins = ["https://localhost:8006"])
//    @RequestMapping("search-by-application-date-range-ncrs", produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun getAllNcrsByApplicationDateRange(
//            @RequestParam("fromDate") fromDate: Date,
//            @RequestParam("toDate") toDate: Date
//    ): List<ConsignmentDocumentEntity>? {
//        return destinationInspectionRepo.findAllByApplicationDateBetweenAndNcrIdNotNull(fromDate, toDate) as List<ConsignmentDocumentEntity>
//    }

//    @RequestMapping("search-by-application-date-range-pvocs", produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun getAllPvocsByApplicationDateRange(
//            @RequestParam("fromDate") fromDate: Date,
//            @RequestParam("toDate") toDate: Date
//    ): List<PvocApplicationEntity>? {
//        return iPvocApplicationRepo.findAllByApplicationDateBetween(fromDate, toDate) as List<PvocApplicationEntity>
//    }


}
