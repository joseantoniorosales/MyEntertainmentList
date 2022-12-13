package com.example.myentertainmentlist.room.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true) val idBook: Int,
    @ColumnInfo val titleBook: String,
    @ColumnInfo val genreBook: String,
    @ColumnInfo val author: String,
    @ColumnInfo val photoBook: String,
    @ColumnInfo val statusBook: String
)