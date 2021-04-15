package org.kebs.app.kotlin.apollo.api.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.springframework.stereotype.Service
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit


@Service
class OTPService {
    private val otpCache: LoadingCache<String, Int>

    //This method is used to push the opt number against Key. Rewrite the OTP if it exists
    //Using user id  as key
    fun generateOTP(key: String): Int {
        val random = Random()
        val otp: Int = 100000 + random.nextInt(900000)
        otpCache.put(key, otp)
        return otp
    }

    //This method is used to return the OPT number against Key->Key values is username
    fun getOtp(key: String): Int {
        return try {
            otpCache.get(key)
        } catch (e: Exception) {
            0
        }
    }

    //This method is used to clear the OTP catched already
    fun clearOTP(key: String) {
        otpCache.invalidate(key)
    }

    companion object {
        //cache based on username and OPT MAX 8
        private const val EXPIRE_MINS = 5
    }

    init {
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS.toLong(), TimeUnit.MINUTES)
            .build(object : CacheLoader<String, Int>() {
                fun load(key: String?): Int {
                    return 0
                }
                override fun load(key: String): Int {
                    TODO("Not yet implemented")
                }
            })
    }
}