package org.kebs.app.kotlin.apollo.store.model.std

import org.springframework.format.annotation.DateTimeFormat
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

interface LevyPayment{
    fun getId(): Long?
    fun getEntryNumber(): Long?
    fun getPaymentDate(): String?
    fun getTotalPaymentAmt(): String?
    fun getCompanyId(): Long?
    fun getCompanyName(): String?
    fun getAssignStatus(): Long?
    fun getFirstName(): String?
    fun getLastName(): String?
    fun getKraPin(): String?
    fun getRegistrationNumber(): String?
    fun getPeriodFrom(): String?
    fun getPeriodTo(): String?
    fun getPaymentSlipNo():String?
    fun getPaymentSlipDate(): String?
    fun getQtyManf(): String?
    fun getExFactVal(): String?
    fun getPaymentType(): String?
    fun getTotalDeclAmt(): String?
    fun getTotalPenaltyAmt(): String?
    fun getBankRefNo(): String?
    fun getBankName(): String?
    fun getCommodityType(): String?
    fun getLevyPaid(): String?
    fun getPenaltyPaid(): String?
}
interface AllLevyPayments{
    fun getId(): Long?
    fun getEntryNumber(): String?
    fun getKraPin(): String?
    fun getCompanyName(): String?
    fun getCompanyId(): Long?
    fun getBusinessLines(): Long?
    fun getBusinessNatures(): String?
    fun getBusinessLineName(): String?
    fun getBusinessNatureName(): String?
    fun getRegion(): Long?
    fun getRegionName(): String?
    fun getPeriodTo(): String?
    fun getPaymentSlipDate(): String?
    fun getPaymentDate(): String?
    fun getLevyPaid(): String?
    fun getPenaltyPaid(): String?
    fun getTotalPenaltyAmt(): String?
    fun getAmountDue(): String?


}

interface LevyPayments{
    fun getId(): Long?
    fun getEntryNumber(): String?
    fun getPaymentDate(): String?
    fun getTotalPaymentAmt(): String?
    fun getCompanyId(): Long?
    fun getCompanyName(): String?
    fun getAssignStatus(): Long?
    fun getFirstName(): String?
    fun getLastName(): String?
    fun getKraPin(): String?
    fun getRegistrationNumber(): String?
    fun getPeriodFrom(): String?
    fun getPeriodTo(): String?
    fun getPaymentSlipNo():String?
    fun getPaymentSlipDate(): String?
    fun getQtyManf(): String?
    fun getExFactVal(): String?
    fun getPaymentType(): String?
    fun getTotalDeclAmt(): String?
    fun getTotalPenaltyAmt(): String?
    fun getBankRefNo(): String?
    fun getBankName(): String?
    fun getCommodityType(): String?
    fun getLevyPaid(): String?
    fun getPenaltyPaid(): String?
}
interface PenaltyDetails{
    fun getPenaltyOrderNo(): Long?
    fun getEntryNo(): String?
    fun getkraPin(): String?
    fun getManufacName(): String?
    fun getPeriodFrom(): Date?
    fun getPeriodTo(): Date?
    fun getPenaltyGenDate(): String?
    fun getPenaltyPayable(): Long?
}

interface LevyPenalty{
    fun getId(): Long?
    fun getEntryNumber(): String?
    fun getPaymentDate(): String?
    fun getPaymentAmount(): String?
    fun getAmountDue(): String?
    fun getPenalty(): String?
    fun getLevyDueDate(): String?
    fun getCompanyId(): Long?
    fun getCompanyName(): String?
    fun getAssignStatus(): Long?
    fun getFirstName(): String?
    fun getLastName(): String?
    fun getKraPin(): String?
    fun getRegistrationNumber(): String?
    fun getPeriodFrom(): String?
    fun getPeriodTo(): String?
    fun getPostalAddress(): String?
    fun getBusinessLines(): Long?
    fun getBusinessNatures(): Long?
    fun getBusinessLineName(): String?
    fun getBusinessNatureName(): String?
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
    fun getOtherBusinessNatureType(): String?
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
    fun getOfficersFeedback(): String?
    fun getStatus(): Long?
    fun getOtherBusinessNatureType(): String?

}
interface BranchNameHolder{
    fun getBranchName(): String?

}
interface BusinessTypeHolder{
    fun getBusinessType(): Long?
}

interface EmailVerificationStatus{
    fun getEmailActivationStatus(): Long?
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

class LevyCustomResponse {
    var response: String? = null
    var payload: MutableList<NotificationFormDetailsHolder>? = null
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

interface CompanySuspensionList{
    fun getId(): Long?
    fun getCompanyId(): Long?
    fun getReason(): String?
    fun getStatus(): Long?
    fun getDescription(): String?
    fun getDateOfSuspension(): String?
    fun getEntryNumber(): String?
    fun getKraPin(): String?
    fun getRegistrationNumber(): String?
    fun getCompanyName(): String?

}

interface CompanyClosureList{
    fun getId(): Long?
    fun getCompanyId(): Long?
    fun getReason(): String?
    fun getStatus(): Long?
    fun getDescription(): String?
    fun getDateOfClosure(): String?
    fun getEntryNumber(): String?
    fun getKraPin(): String?
    fun getRegistrationNumber(): String?
    fun getCompanyName(): String?

}

interface EmailListHolder{
    fun getCompanyEmail(): String?;
    fun getCompanyName(): String?;
}

interface UserEmailListHolder{
    fun getUserEmail(): String?;
    fun getFirstName(): String?;
    fun getLastName(): String?;
}

interface UserIdHolder{
    fun getUserId();
}

interface ManufacturerStatusHolder{
    fun getManufacturerStatus(): Long?
}

interface ProposalDetails{
    fun getId(): Long?
    fun getDocName(): String?
    fun getPreparedDate(): String?
    fun getProposalNumber(): String?
    fun getUploadedBy(): String?
    fun getRemarks(): String?
    fun getAssignedTo(): Long?
    fun getTitle(): String?
    fun getCirculationDate(): String?
    fun getNameOfOrganization(): String?
    fun getNameOfRespondent(): String?
    fun getClosingDate(): String?
    fun getScope(): String?
    fun getTcSecName(): String?
    fun getAdoptionAcceptableAsPresented(): String?
    fun getReasonsForNotAcceptance(): String?
}

interface RegisteredFirms{
    fun getId(): Long?
    fun getEntryNumber(): String?
    fun getKraPin(): String?
    fun getName(): String?
    fun getPostalAddress(): String?
    fun getCompanyTelephone(): String?
    fun getCompanyEmail(): String?
    fun getStreetName(): String?
    fun getBusinessLineName(): String?
    fun getBusinessLines(): Long?
    fun getBusinessNatureName(): String?
    fun getBusinessNatures(): Long?
    fun getRegionName(): String?
    fun getRegion(): Long?
    fun getTownName(): String?
    fun getTown(): Long?
    fun getCreatedOn(): String?
    fun getAdminLocation(): String?
    fun getDateOfClosure(): String?
}


