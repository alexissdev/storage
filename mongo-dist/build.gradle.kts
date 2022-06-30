plugins {
    id("storage.publishing-conventions")
}

dependencies {
    api(project(":storage-api"))
    api(libs.mongo.jack)
}