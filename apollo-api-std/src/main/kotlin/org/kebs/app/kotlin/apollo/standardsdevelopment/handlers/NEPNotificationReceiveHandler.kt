package org.kebs.app.kotlin.apollo.standardsdevelopment.handlers

import org.flowable.engine.delegate.DelegateExecution
import org.flowable.engine.delegate.JavaDelegate

class NEPNotificationReceiveHandler : JavaDelegate {
    override fun execute(p0: DelegateExecution?) {
        println("Notification Received")
    }
}
