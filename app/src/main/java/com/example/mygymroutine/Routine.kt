package com.example.mygymroutine

import kotlinx.serialization.Serializable

@Serializable
data class Routine(
    val exercises: List<Exercise>
)
