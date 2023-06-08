import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
}

description = "apollo-pvoc-mock"

dependencies {
    implementation("org.json:json:20190722")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(kotlin("stdlib-jdk8"))

}
application {
    mainClassName = "org.kebs.app.kotlin.pvoc.mock.PvocMock"
    application.applicationName = "apollo-pvoc-mock"
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

repositories {
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