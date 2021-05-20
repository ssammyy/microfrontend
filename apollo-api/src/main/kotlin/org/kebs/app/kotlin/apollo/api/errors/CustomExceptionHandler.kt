package org.kebs.app.kotlin.apollo.api.errors


import io.jsonwebtoken.ExpiredJwtException
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.ModelAndView
import java.time.Instant
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class CustomExceptionHandler {


    /**
     * Demonstrates how to take total control - setup a model, add useful
     * information and return the "support" view name. This method explicitly
     * creates and returns
     *
     * @param req
     * Current HTTP request.
     * @param exception
     * The exception thrown - always [ExpectedDataNotFound].
     * @return The model and view used by the DispatcherServlet to generate
     * output.
     * @throws Exception
     */
    @ExceptionHandler(Exception::class)
    @Throws(Exception::class)
    fun handleError(req: HttpServletRequest, exception: Exception): ModelAndView? {
        return errorMessageAlert(req, exception, HttpStatus.INTERNAL_SERVER_ERROR)

    }

    @ExceptionHandler(InvalidValueException::class)
    @Throws(Exception::class)
    fun handleInvalidValueException(req: HttpServletRequest, exception: Exception): ModelAndView? {
        return errorMessageAlert(req, exception, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NullValueNotAllowedException::class)
    @Throws(Exception::class)
    fun handleNullValueNotAllowedException(req: HttpServletRequest, exception: Exception): ModelAndView? {
        return errorMessageAlert(req, exception, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ExpectedDataNotFound::class)
    @Throws(Exception::class)
    fun handleErrorExpectedDataNotFound(req: HttpServletRequest, exception: Exception): ModelAndView? {
        return errorMessageAlert(req, exception, HttpStatus.BAD_REQUEST)
    }

    private fun errorMessageAlert(req: HttpServletRequest, exception: Exception, status: HttpStatus): ModelAndView {
        KotlinLogging.logger { }.error("Request: " + req.requestURI + " raised " + exception.message, exception)
        KotlinLogging.logger { }.debug("Request: " + req.requestURI + " raised " + exception.message, exception)
        val mav = ModelAndView()
        mav.addObject("exception", exception)
        mav.addObject("message", exception.message)
        mav.addObject("path", req.requestURL)
        mav.addObject("timestamp", Instant.now())
        mav.addObject("status", 500)
        mav.viewName = "error"
        return mav
    }


    @ExceptionHandler(ServiceMapNotFoundException::class)
    fun handleServiceMapNotFoundException(ex: Exception, request: HttpServletRequest): ModelAndView? {
//        val details = mutableListOf<String?>()
//        details.add(ex.message)
//
//        val error = ErrorResponse("Configuration Issue", details)
//        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
        return errorMessageAlert(request, ex, HttpStatus.BAD_REQUEST)
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
        e: MethodArgumentNotValidException,
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
        e: ConstraintViolationException,
    ): ErrorResponse? {
        val details = mutableListOf<String?>()
        e.constraintViolations.forEach { violation ->
            details.add("${violation.propertyPath.last()}: ${violation.message}")
        }
        return ErrorResponse("Required input missing", details)
    }

    @ExceptionHandler(GenericRuntimeException::class)
    fun handleGenericException(ex: GenericRuntimeException): ResponseEntity<Any> {
        return buildErrorResponseEntity(
            GenericErrorResponse(ex.message),
            HttpStatus.valueOf(ex.status)
        )
    }

    private fun buildErrorResponseEntity(responseBody: GenericErrorResponse, status: HttpStatus
    ): ResponseEntity<Any> { return ResponseEntity<Any>(responseBody, HttpHeaders(), status) }
}


