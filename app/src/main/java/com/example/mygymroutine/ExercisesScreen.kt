package com.example.mygymroutine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygymroutine.data.exercises

@Composable
fun ExercisesScreen(navController: NavController, alreadyAdded: List<String>) {

    val selectedExercises = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(exercises.size) { index ->
                add(exercises[index] in alreadyAdded)
            }
        }
    }
    val numExercises = selectedExercises.count { it }


    Column() {
        Text(
            "Ejercicios",
            modifier = Modifier
                .padding(top = 36.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 24.sp
        )
        exercises.forEachIndexed { index, exercise ->
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Row() {
                    Checkbox(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        checked = selectedExercises[index],
                        onCheckedChange = {
                            selectedExercises[index] = it
                        }
                    )
                    Text(exercise, modifier = Modifier.padding(24.dp))
                }
            }
        }
        Button(modifier = Modifier.fillMaxWidth().padding(24.dp), enabled = numExercises > 0, onClick = {

            val selectedNames = exercises.filterIndexed { index, _ ->
                selectedExercises[index]
            }
            val selectedArg = selectedNames.joinToString(",")

            navController.navigate("createRoutine?day=&selected=$selectedArg")

        }) {
            Text("Agregar $numExercises ejercicios seleccionados")
        }
    }
}
