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
import com.example.myentertainmentlist.EditBookActivity
import com.example.myentertainmentlist.Entities.Book
import com.example.myentertainmentlist.R
import com.example.myentertainmentlist.databinding.RecyclerBookBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class BookAdapter(private val bookList: ArrayList<Book>, private val context: Context) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private lateinit var db: FirebaseFirestore


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_book, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        db = FirebaseFirestore.getInstance()
        holder.setIsRecyclable(false)



        holder.title.text = book.titleBook
        holder.genre.text = book.genreBook
        holder.author.text = book.authorBook
        holder.rating.text = book.ratingBook

        holder.status.isChecked = book.statusBook == true

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {

            override fun onLongClick(v: View?): Boolean {
                val showMenu = PopupMenu(context, holder.binding.bookTitle)
                showMenu.inflate(R.menu.object_context_menu)
                showMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.editObject -> {
                            val intent = Intent(v!!.context, EditBookActivity::class.java)
                            intent.putExtra("bookId", book.idBook)
                            intent.putExtra("titlebook", book.titleBook)
                            intent.putExtra("genrebook", book.genreBook)
                            intent.putExtra("authorbook", book.authorBook)
                            intent.putExtra("statusbook", book.statusBook)
                            intent.putExtra("ratingbook", book.ratingBook)
                            intent.putExtra("groupID", book.groupID)
                            intent.putExtra("position", position)
                            context.startActivity(intent)
                        }
                        R.id.deleteObject -> {
                            db.collection("books").document(book.idBook).delete()
                            bookList.remove(book)
                            notifyItemRemoved(position)
                            notifyDataSetChanged()

                            Snackbar.make(holder.binding.root, "book deleted", Snackbar.LENGTH_LONG)
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
        return bookList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding = RecyclerBookBinding.bind(view)

        val title: TextView = binding.bookTitle
        val portrait: ImageView = binding.bookPortrait
        val author: TextView = binding.bookAuthor
        val genre: TextView = binding.bookGenre
        val status: CheckBox = binding.bookStatusCheck
        val rating: TextView = binding.bookRatingNum
    }

}


