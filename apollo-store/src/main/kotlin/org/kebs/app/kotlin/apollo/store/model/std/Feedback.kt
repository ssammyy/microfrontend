package org.kebs.app.kotlin.apollo.store.model.std



import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


class Feedback {
    @NotNull
     val name: String? = null

    @NotNull
    @Email
     val email: String? = null

    @NotNull
    @Min(10)
     val feedback: String? = null
}
