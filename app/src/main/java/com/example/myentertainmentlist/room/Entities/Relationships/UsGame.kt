package com.example.myentertainmentlist.room.Entities.Relationships

import androidx.room.Entity

@Entity(primaryKeys = ["uid", "idGame"])
data class UsGame(val uid: String, val idGame: Int)