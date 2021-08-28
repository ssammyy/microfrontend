package org.kebs.app.kotlin.apollo.api.payload

import org.kebs.app.kotlin.apollo.store.model.CdBlackListUserTargetTypesEntity
import org.kebs.app.kotlin.apollo.store.model.SectionsEntity
import org.kebs.app.kotlin.apollo.store.model.SubSectionsLevel2Entity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionGeneralEntity
import org.kebs.app.kotlin.apollo.store.model.di.CfsTypeCodesEntity
import org.kebs.app.kotlin.apollo.store.model.di.MinistryStationEntity
import java.sql.Date
import javax.persistence.*
import kotlin.jvm.Transient

class MinistryStationEntityDao {
    var id: Long = 0
    var stationName: String? = null
    var description: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(entity: MinistryStationEntity) = MinistryStationEntityDao()
                .apply {
                    id = entity.id
                    stationName = entity.stationName
                    description = entity.description
                    status = entity.status
                }

        fun fromList(sections: List<MinistryStationEntity>): List<MinistryStationEntityDao> {
            val listSections = mutableListOf<MinistryStationEntityDao>()
            sections.forEach {
                listSections.add(fromEntity(it))
            }
            return listSections
        }
    }
}

class SectionEntityDao {
    var id: Long? = null
    var section: String? = null
    var descriptions: String? = null

    companion object {
        fun fromEntity(entity: SectionsEntity) = SectionEntityDao()
                .apply {
                    id = entity.id
                    section = entity.section
                    descriptions = entity.descriptions
                }

        fun fromList(sections: List<SectionsEntity>): List<SectionEntityDao> {
            val listSections = mutableListOf<SectionEntityDao>()
            sections.forEach {
                listSections.add(fromEntity(it))
            }
            return listSections
        }
    }
}

class SectionL2EntityDao {
    var id: Long? = null
    var subSection: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(entity: SubSectionsLevel2Entity) = SectionL2EntityDao()
                .apply {
                    id = entity.id
                    subSection = entity.subSection
                    status = entity.status
                }

        fun fromList(sections: List<SubSectionsLevel2Entity>): List<SectionL2EntityDao> {
            val listSections = mutableListOf<SectionL2EntityDao>()
            sections.forEach {
                listSections.add(fromEntity(it))
            }
            return listSections
        }
    }
}

class BlacklistTypeDto {
    var id: Long? = 0
    var typeName: String? = null
    var description: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(entity: CdBlackListUserTargetTypesEntity) = BlacklistTypeDto()
                .apply {
                    id = entity.id
                    description = entity.description
                    typeName = entity.typeName
                    status = entity.status
                }

        fun fromList(entities: List<CdBlackListUserTargetTypesEntity>): List<BlacklistTypeDto> {
            val listData = mutableListOf<BlacklistTypeDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}

class FreightStationsDto {
    var id: Long? = null
    var cfsCode: String? = null
    var cfsName: String? = null
    var description: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(entity: CfsTypeCodesEntity) = FreightStationsDto()
                .apply {
                    id = entity.id
                    description = entity.description
                    cfsCode = entity.cfsCode
                    cfsName = entity.cfsName
                    status = entity.status
                }

        fun fromList(entities: List<CfsTypeCodesEntity>): List<FreightStationsDto> {
            val listData = mutableListOf<FreightStationsDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}

class InspectionGeneralDetailsDto {
    var id: Long? = null
    var confirmItemType: Long? = null
    var inspection: String? = null
    var category: String? = null
    var entryPoint: String? = null
    var cfs: String? = null
    var inspectionDate: Date? = null
    var importersName: String? = null
    var clearingAgent: String? = null
    var customsEntryNumber: String? = null
    var idfNumber: String? = null
    var ucrNumber: String? = null
    var cocNumber: String? = null
    var feePaid: String? = null
    var receiptNumber: String? = null
    var overallRemarks: String? = null
    var complianceStatus: Int? = null
    var complianceRecommendations: String? = null
    var inspectionReportApprovalStatus: Int? = null
    var inspectionReportDisapprovalComments: String? = null
    var inspectionReportDisapprovalDate: Date? = null
    var inspectionReportApprovalComments: String? = null
    var inspectionReportApprovalDate: Date? = null
    var description: String? = null
    var inspectionReportRefNumber: String? = null
    var status: Int? = null
    var checklistTypeName: String? = null
    var checklistTypeId: Long? = null

    companion object {
        fun fromEntity(entity: CdInspectionGeneralEntity): InspectionGeneralDetailsDto {
            val dto = InspectionGeneralDetailsDto().apply {
                id = entity.id
                confirmItemType = entity.confirmItemType
                inspection = entity.inspection
                idfNumber = entity.idfNumber
                ucrNumber = entity.ucrNumber
                feePaid = entity.feePaid
                receiptNumber = entity.receiptNumber
                overallRemarks = entity.overallRemarks
                complianceRecommendations = entity.complianceRecommendations
                category = entity.category
                inspectionDate = entity.inspectionDate
                complianceStatus = entity.complianceStatus
                description = entity.description
                status = entity.status
            }
            // Add checklist details
            entity.checkListType?.let {
                dto.checklistTypeName = it.typeName
                dto.checklistTypeId = it.id
            }
            return dto
        }

        fun fromList(entities: List<CdInspectionGeneralEntity>): List<InspectionGeneralDetailsDto> {
            val listData = mutableListOf<InspectionGeneralDetailsDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}