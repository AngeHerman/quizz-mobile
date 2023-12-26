package fr.uparis.nzaba.projetmobile2023

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import fr.uparis.nzaba.projetmobile2023.model.TelechargerQuestionViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme

class TelechargerQuestionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projetmobile2023Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PageTelQuestion()
                }
            }
        }
    }
}

@Composable
fun PageTelQuestion (model: TelechargerQuestionViewModel = viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        model = model,
        drawerState = drawerState,
        scope = scope,
        customComposable = { model, onMenuIconClick -> ComposableTelQuestion(model, onMenuIconClick) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableTelQuestion(model: AndroidViewModel, onMenuIconClick: () -> Unit) {
    EcranTelQuestion(model = model as TelechargerQuestionViewModel, onMenuIconClick = onMenuIconClick)
}

@Composable
fun EcranTelQuestion(model: TelechargerQuestionViewModel = viewModel(), onMenuIconClick: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val sujets by model.sujetsFlow.collectAsState(listOf())
    var value by model.value
    Scaffold(topBar = { TopBarOther("Telecharger Question", onMenuIconClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingV ->
        Column(verticalArrangement = Arrangement.SpaceBetween){
            Telechargement(
                padding = paddingV,
                value = value,
                onValueChange = model::onValueChange,
                onclick = model::onClick,
                sujets = sujets,
                onSelect = model::onSelectSujet
            )
        }
    }
}

@Composable
fun Telechargement(
    padding: PaddingValues,
    value: String,
    onValueChange: (String) -> Unit,
    onclick: () -> Unit,
    sujets: List<Sujet>,
    onSelect: (Int) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally, // Centre le contenu horizontalement
        //verticalArrangement = Arrangement.Center
    ){
        //Text("Choissisez le sujet où envoyer les questions à telecharger")
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clickable { expanded = true }
                .background(
                    color = Color.Gray, // Couleur de fond de la boîte
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
                //.border(1.dp, Color.Black, shape = MaterialTheme.shapes.medium)
                //.shadow(4.dp, shape = MaterialTheme.shapes.medium)
        ) {
            Text(text = "Sélectionnez un sujet",fontSize = 20.sp)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sujets.forEach { sujet ->
                    DropdownMenuItem(
                        text = {Text(text = sujet.libelleSujet)},
                        onClick = {
                            onSelect(sujet.idSujet)
                            expanded = false
                        }
                    )
                }
            }
        }

        Text("Mettez le lien du fichier",fontSize = 20.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = "Ex : https://g.com/z.xml", fontSize = 17.sp) },
            textStyle = TextStyle(fontSize = 20.sp)
        )
        Button(onClick = onclick) {
            Text("Telecharger")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    Projetmobile2023Theme {
        PageTelQuestion()
    }
}