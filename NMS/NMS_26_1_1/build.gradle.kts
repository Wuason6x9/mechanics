plugins {
    id("io.papermc.paperweight.userdev")
}
dependencies {
    paperweight.paperDevBundle((project.extra.get("apiVersion") as String).replace("-R0.1-SNAPSHOT", ".build.+"))
}
