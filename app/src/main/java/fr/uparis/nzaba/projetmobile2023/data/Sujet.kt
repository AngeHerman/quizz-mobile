package fr.uparis.nzaba.projetmobile2023.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class Sujet (
    @PrimaryKey() val libelleSujet : String
)