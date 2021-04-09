import org.springframework.boot.gradle.tasks.bundling.BootJar


description = "apollo-store"


dependencies {
    api(project(":apollo-config"))
//    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    api("org.springframework.boot:spring-boot-starter-data-jpa")

//    implementation("hikari-cp:hikari-cp:2.12.0")
    /**
     * Validation
     */
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.11.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.oracle.database.jdbc:ojdbc10:19.8.0.0")
//    implementation("org.hibernate.validator:hibernate-validator:6.1.5.Final")
    api("com.hazelcast:spring-data-hazelcast:2.4.0")
//    implementation("com.hazelcast:hazelcast:4.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}
application {
//    mainClassName = "org.kebs.app.kotlin.apollo.store.StoreApplicationKt"
    application.applicationName = "apollo-store"
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
//    mainClassName = "org.kebs.app.kotlin.apollo.store.StoreApplicationKt"

}

tasks.getByName<Jar>("jar") {
    enabled = true
}
