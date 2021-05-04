package org.kebs.app.kotlin.apollo.api.ports.provided.bpmn

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
import org.kebs.app.kotlin.apollo.common.dto.brs.response.BrsLookUpResponse
import org.kebs.app.kotlin.apollo.store.model.ManufacturersEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant

@Component
class PrepareEmailForSending(
        private val daoServices: RegistrationDaoServices,
        private val sendToKafkaQueue: SendToKafkaQueue,
        private val serviceRequestRepo: IServiceRequestsRepository,
        private val userRepo: IUserRepository
) {

    @Autowired
    lateinit var configurationRepository: IIntegrationConfigurationRepository

    @Autowired
    lateinit var daoService: DaoService

    @Autowired
    lateinit var logsRepo: IWorkflowTransactionsRepository



    fun sendEmail(user: UsersEntity, sr: ServiceRequestsEntity, map: ServiceMapsEntity): Int? {
        daoServices.generateVerificationToken(sr, user, map)
        KotlinLogging.logger {  }.info { "Sending email to queue" }
        return daoServices.submitEmailToQueue(map, user, sr)

    }

    fun sendToKra(user: UsersEntity, sr: ServiceRequestsEntity, map: ServiceMapsEntity): Int? {
        KotlinLogging.logger {  }.info { "Sending to KRA" }
        return daoServices.submitKraToQueue(map, user, sr)
    }

    /**
     * TODO Check BRS
     */

    /**
     *
     */
    suspend fun checkBrs(user: ManufacturersEntity, sr: ServiceRequestsEntity, map: ServiceMapsEntity, workFlowId: Long): Unit? {
        KotlinLogging.logger {  }.info { "Sending BRS for verification" }
//        sr.varField1 = user.id
        return sendToKafkaQueue.submitAsyncRequestToBus(sr, map.serviceTopic)
    }

}