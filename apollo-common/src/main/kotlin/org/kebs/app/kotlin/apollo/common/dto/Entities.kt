/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.common.dto


import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal
import java.sql.Date
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

open class BaseRequest(
    override var id: Long,
    override var name: String,
    override var description: String,
    override var status: Int,
) :
    BaseEntity<Long>(id, name, description, status) {
    constructor() : this(0L, "", "", 0)

    var spel: String = ""
    var type: String = ""
    var topic: String = ""
    var transactionReference: String = ""

}

data class BaseResponse(
    override var id: Long?,
    override var name: String,
    override var description: String,
    override var status: Int,
) :
    BaseEntity<Long>(id, name, description, status) {
    constructor() : this(0L, "", "", 0)

    var rawResponse: String = ""
    var type: String = ""
    var txnStatus: String = ""
    var message: String = ""
    var requested: BaseRequest = BaseRequest()
    var transactionReference: String = ""

}

data class ServiceMapsDto(
    override var id: Long,
    override var name: String,
    override var description: String,
    override var status: Int,
) :
    BaseEntity<Long>(id, name, description, status) {
    constructor() : this(0L, "", "", 0)

    var messageDigestAlgorithm: String = "SHA-512"
    var secureRandom: String = "SHA1PRNG"
    var transactionRefLength: Int = 15
    var serviceTopic: String = ""
    var requestClass: String = ""
    var responseClass: String = ""
    var actorClass: String = ""
    val activeStatus: Int = 1
    val testStatus: Int = 2
    val inActiveStatus: Int = 0
    var initStatus: Int = 10
    var workingStatus: Int = 10
    var failedStatus: Int = 20
    var successStatus: Int = 30
    var exceptionStatus: Int = 25
    var invalidStatus: Int = -1
    var invalidStatusKra: Int = -2
    var invalidStatusRegistrar: Int = -3
    var validStatus: Int = 100
    var initStage: Int = 0
    var exceptionStatusCode: String = "99"
    var failedStatusCode: String = "05"
    var successStatusCode: String = "00"
    var httpSuccessResponse: Int = 0
    var httpFailureResponse: Int = 0
    var testCodeStatus: Int = 0
    var separator: String = "_"
    var currencySymbol: String = "Ksh."

}

data class BaseNotification(
    override var id: Long,
    override var name: String = "",
    override var description: String = "",
    override var status: Int = 0,
) :
    BaseRequest() {
    constructor() : this(0L, "", "", 0)

    var userEmail: String = ""
    var message: String = ""

}

open class BaseModel(
    open val Id: Int = 0,
)

data class SOA(
    override var id: Long,
    override var name: String = "",
    override var description: String = "",
    override var status: Int = 0,
) :
    BaseRequest() {
    constructor() : this(0L, "", "", 0)

    var refernece: String = ""
}

data class DivisionsEntityDto(
    var id: Long?,
    var division: String?,
    var departmentId: Long?,
    var descriptions: String?,
    var directorateId: Long?,
    var status: Boolean?
)

data class SectionsEntityDto(
    var id: Long?,
    var section: String?,
    var divisionId: Long?,
    var descriptions: String?,
    var status: Boolean?
)

data class SectionsDto(
    var id: Long? = null,
    var section: String? = null,
    var divisionId: Long? = null,
    var descriptions: String? = null,
    var status: Boolean? = null
)

data class SubSectionsL1EntityDto(
    var id: Long?,
    var subSection: String?,
    var sectionId: Long?,
    var status: Boolean?
)

data class SubSectionsL2EntityDto(
    var id: Long?,
    var subSection: String?,
    var subSectionL1Id: Long?,
    var status: Boolean?
)

data class DepartmentsEntityDto(
    var id: Long?,
    var department: String?,
    var descriptions: String?,
    var directorateId: Long?,
    var status: Boolean?
)

data class UnHashListDto(
    val stringDetails: List<UnHashStringDto>? = null
)

data class HashListDto(
    val stringDetails: List<HashedStringDto>? = null
)

data class UnHashStringDto(
    var stringUnHashed: String? = null
)

data class HashedStringDto(
    var stringHashed: String? = null
)

data class DirectoratesEntityDto(
    var id: Long?,
    var directorate: String?,
    var status: Boolean?
)

data class RegionsEntityDto(
    var id: Long?,
    var region: String?,
    var descriptions: String?,
    var status: Boolean?
)

data class BusinessLinesEntityDto(
    var id: Long?,
    var name: String?,
    var descriptions: String?,
    var status: Boolean?
)

