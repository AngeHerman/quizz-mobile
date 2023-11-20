package fr.uparis.nzaba.projetmobile2023.model

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.uparis.nzaba.projetmobile2023.JeuxApplication
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GererSujetViewModel (private val application: Application) : AndroidViewModel(application) {

    private val dao = (application as JeuxApplication).database.jeuxDao()
    var sujetsFlow = dao.loadSujet()
    var sujetField = mutableStateOf("")
    var textBoutonModif = mutableStateOf("Ajouter")


    fun versAjout(){
        textBoutonModif.value = "Ajouter"
    }

    fun versModif(){
        textBoutonModif.value = "Modifier"
    }

    fun changeSujet(value: String){
        sujetField.value = value
    }

    fun RemplirPourModif(sujet: Sujet) {
        sujetField.value = sujet.libelleSujet
        versModif()
    }

    fun addSujet() {
        if(textBoutonModif.value == "Ajouter"){
            viewModelScope.launch(Dispatchers.IO) {
                dao.insertSujet(Sujet(libelleSujet = sujetField.value))
            }
        }else{

        }

    }
}