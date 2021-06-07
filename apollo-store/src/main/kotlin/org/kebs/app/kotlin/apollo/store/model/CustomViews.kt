package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.RegionsCountyTownViewDto.Companion.FIND_ALL
import org.springframework.format.annotation.DateTimeFormat
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import javax.persistence.*

@Entity
@SqlResultSetMapping(
    name = "RegionsCountyTownViewDtoMapping",
    entities = [EntityResult(
        entityClass = RegionsCountyTownViewDto::class,
        fields = [
            FieldResult(name = "regionId", column = "REGION_ID"),
            FieldResult(name = "region", column = "REGION"),
            FieldResult(name = "countyId", column = "COUNTY_ID"),
            FieldResult(name = "county", column = "COUNTY"),
            FieldResult(name = "townId", column = "TOWN_ID"),
            FieldResult(name = "town", column = "TOWN")
        ]
    )
    ]

)
@NamedNativeQuery(
    name = FIND_ALL,
    query = "select r.ID as REGION_ID, r.REGION, c.id as COUNTY_ID, c.COUNTY, t.id as TOWN_id, t.TOWN from CFG_KEBS_REGIONS r right outer join CFG_KEBS_COUNTIES c on c.REGION_ID = r.ID right outer join CFG_KEBS_TOWNS t on t.COUNTY_ID = c.ID where t.COUNTY_ID is not null order by REGION, COUNTY, TOWN",
    resultSetMapping = "RegionsCountyTownViewDtoMapping"
)
class RegionsCountyTownViewDto : Serializable {

    var regionId: Long? = null
    var region: String? = null
    var countyId: Long? = null
    var county: String? = null

    @Id
    var townId: Long? = null
    var town: String? = null

    companion object {
        const val FIND_ALL = "RegionsCountyTownViewDto.findAll"
    }
}


@Entity
@SqlResultSetMapping(
    name = RegionSubRegionViewDto.RESULT_SET_MAPPING,
    entities = [
        EntityResult(
            entityClass = RegionSubRegionViewDto::class,
            fields = [
                FieldResult(name = "id", column = "ID"),
                FieldResult(name = "subRegion", column = "SUB_REGION"),
                FieldResult(name = "regionId", column = "REGION_ID"),
                FieldResult(name = "region", column = "REGION"),
            ]

        )
    ]

)
@NamedNativeQuery(
    name = RegionSubRegionViewDto.FIND_ALL,
    query = "select sr.id, SUB_REGION, r.REGION, sr.REGION_ID from CFG_KEBS_SUB_REGIONS sr, CFG_KEBS_REGIONS r where sr.REGION_ID = r.ID and sr.STATUS = 1 order by ID ",
    resultSetMapping = RegionSubRegionViewDto.RESULT_SET_MAPPING
)
class RegionSubRegionViewDto : Serializable {
    @Id
    var id: Int? = null
    var subRegion: String? = null
    var regionId: Int? = null
    var region: String? = null

    companion object {
        const val FIND_ALL = "RegionSubRegionViewDto.findAll"
        const val RESULT_SET_MAPPING = "RegionSubRegionViewDtoMapping"
    }

}


@Entity
@SqlResultSetMapping(
    name = DirectorateDesignationsViewDto.RESULT_SET_MAPPING,
    entities = [
        EntityResult(
            entityClass = RegionSubRegionViewDto::class,
            fields = [
                FieldResult(name = "id", column = "ID"),
                FieldResult(name = "designationName", column = "DESIGNATION_NAME"),
                FieldResult(name = "directorate", column = "DIRECTORATE"),
            ]

        )
    ]

)
@NamedNativeQuery(
    name = DirectorateDesignationsViewDto.FIND_ALL,
    query = "select de.id, de.DESIGNATION_NAME, di.DIRECTORATE from CFG_KEBS_DESIGNATIONS de, CFG_KEBS_DIRECTORATES di where de.DIRECTORATE_ID= di.ID and de.STATUS = 1 order by de.ID",
    resultSetMapping = DirectorateDesignationsViewDto.RESULT_SET_MAPPING
)
class DirectorateDesignationsViewDto : Serializable {
    @Id
    var id: Int? = null
    var designationName: String? = null
    var directorate: String? = null

    companion object {
        const val FIND_ALL = "DirectorateDesignationsViewDto.findAll"
        const val RESULT_SET_MAPPING = "DirectorateDesignationsViewDtoMapping"
    }

}

