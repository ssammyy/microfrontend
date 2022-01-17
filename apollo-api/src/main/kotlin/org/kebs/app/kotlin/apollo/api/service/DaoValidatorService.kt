package org.kebs.app.kotlin.apollo.api.service

import org.springframework.stereotype.Service
import javax.validation.ConstraintViolation
import javax.validation.Validator

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