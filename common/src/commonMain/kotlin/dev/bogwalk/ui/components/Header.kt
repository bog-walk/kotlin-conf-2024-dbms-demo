package dev.bogwalk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.docking_bay
import dev.bogwalk.common.generated.resources.filter_cd
import dev.bogwalk.common.generated.resources.title
import dev.bogwalk.models.MainState
import dev.bogwalk.models.PlanetInfo
import dev.bogwalk.models.ShipInfo
import dev.bogwalk.ui.style.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Header(
    mainState: MainState,
    currentHangar: Int?,
    currentShip: ShipInfo?,
    currentPlanet: PlanetInfo?,
    onReturnRequest: () -> Unit,
    onFilterRequest: () -> Unit,
    onSortOrderSelected: (Boolean) -> Unit,
    onHistoryRequest: () -> Unit,
    onEditRequest: () -> Unit,
    onDeleteRequest: () -> Unit
) {
    var sortOptionsExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth().requiredHeight(headerHeight),
        contentAlignment = Alignment.Center
    ) {
        if (mainState != MainState.ALL_SHIPS) {
            BackButton(Modifier.align(Alignment.TopStart), onReturnRequest)
        }

        Text(
            text = when (mainState) {
                MainState.ALL_SHIPS, MainState.FILTERING, MainState.SHIP_LIST -> stringResource(Res.string.title)
                MainState.PLANET_OVERVIEW -> currentPlanet!!.name
                else -> "${stringResource(Res.string.docking_bay)} $currentHangar"
            },
            modifier = Modifier.fillMaxSize().padding(top = mediumDp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge
        )

        Row(
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = largeDp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (mainState) {
                MainState.ALL_SHIPS -> OptionButton(Icons.Rounded.FilterList, stringResource(Res.string.filter_cd), true, onFilterRequest)
                MainState.SHIP_OVERVIEW -> {
                    OptionButton(Icons.Rounded.QuestionMark, stringResource(Res.string.question_cd), true, onHistoryRequest)
                    OptionButton(Icons.Rounded.Edit, stringResource(Res.string.edit_cd), currentShip != null, onEditRequest)
                    OptionButton(Icons.Rounded.Delete, stringResource(Res.string.delete_cd), currentShip != null, onDeleteRequest)
                }
                MainState.FILTERING -> {
                    OptionButton(Icons.AutoMirrored.Rounded.Sort, stringResource(Res.string.sort_cd), true) { sortOptionsExpanded = true }
                    DropdownMenu(
                        expanded = sortOptionsExpanded,
                        onDismissRequest = { sortOptionsExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("${stringResource(Res.string.docking_bay)} A-Z") },
                            onClick = {
                                sortOptionsExpanded = false
                                onSortOrderSelected(true)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("${stringResource(Res.string.docking_bay)} Z-A") },
                            onClick = {
                                sortOptionsExpanded = false
                                onSortOrderSelected(false)
                            }
                        )
                    }
                }
                else -> {}
            }
        }
    }
}