import com.google.cloud.tools.jib.api.Jib
import java.util.UUID

//
// this is a meta plugin, combining some tasks from multiple plugins
//  source is configured in ../build.gradle.kts
//  configs are here
//
// see: https://docs.gradle.org/current/userguide/custom_plugins.html#sec:precompiled_plugins

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.cloud.tools.jib") // TODO: make this work with docker inside WSL2
    // id("org.openapitools")
    id("org.openapi.generator")
    id("com.palantir.git-version")
    kotlin("jvm") // org.jetbrains.kotlin:kotlin-gradle-plugin
    kotlin("plugin.spring") // org.jetbrains.kotlin:kotlin-allopen
    kotlin("kapt")
    // id("com.google.devtools.ksp")
    // see: https://sylhare.github.io/2021/07/19/Openapi-swagger-codegen-with-kotlin.html
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


springBoot {
    buildInfo()  // to create the buildInfo object
}

// val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra

//////////////////////
//
// configure the image creation for spring-boot modules
//  to run the service in a non-systemd setting: sudo service docker start
//
// see https://github.com/GoogleContainerTools/jib/blob/master/docs/faq.md for the Docker template
jib {
    from {
        image = "openjdk:17-alpine"
    }
    to {
        val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
        val details = versionDetails()

        //github registry:
        // val registry = "ghcr.io"
        // val repository = "mwohlf/${rootProject.name}-${project.name}"
        // image = "${registry}/${repository}"
        // tags = setOf(
        //    "latest",
        //    "${details.gitHash}",
        //    "${details.branchName}",
        //    // always unspecified:  "${project.version}",
        //)

        val registry = "ttl.sh"
        val repository = "mwohlf/${rootProject.name}-${project.name}"
        image = "${registry}/${repository}"
        tags = setOf("4h") // the ttl
    }

    /*
    val imagePrefix = "ttl.sh"
    // see: https://github.com/palantir/gradle-git-version
    val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
    val details = versionDetails()
    // details.lastTag
    // details.commitDistance
    // details.gitHash // 10 digit git hash
    // val uuid = UUID.randomUUID()
    val module = project.name
    val root = rootProject.name
    to {
        // this is the syntax that we need in the deployment description
        image = "${imagePrefix}/${root}-${module}-${details.gitHash}:1h"
        // image = "${imagePrefix}/${uuid}-${module}:1h"
        auth {
            username = ""
            password = ""
        }
    }
    */
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

//////////////////////
//
// configure the api generation for a spring-boot application
// lookup for the api definition is hardcoded to .../etc/api/<module-name>/api-docs.yml
// some more config at .../etc/api/api-config.json
openApiGenerate {
    val module = project.name
    // see: https://openapi-generator.tech/docs/generators
    generatorName.set("kotlin-spring")
    inputSpec.set("${rootProject.projectDir.absolutePath}/etc/api/${module}/api-docs.yml")
    outputDir.set("$buildDir/generated")
    // see: https://openapi-generator.tech/docs/generators/kotlin-spring
    configFile.set("${rootProject.projectDir.absolutePath}/etc/api/api-config.json")
}

// re-create the API classes before ebuilding
tasks.findByName("build").apply {
    this?.doFirst { tasks.findByName("openApiGenerate") }
}
