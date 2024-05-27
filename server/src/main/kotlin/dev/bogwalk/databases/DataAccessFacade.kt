package dev.bogwalk.databases

import dev.bogwalk.databases.custom.isContainedBy
import dev.bogwalk.databases.tables.Planets
import dev.bogwalk.databases.tables.Ships
import dev.bogwalk.models.*
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.SqlExpressionBuilder.regexp

/**
 * Base representation of all data access operations for defining and manipulating planet and ship data.
 */
interface DataAccessFacade {
    /** Retrieves all stored planet records. */
    suspend fun allPlanets(): List<PlanetInfo>

    /** Retrieves a stored planet record by its [id], along with its referencing ship records, or `null` if none exists. */
    suspend fun planet(id: Long): PlanetWithShipsInfo?

    /** Inserts a new planet record. */
    suspend fun addNewPlanet(name: String, region: Region, distance: Double, prices: IntRange): Long

    /** Retrieves the maximum distance stored from all planet records. */
    suspend fun getMaxPlanetDistance(): Float

    /** Retrieves all stored ship records. */
    suspend fun allShips(): List<ShipInfo>

    /** Retrieves a stored ship record by its [id], or `null` if none exists. */
    suspend fun ship(id: Int): ShipInfo?

    /** Retrieves the log history for all deleted ship records. */
    suspend fun allShipsHistory(): List<HangarLog>

    /** Inserts a new ship record. */
    suspend fun addNewShip(id: Int, license: String, type: String?, arrivalDate: LocalDate, originPlanet: Long): Boolean

    /** Edits a ship record. */
    suspend fun editShip(id: Int, license: String, type: String?, arrivalDate: LocalDate, originPlanet: Long): Boolean

    /** Deletes a ship record. */
    suspend fun deleteShip(id: Int): Boolean

    /** Retrieves all stored ship records that match the provided conditions. */
    suspend fun filterShips(
        sortAscending: Boolean, name: String?, region: Region?, distanceLower: Double?, distanceUpper: Double?, prices: IntRange?,
        license: String?, shipType: String?, nullTypeOnly: Boolean, dateExact: LocalDate?, dateLower: LocalDate?, dateUpper: LocalDate?
    ): List<ShipInfo>

    fun getFilterExpressions(
        name: String?, region: Region?, distanceLower: Double?, distanceUpper: Double?, prices: IntRange?,
        license: String?, shipType: String?, nullTypeOnly: Boolean, dateExact: LocalDate?, dateLower: LocalDate?, dateUpper: LocalDate?
    ): List<Op<Boolean>> {
        return listOfNotNull(
            name?.let { Planets.name like "$name%" },
            region?.let { Planets.region eq it },
            distanceLower?.let {
                if (distanceUpper != null) Planets.distance.between(it, distanceUpper) else Planets.distance greaterEq it
            },
            distanceUpper?.let {
                if (distanceLower != null) null else Planets.distance lessEq it
            },
            prices?.let {
                Planets.prices isContainedBy prices
            },
            license?.let { Ships.license eq it },
            if (nullTypeOnly) Ships.type.isNull() else shipType?.let { Ships.type regexp it },
            dateExact?.let { Ships.arrivalDate eq it },
            dateLower?.let { Ships.arrivalDate greater it },
            dateUpper?.let { Ships.arrivalDate less it }
        )
    }
}