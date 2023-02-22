package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import org.flowable.engine.ProcessEngine
import org.flowable.engine.RepositoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.SchemeMembershipRequest
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRoleAssignmentsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRolesRepository
import org.kebs.app.kotlin.apollo.store.repo.std.SchemeMembershipRequestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class SchemeMembershipService(
    private val runtimeService: RuntimeService,
    private val taskService: TaskService,
    private val processEngine: ProcessEngine,
    private val repositoryService: RepositoryService,
    private val notifications: Notifications,
    val commonDaoServices: CommonDaoServices,
    applicationMapProperties: ApplicationMapProperties,
    private val userRolesRepo: IUserRolesRepository,
    private val userRolesAssignRepo: IUserRoleAssignmentsRepository,
    private val usersRepo: IUserRepository,

    private val schemeMembershipRequestRepository: SchemeMembershipRequestRepository
) {

    var PROCESS_DEFINITION_KEY: String = "schemeMembership"
    val TASK_CANDIDATE_GROUP_HOD_SEC = "SD_HEAD_OF_SIC"
    val TASK_CANDIDATE_GROUP_SIC_OFFICER = "SD_SIC_OFFICER"
    val sic_assignee = "3"
    var appId = applicationMapProperties.mapStandardsDevelopment


    //scheme membership join request
    fun joinRequest(schemeMembershipRequest: SchemeMembershipRequest): ServerResponse {

        //check if user had requested to join to prevent double entry
        schemeMembershipRequest.email?.let {
            schemeMembershipRequest.phone?.let { it1 ->
                schemeMembershipRequestRepository.findAllByEmailOrPhone(
                    it,
                    it1
                )
                    ?.let {
                        // throw InvalidValueException("You Have Already Voted")
                        return ServerResponse(
                            HttpStatus.OK,
                            "Registered", "You Have Already Registered"
                        )

                    }
            }
        }
            ?: run {


                schemeMembershipRequest.createdOn = Timestamp(System.currentTimeMillis())
                schemeMembershipRequestRepository.save(schemeMembershipRequest)
                notifications.sendEmail(
                    schemeMembershipRequest.email!!,
                    "Standard Request Submission",
                    "Hello " + schemeMembershipRequest.name!! + ",\n We have received your online scheme membership registration.: "
                )
                return ServerResponse(
                    HttpStatus.OK,
                    "Registration Request Successful", "Registration Request Successful"
                )


            }
    }

    //HOD tasks
    //request task list retrieval
    fun getHodTasks(): MutableIterable<SchemeMembershipRequest?>? {
        return schemeMembershipRequestRepository.findAll()
    }

    //request task list retrieval
    fun getHodTasksUnassigned(): List<SchemeMembershipRequest> {
        return schemeMembershipRequestRepository.findAllBySicAssignedIdIsNull()
    }

    //request task list retrieval
    fun getHodTasksAssigned(): List<SchemeMembershipRequest?>? {
        return schemeMembershipRequestRepository.findAllBySicAssignedIdIsNotNull()
    }

    // get all Sic officers
    fun getAllSICOfficers(): MutableList<UsersEntity> {
        val users: MutableList<UsersEntity> = ArrayList()

        userRolesRepo.findByRoleNameAndStatus("SIC_OFFICER_SD", 1)
            ?.let { role ->
                userRolesAssignRepo.findByRoleIdAndStatus(role.id, 1)
                    ?.let { roleAssigns ->
                        roleAssigns.forEach { roleAssign ->
                            usersRepo.findByIdOrNull(roleAssign.userId)
                                ?.let { user ->
                                    users.add(user)
                                }
                        }
                    }
                    ?: throw Exception("Role [id=${role.id}] not found, may not be active or assigned yet")

            }
            ?: throw Exception("User role name does not exist")
        return users
    }


    //HOD SEC assigns Join request to an SIC officer
    //  fun assignSicOfficer
    fun assignSICOfficer(schemeMembershipRequest: SchemeMembershipRequest) {
        val selectedSchemeMembershipRequest =
            schemeMembershipRequestRepository.findAllByRequestId(schemeMembershipRequest.requestId)
        selectedSchemeMembershipRequest.sicAssignedId = schemeMembershipRequest.sicAssignedId
        selectedSchemeMembershipRequest.sicAssignedDateAssigned = Timestamp(System.currentTimeMillis())
        schemeMembershipRequestRepository.save(selectedSchemeMembershipRequest)
    }

    fun getSicLoggedInTasks(): List<SchemeMembershipRequest> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return schemeMembershipRequestRepository.findAllBySicAssignedId(loggedInUser.id.toString())
    }


    fun generateInvoice(schemeMembershipRequest: SchemeMembershipRequest) {
        val map = commonDaoServices.serviceMapDetails(appId)

        val selectedSchemeMembershipRequest =
            schemeMembershipRequestRepository.findAllByRequestId(schemeMembershipRequest.requestId)
        selectedSchemeMembershipRequest.invoiceStatus = "GENERATED"
        selectedSchemeMembershipRequest.invoiceNumber = "KIMSREF${
            generateRandomText(
                3,
                map.secureRandom,
                map.messageDigestAlgorithm,
                true
            ).uppercase(Locale.getDefault())
        }"
        selectedSchemeMembershipRequest.invoiceGeneratedDate = Timestamp.from(Instant.now())

        if (selectedSchemeMembershipRequest.accountType == "Individual Membership") {
            selectedSchemeMembershipRequest.invoiceAmount = "3900"
        } else if (selectedSchemeMembershipRequest.accountType == "Corporate Membership") {
            selectedSchemeMembershipRequest.invoiceAmount = "14900"
        }


        schemeMembershipRequestRepository.save(selectedSchemeMembershipRequest)
        notifications.sendEmail(
            schemeMembershipRequest.email!!,
            "Invoice Generated",
            "Hello " + schemeMembershipRequest.name!! + "," +
                    "Thank you for your interest in KEBS services. Your request has been received and will be addressed shortly.\n" +
                    "Should you require a standard urgently, please consider purchasing it on the KEBS Webstore - https://webstore.kebs.org/\n" +
                    "Also note that you can purchase ISO standards on the ISOlutions Store - https://kebs.isolutions.iso.org" +
                    "\n We have generated an invoice for your online scheme membership registration." +
                    "\n Your Invoice Number: " + selectedSchemeMembershipRequest.invoiceNumber +
                    "\n Invoice Amount: " + selectedSchemeMembershipRequest.invoiceAmount +
                    "\n Please make your payment to complete registration."

        )

    }

    fun getAllPendingInvoices(): List<SchemeMembershipRequest> {
        return schemeMembershipRequestRepository.findAllByInvoiceStatus("GENERATED")
    }

    fun updatePayment(schemeMembershipRequest: SchemeMembershipRequest) {
        val selectedSchemeMembershipRequest =
            schemeMembershipRequestRepository.findAllByRequestId(schemeMembershipRequest.requestId)
        selectedSchemeMembershipRequest.invoiceStatus = "PAID"
        selectedSchemeMembershipRequest.invoicePaymentDate = Timestamp.from(Instant.now())
        schemeMembershipRequestRepository.save(selectedSchemeMembershipRequest)

    }

    fun getAllPaidInvoices(): List<SchemeMembershipRequest> {
        return schemeMembershipRequestRepository.findAllByInvoiceStatus("PAID")
    }

    fun addToWebStore(schemeMembershipRequest: SchemeMembershipRequest) {
        val selectedSchemeMembershipRequest =
            schemeMembershipRequestRepository.findAllByRequestId(schemeMembershipRequest.requestId)
        selectedSchemeMembershipRequest.varField1 = schemeMembershipRequest.varField1 //password field for user
        selectedSchemeMembershipRequest.webStoreAccountCreationDate = Timestamp.from(Instant.now())
        schemeMembershipRequestRepository.save(selectedSchemeMembershipRequest)
        notifications.sendEmail(
            selectedSchemeMembershipRequest.email!!,
            "Web Store Account Created",
            "Hello " + selectedSchemeMembershipRequest.name!! + "," +
                    "\n We have created your Web Store Account.Your Details are as follows." +
                    "\n Email: " + selectedSchemeMembershipRequest.email +
                    "\n Password: " + selectedSchemeMembershipRequest.varField1 +
                    "\n Please make sure to change your password."

        )

        notifications.sendEmail(
            selectedSchemeMembershipRequest.email!!,
            "Library Membership",
            "Hello " + selectedSchemeMembershipRequest.name!! + "," +
                    "\n Congratulations. You are now a member of the library. " +

                    "\n Membership Expires after 1 Year."

        )
    }


}
