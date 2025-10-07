rootProject.name = "mechanics"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

include(":plugin", ":lib")
include(":NMS:NMS_COMMON")
include(":NMS:WRAPPER")
include(
    ":NMS:NMS_1_18_R2",
    ":NMS:NMS_1_19_R1",
    ":NMS:NMS_1_19_R2",
    ":NMS:NMS_1_19_R3",
    ":NMS:NMS_1_20_R1",
    ":NMS:NMS_1_20_R2",
    ":NMS:NMS_1_20_R3",
    ":NMS:NMS_1_20_R4",
    ":NMS:NMS_1_21_R1",
    ":NMS:NMS_1_21_R2",
    ":NMS:NMS_1_21_R3",
    ":NMS:NMS_1_21_R4",
    ":NMS:NMS_1_21_R5",
    ":NMS:NMS_1_21_R6",
)
