package com.example.myentertainmentlist.room.Database

import androidx.room.*
import com.example.myentertainmentlist.room.Entities.Book
import com.example.myentertainmentlist.room.Entities.Game
import com.example.myentertainmentlist.room.Entities.TV
import com.example.myentertainmentlist.room.Entities.User

@Dao
interface EntertainmentDAO {

// User Queries

    // Count the number of users
    @Query("SELECT COUNT(*) FROM User;")
    fun countUsers(): Int

    // Returns an user searching is ID
    @Query("SELECT * FROM User WHERE uid = :uid;")
    fun getUserById(uid: String): User

    // Insert an user
    @Insert
    fun insertUser(vararg usu: User)

// TV Queries

    // Count the number of TV items
    @Query("SELECT COUNT(*) FROM TV;")
    fun countSeries(): Int

    // Returns every Tv item
    @Query("SELECT * FROM tv;")
    fun getSeries(): MutableList<TV>

    // Returns a TV item searching is ID
    @Query("SELECT * FROM TV WHERE idTV = :idTV;")
    fun getSerieById(idTV: Int): TV

    // Inserts a new TV item
    @Insert
    fun insertSerie(vararg serie: TV)

    // Updates the info of a TV item
    @Update
    fun updateSerie(vararg serie: TV)

    // Deletes a TV item
    @Delete
    fun deleteSerie(vararg serie: TV)

// Game Queries

    // Count the number of game items
    @Query("SELECT COUNT(*) FROM Game;")
    fun countGame(): Int

    // Returns every game item
    @Query("SELECT * FROM Game;")
    fun getGames(): MutableList<Game>

    // Returns a game item searching is ID
    @Query("SELECT * FROM Game WHERE idGame = :idGame;")
    fun getGameById(idGame: Int): Game

    // Inserts a new game item
    @Insert
    fun insertGame(vararg game: Game)

    // Updates the info of a game item
    @Update
    fun updateGame(vararg game: Game)

    // Deletes a game item
    @Delete
    fun deleteGame(vararg game: Game)

// Book Queries

    // Count the number of book items
    @Query("SELECT COUNT(*) FROM Book;")
    fun countBooks(): Int

    // Returns every book item
    @Query("SELECT * FROM Book;")
    fun getBook(): MutableList<Book>

    // Returns a book item searching is ID
    @Query("SELECT * FROM Book WHERE idBook = :idBook;")
    fun getBookById(idBook: Int): Book

    // Inserts a new book item
    @Insert
    fun insertBook(vararg book: Book)

    // Updates the info of a book item
    @Update
    fun updateBook(vararg book: Book)

    // Deletes a book item
    @Delete
    fun deleteBook(vararg book: Book)
}