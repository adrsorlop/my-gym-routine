package com.example.mygymroutine.ui.theme

import com.example.mygymroutine.data.Exercise
import com.example.mygymroutine.data.Set
import com.example.mygymroutine.data.TrainingDay

var weekRoutine = listOf(
    TrainingDay(
        dayName = "Monday"
    ),
    TrainingDay(
        dayName = "Tuesday",
        routineName = "Día de pierna",
        exercises = listOf(
            Exercise(
                name = "Sentadillas",
                sets = listOf(
                    Set(
                        reps = 12,
                        weight = 80
                    ),
                    Set(
                        reps = 10,
                        weight = 80
                    ),
                    Set(
                        reps = 8,
                        weight = 60
                    ),
                    Set(
                        reps = 8,
                        weight = 60
                    ),
                ),
                coolDown = 90
            ),
            Exercise(
                name = "Curl femoral",
                sets = listOf(
                    Set(
                        reps = 12,
                        weight = 60
                    ),
                    Set(
                        reps = 10,
                        weight = 60
                    ),
                    Set(
                        reps = 8,
                        weight = 50
                    )
                ),
                coolDown = 60
            ),
        )

    ),
    TrainingDay(
        dayName = "Wednesday"
    ),
    TrainingDay(
        dayName = "Thursday"
    ),
    TrainingDay(
        dayName = "Friday"
    ),
    TrainingDay(
        dayName = "Saturday"
    ),
    TrainingDay(
        dayName = "Sunday"
    )
)
val exercises = listOf<String>("Sentadillas", "Press de banca", "Curl de bíceps")