package com.example.myentertainmentlist.adapters

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myentertainmentlist.EditGameActivity
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.RecyclerGameBinding
import com.example.myentertainmentlist.Entities.Game
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class GameAdapter(
    private val gameList: ArrayList<Game>,
    private val context: Context
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private lateinit var db: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.recycler_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameAdapter.ViewHolder, position: Int) {
        val game = gameList[position]
        db = FirebaseFirestore.getInstance()
        holder.setIsRecyclable(false)


        holder.title.text = game.titleGame
        holder.genre.text = game.genreGame
        holder.platform.text = game.platformGame
        holder.rating.text = game.ratingGame

        holder.status.isChecked = game.statusGame == true



        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {

            override fun onLongClick(v: View?): Boolean {
                val showMenu = PopupMenu(context, holder.binding.gameTitle)
                showMenu.inflate(R.menu.object_context_menu)
                showMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.editObject -> {
                            val intent = Intent(v!!.context, EditGameActivity::class.java)
                            intent.putExtra("gameId", game.idGame)
                            intent.putExtra("titleGame", game.titleGame)
                            intent.putExtra("genreGame", game.genreGame)
                            intent.putExtra("platformGame", game.platformGame)
                            intent.putExtra("statusGame", game.statusGame)
                            intent.putExtra("ratingGame", game.ratingGame)
                            intent.putExtra("groupID", game.groupID)
                            intent.putExtra("position", position)
                            context.startActivity(intent)
                        }
                        R.id.deleteObject -> {
                            db.collection("games").document(game.idGame).delete()
                            gameList.remove(game)
                            notifyItemRemoved(position)
                            notifyDataSetChanged()

                            Snackbar.make(holder.binding.root, "Game deleted", Snackbar.LENGTH_LONG)
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
        return gameList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = RecyclerGameBinding.bind(view)

        val title: TextView = binding.gameTitle
        val portrait: ImageView = binding.gamePortrait
        val platform: TextView = binding.gamePlatform
        val genre: TextView = binding.gameGenre
        val status: CheckBox = binding.gameStatusCheck
        val rating: TextView = binding.gameRatingNum
    }
}