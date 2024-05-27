package dev.bogwalk.databases.custom

import org.jetbrains.exposed.sql.ComparisonOp
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap

// Planets.selectAll().where { Planets.prices isContainedBy 5..50 }
/** Represents an SQL function that checks whether [this] range is contained within the bounds of [other]. */
infix fun <T : IntRange?> ExpressionWithColumnType<T>.isContainedBy(
    other: T
) = RangeIsContainedOp(this, wrap(other))

class RangeIsContainedOp<T : IntRange?>(
    expr1: Expression<T>,
    expr2: Expression<T>
) : ComparisonOp(expr1, expr2, "<@")