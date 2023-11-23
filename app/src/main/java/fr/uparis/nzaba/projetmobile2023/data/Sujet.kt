package fr.uparis.nzaba.projetmobile2023.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity (
    indices = [
        Index( value = [ "libelleSujet"],
            unique = true
        )
    ]
)
data class Sujet (
    @PrimaryKey(autoGenerate = true)
    val idSujet: Int = 0,
    val libelleSujet : String
)