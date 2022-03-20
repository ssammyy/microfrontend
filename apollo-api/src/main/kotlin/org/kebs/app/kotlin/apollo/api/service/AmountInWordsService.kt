package org.kebs.app.kotlin.apollo.api.service;

import java.math.BigDecimal
import java.math.RoundingMode


class AmountInWordsService {
    val ONES = arrayOf("", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine")
    var twodigits = arrayOf("Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")
    val TENS = arrayOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninty")
    val POWER_10_DIGITS = arrayOf("", "", "Hundred", "Thousand", "thousand", "million", "million", "billion", "billion", "trillion")


    fun amountToWords(number: BigDecimal): String {
        if (BigDecimal.ZERO.equals(number)) {
            return ""
        }
        val decimaled = number.setScale(2, RoundingMode.UP)
        val numberParts = decimaled.toString().split(".");
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
            return this.toWords(numberParts[0]) + "," + toWords(numberParts[1]) + " " + cents;
        } else {
            return this.toWords(numberParts[0]);
        }
    }

    private fun toWords(number: String): String {
        val builder = StringBuilder()
        println("N: $number")
        val length = number.length
        if (length == 0) {
            return ""
        }
        if (length == 1) {
            return ONES[number.toInt()]
        }
        if (length == 2) {
            println("N1: ${number[1]},N0: ${number[0]}")
            if (number[1] == '0') {
                return TENS[number[1] - '0']
            } else if (number[0] == '1') {
                return twodigits[number[1] - '0']
            } else {
                return TENS[number[0] - '0'] + " " + ONES[number[1] - '0']
            }
        } else {
            var count = length - 1
            while (0 < count) {
                when {
                    number[count] - '0' != 0 -> {
                        // ignore zeros
                        print("ignore")
                    }
                    count >= 3 -> {
                        builder.append(ONES[number[count] - '0']);
                        builder.append(POWER_10_DIGITS[number[count - 3] - '0'])
                    }
                    else -> {

                    }
                }
                count--
            }
        }
        return builder.toString();
    }
}


fun main(args: Array<String>) {
    val service = AmountInWordsService()
    println("Value: " + service.amountToWords(BigDecimal.ONE));
    println("Value 2: " + service.amountToWords(BigDecimal.valueOf(10)));
    println("Value 2: " + service.amountToWords(BigDecimal.valueOf(12)));
    println("Value 2: " + service.amountToWords(BigDecimal.valueOf(17)));
    println("Value 2: " + service.amountToWords(BigDecimal.valueOf(40)));
    println("Value 3: " + service.amountToWords(BigDecimal.valueOf(22)));
    println("Value 3: " + service.amountToWords(BigDecimal.valueOf(37)));
    println("Value 3: " + service.amountToWords(BigDecimal.valueOf(97)));
    println("Value 33.: " + service.amountToWords(BigDecimal.valueOf(33.45)));
    println("Value 3: " + service.amountToWords(BigDecimal.valueOf(379)));


}
