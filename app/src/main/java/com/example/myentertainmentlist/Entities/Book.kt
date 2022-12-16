package com.example.myentertainmentlist.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Book(
    var idBook: String = "",
    var groupID: String = "",
    var titleBook: String = "",
    var authorBook: String = "",
    var genreBook: String = "",
    var photoBook: String = "",
    var statusBook: Boolean? = null,
    var ratingBook: String = ""
)