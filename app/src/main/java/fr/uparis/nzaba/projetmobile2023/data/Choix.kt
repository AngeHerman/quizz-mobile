package fr.uparis.nzaba.projetmobile2023.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Question::class,parentColumns = ["idQuestion"],
            childColumns = ["idQuestion"],
            onDelete = ForeignKey.CASCADE)
    ]
)
class Choix (
    @PrimaryKey(autoGenerate = true) val idChoix : Int = 0,
    val texte: String,
    val bon: Int,
    val idQuestion: Int
)