package com.example.mygymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygymroutine.data.TrainingDay
import com.example.mygymroutine.ui.theme.weekRoutine
import java.time.LocalDate

class MainActivity : ComponentActivity() {

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
                    "",
                    emptyList(),
                ))
            }

            composable("createRoutine") {
                CreateRoutineScreen(navController)
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
        ) {
            weekRoutine.forEach { day ->

                val isToday = day.dayName == todayFormatted

                val detailText: String = if (day.exercises.isNotEmpty()) {
                    if (day.exercises.size == 1) {
                        "${day.exercises.size} ejercicio"
                    } else {
                        "${day.exercises.size} ejercicios"
                    }
                } else {
                    "No hay ejercicios programados"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
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
                        contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row() {
                                Column() {
                                    Text(
                                        text = day.dayName,
                                        fontSize = 20.sp,
                                        color = if (isToday) Color.Blue else Color.Black,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                                if (day.exercises.isNotEmpty()) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Card(
                                            modifier = Modifier.border(
                                                1.dp,
                                                if (isToday) Color.Blue else Color.Black,
                                                shape = MaterialTheme.shapes.small
                                            ), colors = CardDefaults.cardColors(
                                                containerColor = if (isToday) Color.White else MaterialTheme.colorScheme.surface
                                            )
                                        ) {
                                            Text(
                                                text = day.routineName,
                                                fontSize = 20.sp,
                                                color = if (isToday) Color.Blue else Color.Black,
                                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }
                                    }

                                }
                            }

                            Text(text = detailText, fontSize = 16.sp)
                        }
                    }
                }
            }
            Button(
                onClick = { navController.navigate("createRoutine") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(text = "Crear rutina")
            }
        }
    }
}
