package dev.triumphteam.kipp.database

import com.zaxxer.hikari.HikariDataSource
import dev.triumphteam.core.feature.ApplicationFeature
import dev.triumphteam.core.feature.attribute.key
import dev.triumphteam.core.jda.JdaApplication
import dev.triumphteam.kipp.config.Config.Feature.key
import dev.triumphteam.kipp.database.Database.Feature.key
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

class Database {
    companion object Feature : ApplicationFeature<JdaApplication, Database, Database> {

        override val key = key<Database>("database")

        override fun install(application: JdaApplication, configure: Database.() -> Unit): Database {
            val databaseFile = File(application.applicationFolder, "kipp.db").apply {
                if (!exists()) createNewFile()
            }

            val hikari = HikariDataSource().apply {
                jdbcUrl = "jdbc:sqlite:${databaseFile.absolutePath}"
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