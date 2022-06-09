// customized plugin
// see: https://docs.gradle.org/current/userguide/custom_plugins.html#sec:precompiled_plugins

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm") // org.jetbrains.kotlin:kotlin-gradle-plugin
    kotlin("plugin.spring") // org.jetbrains.kotlin:kotlin-allopen
}

// repos for dependencies of the module in which this plugin is used
repositories {
    google()
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.spring.io/release") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}
