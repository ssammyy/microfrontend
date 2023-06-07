package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_PARTNERS")
class PvocPartnersEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_PARTNERS_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_PARTNERS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_PARTNERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)

    var id: Long = 0

    @Column(name = "API_CLIENT_ID", nullable = true)
    @Basic
    var apiClientId: Long? = 0

    @JoinColumn(name = "PARTNER_TYPE_ID", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    var partnerType: PvocPartnerTypeEntity? = null

    @Column(name = "BILLING_ID", nullable = true)
    @Basic
    var billingId: Long? = 0

    @Column(name = "PARTNER_REF_NO", unique = true, nullable = false, length = 250)
    @Basic
    var partnerRefNo: String? = null

    @Column(name = "PARTNER_NAME", nullable = false, length = 150)
    @Basic
    var partnerName: String? = null

    @Column(name = "PARTNER_PIN", nullable = true, length = 150)
    @Basic
    var partnerPin: String? = null

    @Column(name = "PARTNER_ADDRESS_1", nullable = false, length = 150)
    @Basic
    var partnerAddress1: String? = null

    @Column(name = "PARTNER_ADDRESS_2", nullable = true, length = 150)
    @Basic
    var partnerAddress2: String? = null

    @Column(name = "PARTNER_CITY", nullable = false, length = 100)
    @Basic
    var partnerCity: String? = null

    @Column(name = "PARTNER_ZIPCODE", nullable = false, length = 100)
    @Basic
    var partnerZipcode: String? = null

    @Column(name = "PARTNER_TELEPHONE_NUMBER", nullable = false, length = 100)
    @Basic
    var partnerTelephoneNumber: String? = null

    @Column(name = "PARTNER_FAX_NUMBER", nullable = true, length = 100)
    @Basic
    var partnerFaxNumber: String? = null

    @Column(name = "PARTNER_EMAIL", nullable = false, length = 150)
    @Basic
    var partnerEmail: String? = null

    @JoinColumn(name = "PARTNER_COUNTRY", nullable = true, referencedColumnName = "ID")
    @ManyToOne
    var partnerCountry: PvocPartnersCountriesEntity? = null

    @JoinColumn(name = "PARTNER_REGION", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    var partnerRegion: PvocPartnersRegionEntity? = null

    @Column(name = "CHARGE_RATE", nullable = true, precision = 10, scale = 2)
    @Basic
    var chargeRate: BigDecimal? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Int? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PvocPartnersEntity
        return id == that.id &&
                partnerRefNo == that.partnerRefNo &&
                partnerName == that.partnerName &&
                partnerPin == that.partnerPin &&
                partnerAddress1 == that.partnerAddress1 &&
                partnerAddress2 == that.partnerAddress2 &&
                partnerCity == that.partnerCity &&
                partnerCountry == that.partnerCountry &&
                partnerZipcode == that.partnerZipcode &&
                partnerTelephoneNumber == that.partnerTelephoneNumber &&
                partnerFaxNumber == that.partnerFaxNumber &&
                partnerEmail == that.partnerEmail &&
                status == that.status &&
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
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                partnerRefNo,
                partnerName,
                partnerPin,
                partnerAddress1,
                partnerAddress2,
                partnerCity,
                partnerCountry,
                partnerZipcode,
                partnerTelephoneNumber,
                partnerFaxNumber,
                partnerEmail,
                status,
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
                modifiedBy,
                modifiedOn,
                deleteBy,
                deletedOn
        )
    }
}