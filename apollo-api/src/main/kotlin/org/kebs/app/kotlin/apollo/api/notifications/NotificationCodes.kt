package org.kebs.app.kotlin.apollo.api.notifications

enum class NotificationCodes {
    COMPLAINT_RECEIVED,
    COMPLAINT_REJECTED,
    COMPLAINT_APPROVED,
    COMPLAINT_APPROVED_PVOC,
    PVOC_PARTNER_API_CLIENT_CREDENTIAL
}

enum class NotificationTypeCodes(val description: String) {
    SMS("Simple Messages via mobile phones"), EMAIL("Email notification"), PUSH("Push notifications"), OTHER("Other notifications")
}