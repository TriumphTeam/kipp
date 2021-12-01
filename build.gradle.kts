import dev.triumphteam.helper.PlatformType
import dev.triumphteam.helper.core
import dev.triumphteam.helper.implementation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("me.mattstudios.triumph") version "0.2.6"
}

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

    group = "dev.triumphteam"
    version = "1.0"

    dependencies {
        // Kotlin
        implementation(kotlin("stdlib"))

        // Logger
        implementation("org.apache.logging.log4j:log4j-api:2.14.1")
        implementation("org.apache.logging.log4j:log4j-core:2.14.1")
        implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.14.1")

        // Triumph
        implementation(core(PlatformType.JDA, "2.0.1-SNAPSHOT"))

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