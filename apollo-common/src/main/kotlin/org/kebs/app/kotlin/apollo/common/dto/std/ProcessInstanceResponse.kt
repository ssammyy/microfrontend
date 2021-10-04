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
class ProcessInstancePD (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val datePdPrepared: Timestamp) {
}
class ProcessInstanceWD (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val datePdPrepared: Timestamp) {
}
class ProcessInstanceUS (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val ksNumber: String) {
}
class ProcessInstanceComJc (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val datePrepared: Timestamp) {
}
class ProcessInstanceComDraft (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val draftNumber: String) {
}
class ProcessInstanceComStandard (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val comStdNumber: String) {
}
class ProcessInstanceEditStandard (val savedRowID: Long?,val processId: String, val isEnded: Boolean, val comStdNumber: String) {
}
