package org.kebs.app.kotlin.apollo.api.payload.request

data class DemandNoteItem(
        val itemId: Long?,
        val amount: Double
)

data class DemandNoteForm(
        val items: List<DemandNoteItem>,
        val remarks: String?
)