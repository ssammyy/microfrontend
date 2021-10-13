package org.kebs.app.kotlin.apollo.api.routes

import org.kebs.app.kotlin.apollo.api.handlers.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.servlet.function.router

@Configuration
class DestinationInspectionRoutes {
    @Bean
    @CrossOrigin
    fun dashboard(handler: InspectionDashboard)= router {
        "/api/v1/dashboard".nest {
            GET("/all", handler::inspectionStatistics)
            GET("/personal", handler::myInspectionStatistics)
        }
    }
    @Bean
    @CrossOrigin
    fun invoicing(handlers: InvoiceHandlers)= router{
        "/api/v1/di".nest {
            GET("/update/demand/note/payment-status/{invoiceId}",handlers::checkPaymentDemandNotePayment)
            GET("/demand/note/details/{invoiceId}",handlers::cdInvoiceDetails)
            POST("/demand/note/submit/{invoiceId}",handlers::submitDemandNoteForApproval)
            DELETE("/demand/note/delete/{invoiceId}",handlers::deleteDemandNote)
            GET("/demand/note/list/{cdId}",handlers::listDemandNotes)
            GET("/demand-note/fees",handlers::applicationFee)
            POST("/demand/note/upload/exchange-rate",handlers::applicationUploadExchangeRates)
            GET("/demand/note/exchange-rates",handlers::applicationExchangeRates)
            POST("/demand/note/generate/{cdUuid}",handlers::generateDemandNote)
        }
    }

    @Bean
    @CrossOrigin
    fun checkLists(handler: ChecklistHandler)= router{
        "/api/v1/di".nest {
            GET("/checklists/{itemUuid}",handler::listAllChecklists)
            POST("/save-checklist/{cdUuid}",handler::saveChecklist)
            POST("/consignment/document/checklist-scf/{category}/{cdItemID}", handler::addChecklistScfDetails) // Per inspection item
            POST("/consignment/document/item-scf/{cdItemID}", handler::addScfDetails) // Per item
            POST("/consignment/document/checklist-ssf/{category}/{cdItemID}", handler::addChecklistSsfDetails) // Per inspection item
            POST("/consignment/document/item-ssf/{cdItemID}", handler::addSsfDetails) // Per inspection item
            POST("/consignment/document/item-ssf-result/{cdItemID}", handler::updateSsfResults) // Per inspection item
            GET("/lab-result/ssf-files/{ssfId}", handler::ssfPdfFilesResults) // Per inspection item
            GET("/consignment/document/lab-results/{cdItemID}", handler::loadLabResult) // Lab Reult Per item
            GET("/check-list/configurations",handler::checklistConfigurations)
            GET("/consignment/document/checklist/{cdUuid}", handler::consignmentDocumentChecklist)
            GET("/consignment/document/sampled-items/{cdUuid}", handler::consignmentDocumentChecklistSampled)
            POST("/item/compliance/approve-reject/{cdItemId}/{cdUuid}", handler::approveRejectSampledItem)
//            POST("/consignment/document/compliant-vehicle/{inspectionChecklistId}", handler::updateMotorVehicleComplianceStatus)
            // Ministry
            GET("/ministry/inspections/{inspectionStatus}", handler::ministryInspections)
            POST("/ministry/inspections/request/{inspectionId}", handler::ministryInspectionRequest)
            GET("/ministry/inspection/details/{inspectionId}", handler::motorVehicleInspection)
            POST("/ministry/inspection/checklist/{inspectionId}", handler::uploadMinistryCheckList)
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
            POST("/consignment/document/mark-compliant/{cdUuid}", handler::markConsignmentCompliant)
            POST("/consignment/document/blacklist/{cdUuid}", handler::blacklistConsignment)
            POST("/consignment/document/generate-coc/{cdUuid}", handler::generateLocalCoc)
            POST("/consignment/document/generate-cor/{cdUuid}", handler::generateLocalCor)
            POST("/consignment/document/target/{cdUuid}", handler::targetConsignment)
            POST("/consignment/document/supervisor-target/{cdUuid}", handler::supervisorTargetConsignment)
            POST("/consignment/document/process/approve-reject/{taskId}/{cdUuid}", handler::approveRejectTask)
            GET("/my/tasks", handler::supervisorTasks)
            DELETE("/my/task/{taskId}", handler::deleteSupervisorTasks)

        }
    }

    @Bean
    @CrossOrigin
    fun destinationInspectionRoute2(handler: ApiDestinationInspectionHandler) = router {
        "/api/v1/di".nest {
            // Configurations
            GET("/application/types", handler::listApplicationTypes)
            GET("/consignment/document/types", handler::listConsignmentDocumentTypes)
            GET("/consignment/document/ministry/stations", handler::listMinistryStations)
            GET("/consignment/document/configuration", handler::consignmentDocumentConfiguration)
            GET("/cd/inspection/configuration", handler::loadCommonUIComponents)
            GET("/ports", handler::loadPortOfArrival)
            GET("/port/freight/stations/{portId}", handler::listPortFreightStations)
            GET("/blacklist/users", handler::listBlackListedUser)
            // OTHERS
            GET("/consignment/document/details/{coUuid}", handler::consignmentDocumentDetails)
            GET("/consignment/document/attachments/{coUuid}", handler::consignmentDocumentAttachments)
            DELETE("/consignment/document/attachments/{attachmentId}", handler::deleteConsignmentDocumentAttachment)
            POST("/consignment/document/attachments/upload/{cdUuid}", handler::uploadConsignmentDocumentAttachment)
            GET("/consignment/document/manifest/{coUuid}", handler::consignmentDocumentManifest)
            GET("/consignment/document/audit/{cdId}", handler::consignmentDocumentHistory)
            GET("/consignment/document/customs/declaration/{coUuid}", handler::consignmentDocumentCustomsDeclaration)
            GET("/consignment/document/invoices/{coUuid}", handler::consignmentDocumentInvoices)
            GET("/consignment/document/item/{coItemUuid}", handler::consignmentDocumentItemDetails)
            GET("/consignment/document/tasks/{cdUuid}", handler::consignmentDocumentSupervisorTasks)
            GET("/consignment/documents/item/lab/{cdItemUuid}", handler::consignmentDocumentSSFLabDetails)
            // Foreign CoC/CoR
            POST("/foreign/cd/upload", handler::uploadForeignConsignmentDocument)
            // CD Listing
            GET("/consignment/documents/assigned", handler::assignedConsignmentDocuments)
            GET("/consignment/documents/ongoing", handler::ongoingAssignedConsignmentDocuments)
            GET("/consignment/documents/manual/assigned", handler::availableConsignmentDocuments)
            GET("/consignment/documents/completed", handler::completedConsignmentDocuments)
            // CD Search
            POST("/consignment/documents/search", handler::searchConsignmentDocuments)
            // CD Extra Details
            GET("/inspection/{docType}/details/{coUuid}", handler::certificateOfConformance)
            GET("/inspection/idf/details/{coUuid}", handler::importDeclarationFormDetails)
            GET("/inspection/cor/details/{coUuid}", handler::certificateOfRoadWorthines)

        }
    }
}