import mu.KotlinLogging
import org.eclipse.jdt.core.compiler.InvalidInputException
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.StandardsLevyBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/")
class StandardLevy(
    applicationMapProperties: ApplicationMapProperties,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val businessNatureRepository: IBusinessNatureRepository,
    private val standardLevyFactoryVisitReportRepo: IStandardLevyFactoryVisitReportRepository,
    private val standardLevyPaymentsRepository: IStandardLevyPaymentsRepository,
    private val standardsLevyBpmn: StandardsLevyBpmn,
    private val commonDaoServices: CommonDaoServices,
    private val companyProfileRepo: ICompanyProfileRepository,
    private val userRepo: IUserRepository
){

    @GetMapping("sl/get-data")
    fun testGet(@RequestParam("manufacturerId") manufacturerId : Long): String {
        KotlinLogging.logger { }.info { "manufacturerId:  = ${manufacturerId}" }
        return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
    }

    @PostMapping("sl/save-data")
    fun saveFactoryVisitReport(@RequestParam("manufacturerId") manufacturerId : Long, reportData: StandardLevyFactoryVisitReportEntity) : String {
        standardLevyFactoryVisitReportRepo.findByManufacturerEntity(manufacturerId)?.let { report ->
            report.remarks = reportData.remarks
            report.purpose = reportData.purpose
            report.actionTaken = reportData.actionTaken
            report.personMet = reportData.personMet
            standardLevyFactoryVisitReportRepo.save(report)
            return "redirect:/sl/manufacturer?manufacturerId=${manufacturerId}"
        }?: throw InvalidInputException("Please enter a valid id")
    }


}