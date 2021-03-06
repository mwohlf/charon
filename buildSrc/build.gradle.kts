// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//
// this is the build for buildSrc
//
// see: https://docs.gradle.org/current/userguide/custom_plugins.html
//      https://antimonit.github.io/2020/12/06/to-buildsrc-and-back.html
//      https://github.com/gradle/kotlin-dsl-samples/tree/master/samples/buildSrc-plugin

// plugins for the build src
plugins {
    // val kotlinPluginVersion = "2.1.7"
    val kotlinDslPluginVersion = "2.3.3"
    // id("org.jetbrains.kotlin.jvm") version "1.7.0"
    // `kotlin-dsl`
    id("org.gradle.kotlin.kotlin-dsl") version kotlinDslPluginVersion  // `kotlin-dsl`
    id("org.gradle.kotlin.kotlin-dsl.base") version kotlinDslPluginVersion
    id("org.gradle.kotlin.kotlin-dsl.compiler-settings") version kotlinDslPluginVersion
    id("org.gradle.kotlin.kotlin-dsl.precompiled-script-plugins") version kotlinDslPluginVersion
}

// this is the central point for managing plugin versions
// it also contains all the base plugins we aggregate into out custom plugins
dependencies {
    val jetbrainKotlinVersion = "1.6.21"

    // we need to declare dependencies for all the used plugins in the buildScripts...

    // https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.2")
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    implementation("io.spring.gradle:dependency-management-plugin:1.0.12.RELEASE")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${jetbrainKotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${jetbrainKotlinVersion}")
    // implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.6.21-1.0.6")
    // webjar and node plugin code for angular modules

    // https://plugins.gradle.org/plugin/com.coditory.webjar
    implementation("com.coditory.gradle:webjar-plugin:1.3.0")
    // https://plugins.gradle.org/plugin/com.github.node-gradle.node
    implementation("com.github.node-gradle:gradle-node-plugin:3.3.0")
    // https://plugins.gradle.org/plugin/com.google.cloud.tools.jib
    // https://plugins.gradle.org/m2/com/google/cloud/tools/jib/com.google.cloud.tools.jib.gradle.plugin/3.2.1/
    implementation("com.google.cloud.tools.jib:com.google.cloud.tools.jib.gradle.plugin:3.2.1")
    // see: https://sylhare.github.io/2021/07/19/Openapi-swagger-codegen-with-kotlin.html
    implementation("org.openapitools:openapi-generator-gradle-plugin:6.0.0")
    // org/openapi/generator/org.openapi.generator.gradle.plugin/
    implementation("org.openapi.generator:org.openapi.generator.gradle.plugin:6.0.0")
    // https://mvnrepository.com/artifact/com.palantir.gradle.gitversion/gradle-git-version
    implementation("com.palantir.gradle.gitversion:gradle-git-version:0.15.0")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
}


