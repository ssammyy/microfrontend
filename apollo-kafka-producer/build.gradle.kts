import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    war
}

description = "apollo-kafka-producer"

dependencies {
    api(project(":apollo-common"))
    implementation("org.json:json:20190722")
    implementation("org.apache.kafka:kafka-clients:2.4.0")
    implementation("org.springframework.kafka:spring-kafka:2.3.4.RELEASE")
    implementation(project(":apollo-config"))
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation(kotlin("stdlib-jdk8"))

}
application {
    mainClassName = "org.kebs.app.kotlin.apollo.adaptor.kafka.producer.KafkaProducerApplicationKt"
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
    mainClassName = "org.kebs.app.kotlin.apollo.adaptor.kafka.producer.KafkaProducerApplicationKt"
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