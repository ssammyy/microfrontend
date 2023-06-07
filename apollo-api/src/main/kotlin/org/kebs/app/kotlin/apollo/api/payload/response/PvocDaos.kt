package org.kebs.app.kotlin.apollo.api.payload.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.kebs.app.kotlin.apollo.api.payload.request.RfcItemForm
import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import org.kebs.app.kotlin.apollo.store.model.RiskProfileEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.*
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp

class PvocComplaintDao {
    var complaintId: Long? = null
    var refNo: String? = null
    var complaintName: String? = null
    var phoneNo: String? = null
    var address: String? = null
    var cocNo: String? = null
    var rfcNo: String? = null
    var email: String? = null
    var complaintDescription: String? = null
    var complaintTitle: String? = null
    var pvocAgent: String? = null
    var pvocAgentId: Long? = null
    var categoryId: Long? = null
    var categoryName: String? = null
    var subCategoryId: Long? = null
    var subCategoryName: String? = null
    var recommendationId: Long? = null
    var recommendationAction: String? = null
    var recommendationDesc: String? = null
    var pvocQueryResult: String? = null
    var mpvocRemarks: String? = null
    var hodRemarks: String? = null
    var pvocRemarks: String? = null
    var conclusion: String? = null
    var complaintDate: Timestamp? = null
    var officerName: String? = null
    var hodName: String? = null
    var mpvocName: String? = null
    var reviewStatus: String? = null

    companion object {
        fun fromEntity(complaint: PvocComplaintEntity, manufacturer: Boolean): PvocComplaintDao {
            val data = PvocComplaintDao().apply {
                complaintId = complaint.id
                refNo = complaint.refNo
                cocNo = complaint.cocNumber
                rfcNo = complaint.rfcNumber
                complaintName = complaint.firstName + " " + complaint.lastName
                phoneNo = complaint.phoneNo
                address = complaint.address
                email = complaint.email
                reviewStatus = complaint.reviewStatus
                complaintDate = complaint.createdOn
                complaintTitle = complaint.complaintTitle
                complaintDescription = complaint.generalDescription
                pvocAgent = complaint.pvocAgent?.partnerName
                pvocAgentId = complaint.pvocAgent?.id
                categoryId = complaint.compliantNature?.id
                categoryName = complaint.compliantNature?.name
                subCategoryId = complaint.compliantSubCategory?.id
                subCategoryName = complaint.compliantSubCategory?.name
                conclusion = complaint.recomendation
            }
            if (!manufacturer) {
                data.apply {
                    pvocRemarks = complaint.recomendation ?: "N/A"
                    hodRemarks = complaint.hodRemarks ?: "N/A"
                    pvocQueryResult = complaint.agentReviewRemarks ?: "N/A"
                    mpvocRemarks = complaint.mpvocRemarks ?: "N/A"
                    recommendationAction = complaint.recommendedAction?.action
                    recommendationId = complaint.recommendedAction?.id
                    recommendationDesc = complaint.recommendedAction?.description
                    officerName = "${complaint.pvocUser?.firstName} ${complaint.pvocUser?.lastName}"
                    hodName = "${complaint.hod?.firstName} ${complaint.hod?.lastName}"
                    mpvocName = "${complaint.mpvoc?.firstName} ${complaint.mpvoc?.lastName}"
                }
            }
            return data
        }

        fun fromList(complaints: List<PvocComplaintEntity>, manufacturer: Boolean = true): List<PvocComplaintDao> {
            val daos = mutableListOf<PvocComplaintDao>()
            complaints.forEach { daos.add(fromEntity(it, manufacturer)) }
            return daos
        }
    }
}

class PvocComplaintSubCategoryDao {
    var categoryId: Long? = null
    var name: String? = null

