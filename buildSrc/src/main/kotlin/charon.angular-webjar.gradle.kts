plugins {
    java  // for the jar task, also includes the build and clean task
    id("com.coditory.webjar")
    id("com.github.node-gradle.node")
}

node {
    download.set(true)
    distBaseUrl.set("https://nodejs.org/dist")
    version.set(Versions.NODE)
    npmVersion.set(Versions.NPM)
}

// see: https://github.com/coditory/gradle-webjar-plugin/blob/master/README.md
webjar {
    cache {
        enabled = false
    }
}

tasks.npmSetup {
    // to override the config in ~/.npmrc
    args.addAll("--registry", "https://registry.npmjs.org")
}

tasks.findByName("webjarTest")?.enabled = false
tasks.findByName("webjarLint")?.enabled = false
tasks.findByName("webjarClean")?.enabled = false
tasks.findByName("compileJava")?.enabled = false

// attach the webjarBuild to the build task
tasks.findByName("build").apply {
    this?.doLast { tasks.findByName("webjarBuild") }
}
