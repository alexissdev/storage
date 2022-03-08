import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get

plugins {
    id("storage.common-conventions")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "storage-${project.name}"
            from(components["java"])
        }
    }
}