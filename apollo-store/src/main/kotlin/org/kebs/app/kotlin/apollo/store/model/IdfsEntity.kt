package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_IDFS")
class IdfsEntity:Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_IDFS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_IDFS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_IDFS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "IDF_NUMBER", nullable = false, length = 50)
    @Basic
    var idfNumber: String? = null

    @Column(name = "UCR", nullable = false, length = 50)
    @Basic
    var ucr: String? = null

    @Column(name = "IMPORTER_NAME", nullable = false, length = 1000)
    @Basic
    var importerName: String? = null

    @Column(name = "IMPORTER_ADDRESS", nullable = false, length = 1000)
    @Basic
    var importerAddress: String? = null

    @Column(name = "IMPORTER_EMAIL", nullable = false, length = 150)
    @Basic
    var importerEmail: String? = null

    @Column(name = "IMPORTER_TELEPHONE_NUMBER", nullable = false, length = 100)
    @Basic
    var importerTelephoneNumber: String? = null

    @Column(name = "IMPORTER_CONTACT_NAME", nullable = true, length = 100)
    @Basic
    var importerContactName: String? = null

    @Column(name = "IMPORTER_FAX", nullable = true, length = 100)
    @Basic
    var importerFax: String? = null

    @Column(name = "SELLER_NAME", nullable = false, length = 1000)
    @Basic
    var sellerName: String? = null

    @Column(name = "SELLER_ADDRESS", nullable = false, length = 1000)
    @Basic
    var sellerAddress: String? = null

    @Column(name = "SELLER_EMAIL", nullable = false, length = 150)
    @Basic
    var sellerEmail: String? = null

    @Column(name = "SELLER_TELEPHONE_NUMBER", nullable = false, length = 100)
    @Basic
    var sellerTelephoneNumber: String? = null

    @Column(name = "SELLER_CONTACT_NAME", nullable = true, length = 100)
    @Basic
    var sellerContactName: String? = null

    @Column(name = "SELLER_FAX", nullable = true, length = 100)
    @Basic
    var sellerFax: String? = null

    @Column(name = "COUNTRY_OF_SUPPLY", nullable = false, length = 4000)
    @Basic
    var countryOfSupply: String? = null

    @Column(name = "PORT_OF_DISCHARGE", nullable = false, length = 3000)
    @Basic
    var portOfDischarge: String? = null

    @Column(name = "PORT_OF_CUSTOMS_CLEARANCE", nullable = false, length = 3000)
    @Basic
    var portOfCustomsClearance: String? = null

    @Column(name = "MODE_OF_TRANSPORT", nullable = true, length = 150)
    @Basic
    var modeOfTransport: String? = null

    @Column(name = "COMESA", nullable = false, length = 10)
    @Basic
    var comesa: String? = null

    @Column(name = "INVOICE_NUMBER", nullable = false, length = 30)
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "INVOICE_DATE", nullable = false)
    @Basic
    var invoiceDate: Timestamp? = null

    @Column(name = "CURRENCY", nullable = false, length = 10)
    @Basic
    var currency: String? = null

    @Column(name = "EXCHANGE_RATE", nullable = false, precision = 2)
    @Basic
    var exchangeRate: Double = 0.0

    @Column(name = "FOB_VALUE", nullable = false, precision = 2)
    @Basic
    var fobValue: Double = 0.0

    @Column(name = "FREIGHT", nullable = false, precision = 2)
    @Basic
    var freight: Double = 0.0

    @Column(name = "INSURANCE", nullable = true, precision = 2)
    @Basic
    var insurance: Double? = null

    @Column(name = "OTHER_CHARGES", nullable = false, precision = 2)
    @Basic
    var otherCharges: Double = 0.0

    @Column(name = "TOTAL", nullable = false, precision = 2)
    @Basic
    var total: Double = 0.0

    @Column(name = "OBSERVATIONS", nullable = false, length = 500)
    @Basic
    var observations: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Int? = null

    @Column(name = "USED_STATUS", nullable = true, precision = 0)
    @Basic
    var usedStatus: Int? = null

    @Column(name = "PARTNER_ID", nullable = true, precision = 0)
    @Basic
    var partner: Long? = null

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
        val that = other as IdfsEntity
        return id == that.id && exchangeRate == that.exchangeRate && fobValue == that.fobValue && freight == that.freight && otherCharges == that.otherCharges && total == that.total &&
                idfNumber == that.idfNumber &&
                ucr == that.ucr &&
                importerName == that.importerName &&
                importerAddress == that.importerAddress &&
                importerEmail == that.importerEmail &&
                importerTelephoneNumber == that.importerTelephoneNumber &&
                importerContactName == that.importerContactName &&
                importerFax == that.importerFax &&
                sellerName == that.sellerName &&
                sellerAddress == that.sellerAddress &&
                sellerEmail == that.sellerEmail &&
                sellerTelephoneNumber == that.sellerTelephoneNumber &&
                sellerContactName == that.sellerContactName &&
                sellerFax == that.sellerFax &&
                countryOfSupply == that.countryOfSupply &&
                portOfDischarge == that.portOfDischarge &&
                portOfCustomsClearance == that.portOfCustomsClearance &&
                modeOfTransport == that.modeOfTransport &&
                comesa == that.comesa &&
                invoiceNumber == that.invoiceNumber &&
                invoiceDate == that.invoiceDate &&
                currency == that.currency &&
                insurance == that.insurance &&
                observations == that.observations &&
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
            idfNumber,
            ucr,
            importerName,
            importerAddress,
            importerEmail,
            importerTelephoneNumber,
            importerContactName,
            importerFax,
            sellerName,
            sellerAddress,
            sellerEmail,
            sellerTelephoneNumber,
            sellerContactName,
            sellerFax,
            countryOfSupply,
            portOfDischarge,
            portOfCustomsClearance,
            modeOfTransport,
            comesa,
            invoiceNumber,
            invoiceDate,
            currency,
            exchangeRate,
            fobValue,
            freight,
            insurance,
            otherCharges,
            total,
            observations,
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