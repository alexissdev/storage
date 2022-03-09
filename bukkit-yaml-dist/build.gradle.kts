plugins {
    id("storage.publishing-conventions")
    id("storage.spigot-conventions")
}

dependencies {
    api(project(":api"))
    compileOnly(libs.spigot)
}