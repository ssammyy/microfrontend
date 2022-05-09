//package org.kebs.app.kotlin.apollo.api.ports.provided.dao
//
//import kotlinx.coroutines.runBlocking
//import mu.KotlinLogging
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.kebs.app.kotlin.apollo.common.dto.brs.response.BrsLookUpRecords
//import org.kebs.app.kotlin.apollo.common.dto.brs.response.BrsLookUpResponse
//import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
//import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
//import org.kebs.app.kotlin.apollo.store.model.BrsLookupManufacturerDataEntity
//import org.kebs.app.kotlin.apollo.store.model.BrsLookupManufacturerPartnersEntity
//import org.kebs.app.kotlin.apollo.store.model.IntegrationConfigurationEntity
//import org.kebs.app.kotlin.apollo.store.repo.IBrsLookupManufacturerDataRepository
//import org.kebs.app.kotlin.apollo.store.repo.IBrsLookupManufacturerPartnersRepository
//import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
//import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.test.context.junit.jupiter.SpringExtension
//import java.sql.Date
//import java.sql.Timestamp
//import java.text.SimpleDateFormat
//import java.time.Instant
//
//@ExtendWith(SpringExtension::class)
//@SpringBootTest
//class BrsLookupTests {
//    @Autowired
//    lateinit var daoService: RegistrationDaoServices
//
//    @Autowired
//    lateinit var configurationRepository: IIntegrationConfigurationRepository
//
//
//    @Test
//    fun givenInvalidDetailsDenyRegistrationTest() {
//
//        val registrationNumber = "CPR/2011/53201"
//        val directorIdNumber = "28377639"
//        val result = daoService.brsValidationLookup(registrationNumber, directorIdNumber)
//        KotlinLogging.logger { }
//            .trace("Overall matched = ${result.first} and record = ${result.second?.businessName}")
//
//    }
//
//
//}
