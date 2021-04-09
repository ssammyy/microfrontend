/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service.ms

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintCustomersEntity
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.ms.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class MarketSurveillanceComplaintsWorkFlowService(
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val workflowTransactionsRepository: IWorkflowTransactionsRepository,
    private val complaintsRepo: IComplaintRepository,
    private val workflowRepo: IServiceMapsWorkflowsRepository,
    private val complaintsOfficerRepo: IComplaintOfficerRepository,
    private val complaintsDocRepo: IComplaintDocumentsRepository,
    private val complaintRemarksRepo: IComplaintRemarksRepository,
    private val complaintRemediesRepo: IComplaintRemediesRepository,
    private val complaintsKebsRemarksRepository: IComplaintKebsRemarksRepository,
    private val complaintHandlersRepository: IComplaintHandlersRepository,
    private val complaintAcceptWorkFlowRepository: IComplaintAcceptWorkFlowRepository,
    private val complaintsWitnessRepository: IComplaintWitnessRepository,
    private val complaintCompanyRepository: IComplaintCompanyRepository,
    private val complaintCustomersRepository: IComplaintCustomersRepository,
    private val reportsObservationsRepository: IReportObservationsRepository

) {

    fun saveComplaintReportObservations(sr: ServiceRequestsEntity, reportObservationsEntity: ReportObservationsEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {
            var observationsEntity = reportObservationsEntity
            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(observationsEntity) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())
            }

            observationsEntity = reportsObservationsRepository.save(observationsEntity)
            sr.payload = observationsEntity.toString()
            KotlinLogging.logger { }.trace("${this::saveComplaintReportObservations.name} saved with id =[${observationsEntity.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveComplaintCustomer(sr: ServiceRequestsEntity, complaintCustomersEntity: ComplaintCustomersEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {


            var customersEntity = complaintCustomersEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(customersEntity) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            customersEntity = complaintCustomersRepository.save(customersEntity)
            sr.payload = customersEntity.toString()
            KotlinLogging.logger { }.trace("${this::saveComplaintReportObservations.name} saved with id =[${customersEntity.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveComplaintCompany(sr: ServiceRequestsEntity, complaintCompanyEntity: ComplaintCompanyEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {


            var companyEntity = complaintCompanyEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(companyEntity) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            companyEntity = complaintCompanyRepository.save(companyEntity)
            sr.payload = companyEntity.toString()
            KotlinLogging.logger { }.trace("${this::saveComplaintReportObservations.name} saved with id =[${companyEntity.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveComplaintWitness(sr: ServiceRequestsEntity, witnessEntity: ComplaintWitnessEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {


            var witness = witnessEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(witness) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            witness = complaintsWitnessRepository.save(witness)
            sr.payload = witness.toString()
            KotlinLogging.logger { }.trace("${this::saveComplaintReportObservations.name} saved with id =[${witness.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }


    fun saveComplaintAcceptWorkFlow(sr: ServiceRequestsEntity, acceptWorkFlowEntity: ComplaintAcceptWorkFlowEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {

            sr.payload = acceptWorkFlowEntity.toString()
            var acceptWorkFlow = acceptWorkFlowEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(acceptWorkFlow) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            acceptWorkFlow = complaintAcceptWorkFlowRepository.save(acceptWorkFlow)
            KotlinLogging.logger { }.trace("${this::saveComplaintReportObservations.name} saved with id =[${acceptWorkFlow.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveComplainthandlers(sr: ServiceRequestsEntity, handlersEntity: ComplaintHandlersEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {

            sr.payload = handlersEntity.toString()
            var handler = handlersEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(handler) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            handler = complaintHandlersRepository.save(handler)
            KotlinLogging.logger { }.trace("${this::saveComplaintReportObservations.name} saved with id =[${handler.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveComplaintRemedies(sr: ServiceRequestsEntity, remediesEntity: ComplaintRemediesEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {

            sr.payload = remediesEntity.toString()
            var remedy = remediesEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(remedy) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            remedy = complaintRemediesRepo.save(remedy)
            KotlinLogging.logger { }.trace("${this::saveComplaintReportObservations.name} saved with id =[${remedy.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveKebsComplaintRemarks(sr: ServiceRequestsEntity, kebsRemarksEntity: ComplaintKebsRemarksEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {

            sr.payload = kebsRemarksEntity.toString()
            var kebsRemarks = kebsRemarksEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(kebsRemarks) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            kebsRemarks = complaintsKebsRemarksRepository.save(kebsRemarks)
            KotlinLogging.logger { }.trace("${this::saveKebsComplaintRemarks.name} saved with id =[${kebsRemarks.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveComplaintRemarks(sr: ServiceRequestsEntity, remarksEntity: ComplaintRemarksEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {

            sr.payload = remarksEntity.toString()
            var remarks = remarksEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(remarks) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            remarks = complaintRemarksRepo.save(remarks)
            KotlinLogging.logger { }.trace("${this::saveComplaintRemarks.name} saved with id =[${remarks.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun saveComplaintDocuments(sr: ServiceRequestsEntity, documentsEntity: ComplaintDocumentsEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {

            sr.payload = documentsEntity.toString()
            var documents = documentsEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(documents) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            documents = complaintsDocRepo.save(documents)
            KotlinLogging.logger { }.trace("${this::saveComplaintDocuments.name} saved with id =[${documents.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun assignComplaintToOfficer(sr: ServiceRequestsEntity, officerEntity: ComplaintOfficerEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {

            sr.payload = officerEntity.toString()
            var officer = officerEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(officer) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            officer = complaintsOfficerRepo.save(officer)
            KotlinLogging.logger { }.trace("${this::assignComplaintToOfficer.name} saved with id =[${officer.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }

        return log.transactionStatus
    }

    fun registerComplaint(sr: ServiceRequestsEntity, complaintEntity: ComplaintEntity, workFlowId: Long): Int {
        var log = WorkflowTransactionsEntity()
        try {

            sr.payload = complaintEntity.toString()
            var complaint = complaintEntity

            with(log) {
                transactionDate = Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = sr
                retried = 0
                transactionStatus = sr.serviceMapsId?.initStatus!!
                createdBy = "${sr.id}_${sr.transactionReference}"
                createdOn = Timestamp.from(Instant.now())
                workflowRepo.findByIdOrNull(workFlowId)
                    ?.let { workflowsEntity -> methodId = workflowsEntity }
                transactionReference = "${sr.transactionReference}_${methodId?.methodName}"

            }



            with(complaint) {
                transactionDate = Date(Date().time)
                status = sr.serviceMapsId?.activeStatus
                createdBy = log.transactionReference
                createdOn = Timestamp.from(Instant.now())


            }
            complaint = complaintsRepo.save(complaint)
            KotlinLogging.logger { }.trace("${this::registerComplaint.name} saved with id =[${complaint.id}] ")

        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            log.responseMessage = e.message
            log.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            log.transactionStatus = sr.serviceMapsId?.exceptionStatus!!
        }
        log.transactionCompletedDate = Timestamp.from(Instant.now())

        try {
            serviceRequestsRepository.save(sr)
            log = workflowTransactionsRepository.save(log)
        } catch (e: Exception) {
            KotlinLogging.logger { }.trace(e.message, e)
            KotlinLogging.logger { }.error(e.message)

        }


        return log.transactionStatus
    }
}