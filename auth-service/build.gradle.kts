// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("charon.kotlin-spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("org.springframework.security:spring-security-oauth2-authorization-server:${Versions.SPRING_OAUTH2_SERVER}")

    runtimeOnly("com.h2database:h2")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// Align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
}
