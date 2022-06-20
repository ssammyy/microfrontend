package org.kebs.app.kotlin.apollo.api.payload.response

import com.fasterxml.jackson.annotation.JsonFormat
import org.kebs.app.kotlin.apollo.store.model.pvc.*
import java.io.Serializable
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
    var pvocAgent: String? = null
    var pvocAgentId: Long? = null
    var categoryId: Long? = null
    var categoryName: String? = null
    var subCategoryId: Long? = null
    var subCategoryName: String? = null
    var recommendationId: Long? = null
    var recommendationAction: String? = null
    var recommendationDesc: String? = null
    var complaintDate: Timestamp? = null
    var officerName: String? = null
    var hodName: String? = null
    var mpvocName: String? = null

    companion object {
        fun fromEntity(complaint: PvocComplaintEntity): PvocComplaintDao {
            return PvocComplaintDao().apply {
                complaintId = complaint.id
                refNo = complaint.refNo
                rfcNo = complaint.rfcNo
                complaintName = complaint.complaintName
                phoneNo = complaint.phoneNo
                address = complaint.address
                cocNo = complaint.cocNo
                email = complaint.email
                complaintDate = complaint.createdOn
                officerName = "${complaint.pvocUser?.firstName} ${complaint.pvocUser?.lastName}"
                hodName = "${complaint.hod?.firstName} ${complaint.hod?.lastName}"
                mpvocName = "${complaint.mpvoc?.firstName} ${complaint.mpvoc?.lastName}"
                complaintDescription = complaint.generalDescription
                pvocAgent = complaint.pvocAgent?.partnerName
                pvocAgentId = complaint.pvocAgent?.id
                categoryId = complaint.compliantNature?.id
                categoryName = complaint.compliantNature?.name
                subCategoryId = complaint.compliantSubCategory?.id
                subCategoryName = complaint.compliantSubCategory?.name
                recommendationDesc = complaint.recomendation
            }
        }

        fun fromList(complaints: List<PvocComplaintEntity>): List<PvocComplaintDao> {
            val daos = mutableListOf<PvocComplaintDao>()
            complaints.forEach { daos.add(fromEntity(it)) }
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
    var waiverId: Long? = null
    var fileType: String? = null


    companion object {
        fun fromEntity(documentsEntity: PvocWaiversApplicationDocumentsEntity): PvocWaiverAttachmentDao {
            val dao = PvocWaiverAttachmentDao().apply {
                fileId = documentsEntity.id
                name = documentsEntity.name
                waiverId = documentsEntity.waiverId
                fileType = documentsEntity.fileType
                status = documentsEntity.status
                description = documentsEntity.description
            }
            return dao
        }

        fun fromList(documents: List<PvocWaiversApplicationDocumentsEntity>): List<PvocWaiverAttachmentDao> {
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
        fun fromEntity(waiver: PvocWaiversApplicationEntity): PvocWaiverDao {
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
                nscApprovalStatus = waiver.nscApprovalStatus
                csApprovalStatus = waiver.csApprovalStatus
                serialNo = waiver.serialNo
                csResponceCode = waiver.csResponceCode
                csResponceMessage = waiver.csResponceMessage
                submittedOn = waiver.createdOn
            }
            return dao
        }

        fun fromList(waivers: List<PvocWaiversApplicationEntity>): List<PvocWaiverDao> {
            val daos = mutableListOf<PvocWaiverDao>()
            waivers.forEach { daos.add(fromEntity(it)) }
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