package com.example.mygymroutine

import kotlinx.serialization.Serializable

@Serializable
data class TrainingDay(
    val dayName: String,
    val routine: Routine
)
