import gradle.kotlin.dsl.accessors._fcffe14232c178f32c3a5a66a4becfb6.openApiGenerate

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


tasks.npmSetup {
    // to override the config in ~/.npmrc
    args.addAll("--registry", "https://registry.npmjs.org")
}

tasks.findByName("webjarTest")?.enabled = false
tasks.findByName("webjarLint")?.enabled = false
tasks.findByName("webjarClean")?.enabled = false
tasks.findByName("compileJava")?.enabled = false

// re-create the API classes before building the webjar
tasks.findByName("webjarBuild")?.let {
    it.dependsOn("openApiGenerate")
}

// attach the webjarBuild to the build task
tasks.findByName("build")?.let {
    it.doLast { tasks.findByName("webjarBuild") }
}
