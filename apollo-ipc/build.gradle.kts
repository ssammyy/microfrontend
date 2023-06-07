import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
}

description = "apollo-ipc"

dependencies {
    implementation(project(":apollo-common"))
    implementation(project(":apollo-store"))
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.kafka:spring-kafka-test")
    implementation(kotlin("stdlib-jdk8"))
}


application {
//    mainClassName = "org.kebs.app.kotlin.apollo.ipc.IpcApplicationKt"
    application.applicationName = "apollo-ipc"
}

tasks.getByName<BootJar>("bootJar") {
    enabled = true
//    mainClassName = "org.kebs.app.kotlin.apollo.ipc.IpcApplicationKt"
    archiveClassifier.set("boot")
}

tasks.getByName<Jar>("jar") {
    enabled = true
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