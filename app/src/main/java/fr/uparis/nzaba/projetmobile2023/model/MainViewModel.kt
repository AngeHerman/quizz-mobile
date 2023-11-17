package fr.uparis.nzaba.projetmobile2023.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.uparis.nzaba.projetmobile2023.JeuxApplication
import fr.uparis.nzaba.projetmobile2023.data.Sujet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel (private val application: Application) : AndroidViewModel(application) {
    private val dao = (application as JeuxApplication).database.jeuxDao()
    var sujetsFlow = dao.loadSujet()

    fun addSujet() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertSujet(Sujet(libelleSujet = "Valorant"))
        }
    }

}