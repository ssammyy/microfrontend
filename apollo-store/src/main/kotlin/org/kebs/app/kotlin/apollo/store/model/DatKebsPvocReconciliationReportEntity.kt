//package org.kebs.app.kotlin.apollo.store.model
//
//import java.io.Serializable
//import java.sql.Time
//import java.sql.Timestamp
//import java.util.*
//import javax.persistence.*
//
//@Entity
//@Table(name = "DAT_KEBS_PVOC_RECONCILIATION_REPORT")
//class DatKebsPvocReconciliationReportEntity : Serializable {
//    @Column(name = "ID")
//    @SequenceGenerator(name = "DAT_KEBS_PVOC_RECONCILIATION_REPORT_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_RECONCILIATION_REPORT_SEQ", allocationSize = 1)
//    @GeneratedValue(generator = "DAT_KEBS_PVOC_RECONCILIATION_REPORT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
//    @Id
//    var id: Long = 0
//
//    @get:Column(name = "STATUS")
//    @get:Basic
//    var status: Int? = null
//
//    @get:Column(name = "ROUTE_TYPE")
//    @get:Basic
//    var routeType: String? = null
//
//    @get:Column(name = "CERTIFICATE_NO")
//    @get:Basic
//    var certificateNo: String? = null
//
//    @get:Column(name = "ROUTE")
//    @get:Basic
//    var route: String? = null
//
//    @get:Column(name = "KEC_NO")
//    @get:Basic
//    var kecNo: String? = null
//
//    @get:Column(name = "CERTIFICATION_FEE")
//    @get:Basic
//    var certificationFee: Long? = null
//
//    @get:Column(name = "IDF_NO")
//    @get:Basic
//    var idfNo: String? = null
//
//    @get:Column(name = "FOB")
//    @get:Basic
//    var fob: Time? = null
//
//    @get:Column(name = "FOB_VALUE")
//    @get:Basic
//    var fobValue: Long? = null
//
//    @get:Column(name = "CHARGE_FOR_VERICATION")
//    @get:Basic
//    var chargeForVerication: Long? = null
//
//    @get:Column(name = "IMPOTER_NAME")
//    @get:Basic
//    var impoterName: String? = null
//
//    @get:Column(name = "EXMPOTER_NAME")
//    @get:Basic
//    var exmpoterName: String? = null
//
//    @get:Column(name = "INSPECTION_FEE")
//    @get:Basic
//    var inspectionFee: Long? = null
//
//    @get:Column(name = "VERIFICATION_FEE")
//    @get:Basic
//    var verificationFee: Long? = null
//
//    @get:Column(name = "DIFF")
//    @get:Basic
//    var diff: Long? = null
//
//    @get:Column(name = "TEST")
//    @get:Basic
//    var test: String? = null
//
//    @get:Column(name = "ROYALTIES_TO_KEBS")
//    @get:Basic
//    var royaltiesToKebs: Long? = null
//
//    @get:Column(name = "COUNTRY_OF_SUPPLY")
//    @get:Basic
//    var countryOfSupply: String? = null
//
//    @get:Column(name = "INSUANCE_DATE")
//    @get:Basic
//    var insuanceDate: String? = null
//
//    @get:Column(name = "SEAL_NO")
//    @get:Basic
//    var sealNo: String? = null
//
//    @get:Column(name = "REMARKS")
//    @get:Basic
//    var remarks: String? = null
//
//    @get:Column(name = "VAR_FIELD_1")
//    @get:Basic
//    var varField1: String? = null
//
//    @get:Column(name = "VAR_FIELD_2")
//    @get:Basic
//    var varField2: String? = null
//
//    @get:Column(name = "VAR_FIELD_3")
//    @get:Basic
//    var varField3: String? = null
//
//    @get:Column(name = "VAR_FIELD_4")
//    @get:Basic
//    var varField4: String? = null
//
//    @get:Column(name = "VAR_FIELD_5")
//    @get:Basic
//    var varField5: String? = null
//
//    @get:Column(name = "VAR_FIELD_6")
//    @get:Basic
//    var varField6: String? = null
//
//    @get:Column(name = "VAR_FIELD_7")
//    @get:Basic
//    var varField7: String? = null
//
//    @get:Column(name = "VAR_FIELD_8")
//    @get:Basic
//    var varField8: String? = null
//
//    @get:Column(name = "VAR_FIELD_9")
//    @get:Basic
//    var varField9: String? = null
//
//    @get:Column(name = "VAR_FIELD_10")
//    @get:Basic
//    var varField10: String? = null
//
//    @get:Column(name = "CREATED_BY")
//    @get:Basic
//    var createdBy: String? = null
//
//    @get:Column(name = "CREATED_ON")
//    @get:Basic
//    var createdOn: Timestamp? = null
//
//    @get:Column(name = "MODIFIED_BY")
//    @get:Basic
//    var modifiedBy: String? = null
//
//    @get:Column(name = "MODIFIED_ON")
//    @get:Basic
//    var modifiedOn: Timestamp? = null
//
//    @get:Column(name = "DELETE_BY")
//    @get:Basic
//    var deleteBy: String? = null
//
//    @get:Column(name = "DELETED_ON")
//    @get:Basic
//    var deletedOn: Timestamp? = null
//
//    @get:Column(name = "REVIEW_STATUS")
//    @get:Basic
//    var reviewStatus: String? = null
//
//    @get:Column(name = "REVIEWED")
//    @get:Basic
//    var reviewed: Long? = null
//
//    override fun equals(o: Any?): Boolean {
//        if (this === o) return true
//        if (o == null || javaClass != o.javaClass) return false
//        val that = o as DatKebsPvocReconciliationReportEntity
//        return id == that.id &&
//                status == that.status &&
//                routeType == that.routeType &&
//                certificateNo == that.certificateNo &&
//                route == that.route &&
//                kecNo == that.kecNo &&
//                certificationFee == that.certificationFee &&
//                idfNo == that.idfNo &&
//                fob == that.fob &&
//                fobValue == that.fobValue &&
//                chargeForVerication == that.chargeForVerication &&
//                impoterName == that.impoterName &&
//                exmpoterName == that.exmpoterName &&
//                inspectionFee == that.inspectionFee &&
//                verificationFee == that.verificationFee &&
//                diff == that.diff &&
//                test == that.test &&
//                royaltiesToKebs == that.royaltiesToKebs &&
//                countryOfSupply == that.countryOfSupply &&
//                insuanceDate == that.insuanceDate &&
//                sealNo == that.sealNo &&
//                remarks == that.remarks &&
//                varField1 == that.varField1 &&
//                varField2 == that.varField2 &&
//                varField3 == that.varField3 &&
//                varField4 == that.varField4 &&
//                varField5 == that.varField5 &&
//                varField6 == that.varField6 &&
//                varField7 == that.varField7 &&
//                varField8 == that.varField8 &&
//                varField9 == that.varField9 &&
//                varField10 == that.varField10 &&
//                createdBy == that.createdBy &&
//                createdOn == that.createdOn &&
//                modifiedBy == that.modifiedBy &&
//                modifiedOn == that.modifiedOn &&
//                deleteBy == that.deleteBy &&
//                deletedOn == that.deletedOn &&
//                reviewStatus == that.reviewStatus &&
//                reviewed == that.reviewed
//    }
//
//    override fun hashCode(): Int {
//        return Objects.hash(id, status, routeType, certificateNo, route, kecNo, certificationFee, idfNo, fob, fobValue, chargeForVerication, impoterName, exmpoterName, inspectionFee, verificationFee, diff, test, royaltiesToKebs, countryOfSupply, insuanceDate, sealNo, remarks, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, reviewStatus, reviewed)
//    }
//}