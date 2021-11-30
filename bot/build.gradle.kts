import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.triumphteam.helper.CoreFeature
import dev.triumphteam.helper.CorePlatform
import dev.triumphteam.helper.core
import dev.triumphteam.helper.feature
import dev.triumphteam.helper.implementation

plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    application
}

// TODO this
//mainClassName = "me.mattstudios.dev.triumphteam.kipp.MainKt"

/*sourceSets {
    main.kotlin.srcDirs = main.java.srcDirs = ['src/main/kotlin']
    main.resources.srcDirs = ['resources']
}*/

dependencies {
    // TODO remove
    implementation("com.sedmelluq:lavaplayer:1.3.77")

    // Database stuff
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.jetbrains.exposed:exposed-core:0.33.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.33.1")
    implementation("org.xerial:sqlite-jdbc:3.36.0")

    // TODO: 11/29/2021 REMOVE
    implementation("me.mattstudios.utils:matt-framework-jda:1.1.9-BETA")

    // Triumph
    implementation(core(CorePlatform.JDA, "2.0.1-SNAPSHOT"))
    implementation(feature(CoreFeature.CONFIG, "2.0.1-SNAPSHOT"))
    implementation(feature("feature-jda-commands", "2.0.1-SNAPSHOT"))
    implementation(feature("feature-jda-listeners", "2.0.1-SNAPSHOT"))

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
    withType<ShadowJar> {
        archiveFileName.set("dev.triumphteam.kipp.jar")
    }
}