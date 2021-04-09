import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "apollo-config"


dependencies {
    implementation(project(":apollo-common"))
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.kafka:spring-kafka:2.5.1.RELEASE")
//    implementation("org.scala-lang.modules:scala-parallel-collections_2.13:1.0.0")


    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.kafka:spring-kafka-test:2.5.1.RELEASE")


}
application {
//    mainClassName = "org.kebs.app.kotlin.apollo.config.ConfigApplicationKt"
    application.applicationName = "apollo-config"
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
//    mainClassName = "org.kebs.app.kotlin.apollo.config.ConfigApplicationKt"
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
