plugins {
    id("storage.publishing-conventions")
}

dependencies {
    api(project(":storage-api-codec"))
    api(libs.mongo.driver)
}