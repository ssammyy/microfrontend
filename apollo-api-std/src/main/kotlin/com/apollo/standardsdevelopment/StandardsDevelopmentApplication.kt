package com.apollo.standardsdevelopment

import com.apollo.standardsdevelopment.repositories.CustomerRespository
import com.apollo.standardsdevelopment.services.FilesStorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.Resource


@SpringBootApplication
class StandardsDevelopmentApplication(val customerRespository: CustomerRespository) : CommandLineRunner {
    @Resource
    var storageService: FilesStorageService? = null
    override fun run(vararg args: String?) {
        storageService?.deleteAll();
        storageService?.init();
        var listCustomers   = customerRespository.findAll()
        for (element in listCustomers) {
            println(element.name)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<StandardsDevelopmentApplication>(*args)
}
