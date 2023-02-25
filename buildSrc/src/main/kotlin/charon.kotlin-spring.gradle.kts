import com.google.cloud.tools.jib.api.Jib
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import java.util.UUID
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

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
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/release") }
    google()
    mavenLocal()
    mavenCentral()
}


tasks.withType<KotlinCompilationTask<KotlinJvmCompilerOptions>>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        // allWarningsAsErrors.set(true)
    }
}

// see: https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/#integrating-with-actuator.build-info
springBoot {
    buildInfo()  // to create the buildInfo object to have the build data available at runtime
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
        tags = setOf(
            "4h",  // the ttl
            "${details.gitHash}",
            "${details.gitHashFull}",
        )
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
        // creationTime = project.provider { "USE_CURRENT_TIMESTAMP" }
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
    // just in case we need to patch the templates
    // templateDir.set("${rootProject.projectDir.absolutePath}/buildSrc/src/main/resources/openapi/kotlin-spring")
    inputSpec.set("${rootProject.projectDir.absolutePath}/etc/api/${module}.yaml")
    outputDir.set("$buildDir/generated")
    // see: https://openapi-generator.tech/docs/generators/kotlin-spring
    configFile.set("${rootProject.projectDir.absolutePath}/etc/api/config/${generatorName.get()}.json")
    configOptions.put("useSpringBoot3", "true")
    // templates are in modules/openapi-generator/src/main/resources/kotlin-spring/api.mustache
    // whole template dir at
    // https://github.com/jayandran-Sampath/openapi-generator/tree/97818d8279c1b94f3961b8b6af01518000cb4656/modules/openapi-generator/src/main/resources/kotlin-spring
    // openapi-generator/modules/openapi-generator/src/main/resources/kotlin-spring/
    // https://github.com/jayandran-Sampath/openapi-generator/tree/feat13578_1
}

val openApiGenerate = tasks.getByPath("openApiGenerate")

val compileKotlin = tasks.getByPath("compileKotlin")
compileKotlin.dependsOn(openApiGenerate)

// tasks.getByPath("build").let {
//    it.dependsOn(openApiGenerate)
//}

// need api for kapt stubs
// val kaptGenerateStubsKotlin = tasks.withType<org.jetbrains.kotlin.gradle.internal..KaptGenerateStubsKotlin>().


// re-create the API classes before ebuilding


