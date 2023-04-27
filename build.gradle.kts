plugins {
    base
    id("charon.mrproper") // to remove the build dir in the project home
}

//
//  some help to get started
//
tasks.register<DefaultTask>("info") {
    println(" *********************************************************** ")
    println("   some commandlines for this project --")
    println("    ./gradlew mrproper        reset the project to it's checkout state by removing all artifacts")
    println("    ./gradlew build           build all modules and prepare images for deployment")
    println(" *********************************************************** ")
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}
