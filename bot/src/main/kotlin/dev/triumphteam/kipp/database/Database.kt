package dev.triumphteam.kipp.database

import com.zaxxer.hikari.HikariDataSource
import dev.triumphteam.bukkit.feature.ApplicationFeature
import dev.triumphteam.bukkit.feature.attribute.key
import dev.triumphteam.jda.JdaApplication
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class Database {
    companion object Feature : ApplicationFeature<JdaApplication, Database, Database> {

        override val key = key<Database>("database")

        override fun install(application: JdaApplication, configure: Database.() -> Unit): Database {
            val databaseFile = File(application.applicationFolder, "kipp.db")

            val hikari = HikariDataSource().apply {
                dataSourceClassName = "org.sqlite.SQLiteDataSource"
                jdbcUrl = "jdbc:sqlite:${databaseFile.name}"
                isAutoCommit = false
            }

            org.jetbrains.exposed.sql.Database.connect(hikari)

            transaction {
                SchemaUtils.create(
                    Messages,
                )
            }

            return Database()
        }
    }
}