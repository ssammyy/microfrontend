package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc
//
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
//import org.kebs.app.kotlin.apollo.api.service.MyEmailService
//import org.kebs.app.kotlin.apollo.api.service.OTPService
//import org.kebs.app.kotlin.apollo.api.utils.EmailTemplate
//import org.springframework.security.core.context.SecurityContextHolder
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.*
//
//
//@Controller
//class OtpController(
//    private val commonDaoServices: CommonDaoServices
//) {
//
//
//    @Autowired
//    var otpService: OTPService? = null
//
//    @Autowired
//    var myEmailService: MyEmailService? = null
//    @GetMapping("/generateOtp")
//    fun generateOtp(): String {
//        val auth = commonDaoServices.getLoggedInUser()?.userName
//        val otp: Int? = auth?.let { otpService?.generateOTP(it) }
//
//        //Generate The Template to send OTP
//        val template = EmailTemplate("SendOtp.html")
//        val replacements: MutableMap<String, String> = HashMap()
//        replacements["user"] = auth.toString()
//        replacements["otpnum"] = otp.toString()
//        val message: String? = template.getTemplate(replacements)
//        myEmailService?.sendOtpMessage("shrisowdhaman@gmail.com", "OTP -SpringBoot", message)
//        return "otppage"
//    }
//
//    @RequestMapping(value = ["/validateOtp"], method = [RequestMethod.GET])
//    @ResponseBody
//    fun validateOtp(@RequestParam("otpnum") otpnum: Int): String {
//        val SUCCESS = "Entered Otp is valid"
//        val FAIL = "Entered Otp is NOT valid. Please Retry!"
//
//        val username: String? = commonDaoServices.getLoggedInUser()?.userName
//
//        //Validate the Otp
//        return if (otpnum >= 0) {
//            val serverOtp: Int = username?.let { otpService?.getOtp(it) } ?: 0
//            if (serverOtp > 0) {
//                if (otpnum == serverOtp) {
//                    username?.let { otpService?.clearOTP(it) }
//                    "Entered Otp is valid"
//                } else {
//                    SUCCESS
//                }
//            } else {
//                FAIL
//            }
//        } else {
//            FAIL
//        }
//    }
//}