    companion object {
        fun fromEntity(category: PvocComplaintCertificationSubCategoriesEntity): PvocComplaintSubCategoryDao {
            return PvocComplaintSubCategoryDao().apply {
                categoryId = category.id
                name = category.name
            }
        }

        fun fromList(categories: List<PvocComplaintCertificationSubCategoriesEntity>): List<PvocComplaintSubCategoryDao> {
            val daos = mutableListOf<PvocComplaintSubCategoryDao>()
            categories.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocComplaintCategoryDao {
    var categoryId: Long? = null
    var name: String? = null
    var subCategories: List<PvocComplaintSubCategoryDao>? = null

    companion object {
        fun fromEntity(category: PvocComplaintCategoryEntity): PvocComplaintCategoryDao {
            val dao = PvocComplaintCategoryDao().apply {
                categoryId = category.id
                name = category.name
            }
            category.pvocComplaintCertificationSubCategoriesById?.let {
                dao.subCategories = PvocComplaintSubCategoryDao.fromList(it)
            }
            return dao
        }

        fun fromList(categories: List<PvocComplaintCategoryEntity>): List<PvocComplaintCategoryDao> {
            val daos = mutableListOf<PvocComplaintCategoryDao>()
            categories.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocComplaintRecommendationDao {
    var recommendationId: Long? = null
    var action: String? = null
    var description: String? = null

    companion object {
        fun fromEntity(recommendation: PvocComplaintRecommendationEntity): PvocComplaintRecommendationDao {
            val dao = PvocComplaintRecommendationDao().apply {
                recommendationId = recommendation.id
                action = recommendation.action
                description = recommendation.description
            }
            return dao
        }

        fun fromList(complaints: List<PvocComplaintRecommendationEntity>): List<PvocComplaintRecommendationDao> {
            val daos = mutableListOf<PvocComplaintRecommendationDao>()
            complaints.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocWaiverAttachmentDao {
    var fileId: Long? = null
    var status: Long? = null
    var name: String? = null
    var description: String? = null
    var documentRef: String? = null
    var documentRefType: String? = null
    var fileType: String? = null


    companion object {
        fun fromEntity(documentsEntity: PvocApplicationDocumentsEntity): PvocWaiverAttachmentDao {
            val dao = PvocWaiverAttachmentDao().apply {
                fileId = documentsEntity.id
                name = documentsEntity.name
                documentRef = documentsEntity.refId
                documentRefType = documentsEntity.refType
                fileType = documentsEntity.fileType
                status = documentsEntity.status
                description = documentsEntity.description
            }
            return dao
        }

        fun fromList(documents: List<PvocApplicationDocumentsEntity>): List<PvocWaiverAttachmentDao> {
            val daos = mutableListOf<PvocWaiverAttachmentDao>()
            documents.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocWaiverProductDao {
    var productId: Long? = null
    var hsCode: String? = null
    var waiversApplicationId: Long? = null
    var unit: String? = null
    var quantity: String? = null
    var totalAmount: String? = null
    var origin: String? = null
    var productDescription: String? = null
    var serialNo: String? = null
    var currency: String? = null
    var createdBy: String? = null
    var createdOn: Timestamp? = null
    var status: Long? = null

    companion object {
        fun fromEntity(masterListEntity: PvocMasterListEntity): PvocWaiverProductDao {
            val dao = PvocWaiverProductDao().apply {
                productId = masterListEntity.id
                hsCode = masterListEntity.hsCode
                waiversApplicationId = masterListEntity.waiversApplicationId
                unit = masterListEntity.unit
                quantity = masterListEntity.quantity
                totalAmount = masterListEntity.totalAmount
                origin = masterListEntity.origin
                productDescription = masterListEntity.productDescription
                serialNo = masterListEntity.serialNo
                currency = masterListEntity.currency
                createdBy = masterListEntity.createdBy
                createdOn = masterListEntity.createdOn
                status = masterListEntity.status
            }
            return dao
        }

        fun fromList(productss: List<PvocMasterListEntity>): List<PvocWaiverProductDao> {
            val daos = mutableListOf<PvocWaiverProductDao>()
            productss.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }

}

class PvocWaiverRemarksDao {
    var remarkId: Long? = null
    var status: Long? = null
    var remarks: String? = null
    var waiverAction: String? = null
    var waiverId: Long? = null
    var names: String? = null
    var createdOn: Timestamp? = null


    companion object {
        fun fromEntity(documentsEntity: PvocWaiversRemarksEntity): PvocWaiverRemarksDao {
            val dao = PvocWaiverRemarksDao().apply {
                remarkId = documentsEntity.id
                remarks = documentsEntity.remarks
                waiverId = documentsEntity.waiverId
                waiverAction = documentsEntity.waiverAction
                names = "${documentsEntity.firstName} ${documentsEntity.lastName}".trim()
                status = documentsEntity.status
                createdOn = documentsEntity.createdOn
            }
            return dao
        }

        fun fromList(documents: List<PvocWaiversRemarksEntity>): List<PvocWaiverRemarksDao> {
            val daos = mutableListOf<PvocWaiverRemarksDao>()
            documents.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocWaiverDao {
    var waiverId: Long = 0
    var status: Int? = null
    var applicantName: String? = null
    var phoneNumber: String? = null
    var emailAddress: String? = null
    var kraPin: String? = null
    var address: String? = null
    var category: String? = null
    var justification: String? = null
    var productDescription: String? = null
    var documentation: String? = null
    var reviewStatus: String? = null
    var nscApprovalStatus: String? = null
    var csApprovalStatus: String? = null
    var serialNo: String? = null
    var csResponceCode: String? = null
    var csResponceMessage: String? = null
    var submittedOn: Timestamp? = null

    companion object {
        fun fromEntity(waiver: PvocWaiversApplicationEntity, manufacturer: Boolean): PvocWaiverDao {
            val dao = PvocWaiverDao().apply {
                waiverId = waiver.id
                status = waiver.status
                applicantName = waiver.applicantName
                phoneNumber = waiver.phoneNumber
                emailAddress = waiver.emailAddress
                kraPin = waiver.kraPin
                address = waiver.address
                category = waiver.category
                justification = waiver.justification
                productDescription = waiver.productDescription
                documentation = waiver.documentation
                reviewStatus = waiver.reviewStatus
                serialNo = waiver.serialNo
                submittedOn = waiver.createdOn
            }
            if (!manufacturer) {
                dao.apply {
                    nscApprovalStatus = waiver.nscApprovalStatus
                    csApprovalStatus = waiver.csApprovalStatus
                    csResponceCode = waiver.csResponceCode
                    csResponceMessage = waiver.csResponceMessage
                }
            }
            return dao
        }

        fun fromList(waivers: List<PvocWaiversApplicationEntity>, manufacturer: Boolean): List<PvocWaiverDao> {
            val daos = mutableListOf<PvocWaiverDao>()
            waivers.forEach { daos.add(fromEntity(it, manufacturer)) }
            return daos
        }
    }
}

class PvocApplicationDto {
    var exemptionId: Long? = 0
    var status: Int? = null
    var companyName: String? = null
    var companyPinNo: String? = null
    var email: String? = null
    var sn: String? = null
    var applicationStatus: String? = null
    var telephoneNo: String? = null
    var postalAddress: String? = null
    var physicalLocation: String? = null
    var contactPerson: String? = null
    var contactName: String? = null
    var contactEmail: String? = null
    var address: String? = null
    var hsCode: String? = null
    var applicationDate: Timestamp? = null

    companion object {
        fun fromEntity(exemption: PvocApplicationEntity): PvocApplicationDto {
            val dao = PvocApplicationDto().apply {
                exemptionId = exemption.id
                status = exemption.status
                companyName = exemption.conpanyName
                companyPinNo = exemption.companyPinNo
                email = exemption.email
                sn = exemption.sn
                applicationStatus = exemption.reviewStatus
                address = exemption.address
                telephoneNo = exemption.telephoneNo
                postalAddress = exemption.postalAadress
                physicalLocation = exemption.physicalLocation
                contactPerson = exemption.contactPersorn
                contactName = exemption.contactName
                contactEmail = exemption.contactEmail
                applicationDate = exemption.createdOn
            }
            return dao
        }

        fun fromList(exemption: List<PvocApplicationEntity>): List<PvocApplicationDto> {
            val daos = mutableListOf<PvocApplicationDto>()
            exemption.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocApplicationProductDao {
    var productId: Long? = 0
    var productName: String? = null
    var brand: String? = null
    var kebsStandardizationMarkPermit: String? = null
    var expirelyDate: String? = null
    var section: String? = null
    var status: Int? = null

    companion object {
        fun fromEntity(exemptionProduct: PvocApplicationProductsEntity): PvocApplicationProductDao {
            val dao = PvocApplicationProductDao().apply {
                productId = exemptionProduct.id
                productName = exemptionProduct.productName
                brand = exemptionProduct.brand
                kebsStandardizationMarkPermit = exemptionProduct.kebsStandardizationMarkPermit
                expirelyDate = exemptionProduct.expirelyDate
                section = exemptionProduct.section
                status = exemptionProduct.status
            }
            return dao
        }

        fun fromList(products: List<PvocApplicationProductsEntity>): List<PvocApplicationProductDao> {
            val daos = mutableListOf<PvocApplicationProductDao>()
            products.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocExceptionRawMaterialDao {
    var entryId: Long = 0
    var rawMaterialDescription: String? = null
    var countryOfOrgin: String? = null
    var endProduct: String? = null
    var hsCode: String? = null
    var dutyRate: Long? = null
    var exceptionId: Long? = null
    var hsDescription: String? = null
    var status: Long? = null

    companion object {
        fun fromEntity(exemptionProduct: PvocExceptionRawMaterialCategoryEntity): PvocExceptionRawMaterialDao {
            val dao = PvocExceptionRawMaterialDao().apply {
                entryId = exemptionProduct.id
                rawMaterialDescription = exemptionProduct.rawMaterialDescription
                countryOfOrgin = exemptionProduct.countryOfOrgin
                endProduct = exemptionProduct.endProduct
                dutyRate = exemptionProduct.dutyRate
                exceptionId = exemptionProduct.exceptionId
                status = exemptionProduct.status
            }
            return dao
        }

        fun fromList(products: List<PvocExceptionRawMaterialCategoryEntity>): List<PvocExceptionRawMaterialDao> {
            val daos = mutableListOf<PvocExceptionRawMaterialDao>()
            products.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocExceptionMainMachineryDao {
    var machineryId: Long = 0
    var machineDescription: String? = null
    var countryOfOrigin: String? = null
    var makeModel: String? = null
    var hsCode: String? = null
    var dutyRate: Long? = null
    var exceptionId: Long? = null
    var status: Long? = null

    companion object {
        fun fromEntity(exemptionProduct: PvocExceptionMainMachineryCategoryEntity): PvocExceptionMainMachineryDao {
            val dao = PvocExceptionMainMachineryDao().apply {
                machineryId = exemptionProduct.id
                machineDescription = exemptionProduct.machineDescription
                countryOfOrigin = exemptionProduct.countryOfOrigin
                makeModel = exemptionProduct.makeModel
                hsCode = exemptionProduct.hsCode
                dutyRate = exemptionProduct.dutyRate
                exceptionId = exemptionProduct.exceptionId
                status = exemptionProduct.status
            }
            return dao
        }

        fun fromList(products: List<PvocExceptionMainMachineryCategoryEntity>): List<PvocExceptionMainMachineryDao> {
            val daos = mutableListOf<PvocExceptionMainMachineryDao>()
            products.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}

class PvocExceptionIndustrialSparesDao : Serializable {
    var spareId: Long = 0
    var hsCode: String? = null
    var industrialSpares: String? = null
    var countryOfOrigin: String? = null
    var machineToFit: String? = null
    var exceptionId: Long? = null
    var status: Long? = null

    companion object {
        fun fromEntity(exemptionProduct: PvocExceptionIndustrialSparesCategoryEntity): PvocExceptionIndustrialSparesDao {
            val dao = PvocExceptionIndustrialSparesDao().apply {
                spareId = exemptionProduct.id
                hsCode = exemptionProduct.hsCode
                industrialSpares = exemptionProduct.industrialSpares
                countryOfOrigin = exemptionProduct.countryOfOrigin
                hsCode = exemptionProduct.hsCode
                machineToFit = exemptionProduct.machineToFit
                exceptionId = exemptionProduct.exceptionId
                status = exemptionProduct.status
            }
            return dao
        }

        fun fromList(products: List<PvocExceptionIndustrialSparesCategoryEntity>): List<PvocExceptionIndustrialSparesDao> {
            val daos = mutableListOf<PvocExceptionIndustrialSparesDao>()
            products.forEach { daos.add(fromEntity(it)) }
            return daos
        }
    }
}


class PvocPartnerTimelinesDataDto : Serializable {
    var certNumber: String? = null
    var certType: String? = null
    var ucrNumber: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var rfcDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var dateOfInspection: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var docIssueDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var requestDateOfInspection: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var cocConfirmationDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var accDocumentsSubmissionDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var finalDocumentsSubmissionDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var paymentDate: Timestamp? = null
    var rfcToInspectionDays: Long = 0
    var inspectionToIssuanceDays: Long = 0
    var rfcToIssuanceDays: Long = 0
    var accDocumentsToIssuanceDays: Long = 0
    var paymentToIssuanceDays: Long = 0
    var finalDocumentsToIssuanceDays: Long = 0
    var route: String? = null

    companion object {
        fun fromEntity(timeline: PvocTimelinesDataEntity): PvocPartnerTimelinesDataDto {
            return PvocPartnerTimelinesDataDto().apply {
                certNumber = timeline.certNumber
                certType = timeline.certType
                ucrNumber = timeline.ucrNumber
                rfcDate = timeline.rfcDate
                paymentDate = timeline.paymentDate
                dateOfInspection = timeline.dateOfInspection
                docIssueDate = timeline.docIssueDate
                requestDateOfInspection = timeline.requestDateOfInspection
                cocConfirmationDate = timeline.docConfirmationDate
                accDocumentsSubmissionDate = timeline.accDocumentsSubmissionDate
                finalDocumentsSubmissionDate = timeline.finalDocumentsSubmissionDate
                rfcToIssuanceDays = timeline.rfcToIssuanceDays
                rfcToInspectionDays = timeline.rfcToInspectionDays
                accDocumentsToIssuanceDays = timeline.accDocumentsToIssuanceDays
                paymentToIssuanceDays = timeline.paymentToIssuanceDays
                finalDocumentsToIssuanceDays = timeline.finalDocumentsToIssuanceDays
                route = timeline.route
            }
        }

        fun fromList(timelines: List<PvocTimelinesDataEntity>): List<PvocPartnerTimelinesDataDto> {
            val dtos = mutableListOf<PvocPartnerTimelinesDataDto>()
            timelines.forEach {
                dtos.add(fromEntity(it))
            }
            return dtos
        }
    }
}


class PvocPartnerCountryDao {
    var countryId: Long? = null
    var countryName: String? = null
    var abbreviation: String? = null
    var regionId: Long? = null
    var description: String? = null

    companion object {
        fun fromEntity(country: PvocPartnersCountriesEntity): PvocPartnerCountryDao {
            return PvocPartnerCountryDao().apply {
                countryId = country.id
                countryName = country.countryName
                abbreviation = country.abbreviation
                regionId = country.regionId?.id
                description = country.description
            }
        }

        fun fromList(timelines: List<PvocPartnersCountriesEntity>): List<PvocPartnerCountryDao> {
            val dtos = mutableListOf<PvocPartnerCountryDao>()
            timelines.forEach {
                dtos.add(fromEntity(it))
            }
            return dtos
        }
    }
}

// PVOC API
class RiskProfileDao {
    var hsCode: String? = null
    var brandName: String? = null
    var productDescription: String? = null
    var countryOfSupply: String? = null
    var manufacturer: String? = null
    var importerName: String? = null
    var importerPin: String? = null
    var exporterName: String? = null
    var exporterPin: String? = null
    var riskLevel: String? = null
    var riskDescription: String? = null
    var remarks: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var categorizationDate: java.sql.Date? = null

    companion object {
        fun fromEntity(pvocPartner: RiskProfileEntity): RiskProfileDao {
            return RiskProfileDao().apply {
                hsCode = pvocPartner.hsCode
                brandName = pvocPartner.brandName
                productDescription = pvocPartner.productDescription
                countryOfSupply = pvocPartner.countryOfSupply
                manufacturer = pvocPartner.manufacturer
                importerName = pvocPartner.importerName
                importerPin = pvocPartner.importerPin
                exporterName = pvocPartner.exporterName
                exporterPin = pvocPartner.exporterPin
                riskLevel = pvocPartner.riskLevel
                riskDescription = pvocPartner.riskDescription
                remarks = pvocPartner.remarks
                categorizationDate = pvocPartner.categorizationDate
            }
        }

        fun fromList(timelines: List<RiskProfileEntity>): List<RiskProfileDao> {
            val dtos = mutableListOf<RiskProfileDao>()
            timelines.forEach {
                dtos.add(fromEntity(it))
            }
            return dtos
        }
    }
}

// Management UI
class UiRiskProfileDao {
    var riskId: Long? = null
    var hsCode: String? = null
    var brandName: String? = null
    var productDescription: String? = null
    var countryOfSupply: String? = null
    var manufacturer: String? = null
    var importerName: String? = null
    var importerPin: String? = null
    var exporterName: String? = null
    var exporterPin: String? = null
    var riskLevel: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var receivedOn: Timestamp? = null
    var riskDescription: String? = null
    var remarks: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var categorizationDate: java.sql.Date? = null

    companion object {
        fun fromEntity(pvocPartner: RiskProfileEntity): UiRiskProfileDao {
            return UiRiskProfileDao().apply {
                riskId = pvocPartner.id
                receivedOn = pvocPartner.createdOn
                hsCode = pvocPartner.hsCode
                brandName = pvocPartner.brandName
                productDescription = pvocPartner.productDescription
                countryOfSupply = pvocPartner.countryOfSupply
                manufacturer = pvocPartner.manufacturer
                importerName = pvocPartner.importerName
                importerPin = pvocPartner.importerPin
                exporterName = pvocPartner.exporterName
                exporterPin = pvocPartner.exporterPin
                riskLevel = pvocPartner.riskLevel
                riskDescription = pvocPartner.riskDescription
                remarks = pvocPartner.remarks
                categorizationDate = pvocPartner.categorizationDate
            }
        }

        fun fromList(timelines: List<RiskProfileEntity>): List<UiRiskProfileDao> {
            val dtos = mutableListOf<UiRiskProfileDao>()
            timelines.forEach {
                dtos.add(fromEntity(it))
            }
            return dtos
        }
    }
}

class PvocQueryResponseDao {
    var serialNumber: String? = null
    var queryResponse: String? = null
    var responseFrom: String? = null
    var linkToUploads: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var responseDate: Timestamp? = null

    companion object {
        fun fromEntity(pvocPartner: PvocQueryResponseEntity): PvocQueryResponseDao {
            return PvocQueryResponseDao().apply {
                serialNumber = pvocPartner.serialNumber
                responseDate = pvocPartner.createdOn
                queryResponse = pvocPartner.response
                linkToUploads = pvocPartner.linkToUploads
                responseFrom = pvocPartner.responseFrom
            }
        }

        fun fromList(timelines: Iterable<PvocQueryResponseEntity>): List<PvocQueryResponseDao> {
            val dtos = mutableListOf<PvocQueryResponseDao>()
            timelines.forEach {
                dtos.add(fromEntity(it))
            }
            return dtos
        }
    }
}

// Api Query responses
class PvocPartnerQueryDao {
    var rfcNumber: String? = null
    var idfNumber: String? = null
    var invoiceNumber: String? = null
    var ucrNumber: String? = null
    var documentType: String? = null
    var certNumber: String? = null
    var serialNumber: String? = null
    var query: String? = null
    var queryOrigin: String? = null
    var response: String? = null
    var conclusion: String? = null
    var conclusionStatus: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var dateOpened: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var dateClosed: Timestamp? = null
    var responses: List<PvocQueryResponseDao>? = null

    companion object {
        fun fromEntity(pvocPartner: PvocQueriesEntity): PvocPartnerQueryDao {
            return PvocPartnerQueryDao().apply {
                rfcNumber = pvocPartner.rfcNumber
                idfNumber = pvocPartner.idfNumber
                invoiceNumber = pvocPartner.invoiceNumber
                ucrNumber = pvocPartner.ucrNumber
                documentType = pvocPartner.certType
                certNumber = pvocPartner.certNumber
                serialNumber = pvocPartner.serialNumber
                query = pvocPartner.queryDetails
                queryOrigin = pvocPartner.queryOrigin
                response = pvocPartner.queryResponse
                conclusion = pvocPartner.conclusion
                conclusionStatus = when (pvocPartner.conclusionStatus) {
                    1 -> "COMPLETED"
                    else -> "PENDING"
                }
                dateOpened = pvocPartner.createdOn
                dateClosed = pvocPartner.conclusionDate
                responses = PvocQueryResponseDao.fromList(pvocPartner.responses.orEmpty())

            }
        }

        fun fromList(timelines: List<PvocQueriesEntity>): List<PvocPartnerQueryDao> {
            val dtos = mutableListOf<PvocPartnerQueryDao>()
            timelines.forEach {
                dtos.add(fromEntity(it))
            }
            return dtos
        }
    }
}

// UI Query interface
class UiPvocPartnerQueryDao {
    var queryId: Long? = null
    var partnerId: Long? = null
    var cdId: Long? = null
    var rfcId: Long? = null
    var rfcType: String? = null
    var rfcNumber: String? = null
    var idfNumber: String? = null
    var invoiceNumber: String? = null
    var ucrNumber: String? = null
    var documentType: String? = null
    var certNumber: String? = null
    var serialNumber: String? = null
    var query: String? = null
    var queryOrigin: String? = null
    var response: String? = null
    var conclusion: String? = null
    var conclusionStatus: String? = null

    var opennedBy: String? = null
    var clossedBy: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var dateOpened: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var dateClosed: Timestamp? = null
    var responses: List<PvocQueryResponseDao>? = null

    companion object {
        fun fromEntity(pvocPartner: PvocQueriesEntity): UiPvocPartnerQueryDao {
            return UiPvocPartnerQueryDao().apply {
                queryId = pvocPartner.id
                cdId = pvocPartner.cdId
                rfcId = pvocPartner.rfcId
                rfcType = pvocPartner.rfcType
                rfcNumber = pvocPartner.rfcNumber
                idfNumber = pvocPartner.idfNumber
                invoiceNumber = pvocPartner.invoiceNumber
                ucrNumber = pvocPartner.ucrNumber
                documentType = pvocPartner.certType
                certNumber = pvocPartner.certNumber
                serialNumber = pvocPartner.serialNumber
                query = pvocPartner.queryDetails
                queryOrigin = pvocPartner.queryOrigin
                response = pvocPartner.queryResponse
                conclusion = pvocPartner.conclusion
                conclusionStatus = when (pvocPartner.conclusionStatus) {
                    1 -> "COMPLETED"
                    else -> "PENDING"
                }
                dateOpened = pvocPartner.createdOn
                dateClosed = pvocPartner.conclusionDate
                responses = PvocQueryResponseDao.fromList(pvocPartner.responses.orEmpty())
                opennedBy = pvocPartner.createdBy
                clossedBy = pvocPartner.conclusionBy
            }
        }

        fun fromList(timelines: List<PvocQueriesEntity>): List<UiPvocPartnerQueryDao> {
            val dtos = mutableListOf<UiPvocPartnerQueryDao>()
            timelines.forEach {
                dtos.add(fromEntity(it))
            }
            return dtos
        }
    }
}

class RfcDao {
    var rfcId: Long? = null
    var rfcNumber: String? = null
    var idfNumber: String? = null
    var ucrNumber: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var rfcDate: Date? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var receiveDate: Timestamp? = null
    var countryOfDestination: String? = null
    var applicationType: String? = null
    var sorReference: String? = null
    var solReference: String? = null
    var importerName: String? = null
    var importerPin: String? = null
    var importerAddress1: String? = null
    var importerAddress2: String? = null
    var importerCity: String? = null
    var importerCountry: String? = null
    var importerZipCode: String? = null
    var importerTelephoneNumber: String? = null
    var importerFaxNumber: String? = null
    var importerEmail: String? = null
    var exporterName: String? = null
    var exporterPin: String? = null
    var exporterAddress1: String? = null
    var exporterAddress2: String? = null
    var exporterCity: String? = null
    var exporterCountry: String? = null
    var exporterZipCode: String? = null
    var exporterTelephoneNumber: String? = null
    var exporterFaxNumber: String? = null
    var exporterEmail: String? = null
    var placeOfInspection: String? = null
    var placeOfInspectionAddress: String? = null
    var placeOfInspectionEmail: String? = null
    var placeOfInspectionContacts: String? = null
    var portOfLoading: String? = null
    var portOfDischarge: String? = null
    var shipmentMethod: String? = null
    var countryOfSupply: String? = null
    var route: String? = null
    var goodsCondition: String? = null
    var assemblyState: String? = null
    var linkToAttachedDocuments: List<String>? = null
    var clientId: String? = null
    var reviewStatus: String? = null
    var reviewRemarks: String? = null
    var partnerId: Long? = null
    var items: List<RfcItemForm>? = null

    companion object {
        fun fromEntity(rfc: RfcEntity): RfcDao {
            val rfcEntity = RfcDao()
            rfcEntity.rfcId = rfc.id
            rfcEntity.partnerId = rfc.partner
            rfcEntity.rfcNumber = rfc.rfcNumber
            rfcEntity.idfNumber = rfc.idfNumber
            rfcEntity.ucrNumber = rfc.ucrNumber
            rfcEntity.rfcDate = rfc.rfcDate
            rfcEntity.countryOfDestination = rfc.countryOfDestination
            rfcEntity.applicationType = rfc.applicationType
            rfcEntity.solReference = rfc.solReference
            rfcEntity.sorReference = rfc.sorReference
            rfcEntity.importerName = rfc.importerName
            rfcEntity.importerCountry = rfc.importerCountry
            rfcEntity.importerAddress1 = rfc.importerAddress1
            rfcEntity.importerAddress2 = rfc.importerAddress2
            rfcEntity.importerCity = rfc.importerCity
            rfcEntity.importerFaxNumber = rfc.importerFaxNumber
            rfcEntity.importerPin = rfc.importerPin
            rfcEntity.importerZipCode = rfc.importerZipcode
            rfcEntity.importerTelephoneNumber = rfc.importerTelephoneNumber
            rfcEntity.importerEmail = rfc.importerEmail
            rfcEntity.exporterName = rfc.exporterName
            rfcEntity.exporterPin = rfc.exporterPin
            rfcEntity.exporterCity = rfc.exporterCity
            rfcEntity.exporterAddress1 = rfc.exporterAddress1
            rfcEntity.exporterAddress2 = rfc.exporterAddress2
            rfcEntity.exporterCountry = rfc.exporterCountry
            rfcEntity.exporterEmail = rfc.exporterEmail
            rfcEntity.exporterFaxNumber = rfc.exporterFaxNumber
            rfcEntity.exporterTelephoneNumber = rfc.exporterTelephoneNumber
            rfcEntity.exporterZipCode = rfc.exporterZipcode
            rfcEntity.placeOfInspection = rfc.placeOfInspection
            rfcEntity.placeOfInspectionAddress = rfc.placeOfInspectionAddress
            rfcEntity.placeOfInspectionContacts = rfc.placeOfInspectionContacts
            rfcEntity.placeOfInspectionEmail = rfc.placeOfInspectionEmail
            rfcEntity.portOfDischarge = rfc.portOfDischarge
            rfcEntity.portOfLoading = rfc.portOfLoading
            rfcEntity.shipmentMethod = rfc.shipmentMethod
            rfcEntity.countryOfSupply = rfc.countryOfSupply
            rfcEntity.route = rfc.route
            rfcEntity.receiveDate = rfc.createdOn
            rfcEntity.goodsCondition = rfc.goodsCondition
            rfcEntity.assemblyState = rfc.assemblyState
            rfcEntity.linkToAttachedDocuments = rfc.linkToAttachedDocuments?.split(",")
            rfcEntity.clientId = rfc.createdBy
            rfcEntity.reviewStatus = when (rfc.reviewStatus) {
                1 -> "REVIEWED"
                2 -> "REJECTED"
                else -> "NOT REVIEWED"
            }
            rfcEntity.reviewRemarks = rfc.reviewRemarks
            return rfcEntity
        }

        fun fromList(rfcs: List<RfcEntity>): List<RfcDao> {
            val dtos = mutableListOf<RfcDao>()
            rfcs.forEach {
                dtos.add(RfcDao.fromEntity(it))
            }
            return dtos
        }
    }
}

class RfcCorDao {
    var rfcId: Long? = null
    var rfcNumber: String? = null
    var idfNumber: String? = null
    var ucrNumber: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: Date? = null
    var countryOfDestination: String? = null
    var applicationType: String? = null
    var importerName: String? = null
    var importerPin: String? = null
    var importerAddress1: String? = null
    var importerAddress2: String? = null
    var importerCity: String? = null
    var importerCountry: String? = null
    var importerZipCode: String? = null
    var importerTelephoneNumber: String? = null
    var importerFaxNumber: String? = null
    var importerEmail: String? = null
    var exporterName: String? = null
    var exporterPin: String? = null
    var exporterAddress1: String? = null
    var exporterAddress2: String? = null
    var exporterCity: String? = null
    var exporterCountry: String? = null
    var exporterZipCode: String? = null
    var exporterTelephoneNumber: String? = null
    var exporterFaxNumber: String? = null
    var exporterEmail: String? = null
    var placeOfInspection: String? = null
    var applicantName: String? = null
    var applicantPin: String? = null
    var applicantAddress1: String? = null
    var applicantAddress2: String? = null
    var applicantCity: String? = null
    var applicantCountry: String? = null
    var applicantZipCode: String? = null
    var applicantTelephoneNumber: String? = null
    var applicantFaxNumber: String? = null
    var applicantEmail: String? = null
    var placeOfInspectionAddress: String? = null
    var placeOfInspectionEmail: String? = null
    var placeOfInspectionContacts: String? = null
    var portOfLoading: String? = null
    var portOfDischarge: String? = null
    var shipmentMethod: String? = null
    var countryOfSupply: String? = null
    var goodsCondition: String? = null
    var assemblyState: String? = null
    var linkToAttachedDocuments: List<String>? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var preferredInspectionDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    var receiveDate: Timestamp? = null
    var make: String? = null
    var model: String? = null
    var chassisNumber: String? = null
    var engineNumber: String? = null
    var engineCapacity: String? = null
    var yearOfManufacture: String? = null
    var yearOfFirstRegistration: String? = null
    var version: Int? = 1
    var clientId: String? = null
    var reviewStatus: String? = null
    var reviewRemarks: String? = null
    var partnerId: Long? = null

    companion object {
        fun fromEntity(rfc: RfcCorEntity): RfcCorDao {
            val rfcEntity = RfcCorDao()
            rfcEntity.rfcId = rfc.id
            rfcEntity.partnerId = rfc.partner
            rfcEntity.rfcNumber = rfc.rfcNumber
            rfcEntity.idfNumber = rfc.idfNumber
            rfcEntity.ucrNumber = rfc.ucrNumber
            rfcEntity.rfcDate = rfc.rfcDate
            rfcEntity.countryOfDestination = rfc.countryOfDestination
            rfcEntity.importerName = rfc.importerName
            rfcEntity.importerCountry = rfc.importerCountry
            rfcEntity.importerAddress1 = rfc.importerAddress1
            rfcEntity.importerAddress2 = rfc.importerAddress2
            rfcEntity.importerCity = rfc.importerCity
            rfcEntity.importerFaxNumber = rfc.importerFaxNumber
            rfcEntity.importerPin = rfc.importerPin
            rfcEntity.importerZipCode = rfc.importerZipcode
            rfcEntity.importerTelephoneNumber = rfc.importerTelephoneNumber
            rfcEntity.importerEmail = rfc.importerEmail
            rfcEntity.exporterName = rfc.exporterName
            rfcEntity.exporterPin = rfc.exporterPin
            rfcEntity.exporterCity = rfc.exporterCity
            rfcEntity.exporterAddress1 = rfc.exporterAddress1
            rfcEntity.exporterAddress2 = rfc.exporterAddress2
            rfcEntity.exporterCountry = rfc.exporterCountry
            rfcEntity.exporterEmail = rfc.exporterEmail
            rfcEntity.exporterFaxNumber = rfc.exporterFaxNumber
            rfcEntity.exporterTelephoneNumber = rfc.exporterTelephoneNumber
            rfcEntity.exporterZipCode = rfc.exporterZipcode
            rfcEntity.placeOfInspection = rfc.placeOfInspection
            rfcEntity.placeOfInspectionAddress = rfc.placeOfInspectionAddress
            rfcEntity.placeOfInspectionContacts = rfc.placeOfInspectionContacts
            rfcEntity.placeOfInspectionEmail = rfc.placeOfInspectionEmail
            rfcEntity.portOfDischarge = rfc.portOfDischarge
            rfcEntity.portOfLoading = rfc.portOfLoading
            rfcEntity.shipmentMethod = rfc.shipmentMethod
            rfcEntity.countryOfSupply = rfc.countryOfSupply
            rfcEntity.applicantName = rfc.applicantName
            rfcEntity.applicantCity = rfc.applicantCity
            rfcEntity.applicantCountry = rfc.applicantCountry
            rfcEntity.applicantAddress1 = rfc.applicantAddress1
            rfcEntity.applicantAddress2 = rfc.applicantAddress2
            rfcEntity.applicantEmail = rfc.applicantEmail
            rfcEntity.applicantFaxNumber = rfc.applicantFaxNumber
            rfcEntity.applicantZipCode = rfc.applicantZipcode
            rfcEntity.applicantTelephoneNumber = rfc.applicantTelephoneNumber
            rfcEntity.applicantPin = rfc.applicantPin
            rfcEntity.goodsCondition = rfc.goodsCondition
            rfcEntity.assemblyState = rfc.assemblyState
            rfcEntity.linkToAttachedDocuments = rfc.linkToAttachedDocuments?.split(",")
            rfcEntity.clientId = rfc.createdBy
            rfcEntity.reviewStatus = when (rfc.reviewStatus) {
                1 -> "REVIEWED"
                2 -> "REJECTED"
                else -> "NOT REVIEWED"
            }
            rfcEntity.reviewRemarks = rfc.reviewRemarks
            rfcEntity.engineNumber = rfc.engineNumber
            rfcEntity.engineCapacity = rfc.engineCapacity
            rfcEntity.make = rfc.make
            rfcEntity.model = rfc.model
            rfcEntity.chassisNumber = rfc.chassisNumber
            rfcEntity.yearOfFirstRegistration = rfc.yearOfFirstRegistration
            rfcEntity.yearOfManufacture = rfc.yearOfManufacture
            rfcEntity.receiveDate = rfc.createdOn
            return rfcEntity
        }

        fun fromList(rfcs: List<RfcCorEntity>): List<RfcCorDao> {
            val dtos = mutableListOf<RfcCorDao>()
            rfcs.forEach {
                dtos.add(RfcCorDao.fromEntity(it))
            }
            return dtos
        }
    }
}

class CorEntityDao {
    var recordId: Long? = null
    var corNumber: String? = null
    var ucrNumber: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var acceptableDocDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var finalDocDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var corIssueDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var rfcDate: Timestamp? = null
    var countryOfSupply: String? = null
    var inspectionCenter: String? = null
    var exporterName: String? = null
    var exporterAddress1: String? = null
    var exporterAddress2: String? = null
    var exporterEmail: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var applicationBookingDate: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var inspectionDate: Timestamp? = null
    var make: String? = null
    var model: String? = null
    var chassisNumber: String? = null
    var engineNumber: String? = null
    var engineCapacity: String? = null
    var yearOfManufacture: String? = null
    var yearOfFirstRegistration: String? = null
    var inspectionMileage: String? = null
    var unitsOfMileage: String? = null
    var inspectionRemarks: String? = null
    var previousRegistrationNumber: String? = null
    var previousCountryOfRegistration: String? = null
    var tareWeight: Double? = null
    var loadCapacity: Double? = null
    var grossWeight: Double? = null
    var numberOfAxles: Long? = null
    var typeOfVehicle: String? = null
    var numberOfPassengers: Long? = null
    var typeOfBody: String? = null
    var bodyColor: String? = null
    var fuelType: String? = null
    var inspectionFee: Double? = null
    var inspectionFeeReceipt: String? = null
    var transmission: String? = null
    var inspectionOfficer: String? = null
    var inspectionFeeCurrency: String? = null
    var inspectionFeeExchangeRate: Double? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var inspectionFeePaymentDate: Timestamp? = null
    var route: String? = null
    var partnerId: Long? = null
    var reviewStatus: Int? = null
    var reviewStatusDesc: String? = null
    var clientId: String? = null
    var documentsType: String? = null
    var cdReference: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    var createdOn: Timestamp? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    var timeCreated: Timestamp? = null
    var version: Long? = null

    companion object {
        fun fromEntity(rfc: CorsBakEntity): CorEntityDao {
            val rfcEntity = CorEntityDao()
            rfcEntity.recordId = rfc.id
            rfcEntity.cdReference = rfc.consignmentDocId?.uuid
            rfcEntity.partnerId = rfc.partner
            rfcEntity.corNumber = rfc.corNumber ?: rfc.consignmentDocId?.ucrNumber
            rfcEntity.acceptableDocDate = rfc.acceptableDocDate
            rfcEntity.ucrNumber = rfc.ucrNumber
            rfcEntity.finalDocDate = rfc.finalDocDate
            rfcEntity.corIssueDate = rfc.corIssueDate
            rfcEntity.countryOfSupply = rfc.countryOfSupply
            rfcEntity.inspectionCenter = rfc.inspectionCenter
            rfcEntity.exporterName = rfc.exporterName
            rfcEntity.exporterAddress1 = rfc.exporterAddress1
            rfcEntity.exporterAddress2 = rfc.exporterAddress2
            rfcEntity.exporterEmail = rfc.exporterEmail
            rfcEntity.inspectionFeeExchangeRate = rfc.inspectionFeeExchangeRate
            rfcEntity.applicationBookingDate = rfc.applicationBookingDate
            rfcEntity.inspectionDate = rfc.inspectionDate
            rfcEntity.inspectionFee = rfc.inspectionFee
            rfcEntity.inspectionFeeCurrency = rfc.inspectionFeeCurrency
            rfcEntity.inspectionFeeReceipt = rfc.inspectionFeeReceipt
            rfcEntity.inspectionFeePaymentDate = rfc.inspectionFeePaymentDate
            rfcEntity.inspectionRemarks = rfc.inspectionRemarks
            rfcEntity.inspectionOfficer = rfc.inspectionOfficer
            rfcEntity.inspectionMileage = rfc.inspectionMileage
            rfcEntity.clientId = rfc.createdBy
            rfcEntity.reviewStatusDesc = when (rfc.reviewStatus) {
                1 -> "REVIEWED"
                2 -> "REJECTED"
                else -> "NOT REVIEWED"
            }
            rfcEntity.reviewStatus = rfc.reviewStatus
            rfcEntity.numberOfPassengers = rfc.numberOfPassangers
            rfcEntity.numberOfAxles = rfc.numberOfAxles
            rfcEntity.bodyColor = rfc.bodyColor
            rfcEntity.typeOfVehicle = rfc.typeOfVehicle
            rfcEntity.unitsOfMileage = rfc.unitsOfMileage
            rfcEntity.inspectionMileage = rfc.inspectionMileage
            rfcEntity.engineNumber = rfc.engineNumber
            rfcEntity.engineCapacity = rfc.engineCapacity
            rfcEntity.make = rfc.make
            rfcEntity.model = rfc.model
            rfcEntity.fuelType = rfc.fuelType
            rfcEntity.loadCapacity = rfc.loadCapacity
            rfcEntity.tareWeight = rfc.tareWeight
            rfcEntity.chassisNumber = rfc.chasisNumber
            rfcEntity.previousCountryOfRegistration = rfc.previousCountryOfRegistration
            rfcEntity.previousRegistrationNumber = rfc.previousRegistrationNumber
            rfcEntity.yearOfFirstRegistration = rfc.yearOfFirstRegistration
            rfcEntity.yearOfManufacture = rfc.yearOfManufacture
            rfcEntity.route = rfc.route
            rfcEntity.createdOn = rfc.createdOn
            rfcEntity.timeCreated = rfc.createdOn
            rfcEntity.documentsType = rfc.documentsType
            rfcEntity.version = rfc.version
            return rfcEntity
        }

        fun fromList(rfcs: List<CorsBakEntity>): List<CorEntityDao> {
            val dtos = mutableListOf<CorEntityDao>()
            rfcs.forEach {
                dtos.add(fromEntity(it))
            }
            return dtos
        }
    }
}

class QueryKebsResponseDao {
    var serialNumber: String? = null
    var queryResponse: String? = null
    var responseFrom: String? = null
    var queryAnalysis: String? = null
    var responseType: String? = null
    var linkToUploads: List<String>? = null
    var createdOn: Timestamp? = null

    companion object {
        fun toEntity(response: PvocQueryResponseEntity): QueryKebsResponseDao {
            val res = QueryKebsResponseDao()
            res.createdOn = response.createdOn
            res.responseType = "RESPONSE"
            res.serialNumber = response.serialNumber
            res.queryAnalysis = response.varField1
            res.queryResponse = response.response
            res.responseFrom = response.responseFrom
            res.linkToUploads = response.linkToUploads?.split(",") ?: emptyList()
            return res
        }

        fun fromList(responses: Iterable<PvocQueryResponseEntity>): List<QueryKebsResponseDao> {
            val responseList = mutableListOf<QueryKebsResponseDao>()
            responses.forEach { rs ->
                responseList.add(toEntity(rs))
            }
            return responseList
        }
    }
}

class PvocKebsQueryDao {
    var queryId: Long? = null
    var partnerId: Long? = null
    var serialNumber: String? = null
    var documentType: String? = null
    var certNumber: String? = null
    var rfcNumber: String? = null
    var invoiceNumber: String? = null
    var ucrNumber: String? = null
    var kebsQuery: String? = null
    var conclusion: String? = null
    var queryAnalysis: String? = null
    var clossed: Boolean = false
    var version: Long? = 1
    var conclusionDate: Timestamp? = null
    var createdBy: String? = null
    var createdOn: Timestamp? = null
    var responses: List<QueryKebsResponseDao>? = null

    companion object {
        fun toEntity(dt: PvocQueriesEntity): PvocKebsQueryDao {
            val query = PvocKebsQueryDao()
            query.queryId = dt.id
            query.serialNumber = dt.serialNumber
            query.documentType = dt.certType
            query.rfcNumber = dt.rfcNumber
            query.invoiceNumber = dt.invoiceNumber
            query.kebsQuery = dt.queryDetails
            query.ucrNumber = dt.ucrNumber
            query.certNumber = dt.certNumber
            query.conclusionDate = dt.conclusionDate
            query.conclusion = dt.conclusion
            query.clossed = dt.conclusionStatus?.equals(1) ?: false
            query.queryAnalysis = dt.responseAnalysis
            query.version = dt.version
            query.responses = dt.responses?.let { QueryKebsResponseDao.fromList(it) }
            query.partnerId = dt.partnerId
            query.createdBy = dt.createdBy
            query.createdOn = dt.createdOn
            return query
        }

        fun fromList(dts: List<PvocQueriesEntity>): List<PvocKebsQueryDao> {
            val items = mutableListOf<PvocKebsQueryDao>()
            dts.forEach { items.add(toEntity(it)) }
            return items
        }
    }
}

