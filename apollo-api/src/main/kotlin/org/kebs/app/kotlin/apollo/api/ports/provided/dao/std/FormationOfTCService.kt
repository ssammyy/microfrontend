package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.repository.Deployment
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.DecisionFeedbackRepository
import org.kebs.app.kotlin.apollo.store.repo.std.JustificationForTCRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardsDocumentsRepository
import org.kebs.app.kotlin.apollo.store.repo.std.TechnicalCommitteeRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*

@Service
class FormationOfTCService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    @Qualifier("processEngine") private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val justificationForTCRepository: JustificationForTCRepository,
    private val decisionFeedbackRepository: DecisionFeedbackRepository,
    private val commonDaoServices: CommonDaoServices,
    private val technicalCommitteeRepository: TechnicalCommitteeRepository,
    private val sdDocumentsRepository: StandardsDocumentsRepository,
    private val draftDocumentService: DraftDocumentService,

    ) {

    val PROCESS_DEFINITION_KEY = "sd_formation_of_technical_committee"
    val TASK_SPC = "SPC"
    val TASK_SAC = "SAC"

    fun deployProcessDefinition(): Deployment = repositoryService
        .createDeployment()
        .addClasspathResource("processes/std/formation_of_technical_committee.bpmn20.xml")
        .deploy()

    fun submitJustificationForFormationOfTC(justificationForTC: JustificationForTC): ProcessInstanceResponseValue {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        justificationForTC.createdOn = Timestamp(System.currentTimeMillis())
        justificationForTC.createdBy = loggedInUser.id
        justificationForTC.version = "1"
        justificationForTC.status = 1  //uploaded awaiting decision
        justificationForTCRepository.save(justificationForTC)
        return ProcessInstanceResponseValue(
            justificationForTC.id,
            "Complete",
            true,
            justificationForTC.version ?: throw NullValueNotAllowedException("ID is required")
        )

    }

    fun getAllJustifications(): List<JustificationForTC> {
        return justificationForTCRepository.findAll()
    }

    fun getAllHofJustifications(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(1)
    }


    fun approveJustification(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 2   //approved
        u.hofId = loggedInUser.id
        u.hofReviewDate = Timestamp(System.currentTimeMillis())
        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Approved", "Justification Approved."
        )
    }

    fun rejectJustification(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 3   //rejected
        u.hofId = loggedInUser.id
        u.hofReviewDate = Timestamp(System.currentTimeMillis())
        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Rejected", "Justification Rejected. Sent Back To Tc-Sec"
        )
    }

    fun getAllSpcJustifications(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(2)
    }

    fun getAllJustificationsRejectedBySpc(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(3)
    }


    fun approveJustificationSPC(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 4   //approved by SPC
        u.spcId = loggedInUser.id
        u.spcReviewDate = Timestamp(System.currentTimeMillis())

        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Approved", "Justification Approved."
        )
    }

    fun rejectJustificationSPC(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 5   //rejected by SPC
        u.spcId = loggedInUser.id
        u.spcReviewDate = Timestamp(System.currentTimeMillis())

        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Rejected", "Justification Rejected."
        )
    }


    fun sacGetAllApprovedJustificationsBySpc(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(4)
    }

    fun sacGetAllRejectedJustificationsBySpc(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(5)
    }

    fun approveJustificationSAC(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 6   //approved by SAC
        u.sacId = loggedInUser.id
        u.sacReviewDate = Timestamp(System.currentTimeMillis())
        u.tcNumber = generateTCNumber("KEBS")
        justificationForTCRepository.save(u)

        val tc = TechnicalCommittee()
        tc.departmentId = u.departmentId!!
        tc.title = u.nameOfTC
        tc.createdOn = Timestamp(System.currentTimeMillis())
        tc.status = 1.toString()
        tc.createdBy = loggedInUser.id.toString()
        tc.technicalCommitteeNo = u.tcNumber

        technicalCommitteeRepository.save(tc)
        return ServerResponse(
            HttpStatus.OK,
            "Success", "Technical Committee Saved."
        )
    }

    fun rejectJustificationSAC(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 7   //rejected by SAC
        u.spcId = loggedInUser.id
        u.spcReviewDate = Timestamp(System.currentTimeMillis())

        justificationForTCRepository.save(u)
        return ServerResponse(
            HttpStatus.OK,
            "Rejected", "Justification Rejected."
        )
    }

    fun sacGetAllForWebsite(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(6)
    }

    fun sacGetAllRejected(): List<JustificationForTC> {
        return justificationForTCRepository.findAllByStatus(7)
    }


    fun advertiseTcToWebsite(justificationForTC: JustificationForTC): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val u: JustificationForTC = justificationForTCRepository.findById(justificationForTC.id).orElse(null)
        u.status = 8   //advertised To Website
        justificationForTCRepository.save(u)

//       find technical committee
        val tc: TechnicalCommittee = technicalCommitteeRepository.findByTechnicalCommitteeNo(u.tcNumber)!!
        //update technical committee for advertising
        tc.advertisingStatus = "1"
        technicalCommitteeRepository.save(tc)
        return ServerResponse(
            HttpStatus.OK,
            "Success", "Technical Committee Advertised To Website."
        )
    }

    fun generateTCNumber(departmentAbbrv: String?): String {
        val allRequests = technicalCommitteeRepository.findAllByOrderByIdDesc()
        var lastId: String? = "0"
        var finalValue = 1
        for (item in allRequests) {
            lastId = item.id.toString()
            break
        }
        if (lastId != "0") {
            finalValue = (lastId?.toInt()!!)
            finalValue += 1
        }
        val year = Calendar.getInstance()[Calendar.YEAR]
        return "$departmentAbbrv/TC/$finalValue:$year"
    }


    fun uploadAdditionalDocumentsForProposal(
        uploads: DatKebsSdStandardsEntity,
        docFile: MultipartFile,
        doc: String,
        nwi: Long,
        DocDescription: String
    ): DatKebsSdStandardsEntity {

        with(uploads) {
//            filepath = docFile.path
            name = commonDaoServices.saveDocuments(docFile)
//            fileType = docFile.contentType
            fileType = docFile.contentType
            documentType = doc
            description = DocDescription
            document = docFile.bytes
            transactionDate = commonDaoServices.getCurrentDate()
            status = 1
            sdDocumentId = nwi
            createdBy = commonDaoServices.concatenateName(commonDaoServices.loggedInUserDetails())
            createdOn = commonDaoServices.getTimestamp()
        }


        return sdDocumentsRepository.save(uploads)
    }

    fun getDocuments(proposalId: Long): Collection<DatKebsSdStandardsEntity?>? {

        return draftDocumentService.findUploadedDIFileBYIdAndType(proposalId, "Formation Of Tc Additional Documents")

    }

}
