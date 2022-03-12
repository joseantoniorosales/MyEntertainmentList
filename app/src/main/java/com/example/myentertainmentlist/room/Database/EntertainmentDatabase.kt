package com.example.myentertainmentlist.room.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myentertainmentlist.room.Entities.Book
import com.example.myentertainmentlist.room.Entities.Game
import com.example.myentertainmentlist.room.Entities.Relationships.UsBook
import com.example.myentertainmentlist.room.Entities.Relationships.UsGame
import com.example.myentertainmentlist.room.Entities.Relationships.UsTV
import com.example.myentertainmentlist.room.Entities.TV
import com.example.myentertainmentlist.room.Entities.User

@Database(
    entities = [User::class, TV::class, UsTV::class, Game::class, Book::class, UsGame::class, UsBook::class],
    version = 1
)
abstract class EntertainmentDatabase : RoomDatabase() {

    abstract fun getEntertainmentDao(): EntertainmentDAO

    companion object {

        private var instance: EntertainmentDatabase? = null

        @Synchronized
        fun getEntertainmentDatabase(context: Context): EntertainmentDatabase? {

            if (instance === null) {

                instance = Room.databaseBuilder(
                    context,
                    EntertainmentDatabase::class.java,
                    "entertainmentDB"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return instance
        }
    }
}