/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.common.utils

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.ports.provided.RandomTextGenerator
import org.springframework.expression.Expression
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.SpelParserConfiguration
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.security.MessageDigest
import java.sql.Date
import java.sql.Timestamp
import java.text.MessageFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun generateRandomText(length: Int = 12,
                       secureRandomAlgorithm: String = "SHA1PRNG",
                       messageDigestAlgorithm: String = "SHA-512", prefix: Boolean = false): String {
    var prefixText = ""
    val generator = RandomTextGenerator(length, secureRandomAlgorithm, messageDigestAlgorithm)
    when {
        prefix -> prefixText = DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now())
    }
    return "${prefixText}${generator.generateTransactionReference()}"
}

fun getRandomNumberString(): String? {
    val rnd = Random()
    val number = rnd.nextInt(999999)
    val genNumber= String.format("%06d", number)
    var prefixText = DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now())

    // this will convert any number sequence into 6 character.
    return "${prefixText}${genNumber}"
}

fun rand(start: Long, end: Long): Long {
    require(start <= end) { "Illegal Argument" }
    return (start..end).random()
}

/**
 * A custom function which replaces prefixed values in a List of Strings
 * with values from an object, useful when replacing parameter values using SPEL
 * and quite handy in easing configurations
 *
 * @return List<String>
 */
fun <D> List<String>.replacePrefixedItemsWithObjectValues(
        data: D,
        prefix: String?,
        replacement: String?,
        block: (D?, String) -> String?
)
        : List<String> = map { currentItem ->

    prefix?.let { p ->
        replacement
                ?.let { r ->
                    when {
                        currentItem.startsWith(p) -> {
                            block(data, currentItem.replace(p, r))?.trim() ?: currentItem
                        }
                        else -> currentItem
                    }
                } ?: currentItem
    } ?: currentItem

}

fun composeUsingSpel(data: Any?, expression: String?): String? {
    var result: String? = null
    try {
        expression
                ?.let { expr ->
                    val stdContext = StandardEvaluationContext(data)
                    val config = SpelParserConfiguration(true, true)
                    val parser: ExpressionParser = SpelExpressionParser(config)
                    val exp: Expression = parser.parseExpression(expr)
                    val o = exp.getValue(stdContext)

                    result = when (o) {

                        is String -> o
                        is Long ->  String.format("%,2d", o.toInt())
                        //Todo change Long To String
                        is BigDecimal -> String.format("%,.2f", o.setScale(2, RoundingMode.DOWN))
                        is Int -> String.format("%,2d", o)
                        is BigInteger -> String.format("%,.2d", o)
                        is Timestamp -> DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss Z").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(o.toInstant())
                        is Date -> DateTimeFormatter.ofPattern("dd-MMM-yyyy").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(o.toLocalDate())
                        is java.util.Date -> DateTimeFormatter.ofPattern("dd-MMM-yyyy").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault()).format(o.toInstant())
                        else -> try {
                            o as String
                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error(e.message)
                            KotlinLogging.logger { }.debug(e.message, e)
                            null
                        }

                    }


                }
            ?: throw InvalidValueException("Parameter expression should not be null", NullPointerException())

    } catch (e: Exception) {
        KotlinLogging.logger { }.error("${e.message}")
        KotlinLogging.logger { }.debug(e.message, e)
    }

    return result
}

fun String.md5(): String {
    return hashString(this, "MD5")
}

fun String.sha256(): String {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}

fun placeHolderMapper(input: String?, parameters: Array<String>): String? =
    try {
        input
            ?.let { i ->
                val formatter = MessageFormat(i)
                formatter.format(parameters)
            }
            ?: throw InvalidInputException("Input should not be null")


    } catch (e: Exception) {
        KotlinLogging.logger { }.error(e.message, e)
        KotlinLogging.logger { }.debug(e.message, e)
        null
    }

