package fr.uparis.nzaba.projetmobile2023.model

import android.annotation.SuppressLint
import android.app.Application
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.uparis.nzaba.projetmobile2023.JeuxApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
class TelechargerQuestionViewModel (private val application: Application) : AndroidViewModel(application) {
    private val dao = (application as JeuxApplication).database.jeuxDao()
    var sujetsFlow = dao.loadSujet()
    var value = mutableStateOf("")
    var selectedSujetId = mutableStateOf(0)
    private var idLoad= 0L
    //val idDownload : MutableState<Long> = mutableStateOf(0L)
    private val adrImg = mutableStateOf("https://images.contentstack.io/v3/assets/bltb6530b271fddd0b1/blt909ba6b565908b76/6259e1e12777714c51b30c0a/Jett_1920x1080.jpg")
    private val downloadManager =
        application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(p0 : Context?, p1 : Intent?){
            val idDownload: Long = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
            if (idDownload == -1L) {
                Log.d("Receiver",
                    "something wrong with idDownload")
                return
            }
            if (idDownload !=idLoad) {
                Log.d("idDownload", "$idDownload is not mine")
                return
            }
            Log.d("Receiver", "download réussi")
            extraireDonnee()
        }
    }
    private val cancelReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            /* trouver les references de téléchargement à supprimer */
            val reference = p1?.getLongArrayExtra(
                DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS
            )
            if (reference != null)
                downloadManager.remove(*reference)
        }
    }

    private val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
    /* enregistrer les BroadcastReceivers */
    private val cancelFilter = IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED)
    init {
        application.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        application.registerReceiver(cancelReceiver, cancelFilter, Context.RECEIVER_EXPORTED)
    }

    fun onValueChange(s : String){
        value.value = s
    }

    fun onClick(){
        ShowToast("Id est ${selectedSujetId.value} et value est ${value.value}")
        if(selectedSujetId.value!= 0 && value.value != "") startDownload()
    }

    fun onSelectSujet(id : Int){
        selectedSujetId.value = id
    }

    private fun ShowToast(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startDownload(){
        ShowToast("Started")
        val uri = Uri.parse(adrImg.value)
        val request = DownloadManager.Request(uri).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        idLoad = downloadManager.enqueue(request)
        Log.d("start", "download demandé $idLoad")
    }

    fun extraireDonnee(){

    }
}