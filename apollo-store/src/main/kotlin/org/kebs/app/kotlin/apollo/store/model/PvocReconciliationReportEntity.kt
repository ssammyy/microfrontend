package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_RECONCILIATION_REPORT")
class PvocReconciliationReportEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_PVOC_RECONCILIATION_REPORT_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_RECONCILIATION_REPORT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_RECONCILIATION_REPORT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "ROUTE_TYPE")
    @Basic
    var routeType: String? = null

    @Column(name = "CERTIFICATE_NO")
    @Basic
    var certificateNo: String? = null

    @Column(name = "ROUTE")
    @Basic
    var route: String? = null

    @Column(name = "KEC_NO")
    @Basic
    var kecNo: String? = null

    @Column(name = "CERTIFICATION_FEE")
    @Basic
    var certificationFee: String? = null

    @Column(name = "IDF_NO")
    @Basic
    var idfNo: String? = null

    @Column(name = "FOB")
    @Basic
    var fob: String? = null

    @Column(name = "FOB_VALUE")
    @Basic
    var fobValue: Double? = null

    @Column(name = "CHARGE_FOR_VERICATION")
    @Basic
    var chargeForVerication: Long? = null

    @Column(name = "IMPOTER_NAME")
    @Basic
    var impoterName: String? = null

    @Column(name = "EXMPOTER_NAME")
    @Basic
    var exmpoterName: String? = null

    @Column(name = "INSPECTION_FEE")
    @Basic
    var inspectionFee: Long? = null

    @Column(name = "VERIFICATION_FEE")
    @Basic
    var verificationFee: Long? = null

    @Column(name = "DIFF")
    @Basic
    var diff: Long? = null

    @Column(name = "TEST")
    @Basic
    var test: String? = null

    @Column(name = "ROYALTIES_TO_KEBS")
    @Basic
    var royaltiesToKebs: Long? = null

    @Column(name = "COUNTRY_OF_SUPPLY")
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "INSUANCE_DATE")
    @Basic
    var insuanceDate: String? = null

    @Column(name = "SEAL_NO")
    @Basic
    var sealNo: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

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

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "REVIEW_STATUS")
    @Basic
    var reviewStatus: String? = null

    @Column(name = "REVIEWED")
    @Basic
    var reviewed: Int? = null

    @Column(name = "INVOICED")
    @Basic
    var invoiced: Int? = null

    @Column(name = "PENALTY_INVOICED")
    @Basic
    var penaltyInvoiced: Int? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocReconciliationReportEntity
        return id == that.id &&
                status == that.status &&
                routeType == that.routeType &&
                certificateNo == that.certificateNo &&
                route == that.route &&
                kecNo == that.kecNo &&
                certificationFee == that.certificationFee &&
                idfNo == that.idfNo &&
                fob == that.fob &&
                fobValue == that.fobValue &&
                chargeForVerication == that.chargeForVerication &&
                impoterName == that.impoterName &&
                exmpoterName == that.exmpoterName &&
                inspectionFee == that.inspectionFee &&
                verificationFee == that.verificationFee &&
                diff == that.diff &&
                test == that.test &&
                royaltiesToKebs == that.royaltiesToKebs &&
                countryOfSupply == that.countryOfSupply &&
                insuanceDate == that.insuanceDate &&
                sealNo == that.sealNo &&
                remarks == that.remarks &&
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
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                reviewed == that.reviewed &&
                penaltyInvoiced == that.penaltyInvoiced &&
                invoiced == that.invoiced &&
                reviewStatus == that.reviewStatus
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, routeType, certificateNo, route, kecNo, certificationFee, penaltyInvoiced, invoiced, idfNo, fob, fobValue, chargeForVerication, impoterName, exmpoterName, inspectionFee, verificationFee, diff, test, royaltiesToKebs, countryOfSupply, insuanceDate, sealNo, remarks, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, reviewStatus, reviewed)
    }

    override fun toString(): String {
        return "PvocReconciliationReportEntity(id=$id, status=$status, routeType=$routeType, certificateNo=$certificateNo, route=$route, kecNo=$kecNo, certificationFee=$certificationFee, idfNo=$idfNo, fob=$fob, fobValue=$fobValue, chargeForVerication=$chargeForVerication, impoterName=$impoterName, exmpoterName=$exmpoterName, inspectionFee=$inspectionFee, verificationFee=$verificationFee, diff=$diff, test=$test, royaltiesToKebs=$royaltiesToKebs, countryOfSupply=$countryOfSupply, insuanceDate=$insuanceDate, sealNo=$sealNo, remarks=$remarks, varField1=$varField1, varField2=$varField2, varField3=$varField3, varField4=$varField4, varField5=$varField5, varField6=$varField6, varField7=$varField7, varField8=$varField8, varField9=$varField9, varField10=$varField10, createdBy=$createdBy, createdOn=$createdOn, modifiedBy=$modifiedBy, modifiedOn=$modifiedOn, deleteBy=$deleteBy, deletedOn=$deletedOn, reviewStatus=$reviewStatus, reviewed=$reviewed)"
    }

}