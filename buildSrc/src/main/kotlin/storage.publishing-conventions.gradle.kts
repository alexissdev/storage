plugins {
    id("storage.common-conventions")
    `maven-publish`
}

val snapshotRepository: String by project
val releaseRepository: String by project

publishing {
    repositories {
        maven {
            url = if (project.version.toString().endsWith("-SNAPSHOT")) {
                uri(snapshotRepository)
            } else {
                uri(releaseRepository)
            }

            credentials {
                val userKey = "USER"
                val pwdKey = "PASSWORD"
                username = project.properties["LIB_DEVELOP_$userKey"] as String?
                    ?: System.getenv("CI_$userKey")
                password = project.properties["LIB_DEVELOP_$pwdKey"] as String?
                    ?: System.getenv("CI_$pwdKey")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "${rootProject.name}-${project.name}"
            from(components["java"])
        }
    }
}