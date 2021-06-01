package org.kebs.app.kotlin.apollo.standardsdevelopment.seeders

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.*
import org.kebs.app.kotlin.apollo.standardsdevelopment.repositories.*
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DbSeeder(val standardRequestRepository: StandardRequestRepository,
               val departmentRepository: DepartmentRepository,
               val technicalCommitteeRepository: TechnicalCommitteeRepository,
               val productRepository: ProductRepository,
               val userRepository: UserRepository,
               val productSubCategoryRepository: ProductSubCategoryRepository,
               val standardNWIRepository: StandardNWIRepository,
               val standardJustificationRepository: StandardJustificationRepository,
               val standardWorkPlanRepository: StandardWorkPlanRepository
               ):CommandLineRunner {
    override fun run(vararg args: String?) {
        this.departmentRepository.deleteAll()
        this.technicalCommitteeRepository.deleteAll()
        this.productRepository.deleteAll()
        this.productSubCategoryRepository.deleteAll()
        this.userRepository.deleteAll()
        this.standardRequestRepository.deleteAll()
        this.standardNWIRepository.deleteAll()
        this.standardJustificationRepository.deleteAll()
        this.standardWorkPlanRepository.deleteAll()



        val departmentAgric = Department()
        departmentAgric.name="AGRICULTURE"
        departmentAgric.codes="01"

        val departmentCivil = Department()
        departmentCivil.name="CIVIL ENGINEERING"
        departmentCivil.codes="02"

        val departmentElec = Department()
        departmentElec.name="ELECTRICAL ENGINEERING"
        departmentElec.codes="04"

        val departmentFood = Department()
        departmentFood.name="FOOD"
        departmentFood.codes="05"

        val departmentMech = Department()
        departmentMech.name="MECHANICAL ENGINEERING"
        departmentMech.codes="06"

        val departmentICT = Department()
        departmentICT.name="ICT"
        departmentICT.codes="07"

        val departmentText = Department()
        departmentText.name="TEXTILE"
        departmentText.codes="08"

        val departmentServices = Department()
        departmentServices.name="SERVICES"
        departmentServices.codes="09"

        val departments = mutableListOf<Department>()

        departments.add(departmentAgric)
        departments.add(departmentCivil)
        departments.add(departmentElec)
        departments.add(departmentFood)
        departments.add(departmentMech)
        departments.add(departmentICT)
        departments.add(departmentText)
        departments.add(departmentServices)

        this.departmentRepository.saveAll(departments)

        val technicalCommittee = TechnicalCommittee()
        technicalCommittee.technical_committee_no="KEBS/POL 001"
        technicalCommittee.type="NMC/PC"
        technicalCommittee.tc=1
        technicalCommittee.sc=0
        technicalCommittee.wg=0
        technicalCommittee.parentCommitte="KEBS/POL 001"
        technicalCommittee.title="Kenya National Committee of IEC(KNCIEC)"
        technicalCommittee.status="Active"
        technicalCommittee.comment=""

        val technicalCommitteeTwo = TechnicalCommittee()
        technicalCommitteeTwo.technical_committee_no="KEBS/TC 001"
        technicalCommitteeTwo.type="NMC/TC"
        technicalCommitteeTwo.tc=1
        technicalCommitteeTwo.sc=0
        technicalCommitteeTwo.wg=0
        technicalCommitteeTwo.parentCommitte="KEBS/TC 001"
        technicalCommitteeTwo.title="Cereals and Pulses"
        technicalCommitteeTwo.status="Active"
        technicalCommitteeTwo.comment=""

        val technicalCommittees = mutableListOf<TechnicalCommittee>()
        technicalCommittees.add(technicalCommittee)
        technicalCommittees.add(technicalCommitteeTwo)

        this.technicalCommitteeRepository.saveAll(technicalCommittees)

        var wheat = Product()
        wheat.name="Wheat"
        wheat.status=1



        var rice = Product()
        rice.name="Rice"
        rice.status=1

        var products = mutableListOf<Product>()
        products.add(wheat)
        products.add(rice)

        this.productRepository.saveAll(products)

        var wheatSemolina = ProductSubCategory()
        wheatSemolina.name="Durum wheat semolina"
        wheatSemolina.status=1

        this.productSubCategoryRepository.save(wheatSemolina)

        wheatSemolina.product = wheat
        wheat.productSubCategory?.plus(wheatSemolina)




        this.productSubCategoryRepository.save(wheatSemolina)
        this.productRepository.save(wheat)

        val standardRequest = StandardRequest()
        standardRequest.requestNumber

        val standardRequestor = User()
        standardRequestor.name="Edwin Inganji"
        standardRequestor.email="einganji@gmail.com"
        standardRequestor.phoneNumber="0700402788"

        this.userRepository.save(standardRequestor)

        println("--- Database has been initialized")
        println("Wheat is $wheat")
        println("Wheat selmolina is $wheatSemolina")
    }

}
