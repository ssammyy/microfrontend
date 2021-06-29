package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.MasterDataDaoService
import org.kebs.app.kotlin.apollo.common.dto.*
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.badRequest
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body

@Component
class MasterDataHandler(
    private val daoService: MasterDataDaoService,
) {
    @PreAuthorize("hasAuthority('DESIGNATIONS_VIEW')")
    fun designationsUi(req: ServerRequest) = ok().render("admin/masters/designations-crud")

    @PreAuthorize("hasAuthority('DIRECTORATES_VIEW')")
    fun directoratesUi(req: ServerRequest) = ok().render("admin/masters/directorates-crud")

    @PreAuthorize("hasAuthority('DEPARTMENTS_VIEW')")
    fun departmentsUi(req: ServerRequest) = ok().render("admin/masters/departments-crud")

    @PreAuthorize("hasAuthority('DIVISIONS_VIEW')")
    fun divisionsUi(req: ServerRequest) = ok().render("admin/masters/divisions-crud")

    @PreAuthorize("hasAuthority('SECTIONS_VIEW')")
    fun sectionsUi(req: ServerRequest) = ok().render("admin/masters/sections-crud")

    @PreAuthorize("hasAuthority('SUBSECTIONS_L1_VIEW')")
    fun subSectionsL1Ui(req: ServerRequest) = ok().render("admin/masters/sub-sections-l1-crud")

    @PreAuthorize("hasAuthority('SUBSECTIONS_L2_VIEW')")
    fun subSectionsL2Ui(req: ServerRequest) = ok().render("admin/masters/sub-sections-l2-crud")

    @PreAuthorize("hasAuthority('REGIONS_VIEW')")
    fun regionsUi(req: ServerRequest) = ok().render("admin/masters/regions-crud")

    @PreAuthorize("hasAuthority('SUB_REGIONS_VIEW')")
    fun subRegionsUi(req: ServerRequest) = ok().render("admin/masters/sub-regions-crud")

    @PreAuthorize("hasAuthority('COUNTIES_VIEW')")
    fun countiesUi(req: ServerRequest) = ok().render("admin/masters/counties-crud")

    @PreAuthorize("hasAuthority('TOWNS_VIEW')")
    fun townsUi(req: ServerRequest) = ok().render("admin/masters/towns-crud")


    @PreAuthorize("hasAuthority('DIVISIONS_WRITE')")
    fun divisionsUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<DivisionsEntityDto>()
            daoService.updateDivision(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('DIRECTORATES_WRITE')")
    fun directoratesUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<DirectoratesEntityDto>()
            daoService.updateDirectorate(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('REGIONS_WRITE')")
    fun regionsUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<RegionsEntityDto>()
            daoService.updateRegion(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('SUB_REGIONS_WRITE')")
    fun subRegionsUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<SubRegionsEntityDto>()
            daoService.updateSubRegion(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('DESIGNATIONS_WRITE')")
    fun designationsUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<DesignationEntityDto>()
            daoService.updateDesignations(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('DEPARTMENTS_WRITE')")
    fun departmentsUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<DepartmentsEntityDto>()
            daoService.updateDepartments(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('SECTIONS_WRITE')")
    fun sectionsUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<SectionsEntityDto>()
            daoService.updateSection(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('SUBSECTIONS_L1_WRITE')")
    fun subSectionsL1Update(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<SubSectionsL1EntityDto>()
            daoService.updateSubSectionL1(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('SUBSECTIONS_L2_WRITE')")
    fun subSectionsL2Update(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<SubSectionsL2EntityDto>()
            daoService.updateSubSectionL2(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('COUNTIES_WRITE')")
    fun countiesUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<CountiesEntityDto>()
            daoService.updateCounties(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('TOWNS_WRITE')")
    fun townsUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<TownsEntityDto>()
            daoService.updateTowns(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('STANDARD_PRODUCT_CATEGORY_WRITE')")
    fun standardProductCategoryUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<StandardProductCategoryEntityDto>()
            daoService.updateStandardProductCategory(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    //    @PreAuthorize("hasAuthority('STANDARD_PRODUCT_CATEGORY_WRITE')")
    fun userRequestTypeUpdate(req: ServerRequest): ServerResponse {
        try {
            val entity = req.body<UserRequestTypesEntityDto>()
            daoService.updateUserRequestType(entity)
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('DESIGNATIONS_LIST')")
    fun designationsListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllDesignations()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getDesignationsByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('DEPARTMENTS_LIST')")
    fun departmentsListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllDepartments()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getDepartmentsByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('DIVISIONS_LIST')")
    fun divisionsListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllDivisions()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getDivisionsByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('DIRECTORATES_LIST')")
    fun directoratesListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllDirectorates()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getDirectoratesByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

//    @PreAuthorize("isAnonymous()")
    fun regionsListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllRegions()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getRegionsByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('SUB_REGIONS_LIST')")
    fun subRegionsListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllSubRegions()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getSubRegionsByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('SECTIONS_LIST')")
    fun sectionsListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllSections()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getSectionsByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('SUBSECTIONS_L1_LIST')")
    fun subSectionsL1Listing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllSubSectionsL1()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getSubSectionsL1ByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('SUBSECTIONS_L2_LIST')")
    fun subSectionsL2Listing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllSubSectionsL2()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getSubSectionsL2ByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }


    //    @PreAuthorize("hasAuthority('COUNTIES_LIST')")
    @PreAuthorize("isAnonymous()")
    fun countiesListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllCounties()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getCountiesByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("hasAuthority('TOWNS_LIST')")
    fun regionCountyTownListing(req: ServerRequest): ServerResponse =
        try {
            daoService.getRegionCountyTown()
                ?.let { ok().body(it) }
                ?: throw NullValueNotAllowedException("No records found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

    //    @PreAuthorize("hasAuthority('TOWNS_LIST')")
    @PreAuthorize("isAnonymous()")
    fun regionSubRegionListing(req: ServerRequest): ServerResponse =
        try {
            daoService.getRegionSubRegion()
                ?.let { ok().body(it) }
                ?: throw NullValueNotAllowedException("No records found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

    @PreAuthorize("hasAuthority('TOWNS_LIST')")
    fun getDirectorateDesignationsViewDtoListing(req: ServerRequest): ServerResponse =
        try {
            daoService.getDirectorateDesignationsViewDto()
                ?.let { ok().body(it) }
                ?: throw NullValueNotAllowedException("No records found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

    @PreAuthorize("hasAuthority('TOWNS_LIST')")
    fun getDirectorateToSubSectionL2ViewDtoListing(req: ServerRequest): ServerResponse =
        try {
            daoService.getDirectorateToSubSectionL2ViewDto()
                ?.let { ok().body(it) }
                ?: throw NullValueNotAllowedException("No records found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            badRequest().body(e.message ?: "Unknown Error")
        }

//    @PreAuthorize("hasAuthority('TOWNS_LIST')")
    @PreAuthorize("isAnonymous()")
    fun townsListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllTowns()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getTownsByStatus(status)

                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


}

    @PreAuthorize("isAnonymous()")
    fun townsListingByCountyId(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            val countyId = try {
                req.pathVariable("id").toLong()

            } catch (e: Exception) {
                throw e
            }
            when {
                status <= 1 -> {
                    daoService.getAllTownsByCountyId(countyId, 1)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getAllTownsByCountyId(countyId, status)

                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("isAnonymous()")
    fun notSupported(req: ServerRequest): ServerResponse = badRequest().body("Invalid Request: Not supported")

    @PreAuthorize("isAnonymous()")
    fun businessLinesListing(req: ServerRequest): ServerResponse {
        try {
            daoService.getAllBusinessLines()
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("No records found")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    @PreAuthorize("isAnonymous()")
    fun businessNaturesListing(req: ServerRequest): ServerResponse {
        try {
            daoService.getAllBusinessNatures()
                ?.let {
                    return ok().body(it)
                }
                ?: throw NullValueNotAllowedException("No records found")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    //    @PreAuthorize("hasAuthority('STANDARD_PRODUCT_CATEGORY_LIST')")
    fun standardProductCategoryListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllStandardProductCategory()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getStandardProductCategoryByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

    fun userRequestTypeListing(req: ServerRequest): ServerResponse {
        try {
            val status = try {
                req.pathVariable("status").toInt()
            } catch (e: Exception) {
                -1
            }
            when {
                status <= 1 -> {
                    daoService.getAllUserRequestTypes()
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
                else -> {
                    daoService.getUserRequestTypesByStatus(status)
                        ?.let {
                            return ok().body(it)
                        }
                        ?: throw NullValueNotAllowedException("No records found")

                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return badRequest().body(e.message ?: "Unknown Error")
        }


    }

}