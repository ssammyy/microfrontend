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

package org.kebs.app.kotlin.apollo.api.security.bearer

import com.lowagie.text.pdf.codec.Base64
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import javax.servlet.http.HttpServletRequest

@Component
class AuthorizationPayloadExtractorService {
    fun extract(t: ServerWebExchange): String? {
        return t
                .request
                .headers
                .getFirst(HttpHeaders.AUTHORIZATION)
    }

    fun extract(r: HttpServletRequest): String? {
        return r.getHeader(HttpHeaders.AUTHORIZATION)
    }

    fun extractBasic(r: HttpServletRequest): Pair<String,String>? {
        val basicAuth = r.getHeader(HttpHeaders.AUTHORIZATION)
        if(StringUtils.hasLength(basicAuth)){
            try {
                if (basicAuth.startsWith("Basic")) {
                    val data = basicAuth.replace("Basic", "").trim()
                    val decoded = String(Base64.decode(data)).split(":")
                    if(decoded.size==2){
                        return Pair(decoded[0],decoded[1])
                    } else{
                        KotlinLogging.logger {  }.info("Error decoding basic header, expected 2 parts")
                    }

                }
            }catch (ex: Exception){
                KotlinLogging.logger {  }.info("Error decoding basic header")
            }
        }
        return null
    }
}