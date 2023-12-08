package fr.uparis.nzaba.projetmobile2023

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.uparis.nzaba.projetmobile2023.model.MainViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        Text(
            text = "Hello World!",
        )
        Button(onClick = {
            model.addSujet()
        }) {
            Text(text = "Ajout")
        }
        Button(onClick = {
            val iii = Intent(context,GererSujetsActivity::class.java)
            context.startActivity (iii)
        }) {
            Text("Gerer Sujet")
        }

        Button(onClick = {
            val iii = Intent(context,GererQuestionsActivity::class.java)
            context.startActivity (iii)
        }) {
            Text("Gerer les questions")
        }
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)) {
            items(sujets) {
                Row {
                    Text(text = it.libelleSujet ?: "", fontSize = 30.sp)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Projetmobile2023Theme {
        Greeting()
    }
}