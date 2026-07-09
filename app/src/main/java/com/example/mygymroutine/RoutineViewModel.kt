package com.example.mygymroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygymroutine.data.TrainingDay
import com.example.mygymroutine.data.WeekRoutineRepository
import com.example.mygymroutine.data.defaultWeekRoutine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutineViewModel(
    private val repo: WeekRoutineRepository
) : ViewModel() {

    val weekRoutine = repo.weekRoutineFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            defaultWeekRoutine
        )

    fun saveWeekRoutine(list: List<TrainingDay>) {
        viewModelScope.launch {
            repo.saveWeekRoutine(list)
        }
    }

    fun saveTrainingDay(day: TrainingDay) {
        viewModelScope.launch {
            val current = weekRoutine.value

            val updated = current.map {
                if (it.dayName == day.dayName) day else it
            }

            repo.saveWeekRoutine(updated)
        }
    }
}
