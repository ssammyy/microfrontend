package org.kebs.app.kotlin.apollo.api.payload.request

data class DemandNoteItem(
        val itemId: Long?,
        val feeId: Long=0
)

data class DemandNoteForm(
        val items: List<DemandNoteItem>,
        val remarks: String?,
        val presentment: Boolean,
        val amount: Double, // For Foreign COC/COR
        var includeAll: Boolean
)