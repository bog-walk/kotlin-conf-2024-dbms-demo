package dev.bogwalk.ui.components

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.empty_filter_ships
import dev.bogwalk.common.generated.resources.ship_cd
import dev.bogwalk.models.ShipInfo
import dev.bogwalk.ui.style.filteredCardHeight
import dev.bogwalk.ui.style.largeDp
import dev.bogwalk.ui.style.mediumDp
import dev.bogwalk.ui.style.smallDp
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FilteredList(
    ships: List<ShipInfo>,
    displayPlanet: Boolean,
    modifier: Modifier? = null,
    onShipSelected: (Int) -> Unit
) {
    val scrollState = rememberLazyListState()

    Box(
        modifier = modifier ?: Modifier.fillMaxSize().padding(start = smallDp, end = smallDp, bottom = smallDp),
        contentAlignment = if (ships.isNotEmpty()) Alignment.TopCenter else Alignment.Center
    ) {
        if (ships.isNotEmpty()) {
            LazyColumn(
                state = scrollState,
            ) {
                itemsIndexed(
                    items = ships,
                    key = { index: Int, s: ShipInfo -> "$index${s.hangarId}" }
                ) { _, ship ->
                    FilteredShipCard(ship, displayPlanet, onShipSelected)
                }
            }
            VerticalScrollbar(
                adapter = ScrollbarAdapter(scrollState),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        } else {
            Text(text = stringResource(Res.string.empty_filter_ships), modifier = Modifier.padding(largeDp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FilteredShipCard(
    ship: ShipInfo,
    displayPlanet: Boolean,
    onShipSelected: (Int) -> Unit
) {
    var isInFocus by remember { mutableStateOf(false) }

    ElevatedCard(
        onClick = { onShipSelected(ship.hangarId) },
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(filteredCardHeight)
            .padding(smallDp)
            .onPointerEvent(PointerEventType.Enter) { isInFocus = true }
            .onPointerEvent(PointerEventType.Exit) { isInFocus = false },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Rounded.Flight,
                contentDescription = stringResource(Res.string.ship_cd),
                modifier = Modifier.padding(smallDp).size(largeDp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(smallDp))
            Text(
                text = ship.hangarId.toString(),
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.width(mediumDp))
            Text(
                text = "${ship.license}${if (displayPlanet) "  |  ${ship.originPlanet.name}" else ""}",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}