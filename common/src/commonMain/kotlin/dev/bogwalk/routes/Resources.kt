package dev.bogwalk.routes

import dev.bogwalk.models.IntRangeSerializer
import dev.bogwalk.models.Region
import io.ktor.resources.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
@Resource(Routes.ALL_SHIPS)
class Ships(
    val asc: Boolean = true,
    val name: String? = null,
    val region: Region? = null,
    val distLower: Double? = null,
    val distUpper: Double? = null,
    @Serializable(with = IntRangeSerializer::class)
    val prices: IntRange? = null,
    val license: String? = null,
    val type: String? = null,
    val nullType: Boolean = false,
    val dateExact: LocalDate? = null,
    val dateLower: LocalDate? = null,
    val dateUpper: LocalDate? = null
) {
    @Serializable
    @Resource(Routes.HISTORY)
    class ShipHistory(val parent: Ships = Ships())
    @Serializable
    @Resource(Routes.BY_SHIP_ID)
    class ShipId(val parent: Ships = Ships(), val hangar_id: Int)
}

@Serializable
@Resource(Routes.ALL_PLANETS)
class Planets {
    @Serializable
    @Resource(Routes.MAX_DISTANCE)
    class PlanetDistance(val parent: Planets = Planets())
    @Serializable
    @Resource(Routes.BY_PLANET_ID)
    class PlanetId(val parent: Planets = Planets(), val p_id: Long)
}

@Serializable
@Resource(Routes.SHUTDOWN)
class ServerShutdown