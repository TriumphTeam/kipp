package me.mattstudios.kipp.listeners

import me.mattstudios.kipp.data.Cache
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * @author Matt
 */
class SettingsListener(private val config: Config, private val cache: Cache) : ListenerAdapter() {

    /**
     * Adds roles to the subscribe category
     */
    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        if (event.messageId != config[Setting.SETTINGS_MESSAGE]) return
        val member = event.member
        val guild = event.guild

        val memberRoles = member.roles

        when (event.reactionEmote.emote) {
            cache.ccmdEmote -> {
                val ccmdRole = cache.ccmdRole ?: return
                if (ccmdRole in memberRoles) return

                guild.addRoleToMember(member, ccmdRole).queue()
                member.addSubscriptionsRole(guild)
            }

            cache.mfEmote -> {
                val mfRole = cache.mfRole ?: return
                if (mfRole in memberRoles) return

                guild.addRoleToMember(member, mfRole).queue()
                member.addSubscriptionsRole(guild)
            }

            cache.mattEmote -> {
                val mattRole = cache.mattRole ?: return
                if (mattRole in memberRoles) return

                guild.addRoleToMember(member, mattRole).queue()
                member.addSubscriptionsRole(guild)
            }
        }

    }

    /**
     * Removes the roles from the subscribe categories
     */
    override fun onGuildMessageReactionRemove(event: GuildMessageReactionRemoveEvent) {
        if (event.messageId != config[Setting.SETTINGS_MESSAGE]) return
        val member = event.member ?: return
        val guild = event.guild

        val memberRoles = member.roles

        when (event.reactionEmote.emote) {
            cache.ccmdEmote -> {
                val ccmdRole = cache.ccmdRole ?: return
                if (ccmdRole !in memberRoles) return

                guild.removeRoleFromMember(member, ccmdRole).queue() {
                    member.checkSubscriptionsRole(guild)
                }
            }

            cache.mfEmote -> {
                val mfRole = cache.mfRole ?: return
                if (mfRole !in memberRoles) return

                guild.removeRoleFromMember(member, mfRole).queue() {
                    member.checkSubscriptionsRole(guild)
                }
            }

            cache.mattEmote -> {
                val mattRole = cache.mattRole ?: return
                if (mattRole !in memberRoles) return

                guild.removeRoleFromMember(member, mattRole).queue() {
                    member.checkSubscriptionsRole(guild)
                }
            }
        }
    }

    /**
     * Adds the ping role to the member
     */
    private fun Member.addSubscriptionsRole(guild: Guild) {
        val subscriptionRole = cache.subscriptionsRole ?: return
        if (subscriptionRole in roles) return
        guild.addRoleToMember(this, subscriptionRole).queue()
    }

    /**
     * Adds the ping role to the member
     */
    private fun Member.checkSubscriptionsRole(guild: Guild) {
        val subscriptionRole = cache.subscriptionsRole ?: return
        if (this.roles.any { it.position < subscriptionRole.position }) return
        guild.removeRoleFromMember(this, subscriptionRole).queue()
    }

}