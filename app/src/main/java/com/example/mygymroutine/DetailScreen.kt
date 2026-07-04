package com.example.mygymroutine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mygymroutine.data.Exercise
import com.example.mygymroutine.data.TrainingDay

@Composable
fun DetailScreen(day: TrainingDay) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(text = day.dayName, fontSize = 28.sp)
        if (day.exercises.isEmpty()) {
            Text(text = "No hay ejercicios programados para este día.", fontSize = 18.sp)
            return@Column
        } else {
            for (exercise in day.exercises) {
                DetailExercise(exercise)
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
                .fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                "Descanso: ${exercise.coolDown} segundos",
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }

}