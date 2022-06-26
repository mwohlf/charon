import java.util.*

// customized plugin
// see: https://docs.gradle.org/current/userguide/custom_plugins.html#sec:precompiled_plugins

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.cloud.tools.jib") // TODO: make this work with docker inside WSL2
    kotlin("jvm") // org.jetbrains.kotlin:kotlin-gradle-plugin
    kotlin("plugin.spring") // org.jetbrains.kotlin:kotlin-allopen
    kotlin("kapt")
    // id("com.google.devtools.ksp")
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

// see https://github.com/GoogleContainerTools/jib/blob/master/docs/faq.md for the Docker template
jib {
    val imagePrefix = "ttl.sh"
    val uuid = UUID.randomUUID()
    val module = project.name
    from.image = "openjdk:17-alpine"
    to {
        image = "${imagePrefix}/${uuid}-${module}:1h"
    }
    container {
        creationTime = "USE_CURRENT_TIMESTAMP"
        ports = listOf("8080")
        jvmFlags = listOf(
            "-Djava.security.egd=file:/dev/urandom",
            "-Duser.timezone=\"Europe/Berlin\"",
        )
        // allowInsecureRegistries = true
        // http://ttl.sh
        // $ IMAGE_NAME=$(uuidgen)
        // $ docker build -t ttl.sh/${IMAGE_NAME}:1h .
        // $ docker push ttl.sh/${IMAGE_NAME}:1h
    }
}
