import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files

plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
    maven("https://repo.fancyplugins.de/releases")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

group = "dev.thezexquex.buggybedwars"
version = "1.0.0"
description = "buggybedwars"

val shadeBasePath = "${group}.libs."

dependencies {
    implementation("xyz.xenondevs.invui:invui:1.46")
    implementation("commons-io:commons-io:2.16.1")
    bukkitLibrary("org.incendo:cloud-paper:2.0.0-beta.10")
    bukkitLibrary("commons-io:commons-io:2.16.1")

    val scoreboardLibraryVersion = "2.4.0"
    implementation("net.megavex:scoreboard-library-api:$scoreboardLibraryVersion")
    implementation("net.megavex:scoreboard-library-implementation:$scoreboardLibraryVersion")
    implementation("net.megavex:scoreboard-library-modern:$scoreboardLibraryVersion")

    compileOnly("de.oliver:FancyNpcs:2.6.0")
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveClassifier = ""

        relocate("xyz.xenondevs.invui", shadeBasePath + "xyz.xenondevs.invui")
        relocate("net.megavex.scoreboardlibrary", shadeBasePath + "net.megavex.scoreboardlibrary")
    }

    runServer {
        dependsOn(shadowJar)
        minecraftVersion("1.21.5")

        downloadPlugins {
            modrinth("fancynpcs", "2.6.0")
            jvmArgs("-Dcom.mojang.eula.agree=true")
        }
    }
}

bukkit {
    name = "BuggyBedwars"
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    main = "$group.BuggyBedwarsPlugin"
    apiVersion = "1.21"

    depend = listOf("FancyNpcs")
}
