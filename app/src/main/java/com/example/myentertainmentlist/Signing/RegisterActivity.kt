package com.example.myentertainmentlist.Signing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}