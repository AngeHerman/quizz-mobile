package fr.uparis.nzaba.projetmobile2023

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
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
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(model: MainViewModel = viewModel()) {
    val sujets by model.sujetsFlow.collectAsState(listOf())
    val context = LocalContext.current
    Column {
        Button(onClick = {
            val iii = Intent(context, MemorisationActivity::class.java)
            context.startActivity(iii)
        }) {
            Text("Jouer")
        }
        Button(onClick = {
            model.addSujet()
        }) {
            Text(text = "Ajout")
        }
        Button(onClick = {
            val iii = Intent(context, GererSujetsActivity::class.java)
            context.startActivity(iii)
        }) {
            Text("Gerer Sujet")
        }

        Button(onClick = {
            val iii = Intent(context, GererQuestionsActivity::class.java)
            context.startActivity(iii)
        }) {
            Text("Gerer les questions")
        }
        Button(onClick = {
            val iii = Intent(context, ReglerNotifActivity::class.java)
            context.startActivity(iii)
        }) {
            Text("Gerer Rappel")
        }
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            items(sujets) {
                Row {
                    Text(text = it.libelleSujet ?: "", fontSize = 30.sp)
                }
            }
        }


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
            Greeting()
        }
    }
