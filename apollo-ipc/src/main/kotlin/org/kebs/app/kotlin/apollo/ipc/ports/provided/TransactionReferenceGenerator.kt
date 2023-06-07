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
import org.apache.commons.codec.binary.Hex
import org.kebs.app.kotlin.apollo.common.ports.required.ITransactionReferenceGenerator
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

class TransactionReferenceGenerator(
    private val length: Int,
    secureRandomAlgorithm: String,
    messageDigestAlgorithm: String
) : ITransactionReferenceGenerator<String> {
    private var prng = SecureRandom.getInstance(secureRandomAlgorithm)
    private var sha = MessageDigest.getInstance(messageDigestAlgorithm)
    private var randomNum = ""
    override fun generateTransactionReference(): String {

        return try {
            randomNum = prng.nextInt().toString()
            Hex.encodeHexString(sha.digest(randomNum.toByteArray())).substring(0, length)
        } catch (e: NoSuchAlgorithmException) {
            KotlinLogging.logger { }.error(e.message, e)
            ""
        }
    }
}
