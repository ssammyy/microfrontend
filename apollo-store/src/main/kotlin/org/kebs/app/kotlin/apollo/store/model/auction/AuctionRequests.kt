package org.kebs.app.kotlin.apollo.store.model.auction

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_AUCTION_REQUESTS")
class AuctionRequests : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_AUCTION_REQUESTS_SEQ_GEN", sequenceName = "DAT_KEBS_AUCTION_REQUESTS_COR_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_AUCTION_REQUESTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null

    @Column(name = "CONSIGNMENT_ID")
    @Basic
    var consignmentId: Long? = null // If it was inspected, this will be filled

    @JoinColumn(name = "CTAEGORY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var category: AuctionCategory? = null // If it was inspected, this will be filled

    @Column(name = "AUCTION_LOG_NO")
    @Basic
    var auctionLotNo: String? = null

    @Column(name = "AUCTION_DATE")
    @Basic
    var auctionDate: Date? = null

    @Column(name = "SHIPMENT_PORT")
    @Basic
    var shipmentPort: String? = null

    @Column(name = "SHIPMENT_DATE")
    @Basic
    var shipmentDate: Date? = null

    @Column(name = "ARRIVAL_DATE")
    @Basic
    var arrivalDate: Date? = null

    @Column(name = "IMPORTER_NAME")
    @Basic
    var importerName: String? = null

    @Column(name = "ITEMS_LOCATION")
    @Basic
    var location: String? = null

    @Column(name = "IMPORTER_PHONE")
    @Basic
    var importerPhone: String? = null

    @Column(name = "ASSIGNER")
    @Basic
    var assigner: UsersEntity? = null

    @Column(name = "ASSIGNED_OFFICER")
    @Basic
    var assignedOfficer: UsersEntity? = null

    @Column(name = "ASSIGNED_ON")
    @Basic
    var assignedOn: Date? = null

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: Int? = null

    @Column(name = "APPROVED_REJECTED_OM")
    @Basic
    var approvedRejectedOn: Timestamp? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "REPORT_ID")
    @Basic
    var reportId: Long? = null

    @Column(name = "DEMAND_NOTE_ID")
    @Basic
    var demandNoteId: Long? = null

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
}