package dev.bogwalk.databases

import dev.bogwalk.databases.custom.getMaxDistance
import dev.bogwalk.databases.tables.HangarLogs
import dev.bogwalk.databases.tables.Planets
import dev.bogwalk.databases.tables.Ships
import dev.bogwalk.models.*
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

/**
 * Represents all data access operations using the DSL approach.
 */
class DSLFacadeImpl(private val db: DatabaseFactory) : DataAccessFacade {
    override suspend fun allPlanets(): List<PlanetInfo> = db.query {
        Planets
            .selectAll()
            .map(Converters::rowToPlanetInfo)
    }

    override suspend fun planet(id: Long): PlanetWithShipsInfo? = db.query {
        val results = (Planets leftJoin Ships)
            .selectAll()
            .where { Planets.id eq id }
            .toList()
        results
            .ifEmpty { null }
            ?.let { rr ->
                PlanetWithShipsInfo(
                    Converters.rowToPlanetInfo(results.first()),
                    rr.mapNotNull {
                        if (it[Ships.id] == null) null else Converters.rowToShipInfo(it)
                    }
                )
            }
    }

    override suspend fun addNewPlanet(
        name: String,
        region: Region,
        distance: Double,
        prices: IntRange
    ): Long = db.query {
        Planets.insertAndGetId {
            it[Planets.name] = name
            it[Planets.region] = region
            it[Planets.distance] = distance
            it[Planets.prices] = prices
        }.value
    }

    override suspend fun getMaxPlanetDistance(): Float = db.query { getMaxDistance(0) }

    override suspend fun allShips(): List<ShipInfo> = db.query {
        (Ships leftJoin Planets)
            .selectAll()
            .groupBy(Planets.id, Ships.id)
            .orderBy(Ships.id, SortOrder.ASC)
            .map(Converters::rowToShipInfo)
    }

    override suspend fun ship(id: Int): ShipInfo? = db.query {
        (Ships leftJoin Planets)
            .selectAll()
            .where { Ships.id eq id }
            .map(Converters::rowToShipInfo)
            .singleOrNull()
    }

    override suspend fun allShipsHistory(): List<HangarLog> = db.query {
        HangarLogs
            .selectAll()
            .groupBy(HangarLogs.hangarId, HangarLogs.transactionId)
            .orderBy(HangarLogs.departed, SortOrder.DESC)
            .map(Converters::rowToHangarLog)
    }

    override suspend fun addNewShip(
        id: Int,
        license: String,
        type: String?,
        arrivalDate: LocalDate,
        originPlanet: Long
    ): Boolean = db.query {
        val insert = Ships.insert {
            it[Ships.id] = id
            it[Ships.license] = license
            it[Ships.type] = type
            it[Ships.arrivalDate] = arrivalDate
            it[Ships.originPlanet] = originPlanet
        }
        insert.insertedCount == 1
    }

    override suspend fun editShip(
        id: Int,
        license: String,
        type: String?,
        arrivalDate: LocalDate,
        originPlanet: Long
    ): Boolean = db.query {
        Ships.update({ Ships.id eq id }) {
            it[Ships.license] = license
            it[Ships.type] = type
            it[Ships.arrivalDate] = arrivalDate
            it[Ships.originPlanet] = originPlanet
        } == 1
    }

    override suspend fun deleteShip(id: Int): Boolean = db.query {
        Ships.deleteWhere {
            Ships.id eq id
        } == 1
    }

    override suspend fun filterShips(
        sortAscending: Boolean, name: String?, region: Region?, distanceLower: Double?, distanceUpper: Double?, prices: IntRange?,
        license: String?, shipType: String?, nullTypeOnly: Boolean, dateExact: LocalDate?, dateLower: LocalDate?, dateUpper: LocalDate?
    ): List<ShipInfo> = db.query {
        val conditions  = getFilterExpressions(
            name, region, distanceLower, distanceUpper, prices, license, shipType, nullTypeOnly, dateExact, dateLower, dateUpper
        )
        if (conditions.isEmpty()) {
            allShips()
        } else {
            (Ships leftJoin Planets)
                .selectAll()
                .where { conditions.compoundAnd() }
                .groupBy(Planets.id, Ships.id)
                .orderBy(Ships.id, if (sortAscending) SortOrder.ASC else SortOrder.DESC)
                .map(Converters::rowToShipInfo)
        }
    }
}