plugins {
    base
}


// gradlew build --scan



//
//  mrproper task for resetting the project
//
allprojects {

    tasks.register<Delete>("mrproper") {
        // show task dependencies: ./gradlew mrproper --dry-run
        // dependsOn("clean") // this pulls in a lot of dependencies which we need to disable:
        // tasks.findByName("nodeSetup")?.enabled = false
        // tasks.findByName("npmSetup")?.enabled = false
        // tasks.findByName("webjarInstall")?.enabled = false
        // tasks.findByName("webjarClean")?.enabled = false

        // dependsOn("cleanBuildCache")
        description = """
            Cleanup anything that might be downloaded or created,
            trying to reset the project to the checkout state,
            this task is shared with every module
        """
        delete = setOf (
            ".node", ".angular",
            "build", "dist",
            "node_modules",
            "target", "yarn.lock",
            "guided-tests/js/guided-test-runtime.js", "yarn-error.log",
            "src/generated", "out", "build"
            // "package-lock.json"
        )
    }

}

//
//  some help to get stated
//
tasks.register<DefaultTask>("info") {
    println("-- some commandlines for this project --")
    println("   * ./gradlew build: create the artifacts")
    println("   * ./gradlew mrproper: restores the status at checkout, removing any cached or built artifacts")
}
