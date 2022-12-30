import com.github.gradle.node.npm.task.NpmTask

plugins {
    java  // for the jar task, also includes the build and clean task
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


tasks.findByName("webjarTest")?.enabled = false
tasks.findByName("webjarLint")?.enabled = false
tasks.findByName("webjarClean")?.enabled = false
tasks.findByName("compileJava")?.enabled = false

tasks.register<NpmTask>("syncVersion") {
    args.set(listOf("version", this.project.version.toString(), "--silent"))
    this.ignoreExitValue.set(true);
}
// re-create the API classes before building the webjar
tasks.findByName("openApiGenerate")?.let {
    it.dependsOn("syncVersion")
}

// re-create the API classes before building the webjar
tasks.findByName("webjarBuild")?.let {
    it.dependsOn("openApiGenerate")
}

// attach the webjarBuild to the build task
tasks.findByName("build")?.let {
    it.dependsOn("webjarBuild")
}


// ngrx still depends on angularCore 14
// we need to add "--legacy-peer-deps"

tasks.findByName("webjarInstall")?.let {
    // val npmTask = it as NpmTask
    (it as NpmTask).args.set(listOf("install", "--legacy-peer-deps"))
}

tasks.findByName("webjarInit")?.let {
    // val npmTask = it as NpmTask
    (it as NpmTask).args.set(listOf("install", "--legacy-peer-deps"))
}
