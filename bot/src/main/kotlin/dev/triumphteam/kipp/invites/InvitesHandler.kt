package dev.triumphteam.kipp.invites

import dev.triumphteam.core.feature.ApplicationFeature
import dev.triumphteam.core.feature.attribute.key
import dev.triumphteam.core.jda.JdaApplication
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class InvitesHandler : ListenerAdapter() {

    private val invites = mutableMapOf<Long, List<Invite>>()

    override fun onGuildReady(event: GuildReadyEvent) {
        event.guild.retrieveInvites().queue {
            invites[event.guild.idLong] = it
        }
    }

    /**
     * Gets the invite used.
     */
    fun getLastInvite(guild: Guild): Invite? {
        var invite: Invite? = null
        guild.retrieveInvites().queue { newInvites ->
            val oldInvites = invites[guild.idLong] ?: return@queue

            invite = newInvites.find { invite ->
                val foundInvite = oldInvites.find { it.code == invite.code } ?: return@find false
                return@find invite.uses > foundInvite.uses
            }

            invites[guild.idLong] = newInvites
        }
        return invite
    }

    companion object Feature : ApplicationFeature<JdaApplication, InvitesHandler, InvitesHandler> {

        override val key = key<InvitesHandler>("invites-handler")

        override fun install(application: JdaApplication, configure: InvitesHandler.() -> Unit): InvitesHandler {
            return InvitesHandler().apply(configure).apply { application.jda.addEventListener(this) }
        }
    }

}