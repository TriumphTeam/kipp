package me.mattstudios.kipp.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.mattstudios.kipp.Kipp
import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.entities.Member


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

    fun insertAll(guild: Guild) {
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

    fun insertMember(member: Member, invite: Invite?) {
        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement("replace into members values (?, ?, ?)")

            preparedStatement.setLong(1, member.idLong)
            preparedStatement.setString(2, invite?.code)
            preparedStatement.setString(3, invite?.inviter?.name)

            preparedStatement.executeUpdate()
        }
    }

}