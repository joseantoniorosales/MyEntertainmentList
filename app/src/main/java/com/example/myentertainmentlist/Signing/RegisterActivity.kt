package com.example.myentertainmentlist.Signing

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.robertlevonyan.components.picker.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var userPhoto: Uri? = null
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signUpBut.setOnClickListener { register(it) }

        // Camera button
        binding.PhotoBut.setOnClickListener { LaunchCamera() }
    }


    private fun LaunchCamera() {
        pickerDialog {
            setTitle("Select from: ")
            setTitleTextSize(20f)
            setTitleTextBold(false)
            setTitleGravity(Gravity.START)
            setItems(
                setOf(
                    ItemModel(
                        ItemType.Camera,
                        backgroundType = ShapeType.TYPE_ROUNDED_SQUARE,
                        itemBackgroundColor = Color.rgb(37, 150, 190)
                    ),
                    ItemModel(
                        ItemType.ImageGallery(MimeType.Image.All),
                        backgroundType = ShapeType.TYPE_ROUNDED_SQUARE,
                        itemBackgroundColor = Color.rgb(37, 150, 190)
                    ),
                )
            )

            setListType(PickerDialog.ListType.TYPE_LIST)
        }.setPickerCloseListener { type, uris ->

            when(type) {
                ItemType.Camera -> setChoosenImg(uris.first())
                is ItemType.ImageGallery -> {
                    setChoosenImg(uris.first())
                }
                else -> {}
            }
        }.show()


    }

    private fun setChoosenImg(uri: Uri) {

        userPhoto = uri
        binding.profileImage.setImageURI(userPhoto)
    }

    private fun uploadImgFirebase(view: View, email: String, password: String, username: String) {

        uploadAlert(getString(R.string.createUser_loading))
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(userPhoto!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                db.collection("users").document(email)
                    .set(
                        hashMapOf(
                            "email" to email,
                            "username" to username,
                            "password" to password,
                            "onGroup" to false,
                            "groupAdmin" to false,
                            "imgUrl" to it.toString()
                        )
                    )

                finish()
            }
        }
    }

    private fun register(view: View) {

        var email = binding.emailInput.text.toString().trim()
        var username = binding.usernameInput.text.toString().trim()
        var pw = binding.passwordInput.text.toString().trim()

        auth = Firebase.auth

        when {
            email != "" && pw != "" && username != "" -> {

                if (pw.length >= 6) {

                    if (userPhoto != null) {

                        auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this) {

                            if (it.isSuccessful) {
                                setResult(RESULT_OK)
                                uploadImgFirebase(view, email, pw, username)

                            } else {
                                Snackbar.make(
                                    view,
                                    getString(R.string.register_fail),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }

                    } else {
                        Snackbar.make(
                            view,
                            getString(R.string.image_select_waring),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                } else {

                    Snackbar.make(view, getString(R.string.pw_error), Snackbar.LENGTH_LONG).show()
                }

            }
            else -> {

                Snackbar.make(view, getString(R.string.fields_unfilled), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun uploadAlert(message: String) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        val dialog = builder.create()
        dialog.show()
    }
}