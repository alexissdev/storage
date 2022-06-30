rootProject.name = "storage"

arrayOf(
    "api", "api-codec", "mongo-dist", "mongo-legacy-dist",
    "sql-dist", "redis-dist",
    "gson-dist", "bukkit-yaml-dist"
).forEach {
    includePrefixed(it)
}

fun includePrefixed(name: String) {
    val kebabName = name.replace(':', '-')
    val path = name.replace(':', '/')
    val baseName = "${rootProject.name}-$kebabName"

    include(baseName)
    project(":$baseName").projectDir = file(path)
}