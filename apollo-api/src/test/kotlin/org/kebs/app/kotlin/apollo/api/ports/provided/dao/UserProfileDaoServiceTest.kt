package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import com.fasterxml.jackson.core.type.TypeReference
import mu.KotlinLogging
//import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.ports.provided.criteria.SearchCriteria
import org.kebs.app.kotlin.apollo.api.ports.provided.spec.UserSpecification
import org.kebs.app.kotlin.apollo.common.dto.SideBarMainMenusEntityDto
import org.kebs.app.kotlin.apollo.common.dto.UserSearchValues
import org.kebs.app.kotlin.apollo.store.model.DirectorateToSubSectionL2ViewDto
import org.kebs.app.kotlin.apollo.store.model.RegionsCountyTownViewDto
import org.kebs.app.kotlin.apollo.store.model.SidebarChildrenEntity
import org.kebs.app.kotlin.apollo.store.model.SidebarMainEntity
import org.kebs.app.kotlin.apollo.store.repo.ISidebarChildrenRepository
import org.kebs.app.kotlin.apollo.store.repo.ISidebarMainRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.Timestamp
import java.time.Instant
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import org.junit.Test


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

    @Autowired
    lateinit var daoService: DaoService

    @Autowired
    lateinit var sideBarMainRepository: ISidebarMainRepository

    @Autowired
    lateinit var sideBarChildrenRepository: ISidebarChildrenRepository

