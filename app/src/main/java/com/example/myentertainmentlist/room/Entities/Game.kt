package com.example.myentertainmentlist.room.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Game(
    var idGame: String = "",
    var groupID: String = "",
    var titleGame: String = "",
    var platformGame: String = "",
    var genreGame: String = "",
    var photoGame: String = "",
    var statusGame: Boolean? = null,
    var ratingGame: String = ""
)