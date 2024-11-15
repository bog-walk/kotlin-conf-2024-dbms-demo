package dev.bogwalk.plugins

import dev.bogwalk.databases.DAOFacadeImpl
import dev.bogwalk.databases.DSLFacadeImpl
import dev.bogwalk.databases.DataAccessFacade
import dev.bogwalk.databases.DatabaseFactoryImpl
import io.ktor.server.application.*

fun Application.configureDatabase(): DataAccessFacade {
    val db = DatabaseFactoryImpl.apply { init(environment.config) }
    db.connect()

    val approach = environment.config.property("storage.dataAccess").getString()
    return when (DataAccessApproach.valueOf(approach)) {
        DataAccessApproach.DSL -> DSLFacadeImpl(db)
        DataAccessApproach.DAO -> DAOFacadeImpl(db)
    }
}

enum class DataAccessApproach {
    DSL,
    DAO
}