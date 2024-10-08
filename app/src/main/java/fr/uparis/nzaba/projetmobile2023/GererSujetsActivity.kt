package fr.uparis.nzaba.projetmobile2023

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Checkbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import fr.uparis.nzaba.projetmobile2023.model.GererSujetViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme

class GererSujetsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projetmobile2023Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    EcranSujet(calculateWindowSizeClass(this))
                }
            }
        }
    }
}


@Composable
fun EcranSujet(size: WindowSizeClass, model: GererSujetViewModel= viewModel()){
    when(size.widthSizeClass){
        WindowWidthSizeClass.Compact-> PageSujetPortrait(model)
        else-> PageSujetLandscape(model)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSujetPortrait(model: GererSujetViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        model = model,
        drawerState = drawerState,
        scope = scope,
        customComposable = { model, onMenuIconClick -> ComposableSujetPortrait(model, onMenuIconClick) }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSujetLandscape(model: GererSujetViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        model = model,
        drawerState = drawerState,
        scope = scope,
        customComposable = { model, onMenuIconClick -> ComposableSujetLandscape(model, onMenuIconClick) }
    )
}

//J'en avais besoin pour passser un composable en paramètre au notification drawer
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableSujetPortrait(model: AndroidViewModel, onMenuIconClick: () -> Unit) {
    EcranGererSujetsPortrait(model = model as GererSujetViewModel, onMenuIconClick = onMenuIconClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableSujetLandscape(model: AndroidViewModel, onMenuIconClick: () -> Unit) {
    EcranGererSujetsLandscape(model = model as GererSujetViewModel, onMenuIconClick = onMenuIconClick)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranGererSujetsPortrait(model: GererSujetViewModel = viewModel(),onMenuIconClick: () -> Unit) {
    val navController = rememberNavController()
    val sujetField by model.sujetField
    val boutonModif by model.textBoutonModif
    val sujets by model.sujetsFlow.collectAsState(listOf())
    val snackbarHostState = remember { SnackbarHostState() }
    val erreur by model.erreurIns
    val cpt by model.compteurIns
    var dialogSupp by remember { mutableStateOf(model.DialogSupp) }
    var sujetsSelectionnes = model.sujetsSelectionnes

    Scaffold(topBar = { TopBarOther("Gérer les sujets", onMenuIconClick) },
        bottomBar = { MyBottomBar(navController,model::versAjout) },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingV ->
        NavHost(navController = navController,startDestination =
        "list",modifier = Modifier.padding(paddingV)) {
            composable("list") {
                    ListeSujet(paddingV,sujets, model::RemplirPourModif, navController,sujetsSelectionnes,model::updateSujetsSelectionnes,dialogSupp,model::deleteSujetsSelectionnes,true)
            }
            composable("ajout") {
                ModifSujet(
                    paddingV,
                    sujetField,
                    model::changeSujet,
                    boutonModif,
                    model::addSujet,
                    snackbarHostState,
                    erreur,
                    cpt
                )
            }
        }
    }
}

private @Composable
fun EcranGererSujetsLandscape(model: GererSujetViewModel = viewModel(),onMenuIconClick: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val sujetField by model.sujetField
    val boutonModif by model.textBoutonModif
    val sujets by model.sujetsFlow.collectAsState(listOf())
    val erreur by model.erreurIns
    val cpt by model.compteurIns
    var dialogSupp by remember { mutableStateOf(model.DialogSupp) }
    var sujetsSelectionnes = model.sujetsSelectionnes

    Scaffold(topBar = { TopBarOther("Gérer les sujets", onMenuIconClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingV ->
        Row(horizontalArrangement = Arrangement.SpaceBetween){
            ModifSujet(
                paddingV,
                sujetField,
                model::changeSujet,
                boutonModif,
                model::addSujet,
                snackbarHostState,
                erreur,
                cpt
            )
            ListeSujet(paddingV,sujets, model::RemplirPourModif, navController,sujetsSelectionnes,model::updateSujetsSelectionnes,dialogSupp,model::deleteSujetsSelectionnes,false)

        }
    }
}


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
    /*LaunchedEffect(cpt) {
        val msg = if (erreur) "erreur d'insertion $cpt" else "insertion réussie $erreur"
        snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Long)
    }*/
    Column(Modifier.padding(padding)) {
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
    navController: NavHostController,
    sujetsSelectionnes: List<Sujet>,
    updateSujetSelectionne: (List<Sujet>) -> Unit,
    dialogSupp: MutableState<Boolean>,
    deleteSujetsSelects: () -> Unit,
    portrait: Boolean
){
    Column (Modifier.padding(padding)) {
        Button(
            onClick = {
                if (sujetsSelectionnes.isNotEmpty()) {
                    // Afficher la boîte de dialogue de confirmation avant la suppression
                    dialogSupp.value = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Supprimer sélectionnés")
        }
        LazyColumn(
            Modifier

                .fillMaxWidth()
                .fillMaxHeight(1f)
                .padding(padding)) {
            itemsIndexed(sujets) {
                    index, item -> UnSujet(index = index, item = item,remplir,navController,sujetsSelectionnes,updateSujetSelectionne,portrait)
            }
        }
    }

    if (dialogSupp.value) {
        DialogSupp(
            annuler = { dialogSupp.value = false },
            supprimer = {
                // Supprimer les sujets sélectionnés
                deleteSujetsSelects()
                dialogSupp.value = false
            }
        )
    }

}

@Composable
fun UnSujet(
    index: Int,
    item: Sujet,
    remplir: (Sujet) -> Unit,
    navController: NavHostController,
    sujetSelectionne: List<Sujet>,
    updateSujetSelectionne: (List<Sujet>) -> Unit,
    portrait: Boolean
) {

    val col=when{
        index%2==0 -> MaterialTheme.colorScheme.secondary
        else ->  MaterialTheme.colorScheme.tertiary
    }
    Card(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        colors = CardDefaults.cardColors(containerColor = col)
    ) {
        Row{
            Text(item.libelleSujet, fontSize = 18.sp, modifier=Modifier.weight(1f).padding(5.dp))
            Button(onClick = {
                remplir(item)
                if (portrait){
                    navController.navigate("ajout") { popUpTo("list") }
                }
            }) {
                Text(text = "Modifier")
            }
            Checkbox(
                checked = sujetSelectionne.contains(item),
                onCheckedChange = {
                    val selectedSet = sujetSelectionne.toMutableList()
                    if (it) {
                        selectedSet.add(item)
                    } else {
                        selectedSet.remove(item)
                    }
                    updateSujetSelectionne(selectedSet)
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun DialogSupp(annuler: ()-> Unit, supprimer: () -> Unit){
    AlertDialog(
        text = {Text("Es-tu sûr de supprimer?")},
        onDismissRequest =  annuler,
        confirmButton = {Button(onClick = supprimer){Text("Oui")}},
        dismissButton = {Button(onClick = annuler){Text("Non")}}
    )
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Projetmobile2023Theme {
        //EcranGererSujets()
    }
}