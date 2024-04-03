package com.letar.realetar.Services

import com.letar.realetar.DataClasses.ExcelData
import com.letar.realetar.Models.MaterialSchedule
import com.letar.realetar.Repositories.MaterialScheduleRepository
import com.letar.realetar.Repositories.OrderRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MaterialScheduleService(
    private val materialScheduleRepository: MaterialScheduleRepository,
    private val orderService: OrderService
) {
    fun getAllMaterialSchedules(): List<MaterialSchedule> {
        return materialScheduleRepository.findAll()
    }
    fun getMaterialScheduleById(id: Long): MaterialSchedule? {
        return materialScheduleRepository.findById(id).orElse(null)
    }

    fun createMaterialSchedule(materialSchedule: MutableList<ExcelData>): MutableList<MaterialSchedule> {
        val entityList = mutableListOf<MaterialSchedule>()
       for (item in materialSchedule){
           val entity = MaterialSchedule()
           entity.structure = item.structure
           entity.material = item.material
           entity.element = item.element
           entity.floor = item.floor
           entity.orderDate = item.orderDate
           entity.quantity = item.quantity.toInt()
           entity.itemName = item.itemName
           entity.itemId = item.itemId.replace(".0", "")
           entity.startDate = item.startDate
           entityList.add(entity)
           materialScheduleRepository.save(entity)
       }
        orderService.generateOrders(entityList)
        return entityList
    }
    fun updateMaterialSchedule(materialSchedule: MaterialSchedule): MaterialSchedule {
        return materialScheduleRepository.save(materialSchedule)
    }

    fun saveExcelData(excelDataEntities: List<MaterialSchedule>) {
        println("DATA SIZE "+ excelDataEntities.size)
        materialScheduleRepository.saveAll(excelDataEntities)
    }

    fun deleteMaterialSchedule(id: Long) {
        materialScheduleRepository.deleteById(id)
    }

}