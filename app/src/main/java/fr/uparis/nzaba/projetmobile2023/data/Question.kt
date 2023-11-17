package fr.uparis.nzaba.projetmobile2023.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity (
    indices = [
        Index( value = [ "texte"],
            unique = true
        )
    ],
    foreignKeys = [
        ForeignKey(
            entity = Sujet::class,parentColumns = ["libelleSujet"],
            childColumns = ["libelleSujet"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class Question (
    @PrimaryKey(autoGenerate = true) val idQuestion : Int = 0,
    val qcm: Int,
    val texte: String,
    val rep: String,
    val statut: Int,
    val nextDate: String,
    val libelleSujet : String
)