package com.example.mygymroutine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygymroutine.ui.theme.exercises
import com.example.mygymroutine.ui.theme.weekRoutine

@Composable
fun CreateRoutineScreen(navController: NavController) {
    Column(modifier = Modifier.padding(24.dp)) {
        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { navController.navigate("days") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                ) {
                    Text(text = "Salir")
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = { navController.navigate("days") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                ) {
                    Text(text = "Listo")
                }
            }
        }
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(top = 24.dp)
        )
        OutlinedTextField(
            state = rememberTextFieldState(),
            label = { Text("Nombre de la rutina") }
        )
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(top = 24.dp)
        )
        DaySelector()
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        ExerciseSelector()
    }
}

@Composable
fun DaySelector() {
    val maxPerColumn = 4
    val states = remember {
        mutableStateListOf(*Array(weekRoutine.size) { false })
    }

    Row(modifier = Modifier.padding(24.dp)) {

        val columns = (weekRoutine.size + maxPerColumn - 1) / maxPerColumn

        repeat(columns) { colIndex ->

            Column(modifier = Modifier.padding(end = 16.dp)) {

                for (rowIndex in 0 until maxPerColumn) {

                    val realIndex = colIndex * maxPerColumn + rowIndex

                    if (realIndex < weekRoutine.size) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = states[realIndex],
                                onCheckedChange = { states[realIndex] = it }
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
fun ExerciseSelector() {

    var rows by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(rows) { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    ExerciseRow(
                        row = row,
                        onDelete = { toDelete ->
                            rows = rows.filter { it != toDelete }
                        }
                    )
                }
            }
        }

        Button(onClick = {
            rows = rows + "Nueva fila ${rows.size + 1}"
        }) {
            Text("Agregar ejercicio")
        }
    }
}

@Composable
fun ExerciseRow(
    row: String,
    onDelete: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Select exercise",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                            .weight(1f),
                        tint = Color.Black
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = selectedExercise,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onDelete(row) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                exercises.forEach { exercise ->
                    DropdownMenuItem(
                        text = { Text(exercise) },
                        onClick = {
                            selectedExercise = exercise
                            expanded = false
                        }
                    )
                }
            }

            if (selectedExercise.isNotEmpty()) {

                var sets by remember { mutableStateOf(List(1) { it + 1 }) }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp)
                )

                FlowRow(
                    maxItemsInEachRow = 1,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    sets.forEachIndexed { index, _ ->
                        Card(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(modifier = Modifier.padding(8.dp)) {

                                OutlinedTextField(
                                    state = rememberTextFieldState(),
                                    label = { Text("Reps") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )

                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove set",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.CenterVertically)
                                        .weight(1f),
                                    tint = Color.Black
                                )

                                OutlinedTextField(
                                    state = rememberTextFieldState(),
                                    label = { Text("Peso") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )

                                IconButton(
                                    onClick = {
                                        sets = sets.toMutableList().also { it.removeAt(index) }
                                    },
                                    enabled = sets.size > 1,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove set",
                                        modifier = Modifier.size(16.dp),
                                        tint = if (sets.size > 1) Color.Red else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    IconButton(onClick = {
                        sets = sets + (sets.size + 1)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }
        }
    }
}