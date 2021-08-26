package org.kebs.app.kotlin.apollo.api.payload

import org.kebs.app.kotlin.apollo.store.model.CdBlackListUserTargetTypesEntity
import org.kebs.app.kotlin.apollo.store.model.SectionsEntity
import org.kebs.app.kotlin.apollo.store.model.SubSectionsLevel2Entity
import org.kebs.app.kotlin.apollo.store.model.di.CfsTypeCodesEntity
import javax.persistence.*

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
            val listSections= mutableListOf<SectionEntityDao>()
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
            val listSections= mutableListOf<SectionL2EntityDao>()
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
            val listData= mutableListOf<BlacklistTypeDto>()
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
            val listData= mutableListOf<FreightStationsDto>()
            entities.forEach {
                listData.add(fromEntity(it))
            }
            return listData
        }
    }
}

class CheckListItem{
    
}