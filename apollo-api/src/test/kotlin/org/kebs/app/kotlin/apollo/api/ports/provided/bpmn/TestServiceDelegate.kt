//package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn
//
//
//import mu.KotlinLogging
//import org.flowable.engine.delegate.DelegateExecution
//import org.flowable.engine.delegate.JavaDelegate
//import org.springframework.stereotype.Service
//
//@Service
//class TestServiceDelegate : JavaDelegate {
//    override fun execute(execution: DelegateExecution?) {
//        KotlinLogging.logger { }.info("ActivityId=${execution?.currentActivityId} id=${execution?.id} ProcessInstanceId=${execution?.processInstanceId} ")
////        var mapsEntity:ServiceMapsEntity? =null
////        var usersEntity:UsersEntity?= null
////        execution?.variables?.let {
////            when{
////                it["map"] is ServiceMapsEntity? -> mapsEntity = it["map"] as ServiceMapsEntity?
////                it["user"] is UsersEntity? -> usersEntity = it["user"] as UsersEntity?
////            }
////        }
////        when {execution?.variables.get("map") is ServiceMapsEntity->mapsEntity =execution?.variables?.get("map")
////
////        }
////        daoServices.registerEmployee()
//
//    }
//}
