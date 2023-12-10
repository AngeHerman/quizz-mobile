package fr.uparis.nzaba.projetmobile2023.data

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    // Ajoutez d'autres écrans au besoin
}