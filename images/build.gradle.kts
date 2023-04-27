// https://bmuschko.github.io/gradle-docker-plugin/current/user-guide/
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage

plugins {
    id("com.bmuschko.docker-remote-api").version("9.3.1")
    id("com.palantir.git-version") // already in the classpath
}

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
val details = versionDetails()

tasks.create("buildPostgres", DockerBuildImage::class) {
    inputDir.set(file("postgres"))
    dockerFile.set(file("postgres/Dockerfile"))

    val registry = "ttl.sh"
    val repository = "mwohlf/${rootProject.name}-postgres"

    images.set(setOf(
        "${registry}/${repository}:4h",  // the ttl
        "${registry}/${repository}:${details.gitHash}",
        "${registry}/${repository}:${details.gitHashFull}",
    ))
}

tasks.create("pushPostgres", DockerPushImage::class) {
    dependsOn("buildPostgres")

    val registry = "ttl.sh"
    val repository = "mwohlf/${rootProject.name}-postgres"

    images.set(setOf(
        "${registry}/${repository}:4h",  // the ttl
        "${registry}/${repository}:${details.gitHash}",
        "${registry}/${repository}:${details.gitHashFull}",
    ))
}

tasks.create("buildRedis", DockerBuildImage::class) {
    inputDir.set(file("redis"))
    dockerFile.set(file("redis/Dockerfile"))

    val registry = "ttl.sh"
    val repository = "mwohlf/${rootProject.name}-redis"

    images.set(setOf(
        "${registry}/${repository}:4h",  // the ttl
        "${registry}/${repository}:${details.gitHash}",
        "${registry}/${repository}:${details.gitHashFull}",
    ))
}

tasks.create("pushRedis", DockerPushImage::class) {
    dependsOn("buildRedis")

    val registry = "ttl.sh"
    val repository = "mwohlf/${rootProject.name}-redis"

    images.set(setOf(
        "${registry}/${repository}:4h",  // the ttl
        "${registry}/${repository}:${details.gitHash}",
        "${registry}/${repository}:${details.gitHashFull}",
    ))
}


tasks.create("backends", DefaultTask::class) {
    dependsOn("pushPostgres")
    dependsOn("pushRedis")
}
