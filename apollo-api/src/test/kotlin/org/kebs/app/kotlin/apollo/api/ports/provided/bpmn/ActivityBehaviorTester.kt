package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.flowable.engine.delegate.DelegateExecution
import org.flowable.engine.impl.delegate.ActivityBehavior
import org.kebs.app.kotlin.apollo.store.model.UsersEntity

class ActivityBehaviorTester : ActivityBehavior {
    var user: UsersEntity? = null
    override fun execute(execution: DelegateExecution?) {
        KotlinLogging.logger { }.info("Loaded ${user?.firstName}")
    }
}