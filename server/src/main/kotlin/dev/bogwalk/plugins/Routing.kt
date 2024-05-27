package dev.bogwalk.plugins

import dev.bogwalk.databases.DataAccessFacade
import dev.bogwalk.routes.planetRouting
import dev.bogwalk.routes.shipRouting
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Application.configureRouting(daf: DataAccessFacade) {
    install(Resources)

    routing {
        planetRouting(daf)
        shipRouting(daf)
    }
}