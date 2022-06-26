// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.cloud.tools.jib.gradle.JibExtension
import java.util.UUID

plugins {
    id("charon.mrproper")
	id("charon.kotlin-spring")  // this includes jib, spring-boot, kotlin, ...
    // see: https://sylhare.github.io/2021/07/19/Openapi-swagger-codegen-with-kotlin.html
    id("org.openapi.generator") version "6.0.0"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// Align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // pickup the webjar
    implementation(project(":frontend"))
}

openApiGenerate {
    val uuid: UUID = UUID.randomUUID()

    // see: https://openapi-generator.tech/docs/generators
    generatorName.set("kotlin-spring")
    inputSpec.set("${rootProject.projectDir.absolutePath}/etc/api/backend/api-docs.yml")
    outputDir.set("$buildDir/generated")
    // see: https://openapi-generator.tech/docs/generators/kotlin-spring
    configFile.set("${rootProject.projectDir.absolutePath}/etc/api/backend/api-config.json")
}

java.sourceSets["main"].java.srcDir("$buildDir/generated/source/openApi/kotlin")
