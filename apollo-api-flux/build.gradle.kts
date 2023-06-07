import org.springframework.boot.gradle.tasks.bundling.BootWar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
}
description = "apollo-api Test Bed"

dependencies {
    implementation(project(":apollo-common"))
    implementation(project(":apollo-store"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
//    implementation("org.springframework.boot:spring-boot-starter-web") {
//        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
//    }
    /**
     * Validation
     */
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")


    implementation("io.jsonwebtoken:jjwt:0.9.1")


    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    implementation(kotlin("stdlib-jdk8"))
}

application {
//    mainClassName = "org.kebs.app.kotlin.apollo.api.flux.ApiFluxApplicationKt"
  //  application.applicationName = "apollo-api-flux"
}

tasks.withType<BootWar>().configureEach {
    launchScript()
}
repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
