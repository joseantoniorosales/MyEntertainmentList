package com.example.myentertainmentlist.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainmentlist.EditTvActivity
import com.example.myentertainmentlist.Entities.TV
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.RecyclerTvBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class TVAdapter(private val tvList: ArrayList<TV>, private val context: Context) :
    RecyclerView.Adapter<TVAdapter.ViewHolder>() {

    private lateinit var db: FirebaseFirestore


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_tv, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tv = tvList[position]
        db = FirebaseFirestore.getInstance()
        holder.setIsRecyclable(false)



        holder.title.text = tv.titleTV
        holder.genre.text = tv.genreTV
        holder.author.text = tv.platformTV
        holder.rating.text = tv.ratingTV

        holder.status.isChecked = tv.statusTV == true

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {

            override fun onLongClick(v: View?): Boolean {
                val showMenu = PopupMenu(context, holder.binding.TVTitle)
                showMenu.inflate(R.menu.object_context_menu)
                showMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.editObject -> {
                            val intent = Intent(v!!.context, EditTvActivity::class.java)
                            intent.putExtra("TVId", tv.idTV)
                            intent.putExtra("titleTV", tv.titleTV)
                            intent.putExtra("genreTV", tv.genreTV)
                            intent.putExtra("platformTV", tv.platformTV)
                            intent.putExtra("statusTV", tv.statusTV)
                            intent.putExtra("ratingTV", tv.ratingTV)
                            intent.putExtra("groupID", tv.groupID)
                            intent.putExtra("position", position)
                            context.startActivity(intent)
                        }
                        R.id.deleteObject -> {
                            db.collection("TVs").document(tv.idTV).delete()
                            tvList.remove(tv)
                            notifyItemRemoved(position)
                            notifyDataSetChanged()

                            Snackbar.make(holder.binding.root, "TV deleted", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                    return@setOnMenuItemClickListener true
                }
                showMenu.show()
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return tvList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding = RecyclerTvBinding.bind(view)

        val title: TextView = binding.TVTitle
        val portrait: ImageView = binding.TVPortrait
        val author: TextView = binding.TVPlatform
        val genre: TextView = binding.TVGenre
        val status: CheckBox = binding.TVStatusCheck
        val rating: TextView = binding.TVRatingNum

    }

}