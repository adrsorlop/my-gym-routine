package com.example.mygymroutine

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygymroutine.data.Exercise
import com.example.mygymroutine.ui.theme.exercises
import com.example.mygymroutine.ui.theme.weekRoutine
import com.example.mygymroutine.data.Set

@Composable
fun CreateRoutineScreen(navController: NavController) {

    val routineName = remember { mutableStateOf("") }
    val selectedDays = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(7) { add(false) }
        }
    }

    val exercisesState = remember { mutableStateListOf<Exercise>() }


    Column(modifier = Modifier.padding(24.dp)) {
        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {

            OutlinedButton(onClick = {
                navController.navigate("days")

            }, modifier = Modifier.weight(1f)) {
                Text("Cancelar")
            }

            Button(onClick = {

                val createdExercises = exercisesState.toList()

                val updatedWeek = weekRoutine.mapIndexed { index, day ->

                    if (selectedDays[index]) {
                        day.copy(
                            routineName = routineName.value,
                            exercises = createdExercises
                        )
                    } else {
                        day
                    }
                }

                weekRoutine = updatedWeek

                navController.navigate("days")

            }, modifier = Modifier.weight(1f)) {
                Text("Listo")
            }

        }
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(top = 24.dp)
        )
        OutlinedTextField(
            value = routineName.value,
            onValueChange = { routineName.value = it },
            label = { Text("Nombre de la rutina") },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(top = 24.dp)
        )
        DaySelector(
            selectedDays = selectedDays,
            onDayChange = { index, value ->
                selectedDays[index] = value
            }
        )
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        ExerciseSelector(
            exercises = exercisesState,
            onUpdate = { newList ->
                exercisesState.clear()
                exercisesState.addAll(newList)
            }
        )
    }
}

@Composable
fun DaySelector(
    selectedDays: List<Boolean>,
    onDayChange: (Int, Boolean) -> Unit
) {
    val maxPerColumn = 4

    Row(modifier = Modifier.padding(24.dp)) {
        val columns = (selectedDays.size + maxPerColumn - 1) / maxPerColumn

        repeat(columns) { colIndex ->
            Column(modifier = Modifier.padding(end = 16.dp)) {
                for (rowIndex in 0 until maxPerColumn) {
                    val realIndex = colIndex * maxPerColumn + rowIndex
                    if (realIndex < selectedDays.size) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedDays[realIndex],
                                onCheckedChange = { onDayChange(realIndex, it) }
                            )
                            Text(weekRoutine[realIndex].dayName)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseSelector(
    exercises: List<Exercise>,
    onUpdate: (List<Exercise>) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(exercises) { index, exercise ->
                ExerciseRow(
                    exercise = exercise,
                    onUpdate = { updated ->
                        val newList = exercises.toMutableList()
                        newList[index] = updated
                        onUpdate(newList)
                    },
                    onDelete = {
                        val newList = exercises.toMutableList()
                        newList.removeAt(index)
                        onUpdate(newList)
                    }
                )
            }
        }

        Button(onClick = {
            onUpdate(exercises + Exercise(name = "", sets = emptyList(), coolDown = 0))
        }) {
            Text("Agregar ejercicio")
        }
    }
}


@Composable
fun ExerciseRow(
    exercise: Exercise,
    onUpdate: (Exercise) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }

                Text(exercise.name, fontSize = 18.sp)

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                exercises.forEach { name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onUpdate(exercise.copy(name = name))
                            expanded = false
                        }
                    )
                }
            }

            exercise.sets.forEachIndexed { index, set ->
                SetRow(
                    set = set,
                    onUpdate = { updated ->
                        val newSets = exercise.sets.toMutableList()
                        newSets[index] = updated
                        onUpdate(exercise.copy(sets = newSets))
                    },
                    onDelete = {
                        val newSets = exercise.sets.toMutableList()
                        newSets.removeAt(index)
                        onUpdate(exercise.copy(sets = newSets))
                    }
                )
            }

            IconButton(onClick = {
                onUpdate(exercise.copy(sets = exercise.sets + Set(reps = 0, weight = 0)))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add set")
            }
        }
    }
}

@Composable
fun SetRow(
    set: Set,
    onUpdate: (Set) -> Unit,
    onDelete: () -> Unit
) {
    Row(modifier = Modifier.padding(8.dp)) {

        OutlinedTextField(
            value = set.reps.toString(),
            onValueChange = { onUpdate(set.copy(reps = it.toIntOrNull() ?: 0)) },
            label = { Text("Reps") },
            modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
            value = set.weight.toString(),
            onValueChange = { onUpdate(set.copy(weight = (it.toFloatOrNull() ?: 0f).toInt())) },
            label = { Text("Peso") },
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
        }
    }
}

