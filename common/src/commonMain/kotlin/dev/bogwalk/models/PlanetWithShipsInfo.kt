package dev.bogwalk.models

import kotlinx.serialization.Serializable

@Serializable
data class PlanetWithShipsInfo(
    val planet: PlanetInfo,
    val starships: List<ShipInfo>
)
