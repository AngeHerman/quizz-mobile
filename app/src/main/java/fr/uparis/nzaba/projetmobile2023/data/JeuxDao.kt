package fr.uparis.nzaba.projetmobile2023.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JeuxDao {
    @Insert(onConflict = ABORT)
    suspend fun insertSujet(sujet: Sujet)

    @Insert(onConflict= IGNORE)
    suspend fun insertQuestion(question: Question)

    @Insert(onConflict= IGNORE)
    suspend fun insertChoix(choix: Choix)

    @Query("SELECT * FROM Sujet")
    fun loadSujet(): Flow<List<Sujet>>

    @Query("SELECT * FROM Question WHERE libelleSujet = :libelleSujet")
    fun loadQuestion(libelleSujet: String): Flow<List<Question>>

    @Query("SELECT * FROM Choix WHERE idQuestion = :idQuestion")
    fun loadChoixReponse(idQuestion: Int): Flow<List<Choix>>
}