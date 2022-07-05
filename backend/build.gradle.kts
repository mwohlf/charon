// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.cloud.tools.jib.gradle.JibExtension
import java.util.UUID

plugins {
    id("charon.mrproper")
	id("charon.kotlin-spring")  // this includes jib, spring-boot, kotlin, ...
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web")
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
