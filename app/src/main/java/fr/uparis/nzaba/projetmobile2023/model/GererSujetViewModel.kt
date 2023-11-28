package fr.uparis.nzaba.projetmobile2023.model

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.uparis.nzaba.projetmobile2023.JeuxApplication
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GererSujetViewModel (private val application: Application) : AndroidViewModel(application) {

    var DialogSupp = mutableStateOf(false)
    private val dao = (application as JeuxApplication).database.jeuxDao()
    var sujetsFlow = dao.loadSujet()
    var sujetField = mutableStateOf("")
    var textBoutonModif = mutableStateOf("Ajouter")
    var erreurIns= mutableStateOf(false)
    val compteurIns=mutableStateOf(0)
    val sujetAModifier = mutableStateOf(0)
    var sujetsSelectionnes = mutableStateListOf<Sujet>()


    fun updateSujetsSelectionnes(newList: List<Sujet>) {
        sujetsSelectionnes.clear()
        sujetsSelectionnes.addAll(newList)
    }


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
        sujetAModifier.value = sujet.idSujet
        versModif()
    }

    fun addSujet() {
        if(textBoutonModif.value == "Ajouter"){
            viewModelScope.launch(Dispatchers.IO) {
                val res=async{dao.insertSujet(Sujet(libelleSujet = sujetField.value))}
                erreurIns.value= (res.await()== -1L)
                if(erreurIns.value) ShowToast("Erreur d'insertion")
                else ShowToast("Insertion réussi")
                //compteurIns.value ++;
            }
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val sujetToUpdate = Sujet(idSujet = sujetAModifier.value, libelleSujet = sujetField.value)
                    val rowsAffected = dao.updateSujet(sujetToUpdate)

                    // Now you can check the value of rowsAffected to determine if the update was successful
                    if (rowsAffected > 0) {
                        ShowToast("Modification réussi")
                    } else {
                        ShowToast("Modification non réussi")
                    }
                } catch (e: SQLiteConstraintException) {
                    // Handle the unique constraint violation
                    ShowToast("Erreur: Libellé déjà existant")
                }
            }
        }

    }
    private fun ShowToast(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteSujetsSelectionnes() {
        val deletedSubjects = mutableListOf<String>()
        val failedDeletions = mutableListOf<String>()

        for (sujet in sujetsSelectionnes.toList()) {
            try {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.deleteSujet(sujet)
                }
                deletedSubjects.add(sujet.libelleSujet)
                sujetsSelectionnes.remove(sujet)
            } catch (e: SQLiteConstraintException) {
                // Handle foreign key constraint violation
                failedDeletions.add(sujet.libelleSujet)
            }
        }

        val deletedSubjectsMessage = if (deletedSubjects.isNotEmpty()) {
            "Supprimés : ${deletedSubjects.joinToString(", ")}"
        } else {
            "Aucun sujet sélectionné"
        }

        val failedDeletionsMessage = if (failedDeletions.isNotEmpty()) {
            "Échecs de suppression (clé étrangère) : ${failedDeletions.joinToString(", ")}"
        } else {
            ""
        }

        val toastMessage = "$deletedSubjectsMessage\n$failedDeletionsMessage"

        ShowToast(toastMessage)
    }


}