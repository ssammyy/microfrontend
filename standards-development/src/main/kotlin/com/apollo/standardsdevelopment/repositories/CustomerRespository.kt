package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRespository: JpaRepository<Customer, Int> {
}