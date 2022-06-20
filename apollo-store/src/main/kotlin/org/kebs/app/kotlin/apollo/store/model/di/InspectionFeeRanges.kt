package org.kebs.app.kotlin.apollo.store.model.di

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_DESTINATION_FEE_RANGES")
class InspectionFeeRanges : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "CFG_KEBS_DESTINATION_FEE_RANGES_SEQ_GEN", sequenceName = "CFG_KEBS_DESTINATION_FEE_RANGES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "CFG_KEBS_DESTINATION_FEE_RANGES_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "MINIMUM_USD")
    @Basic
    var minimumUsd: BigDecimal? = null

    @Column(name = "MAXIMUM_USD")
    @Basic
    var maximumUsd: BigDecimal? = null

    @Column(name = "MINIMUM_KSH")
    @Basic
    var minimumKsh: BigDecimal? = null

    @Column(name = "MAXIMUM_KSH")
    @Basic
    var maximumKsh: BigDecimal? = null

    @Column(name = "FIXED_AMOUNT")
    @Basic
    var fixedAmount: BigDecimal? = null

    @Column(name = "RATE")
    @Basic
    var rate: BigDecimal? = null

    // PERCENTAGE,FIXED,RANGE,MANUAL
    @Column(name = "RATE_TYPE")
    @Basic
    var rateType: String? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null


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

    @JoinColumn(name = "INSPECTION_FEE_ID", referencedColumnName = "ID")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var inspectionFee: DestinationInspectionFeeEntity? = null
}