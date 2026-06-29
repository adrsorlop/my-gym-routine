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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mygymroutine.data.Exercise
import com.example.mygymroutine.data.Set
import com.example.mygymroutine.data.TrainingDay
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private val weekRoutine = listOf(
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
                    )
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
                    )
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

    private val exercises = listOf<String>("Sentadillas", "Press de banca", "Curl de bíceps")

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
                var detailText = ""

                if (day.exercises.isNotEmpty()) {
                    if (day.exercises.size == 1) {
                        detailText = "${day.exercises.size} ejercicio"
                    } else {
                        detailText = "${day.exercises.size} ejercicios"
                    }
                } else {
                    detailText = "No hay ejercicios programados"
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
                            if (day.exercises.isNotEmpty()) {

                            }
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
                                        Card {
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
                        onClick = { /*TODO*/ },
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

        Column {
            LazyColumn {
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
                Row() {
                    Column() {
                        Button(onClick = { expanded = !expanded }) {
                            Text(
                                if (selectedExercise.isEmpty())
                                    "Seleccionar ejercicio"
                                else
                                    selectedExercise
                            )
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
                    }
                    Column() {
                        IconButton(onClick = { onDelete(row) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
                Row() {

                }
            }
        }
    }

}


