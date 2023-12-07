package fr.uparis.nzaba.projetmobile2023

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.uparis.nzaba.projetmobile2023.data.TimeConfig
import fr.uparis.nzaba.projetmobile2023.model.ReglerNotifViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ReglerNotifActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projetmobile2023Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EcranReglage()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranReglage(model: ReglerNotifViewModel = viewModel()) {
    val prefConfig= runBlocking {model.prefConfig.first()  }
    val clockState = rememberTimePickerState (
        initialHour = prefConfig.h,
        initialMinute = prefConfig.m,
        is24Hour = true
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        Log.d("permissions", if(it)"granted" else "denied")
    }
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Choisi l'heure de rappel du jeu", fontSize = 30.sp)
        Row {
            TimePicker(state = clockState, layoutType = TimePickerLayoutType.Vertical)
        }
        FloatingActionButton(onClick = {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            val newConfig = TimeConfig(
                clockState.hour,
                clockState.minute
            )
            Log.d("Periodic", "config=$newConfig")
            model.save(newConfig)
            model.schedule(newConfig)
        }) {
            Text("Enregistrer")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Projetmobile2023Theme {
        EcranReglage()
    }
}