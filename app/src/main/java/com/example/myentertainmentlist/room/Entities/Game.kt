package com.example.myentertainmentlist.room.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val idGame: Int,
    @ColumnInfo val titleGame: String,
    @ColumnInfo val genreGame: String,
    @ColumnInfo val Developer: String,
    @ColumnInfo val platformGame: String,
    @ColumnInfo val photoGame: String,
    @ColumnInfo val statusGame: String
)