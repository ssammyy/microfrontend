import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.4.30")
        classpath("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.4.3")
    }
}

//bootJar {
//    enabled = false
//}
//
//jar {
//    enabled = true
//}

allprojects {
    group = "org.kebs.app.kotlin.apollo"
    version = "2.0-SNAPSHOT"
    repositories {
        mavenCentral()
    }

//    apply(plugin = "jacoco")
}


subprojects {


    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.jvm")
//    apply(plugin = "java")

    configure(allprojects - project(":apollo-common")) { //or ':Android:AndroidApps' not sure
        apply(plugin = "application")
        apply(plugin = "io.spring.dependency-management")
        apply(plugin = "org.springframework.boot")
        apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    }

    // dependencies.gradle.kts
    val implementation by configurations
    val testImplementation by configurations


    dependencies {
        testImplementation(kotlin("test-junit"))
        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
//        implementation("ch.qos.logback:logback    -classic")
        implementation("commons-codec:commons-codec:1.13")
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
        implementation("ch.qos.logback:logback-classic:1.2.3")
        implementation("io.github.microutils:kotlin-logging:1.7.9")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.2")


        implementation("com.fasterxml.jackson.core:jackson-core:2.12.1")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.12.1")
        implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.1")
        implementation ("com.beust:klaxon:5.5")

        implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.1")

        implementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
        implementation("org.apache.commons:commons-text:1.8")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.2")


        implementation("io.netty:netty-handler:4.1.56.Final")

        /**
         * Ktor
         */
        implementation("io.ktor:ktor-client-core-jvm:1.4.0")
        implementation("io.ktor:ktor-client-logging-jvm:1.4.0")
        implementation("io.ktor:ktor-client-apache:1.4.0")
        implementation("io.ktor:ktor-client-gson:1.4.0")
        implementation("io.ktor:ktor-client-auth-jvm:1.4.0")
        implementation("io.ktor:ktor-jackson:1.4.0")

        /**
         * GSON
         */
        implementation("com.google.code.gson:gson:2.8.6")
        /**
         * Wood stock
         */
        implementation("com.fasterxml.woodstox:woodstox-core:6.2.3")
        /**
         * Testing Akka
         */
        implementation("com.typesafe.akka:akka-actor_2.13:2.6.10")
        implementation("com.typesafe.akka:akka-testkit_2.13:2.6.10")

//        implementation("org.scala-lang.modules:scala-java8-compat_2.13:0.9.1")
        implementation("com.typesafe:config:1.4.1")
//        implementation("com.typesafe.akka:akka-actor-typed_2.13:2.6.10")

        implementation("com.google.guava:guava:30.1-jre")

        /*
        Jsch Sftp Lib
        */
        implementation("com.jcraft:jsch:0.1.55")

        /*
        Apache commons IO Lib
        */
        implementation("commons-io:commons-io:2.8.0")
        /**
         * QR Code
         */
        implementation("com.google.zxing:core:3.4.1")
        implementation("com.google.zxing:javase:3.4.1")
    }

    tasks.withType<KotlinCompile> {
//        kotlinOptions.useIR = true
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
    tasks.withType<Test> {
//        org.jetbrains.kotlin.config.JvmTarget.JVM_11
        useJUnitPlatform()
    }


}
