package com.example.mygymroutine

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val sets: List<Set>
)
