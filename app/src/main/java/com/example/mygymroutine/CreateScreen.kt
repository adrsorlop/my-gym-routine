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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mygymroutine.data.Exercise
import com.example.mygymroutine.data.Set
import com.example.mygymroutine.data.TrainingDay
import com.example.mygymroutine.data.WeekRoutineRepository
import com.example.mygymroutine.viewmodel.RoutineViewModel
import com.example.mygymroutine.viewmodel.RoutineViewModelFactory

@Composable
fun CreateRoutineScreen(
    navController: NavController,
    trainingDay: TrainingDay?,
    selectedExercises: List<String>
) {

    val context = LocalContext.current

    val vm = viewModel<RoutineViewModel>(
        factory = RoutineViewModelFactory(
            WeekRoutineRepository(context)
        )
    )

    val weekRoutine by vm.weekRoutine.collectAsState()

    val routineName = remember { mutableStateOf("") }
    val selectedDays = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(7) { add(false) }
        }
    }
    val exercisesState = remember { mutableStateListOf<Exercise>() }

    LaunchedEffect(trainingDay) {
        if (trainingDay != null) {
            routineName.value = trainingDay.routineName

            weekRoutine.forEachIndexed { index, day ->
                if (day.dayName == trainingDay.dayName) {
                    selectedDays[index] = true
                }
            }
        }

        if (trainingDay != null) {
            trainingDay.exercises.forEach {
                exercisesState.add(it)
            }
        } else {
            selectedExercises.forEachIndexed { index, string ->
                exercisesState.add(
                    Exercise(
                        string, sets = listOf(
                            Set(
                                reps = 0,
                                weight = 0
                            )
                        )
                    )
                )
            }
        }
    }

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

            Button(
                onClick = {

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

                    vm.saveWeekRoutine(updatedWeek)

                    navController.navigate("days")

                }, modifier = Modifier.weight(1f),
                enabled = if (selectedDays.contains(true))  routineName.value != "" && exercisesState.isNotEmpty() else false
            ) {
                Text("Listo")
            }

        }
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(top = 24.dp, bottom = 18.dp)
        )
        OutlinedTextField(
            value = routineName.value,
            onValueChange = { routineName.value = it },
            label = { Text("Nombre de la rutina") },
            modifier = Modifier.fillMaxWidth()
        )
        if (trainingDay == null) {
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                "Selecciona los días: ",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 24.dp)
            )
            DaySelector(
                selectedDays = selectedDays,
                onDayChange = { index, value ->
                    selectedDays[index] = value
                }
            )
        }
        HorizontalDivider(
            thickness = 2.dp
        )
        Text("Ejercicios: ", fontSize = 18.sp, modifier = Modifier.padding(top = 24.dp))
        Exercises(
            exercises = exercisesState,
            onUpdate = { newList ->
                exercisesState.clear()
                exercisesState.addAll(newList)
            },
            navController = navController
        )
    }
}

@Composable
fun DaySelector(
    selectedDays: List<Boolean>,
    onDayChange: (Int, Boolean) -> Unit
) {

    val context = LocalContext.current

    val vm = viewModel<RoutineViewModel>(
        factory = RoutineViewModelFactory(
            WeekRoutineRepository(context)
        )
    )

    val weekRoutine by vm.weekRoutine.collectAsState()

    MultiChoiceSegmentedButtonRow(modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)) {
        repeat(7) { index ->
            SegmentedButton(
                onCheckedChange = { onDayChange(index, !selectedDays[index]) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = selectedDays.size),
                checked = selectedDays[index],
                icon = { SegmentedButtonDefaults.Icon(selectedDays[index]) },
                label = { Text(weekRoutine[index].dayName[0].toString()) }
            )
        }
    }
}

@Composable
fun Exercises(
    exercises: List<Exercise>,
    navController: NavController,
    onUpdate: (List<Exercise>) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            if (exercises.isNotEmpty()) {
                itemsIndexed(exercises) { index, exercise ->
                    ExerciseCard(
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
            } else {
                item {
                    Text(
                        "No hay ejercicios programados",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
        }

        Button(onClick = {
            navController.navigate(
                "createRoutine/exercises?added=${
                    exercises.map { it.name }.joinToString(",")
                }"
            )
        }) {
            if (exercises.size == 0) {
                Text("Agregar ejercicios")
            } else {
                Text("Editar ejercicios")
            }
        }
    }
}


@Composable
fun ExerciseCard(
    exercise: Exercise,
    onUpdate: (Exercise) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(modifier = Modifier.fillMaxWidth()) {

                Text(
                    exercise.name,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
            exercise.sets.forEachIndexed { index, set ->
                ExerciseSet(
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
                    }, exercise.sets.size
                )
            }

            if (exercise.name != "") {
                IconButton(onClick = {
                    onUpdate(exercise.copy(sets = exercise.sets + Set(reps = 0, weight = 0)))
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add set")
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = exercise.coolDown.toString(),
                    onValueChange = { onUpdate(exercise.copy(coolDown = it.toIntOrNull() ?: 0)) },
                    label = { Text("Descanso (segundos)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }


        }
    }
}

@Composable
fun ExerciseSet(
    set: Set,
    onUpdate: (Set) -> Unit,
    onDelete: () -> Unit,
    setsNumber: Int
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
        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.CenterVertically),
            enabled = setsNumber > 1
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                tint = if (setsNumber > 1) Color.Red else Color.Gray
            )
        }
    }
}