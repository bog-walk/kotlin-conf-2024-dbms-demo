package dev.bogwalk.client

import dev.bogwalk.models.FilterConditions
import dev.bogwalk.models.PlanetInfo
import dev.bogwalk.models.ShipInfo

/**
 * Base representation of all client-side operations for defining and manipulating planet and ship data.
 */
interface Client {
    /** Shuts down the server and closes the underlying HttpClient engine. */
    fun cleanUp()

    /** Loads any existing database records into the app's [StateHandler] caches. */
    suspend fun loadSavedData()

    /** Retrieves a planet's details by its [id]. */
    fun showPlanet(id: Long)

    /** Adds a new planet. */
    fun addNewPlanet(planet: PlanetInfo)

    /** Retrieves a ship's details by its [id]. */
    fun showShip(id: Int)

    /** Filters ships by the specified [conditions]. */
    fun filterShips(ascending: Boolean, conditions: FilterConditions)

    /** Adds a new ship. */
    fun addNewShip(ship: ShipInfo)

    /** Edits a ship's details. */
    fun editShip(updated: ShipInfo)

    /** Deletes a ship. */
    fun deleteShip()
}