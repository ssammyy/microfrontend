package org.kebs.app.kotlin.apollo.api.controllers

import com.google.gson.Gson
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/system/admin/")
class commonJSONControllers(
        private val applicationMapProperties: ApplicationMapProperties,
        private val iPvocApplicationRepo: IPvocApplicationRepo,
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
        private val daoServices: CommonDaoServices,
        private val regionsRepository: IRegionsRepository
) {
    private val userRegistration: Int = applicationMapProperties.mapUserRegistration
    private val activeStatus: Int = 1
    private val inActiveStatus: Int = 0


    @GetMapping("users/view/notification/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myNotification(
            @PathVariable(value = "id", required = false) id: Long
    ): String? {
        daoServices.loggedInUserDetails()
                .let { usersEntity ->
                    usersEntity.email?.let {
                        daoServices.findNotification(id, it)
                                .let { notificationsBufferEntity ->
                                    daoServices.updateNotification(notificationsBufferEntity, usersEntity)
                                    val gson = Gson()
                                    return gson.toJson(notificationsBufferEntity)
                                }
                    }
                            ?: throw Exception("Missing Email details, recheck Directorate")
                }
    }

    @GetMapping("counties/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myCounties(
            @PathVariable(value = "id") id: Long
    ): String? {
//        regionsRepository.findByIdOrNull(id)
//                ?.let {regionEntity ->
        val gson = Gson()
        return gson.toJson(countiesRepo.findByRegionIdAndStatus(id, activeStatus))
//                }
//                ?: throw Exception("Missing Directorate [id=$id], recheck Directorate")

    }

    @GetMapping("towns/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myTowns(
            @PathVariable(value = "id") id: Long
    ): String? {
        countiesRepo.findByIdOrNull(id)
                ?.let { countyEntity ->
                    val gson = Gson()
                    return gson.toJson(townsRepo.findByCountiesAndStatus(countyEntity, activeStatus))
                }
                ?: throw Exception("Missing Directorate [id=$id], recheck Directorate")

    }

    @GetMapping("sub-region/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun mySubRegions(
            @PathVariable(value = "id") id: Long
    ): String? {
        regionsRepository.findByIdOrNull(id)
                ?.let { regionEntity ->
                    val gson = Gson()
                    return gson.toJson(subRegionsRepository.findByRegionIdAndStatus(regionEntity, activeStatus))
                }
                ?: throw Exception("Missing Directorate [id=$id], recheck Directorate")

    }

    @GetMapping("business-nature-list/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myBusinessNature(
            @PathVariable(value = "id") id: Long
    ): String? {
        val gson = Gson()
        return gson.toJson(daoServices.findBusinessNatureListByBusinessNatureLine(daoServices.findBusinessLineEntityByID(id, activeStatus), activeStatus))
    }

    @GetMapping("county-list/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myCountyList(
            @PathVariable(value = "id") id: Long
    ): String? {
        val gson = Gson()
        return gson.toJson(daoServices.findCountyListByRegion(id, activeStatus))
    }

    @GetMapping("town-list/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myTownList(
            @PathVariable(value = "id") id: Long
    ): String? {
        val gson = Gson()
        return gson.toJson(daoServices.findTownListByCountyID(daoServices.findCountiesEntityByCountyId(id, activeStatus), activeStatus))
    }

    @GetMapping("designation-list/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myDesignation(
            @PathVariable(value = "id") id: Long
    ): String? {
        directorateRepo.findByIdOrNull(id)
                ?.let { directorateEntity ->
                    val gson = Gson()
                    return gson.toJson(designationRepository.findByDirectorateIdAndStatus(directorateEntity, activeStatus))
                }
                ?: throw Exception("Missing Directorate [id=$id], recheck Directorate")

    }

    @GetMapping("division-list/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myDivision(
            @PathVariable(value = "id") id: Long
    ): String? {
        val gson = Gson()
        return gson.toJson(divisionsRepo.findByDepartmentIdAndStatus(daoServices.findDepartmentByID(id), activeStatus))


    }

    @GetMapping("section-list/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun mySection(
            @PathVariable(value = "id") id: Long
    ): String? {
        divisionsRepo.findByIdOrNull(id)
                ?.let { divisionEntity ->
                    val gson = Gson()
                    return gson.toJson(sectionsRepo.findByDivisionIdAndStatus(divisionEntity, activeStatus))
                }
                ?: throw Exception("Missing Division [id=$id], recheck Division ID")

    }

    @GetMapping("sub-section-l1-list/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun mySubSectionL1(
            @PathVariable(value = "id") id: Long
    ): String? {
        sectionsRepo.findByIdOrNull(id)
                ?.let { sectionEntity ->
                    val gson = Gson()
                    return gson.toJson(SubSectionsLevel1Repo.findBySectionIdAndStatus(sectionEntity, activeStatus))
                }
                ?: throw Exception("Missing Section [id=$id], recheck Section ID")

    }

    @GetMapping("sub-section-l2-list/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun mySubSectionL2(
            @PathVariable(value = "id") id: Long
    ): String? {
        SubSectionsLevel1Repo.findByIdOrNull(id)
                ?.let { subSectionLevel1Entity ->
                    val gson = Gson()
                    return gson.toJson(SubSectionsLevel2Repo.findBySubSectionLevel1IdAndStatus(subSectionLevel1Entity, activeStatus))
                }
                ?: throw Exception("Missing Sub-Section-Level1 [id=$id], recheck Sub-Section-Level1  ID")

    }

    @GetMapping("get-departments/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myDepartments(
            @PathVariable(value = "id") id: Long
    ): String? {
        val gson = Gson()
        directorateRepo.findByIdOrNull(id)
                ?.let { directoratesEntity ->
                    return gson.toJson(departmentsRepo.findByDirectorateIdAndStatus(directoratesEntity, activeStatus))
                }
                ?: throw Exception("Missing Directorate [id=$id], recheck Directorate")

    }

    @GetMapping("email-check/{email}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun myEmail(
            @PathVariable(value = "email", required = false) email: String
    ): String? {
        var result = "notExist"
        usersRepo.findByEmail(email)
                .let { usersEntity ->
                    if (usersEntity?.email == email) {
                        result = email
                    } else {
                        result
                    }
                }
        return result
    }


}
