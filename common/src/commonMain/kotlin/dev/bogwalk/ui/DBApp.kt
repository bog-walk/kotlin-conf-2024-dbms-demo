package dev.bogwalk.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import dev.bogwalk.client.StateHandler
import dev.bogwalk.models.MainState
import dev.bogwalk.ui.components.*
import dev.bogwalk.ui.components.forms.FilterForm
import dev.bogwalk.ui.components.forms.PlanetForm
import dev.bogwalk.ui.components.forms.ShipForm

@Composable
fun DBApp(state: StateHandler) {
    Column {
        Header(
            state.mainScreenState,
            state.currentHangar,
            state.currentShip,
            state.currentPlanet,
            state::handleBackButton,
            state::showFilterScreen,
            state::changeFilterSortOrder,
            state::toggleHangarHistoryVisibility,
            state::showEditShipScreen,
            state::openDeleteDialog
        )
        if (state.isLoadingData) {
            LoadingBar()
        } else {
            when (state.mainScreenState) {
                MainState.ALL_SHIPS -> GridList(state.shipCache.map { it.hangarId }, state.hangarGridState, state::rememberGridState) { state.showShip(it) }
                MainState.SHIP_OVERVIEW -> ShipOverview(state.currentShip, state.currentHistory, state::showAddNewShipScreen) { state.showPlanet(it) }
                MainState.ADDING_SHIP -> ShipForm(state.currentHangar!!, state.currentShip, state.tempEditShip, state.planetCache, state::showAddNewPlanetScreen) {
                    state.addNewShip(it)
                }
                MainState.ADDING_PLANET -> PlanetForm { state.addNewPlanet(it) }
                MainState.UPDATING_SHIP -> ShipForm(state.currentHangar!!, state.currentShip, null, state.planetCache, null) {
                    state.editShip(it)
                }
                MainState.FILTERING -> FilterForm(state.planetCache, state.maxPlanetDistance) { state.filterShips(state.sortFilterAscending, it) }
                MainState.SHIP_LIST -> FilteredList(state.filterShipCache, true) { state.showShip(it) }
                MainState.PLANET_OVERVIEW -> PlanetOverview(state.currentPlanet!!, state.filterShipCache) { state.showShip(it) }
            }
        }
    }
}