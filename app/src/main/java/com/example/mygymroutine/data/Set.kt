package com.example.mygymroutine.data

import kotlinx.serialization.Serializable

@Serializable
data class Set(
    val reps: Int,
    val weight: Int
)
