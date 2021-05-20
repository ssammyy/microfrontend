package org.kebs.app.kotlin.apollo.api.ports.provided.lims

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.binary.Base64
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.response.RootTestResultsAndParameters
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.response.TestParameter
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.response.TestResult
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdRiskDetailsActionDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleLabTestParametersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleLabTestResultsEntity
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestParametersRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestResultsRepository
import org.springframework.stereotype.Service
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection


@Service
class LimsServices(
    private val commonDaoServices: CommonDaoServices,
    private val jasyptStringEncryptor: StringEncryptor,
    private val daoService: DaoService,
    private val applicationMapProperties: ApplicationMapProperties,
    private val sampleLabTestResults: IQaSampleLabTestResultsRepository,
    private val sampleLabTestParameters: IQaSampleLabTestParametersRepository,
    private val configurationRepository: IIntegrationConfigurationRepository
) {

    fun performPostCall(
        postDataParams: HashMap<String, String>
    ): String? {
        val url: URL
        var response: String? = ""
        try {
            val appId = applicationMapProperties.mapLimsTransactions
            val map = commonDaoServices.serviceMapDetails(appId)
            val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapLimsConfigIntegration)

            url = URL(config.url)
            val b = Base64()
            val encoding: String = b.encodeAsString(("${jasyptStringEncryptor.decrypt(config.username)}:${jasyptStringEncryptor.decrypt(config.password)}").toByteArray())

            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 15000
            conn.connectTimeout = 15000
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true
            conn.setRequestProperty  ("Authorization", "Basic $encoding");
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

    fun labResponseResults(response: String){
        val resultsParam: RootTestResultsAndParameters = ObjectMapper().readValue(response, RootTestResultsAndParameters::class.java)
        //Loop
        resultsParam.test_result
            ?.forEach { testResults ->
                limsTestResultsDetails(testResults)
            }
        //Loop
        resultsParam.test_parameter
            ?.forEach { testParam ->
                limsTestParamsDetails(testParam)
            }
    }

    fun mainFunctionLims(bsNumber: String): Boolean{
        var results = false
        val hmap = HashMap<String, String>()
        hmap["bsnumber"] = bsNumber
        val myResults = performPostCall(hmap)
        if (myResults != null) {
            labResponseResults(myResults)
            results= true
        }
        return results
    }


    fun limsTestResultsDetails(
        testResults: TestResult
    ): QaSampleLabTestResultsEntity {
        val testResultsDetails = QaSampleLabTestResultsEntity()
        with(testResultsDetails) {
            orderId =testResults.orderID
            sampleNumber =testResults.sampleNumber
            test =testResults.test
            param =testResults.param
            sortOrder =testResults.sortOrder
            method =testResults.method
            result =testResults.result
            numericResult =testResults.numericResult
            units =testResults.units
            dilution =testResults.dilution
            analysisVolume =testResults.analysisVolume
            sampleType =testResults.sampleType
            qualifier =testResults.qualifier
            repLimit =testResults.repLimit
            retentionTime =testResults.retentionTime
            tic =testResults.tIC
            resultStatus =testResults.resultStatus
            enteredDate =testResults.enteredDate
            enteredBy =testResults.enteredBy
            validatedDate =testResults.validatedDate
            validatedBy =testResults.validatedBy
            approvedDate =testResults.approvedDate
            approvedBy =testResults.approvedBy
            approvedReason =testResults.approvedReason
            commnt =testResults.commnt
            measuredResult =testResults.measuredResult
            percentMoisture =testResults.percentMoisture
            methodVolume =testResults.methodVolume
            extractVolume =testResults.extractVolume
            methodExtractVolume =testResults.methodExtractVolume
            instrument =testResults.instrument
            resultsUser1 =testResults.results_User1
            resultsUser2 =testResults.results_User2
            resultsUser3 =testResults.results_User3
            resultsUser4 =testResults.results_User4
            report =testResults.report
            paramAnalyst =testResults.ts
            paramAnalysisDateTime =testResults.paramAnalyst
            printed =testResults.paramAnalysisDateTime
            printedAt =testResults.printed
            ts =testResults.printedAt
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
            orderId =testParams.orderID
            sampleNumber =testParams.sampleNumber
            test =testParams.test
            matrix =testParams.matrix
            method =testParams.method
            testGroup =testParams.testGroup
            priority =testParams.priority
            currentDepartment =testParams.currentDepartment
            lastDepartment =testParams.lastDepartment
            reDepartment =testParams.rEDepartment
            testPrice =testParams.testPrice
            customerTestPrice =testParams.customerTestPrice
            dueDate =testParams.dueDate
            dueDateFlag =testParams.dueDateFlag
            prepDueDate =testParams.prepDueDate
            prepMethod =testParams.prepMethod
            analysisDueDate =testParams.analysisDueDate
            analysisTime =testParams.analysisTime
            analysisEmployee =testParams.analysisEmployee
            keepTest =testParams.keepTest
            cancelled =testParams.cancelled
            hasResults =testParams.hasResults
            supplyReconciled =testParams.supplyReconciled
            customParams =testParams.customParams
            preservative =testParams.preservative
            bottleType =testParams.bottleType
            storageLocation =testParams.storageLocation
            sampleDetailsUser1 =testParams.sampleDetails_User1
            sampleDetailsUser2 =testParams.sampleDetails_User2
            sampleDetailsUser3 =testParams.sampleDetails_User3
            sampleDetailsUser4 =testParams.sampleDetails_User4
            ts =testParams.ts
            status = 1
            createdBy = "ADMIN"
            createdOn = commonDaoServices.getTimestamp()
        }
        return sampleLabTestParameters.save(testParamsDetails)
    }
}