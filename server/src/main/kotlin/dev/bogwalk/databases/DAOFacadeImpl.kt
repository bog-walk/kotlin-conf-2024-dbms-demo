package dev.bogwalk.databases

import dev.bogwalk.databases.custom.getMaxDistance
import dev.bogwalk.databases.tables.*
import dev.bogwalk.models.*
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.compoundAnd
import org.jetbrains.exposed.sql.selectAll

/**
 * Represents all data access operations using the DAO approach.
 */
class DAOFacadeImpl(private val db: DatabaseFactory) : DataAccessFacade {
    override suspend fun allPlanets(): List<PlanetInfo> = db.query {
        Planet
            .all()
            .map(Converters::toPlanetInfo)
    }

    override suspend fun planet(id: Long): PlanetWithShipsInfo? = db.query {
        Planet
            .findById(id)
            ?.let {
                PlanetWithShipsInfo(
                    Converters.toPlanetInfo(it),
                    it.ships.map(Converters::toShipInfo)
                )
            }
    }

    override suspend fun addNewPlanet(
        name: String,
        region: Region,
        distance: Double,
        prices: IntRange
    ): Long = db.query {
        Planet
            .new {
                this.name = name
                this.region = region
                this.distance = distance
                this.prices = prices
            }.id.value
    }

    override suspend fun getMaxPlanetDistance(): Float = db.query { getMaxDistance(0) }

    override suspend fun allShips(): List<ShipInfo> = db.query {
        Ship
            .all()
            .orderBy(Ships.id to SortOrder.ASC)
            .map(Converters::toShipInfo)
    }

    override suspend fun ship(id: Int): ShipInfo? = db.query {
        Ship
            .findById(id)
            ?.let {
                Converters.toShipInfo(it)
            }
    }

    override suspend fun allShipsHistory(): List<HangarLog> = db.query {
        HangarLogs
            .selectAll()
            .groupBy(HangarLogs.hangarId, HangarLogs.transactionId)
            .orderBy(HangarLogs.departed, SortOrder.DESC)
            .map(Converters::rowToHangarLog)
    }

    override suspend fun addNewShip(id: Int, license: String, type: String?, arrivalDate: LocalDate, originPlanet: Long): Boolean = db.query {
        Ship
            .new(id) {
                this.license = license
                this.type = type
                this.arrivalDate = arrivalDate
                this.originPlanet = Planet.findById(originPlanet)!!
            }.id.value == id
    }

    override suspend fun editShip(id: Int, license: String, type: String?, arrivalDate: LocalDate, originPlanet: Long): Boolean = db.query {
        val entity = Ship.findById(id)
        entity?.license = license
        entity?.type = type
        entity?.arrivalDate = arrivalDate
        entity?.originPlanet = Planet.findById(originPlanet)!!
        entity != null
    }

    override suspend fun deleteShip(id: Int): Boolean = db.query {
        val entity = Ship.findById(id)
        entity?.delete()
        entity == null || Ship.testCache(entity.id) == null
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