package org.kebs.app.kotlin.apollo.api.controllers.diControllers.importer

import com.google.gson.Gson
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ImporterDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/importer")
class ImporterJSONControllers(
        private val iLaboratoryRepo: ILaboratoryRepository,
        private val daoServices: ImporterDaoServices,
        private val destinationInspectionDaoServices: DestinationInspectionDaoServices,
        private val applicationMapProperties: ApplicationMapProperties,
        private val iPvocApplicationRepo: IPvocApplicationRepo,
        private val idfsRepo: IIdfsRepository,
        private val usersRepo: IUserRepository,
        private val SubSectionsLevel1Repo: ISubSectionsLevel1Repository,
        private val SubSectionsLevel2Repo: ISubSectionsLevel2Repository,
        private val titlesRepository: ITitlesRepository,
        private val divisionsRepo: IDivisionsRepository,
        private val departmentsRepo: IDepartmentsRepository,
        private val directorateRepo: IDirectoratesRepository,
        private val designationRepository: IDesignationsRepository,
        private val sectionsRepo: ISectionsRepository,
        private val subRegionsRepository: ISubRegionsRepository,
        private val countiesRepo: ICountiesRepository,
        private val townsRepo: ITownsRepository,
        private val regionsRepository: IRegionsRepository
) {
    @Value("\${common.active.status}")
    lateinit var activeStatus: String

    @Value("\${common.inactive.status}")
    lateinit var inActiveStatus: String

    private val userRegistration: Int = applicationMapProperties.mapUserRegistration


//
//    @GetMapping("/api/importer/rfc-coi-item-details/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun myDepartments(
//            @PathVariable(value = "id") id: Long
//    ): String? {
//        val gson = Gson()
//        return gson.toJson(daoServices.fin(id))
//    }

    @GetMapping("/idf/{idfNumber}/{ucrNumber}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myIDFDetails(
            @PathVariable(value = "idfNumber", required = false) idfNumber: String,
            @PathVariable(value = "ucrNumber", required = false) ucrNumber: String
    ): Boolean {
        return daoServices.findByIdfNoAndUCRNumberAndStatus(idfNumber, ucrNumber, inActiveStatus.toInt())
    }

    @GetMapping("/cd/{ucrNumber}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myCdDetails(
            @PathVariable(value = "ucrNumber", required = false) ucrNumber: String
    ): String? {
        val gson = Gson()
        return gson.toJson(destinationInspectionDaoServices.findCdWithUcrNumberLatest(ucrNumber))
    }

//    @GetMapping("rfc/{idfNumber}/{ucrNumber}", produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun myRFCDetails(
//            @PathVariable(value = "idfNumber", required = false) idfNumber: String,
//            @PathVariable(value = "ucrNumber", required = false) ucrNumber: String
//    ): Boolean {
//        var result = false
//        rfcsRepo.findByIdfNumberAndUcrNumber(idfNumber,ucrNumber)
//                .let { idfEntity ->
//                    result = idfEntity.ucr == ucrNumber
//                }
//        return result
//    }


}
