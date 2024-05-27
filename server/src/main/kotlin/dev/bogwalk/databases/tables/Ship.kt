package dev.bogwalk.databases.tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Ship(id: EntityID<Int>) : IntEntity(id) {
    var license by Ships.license
    var type by Ships.type
    var arrivalDate by Ships.arrivalDate
    var originPlanet by Planet referencedOn Ships.originPlanet

    companion object : IntEntityClass<Ship>(Ships)
}