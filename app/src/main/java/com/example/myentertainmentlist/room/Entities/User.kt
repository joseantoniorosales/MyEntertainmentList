package com.example.myentertainmentlist.room.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class User( var email: String = "",
                 var username: String = "",
                 var group: String = "",
                 var groupAdmin: Boolean? = null,
                 var onGroup: Boolean? = null,
                 var imgUrl: String = ""
)