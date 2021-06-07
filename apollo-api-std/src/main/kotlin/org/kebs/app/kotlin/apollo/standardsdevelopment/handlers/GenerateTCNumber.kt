package org.kebs.app.kotlin.apollo.standardsdevelopment.handlers

import org.flowable.engine.delegate.DelegateExecution
import org.flowable.engine.delegate.JavaDelegate

class GenerateTCNumber : JavaDelegate {
    override fun execute(execution: DelegateExecution?) {
        println("Generating TC number and advertising position on website")
    }
}
