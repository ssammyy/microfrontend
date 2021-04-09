package org.kebs.app.kotlin.apollo.api.flux.errors


import io.jsonwebtoken.ExpiredJwtException
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import javax.validation.ConstraintViolationException


@ControllerAdvice
class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest?): ResponseEntity<Any?> {
        val details = mutableListOf<String?>()
        KotlinLogging.logger { }.error(ex.message, ex)
        details.add(ex.message)
//        details.add(ex.printStackTrace().toString())

        val error = ErrorResponse("Error Occurred", details)
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceMapNotFoundException::class)
    fun handleServiceMapNotFoundException(ex: Exception, request: WebRequest?): ResponseEntity<Any?> {
        val details = mutableListOf<String?>()
        details.add(ex.message)
        val error = ErrorResponse("Configuration Issue", details)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler(ExpiredJwtException::class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    fun handleExpiredTokenException(ex: Exception, request: WebRequest?): ResponseEntity<Any> {
        val details = mutableListOf<String?>()
//        details.add(ex.cause?.javaClass?.name)
        KotlinLogging.logger { }.error(ex.message, ex)
        details.add(ex.message)
        val error = ErrorResponse("Error Occurred", details)
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

//    @ExceptionHandler(MethodArgumentNotValidException::class)
//    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
//        val details = mutableListOf<String?>()
//        for (error in ex.bindingResult.fieldErrors) {
//            details.add("${error.field}: ${error.defaultMessage}")
//        }
//        val error = ErrorResponse("Validation Failed", details)
//        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
//    }


    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun onMethodArgumentNotValidException(
        e: MethodArgumentNotValidException
    ): ErrorResponse? {
        val details = mutableListOf<String?>()
        e.bindingResult.fieldErrors.forEach { violation ->
            details.add("${violation.field} ${violation.defaultMessage}")
        }
        return ErrorResponse("MethodArgumentNotValidException", details)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun onConstraintValidationException(
        e: ConstraintViolationException
    ): ErrorResponse? {
        val details = mutableListOf<String?>()
        e.constraintViolations.forEach { violation ->
            details.add("${violation.propertyPath.last()}: ${violation.message}")
        }
        return ErrorResponse("Required input missing", details)
    }

}


