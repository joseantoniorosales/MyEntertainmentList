package com.example.myentertainmentlist.Signing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signUpBut.setOnClickListener { register(it) }
    }

    private fun register(view: View) {

        val email = binding.emailInput.text.toString().trim()
        val pw = binding.passwordInput.text.toString().trim()

        if ((email != "") && (pw != "")) {

            auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this) {

                var msg = ""

                if (it.isSuccessful) {

                    msg = "Registration process successfully completed"

                } else {

                    msg = "Registration process failed"
                }

                val intent = Intent()

                intent.putExtra("_response", msg)
                setResult(RESULT_OK, intent)
                finish()
            }

        } else {

            Snackbar.make(binding.root, "Email and password cannot be empty", Snackbar.LENGTH_LONG).show()
        }
    }
}