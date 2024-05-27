package dev.bogwalk.databases.custom

import dev.bogwalk.databases.tables.HangarLogs
import dev.bogwalk.models.currentDate
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.StatementInterceptor
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.statements.expandArgs

/**
 * Responsible for updating logs whenever a deletion occurs.
 */
class AuditInterceptor : StatementInterceptor {
    override fun afterExecution(
        transaction: Transaction,
        contexts: List<StatementContext>,
        executedStatement: PreparedStatementApi
    ) {
        contexts.forEach { context ->
            if (context.statement.type == StatementType.DELETE) {
                logDeletion(
                    transaction.id,
                    context.expandArgs(transaction),
                    currentDate()
                )
            }
        }
    }
}

private fun logDeletion(txId: String, details: String, date: LocalDate) {
    HangarLogs.insert {
        it[hangarId] = details.substringAfter('=').trim().toInt()
        it[transactionId] = txId
        it[departed] = date
    }
}