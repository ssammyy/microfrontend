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

package org.kebs.app.kotlin.apollo.api.payload.request

import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdNwaUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.std.NWAJustification

class LoginRequest {
    var username: String? = null
    var password: String? = null

}

class CustomResponse {
    var response: String? = null
    var status: Int? = null
    var payload: String? = null
}

class JustificationTaskDataDto(
    var task: TaskDetails,
    var justification: NWAJustification?,
    var uploads: List<DatKebsSdNwaUploadsEntity>?)
