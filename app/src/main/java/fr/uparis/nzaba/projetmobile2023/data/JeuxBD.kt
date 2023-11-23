package fr.uparis.nzaba.projetmobile2023.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Sujet::class, Question::class, Choix::class], version = 2)
abstract class JeuxBD : RoomDatabase() {
    abstract fun jeuxDao(): JeuxDao

    companion object {
        @Volatile
        private var instance: JeuxBD? = null

        fun getDataBase(c: Context): JeuxBD {
            if (instance != null) return instance!!
            val db = Room.databaseBuilder(c.applicationContext, JeuxBD::class.java, "jeuxbd")
                .fallbackToDestructiveMigration().build()
            instance = db
            return instance!!
        }
    }
}