data class BusinessNatureEntityDto(
    var id: Long?,
    var businessLinesId: Long?,
    var name: String?,
    var descriptions: String?,
    var status: Boolean?
)

data class SubRegionsEntityDto(
    var id: Long?,
    var subRegion: String?,
    var descriptions: String?,
    var regionId: Long?,
    var status: Boolean?
)

data class CountiesEntityDto(
    var id: Long?,
    var county: String?,
    var regionId: Long?,
    var status: Boolean?
)

data class TownsEntityDto(
    var id: Long?,
    var town: String?,
    var countyId: Long?,
    var status: Boolean?
)

data class DesignationEntityDto(
    var id: Long?,
    var designationName: String?,
    var descriptions: String?,
    var status: Boolean?,
    var directorateId: Long?
)

data class UserSearchValues(
    var userName: String? = null,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null
)

data class UserPasswordVerificationValuesDto(
    var emailUsername: String? = null,
    var otpVerification: String? = null,
    var password: String? = null,
    var passwordConfirmation: String? = null
)

data class UserPasswordResetValuesDto(
    var emailUsername: String? = null,
)

data class OtpRequestValuesDto(
    var username: String? = null,
    var password: String? = null
)

data class ValidatePhoneNumberOtpRequestValuesDto(
    var phone: String? = null,
    var token: String? = null
)

data class OtpResponseDto(
    var message: String? = null,
    var otp: String? = null
)


data class UserCompanyDto(
    var id: Long? = null,
    var name: String? = null,
    var kraPin: String? = null,
    var userId: Long? = null,
    var registrationNumber: String? = null,
    var postalAddress: String? = null,
    var physicalAddress: String? = null,
    var plotNumber: String? = null,
    var companyEmail: String? = null,
    var companyTelephone: String? = null,
    var yearlyTurnover: BigDecimal? = null,
    var businessLines: String? = null,
    var businessNatures: String? = null,
    var buildingName: String? = null,
    var streetName: String? = null,
    var region: String? = null,
    var county: String? = null,
    var town: String? = null,
    var factoryVisitDate: Date? = null,
    var factoryVisitStatus: Int? = null,
    var manufactureStatus: Int? = null,
)

data class BrsConfirmationRequest(
    @NotEmpty(message = "is required")
    val registrationNumber: String,
    @NotEmpty(message = "is required")
    val directorIdNumber: String
)

data class ValidatePhoneNumberRequestDto(
    @NotEmpty(message = "is required")
    val phone: String
)

data class SendTokenRequestDto(
    @NotEmpty(message = "is required")
    val username: String
)

data class ValidateTokenRequestDto(
    @NotEmpty(message = "is required")
    val username: String,
    @NotEmpty(message = "is required")
    val token: String?
)

data class ValidatePhoneNumberTokenRequestDto(
    @NotEmpty(message = "is required")
    val phone: String,
    @NotEmpty(message = "is required")
    val token: String?
)

data class MPesaPushDto(
    @NotEmpty(message = "is required")
    var entityValueID: Long,
    @NotEmpty(message = "is required")
    var phoneNumber: String
)

data class UserCompanyEntityDto(
    @JsonProperty("name")
    @NotEmpty(message = "is mandatory")
    var name: String? = null,
    @JsonProperty("kraPin")
    @NotEmpty(message = "is mandatory")
    var kraPin: String? = null,

    var userId: Long? = null,
    var profileType: Long? = null,
    @JsonProperty("registrationNumber")
    @NotEmpty(message = "is mandatory")
    var registrationNumber: String? = null,
    @JsonProperty("postalAddress")
    var postalAddress: String? = null,

    @JsonProperty("physicalAddress")
    @NotEmpty(message = "is mandatory")
    var physicalAddress: String? = null,
    var plotNumber: String? = null,
    @JsonProperty("companyEmail")
    @NotEmpty(message = "is mandatory")
    @Email(message = "Email format is not valid")
    var companyEmail: String? = null,
    @JsonProperty("companyTelephone")
    @NotEmpty(message = "is mandatory")
    @Size(min = 9, message = "Phone Number must start with 254")
    var companyTelephone: String? = null,
    @JsonProperty("yearlyTurnover")
    @NotBlank(message = "is mandatory")
//    @Pattern(regexp = "/(\\d{0,3},)?(\\d{3},)?\\d{0,3}/", message = "must be numeric")
    var yearlyTurnover: BigDecimal? = null,
    @JsonProperty("businessLines")
    @NotBlank(message = "is mandatory")
    var businessLines: Long? = null,
    @JsonProperty("businessNatures")
    @NotBlank(message = "is mandatory")
    var businessNatures: Long? = null,
    @JsonProperty("buildingName")
    @NotEmpty(message = "is mandatory")
    var buildingName: String? = null,
    @JsonProperty("streetName")
    @NotEmpty(message = "is mandatory")
    var streetName: String? = null,
    @JsonProperty("directorIdNumber")
    @NotEmpty(message = "is mandatory")
    var directorIdNumber: String? = null,
    @JsonProperty("region")
    @NotBlank(message = "is mandatory")
    var region: Long? = null,
    @JsonProperty("county")
    @NotBlank(message = "is mandatory")
    var county: Long? = null,
    @JsonProperty("town")
    @NotBlank(message = "is mandatory")
    var town: Long? = null,
    var factoryVisitDate: Date? = null,
    var factoryVisitStatus: Int? = null,
    var manufactureStatus: Int? = null,

    ) {
    var id: Long? = null
    var status: Boolean = false
}

