package org.kebs.app.kotlin.apollo.api.controllers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SftpServiceImpl
import org.kebs.app.kotlin.apollo.api.utils.RestResponseModel
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileInputStream

import org.xhtmlrenderer.util.GeneralUtil.inputStreamToString




@RestController
@RequestMapping("/api/v1/sftp/")
class SftpController {

    @Autowired
    lateinit var sftpServiceImpl: SftpServiceImpl

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var consignmentDocumentDaoService: ConsignmentDocumentDaoService

    @Autowired
    lateinit var iDFDaoService: IDFDaoService

    @Autowired
    lateinit var declarationDaoService: DeclarationDaoService

    @Autowired
    lateinit var manifestDaoService: ManifestDaoService

    @GetMapping("/kesws/download")
    fun downloadKeswsFiles(): ResponseEntity<RestResponseModel> {
        //TODO: Fetch DocTypes from the DB
//        val keswsDocTypes = listOf(applicationMapProperties.mapKeswsCdDoctype, applicationMapProperties.mapKeswsBaseDocumentDoctype,
//            applicationMapProperties.mapKeswsUcrResDoctype, applicationMapProperties.mapKeswsDeclarationDoctype, applicationMapProperties.mapKeswsManifestDoctype)
        val keswsDocTypes = listOf(applicationMapProperties.mapKeswsCdDoctype)

        for (doctype in keswsDocTypes) {
            when(doctype) {
                applicationMapProperties.mapKeswsBaseDocumentDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsBaseDocumentDoctype)
                    KotlinLogging.logger { }.info("No of Base Documents found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        val baseDocumentResponse: BaseDocumentResponse = commonDaoServices.deserializeFromXML(xml)
                        val docSaved = iDFDaoService.mapBaseDocumentToIDF(baseDocumentResponse)
                        if (docSaved) { sftpServiceImpl.moveFileToProcessedFolder(file) }
                    }
                }
                applicationMapProperties.mapKeswsUcrResDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsUcrResDoctype)
                    KotlinLogging.logger { }.info("No of UCR Response files found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        val ucrNumberMessage: UCRNumberMessage = commonDaoServices.deserializeFromXML(xml)
                        val baseDocRefNo = ucrNumberMessage.data?.dataIn?.sadId
                        val ucrNumber = ucrNumberMessage.data?.dataIn?.ucrNumber
                        if (baseDocRefNo == null || ucrNumber == null) {
                            KotlinLogging.logger { }.error { "BaseDocRef Number or UcrNumber missing" }
                            throw Exception("BaseDocRef Number or UcrNumber missing")
                        }
                        val idfUpdated = iDFDaoService.updateIdfUcrNumber(baseDocRefNo, ucrNumber)
                        if (idfUpdated) { sftpServiceImpl.moveFileToProcessedFolder(file) }
                    }
                }
                applicationMapProperties.mapKeswsDeclarationDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsDeclarationDoctype)
                    KotlinLogging.logger { }.info("No of Declaration Documents found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        val declarationDocumentMessage: DeclarationDocumentMessage = commonDaoServices.deserializeFromXML(xml)
                        val docSaved = declarationDaoService.mapDeclarationMessageToEntities(declarationDocumentMessage)
                        if (docSaved) { sftpServiceImpl.moveFileToProcessedFolder(file) }
                    }
                }
                applicationMapProperties.mapKeswsManifestDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsManifestDoctype)
                    KotlinLogging.logger { }.info("No of Manifest Documents found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        val manifestDocumentMessage: ManifestDocumentMessage = commonDaoServices.deserializeFromXML(xml)
                        val docSaved = manifestDaoService.mapManifestMessageToManifestEntity(manifestDocumentMessage)
                        if (docSaved) { sftpServiceImpl.moveFileToProcessedFolder(file) }
                    }
                }
                applicationMapProperties.mapKeswsCdDoctype -> {
                    val allFiles = sftpServiceImpl.downloadFilesByDocType(applicationMapProperties.mapKeswsCdDoctype)
                    KotlinLogging.logger { }.info("No of Consignment Documents found in bucket: ${allFiles.size}")
                    for (file in allFiles) {
                        val xml = inputStreamToString(FileInputStream(file))
                        val stringToExclude = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        val consignmentDoc: ConsignmentDocument = commonDaoServices.deserializeFromXML(xml, stringToExclude)
                        consignmentDocumentDaoService.insertConsignmentDetailsFromXml(consignmentDoc, xml.toByteArray())
                        sftpServiceImpl.moveFileToProcessedFolder(file)
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(RestResponseModel(HttpStatus.OK.value(), "Files successfully Downloaded"));
    }
}
