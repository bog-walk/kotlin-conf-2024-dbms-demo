package dev.bogwalk.routes

import dev.bogwalk.databases.DataAccessFacade
import dev.bogwalk.models.PlanetInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.planetRouting(daf: DataAccessFacade) {
    get<Planets> {
        call.respond(daf.allPlanets())
    }
    get<Planets.PlanetDistance> {
        call.respond(daf.getMaxPlanetDistance())
    }
    get<Planets.PlanetId> { planet ->
        val result = daf.planet(planet.p_id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(result)
    }
    post<Planets> {
        val toAdd = call.receive<PlanetInfo>()
        val newPlanet = daf.addNewPlanet(toAdd.name, toAdd.region, toAdd.distance, toAdd.prices)
        call.respond(HttpStatusCode.Created, newPlanet)
    }
}