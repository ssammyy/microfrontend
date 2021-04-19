//pluginManagement {
//    repositories {
//        maven {
//            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
//        }
//        mavenCentral()
//
//        maven("https://dl.bintray.com/kotlin/kotlin-eap")
//
//        maven("https://plugins.gradle.org/m2/")
//    }
//}

rootProject.name = "apollo"

include("apollo-common")
include("apollo-config")
include("apollo-store")
include("apollo-kafka-producer")
include("apollo-api")
include("apollo-api-flux")
include("apollo-ipc")
include("Apollo-api-std")
include("Apollo-api-std")
