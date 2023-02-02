package org.kebs.app.kotlin.apollo.api.service

import org.springframework.stereotype.Service
import java.net.MalformedURLException
import java.net.URL
import javax.validation.*
import kotlin.reflect.KClass

@Service
class DaoValidatorService(private var validator: Validator) {

    fun validateInputWithInjectedValidator(input: Any?): Map<String, String>? {
        val errors = HashMap<String, String>()
        val violations: Set<ConstraintViolation<Any?>> = validator.validate(input)
        if (violations.isEmpty()) {
            return null
        }
        violations.forEach {
            errors.put(it.propertyPath.toString(), it.message)
        }
        return errors
    }
}

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UrlValidator::class])
@MustBeDocumented
annotation class ValidateUrl(
    val message: String = "Invalid ip address",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class UrlValidator : ConstraintValidator<ValidateUrl, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        try {
            var url: URL? = null
            if (!value.isNullOrEmpty()) {
                url = URL(value)
                // Only valid if its http or https
                if (!(url.protocol == "http" || url.protocol == "https")) {
                    return false
                }
            }
            return url != null
        } catch (ex: MalformedURLException) {

        }
        return false
    }

}
