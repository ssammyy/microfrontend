package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.ApiDestinationInspectionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Configuration
class DestinationInspectionRoutes {

    @Bean
    @CrossOrigin
    fun destinationInspectionRoute2(handler: ApiDestinationInspectionHandler) = router {
        "/api/v1/di".nest {
            GET("/application/types", handler::listApplicationTypes)
            GET("/consignment/document/types", handler::listConsignmentDocumentTypes)
            GET("/consignment/document/configuration", handler::consignmentDocumentConfiguration)
            POST("/foreign/cd/upload", handler::uploadForeignConsignmentDocument)
            GET("/consignment/document/details/{coUuid}", handler::consignmentDocumentDetails)
            GET("/consignment/document/manifest/{coUuid}", handler::consignmentDocumentManifest)
            GET("/consignment/document/customs/declaration/{coUuid}", handler::consignmentDocumentCustomsDeclaration)
            GET("/consignment/document/invoices/{coUuid}", handler::consignmentDocumentInvoices)
            GET("/consignment/document/item/{coItemUuid}", handler::consignmentDocumentItemDetails)
            GET("/consignment/documents/item/checklist/{cdItemUuid}", handler::consignmentDocumentChecklist)
            GET("/consignment/documents/item/lab/{cdItemUuid}", handler::consignmentDocumentSSFLabDetails)
            GET("/ministry/inspections/{inspectionStatus}", handler::ministryInspections)
            GET("/ministry/inspection/details/{itemId}", handler::motorVehicleInspection)
            POST("/ministry/inspection/checklist/{itemId}", handler::uploadMinistryCheckList)
            GET("/ministry/inspection/checklist/download/{itemId}", handler::downloadMinistryCheckList)
            GET("/consignment/documents/assigned", handler::assignedConsignmentDocuments)
            GET("/consignment/documents/ongoing", handler::outgoingConsignmentDocuments)
            GET("/consignment/documents/manual/assigned", handler::manualAssignedConsignmentDocuments)
            GET("/consignment/documents/completed", handler::completedConsignmentDocuments)
            GET("/inspection/coc/details/{coUuid}", handler::certificateOfConformance)
            GET("/inspection/idf/details/{coUuid}", handler::importDeclarationFormDetails)
            GET("/inspection/cor/details/{coUuid}", handler::certificateOfRoadWorthines)

//            POST("/exemption/apply", handler::exemptionApplication)
//            GET("/exemption/check/eligible", handler::checkExemptionEligibility)
//            GET("/exemption/history", handler::exemptionHistory)
//            GET("/exemption/{exemptionId}", handler::viewExemption)

        }
    }
}