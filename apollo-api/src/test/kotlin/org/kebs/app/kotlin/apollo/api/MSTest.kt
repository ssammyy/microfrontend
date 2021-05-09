package org.kebs.app.kotlin.apollo.api

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.UpAndDownLoad
import org.kebs.app.kotlin.apollo.api.service.UserRolesService
import org.kebs.app.kotlin.apollo.common.dto.HashListDto
import org.kebs.app.kotlin.apollo.common.dto.ms.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.IDestinationInspectionFeeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@SpringBootTest
@RunWith(SpringRunner::class)
class MSTest {
    @Autowired
    lateinit var destinationInspectionDaoServices: DestinationInspectionDaoServices

    @Autowired
    lateinit var consignmentDocumentDaoService: ConsignmentDocumentDaoService

    @Autowired
    lateinit var invoiceDaoService: InvoiceDaoService

    @Autowired
    lateinit var marketSurveillanceDaoServices: MarketSurveillanceDaoServices

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var idfsEntityRepository: IdfsEntityRepository

    @Autowired
    lateinit var cocRepository: ICocsRepository

    @Autowired
    lateinit var coisRepository: ICoisRepository

    @Autowired
    lateinit var corsEntityRepository: ICorsBakRepository

    @Autowired
    lateinit var usersRepo: IUserRepository

    @Autowired
    lateinit var iDIFeeDetailsRepo: IDestinationInspectionFeeRepository

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var upAndDownLoad: UpAndDownLoad

    @Autowired
    lateinit var userRolesService: UserRolesService

    @Autowired
    lateinit var pvocReconciliationReportEntityRepo: PvocReconciliationReportEntityRepo



    @Test
    fun changeToJsonObject() {
      val msType =  JSONObject(ObjectMapper().writeValueAsString(MSTypeDto()))
      val complaint =  JSONObject(ObjectMapper().writeValueAsString(ComplaintDto()))
        val comlantdetails = NewComplaintDto(
                ComplaintDto(),
                ComplaintCustomersDto(),
                ComplaintLocationDto()
//                ComplaintFilesDto()
        )
      val newComplaintDto =  JSONObject(ObjectMapper().writeValueAsString(comlantdetails))
      val complaintCustomerDetails =  JSONObject(ObjectMapper().writeValueAsString(ComplaintCustomersDto()))
      val complaintLocation =  JSONObject(ObjectMapper().writeValueAsString(ComplaintLocationDto()))
      val hashedStringDto =  JSONObject(ObjectMapper().writeValueAsString(HashListDto()))

        KotlinLogging.logger { }.info { "msType = $msType " }
        KotlinLogging.logger { }.info { "complaint = $complaint " }
        KotlinLogging.logger { }.info { "complaintCustomerDetails = $complaintCustomerDetails " }
        KotlinLogging.logger { }.info { "complaintLocation = $complaintLocation " }
        KotlinLogging.logger { }.info { "COmplaint dTO = $newComplaintDto" }
        KotlinLogging.logger { }.info { "hashedStringDto dTO = $hashedStringDto" }

    }

    @Test
    fun complaintDetails() {
        val appId = applicationMapProperties.mapMarketSurveillance
        val map = commonDaoServices.serviceMapDetails(appId)
        val complaint =marketSurveillanceDaoServices.msComplaint("REF202101177E3528",map)
        KotlinLogging.logger { }.info { "complaint = $complaint " }
    }

    @Test
    fun testUserIdReturned(){
       val userId = userRolesService.getUserId("PVOC_APPLICATION_READ")
        KotlinLogging.logger {  }.info { "This is userId ==>" +userId }
    }




}
