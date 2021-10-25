/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootWar


plugins {
    war
}

description = "apollo-api"

dependencies {
    implementation(project(":apollo-store"))
    implementation(project(":apollo-kafka-producer"))
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-web")
    {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("org.springframework.boot:spring-boot-starter-test:2.2.4.RELEASE")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("org.hibernate:hibernate-search-orm:5.8.2.Final")
    implementation("org.hibernate.validator:hibernate-validator:6.1.5.Final")
    /**
     * BPMN BOM
     */
    implementation("org.flowable:flowable-spring-boot-starter-basic:6.6.0")

    /**
     * Swagger
     */
    implementation("io.springfox:springfox-boot-starter:3.0.0")

    /**
     * MPESA
     */
    implementation("org.json:json:20200518")
    implementation("com.squareup.okhttp3:okhttp:4.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.10.0")
    implementation("org.apache.poi:poi-ooxml:3.17")
    /**
     * Camel
     * Ref: https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.2/html/apache_camel_component_reference/idu-ftp2
     */
    implementation("org.apache.camel:camel-ftp:3.12.0")
    implementation("org.apache.camel:camel-bean:3.12.0")
    implementation("org.apache.camel:camel-direct:3.12.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.apache.camel.springboot:camel-spring-boot:3.12.0")
    implementation("org.apache.camel:camel-jacksonxml:3.12.0")
    testImplementation("org.apache.camel:camel-test-junit5:3.12.0")
    /**
     * Jasper reports
     */
    implementation("net.sf.jasperreports:jasperreports:6.13.0")
    implementation("com.lowagie:itext:2.1.7")
    /**
     * QR Code
     */
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.google.zxing:javase:3.4.1")

    /**
     * Mapper reports
     */
    implementation("net.sf.jasperreports:jasperreports:6.13.0")
    implementation("org.modelmapper:modelmapper:1.1.0")

    /**
     * SFTP
     */
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.integration:spring-integration-sftp:5.2.5.RELEASE")

    /**
     * Thymeleaf
     */
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.0.4.RELEASE")
    implementation("org.thymeleaf:thymeleaf-spring5:3.0.11.RELEASE")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.codehaus.jackson:jackson-mapper-asl:1.9.13")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.11.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.12.1")
    implementation("com.fasterxml.woodstox:woodstox-core:5.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    /**
     * Flying saucer pdf
     */
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.20")

    /**
     * Maven OGNL
     */
    implementation("ognl:ognl:3.2.15")

    /**
     * Joda Time
     */
    implementation("joda-time:joda-time:2.10.6")

    ///XNIO DEPENDENCY
//    implementation("org.jboss.xnio:xnio-nio:3.4.6.Final")

//    excel wrte,read dependencies
//    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.2.21")
//    implementation("org.apache.poi:poi-ooxml:3.17")

//    implementation("com.fasterxml.jackson.core:jackson-databind:2.7.4")
//    implementation( "com.fasterxml.jackson.core:jackson-core:2.7.4")
//    implementation("com.fasterxml.jackson.core:jackson-annotations:2.7.4")
//For Gradle, add to dependencies section of build.gradle
//    compile group: 'com.cloudinary', name: 'cloudinary-http44', version: '[Cloudinary API version, e.g. 1.1.3]'
//    implementation("com.cloudinary:cloudinary-http44:1.1.3")
    /**
     * Commons file upload
     */

    /**
     * KRA Integrations
     */
//    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("javax.xml.bind:jaxb-api:2.3.0")
    implementation("javax.activation:activation:1.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.0")
//    implementation("javax.xml.bind:jaxb-api:2.3.0-b170201.1204")
//    implementation("javax.activation:activation:1.1")
//    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.0-b170127.1453")
    implementation("org.springframework:spring-oxm:5.3.4")
    implementation("org.glassfish.metro:webservices-rt:2.4.4")
    implementation("org.glassfish.metro:webservices-api:2.4.4")
    implementation("com.sun.xml.messaging.saaj:saaj-impl")


    /**
     * kra Integrations end
     */
    implementation("commons-fileupload:commons-fileupload:1.4")
    /**
     * Starter mail
     */
    implementation("org.springframework.boot:spring-boot-starter-mail")
    /**
     * Spring Security
     */
    testImplementation("org.springframework.security:spring-security-test")

    /**
     * Google guava
     */
    //implementation("com.google.guava:11.0.2")

    implementation("net.sf.ehcache:ehcache-core:2.6.11")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    implementation(kotlin("stdlib-jdk8"))
}
application {
    mainClassName = "org.kebs.app.kotlin.apollo.api.ApiApplicationKt"
    application.applicationName = "apollo-api"
}

tasks.withType<BootWar>().configureEach {
    launchScript()
}

repositories {
//    maven("https://dl.bintray.com/kotlin/kotlin-eap")
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
