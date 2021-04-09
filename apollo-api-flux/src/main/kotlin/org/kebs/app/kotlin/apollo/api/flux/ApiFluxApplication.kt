package org.kebs.app.kotlin.apollo.api.flux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["org.kebs.app.kotlin.apollo"])
class ApiFluxApplication

fun main(args: Array<String>) {
    runApplication<ApiFluxApplication>(*args)
}
