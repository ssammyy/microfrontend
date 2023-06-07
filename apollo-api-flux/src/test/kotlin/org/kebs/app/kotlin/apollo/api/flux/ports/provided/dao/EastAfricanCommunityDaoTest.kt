package org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao

import kotlinx.coroutines.runBlocking
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.common.dto.eac.requests.CertifiedProduct
import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.ICertifiedProductsDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class EastAfricanCommunityDaoTest {
    @Autowired
    lateinit var integRepo: IIntegrationConfigurationRepository

    @Autowired
    lateinit var jobsRepo: IBatchJobDetailsRepository

    @Autowired
    lateinit var certifiedProductRepo: ICertifiedProductsDetailsRepository

    @Autowired
    lateinit var service: EastAfricanCommunityDao

    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor

    @Test
    fun postProductTest() {
        integRepo.findByIdOrNull(27L)
            ?.let { config ->
                runBlocking {
                    jobsRepo.findByIdOrNull(8L)
                        ?.let { job ->
                            certifiedProductRepo.findByIdOrNull(1L)
                                ?.let { p ->
                                    val data = CertifiedProduct()
                                    data.accessToken = config.token
                                    data.secret = jasyptStringEncryptor.decrypt(config.secretValue)
                                    data.productName = p.productName
                                    data.brandName = p.brandName
                                    data.countryOfOrigin = p.countryOfOrigin
                                    data.hsCode = p.hsCode
                                    data.standardGoverning = p.standardGoverning
                                    data.dateMarkIssued = p.dateMarkIssued
                                    data.dateMarkedExpires = p.dateMarkedExpires
                                    data.productDescription = p.productDescription
                                    data.permitNumber = p.permitNumber
                                    data.productReference = p.productReference
                                    data.agency = p.agency
                                    data.regulationStatus = p.regulationStatus
                                    data.productState = p.productState

                                    service.postProduct(data, config, job)

                                }

                        }

                }
            }

    }

    @Test
    fun generateTokenTest() {
        integRepo.findByIdOrNull(27L)
            ?.let { config ->
                runBlocking {
                    service.generateToken(config)
                }
            }

    }
}
