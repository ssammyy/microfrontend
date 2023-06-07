package org.kebs.app.kotlin.apollo.ipc.adaptor.kafka.consumer

//import org.kebs.app.kotlin.apollo.common.brs.BrsKafkaRequest
import akka.actor.ActorRef
import akka.actor.ActorSystem
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.integ.KtorHttpClient
import org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service.LookUpBusinessOnBrsService
import org.kebs.app.kotlin.apollo.ipc.dto.BrsLookUpResponse
import org.kebs.app.kotlin.apollo.ipc.ports.provided.TransactionReferenceGenerator
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
@EncryptablePropertySource("\"file:\\\${CONFIG_PATH}/kafka.properties\"")
class LookUpBrsConsumerService(
    extension: ActorSpringExtension,
    actorSystem: ActorSystem,
    private val mapsRepository: IServiceMapsRepository,
    private val lookUpBrsService: LookUpBusinessOnBrsService,
    private val ktorHttpClient: KtorHttpClient,
    private val serviceMapsWorkflowsRepository: IServiceMapsWorkflowsRepository,
    private val integrationConfigurationRepository: IIntegrationConfigurationRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val workflowTransactionsRepository: IWorkflowTransactionsRepository,
    private val brsLookupManufacturerDataRepo: IBrsLookupManufacturerDataRepository,
    private val brsLookupManufacturerPartnerRepo: IBrsLookupManufacturerPartnersRepository,
    private val manufacturerRepository: IManufacturerRepository
) {

    final var processorSelectionActor: ActorRef? = null

    init {
        processorSelectionActor = actorSystem.actorOf(
            extension[actorSystem].props("processorSelectionActor"),
            "processorSelectionNotificationsActor"
        )
    }

    private fun transactionReferenceGenerator(serviceMapsEntity: ServiceMapsEntity): String {
        val result: String
        val generateTransactionReference = TransactionReferenceGenerator(
                serviceMapsEntity.transactionRefLength,
                serviceMapsEntity.secureRandom,
                serviceMapsEntity.messageDigestAlgorithm
        )
        result = generateTransactionReference.generateTransactionReference()
        return result

    }


    @KafkaListener(
            clientIdPrefix = "brsUseCase",
            topics = ["#{'\${org.kebs.app.kotlin.apollo.kafka.consumer.notification.select.usecase.topics.list}'.split(',')}"],
            groupId = "\${org.kebs.app.kotlin.apollo.kafka.consumer.groupId}"
    )
    fun brsUseCase(
            requests: List<ServiceRequestsEntity>,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partitions: Int,
            @Header(KafkaHeaders.OFFSET) offsets: Long,
            @Header(KafkaHeaders.TIMESTAMP_TYPE) timestampType: String,
            @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) messageKey: String?,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) timestamp: Long
    )
    {
                    requests.forEach { req ->
                        try {
                            with(req) {
                                kafkaPartitionId = partitions
                                kafkaOffset = offsets
                                kafkaTopic = topic
                                kafkaMessageKey = messageKey
                                kafkaTimestampType = timestampType
                                kafkaTimestamp = timestamp
                                requestDate = Date(Date().time)
                                serviceRequestsRepository.save(req)
                            }
                            var log = WorkflowTransactionsEntity()
                            var brsLookupManufacturerDataEntity =  BrsLookupManufacturerDataEntity()
                            try {
                                req.serviceMapsId?.let { map ->
                                    with(log) {
                                        transactionDate = Date(Date().time)
                                        transactionStartDate = Timestamp.from(Instant.now())
                                        serviceRequests = req
                                        retried = 0
                                        transactionStatus = map.initStatus
                                        createdBy = "${req.id}_${req.transactionReference}"
                                        createdOn = Timestamp.from(Instant.now())
                                    }

                                    serviceMapsWorkflowsRepository.findByIdOrNull(req.varField2?.toLong())
                                            ?.let { flowId ->
                                                log.methodId = flowId
                                                integrationConfigurationRepository.findByWorkflowId(flowId.id)
                                                        ?.let { config ->
                                                            log.currentSequence = req.currentStage
                                                            log.executionOrder = req.currentSequence
                                                            log.transactionReference = "${req.transactionReference}_${log.currentSequence}_${log.executionOrder}"
                                                            log.integrationId = config
                                                            val requestParams: MutableMap<String, String> = mutableMapOf()
                                                            manufacturerRepository.findByIdOrNull(req.varField3?.toLong())
                                                                    ?.let { man ->
                                                                        man.registrationNumber.let { it?.let { it1 -> requestParams.putIfAbsent(config.requestParams.split(config.requestParamsSeparator)[0], it1) } }
                                                                        log = runBlocking {
                                                                            ktorHttpClient.sendRequests(config, null, log, map, requestParams, null)
                                                                                    ?: throw InvalidValueException("Empty value")
                                                                        }
                                                                        lookUpBrsService.convertIntegrationResponseToJson(log, config)
                                                                                ?.let { testModel ->
                                                                                    val brsLookUpResponse: BrsLookUpResponse = testModel as BrsLookUpResponse
                                                                                    brsLookUpResponse.record
                                                                                            ?.let { record ->
                                                                                                if ((record.registrationNumber == man.registrationNumber)
                                                                                                        && (record.kraPin == man.kraPin) &&
                                                                                                        record.businessName == man.name)   {
                                                                                                    with(req){
                                                                                                        responseMessage = "Details match"
                                                                                                        with(brsLookupManufacturerDataEntity) {
                                                                                                            manufacturerId =
                                                                                                                man.id
                                                                                                            transactionDate =
                                                                                                                Date(
                                                                                                                    Date().time
                                                                                                                )
                                                                                                            registrationNumber = record.registrationNumber
                                                                                                            registrationDate = record.registrationDate?.time?.let { Date(it) }
                                                                                                            postalAddress = record.postalAddress
                                                                                                            physicalAddress = record.physicalAddress
                                                                                                            phoneNumber = record.phoneNumber
                                                                                                            brsId = record.id
                                                                                                            email = record.email
                                                                                                            kraPin = record.kraPin
                                                                                                            businessName = record.businessName
                                                                                                            brsStatus = record.status
                                                                                                            status = log.serviceRequests?.serviceMapsId?.successStatus
                                                                                                            createdBy = log.transactionReference
                                                                                                            createdOn = Timestamp.from(Instant.now())
                                                                                                        }
                                                                                                        brsLookupManufacturerDataEntity = brsLookupManufacturerDataRepo.save(brsLookupManufacturerDataEntity)

                                                                                                        var brsLookupManufacturerPartnersEntity: BrsLookupManufacturerPartnersEntity

                                                                                                        record.partners.forEach { partner ->
                                                                                                            brsLookupManufacturerPartnersEntity = BrsLookupManufacturerPartnersEntity()
                                                                                                            with(brsLookupManufacturerPartnersEntity) {
                                                                                                                manufacturerId =
                                                                                                                    brsLookupManufacturerDataEntity.manufacturerId
                                                                                                                transactionDate =
                                                                                                                    Date(
                                                                                                                        Date().time
                                                                                                                    )
                                                                                                                names = partner.name
                                                                                                                idType = partner.idType
                                                                                                                idNumber = partner.idNumber
                                                                                                                status = log.serviceRequests?.serviceMapsId?.successStatus
                                                                                                                createdBy = log.transactionReference
                                                                                                                createdOn = Timestamp.from(Instant.now())
                                                                                                            }
                                                                                                            brsLookupManufacturerPartnersEntity = brsLookupManufacturerPartnerRepo.save(brsLookupManufacturerPartnersEntity)
                                                                                                        }
                                                                                                    }

                                                                                                    serviceRequestsRepository.save(req)
                                                                                                    KotlinLogging.logger {  }.info { "Details match" }
                                                                                                } else {
                                                                                                    with(req){
                                                                                                        responseMessage = "Details mismatch"
                                                                                                    }
                                                                                                    serviceRequestsRepository.save(req)
                                                                                                    KotlinLogging.logger {  }.info { "Details mismatch" }
                                                                                                }
                                                                                            }

                                                                                }

                                                                    }
                                                        }
                                            }
                                }


                            }catch (e: Exception) {

                            }
                        }catch (e: Exception) {
                            KotlinLogging.logger { }.error(e.message, e)
                        }
                    }
                }
}