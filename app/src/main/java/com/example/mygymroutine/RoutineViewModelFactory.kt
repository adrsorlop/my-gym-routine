package com.example.mygymroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygymroutine.data.WeekRoutineRepository

class RoutineViewModelFactory(
    private val repo: WeekRoutineRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutineViewModel::class.java)) {
            return RoutineViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}