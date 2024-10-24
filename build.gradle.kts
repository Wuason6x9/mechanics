import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import kotlin.text.set

plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "1.7.1" apply false
    id("org.gradle.maven-publish")
}

val libs = listOf(
    "dev.dejvokep:boosted-yaml:1.3.6",
    "org.apache.commons:commons-lang3:3.14.0",
    "de.tr7zw:item-nbt-api:2.13.1", //FOR REMOVAL
    "org.apache.commons:commons-lang3:3.14.0",
    "org.apache-extras.beanshell:bsh:2.1.1",
    "com.github.oraxen:protectionlib:1.5.1",
    "dev.jorel:commandapi-bukkit-shade:9.5.3",
    "com.google.code.gson:gson:2.11.0",
    "com.jeff-media:MorePersistentDataTypes:2.4.0",
    "com.jeff-media:custom-block-data:2.2.2",

    "com.saicone.rtag:rtag:1.5.5",
    "com.saicone.rtag:rtag-block:1.5.5",
    "com.saicone.rtag:rtag-entity:1.5.5",
    "com.saicone.rtag:rtag-item:1.5.5",
)

val relocateMap = mapOf(
    "dev.dejvokep.boostedyaml" to "dev.wuason.libs.boostedyaml",
    "dev.jorel.commandapi" to "dev.wuason.libs.commandapi",
    "de.tr7zw.changeme.nbtapi" to "dev.wuason.libs.nbtapi",
    "io.th0rgal.protectionlib" to "dev.wuason.libs.protectionlib",
    "bsh" to "dev.wuason.libs.bsh",
    "org.apache.commons" to "dev.wuason.libs.apache.commons",
    "dev.wuason.mechanics.invmechanic" to "dev.wuason.libs.invmechaniclib",
    "com.google.gson" to "dev.wuason.libs.google.gson",
    "com.google.errorprone" to "dev.wuason.libs.google.errorprone",
    "com.jeff_media.morepersistentdatatypes" to "dev.wuason.libs.jeffmedia.morepersistentdatatypes",
    "com.jeff_media.customblockdata" to "dev.wuason.libs.jeffmedia.customblockdata",
    "com.saicone.rtag" to "dev.wuason.libs.saicone.rtag",
)

allprojects {

    project.group = "dev.wuason"
    project.version = "1.0.2"

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

    dependencies {
        implementation("org.jetbrains:annotations:26.0.1")
    }
}

subprojects {

    tasks.shadowJar {
        archiveClassifier.set("")
    }

    //apply default version
    if (project.path in arrayOf(
            ":plugin:core",
            ":plugin:adapter:common",
            ":plugin:adapter"
        )
    ) {
        dependencies {
            compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
        }
    }

    if (project.path in arrayOf(":plugin:adapter", ":plugin:adapter:common", ":plugin:item_builder")) {
        java {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    if (project.path in arrayOf(":plugin:adapter", ":plugin:adapter:common", ":plugin:core", ":plugin:item_builder")) {

        dependencies {
            //libs
            for (lib in libs) {
                implementation(lib)
            }
        }
    }

    if (":nms:v" in project.path) {
        dependencies {
            compileOnly(project(":plugin:core"))
        }
    }

}

project(":plugin") {
    dependencies {
        implementation(project(":plugin:adapter"))
        implementation(project(":plugin:core"))
        rootProject.subprojects.filter { it.path.contains(":nms:v") }.forEach {
            implementation(project(it.path, configuration = "reobf"))
        }
    }
}

project(":plugin:core") {
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
    dependencies {

        compileOnly("me.clip:placeholderapi:2.11.6")//PlaceholderAPI

        compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.7")//WorldGuard

        compileOnly("org.ow2.asm:asm-commons:9.7")//ASM
        compileOnly("org.ow2.asm:asm:9.7")

        compileOnly(project(":plugin:adapter"))
        compileOnly(project(":plugin:adapter:common"))

        compileOnly("org.apache.httpcomponents:httpclient:4.5.14")

    }

}

// adapter PROJECTS
project(":plugin:adapter:oraxen2") {
    dependencies {
        compileOnly(project(":plugin:adapter:common"))
        compileOnly(fileTree("libs") { include("*.jar") })
        compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
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

project(":plugin:adapter") {
    dependencies {

        implementation(project(":plugin:adapter:common"))
        implementation(project(":plugin:adapter:oraxen2"))

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

dependencies {
    implementation(project(":App"))
    implementation(project(":plugin"))
}

val libJar = tasks.register<ShadowJar>("libJar") {
    dependsOn(tasks.shadowJar)
    description = "Creates a shadowed jar with all dependencies"
    group = "build"
    configurations = listOf(project.configurations["runtimeClasspath"])
    destinationDirectory.set(file("$rootDir/${rootProject.properties.get("targetDir")}"))
    archiveBaseName.set("${rootProject.name}-${rootProject.version}")
    archiveClassifier.set("lib")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = rootProject.properties.get("mainClassApp")
    }
    relocateMap.forEach { (from, to) ->
        relocate(from, to)
    }
}

tasks {
    shadowJar {
        destinationDirectory.set(file("$rootDir/${rootProject.properties.get("targetDir")}"))
        archiveBaseName.set("${rootProject.name}-${rootProject.version}")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes["Main-Class"] = rootProject.properties.get("mainClassApp")
        }
        relocateMap.forEach { (from, to) ->
            relocate(from, to)
        }
        project(":plugin:core").configurations.runtimeClasspath.get().resolvedConfiguration.firstLevelModuleDependencies.forEach { module ->
            if (module.name in libs) {
                module.allModuleArtifacts.forEach { artifact ->
                    exclude(artifact.file.name)
                }
            }
        }
    }
    build {
        dependsOn(libJar)
    }
}



publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            artifact(libJar)
        }
    }
}
