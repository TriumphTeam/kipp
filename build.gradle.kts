
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.triumphteam.helper.CoreFeature
import dev.triumphteam.helper.CorePlatform
import dev.triumphteam.helper.core
import dev.triumphteam.helper.feature
import dev.triumphteam.helper.implementation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    application
    id("me.mattstudios.triumph") version "0.2.3"
}

group = "dev.triumphteam"
version = "1.0"

// TODO this
//mainClassName = "me.mattstudios.kipp.MainKt"

/*sourceSets {
    main.kotlin.srcDirs = main.java.srcDirs = ['src/main/kotlin']
    main.resources.srcDirs = ['resources']
}*/

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io")
    maven("https://repo.triumphteam.dev/artifactory/public/")
    maven("https://m2.dv8tion.net/releases")
}

dependencies {

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")

    // Logger
    implementation("org.slf4j:slf4j-log4j12:1.7.5")
    implementation("log4j:apache-log4j-extras:1.2.17")

    // JDA
    implementation("net.dv8tion:JDA:4.3.0_307") {
        exclude(module = "opus-java")
    }

    // Database stuff
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("mysql:mysql-connector-java:8.0.20")
    implementation("org.xerial:sqlite-jdbc:3.36.0")


    // My stuff
    implementation("me.mattstudios.utils:matt-framework-jda:1.1.9-BETA")

    // Triumph
    implementation("me.mattstudios:triumph-config:1.0.5-SNAPSHOT")
    implementation(core(CorePlatform.JDA, "2.0.0"))
    implementation(feature(CoreFeature.CONFIG, "2.0.0"))

    // Google's shit
    implementation("com.google.cloud:google-cloud-dialogflow:2.0.0")
    implementation("com.google.cloud:google-cloud-storage:1.108.0")
    implementation("com.google.code.gson:gson:2.8.6")

    implementation("commons-validator:commons-validator:1.6")
    implementation("commons-cli:commons-cli:1.4")

    implementation("com.google.api-client:google-api-client:1.30.4")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.30.6")
    implementation("com.google.apis:google-api-services-sheets:v4-rev581-1.25.0")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
        }
    }

    withType<ShadowJar> {
        archiveFileName.set("kipp.jar")
    }
}