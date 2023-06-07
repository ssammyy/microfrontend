package org.kebs.app.kotlin.apollo.api.objects

import org.flowable.task.api.Task
import java.sql.Timestamp

class DisplayInvoiceDetails()   {
    var id: Long = 0
    var createdOn: Timestamp? = null
    var expiryDate: Timestamp? = null
    var permitType: String = ""
    var status: Int = 0
    var product: Long = 0
    var tradeMark: String = ""

    override fun toString(): String {
        return "DisplayInvoiceDetails(id=$id, createdOn=$createdOn, expiryDate=$expiryDate, permitType='$permitType', status=$status, product=$product, tradeMark='$tradeMark')"
    }

}