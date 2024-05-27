package dev.bogwalk.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class HangarLog(
    val hangarId: Int,
    val txId: String,
    val instant: LocalDate
)