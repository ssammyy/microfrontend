package org.kebs.app.kotlin.apollo.store.model.ms

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_MS_SEIZURE", schema = "APOLLO", catalog = "")
class MsSeizureEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_MS_SEIZURE_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_MS_SEIZURE_SEQ")
    @GeneratedValue(generator = "DAT_MS_SEIZURE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Basic
    @Column(name = "MARKET_TOWN_CENTER")
    var marketTownCenter: String? = null

    @Column(name = "WORKPLAN_GENERATED_ID")
    @Basic
    var workPlanGeneratedID: Long? = null

    @Basic
    @Column(name = "NAME_OF_OUTLET")
    var nameOfOutlet: String? = null

    @Basic
    @Column(name = "DESCRIPTION_PRODUCTS_SEIZED")
    var descriptionProductsSeized: String? = null

    @Basic
    @Column(name = "BRAND")
    var brand: String? = null

    @Basic
    @Column(name = "SECTOR")
    var sector: String? = null

    @Basic
    @Column(name = "REASON_SEIZURE")
    var reasonSeizure: String? = null

    @Basic
    @Column(name = "NAME_SEIZING_OFFICER")
    var nameSeizingOfficer: String? = null

    @Basic
    @Column(name = "SEIZURE_SERIAL")
    var seizureSerial: String? = null

    @Basic
    @Column(name = "QUANTITY")
    var quantity: String? = null

    @Basic
    @Column(name = "UNIT")
    var unit: String? = null

    @Basic
    @Column(name = "ESTIMATED_COST")
    var estimatedCost: String? = null

    @Basic
    @Column(name = "CURRENT_LOCATION")
    var currentLocation: String? = null

    @Basic
    @Column(name = "PRODUCTS_DESTRUCTION")
    var productsDestruction: String? = null

    @Basic
    @Column(name = "STATUS")
    var status: Int? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Basic
    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Basic
    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null

}
