package com.example.myentertainmentlist.room.Entities.Relationships

import androidx.room.Entity

@Entity(primaryKeys = ["uid", "idTV"])
data class UsTV(val uid: String, val idTV: Int)