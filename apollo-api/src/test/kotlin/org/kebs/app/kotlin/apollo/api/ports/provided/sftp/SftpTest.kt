package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import org.apache.commons.io.FileUtils
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.HttpClientBuilder
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.customdto.*
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import org.kebs.app.kotlin.apollo.store.repo.ICorsBakRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import kotlin.test.assertNotNull
import org.kebs.app.kotlin.apollo.store.model.CocsBakEntity
import org.kebs.app.kotlin.apollo.store.model.CoisEntity
import org.kebs.app.kotlin.apollo.store.repo.ICocsBakRepository
import org.kebs.app.kotlin.apollo.store.repo.ICocItemRepository
import org.kebs.app.kotlin.apollo.store.repo.ICoisRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
@RunWith(SpringRunner::class)
class SftpTest {

    @Autowired
    lateinit var sftpService: SftpServiceImpl

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var destinationInspectionDaoServices: DestinationInspectionDaoServices

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var iCorsBakRepository: ICorsBakRepository

    @Autowired
    lateinit var iCocsBakRepository: ICocsBakRepository

    @Autowired
    lateinit var iCoisBakRepository: ICoisRepository

    @Autowired
    lateinit var iDemandNoteRepo: IDemandNoteRepository

    @Autowired
    lateinit var iCocItemRepository: ICocItemRepository

    private val baseUrl = "https://127.0.0.1:8006"
    private val downloadKeswsFilesUrl = "$baseUrl/api/sftp/kesws/download"

    @Test
    fun whenUploadFileUsingJsch_thenSuccess() {
        val localFile = "src/main/resources/COR-COR20210311-1.xml"
        val newFile = File(localFile)
        FileUtils.touch(newFile)

        assertTrue(sftpService.uploadFile(newFile))
    }

    @Test
    fun testFileListReturnedOnDownloadUsingJsch() {
        val filesList = sftpService.downloadFilesByDocType(applicationMapProperties.mapKeswsCorDoctype)
        assertTrue(filesList.isNotEmpty())
    }

    @Test
    fun givenAllFilesAreDownloaded_then200IsReceived() {
        //Given
        val request: HttpUriRequest = HttpGet(downloadKeswsFilesUrl)
        // When
        val httpResponse = HttpClientBuilder.create().build().execute(request)
        //Then
        assertThat(httpResponse.statusLine.statusCode, equalTo(HttpStatus.SC_OK))
    }

    @Test
    fun whenCorpojoSerializedToXmlFile_thenCorrect() {
        val corId: Long = 641

        val corsBakEntity: CorsBakEntity = iCorsBakRepository.findById(corId).get()
        corsBakEntity.let {
            val cor: CustomCorXmlDto = it.toCorXmlRecordRefl()
            val corDto = CORXmlDTO()
            corDto.cor = cor

            val fileName = corDto.cor?.chasisNumber?.let {
                commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsCorDoctype,
                    it
                )
            }

            val xmlFile = fileName?.let { commonDaoServices.serializeToXml(it, corDto) }

            xmlFile?.let { it1 -> sftpService.uploadFile(it1) }
        }
    }

    @Test
    fun whenCocpojoSerializedToXmlFile_thenCorrect() {
        val cocId: Long = 1282

        val cocsBakEntity: CocsBakEntity = iCocsBakRepository.findById(cocId).get()
        cocsBakEntity.let {
            val coc: CustomCocXmlDto = it.toCocXmlRecordRefl()
            //COC ITEM
            val cocItem = iCocItemRepository.findByCocId(cocsBakEntity.id)?.get(0)
            cocItem?.toCocItemDetailsXmlRecordRefl().let {
                coc.cocDetals = it
                val cocFinalDto = COCXmlDTO()
                cocFinalDto.coc = coc

                val fileName = cocFinalDto.coc?.ucrNumber?.let {
                    commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsCocDoctype,
                        it
                    )
                }

                val xmlFile = fileName?.let { commonDaoServices.serializeToXml(it, cocFinalDto) }

                xmlFile?.let { it1 -> sftpService.uploadFile(it1) }

            }

        }
    }

