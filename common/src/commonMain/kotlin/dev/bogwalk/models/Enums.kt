package dev.bogwalk.models

enum class Region(val output: String) {
    DEEP_CORE("Deep Core"),
    CORE_WORLDS("Core Worlds"),
    INNER_RIM("Inner Rim"),
    MID_RIM("Mid Rim"),
    OUTER_RIM("Outer Rim"),
    UNKNOWN_REGIONS("Unknown Regions")
}

enum class MainState {
    ALL_SHIPS,
    SHIP_OVERVIEW,
    PLANET_OVERVIEW,
    ADDING_SHIP,
    UPDATING_SHIP,
    ADDING_PLANET,
    FILTERING,
    SHIP_LIST
}

enum class DateOption(val text: String) {
    EQUALS("is equal to"),
    GREATER("is after"),
    LESSER("is before")
}