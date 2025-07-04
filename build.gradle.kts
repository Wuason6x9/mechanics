import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.concurrent.Executors

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17" apply false
    id("org.gradle.maven-publish")
}

class MCVersion(val vsr: String, val nmsVersion: String, val javaVersion: Int, val order: Int = 0) {
    fun getApiVersion(): String {
        return "${vsr}-R0.1-SNAPSHOT"
    }

    fun isLessThan(mcVersion: MCVersion): Boolean {
        return order < mcVersion.order
    }

    fun isGreaterThan(mcVersion: MCVersion): Boolean {
        return order > mcVersion.order
    }

}

val MC_VERSIONS = mapOf(
    "1.18.2" to MCVersion("1.18.2", "1_18_R2", 17, 0),
    "1.19" to MCVersion("1.19", "1_19_R1", 17, 1),
    "1.19.1" to MCVersion("1.19.1", "1_19_R1", 17, 2),
    "1.19.2" to MCVersion("1.19.2", "1_19_R1", 17, 3),
    "1.19.3" to MCVersion("1.19.3", "1_19_R2", 17, 4),
    "1.19.4" to MCVersion("1.19.4", "1_19_R3", 17, 5),
    "1.20" to MCVersion("1.20", "1_20_R1", 17, 6),
    "1.20.1" to MCVersion("1.20.1", "1_20_R1", 17, 7),
    "1.20.2" to MCVersion("1.20.2", "1_20_R2", 17, 8),
    "1.20.4" to MCVersion("1.20.4", "1_20_R3", 17, 9),
    "1.20.5" to MCVersion("1.20.5", "1_20_R4", 21, 10),
    "1.20.6" to MCVersion("1.20.6", "1_20_R4", 21, 11),
    "1.21" to MCVersion("1.21", "1_21_R1", 21, 12),
    "1.21.1" to MCVersion("1.21.1", "1_21_R1", 21, 12),
    //"1.21.2" to MCVersion("1.21.2", "1_21_R1", 21, 13),
    "1.21.3" to MCVersion("1.21.3", "1_21_R2", 21, 14),
    "1.21.4" to MCVersion("1.21.4", "1_21_R3", 21, 15),
    "1.21.5" to MCVersion("1.21.5", "1_21_R4", 21, 16),
)

val NMS_MAP = mutableMapOf<String, MCVersion>()
val DEF_VERSION = MC_VERSIONS["1.18.2"]!!

for ((_, mcVersion) in MC_VERSIONS) {
    NMS_MAP["NMS_${mcVersion.nmsVersion}"] = mcVersion
}

val LIBS = listOf(
    "dev.dejvokep:boosted-yaml:1.3.6",
    "org.apache.commons:commons-lang3:3.14.0",
    "de.tr7zw:item-nbt-api:2.15.0",
    "org.apache-extras.beanshell:bsh:2.1.1",
    "io.th0rgal:protectionlib:1.5.1",
    "dev.jorel:commandapi-bukkit-shade:10.1.1",
    "com.google.code.gson:gson:2.11.0",
    "com.jeff-media:MorePersistentDataTypes:2.4.0",
    "com.jeff-media:custom-block-data:2.2.2",
    "com.github.Wuason6x9:Adapter:1.0.5",

    "net.momirealms:craft-engine-bukkit:0.0.59",
    "net.momirealms:craft-engine-core:0.0.59"
)


