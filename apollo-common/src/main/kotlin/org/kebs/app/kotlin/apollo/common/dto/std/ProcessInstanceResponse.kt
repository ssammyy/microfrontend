package org.kebs.app.kotlin.apollo.common.dto.std


class ProcessInstanceResponseValue (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val requestNumber: String) {
}
class ProcessInstanceResponse(val processId: String, val isEnded: Boolean) {
}
class ProcessInstanceDISDT (val savedRowID: Long?,val processId: String, val isEnded: Boolean) {
}
