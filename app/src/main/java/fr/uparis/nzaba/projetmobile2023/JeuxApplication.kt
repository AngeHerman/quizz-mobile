package fr.uparis.nzaba.projetmobile2023
import android.app.Application
import fr.uparis.nzaba.projetmobile2023.data.JeuxBD

class JeuxApplication : Application() {
    val database: JeuxBD by lazy {
        JeuxBD.getDataBase(this)
    }
}
