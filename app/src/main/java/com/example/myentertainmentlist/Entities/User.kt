package com.example.myentertainmentlist.Entities

data class User( var email: String = "",
                 var username: String = "",
                 var group: String = "",
                 var groupAdmin: Boolean? = null,
                 var onGroup: Boolean? = null,
                 var imgUrl: String = ""
)