package com.example.myentertainmentlist.Entities

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