package dev.bogwalk.databases.custom

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject

/** Registers a table column that accepts `IntRange` values as input. */
fun Table.intRange(name: String): Column<IntRange> = registerColumn(name, IntRangeColumnType())

class IntRangeColumnType : ColumnType<IntRange>() {
    override fun sqlType(): String = "int4range"

    override fun nonNullValueToString(value: IntRange): String = "[${value.first},${value.last}]"

    override fun nonNullValueAsDefaultString(value: IntRange): String = "${sqlType()}(${value.first},${value.last},'[]')"

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val parameterValue: PGobject? = value?.let {
            PGobject().apply {
                type = sqlType()
                this.value = nonNullValueToString(it as IntRange)
            }
        }
        super.setParameter(stmt, index, parameterValue)
    }

    override fun valueFromDB(value: Any): IntRange? = when (value) {
        is PGobject -> value.value?.let {
            val components = it.trim('[', ')').split(',')
            IntRange(components.first().toInt(), components.last().toInt() - 1)
        }
        else -> error("Retrieved unexpected value of type ${value::class.simpleName}")
    }
}