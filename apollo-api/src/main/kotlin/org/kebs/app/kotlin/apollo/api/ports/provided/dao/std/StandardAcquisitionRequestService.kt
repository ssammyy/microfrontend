package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std


import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRoleAssignmentsRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserRolesRepository
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.*


@Service
class StandardAcquisitionRequestService(
    private val notifications: Notifications,
    val commonDaoServices: CommonDaoServices,
    private val applicationMapProperties: ApplicationMapProperties,
    private val userRolesRepo: IUserRolesRepository,
    private val userRolesAssignRepo: IUserRoleAssignmentsRepository,
    private val usersRepo: IUserRepository,
    private val standardAcquisitionRepository: StandardAcquisitionRepository,

    ) {

    var appId = applicationMapProperties.mapStandardsDevelopment


    //Request For Standard
    fun requestForStandard(standardAcquisitionRequest: StandardAcquisitionRequest): ServerResponse {

        //check if user had requested  to prevent double entry
        standardAcquisitionRequest.email?.let {
            standardAcquisitionRequest.standardNameRequested?.let { it2 ->
                standardAcquisitionRequest.phone?.let { it1 ->
                    standardAcquisitionRepository.findAllByEmailAndPhoneAndStandardNameRequested(
                        it,
                        it1,
                        it2
                    )
                        ?.let {
                            // throw InvalidValueException("You Have Already Voted")
                            return ServerResponse(
                                HttpStatus.OK,
                                "Requested", "You Have Already Requested For A Similar Standard"
                            )

                        }
                }
            }
        }
            ?: run {


                standardAcquisitionRequest.date = Timestamp(System.currentTimeMillis())

                standardAcquisitionRepository.save(standardAcquisitionRequest)
                notifications.sendEmail(
                    standardAcquisitionRequest.email!!,
                    "Standard Request Submission",
                    "Hello " + standardAcquisitionRequest.name!! + ",\n We have received your request for a standard. An email has been sent to your employer to confirm this: "
                )

                sendEmailToEmployerForApproval(standardAcquisitionRequest)

                return ServerResponse(
                    HttpStatus.OK,
                    " Request Successful", "Request Successful"
                )


            }


    }

    private fun sendEmailToEmployerForApproval(standardAcquisitionRequest: StandardAcquisitionRequest) {
        val encryptedId = BCryptPasswordEncoder().encode(standardAcquisitionRequest.requestId.toString())
        standardAcquisitionRequest.encryptedId = encryptedId
        standardAcquisitionRequest.emailSentToEmployer = "1"
        standardAcquisitionRepository.save(standardAcquisitionRequest)

        val link =
            "${applicationMapProperties.baseUrlQRValue}approveApplication?applicationID=${standardAcquisitionRequest.encryptedId}"
        val messageBody =
            " Hello ${standardAcquisitionRequest.employerCompanyName} \n A Request For Standard ${standardAcquisitionRequest.standardNameRequested} has been made by ${standardAcquisitionRequest.name}." +
                    " Please click on the following link to confirm request \n " +
                    link +
                    "\n\n\n\n\n\n"
        standardAcquisitionRequest.email?.let { notifications.sendEmail(it, "Employer Approval", messageBody) }


    }

    fun checkIfValid(
        applicationID: String
    ): ResponseEntity<String> {

        val u: StandardAcquisitionRequest? = standardAcquisitionRepository.findAllByEncryptedId(applicationID)
        return if (u != null) {
            if (u.approvedByEmployer.equals("1")) {
                ResponseEntity.ok("This Link Has Already Been Used");

            } else {

                ResponseEntity.ok("OK");
            }

        } else {
            throw ExpectedDataNotFound("This is not valid")
        }

    }


    fun finalizeRequest(standardAcquisitionRequest: StandardAcquisitionRequest): ServerResponse {
        val selectedSchemeMembershipRequest =
            standardAcquisitionRequest.encryptedId?.let { standardAcquisitionRepository.findAllByEncryptedId(it) }
        if (selectedSchemeMembershipRequest != null) {
            selectedSchemeMembershipRequest.approvedByEmployer = "1"
            selectedSchemeMembershipRequest.employerCompanyName = standardAcquisitionRequest.employerCompanyName
            selectedSchemeMembershipRequest.employerName = standardAcquisitionRequest.employerName
            selectedSchemeMembershipRequest.employerOccupation = standardAcquisitionRequest.employerOccupation
            selectedSchemeMembershipRequest.employerAddress = standardAcquisitionRequest.employerAddress
            selectedSchemeMembershipRequest.employerTelephoneNo = standardAcquisitionRequest.employerTelephoneNo
            selectedSchemeMembershipRequest.employerApprovalDate = Timestamp(System.currentTimeMillis())
            standardAcquisitionRepository.save(selectedSchemeMembershipRequest)
            return ServerResponse(
                HttpStatus.OK,
                " Request Successful", "Request Successful"
            )

        } else {
            return ServerResponse(
                HttpStatus.OK,
                " Request UnSuccessful", "Please Try Again Later"
            )
        }

    }


    //HOD tasks
    //request task list retrieval
    fun getHodTasks(): List<StandardAcquisitionRequest> {
        return standardAcquisitionRepository.findAllBySicAssignedIdIsNullAndApprovedByEmployerIsNotNull()
    }


    //request task list retrieval
    fun getHodTasksUnassigned(): List<StandardAcquisitionRequest> {
        return standardAcquisitionRepository.findAllBySicAssignedIdIsNull()
    }

    //request task list retrieval
    fun getHodTasksAssigned(): List<StandardAcquisitionRequest?>? {
        return standardAcquisitionRepository.findAllBySicAssignedIdIsNotNull()
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
    fun assignSICOfficer(standardAcquisitionRequest: StandardAcquisitionRequest) {
        val selectedStandardAcquisitionRequest =
            standardAcquisitionRepository.findAllByRequestId(standardAcquisitionRequest.requestId)
        selectedStandardAcquisitionRequest.sicAssignedId = standardAcquisitionRequest.sicAssignedId
        selectedStandardAcquisitionRequest.sicAssignedDateAssigned = Timestamp(System.currentTimeMillis())
        standardAcquisitionRepository.save(selectedStandardAcquisitionRequest)
    }

    fun getSicLoggedInTasks(): List<StandardAcquisitionRequest> {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return standardAcquisitionRepository.findAllBySicAssignedId(loggedInUser.id.toString())
    }





}
