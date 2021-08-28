package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_ITEM_DETAILS")
class CdItemDetailsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_CD_ITEM_DETAILS_SEQ_GEN",
        sequenceName = "DAT_KEBS_CD_ITEM_DETAILS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_CD_ITEM_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Transient
    var confirmFeeIdSelected: Long? = null

    @Basic
    @Column(name = "UUID")
    var uuid: String? = null

    @Column(name = "ITEM_DESCRIPTION")
    @Basic
    var itemDescription: String? = null

    @Column(name = "ITEM_HS_CODE")
    @Basic
    var itemHsCode: String? = null

    @Column(name = "OWNER_PIN")
    @Basic
    var ownerPin: String? = null

    @Column(name = "OWNER_NAME")
    @Basic
    var ownerName: String? = null


    @Column(name = "INTERNAL_FILE_NUMBER")
    @Basic
    var internalFileNumber: String? = null

    @Column(name = "INTERNAL_PRODUCT_NO")
    @Basic
    var internalProductNo: String? = null

    @Column(name = "PRODUCT_TECHNICAL_NAME")
    @Basic
    var productTechnicalName: String? = null

    @Column(name = "PRODUCT_BRAND_NAME")
    @Basic
    var productBrandName: String? = null

    @Column(name = "PRODUCT_ACTIVE_INGREDIENTS")
    @Basic
    var productActiveIngredients: String? = null

    @Column(name = "PRODUCT_PACKAGING_DETAILS")
    @Basic
    var productPackagingDetails: String? = null

    @Column(name = "PRODUCT_CLASS_CODE")
    @Basic
    var productClassCode: String? = null

    @Column(name = "PRODUCT_CLASS_DESCRIPTION")
    @Basic
    var productClassDescription: String? = null

    @Column(name = "DNOTE_STATUS")
    @Basic
    var dnoteStatus: Int? = null

    @Column(name = "INSPECTION_REPORT_STATUS")
    @Basic
    var inspectionReportStatus: Int? = null

    @Column(name = "ITEM_NO")
    @Basic
    var itemNo: Long? = null

    @Column(name = "SAMPLE_BS_NUMBER_STATUS")
    @Basic
    var sampleBsNumberStatus: Int? = null

    @Column(name = "SAMPLE_SUBMISSION_STATUS")
    @Basic
    var sampleSubmissionStatus: Int? = null

    @Column(name = "MINISTRY_SUBMISSION_STATUS")
    @Basic
    var ministrySubmissionStatus: Int? = null

    @JoinColumn(name = "MINISTRY_STATION_ID", referencedColumnName = "ID")
    @ManyToOne
    var ministryStationId: MinistryStationEntity? = null

    @Column(name = "CHECKLIST_STATUS")
    @Basic
    var checklistStatus: Int? = null

    @Column(name = "QUANTITY")
    @Basic
    var quantity: BigDecimal? = null

    @Column(name = "PACKAGE_QUANTITY")
    @Basic
    var packageQuantity: BigDecimal? = null

    @Column(name = "UNIT_PRICE_NCY")
    @Basic
    var unitPriceNcy: BigDecimal? = null

    @Column(name = "ITEM_GROSS_WEIGHT")
    @Basic
    var itemGrossWeight: String? = null

    @Column(name = "HS_DESCRIPTION")
    @Basic
    var hsDescription: String? = null

    @Column(name = "UNIT_OF_QUANTITY")
    @Basic
    var unitOfQuantity: String? = null

    @Column(name = "UNIT_OF_QUANTITY_DESC")
    @Basic
    var unitOfQuantityDesc: String? = null

    @Column(name = "FOREIGN_CURRENCY_CODE")
    @Basic
    var foreignCurrencyCode: String? = null

    @Column(name = "TOTAL_PRICE_NCY")
    @Basic
    var totalPriceNcy: BigDecimal? = null

    @Column(name = "APPLICANT_REMARKS")
    @Basic
    var applicantRemarks: String? = null

    @Column(name = "SUPPLIMENTARY_QUANTITY")
    @Basic
    var supplimentaryQuantity: String? = null

    @Column(name = "UNIT_PRICE_FCY")
    @Basic
    var unitPriceFcy: BigDecimal? = null

    @Column(name = "COUNTRY_OF_ORGIN")
    @Basic
    var countryOfOrgin: String? = null

    @Column(name = "COUNTRY_OF_ORGIN_DESC")
    @Basic
    var countryOfOrginDesc: String? = null

    @Column(name = "PACKAGE_TYPE")
    @Basic
    var packageType: String? = null

    @Column(name = "PACKAGE_TYPE_DESC")
    @Basic
    var packageTypeDesc: String? = null

    @Column(name = "MARKS_AND_CONTAINERS")
    @Basic
    var marksAndContainers: String? = null

    @Column(name = "TOTAL_PRICE_FCY")
    @Basic
    var totalPriceFcy: BigDecimal? = null

    @Column(name = "ITEM_NET_WEIGHT")
    @Basic
    var itemNetWeight: String? = null

    @Column(name = "DATE_SUBMITTED")
    @Basic
    var dateSubmitted: Date? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "LAST_MODIFIED_BY")
    @Basic
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    @Basic
    var lastModifiedOn: Timestamp? = null

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @Column(name = "CONSIGMENT_ID")
    @Basic
    var consigmentId: Long? = null

    @Column(name = "ITEM_STATUS")
    @Basic
    var itemStatus: String? = null

    @Column(name = "APPROVE_STATUS")
    @Basic
    var approveStatus: Int? = null

    @Column(name = "APPROVE_DATE")
    @Basic
    var approveDate: Date? = null

    @Column(name = "REJECT_STATUS")
    @Basic
    var rejectStatus: Int? = null

    @Column(name = "REJECT_DATE")
    @Basic
    var rejectDate: Date? = null

    @Column(name = "REJECT_REASON")
    @Basic
    var rejectReason: String? = null

    @Column(name = "APPROVE_REASON")
    @Basic
    var approveReason: String? = null

