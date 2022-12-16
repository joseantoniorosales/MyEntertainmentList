package com.example.myentertainmentlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myentertainmentlist.Entities.Book
import com.example.myentertainmentlist.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val BookFragment = BookFragment()

    private val GameFragment = GameFragment()

    private val TVFragment = TVFragment()

    private val UserFragment = UserFragment()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentNavigationView = findViewById<BottomNavigationView>(R.id.fragmentNavigation)

        changeFragment(GameFragment)

        fragmentNavigationView.selectedItemId = R.id.gameFragment

        fragmentNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.bookFragment -> {
                    changeFragment(BookFragment)
                    item.title
                }

                R.id.gameFragment -> {
                    changeFragment(GameFragment)
                    item.title
                }

                R.id.TVFragment -> {
                    changeFragment(TVFragment)
                    item.title
                }


                R.id.UserFragment -> {
                    changeFragment(UserFragment)
                    item.title
                }
            }
            true
        }
    }


    //override fun onBackPressed() {}

    internal fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameContainer, fragment)
        transaction.commit()
    }
}