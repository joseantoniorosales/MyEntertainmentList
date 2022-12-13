package com.example.myentertainmentlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myentertainmentlist.Signing.LoginActivity
import com.example.myentertainmentlist.adapters.GameAdapter
import com.example.myentertainmentlist.databinding.GameFragmentBinding
import com.example.myentertainmentlist.room.Entities.Game
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import java.util.*

class GameFragment : Fragment() {

    // Firebase variables
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    //list of games
    private lateinit var gameList: ArrayList<Game>

    // Adapter
    private lateinit var gameShowAdapter: GameAdapter

    // Fragments variables
    private var bindingFrag: GameFragmentBinding? = null
    private val binding get() = bindingFrag!!

    // Game info variables
    private lateinit var gameTitle: String
    private lateinit var gamePlatform: String
    private lateinit var gameGenre: String
    private lateinit var gameStatus: String
    private lateinit var gameRating: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFrag = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()

        //Shared preferences to get the email
        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        val email = prefs?.getString("email", null)
        val groupId = prefs?.getString("group", "")

        auth = Firebase.auth

        gameList = arrayListOf()

        gameShowAdapter = GameAdapter(gameList, activity as Context)

        binding.GameRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = gameShowAdapter
        }

        binding.GameMenuBut.setOnClickListener {

            ShowMenu(binding.GameMenuBut)
        }

        binding.GameAddBut.setOnClickListener {

            db.collection("users").document(email as String).get().addOnSuccessListener {

                if (it.get("groupAdmin") == true) {

                    val gameDialog: View = layoutInflater.inflate(
                        R.layout.create_game_dialog_layout,
                        binding.root,
                        false
                    )

                    //gameDialog.removeDialog()

                    MaterialAlertDialogBuilder(context as Context)
                        .setView(gameDialog)
                        .setTitle("Create new game")
                        .setNegativeButton("Cancel") { dialog, which -> }
                        .setPositiveButton("Save") { dialog, which ->
                            //gameTitle = gameDialog.new.text().toString().trim()

                            createGame(groupId)
                        }
                        .show()

                } else {

                    //binding.GameAddBut.hide()
                }
            }
        }
        addDb()
    }

    private fun addDb() {

        db = FirebaseFirestore.getInstance()

        val prefs =
            activity?.getSharedPreferences(R.string.prefs_file.toString(), Context.MODE_PRIVATE)

        val groupID = prefs?.getString("group", "")

        db.collection("games").whereEqualTo("groupID", groupID)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {

                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {

                    if (error != null) {

                        Log.i("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {
                            var game = dc.document.toObject(Game::class.java)
                            game.idGame = dc.document.id

                            if (gameList.indexOf(game) == -1) {
                                gameList.add(game)
                                gameShowAdapter.notifyItemInserted(gameList.size)
                            }
                        }

                        //IF GAME EDITED
                        if (dc.type == DocumentChange.Type.MODIFIED) {

                            var newGame = dc.document.toObject(Game::class.java)
                            newGame.idGame = dc.document.id

                            var position: Int = 0
                            for (game in gameList) {

                                if (game.idGame == dc.document.id) {
                                    position = gameList.indexOf(game)
                                    gameList[position] = newGame
                                    gameShowAdapter.notifyItemChanged(position)
                                    gameShowAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
            })
    }

    private fun ShowMenu(view: View) {

        val menuShow = PopupMenu(activity as Context, view)
        val inflater: MenuInflater = menuShow.menuInflater
        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                ?.edit()
        inflater.inflate(R.menu.options_menu, menuShow.menu)
        menuShow.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.log_out -> {
                    prefs?.clear()
                    prefs?.apply()
                    goLoginPage()
                    auth.signOut()
                    activity?.finish()
                }
            }

            true
        }

        menuShow.show()
    }

    fun goLoginPage() {

        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun View.removeDialog() {

        this ?: return
        val parentView = parent as? ViewGroup ?: return
        parentView.removeView(this)
    }

    private fun createGame(groupId: String?) {

        var gameId = UUID.randomUUID().toString()

        if (gameTitle != "") {
            db.collection("games").document(gameId).set(
                hashMapOf(
                    "gameId" to gameId,
                    "title" to gameTitle,
                    "platform" to gamePlatform,
                    "genre" to gameGenre,
                    "status" to gameStatus,
                    "rating" to gameRating,
                    "group" to groupId
                )
            ).addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Game Successfully created",
                    Toast.LENGTH_LONG
                ).show()
            }

        } else {
            Toast.makeText(context, "Game failed to be created", Toast.LENGTH_LONG).show()

        }
    }
}