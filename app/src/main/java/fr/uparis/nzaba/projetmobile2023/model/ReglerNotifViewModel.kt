package fr.uparis.nzaba.projetmobile2023.model

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import fr.uparis.nzaba.projetmobile2023.RappelWorker
import fr.uparis.nzaba.projetmobile2023.data.TimeConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ReglerNotifViewModel (private val application: Application) : AndroidViewModel(application){
    private val KEY_H = intPreferencesKey("hour")
    private val KEY_M = intPreferencesKey("minute")
    private val KEY_TIME = intPreferencesKey("time")
    private val mystore = application.dataStore
    val prefConfig = mystore.data.map {
        TimeConfig(
            it[KEY_H] ?: 8, it[KEY_M] ?: 0,
        )
    }
    val defaultConfig = mystore.data.map {
        it[KEY_TIME] ?: 30
    }

    fun saveTime(timePerAnswer : String){
        val intValue: Int? = timePerAnswer.toIntOrNull()
        if(intValue == null){
            ShowToast("Taper un entier s'il vous plaît")
        }else{
            viewModelScope.launch {
                mystore.edit {
                    it[KEY_TIME] = intValue
                }
            }
            ShowToast("Enregistré")
        }
    }

    private fun ShowToast(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
        }
    }

    fun save(config: TimeConfig) {
        viewModelScope.launch {
            mystore.edit {
                it[KEY_H] = config.h
                it[KEY_M] = config.m
            }
        }
        ShowToast("Enregistré")
    }

    fun schedule(config: TimeConfig) {
        val wm = WorkManager.getInstance(application)
        wm.cancelAllWork()
        wm.enqueue(perRequest(config.h, config.m))
    }

    private fun perRequest(h: Int, m: Int): PeriodicWorkRequest {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, h)
            set(Calendar.MINUTE, m)
        }
        if (target.before(now))
            target.add(Calendar.DAY_OF_YEAR, 1)
        val delta=target.timeInMillis - now.timeInMillis
        val request = PeriodicWorkRequestBuilder<RappelWorker>(
            1,
            TimeUnit.DAYS
        ).setInitialDelay(delta, TimeUnit.MILLISECONDS).build()
        Log.d("Periodic", "request: $request")
        return request
    }
}