package org.kebs.app.kotlin.apollo.api.controllers.diControllers

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionAgrochemItemChecklistEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionEngineeringItemChecklistEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionMotorVehicleItemChecklistEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdInspectionOtherItemChecklistEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("checklist-content")
class DiChecklistContentController(
        private val commonDaoServices: CommonDaoServices,
        private val daoServices: DestinationInspectionDaoServices
) {

    @RequestMapping("")
    fun loadContent(): String? {
        return ""
    }

    @RequestMapping("agrochem_checklist")
    fun getAgrochemChecklist(
            model: Model,
            @RequestParam("itemID",) itemID: Long,
    ): String? {
        model.addAttribute("item", daoServices.findItemWithItemID(itemID))
        model.addAttribute("agrochemItemInspectionChecklist", CdInspectionAgrochemItemChecklistEntity())
        return "fragments/inspection-checklist-fragment :: agrochemChecklist"
    }

    @RequestMapping("engineering_checklist")
    fun getEngineeringChecklist(
            model: Model,
            @RequestParam("itemID",) itemID: Long,
    ): String? {
        model.addAttribute("item", daoServices.findItemWithItemID(itemID))
        model.addAttribute("engineeringItemInspectionChecklist", CdInspectionEngineeringItemChecklistEntity())
        return "fragments/inspection-checklist-fragment :: agrochemChecklist"
    }

    @RequestMapping("other_item_checklist")
    fun getOtherItemChecklist(
            model: Model,
            @RequestParam("itemID",) itemID: Long,
    ): String? {
        model.addAttribute("item", daoServices.findItemWithItemID(itemID))
        model.addAttribute("otherItemInspectionChecklist", CdInspectionOtherItemChecklistEntity())
        return "fragments/inspection-checklist-fragment :: otherItemChecklist"
    }

    @RequestMapping("motor_vehicle_checklist")
    fun getMotorVehicleChecklist(model: Model): String? {
        model.addAttribute("motorVehicleItemInspectionChecklist", CdInspectionMotorVehicleItemChecklistEntity())
        model.addAttribute("currentDate", commonDaoServices.getCurrentDate())

        return "fragments/inspection-checklist-fragment :: motorVehicleChecklist"
    }
}