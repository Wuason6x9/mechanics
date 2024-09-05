import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.concurrent.Executors

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "1.7.1" apply false
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
    "1.21" to MCVersion("1.21", "1_21_R1", 21, 12)
)

val NMS_MAP = mutableMapOf<String, MCVersion>()
val DEF_VERSION = MC_VERSIONS["1.18.2"]!!

for ((_, mcVersion) in MC_VERSIONS) {
    NMS_MAP["NMS_${mcVersion.nmsVersion}"] = mcVersion
}

val LIBS = listOf(
    "dev.dejvokep:boosted-yaml:1.3.6",
    "org.apache.commons:commons-lang3:3.14.0",
    "de.tr7zw:item-nbt-api:2.13.1", //FOR REMOVING
    "org.apache.commons:commons-lang3:3.14.0",
    "org.apache-extras.beanshell:bsh:2.1.1",
    "com.github.oraxen:protectionlib:1.5.1",
    "dev.jorel:commandapi-bukkit-shade:9.5.0",
    "com.google.code.gson:gson:2.11.0",
    "com.jeff-media:MorePersistentDataTypes:2.4.0",
    "com.jeff-media:custom-block-data:2.2.2",

    "com.saicone.rtag:rtag:1.5.5",
    "com.saicone.rtag:rtag-block:1.5.5",
    "com.saicone.rtag:rtag-entity:1.5.5",
    "com.saicone.rtag:rtag-item:1.5.5"
)


allprojects {

    project.group = "dev.wuason"
    project.version = "1.0.1.13"

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
        maven("https://repo.oraxen.com/releases")
        maven("https://repo.oraxen.com/snapshots")
    }

    //nms projects
    if (project.name in NMS_MAP) {
        val vrs = NMS_MAP[project.name]!!
        project.extra.set("mcVersion", vrs)
        project.extra.set("apiVersion", vrs.getApiVersion())

        dependencies {
            compileOnly(project(":NMS:NMS_COMMON"))
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
    } else {

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

    println(project.path)

    //apply default version
    if (project.path in arrayOf(
            ":plugin:core",
            ":NMS:NMS_COMMON",
            ":NMS:WRAPPER",
            ":plugin:compatibilities:common",
            ":plugin:compatibilities"
        )
    ) {
        dependencies {
            compileOnly("io.papermc.paper:paper-api:${DEF_VERSION.getApiVersion()}")
        }
    }

    if (project.path in arrayOf(":plugin:compatibilities", ":plugin:compatibilities:common", ":plugin:core")) {
        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }

        dependencies {
            //libs
            for (lib in LIBS) {
                compileOnly(lib)
            }
        }
    }

    if (project.path in arrayOf(":plugin", ":lib")) {

        tasks.withType<ShadowJar> {
            destinationDirectory.set(file("$rootDir/target"))
            archiveBaseName.set("${rootProject.name}-${rootProject.version}" + if (project.name == "plugin") "" else "-lib")
            archiveClassifier.set("")
            archiveVersion.set("")
            manifest {
                attributes["Main-Class"] = "dev.wuason.mechanics.app.Main"
            }
            relocate("dev.dejvokep.boostedyaml", "dev.wuason.libs.boostedyaml")
            relocate("dev.jorel.commandapi", "dev.wuason.libs.commandapi")
            relocate("de.tr7zw.changeme.nbtapi", "dev.wuason.libs.nbtapi")
            relocate("io.th0rgal.protectionlib", "dev.wuason.libs.protectionlib")
            relocate("bsh", "dev.wuason.libs.bsh")
            relocate("org.apache.commons", "dev.wuason.libs.apache.commons")
            relocate("dev.wuason.mechanics.invmechanic", "dev.wuason.libs.invmechaniclib")
            relocate("com.google.gson", "dev.wuason.libs.google.gson")
            relocate("com.google.errorprone", "dev.wuason.libs.google.errorprone")
            relocate("com.jeff_media.morepersistentdatatypes", "dev.wuason.libs.jeffmedia.morepersistentdatatypes")
            relocate("com.jeff_media.customblockdata", "dev.wuason.libs.jeffmedia.customblockdata")
            relocate("com.saicone.rtag", "dev.wuason.libs.saicone.rtag")
        }
    }
}

project(":NMS:WRAPPER") {
    dependencies {
        //NMS COMMON
        implementation(project(":NMS:NMS_COMMON"))

        for ((nms, mc) in NMS_MAP) {
            if (mc.isLessThan(MC_VERSIONS["1.20.5"]!!)) {
                implementation(project(":NMS:$nms", configuration = "reobf"))
            } else {
                implementation(project(":NMS:$nms"))
            }
        }
    }
}

project(":lib") {
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

    dependencies {
        for (lib in LIBS) {
            implementation(lib)
        }
        implementation(project(":plugin"))
    }
}

project(":plugin") {
    dependencies {
        implementation(project(":App"))
        implementation(project(":plugin:core"))
        implementation(project(":plugin:compatibilities"))
    }
}

project(":plugin:core") {
    dependencies {

        //NMS
        implementation(project(":NMS:WRAPPER"))

        //PlaceholderAPI
        compileOnly("me.clip:placeholderapi:2.11.3")

        //WorldGuard
        compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.7")

        compileOnly(project(":NMS:NMS_COMMON"))

        //ASM
        compileOnly("org.ow2.asm:asm-commons:9.7")
        compileOnly("org.ow2.asm:asm:9.7")

        compileOnly(project(":plugin:compatibilities"))
        compileOnly(project(":plugin:compatibilities:common"))

        //libs
        for (lib in LIBS) {
            compileOnly(lib)
        }

        compileOnly("org.apache.httpcomponents:httpclient:4.5.14")

    }

}

// COMPATIBILITIES PROJECTS
project(":plugin:compatibilities:oraxen2") {
    dependencies {
        compileOnly(project(":plugin:compatibilities:common"))
        compileOnly(fileTree("libs") { include("*.jar") })
        compileOnly("io.papermc.paper:paper-api:${MC_VERSIONS["1.20.6"]!!.getApiVersion()}")
        compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT")
        compileOnly("io.lumine:Mythic-Dist:5.3.5")
        compileOnly("io.lumine:MythicCrucible:1.7.0-20230723.171823-33")
        compileOnly("io.th0rgal:oraxen:2.0-SNAPSHOT")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}

project(":plugin:compatibilities") {
    dependencies {

        implementation(project(":plugin:compatibilities:common"))
        implementation(project(":plugin:compatibilities:oraxen2"))

        compileOnly(project(":NMS:WRAPPER"))

        compileOnly(fileTree("libs") { include("*.jar") })

        //for storagemechanic TODO: SHEDULED FOR REMOVAL (THIS IS PROVISIONAL)
        compileOnly("com.github.Wuason6x9:mechanics:1.0.1.12a")

        //Oraxen
        compileOnly("com.github.oraxen:oraxen:1.159.0")

        //LoneDev
        compileOnly("com.github.LoneDev6:API-ItemsAdder:3.5.0b")

        //Mythic
        compileOnly("io.lumine:Mythic-Dist:5.3.5")
        compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT")

        //Phoneix
        compileOnly("net.Indyuce:MMOItems-API:6.9.4-SNAPSHOT")

    }
}


tasks.build {
    dependsOn(tasks.getByPath(":plugin:shadowJar"))
    dependsOn(tasks.getByPath(":lib:shadowJar"))
}