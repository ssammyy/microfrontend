package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaUploadsEntity
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("api/v1/migration/qa")
class QualityAssuranceJSONControllers(
    private val applicationMapProperties: ApplicationMapProperties,
    private val qaDaoServices: QADaoServices,
    private val notifications: Notifications,
    private val commonDaoServices: CommonDaoServices,
) {

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    @PostMapping("/permit/apply/sta3-update-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQASTA3Permit(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

        docFile.forEach { u ->
            val upload = QaUploadsEntity()
            with(upload) {
                sta3Status = 1
                ordinaryStatus = 0
            }
            qaDaoServices.uploadQaFile(
                upload,
                commonDaoServices.convertMultipartFileToFile(u),
                "STA3-UPLOADS",
                permitDetails.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                loggedInUser
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return sm
//        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)
    }

    @PostMapping("/permit/apply/sta10-update-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQASTA10Permit(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

        docFile.forEach { u ->
            val upload = QaUploadsEntity()
            with(upload) {
                sta10Status = 1
                ordinaryStatus = 0
            }
            qaDaoServices.uploadQaFile(
                upload,
                commonDaoServices.convertMultipartFileToFile(u),
                "STA10-UPLOADS",
                permitDetails.permitRefNumber ?: throw Exception("MISSING PERMIT REF NUMBER"),
                loggedInUser
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return sm
//        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)
    }


    @PostMapping("/kebs/add/new-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQA(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("manufactureNonStatus") manufactureNonStatus: Int,
        @RequestParam("ordinaryStatus") ordinaryStatus: Int,
        @RequestParam("inspectionReportStatus") inspectionReportStatus: Int?,
        @RequestParam("sta10Status") sta10Status: Int?,
        @RequestParam("sscUploadStatus") sscUploadStatus: Int?,
        @RequestParam("scfStatus") scfStatus: Int?,
        @RequestParam("ssfStatus") ssfStatus: Int?,
        @RequestParam("cocStatus") cocStatus: Int?,
        @RequestParam("assessmentReportStatus") assessmentReportStatus: Int?,
        @RequestParam("labResultsStatus") labResultsStatus: Int?,
        @RequestParam("docFileName") docFileName: String,
        @RequestParam("doc_file") docFile: MultipartFile,
        @RequestParam("assessment_recommendations") assessmentRecommendations: String?,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

//        val result: ServiceRequestsEntity?
        val uploads = QaUploadsEntity()
        var versionNumber: Long = 1
        var uploadResults: Pair<ServiceRequestsEntity, QaUploadsEntity>? = null

        when (ordinaryStatus) {
            map.activeStatus -> {
                uploads.ordinaryStatus = ordinaryStatus
                uploadResults = qaDaoServices.saveQaFileUploads(
                    docFile,
                    docFileName,
                    loggedInUser,
                    map,
                    uploads,
                    permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                    versionNumber,
                    manufactureNonStatus
                )
            }
            map.inactiveStatus -> {
                uploads.ordinaryStatus = ordinaryStatus
                when {
                    cocStatus != null -> {
                        uploads.cocStatus = cocStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndCocStatus(
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            versionNumber,
                            manufactureNonStatus
                        )
                        permitDetails.cocId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusCocUploaded,
                            loggedInUser
                        )

                    }
                    sscUploadStatus != null -> {
                        uploads.sscStatus = sscUploadStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndSscStatus(
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            versionNumber,
                            manufactureNonStatus
                        )
                        permitDetails.generateSchemeStatus = map.activeStatus
                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPApprSSC,
                            loggedInUser
                        )

                    }
                    assessmentReportStatus != null -> {
                        uploads.assessmentReportStatus = assessmentReportStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndAssessmentReportStatus(
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            versionNumber,
                            manufactureNonStatus
                        )

                        val hodDetails = qaDaoServices.assignNextOfficerAfterPayment(
                            permitDetails,
                            map,
                            applicationMapProperties.mapQADesignationIDForHODId
                        )


                        with(permitDetails) {
                            assessmentScheduledStatus = map.successStatus
                            assessmentReportRemarks = assessmentRecommendations
                            hodId = hodDetails?.id
                            permitStatus = applicationMapProperties.mapQaStatusPApprovalAssesmentReport
                            userTaskId = applicationMapProperties.mapUserTaskNameHOD
                        }
                        qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser)

                        //Send notification to PAC secretary
                        val hodSec = hodDetails?.id?.let { commonDaoServices.findUserByID(it) }
                        hodSec?.email?.let { qaDaoServices.sendPacDmarkAssessmentNotificationEmail(it, permitDetails) }

//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
//                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
//                        qaDaoServices.permitInsertStatus(
//                            permitDetails,
//                            applicationMapProperties.mapQaStatusPApprSSC,
//                            loggedInUser
//                        )

                    }
                    inspectionReportStatus != null -> {
                        uploads.inspectionReportStatus = inspectionReportStatus
//                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndSscStatus(permitID, map.activeStatus).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            versionNumber,
                            manufactureNonStatus
                        )
//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
//                        qaDaoServices.permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPApprSSC, loggedInUser)

                    }
                    sta10Status != null -> {
                        uploads.sta10Status = sta10Status
//                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndSscStatus(permitID, map.activeStatus).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                            versionNumber,
                            manufactureNonStatus
                        )
//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
//                        qaDaoServices.permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPApprSSC, loggedInUser)

                    }

                }
            }
        }

//        result = uploadResults?.first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return sm
//        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)
    }

}
