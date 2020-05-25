package me.mattstudios.kipp.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.mattstudios.kipp.Kipp
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import me.mattstudios.kipp.utils.Embed
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis


/**
 * @author Matt
 */
class Database(config: Config) {

    private val dataSource: HikariDataSource

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

        dataSource = HikariDataSource(hikariConfig)

        if (dataSource.isRunning) Kipp.logger.info("Connected to \"${config[Setting.SQL_DATABASE]}\" successfully")
    }

    /**
     * Inserts all the members into the database
     */
    fun insertAll(guild: Guild, channel: MessageChannel) {
        val task = CompletableFuture.supplyAsync {
            return@supplyAsync measureTimeMillis {
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

        task.thenAccept {
            channel.sendMessage(
                    Embed().field(
                            "Updating database",
                            "Successfully updated!" +
                            "\nComplete in ${TimeUnit.MILLISECONDS.toSeconds(it)}s"
                    ).build()
            ).queue()
        }
    }

    /**
     * Insert the new member
     */
    fun insertMember(member: Member, invite: Invite?) {
        CompletableFuture.runAsync {
            dataSource.connection.use { connection ->
                val preparedStatement = connection.prepareStatement("replace into members values (?, ?, ?)")

                preparedStatement.setLong(1, member.idLong)
                preparedStatement.setString(2, invite?.code)
                preparedStatement.setString(3, invite?.inviter?.name)

                preparedStatement.executeUpdate()
            }
        }
    }

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

}