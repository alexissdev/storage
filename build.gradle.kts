plugins {
    java
    `maven-publish`
}

subprojects {
    apply(plugin="java-library")

    repositories {
        mavenLocal()
        mavenCentral()
    }
}