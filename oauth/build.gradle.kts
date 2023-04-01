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
    implementation("io.github.microutils:kotlin-logging-jvm:${Versions.KOTLIN_LOGGING_JVM}")
    implementation("io.swagger.core.v3:swagger-annotations:${Versions.SWAGGER}")
    implementation("io.swagger.core.v3:swagger-models:${Versions.SWAGGER}")
    implementation("jakarta.annotation:jakarta.annotation-api:${Versions.JAKARTA_ANNOTATION_API}")
    implementation("javax.annotation:javax.annotation-api:${Versions.JAVAX_ANNOTATION_API}")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf") // used for login page etc.
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    // @Suppress("GradlePackageUpdate")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server:${Versions.SPRING_OAUTH2_SERVER}")

    implementation("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // to create spring-configuration-metadata.json
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// Align versions of all Kotlin components
	// implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("junit:junit")
    testImplementation("net.sourceforge.htmlunit:htmlunit")

}
