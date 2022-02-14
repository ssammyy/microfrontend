package org.kebs.app.kotlin.apollo.api.ports.provided.dao


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.UpAndDownLoad
import org.kebs.app.kotlin.apollo.api.utils.Delimiters
import org.kebs.app.kotlin.apollo.api.utils.XMLDocument
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ProcessesStagesEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader

@Service
class ConsignmentDocumentDaoService(
        private val applicationMapProperties: ApplicationMapProperties,
        private val commonDaoServices: CommonDaoServices,
        private val daoServices: DestinationInspectionDaoServices,
        private val cdFileXMLRepo: ICdFileXmlRepository,
        private val iCdTransportRepo: ICdTransportEntityRepository,
        private val iCdConsignorRepo: ICdConsignorEntityRepository,
        private val iCdServiceProviderRepo: ICdServiceProviderEntityRepository,
        private val iCdStandardsTwoRepo: ICdStandardsTwoEntityRepository,
        private val iConsignmentDocumentDetailsRepo: IConsignmentDocumentDetailsRepository,
        private val iCdStandardsRepo: ICdStandardsEntityRepository,
        private val iCdImporterRepo: ICdImporterEntityRepository,
        private val iCdItemsRepo: IConsignmentItemsRepository,
        private val iCdItemsCurrierRepo: ICdItemsCurrierRepository,
        private val iCdItemNonStandardEntityRepository: ICdItemNonStandardEntityRepository,
        private val iCdApplicantDefinedThirdPartyDetailsRepository: ICdApplicantDefinedThirdPartyDetailsRepository,
        private val iCdApprovalHistoryRepository: ICdApprovalHistoryRepository,
        private val iCdContainerRepository: ICdContainerRepository,
        private val iCdDocumentFeeRepository: ICdDocumentFeeRepository,
//        private val iCdDocumentHeaderRepository: ICdDocumentHeaderRepository,
        private val iCdHeaderTwoDetailsRepository: ICdHeaderTwoDetailsRepository,
        private val iCdPgaHeaderFieldsRepository: ICdPgaHeaderFieldsRepository,
        private val iCdItemCommodityDetailsRepository: ICdItemCommodityDetailsRepository,
        private val iCdProcessingFeeRepository: ICdProcessingFeeRepository,
        private val iCdProducts2EndUserDetailsRepository: ICdProducts2EndUserDetailsRepository,
        private val iCdProducts2Repository: ICdProducts2Repository,
        private val iCdRiskDetailsActionDetailsRepository: ICdRiskDetailsActionDetailsRepository,
        private val iCdRiskDetailsAssessmentRepository: ICdRiskDetailsAssessmentRepository,
        private val iCdThirdPartyDetailsRepository: ICdThirdPartyDetailsRepository,

        private val iCdValuesHeaderLevelRepo: ICdValuesHeaderLevelEntityRepository,
        private val iCdConsigneeRepo: ICdConsigneeEntityRepository,
        private val iCdExporterRepo: ICdExporterEntityRepository,
        private val upAndDownLoad: UpAndDownLoad
) {

    fun cdUpload() {
        upAndDownLoad.upload(applicationMapProperties.mapKeswsConfigIntegration)
    }

    fun cdDownload() {
        val download = upAndDownLoad.download(applicationMapProperties.mapKeswsConfigIntegration)

        val downLoadedToString = String(download)
        KotlinLogging.logger { }.info { "Downloaded text = $downLoadedToString " }
        val stringToExclude = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        val consignmentDoc: ConsignmentDocument =
                commonDaoServices.deserializeFromXML(downLoadedToString, stringToExclude)

        this.insertConsignmentDetailsFromXml(consignmentDoc, download)
    }

    fun insertConsignmentDetailsFromXml(consignmentDoc: ConsignmentDocument, byteArray: ByteArray) {
        val appId = applicationMapProperties.mapKesws
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.findUserByUserName(applicationMapProperties.mapSFTPUserName)
        try {
            consignmentDoc.documentDetails?.consignmentDocDetails?.cdStandard?.ucrNumber?.let { ucr ->
                val docSummary =
                        consignmentDoc.documentSummaryResponse
                                ?: throw ExpectedDataNotFound("Document Summary, does not exist")
                daoServices.findCdWithUcrNumberLatest(ucr).let { CDDocumentDetails ->

                    when (CDDocumentDetails) {
                        null -> {
                            KotlinLogging.logger { }
                                    .info { ":::::::::::::::::::::CD With UCR = $ucr, Does Not exist::::::::::::::::::::::::::: " }
                            consignmentDoc.documentDetails?.consignmentDocDetails?.let { consignmentDocDetails ->
                                mainCDFunction(
                                        consignmentDocDetails,
                                        docSummary,
                                        byteArray,
                                        loggedInUser,
                                        map,
                                        commonDaoServices.findProcesses(appId),
                                        1
                                )
                            }
                        }
                        else -> {
                            var cdDetails = CDDocumentDetails
                            KotlinLogging.logger { }.info { "::::::::::::::::::CD With UCR = $ucr, Exists::::::::::::::::::::: " }
                            consignmentDoc.documentDetails?.consignmentDocDetails?.let { consignmentDocDetails ->
                                val cdCreated = mainCDFunction(
                                        consignmentDocDetails,
                                        docSummary,
                                        byteArray,
                                        loggedInUser,
                                        map,
                                        commonDaoServices.findProcesses(appId),
                                        this.daoServices.getVersionCount(ucr) + 1
                                )
                                //Update Old CD with Status 1
                                with(cdDetails) {
                                    this?.oldCdStatus = map.activeStatus
                                    this?.approveRejectCdStatusType = null
                                }
                                cdDetails = daoServices.updateCdDetailsInDB(cdDetails!!, loggedInUser)
                                // Clear process data on new CD
                                cdCreated.diProcessStatus=0
                                cdCreated.diProcessStartedOn=null
                                cdCreated.diProcessCompletedOn=null
                                // Update details
                                daoServices.updateCdDetailsInDB(cdCreated, loggedInUser)
                            }
                        }
                    }


                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("An error occurred while saving CD details", e)
        }
    }

    fun updateCDDetails(consignmentDocument: String, statusToUpdateWith: String, dateToUpdateWith: String): Any {
        consignmentDocument.replace(applicationMapProperties.mapSftpDownloadFileReplaceCharacters, "")

        val xmlDoc: Document = XMLDocument.documentBuilder.parse(
                InputSource(StringReader(consignmentDocument))
        )
        xmlDoc.documentElement.normalize()
        val doc = XMLDocument(xmlDoc.documentElement)

        fun List<String>.toPath(): String {
            return "${Delimiters.pointerSeparator}" + this.joinToString(separator = "${Delimiters.pointerSeparator}")
        }

        //Todo : JUST in case there are so many fields need update create a function  that can loop a given list and replace with the given value
        val newStatus = doc.document.ownerDocument.createTextNode(statusToUpdateWith)
        val statusPath: String = applicationMapProperties.mapSftpDownloadFileReplaceStatusPath.split(",").toPath()
        doc.setNested(statusPath, XMLDocument(newStatus))

        val newDate = doc.document.ownerDocument.createTextNode(dateToUpdateWith)
        val datePath: String = applicationMapProperties.mapSftpDownloadFileReplaceDatePath.split(",").toPath()
        doc.setNested(datePath, XMLDocument(newDate))

        print(doc.toSerializable())

        return doc.toSerializable()
    }


    fun saveDownLoadedCDXmlFile(
            docFileByteArray: ByteArray,
            documentId: Long?,
            map: ServiceMapsEntity,
            user: UsersEntity,
            throwable: Throwable?,
    ): CdFileXmlEntity {
        var cdFileXml = CdFileXmlEntity()
        if (docFileByteArray.isEmpty()) {
            return cdFileXml
        }
        with(cdFileXml) {
            cdId = documentId
            name = applicationMapProperties.mapSftpUploadName
            fileType = commonDaoServices.getFileType(docFileByteArray)
            documentType = applicationMapProperties.mapKeswsDocType
            document = docFileByteArray
            fileErrors = throwable?.stackTraceToString()
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }

        cdFileXml = cdFileXMLRepo.save(cdFileXml)
        return cdFileXml
    }

    fun findSavedCDXmlFile(cdId: Long, status: Int): ByteArray {
        cdFileXMLRepo.findByCdIdAndStatus(cdId, status)
                ?.let { cdXmlFile ->
                    cdXmlFile.document?.let { return it }
                }
                ?: throw ExpectedDataNotFound("CD FILE XML WITH ID = $cdId and Status = $status, does not exist")

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun mainCDFunction(
            consignmentDocDetails: ConsignmentDocDetails,
            documentSummary: DocumentSummaryResponse,
            downloadByteArray: ByteArray,
            user: UsersEntity,
            map: ServiceMapsEntity,
            processesStages: ProcessesStagesEntity,
            versionNumber: Long
    ): ConsignmentDocumentDetailsEntity {
        var createdCDDetails =
                processesStages.process1?.let { createConsignmentDocumentDetails(user, map, it, versionNumber) }
        if (createdCDDetails != null) {
            createdCDDetails = consignmentDetails(createdCDDetails, consignmentDocDetails, documentSummary, user, map, processesStages)
        }
        return createdCDDetails ?: throw ExpectedDataNotFound("No CD Details EXISTING")
    }

    fun createConsignmentDocumentDetails(
            user: UsersEntity,
            map: ServiceMapsEntity,
            processStages: String,
            versionNumber: Long
    ): ConsignmentDocumentDetailsEntity {
        var consignmentDocumentDetails = ConsignmentDocumentDetailsEntity()
        with(consignmentDocumentDetails) {
            uuid = commonDaoServices.generateUUIDString()
            status = map.initStatus
            version = versionNumber
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        consignmentDocumentDetails = iConsignmentDocumentDetailsRepo.save(consignmentDocumentDetails)
        KotlinLogging.logger { }.info { "Consignment Document saved ID = ${consignmentDocumentDetails.id}" }

        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStages)
            }
        }

        return consignmentDocumentDetails
    }

    fun updateConsignmentDocumentDetails(
            consignmentDocumentDetails: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        with(consignmentDocumentDetails) {
            status = map.initStatus
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return iConsignmentDocumentDetailsRepo.save(consignmentDocumentDetails)
    }


    fun consignmentDetails(
            cdDetails: ConsignmentDocumentDetailsEntity,
            consignmentDocDetails: ConsignmentDocDetails,
            documentSummary: DocumentSummaryResponse,
            user: UsersEntity,
            map: ServiceMapsEntity,
            processesStages: ProcessesStagesEntity
    ): ConsignmentDocumentDetailsEntity? {

        var consignmentDocumentDetails: ConsignmentDocumentDetailsEntity? = null
        if (consignmentDocumentDetails == null) {
            consignmentDocumentDetails = cdDetails
        }

        //Add CD STANDARDS DETAILS
        consignmentDocDetails.cdStandard?.let {
            processesStages.process2?.let { it1 ->
                consignmentDocumentDetails = cdStandardsDetails(it, it1, cdDetails, user, map)
                if (consignmentDocumentDetails == null) {
                    consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                }
            }
        }

        //Add CD IMPORTER DETAILS
        consignmentDocDetails.cdImporter?.let {
            processesStages.process3?.let { it1 ->
                consignmentDocumentDetails?.let { it2 ->
                    consignmentDocumentDetails = importerCDDetails(it, it1, it2, user, map)
                    if (consignmentDocumentDetails == null) {
                        consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                    }
                }
            }
        }

        //Add CD TRANSPORT DETAILS
        consignmentDocDetails.cdTransport?.let {
            consignmentDocumentDetails?.let { it2 ->
                consignmentDocumentDetails = transportCDDetails(it, processesStages, it2, user, map)
                if (consignmentDocumentDetails == null) {
                    consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                }
            }
        }

        //Add CD EXPORTER DETAILS
        consignmentDocDetails.cdExporter?.let {
            processesStages.process5?.let { it1 ->
                consignmentDocumentDetails?.let { it2 ->
                    consignmentDocumentDetails = exporterCDDetails(it, it1, it2, user, map)
                    if (consignmentDocumentDetails == null) {
                        consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                    }
                }
            }
        }

        //Add CD PGA HEADER FILES DETAILS
        consignmentDocDetails.cdPGAHeaderFields?.let {
            processesStages.process14?.let { it1 ->
                consignmentDocumentDetails?.let { it2 ->
                    consignmentDocumentDetails = pgaHeaderFieldsCDDetails(it, it1, it2, user, map)
                    if (consignmentDocumentDetails == null) {
                        consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                    }
                }
            }
        }

        //Add CD CONSIGNEE DETAILS
        consignmentDocDetails.cdConsignee?.let {
            processesStages.process6?.let { it1 ->
                consignmentDocumentDetails?.let { it2 ->
                    consignmentDocumentDetails = consigneeCDDetails(it, it1, it2, user, map)
                    if (consignmentDocumentDetails == null) {
                        consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                    }
                }
            }
        }

        //Add CD CONSIGNOR DETAILS
        consignmentDocDetails.cdConsignor?.let {
            processesStages.process7?.let { it1 ->
                consignmentDocumentDetails?.let { it2 ->
                    consignmentDocumentDetails = consignorCDDetails(it, it1, it2, user, map)
                    if (consignmentDocumentDetails == null) {
                        consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                    }
                }
            }
        }

        //Add CD HEADER ONE DETAILS
        consignmentDocDetails.cdHeaderOne?.let {
            processesStages.process8?.let { it1 ->
                consignmentDocumentDetails?.let { it2 ->
                    consignmentDocumentDetails = valuesHeaderCDDetails(it, it1, it2, user, map)
                    if (consignmentDocumentDetails == null) {
                        consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                    }
                }
            }
        }

        //Add CD HEADER TWO DETAILS
        consignmentDocDetails.cdHeaderTwo?.let {
            processesStages.process15?.let { it1 ->
                consignmentDocumentDetails?.let { it2 ->
                    consignmentDocumentDetails = headerTwoCDDetails(it, it1, it2, user, map)
                    if (consignmentDocumentDetails == null) {
                        consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                    }
                }
            }
        }


        //Add CD STANDARDS TWO DETAILS
        consignmentDocDetails.cdStandardTwo?.let {
            consignmentDocumentDetails?.let { it2 ->
                consignmentDocumentDetails = cdStandardsTwoDetails(it, processesStages, it2, user, map)
                if (consignmentDocumentDetails == null) {
                    consignmentDocumentDetails = cdDetails.id?.let { cdID -> daoServices.findCD(cdID) }
                }
            }
        }


        //Add CD PRODUCT DETAILS
        consignmentDocDetails.cdProductDetails?.itemDetails
                ?.forEach { itemDetails ->
                    val itemCountDetails = itemDetails.itemCount
                    //Add CD ITEMS DETAILS DETAILS
                    val cdProduct1Details = itemDetails.cdProduct1
                    val itemDetail = consignmentDocumentDetails?.let {
                        cdProduct1Details?.let { it1 ->
                            itemCDDetails(
                                    it1,
                                    user,
                                    map,
                                    it
                            )
                        }
                    }

                    itemDetail?.let {
                        commonDaoServices.convertClassToJson(it)?.let {
                            consignmentDocumentDetails?.id?.let { it1 ->
                                processesStages.process10?.let { it2 ->
                                    daoServices.createCDTransactionLog(map, user, it1, it, it2)
                                }
                            }
                        }
                    }

                    //Add CD ITEM OTHER DETAILS DETAILS
                    val cdProducts2 = itemDetails.cdProduct2
                    val products2Details = cdProducts2?.let {
                        consignmentDocumentDetails?.let { it1 ->
                            cdProducts2Details(
                                    it,
                                    user,
                                    map,
                                    it1
                            )
                        }
                    }

                    //Add CD NON STANDARDS DETAILS
                    val nonStandards = itemDetail?.let {
                        itemDetails.cdItemNonStandard?.let { it1 ->
                            nonStandardItemCDDetails(
                                    it1,
                                    user,
                                    map,
                                    it
                            )
                        }
                    }

                    //Add CD STANDARDS DETAILS
                    val cdItemNonCommodity = itemDetails.cdItemCommodity
                    val itemNonCommodity = cdItemNonCommodity?.let { cdItemNonCommodityDetails(it, user, map) }

                    //Add CD CURRIER DETAILS FOR ALL VALUES ADDED ABOVE SECTION FOR ITEM  DETAILS
                    val itemCurrierDetails = CdItemsCurrierDetailsEntity()
                    with(itemCurrierDetails) {
                        itemId = itemDetail?.id
                        cdProductTwoId = products2Details?.id
                        cdId = consignmentDocumentDetails?.id
                        itemCount = itemCountDetails
                        cdItemNonStandardsId = nonStandards?.id
                        cdItemCommodityId = itemNonCommodity?.id

                    }
                    itemCurrier(itemCurrierDetails, map, user)
                }

        // Update Document Type Details and processing document
        consignmentDocumentDetails?.let {
            daoServices.updateCDDetailsWithCdType(consignmentDocDetails.cdStandardTwo?.localCocType ?: "L", it)
        }

        //Add CD APPROVAL HISTORY DETAILS DETAILS
        consignmentDocDetails.approvalDetails?.approvalHistory
                ?.forEach { approvalHistory ->
                    val approvalHistoryDetails =
                            consignmentDocumentDetails?.let { cdApprovalHistoryDetails(approvalHistory, user, map, it) }
                    if (approvalHistoryDetails != null) {
                        commonDaoServices.convertClassToJson(approvalHistoryDetails)?.let {
                            consignmentDocumentDetails?.id?.let { it1 ->
                                processesStages.process12?.let { it2 ->
                                    daoServices.createCDTransactionLog(map, user, it1, it, it2)

                                }
                            }
                        }
                    }
                }

        //Add CD RISK  DETAILS
        consignmentDocDetails.cdRiskDetailsResponse?.riskAssessmentResponse
                ?.forEach { riskResponse ->
                    val riskAssessmentDetails =
                            consignmentDocumentDetails?.let { cdRiskDetails(riskResponse, it, user, map) }
                    riskAssessmentDetails?.let {
                        commonDaoServices.convertClassToJson(it)?.let {
                            consignmentDocumentDetails?.id?.let { it1 ->
                                processesStages.process20?.let { it2 ->
                                    daoServices.createCDTransactionLog(map, user, it1, it, it2)

                                }
                            }
                        }
                    }
                }

        //Add CD UPDATE CD DETAILS WITH VALUES BELOW
        cdDetails.id?.let { cdID ->
            daoServices.findCD(cdID)
                    .let { fetchedCdDetails ->
                        with(fetchedCdDetails) {
                            ucrNumber = cdStandard?.ucrNumber
                            idfNumber = ucrNumber?.let { daoServices.findIdf(it)?.baseDocRefNo }
                            issuedDateTime = documentSummary.issuedDateTime
                            summaryPageURL = documentSummary.summaryPageURL

                            val transportDetails =
                                    fetchedCdDetails.cdTransport?.let { daoServices.findCdTransportDetails(it) }
                            val cdCfsEntity = transportDetails?.freightStation?.let { daoServices.findCfsCd(it) }
//                        val cdCfsAndUserCfs = cdCfsEntity?.id?.let { daoServices.findCfsUserFromCdCfs(it) }
//            val sectionL3 = cdCfsAndUserCfs?.userCfs?.let { daoServices.findFreightStation(it) }
//                        freightStation = cdCfsAndUserCfs?.userCfs
                            freightStation = cdCfsEntity
//                        val sectionsLevel2 = freightStation?.let { commonDaoServices.findSectionLevel2WIthId(it) }
//                        clusterId = sectionsLevel2?.subSectionLevel1Id?.id
//                        portOfArrival = sectionsLevel2?.sectionId?.id
                        }

                        val updatedConsignmentDocumentDetails =
                                updateConsignmentDocumentDetails(fetchedCdDetails, user, map)
                        commonDaoServices.convertClassToJson(updatedConsignmentDocumentDetails)?.let {
                            updatedConsignmentDocumentDetails.id?.let { it1 ->
                                processesStages.process11?.let { it2 ->
                                    daoServices.createCDTransactionLog(map, user, it1, it, it2)
                                }
                            }
                        }

                        KotlinLogging.logger { }
                                .info { "Updated Consignment Document saved ID = ${updatedConsignmentDocumentDetails.id}" }
                        consignmentDocumentDetails = updatedConsignmentDocumentDetails
                    }
        }
        return consignmentDocumentDetails
    }

    fun itemCurrier(cdItemsCurrierDetails: CdItemsCurrierDetailsEntity, map: ServiceMapsEntity, user: UsersEntity) {
        var itemCurrier = cdItemsCurrierDetails
        with(itemCurrier) {
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()

        }

        itemCurrier = iCdItemsCurrierRepo.save(itemCurrier)
        KotlinLogging.logger { }.info { "item Currier saved ID = ${itemCurrier.id}" }
    }

    fun cdStandardsDetails(
            cdStandardResponse: CDStandardResponse,
            processStage: String,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var standardsDetails = CdStandardsEntity()
        with(standardsDetails) {
            applicationTypeCode = cdStandardResponse.applicationTypeCode
            applicationTypeDescription = cdStandardResponse.applicationTypeDescription
            documentTypeCode = cdStandardResponse.documentTypeCode
            cmsDocumentTypeCode = cdStandardResponse.cmsDocumentTypeCode
            documentTypeDescription = cdStandardResponse.documentTypeDescription
            consignmentTypeCode = cdStandardResponse.consignmentTypeCode
            consignmentTypeDescription = cdStandardResponse.consignmentTypeDescription
            mdaCode = cdStandardResponse.mdaCode
            mdaDescription = cdStandardResponse.mdaDescription
            documentCode = cdStandardResponse.documentCode
            documentDescription = cdStandardResponse.documentDescription
            processCode = cdStandardResponse.processCode
            processDescription = cdStandardResponse.processDescription
            applicationDate = cdStandardResponse.applicationDate
            updatedDate = cdStandardResponse.updatedDate
            approvalStatus = cdStandardResponse.approvalStatus
            approvalDate = cdStandardResponse.approvalDate
            finalApprovalDate = cdStandardResponse.finalApprovalDate
            applicationRefNo = cdStandardResponse.applicationRefNo
            versionNo = cdStandardResponse.versionNo
            ucrNumber = cdStandardResponse.ucrNumber
            declarationNumber = cdStandardResponse.declarationNumber
            expiryDate = cdStandardResponse.expiryDate
            amendedDate = cdStandardResponse.amendedDate
            usedStatus = cdStandardResponse.usedStatus
            usedDate = cdStandardResponse.usedDate
            referencedPermitExemptionNo = cdStandardResponse.referencedPermitExemptionNo
            referencedPermitExemptionVersionNo = cdStandardResponse.referencedPermitExemptionVersionNo

            cdServiceProvider = cdStandardResponse.cdServiceProvider?.let { cdServicesProviderDetails(it, user, map) }

            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        standardsDetails = iCdStandardsRepo.save(standardsDetails)
        KotlinLogging.logger { }.info { "standards Details saved ID = ${standardsDetails.id}" }

        with(consignmentDocumentDetailsEntity) {
            cdStandard = standardsDetails
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStage)
            }
        }

        return consignmentDocumentDetails
    }

    fun cdRiskDetails(
            riskAssessmentResponse: RiskAssessmentResponse,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdRiskDetailsAssessmentEntity {
        var riskDetails = CdRiskDetailsAssessmentEntity()
        with(riskDetails) {
            cdId = consignmentDocumentDetailsEntity.id
            profileCode = riskAssessmentResponse.profileCode
            profileName = riskAssessmentResponse.profileName
            assessedLane = riskAssessmentResponse.assessedLane
            assessedDate = riskAssessmentResponse.assessedDate
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        riskDetails = iCdRiskDetailsAssessmentRepository.save(riskDetails)
        KotlinLogging.logger { }.info { "Risk Details saved ID = ${riskDetails.id}" }

        riskAssessmentResponse.actionDetails?.actionValues
                ?.forEach { riskActionValuesResponse ->
                    cdRiskActionDetails(riskActionValuesResponse, riskDetails, user, map)
                }

        return riskDetails
    }

    fun cdRiskActionDetails(
            riskActionValuesResponse: ActionValues,
            riskDetails: CdRiskDetailsAssessmentEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdRiskDetailsActionDetailsEntity {
        val riskActionDetails = CdRiskDetailsActionDetailsEntity()
        with(riskActionDetails) {
            actionCode = riskActionValuesResponse.actionCode
            actionName = riskActionValuesResponse.actionName
            cdRiskDetailsAssessmentId = riskDetails.id
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return iCdRiskDetailsActionDetailsRepository.save(riskActionDetails)
    }


    fun cdServicesProviderDetails(
            cDServiceProviderResponse: CDServiceProviderResponse,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdServiceProviderEntity {
        var servicesProviderDetails = CdServiceProviderEntity()
        with(servicesProviderDetails) {
            applicationCode = cDServiceProviderResponse.applicationCode
            name = cDServiceProviderResponse.name
            pin = cDServiceProviderResponse.pin
            physicalAddress = cDServiceProviderResponse.physicalAddress
            phyCountry = cDServiceProviderResponse.phyCountry
            physicalCountryName = phyCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        servicesProviderDetails = iCdServiceProviderRepo.save(servicesProviderDetails)

        KotlinLogging.logger { }.info { "services Provider Details saved ID = ${servicesProviderDetails.id}" }

        return servicesProviderDetails
    }

    fun cdItemNonCommodityDetails(
            itemCommodityResponse: CDItemCommodityResponse,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdItemCommodityDetailsEntity {
        var itemNonCommodity = CdItemCommodityDetailsEntity()
        with(itemNonCommodity) {
            commonName = itemCommodityResponse.commonName
            organismStrain = itemCommodityResponse.organismStrain
            treatmentInformation = itemCommodityResponse.treatmentInformation
            treatmentDate = itemCommodityResponse.treatmentDate
            durationsAndTemperature = itemCommodityResponse.durationsAndTemperature
            chemicalsActiveIngredients = itemCommodityResponse.chemicalsActiveIngredients
            concentrationActiveIngredients = itemCommodityResponse.concentrationActiveIngredients
            seedReferenceNo = itemCommodityResponse.seedReferenceNo
            producerDetails = itemCommodityResponse.producerDetails
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        itemNonCommodity = iCdItemCommodityDetailsRepository.save(itemNonCommodity)

        KotlinLogging.logger { }.info { "services Provider Details saved ID = ${itemNonCommodity.id}" }

        return itemNonCommodity
    }

    fun cdStandardsTwoDetails(
            cdStandardsTwoResponse: CDStandardTwoResponse,
            processStage: ProcessesStagesEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var standardsTwoDetails = CdStandardsTwoEntity()
        with(standardsTwoDetails) {
            conditionsOfApproval = cdStandardsTwoResponse.conditionsOfApproval
            applicantRemarks = cdStandardsTwoResponse.applicantRemarks
            mdaRemarks = cdStandardsTwoResponse.mdaRemarks
            customsRemarks = cdStandardsTwoResponse.customsRemarks
            purposeOfImport = cdStandardsTwoResponse.purposeOfImport
            cocType = cdStandardsTwoResponse.cocType
            localCocType = cdStandardsTwoResponse.localCocType
            cdProcessingFeeId = cdStandardsTwoResponse.processingFeeResponse?.let {
                processStage.process18?.let { it1 ->
                    standards2ProcessingFee(
                            it,
                            it1,
                            user,
                            map
                    ).id
                }
            }
            cdDocumentFeeId = cdStandardsTwoResponse.documentFeeResponse?.let {
                processStage.process19?.let { it1 ->
                    standards2DocumentFee(
                            it,
                            it1,
                            user,
                            map
                    ).id
                }
            }
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        standardsTwoDetails = iCdStandardsTwoRepo.save(standardsTwoDetails)
        //  Set standard two
        consignmentDocumentDetailsEntity.cdStandardsTwo=standardsTwoDetails
        KotlinLogging.logger { }.info { "Standards Two Details saved ID = ${standardsTwoDetails.id}" }

        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                processStage.process9?.let { it2 -> daoServices.createCDTransactionLog(map, user, it1, it, it2) }
            }
        }
        // Update extra details
        cdStandardsTwoResponse.thirdPartyDetails?.thirdPartyResponses
                ?.forEach { thirdParties ->
                    val containerDetails = thirdPartyDetails(thirdParties, user, map, standardsTwoDetails)
                    commonDaoServices.convertClassToJson(containerDetails)?.let {
                        consignmentDocumentDetails.id?.let { it1 ->
                            processStage.process16?.let { it2 ->
                                daoServices.createCDTransactionLog(map, user, it1, it, it2)
                            }
                        }
                    }
                }

        cdStandardsTwoResponse.applicantDefinedThirdPartyDetails?.thirdPartiesApplicantDefinedResponse
                ?.forEach { thirdPartiesApplicantDefined ->
                    val containerDetails =
                            applicantDefinedThirdParty(thirdPartiesApplicantDefined, user, map, standardsTwoDetails)
                    commonDaoServices.convertClassToJson(containerDetails)?.let {
                        consignmentDocumentDetails.id?.let { it1 ->
                            processStage.process17?.let { it2 ->
                                daoServices.createCDTransactionLog(map, user, it1, it, it2)
                            }
                        }
                    }
                }
        cdStandardsTwoResponse.attachments?.forEach {
            val attachment = DiUploadsEntity()
            attachment.name = it.fileName
            attachment.description = it.attachmentCodeDesc
            attachment.documentType = it.attachmentCode
            attachment.cdId = consignmentDocumentDetailsEntity
            this.downloadAttachment(attachment)
        }

        return consignmentDocumentDetails
    }

    fun downloadAttachment(attachment: DiUploadsEntity) {

    }

    fun standards2ProcessingFee(
            processingFeeResponse: ProcessingFeeResponse,
            processStage: String,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdProcessingFeeEntity {
        var processingFee = CdProcessingFeeEntity()
        with(processingFee) {
            currency = processingFeeResponse.currency
            amountFcy = processingFeeResponse.amountFCY
            amountNcy = processingFeeResponse.amountNCY
            paymentMode = processingFeeResponse.paymentMode
            receiptNumber = processingFeeResponse.receiptNumber
            receiptDate = processingFeeResponse.receiptDate
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        processingFee = iCdProcessingFeeRepository.save(processingFee)
        return processingFee
    }

    fun standards2DocumentFee(
            documentFeeResponse: DocumentFeeResponse,
            processStage: String,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdDocumentFeeEntity {
        var documentFee = CdDocumentFeeEntity()
        with(documentFee) {
            currency = documentFeeResponse.currency
            amountFcy = documentFeeResponse.amountFCY
            amountNcy = documentFeeResponse.amountNCY
            paymentMode = documentFeeResponse.paymentMode
            receiptNumber = documentFeeResponse.receiptNumber
            receiptDate = documentFeeResponse.receiptDate
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        documentFee = iCdDocumentFeeRepository.save(documentFee)
        return documentFee
    }

    //save importer details
    fun importerCDDetails(
            cdImporterResponse: CdImporterResponse,
            processStage: String,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var importerDetails = CdImporterDetailsEntity()
        with(importerDetails) {
            name = cdImporterResponse.name
            pin = cdImporterResponse.pin
            mdaRefNo = cdImporterResponse.mdaRefNo
            physicalAddress = cdImporterResponse.physicalAddress
            physicalCountry = cdImporterResponse.physicalCountry
            physicalCountryName = physicalCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            postalCountryName = postalCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            postalAddress = cdImporterResponse.postalAddress
            postalCountry = cdImporterResponse.postalCountry
            postalCountryName = cdImporterResponse.postalCountryName
            telephone = cdImporterResponse.telephone
            fax = cdImporterResponse.fax
            sectorOfActivity = cdImporterResponse.sectorOfActivity
            warehouseCode = cdImporterResponse.warehouseCode
            warehouseLocation = cdImporterResponse.warehouseLocation
            email = cdImporterResponse.email
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        importerDetails = iCdImporterRepo.save(importerDetails)

        KotlinLogging.logger { }.info { "Importer Details saved ID = ${importerDetails.id}" }

        with(consignmentDocumentDetailsEntity) {
            cdImporter = importerDetails.id
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStage)
            }
        }

        return consignmentDocumentDetails
    }

    // save Exporter Details
    fun exporterCDDetails(
            cdExporterResponse: CdExporterResponse,
            processStage: String,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var exporterDetails = CdExporterDetailsEntity()
        with(exporterDetails) {
            name = cdExporterResponse.name
            pin = cdExporterResponse.pin
            mdaRefNo = cdExporterResponse.mdaRefNo
            physicalAddress = cdExporterResponse.physicalAddress
            physicalCountry = cdExporterResponse.physicalCountry
            postalAddress = cdExporterResponse.postalAddress
            postalCountry = cdExporterResponse.postalCountry
            physicalCountryName = physicalCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            postalCountryName = postalCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            telephone = cdExporterResponse.telephone
            fax = cdExporterResponse.fax
            sectorOfActivity = cdExporterResponse.sectorOfActivity
            warehouseCode = cdExporterResponse.warehouseCode
            warehouseLocation = cdExporterResponse.warehouseLocation
            email = cdExporterResponse.email
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        exporterDetails = iCdExporterRepo.save(exporterDetails)

        KotlinLogging.logger { }.info { "Exporter Details saved ID = ${exporterDetails.id}" }

        with(consignmentDocumentDetailsEntity) {
            cdExporter = exporterDetails.id
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStage)
            }
        }

        return consignmentDocumentDetails
    }

    // save Transport Details
    fun transportCDDetails(
            cdTransportResponse: CdTransportResponse,
            processStage: ProcessesStagesEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var transportDetails = CdTransportDetailsEntity()
        with(transportDetails) {
            modeOfTransport = cdTransportResponse.modeOfTransport
            modeOfTransportDesc = cdTransportResponse.modeOfTransportDesc
            vesselName = cdTransportResponse.vesselName
            voyageNo = cdTransportResponse.voyageNo
            shipmentDate = cdTransportResponse.shipmentDate
            carrier = cdTransportResponse.carrier
            manifestNo = cdTransportResponse.manifestNo
            blawb = cdTransportResponse.blawb
            marksAndNumbers = cdTransportResponse.marksAndNumbers
            portOfArrival = cdTransportResponse.portOfArrival
            portOfArrivalDesc = cdTransportResponse.portOfArrivalDesc
            portOfDeparture = cdTransportResponse.portOfDeparture
            portOfDepartureDesc = cdTransportResponse.portOfDepartureDesc
            customOffice = cdTransportResponse.customOffice
            customOfficeDesc = cdTransportResponse.customOfficeDesc
            freightStation = cdTransportResponse.freightStation
            freightStationDesc = cdTransportResponse.freightStationDesc
            cargoTypeIndicator = cdTransportResponse.cargoTypeIndicator
            inLandTransPortCo = cdTransportResponse.inLandTransPortCo
            inLandTransPortCoRefNo = cdTransportResponse.inLandTransPortCoRefNo
            kephisCollectionOffice = cdTransportResponse.kephisCollectionOffice
            certificationCategory = cdTransportResponse.certificationCategory
            placeOfApplication = cdTransportResponse.placeOfApplication
            dateOfArrival = cdTransportResponse.dateOfArrival

            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        transportDetails = iCdTransportRepo.save(transportDetails)

        KotlinLogging.logger { }.info { "Transport Details saved ID = ${transportDetails.id}" }
        with(consignmentDocumentDetailsEntity) {
            cdTransport = transportDetails.id
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                processStage.process4?.let { it2 -> daoServices.createCDTransactionLog(map, user, it1, it, it2) }
            }
        }

        cdTransportResponse.containerDetails?.containers
                ?.forEach { container ->
                    val containerDetails = transportContainerDetailsDetails(container, user, map, transportDetails)
                    commonDaoServices.convertClassToJson(containerDetails)?.let {
                        consignmentDocumentDetails.id?.let { it1 ->
                            processStage.process13?.let { it2 ->
                                daoServices.createCDTransactionLog(map, user, it1, it, it2)
                            }
                        }
                    }
                }


        return consignmentDocumentDetails
    }

    // save Consignee Details
    fun consigneeCDDetails(
            cdConsigneeResponse: CdConsigneeResponse,
            processStage: String,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var consigneeDetails = CdConsigneeDetailsEntity()
        with(consigneeDetails) {
            name = cdConsigneeResponse.name
            pin = cdConsigneeResponse.pin
            mdaRefNo = cdConsigneeResponse.mdaRefNo
            physicalAddress = cdConsigneeResponse.physicalAddress
            physicalCountry = cdConsigneeResponse.physicalCountry
            postalAddress = cdConsigneeResponse.postalAddress
            postalCountry = cdConsigneeResponse.postalCountry
            physicalCountryName = physicalCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            postalCountryName = postalCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            telephone = cdConsigneeResponse.telephone
            fax = cdConsigneeResponse.fax
            sectorOfActivity = cdConsigneeResponse.sectorOfActivity
            warehouseCode = cdConsigneeResponse.warehouseCode
            warehouseLocation = cdConsigneeResponse.warehouseLocation
            email = cdConsigneeResponse.email
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        consigneeDetails = iCdConsigneeRepo.save(consigneeDetails)
        KotlinLogging.logger { }.info { "Consignee Details saved ID = ${consigneeDetails.id}" }
        with(consignmentDocumentDetailsEntity) {
            cdConsignee = consigneeDetails.id
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStage)
            }
        }
        return consignmentDocumentDetails
    }

    // save PGA Header Fields Details
    fun pgaHeaderFieldsCDDetails(
            pgaHeaderFieldsResponse: PGAHeaderFieldsResponse,
            processStage: String,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var pgaHeaderFieldsDetails = CdPgaHeaderFieldsEntity()
        with(pgaHeaderFieldsDetails) {
            collectionOffice = pgaHeaderFieldsResponse.collectionOffice
            certificationCategory = pgaHeaderFieldsResponse.certificationCategory
            placeOfApplication = pgaHeaderFieldsResponse.placeOfApplication
            preferredInspectionDate = pgaHeaderFieldsResponse.preferredInspectionDate
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        pgaHeaderFieldsDetails = iCdPgaHeaderFieldsRepository.save(pgaHeaderFieldsDetails)
        KotlinLogging.logger { }.info { "PGA Header Fields Details saved ID = ${pgaHeaderFieldsDetails.id}" }
        with(consignmentDocumentDetailsEntity) {
            cdPgaHeader = pgaHeaderFieldsDetails.id
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStage)
            }
        }
        return consignmentDocumentDetails
    }

    // save Consignor Details
    fun consignorCDDetails(
            cdConsignorResponse: CdConsignorResponse,
            processStage: String,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var consignorDetails = CdConsignorDetailsEntity()
        with(consignorDetails) {
            name = cdConsignorResponse.name
            pin = cdConsignorResponse.pin
            mdaRefNo = cdConsignorResponse.mdaRefNo
            physicalAddress = cdConsignorResponse.physicalAddress
            physicalCountry = cdConsignorResponse.physicalCountry
            postalAddress = cdConsignorResponse.postalAddress
            postalCountry = cdConsignorResponse.postalCountry
            physicalCountryName = physicalCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            postalCountryName = postalCountry?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            telephone = cdConsignorResponse.telephone
            fax = cdConsignorResponse.fax
            sectorOfActivity = cdConsignorResponse.sectorOfActivity
            warehouseCode = cdConsignorResponse.warehouseCode
            warehouseLocation = cdConsignorResponse.warehouseLocation
//            ogaRefNo = cdConsignorResponse.ogaRefNo
            email = cdConsignorResponse.email
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        consignorDetails = iCdConsignorRepo.save(consignorDetails)
        KotlinLogging.logger { }.info { "Consignor Details saved ID = ${consignorDetails.id}" }
        with(consignmentDocumentDetailsEntity) {
            cdConsignor = consignorDetails.id
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStage)
            }
        }
        return consignmentDocumentDetails
    }

    // save ValuesHeader Details
    fun valuesHeaderCDDetails(
            cdHeaderOneResponse: CdHeaderOneResponse,
            processStage: String,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var valuesHeaderDetails = CdValuesHeaderLevelEntity()
        with(valuesHeaderDetails) {
            foreignCurrencyCode = cdHeaderOneResponse.foreignCurrencyCode
            forexRate = cdHeaderOneResponse.forexRate
            fobFcy = cdHeaderOneResponse.fobFcy
            freightFcy = cdHeaderOneResponse.freightFcy
            insuranceFcy = cdHeaderOneResponse.insuranceFcy
            otherChargesFcy = cdHeaderOneResponse.otherChargesFcy
            cifFcy = cdHeaderOneResponse.cifFcy
            fobNcy = cdHeaderOneResponse.fobNcy
            freightNcy = cdHeaderOneResponse.freightNcy
            insuranceNyc = cdHeaderOneResponse.insuranceNyc
            otherChargesNcy = cdHeaderOneResponse.otherChargesNcy
            cifNcy = cdHeaderOneResponse.cifNcy
            incoTerms = cdHeaderOneResponse.incoTerms
            transactionTerms = cdHeaderOneResponse.transactionTerms
            comesa = cdHeaderOneResponse.comesa
            invoiceDate = cdHeaderOneResponse.invoiceDate
            invoiceNumber = cdHeaderOneResponse.invoiceNumber
            countryOfSupply = cdHeaderOneResponse.countryOfSupply
            countryOfSupplyName = countryOfSupply?.let { daoServices.findCdCountryByCountryCode(it)?.countryName }
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        valuesHeaderDetails = iCdValuesHeaderLevelRepo.save(valuesHeaderDetails)

        KotlinLogging.logger { }.info { "Values Header Details saved ID = ${valuesHeaderDetails.id}" }
        with(consignmentDocumentDetailsEntity) {
            cdHeaderOne = valuesHeaderDetails.id
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStage)
            }
        }
        return consignmentDocumentDetails
    }

    // save ValuesHeader Details
    fun headerTwoCDDetails(
            cdHeaderTwoResponse: CDHeaderTwoResponse,
            processStage: String,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        var headerTwoDetails = CdHeaderTwoDetailsEntity()
        with(headerTwoDetails) {
            termsOfPayment = cdHeaderTwoResponse.termsOfPayment
            termsOfPaymentDesc = cdHeaderTwoResponse.termsOfPaymentDesc
            localBankCode = cdHeaderTwoResponse.localBankCode
            localBankDesc = cdHeaderTwoResponse.localBankDesc
            receiptOfRemittance = cdHeaderTwoResponse.receiptOfRemittance
            remittanceCurrency = cdHeaderTwoResponse.remittanceCurrency
            remittanceAmount = cdHeaderTwoResponse.remittanceAmount
            remittanceDate = cdHeaderTwoResponse.remittanceDate
            remittanceReference = cdHeaderTwoResponse.remittanceReference
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        headerTwoDetails = iCdHeaderTwoDetailsRepository.save(headerTwoDetails)

        KotlinLogging.logger { }.info { "Header Two Details saved ID = ${headerTwoDetails.id}" }
        with(consignmentDocumentDetailsEntity) {
            cdHeaderTwo = headerTwoDetails.id
        }
        val consignmentDocumentDetails = updateConsignmentDocumentDetails(consignmentDocumentDetailsEntity, user, map)
        commonDaoServices.convertClassToJson(consignmentDocumentDetails)?.let {
            consignmentDocumentDetails.id?.let { it1 ->
                daoServices.createCDTransactionLog(map, user, it1, it, processStage)
            }
        }
        return consignmentDocumentDetails
    }

    // save Item Details Details
    fun itemCDDetails(
            product1Response: CDProduct1Response,
            user: UsersEntity,
            map: ServiceMapsEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity
    ): CdItemDetailsEntity {
        var itemDetails = CdItemDetailsEntity()
        with(itemDetails) {
            uuid = commonDaoServices.generateUUIDString()
            cdDocId = consignmentDocumentDetailsEntity

            itemNo = product1Response.itemNo?.toLong()
            itemDescription = product1Response.itemDescription
            itemHsCode = product1Response.itemHsCode
            hsDescription = product1Response.hsDescription
            internalFileNumber = product1Response.internalFileNumber
            internalProductNo = product1Response.internalProductNo
            productTechnicalName = product1Response.productTechnicalName
            productBrandName = product1Response.productBrandName
            productActiveIngredients = product1Response.productActiveIngredients
            productPackagingDetails = product1Response.productPackagingDetails
            productClassCode = product1Response.productClassCode
            productClassDescription = product1Response.productClassDescription
            packageType = product1Response.packageType
            packageTypeDesc = product1Response.packageTypeDesc
            packageQuantity = product1Response.packageQuantity?.toBigDecimal()
            foreignCurrencyCode = product1Response.foreignCurrencyCode
            unitPriceFcy = product1Response.unitPriceFcy?.toBigDecimal()
            totalPriceFcy = product1Response.totalPriceFcy?.toBigDecimal()
            unitPriceNcy = product1Response.unitPriceNcy?.toBigDecimal()
            countryOfOrgin = product1Response.countryOfOrigin
            countryOfOrginDesc = product1Response.countryOfOriginDesc
            totalPriceNcy = product1Response.totalPriceNcy?.toBigDecimal()
            itemNetWeight = product1Response.itemNetWeight
            itemGrossWeight = product1Response.itemGrossWeight
            marksAndContainers = product1Response.marksAndContainers
            quantity = product1Response.quantity?.qty?.toBigDecimal()
            unitOfQuantity = product1Response.quantity?.unitOfQty
            unitOfQuantityDesc = product1Response.quantity?.unitOfQtyDesc
            supplementaryQty = product1Response.supplementaryQuantity?.qty
            supplementaryUnitOfQty = product1Response.supplementaryQuantity?.unitOfQty
            supplementaryUnitOfQtyDesc = product1Response.supplementaryQuantity?.unitOfQtyDesc

            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        itemDetails = iCdItemsRepo.save(itemDetails)

        KotlinLogging.logger { }.info { "Item Details saved ID = ${itemDetails.id}" }

        return itemDetails
    }


    // save Item Details Details
    fun cdProducts2Details(
            product2Response: CDProduct2Response,
            user: UsersEntity,
            map: ServiceMapsEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity
    ): CdProducts2Entity {
        var products2Details = CdProducts2Entity()
        with(products2Details) {
            riskClassification = product2Response.riskClassification
            riskDetails = product2Response.riskDetails
            safetyClassification = product2Response.safetyClassification
            safetyDetails = product2Response.safetyDetails
            risksAfetyRemarks = product2Response.riskSafetyRemarks
            samplingRequirement = product2Response.samplingRequirement
            samplingResults = product2Response.samplingResults
            applicantRemarks = product2Response.applicantRemarks
            mdaRemarks = product2Response.mdaRemarks
            customsRemarks = product2Response.customsRemarks
            mdaItemApprovalFlag = product2Response.mdaItemApprovalFlag

            cdProducts2EndUserDetailsId =
                    product2Response.endUserDetailsResponse?.let { endUserDetails(it, user, map).id }

            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        products2Details = iCdProducts2Repository.save(products2Details)

        KotlinLogging.logger { }.info { "Products2 Details saved ID = ${products2Details.id}" }

        return products2Details
    }

    // save Approval History Details
    fun cdApprovalHistoryDetails(
            approvalHistoryResponse: ApprovalHistoryResponse,
            user: UsersEntity,
            map: ServiceMapsEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity
    ): CdApprovalHistoryEntity {
        var approvalDetails = CdApprovalHistoryEntity()
        with(approvalDetails) {

            stageNo = approvalHistoryResponse.stageNo
            stepCode = approvalHistoryResponse.stepCode
            mdaCode = approvalHistoryResponse.MDACode
            roleCode = approvalHistoryResponse.roleCode
            docStatus = approvalHistoryResponse.status
            userId = approvalHistoryResponse.userId
            updatedDate = approvalHistoryResponse.updatedDate
            premiseInspection = approvalHistoryResponse.premiseInspection
            examinationRequired = approvalHistoryResponse.examinationRequired
            technicalRejection = approvalHistoryResponse.technicalRejection
            cdId = consignmentDocumentDetailsEntity.id

            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        approvalDetails = iCdApprovalHistoryRepository.save(approvalDetails)

        KotlinLogging.logger { }.info { "Approval Details saved ID = ${approvalDetails.id}" }

        return approvalDetails
    }

    fun endUserDetails(
            endUserResponse: EndUserDetailsResponse,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdProducts2EndUserDetailsEntity {
        var endUserDetails = CdProducts2EndUserDetailsEntity()
        with(endUserDetails) {
            regNo = endUserResponse.regNo
            name = endUserResponse.name
            physicalAddress = endUserResponse.physicalAddress
            telFax = endUserResponse.telFax
            useGeneralDescription = endUserResponse.useGeneralDescription
            useDetails = endUserResponse.useDetails
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        endUserDetails = iCdProducts2EndUserDetailsRepository.save(endUserDetails)

        KotlinLogging.logger { }.info { "End User Details saved ID = ${endUserDetails.id}" }

        return endUserDetails
    }

    // save Container Details
    fun transportContainerDetailsDetails(
            containersResponse: ContainersResponse,
            user: UsersEntity,
            map: ServiceMapsEntity,
            cdTransportDetails: CdTransportDetailsEntity
    ): CdContainerEntity {
        var containerDetails = CdContainerEntity()
        with(containerDetails) {

            containerNumbers = containersResponse.containerNumbers
            containerNoOfPackages = containersResponse.containerNoOfPackages
            containerSize = containersResponse.containerSize
            containerSealNo = containersResponse.containerSealNo
            containerLoadIndicator = containersResponse.containerLoadIndicator
            cdTransportId = cdTransportDetails.id

            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        containerDetails = iCdContainerRepository.save(containerDetails)

        KotlinLogging.logger { }.info { "Container Details saved ID = ${containerDetails.id}" }

        return containerDetails
    }

    // save Third Party Details
    fun thirdPartyDetails(
            thirdPartiesResponse: ThirdPartiesResponse,
            user: UsersEntity,
            map: ServiceMapsEntity,
            cdStandardsTwoDetails: CdStandardsTwoEntity
    ): CdThirdPartyDetailsEntity {
        var thirdPartiesDetails = CdThirdPartyDetailsEntity()
        with(thirdPartiesDetails) {

            thirdPartyCode = thirdPartiesResponse.thirdPartyCode
            thirdPartyDescription = thirdPartiesResponse.thirdPartyDescription
            distributionMethod = thirdPartiesResponse.distributionMethod
            thirdPartyMailBox = thirdPartiesResponse.thirdPartyMailbox
            thirdPartyAccount = thirdPartiesResponse.thirdPartyAccount
            cdStandardsTwoId = cdStandardsTwoDetails.id

            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        thirdPartiesDetails = iCdThirdPartyDetailsRepository.save(thirdPartiesDetails)

        KotlinLogging.logger { }.info { "Third Party Details saved ID = ${thirdPartiesDetails.id}" }

        return thirdPartiesDetails
    }

    // save Applicant Defined Third Party Details
    fun applicantDefinedThirdParty(
            thirdPartiesApplicantResponseResponse: ThirdPartiesApplicantDefinedResponse,
            user: UsersEntity,
            map: ServiceMapsEntity,
            cdStandardsTwoDetails: CdStandardsTwoEntity
    ): CdApplicantDefinedThirdPartyDetailsEntity {
        var thirdPartiesDetails = CdApplicantDefinedThirdPartyDetailsEntity()
        with(thirdPartiesDetails) {

            thirdPartyCode = thirdPartiesApplicantResponseResponse.thirdPartyCode
            thirdPartyDescription = thirdPartiesApplicantResponseResponse.thirdPartyDescription
            distributionMethod = thirdPartiesApplicantResponseResponse.distributionMethod
            thirdPartyMailBox = thirdPartiesApplicantResponseResponse.thirdPartyMailbox
            thirdPartyAccount = thirdPartiesApplicantResponseResponse.thirdPartyAccount
            cdStandardsTwoId = cdStandardsTwoDetails.id

            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        thirdPartiesDetails = iCdApplicantDefinedThirdPartyDetailsRepository.save(thirdPartiesDetails)

        KotlinLogging.logger { }.info { "Applicant Defined Third Party Details saved ID = ${thirdPartiesDetails.id}" }

        return thirdPartiesDetails
    }

    // save Non standard Item Details
    fun nonStandardItemCDDetails(
            cdItemNonStandardResponse: CDItemNonStandardResponse,
            user: UsersEntity,
            map: ServiceMapsEntity,
            cdItemDetails: CdItemDetailsEntity
    ): CdItemNonStandardEntity {
        var nonStandardEntity = CdItemNonStandardEntity()
        with(nonStandardEntity) {
            cdItemDetailsId = cdItemDetails
            chassisNo = cdItemNonStandardResponse.chassisNo
            usedIndicator = cdItemNonStandardResponse.usedIndicator
            vehicleYear = cdItemNonStandardResponse.vehicleYear
            vehicleModel = cdItemNonStandardResponse.vehicleModel
            vehicleMake = cdItemNonStandardResponse.vehicleMake
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        nonStandardEntity = iCdItemNonStandardEntityRepository.save(nonStandardEntity)
        // Update chassis number
        cdItemDetails.chassisNumber=cdItemNonStandardResponse.chassisNo
        this.iCdItemsRepo.save(cdItemDetails)
        //
        KotlinLogging.logger { }.info { "Non standard Item Details saved ID = ${nonStandardEntity.id}" }

        return nonStandardEntity
    }


}

