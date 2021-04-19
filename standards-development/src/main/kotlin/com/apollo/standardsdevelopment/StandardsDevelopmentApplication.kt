package com.apollo.standardsdevelopment

import com.apollo.standardsdevelopment.repositories.CustomerRespository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StandardsDevelopmentApplication(val customerRespository: CustomerRespository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        var listCustomers   = customerRespository.findAll()
        for (element in listCustomers) {
            println(element.name)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<StandardsDevelopmentApplication>(*args)
}