//    @Column(name = "TARGET_REASON")
//    @Basic
//    var targetReason: String? = null

//    @Column(name = "TARGET_DATE")
//    @Basic
//    var targetDate: Date? = null

//    @Column(name = "TARGET_STATUS")
//    @Basic
//    var targetStatus: Int? = null

//    @Column(name = "TARGET_APPROVED_REMARKS")
//    @Basic
//    var targetApproveRemarks: String? = null

//    @Column(name = "TARGET_APPROVED_DATE")
//    @Basic
//    var targetApproveDate: Date? = null

    @Column(name = "PAYMENT_MADE_STATUS")
    @Basic
    var paymentMadeStatus: Int? = null

    @Column(name = "PAYMENT_NEEDED_STATUS")
    @Basic
    var paymentNeededStatus: Int? = null

    @Column(name = "PAYMENT_NEEDED_REMARKS")
    @Basic
    var paymentNeededRemarks: String? = null

    @Column(name = "PAYMENT_NEEDED_DATE")
    @Basic
    var paymentNeededDate: Date? = null

//    @Column(name = "INSPECTION_DATE_SET_STATUS")
//    @Basic
//    var inspectionDateSetStatus: Int? = null
//
//    @Column(name = "INSPECTION_NOTIFICATION_STATUS")
//    @Basic
//    var inspectionNotificationStatus: Int? = null
//
//    @Column(name = "INSPECTION_REMARKS")
//    @Basic
//    var inspectionRemarks: String? = null
//
//    @Column(name = "INSPECTION_DATE")
//    @Basic
//    var inspectionDate: Date? = null
//
//    @Column(name = "INSPECTION_NOTIFICATION_DATE")
//    @Basic
//    var inspectionNotificationDate: Date? = null

