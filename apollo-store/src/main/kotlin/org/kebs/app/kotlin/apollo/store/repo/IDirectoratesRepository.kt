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
interface IDirectoratesRepository : HazelcastRepository<DirectoratesEntity, Long> {
    fun findByIdAndStatusOrderByDirectorate(id: Long, status: Int): List<DirectoratesEntity>?
    fun findByStatusOrderByDirectorate(status: Int): List<DirectoratesEntity>?
}

@Repository
interface IDepartmentsRepository : HazelcastRepository<DepartmentsEntity, Long> {
    fun findByIdAndStatusOrderByDepartment(id: Long, status: Int): List<DepartmentsEntity>?
    fun findByStatusOrderByDepartment(status: Int): List<DepartmentsEntity>?
    fun findByDirectorateIdAndStatus(directorateId: DirectoratesEntity, status: Int): List<DepartmentsEntity>?
    fun findByDirectorateId(directorateId: DirectoratesEntity) : DepartmentsEntity
    fun findByDepartmentAndStatus(department: String, status: Int): DepartmentsEntity?

}

@Repository
interface IDivisionsRepository : HazelcastRepository<DivisionsEntity, Long> {
    fun findByIdAndStatusOrderByDivision(id: Long, status: Int): List<DivisionsEntity>?
    fun findByStatusOrderByDivision(status: Int): List<DivisionsEntity>?
    fun findByStatus(status: Int): List<DivisionsEntity>?
    fun findByDepartmentIdAndStatus(departmentId: DepartmentsEntity, status: Int): List<DivisionsEntity>?
}

@Repository
interface ISectionsRepository : HazelcastRepository<SectionsEntity, Long> {
    fun findByIdAndStatusOrderBySection(id: Long, status: Int): List<SectionsEntity>?
    fun findByStatusOrderBySection(status: Int): List<SectionsEntity>?
    fun findByStatus(status: Int): List<SectionsEntity>?
    fun findByDivisionIdAndStatus(divisionId: DivisionsEntity, status: Int): List<SectionsEntity>?
}

@Repository
interface ISubSectionsLevel1Repository : HazelcastRepository<SubSectionsLevel1Entity, Long> {
    fun findByIdAndStatusOrderBySubSection(id: Long, status: Int): List<SubSectionsLevel1Entity>?
    fun findByStatusOrderBySubSection(status: Int): List<SubSectionsLevel1Entity>?
    fun findBySectionIdAndStatus(sectionId: SectionsEntity, status: Int): List<SubSectionsLevel1Entity>?
}

@Repository
interface ISubSectionsLevel2Repository : HazelcastRepository<SubSectionsLevel2Entity, Long> {
    fun findBySubSectionAndStatus(subSection: String, status: Int): SubSectionsLevel2Entity?
    fun findByIdAndStatusOrderBySubSection(id: Long, status: Int): List<SubSectionsLevel2Entity>?
    fun findByStatusOrderBySubSection(status: Int): List<SubSectionsLevel2Entity>?
    fun findBySectionIdAndStatus(sectionId: SectionsEntity, status: Int): List<SubSectionsLevel2Entity>?
    fun findBySubSectionLevel1IdAndStatus(subSectionLevel1Id: SubSectionsLevel1Entity, status: Int): List<SubSectionsLevel2Entity>?
}


