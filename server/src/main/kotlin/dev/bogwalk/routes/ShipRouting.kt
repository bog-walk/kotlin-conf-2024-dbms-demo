package dev.bogwalk.routes

import dev.bogwalk.databases.DataAccessFacade
import dev.bogwalk.models.Region
import dev.bogwalk.models.ShipInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.toLocalDate

fun Route.shipRouting(daf: DataAccessFacade) {
    get<Ships> {
        if (!call.request.queryParameters.isEmpty()) {
            val qp = call.request.queryParameters
            val sort = qp["asc"]!!.toBoolean()
            val name = qp["name"]
            val range = qp.getAll("prices")?.let { IntRange(it.first().toInt(), it.last().toInt()) }
            val region = qp["region"]?.let { r -> Region.valueOf(r) }
            val distanceLower = qp["distLower"]?.toDouble()
            val distanceUpper = qp["distUpper"]?.toDouble()
            val license = qp["license"]
            val shipType = qp["type"]
            val nullTypeOnly = qp["nullType"]!!.toBoolean()
            val dateExact = qp["dateExact"]?.toLocalDate()
            val dateLower = qp["dateLower"]?.toLocalDate()
            val dateUpper = qp["dateUpper"]?.toLocalDate()
            call.respond(
                daf.filterShips(
                    sort, name, region, distanceLower, distanceUpper, range,
                    license, shipType, nullTypeOnly, dateExact, dateLower, dateUpper
                )
            )
        } else {
            call.respond(daf.allShips())
        }
    }
    get<Ships.ShipId> { ship ->
        val result = daf.ship(ship.hangar_id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(result)
    }
    get<Ships.ShipHistory> {
        call.respond(daf.allShipsHistory())
    }
    post<Ships> {
        val toAdd = call.receive<ShipInfo>()
        if (daf.addNewShip(toAdd.hangarId, toAdd.license, toAdd.type, toAdd.arrivalDate, toAdd.originPlanet.id)) {
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.Conflict)
        }
    }
    put<Ships.ShipId> { ship ->
        val toUpdate = call.receive<ShipInfo>()
        if (daf.editShip(ship.hangar_id, toUpdate.license, toUpdate.type, toUpdate.arrivalDate, toUpdate.originPlanet.id)) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
    delete<Ships.ShipId> { ship ->
        if (daf.deleteShip(ship.hangar_id)) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}