package me.mattstudios.kipp.data

import com.google.gson.Gson
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import me.mattstudios.kipp.Kipp
import me.mattstudios.kipp.json.JsonEmbed
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.settings.Setting.BLACK_LISTED_CHANNELS
import me.mattstudios.kipp.utils.Color
import me.mattstudios.kipp.utils.Embed
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import java.sql.SQLException
import java.time.ZoneId
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis


/**
 * @author Matt
 */
class Database(private val config: Config) {

    private val dataSource: HikariDataSource
    private val gson = Gson()

    init {
        val hikariConfig = HikariConfig()

        hikariConfig.jdbcUrl = "jdbc:mysql://${config[Setting.SQL_HOST]}:3306/${config[Setting.SQL_DATABASE]}"
        hikariConfig.username = config[Setting.SQL_USER]
        hikariConfig.password = config[Setting.SQL_PASSWORD]

        hikariConfig.addDataSourceProperty("cachePrepStmts", true)
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250)
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048)
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true)
        hikariConfig.addDataSourceProperty("useLocalSessionState", true)
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", true)
        hikariConfig.addDataSourceProperty("cacheResultSetMetadata", true)
        hikariConfig.addDataSourceProperty("cacheServerConfiguration", true)
        hikariConfig.addDataSourceProperty("elideSetAutoCommits", true)
        hikariConfig.addDataSourceProperty("maintainTimeStats", false)
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8")
        hikariConfig.addDataSourceProperty("useUnicode", "true")
        hikariConfig.connectionInitSql = "SET NAMES 'utf8mb4'"

        dataSource = HikariDataSource(hikariConfig)

        if (dataSource.isRunning) Kipp.logger.info("Connected to \"${config[Setting.SQL_DATABASE]}\" successfully")
    }

    /**
     * Inserts all the members into the database
     */
    suspend fun insertAll(guild: Guild, channel: MessageChannel) {
        val time = withContext(IO) {
            measureTimeMillis {
                dataSource.connection.use { connection ->
                    guild.members.forEach { member ->
                        val preparedStatement = connection.prepareStatement("insert ignore into members values (?, ?, ?)")

                        preparedStatement.setLong(1, member.idLong)
                        preparedStatement.setString(2, "unknown")
                        preparedStatement.setString(3, "unknown")

                        preparedStatement.executeUpdate()
                    }
                }
            }
        }

        channel.queueMessage(
                Embed()
                        .color(Color.SUCCESS)
                        .field(
                                "Updating database",
                                "Successfully updated!" +
                                "\nComplete in ${TimeUnit.MILLISECONDS.toSeconds(time)}s"
                        ).build()
        )
    }

    /**
     * Inserts all the members into the database
     */
    suspend fun insertAllMessages(guild: Guild, messageChannel: TextChannel) {
        val time = withContext(IO) {
            measureTimeMillis {
                val connection = dataSource.connection
                guild.textChannels.filter { it.id !in config[BLACK_LISTED_CHANNELS] }
                        .forEach { channel ->
                            channel.history.retrievePast(100).complete()
                                    .filter { !it.author.isBot }
                                    .forEach { message ->
                                        try {
                                            val statement = connection.prepareStatement("replace into messages(message_id, message, author) values(?, ?, ?)")
                                            statement.setLong(1, message.idLong)
                                            statement.setString(2, message.contentDisplay)
                                            statement.setLong(3, message.author.idLong)

                                            statement.executeUpdate()
                                        } catch (exception: SQLException) {
                                            Kipp.logger.warn("Error inserting message to database!")
                                        }
                                    }
                        }
            }

        }

        messageChannel.queueMessage(
                Embed()
                        .color(Color.SUCCESS)
                        .field(
                                "Updating database messages",
                                "Successfully updated all messages!" +
                                "\nComplete in ${TimeUnit.MILLISECONDS.toSeconds(time)}s"
                        ).build()
        )

    }

    /**
     * Insert the new member
     */
    suspend fun insertMember(member: Member, invite: Invite?) {
        withContext(IO) {
            dataSource.connection.use { connection ->
                val preparedStatement = connection.prepareStatement("replace into members values (?, ?, ?)")

                preparedStatement.setLong(1, member.idLong)
                preparedStatement.setString(2, invite?.code)
                preparedStatement.setString(3, invite?.inviter?.name)

                preparedStatement.executeUpdate()
            }
        }
    }

    /**
     * Insert the new message
     */
    suspend fun insertMessage(message: Message) {
        withContext(IO) {
            dataSource.connection.use { connection ->
                val preparedStatement = connection.prepareStatement("replace into messages(message_id, message, author) values (?, ?, ?)")

                preparedStatement.setLong(1, message.idLong)
                preparedStatement.setString(2, message.contentDisplay)
                preparedStatement.setLong(3, message.author.idLong)

                preparedStatement.executeUpdate()
            }
        }
    }

    /**
     * Inserts a faq into the database
     */
    suspend fun insertFaq(command: String, jsonEmbed: JsonEmbed, author: User) {
        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("replace into faqs(command, embed, creator_id) values (?, ?, ?)")

            preparedStatement.setString(1, command)
            preparedStatement.setString(2, gson.toJson(jsonEmbed))
            preparedStatement.setLong(3, author.idLong)

            preparedStatement.executeUpdate()
        }
    }

    /**
     * Inserts a to-do into the database
     */
    fun insertTodo(id: String, todo: String) {
        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("replace into todos(todo_id, todo) values (?, ?)")

            preparedStatement.setString(1, id)
            preparedStatement.setString(2, todo)

            preparedStatement.executeUpdate()
        }
    }

    /**
     * Insert a reminder into the database
     */
    fun insertReminder(date: Date, userId: Long, reminder: String) {
        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("insert into reminders(date, reminder, user) values (?, ?, ?)")

            preparedStatement.setObject(1, date.toInstant().atZone(ZoneId.of("Europe/Lisbon")).toLocalDateTime())
            preparedStatement.setString(2, reminder)
            preparedStatement.setLong(3, userId)

            preparedStatement.executeUpdate()
        }
    }

    /**
     * Gets an invite from the member id
     */
    fun getInvite(memberId: Long): KippInvite? {
        return dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("select invite, invited_by from members where member_id = ?")
            preparedStatement.setLong(1, memberId)

            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val invite = resultSet.getString("invite")
                val inviter = resultSet.getString("invited_by")
                return@use KippInvite(invite, inviter)
            }

            return@use null
        }
    }

    /**
     * Gets the message if it's on the database
     */
    fun getMessage(messageId: Long): String? {
        return dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("select message from messages where message_id = ?")
            preparedStatement.setLong(1, messageId)

            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) return@use resultSet.getString("message")
            return@use null
        }
    }

    /**
     * Gets the message if it's on the database
     */
    fun getMessageAuthor(messageId: Long): Long? {
        return dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("select author from messages where message_id = ?")
            preparedStatement.setLong(1, messageId)

            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) return@use resultSet.getLong("author")
            return@use null
        }
    }

    /**
     * Gets a list of faq's that was stored in the database
     */
    fun getFaqs(): List<JsonEmbed> {
        val list = mutableListOf<JsonEmbed>()

        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("select embed from faqs")
            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) list.add(gson.fromJson(resultSet.getString("embed"), JsonEmbed::class.java))
        }

        return list
    }

    /**
     * Gets a to-do list that was stored in the database
     */
    fun getTodos(): Map<String, String> {
        val list = mutableMapOf<String, String>()

        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("select * from todos")
            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) list[resultSet.getString("todo_id")] = resultSet.getString("todo")
        }

        return list
    }

    /**
     * Deletes the faq from the database
     */
    fun deleteFaq(faq: String) {
        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("delete from faqs where command = ?")
            preparedStatement.setString(1, faq)
            preparedStatement.executeUpdate()
        }
    }

    /**
     * Deletes the faq from the database
     */
    fun deleteTodo(id: String) {
        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("delete from todos where todo_id = ?")
            preparedStatement.setString(1, id)
            preparedStatement.executeUpdate()
        }
    }

    /**
     * Deletes the faq from the database
     */
    fun deleteReminder(id: String) {
        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("delete from todos where todo_id = ?")
            preparedStatement.setString(1, id)
            preparedStatement.executeUpdate()
        }
    }

}