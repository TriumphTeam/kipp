package dev.triumphteam.kipp.database


import org.jetbrains.exposed.sql.Table

object Messages : Table() {
    val id = long("message-id").uniqueIndex()
    val content = varchar("message", 4050)
    val sender = long("author")
    val timestamp = long("timestamp").default(System.currentTimeMillis())

    override val primaryKey = PrimaryKey(id)
}