allprojects {

    project.group = "dev.wuason"
    project.version = "1.0.3.7"

    apply(plugin = "java")
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "io.github.goooler.shadow")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
        maven("https://mvn.lumine.io/repository/maven-public/")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://jitpack.io")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://maven.enginehub.org/repo/")
        maven("https://nexus.frengor.com/repository/public/")
        maven("https://invesdwin.de/repo/invesdwin-oss/") //bsh
        maven("https://repo.nexomc.com/releases")
        maven("https://repo.oraxen.com/releases")

        maven("https://repo.momirealms.net/releases/")
    }

    if (project.name in listOf("plugin", "lib")) {
        apply(plugin = "io.github.goooler.shadow")

        tasks.withType<ShadowJar> {
            destinationDirectory.set(file("$rootDir/target"))
            archiveBaseName.set("${rootProject.name}-${rootProject.version}" + if (project.name == "plugin") "" else "-lib")
            archiveClassifier.set("")
            archiveVersion.set("")
            relocate("dev.dejvokep.boostedyaml", "dev.wuason.libs.boostedyaml")
            relocate("dev.jorel.commandapi", "dev.wuason.libs.commandapi")
            relocate("de.tr7zw.changeme.nbtapi","dev.wuason.libs.nbtapi")
            relocate("io.th0rgal.protectionlib", "dev.wuason.libs.protectionlib")
            relocate("bsh", "dev.wuason.libs.bsh")
            relocate("org.apache.commons", "dev.wuason.libs.apache.commons")
            relocate("dev.wuason.mechanics.invmechanic", "dev.wuason.libs.invmechaniclib")
            relocate("com.google.gson", "dev.wuason.libs.google.gson")
            relocate("com.google.errorprone", "dev.wuason.libs.google.errorprone")
            relocate("com.jeff_media.morepersistentdatatypes", "dev.wuason.libs.jeffmedia.morepersistentdatatypes")
            relocate("com.jeff_media.customblockdata", "dev.wuason.libs.jeffmedia.customblockdata")
            relocate("dev.wuason.adapter", "dev.wuason.libs.adapter")
        }
    }

    if (project.name == "lib") {
        dependencies {
            for (lib in LIBS) {
                implementation(lib)
            }
            implementation(project(":plugin"))
        }
    }

    //plugin module
    if(project.name == "plugin") {
        apply(plugin = "io.github.goooler.shadow")
        dependencies {
            //NMS
            implementation(project(":NMS:WRAPPER"))
            //PlaceholderAPI
            compileOnly("me.clip:placeholderapi:2.11.6")
            //WorldGuard
            compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.7")
            compileOnly(fileTree("libs") { include("*.jar") })
            implementation(fileTree("libs-impl") { include("*.jar") })
            compileOnly(project(":NMS:NMS_COMMON"))
            //ASM
            compileOnly("org.ow2.asm:asm-commons:9.7")
            compileOnly("org.ow2.asm:asm:9.7")
            //libs
            for (lib in LIBS) {
                compileOnly(lib)
            }

            compileOnly("org.apache.httpcomponents:httpclient:4.5.14")

        }
    }

    //apply default version
    if (project.name in listOf("plugin", "NMS_COMMON", "WRAPPER")) {
        dependencies {
            compileOnly("io.papermc.paper:paper-api:${DEF_VERSION.getApiVersion()}")
        }
    }


    //wrapper project
    if (project.name == "WRAPPER") {

        dependencies {
            //NMS COMMON
            implementation(project(":NMS:NMS_COMMON"))

            for (nms in NMS_MAP.keys) {
                implementation(project(":NMS:$nms", configuration = "reobf"))
            }
        }

        tasks {
            withType<ShadowJar> {
                dependsOn(":NMS:NMS_COMMON:shadowJar")
            }
        }
    }

    //nms projects
    if (project.name in NMS_MAP) {
        val vrs = NMS_MAP[project.name]!!
        project.extra.set("mcVersion", vrs)
        project.extra.set("apiVersion", vrs.getApiVersion())
        project.group = "dev.wuason.nms.${vrs.nmsVersion}"
        project.version = rootProject.version

        dependencies {
            compileOnly(project(":NMS:NMS_COMMON"))
        }

        tasks {
            jar {
                dependsOn(named("reobfJar"))
            }
        }

    }


    //prevent errors
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"

    }

    //replace text in all plugin.yml
    tasks.withType<ProcessResources> {
        val vars = mapOf("version" to rootProject.version, "name" to rootProject.name)
        inputs.properties(vars)
        filesMatching("**/plugin.yml") {
            expand(vars)
        }
    }

    //select java version
    if (project.extra.has("mcVersion")) {
        val mcVersion: MCVersion = project.extra.get("mcVersion") as MCVersion
        val jvmVer: JavaVersion = JavaVersion.valueOf("VERSION_${mcVersion.javaVersion}")

        java {
            sourceCompatibility = jvmVer
            targetCompatibility = jvmVer
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(mcVersion.javaVersion))
            }
        }
    }
    else {

        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

}

subprojects {

    tasks.shadowJar {
        archiveClassifier.set("")
    }

    if (project.name == "lib") {
        publishing {
            publications {
                create<MavenPublication>("mavenJava") {
                    groupId = rootProject.group.toString()
                    artifactId = rootProject.name
                    version = rootProject.version.toString()
                    artifact(tasks.shadowJar)
                }
            }
        }
    }
}


tasks.build {
    dependsOn(tasks.getByPath(":plugin:shadowJar"))
    dependsOn(tasks.getByPath(":lib:shadowJar"))
}