package org.kebs.app.kotlin.apollo.standardsdevelopment.handlers

import org.flowable.engine.delegate.DelegateExecution
import org.flowable.engine.delegate.JavaDelegate


class EnquiryNotificationHandler : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        println("SIC Notification Received")
    }
}
