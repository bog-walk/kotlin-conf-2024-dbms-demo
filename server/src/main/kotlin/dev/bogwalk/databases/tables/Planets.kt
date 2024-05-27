package dev.bogwalk.databases.tables

import dev.bogwalk.databases.custom.intRange
import dev.bogwalk.models.Region
import org.jetbrains.exposed.dao.id.LongIdTable

object Planets : LongIdTable("planets") {
    val name = varchar("planet_name", 128)
    val region = enumeration<Region>("region")
    val distance = double("distance")
    val prices = intRange("prices").default(0..1000)
}