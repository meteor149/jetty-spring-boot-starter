plugins {
    alias(libs.plugins.spring.boot).apply(false)
}

allprojects {
    group = "org.meteor.boot"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

