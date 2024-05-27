package dev.bogwalk.client

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.bogwalk.models.*

abstract class StateHandler : Client {
    var mainScreenState by mutableStateOf(MainState.ALL_SHIPS)

    var isLoadingData by mutableStateOf(true)
    var planetCache by mutableStateOf(emptyList<PlanetInfo>())
    var shipCache by mutableStateOf(emptyList<ShipInfo>())
    var hangarHistoryCache by mutableStateOf(emptyList<HangarLog>())

    var currentHangar by mutableStateOf<Int?>(null)
    var currentShip by mutableStateOf<ShipInfo?>(null)
    var currentPlanet by mutableStateOf<PlanetInfo?>(null)
    var currentHistory by mutableStateOf<List<HangarLog>?>(null)
    var hangarGridState by mutableStateOf(GridScrollPosition(0, 0))

    var tempEditShip by mutableStateOf<TemporaryShipInfo?>(null)
    var filterShipCache by mutableStateOf(emptyList<ShipInfo>())
    var sortFilterAscending by mutableStateOf(true)
    var maxPlanetDistance by mutableStateOf(60000f)

    var isDeleteDialogOpen by mutableStateOf(false)
    var isWarningDialogOpen by mutableStateOf(false)

    fun rememberGridState(newState: GridScrollPosition) {
        hangarGridState = newState
    }

    fun showAddNewShipScreen() {
        currentHistory = null
        mainScreenState = MainState.ADDING_SHIP
    }

    fun showAddNewPlanetScreen(tempShip: TemporaryShipInfo?) {
        tempEditShip = tempShip
        mainScreenState = MainState.ADDING_PLANET
    }

    fun showEditShipScreen() {
        currentHistory = null
        mainScreenState = MainState.UPDATING_SHIP
    }

    fun openDeleteDialog() {
        isDeleteDialogOpen = true
    }

    fun closeDeleteDialog() {
        isDeleteDialogOpen = false
    }

    fun openWarningDialog() {
        isWarningDialogOpen = true
    }

    fun closeWarningDialog() {
        isWarningDialogOpen = false
    }

    fun toggleHangarHistoryVisibility() {
        currentHistory = if (currentHistory == null) {
            hangarHistoryCache.filter { it.hangarId == currentHangar }
        } else {
           null
        }
    }

    fun confirmLeaveForm() {
        mainScreenState = when (mainScreenState) {
            MainState.ADDING_PLANET -> MainState.ADDING_SHIP
            MainState.ADDING_SHIP -> {
                tempEditShip = null
                MainState.SHIP_OVERVIEW
            }
            MainState.UPDATING_SHIP -> MainState.SHIP_OVERVIEW
            else -> mainScreenState
        }
        closeWarningDialog()
    }

    fun handleBackButton() {
        when (mainScreenState) {
            MainState.SHIP_LIST, MainState.FILTERING -> {
                currentHangar = null
                currentShip = null
                currentPlanet = null
                filterShipCache = emptyList()
                mainScreenState = MainState.ALL_SHIPS
            }
            MainState.SHIP_OVERVIEW-> {
                currentHangar = null
                currentShip = null
                currentHistory = null
                if (filterShipCache.isEmpty()) {
                    currentPlanet = null
                    mainScreenState = MainState.ALL_SHIPS
                } else {
                    mainScreenState = if (currentPlanet == null) MainState.SHIP_LIST else MainState.PLANET_OVERVIEW
                }
            }
            MainState.PLANET_OVERVIEW -> {
                filterShipCache = emptyList()
                mainScreenState = if (currentHangar == null) {
                    MainState.ALL_SHIPS
                } else {
                    MainState.SHIP_OVERVIEW
                }
            }
            MainState.ADDING_PLANET, MainState.ADDING_SHIP, MainState.UPDATING_SHIP -> openWarningDialog()
            MainState.ALL_SHIPS -> {}
        }
    }

    fun showFilterScreen() {
        mainScreenState = MainState.FILTERING
    }

    fun changeFilterSortOrder(isAscending: Boolean) {
        sortFilterAscending = isAscending
    }
}