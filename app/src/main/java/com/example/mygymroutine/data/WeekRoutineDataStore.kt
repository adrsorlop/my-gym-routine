package com.example.mygymroutine.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.weekRoutineDataStore by preferencesDataStore("week_routine")

class WeekRoutineRepository(private val context: Context) {

    private val WEEK_ROUTINE_KEY = stringPreferencesKey("week_routine_json")

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    val weekRoutineFlow = context.weekRoutineDataStore.data.map { prefs ->
        val storedJson = prefs[WEEK_ROUTINE_KEY]

        if (storedJson == null) {
            defaultWeekRoutine
        } else {
            json.decodeFromString<List<TrainingDay>>(storedJson)
        }
    }

    suspend fun saveWeekRoutine(list: List<TrainingDay>) {
        val encoded = json.encodeToString(list)

        context.weekRoutineDataStore.edit { prefs ->
            prefs[WEEK_ROUTINE_KEY] = encoded
        }
    }
}