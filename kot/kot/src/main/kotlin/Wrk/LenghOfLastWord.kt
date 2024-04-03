package Wrk

class LenghOfLastWord {
    fun lengthOfLastWord(s: String) :Int{

        val words = s.trim().split(" ")

            return if (words.isNotEmpty()) words.last().length else 0



    }
}