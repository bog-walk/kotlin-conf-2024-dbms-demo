package dev.bogwalk.databases.tables

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Planet(id: EntityID<Long>) : LongEntity(id) {
    var name by Planets.name
    var region by Planets.region
    var distance by Planets.distance
    var prices by Planets.prices
    val ships by Ship referrersOn Ships.originPlanet

    companion object : LongEntityClass<Planet>(Planets)
}