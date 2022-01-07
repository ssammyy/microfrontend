package org.kebs.app.kotlin.apollo.common.dto.ms

import org.kebs.app.kotlin.apollo.common.dto.qa.SSFComplianceStatusDetailsDto
import org.kebs.app.kotlin.apollo.common.dto.qa.SSFPDFListDetailsDto
import java.math.BigDecimal
import java.sql.Date
import javax.validation.constraints.NotNull

data class BatchFileFuelSaveDto(
        @NotNull(message = "Required field")
        var county: Long,
        @NotNull(message = "Required field")
        var town: Long,

        var remarks: String? = null,
)

data class FuelBatchDetailsDto(
        var id: Long? = null,
        var region: String? = null,
        var county: String? = null,
        var town: String? = null,
        var referenceNumber: String? = null,
        var batchFileYear: String? = null,
        var remarks: String? = null,
        var batchClosed: Boolean? = null,
)

data class FuelInspectionScheduleListDetailsDto(
        var fuelInspectionDto: List<FuelInspectionDto>? = null,
        var fuelBatchDetailsDto: FuelBatchDetailsDto? = null
)


data class FuelInspectionDto(
        var id: Long? = null,
        var referenceNumber: String? = null,
        var company: String? = null,
        var petroleumProduct: String? = null,
        var physicalLocation: String? = null,
        var inspectionDateFrom: Date? = null,
        var inspectionDateTo: Date? = null,
        var processStage: String? = null,
        var closedStatus: Boolean? = null,
        var batchDetails: FuelBatchDetailsDto?= null,
        var officersList: List<MsUsersDto>? = null,
        var officersAssigned: MsUsersDto? = null,
        var rapidTestStatus: Boolean? = null,
        var rapidTestRemarks: String? = null,
        var sampleCollected: SampleCollectionDto? = null,
        var sampleSubmitted: SampleSubmissionDto? = null,
        var sampleLabResults: MSSSFLabResultsDto? = null,
)

data class FuelEntityDto(
        @NotNull(message = "Required field")
        var company: String,
        @NotNull(message = "Required field")
        var petroleumProduct: String,
        @NotNull(message = "Required field")
        var physicalLocation: String,
        @NotNull(message = "Required field")
        var inspectionDateFrom: Date,
        @NotNull(message = "Required field")
        var inspectionDateTo: Date,
        @NotNull(message = "Required field")
        var stationOwnerEmail: String,
        var remarks: String? = null,
)

data class FuelEntityAssignOfficerDto(
        @NotNull(message = "Required field")
        var assignedUserID: Long,
        var remarks: String? = null,
)

data class FuelEntityRapidTestDto(
        @NotNull(message = "Required field")
        var rapidTestRemarks: String? = null,
        @NotNull(message = "Required field")
        var rapidTestStatus: Boolean,
)

data class SampleCollectionDto(
        var nameManufacturerTrader: String?= null,
        var addressManufacturerTrader: String?= null,
        var samplingMethod: String?= null,
        var reasonsCollectingSamples: String?= null,
        var anyRemarks: String?= null,
        var designationOfficerCollectingSample: String?= null,
        var nameOfficerCollectingSample: String?= null,
        var dateOfficerCollectingSample: Date?= null,
        var nameWitness: String?= null,
        var designationWitness: String?= null,
        var dateWitness: Date?= null,
        var productsList: List<SampleCollectionItemsDto>? = null,
)

data class SampleCollectionItemsDto(
        var id: Long? = null,
        var productBrandName: String? = null,
        var batchNo: String? = null,
        var batchSize: String? = null,
        var sampleSize: String? = null,
)

data class SampleSubmissionDto(
        var nameProduct : String? = null,
        var packaging : String? = null,
        var labellingIdentification : String? = null,
        var fileRefNumber : String? = null,
        var referencesStandards : String? = null,
        var sizeTestSample : Long? = null,
        var sizeRefSample : Long? = null,
        var condition : String? = null,
        var sampleReferences : String? = null,
        var sendersName : String? = null,
        var designation : String? = null,
        var address : String? = null,
        var sendersDate : Date? = null,
        var receiversName : String? = null,
        var testChargesKsh : BigDecimal? = null,
        var receiptLpoNumber : String? = null,
        var invoiceNumber : String? = null,
        var disposal : String? = null,
        var remarks : String? = null,
        var sampleCollectionNumber : Long? = null,
        var bsNumber : String? = null,
        var parametersList: List<SampleSubmissionItemsDto>? = null,
)

