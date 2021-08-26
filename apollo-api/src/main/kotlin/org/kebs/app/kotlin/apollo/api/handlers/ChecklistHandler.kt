package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class ChecklistHandler (
            private val daoServices: DestinationInspectionDaoServices,
            private val invoiceDaoService: InvoiceDaoService,
            private val reportsDaoService: ReportsDaoService,
            private val applicationMapProperties: ApplicationMapProperties
    ) {

    fun downloadMinistryChecklist(req: ServerRequest): ServerResponse {
        val map = hashMapOf<String, Any>()
        val response=ApiResponseModel()
        try {
            req.pathVariable("checklistId").let {
                daoServices.findInspectionMotorVehicleById(it.toLongOrDefault(0))?.let { mvInspectionChecklist ->
                    map["ImporterName"] = mvInspectionChecklist.inspectionGeneral?.importersName.toString()
                    map["VehicleMake"] = mvInspectionChecklist.makeVehicle.toString()
                    map["EngineCapacity"] = mvInspectionChecklist.engineNoCapacity.toString()
                    map["ManufactureDate"] = mvInspectionChecklist.manufactureDate.toString()
                    map["OdometerReading"] = mvInspectionChecklist.odemetreReading.toString()
                    map["RegistrationDate"] = mvInspectionChecklist.registrationDate.toString()
                    map["ChassisNo"] = mvInspectionChecklist.chassisNo.toString()

                } ?: throw ExpectedDataNotFound("Motor Vehicle Inspection Checklist does not exist")

                val stream=reportsDaoService.extractReportEmptyDataSource(map, applicationMapProperties.mapReportMinistryChecklistPath)
                return ServerResponse.ok()
                        .header("Content-Disposition", "inline; filename=MINISTRY-CHECKLIST-${it}.pdf;")
                        .contentType(MediaType.APPLICATION_PDF)
                        .contentLength(stream.size().toLong())
                        .body(stream)
            }
        }catch (ex: Exception){
            KotlinLogging.logger {  }.error { ex }
            response.message=ex.localizedMessage
            response.responseCode=ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response)
    }
    fun listMinistryChecklists(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body("")
    }
}