//    @Test
//    fun whenCoipojoSerializedToXmlFile_thenCorrect() {
//        val coiId: Long = 321
//
//        val coisEntity: CoisEntity = iCoisBakRepository.findById(coiId).get()
//        coisEntity.let {
//            val coi: CustomCoiXmlDto = it.toCoiXmlRecordRefl()
//            val coiFinalDto = COIXmlDTO()
//            coiFinalDto.coi = coi
//
//            val fileName = coiFinalDto.coi?.ucrNumber?.let {
//                commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsCoiDoctype, it)
//            }
//
//            val xmlFile = fileName?.let { commonDaoServices.serializeToXml(it, coiFinalDto) }
//
//            xmlFile?.let { it1 -> sftpService.uploadFile(it1) }
//
//        }
//    }

    @Test
    fun whenDemandNotepojoSerializedToXmlFile_thenCorrect() {
        val coiId: Long = 641

        destinationInspectionDaoServices.sendDemandNotGeneratedToKWIS(coiId)
    }


    @Test
    fun whenDemandNotePaypojoSerializedToXmlFile_thenCorrect() {
        val coiId: Long = 641
        destinationInspectionDaoServices.sendDemandNotePayedStatusToKWIS(coiId)

    }


//    @Test
//    fun whenCDApprovalpojoSerializedToXmlFile_thenCorrect() {
//        val current = LocalDateTime.now()
//        val expiryDate = current.plusYears(1)
//
//        val formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss")
//        val formatted = current.format(formatter)
//        val formattedExipry = expiryDate.format(formatter)
//
//        val cdNumber = "2021CDOC0000038869"
//        val messageDate = formatted
//
//        val docHeader = DocumentHeader(cdNumber, messageDate)
//        val docDetails = DocumentDetails(cdNumber, "1000", formattedExipry, messageDate, "AP")
//        val cdApprovalResponseDTO = CDApprovalResponseDTO()
//        cdApprovalResponseDTO.documentHeader = docHeader
//        cdApprovalResponseDTO.documentDetails = docDetails
//
//        val fileName = commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsCdApprovalDoctype, cdNumber)
//        val xmlFile = fileName.let { commonDaoServices.serializeToXml(it, cdApprovalResponseDTO) }
//        xmlFile.let { it1 -> sftpService.uploadFile(it1) }
//    }

    /*
    Test KRA verification request submission
     */
    @Test
    fun whenVerificationRequestpojoSerializedToXmlFile_thenCorrect() {

        val declarationNo = "DEC20210319BNZ"

        val cdVerificationRequestDataSAD = CdVerificationRequestDataSAD()
        val cdVerificationRequestDataIn = CdVerificationRequestDataIn()
        cdVerificationRequestDataIn.cdVerificationRequestDataSAD = cdVerificationRequestDataSAD
        val cdVerificationRequestData = CdVerificationRequestData()
        cdVerificationRequestData.cdVerificationRequestDataIn = cdVerificationRequestDataIn
        val cdVerificationRequestHeader = CdVerificationRequestHeader()

        val cdVerificationRequestXmlDTO = CDVerificationRequestXmlDTO()
        cdVerificationRequestXmlDTO.cdVerificationRequestHeader = cdVerificationRequestHeader
        cdVerificationRequestXmlDTO.cdVerificationRequestData = cdVerificationRequestData

        val fileName = commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsDemandNoteDoctype, declarationNo)

        val xmlFile = fileName.let { commonDaoServices.serializeToXml(fileName, cdVerificationRequestXmlDTO) }

        xmlFile.let { it1 -> sftpService.uploadFile(it1) }
    }

    @Test
    fun testKeswsDateConversion() {
        val cocId: Long = 861

        val cocsBakEntity: CocsBakEntity = iCocsBakRepository.findById(cocId).get()
        cocsBakEntity.let {
            val keswsDate = it.cocIssueDate?.let { it1 -> commonDaoServices.convertTimestampToKeswsValidDate(it1) }
        }
    }
}
