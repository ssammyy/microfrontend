package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import org.kebs.app.kotlin.apollo.api.controllers.msControllers.MSReportsControllers
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.store.repo.IPvocInvoicingRepository
import org.kebs.app.kotlin.apollo.store.repo.IPvocPartnersRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.sql.Timestamp
import java.text.SimpleDateFormat

@Service
class PvocDaoServices(
        private val iPvocPartnersRepository: IPvocPartnersRepository,
        private val iPvocInvoicingRepository: IPvocInvoicingRepository,
        private val msReportsControllers: MSReportsControllers,
        private val notifications: Notifications
) {
    val map = hashMapOf<String, Any>()
    fun getDateTime(s: Timestamp?): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            return sdf.format(s)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun generateRandomNumbers(symbol: String): String{
        val randomPIN = (Math.random() * 90000).toInt() + 100000
        return "${symbol}-" + randomPIN
    }

    fun sendEmailWithReconciliationInvoice(recipient: String, attachment: String?): Boolean {
        val subject = "RECONCILIATION INVOICE"
        val messageBody = "Check The attached Reconciliation Invoice for payment"

//        notifications.sendEmail(recipient, subject, messageBody)
        notifications.processEmail(recipient, subject, messageBody, attachment)

        return true
    }

    fun sendInvoice(id: Long){
        id.let {it ->
            iPvocInvoicingRepository.findByReconcialitionId(it).let { invoice ->
                iPvocPartnersRepository.findByIdOrNull(invoice?.partner)?.let { partinerDetails ->
                    map["exporterName"] = partinerDetails.partnerName.toString()
                    map["exporterAddress"] = partinerDetails.partnerAddress1.toString()
                }
                map["invoiceNumber"] = invoice?.invoiceNumber.toString()
                map["documentDate"] = getDateTime(invoice?.createdOn).toString()
                map["expoterAddress"] = invoice?.partner.toString()
                map["orderNumber"] = invoice?.orderNumber.toString()
                map["orderDate"] = invoice?.orderDate.toString()
                map["customerNumber"] = invoice?.customerNumber.toString()
                map["poNumber"] = invoice?.poNumber.toString()
                map["shipVia"] = invoice?.shipVia.toString()
                map["termsCode"] = invoice?.partner.toString()
                map["amount"] = invoice?.amountDue.toString()
                map["description"] = invoice?.description.toString()
                map["subtotalBeforeTaxes"] = "0.00"
                map["totalTaxes"] = "0.00"
                map["totalAmount"] = invoice?.amountDue.toString()
            }
        }
        //Todo Gabriel check also this
        iPvocInvoicingRepository.findByReconcialitionId(id).let { invoice ->
            invoice?.reconcialitionId?.let {
                msReportsControllers.extractAndSaveReport(map, "classpath:reports/reconciliationReport.jrxml", "Remediation-Invoice", listOf<Any>())
            }
            iPvocPartnersRepository.findByIdOrNull(invoice?.partner)?.let { partiner ->
                partiner.partnerEmail?.let { sendEmailWithReconciliationInvoice(it, ResourceUtils.getFile("classpath:templates/TestPdf/Remediation-Invoice.pdf").toString()) }
            }
        }
    }
}