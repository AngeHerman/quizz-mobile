package fr.uparis.nzaba.projetmobile2023

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import fr.uparis.nzaba.projetmobile2023.model.GererSujetViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme

class GererSujetsActivity : ComponentActivity() {
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projetmobile2023Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    EcranGererSujets()
                }
            }
        }
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Gérer la logique de retour ici
                // Par exemple, revenir à l'activité principale (MainActivity)
                val intent = Intent(this@GererSujetsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(
            this, backCallback
        )

        setContent {
            Projetmobile2023Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EcranGererSujets()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranGererSujets(model: GererSujetViewModel = viewModel()) {
    val navController = rememberNavController()
    val sujetField by model.sujetField
    val boutonModif by model.textBoutonModif
    val sujets by model.sujetsFlow.collectAsState(listOf())
    val snackbarHostState = remember { SnackbarHostState() }
    val erreur by model.erreurIns
    val cpt by model.compteurIns

    Scaffold(topBar = { MyTopBar() }, bottomBar = { MyBottomBar(navController,model::versAjout) },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingV ->
        NavHost(navController = navController,startDestination =
        "list",modifier = Modifier.padding(paddingV)) {
            composable("list") { ListeSujet(paddingV,sujets, model::RemplirPourModif, navController) }
            composable("ajout") { ModifSujet(paddingV,sujetField,model::changeSujet,boutonModif,model::addSujet,snackbarHostState,erreur,cpt) }
        }
    }
}

@Composable
fun MyTopBar() =
    TopAppBar(title = { Text("Gérer les sujets", style = MaterialTheme.typography.displayMedium) }, backgroundColor = MaterialTheme.colorScheme.primaryContainer)

@Composable
fun MyBottomBar(
    navController: NavHostController,
    versAjout: () -> Unit
) = BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primaryContainer ) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    BottomNavigationItem(selected = currentRoute == "list", onClick = {
        navController.navigate("list") {
            launchSingleTop = true
            Log.d("Navigation", "Navigating to list")
        }
    }, icon = { Icon(Icons.Default.List, "liste") })
    BottomNavigationItem(selected = currentRoute == "ajout", onClick = {
        /*if (currentRoute != "ajout") {
            versAjout()
            navController.navigate("ajout") { popUpTo("list") }
        }*/
        versAjout()
        navController.navigate("ajout") {
            popUpTo("list")
            Log.d("Navigation", "Navigating to ajout")
        }
    }, icon = { Icon(Icons.Default.Add, "ajouter") })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifSujet(
    padding: PaddingValues,
    value: String,
    onValueChange: (String) -> Unit,
    boutonModif: String,
    addSujet: () -> Unit,
    snackbarHostState:SnackbarHostState,
    erreur: Boolean,
    cpt: Int

){
    LaunchedEffect(cpt) {
        val msg = if (erreur) "erreur d'insertion $cpt" else "insertion réussie $erreur"
        snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Long)
    }
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = "Ex : Le climat", fontSize = 20.sp) },
            textStyle = TextStyle(fontSize = 20.sp)
        )
        Button(onClick = {
            addSujet()
        }) {
            Text("$boutonModif")
        }

    }

}

@Composable
fun ListeSujet(
    padding: PaddingValues,
    sujets: List<Sujet>,
    remplir: (Sujet) -> Unit,
    navController: NavHostController
){
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)) {
        itemsIndexed(sujets) {
                index, item -> UnSujet(index = index, item = item,remplir,navController)
        }
    }
}

@Composable
fun UnSujet(
    index: Int,
    item: Sujet,
    remplir: (Sujet) -> Unit,
    navController: NavHostController
) {
    val col=when{
        index%2==0 -> colorResource(id = R.color.purple_200)
        else -> colorResource(id = R.color.purple_700 )
    }
    Card(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = col)
    ) {
        Row{
            Text("$index")
            Text(item.libelleSujet, fontSize = 18.sp, modifier=Modifier.weight(1f))
            Button(onClick = {
                remplir(item)
                navController.navigate("ajout") { popUpTo("list") }
            }) {
                Text(text = "Modifier")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Projetmobile2023Theme {
        EcranGererSujets()
    }
}