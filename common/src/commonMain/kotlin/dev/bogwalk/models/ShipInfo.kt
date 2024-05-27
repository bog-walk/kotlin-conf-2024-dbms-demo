package dev.bogwalk.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ShipInfo(
    val hangarId: Int,
    val license: String,
    val type: String?,
    val arrivalDate: LocalDate,
    val originPlanet: PlanetInfo
)
