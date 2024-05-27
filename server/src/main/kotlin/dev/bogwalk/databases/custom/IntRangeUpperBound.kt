package dev.bogwalk.databases.custom

import org.jetbrains.exposed.sql.CustomFunction
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.IntegerColumnType

// Planets.selectAll().where { Planets.prices.upperBound() lessEq 30 }
/** Represents an SQL function that returns [this] range's upper bound value. */
fun <T : IntRange?> Expression<T>.upperBound() = RangeUpperBound(this)

class RangeUpperBound<T : IntRange?>(
    expr: Expression<T>
) : CustomFunction<Int?>("UPPER", IntegerColumnType(), expr)