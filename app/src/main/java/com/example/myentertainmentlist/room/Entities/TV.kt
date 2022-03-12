package com.example.myentertainmentlist.room.Entities

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TV(
    @PrimaryKey(autoGenerate = true) val idTV: Int,
    @ColumnInfo val titleTV: String,
    @ColumnInfo val genreTV: String,
    @ColumnInfo val platformTV: String,
    @ColumnInfo val typeTV: String,
    @ColumnInfo val photoTV: String,
    @ColumnInfo val statusTV: String
)