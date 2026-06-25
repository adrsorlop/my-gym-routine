package com.example.mygymroutine

import kotlinx.serialization.Serializable

@Serializable
data class Set(
    val reps: Int,
    val weight: Int
)
