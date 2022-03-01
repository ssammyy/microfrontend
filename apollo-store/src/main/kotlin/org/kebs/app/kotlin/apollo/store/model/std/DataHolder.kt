package org.kebs.app.kotlin.apollo.store.model.std

import oracle.sql.TIMESTAMP
import java.util.*


//class DataHolder {
//    @NotNull
//    var id: Long =0
//
//    @NotNull
//    @Email
//     val name: String? = null
//
//    @NotNull
//    @Min(10)
//     val sub: String? = null
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as DataHolder
//
//        if (id != other.id) return false
//        if (name != other.name) return false
//        if (sub != other.sub) return false
//
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = id.hashCode()
//        result = 31 * result + (name?.hashCode() ?: 0)
//        result = 31 * result + (sub?.hashCode() ?: 0)
//
//        return result
//    }
//
//    override fun toString(): String {
//        return "DataHolder(id=$id, name=$name, sub=$sub)"
//    }
//}
interface DataHolder {
    fun getId(): Long?
    fun getTc_Title(): String?
    fun getName(): String?
    fun getV1(): String?
    fun getV2(): String?
    fun getV3(): String?
    fun getV4(): String?
    fun getV5(): String?
    fun getV6(): String?

}
interface UserTypeHolder {
    fun getName(): String?
}

interface UserRoleHolder {
    fun getId(): Long?
}

interface UserDetailHolder {
    fun getId(): Long?
    fun getFirstName(): String?
    fun getLastName(): String?
}

interface CompleteTasksDetailHolder {
    fun getPurpose(): String?
    fun getPerson(): String?
    fun getActionTaken(): String?
    fun getRemarks(): String?
    fun getFeedBackRemarks(): String?
    fun getReportRemarks(): String?
    fun getVisitId(): Long?
    fun getVisitDate(): Date?
    fun getChiefManagerRemarks(): String?
    fun getAsstManagerRemarks(): String?
    fun getCompanyId(): Long?
    fun getCompanyName(): String?
    fun getEntryNumber(): String?
    fun getKraPin(): String?
    fun getRegistrationNumber(): String?
    fun getDirectorIdNumber(): Long?
    fun getPostalAddress(): String?
    fun getPhysicalAddress(): String?
    fun getPlotNumber(): String?
    fun getCompanyEmail(): String?
    fun getCompanyTelephone(): String?
    fun getYearlyTurnOver(): String?
    fun getBusinessLines(): String?
    fun getBusinessNature(): String?
    fun getBranchName(): String?
    fun getStreetName(): String?

}