//    @Autowired
//    lateinit var sessionFactory: SessionFactory

    @Test
    fun initialIntegrationTest() {

        val list: List<RegionsCountyTownViewDto> = entityManager.createNamedQuery(
                RegionsCountyTownViewDto.FIND_ALL,
                RegionsCountyTownViewDto::class.java
        ).resultList.filter { it.townId != null }
//        expect(list.isEmpty(), "Empty List found", { false })
        list.forEach {
            KotlinLogging.logger { }.info("Record found: ${it.townId} ${it.county}")
        }
    }

    @Test
    fun initialDirectorateToSubSectionL2ViewDtoTest() {
        val list = entityManager.createNamedQuery(
                DirectorateToSubSectionL2ViewDto.FIND_ALL,
                DirectorateToSubSectionL2ViewDto::class.java
        ).resultList
        list.forEach { l -> KotlinLogging.logger { }.info("Record found: ${l.department} ${l.directorate}") }
    }

    @Test
    fun testBrs() {
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
        other.forEach { l ->
            KotlinLogging.logger { }.info("${l.id} ${l.userName} ${l.email} ${l.firstName} ${l.lastName}")
        }


    }

    @Test
    fun loadAndSaveSideBarToDatastoreGivenRawJsonTest() {
        val json = "[\n" +
                "  {\n" +
                "    \"path\": \"/dashboard\",\n" +
                "    \"title\": \"Dashboard\",\n" +
                "    \"type\": \"link\",\n" +
                "    \"icontype\": \"dashboard\",\n" +
                "    \"children\": [\n" +
                "\n" +
                "    ]\n" +
                "  },\n" +
                "\n" +
                "  {\n" +
                "    \"path\": \"/company\",\n" +
                "    \"title\": \"My Companies\",\n" +
                "    \"type\": \"sub\",\n" +
                "    \"icontype\": \"business\",\n" +
                "    \"collapse\": \"company\",\n" +
                "    \"children\": [\n" +
                "      {\"path\": \"companies\", \"title\": \"View Companies\", \"ab\": \"VC\"},\n" +
                "      {\"path\": \"users\", \"title\": \"View Users \", \"ab\": \"VU\"}\n" +
                "    ]\n" +
                "  },\n" +
                "\n" +
                "  {\n" +
                "    \"path\": \"/fmark\",\n" +
                "    \"title\": \"Fortification Mark\",\n" +
                "    \"type\": \"sub\",\n" +
                "    \"icontype\": \"recommended\",\n" +
                "    \"collapse\": \"fmark\",\n" +
                "    \"children\": [\n" +
                "      {\"path\": \"application\", \"title\": \"Make Application\", \"ab\": \"MA\"},\n" +
                "      {\"path\": \"fMarkAllApp\", \"title\": \"All My Applications\", \"ab\": \"AMA\"},\n" +
                "      {\"path\": \"panels\", \"title\": \"Awarded Applications\", \"ab\": \"AA\"}\n" +
                "    ]\n" +
                "  }, {\n" +
                "  \"path\": \"/dmark\",\n" +
                "  \"title\": \"Diamond Mark\",\n" +
                "  \"type\": \"sub\",\n" +
                "  \"icontype\": \"verified\",\n" +
                "  \"collapse\": \"forms\",\n" +
                "  \"children\": [\n" +
                "    {\"path\": \"newDmarkPermit\", \"title\": \"Make Application\", \"ab\": \"MA\"},\n" +
                "    {\"path\": \"all_dmark\", \"title\": \"All My Applications\", \"ab\": \"AMA\"},\n" +
                "    {\"path\": \"panels\", \"title\": \"Awarded Applications\", \"ab\": \"AA\"}\n" +
                "  ]\n" +
                "}, {\n" +
                "  \"path\": \"/smark\",\n" +
                "  \"title\": \"Standardization Mark\",\n" +
                "  \"type\": \"sub\",\n" +
                "  \"icontype\": \"class\",\n" +
                "  \"collapse\": \"tables\",\n" +
                "  \"children\": [\n" +
                "    {\"path\": \"newSmarkPermit\", \"title\": \"Make Application\", \"ab\": \"MA\"},\n" +
                "    {\"path\": \"all_smark\", \"title\": \"All My Applications\", \"ab\": \"AMA\"},\n" +
                "    {\"path\": \"panels\", \"title\": \"Awarded Applications\", \"ab\": \"AA\"}\n" +
                "  ]\n" +
                "}, {\n" +
                "  \"path\": \"/invoice\",\n" +
                "  \"title\": \"Invoices\",\n" +
                "  \"type\": \"sub\",\n" +
                "  \"icontype\": \"receipt\",\n" +
                "  \"collapse\": \"invoice\",\n" +
                "  \"children\": [\n" +
                "    {\"path\": \"all_invoice\", \"title\": \"All Invoices\", \"ab\": \"AI\"},\n" +
                "    {\"path\": \"consolidate_invoice\", \"title\": \"Consolidate Invoices\", \"ab\": \"CI\"}\n" +
                "  ]\n" +
                "},\n" +
                "{\n" +
                "  \"path\": \"/pvoc\",\n" +
                "  \"title\": \"Exports\",\n" +
                "  \"type\": \"sub\",\n" +
                "  \"icontype\": \"receipt\",\n" +
                "  \"collapse\": \"pvoc\",\n" +
                "  \"children\": [\n" +
                "    {\"path\": \"waivers\", \"title\": \"Export Waivers\", \"ab\": \"EW\"},\n" +
                "    {\"path\": \"exceptions\", \"title\": \"Export Exceptions\", \"ab\": \"EE\"}\n" +
                "  ]\n" +
                "}" +
                "  {\n" +
                "    \"path\": \"/all_tasks_list\",\n" +
                "    \"title\": \"My Tasks\",\n" +
                "    \"type\": \"link\",\n" +
                "    \"icontype\": \"task\"\n" +
                "  }\n" +
                "]"

        val mainMenu: List<SideBarMainMenusEntityDto> =
                daoService.mapper().readValue(json, object : TypeReference<List<SideBarMainMenusEntityDto>>() {})

        KotlinLogging.logger { }.info("Size ${mainMenu.size}")
        mainMenu.forEach { m ->
            var entity = SidebarMainEntity().apply {
                status = 1
                path = m.path
                title = m.title
                type = m.type
                iconType = m.iconType
                collapse = m.collapse
                createdOn = Timestamp.from(Instant.now())
                createdBy = "Admin"
                roleId = 0

            }
            entity = sideBarMainRepository.save(entity)
            m.children?.forEach { c ->
                val child = SidebarChildrenEntity().apply {
                    roleId = entity.roleId
                    mainId = entity.id
                    status = 1
                    path = c.path
                    title = c.title
                    aB = c.ab
                    createdOn = Timestamp.from(Instant.now())
                    createdBy = "Admin"
                }
                sideBarChildrenRepository.save(child)
            }
        }


    }
}


