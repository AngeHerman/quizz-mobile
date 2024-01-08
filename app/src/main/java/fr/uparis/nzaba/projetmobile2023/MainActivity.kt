package fr.uparis.nzaba.projetmobile2023

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.uparis.nzaba.projetmobile2023.model.MainViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme

const val CHANNEL_ID = "MY_CHANNEL_ID"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createChannel(this)
        setContent {
            Projetmobile2023Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PageMain()
                }
            }
        }
    }
}

@Composable
fun PageMain(model: MainViewModel = viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        model = model,
        drawerState = drawerState,
        scope = scope,
        customComposable = { model, onMenuIconClick -> ComposableMain(model, onMenuIconClick) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableMain(model: AndroidViewModel, onMenuIconClick: () -> Unit) {
    EcranMain(model = model as MainViewModel, onMenuIconClick = onMenuIconClick)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EcranMain(model: MainViewModel = viewModel(),onMenuIconClick: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(topBar = { TopBarMain("Jeu", onMenuIconClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingV ->
        Column(verticalArrangement = Arrangement.SpaceBetween){
            MesBouttons(paddingV,model::addSujet)
        }
    }

}

@Composable
fun MesBouttons(
    padding: PaddingValues,
    addSujet: ()->Unit
){
    val context = LocalContext.current
    Column (Modifier.padding(padding)){
        Text(
            text = "Let the game begin!",
        )
    }

}

fun createChannel(c: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel.
        val name = "MY_CHANNEL"
        val descriptionText = "notification channel for Periodic project"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        val notificationManager =
            c.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}

fun createNotif(c: Context) {

    val intent1 = Intent(c, MainActivity::class.java)
    //val intent2 = Intent(c, ReglerActivity::class.java)
    val pending1 = PendingIntent.getActivity(c, 1, intent1, PendingIntent.FLAG_IMMUTABLE)
    //val pending2 = PendingIntent.getActivity(c, 1, intent2, PendingIntent.FLAG_IMMUTABLE)
    val builder = NotificationCompat.Builder(c, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Il est l'heure de venir jouer")
        .setContentText("Viens approfondir tes connaissances")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true)
        .setContentIntent(pending1).setCategory(Notification.CATEGORY_REMINDER)
    //.addAction(androidx.core.R.drawable.ic_call_answer, "réglages", pending2)
    val myNotif = builder.build()
    val notificationManager =
        c.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(44, myNotif)
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Projetmobile2023Theme {
        PageMain()
    }
}
