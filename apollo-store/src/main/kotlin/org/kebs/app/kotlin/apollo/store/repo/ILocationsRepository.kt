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
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ILocationsRepository : HazelcastRepository<LocationsEntity, Long> {
    fun findAllByStatus(status: Int): Collection<LocationsEntity>?
}

@Repository
interface ICountiesRepository : HazelcastRepository<CountiesEntity, Long> {
    fun findByStatusOrderByCounty(status: Int): List<CountiesEntity>?
    fun findByRegionIdAndStatus(regionId: Long, status: Int): List<CountiesEntity>?
    fun findByIdAndStatus(id: Long, status: Int): CountiesEntity?
}

@Repository
interface ISubRegionsRepository : HazelcastRepository<SubRegionsEntity, Long> {
    fun findByStatusOrderBySubRegion(status: Int): List<SubRegionsEntity>?
    fun findByRegionIdAndStatus(regionId: RegionsEntity, status: Int): List<SubRegionsEntity>?
}

@Repository
interface IRegionsRepository : HazelcastRepository<RegionsEntity, Long> {
    fun findByStatusOrderByRegion(status: Int): List<RegionsEntity>?
    fun findByIdAndStatus(id: Long, status: Int): RegionsEntity?

}

@Repository
interface ITownsRepository : HazelcastRepository<TownsEntity, Long> {
    fun findByStatusOrderByTown(status: Int): List<TownsEntity>?
    fun findByCountiesAndStatus(counties: CountiesEntity, status: Int): List<TownsEntity>?
    fun findByIdAndStatus(id: Long, status: Int): TownsEntity?
}

