plugins {
    base
    id("charon.mrproper") // to remove the build dir in the project home
}

//
//  some help to get stated
//
tasks.register<DefaultTask>("info") {
    println("-- some commandlines for this project --")
    println("   * ./gradlew build: create the artifacts")
    println("   * ./gradlew mrproper: restores the status at checkout, removing any cached or built artifacts")
}
