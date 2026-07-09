package com.example.mygymroutine

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygymroutine.data.Exercise
import com.example.mygymroutine.data.TrainingDay
import com.example.mygymroutine.data.WeekRoutineRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun DetailScreen(day: TrainingDay, navController: NavController) {

    val context = LocalContext.current

    val vm = viewModel<RoutineViewModel>(
        factory = RoutineViewModelFactory(
            WeekRoutineRepository(context)
        )
    )

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxHeight()
    ) {
        Card(
            modifier = Modifier
                .padding(18.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = day.dayName, fontSize = 28.sp, modifier = Modifier.padding(12.dp))
        }

        if (day.exercises.isEmpty()) {
            Text(text = "No hay ejercicios programados para este día.", fontSize = 18.sp)
            return@Column
        } else {
            for (exercise in day.exercises) {
                DetailExercise(exercise)
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {
                    vm.saveTrainingDay(TrainingDay(
                        dayName = day.dayName,
                        routineName = "",
                        exercises = emptyList()
                    ))
                    navController.navigate("days")
                },
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
            OutlinedButton(
                onClick = {
                    navController.navigate("days")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver")
            }
            Button(
                onClick = {
                    val json = Json.encodeToString(day)
                    val encoded = Uri.encode(json)
                    navController.navigate("createRoutine?day=$encoded")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Editar")
            }
        }
    }
}

@Composable
fun DetailExercise(exercise: Exercise) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Text(exercise.name, modifier = Modifier.padding(8.dp), fontSize = 18.sp)
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            exercise.sets.forEach { set ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("${set.reps}")
                        Text("x")
                        Text("${set.weight} Kg")
                    }
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Card(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.CenterHorizontally), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row() {
                Icon(
                    Icons.Default.Timer,
                    contentDescription = "Add set",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    "Descanso: ${exercise.coolDown} segundos",
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}