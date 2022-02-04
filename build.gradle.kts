import dev.triumphteam.helper.PlatformType
import dev.triumphteam.helper.core
import dev.triumphteam.helper.implementation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("me.mattstudios.triumph") version "0.2.6"
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://m2.dv8tion.net/releases")
        maven("https://repo.triumphteam.dev/releases/")
        maven("https://repo.triumphteam.dev/snapshots/")
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
        implementation(core(PlatformType.JDA, "2.0.2"))
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "16"
                javaParameters = true
            }
        }
    }

}