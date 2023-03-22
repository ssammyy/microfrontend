package org.kebs.app.kotlin.apollo.store.model.std

import org.springframework.format.annotation.DateTimeFormat
import java.sql.Timestamp
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
    fun getName(): String?
    fun getLastName(): String?
    fun getEmail(): String?
    fun getTelephone(): String?
}

interface UserHolder {
    fun getId(): Long?
    fun getName(): String?
    fun getEmail(): String?
}

interface BusinessLineHolder{
   fun getId(): Long?
   fun getName(): String?
}

interface RegionHolder{
    fun getId(): Long?
    fun getRegion(): String?
}

interface SiteVisitListHolder {
    fun getId(): Long?
}

interface WindingUpReportListHolder {
    fun getId(): Long?
}

interface CompanyRemarks{
    fun getId(): Long?
    fun getSiteVisitId(): Long?
    fun getRemarks(): String?
    fun getRemarkBy(): String?
    fun getStatus(): String?
    fun getRole(): String?
    fun getDescription(): String?
    fun getDateOfRemark(): String?
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
    fun getPeriodFrom(): String?
    fun getPaymentSlipDate(): String?
    fun getPaymentDate(): String?
    fun getLevyPaid(): String?
    fun getPenaltyPaid(): String?
    fun getTotalPenaltyAmt(): String?
    fun getAmountDue(): String?
    fun getMonthsLate() : String?


}
interface ReceivedStandards{
    fun getCreatedON(): Timestamp?
    fun getName(): String?
    fun getPhone(): String?
    fun getStatus(): String?
    fun getFirstName(): String?
    fun getLastName(): String?
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
    fun getPenaltyGenDate(): Timestamp?
    fun getPenaltyPayable(): Long?
}

