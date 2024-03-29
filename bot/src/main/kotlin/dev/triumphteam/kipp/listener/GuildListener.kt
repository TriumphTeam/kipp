package dev.triumphteam.kipp.listener

import dev.triumphteam.core.jda.JdaApplication
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.Settings
import dev.triumphteam.kipp.func.kippInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import net.dv8tion.jda.api.entities.Guild

private val scope = CoroutineScope(Default)

/*fun JdaApplication.guildListener() {
    val config = feature(Config)

    on<GuildReadyEvent> {
        repeatingTask(period = minutes(2), delay = seconds(30)) {
            updatePresence(guild)
        }

        scope.launch { checkMember(guild, config) }
    }
}*/

suspend fun JdaApplication.checkMember(guild: Guild, config: Config) {
    val memberRole = guild.getRoleById(config[Settings.ROLES].member) ?: return
    var count = 0
    guild.members.forEach { member ->
        if (memberRole in member.roles) return@forEach
        guild.addRoleToMember(member, memberRole).queue()
        count++
        // Delay is because discord is super buggy and sometimes won't add the roles right if done too fast
        delay(500)
    }

    if (count == 0) return

    kippInfo { "Synced member role to $count members." }
}