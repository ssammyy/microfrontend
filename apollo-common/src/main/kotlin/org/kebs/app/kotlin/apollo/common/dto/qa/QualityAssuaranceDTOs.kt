package org.kebs.app.kotlin.apollo.common.dto.qa

import org.kebs.app.kotlin.apollo.common.dto.UserEntityDto
import java.io.File
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.number


data class ST10Dto(
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

data class CommonPermitDto(
    var firmName: String? = null,
    var directorList: String? = null,
    var postalAddress: String? = null,
    var physicalAddress: String? = null,
    var contactPerson: String? = null,
    var telephoneNo: String? = null,
    var email: String? = null,
    var fax: String? = null,
    var county: String? = null,
    var town: String? = null,
    var region: String? = null,
    var companyID: Long? = null,
    var plantID: Long? = null,
    var countyID: Long? = null,
    var townID: Long? = null,
    var regionID: Long? = null,
    var firmTypeID: Long? = null,
    var firmTypeName: String? = null,
)

data class WorkPlanDto(
        var firmName: String? = null,
        var refNumber: String? = null,
        var permitNumber: String? = null,
        var physicalAddress: String? = null,
        var town: String? = null,
        var productBrand: String? = null,
        var issueDate: Date? = null,
        var expiryDate: Date? = null,
        var visitsScheduled: Date? = null,
)

data class InvoiceDto(
        var batchID: Long? = null,
        var firmName: String? = null,
        var postalAddress: String? = null,
        var physicalAddress: String? = null,
        var contactPerson: String? = null,
        var telephoneNo: String? = null,
        var email: String? = null,
        var invoiceNumber: String? = null,
        var receiptNo: String? = null,
        var paidDate: Timestamp? = null,
        var totalAmount: BigDecimal? = null,
        var paidStatus: Int? = null,
        var submittedStatus: Int? = null,
        var plantId: Long? = null,
)

data class ConsolidatedInvoiceDto(
    var id: Long? = null,
    var invoiceNumber: String? = null,
    var totalAmount: BigDecimal? = null,
    var paidDate: Timestamp? = null,
    var paidStatus: Int? = null,
    var submittedStatus: Int? = null,
    var receiptNo: String? = null,
)

data class PermitInvoiceDto(
    var permitID: Long? = null,
    var invoiceNumber: String? = null,
    var commodityDescription: String? = null,
    var brandName: String? = null,
    var totalAmount: BigDecimal? = null,
    var paidStatus: Int? = null,
    var permitRefNumber: String? = null,
    var batchID: Long? = null
)

data class SSFPDFListDetailsDto(
    var pdfSavedId: Long? = null,
    var pdfName: String? = null,
    var sffId: Long? = null,
    var complianceRemarks: String? = null,
    var complianceStatus: Boolean? = null,
)

data class SSFComplianceStatusDetailsDto(
    var sffId: Long? = null,
    var bsNumber: String? = null,
    var complianceRemarks: String? = null,
    var complianceStatus: Boolean? = null,
)

data class PermitSSFLabResultsDto(
    var ssfResultsList: List<SSFComplianceStatusDetailsDto>? = null,
    var labResultsList: List<SSFPDFListDetailsDto>? = null,
)

data class PermitAllRemarksDetailsDto(
    var hofQamCompleteness: RemarksAndStatusDto? = null,
    var labResultsCompleteness: RemarksAndStatusDto? = null,
    var pcmApproval: RemarksAndStatusDto? = null,
    var pscMemberApproval: RemarksAndStatusDto? = null,
    var pcmReviewApproval: RemarksAndStatusDto? = null,
    var justificationReport: RemarksAndStatusDto? = null,
)

data class NewBatchInvoiceDto(
    var batchID: Long = -1L,
    var plantID: Long? = null,
    var permitRefNumber: String? = null,
    var permitInvoicesID: Array<Long>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewBatchInvoiceDto

        if (batchID != other.batchID) return false
        if (plantID != other.plantID) return false
        if (permitRefNumber != other.permitRefNumber) return false
        if (permitInvoicesID != null) {
            if (other.permitInvoicesID == null) return false
            if (!permitInvoicesID.contentEquals(other.permitInvoicesID)) return false
        } else if (other.permitInvoicesID != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = batchID.hashCode()
        result = 31 * result + (plantID?.hashCode() ?: 0)
        result = 31 * result + (permitRefNumber?.hashCode() ?: 0)
        result = 31 * result + (permitInvoicesID?.contentHashCode() ?: 0)
        return result
    }
}


data class BatchInvoiceDto(
    var batchDetails: InvoiceDto? = null,
    var AllRelatedBatchInvoices: List<PermitInvoiceDto>? = null,
    )

data class UploadsDtoSTA3(
    var uploadedFiles: List<File>? = null,
    var sta3Status: Boolean? = null
)

data class PermitProcessStepDto(
    var permitID: Long? = null,
    var processStep: Int? = null
)

data class STA1Dto(
    var id: Long? = null,
    var commodityDescription: String? = null,
    var tradeMark: String? = null,
    var applicantName: String? = null,
    var sectionId: Long? = null,
    var permitForeignStatus: Int? = null,
    var attachedPlant: Long? = null,
    var createFmark: Int? = null,
)

data class STA10ProductsManufactureDto(
    var id: Long? = null,
    var productName: String? = null,
    var productBrand: String? = null,
    var productStandardNumber: String? = null,
    var available: Boolean? = null,
    var permitNo: String? = null,
)

data class STA10RawMaterialsDto(
    var id: Long? = null,
    var name: String? = null,
    var origin: String? = null,
    var specifications: String? = null,
    var qualityChecksTestingRecords: String? = null,
)

data class STA10PersonnelDto(
    var id: Long? = null,
    var personnelName: String? = null,
    var qualificationInstitution: String? = null,
    var dateOfEmployment: Date? = null,
)

data class STA10MachineryAndPlantDto(
    var id: Long? = null,
    var machineName: String? = null,
    var typeModel: String? = null,
    var countryOfOrigin: String? = null,
)

data class RemarksAndStatusDto(
    var remarksStatus: Boolean? = null,
    var remarksValue: String? = null,
)

data class LimsFilesFoundDto(
    var fileSavedStatus: Boolean? = null,
    var fileName: String? = null,
)

data class InvoiceDetailsDto(
    var invoiceMasterId: Long? = null,
    var invoiceRef: String? = null,
    var description: String? = null,
    var taxAmount: BigDecimal? = null,
    var subTotalBeforeTax: BigDecimal? = null,
    var totalAmount: BigDecimal? = null,
    var invoiceDetailsList: List<InvoicePerDetailsDto>? = null,
)

data class PermitUploads(
    var permitID: Long? = null,
    var manufactureNonStatus: Int,
    var ordinaryStatus: Int? = null,
    var inspectionReportStatus: Int? = null,
    var sta10Status: Int? = null,
    var sscUploadStatus: Int? = null,
    var scfStatus: Int? = null,
    var ssfStatus: Int?= null,
    var cocStatus: Int?= null,
    var assessmentReportStatus: Int?= null,
    var labResultsStatus: Int?= null,
    var docFileName: String,
//    var docFile: MultipartFile,
    var assessmentRecommendations: String?= null,
)

data class STA10ManufacturingProcessDto(
    var id: Long? = null,
    var processFlowOfProduction: String? = null,
    var operations: String? = null,
    var criticalProcessParametersMonitored: String? = null,
    var frequency: String? = null,
    var processMonitoringRecords: String? = null,
)

data class STA10SectionADto(
    var id: Long? = null,
    var firmName: String? = null,
    var statusCompanyBusinessRegistration: String? = null,
    var ownerNameProprietorDirector: String? = null,
    var postalAddress: String? = null,
    var contactPerson: String? = null,
    var telephone: String? = null,
    var emailAddress: String? = null,
    var physicalLocationMap: String? = null,
    var county: Long? = null,
    var town: Long? = null,
    var totalNumberFemale: Long? = null,
    var totalNumberMale: Long? = null,
    var totalNumberPermanentEmployees: Long? = null,
    var totalNumberCasualEmployees: Long? = null,

    var averageVolumeProductionMonth: String? = null,

    var handledManufacturingProcessRawMaterials: String? = null,
    var handledManufacturingProcessInprocessProducts: String? = null,
    var handledManufacturingProcessFinalProduct: String? = null,
    var strategyInplaceRecallingProducts: String? = null,
    var stateFacilityConditionsRawMaterials: String? = null,
    var stateFacilityConditionsEndProduct: String? = null,
    var testingFacilitiesExistSpecifyEquipment: String? = null,
    var testingFacilitiesExistStateParametersTested: String? = null,
    var testingFacilitiesSpecifyParametersTested: String? = null,
    var calibrationEquipmentLastCalibrated: String? = null,
    var handlingConsumerComplaints: String? = null,
    var companyRepresentative: String? = null,
    var applicationDate: Date? = null,
)

data class STA3Dto(
    var produceOrdersOrStock: String? = null,
    var issueWorkOrderOrEquivalent: String? = null,
    var identifyBatchAsSeparate: String? = null,
    var productsContainersCarryWorksOrder: String? = null,
    var isolatedCaseDoubtfulQuality: String? = null,
    var headQaQualificationsTraining: String? = null,
    var reportingTo: String? = null,
    var separateQcid: String? = null,
    var testsRelevantStandard: String? = null,
    var spoComingMaterials: String? = null,
    var spoProcessOperations: String? = null,
    var spoFinalProducts: String? = null,
    var monitoredQcs: String? = null,
    var qauditChecksCarried: String? = null,
    var informationQcso: String? = null,
    var mainMaterialsPurchasedSpecification: String? = null,
    var adoptedReceiptMaterials: String? = null,
    var storageFacilitiesExist: String? = null,
    var stepsManufacture: String? = null,
    var maintenanceSystem: String? = null,
    var qcsSupplement: String? = null,
    var qmInstructions: String? = null,
    var testEquipmentUsed: String? = null,
    var indicateExternalArrangement: String? = null,
    var levelDefectivesFound: String? = null,
    var levelClaimsComplaints: String? = null,
    var independentTests: String? = null,
    var indicateStageManufacture: String? = null,
    var sta3FilesList: List<FilesListDto>? = null
)

data class FilesListDto(
    var id: Long? = null,
    var name: String? = null,
    var fileType: String? = null,
    var documentType: String? = null,
    var versionNumber: Long? = null,
    var document: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilesListDto

        if (id != other.id) return false
        if (name != other.name) return false
        if (fileType != other.fileType) return false
        if (documentType != other.documentType) return false
        if (versionNumber != other.versionNumber) return false
        if (document != null) {
            if (other.document == null) return false
            if (!document.contentEquals(other.document)) return false
        } else if (other.document != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (fileType?.hashCode() ?: 0)
        result = 31 * result + (documentType?.hashCode() ?: 0)
        result = 31 * result + (versionNumber?.hashCode() ?: 0)
        result = 31 * result + (document?.contentHashCode() ?: 0)
        return result
    }
}

data class PermitEntityDto(
    var id: Long? = null,
    var firmName: String? = null,
    var permitRefNumber: String? = null,
    var productName: String? = null,
    var tradeMark: String? = null,
    var awardedPermitNumber: String? = null,
    var dateOfIssue: Date? = null,
    var dateOfExpiry: Date? = null,
    var permitStatus: String? = null,
    var userId: Long? = null,
    var createdOn: Timestamp? = null,
    var county: String? = null,
    var town: String? = null,
    var region: String? = null,
    var divisionValue: String? = null,
    var sectionValue: String? = null,
    var permitAwardStatus: Boolean? = null,
    var permitExpiredStatus: Boolean? = null,
    var taskID: Long? = null,
    var companyId: Long? = null,
    var permitType: Long? = null,
    var processStatusID: Long? = null,
    var versionNumber: Long? = null,
)

data class PermitDetailsDto(
    var Id: Long? = null,
    var permitNumber: String? = null,
    var permitRefNumber: String? = null,
    var firmName: String? = null,
    var postalAddress: String? = null,
    var physicalAddress: String? = null,
    var contactPerson: String? = null,
    var telephoneNo: String? = null,
    var regionPlantValue: String? = null,
    var countyPlantValue: String? = null,
    var townPlantValue: String? = null,
    var location: String? = null,
    var street: String? = null,
    var buildingName: String? = null,
    var nearestLandMark: String? = null,
    var faxNo: String? = null,
    var plotNo: String? = null,
    var email: String? = null,
    var createdOn: Timestamp? = null,
    var dateOfIssue: Date? = null,
    var dateOfExpiry: Date? = null,
    var commodityDescription: String? = null,
    var brandName: String? = null,
    var standardNumber: String? = null,
    var standardTitle: String? = null,
    var permitForeignStatus: Boolean? = null,
    var assignOfficer: String? = null,
    var assignAssessor: String? = null,
    var divisionValue: String? = null,
    var sectionValue: String? = null,
    var inspectionDate: Date? = null,
    var inspectionScheduledStatus: Boolean? = null,
    var assessmentDate: Date? = null,
    var assessmentScheduledStatus: Boolean? = null,
    var processStatusName: String? = null,
    var versionNumber: Long? = null,
    var fmarkGenerated: Boolean? = null,
    var recommendationRemarks: String? = null,
    var factoryVisit: Date? = null,
    var firmTypeID: Long? = null,
    var firmTypeName: String? = null,
    var permitTypeName: String? = null,
    var permitTypeID: Long? = null,
    var permitAwardStatus: Boolean? = null,
    var invoiceGenerated: Boolean? = null,
    var approvedRejectedScheme: Boolean? = null,
    var sendForPcmReview: Boolean? = null,
    var sendApplication: Boolean? = null,
    var pcmReviewApprove: Boolean? = null,
    var hofQamCompletenessStatus: Boolean? = null,
    var generateSchemeStatus: Boolean? = null,
    var resubmitApplicationStatus: Boolean? = null,
    var processStep: Int? = null,
    var processStatusID: Long? = null,
    var fmarkGeneratedID: Long? = null,
    var oldPermitStatus: Boolean? = null,
)

data class AllPermitDetailsDto(
    var permitDetails: PermitDetailsDto? = null,
    var remarksDetails: PermitAllRemarksDetailsDto? = null,
    var invoiceDetails: InvoiceDetailsDto? = null,
//    var standardList: List<UserEntityDto>? = null,
    var officerList: List<UserEntityDto>? = null,
    var oldVersionList: List<PermitEntityDto>? = null,
    var ordinaryFilesList: List<FilesListDto>? = null,
    var sta3FilesList: List<FilesListDto>? = null,
    var sta10FilesList: List<FilesListDto>? = null,
    var labResultsList: PermitSSFLabResultsDto? = null,
    var schemeOfSuperVision: FilesListDto? = null,
    var batchID: Long? = null
)


data class AllSTA10DetailsDto(
    var sta10FirmDetails: STA10SectionADto? = null,
    var sta10PersonnelDetails: List<STA10PersonnelDto>? = null,
    var sta10ProductsManufactureDetails: List<STA10ProductsManufactureDto>? = null,
    var sta10RawMaterialsDetails: List<STA10RawMaterialsDto>? = null,
    var sta10MachineryAndPlantDetails: List<STA10MachineryAndPlantDto>? = null,
    var sta10ManufacturingProcessDetails: List<STA10ManufacturingProcessDto>? = null,
    var sta10FilesList: List<FilesListDto>? = null,
)

data class StandardsDto(
    var id: Long? = null,
    var standardTitle: String? = null,
    var standardNumber: String? = null,
)

data class InvoicePerDetailsDto(
    var id: Long? = null,
    var itemDescName: String? = null,
    var itemAmount: BigDecimal? = null,
    var inspectionStatus: Boolean? = null,
    var permitStatus: Boolean? = null,
    var fmarkStatus: Boolean? = null,
)

data class SSCApprovalRejectionDto(
    var approvedRejectedScheme: Int? = null,
    var approvedRejectedSchemeRemarks: String? = null,
)

data class ResubmitApplicationDto(
    var resubmitRemarks: String? = null,
    var resubmittedDetails: String? = null,
)

data class TaskDto(
    var permitId: Long? = null,
    var taskName: String? = null,
    var taskCreateTime: java.util.Date? = null,
    var permitRefNo: String? = null,
)


data class PlantsDetailsDto(
    var id: Long? = null,
    var companyProfileId: Long? = null,
    var county: String? = null,
    var town: String? = null,
    var location: String? = null,
    var street: String? = null,
    var buildingName: String? = null,
    var nearestLandMark: String? = null,
    var postalAddress: String? = null,
    var telephone: String? = null,
    var emailAddress: String? = null,
    var physicalAddress: String? = null,
    var faxNo: String? = null,
    var plotNo: String? = null,
    var designation: String? = null,
    var contactPerson: String? = null,
)





