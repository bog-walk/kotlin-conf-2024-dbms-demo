package dev.bogwalk

import dev.bogwalk.plugins.configureDatabase
import dev.bogwalk.plugins.configureRouting
import dev.bogwalk.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    val daf = configureDatabase()

    configureSerialization()
    configureRouting(daf)
}