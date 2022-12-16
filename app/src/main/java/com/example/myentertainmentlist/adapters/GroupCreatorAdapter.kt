package com.example.myentertainmentlist.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.RecyclerUserGroupBinding
import com.example.myentertainmentlist.Entities.User

class GroupCreatorAdapter(
    private val userList: ArrayList<User>,
    private val context: Context,
    private val CurrentUser: String

) : RecyclerView.Adapter<GroupCreatorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_user_group, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupCreatorAdapter.ViewHolder, position: Int) {
        val user = userList[position]

        holder.username.text = user.username
        holder.email.text = user.email
        holder.onGroupToggle.setOnClickListener {

            if (holder.onGroupToggle.isChecked) {
                user.onGroup = true
                holder.onGroupToggle.setBackgroundColor(Color.GREEN)
            } else {
                holder.onGroupToggle.setBackgroundColor(Color.RED)
                user.onGroup = false
            }
        }

        if (CurrentUser == user.email) holder.onGroupToggle.visibility = View.GONE

        Glide.with(context).load(user.imgUrl).skipMemoryCache(true).into(holder.imageView)
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding = RecyclerUserGroupBinding.bind(view)

        val imageView = binding.userPortrait
        val username: TextView = binding.usernameText
        val email: TextView = binding.emailText
        val onGroupToggle: ToggleButton = binding.onGroupToggle


    }
}

