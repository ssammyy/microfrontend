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

package org.kebs.app.kotlin.apollo.ipc.ports.provided

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.ports.required.IObjectMapper
import org.springframework.stereotype.Component
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

@Component
class CustomGenericObjectMapper : IObjectMapper {
    override fun <I : Any, O : Any> forward(i: I?, o: O?): O? {
        i?.let { ip ->

            ip::class.memberProperties.forEach { m ->
                //                m.isAccessible = true
                val v = (m as KMutableProperty1<I, Any>).get(ip)
                KotlinLogging.logger { }.trace { "Working on ${m.name} -> $v" }
                when (val p1 = o!!::class.memberProperties.firstOrNull { it.name.equals(m.name, ignoreCase = true) }) {
                    is KMutableProperty<*> -> {
                        try {
                            (p1 as KMutableProperty1<O, Any>).set(o, v)


                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error { "${m.name} ${e.message}" }
                        }

                    }
                    else -> {
                        KotlinLogging.logger { }.error { "Missing or Immutable field ${m.name} -> $v, skipping ..." }
                    }
                }
            }
            KotlinLogging.logger { }.info { "Extracted $o" }
//            o.toString()
        }


        return o
    }

    override fun <I : Any, O : Any> backward(i: I, o: O): I {
        TODO("Not yet implemented")
    }

}