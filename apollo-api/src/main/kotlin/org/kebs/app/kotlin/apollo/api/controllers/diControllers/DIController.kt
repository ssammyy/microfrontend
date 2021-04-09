package org.kebs.app.kotlin.apollo.api.controllers.diControllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/di/")
class DIController {

//    /**
//     * Destination Inspection Home
//     */
//    @GetMapping("home")
//    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')  or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ')  or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ')  or hasAuthority('CD_SUPERVISOR_READ') or hasAuthority('IMPORTER') or hasAuthority('CD_OFFICER_READ') or hasAuthority('PVOC_APPLICATION_READ') or hasAuthority('PVOC_APPLICATION_PROCESS') or hasAuthority('MAKE_WAIVER_APPLICATION') or hasAuthority('WAIVERS_APPLICATION_REVIEW') or hasAuthority('WAIVER_APPLICATION_REPORT_PROCESS')")
//    fun homePage(): String {
//        return "destination-inspection/di-index"
//    }



    /**
     * Consignment with coc Home
     */
    @GetMapping("with/coc")
    fun cocPage(): String{
        return "destination-inspection/with-coc-index"
    }

    @GetMapping("with/ncr")
    fun ncrPage() : String{
        return "destination-inspection/with-ncr-index"
    }

    @GetMapping("with/cor")
    fun corPage() : String{
        return "destination-inspection/with-cor-index"
    }

    @GetMapping("with/pvoc")
    fun pvocPage(): String{
        return "destination-inspection/pvoc/index"
    }

    @GetMapping("exceptions-index")
    fun exceptionsIndex(): String{
        return "destination-inspection/pvoc/Exceptions_index"
    }

    @GetMapping("waivers-index")
    fun waiversIndex(): String{
        return "destination-inspection/pvoc/Waivers_index"
    }

    @GetMapping("monitoring-index")
    fun monitoringIndex(): String{
        return "destination-inspection/pvoc/monitoring/index"
    }

    @GetMapping("complaints-index")
    fun complaintsIndex(): String{
        return "destination-inspection/pvoc/complaint/index"
    }

    @GetMapping("reconsiliations-index")
    fun reconsilationIndex(): String{
        return "destination-inspection/pvoc/reconsiliation/index"
    }

    @GetMapping("auditing-index")
    fun auditingIndex(): String{
        return "destination-inspection/pvoc/auditing/index"
    }


}