data class SampleSubmissionItemsDto(
        var parameters : String? = null,
        var laboratoryName : String? = null,
)

data class BSNumberSaveDto(
        @NotNull(message = "Required field")
        var bsNumber: String,
        @NotNull(message = "Required field")
        var submittedDate: Date,

        var remarks: String? = null,
)

data class LabResultsDto(
        var parametersListTested: List<LabResultsParamDto>? = null,
        var savedPDFFiles: List<LabResultsParamDto>? = null,
        var result : String? = null,
        var method : String? = null,
)

data class LabResultsParamDto(
        var param : String? = null,
        var result : String? = null,
        var method : String? = null,
)

data class MSSSFLabResultsDto(
        var ssfResultsList: MSSSFComplianceStatusDetailsDto? = null,
        var savedPDFFiles:  List<MSSSFPDFListDetailsDto>? = null,
        var limsPDFFiles:  List<LIMSFilesFoundDto>? = null,
        var parametersListTested: List<LabResultsParamDto>? = null,
)

data class MSSSFComplianceStatusDetailsDto(
        var sffId: Long? = null,
        var bsNumber: String? = null,
        var complianceRemarks: String? = null,
        var complianceStatus: Boolean? = null,
)

data class LIMSFilesFoundDto(
        var fileSavedStatus: Boolean? = null,
        var fileName: String? = null,
)

data class PDFSaveComplianceStatusDto(
        @NotNull(message = "Required field")
        var ssfID: Long,
        @NotNull(message = "Required field")
        var bsNumber: String,
        @NotNull(message = "Required field")
        var PDFFileName: String,
        @NotNull(message = "Required field")
        var complianceStatus: Boolean,
        @NotNull(message = "Required field")
        var complianceRemarks: String,
)

data class SSFSaveComplianceStatusDto(
        @NotNull(message = "Required field")
        var ssfID: Long,
        @NotNull(message = "Required field")
        var bsNumber: String,
        @NotNull(message = "Required field")
        var complianceStatus: Boolean,
        @NotNull(message = "Required field")
        var complianceRemarks: String,
)

data class CompliantRemediationDto(
        var remarks: String? = null,
        var proFormaInvoiceStatus: Boolean? = null,
        var dateOfRemediation: Date? = null,
        var volumeFuelRemediated: Long?= null,
        var subsistenceTotalNights: Long?= null,
        var transportAirTicket: Long?= null,
        var transportInkm: Long?= null,
)

data class RemediationDto(
        var productType: String? = null,
        var quantityOfFuel: String? = null,
        var contaminatedFuelType: String? = null,
        var applicableKenyaStandard: String? = null,
        var remediationProcedure: String? = null,
        var volumeOfProductContaminated: String? = null,
        var volumeAdded: String? = null,
        var totalVolume: String? = null,
)


data class MSSSFPDFListDetailsDto(
        var pdfSavedId: Long? = null,
        var pdfName: String? = null,
        var sffId: Long? = null,
        var complianceRemarks: String? = null,
        var complianceStatus: Boolean? = null,
)



data class ComplaintApproveRejectAssignDto(
        var division: Long? = null,
        var approved: Int? = 0,
        var approvedRemarks: String? = null,
        var rejected: Int? = 0,
        var rejectedRemarks: String? = null,
        var mandateForOga: Int? = 0,
        var advisedWhereToRemarks: String? = null,
        var assignedIoStatus: Int? = null,
        var assignedIoRemarks: String? = null,
        var assignedIo: Long? = null
)

data class ComplaintApproveDto(
        var division: Long? = null,
        var approved: Int? = null,
        var approvedRemarks: String? = null
)

data class ComplaintRejectDto(
        var rejected: Int? = null,
        var rejectedRemarks: String? = null
)

