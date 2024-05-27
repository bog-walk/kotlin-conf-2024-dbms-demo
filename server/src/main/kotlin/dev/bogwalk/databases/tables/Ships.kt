package dev.bogwalk.databases.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDate
import org.jetbrains.exposed.sql.kotlin.datetime.date

object Ships : IdTable<Int>("ships") {
    override val id = integer("hangar_id").check { it.between(1, 47) }.entityId()
    val license = varchar("license", 128).uniqueIndex()
    val type = varchar("ship_type", 128).nullable()
    val arrivalDate = date("arrival_date").defaultExpression(CurrentDate)
    val originPlanet = reference("origin_planet", Planets)

    override val primaryKey = PrimaryKey(id)
}