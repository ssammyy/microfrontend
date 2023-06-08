package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "STG_CERTIFIED_PRODUCTS_DETAILS")
class CertifiedProductsDetailsEntity : Serializable {
    @Id
    @SequenceGenerator(name = "STG_CERTIFIED_PRODUCTS_DETAILS_SEQ_GEN", sequenceName = "STG_CERTIFIED_PRODUCTS_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "STG_CERTIFIED_PRODUCTS_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    var id: Long? = null

    @Column(name = "PRODUCT_NAME")
    var productName: String? = null

    @Column(name = "BRAND_NAME")
    var brandName: String? = null

    @Column(name = "COUNTRY_OF_ORIGIN")
    var countryOfOrigin: String? = null

    @Column(name = "HS_CODE")
    var hsCode: String? = null

    @Column(name = "STANDARD_GOVERNING")
    var standardGoverning: String? = null

    @Column(name = "DATE_MARK_ISSUED")
    var dateMarkIssued: LocalDate? = null

    @Column(name = "DATE_MARKED_EXPIRES")
    var dateMarkedExpires: LocalDate? = null

    @Column(name = "PRODUCT_DESCRIPTION")
    var productDescription: String? = null

    @Column(name = "PERMIT_NUMBER")
    var permitNumber: String? = null

    @Column(name = "PRODUCT_REFERENCE")
    var productReference: String? = null

    @Column(name = "AGENCY")
    var agency: String? = null

    @Column(name = "REGULATION_STATUS")
    var regulationStatus: String? = null

    @Column(name = "PRODUCT_STATE")
    var productState: String? = null

    @Column(name = "EAC_RESPONSE_CODE")
    var eacResponseCode: String? = null

    @Column(name = "EAC_RESPONSE_STATUS")
    var eacResponseStatus: String? = null

    @Column(name = "EAC_RESPONSE_MESSAGE")
    var eacResponseMessage: String? = null

    @Column(name = "EAC_RESPONSE_PAYLOAD")
    var eacResponsePayload: String? = null

    @Column(name = "TRANSACTION_DATE", nullable = false)
    var transactionDate: LocalDate? = null

    @Column(name = "TRANSMISSION_DATE")
    var transmissionDate: LocalDateTime? = null


    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "STATUS")
    var status: BigDecimal? = null

    @Column(name = "DESCRIPTIONS")
    var descriptions: String? = null

    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false)
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    var createdOn: Date? = null

    @Column(name = "LAST_MODIFIED_BY")
    var lastModifiedBy: String? = null

    @Column(name = "LAST_MODIFIED_ON")
    var lastModifiedOn: Date? = null

    @Column(name = "UPDATE_BY")
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    var updatedOn: Date? = null

    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    var deletedOn: Date? = null

    @Column(name = "VERSION")
    var version: BigDecimal? = null


}
