package org.kebs.app.kotlin.apollo.api.controllers.msControllers

import com.google.gson.Gson
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/ms/")
class MSJSONControllers(
        private val iLaboratoryRepo: ILaboratoryRepository,
        private val applicationMapProperties: ApplicationMapProperties,
        private val iPvocApplicationRepo: IPvocApplicationRepo,
        private  val usersRepo: IUserRepository,
        private val SubSectionsLevel1Repo: ISubSectionsLevel1Repository,
        private val SubSectionsLevel2Repo: ISubSectionsLevel2Repository,
        private val titlesRepository: ITitlesRepository,
        private val divisionsRepo: IDivisionsRepository,
        private val departmentsRepo: IDepartmentsRepository,
        private val directorateRepo: IDirectoratesRepository,
        private val designationRepository: IDesignationsRepository,
        private val sectionsRepo: ISectionsRepository,
        private val subRegionsRepository: ISubRegionsRepository,
        private val regionsRepository: IRegionsRepository
){
    private val importInspection: Int = applicationMapProperties.mapImportInspection



    @GetMapping("populate-all-labs",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun populateBroadCategory(): String? {
        val gson = Gson()
        return gson.toJson(iLaboratoryRepo.findAll())
    }

    @GetMapping("email-check/{email}",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myEmail(
            @PathVariable(value = "email", required = false) email: String
    ): String? {
        var result = "notExist"
        usersRepo.findByEmail(email)
                .let {usersEntity ->
                    if (usersEntity?.email == email){
                        result = email
                    }else{
                        result
                    }
                }
        return result
    }


}
