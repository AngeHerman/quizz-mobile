package fr.uparis.nzaba.projetmobile2023

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.uparis.nzaba.projetmobile2023.data.Screen
import fr.uparis.nzaba.projetmobile2023.data.TimeConfig
import fr.uparis.nzaba.projetmobile2023.model.ReglerNotifViewModel
import fr.uparis.nzaba.projetmobile2023.ui.theme.Projetmobile2023Theme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ReglerNotifActivity : ComponentActivity() {
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
                    EcranReglage(calculateWindowSizeClass(this))
                }
            }
        }
    }
}
@Composable
fun EcranReglage(size: WindowSizeClass,model: ReglerNotifViewModel = viewModel()){
    when(size.widthSizeClass){
        WindowWidthSizeClass.Compact-> PageReglagePortrait(model)
        else-> PageReglageLandscape(model)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageReglagePortrait(model: ReglerNotifViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        model = model,
        drawerState = drawerState,
        scope = scope,
        customComposable = { model, onMenuIconClick -> CustomComposablePortrait(model, onMenuIconClick) }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageReglageLandscape(model: ReglerNotifViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(
        model = model,
        drawerState = drawerState,
        scope = scope,
        customComposable = { model, onMenuIconClick -> CustomComposableLandscape(model, onMenuIconClick) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomComposablePortrait(model: AndroidViewModel, onMenuIconClick: () -> Unit) {
    EcranReglagePortrait(model = model as ReglerNotifViewModel, onMenuIconClick = onMenuIconClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomComposableLandscape(model: AndroidViewModel, onMenuIconClick: () -> Unit) {
    EcranReglageLandscape(model = model as ReglerNotifViewModel, onMenuIconClick = onMenuIconClick)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranReglagePortrait(model: ReglerNotifViewModel = viewModel(),onMenuIconClick: () -> Unit) {
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
    val snackbarHostState = remember { SnackbarHostState() }

    var timePerAnswer by remember {
        mutableStateOf("")
    }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(topBar = { TopBarOther("Réglages", onMenuIconClick) },
        bottomBar = {
            SettingsBottomBar(
                navigateTo = { navController.navigate("notif")},
                navigateTo2 = { navController.navigate("timeBetweenAnswer") },
                currentRoute = currentRoute
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        NavHost(navController = navController ,
            startDestination = "notif",
            modifier = Modifier.padding(padding)) {
            composable("notif") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height(10.dp))
                    TimePicker(state = clockState, layoutType = TimePickerLayoutType.Vertical)
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
                        Text("Enregistrer", modifier = Modifier.padding(10.dp))
                    }
                }
            }
            composable("timeBetweenAnswer"){
                MenuChangementTemps(timePerAnswer = timePerAnswer, changeTimePerAnswer = {timePerAnswer = it})
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcranReglageLandscape(model: ReglerNotifViewModel = viewModel(),onMenuIconClick: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val prefConfig= runBlocking {model.prefConfig.first()  }
    val clockState = rememberTimePickerState (
        initialHour = prefConfig.h,
        initialMinute = prefConfig.m,
        is24Hour = true
    )

    var timePerAnswer by remember {
        mutableStateOf("")
    }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        Log.d("permissions", if(it)"granted" else "denied")
    }
    Scaffold(
        topBar = { TopBarOther("Réglages", onMenuIconClick) },
        bottomBar = {
            SettingsBottomBar(
                navigateTo = { navController.navigate("notif")},
                navigateTo2 = { navController.navigate("timeBetweenAnswer") },
                currentRoute = currentRoute
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController ,
            startDestination = "notif"){
            composable("notif") {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        //Spacer(modifier = Modifier.width(60.dp))
                        Box(modifier = Modifier.padding(start = 16.dp)) {
                            TimePicker(
                                state = clockState,
                                layoutType = TimePickerLayoutType.Horizontal,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                        Spacer(modifier = Modifier.width(70.dp))
                        Column((Modifier.padding(padding)), horizontalAlignment = Alignment.CenterHorizontally) {
                            //Text("Choisi l'heure de rappel du jeu", fontSize = 30.sp)
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
                                Text(text = "Enregistrer", Modifier.padding(5.dp))
                            }
                        }
                    }
                }
            }
            composable("timeBetweenAnswer"){
                MenuChangementTemps(timePerAnswer = timePerAnswer, changeTimePerAnswer = {timePerAnswer = it})
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavHostController) {
    Column {
        // Ajoutez des éléments de menu pour chaque écran
        DrawerItem("Home") { navController.navigate(Screen.Home.route) }
        DrawerItem("Settings") { navController.navigate(Screen.Settings.route) }
        // Ajoutez d'autres éléments au besoin
    }
}

@Composable
fun DrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    )
}
@Composable
fun MenuChangementTemps(timePerAnswer : String,
                        changeTimePerAnswer : (String) -> Unit,
                        model: ReglerNotifViewModel = viewModel()){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Choissisez un temps de réponse par question :", modifier = Modifier.padding(10.dp))
            TextfieldQuestion(
                text = "",
                addToString = changeTimePerAnswer,
                value = timePerAnswer
            )
            Button(onClick = { model.saveTime(timePerAnswer) }) {
                Text("Enregistrer")
            }
    }
}
@Composable
fun SettingsBottomBar(navigateTo: () -> Unit,
                     navigateTo2: () -> Unit,
                     currentRoute: String?){
    BottomAppBar(modifier = Modifier.height(60.dp)){
        MyBottomBarItem(
            navigateTo = navigateTo,
            routeCheck = currentRoute == "notif",
            icon = {Icon(Icons.Default.DateRange,"")},
            rowScope = this
        )
        MyBottomBarItem(
            navigateTo = navigateTo2,
            routeCheck = currentRoute == "timeBetweenAnswer",
            icon = { Icon(Icons.Default.Build,"")},
            rowScope = this
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Projetmobile2023Theme {
        //EcranReglage()
    }
}