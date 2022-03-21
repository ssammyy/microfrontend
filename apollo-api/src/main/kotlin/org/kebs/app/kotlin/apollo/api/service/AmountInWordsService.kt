package org.kebs.app.kotlin.apollo.api.service;

import java.math.BigDecimal
import java.math.RoundingMode


class AmountInWordsService {
    val ONES = arrayOf("Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine")
    var twodigits = arrayOf("Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")
    val TENS = arrayOf("", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninty")
    val POWER_10_DIGITS = arrayOf("million", "billion", "trillion")


    fun amountToWords(number: BigDecimal, currencyName: String = "shilling"): String {
        if (BigDecimal.ZERO.equals(number)) {
            return ""
        }
        val decimaled = number.setScale(2, RoundingMode.UP)
        val numberParts = decimaled.toString().split(".");
        val builder = StringBuilder()
        if (numberParts.size > 1) {
            var cents = "cents";
            if (numberParts[1].equals("01")) {
                cents = "cent";
            } else if (numberParts[1].equals("00")) {
                cents = "";
            }
            if (cents.isEmpty()) {
                return toWords(numberParts[0]);
            }
            builder.append(this.toWords(numberParts[0]))
            builder.append(" ")
            builder.append(currencyName)
            if (number > BigDecimal.ONE) {
                builder.append("s")
            }
            builder.append(", ")
            builder.append(toWords(numberParts[1]))
            builder.append(" ")
            builder.append(cents)
        } else {
            builder.append(this.toWords(numberParts[0]))
        }
        println("Value[$number]: $builder")
        return builder.toString().trim()
    }

    private fun toWordsN(number: String): String {
        if (number.length > 2) {
            throw Exception("Expected 0-99")
        }
        // All are zeros
        if (number.count { it == '0' } == 2) {
            return ""
        }
        if (number.length == 0) {
            return ""
        }
        if (number.length == 1) {
            return ONES[number.toInt()]
        }
        val builder = StringBuilder()
//        println("Data $number")
//        println("N1: ${number[1]},N0: ${number[0]}")
        if (number[1] == '0') {
            builder.append(TENS[number[0] - '0'])
        } else if (number[0] == '1') {
            builder.append(twodigits[number[1] - '0'])
        } else {
            builder.append(TENS[number[0] - '0'] + " " + ONES[number[1] - '0'])
        }
        return builder.toString()
    }

    /**
     * Format upto 999
     */
    private fun toWordsNn(number: String): String {
//        println("Value: $number")
        val length = number.length
        if (length > 3) {
            throw Exception("Expected upto 3 digit number: $number")
        }
        if (length == 0) {
            return ""
        }
        if (length == 1) {
            return ONES[number.toInt()]
        }
        val builder = StringBuilder(512)
        if (length == 2) {
            builder.append(toWordsN(number))
        } else {
//            println("N2: ${number[2]} N1: ${number[1]},N0: ${number[0]}")
            if (number[0] != '0') {
                builder.append(ONES[number[0] - '0'])
                if (number[1] == '0' && number[2] == '0') {
                    builder.append(" Hundred ")
                } else {
                    builder.append(" Hundred and ")
                    builder.append(toWordsN(number[1] + "" + number[2]))
                }
            } else {
                builder.append(toWordsN(number[1] + "" + number[2]))
            }
        }
        return builder.toString()
    }

    /**
     * Format upto 999,999
     */
    private fun toWordsNnn(number: String): String {
        val length = number.length
        if (length <= 3) {
            throw Exception("Expected number greater than one thousand")
        }
        if (length > 6) {
            throw Exception("Expected number less than one million")
        }
        var allZeros = number.count { it == '0' }
        if (allZeros == length) {
            return ""
        }
        val hundrends = number.substring(number.length - 3)
//        print("Hundrends $hundrends")
        var thousands = ""
        if (length > 3) {
            thousands = number.substring(0, length - 3)
        }
//        print("Thousands $thousands")
        val builder = StringBuilder(512)
        builder.append(toWordsNn(thousands))
        builder.append(" Thousand ")
        allZeros = hundrends.count { it == '0' }
        if (thousands.isNotEmpty() && allZeros != 3) {
            builder.append(toWordsNn(hundrends))
        }
        return builder.toString()
    }

    private fun toWords(number: String): String {
//        println("N: $number")
        val builder = StringBuilder(512)
        var length = number.length
        if (length <= 3) {
            builder.append(toWordsNn(number))
        } else if (length <= 6) {
            builder.append(toWordsNnn(number))
        } else {
            val firstNumber = number.reversed().substring(0, 6).reversed()
            val lastNumber = number.reversed().substring(6).reversed()
            println("First Number: $firstNumber")
            println("Last Number: $lastNumber")
            val characterGroups = lastNumber.reversed().split(Regex("(?<=\\G.{3})"))
            var count = characterGroups.size - 1
            while (count >= 0) {
                val current = characterGroups[count].reversed()
                println("Value MMM: $count -> $current")
                when {
                    current.count { it == '0' } == 3 -> {
                        // ignore zeros
                        print("ignore")
                    }
                    current.isEmpty() -> {
                        print("ignore")
                    }
                    else -> {
                        when (current.length) {
                            1 -> {
                                builder.append(toWordsN(current))
                            }
                            2, 3 -> {
                                builder.append(toWordsNn(current))
                            }
                        }
                        builder.append(" ")
                        builder.append(POWER_10_DIGITS[count])
                        builder.append(" ")
                    }
                }
                count--
            }
            builder.append(toWordsNnn(firstNumber))
        }
        return builder.toString();
    }
}


fun main(args: Array<String>) {
    val service = AmountInWordsService()
    println("Value: " + service.amountToWords(BigDecimal.ONE));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(10)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(12)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(17)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(40)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(22)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(37)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(97)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(33.45)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(379)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(222)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(22234)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(222356)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(20200080)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(820200080)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(1820200080)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(109820200080)));
    println("Value: " + service.amountToWords(BigDecimal.valueOf(1109820200080)));
}
