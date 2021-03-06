description = "Charon Portal - OAuth"

plugins {
    id("charon.mrproper")
	id("charon.kotlin-spring")
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
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.1")
    implementation("io.swagger.core.v3:swagger-models:2.2.1")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.security:spring-security-oauth2-authorization-server:${Versions.SPRING_OAUTH2_SERVER}")

    runtimeOnly("com.h2database:h2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // to create spring-configuration-metadata.json
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// Align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("junit:junit")
    testImplementation("net.sourceforge.htmlunit:htmlunit")

}
