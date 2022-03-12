package com.example.myentertainmentlist.room.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey val uid: String,
    @ColumnInfo val username: String,
    @ColumnInfo val photo: String? = null
) : Serializable