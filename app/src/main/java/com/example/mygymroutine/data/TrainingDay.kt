package com.example.mygymroutine.data

import kotlinx.serialization.Serializable

@Serializable
data class TrainingDay(
    val dayName: String,
    val routineName: String = "",
    val exercises: List<Exercise> = emptyList()
)
