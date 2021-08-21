package org.kebs.app.kotlin.apollo.common.dto.std

import java.sql.Timestamp


class ProcessInstanceResponseValue (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val requestNumber: String) {
}
class ProcessInstanceResponse(val processId: String, val isEnded: Boolean) {
}
class ProcessInstanceDISDT (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val datePrepared: Timestamp) {
}

class ProcessInstanceProposal (val savedRowID: Long?,val processId: String, val isEnded: Boolean,val proposalNumber: String) {

}
