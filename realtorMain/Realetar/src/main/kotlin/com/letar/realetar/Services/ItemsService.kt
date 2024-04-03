package com.letar.realetar.Services

import com.letar.realetar.Models.Items
import com.letar.realetar.Repositories.ItemsRepository
import com.letar.realetar.Repositories.OrderRepo
import org.springframework.stereotype.Service

@Service
class ItemsService(
    private val itemsRepository: ItemsRepository
) {
    fun getAllItems(): List<Items> {
        return itemsRepository.findAll()
    }

    fun getItemsById(id: Long): Items? {
        return itemsRepository.findById(id).orElse(null)
    }

    fun createItems(items: Items): Items {
        return itemsRepository.save(items)
    }

    fun updateItems(items: Items): Items {
        return itemsRepository.save(items)
    }

    fun deleteItems(id: Long) {
        itemsRepository.deleteById(id)
    }

}