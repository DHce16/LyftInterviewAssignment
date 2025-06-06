package com.example.lyftinterviewassignment.model
import java.time.LocalDate

enum class SightingType { BLOB, LAMPSHADE }

data class UFOSighting(
    val id: Long,
    val date: String,
    val type: SightingType,
    val speed: Int
)
