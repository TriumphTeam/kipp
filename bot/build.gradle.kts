import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.triumphteam.helper.Feature
import dev.triumphteam.helper.PlatformType
import dev.triumphteam.helper.feature
import dev.triumphteam.helper.implementation

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

// TODO this
//mainClassName = "me.mattstudios.dev.triumphteam.kipp.MainKt"

/*sourceSets {
    main {
        java.srcDir("src/main/kotlin")
        resources.srcDir("src/main/resources")
    }
}*/

dependencies {
    // Database stuff
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.jetbrains.exposed:exposed-core:0.36.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.36.2")
    implementation("org.xerial:sqlite-jdbc:3.36.0")

    // Triumph
    implementation(feature(Feature.CONFIG, "2.0.1-SNAPSHOT"))
    implementation(feature(Feature.SCHEDULER, "2.0.1-SNAPSHOT"))
    implementation(feature(Feature.COMMANDS, PlatformType.JDA, "2.0.1-SNAPSHOT"))
    implementation(feature(Feature.LISTENERS, PlatformType.JDA, "2.0.1-SNAPSHOT"))

    // Google's shit
    implementation("com.google.code.gson:gson:2.8.6")

    implementation("commons-validator:commons-validator:1.6")
    implementation("commons-cli:commons-cli:1.4")
}

tasks {
    withType<ShadowJar> {
        archiveFileName.set("dev.triumphteam.kipp.jar")
    }
}