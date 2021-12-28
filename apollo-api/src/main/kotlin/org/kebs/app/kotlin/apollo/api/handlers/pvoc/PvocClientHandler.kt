package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import akka.io.dns.internal.ResponseCode
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.CocEntityForm
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocClientService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocClientHandler(
        private val pvocClientService: PvocClientService,
        private val validatorService: DaoValidatorService
) {
    fun foreignCoc(req: ServerRequest): ServerResponse{
        val response=ApiResponseModel()
        try{
            val form=req.body(CocEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message="Request validation failed"
                response.errors=it
                response.responseCode=ResponseCodes.INVALID_CODE
                response.data=form
                response
            }?:run {
                return ServerResponse.ok().body(pvocClientService.receiveCoc(form))
            }
        }catch (ex:  Exception ){
            response.responseCode= ResponseCodes.FAILED_CODE
            response.message="Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun foreignCor(req: ServerRequest): ServerResponse{
        val response=ApiResponseModel()
        try{
            val form=req.body(CocEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message="Request validation failed"
                response.errors=it
                response.responseCode=ResponseCodes.INVALID_CODE
                response.data=form
                response
            }?:run {
                return ServerResponse.ok().body(pvocClientService.receiveCoc(form))
            }
        }catch (ex:  Exception ){
            response.responseCode= ResponseCodes.FAILED_CODE
            response.message="Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun foreignNcr(req: ServerRequest): ServerResponse{
        val response=ApiResponseModel()
        try{
            val form=req.body(CocEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message="Request validation failed"
                response.errors=it
                response.responseCode=ResponseCodes.INVALID_CODE
                response.data=form
                response
            }?:run {
                return ServerResponse.ok().body(pvocClientService.receiveCoc(form))
            }
        }catch (ex:  Exception ){
            response.responseCode= ResponseCodes.FAILED_CODE
            response.message="Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }
}