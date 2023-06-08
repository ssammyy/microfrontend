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

package org.kebs.app.kotlin.apollo.common.dto

import java.time.Instant

open class BaseEntity<I>(
    open val id: I? = null,
    open val name: String = "",
    open val description: String = "",
    open val status: Int = 0
) {

    var createdBy: String = ""
    var createdOn: Instant = Instant.now()
    var modifiedBy: String = ""
    var modifiedOn: Instant = Instant.now()
    var deletedBy: String = ""
    var deletedOn: Instant = Instant.now()


    override fun toString(): String {

        return "[id=$id, name=$name, description=$description, status=$status]"
    }

}