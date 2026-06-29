package com.example.mygymroutine.data

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val sets: List<Set>,
    val coolDown: Int = 0
)
