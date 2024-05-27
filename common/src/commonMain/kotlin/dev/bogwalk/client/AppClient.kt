package dev.bogwalk.client

import dev.bogwalk.models.*
import dev.bogwalk.routes.Planets
import dev.bogwalk.routes.ServerShutdown
import dev.bogwalk.routes.Ships
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*

class AppClient(
    private val coroutineScope: CoroutineScope
) : StateHandler() {
    private val client = HttpClient(Java) {
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            host = "0.0.0.0"
            port = 8080
            url { protocol = URLProtocol.HTTP }
        }
        expectSuccess = true
    }

    override fun cleanUp() {
        coroutineScope.launch {
            client.get(ServerShutdown())
            client.close()
        }
    }

    override suspend fun loadSavedData() {
        planetCache = client.get(Planets()).body()
        shipCache = client.get(Ships()).body()
        hangarHistoryCache = client.get(Ships.ShipHistory()).body()
    }

    override fun showPlanet(id: Long) {
        coroutineScope.launch {
            try {
                val result = client.get(Planets.PlanetId(p_id = id)).body<PlanetWithShipsInfo?>()
                currentPlanet = result?.planet
                filterShipCache = result?.starships ?: emptyList()
                mainScreenState = MainState.PLANET_OVERVIEW
            } catch (ex: ClientRequestException) {
                println(ex.message)
            }
        }
    }

    override fun addNewPlanet(planet: PlanetInfo) {
        coroutineScope.launch {
            try {
                val newId = client.post(Planets()) {
                    contentType(ContentType.Application.Json)
                    setBody(planet)
                }.body<Long>()
                maxPlanetDistance = client.get(Planets.PlanetDistance()).body()
                currentPlanet = planet.copy(id = newId)
                planetCache = listOf(currentPlanet!!) + planetCache
                tempEditShip = tempEditShip?.copy(originPlanet = currentPlanet)
                mainScreenState = MainState.ADDING_SHIP
            } catch (ex: ClientRequestException) {
                println(ex.message)
            }
        }
    }

    override fun showShip(id: Int) {
        coroutineScope.launch {
            try {
                currentHangar = id
                currentShip = shipCache.firstOrNull { it.hangarId == id }
                tempEditShip = null
                mainScreenState = MainState.SHIP_OVERVIEW
            } catch (ex: ClientRequestException) {
                println(ex.message)
            }
        }
    }

    override fun filterShips(ascending: Boolean, conditions: FilterConditions) {
        coroutineScope.launch {
            try {
                val queryRoute = Ships(
                    ascending,
                    conditions.planetName, conditions.region, conditions.distanceLowerBound, conditions.distanceUpperBound, conditions.prices,
                    conditions.license, conditions.shipType, conditions.nullTypeOnly, conditions.dateExact, conditions.dateLowerBound, conditions.dateUpperBound
                )
                filterShipCache = client.get(queryRoute).body()
                mainScreenState = MainState.SHIP_LIST
            } catch (ex: ClientRequestException) {
                println(ex.message)
            }
        }
    }

    override fun addNewShip(ship: ShipInfo) {
        coroutineScope.launch {
            try {
                client.post(Ships()) {
                    contentType(ContentType.Application.Json)
                    setBody(ship)
                }
                currentHangar = ship.hangarId
                currentShip = ship
                shipCache = listOf(currentShip!!) + shipCache
                mainScreenState = MainState.SHIP_OVERVIEW
            } catch (ex: ClientRequestException) {
                println(ex.message)
            }
        }
    }

    override fun editShip(updated: ShipInfo) {
        coroutineScope.launch {
            try {
                currentShip?.let {
                    client.put(Ships.ShipId(hangar_id = updated.hangarId)) {
                        contentType(ContentType.Application.Json)
                        setBody(updated)
                    }
                    currentHangar = updated.hangarId
                    currentShip = updated
                    shipCache = listOf(updated) + (shipCache - it)
                    if (it in filterShipCache) {
                        filterShipCache = listOf(updated) + (filterShipCache - it)
                    }
                    mainScreenState = MainState.SHIP_OVERVIEW
                }
            } catch (ex: ClientRequestException) {
                println(ex.message)
            }
        }
    }

    override fun deleteShip() {
        coroutineScope.launch {
            currentShip?.let {
                try {
                    client.delete(Ships.ShipId(hangar_id = it.hangarId))
                    hangarHistoryCache = client.get(Ships.ShipHistory()).body()
                    currentHangar = it.hangarId
                    currentShip = null
                    currentPlanet = null
                    shipCache -= it
                    filterShipCache -= it
                    mainScreenState = MainState.SHIP_OVERVIEW
                } catch (ex: ClientRequestException) {
                    println(ex.message)
                }
            }
        }
        closeDeleteDialog()
    }
}