interface CompanyData{
    fun getCompanyEmail():String?
    fun getCompanyName():String?
    fun getPhysicalAddress():String?
    fun getPostalAddress():String?
    fun getOwnership():String?
    fun getCompanyTelephone():String?
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
    fun getMonthsLate(): String?
    fun getPenaltyPaid(): String?
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
    fun getAssignStatus(): String?
    fun getExemptionStatus(): Long?
}
interface SiteVisits{
    fun getId(): Long?
    fun getCompanyName(): String?
    fun getEntryNumber(): String?
    fun getKraPin(): String?
    fun getOfficerName(): String?
    fun getDateOfVisit(): String?
    fun getPurpose(): String?
    fun getRegion(): String?
    fun getBusinessLine(): String?
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

interface EmailList{
   fun getEmail(): String?
   fun getName(): String?
   fun getTelephone(): String?
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
    fun getPreparedDate(): Timestamp?
    fun getProposalNumber(): String?
    fun getUploadedBy(): String?
    fun getRemarks(): String?
    fun getAssignedTo(): Long?
    fun getTitle(): String?
    fun getCirculationDate(): Timestamp?
    fun getNameOfOrganization(): String?
    fun getNameOfRespondent(): String?
    fun getClosingDate(): Timestamp?
    fun getScope(): String?
    fun getTcSecName(): String?
    fun getTcSecEmail(): String?
    fun getAdoptionAcceptableAsPresented(): String?
    fun getReasonsForNotAcceptance(): String?
    fun getStandardNumber(): String?
    fun getDeadlineDate(): Timestamp?
    fun getNoOfComments(): Long?
    fun getDraftId(): Long?
    fun getDraftNumber(): String?
    fun getDraftTitle(): String?
    fun getIStandardNumber(): String?
    fun getCompanyName(): String?
    fun getContactOneEmail(): String?
    fun getContactOneFullName(): String?
    fun getContactOneTelephone(): String?
    fun getAdoptionLink(): String?
    fun getVoteFor(): Long?
    fun getVoteAgainst(): Long?
    fun getRequesterName(): String?
}
interface PermitsAwarded{
    fun getId(): Long?
    fun getKraPin(): String?
    fun getEntryNumber(): String?
    fun getName(): String?
    fun getTelephone(): String?
    fun getCompanyEmail(): String?
    fun getPhysicalAddress(): String?
    fun getPostalAddress(): String?
    fun getTown(): String?
    fun getRegion(): String?
    fun getProductName(): String?
    fun getIssueDate(): String?
    fun getExpiryDate(): String?
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

interface ReviewStandards{
    fun getId(): Long?
    fun getTitle(): String?
    fun getScope(): String?
    fun getNormativeReference(): String?
    fun getSymbolsAbbreviatedTerms(): String?
    fun getClause(): String?
    fun getSpecial(): String?
    fun getStandardNumber(): String?
    fun getStandardType(): String?
    fun getDateFormed(): String?
    fun getUserName(): String?
    fun getAdoptionComment(): String?
    fun getCommentTime(): String?
    fun getProposalId(): Long?
    fun getDocumentType(): String?
    fun getParagraph(): String?
    fun getTypeOfComment(): String?
    fun getProposedChange(): String?



}

interface RejectedComDetails{
    fun getId(): Long?
    fun getCompanyId(): Long?
    fun getCompanyName(): String?
    fun getEditId(): Long?
    fun getPhysicalAddressEdit(): String?
    fun getPhysicalAddress(): String?
    fun getPostalAddressEdit(): String?
    fun getPostalAddress(): String?
    fun getOwnershipEdit(): String?
    fun getOwnership(): String?
    fun getCompanyEmailEdit(): String?
    fun getCompanyEmail(): String?
    fun getCompanyTelephoneEdit(): String?
    fun getCompanyTelephone(): String?
    fun getAssignedTo(): String?
    fun getStatus(): Long?
}

interface KnwaJustification{
    fun getId(): Long?
    fun getDateOfMeeting(): String?
    fun getKnwSec(): String?
    fun getSlNumber(): String?
    fun getRequestNumber(): String?
    fun getRequestedBy(): String?
    fun getIssuesAddressed(): String?
    fun getAcceptanceDate(): String?
    fun getReferenceMaterial(): String?
    fun getRemarks(): String?
    fun getSubmissionDate(): String?
    fun getKnwCommittee(): String?
    fun getDepartmentName(): String?
}

interface NwaPDraft{
    fun getId(): Long?
    fun getTitle(): String?
    fun getDiJNumber(): String?
    fun getRemarks(): String?
    fun getScope(): String?
    fun getNormativeReference(): String?
    fun getSymbolsAbbreviatedTerms(): String?
    fun getClause(): String?
    fun getDatePdPrepared(): String?
    fun getWorkShopDate(): String?
    fun getSpecial(): String?
    fun getCdAppNumber(): String?
    fun getPreparedBy(): String?
    fun getJustificationNumber(): Long?

}

interface NWAApprovedDraft{
    fun getId(): String?
    fun getRequestorName(): String?
    fun getStandardOfficerName(): String?
    fun getVersionNumber(): String?
    fun getEditedBY(): String?
    fun getEditedDate(): String?
    fun getProofreadStatus(): String?
    fun getProofReadBy(): String?
    fun getProofReadDate(): String?
    fun getDraughtingStatus(): String?
    fun getDraughtingBy(): String?
    fun getDraughtingDate(): String?
    fun getDraftId(): Long?
}

interface ISAdoptionProposals{
    fun getId(): Long?
    fun getProposal_doc_name(): String?
    fun getCirculationDate(): String?
    fun getClosingDate(): String?
    fun getTcSecName(): String?
    fun getTitle(): String?
    fun getScope(): String?
    fun getAdoptionAcceptableAsPresented(): String?
    fun getReasonsForNotAcceptance(): String?
    fun getRecommendations(): String?
    fun getNameOfRespondent(): String?
    fun getPositionOfRespondent(): String?
    fun getNameOfOrganization(): String?
    fun getDateOfApplication(): String?
    fun getUploadedBy(): String?
    fun getPreparedDate(): String?
    fun getProposalNumber(): String?
}

interface ISProposalComments{
    fun getId(): String?
    fun getUserId(): String?
    fun getAdoptionComment(): String?
    fun getCommentTime(): String?
    fun getProposalId(): String?
    fun getTitle(): String?
    fun getDocumentType(): String?
    fun getClause(): String?
    fun getComNameOfOrganization(): String?
    fun getParagraph(): String?
    fun getTypeOfComment(): String?
    fun getProposedChange(): String?
    fun getAdopt(): String?
    fun getReasonsForNotAcceptance(): String?
    fun getRecommendations(): String?
    fun getNameOfRespondent(): String?
    fun getPositionOfRespondent(): String?
    fun getNameOfOrganization(): String?
    fun getDateOfApplication(): String?
    fun getScope(): String?
    fun getObservation(): String?
}
interface ISUploadedDraft{
    fun getId(): Long?
    fun getTitle(): String?
    fun getScope(): String?
    fun getNormativeReference(): String?
    fun getSymbolsAbbreviatedTerms(): String?
    fun getClause(): String?
    fun getSpecial(): String?
    fun getIsNumber(): String?
    fun getUploadDate(): String?
    fun getJustificationNo(): Long?
    fun getProposalId(): Long?
    fun getDocumentType():String?
    fun getPreparedBy():String?
}

interface ISAdoptionProposalJustification{
    fun getId(): Long?
    fun getMeetingDate(): String?
    fun getTcId(): String?
    fun getTcSec(): String?
    fun getSlNumber(): String?
    fun getEdition(): String?
    fun getRequestNumber(): String?
    fun getRequestedBy(): String?
    fun getIssuesAddressed(): String?
    fun getTcAcceptanceDate(): String?
    fun getReferenceMaterial(): String?
    fun getDepartment(): String?
    fun getRemarks(): String?
    fun getSubmissionDate(): String?
    fun getTcCommittee(): String?
    fun getDepartmentName(): String?
    fun getPositiveVotes(): String?
    fun getNegativeVotes(): String?
    fun getTitle(): String?
    fun getScope(): String?
    fun getNormativeReference(): String?
    fun getSymbolsAbbreviatedTerms(): String?
    fun getClause(): String?
    fun getSpecial(): String?
    fun getProposalId(): Long?

}

interface COMUploadedDraft{
    fun getId(): Long?
    fun getRequestId(): Long?
    fun getDraftId(): Long?
    fun getTitle(): String?
    fun getScope(): String?
    fun getNormativeReference(): String?
    fun getSymbolsAbbreviatedTerms(): String?
    fun getClause(): String?
    fun getSpecial(): String?
    fun getStatus(): Long?
    fun getComStdNumber(): String?
    fun getUploadDate(): String?
    fun getRequestNumber(): String?
    fun getProposalId(): Long?
    fun getDocumentType():String?
    fun getPreparedBy():String?
    fun getDepartmentid(): String?
    fun getDepartmentname(): String?
    fun getSubject(): String?
    fun getDescription(): String?
    fun getContactonefullname(): String?
    fun getContactonetelephone(): String?
    fun getContactoneemail(): String?
    fun getContacttwofullname(): String?
    fun getContacttwotelephone(): String?
    fun getContacttwoemail(): String?
    fun getContactthreefullname(): String?
    fun getContactthreetelephone(): String?
    fun getContactthreeemail(): String?
    fun getCompanyname(): String?
    fun getCompanyphone(): String?
}
interface NwaRequest{
    fun getDraftId(): Long?
    fun getTitle(): String?
    fun getScope(): String?
    fun getNormativeReference(): String?
    fun getSymbolsAbbreviatedTerms(): String?
    fun getClause(): String?
    fun getSpecial(): String?
    fun getUploadDate(): Timestamp?
    fun getDeadlineDate(): Timestamp?
    fun getDraftNumber(): String?
    fun getRemarks(): String?
    fun getRequestId(): Long?
    fun getDepartmentId(): String?
    fun getStandardType(): String?
    fun getWorkShopDate(): String?
    fun getRequestNumber(): String?
    fun getRank(): String?
    fun getName(): String?
    fun getPhone(): String?
    fun getEmail(): String?
    fun getSubmissionDate(): String?
    fun getProductSubCategoryId(): String?
    fun getTcId(): String?
    fun getProductId(): String?
    fun getOrganisationName(): String?
    fun getSubject(): String?
    fun getDescription(): String?
    fun getEconomicEfficiency(): String?
    fun getHealthSafety(): String?
    fun getEnvironment(): String?
    fun getIntegration(): String?
    fun getExportMarkets(): String?
    fun getLevelOfStandard(): String?
    fun getDepartmentName(): String?
    fun getNwaCdNumber(): String?
}

interface ComStandard {
    fun getId(): Long?
    fun getTitle(): String?
    fun getScope(): String?
    fun getNormativeReference(): String?
    fun getSymbolsAbbreviatedTerms(): String?
    fun getClause(): String?
    fun getSpecial(): String?
    fun getComStdNumber(): String?
    fun getDocumentType(): String?
    fun getPreparedBy(): String?
    fun getUploadDate(): String?
    fun getRequestNumber(): String?
    fun getStatus(): String?
    fun getRequestId(): String?
    fun getDraftId(): String?
    fun getDepartmentId(): Long?
    fun getDepartmentName(): String?
    fun getSubject(): String?
    fun getDescription(): String?
    fun getContactOneFullName(): String?
    fun getContactOneTelephone(): String?
    fun getContactOneEmail(): String?
    fun getContactTwoFullName(): String?
    fun getContactTwoTelephone(): String?
    fun getContactTwoEmail(): String?
    fun getContactThreeFullName(): String?
    fun getContactThreeTelephone(): String?
    fun getContactThreeEmail(): String?
    fun getCompanyName(): String?
    fun getCompanyPhone(): String?
    fun getStandardType(): String?
    fun getWorkShopDate(): Timestamp?
}

interface ComStdRequest{
    fun getId(): Long?
    fun getRequestNumber(): String?
    fun getSubmissionDate(): Timestamp?
    fun getCompanyName(): String?
    fun getCompanyPhone(): String?
    fun getCompanyEmail(): String?
    fun getTcName(): String?
    fun getDepartmentName(): String?
    fun getDepartmentId(): Long?
    fun getProductName(): String?
    fun getProductSubCategoryName(): String?
    fun getStatus(): Long?
    fun getSubject(): String?
    fun getDescription(): String?
    fun getContactOneFullName(): String?
    fun getContactOneTelephone(): String?
    fun getContactOneEmail(): String?
    fun getContactTwoFullName(): String?
    fun getContactTwoTelephone(): String?
    fun getContactTwoEmail(): String?
    fun getContactThreeFullName(): String?
    fun getContactThreeTelephone(): String?
    fun getContactThreeEmail(): String?
}




