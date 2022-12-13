package com.example.myentertainmentlist

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import com.example.myentertainmentlist.Signing.LoginActivity
import com.example.myentertainmentlist.databinding.ActivityHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        var email = intent.extras?.get("email")

        binding.CreateButton.setOnClickListener { CreateGroup(email, it) }
        binding.JoinButton.setOnClickListener { JoinGroup(email, it) }

        binding.menuButton.setOnClickListener { showMenu(binding.menuButton) }

    }

    override fun onBackPressed() {}

    fun CreateGroup(email: Any?, view: View) {

        val intent = Intent(this, GroupCreatorActivity::class.java)
        db.collection("users").document(email as String).get().addOnSuccessListener {
            if (it.get("onGroup") == false) {
                intent.putExtra("email", "${email}")
                startActivity(intent)

            } else {
                Snackbar.make(
                    view,
                    "User already assigned to a group, please press the join button",
                    Snackbar.LENGTH_LONG
                ).show()

            }
        }
    }

    fun JoinGroup(email: Any?, view: View) {

        val intent = Intent(this, MainActivity::class.java)
        db.collection("users").document(email as String).get().addOnSuccessListener {

            if (it.get("onGroup") == true) {
                GroupIdSaver(it.get("group") as String)
                intent.putExtra("email", "${email}")
                startActivity(intent)

            } else {
                Snackbar.make(
                    view,
                    "This user has not been assigned to a group by an admin",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun GroupIdSaver(groupId: String) {
        val prefs =
            getSharedPreferences(
                getString(R.string.prefs_file),
                Context.MODE_PRIVATE
            ).edit()

        prefs.putString("group", groupId)
        prefs.apply()
    }

    private fun showMenu(view: View) {

        val popUp = PopupMenu(this, view)
        val inflater: MenuInflater = popUp.menuInflater
        val prefs =
            getSharedPreferences(
                getString(R.string.prefs_file),
                Context.MODE_PRIVATE
            )?.edit()
        val intent = Intent(this, LoginActivity::class.java)
        inflater.inflate(R.menu.options_menu, popUp.menu)
        popUp.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.log_out -> {

                    prefs?.clear()
                    prefs?.apply()
                    startActivity(intent)

                    auth.signOut()
                    finish()
                }
            }
            true
        }

        popUp.show()
    }
}