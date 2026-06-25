package com.example.mygymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private val weekRoutine = listOf(
        TrainingDay(
            dayName = "Monday",
            routine = Routine(
                exercises = emptyList()
            )
        ),
        TrainingDay(
            dayName = "Tuesday",
            routine = Routine(
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
                        )
                    )
                )
            )
        ),
        TrainingDay(
            dayName = "Wednesday",
            routine = Routine(
                exercises = emptyList()
            )
        ),
        TrainingDay(
            dayName = "Thursday",
            routine = Routine(
                exercises = emptyList()
            )
        ),
        TrainingDay(
            dayName = "Friday",
            routine = Routine(
                exercises = emptyList()
            )
        ),
        TrainingDay(
            dayName = "Saturday",
            routine = Routine(
                exercises = emptyList()
            )
        ),
        TrainingDay(
            dayName = "Sunday",
            routine = Routine(
                exercises = emptyList()
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppNavigation()
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "days"
        ) {
            composable("days") {
                GetDays(navController)
            }

            composable("detail/{day}") { backStackEntry ->
                val day = backStackEntry.arguments?.getString("day") ?: ""
                DetailScreen(weekRoutine.find { it.dayName == day } ?: TrainingDay(
                    "",
                    Routine(emptyList())
                ))
            }
        }
    }

    @Composable
    fun GetDays(navController: NavController) {

        val today = LocalDate.now().dayOfWeek.name
        val todayFormatted = today.lowercase().replaceFirstChar { it.uppercase() }

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight()
        ) {
            weekRoutine.forEach { day ->

                val isToday = day.dayName == todayFormatted

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(12.dp)
                        .clickable {
                            navController.navigate("detail/${day.dayName}")
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isToday) Color(0xFFB3E5FC) else Color.White
                    ),
                    border = if (isToday) BorderStroke(2.dp, Color.Blue) else BorderStroke(
                        1.dp,
                        Color.Black
                    ),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.dayName,
                            fontSize = 20.sp,
                            color = if (isToday) Color.Blue else Color.Black,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun DetailScreen(day: TrainingDay) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = day.dayName, fontSize = 28.sp)
            if (day.routine.exercises.isEmpty()) {
                Text(text = "No hay ejercicios programados para este día.", fontSize = 18.sp)
                return@Column
            } else {
                for (exercise in day.routine.exercises) {
                    Text(exercise.name)
                    for (set in exercise.sets) {
                        Text(text = "${set.reps}x${set.weight}Kg")
                    }
                }
            }
        }
    }
}


