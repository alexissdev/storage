plugins {
    java
    `maven-publish`
}

subprojects {
    apply(plugin="java-library")
    apply(plugin="maven-publish")

    tasks {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(8))
            }
        }

        compileJava {
            options.compilerArgs.add("-parameters")
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "storage-${project.name}"
                from(components["java"])
            }
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }
}