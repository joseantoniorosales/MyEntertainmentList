package com.example.myentertainmentlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.myentertainmentlist.databinding.ActivityTvListBinding
import com.example.myentertainmentlist.databinding.ActivityTvviewBinding

class TVViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTvviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTvviewBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val id: Int = intent.extras?.getInt("_id") as Int

        //Search for the specified item

    }
}