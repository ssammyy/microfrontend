package org.kebs.app.kotlin.apollo.api.controllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SftpServiceImpl
import org.kebs.app.kotlin.apollo.api.utils.RestResponseModel
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.xhtmlrenderer.util.GeneralUtil.inputStreamToString
import java.io.FileInputStream




@RestController
@RequestMapping("/api/v1/sftp/")
class SftpController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val sftpServiceImpl: SftpServiceImpl,
    private val commonDaoServices: CommonDaoServices,
    private val consignmentDocumentDaoService: ConsignmentDocumentDaoService,
    private val iDFDaoService: IDFDaoService,
    private val declarationDaoService: DeclarationDaoService,
    private val manifestDaoService: ManifestDaoService,
    private val destinationInspectionDaoServices: DestinationInspectionDaoServices
) {

    val processedRootFolder = applicationMapProperties.mapSftpProcessedRoot
    val unprocessableRootFolder = applicationMapProperties.mapSftpUnprocessableRoot

    @GetMapping("/kesws/download")
    fun downloadKeswsFiles(): ResponseEntity<RestResponseModel> {
        //TODO: Fetch DocTypes from the DB
        val keswsDocTypes = listOf(applicationMapProperties.mapKeswsBaseDocumentDoctype, applicationMapProperties.mapKeswsUcrResDoctype,
            applicationMapProperties.mapKeswsDeclarationDoctype, applicationMapProperties.mapKeswsManifestDoctype, applicationMapProperties.mapKeswsAirManifestDoctype,
            applicationMapProperties.mapKeswsCdDoctype, applicationMapProperties.mapKeswsDeclarationVerificationDoctype)

        for (doctype in keswsDocTypes) {
            when(doctype) {
                applicationMapProperties.mapKeswsBaseDocumentDoctype -> {
                    val allFiles =
                        sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsBaseDocumentDoctype)
                    KotlinLogging.logger { }.info("No of Base Documents found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        KotlinLogging.logger { }.info("Deserializing IDF Doc: ${file.name}")
                        var baseDocumentResponse: BaseDocumentResponse? = null
                        try {
                            baseDocumentResponse = commonDaoServices.deserializeFromXML(xml)
                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error("An error occurred while deserializing ${file.name}", e)
                            sftpServiceImpl.moveFileToProcessedFolder(file, unprocessableRootFolder)
                        }
                        if (baseDocumentResponse != null) {
                            val docSaved = iDFDaoService.mapBaseDocumentToIDF(baseDocumentResponse)
                            if (docSaved != null) {
                                sftpServiceImpl.moveFileToProcessedFolder(file, processedRootFolder)
                            }
                        }
                    }
                }
                applicationMapProperties.mapKeswsUcrResDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsUcrResDoctype)
                    KotlinLogging.logger { }.info("No of UCR Response files found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        KotlinLogging.logger { }.info("Deserializing UCR Doc: ${file.name}")
                        var ucrNumberMessage: UCRNumberMessage? = null
                        try {
                            ucrNumberMessage = commonDaoServices.deserializeFromXML(xml)
                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error("An error occurred while deserializing ${file.name}", e)
                        }
                        if (ucrNumberMessage == null) {
                            sftpServiceImpl.moveFileToProcessedFolder(file, unprocessableRootFolder)
                        } else {
                            val baseDocRefNo = ucrNumberMessage.data?.dataIn?.sadId
                            val ucrNumber = ucrNumberMessage.data?.dataIn?.ucrNumber
                            if (baseDocRefNo == null || ucrNumber == null) {
                                KotlinLogging.logger { }.error { "BaseDocRef Number or UcrNumber missing" }
                                throw Exception("BaseDocRef Number or UcrNumber missing")
                            }
                            val idfUpdated = iDFDaoService.updateIdfUcrNumber(baseDocRefNo, ucrNumber)
                            if (idfUpdated) { sftpServiceImpl.moveFileToProcessedFolder(file, processedRootFolder) }
                        }
                    }
                }
                applicationMapProperties.mapKeswsDeclarationDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsDeclarationDoctype)
                    KotlinLogging.logger { }.info("No of Declaration Documents found in bucket: ${allFiles.size}")
                    var declarationDocumentMessage: DeclarationDocumentMessage? = null
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        try {
                            declarationDocumentMessage = commonDaoServices.deserializeFromXML(xml)
                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error("An error occurred while deserializing ${file.name}", e)
                            sftpServiceImpl.moveFileToProcessedFolder(file, unprocessableRootFolder)
                        }
                        if (declarationDocumentMessage != null) {
                            val docSaved = declarationDaoService.mapDeclarationMessageToEntities(declarationDocumentMessage)
                            if (docSaved) { sftpServiceImpl.moveFileToProcessedFolder(file, processedRootFolder) }
                        }
                    }
                }
                applicationMapProperties.mapKeswsManifestDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsManifestDoctype)
                    KotlinLogging.logger { }.info("No of Manifest Documents found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        var manifestDocumentMessage: ManifestDocumentMessage? = null
                        val xml = inputStreamToString(FileInputStream(file))
                        try {
                            manifestDocumentMessage = commonDaoServices.deserializeFromXML(xml)
                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error("An error occurred while deserializing ${file.name}", e)
                            sftpServiceImpl.moveFileToProcessedFolder(file, unprocessableRootFolder)
                        }
                        if (manifestDocumentMessage != null) {
                            val docSaved = manifestDaoService.mapManifestMessageToManifestEntity(manifestDocumentMessage)
                            if (docSaved) { sftpServiceImpl.moveFileToProcessedFolder(file, processedRootFolder) }
                        }
                    }
                }
                applicationMapProperties.mapKeswsAirManifestDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsAirManifestDoctype)
                    KotlinLogging.logger { }.info("No of Air Manifest Documents found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        var manifestDocumentMessage: ManifestDocumentMessage? = null
                        val xml = inputStreamToString(FileInputStream(file))
                        try {
                            manifestDocumentMessage = commonDaoServices.deserializeFromXML(xml)
                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error("An error occurred while deserializing ${file.name}", e)
                            sftpServiceImpl.moveFileToProcessedFolder(file, unprocessableRootFolder)
                        }
                        if (manifestDocumentMessage != null) {
                            val docSaved = manifestDaoService.mapManifestMessageToManifestEntity(manifestDocumentMessage)
                            if (docSaved) { sftpServiceImpl.moveFileToProcessedFolder(file, processedRootFolder) }
                        }
                    }
                }
                applicationMapProperties.mapKeswsCdDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsCdDoctype)
                    KotlinLogging.logger { }.info("No of Consignment Documents found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        var consignmentDoc: ConsignmentDocument? = null
                        val xml = inputStreamToString(FileInputStream(file))
                        val stringToExclude = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        try {
                            consignmentDoc = commonDaoServices.deserializeFromXML(xml, stringToExclude)
                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error("An error occurred while deserializing ${file.name}", e)
                            sftpServiceImpl.moveFileToProcessedFolder(file, unprocessableRootFolder)
                        }
                        if (consignmentDoc != null) {
                            try {
                                consignmentDocumentDaoService.insertConsignmentDetailsFromXml(consignmentDoc, xml.toByteArray())
                            } catch (e: Exception) {
                                KotlinLogging.logger { }.error("An error occurred while saving CD details ${file.name}", e)
                                sftpServiceImpl.moveFileToProcessedFolder(file, unprocessableRootFolder)
                            }
                            sftpServiceImpl.moveFileToProcessedFolder(file, processedRootFolder)
                        }
                    }
                }
                applicationMapProperties.mapKeswsDeclarationVerificationDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsDeclarationVerificationDoctype)
                    KotlinLogging.logger { }.info("No of Declaration Verification Documents found in bucket: ${allFiles.size}")
                    var declarationVerificationDocumentMessage: DeclarationVerificationMessage? = null
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        try {
                            declarationVerificationDocumentMessage = commonDaoServices.deserializeFromXML(xml)
                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error("An error occurred while deserializing ${file.name}", e)
                            sftpServiceImpl.moveFileToProcessedFolder(file, unprocessableRootFolder)
                        }
                        if (declarationVerificationDocumentMessage != null) {
                            destinationInspectionDaoServices.updateCdVerificationSchedule(declarationVerificationDocumentMessage)
                            sftpServiceImpl.moveFileToProcessedFolder(file, processedRootFolder)
                        }
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(RestResponseModel(HttpStatus.OK.value(), "Files successfully Downloaded"));
    }
}
