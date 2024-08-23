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
    "de.tr7zw:item-nbt-api:2.13.1",
    "org.apache.commons:commons-lang3:3.14.0",
    "org.apache-extras.beanshell:bsh:2.1.1",
    "com.github.oraxen:protectionlib:1.5.1",
    "dev.jorel:commandapi-bukkit-shade:9.5.0",
    "com.google.code.gson:gson:2.11.0",
    "com.jeff-media:MorePersistentDataTypes:2.4.0",
    "com.jeff-media:custom-block-data:2.2.2"
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
    }

    if (project.name in listOf("plugin", "lib")) {
        apply(plugin = "io.github.goooler.shadow")

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
            relocate("de.tr7zw.changeme.nbtapi","dev.wuason.libs.nbtapi")
            relocate("io.th0rgal.protectionlib", "dev.wuason.libs.protectionlib")
            relocate("bsh", "dev.wuason.libs.bsh")
            relocate("org.apache.commons", "dev.wuason.libs.apache.commons")
            relocate("dev.wuason.mechanics.invmechanic", "dev.wuason.libs.invmechaniclib")
            relocate("com.google.gson", "dev.wuason.libs.google.gson")
            relocate("com.google.errorprone", "dev.wuason.libs.google.errorprone")
            relocate("com.jeff_media.morepersistentdatatypes", "dev.wuason.libs.jeffmedia.morepersistentdatatypes")
            relocate("com.jeff_media.customblockdata", "dev.wuason.libs.jeffmedia.customblockdata")
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
            //App
            implementation(project(":App"))
            //NMS
            implementation(project(":NMS:WRAPPER"))
            //Oraxen
            compileOnly("com.github.oraxen:oraxen:1.159.0")
            //LoneDev
            compileOnly("com.github.LoneDev6:API-ItemsAdder:3.5.0b")
            //PlaceholderAPI
            compileOnly("me.clip:placeholderapi:2.11.3")
            //WorldGuard
            compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.7")
            //Mythic
            compileOnly("io.lumine:Mythic-Dist:5.3.5")
            compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT")
            compileOnly("io.lumine:MythicCrucible:1.7.0-20230723.171823-33")
            //Phoneix
            compileOnly("net.Indyuce:MMOItems-API:6.9.4-SNAPSHOT")
            //libs compileOnly and implementation
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


/*
    * Create a task to run all servers
    * Create a task to run a specific server
 */

//create function to create a server test environment
/*
Servers path ./dev/
format_server_example: ./dev/1.18.2/
 */

/*fun server(ver: String, javaVer: Int): Future<*> {
    val executor: ExecutorService = Executors.newFixedThreadPool(1) as ThreadPoolExecutor
    return executor.submit {

    }
}*/

fun server(ver: String, javaVer: Int): String {
    val taskName = "server-${ver.replace(".", "_")}"
    tasks.register(taskName) {
        doFirst {

            println("Server -> $ver -> Starting")

            extra.set("ver", ver)
            extra.set("javaVer", javaVer)

            //copy plugin generated jar from shadowJar to server folder
            val pluginJar = file("target/${rootProject.name}-${rootProject.version}.jar")

            if (!pluginJar.exists()) {
                throw IllegalStateException("Plugin jar not found")
            }

            pluginJar.copyTo(file("dev/$ver/plugins/plugin.jar"), true)

            val javaPath = "C:\\Program Files\\Java\\jdk-$javaVer\\bin\\java.exe"
            val serverJarPath = "minecraft_$ver.jar"
            val command = listOf(javaPath, "-Xms512M", "-Xmx1G", "-jar", serverJarPath, "nogui")
            val processBuilder = ProcessBuilder(command)
            processBuilder.directory(file("dev/$ver"))
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            val thread = Executors.newSingleThreadExecutor()
            thread.submit {
                val lastLines = mutableListOf<String>()
                process.inputStream.bufferedReader().use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {

                        //saves the 10 last lines
                        if (lastLines.size >= 30) {
                            lastLines.removeAt(0)
                        }
                        lastLines.add(line!!)
                        if (line!!.contains("Disabling Mechanics")) {
                            process.destroy()
                            thread.shutdownNow()
                            extra.set("error", true)
                            extra.set("lastLines", lastLines)
                            println("Server -> $ver -> Finished")
                            break
                        }
                        if (line!!.contains("Mechanics plugin ok!")) {
                            process.destroy()
                            thread.shutdownNow()
                            extra.set("error", false)
                            extra.set("lastLines", lastLines)
                            println("Server -> $ver -> Finished")
                            break
                        }
                        //println("line: $line")
                    }
                }
            }
            //thread.shutdown()
            process.waitFor()
        }
    }
    return taskName
}

//create a tasK for all servers

val SERVER_1_18_2 = server("1.18.2", 17)
val SERVER_1_19 = server("1.19", 17)
val SERVER_1_19_1 = server("1.19.1", 17)
val SERVER_1_19_2 = server("1.19.2", 17)
val SERVER_1_19_3 = server("1.19.3", 17)
val SERVER_1_19_4 = server("1.19.4", 17)
val SERVER_1_20 = server("1.20", 17)
val SERVER_1_20_1 = server("1.20.1", 17)
val SERVER_1_20_2 = server("1.20.2", 17)
val SERVER_1_20_4 = server("1.20.4", 17)
val SERVER_1_20_5 = server("1.20.5", 21)
val SERVER_1_20_6 = server("1.20.6", 21)
val SERVER_1_21 = server("1.21", 21)


tasks.build {
    dependsOn(tasks.getByPath(":plugin:shadowJar"))
    dependsOn(tasks.getByPath(":lib:shadowJar"))
}


tasks.register("runAllServers") {
    val list = listOf(
        SERVER_1_18_2,
        SERVER_1_19, SERVER_1_19_1, SERVER_1_19_2, SERVER_1_19_3, SERVER_1_19_4,
        SERVER_1_20, SERVER_1_20_1, SERVER_1_20_2, SERVER_1_20_4, SERVER_1_20_5, SERVER_1_20_6,
        SERVER_1_21
    )

    dependsOn(list)

    doLast {

        val RED = "\u001B[31m"
        val BLUE = "\u001B[34m"
        val YELLOW = "\u001B[33m"
        val GREEN = "\u001B[32m"
        val RESET = "\u001B[0m"

        println("")
        println("")
        println("")

        println("-------------- LAST LINES ERRORS ---------------")
        println("")
        for (task in list) {
            val lastLines = tasks.getByName(task).extra.get("lastLines") as List<String>
            val ver = tasks.getByName(task).extra.get("ver")
            val error = tasks.getByName(task).extra.get("error") as Boolean?
            if (error == false) {
                continue
            }
            println("----------------- $BLUE$ver$RESET -----------------")
            println("")
            for (line in lastLines) {
                println("$YELLOW$line$RESET")
            }
            println("")
        }
        println("------------------------------------------------")

        println("")

        println("--------------STATUS---------------")
        for (task in list) {
            val error = tasks.getByName(task).extra.get("error") as Boolean?
            val ver = tasks.getByName(task).extra.get("ver")
            val colorCode = if (error == true) "\u001B[31m" else "\u001B[32m" // Rojo para error, verde para OK
            println("$colorCode Server $ver: ${if (error == true) "Error" else "Ok"} \u001B[0m")
        }

    }
}
