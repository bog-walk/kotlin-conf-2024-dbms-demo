package dev.bogwalk.databases

import dev.bogwalk.databases.tables.*
import dev.bogwalk.models.HangarLog
import dev.bogwalk.models.PlanetInfo
import dev.bogwalk.models.ShipInfo
import org.jetbrains.exposed.sql.ResultRow

internal object Converters {
    fun rowToPlanetInfo(result: ResultRow): PlanetInfo = PlanetInfo(
        result[Planets.id].value,
        result[Planets.name],
        result[Planets.region],
        result[Planets.distance],
        result[Planets.prices]
    )

    fun toPlanetInfo(planet: Planet): PlanetInfo = PlanetInfo(
        planet.id.value,
        planet.name,
        planet.region,
        planet.distance,
        planet.prices
    )

    fun rowToShipInfo(result: ResultRow): ShipInfo = ShipInfo(
        result[Ships.id].value,
        result[Ships.license],
        result[Ships.type],
        result[Ships.arrivalDate],
        rowToPlanetInfo(result)
    )

    fun toShipInfo(ship: Ship): ShipInfo = ShipInfo(
        ship.id.value,
        ship.license,
        ship.type,
        ship.arrivalDate,
        toPlanetInfo(ship.originPlanet)
    )

    fun rowToHangarLog(result: ResultRow): HangarLog = HangarLog(
        result[HangarLogs.hangarId],
        result[HangarLogs.transactionId],
        result[HangarLogs.departed]
    )
}