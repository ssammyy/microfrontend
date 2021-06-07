

plugins {
    war
}

group = "org.kebs.app.kotlin.apollo"
version = "2.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":apollo-store"))
    implementation(project(":apollo-common"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flowable:flowable-spring-boot-starter-basic:6.6.0")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("javax.validation:validation-api:2.0.1.Final")
    implementation ("org.springframework.boot:spring-boot-starter-mail:1.2.0.RELEASE")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.oracle.database.jdbc:ojdbc8")
    /**
     * JWT
     */
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    /**
     * Spring Security
     */
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")
    /**
     * Testing
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")


}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}



tasks.withType<Test> {
    useJUnitPlatform()
}
