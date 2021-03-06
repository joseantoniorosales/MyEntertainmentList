package com.example.myentertainmentlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myentertainmentlist.databinding.ActivityMainBinding
import com.example.myentertainmentlist.lists.BookListActivity
import com.example.myentertainmentlist.lists.GameListActivity
import com.example.myentertainmentlist.lists.TVListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.watchBut.setOnClickListener {

            val intent: Intent = Intent(this, TVListActivity::class.java)
            startActivity(intent)
        }

        binding.gameBut.setOnClickListener {

            val intent: Intent = Intent(this, GameListActivity::class.java)
            startActivity(intent)
        }

        binding.bookBut.setOnClickListener {

            val intent: Intent = Intent(this, BookListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {}
}