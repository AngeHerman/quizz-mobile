package fr.uparis.nzaba.projetmobile2023.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface JeuxDao {
    @Insert(onConflict = IGNORE)
    suspend fun insertSujet(sujet: Sujet) : Long

    @Update(onConflict = ABORT)
    suspend fun updateSujet(sujet: Sujet) : Int

    @Delete
    suspend fun deleteSujet(sujet: Sujet)

    /*@Query("UPDATE Sujet SET libelleSujet = :newLibelleSujet WHERE libelleSujet = :oldLibelleSujet")
    suspend fun updateSujet(newLibelleSujet: String, oldLibelleSujet: String) : Int*/

    @Upsert
    suspend fun insertQuestion(question: Question) : Long

    @Insert(onConflict= IGNORE)
    suspend fun insertChoix(choix: Choix) : Long

    @Update(onConflict = ABORT)
    suspend fun updateChoix(choix : Choix)

    @Query("SELECT * FROM Sujet")
    fun loadSujet(): Flow<List<Sujet>>

    @Query("SELECT * FROM Sujet WHERE idSujet = :idSujet")
    fun selectSubject(idSujet: Int): Sujet

    @Query("SELECT * FROM Question WHERE idSujet = :idSujet")
    fun loadQuestion(idSujet: Int): Flow<List<Question>>

    @Delete
    suspend fun deleteSelectedQuestion(question: Question)

    @Query("SELECT * FROM Choix WHERE idQuestion = :idQuestion")
    fun loadChoixReponse(idQuestion: Int): Flow<List<Choix>>
}