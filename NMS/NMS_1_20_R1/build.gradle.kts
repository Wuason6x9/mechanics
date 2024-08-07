plugins {
    id("io.papermc.paperweight.userdev")
}
dependencies {
    paperweight.paperDevBundle(project.extra.get("apiVersion") as String)
}