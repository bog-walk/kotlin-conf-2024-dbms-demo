package dev.bogwalk.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.models.HangarLog
import dev.bogwalk.models.PlanetInfo
import dev.bogwalk.models.ShipInfo
import dev.bogwalk.ui.style.largeDp
import dev.bogwalk.ui.style.mediumDp
import dev.bogwalk.ui.style.smallDp
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ShipOverview(
    ship: ShipInfo?,
    history: List<HangarLog>?,
    onAddShipRequest: () -> Unit,
    onPlanetSelected: (Long) -> Unit
) {
    Overview(
        isEmpty = ship == null,
        boxContent = {
            history?.let { logs ->
                Text(
                    text = logs.joinToString("\n", "Previous departures:\n") { "ID ${it.txId} on ${it.instant}" },
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart).padding(smallDp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    ) {
        if (ship == null) {
            Text(text = stringResource(Res.string.empty_docking_bay))
            Spacer(Modifier.height(mediumDp))
            TextButton(onClick = { onAddShipRequest() }) {
                Text(text = stringResource(Res.string.fill_docking_bay))
            }
        } else {
            DetailsRow(stringResource(Res.string.ship_license), ship.license, null)
            DetailsRow(stringResource(Res.string.ship_type), ship.type ?: stringResource(Res.string.ship_unclassified), null)
            DetailsRow(stringResource(Res.string.ship_arrival_date), ship.arrivalDate.toString(), null)
            DetailsRow(stringResource(Res.string.ship_origin_planet), ship.originPlanet.name) { onPlanetSelected(ship.originPlanet.id) }
        }
    }
}

@Composable
internal fun PlanetOverview(
    planet: PlanetInfo,
    ships: List<ShipInfo>?,
    onShipSelected: (Int) -> Unit
) {
    Overview {
        DetailsRow(stringResource(Res.string.planet_region), planet.region.output, null)
        DetailsRow(stringResource(Res.string.planet_distance), "${planet.distance} parsecs", null)
        DetailsRow(stringResource(Res.string.planet_prices), "${planet.prices.first} to ${planet.prices.last} credits", null)
        Spacer(Modifier.height(mediumDp))
        if (ships == null) {
            Text(text = stringResource(Res.string.empty_planet_ships), modifier = Modifier.padding(largeDp))
        } else {
            FilteredList(ships, false, Modifier, onShipSelected)
        }
    }
}

@Composable
private fun Overview(
    isEmpty: Boolean = false,
    boxContent: @Composable (BoxScope.() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = smallDp, end = smallDp, bottom = smallDp)
            .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
        contentAlignment = if (isEmpty) Alignment.Center else Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(mediumDp),
            verticalArrangement = if (isEmpty) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
        boxContent?.invoke(this)
    }
}

@Composable
private fun DetailsRow(
    label: String,
    details: String,
    onDetailsClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(smallDp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(0.2f),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.width(mediumDp))
        if (onDetailsClick == null) {
            Text(
                text = details,
                modifier = Modifier.weight(0.5f),
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            TextButton(
                onClick = { onDetailsClick() },
                modifier = Modifier.weight(0.5f),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = details,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}