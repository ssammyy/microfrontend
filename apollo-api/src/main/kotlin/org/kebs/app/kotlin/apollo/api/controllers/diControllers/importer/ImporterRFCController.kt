package org.kebs.app.kotlin.apollo.api.controllers.diControllers.importer

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ImporterDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.importer.RfcCoiItemsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.importer.RfcDocumentsDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid


@Controller
@RequestMapping("/api/importer/")
@SessionAttributes("userRole", "schedule", "userNumber", "CoCs", "destinationFees", "demandNote", "actionType", "docView", "CoCsBlacklist", "myCoc", "coc", "itemCoCs", "status", "item", "itemCerts", "itemIdf", "certBack", "officers", "inspectionType", "remarks", "filledChecklist")
class ImporterRFCController(
        private val applicationMapProperties: ApplicationMapProperties,
        private val daoServices: ImporterDaoServices,
        private val commonDaoServices: CommonDaoServices,
) {


    final var appId = applicationMapProperties.mapImportInspection

    final var redirectRFCDetailPage = "redirect:/api/importer/rfc-detail?rfcID"
    final var redirectRFCListPage = "redirect:/api/importer/rfc-list?rfcTypeID"


    @PreAuthorize("hasAuthority('IMPORTER')")
    @PostMapping("rfc/new/save")
    fun applicationForRFC(
            model: Model,
            @ModelAttribute("rfcDocumentDetailsEntity") @Valid rfcDocumentDetailsEntity: RfcDocumentsDetailsEntity,
            @RequestParam("rfcTypeID") rfcTypeID: Long,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {

        commonDaoServices.serviceMapDetails(appId)
                .let { map ->
                    commonDaoServices.loggedInUserDetails()
                            .let { loggedInUser ->
                                daoServices.findRfcTypeWithID(rfcTypeID)
                                        .let { rfcTypesEntity ->
                                            daoServices.rfcSave(rfcDocumentDetailsEntity, rfcTypesEntity, loggedInUser, map)
                                                    .let { savedRFCCOC ->
                                                        when (savedRFCCOC.rfcTypeId?.id) {
                                                            daoServices.rfcCORTypeID.toLong(), daoServices.rfcCOCTypeID.toLong() -> {
                                                                daoServices.rfcUpdatesSave(savedRFCCOC, loggedInUser, map)
                                                            }
                                                        }

                                                        when {
                                                            savedRFCCOC.rfcTypeId?.id != daoServices.rfcCORTypeID.toLong() -> {
                                                                savedRFCCOC.idfNumber?.let { idfNumber ->
                                                                    savedRFCCOC.ucrNumber?.let { ucrNumber ->
                                                                        daoServices.findByIdfNoAndUCRNumber(idfNumber, ucrNumber)
                                                                                .let { idfEntity ->
                                                                                    daoServices.rfcSaveIDFUsed(idfEntity, loggedInUser, map)
                                                                                }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        return "$redirectRFCListPage=${rfcTypeID}"
                                                    }
                                        }
                            }
                }
    }


    @PreAuthorize("hasAuthority('IMPORTER')")
    @PostMapping("rfc-item/new/save")
    fun applicationForRFCItems(
            model: Model,
            @RequestParam(value = "rfcID") rfcID: Long,
            @ModelAttribute("rfcCoiItemEntity") @Valid rfcCoiItemEntity: RfcCoiItemsDetailsEntity,
            results: BindingResult,
            redirectAttributes: RedirectAttributes
    ): String {
        commonDaoServices.serviceMapDetails(appId)
                .let { map ->
                    commonDaoServices.loggedInUserDetails()
                            .let { loggedInUser ->
                                daoServices.findRFCDetailsWithID(rfcID)
                                        .let { rfcDetails ->
                                            daoServices.rfcItemSave(rfcCoiItemEntity, rfcDetails, loggedInUser, map)
                                                    .let { savedRFCItem ->
                                                        return "$redirectRFCDetailPage=${savedRFCItem.rfcId?.id}"
                                                    }
                                        }

                            }
                }


    }


}