data class UserEntityDto(

    var id: Long? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var userName: String? = null,
    var userPinIdNumber: String? = null,
    var personalContactNumber: String? = null,
    var typeOfUser: Int? = null,
    var email: String? = null,
    var userRegNo: String? = null,
    var enabled: Boolean = false,
    var accountExpired: Boolean = false,
    var accountLocked: Boolean = false,
    var credentialsExpired: Boolean = false,
    var status: Boolean = false,
    var registrationDate: Date? = null,
    var userType: Long? = null,
    var title: Long? = null,
    var directorate: Long? = null,
    var department: Long? = null,
    var division: Long? = null,
    var section: Long? = null,
    var l1SubSubSection: Long? = null,
    var l2SubSubSection: Long? = null,
    var designation: Long? = null,
    var profileId: Long? = null,
    var region: Long? = null,
    var county: Long? = null,
    var town: Long? = null,
    var subRegion: Long? = null,
)


data class UserDetailsDto(

    var id: Long?,
    var firstName: String?,
    var lastName: String?,
    var userName: String?,
    var email: String?,
    var userPinIdNumber: String?,
    var personalContactNumber: String?,
    var typeOfUser: Int?,
    var userRegNo: String?,
    var enabled: Boolean = false,
    var accountExpired: Boolean = false,
    var accountLocked: Boolean = false,
    var credentialsExpired: Boolean = false,
    var status: Boolean = false,
    var registrationDate: Date?,
    var title: String?,
    var employeeProfile: EmployeeProfileDetailsDto? = null,
    var companyProfile: UserCompanyDto? = null,
)

data class EmployeeProfileDetailsDto(
    var directorate: String? = null,
    var department: String? = null,
    var division: String? = null,
    var section: String? = null,
    var l1SubSubSection: String? = null,
    var l2SubSubSection: String? = null,
    var designation: String? = null,
    var profileId: Long? = null,
    var region: String? = null,
    var county: String? = null,
    var town: String? = null,
    var status: Boolean = false,
)


data class RolesEntityDto(
    var id: Long?,
    val roleName: String?,
    var descriptions: String?,
    var status: Boolean?,
)

data class AuthoritiesEntityDto(
    var id: Long?,
    val name: String?,
    var descriptions: String?,
    var status: Boolean?,
)

data class TitlesEntityDto(
    var id: Long?,
    val title: String?,
    val remarks: String?,
    var status: Boolean?
)

data class UserTypesEntityDto(
    var id: Long?,
    var typeName: String?,
    var descriptions: String?,
    var status: Boolean?,
    var defaultRoleId: Long?
)

data class StandardProductCategoryEntityDto(
    var id: Long? = null,
    var standardCategory: String? = null,
    var standardNickname: String? = null,
    var standardId: Long? = null,
    var status: Boolean? = null
)

data class UserRequestTypesEntityDto(
    var id: Long? = null,
    var userRequest: String? = null,
    var description: String? = null,
    var status: Boolean? = null
)

data class ManufactureSubmitEntityDto(
    var closedCommodityManufactured: Int? = null,
    var closedContractsUndertaken: Int? = null,
    var submittedStatus: Int? = null,
)

data class UserRequestEntityDto(
    var id: Long? = null,
    var requestId: Long? = null,
    var userId: Long? = null,
    var userRoleAssigned: Long? = null,
    var requestStatus: Boolean? = null,
    var description: String? = null,
    var status: Boolean? = null
)

