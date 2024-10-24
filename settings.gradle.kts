rootProject.name = "Mechanics"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

include(
    ":App",
    ":plugin",
    ":plugin:adapter",
    ":plugin:adapter:common",
    ":plugin:adapter:oraxen2",
    ":plugin:core",
    ":nms:v1_18_R2",
    ":nms:v1_19_R1",
    ":nms:v1_19_R2",
    ":nms:v1_19_R3",
    ":nms:v1_20_R1",
    ":nms:v1_20_R2",
    ":nms:v1_20_R3",
    ":nms:v1_20_R4",
    ":nms:v1_21_R1"
)