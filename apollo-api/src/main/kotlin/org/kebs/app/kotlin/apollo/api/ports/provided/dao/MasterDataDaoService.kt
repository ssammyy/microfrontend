package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.common.dto.*
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.registration.UserRequestTypesEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ICfsTypeCodesRepository
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.ICfgKebsMsOgaRepository
import org.kebs.app.kotlin.apollo.store.repo.ms.IPredefinedResourcesRequiredRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitRatingRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.EntityManager

@Service
class MasterDataDaoService(
    private val commonDaoServices: CommonDaoServices,
    private val designationsRepo: IDesignationsRepository,
    private val departmentsRepo: IDepartmentsRepository,
    private val divisionsRepo: IDivisionsRepository,
    private val directoratesRepo: IDirectoratesRepository,
    private val regionsRepo: IRegionsRepository,
    private val subRegionsRepo: ISubRegionsRepository,
    private val sectionsRepo: ISectionsRepository,
    private val subSectionsL1Repo: ISubSectionsLevel1Repository,
    private val subSectionsL2Repo: ISubSectionsLevel2Repository,
    private val iCfsTypeCodesRepo: ICfsTypeCodesRepository,
    private val countiesRepo: ICountiesRepository,
    private val townsRepo: ITownsRepository,
    private val userRequestTypesRepo: IUserRequestTypesRepository,
    private val standardCategoryRepo: IStandardCategoryRepository,
    private val sampleStandardsRepo: ISampleStandardsRepository,
    private val laboratoryRepo: ILaboratoryRepository,
    private val predefinedResourcesRequiredRepo: IPredefinedResourcesRequiredRepository,
    private val cfgKebsMsOgaRepo: ICfgKebsMsOgaRepository,
    private val productCategoriesRepo: IKebsProductCategoriesRepository,
    private val countriesRepo: ICountriesRepository,
    private val broadProductCategoryRepo: IBroadProductCategoryRepository,
    private val productSubCategoryRepo: IProductSubcategoryRepository,
    private val productsRepo: IProductsRepository,
    private val businessLinesRepo: IBusinessLinesRepository,
    private val businessNatureRepo: IBusinessNatureRepository,
    private val entityManager: EntityManager,
    private val iPermitRatingRepo: IPermitRatingRepository,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val iUserTypesEntityRepository: IUserTypesEntityRepository,
    private val iUserRepository: IUserRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val notifications: Notifications,


    ) {

    fun getAllCompanies(): List<UserCompanyEntityDto>? = companyProfileRepo.findAll()
        .sortedBy { it.id }
        .map {
            UserCompanyEntityDto(
                it.name,
                it.kraPin,
                it.userId,
                null,
                it.registrationNumber,
                it.postalAddress,
                it.physicalAddress,
                it.plotNumber,
                it.companyEmail,
                it.companyTelephone,
                it.yearlyTurnover,
                it.businessLines,
                it.businessNatures,
                it.buildingName,
                null,
                it.streetName,
                it.directorIdNumber,
                it.region,
                it.county,
                it.town,
                null,
                null,
                null,
                null,
                iPermitRatingRepo.findByIdOrNull(it.firmCategory)?.firmType
            ).apply {
                id = it.id
                status = it.status
            }
        }


    fun getAllTivets(): List<CompanyProfileEntity>? =
        companyProfileRepo.findAllByVarField1IsNotNullOrderByIdDesc()?.sortedBy { it.id }


    fun getAllFirmType(): List<FirmTypeEntityDto>? = iPermitRatingRepo.findAll()
        .sortedBy { it.id }
        .map {
            FirmTypeEntityDto(
                it.id,
                it.min,
                it.max,
                it.firmFee,
                it.productFee,
                it.extraProductFee,
                it.countBeforeFee,
                it.countBeforeFree,
                it.validity,
                it.invoiceDesc,
                it.firmType,
            )
        }

    fun getAllFirmTypeByStatus(status: Int): List<FirmTypeEntityDto>? = iPermitRatingRepo.findAllByStatus(status)
        ?.sortedBy { it.id }
        ?.map {
            FirmTypeEntityDto(
                it.id,
                it.min,
                it.max,
                it.firmFee,
                it.productFee,
                it.extraProductFee,
                it.countBeforeFee,
                it.countBeforeFree,
                it.validity,
                it.invoiceDesc,
                it.firmType,
            )
        }

    fun getAllDepartments(): List<DepartmentsEntityDto>? = departmentsRepo.findAll()
        .sortedBy { it.id }
        .map { DepartmentsEntityDto(it.id, it.department, it.descriptions, it.directorateId?.id, it.status == 1) }

    fun getDepartmentsByStatus(status: Int): List<DepartmentsEntityDto>? =
        departmentsRepo.findByStatusOrderByDepartment(status)
            ?.sortedBy { it.id }
            ?.map { DepartmentsEntityDto(it.id, it.department, it.descriptions, it.directorateId?.id, it.status == 1) }

    fun getAllDivisions(): List<DivisionsEntityDto>? = divisionsRepo.findAll()
        .sortedBy { it.id }
        .map {
            DivisionsEntityDto(
                it.id,
                it.division,
                it.departmentId?.id,
                it.descriptions,
                it.departmentId?.directorateId?.id,
                it.status == 1
            )
        }

    fun getDivisionsByStatus(status: Int): List<DivisionsEntityDto>? = divisionsRepo.findByStatusOrderByDivision(status)
        ?.sortedBy { it.id }
        ?.map {
            DivisionsEntityDto(
                it.id,
                it.division,
                it.departmentId?.id,
                it.descriptions,
                it.departmentId?.directorateId?.id,
                it.status == 1
            )
        }

    fun getAllDirectorates(): List<DirectoratesEntityDto>? = directoratesRepo.findAll()
        .sortedBy { it.id }
        .map { DirectoratesEntityDto(it.id, it.directorate, it.status == 1) }

    fun getDirectoratesByStatus(status: Int): List<DirectoratesEntityDto>? =
        directoratesRepo.findByStatusOrderByDirectorate(status)
            ?.sortedBy { it.id }
            ?.map { DirectoratesEntityDto(it.id, it.directorate, it.status == 1) }

    fun getAllStandardProductCategory(): List<StandardProductCategoryEntityDto>? =
        standardCategoryRepo.findAll().sortedBy { it.id }.sortedBy { it.id }.map {
            StandardProductCategoryEntityDto(
                it.id,
                it.standardCategory,
                it.standardNickname,
                it.standardId,
                it.status == 1
            )
        }


    fun getAllStandardsDetails(): List<KebsStandardsDto>? =
        sampleStandardsRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
            .map { KebsStandardsDto(it.id, it.standardTitle, it.standardNumber, it.status == 1) }

    fun getAllLaboratories(): List<LaboratoryEntityDto>? =
        laboratoryRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
            .map { LaboratoryEntityDto(it.id, it.labName, it.description, it.status == 1) }

    fun getAllPredefinedResourcesRequired(): List<PredefinedResourcesRequiredEntityDto>? =
        predefinedResourcesRequiredRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
            .map { PredefinedResourcesRequiredEntityDto(it.id, it.resourceName, it.status == 1) }

    fun getAllOGAList(): List<OGAEntity>? = cfgKebsMsOgaRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
        .map { OGAEntity(it.id, it.ogaName, it.status == 1) }

    fun getStandardProductCategoryByStatus(status: Int): List<StandardProductCategoryEntityDto>? =
        standardCategoryRepo.findByStatusOrderByStandardCategory(status)?.sortedBy { it.id }?.sortedBy { it.id }?.map {
            StandardProductCategoryEntityDto(
                it.id,
                it.standardCategory,
                it.standardNickname,
                it.standardId,
                it.status == 1
            )
        }

    fun getAllUserRequestTypes(): List<UserRequestTypesEntityDto>? =
        userRequestTypesRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
            .map { UserRequestTypesEntityDto(it.id, it.userRequest, it.description, it.status == 1) }

    fun getUserRequestTypesByStatus(status: Int): List<UserRequestTypesEntityDto>? =
        userRequestTypesRepo.findByStatusOrderByUserRequest(status)?.sortedBy { it.id }?.sortedBy { it.id }
            ?.map { UserRequestTypesEntityDto(it.id, it.userRequest, it.description, it.status == 1) }

    fun getAllCountries(): List<CountriesEntityDto>? = countriesRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
        .map { CountriesEntityDto(it.id, it.country, it.status == 1) }

    fun getAllProductCategories(): List<ProductCategoriesEntityDto>? =
        productCategoriesRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
            .map { ProductCategoriesEntityDto(it.id, it.name, it.status == 1, it.broadProductCategoryId) }

    fun getProductCategoriesByStatus(status: Int): List<ProductCategoriesEntityDto>? =
        productCategoriesRepo.findByStatusOrderByName(status)?.sortedBy { it.id }?.sortedBy { it.id }
            ?.map { ProductCategoriesEntityDto(it.id, it.name, it.status == 1, it.broadProductCategoryId) }

    fun getAllBroadProductCategory(): List<BroadProductCategoryEntityDto>? =
        broadProductCategoryRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
            .map { BroadProductCategoryEntityDto(it.id, it.category, it.status == 1, it.divisionId?.id) }

    fun getBroadProductCategoryByStatus(status: Int): List<BroadProductCategoryEntityDto>? =
        broadProductCategoryRepo.findByStatusOrderByCategory(status)?.sortedBy { it.id }?.sortedBy { it.id }
            ?.map { BroadProductCategoryEntityDto(it.id, it.category, it.status == 1, it.divisionId?.id) }

    fun getAllProducts(): List<ProductsEntityDto>? = productsRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
        .map { ProductsEntityDto(it.id, it.name, it.status == 1, it.productCategoryId) }

    fun getProductsByStatus(status: Int): List<ProductsEntityDto>? =
        productsRepo.findByStatusOrderByName(status)?.sortedBy { it.id }?.sortedBy { it.id }
            ?.map { ProductsEntityDto(it.id, it.name, it.status == 1, it.productCategoryId) }

    fun getAllProductSubcategory(): List<ProductSubcategoryEntityDto>? =
        productSubCategoryRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
            .map { ProductSubcategoryEntityDto(it.id, it.name, it.status == 1, it.productId) }

    fun getProductSubcategoryByStatus(status: Int): List<ProductSubcategoryEntityDto>? =
        productSubCategoryRepo.findByStatusOrderByName(status)?.sortedBy { it.id }?.sortedBy { it.id }
            ?.map { ProductSubcategoryEntityDto(it.id, it.name, it.status == 1, it.productId) }

    fun getRegionCountyTown(): List<RegionsCountyTownViewDto>? = entityManager.createNamedQuery(
        RegionsCountyTownViewDto.FIND_ALL,
        RegionsCountyTownViewDto::class.java
    ).resultList

    fun getRegionSubRegion(): List<RegionSubRegionViewDto>? =
        entityManager.createNamedQuery(RegionSubRegionViewDto.FIND_ALL, RegionSubRegionViewDto::class.java).resultList

    fun getDirectorateDesignationsViewDto(): List<DirectorateDesignationsViewDto>? = entityManager.createNamedQuery(
        DirectorateDesignationsViewDto.FIND_ALL,
        DirectorateDesignationsViewDto::class.java
    ).resultList

    fun getDirectorateToSubSectionL2ViewDto(): List<DirectorateToSubSectionL2ViewDto>? = entityManager.createNamedQuery(
        DirectorateToSubSectionL2ViewDto.FIND_ALL,
        DirectorateToSubSectionL2ViewDto::class.java
    ).resultList

    fun getAllCounties(): List<CountiesEntityDto>? = countiesRepo.findAll().sortedBy { it.id }.sortedBy { it.id }
        .map { CountiesEntityDto(it.id, it.county, it.regionId, it.status == 1) }

    fun getCountiesByStatus(status: Int): List<CountiesEntityDto>? =
        countiesRepo.findByStatusOrderByCounty(status)?.sortedBy { it.id }
            ?.map { CountiesEntityDto(it.id, it.county, it.regionId, it.status == 1) }

    fun getAllSubRegions(): List<SubRegionsEntityDto>? =
        subRegionsRepo.findAll().sortedBy<SubRegionsEntity, Long> { it.id }
            .map { SubRegionsEntityDto(it.id, it.subRegion, it.descriptions, it.regionId?.id, it.status == 1) }

    fun getSubRegionsByStatus(status: Int): List<SubRegionsEntityDto>? =
        subRegionsRepo.findByStatusOrderBySubRegion(status)?.sortedBy { it.id }
            ?.map { SubRegionsEntityDto(it.id, it.subRegion, it.descriptions, it.regionId?.id, it.status == 1) }

    fun getAllSections(): List<SectionsEntityDto>? = sectionsRepo.findAll().sortedBy { it.id }
        .map { SectionsEntityDto(it.id, it.section, it.divisionId?.id, it.descriptions, it.status == 1) }

    fun getSectionsByStatus(status: Int): List<SectionsEntityDto>? = sectionsRepo.findByStatusOrderBySection(status)
        ?.map { SectionsEntityDto(it.id, it.section, it.divisionId?.id, it.descriptions, it.status == 1) }

    fun getAllSubSectionsL1(): List<SubSectionsL1EntityDto>? = subSectionsL1Repo.findAll().sortedBy { it.id }
        .map { SubSectionsL1EntityDto(it.id, it.subSection, it.sectionId?.id, it.status == 1) }

    fun getSubSectionsL1ByStatus(status: Int): List<SubSectionsL1EntityDto>? =
        subSectionsL1Repo.findByStatusOrderBySubSection(status)?.sortedBy { it.id }
            ?.map { SubSectionsL1EntityDto(it.id, it.subSection, it.sectionId?.id, it.status == 1) }

    fun getAllSubSectionsL2(): List<SubSectionsL2EntityDto>? = subSectionsL2Repo.findAll().sortedBy { it.id }
        .map { SubSectionsL2EntityDto(it.id, it.subSection, it.subSectionLevel1Id?.id, it.status == 1) }

    fun getSubSectionsL2ByStatus(status: Int): List<SubSectionsLevel2Entity>? =
        subSectionsL2Repo.findByStatusOrderBySubSection(status)

    fun getAllTowns(): List<TownsEntityDto>? =
        townsRepo.findAll().sortedBy { it.id }.map { TownsEntityDto(it.id, it.town, it.counties?.id, it.status == 1) }

    fun getAllCFS(): List<FreightStationsDto>? =
        iCfsTypeCodesRepo.findAll().sortedBy { it.id }
            .map { FreightStationsDto(it.id, it.cfsCode, it.cfsName, it.description, it.status == 1) }

    fun getAllTownsByCountyId(countyId: Long, status: Int): List<TownsEntityDto>? =
        townsRepo.findByCountyIdAndStatus(countyId, status)?.sortedBy { it.id }
            ?.map { TownsEntityDto(it.id, it.town, it.counties?.id, it.status == 1) }

    fun getTownsByStatus(status: Int): List<TownsEntityDto>? =
        townsRepo.findByStatusOrderByTown(status)?.sortedBy { it.id }
            ?.map { TownsEntityDto(it.id, it.town, it.counties?.id, it.status == 1) }

    fun getCFSByStatus(status: Int): List<FreightStationsDto>? =
        iCfsTypeCodesRepo.findByStatusOrderByCfsName(status)?.sortedBy { it.id }
            ?.map { FreightStationsDto(it.id, it.cfsCode, it.cfsName, it.description, it.status == 1) }


    fun getAllRegions(): List<RegionsEntityDto>? = regionsRepo.findAll().sortedBy { it.id }
        .map { RegionsEntityDto(it.id, it.region, it.descriptions, it.status == 1) }.sortedBy { it.id }

    fun getRegionsByStatus(status: Int): List<RegionsEntityDto>? = regionsRepo.findByStatusOrderByRegion(status)
        ?.map { RegionsEntityDto(it.id, it.region, it.descriptions, it.status == 1) }?.sortedBy { it.id }

    fun getAllBusinessLines(): List<BusinessLinesEntityDto>? = businessLinesRepo.findAll().sortedBy { it.id }
        .map { BusinessLinesEntityDto(it.id, it.name, it.descriptions, it.status == 1) }

    fun getAllBusinessNatures(): List<BusinessNatureEntityDto>? = businessNatureRepo.findAll().sortedBy { it.id }
        .map { BusinessNatureEntityDto(it.id, it.businessLinesId?.id, it.name, it.descriptions, it.status == 1) }

    fun getAllDesignations(): List<DesignationEntityDto>? = designationsRepo.findAll()
        .sortedBy { it.id }
        .map {
            DesignationEntityDto(
                it.id,
                it.designationName,
                it.descriptions,
                it.status == 1,
                it.directorateId?.id
            )
        }

    fun getDesignationsByStatus(status: Int): List<DesignationEntityDto>? = designationsRepo.findByStatus(status)
        ?.sortedBy { it.id }
        ?.map {
            DesignationEntityDto(
                it.id,
                it.designationName,
                it.descriptions,
                it.status == 1,
                it.directorateId?.id
            )
        }

    fun updateDesignations(entity: DesignationEntityDto): DesignationsEntity? {
        when {
            entity.id ?: -1L < 1L -> {
                val d = DesignationsEntity()
                d.designationName = entity.designationName
                d.descriptions = entity.descriptions
                when (entity.status) {
                    true -> d.status = 1
                    else -> d.status = 0
                }

                d.directorateId = directoratesRepo.findByIdOrNull(entity.directorateId)
                d.createdBy = commonDaoServices.loggedInUserDetails().userName
                d.createdOn = Timestamp.from(Instant.now())
                return designationsRepo.save(d)
            }

            else -> {
                designationsRepo.findByIdExcludingJoin(entity.id)
                    ?.let { r ->
                        r.designationName = entity.designationName
                        when (entity.status) {
                            true -> r.status = 1
                            else -> r.status = 0
                        }
                        r.descriptions = entity.descriptions
                        r.directorateId = directoratesRepo.findByIdOrNull(entity.directorateId)

                        r.lastModifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.lastModifiedOn = Timestamp.from(Instant.now())
                        return designationsRepo.save(r)
                    }
                    ?: throw NullValueNotAllowedException("Record id=${entity.id} not found, check and try again")
            }
        }

    }

    fun updateDepartments(entity: DepartmentsEntityDto): DepartmentsEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val d = DepartmentsEntity()
                d.department = entity.department
                d.descriptions = entity.descriptions
                when (entity.status) {
                    true -> d.status = 1
                    else -> d.status = 0
                }
                d.directorateId = directoratesRepo.findByIdOrNull(entity.directorateId)
                d.createdBy = commonDaoServices.loggedInUserDetails().userName
                d.createdOn = Timestamp.from(Instant.now())
                departmentsRepo.save(d)
                return entity
            }

            else -> {
                departmentsRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.department = entity.department
                        when (entity.status) {
                            true -> r.status = 1
                            else -> r.status = 0
                        }
                        r.descriptions = entity.descriptions

                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        departmentsRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateDivision(entity: DivisionsEntityDto): DivisionsEntityDto? {
        when {
            entity.id ?: 0 < 1L -> {
                val d = DivisionsEntity()
                d.division = entity.division
                d.departmentId = departmentsRepo.findByIdOrNull(entity.departmentId)
                d.descriptions = entity.descriptions
                when (entity.status) {
                    true -> d.status = 1
                    else -> d.status = 0
                }
                d.createdBy = commonDaoServices.loggedInUserDetails().userName
                d.createdOn = Timestamp.from(Instant.now())
                divisionsRepo.save(d)
                return entity
            }

            else -> {
                divisionsRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.division = entity.division
                        when (entity.status) {
                            true -> r.status = 1
                            else -> r.status = 0
                        }
                        r.descriptions = entity.descriptions

                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        divisionsRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateDirectorate(entity: DirectoratesEntityDto): DirectoratesEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val d = DirectoratesEntity()
                d.directorate = entity.directorate
                when (entity.status) {
                    true -> d.status = 1
                    else -> d.status = 0
                }

                d.createdBy = commonDaoServices.loggedInUserDetails().userName
                d.createdOn = Timestamp.from(Instant.now())
                directoratesRepo.save(d)
                return entity
            }

            else -> {
                directoratesRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.directorate = entity.directorate
                        when (entity.status) {
                            true -> r.status = 1
                            else -> r.status = 0
                        }
                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        directoratesRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateRegion(entity: RegionsEntityDto): RegionsEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = RegionsEntity()
                r.region = entity.region
                r.descriptions = entity.descriptions
                if (entity.status == true) r.status = 1 else r.status = 0
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                regionsRepo.save(r)
                return entity
            }

            else -> {
                regionsRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.region = entity.region
                        if (entity.status == true) r.status = 1 else r.status = 0
                        r.descriptions = entity.descriptions

                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        regionsRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateSubRegion(entity: SubRegionsEntityDto): SubRegionsEntity? {
        when {
            (entity.id ?: 0L) < 1L -> {
                val r = SubRegionsEntity()
                r.subRegion = entity.subRegion
                r.descriptions = entity.descriptions
                if (entity.status == true) r.status = 1 else r.status = 0
                r.regionId = regionsRepo.findByIdOrNull(entity.regionId)
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                return subRegionsRepo.save(r)
            }

            else -> {
                subRegionsRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.subRegion = entity.subRegion
                        r.descriptions = entity.descriptions
                        if (entity.status == true) r.status = 1 else r.status = 0
                        r.regionId = regionsRepo.findByIdOrNull(entity.regionId)

                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        return subRegionsRepo.save(r)
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateSection(entity: SectionsEntityDto): SectionsEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = SectionsEntity()
                r.section = entity.section
                r.divisionId = divisionsRepo.findByIdOrNull(entity.divisionId)
                if (entity.status == true) r.status = 1 else r.status = 0
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                sectionsRepo.save(r)
                return entity
            }

            else -> {
                sectionsRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.section = entity.section
                        r.divisionId = divisionsRepo.findByIdOrNull(entity.divisionId)
                        if (entity.status == true) r.status = 1 else r.status = 0
                        r.descriptions = entity.descriptions

                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        sectionsRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateSubSectionL1(entity: SubSectionsL1EntityDto): SubSectionsL1EntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = SubSectionsLevel1Entity()
                r.subSection = entity.subSection
                r.sectionId = sectionsRepo.findByIdOrNull(entity.sectionId)
                if (entity.status == true) r.status = 1 else r.status = 0
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                subSectionsL1Repo.save(r)
                return entity
            }

            else -> {
                subSectionsL1Repo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.subSection = entity.subSection
                        r.sectionId = sectionsRepo.findByIdOrNull(entity.sectionId)
                        if (entity.status == true) r.status = 1 else r.status = 0

                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        subSectionsL1Repo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateSubSectionL2(entity: SubSectionsL2EntityDto): SubSectionsL2EntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = SubSectionsLevel2Entity()
                r.subSection = entity.subSection
                r.subSectionLevel1Id = subSectionsL1Repo.findByIdOrNull(entity.subSectionL1Id)
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                if (entity.status == true) r.status = 1 else r.status = 0
                subSectionsL2Repo.save(r)
                return entity
            }

            else -> {
                subSectionsL2Repo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.subSection = entity.subSection
                        r.subSectionLevel1Id = subSectionsL1Repo.findByIdOrNull(entity.subSectionL1Id)
                        if (entity.status == true) r.status = 1 else r.status = 0

                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        subSectionsL2Repo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }


    fun updateCounties(entity: CountiesEntityDto): CountiesEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = CountiesEntity()
                r.county = entity.county
                r.regionId = entity.regionId
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                if (entity.status == true) r.status = 1 else r.status = 0
                countiesRepo.save(r)
                return entity
            }

            else -> {
                countiesRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.county = entity.county
                        if (entity.status == true) r.status = 1 else r.status = 0
                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        countiesRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateStandardProductCategory(entity: StandardProductCategoryEntityDto): StandardProductCategoryEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = StandardsCategoryEntity()
                r.standardCategory = entity.standardCategory
                r.standardNickname = entity.standardNickname
                r.standardId = entity.standardId
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                if (entity.status == true) r.status = 1 else r.status = 0
                standardCategoryRepo.save(r)
                return entity
            }

            else -> {
                standardCategoryRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.standardCategory = entity.standardCategory
                        r.standardNickname = entity.standardNickname
                        if (entity.status == true) r.status = 1 else r.status = 0
                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        standardCategoryRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateUserRequestType(entity: UserRequestTypesEntityDto): UserRequestTypesEntityDto? {
        when {
            entity.id ?: 0L < 1L -> {
                val r = UserRequestTypesEntity()
                r.userRequest = entity.userRequest
                r.description = entity.description
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                if (entity.status == true) r.status = 1 else r.status = 0
                userRequestTypesRepo.save(r)
                return entity
            }

            else -> {
                userRequestTypesRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.userRequest = entity.userRequest
                        r.description = entity.description
                        if (entity.status == true) r.status = 1 else r.status = 0
                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        userRequestTypesRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun updateTowns(entity: TownsEntityDto): TownsEntityDto? {
        when {
            (entity.id ?: 0L) < 1L -> {
                val r = TownsEntity()
                r.counties = countiesRepo.findByIdOrNull(entity.countyId)
                r.town = entity.town
                if (entity.status == true) r.status = 1 else r.status = 0
                r.createdBy = commonDaoServices.loggedInUserDetails().userName
                r.createdOn = Timestamp.from(Instant.now())
                townsRepo.save(r)
                return entity
            }

            else -> {
                townsRepo.findByIdOrNull(entity.id)
                    ?.let { r ->
                        r.town = entity.town
                        if (entity.status == true) r.status = 1 else r.status = 0
                        r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                        r.modifiedOn = Timestamp.from(Instant.now())
                        townsRepo.save(r)
                        return entity
                    }
                    ?: throw NullValueNotAllowedException("Record not found, check and try again")
            }
        }

    }

    fun getAllUserTypes(): List<UserTypeEntityDto>? = iUserTypesEntityRepository.findAll().sortedBy { it.id }
        .map { UserTypeEntityDto(it.id, it.varField1, it.descriptions, it.status == 1) }


    fun updateTivetToActive(tivet: CompanyProfileEntity): CompanyProfileEntity? {
        companyProfileRepo.findByIdOrNull(tivet.id)
            ?.let { r ->
                r.varField2 = "1"
                r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                r.modifiedOn = Timestamp.from(Instant.now())
                companyProfileRepo.save(r)
                val user: UsersEntity? = iUserRepository.findByIdOrNull(r.userId)
                if (user != null) {
                    val subject = "Account Activated"
                    val messageBody = "Your Account Has Been Successfully Activated. " +
                            "You can now login onto the portal."
                    user.email?.let { notifications.sendEmail(it, subject, messageBody) }


                    user.enabled = applicationMapProperties.transactionActiveStatus
                    user.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                    user.modifiedOn = Timestamp.from(Instant.now())
                    iUserRepository.save(user)
                }
                return tivet
            }
            ?: throw NullValueNotAllowedException("Record not found, check and try again")


    }

    fun rejectTivet(tivet: CompanyProfileEntity): CompanyProfileEntity? {
        companyProfileRepo.findByIdOrNull(tivet.id)
            ?.let { r ->
                r.varField2 = "1"
                r.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                r.modifiedOn = Timestamp.from(Instant.now())
                val user: UsersEntity? = iUserRepository.findByIdOrNull(r.userId)

                if (user != null) {
                    val subject = "Account Rejected"
                    val messageBody = "Your Account Has Been  Rejected. " +
                            "Please Register Again."
                    user.email?.let { notifications.sendEmail(it, subject, messageBody) }


                    user.enabled = applicationMapProperties.transactionActiveStatus
                    user.modifiedBy = commonDaoServices.loggedInUserDetails().userName
                    user.modifiedOn = Timestamp.from(Instant.now())
                    user.id?.let { iUserRepository.deleteById(it) }
                    r.id?.let { companyProfileRepo.deleteById(it) }

                }
                return tivet
            }
            ?: throw NullValueNotAllowedException("Record not found, check and try again")


    }


}
