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
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
    maven("https://repo.fancyplugins.de/releases")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

group = "club.devcord.gamejam"
version = "1.0.0"
description = "gamejam"

val shadeBasePath = "${group}.libs."

dependencies {
    implementation("xyz.xenondevs.invui:invui:1.33")
    implementation("commons-io:commons-io:2.16.1")
    bukkitLibrary("org.incendo:cloud-paper:2.0.0-beta.10")
    bukkitLibrary("commons-io:commons-io:2.16.1")

    val scoreboardLibraryVersion = "2.1.12"
    bukkitLibrary("net.megavex:scoreboard-library-api:$scoreboardLibraryVersion")
    bukkitLibrary("net.megavex:scoreboard-library-implementation:$scoreboardLibraryVersion")
    bukkitLibrary("net.megavex:scoreboard-library-modern:$scoreboardLibraryVersion")

    compileOnly("de.oliver:FancyNpcs:2.2.2")
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
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
        relocate("io.github.juliarn", shadeBasePath + "io.github.juliarn")
    }

    runServer {
        dependsOn(shadowJar)
        minecraftVersion("1.21.1")

        downloadPlugins {
//            url("https://ci.mg-dev.eu/job/BKCommonLib/lastBuild/artifact/build/BKCommonLib-1.21.1-v1-SNAPSHOT-1793.jar")
//            url("https://ci.mg-dev.eu/job/MyWorlds/lastBuild/artifact/target/MyWorlds-1.21.1-v1-SNAPSHOT-311.jar")
            modrinth("fancynpcs", "2.2.2")
            jvmArgs("-Dcom.mojang.eula.agree=true")
        }
    }

    register("uploadJar") {
        dependsOn(shadowJar)

        doLast {
            val filePath = project.tasks.shadowJar.get().archiveFile.get().asFile.toPath()
            // Add the upload api url to your repo secrets under UPLOAD_URL
            // Obtain the upload url via /team api
            val apiUrl = System.getenv("UPLOAD_URL") ?: throw RuntimeException("Please set UPLOAD_URL in your github secrets")
            val client = HttpClient.newHttpClient()
            val request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                //.uri(URI.create("$apiUrl?restart=true"))
                .header("Content-Type", "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(Files.readAllBytes(filePath)))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() != 202) throw RuntimeException("Could not upload:\n" + response.body())
            println("Upload successful")
        }
    }
}

bukkit {
    name = "BuggyBedwars"
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    main = "club.devcord.gamejam.BuggyBedwarsPlugin"
    apiVersion = "1.20"

    depend = listOf("FancyNpcs")
}
