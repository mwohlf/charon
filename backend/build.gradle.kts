description = "Charon Portal - Backend"

plugins {
    id("charon.mrproper")
	id("charon.kotlin-spring")  // this includes jib, spring-boot, kotlin, ...
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.1")
    implementation("io.swagger.core.v3:swagger-models:2.2.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // pickup the webjar
    implementation(project(":frontend"))
}

// TODO: check if we can pull this up into buildSrc
java.sourceSets["main"].java.srcDir("$buildDir/generated/source/openApi/kotlin")
