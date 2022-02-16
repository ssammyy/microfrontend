package org.kebs.app.kotlin.apollo.api.ports.provided.lims

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.FileUtils
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.response.RootLabPdfList
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.response.RootTestResultsAndParameters
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.response.TestParameter
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.response.TestResult
import org.kebs.app.kotlin.apollo.api.service.ChecklistService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleLabTestParametersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleLabTestResultsEntity
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitApplicationsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestParametersRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestResultsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection


@Service
class LimsServices(
        private val commonDaoServices: CommonDaoServices,
        private val jasyptStringEncryptor: StringEncryptor,
        private val checklistService: ChecklistService,
        private val downloaderFile: DownloaderFile,
        private val applicationMapProperties: ApplicationMapProperties,
        private val sampleLabTestResults: IQaSampleLabTestResultsRepository,
        private val sampleLabTestParameters: IQaSampleLabTestParametersRepository,
        private val sampleSubmissionRepo: IQaSampleSubmissionRepository,
        private val permitRepo: IPermitApplicationsRepository,
        private val configurationRepository: IIntegrationConfigurationRepository
) {

    @Lazy
    @Autowired
    lateinit var diDaoServices: DestinationInspectionDaoServices

    @Lazy
    @Autowired
    lateinit var qaDaoServices: QADaoServices

//    @Autowired
//    lateinit var permitRepo: IPermitApplicationsRepository

    val appId = applicationMapProperties.mapLimsTransactions
    val appIdPDF = applicationMapProperties.mapLimsTransactions

    fun performPostCall(
            postDataParams: HashMap<String, String>,
            applicationMapID: Long
    ): String? {
        val url: URL
        var response: String? = ""
        try {

            val map = commonDaoServices.serviceMapDetails(appId)
            val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapID)

            url = URL(config.url)
            val b = Base64()
            val encoding: String = b.encodeAsString(("${jasyptStringEncryptor.decrypt(config.username)}:${jasyptStringEncryptor.decrypt(config.password)}").toByteArray())

            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 15000
            conn.connectTimeout = 15000
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true
            conn.setRequestProperty("Authorization", "Basic $encoding")
            val os: OutputStream = conn.outputStream
            val writer = BufferedWriter(
                    OutputStreamWriter(os, "UTF-8")
            )
            writer.write(getPostDataString(postDataParams))
            writer.flush()
            writer.close()
            os.close()
            val responseCode: Int = conn.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                var line: String?
                System.out.println("::::::::::::::::::::::::CONNECTION MADE::::::::::::::::")
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                while (br.readLine().also { line = it } != null) {
                    response += line
                    System.out.println(response.toString())
                }
            } else {
                response = ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    fun performPostCallReceiveFile(
            postDataParams: HashMap<String, String>,
            applicationMapID: Long
    ): File? {
        val url: URL
        var response: File? = null
        try {

            val map = commonDaoServices.serviceMapDetails(appId)
            val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapID)

            url = URL(config.url)
            val b = Base64()
            val encoding: String = b.encodeAsString(
                    ("${jasyptStringEncryptor.decrypt(config.username)}:${
                        jasyptStringEncryptor.decrypt(config.password)
                    }").toByteArray()
            )

            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 15000
            conn.connectTimeout = 15000
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true
            conn.setRequestProperty("Authorization", "Basic $encoding")
            val os: OutputStream = conn.outputStream
            val writer = BufferedWriter(
                    OutputStreamWriter(os, "UTF-8")
            )
            writer.write(getPostDataString(postDataParams))
            writer.flush()
            writer.close()
            os.close()
            val responseCode: Int = conn.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                var line: String?
                System.out.println("::::::::::::::::::::::::CONNECTION MADE::::::::::::::::")
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                val source: InputStream = conn.inputStream
                FileUtils.copyInputStreamToFile(source, response)
            } else {
                response = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getPostDataString(params: HashMap<String, String>): String {
        val result = StringBuilder()
        var first = true
        for ((key, value) in params) {
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value, "UTF-8"))
        }
        return result.toString()
    }

    fun labResponseResults(response: String): Boolean {
        val resultsParam: RootTestResultsAndParameters = ObjectMapper().readValue(response, RootTestResultsAndParameters::class.java)
        var myStatus: Boolean = false
        //Loop
        if (resultsParam.test_parameter?.isNullOrEmpty() == true) {
            println("List is null or empty")
            throw ExpectedDataNotFound("NO RESULTS FOUND")
//            return myStatus
        } else {
            myStatus = true
            resultsParam.test_result
                    ?.forEach { testResults ->
                        limsTestResultsDetails(testResults)
                    }
            //Loop
            resultsParam.test_parameter
                    ?.forEach { testParam ->
                        limsTestParamsDetails(testParam)
                    }

            return myStatus
        }

    }

    fun labPdfListResponseResults(response: String): List<String>? {
        val resultsParam: RootLabPdfList = ObjectMapper().readValue(response, RootLabPdfList::class.java)
        //Loop
        if (resultsParam.pdf_files?.isNullOrEmpty() == true) {
            println("List is null or empty")
            throw ExpectedDataNotFound("NO RESULTS PDF FOUND")
//            return myStatus
        } else {
            return resultsParam.pdf_files
        }

    }

    fun checkBsNumberExistOnLims(bsNumber: String): Boolean {
        var results = false
        val hmap = HashMap<String, String>()
        hmap["bsnumber"] = bsNumber
        val myResults = performPostCall(hmap, applicationMapProperties.mapLimsConfigIntegration)
        if (myResults != null) {
            try {
                val resultsParam: RootTestResultsAndParameters = ObjectMapper().readValue(myResults, RootTestResultsAndParameters::class.java)
                if ("OK".equals(resultsParam.status, true)) {
                    results = true
                }
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("Failed to decode reponse", ex)
            }
        }
        return results
    }

    fun mainFunctionLims(bsNumber: String): Boolean {
        var results = false
        val hmap = HashMap<String, String>()
        hmap["bsnumber"] = bsNumber
        val myResults = performPostCall(hmap, applicationMapProperties.mapLimsConfigIntegration)
        if (myResults != null) {
            results = labResponseResults(myResults)
        }
        return results
    }

    fun mainFunctionLimsGetPDFList(bsNumber: String): List<String>? {
        var results: List<String>? = null
        val hmap = HashMap<String, String>()
        hmap["bsnumber"] = bsNumber
        val myResults = performPostCall(hmap, applicationMapProperties.mapLimsConfigIntegrationListPDF)
        if (myResults != null) {
            results = labPdfListResponseResults(myResults)
        }
        return results
    }

    fun mainFunctionLimsGetPDF(bsNumber: String, pdf: String): File {
        var results = false
        val hmap = HashMap<String, String>()
        hmap["bsnumber"] = bsNumber
        hmap["pdf"] = pdf

        val file = File(pdf)

        return downloaderFile.download(file, applicationMapProperties.mapLimsConfigIntegrationPDF, hmap)
//        return performPostCallReceiveFile(hmap, applicationMapProperties.mapLimsConfigIntegrationPDF)
//        return performPostCallReceiveFile(hmap, applicationMapProperties.mapLimsConfigIntegrationPDF)
    }


    fun limsTestResultsDetails(
            testResults: TestResult
    ): QaSampleLabTestResultsEntity {
        val testResultsDetails = QaSampleLabTestResultsEntity()
        with(testResultsDetails) {
            orderId = testResults.orderID
            sampleNumber = testResults.sampleNumber
            test = testResults.test
            param = testResults.param
            sortOrder = testResults.sortOrder
            method = testResults.method
            result = testResults.result
            numericResult = testResults.numericResult
            units = testResults.units
            dilution = testResults.dilution
            analysisVolume = testResults.analysisVolume
            sampleType = testResults.sampleType
            qualifier = testResults.qualifier
            repLimit = testResults.repLimit
            retentionTime = testResults.retentionTime
            tic = testResults.tIC
            resultStatus = testResults.resultStatus
            enteredDate = testResults.enteredDate
            enteredBy = testResults.enteredBy
            validatedDate = testResults.validatedDate
            validatedBy = testResults.validatedBy
            approvedDate = testResults.approvedDate
            approvedBy = testResults.approvedBy
            approvedReason = testResults.approvedReason
            commnt = testResults.commnt
            measuredResult = testResults.measuredResult
            percentMoisture = testResults.percentMoisture
            methodVolume = testResults.methodVolume
            extractVolume = testResults.extractVolume
            methodExtractVolume = testResults.methodExtractVolume
            instrument = testResults.instrument
            resultsUser1 = testResults.results_User1
            resultsUser2 = testResults.results_User2
            resultsUser3 = testResults.results_User3
            resultsUser4 = testResults.results_User4
            report = testResults.report
            paramAnalyst = testResults.ts
            paramAnalysisDateTime = testResults.paramAnalyst
            printed = testResults.paramAnalysisDateTime
            printedAt = testResults.printed
            ts = testResults.printedAt
            status = 1
            createdBy = "ADMIN"
            createdOn = commonDaoServices.getTimestamp()
        }
        return sampleLabTestResults.save(testResultsDetails)
    }

    fun limsTestParamsDetails(
            testParams: TestParameter
    ): QaSampleLabTestParametersEntity {
        val testParamsDetails = QaSampleLabTestParametersEntity()
        with(testParamsDetails) {
            orderId = testParams.orderID
            sampleNumber = testParams.sampleNumber
            test = testParams.test
            matrix = testParams.matrix
            method = testParams.method
            testGroup = testParams.testGroup
            priority = testParams.priority
            currentDepartment = testParams.currentDepartment
            lastDepartment = testParams.lastDepartment
            reDepartment = testParams.rEDepartment
            testPrice = testParams.testPrice
            customerTestPrice = testParams.customerTestPrice
            dueDate = testParams.dueDate
            dueDateFlag = testParams.dueDateFlag
            prepDueDate = testParams.prepDueDate
            prepMethod = testParams.prepMethod
            analysisDueDate = testParams.analysisDueDate
            analysisTime = testParams.analysisTime
            analysisEmployee = testParams.analysisEmployee
            keepTest = testParams.keepTest
            cancelled = testParams.cancelled
            hasResults = testParams.hasResults
            supplyReconciled = testParams.supplyReconciled
            customParams = testParams.customParams
            preservative = testParams.preservative
            bottleType = testParams.bottleType
            storageLocation = testParams.storageLocation
            sampleDetailsUser1 = testParams.sampleDetails_User1
            sampleDetailsUser2 = testParams.sampleDetails_User2
            sampleDetailsUser3 = testParams.sampleDetails_User3
            sampleDetailsUser4 = testParams.sampleDetails_User4
            ts = testParams.ts
            status = 1
            createdBy = "ADMIN"
            createdOn = commonDaoServices.getTimestamp()
        }
        return sampleLabTestParameters.save(testParamsDetails)
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateLabResultsWithDetails(bsNumber: String, status: Int) {
        val map = commonDaoServices.serviceMapDetails(appId)
        //Find all Sample with Lab results inactive
        KotlinLogging.logger { }.info { "::::::::::::::::::::::::STARTED LAB RESULTS SCHEDULER::::::::::::::::::" }
        var samples = 1
        sampleSubmissionRepo.findByLabResultsStatusAndBsNumber(status, bsNumber)
                ?.let { ssfFound ->
                    KotlinLogging.logger { }
                            .info { "::::::::::::::::::::::::SAMPLES WITH RESULTS FOUND = ${samples++}::::::::::::::::::" }
                    when {
                        mainFunctionLims(bsNumber) -> {
                            with(ssfFound) {
                                modifiedBy = "SYSTEM SCHEDULER"
                                modifiedOn = commonDaoServices.getTimestamp()
                                labResultsStatus = map.activeStatus
                                resultsDate = commonDaoServices.getCurrentDate()
                            }
                            sampleSubmissionRepo.save(ssfFound)
                            when {
                                ssfFound.permitId != null -> {
                                    qaDaoServices.findPermitBYID(
                                            ssfFound.permitId ?: throw Exception("PERMIT ID NOT FOUND")
                                    )
                                            .let { pm ->
                                                with(pm) {
                                                    permitStatus = applicationMapProperties.mapQaStatusPLABResultsCompletness
                                                    modifiedBy = "SYSTEM SCHEDULER"
                                                    modifiedOn = commonDaoServices.getTimestamp()
                                                }
                                                permitRepo.save(pm)
                                            }
                                }
                                ssfFound.cdItemId != null -> {
                                    diDaoServices.findItemWithItemID(
                                            ssfFound.cdItemId ?: throw Exception("CD ITEM ID NOT FOUND")
                                    )
                                            .let { cdItem ->
                                                // Update Item result
                                                cdItem.allTestReportStatus = map.activeStatus
                                                cdItem.varField10 = "LAB RESULT RECEIVED"
                                                diDaoServices.updateCdItemDetailsInDB(cdItem, null)
                                                // Update consignment status if all reports have been received
                                                diDaoServices.findCD(cdItem.cdDocId?.id
                                                        ?: throw Exception("CD ID NOT FOUND"))
                                                        .let { updatedCDDetails -> checklistService.updateConsignmentSampledStatus(updatedCDDetails, true) }

                                            }
                                }
                                else -> throw ExpectedDataNotFound("INVALID SSF DETAILS WITH MISSING PERMIT/CD ITEM DETAILS")
                            }


                        }
                        else -> throw ExpectedDataNotFound("NO RESULTS FOUND")
                    }

                }
    }

    fun checkPDFFiles(bsNumber: String): List<String>? {
        return mainFunctionLimsGetPDFList(bsNumber)
    }
}