package dev.bogwalk.models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class TemporaryShipInfo(
    val hangarId: Int,
    val license: String?,
    val type: String?,
    val arrivalDate: LocalDate?,
    val originPlanet: PlanetInfo?
)

data class FilterConditions(
    val planetName: String?,
    val region: Region?,
    val distanceLowerBound: Double?,
    val distanceUpperBound: Double?,
    val prices: IntRange?,
    val license: String?,
    val shipType: String?,
    val nullTypeOnly: Boolean,
    val dateExact: LocalDate?,
    val dateLowerBound: LocalDate?,
    val dateUpperBound: LocalDate?
)

data class GridScrollPosition(
    val firstVisibleItemIndex: Int,
    val firstVisibleItemScrollOffset: Int
)

fun currentDate(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date

const val MAX_PLANET_DISTANCE = 60000f