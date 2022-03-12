package com.example.myentertainmentlist.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.room.Entities.TV
import com.example.myentertainmentlist.databinding.RecyclerTvBinding

class TVAdapter(var data: MutableList<TV>) : RecyclerView.Adapter<TVAdapter.TvContainer>() {

    var singleClick: (TV) -> Unit = {}
        set(value) {

            field = value
        }

    var longClick: (MenuItem, TV) -> Boolean = { menuItem: MenuItem, serie: TV -> false }
        set(value) {

            field = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvContainer {

        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerTvBinding.inflate(inflater, parent, false)

        return TvContainer(binding)
    }

    override fun onBindViewHolder(holder: TvContainer, position: Int) {

        holder.bindTv(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class TvContainer(val binding: RecyclerTvBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bindTv(serie: TV) {

            // Title show
            binding.titleText.text = serie.titleTV

            // Genre show
            binding.genreText.text = serie.genreTV

            // Platform show
            binding.platformText.text = serie.platformTV

            // Type show
            binding.typeText.text = serie.typeTV

            // Status show
            binding.statusText.text = serie.statusTV

            //Status show
            when (serie.statusTV) {

                "Watched" -> {

                    binding.statusText.setTextColor(Color.GREEN)

                }

                "Plan to Watch" -> {

                    binding.statusText.setTextColor(Color.YELLOW)
                }

                "Dropped" -> {

                    binding.statusText.setTextColor(Color.RED)
                }
            }

            //Photo show

            Glide.with(binding.root).load(serie.photoTV).into(binding.TVimage)

            // Single click listener
            binding.root.setOnClickListener {

                singleClick(serie)

            }

            // Long click listener (contextual menu)
            binding.root.setOnLongClickListener {

                val pop = PopupMenu(binding.root.context, binding.titleText)
                pop.inflate(R.menu.item_menu)
                pop.setOnMenuItemClickListener { longClick(it, serie) }
                pop.show()

                true
            }
        }
    }
}