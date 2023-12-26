package fr.uparis.nzaba.projetmobile2023

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import fr.uparis.nzaba.projetmobile2023.data.DrawItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TopBarMain(
    s: String,
    onMenuIconClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(s)
        },
        navigationIcon = {
            IconButton(onClick = onMenuIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Drawer"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TopBarOther(
    s: String,
    onMenuIconClick: () -> Unit,
   // context : Context
) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(s)
        },
        navigationIcon = {
            IconButton(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Retour derriere"
                )
            }
        },
        actions = {
            // Ajoutez le bouton du Drawer ici
            IconButton(onClick = onMenuIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Drawer"
                )
            }
        }
    )
}

@Composable
fun NavigationDrawer(
    model: AndroidViewModel,
    drawerState : DrawerState,
    scope : CoroutineScope,
    customComposable: @Composable (model: AndroidViewModel,onMenuIconClick: () -> Unit) -> Unit, // Paramètre pour le composable personnalisé

){
    val context = LocalContext.current
    //val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    //val scope = rememberCoroutineScope()
    val items = listOf(
        DrawItem(icon = Icons.Default.Home, label = "Home", activityClass = MainActivity::class.java),
        DrawItem(icon = Icons.Default.Create, label = "Gérer sujets", activityClass = GererSujetsActivity::class.java),
        DrawItem(icon = Icons.Default.Create, label = "Gérer Questions", activityClass = GererQuestionsActivity::class.java),
        DrawItem(icon = Icons.Default.Settings, label = "Planifier Rappel", activityClass = ReglerNotifActivity::class.java),
        DrawItem(icon = Icons.Default.Build, label = "Telecharger Question", activityClass = TelechargerQuestionActivity::class.java)
    )
    var selectedItem by remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Menu", style = MaterialTheme.typography.headlineLarge)
                }
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.label) },
                        selected = item == selectedItem,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = item
                            val intent = Intent(context, item.activityClass)
                            context.startActivity(intent)
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label)},
                        //badge = { Text(text = item.secondaryLabel)},
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            customComposable(model,onMenuIconClick = {scope.launch { drawerState.open() }})
        }
    )
}

