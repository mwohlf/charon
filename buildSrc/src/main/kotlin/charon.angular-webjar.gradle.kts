import com.github.gradle.node.npm.task.NpmTask

plugins {
    // java  // for the jar task, also includes the build and clean task
    id("com.coditory.webjar")
    id("org.openapi.generator")
    id("com.github.node-gradle.node")
}

node {
    download.set(true)
    distBaseUrl.set("https://nodejs.org/dist")
    version.set(Versions.NODE)
    npmVersion.set(Versions.NPM)
}

webjar {
// see: https://github.com/coditory/gradle-webjar-plugin/blob/master/README.md
//    cache {
//        enabled = false
//    }
}

//////////////////////
//
// configure the api generation for a spring-boot application
// lookup for the api definition is hardcoded to .../etc/api/<module-name>/api-docs.yml
// some more config at .../etc/api/api-config.json
openApiGenerate {
    val module = project.name
    // see: https://openapi-generator.tech/docs/generators
    generatorName.set("typescript-angular")
    inputSpec.set("${rootProject.projectDir.absolutePath}/etc/api/${module}.yaml")
    outputDir.set("$buildDir/generated")
    // see: https://openapi-generator.tech/docs/generators/typescript-angular
    configFile.set("${rootProject.projectDir.absolutePath}/etc/api/config/${generatorName.get()}.json")
}


// update the version in package.json:
// npm version 'your version'

tasks.npmSetup {
    // to override the config in ~/.npmrc
    args.addAll("--registry", "https://registry.npmjs.org")
}


tasks.getByName("webjarTest").enabled = false
tasks.getByName("webjarLint").enabled = false
tasks.getByName("webjarClean").enabled = false
tasks.getByName("compileJava").enabled = false

tasks.register<NpmTask>("syncVersion") {
    args.set(listOf("version", this.project.version.toString(), "--silent"))
    this.ignoreExitValue.set(true)
}
// re-create the API classes before building the webjar
tasks.getByName("openApiGenerate").dependsOn("syncVersion")

// re-create the API classes before building the webjar
tasks.getByName("webjarBuild").dependsOn("openApiGenerate")

// attach the webjarBuild to the build task, build is from the java plugin
//tasks.register<DefaultTask>("build").let {
//    //it.dependsOn("webjarBuild")
//}

// attach the webjarBuild to the build task, clean is from the java plugin
// tasks.register<DefaultTask>("clean")?.let {
//    //it.dependsOn("webjarClean")
//    // it.destroyables.register("$buildDir/node/")
//}

val delete = tasks.register<Delete>("delete") {
    delete = setOf(
        "build", "dist"
    )
}

tasks.getByName("clean").let {
    it.actions.clear()
    it.setDependsOn(listOf(delete))
}

// workaround for upstream dependency issues
// we need to add "--legacy-peer-deps"

tasks.findByName("webjarInstall")?.let {
    // val npmTask = it as NpmTask
    (it as NpmTask).args.set(listOf("install", "--legacy-peer-deps"))
}

tasks.findByName("webjarInit")?.let {
    // val npmTask = it as NpmTask
    (it as NpmTask).args.set(listOf("install", "--legacy-peer-deps"))
}
