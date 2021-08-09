package me.mattstudios.kipp.settings

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.configurationdata.CommentsConfiguration
import me.mattstudios.config.properties.Property

object Setting : SettingsHolder {

    @Path("token")
    @Comment("The token that is needed to login into the bot")
    val TOKEN = Property.create("none")

    @Path("channel.join-log")
    val JOIN_LOG_CHANNEL = Property.create("0")

    @Path("channel.leak-log")
    val LEAK_LOG_CHANNEL = Property.create("0")

    @Path("channel.message-log")
    val MESSAGE_LOG_CHANNEL = Property.create("0")

    @Path("channel.reminder")
    val REMINDER_CHANNEL = Property.create("0")

    @Path("channel.suggestions")
    val SUGGESTIONS_CHANNEL = Property.create("0")

    @Path("channel.bugs")
    val BUGS_CHANNEL = Property.create("0")

    @Path("channel.bot-cmds")
    val BOT_CMDS_CHANNEL = Property.create("0")

    @Path("role.member")
    val MEMBER_ROLE = Property.create("0")

    @Path("role.plugins")
    val PLUGINS_ROLE = Property.create("0")

    @Path("role.subscriptions")
    val SUBSCRIPTIONS_ROLE = Property.create("0713927697679777802")

    @Path("role.admin")
    val ADMIN_ROLE = Property.create("496353695605456897")

    @Path("role.ccmd")
    val CCMD_ROLE = Property.create("713929371290959904")

    @Path("role.mf")
    val MF_ROLE = Property.create("713929424269082684")

    @Path("role.matt")
    val MATT_ROLE = Property.create("713929561603047466")

    @Path("message.settings")
    val SETTINGS_MESSAGE = Property.create("0")

    @Path("database.host")
    val SQL_HOST = Property.create("localhost")

    @Path("database.user")
    val SQL_USER = Property.create("matt")

    @Path("database.password")
    val SQL_PASSWORD = Property.create("bleh")

    @Path("database.database")
    val SQL_DATABASE = Property.create("matt")

    @Path("dialogflow.project-id")
    val DIALOGFLOW_PROJECT = Property.create("")

    @Path("leak-words")
    @Comment("\n", "List with all the leak detection words")
    val LEAK_WORDS: Property<MutableList<String>> = Property.create(listOf(""))

    @Path("black-listed-channels")
    @Comment("\n", "List with the ids of channels that should be excluded from the message logging")
    val BLACK_LISTED_CHANNELS = Property.create(listOf(""))

    override fun registerComments(conf: CommentsConfiguration) {
        conf.setComment("channel", "\n", "The default channels for various actions")
        conf.setComment("role", "\n", "The default roles for various actions")
        conf.setComment("message", "\n", "Ids for various messages")
        conf.setComment("database", "\n", "Database data")
        conf.setComment("dialogflow", "\n", "Dialowgflow related settings")
    }
}
