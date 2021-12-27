package org.kebs.app.kotlin.apollo.api.payload.request


import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

class InternationalStandardMarkForm {
    @NotNull(message = "UCR Number is required")
    val ucrNumber: String? = null
    @NotNull(message = "UCR Number is required")
    @Email(message = "Invalid email address provided")
    val emailAddress: String? = null
    @NotNull(message = "Applicant First name is required")
    @Pattern(regexp = "\\W+",message="Invalid characters in first name")
    val firstName: String? = null
    @NotNull(message = "Applicant Middle name is required")
    @Pattern(regexp = "\\W+",message="Invalid characters in middle name")
    val middleName: String? = null
    val lastName: String? = null
    @NotNull(message = "Company name is required")
    val companyName: String? = null
    val remarks: String? = null
}

class InternationalStandardMarkRequestsForm {
    @NotNull(message = "UCR Number is required")
    @Email(message = "Invalid email address provided")
    val emailAddress: String? = null
}