data class UserRequestListEntityDto(
    var id: Long? = null,
    var requestName: String? = null,
    var userName: String? = null,
    var userId: Long? = null,
    var userRoleAssignedName: String? = null,
    var requestStatus: Boolean? = null,
    var description: String? = null,
    var status: Boolean? = null
)

data class ProductCategoriesEntityDto(
    var id: Long? = null,
    var name: String? = null,
    var status: Boolean? = null,
    var broadProductCategoryId: Long? = null,
)

data class BroadProductCategoryEntityDto(
    var id: Long? = null,
    var category: String? = null,
    var status: Boolean? = null,
    var divisionId: Long? = null,
)

data class ProductsEntityDto(
    var id: Long? = null,
    var name: String? = null,
    var status: Boolean? = null,
    var productCategoryId: Long? = null,
)

data class FmarkEntityDto(
    var smarkPermitID: Long? = null,
)

data class ProductSubcategoryEntityDto(
    var id: Long? = null,
    var name: String? = null,
    var status: Boolean? = null,
    var productId: Long? = null,
)

/*
Ministry Inspection table view details
 */
data class MinistryInspectionListResponseDto(
    var cdId: Long = 0,
    var cdUcr: String? = null,
    var chassis: String? = null,
    var used: String? = null,
    var year: String? = null,
    var model: String? = null,
    var make: String? = null,
    var cdItemDetailsId: Long? = null
)

class LoginRequest {
    @NotBlank
    var username: String? = null

    @NotBlank
    @Size(min = 5, max = 255)
    var password: String? = null
}

class JwtResponse(
    var accessToken: String?,
    var id: Long?,
    var username: String?,
    var email: String?,
    var fullName: String?,
    val roles: List<String>?
) {
    var tokenType = "Bearer"

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    var expiry: LocalDateTime? = null
    var companyID: Long? = null
    var branchID: Long? = null
    var turnover: BigDecimal? = null

}


class CustomResponse {
    var response: String? = null
    var status: Int? = null
    var payload: String? = null
}

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class OrganizationUserEntityDto(
    var id: Long?,
    @JsonProperty("firstName")
    @NotEmpty(message = "is mandatory")
    var firstName: String?,
    @JsonProperty("lastName")
    @NotEmpty(message = "is mandatory")
    var lastName: String?,
    @JsonProperty("userName")
    @NotEmpty(message = "is mandatory")
    var userName: String?,
    @Email(regexp = ".+@.+\\..+")
    @NotEmpty(message = "is mandatory and should be in the correct format")
    @JsonProperty("email")
    var email: String?,
    @JsonProperty("enabled")
    var enabled: Boolean = false,
    @JsonProperty("status")
    var status: Boolean = false,
    @JsonProperty("title")
    var title: Long?,
    @JsonProperty("credentials")
    @NotEmpty(message = "Password is mandatory")
    @Size(min = 8, max = 25)
    var credentials: String?,
    @JsonProperty("cellphone")
    @NotEmpty(message = "is mandatory")
    var cellphone: String?
) {
    var companyId: Long? = null
    var plantId: Long? = null

}

data class RegistrationPayloadDto(
    @Valid
    val company: UserCompanyEntityDto,
    @Valid
    val user: OrganizationUserEntityDto
)


class PlantEntityDto {
    var id: Long? = null
    var companyProfileId: Long? = null
    var location: String? = null
    var street: String? = null
    var buildingName: String? = null
    var nearestLandMark: String? = null
    var postalAddress: String? = null
    var telephone: String? = null
    var emailAddress: String? = null
    var physicalAddress: String? = null
    var faxNo: String? = null
    var plotNo: String? = null
    var designation: String? = null
    var contactPerson: String? = null
    var status: Boolean = false
    var descriptions: String? = null
    var region: Long? = null
    var county: Long? = null
    var town: Long? = null
}

class ProfileDirectorsEntityDto {
    var id: Long? = null
    var companyProfileId: Long? = null
    var directorName: String? = null
    var directorId: String? = null
    var description: String? = null
    var status: Boolean = false
}

data class UserCompanyDetailsDto(
    @JsonProperty("companyId")
    val companyId: Long? = null,
    @JsonProperty("branchId")
    val branchId: Long? = null,
    @JsonProperty("branchCount")
    val branchCount: Long? = null,
    @JsonProperty("turnover")
    val turnover: BigDecimal? = null,
    @JsonProperty("userId")
    val userId: Long? = null,
    @JsonProperty("countAwarded")
    val countAwarded: Long? = null,
    @JsonProperty("countExpired")
    val countExpired: Long? = null
)