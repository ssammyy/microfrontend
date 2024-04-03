package Wrk

class RemoveDuplicates {
    fun removeDuplicates(nums : IntArray): Int{
        var uniqueIndex = 0
        for(i in 1 until nums.size){
            if(nums[i] != nums[uniqueIndex]){
                ++uniqueIndex
                nums[uniqueIndex] = nums[i]
            }

        }
        return nums.size.minus(uniqueIndex)
    }
}