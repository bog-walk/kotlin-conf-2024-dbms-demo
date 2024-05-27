package dev.bogwalk.databases.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object HangarLogs : Table("hangar_logs") {
    val hangarId = integer("hangar_id")
    val transactionId = varchar("transaction_id", 128)
    val departed = date("departed")

    override val primaryKey = PrimaryKey(hangarId, transactionId)
}