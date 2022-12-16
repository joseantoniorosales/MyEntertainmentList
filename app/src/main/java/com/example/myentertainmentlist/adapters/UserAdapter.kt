package com.example.myentertainmentlist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.RecyclerUsersBinding
import com.example.myentertainmentlist.Entities.User

class UserAdapter(private val userList: ArrayList<User>, private val context: Context) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_users, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = userList[position]

        holder.usersUsername.text = user.username
        holder.usersEmail.text = user.email

        Glide.with(context).load(user.imgUrl).skipMemoryCache(true).into(holder.usersPortrait)

        holder.usersAdminCheck.isChecked = user.groupAdmin == true

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding = RecyclerUsersBinding.bind(view)

        val usersUsername: TextView = binding.usersNameText
        val usersEmail: TextView = binding.usersEmailText
        val usersPortrait: ImageView = binding.usersPortrait
        val usersAdminCheck: CheckBox = binding.adminCheck
    }

}