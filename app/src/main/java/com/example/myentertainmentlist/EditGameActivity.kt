package com.example.myentertainmentlist

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myentertainmentlist.databinding.ActivityEditGameBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.robertlevonyan.components.picker.*

class EditGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditGameBinding
    private lateinit var db: FirebaseFirestore

    private lateinit var gameId: String
    private lateinit var groupId: String
    private lateinit var titleGame: String
    private lateinit var platformGame: String
    private lateinit var genreGame: String
    private lateinit var ratingGame: String
    private lateinit var statusGame: String

    private var gamePhoto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        gameId = intent.extras?.get("gameId").toString().trim()
        groupId = intent.extras?.get("groupId").toString().trim()
        titleGame = intent.extras?.get("titleGame").toString().trim()
        platformGame = intent.extras?.get("platformGame").toString().trim()
        genreGame = intent.extras?.get("genreGame").toString().trim()
        ratingGame = intent.extras?.get("ratingGame").toString().trim()
        statusGame = intent.extras?.get("statusGame").toString().trim()

        binding.gameTitleEditInput.setText(titleGame)
        binding.gamePlatformEditInput.setText(platformGame)
        binding.gameGenreEditInput.setText(genreGame)
        binding.gameRatingEditInput.setText(ratingGame)
        binding.gameStatusEditInput.setText(statusGame)

        binding.butSaveEditGame.setOnClickListener { updateGame(groupId, it) }
        binding.butCancelEditGame.setOnClickListener { finish() }

    }

    /*
        private fun callCamera() {
            pickerDialog {
                setTitle("Select from: ")
                setTitleTextSize(20f)
                setTitleTextBold(true)
                setTitleGravity(Gravity.START)
                setItems(
                    setOf(
                        ItemModel(
                            ItemType.Camera,
                            itemBackgroundColor = Color.rgb(37, 150, 190)
                        ),
                        ItemModel(
                            ItemType.ImageGallery(MimeType.Image.All)
                        ),
                    )
                )

                setListType(PickerDialog.ListType.TYPE_LIST)
            }.setPickerCloseListener { type, uris ->
                when (type) {
                    ItemType.Camera -> setChoosenImg(uris.first())
                    is ItemType.ImageGallery -> {
                        setChoosenImg(uris.first())
                    }
                    else -> {}
                }
            }.show()
        }

        private fun setChoosenImg(uri: Uri) {

            gamePhoto = uri
            binding.gameImageEdit.setImageURI(gamePhoto)
        }
    */
    override fun onBackPressed() {}

    private fun updateGame(groupId: String?, view: View) {
        var gameTitleEdit = binding.gameTitleEditInput.text.toString().trim()
        var gamePlatformEdit = binding.gamePlatformEditInput.text.toString().trim()
        var gameGenreEdit = binding.gameGenreEditInput.text.toString().trim()
        var gameRatingEdit = binding.gameRatingEditInput.text.toString().trim()
        var gameStatusEdit = binding.gameStatusEditInput.text.toString().trim()

        if (gameTitleEdit != "" && gamePlatformEdit != "" && gameGenreEdit != "" && gameRatingEdit != "" && gameStatusEdit != "") {
            db.collection("games").document(gameId)
                .set(
                    hashMapOf(
                        "gameId" to gameId,
                        "gameTitle" to gameTitleEdit,
                        "gamePlatform" to gamePlatformEdit,
                        "gameGenre" to gameGenreEdit,
                        "gameRating" to gameRatingEdit,
                        "gameStatus" to gameStatusEdit
                    )
                ).addOnSuccessListener { finish() }
        } else {
            Snackbar.make(view, "AN ERROR AS OCURRED", Snackbar.LENGTH_LONG).show()
        }


    }
}