description = "Charon Portal - Backend"

plugins {
    id("charon.mrproper")
	id("charon.kotlin-spring")  // this includes jib, spring-boot, kotlin, ...
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
}

// disable jit for now...
// tasks.findByName("jib")?.enabled = false

tasks.test {
    useJUnitPlatform()
}

dependencies {
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.6")
	implementation("io.swagger.core.v3:swagger-models:2.2.6")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // to create spring-configuration-metadata.json
    kapt("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // pickup the webjar
    implementation(project(":frontend"))
}

// TODO: check if we can pull this up into buildSrc
java.sourceSets["main"].java.srcDir("$buildDir/generated/source/openApi/kotlin")
