package com.example.myentertainmentlist.room.Entities.Relationships

import androidx.room.Entity

@Entity(primaryKeys = ["uid", "idBook"])
data class UsBook (val uid: String, val idBook: Int)