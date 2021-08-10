package dev.triumphteam.kipp.database

import org.jetbrains.exposed.sql.Table

object Messages : Table() {
    val messageId = long("message-id").uniqueIndex()
    val message = text("message")
    val author = long("author")

    override val primaryKey = PrimaryKey(messageId)
}