package org.kebs.app.kotlin.apollo.store.model.std

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

interface UserEmailHolder {
    fun getEmail(): String?
}

interface UserRoleHolder {
    fun getId(): Long?
}

interface UserDetailHolder {
    fun getId(): Long?
    fun getFirstName(): String?
    fun getLastName(): String?
}
interface SiteVisitListHolder {
    fun getId(): Long?
}

interface WindingUpReportListHolder {
    fun getId(): Long?
}
interface LevyPayments{
    fun getId(): Long?
    fun getEntryNumber(): String?
    fun getPaymentDate(): String?
    fun getPaymentAmount(): String?
    fun getCompanyId(): Long?
    fun getCompanyName(): String?
    fun getAssignStatus(): Long?
    fun getFirstName(): String?
    fun getLastName(): String?
}
interface ManufactureListHolder {
    fun getId(): Long?
    fun getName(): String?
    fun getPhysicalAddress(): String?
    fun getKraPin(): String?
    fun getManufactureStatus(): Long?
    fun getRegistrationNumber(): String?
    fun getPostalAddress(): String?
    fun getPlotNumber(): String?
    fun getCompanyEmail(): String?
    fun getCompanyTelephone(): String?
    fun getYearlyTurnover(): Long?
    fun getBusinessLines(): Long?
    fun getBusinessLineName(): String?
    fun getBusinessNatures(): Long?
    fun getBusinessNatureName(): String?
    fun getBuildingName(): String?
    fun getStreetName(): String?
    fun getRegion(): Long?
    fun getRegionName(): String?
    fun getCounty(): Long?
    fun getCountyName(): String?
    fun getFirmCategory(): String?
    fun getTown(): Long?
    fun getTownName(): String?
    fun getUserId(): Long?
    fun getDirectorIdNumber(): String?
    fun getEntryNumber(): String?
    fun getStatus(): Long?
    fun getClosedCommodityManufactured(): String?
    fun getClosedContractsUndertaken(): String?
    fun getTaskType(): Long?
    fun getTaskId(): String?
    fun getOwnership(): String?
    fun getBranchName(): String?
    fun getClosureOfOperations(): String?
    fun getTypeOfManufacture(): Long?
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
interface BranchNameHolder{
    fun getBranchName(): String?

}
interface BusinessTypeHolder{
    fun getBusinessType(): Long?
}
interface NotificationFormDetailsHolder{
//    fun getCompanyName(): String?
//    fun getPlotNumber(): String?
//    fun getStreetName(): String?
//    fun getPhysicalAddress(): String?
//    fun getTelephoneNumber(): String?
//    fun getPostalAddress(): String?
//    fun getRegistrationNumber(): String?
//    fun getKraPin(): String?
//    fun getBranchName(): String?
    fun getCommodity(): String?
    fun getDateOfManufacture(): Date?
    fun getTotalValue(): Long?
    fun getId(): Long?
    fun getNameOfBusinessProprietor(): String?

}

interface DirectorListHolder {
    fun getDirectorName(): String?
}

interface SACSummaryHolder {

    fun getId(): Long?
    fun getDepartmentName(): String?
    fun getTechnicalCommittee(): String?
    fun getBackgroundInformation(): String?
    fun getDateOfApproval(): String?
    fun getKs(): String?
    fun getEdition(): String?
    fun getTitle(): String?
    fun getIssuesAddressed(): String?
    fun getFeedback(): String?
    fun getRequestedBy(): String?
    fun getVarField1(): String?
    fun getReferenceMaterial(): String?
    fun getSl(): String?
    fun getRequestedByName(): String?


}


