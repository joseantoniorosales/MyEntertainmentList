package com.example.myentertainmentlist.Entities


data class TV(
    var idTV: String = "",
    var groupID: String = "",
    var titleTV: String = "",
    var platformTV: String = "",
    var genreTV: String = "",
    var photoTV: String = "",
    var statusTV: Boolean? = null,
    var ratingTV: String = ""
)