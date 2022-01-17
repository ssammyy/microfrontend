package org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class PvocAgentOfficerCommunications(
) {

    @Autowired
    private val restTemplate: RestTemplate? = null

//put url to properties

    //create pojo
    private val url = "http://localhost:8005/api/pvoc/send/monitoring/queries"
    private val url2 = "http://localhost:8005/api/pvoc/get/monitoring/queries"

//    @GetMapping("/")
//    fun getQueries(): List<Any?>? {
//        val queries = restTemplate!!.getForObject(
//            url,
//            Any::class.java
//        ) as Array<Any>?
//        return Arrays.asList(queries)
//    }

}