@Entity
@SqlResultSetMapping(
    name = DirectorateToSubSectionL2ViewDto.RESULT_SET_MAPPING,
    entities = [
        EntityResult(
            entityClass = DirectorateToSubSectionL2ViewDto::class,
            fields = [
                FieldResult(name = "id", column = "ROW_ID"),
                FieldResult(name = "l2Id", column = "L2_ID"),
                FieldResult(name = "l1Id", column = "L1_ID"),
                FieldResult(name = "l2SubSubSection", column = "L2_SUB_SUB_SECTION"),
                FieldResult(name = "l1SubSubSection", column = "L1_SUB_SUB_SECTION"),
                FieldResult(name = "sectionId", column = "SECTION_ID"),
                FieldResult(name = "section", column = "SECTION"),
                FieldResult(name = "divisionId", column = "DIVISION_ID"),
                FieldResult(name = "division", column = "DIVISION"),
                FieldResult(name = "departmentId", column = "DEPARTMENT_ID"),
                FieldResult(name = "department", column = "DEPARTMENT"),
                FieldResult(name = "directorateId", column = "DIRECTORATE_ID"),
                FieldResult(name = "directorate", column = "DIRECTORATE")
            ]

        )
    ]

)
@NamedNativeQuery(
    name = DirectorateToSubSectionL2ViewDto.FIND_ALL,
    query = "select ROWNUM row_id,dep.*, dir.DIRECTORATE from CFG_KEBS_DIRECTORATES dir left outer join ( select div.*, dep.DEPARTMENT, dep.DIRECTORATE_ID from CFG_KEBS_DEPARTMENTS dep left outer join ( select sec.*, d.DIVISION, d.DEPARTMENT_ID from CFG_KEBS_DIVISIONS d left outer join ( select sub.*, s.SECTION, s.DIVISION_ID from CFG_KEBS_SECTIONS s left outer join ( select nvl(l2.id, ROWNUM) l2_id, nvl(l1.id, ROWNUM) l1_id, l2.SUB_SECTION     l2_SUB_sub_SECTION, l1.SUB_SECTION     l1_SUB_sub_SECTION, l1.SECTION_ID from CFG_KEBS_SUB_SECTIONS_LEVEL2 l2 left outer join CFG_KEBS_SUB_SECTIONS_LEVEL1 l1 on l2.SUB_SECTIONS_LEVEL1_ID = l1.ID where l2.STATUS = 1 order by l2_id) sub on s.id = sub.SECTION_ID where s.STATUS = 1 ) sec on d.ID = sec.DIVISION_ID ) div on div.DEPARTMENT_ID = dep.ID where dep.STATUS = 1 ) dep on dir.ID = dep.DIRECTORATE_ID where dir.STATUS = 1",
    resultSetMapping = DirectorateToSubSectionL2ViewDto.RESULT_SET_MAPPING
)
class DirectorateToSubSectionL2ViewDto : Serializable {
    @Id
    var id: Int? = null
    var l2Id: Long? = null
    var l1Id: Long? = null
    var sectionId: Long? = null
    var divisionId: Long? = null
    var departmentId: Long? = null
    var directorateId: Long? = null
    var l2SubSubSection: String? = null
    var l1SubSubSection: String? = null
    var section: String? = null
    var division: String? = null
    var department: String? = null
    var directorate: String? = null

    companion object {
        const val FIND_ALL = "DirectorateToSubSectionL2ViewDto.findAll"
        const val RESULT_SET_MAPPING = "DirectorateToSubSectionL2ViewDtoMapping"
    }

}

@Entity
@SqlResultSetMapping(
    name = KraPaymentsEntityDto.RESULT_SET_MAPPING,
    entities = [
        EntityResult(
            entityClass = KraPaymentsEntityDto::class,
            fields = [
                FieldResult(name = "entryNo", column = "REQUEST_HEADER_ENTRY_NO"),
                FieldResult(name = "manufacturerName", column = "REQUEST_HEADER_MANUFACTURER_NAME"),
                FieldResult(name = "requestHeaderKraPin", column = "REQUEST_HEADER_KRA_PIN"),
                FieldResult(name = "requestHeaderPaymentSlipNo", column = "REQUEST_HEADER_PAYMENT_SLIP_NO"),
                FieldResult(name = "requestHeaderPaymentSlipDate", column = "REQUEST_HEADER_PAYMENT_SLIP_DATE"),
                FieldResult(name = "requestHeaderTotalPaymentAmt", column = "REQUEST_HEADER_TOTAL_PAYMENT_AMT"),
                FieldResult(name = "requestHeaderTotalPenaltyAmt", column = "REQUEST_HEADER_TOTAL_PENALTY_AMT"),
                FieldResult(name = "requestHeaderTotalDeclAmt", column = "REQUEST_HEADER_TOTAL_DECL_AMT")
            ]

        )
    ]

)
@NamedNativeQuery(
    name = KraPaymentsEntityDto.FIND_ALL,
    query = "select distinct request_header_entry_No, request_header_manufacturer_name, request_header_kra_pin, request_header_payment_Slip_No, request_header_payment_Slip_date, request_header_TOTAL_PAYMENT_AMT, request_header_TOTAL_PENALTY_AMT, request_header_TOTAL_DECL_AMT from LOG_SL2_PAYMENTS_HEADER where request_header_entry_No = ?",
    resultSetMapping = KraPaymentsEntityDto.RESULT_SET_MAPPING
)
class KraPaymentsEntityDto : Serializable {
    @Id
    var entryNo: Long? = null
    var manufacturerName: String? = null
    var requestHeaderKraPin: String? = null
    var requestHeaderPaymentSlipNo: String? = null

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    var requestHeaderPaymentSlipDate: Timestamp? = null
    var requestHeaderTotalPaymentAmt: BigDecimal? = null
    var requestHeaderTotalPenaltyAmt: BigDecimal? = null
    var requestHeaderTotalDeclAmt: BigDecimal? = null

    companion object {
        const val FIND_ALL = "KraPaymentsEntityDto.findAll"
        const val RESULT_SET_MAPPING = "KraPaymentsEntityDto"
    }
}



