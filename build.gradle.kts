
import dev.triumphteam.helper.implementation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("me.mattstudios.triumph") version "0.2.3"
}

group = "dev.triumphteam"
version = "1.0"

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://m2.dv8tion.net/releases")
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("kotlinx-serialization")
        plugin("me.mattstudios.triumph")
    }

    dependencies {
        // Kotlin
        implementation(kotlin("stdlib"))

        // Logger
        implementation("ch.qos.logback:logback-classic:1.2.5")

        // JDA
        implementation("net.dv8tion:JDA:5.0.0-alpha.1") {
            //exclude(module = "opus-java")
        }
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "16"
            }
        }
    }

}