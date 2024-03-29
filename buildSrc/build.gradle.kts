//
// this is the build for buildSrc
//
// see: https://docs.gradle.org/current/userguide/custom_plugins.html
//      https://antimonit.github.io/2020/12/06/to-buildsrc-and-back.html
//      https://github.com/gradle/kotlin-dsl-samples/tree/master/samples/buildSrc-plugin

// plugins for the build src
plugins {
    // https://plugins.gradle.org/plugin/org.gradle.kotlin.kotlin-dsl
    val kotlinDslPluginVersion = "4.2.1"
    id("org.gradle.kotlin.kotlin-dsl") version kotlinDslPluginVersion  // `kotlin-dsl`
    id("org.gradle.kotlin.kotlin-dsl.base") version kotlinDslPluginVersion
    id("org.gradle.kotlin.kotlin-dsl.compiler-settings") version kotlinDslPluginVersion
    id("org.gradle.kotlin.kotlin-dsl.precompiled-script-plugins") version kotlinDslPluginVersion
}

// this is the central point for managing plugin versions
// it also contains all the base plugins we aggregate into out custom plugins
dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-gradle-plugin
    val jetbrainsKotlinVersion = "1.9.21"

    // we need to declare dependencies for all the used plugins in the buildScripts...

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${jetbrainsKotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${jetbrainsKotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-noarg:${jetbrainsKotlinVersion}") // for JPA entities

    // https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.2.0")
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    implementation("io.spring.gradle:dependency-management-plugin:1.1.4")
    // implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.6.21-1.0.6")
    // webjar and node plugin code for angular modules

    // https://plugins.gradle.org/plugin/com.coditory.webjar
    implementation("com.coditory.gradle:webjar-plugin:1.3.1")
    // https://plugins.gradle.org/plugin/com.github.node-gradle.node
    implementation("com.github.node-gradle:gradle-node-plugin:7.0.1")
    // https://plugins.gradle.org/plugin/com.google.cloud.tools.jib
    // https://plugins.gradle.org/m2/com/google/cloud/tools/jib/com.google.cloud.tools.jib.gradle.plugin
    implementation("com.google.cloud.tools.jib:com.google.cloud.tools.jib.gradle.plugin:3.4.0")  // 3.3.2 has a buggy dependency (snakeyaml)
    // https://sylhare.github.io/2021/07/19/Openapi-swagger-codegen-with-kotlin.html
    // https://plugins.gradle.org/plugin/org.openapi.generator
    implementation("org.openapitools:openapi-generator-gradle-plugin:7.1.0")
    // org/openapi/generator/org.openapi.generator.gradle.plugin/
    // https://repo.maven.apache.org/maven2/org/openapitools/openapi-generator-gradle-plugin/
    implementation("org.openapi.generator:org.openapi.generator.gradle.plugin:7.1.0")
    // https://mvnrepository.com/artifact/com.palantir.gradle.gitversion/gradle-git-version
    // https://github.com/palantir/gradle-git-version/tags
    implementation("com.palantir.gradle.gitversion:gradle-git-version:3.0.0")
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://repo.spring.io/release") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}
