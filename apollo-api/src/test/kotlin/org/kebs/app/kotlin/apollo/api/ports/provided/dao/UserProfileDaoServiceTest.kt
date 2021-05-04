package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.flowable.common.engine.impl.interceptor.SessionFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.ports.provided.criteria.SearchCriteria
import org.kebs.app.kotlin.apollo.api.ports.provided.spec.UserSpecification
import org.kebs.app.kotlin.apollo.common.dto.UserSearchValues
import org.kebs.app.kotlin.apollo.store.model.DirectorateToSubSectionL2ViewDto
import org.kebs.app.kotlin.apollo.store.model.RegionsCountyTownViewDto
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.test.expect


@ExtendWith(SpringExtension::class)
@SpringBootTest
class UserProfileDaoServiceTest {
    @Autowired
    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var registrationDaoServices: RegistrationDaoServices

    @Autowired
    lateinit var usersRepo: IUserRepository

//    @Autowired
//    lateinit var sessionFactory: SessionFactory

    @Test
    fun initialIntegrationTest() {

        val list: List<RegionsCountyTownViewDto> = entityManager.createNamedQuery(RegionsCountyTownViewDto.FIND_ALL, RegionsCountyTownViewDto::class.java).resultList.filter { it.townId != null }
        expect(list.isEmpty(), "Empty List found", { false })
        list.forEach {
            KotlinLogging.logger { }.info("Record found: ${it.townId} ${it.county}")
        }
    }

    @Test
    fun initialDirectorateToSubSectionL2ViewDtoTest() {
        val list = entityManager.createNamedQuery(DirectorateToSubSectionL2ViewDto.FIND_ALL, DirectorateToSubSectionL2ViewDto::class.java).resultList
        list.forEach { l -> KotlinLogging.logger { }.info("Record found: ${l.department} ${l.directorate}") }
    }

    @Test
    fun testBrs(){
//        usersRepo.findByIdOrNull(1464)?.let { user ->
//            registrationDaoServices.checkBrs(user)
//        }
    }

    @Test
    fun returnUserListGivenSearchFormParameterTest() {
        val search = UserSearchValues("vmuriuki", "vincentmuriuki42@gmail.com", "Vincent")
//        val list = usersRepo.findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search.userName, search.email, search.firstName, search.lastName)
//        list?.forEach { l -> KotlinLogging.logger { }.info("${l.id} ${l.userName} ${l.email} ${l.firstName} ${l.lastName}") }

//        val query = "select * from DAT_KEBS_USERS where USER_NAME = :userName and EMAIL= :email and FIRST_NAME = :firstName and LAST_NAME = :lastName"
//        val list: List<UsersEntity> = entityManager.createNativeQuery(query)
//            .setParameter("userName", search.userName)
//            .setParameter("email", search.email)
//            .setParameter("firstName", search.firstName)
//            .setParameter("lastName", search.lastName)
//            .resultList as List<UsersEntity>
//        list.forEach { l -> KotlinLogging.logger { }.info(l.userName) }

        val spec = UserSpecification(SearchCriteria("email", ":", search.email))
        val spec4 = UserSpecification(SearchCriteria("userName", ":", search.userName))
        val spec2 = UserSpecification(SearchCriteria("firstName", ":", search.firstName))
        val spec3 = UserSpecification(SearchCriteria("lastName", ":", search.lastName))
        val other = usersRepo.findAll(spec.or(spec2).or(spec3).or(spec4))
        other.forEach { l -> KotlinLogging.logger { }.info("${l.id} ${l.userName} ${l.email} ${l.firstName} ${l.lastName}") }


    }

}

