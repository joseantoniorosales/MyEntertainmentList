package com.example.myentertainmentlist.Signing

// Test credentials: testing@gmail.com / password: 123456789

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myentertainmentlist.MainActivity
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.ActivityLandingBinding
import com.example.myentertainmentlist.room.Database.EntertainmentDatabase
import com.example.myentertainmentlist.room.Entities.TV
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.FirebaseAuthKtxRegistrar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LandingActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityLandingBinding

    //Firebase
    private lateinit var auth: FirebaseAuth

    private val registerResponse =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if (it.resultCode == RESULT_OK) {

                it.data?.extras?.getString("_response")?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }

            } else {

                Snackbar.make(
                    binding.root,
                    "An error happened during the register process",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        //DB Initialization
        dbInitialize()

        //Checks if an user is already logged
        if (auth.currentUser != null) {

            goMain(auth.uid.toString())
        }

        binding.signInBut.setOnClickListener {

            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.signUpBut.setOnClickListener {

            val intent: Intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(view: View) {

        val email: String = binding.emailInput.text.toString().trim()
        val password: String = binding.passwordInput.toString().trim()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {

            if (it.isSuccessful) {

                goMain(auth.uid.toString())

            } else {

                Snackbar.make(view, "Wrong mail/password", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun register(view: View) {

        val intent = Intent(this, RegisterActivity::class.java)

    }

    private fun goMain(uid: String) {

        val db = EntertainmentDatabase.getEntertainmentDatabase(this)

        val users = db?.getEntertainmentDao()?.getUserById(uid)

        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra("_user", users)
        startActivity(intent)
    }


    private fun dbInitialize() {

        val dao = EntertainmentDatabase.getEntertainmentDatabase(this)?.getEntertainmentDao()

        if (dao?.countSeries() == 0) {

            dao.insertSerie(
                TV(
                    0,
                    "Steins;Gate",
                    "Sci-fi",
                    "Crunchyroll",
                    "Anime",
                    "https://pics.filmaffinity.com/Steins_Gate_Serie_de_TV-571124347-large.jpg",
                    "Watched"
                )
            )
        }
    }
}
