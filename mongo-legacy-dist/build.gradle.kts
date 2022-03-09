plugins {
    id("storage.publishing-conventions")
}

dependencies {
    api(project(":api"))
    api(libs.mongo.driver)

    testImplementation(project(":mongo-legacy-dist"))
}