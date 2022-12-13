package com.example.myentertainmentlist.Signing

// Test credentials: testing@gmail.com / password: 123456789

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.example.myentertainmentlist.HomeActivity
import com.example.myentertainmentlist.MainActivity
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityLoginBinding

    //Firebase auth
    private lateinit var auth: FirebaseAuth

    private val registerResponse =
        registerForActivityResult(StartActivityForResult()) {

            if (it.resultCode == RESULT_OK) {

                Snackbar.make(binding.root, "Register completed", Snackbar.LENGTH_LONG).show()

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

        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Firebase auth instance
        auth = Firebase.auth

        binding.signInBut.setOnClickListener { login(it) }

        binding.signUpBut.setOnClickListener { register() }

        storedUser()
    }

    fun login(view: View) {

        val intentLogin = Intent(this, HomeActivity::class.java)

        var email: String = binding.emailInput.text.toString().trim()
        var password: String = binding.passwordInput.text.toString().trim()

        if (email != "" && password != "") {

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {

                if (it.isSuccessful) {
                    saveUserLogin(email)
                    intentLogin.putExtra("email", "${email}")
                    startActivity(intentLogin)

                } else {

                    Snackbar.make(view, "Wrong mail/password", Snackbar.LENGTH_LONG).show()
                }
            }
        } else {

            Snackbar.make(view, "Fill the textfields", Snackbar.LENGTH_LONG).show()
        }
    }

    fun register() {

        val intent = Intent(this, RegisterActivity::class.java)
        registerResponse.launch(intent)
    }

    override fun onBackPressed() {}

    fun saveUserLogin(email: String) {

        val preferences =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        preferences.putString("email", email)
        preferences.apply()
    }

    private fun storedUser() {

        val preferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        val email = preferences.getString("email", null)

        if (email != null) {

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("email", "${email}")
            startActivity(intent)

        }
    }
}