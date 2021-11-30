
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
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        // Logger
        implementation("ch.qos.logback:logback-classic:1.2.5")

        // JDA
        implementation("net.dv8tion:JDA:4.3.0_307") {
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