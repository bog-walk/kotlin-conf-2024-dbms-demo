package dev.bogwalk.databases

import com.zaxxer.hikari.HikariDataSource
import dev.bogwalk.databases.custom.AuditInterceptor
import dev.bogwalk.databases.custom.createDistanceFunction
import dev.bogwalk.databases.tables.HangarLogs
import dev.bogwalk.databases.tables.Planets
import dev.bogwalk.databases.tables.Ships
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

interface DatabaseFactory {
    fun createHikariDataSource(): HikariDataSource

    fun connect() {
        Database.connect(
            createHikariDataSource(),
            databaseConfig = DatabaseConfig {
                defaultMaxAttempts = 1
            }
        )
        transaction {
            SchemaUtils.create(Planets, Ships, HangarLogs)

            createDistanceFunction()
        }
    }
    suspend fun <T> query(block: suspend Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        val auditor = AuditInterceptor()
        registerInterceptor(auditor)
        block().also { unregisterInterceptor(auditor) }
    }
}