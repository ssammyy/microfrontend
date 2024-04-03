package com.renegade.parcel.DAO.Repos

import com.renegade.parcel.DAO.Models.ItemCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemCategoryRepository : JpaRepository<ItemCategory, Long> {

}