//    @Column(name = "TARGET_APPROVED_STATUS")
//    @Basic
//    var targetApproveStatus: Int? = null

    @Column(name = "CHASIS_NUMBER")
    @Basic
    var chasisNumber: String? = null

    @Column(name = "SAMPLED_STATUS")
    @Basic
    var sampledStatus: Int? = null

    @Column(name = "SAMPLE_COLLECTED_STATUS")
    @Basic
    var sampledCollectedStatus: Int? = null

    @Column(name = "ALL_TEST_REPORT_STATUS")
    @Basic
    var allTestReportStatus: Int? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "SUPPLEMENTARY_QTY")
    @Basic
    var supplementaryQty: String? = null

    @Column(name = "SUPPLEMENTARY_UNIT_OF_QTY")
    @Basic
    var supplementaryUnitOfQty: String? = null

    @Column(name = "SUPPLEMENTARY_UNIT_OF_QTY_DESC")
    @Basic
    var supplementaryUnitOfQtyDesc: String? = null

    @Column(name = "INSPECTION_PROCESS_STATUS")
    @Basic
    var inspectionProcessStatus: Int? = null

    @Column(name = "INSPECTION_PROCESS_INSTANCE_ID")
    @Basic
    var inspectionProcessInstanceId: String? = null

    @Column(name = "INSPECTION_PROCESS_STARTED_ON")
    @Basic
    var inspectionProcessStartedOn: Timestamp? = null

    @Column(name = "INSPECTION_PROCESS_COMPLETED_ON")
    @Basic
    var inspectionProcessCompletedOn: Timestamp? = null

    @JoinColumn(name = "CD_DOC_ID", referencedColumnName = "ID")
    @ManyToOne
    var cdDocId: ConsignmentDocumentDetailsEntity? = null

    @JoinColumn(name = "PAYMENT_FEE_ID_SELECTED", referencedColumnName = "ID")
    @ManyToOne
    var paymentFeeIdSelected: DestinationInspectionFeeEntity? = null

    @JoinColumn(name = "CHECKLIST_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    var checkListTypeId: CdChecklistTypesEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdItemDetailsEntity
        return id == that.id &&
                inspectionReportStatus == that.inspectionReportStatus &&
                uuid == that.uuid &&
                countryOfOrginDesc == that.countryOfOrginDesc &&
                packageTypeDesc == that.packageTypeDesc &&
                supplementaryQty == that.supplementaryQty &&
                supplementaryUnitOfQty == that.supplementaryUnitOfQty &&
                supplementaryUnitOfQtyDesc == that.supplementaryUnitOfQtyDesc &&
                internalFileNumber == that.internalFileNumber &&
                internalProductNo == that.internalProductNo &&
                productTechnicalName == that.productTechnicalName &&
                productBrandName == that.productBrandName &&
                productActiveIngredients == that.productActiveIngredients &&
                productPackagingDetails == that.productPackagingDetails &&
                unitOfQuantityDesc == that.unitOfQuantityDesc &&
                sampleSubmissionStatus == that.sampleSubmissionStatus &&
                sampleBsNumberStatus == that.sampleBsNumberStatus &&
                dnoteStatus == that.dnoteStatus &&
                itemNo == that.itemNo &&
                allTestReportStatus == that.allTestReportStatus &&
                checklistStatus == that.checklistStatus &&
                itemDescription == that.itemDescription &&
                confirmFeeIdSelected == that.confirmFeeIdSelected &&
                itemHsCode == that.itemHsCode &&
                productClassCode == that.productClassCode &&
                productClassDescription == that.productClassDescription &&
                quantity == that.quantity &&
                packageQuantity == that.packageQuantity &&
                unitPriceNcy == that.unitPriceNcy &&
                marksAndContainers == that.marksAndContainers &&
                itemGrossWeight == that.itemGrossWeight &&
                hsDescription == that.hsDescription &&
                unitOfQuantity == that.unitOfQuantity &&
                foreignCurrencyCode == that.foreignCurrencyCode &&
                totalPriceNcy == that.totalPriceNcy &&
                applicantRemarks == that.applicantRemarks &&
                supplimentaryQuantity == that.supplimentaryQuantity &&
                unitPriceFcy == that.unitPriceFcy &&
                countryOfOrgin == that.countryOfOrgin &&
                packageType == that.packageType &&
                totalPriceFcy == that.totalPriceFcy &&
                itemNetWeight == that.itemNetWeight &&
                dateSubmitted == that.dateSubmitted &&
                ownerPin == that.ownerPin &&
                ownerName == that.ownerName &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version &&
                consigmentId == that.consigmentId &&
                itemStatus == that.itemStatus &&
                approveStatus == that.approveStatus &&
                approveDate == that.approveDate &&
                rejectStatus == that.rejectStatus &&
                rejectDate == that.rejectDate &&
                rejectReason == that.rejectReason &&
                approveReason == that.approveReason &&
//                targetReason == that.targetReason &&
//                targetDate == that.targetDate &&
//                targetStatus == that.targetStatus &&
//                targetApproveRemarks == that.targetApproveRemarks &&
//                targetApproveDate == that.targetApproveDate &&
//                targetApproveStatus == that.targetApproveStatus &&
                chasisNumber == that.chasisNumber &&
                sampledStatus == that.sampledStatus &&
                sampledCollectedStatus == that.sampledCollectedStatus &&
//                inspectionDate == that.inspectionDate &&
//                inspectionNotificationDate == that.inspectionNotificationDate &&
//                inspectionDateSetStatus == that.inspectionDateSetStatus &&
//                inspectionRemarks == that.inspectionRemarks &&
//                inspectionNotificationStatus == that.inspectionNotificationStatus &&
                paymentMadeStatus == that.paymentMadeStatus &&
                paymentNeededDate == that.paymentNeededDate &&
                paymentNeededRemarks == that.paymentNeededRemarks &&
                paymentNeededStatus == that.paymentNeededStatus &&
                status == that.status &&
                inspectionProcessStatus == that.inspectionProcessStatus &&
                ministrySubmissionStatus == that.ministrySubmissionStatus &&
                inspectionProcessInstanceId == that.inspectionProcessInstanceId &&
                inspectionProcessStartedOn == that.inspectionProcessStartedOn &&
                inspectionProcessCompletedOn == that.inspectionProcessCompletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            uuid,
            internalFileNumber,
            packageTypeDesc,
            countryOfOrginDesc,
            internalProductNo,
            productTechnicalName,
            ownerPin,
            ownerName,
            productBrandName,
            productActiveIngredients,
            supplementaryQty,
            supplementaryUnitOfQty,
            supplementaryUnitOfQtyDesc,
            productPackagingDetails,
            unitOfQuantityDesc,
            inspectionReportStatus,
            productClassDescription,
            productClassCode,
            dnoteStatus,
            itemStatus,
//            targetApproveDate,
            sampleBsNumberStatus,
            confirmFeeIdSelected,
            paymentNeededRemarks,
            paymentNeededStatus,
            paymentNeededDate,
            sampledCollectedStatus,
//            inspectionNotificationStatus,
//            inspectionNotificationDate,
//            inspectionDate,
//            inspectionDateSetStatus,
//            inspectionRemarks,
//            targetApproveStatus,
//            targetApproveRemarks,
            marksAndContainers,
            itemNo,
            allTestReportStatus,
            sampleSubmissionStatus,
            checklistStatus,
            itemDescription,
            itemHsCode,
            quantity,
            packageQuantity,
            unitPriceNcy,
            itemGrossWeight,
            hsDescription,
            unitOfQuantity,
            foreignCurrencyCode,
            totalPriceNcy,
            applicantRemarks,
            supplimentaryQuantity,
            unitPriceFcy,
            countryOfOrgin,
            packageType,
            totalPriceFcy,
            itemNetWeight,
            dateSubmitted,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            createdBy,
            createdOn,
            lastModifiedBy,
            lastModifiedOn,
            updateBy,
            updatedOn,
            deleteBy,
            deletedOn,
            version,
            consigmentId,
            approveStatus,
            approveDate,
            rejectStatus,
            rejectDate,
            rejectReason,
            approveReason,
//            targetReason,
//            targetDate,
//            targetStatus,
            chasisNumber,
            paymentMadeStatus,
            sampledStatus,
            status,
            inspectionProcessStatus,
            ministrySubmissionStatus,
            inspectionProcessInstanceId,
            inspectionProcessStartedOn,
            inspectionProcessCompletedOn
        )
    }
}
