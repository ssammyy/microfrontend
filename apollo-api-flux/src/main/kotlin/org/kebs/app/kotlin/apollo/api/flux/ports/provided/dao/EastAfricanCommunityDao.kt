package org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao

import akka.actor.ActorSystem
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.common.dto.eac.requests.CertifiedProduct
import org.kebs.app.kotlin.apollo.common.dto.eac.requests.TokenRequest
import org.kebs.app.kotlin.apollo.common.dto.eac.responses.RequestResult
import org.kebs.app.kotlin.apollo.common.dto.eac.responses.TokenRequestResult

import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.kebs.app.kotlin.apollo.store.model.BatchJobDetails
import org.kebs.app.kotlin.apollo.store.model.IntegrationConfigurationEntity
import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EastAfricanCommunityDao(
    private val integRepo: IIntegrationConfigurationRepository,
    private val jobsRepo: IBatchJobDetailsRepository,
    private val extension: ActorSpringExtension,
    private val actorSystem: ActorSystem,
    private val daoService: DaoService,
    private val jasyptStringEncryptor: StringEncryptor,
) {
    suspend fun startProductPostJob(jobId: String): String {
        return submitJobToActor(jobId)
    }

    /**
     * Submit a single product for onward processing on eac api end-point
     * @param data the CertifiedProduct data to be sent to EAC
     */
    suspend fun postProduct(data: CertifiedProduct, config: IntegrationConfigurationEntity, job: BatchJobDetails): RequestResult? {
        val c = generateToken(config)
        val log = daoService.createTransactionLog(0, daoService.generateTransactionReference())
        val finalUrl = "${c.url}${job.jobUri}"
        val resp = daoService.getHttpResponseFromPostCall(finalUrl, c.token, data, c, null, null, log)
        return daoService.processResponses<RequestResult>(resp, log, finalUrl, c).second


    }

    /**
     * Invoke the authenticate function and save token and token validity information on the database
     * Current implementation deserializes the input json and saves the token on the  IntegrationConfigurationEntity
     *
     * @param config Integration configuration with an expired or blank token
     *
     * @return IntegrationConfigurationEntity updated with a token that is not expired
     */
    suspend fun generateToken(config: IntegrationConfigurationEntity): IntegrationConfigurationEntity {
        val finalUrl = "${config.url}${config.clientAuthenticationRealm}"
        val log = daoService.createTransactionLog(0, daoService.generateTransactionReference())
        val loginRequest = TokenRequest(jasyptStringEncryptor.decrypt(config.username), jasyptStringEncryptor.decrypt(config.password), jasyptStringEncryptor.decrypt(config.secretValue))
        val resp = daoService.getHttpResponseFromPostCall(finalUrl, null, loginRequest, config, null, null, log)
        val data = daoService.processResponses<TokenRequestResult>(resp, log, finalUrl, config)
        config.tokenTimeGenerated = log.transactionStartDate
        val tokenResult = data.second?.message

        tokenResult?.accessToken?.let {
            config.tokenTimeExpires = tokenResult.expiryDate
            config.token = it
        }

        return integRepo.save(config)


    }
    /**
     *
     */

    /**
     * Pick up correct akka worker and submit job for processing
     * The worker is set up using {@link org.kebs.app.kotlin.apollo.store.model.BatchJobDetails#processingActorBean}
     */
    private fun submitJobToActor(jobId: String): String {
        var result = ""
        try {
            jobId.toLongOrNull()
                ?.let { id ->
                    jobsRepo.findByIdOrNull(id)
                        ?.let { job ->
                            job.processingActorBean
                                ?.let { actorBean ->
                                    val actorRef = actorSystem.actorOf(extension.get(actorSystem).props(actorBean), actorBean)
                                    actorRef.tell(job, actorRef)
                                    result = "Job successfully submitted for processing"

                                }
                                ?: throw NullValueNotAllowedException("Processing Actor not found for job {id=${job.id}")


                        }

                        ?: throw InvalidInputException("Invalid jobId=$jobId no BatchJob found, aborting")
                }
                ?: throw InvalidInputException("Invalid jobId=$jobId, aborting")
        } catch (e: Exception) {
            throw InvalidInputException(e.message)
        }
        return result
    }

}
