package org.kebs.app.kotlin.apollo.api.payload.request

import org.kebs.app.kotlin.apollo.store.model.di.DestinationInspectionFeeEntity
import java.math.BigDecimal

data class DemandNoteItem(
        val itemId: Long?,
        val feeId: Long = 0
)

data class DemandNoteForm(
        val items: List<DemandNoteItem>,
        val remarks: String?,
        val presentment: Boolean,
        val amount: Double, // For Foreign COC/COR
        var includeAll: Boolean
)

class DemandNoteRequestItem {
    var itemId: Long? = null
    var itemValue: BigDecimal? = null
    var currency: String? = null
    var quantity: Long = 0
    var route: String = "A"
    var productName: String? = null
    var fee: DestinationInspectionFeeEntity? = null
}

class DemandNoteRequestForm {
    var items: MutableList<DemandNoteRequestItem>? = null
    var invoicePrefix: String = "DN"
    var referenceId: Long? = null
    var referenceNumber: String? = null
    var name: String? = null
    var importerPin: String? = null
    var product: String? = null
    var ucrNumber: String? = null
    var phoneNumber: String? = null
    var ablNumber: String? = null
    var address: String? = null
    var courier: String? = null
    var customsOffice: String? = null
    var entryPoint: String? = null
    var entryNo: String? = null
    var remarks: String? = null
    var presentment: Boolean = false
    var amount: Double = 0.0 // For Foreign COC/COR
    fun addItem(itm: DemandNoteRequestItem) {
        if (this.items == null) {
            this.items = mutableListOf()
        }
        this.items?.add(itm)
    }
}