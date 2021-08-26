package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.ApiDestinationInspectionHandler
import org.kebs.app.kotlin.apollo.api.handlers.ChecklistHandler
import org.kebs.app.kotlin.apollo.api.handlers.DestinationInspectionActionsHandler
import org.kebs.app.kotlin.apollo.api.handlers.InvoiceHandlers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Configuration
class DestinationInspectionRoutes {

    @Bean
    @CrossOrigin
    fun invoicing(handlers: InvoiceHandlers)= router{
        "/api/v1/di".nest {
            GET("/demand/note/{demandNoteId}",handlers::downloadDemandNote)
            GET("/demand/notes/{cdUuid}",handlers::cdInvoiceDetails)
            POST("/generate/demand/note/{demandNoteId}",handlers::generateDemandNote)
        }
    }

    @Bean
    @CrossOrigin
    fun checkLists(handlers: ChecklistHandler)= router{
        "/api/v1/di".nest {
            GET("/ministry/checklist/{checklistId}",handlers::downloadMinistryChecklist)
            GET("/ministry/checklists/{checklistId}",handlers::listMinistryChecklists)
        }
    }

    @Bean
    @CrossOrigin
    fun destinationInspectionActionsRoute(handler: DestinationInspectionActionsHandler) = router {
        "/api/v1/di".nest {
            POST("/consignment/document/approve-reject/{cdUuid}", handler::approveRejectConsignment)
            POST("/consignment/document/assign-port/{cdUuid}", handler::assignPort)
            POST("/consignment/document/assign-io/{cdUuid}", handler::assignInspectionOfficer)
            POST("/consignment/document/reassign-io/{cdUuid}", handler::assignInspectionOfficer)
            POST("/consignment/document/manual-pick/{cdUuid}", handler::pickConsignmentInspectionOfficer)
            POST("/consignment/document/send-coi/{cdUuid}", handler::sendCertificateOfInspection)
            POST("/consignment/document/blacklist/{cdUuid}", handler::blacklistConsignment)
            POST("/consignment/document/approve-blacklist/{cdUuid}", handler::approveBlacklistConsignment)
            POST("/consignment/document/generate-coc/{cdUuid}", handler::generateLocalCoc)
            POST("/consignment/document/generate-cor/{cdUuid}", handler::generateLocalCor)
            POST("/consignment/document/compliant-vehicle/{inspectionChecklistId}", handler::updateMotorVehicleComplianceStatus)
            POST("/consignment/document/target/{cdUuid}", handler::targetConsignment)
            POST("/consignment/document/target-approval/{cdUuid}", handler::approveTargetConsignment)
            POST("/consignment/document/target-supervisor/{cdUuid}", handler::supervisorTargetConsignment)
        }
    }

    @Bean
    @CrossOrigin
    fun destinationInspectionRoute2(handler: ApiDestinationInspectionHandler) = router {
        "/api/v1/di".nest {
            // Configurations
            GET("/application/types", handler::listApplicationTypes)
            GET("/consignment/document/types", handler::listConsignmentDocumentTypes)
            GET("/consignment/document/configuration", handler::consignmentDocumentConfiguration)
            GET("/cd/inspection/configuration", handler::loadCommonUIComponents)
            GET("/ports", handler::loadPortOfArrival)
            GET("/port/freight/stations/{portId}", handler::listPortFreightStations)
            GET("/blacklist/users", handler::listBlackListedUser)
            // OTHERS
            GET("/consignment/document/details/{coUuid}", handler::consignmentDocumentDetails)
            GET("/consignment/document/attachments/{coUuid}", handler::consignmentDocumentAttachments)
            POST("/consignment/document/attachments/upload/{cdUuid}", handler::uploadConsignmentDocumentAttachment)
            GET("/cd/download/attachments/{uploadId}", handler::downloadConsignmentDocumentAttachment)
            GET("/consignment/document/manifest/{coUuid}", handler::consignmentDocumentManifest)
            GET("/consignment/document/audit/{cdId}", handler::consignmentDocumentHistory)
            GET("/consignment/document/customs/declaration/{coUuid}", handler::consignmentDocumentCustomsDeclaration)
            GET("/consignment/document/invoices/{coUuid}", handler::consignmentDocumentInvoices)
            GET("/consignment/document/item/{coItemUuid}", handler::consignmentDocumentItemDetails)
            GET("/consignment/documents/item/checklist/{cdItemUuid}", handler::consignmentDocumentChecklist)
            GET("/consignment/documents/item/lab/{cdItemUuid}", handler::consignmentDocumentSSFLabDetails)
            // Foreign CoC/CoR
            POST("/foreign/cd/upload", handler::uploadForeignConsignmentDocument)
            // CD Listing
            GET("/consignment/documents/assigned", handler::assignedConsignmentDocuments)
            GET("/consignment/documents/ongoing", handler::outgoingConsignmentDocuments)
            GET("/consignment/documents/manual/assigned", handler::manualAssignedConsignmentDocuments)
            GET("/consignment/documents/completed", handler::completedConsignmentDocuments)
            //
            GET("/inspection/coc/details/{coUuid}", handler::certificateOfConformance)
            GET("/inspection/idf/details/{coUuid}", handler::importDeclarationFormDetails)
            GET("/inspection/cor/details/{coUuid}", handler::certificateOfRoadWorthines)
            // Ministry
            GET("/ministry/inspections/{inspectionStatus}", handler::ministryInspections)
            GET("/ministry/inspection/details/{itemId}", handler::motorVehicleInspection)
            POST("/ministry/inspection/checklist/{itemId}", handler::uploadMinistryCheckList)
            GET("/ministry/inspection/checklist/download/{itemId}", handler::downloadMinistryCheckList)
        }
    }
}