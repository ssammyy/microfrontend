/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.ManufacturePlantDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaProcessStatusEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface IManufacturerRepository : HazelcastRepository<ManufacturersEntity, Long> {
    fun findByUserIdAndStatus(userId: UsersEntity, status: Int): ManufacturersEntity?
    fun findByIdAndStatus(id: Long, status: Int): ManufacturersEntity?
    fun findByUserId(userId: UsersEntity?): ManufacturersEntity?
    fun findByOrderByIdDesc(page: Pageable?): Page<ManufacturersEntity>?
}

@Repository
interface IManufacturerProductRepository : HazelcastRepository<ManufactureProductsEntity, Long> {
    fun findByProductName(productName: String): ManufactureProductsEntity?
    fun findByManufacturerId(manufacturerId: Long): ManufactureProductsEntity?
    fun findByManufacturerIdAndStatus(manufacturerId: Long, status: Int): List<ManufactureProductsEntity>
}

@Repository
interface IManufacturePlantDetailsRepository : HazelcastRepository<ManufacturePlantDetailsEntity, Long> {
//    fun findByProductName(productName: String): ManufacturePlantDetailsEntity?
    fun findByUserId(manufactureId: Long): List<ManufacturePlantDetailsEntity>?
    fun findByCompanyProfileId(manufactureId: Long): List<ManufacturePlantDetailsEntity>?
    fun findByCompanyProfileIdAndStatus(manufactureId: Long, status: Int): List<ManufacturePlantDetailsEntity>?
}

@Repository
interface IManufacturerProductBrandRepository : HazelcastRepository<ManufactureBrandEntity, Long> {
    fun findByBrandName(brandName: String): ManufactureBrandEntity?
    fun findByManufacturerIdAndStatus(manufacturerId: Long, status: Int): List<ManufactureBrandEntity>
    fun findByStatusOrderByBrandName(status: Int): List<ManufactureBrandEntity>?
}

@Repository
interface IManufacturerBranchRepository : HazelcastRepository<ManufactureBranchEntity, Long> {
    fun findByBranchName(branchName: String): ManufactureBranchEntity?
    fun findByManufacturerIdAndStatus(manufacturerId: Long, status: Int): List<ManufactureBranchEntity>
}

@Repository
interface IManufacturerPaymentDetailsRepository : HazelcastRepository<ManufacturePaymentDetailsEntity, Long> {
    fun findByStatus(status: Int): List<ManufacturePaymentDetailsEntity>
    fun findByManufacturerIdAndStatus(manufacturerId: Long, status: Int): ManufacturePaymentDetailsEntity?
    fun findByManufacturerId(manufacturerId: Long): ManufacturePaymentDetailsEntity?
}

@Repository
interface IBusinessLinesRepository : HazelcastRepository<BusinessLinesEntity, Long> {
    fun findByStatusOrderByName(status: Int): List<BusinessLinesEntity>?
    fun findByStatus(status: Int): List<BusinessLinesEntity>?
    fun findByIdAndStatus(id: Long, status: Int): BusinessLinesEntity?
}

@Repository
interface IBusinessNatureRepository : HazelcastRepository<BusinessNatureEntity, Long> {
    fun findByStatus(status: Int): List<BusinessNatureEntity>?
    fun findByIdAndStatus(id: Long, status: Int): BusinessNatureEntity?
    fun findByBusinessLinesIdAndStatus(businessLinesId: BusinessLinesEntity, status: Int): List<BusinessNatureEntity>?
}

@Repository
interface IManufacturerAddressRepository : HazelcastRepository<ManufacturerAddressesEntity, Long> {
    fun findFirstByManufacturerIdAndStatusOrderByVersions(manufacturerId: ManufacturersEntity, status: Int): ManufacturerAddressesEntity?
    fun findByManufacturerIdAndStatusOrderByVersions(manufacturerId: ManufacturersEntity, status: Int): Collection<ManufacturerAddressesEntity>?
}

@Repository
interface IStdLevyNotificationFormRepository : HazelcastRepository<StdLevyNotificationFormEntity, Long> {
//    fun findFirstByManufacturerIdAndStatus(manufacturerId: ManufacturersEntity, status: Long): StdLevyNotificationFormEntity?
//    fun findByManufacturerIdAndStatus(manufacturerId: ManufacturersEntity, status: Long): Collection<StdLevyNotificationFormEntity>?
}

@Repository
interface IManufacturerContactsRepository : HazelcastRepository<ManufacturerContactsEntity, Long>{
    fun findByManufacturerId(manufacturerId: ManufacturersEntity): ManufacturerContactsEntity?
}

@Repository
interface ISdlSl1FormsRepository : HazelcastRepository<SdlSl1FormsEntity, Long>

@Repository
interface IManufacturerDepartmentsRepository : HazelcastRepository<ManufacturerDepartmentsEntity, Long>

@Repository
interface IBrsLookupManufacturerDataRepository : HazelcastRepository<BrsLookupManufacturerDataEntity, Long> {
    fun findFirstByRegistrationNumberAndStatusOrderById(registrationNumber: String, status: Int): BrsLookupManufacturerDataEntity?
}

@Repository
interface IBrsLookupManufacturerPartnersRepository : HazelcastRepository<BrsLookupManufacturerPartnersEntity, Long> {
    fun findBrsLookupManufacturerPartnersEntitiesByManufacturerIdAndStatus(manufacturerId: BrsLookupManufacturerDataEntity, status: Int): Collection<BrsLookupManufacturerPartnersEntity>?
}



