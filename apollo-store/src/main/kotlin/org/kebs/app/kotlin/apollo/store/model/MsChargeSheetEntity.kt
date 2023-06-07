package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_CHARGE_SHEET")
class MsChargeSheetEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_CHARGE_SHEET_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_CHARGE_SHEET_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_CHARGE_SHEET_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "CHRISTIAN_NAME")
    @Basic
    var christianName: String? = null

    @Column(name = "SURNAME")
    @Basic
    var surname: String? = null

    @Column(name = "SEX")
    @Basic
    var sex: String? = null

    @Column(name = "NATIONALITY")
    @Basic
    var nationality: String? = null

    @Column(name = "AGE")
    @Basic
    var age: Long? = null

    @Column(name = "ADDRESS_DISTRICT")
    @Basic
    var addressDistrict: String? = null

    @Column(name = "ADDRESS_LOCATION")
    @Basic
    var addressLocation: String? = null

    @Column(name = "FIRST_COUNT")
    @Basic
    var firstCount: String? = null

    @Column(name = "PARTICULARS_OFFENCE_ONE")
    @Basic
    var particularsOffenceOne: String? = null

    @Column(name = "SECOND_COUNT")
    @Basic
    var secondCount: String? = null

    @Column(name = "PARTICULARS_OFFENCE_SECOND")
    @Basic
    var particularsOffenceSecond: String? = null

    @Column(name = "DATE_ARREST")
    @Basic
    var dateArrest: Date? = null

    @Column(name = "WITH_WARRANT")
    @Basic
    var withWarrant: String? = null

    @Column(name = "APPLICATION_MADE_SUMMONS_SUE")
    @Basic
    var applicationMadeSummonsSue: String? = null

    @Column(name = "DATE_APPREHENSION_COURT")
    @Basic
    var dateApprehensionCourt: Date? = null

    @Column(name = "BOND_BAIL_AMOUNT")
    @Basic
    var bondBailAmount: Long? = null

    @Column(name = "REMANDED_ADJOURNED")
    @Basic
    var remandedAdjourned: String? = null

    @Column(name = "COMPLAINANT_NAME")
    @Basic
    var complainantName: String? = null

    @Column(name = "COMPLAINANT_ADDRESS")
    @Basic
    var complainantAddress: String? = null

    @Column(name = "PROSECUTOR")
    @Basic
    var prosecutor: String? = null

    @Column(name = "WITNESSES")
    @Basic
    var witnesses: String? = null

    @Column(name = "SENTENCE")
    @Basic
    var sentence: String? = null

    @Column(name = "FINE_PAID")
    @Basic
    var finePaid: String? = null

    @Column(name = "COURT_NAME")
    @Basic
    var courtName: String? = null

    @Column(name = "COURT_DATE")
    @Basic
    var courtDate: Date? = null

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

    @Column(name = "MS_WORKPLAN_GENERATED_ID")
    @Basic
    var workPlanGeneratedID: Long? = null

}
