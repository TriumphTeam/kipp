package dev.triumphteam.kipp.commands.prefixed

import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.Default
import dev.triumphteam.cmd.core.annotation.Requirement
import dev.triumphteam.cmd.prefixed.annotation.Prefix
import dev.triumphteam.cmd.prefixed.sender.PrefixedSender
import dev.triumphteam.core.feature.get
import dev.triumphteam.kipp.Kipp
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.embed
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.net.URL

@Prefix("!")
@Command("release")
class ReleaseCommand(kipp: Kipp) : BaseCommand() {

    private val config = kipp[Config]
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
            accept(ContentType.Application.Json)
            accept(ContentType.Text.Plain)
        }
    }

    @Default
    @Requirement("admin")
    fun PrefixedSender.default(project: Project, url: URL) {
        val (channelId, roleId) = when (project.projectType) {
            ProjectType.PLUGIN -> config[Settings.CHANNELS].pluginUpdates to config[Settings.ROLES].libs
            ProjectType.LIBRARY -> config[Settings.CHANNELS].libUpdates to config[Settings.ROLES].libs
            ProjectType.EXTRA -> config[Settings.CHANNELS].extraUpdates to config[Settings.ROLES].extra
        }

        val channel = guild.getTextChannelById(channelId) ?: return
        val role = guild.getRoleById(roleId) ?: return

        CoroutineScope(Dispatchers.Default).launch {
            val release = client.get<Release>(url)

            val embed = embed {
                title("New ${project.projectName} update!")
                description(release.description)
                fields {
                    field("Version", "[${release.version}](https://google.com/)")
                    field(
                        "",
                        """
                            ```
                            ${release.changelog}
                            ```
                        """.trimIndent()
                    )
                }
                thumbnail(project.thumbnail)
                color(project.color)
            }

            channel.sendMessage(role.asMention).setEmbeds(embed).queue()
        }
    }
}

enum class ProjectType {
    PLUGIN,
    LIBRARY,
    EXTRA
}

enum class Project(
    val projectName: String,
    val thumbnail: String,
    val color: String,
    val projectType: ProjectType,
) {
    CMD("triumph-cmd", "https://i.imgur.com/9JnGwCe.png", "#D267A2", ProjectType.LIBRARY),
    GUI("triumph-gui", "https://i.imgur.com/m8HMvpQ.png", "#22BD85", ProjectType.LIBRARY),
    CORE("triumph-core", "https://i.imgur.com/gkixuUM.png", "#7F4AA7", ProjectType.EXTRA),
}

@Serializable
data class Release(
    val version: String,
    val description: String,
    val changelog: String,
    val github: String,
)