//data class FindComplaintDetailsDto(
//        var refNumber: String? = null,
//)
data class ComplaintSearchValues(
        var refNumber: String? = null,
        var date: Date? = null
//        var approvedStatus: Int? = null,
//        var assignedIOStatus: Int? = null,
//        var rejectedStatus: Int? = null,
//        var lastName: String? = null
)

data class ComplaintAdviceRejectDto(
        var mandateForOga: Int? = null,
        var advisedWhereToRemarks: String? = null,
        var rejectedRemarks: String? = null
)

data class ComplaintAssignDto(
        var assignedIoStatus: Int? = null,
        var assignedRemarks: String? = null,
        var assignedIo: Long? = null
)

data class ComplaintsDetailsDto(
        var id: Long? = null,
        var refNumber: String? = null,
        var complainantName: String? = null,
        var complainantEmail: String? = null,
        var complainantPhoneNumber: String? = null,
        var complainantPostalAddress: String? = null,
        var complaintCategory: String? = null,
        var complaintTitle: String? = null,
        var complaintDescription: String? = null,
        var broadProductCategory: String? = null,
        var productClassification: String? = null,
        var productSubcategory: String? = null,
        var productName: String? = null,
        var productBrand: String? = null,
        var county: String? = null,
        var town: String? = null,
        var marketCenter: String? = null,
        var buildingName: String? = null,
        var date: Date? = null,
        var status: String? = null,
        var officersList: List<MsUsersDto>? = null,
        var divisionList: List<MsDivisionDto>? = null,
        var approvedStatus: Int? = null,
        var assignedIOStatus: Int? = null,
        var rejectedStatusStatus: Int? = null

)

data class ComplaintsDto(
        var refNumber: String? = null,
        var complainantName: String? = null,
        var complaintCategory: String? = null,
        var complaintTitle: String? = null,
        var date: Date? = null,
        var status: String? = null
)

data class MsDepartmentDto(
        val id: Long? = null,
        val department: String? = null,
        val descriptions: String? = null,
        val directorateId: Long? = null,
        val status: Boolean? = null
)

data class MsUsersDto(
        var id: Long? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var userName: String? = null,
        var email: String? = null,
        val status: Boolean? = null
)


data class MsDivisionDto(
        val id: Long? = null,
        val division: String? = null,
        val descriptions: String? = null,
        val status: Int? = null,
        val departmentId: Long? = null
)

data class NewComplaintDto(
        val complaintDetails: ComplaintDto,
        val customerDetails: ComplaintCustomersDto,
        val locationDetails: ComplaintLocationDto
//        val complaintFilesDetails: ComplaintFilesDto,
)

data class ComplaintFilesDto(
        val fileDetails: List<FileDTO>? = null
)

data class FileDTO(
        var fileType: String? = null,
        var name: String? = null,
        var document: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileDTO

        if (fileType != other.fileType) return false
        if (name != other.name) return false
        if (document != null) {
            if (other.document == null) return false
            if (!document.contentEquals(other.document)) return false
        } else if (other.document != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileType?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (document?.contentHashCode() ?: 0)
        return result
    }
}

data class ComplaintDto(
        var complaintCategory: Long? = null,
        var complaintTitle: String? = null,
        var productClassification: Long? = null,
        var broadProductCategory: Long? = null,
        var productCategory: Long? = null,
        var myProduct: Long? = null,
        var productSubcategory: Long? = null,
        var productBrand: String? = null,
        var complaintDescription: String? = null
)

data class ComplaintCustomersDto(
        var firstName: String? = null,
        var lastName: String? = null,
        var phoneNumber: String? = null,
        var emailAddress: String? = null,
        var postalAddress: String? = null
)

data class ComplaintLocationDto(
        var county: Long? = null,
        var town: Long? = null,
        var marketCenter: String? = null,
        var buildingName: String? = null
)


data class MSTypeDto(
        var uuid: String? = null,
        var typeName: String? = null,
        var markRef: String? = null,
        var description: String? = null,
        var status: Int? = null
)

data class MSComplaintSubmittedSuccessful(
        var message: String? = null
)

